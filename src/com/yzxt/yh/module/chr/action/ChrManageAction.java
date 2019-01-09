package com.yzxt.yh.module.chr.action;

import java.util.HashMap;
import java.util.Map;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.module.chr.bean.Manage;
import com.yzxt.yh.module.chr.service.ChrManageService;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.StringUtil;

import common.Logger;

public class ChrManageAction extends BaseAction
{

    private static final long serialVersionUID = 1L;

    private Logger logger = Logger.getLogger(ChrManageAction.class);

    private Manage chrManage;

    private String[] mngType;

    private ChrManageService chrManageService;

    public Manage getChrManage()
    {
        return chrManage;
    }

    public void setChrManage(Manage chrManage)
    {
        this.chrManage = chrManage;
    }

    public String[] getMngType()
    {
        return mngType;
    }

    public void setMngType(String[] mngType)
    {
        this.mngType = mngType;
    }

    public ChrManageService getChrManageService()
    {
        return chrManageService;
    }

    public void setChrManageService(ChrManageService chrManageService)
    {
        this.chrManageService = chrManageService;
    }

    /**
     * @author huangGang
     * 2015.4.22
     * 将人纳入慢病管理表
     * */
    public void addChr()
    {
        Result r = null;
        try
        {
            // 当前操作人
            User curOper = (User) super.getCurrentUser();
            chrManage.setMngType(mngType);
            chrManage.setCreateBy(curOper.getId());
            r = chrManageService.addChr(chrManage);
        }
        catch (Exception e)
        {
            r = new Result(Result.STATE_FAIL, "纳入慢病失败。", null);
            logger.error("纳入慢病失败。", e);
        }
        super.write(r);
    }

    /**
     * 医生获取自己添加的慢病列表
     * @author huangGang
     * 2015.4.21
     * @param
     * */
    public void getChrList()
    {
        try
        {
            User curOper = (User) super.getCurrentUser();
            Map<String, Object> filter = new HashMap<String, Object>();
            /* String consultTitle = request.getParameter("consultTitle");
             String consultContext = request.getParameter("consultContext");*/
            String MemberName = request.getParameter("memberName");
            String manageType = request.getParameter("manageType");
            filter.put("curOper", curOper);
            /*filter.put("consultTitle", StringUtil.trim(consultTitle));
            filter.put("consultContext", StringUtil.trim(consultContext));*/
            filter.put("MemberName", StringUtil.trim(MemberName));
            filter.put("manageType", StringUtil.trim(manageType));
            PageModel<Manage> pageModel = chrManageService.getChrList(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("获取咨询列表失败", e);
        }
    }
}
