package com.yzxt.yh.module.chr.action;

import java.util.List;

import net.sf.json.JSONArray;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.common.Result;
import com.yzxt.yh.module.chr.bean.PreDrug;
import com.yzxt.yh.module.chr.bean.PreSport;
import com.yzxt.yh.module.chr.bean.Pressure;
import com.yzxt.yh.module.chr.service.ChrPreService;
import com.yzxt.yh.module.chr.service.ChrVisitService;
import com.yzxt.yh.module.sys.bean.User;
import common.Logger;

public class ChrPreInfoAction extends BaseAction
{
    private static final long serialVersionUID = 1L;

    private Logger logger = Logger.getLogger(ChrPreInfoAction.class);

    private Pressure info;

    private ChrPreService chrPreService;

    private ChrVisitService chrVisitService;

    public Pressure getInfo()
    {
        return info;
    }

    public void setInfo(Pressure info)
    {
        this.info = info;
    }

    public ChrPreService getChrPreService()
    {
        return chrPreService;
    }

    public void setChrPreService(ChrPreService chrPreService)
    {
        this.chrPreService = chrPreService;
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
     * 查看随访详情记录
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
            Pressure preInfo = chrPreService.getPreInfoByVisitId(id);
            if (preInfo == null)
            {

            }
            List<PreDrug> drugs = chrPreService.getDrugsByPreId(preInfo.getId());
            if (drugs != null)
            {
                preInfo.setDrugs(JSONArray.fromObject(drugs).toString());
            }
            List<PreSport> sports = chrPreService.getSportsByPreId(preInfo.getId());
            if (sports != null)
            {
                preInfo.setSports(JSONArray.fromObject(sports).toString());
            }
            request.setAttribute("info", preInfo);
            return "view";
        }
        catch (Exception e)
        {
            logger.error("查看随访页面详情失败", e);
        }
        return null;
    }

    /**
     * 保存血压随访记录表情况
     * */
    public void doXYRecord()
    {
        Result r = null;
        try
        {
            User curOper = (User) super.getCurrentUser();
            info.setDoctorId(curOper.getId());
            r = chrPreService.savePreInfo(info);
        }
        catch (Exception e)
        {
            r = new Result(Result.STATE_FAIL, "保存随访记录表失败", r);
            logger.error("保存随访记录表失败", e);
        }
        super.write(r);
    }
}
