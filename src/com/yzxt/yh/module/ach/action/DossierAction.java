package com.yzxt.yh.module.ach.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.ConstDict;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.ach.bean.Dossier;
import com.yzxt.yh.module.ach.bean.DossierDetail;
import com.yzxt.yh.module.ach.service.DossierService;
import com.yzxt.yh.module.sys.bean.DictDetail;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.service.DictService;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.FileUtil;
import com.yzxt.yh.util.StringUtil;

public class DossierAction extends BaseAction
{
    private static final long serialVersionUID = 1L;
    private Logger logger = Logger.getLogger(DossierAction.class);

    private DossierService dossierService;
    private DictService dictService;

    public DossierService getDossierService()
    {
        return dossierService;
    }

    public void setDossierService(DossierService dossierService)
    {
        this.dossierService = dossierService;
    }

    public DictService getDictService()
    {
        return dictService;
    }

    public void setDictService(DictService dictService)
    {
        this.dictService = dictService;
    }

    /**
     * 跳转的我的病历夹
     * @return
     */
    public String toMyDossier()
    {
        User operOper = (User) super.getCurrentUser();
        List<DictDetail> dossierType = dictService.getDictDetails(ConstDict.DOSSIER_TYPE);
        request.setAttribute("dossierTypes", dossierType);
        String custId = request.getParameter("custId");
        if (StringUtil.isEmpty(custId) && Constant.USER_TYPE_CUSTOMER.equals(operOper.getType()))
        {
            custId = operOper.getId();
        }
        request.setAttribute("custId", custId);
        return "myDossiers";
    }

    /**
     * 查询病历夹列表
     */
    public void query()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            // 查询人
            filter.put("operUser", super.getCurrentUser());
            filter.put("custId", request.getParameter("custId"));
            String typeStr = request.getParameter("type");
            if (StringUtil.isNotEmpty(typeStr))
            {
                filter.put("type", Integer.valueOf(typeStr));
            }
            filter.put("startCreateDate", DateUtil.getDateFromHtml(request.getParameter("startCreateDate")));
            filter.put("endCreateDate", DateUtil.getDateFromHtml(request.getParameter("endCreateDate")));
            PageModel<Dossier> pageModel = dossierService.query(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询病历夹错误。", e);
        }
    }

    /**
     * 病历夹明细
     */
    public String toDetail()
    {
        try
        {
            String id = request.getParameter("id");
            Dossier dossier = dossierService.load(id);
            List<DossierDetail> details = dossier.getDetails();
            if (details != null)
            {
                for (DossierDetail dd : details)
                {
                    dd.setPath(FileUtil.encodePath(dd.getPath()));
                }
            }
            request.setAttribute("dossier", dossier);
            return "view";
        }
        catch (Exception e)
        {
            logger.error("查看病历夹错误。", e);
        }
        return "error";
    }

}
