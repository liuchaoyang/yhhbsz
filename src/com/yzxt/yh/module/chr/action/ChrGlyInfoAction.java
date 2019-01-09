package com.yzxt.yh.module.chr.action;

import java.util.List;

import net.sf.json.JSONArray;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.common.Result;
import com.yzxt.yh.module.chr.bean.GlyCheck;
import com.yzxt.yh.module.chr.bean.GlyDrug;
import com.yzxt.yh.module.chr.bean.GlySport;
import com.yzxt.yh.module.chr.bean.Glycuresis;
import com.yzxt.yh.module.chr.service.ChrGlyInfoService;
import com.yzxt.yh.module.chr.service.ChrVisitService;
import com.yzxt.yh.module.sys.bean.User;

import common.Logger;

public class ChrGlyInfoAction extends BaseAction
{

    private static final long serialVersionUID = 1L;

    private Logger logger = Logger.getLogger(ChrGlyInfoAction.class);

    private Glycuresis info;

    private ChrGlyInfoService chrGlyInfoService;

    private ChrVisitService chrVisitService;

    public Glycuresis getInfo()
    {
        return info;
    }

    public void setInfo(Glycuresis info)
    {
        this.info = info;
    }

    public ChrGlyInfoService getChrGlyInfoService()
    {
        return chrGlyInfoService;
    }

    public void setChrGlyInfoService(ChrGlyInfoService chrGlyInfoService)
    {
        this.chrGlyInfoService = chrGlyInfoService;
    }

    public ChrVisitService getChrVisitService()
    {
        return chrVisitService;
    }

    public void setChrVisitService(ChrVisitService chrVisitService)
    {
        this.chrVisitService = chrVisitService;
    }

    /**
     * 查看随访详情血压记录表
     * @author huangGang
     * 2015.4.24
     * */
    public String view()
    {
        try
        {
            User curOper = (User) super.getCurrentUser();
            String id = request.getParameter("id");
            String custId = request.getParameter("custId");
            Glycuresis glyInfo = chrGlyInfoService.getGlyInfoByVisitId(id);
            if (glyInfo == null)
            {

            }
            List<GlyDrug> drugs = chrGlyInfoService.getDrugsByGlyId(glyInfo.getId());
            if (drugs != null)
            {
                glyInfo.setDrugs(JSONArray.fromObject(drugs).toString());
            }
            List<GlySport> sports = chrGlyInfoService.getSportsByGlyId(glyInfo.getId());
            if (sports != null)
            {
                glyInfo.setSports(JSONArray.fromObject(sports).toString());
            }
            List<GlyCheck> checks = chrGlyInfoService.getChecksByGlyId(glyInfo.getId());
            if (checks != null)
            {
                glyInfo.setChecks(JSONArray.fromObject(checks).toString());
            }
            request.setAttribute("info", glyInfo);
            return "view";
        }
        catch (Exception e)
        {
            logger.error("查看随访页面详情失败", e);
        }
        return null;
    }

    /**
     * 保存血糖随访记录表情况
     * */
    public void doXTRecord()
    {
        Result r = null;
        try
        {
            User curOper = (User) super.getCurrentUser();
            info.setDoctorId(curOper.getId());
            r = chrGlyInfoService.saveGlyInfo(info);
        }
        catch (Exception e)
        {
            r = new Result(Result.STATE_FAIL, "保存随访记录表失败", r);
            logger.error("保存随访记录表失败", e);
        }
        super.write(r);
    }
}
