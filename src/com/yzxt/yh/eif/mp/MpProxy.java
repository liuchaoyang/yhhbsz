package com.yzxt.yh.eif.mp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;

import com.google.gson.JsonObject;
import com.yzxt.fw.server.BeanFactoryHelper;
import com.yzxt.fw.server.Config;
import com.yzxt.fw.util.GsonUtil;
import com.yzxt.yh.constant.ConstMap;
import com.yzxt.yh.module.cli.bean.FenceWarn;
import com.yzxt.yh.module.cli.service.FenceWarnService;
import com.yzxt.yh.util.StringUtil;

/**
 * 地图服务处理
 */
public class MpProxy extends Thread
{
    private static int DATE_LEN = 2048;
    private static Charset cs = Charset.forName("GBK");
    private Logger logger = Logger.getLogger(MpProxy.class);
    private DatagramSocket socket;
    // 服务器运行状态，1：未启用，2运行中，3，已停止
    private int state = 1;
    private byte[] inBuff;
    private byte[] outBuff;
    private InetAddress mpSvcAddr;
    private int mpSvcPort = 0;
    private String webBaseUrl;
    private String lbsEncoding;
    private FenceWarnService fenceWarnService;

    private static MpProxy instance;

    /**
     * 获取实例引用
     * 
     * @return
     */
    public static MpProxy getInstance()
    {
        return instance;
    }

    /**
     * 消息调度
     * 
     * @throws Exception
     */
    public void begin() throws Exception
    {
        Config config = Config.getInstance();
        String host = config.getString("map.service.host");
        mpSvcPort = config.getInteger("map.service.port", 0);
        int myPort = config.getInteger("map.myPort", 0);
        webBaseUrl = config.getString("map.web.base.url");
        lbsEncoding = config.getString("map.web.lbs.encoding", "UTF-8");
        if (StringUtil.isEmpty(host) || mpSvcPort <= 0 || myPort <= 0 || StringUtil.isEmpty(webBaseUrl))
        {
            logger.debug("地图配置无效，忽略此服务。");
            return;
        }
        BeanFactory beanFactory = BeanFactoryHelper.getBeanfactory();
        if (beanFactory == null)
        {
            logger.error("BeanFactory不存在，忽略地图服务。");
            return;
        }
        fenceWarnService = (FenceWarnService) beanFactory.getBean("fenceWarnService");
        mpSvcAddr = InetAddress.getByName(host);
        socket = new DatagramSocket(myPort);
        inBuff = new byte[DATE_LEN];
        outBuff = new byte[DATE_LEN];
        instance = this;
        state = 2;
        super.setDaemon(true);
        super.start();
    }

    /**
     * 发送给地图服务器消息
     * 
     * @param cmd
     * @return
     */
    int send(String content)
    {
        if (state != 2)
        {
            logger.error("Mp server do not running." + content);
            return MpConst.OPER_STATE_SERVER_NOT_RUNNING;
        }
        if (StringUtil.isNotEmpty(content))
        {
            try
            {
                outBuff = content.getBytes(cs);
                DatagramPacket outPacket = new DatagramPacket(outBuff, 0, outBuff.length, mpSvcAddr, mpSvcPort);
                socket.send(outPacket);
            }
            catch (IOException e)
            {
                logger.error("Send to mp error," + content, e);
                return MpConst.OPER_STATE_SEND_ERROR;
            }
        }
        return MpConst.OPER_STATE_SUCCESS;
    }

    /**
     * 处理消息接收
     */
    @Override
    public void run()
    {
        state = 2;
        while (state == 2)
        {
            try
            {
                DatagramPacket inPacket = new DatagramPacket(inBuff, DATE_LEN);
                socket.receive(inPacket);
                inBuff = inPacket.getData();
                // *IMEI,Alarm,checkTime, Longitude,Latitude,Location,出区域;
                String str = new String(inBuff, inPacket.getOffset(), inPacket.getLength(), cs);
                if (StringUtil.isNotEmpty(str))
                {
                    List<String> msgs = decodeMsg(str);
                    // 围栏告警
                    if ("Alarm".equals(msgs.get(1)))
                    {
                        // From map like *u018af48b4d509260310150b29097f90291,prefix 4 chars is type
                        String prefix = msgs.get(0).substring(1, 4);
                        if (ConstMap.CUST_CODE_PREFIX.equals(prefix))
                        {
                            FenceWarn fenceWarn = new FenceWarn();
                            fenceWarn.setCustId(msgs.get(0).substring(4));
                            fenceWarn.setLongitude(Double.valueOf(msgs.get(3)));
                            fenceWarn.setLatitude(Double.valueOf(msgs.get(4)));
                            fenceWarn.setWarnTime(new Timestamp(MpConst.daf.parse(msgs.get(2)).getTime()));
                            fenceWarn.setAddress(msgs.get(5));
                            fenceWarnService.save(fenceWarn);
                        }
                    }
                }
            }
            catch (Exception e)
            {
                logger.error("Receive from mp error", e);
            }
        }
        inBuff = null;
        outBuff = null;
    }

    /**
     * 关闭服务
     */
    public void shutdown()
    {
        state = 3;
        if (socket != null && !socket.isClosed())
        {
            socket.close();
        }
    }

    /**
     * 地图返回信息解码 告警数据格式： *IMEI,Alarm,checkTime, Longitude,Latitude,Location,出区域;
     * 
     * @param msg
     * @return
     */
    public List<String> decodeMsg(String msg)
    {
        List<String> list = new ArrayList<String>();
        // 分隔符位置
        int lastSplitPos = -1;
        for (int i = 0; i < msg.length(); i++)
        {
            if (msg.charAt(i) == ',')
            {
                list.add(msg.substring(lastSplitPos + 1, i));
                lastSplitPos = i;
            }
        }
        list.add(msg.substring(lastSplitPos + 1));
        return list;
    }

    /**
     * 参数一 key API KEY String 测试接口无需提供key，每个IP每日限访问20次 参数二 mnc 移动网络号 String
     * 0：移动，1：联通，电信对应sid，为十进制 参数三 lac 位置区码 String 电信对应nid，十进制 参数四 ci 小区识别 String
     * 电信对应bid，十进制 参数五 mcc 国家代码 String 中国对应460
     */
    public LacConvertResult lbsToGps(String mcc, String mnc, String lac, String ci)
    {
        LacConvertResult lacConvertResult = new LacConvertResult();
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        try
        {
            URL getUrl = new URL(new StringBuilder(webBaseUrl).append("LBSInterface/getCell.ashx?mcc=").append(mcc)
                    .append("&mnc=").append(mnc).append("&lac=").append(lac).append("&ci=").append(ci).toString());
            conn = (HttpURLConnection) getUrl.openConnection();
            // 设置连接超时为10秒
            conn.setConnectTimeout(10000);
            // 设定请求方式
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                String encoding = lbsEncoding;
                // Content-Type: text/html;charset=gb2312
                String hct = conn.getHeaderField("Content-Type");
                if (hct != null)
                {
                    int csIdx = hct.indexOf("charset=");
                    if (csIdx > -1)
                    {
                        String hcs = hct.substring(csIdx + 8).trim();
                        if (hcs.length() > 0)
                        {
                            encoding = hcs;
                        }
                    }
                }
                // 获取数据
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));
                StringBuffer sb = new StringBuffer();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line);
                }
                JsonObject jsonObj = GsonUtil.parse(sb.toString()).getAsJsonObject();
                // OK：成功;INVALID:参数错误;KEY_ERROR:API key错误;NO_RESULT:无查询结果;QUOTA_EXCEED:超出查询限额
                String statusStr = GsonUtil.toString(jsonObj.get("status"));
                if ("OK".equals(statusStr))
                {
                    JsonObject ctt = jsonObj.get("content").getAsJsonObject();
                    lacConvertResult.setStatus(LacConvertResult.STATUS_SUCCESS);
                    lacConvertResult.setLongitude(GsonUtil.toDouble(ctt.get("Lng")));
                    lacConvertResult.setLatitude(GsonUtil.toDouble(ctt.get("Lat")));
                    lacConvertResult.setAddress(GsonUtil.toString(ctt.get("address")));
                }
                else
                {
                    lacConvertResult.setStatus(LacConvertResult.STATUS_ERROR);
                    lacConvertResult.setErrorMsg("Error status:" + statusStr + ",error msg:"
                            + GsonUtil.toString(jsonObj.get("errorMsg")));
                }
            }
            else
            {
                lacConvertResult.setStatus(LacConvertResult.STATUS_ERROR);
                lacConvertResult.setErrorMsg("Get LBS error code: " + conn.getResponseCode());
            }
        }
        catch (Exception e)
        {
            lacConvertResult.setStatus(LacConvertResult.STATUS_ERROR);
            lacConvertResult.setErrorMsg(e);
        } finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (Exception e)
                {
                }
            }
            if (conn != null)
            {
                try
                {
                    conn.disconnect();
                }
                catch (Exception e)
                {
                }
            }
        }
        return lacConvertResult;
    }

}
