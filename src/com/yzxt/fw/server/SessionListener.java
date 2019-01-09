package com.yzxt.fw.server;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

public class SessionListener implements javax.servlet.http.HttpSessionListener
{
    private static ServletContext context;

    public SessionListener()
    {
    }

    public void sessionCreated(HttpSessionEvent e)
    {
    }

    @SuppressWarnings("rawtypes")
    public void sessionDestroyed(HttpSessionEvent e)
    {
        String uniqueKey = (String) e.getSession().getAttribute("uniqueKey");
        if (uniqueKey != null && uniqueKey.length() > 0)
        {
            Map activeSessions = context != null ? (HashMap) context.getAttribute("activeSessions") : null;
            if (activeSessions != null)
            {
                activeSessions.remove(uniqueKey);
            }
        }
    }

    @SuppressWarnings(
    {"rawtypes", "unchecked"})
    public static void put(String uniqueKey, HttpSession session, Object obj)
    {
        session.setAttribute("uniqueKey", uniqueKey);
        session.setAttribute(ConstSv.SESSION_USER_KEY, obj);
        if (context == null)
        {
            context = session.getServletContext();
            context.setAttribute("activeSessions", new HashMap());
        }
        Map sessions = (Map) context.getAttribute("activeSessions");
        sessions.put(uniqueKey, session);
    }

    @SuppressWarnings(
    {"rawtypes", "unchecked"})
    public static Object get(String uniqueKey, HttpSession session)
    {
        Object obj = null;
        if (context != null)
        {
            Map sessions = (Map) context.getAttribute("activeSessions");
            HttpSession sessionP = (HttpSession) sessions.get(uniqueKey);
            if (sessionP != null)
            {
                obj = sessionP.getAttribute(ConstSv.SESSION_USER_KEY);
                if (obj != null)
                {
                    session.setAttribute("uniqueKey", uniqueKey);
                    session.setAttribute(ConstSv.SESSION_USER_KEY, obj);
                    sessions.put(uniqueKey, session);
                }
                else
                {
                    sessions.remove(uniqueKey);
                }
            }
        }
        return obj;
    }

}