package com.yzxt.yh.module.sys.action;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.common.Result;
import com.yzxt.yh.constant.ConstFilePath;
import com.yzxt.yh.constant.ConstRole;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.sys.bean.FileDesc;
import com.yzxt.yh.module.sys.bean.Org;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.service.OrgService;
import com.yzxt.yh.util.FileUtil;
import com.yzxt.yh.util.StringUtil;

public class OrgAction extends BaseAction
{
    private static final long serialVersionUID = 1L;
    private Logger logger = Logger.getLogger(OrgAction.class);

    private OrgService orgService;

    private Org org;

    // 组织图标
    private File logo;

    private String logoFileName;

    private String logoContentType;

    public OrgService getOrgService()
    {
        return orgService;
    }

    public void setOrgService(OrgService orgService)
    {
        this.orgService = orgService;
    }

    public Org getOrg()
    {
        return org;
    }

    public void setOrg(Org org)
    {
        this.org = org;
    }

    public File getLogo()
    {
        return logo;
    }

    public void setLogo(File logo)
    {
        this.logo = logo;
    }

    public String getLogoFileName()
    {
        return logoFileName;
    }

    public void setLogoFileName(String logoFileName)
    {
        this.logoFileName = logoFileName;
    }

    public String getLogoContentType()
    {
        return logoContentType;
    }

    public void setLogoContentType(String logoContentType)
    {
        this.logoContentType = logoContentType;
    }

    /**
     * 获取子组织，主要用于管理员查询
     * @throws Exception 
     */
    public void listChildren()
    {
        try
        {
            List<Org> children = null;
            String parentId = getRequest().getParameter("id");
            if (StringUtil.isEmpty(parentId))
            {
                // 操作人
                User user = (User) super.getCurrentUser();
                if (user != null && Constant.USER_TYPE_ADMIN.equals(user.getType()))
                {
                    Collection<String> roles = user.getRoles();
                    if (roles.contains(ConstRole.ADMIN))
                    {
                        children = orgService.getChildren(null, true);
                    }
                    else
                    {
                        children = orgService.getChildren(user.getOrgId(), false);
                    }
                }
            }
            else
            {
                children = orgService.getChildren(parentId, true);
            }
            for (Org org : children)
            {
                if (org.getIsLeaf().intValue() == 1)
                {
                    org.setState("closed");
                }
                else
                {
                    org.setState("open");
                }
                if (StringUtil.isNotEmpty(org.getLogoPath()))
                {
                    org.setLogoPath(FileUtil.encodePath(org.getLogoPath()));
                }
            }
            super.write(children);
        }
        catch (Exception e)
        {
            logger.error("获取子组织出错。", e);
        }
    }

    /**
     * 获取树的子组织
     */
    public void treeChildren()
    {
        List<Org> children = null;
        String parentId = getRequest().getParameter("id");
        if (StringUtil.isEmpty(parentId))
        {
            // 操作人
            User user = (User) super.getCurrentUser();
            if (user != null && Constant.USER_TYPE_ADMIN.equals(user.getType()))
            {
                Collection<String> roles = user.getRoles();
                if (roles.contains(ConstRole.ADMIN))
                {
                    children = orgService.getChildren(null, true);
                }
                else
                {
                    children = orgService.getChildren(user.getOrgId(), false);
                }
            }
        }
        else
        {
            children = orgService.getChildren(parentId, true);
        }
        JsonArray ar = new JsonArray();
        for (Org org : children)
        {
            JsonObject jsObj = new JsonObject();
            if (org.getIsLeaf().intValue() == 1)
            {
                jsObj.addProperty("state", "closed");
            }
            else
            {
                jsObj.addProperty("state", "open");
            }
            jsObj.addProperty("id", org.getId());
            jsObj.addProperty("text", org.getName());
            ar.add(jsObj);
        }
        super.write(ar);
    }

    /**
     * 跳转到组织明细页面
     * @throws Exception 
     */
    public String toDetail() throws Exception
    {
        try
        {
            Org parentOrg = null;
            Org org = null;
            String operType = request.getParameter("operType");
            if ("addTop".equals(operType))
            {
                org = new Org();
                org.setLevel(1);
            }
            else if ("addSub".equals(operType))
            {
                String parentId = request.getParameter("parentId");
                parentOrg = orgService.getOrg(parentId);
                org = new Org();
                org.setParentId(parentOrg.getId());
                org.setFullId(parentOrg.getFullId());
                org.setParentName(parentOrg.getName());
                org.setLevel(parentOrg.getLevel() + 1);
            }
            else if ("update".equals(operType))
            {
                String id = request.getParameter("id");
                org = orgService.load(id);
                String parentId = org.getParentId();
                if (StringUtil.isNotEmpty(parentId))
                {
                    parentOrg = orgService.getOrg(parentId);
                    org.setParentName(parentOrg != null ? parentOrg.getName() : "");
                }
                if (StringUtil.isNotEmpty(org.getLogoPath()))
                {
                    org.setLogoPath(FileUtil.encodePath(org.getLogoPath()));
                }
            }
            request.setAttribute("org", org);
            request.setAttribute("operType", operType);
            if ("view".equals(operType))
            {
                return "view";
            }
            else
            {
                return "edit";
            }
        }
        catch (Exception e)
        {
            logger.error("跳转至组织明细出错。", e);
            return "error";
        }
    }

    /**
     * 添加组织
     */
    public void add()
    {
        Result r = null;
        try
        {
            // 组织图标
            if (logo != null)
            {
                FileDesc fileDesc = FileUtil.save(logo, logoFileName, ConstFilePath.ORG_LOGO_FOLDER, true);
                org.setLogo(fileDesc);
            }
            User curOper = (User) super.getCurrentUser();
            org.setCreateBy(curOper.getId());
            r = orgService.add(org);
        }
        catch (Exception e)
        {
            r = new Result(Result.STATE_FAIL, "添加组织错误。", null);
            logger.error("添加组织错误。", e);
        }
        super.write(r);
    }

    /**
     * 修改组织
     */
    public void update()
    {
        Result r = null;
        try
        {
            // 组织图标
            if (logo != null)
            {
                FileDesc fileDesc = FileUtil.save(logo, logoFileName, ConstFilePath.ORG_LOGO_FOLDER, true);
                org.setLogo(fileDesc);
            }
            User curOper = (User) super.getCurrentUser();
            org.setUpdateBy(curOper.getId());
            r = orgService.update(org);
        }
        catch (Exception e)
        {
            r = new Result(Result.STATE_FAIL, "修改组织错误。", null);
            logger.error("修改组织错误。", e);
        }
        super.write(r);
    }

}
