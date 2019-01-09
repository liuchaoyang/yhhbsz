package com.yzxt.yh.eif.surezen;

import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class SurezenHandleProxy extends IoHandlerAdapter
{
    private SurezenHandle surezenHandle;

    public SurezenHandle getSurezenHandle()
    {
        return surezenHandle;
    }

    public void setSurezenHandle(SurezenHandle surezenHandle)
    {
        this.surezenHandle = surezenHandle;
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception
    {
        // 删除缓存的会话
        String key = (String) session.getAttribute(SurezenConst.SESSION_KEY_IMEI);
        if (key != null && key.length() > 0)
        {
            SessionMap.getInstance().remove(key);
        }
    }

    @Override
    public void messageReceived(IoSession session, Object object) throws Exception
    {
        String msg = object.toString().trim();
        if (msg != null && msg.length() > 0)
        {
            String rStr = processMsg(session, object.toString());
            if (rStr != null && rStr.length() > 0)
            {
                session.write(rStr);
            }
        }
    }

    public String processMsg(IoSession ioSession, String msg)
    {
        String[] reqInfos = splitReq(msg);
        // 合法的指令至少有6项
        if (reqInfos.length < 6 || !reqInfos[3].startsWith("T"))
        {
            return null;
        }
        String operCode = reqInfos[3].substring(1);
        String resStr = null;
        if (Integer.valueOf(operCode) <= 70)
        {
            resStr = msg;
            if ("1".equals(operCode))
            // 腕表登录
            {
                resStr = surezenHandle.login(ioSession, reqInfos, msg, operCode);
            }
            else if ("2".equals(operCode))
            // 连接心跳
            {
                resStr = surezenHandle.beat(ioSession, reqInfos, msg, operCode);
            }
            else if ("11".equals(operCode))
            // 脉搏
            {
                resStr = surezenHandle.pulse(ioSession, reqInfos, msg, operCode);
            }
            else if ("12".equals(operCode))
            // 温度
            {
                resStr = surezenHandle.temperature(ioSession, reqInfos, msg, operCode);
            }
            else if ("13".equals(operCode))
            // 血压
            {
                resStr = surezenHandle.pressure(ioSession, reqInfos, msg, operCode);
            }
            else if ("14".equals(operCode))
            // 血糖
            {
                resStr = surezenHandle.sugar(ioSession, reqInfos, msg, operCode);
            }
            else if ("31".equals(operCode))
            // 定位LBS
            {
                resStr = surezenHandle.lbs(ioSession, reqInfos, msg, operCode);
            }
            else if ("32".equals(operCode))
            // 定位GPS
            {
                resStr = surezenHandle.gps(ioSession, reqInfos, msg, operCode);
            }
            else if ("33".equals(operCode))
            // 记步
            {
                resStr = surezenHandle.step(ioSession, reqInfos, msg, operCode);
            }
            else if ("34".equals(operCode))
            // 睡眠
            {
                resStr = surezenHandle.sleep(ioSession, reqInfos, msg, operCode);
            }
            else
            {
                resStr = surezenHandle.otherWatchReq(ioSession, reqInfos, msg, operCode);
            }
        }
        else
        // 腕表回应服务器指令
        {
            // 腕表回应服务器信息不用处理
        }
        return resStr;
    }

    /**
     * 分隔腕表请求
     * @param str
     * @return
     */
    private static String[] splitReq(String str)
    {
        List<String> list = new ArrayList<String>();
        if (str != null)
        {
            int startPos = 0;
            for (int i = 0; i < str.length(); i++)
            {
                if (str.charAt(i) == ',')
                {
                    list.add(str.substring(startPos, i));
                    startPos = i + 1;
                }
            }
            list.add(str.substring(startPos));
        }
        return list.toArray(new String[list.size()]);
    }

}
