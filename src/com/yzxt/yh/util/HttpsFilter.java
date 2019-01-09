package com.yzxt.yh.util;

import java.io.IOException;
import java.net.URL;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpsFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        String scheme =req.getScheme();
        URL newUrl = null;
        if (scheme.equals("https")) {
            chain.doFilter(req,resp);
        } else {
            HttpServletResponse response = (HttpServletResponse) resp;
            HttpServletRequest httpRequest = (HttpServletRequest) req;
            String queryString = (httpRequest.getQueryString() ==null ? "" : "?" +httpRequest.getQueryString());
            response.setStatus(301);
            String requestUrl =httpRequest.getRequestURL().toString();
            URL reqUrl=new URL(requestUrl+queryString);
            newUrl=new URL("https",reqUrl.getHost(),reqUrl.getPort(),reqUrl.getFile());
            response.setHeader("Location",newUrl.toString());
            response.setHeader("Connection","close");
        }
    }
    @Override
    public void destroy() {

    }
}

