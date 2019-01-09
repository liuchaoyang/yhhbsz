package com.yzxt.yh.module.sys.action;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.yh.module.sys.bean.Org;
import com.yzxt.yh.module.sys.service.OrgService;
import com.yzxt.yh.util.FileUtil;
import com.yzxt.yh.util.StringUtil;

public class HelpAction extends BaseAction
{
    private static final long serialVersionUID = 1L;
    private Logger logger = Logger.getLogger(HelpAction.class);

    private OrgService orgService;

    public OrgService getOrgService()
    {
        return orgService;
    }

    public void setOrgService(OrgService orgService)
    {
        this.orgService = orgService;
    }

    /**
     * 系统帮助
     */
    public String list()
    {
        try
        {
            String orgId = request.getParameter("orgId");
            // 登录成功
            Org org = orgService.getShowOrg(orgId);
            if (org != null && StringUtil.isNotEmpty(org.getLogoPath()))
            {
                org.setLogoPath(FileUtil.encodePath(org.getLogoPath()));
            }
            request.setAttribute("org", org);
        }
        catch (Exception e)
        {
            logger.error("跳转至帮助信息出错。", e);
        }
        return "list";
    }

    /**
     * 客户端帮助
     */
    public String client() throws IOException
    {
        return "client";
    }

}
