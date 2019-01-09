package com.yzxt.yh.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yzxt.fw.common.ResultTran;
import com.yzxt.fw.server.ConstSv;
import com.yzxt.fw.server.SessionListener;
import com.yzxt.fw.util.GsonUtil;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.bean.UserSession;
import com.yzxt.yh.module.sys.service.UserRoleService;
import com.yzxt.yh.module.sys.service.UserService;

public class RequestFilter extends HttpServlet implements Filter
{

    private static final long serialVersionUID = 1L;

    private FilterConfig filterConfig;

    private static Logger logger = Logger.getLogger(RequestFilter.class);

    public RequestFilter()
    {
        super();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
        this.filterConfig = filterConfig;
    }

    public void destroy()
    {
        super.destroy();
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
            ServletException
    {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        ServletContext servletContext = filterConfig.getServletContext();
        // 获取请求的地址
        String servletPath = req.getServletPath();//.getr.getRequestURI();
        String clientparams = req.getParameter("clientparams");
        String method = req.getMethod();
        User us  = new User();
        Gson gson = new Gson();
    	JsonParser jsonParser = new JsonParser();
    	JsonElement jsonElement = jsonParser.parse(gson.toJson(us));
    	JsonArray retJson = new JsonArray();
        if (StringUtil.isEmpty(clientparams))
        // 浏览器访问
        {
            try
            {
                String webPubPages = servletContext.getInitParameter("webPubPages");
                if (webPubPages.contains(servletPath) || servletPath.startsWith("/pub")
                        || servletPath.startsWith("/resources"))
                {
                    filterChain.doFilter(req, response);
                }
                else
                {
                    User user = (User) session.getAttribute(ConstSv.SESSION_USER_KEY);
                    if (user != null)
                    {
                        filterChain.doFilter(request, response);
                    }
                    else
                    {
                        // 尝试从Cookies中获取组织ID
                        res.sendRedirect(req.getContextPath() + "/sys/wel_login.action");
                    }
                }
            }
            catch (Exception e)
            {
                logger.error("Filter page errer, servletPath:" + servletPath, e);
            }
        }
        else
        // 客户端访问
        {
        	if ("GET".equals(method)) {	
        		System.out.println("it");			
        		System.out.print("Get is not allowed");
        		return;
        		}

            try
            {
                JsonObject jsonObj = (JsonObject) GsonUtil.parse(clientparams);
                req.setAttribute("jsonObj", jsonObj);
                String tranPubPages = servletContext.getInitParameter("tranPubPages");
                if (!tranPubPages.contains(servletPath) && !servletPath.startsWith("/pubs"))
                {
                    String ticket = GsonUtil.toString(jsonObj.get("ticket"));
                    String userId = GsonUtil.toString(jsonObj.get("userId"));
                    String phone = GsonUtil.toString(jsonObj.get("phone"));
                    String password = GsonUtil.toString(jsonObj.get("password"));
                    User user = null;
                    // 有登录信息
                    if (StringUtil.isNotEmpty(ticket))
                    {
                        user = (User) SessionListener.get(ticket, session);
                        // 判断数据库中回话是否过期
                        if (user == null)
                        {
                            WebApplicationContext wac = WebApplicationContextUtils
                                    .getRequiredWebApplicationContext(session.getServletContext());
                            UserService userService = (UserService) wac.getBean("userService");
                            UserRoleService userRoleService = (UserRoleService) wac.getBean("userRoleService");
                            UserSession userSession = userService.getValidSession(ticket, userId);
                            if (userSession != null)
                            {
                                user = userService.getUser(userId);
                                Collection<String> roles = userRoleService.getRoleIdsByUser(userId);
                                user.setRoles(roles);
                                SessionListener.put(ticket, session, user);
                            }
                        }
                        // 获取用户信息失效
                        if (user != null)
                        {
                            req.setAttribute("operUser", user);
                            filterChain.doFilter(req, response);
                        }
                        else
                        {
                            sendClientResponse(response, ResultTran.STATE_SESSION_TIMEOUT, "登录信息已经失效，请重新系统。", retJson);
                        }
                    }
                    else if(StringUtil.isNotEmpty(phone)&&StringUtil.isNotEmpty(password)){
                    	 filterChain.doFilter(req, response);
                    }else
                    {   
                    	
                        sendClientResponse(response, ResultTran.STATE_ERROR, "请登录系统。",retJson);
                    }
                }
                else
                {
                    filterChain.doFilter(req, response);
                }
            }
            catch (Exception e)
            {
                sendClientResponse(response, ResultTran.STATE_SERVER_ERROR, "服务器访问错误，请稍后重试。", retJson);
                logger.error("Filter client errer, servletPath:" + servletPath, e);
            }
        }
    }

    private void sendClientResponse(ServletResponse response, String resultCode, String msg, JsonElement data)
            throws IOException
    {
        JsonObject resDatas = new JsonObject();
        resDatas.addProperty("resultCode", resultCode);
        resDatas.addProperty("resultMsg", msg);
        if (data == null)
        {
            resDatas.addProperty("data", "");
        }
        else
        {
            resDatas.add("data", data);
        }
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(resDatas);
        out.flush();
    }

}
