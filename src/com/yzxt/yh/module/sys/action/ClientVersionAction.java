package com.yzxt.yh.module.sys.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.server.Config;
import com.yzxt.yh.constant.ConfigKey;
import com.yzxt.yh.constant.ConstFilePath;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.sys.bean.ClientVersion;
import com.yzxt.yh.module.sys.bean.Org;
import com.yzxt.yh.module.sys.service.ClientVersionService;
import com.yzxt.yh.module.sys.service.OrgService;
import com.yzxt.yh.util.FileUtil;
import com.yzxt.yh.util.StringUtil;

public class ClientVersionAction extends BaseAction
{
    private static final long serialVersionUID = 1L;
    private Logger logger = Logger.getLogger(ClientVersionAction.class);

    private ClientVersionService clientVersionService;

    private OrgService orgService;

    public ClientVersionService getClientVersionService()
    {
        return clientVersionService;
    }

    public void setClientVersionService(ClientVersionService clientVersionService)
    {
        this.clientVersionService = clientVersionService;
    }

    public OrgService getOrgService()
    {
        return orgService;
    }

    public void setOrgService(OrgService orgService)
    {
        this.orgService = orgService;
    }

    /**
     * 下载客户端
     * 由于多个手机浏览器不支持跳转，目前将APK改为相同的名字，使下载地址统一
     * @throws IOException 
     */
    public void dl() throws IOException
    {
        String appType = request.getParameter("appType");
        String deviceType = request.getParameter("deviceType");
        if (StringUtil.isEmpty(appType) || StringUtil.isEmpty(deviceType))
        {
            response.getWriter().write("请求参数错误!");
            return;
        }
        ClientVersion lv = clientVersionService.getLatestVersion(appType, deviceType);
        String path = lv != null ? lv.getPath() : null;
        if (StringUtil.isEmpty(path))
        {
            response.getWriter().write("此APK不存在!");
            return;
        }
        path = Config.getInstance().getString(ConfigKey.SERVER_URL);
        response.sendRedirect(path);
        response.flushBuffer();
    }

    /**
     * 下载客户端
     * @throws IOException 
     */
    public void d() throws IOException
    {
        String fileName = request.getParameter("file");
        String filePath = FileUtil.getFullPath(ConstFilePath.CLIENT_APP_FOLDER + fileName);
        File file = new File(filePath);
        if (!file.exists() || file.isDirectory())
        {
            logger.error("需要下载的客户端文件“”" + filePath + "”不存在。");
            return;
        }
        response.reset();
        // 设置response的Header
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment;filename="
                + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
        response.addHeader("Content-Length", "" + file.length());
        OutputStream os = response.getOutputStream();
        InputStream is = new BufferedInputStream(new FileInputStream(file));
        byte[] buffer = new byte[4096];
        int read = -1;
        while ((read = is.read(buffer)) != -1)
        {
            os.write(buffer, 0, read);
        }
        is.close();
        os.close();
    }

    /**
     * 显示可下载客户端列表
     * @return
     * @throws Exception 
     */
    public String dlList() throws Exception
    {
        String orgId = request.getParameter("orgId");
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put("state", Constant.COMMON_STATE_ENABLE);
        List<ClientVersion> clientVersions = clientVersionService.queryLatestVersions(filter);
        request.setAttribute("clientVersions", clientVersions);
        Org org = orgService.getShowOrg(orgId);
        if (org != null && StringUtil.isNotEmpty(org.getLogoPath()))
        {
            org.setLogoPath(FileUtil.encodePath(org.getLogoPath()));
        }
        request.setAttribute("org", org);
        return "clientApps";
    }

    /**
     * 客户端显示下载页面
     * @return
     * @throws IOException
     */
    public String dPage() throws IOException
    {
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put("state", Constant.COMMON_STATE_ENABLE);
        String appType = (String) request.getParameter("appType");
        filter.put("appType", appType);
        List<ClientVersion> clientVersions = clientVersionService.queryVersions(filter);
        request.setAttribute("clientVersions", clientVersions);
        return "clientPage";
    }

}
