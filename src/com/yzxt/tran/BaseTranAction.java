package com.yzxt.tran;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opensymphony.xwork2.ActionSupport;
import com.yzxt.fw.common.ResultTran;
import com.yzxt.fw.ext.gson.DateAdapter;
import com.yzxt.fw.ext.gson.NumberAdapter;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.StringUtil;

public class BaseTranAction extends ActionSupport implements ServletRequestAware, ServletResponseAware
{
    private static final long serialVersionUID = 1L;
    protected final static int defaultPageSize = 10000;
    protected static Gson gson = new GsonBuilder().setDateFormat("yyyy/MM/dd HH:mm:ss")
            .registerTypeAdapter(Timestamp.class, new DateAdapter()).registerTypeAdapter(Date.class, new DateAdapter())
            .registerTypeAdapter(Double.class, new NumberAdapter())
            .registerTypeAdapter(Integer.class, new NumberAdapter()).serializeNulls().create();
    private static Logger logger = Logger.getLogger(BaseTranAction.class);
    private Map<String, String> cookies;
    protected HttpServletResponse response;
    protected HttpServletRequest request;
    // 查询参数
    protected Timestamp synTime;
    protected int synType;
    protected int pageSize;

    public Map<String, String> getCookies()
    {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies)
    {
        this.cookies = cookies;
    }

    public void setServletRequest(HttpServletRequest req)
    {
        this.request = req;
    }

    public void setServletResponse(HttpServletResponse res)
    {
        this.response = res;
        this.response.setContentType("text/html");
        this.response.setCharacterEncoding("UTF-8");
    }

    /**
     * 初始查询参数
     * @param obj
     */
    protected void initQuery(JsonObject obj)
    {
        if (obj != null)
        {
            JsonElement sTimeObj = obj.get("lastSynTime");
            if (sTimeObj != null && StringUtil.isNotEmpty(sTimeObj.getAsString()))
            {
                synTime = gson.fromJson(sTimeObj, Timestamp.class);
            }
            else
            {
                synTime = null;
            }
            // 对于老的接口，包含此参数，新的接口不包含此参数，当synTime不为空时去synTime之前的数据
            JsonElement sTypeObj = obj.get("synType");
            if (sTypeObj != null && StringUtil.isNotEmpty(sTypeObj.getAsString()))
            {
                synType = sTypeObj.getAsInt();
                // 1：同步lastSynTime以后的数据，2：同步lastSynTime以前的数据
                if (synType == 2)
                {
                    synType = -1;
                }
            }
            else
            {
                synType = -1;
            }
            JsonElement sSize = obj.get("pageSize");
            if (sSize != null && StringUtil.isNotEmpty(sSize.getAsString()))
            {
                pageSize = sSize.getAsInt();
            }
            else
            {
                pageSize = 10;
            }
        }
    }

    protected JsonObject getParams()
    {
        return (JsonObject) request.getAttribute("jsonObj");
    }

    public User getOperUser()
    {
        return (User) request.getAttribute("operUser");
    }

    protected void outWrite(Object obj)
    {
        try
        {
            response.setHeader("Cache-Control", "no-cache");
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(obj);
            out.flush();
            out.close();
        }
        catch (IOException e)
        {
            logger.error("输出客户端数据出错。", e);
            e.printStackTrace();
        }
    }

    /**
     * 写入客户端数据
     * @param resultCode
     * @param msg
     * @param data
     */
    protected void write(String resultCode, String msg, Object data)
    {
        JsonObject obj = new JsonObject();
        obj.addProperty("resultCode", resultCode);
        obj.addProperty("resultMsg", msg);
        if (data != null)
        {
            if (data instanceof JsonElement)
            {
                obj.add("data", (JsonElement) data);
            }
            else if (data instanceof PageTran<?>)
            {
                obj.add("data", gson.toJsonTree(((PageTran<?>) data).getData()));
            }
            else
            {
                obj.add("data", gson.toJsonTree(data));
            }
        }
        outWrite(obj);
    }

    /**
     * 写入客户端数据
     * @param resultCode
     * @param msg
     * @param data
     */
    protected void write(String resultCode, String msg)
    {
        write(resultCode, msg, null);
    }

    /**
     * 写入客户端数据
     * @param resultCode
     * @param msg
     * @param data
     */
    protected void write(String resultCode)
    {
        write(resultCode, ResultTran.STATE_OK.equals(resultCode) ? "操作成功。" : "操作失败。", null);
    }

    /**
     * 编码客户端文件上传信息
     * @param filePath
     * @param fileName
     * @return
     */
    protected static String encodeFileInfo(String filePath, String fileName)
    {
        String str = "";
        try
        {
            filePath = filePath != null ? filePath : "";
            fileName = fileName != null ? fileName : "";
            str = Base64.encodeBase64URLSafeString(("path:" + filePath + ",name:" + fileName).getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            logger.error("文件信息转换失败，不支持UTF-8编码.", e);
        }
        return str;
    }

    /**
     * 解码文件上传信息
     * @param fileInfo
     * @return 文件路径和
     */
    protected static String[] decodeFileInfo(String fileInfo)
    {
        if (StringUtil.isEmpty(fileInfo))
        {
            return null;
        }
        String[] strs = null;
        try
        {
            String str = new String(Base64.decodeBase64(fileInfo), "UTF-8");
            int mPos = str.indexOf(",name:");
            if (mPos > -1)
            {
                strs = new String[]
                {str.substring(5, mPos), str.substring(mPos + 6)};
            }
        }
        catch (UnsupportedEncodingException e)
        {
            logger.error("文件信息转换失败，不支持UTF-8编码.", e);
            e.printStackTrace();
        }
        return strs;
    }

}
