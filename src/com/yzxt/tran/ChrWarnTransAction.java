package com.yzxt.tran;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.yzxt.fw.common.ResultTran;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.fw.util.GsonUtil;
import com.yzxt.yh.module.chk.bean.CheckWarn;
import com.yzxt.yh.module.chk.service.CheckWarnService;
import com.yzxt.yh.module.chr.bean.Manage;
import com.yzxt.yh.module.chr.service.ChrManageService;
import com.yzxt.yh.util.DateUtil;

import common.Logger;

public class ChrWarnTransAction extends BaseTranAction
{
    private static final long serialVersionUID = 1L;

    private Logger logger = Logger.getLogger(ChrWarnTransAction.class);

    private CheckWarnService checkWarnService;
    private ChrManageService chrManageService;

    /*	private ChrVisitService chrVisitService;
    	
    	private ChrManageService chrMngService;

    	private ChrGlyInfoService chrGlyService;
    	
    	private ChrPreService chrPreService;*/

    public CheckWarnService getCheckWarnService()
    {
        return checkWarnService;
    }

    public void setCheckWarnService(CheckWarnService checkWarnService)
    {
        this.checkWarnService = checkWarnService;
    }

    /*    public ChrVisitService getChrVisitService()
        {
            return chrVisitService;
        }

        public void setChrVisitService(ChrVisitService chrVisitService)
        {
            this.chrVisitService = chrVisitService;
        }

        public ChrManageService getChrMngService()
        {
            return chrMngService;
        }

        public void setChrMngService(ChrManageService chrMngService)
        {
            this.chrMngService = chrMngService;
        }

        public ChrGlyInfoService getChrGlyService()
        {
            return chrGlyService;
        }

        public void setChrGlyService(ChrGlyInfoService chrGlyService)
        {
            this.chrGlyService = chrGlyService;
        }

        public ChrPreService getChrPreService()
        {
            return chrPreService;
        }

        public void setChrPreService(ChrPreService chrPreService)
        {
            this.chrPreService = chrPreService;
        }*/

    public ChrManageService getChrManageService()
    {
        return chrManageService;
    }

    public void setChrManageService(ChrManageService chrManageService)
    {
        this.chrManageService = chrManageService;
    }

    /**
     * 查询预警人员列表
     * @return
     */
    public void riskCusts()
    {
        try
        {
            JsonObject obj = super.getParams();
            initQuery(obj);
            Map<String, Object> filter = new HashMap<String, Object>();
            JsonElement uObj = obj.get("userId");
            String userId = uObj.getAsString();
            filter.put("userId", userId);
            PageTran<CheckWarn> pageTran = checkWarnService.queryWarnCustTran(filter, synTime, synType, pageSize);
            List<CheckWarn> list = pageTran.getData();
            JsonArray ja = new JsonArray();
            for (CheckWarn cw : list)
            {
                JsonObject jObj = new JsonObject();
                jObj.addProperty("custId", cw.getCustId());
                jObj.addProperty("userName", cw.getName());
                jObj.addProperty("cardNo", cw.getIdCard());
                jObj.addProperty("warningType", getTranWarnType(cw.getType()));
                jObj.addProperty("warningGrade", cw.getLevel());
                jObj.addProperty("warningDiscribe", cw.getDescript());
                jObj.addProperty("warningTime", DateUtil.getTranTime(cw.getWarnTime()));
                jObj.addProperty("uploadTime", DateUtil.getTranTime(cw.getWarnTime()));
                ja.add(jObj);
            }
            super.write(ResultTran.STATE_OK, null, ja);
        }
        catch (Exception e)
        {
            logger.error("查询预警会员错误。", e);
            super.write(ResultTran.STATE_ERROR, "查询预警会员错误。");
        }
    }

    /**
     * 预警信息的查询
     * @return
     */
    public String risklist()
    {
        try
        {
            JsonObject paramJson = super.getParams();
            if (paramJson == null)
            {
                return null;
            }
            int pageSize = paramJson.get("pageSize").getAsInt();
            String custId = GsonUtil.toString(paramJson.get("custId"));
            //此时的lastSynTime的时间格式是yyyy/MM/dd hh:mm:ss 000
            Timestamp synTime = DateUtil.getSynTimeByStr(GsonUtil.toString(paramJson.get("lastSynTime")));
            // 同步类型,1表示同步时间点之后的数据，2表示同步时间点之前的数据
            /* int synType = (paramJson.get("synType") == null || paramJson.get("synType").getAsString().equals("")) ? 2 : paramJson.get("synType")
                      .getAsInt();*/
            JsonArray retJson = new JsonArray();
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("custId", custId);
            // 拉取时间
            filter.put("sysTime", synTime);
            filter.put("maxSize", pageSize);
            List<CheckWarn> checkWarns = checkWarnService.getWarnListForClient(filter);
            if (checkWarns != null && checkWarns.size() > 0)
            {
                for (CheckWarn checkWarn : checkWarns)
                {
                    JsonObject jsonObj = new JsonObject();
                    jsonObj.addProperty("id", (checkWarn.getId()));
                    jsonObj.addProperty("userName", (checkWarn.getName()));
                    jsonObj.addProperty("cardNo", (checkWarn.getIdCard()));
                    jsonObj.addProperty("riskId", "null");
                    jsonObj.addProperty("warningType", getTranWarnType(checkWarn.getType()));
                    jsonObj.addProperty("status", (checkWarn.getState()));
                    jsonObj.addProperty("warningGrade", (checkWarn.getLevel()));
                    jsonObj.addProperty("warningDiscribe", (checkWarn.getDescript()));
                    String startTimeStr = null;
                    if (checkWarn.getWarnTime() != null)
                    {
                        startTimeStr = DateUtil.toSynTimeStr(new Timestamp(checkWarn.getWarnTime().getTime()));
                        startTimeStr = startTimeStr.replace('-', '/');
                        startTimeStr = startTimeStr.substring(0, 10);
                    }
                    jsonObj.addProperty("warningTime", startTimeStr);
                    jsonObj.addProperty("uploadTime", DateUtil.toSynTimeStr(new Timestamp(checkWarn.getWarnTime().getTime())).replace('-', '/'));
                    retJson.add(jsonObj);
                }
            }
            super.write(ResultTran.STATE_OK, "操作成功。", retJson);
        }
        catch (Exception e)
        {
            logger.error("客户端刷新失败.", e);
            super.write(ResultTran.STATE_ERROR, "操作失败。", null);
        }
        return null;
    }

    /**
     * 获取客户端告警类型
     * @param type
     * @return
     */
    public static String getTranWarnType(String type)
    {
        if ("xy".equals(type))
        {
            return "1";
        }
        else if ("xt".equals(type))
        {
            return "2";
        }
        else if ("xl".equals(type))
        {
            return "3";
        }
        else if ("xo".equals(type))
        {
            return "4";
        }
        else if ("tw".equals(type))
        {
            return "5";
        }
        else if ("dgc".equals(type))
        {
            return "6";
        }
        else if ("ns".equals(type))
        {
            return "7";
        }
        else if ("nsfx".equals(type))
        {
            return "8";
        }
        else if ("tz".equals(type))
        {
            return "10";
        }
        return "";
    }

    /**
     * 查询慢病人员列表
     * @return
     */
    public void mnglist()
    {
        try
        {
            JsonObject obj = super.getParams();
            initQuery(obj);
            Map<String, Object> filter = new HashMap<String, Object>();
            JsonElement uObj = obj.get("userId");
            String userId = uObj.getAsString();
            filter.put("userId", userId);
            PageTran<Manage> pageTran = chrManageService.queryChrTran(filter, synTime, synType, pageSize);
            List<Manage> list = pageTran.getData();
            JsonArray ja = new JsonArray();
            for (Manage m : list)
            {
                /*
                JsonObject jObj = new JsonObject();
                jObj.addProperty("custId", cw.getCustId());
                jObj.addProperty("userName", cw.getName());
                jObj.addProperty("cardNo", cw.getIdCard());
                jObj.addProperty("warningType", getTranWarnType(cw.getType()));
                jObj.addProperty("warningGrade", cw.getLevel());
                jObj.addProperty("warningDiscribe", cw.getDescript());
                jObj.addProperty("warningTime", DateUtil.getTranTime(cw.getWarnTime()));
                jObj.addProperty("uploadTime", DateUtil.getTranTime(cw.getWarnTime()));
                ja.add(jObj);
                */
            }
            super.write(ResultTran.STATE_OK, null, ja);
        }
        catch (Exception e)
        {
            logger.error("查询慢病人员错误。", e);
            super.write(ResultTran.STATE_ERROR, "查询慢病人员错误。");
        }
    }

}
