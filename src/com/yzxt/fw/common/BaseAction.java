package com.yzxt.fw.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.opensymphony.xwork2.ActionSupport;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.fw.server.ConstSv;

public class BaseAction extends ActionSupport implements ServletRequestAware, ServletResponseAware
{
    private static final long serialVersionUID = 1L;
    protected static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    private Map<String, String> cookies;
    private int page;
    private int rows;
    protected HttpServletResponse response;
    protected HttpServletRequest request;

    public Map<String, String> getCookies()
    {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies)
    {
        this.cookies = cookies;
    }

    public int getPage()
    {
        return page;
    }

    public void setPage(int page)
    {
        this.page = page;
    }

    public int getRows()
    {
        return rows;
    }

    public void setRows(int rows)
    {
        this.rows = rows;
    }

    public void setServletResponse(HttpServletResponse response)
    {
        this.response = response;
        this.response.setContentType("text/html");
        this.response.setCharacterEncoding("UTF-8");
    }

    public void setServletRequest(HttpServletRequest request)
    {
        this.request = request;
    }

    public HttpServletResponse getResponse()
    {
        return response;
    }

    public HttpServletRequest getRequest()
    {
        return request;
    }

    public HttpSession getSession()
    {
        return request.getSession();
    }

    public Object getCurrentUser()
    {
        return getSession().getAttribute(ConstSv.SESSION_USER_KEY);
    }

    protected void write(Object data)
    {
        try
        {
            this.response.setHeader("Cache-Control", "no-cache");
            this.response.setContentType("text/html;charset=utf-8");
            PrintWriter out = getResponse().getWriter();
            String s = getDataString(data);
            out.print(s);
            out.flush();
            out.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private String getDataString(Object data)
    {
        if (data == null)
        {
            return "";
        }
        if (data instanceof String)
        {
            return (String) data;
        }
        else if (data instanceof Result)
        {
            Result result = (Result) data;
            JsonObject json = new JsonObject();
            json.addProperty("state", result.getState());
            json.addProperty("msg", result.getMsg());
            if (result.getData() != null)
            {
                json.addProperty("data", result.getData().toString());
            }
            return json.toString();
        }
        else if (data instanceof PageModel)
        {
            PageModel<?> pageModel = (PageModel<?>) data;
            return new StringBuilder().append("{\"total\":").append(pageModel.getTotal()).append(",\"rows\":")
                    .append(gson.toJson(pageModel.getData())).append("}").toString();
        }
        else if (data instanceof List)
        {
            return gson.toJson(data);
        }
        else
        {
            return data.toString();
        }
    }

}
