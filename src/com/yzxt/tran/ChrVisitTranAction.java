package com.yzxt.tran;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.common.ResultTran;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.fw.util.GsonUtil;
import com.yzxt.yh.module.chr.bean.GlyCheck;
import com.yzxt.yh.module.chr.bean.GlyDrug;
import com.yzxt.yh.module.chr.bean.GlySport;
import com.yzxt.yh.module.chr.bean.Glycuresis;
import com.yzxt.yh.module.chr.bean.PreDrug;
import com.yzxt.yh.module.chr.bean.PreSport;
import com.yzxt.yh.module.chr.bean.Pressure;
import com.yzxt.yh.module.chr.bean.Visit;
import com.yzxt.yh.module.chr.service.ChrGlyInfoService;
import com.yzxt.yh.module.chr.service.ChrPreService;
import com.yzxt.yh.module.chr.service.ChrVisitService;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.DateUtil;
import common.Logger;

/*
 * 随访接口：添加随访记录（二个），会员随访表，随访详细表，查看随访记录表
 * */
public class ChrVisitTranAction extends BaseTranAction
{

    private static final long serialVersionUID = 1L;

    private Logger logger = Logger.getLogger(ChrVisitTranAction.class);

    private Visit visit;

    private ChrVisitService chrVisitService;

    private ChrGlyInfoService chrGlyInfoService;

    private ChrPreService chrPreService;

    public Visit getVisit()
    {
        return visit;
    }

    public void setVisit(Visit visit)
    {
        this.visit = visit;
    }

    public ChrVisitService getChrVisitService()
    {
        return chrVisitService;
    }

    public void setChrVisitService(ChrVisitService chrVisitService)
    {
        this.chrVisitService = chrVisitService;
    }

    public ChrGlyInfoService getChrGlyInfoService()
    {
        return chrGlyInfoService;
    }

    public void setChrGlyInfoService(ChrGlyInfoService chrGlyInfoService)
    {
        this.chrGlyInfoService = chrGlyInfoService;
    }

    public ChrPreService getChrPreService()
    {
        return chrPreService;
    }

    public void setChrPreService(ChrPreService chrPreService)
    {
        this.chrPreService = chrPreService;
    }

    /**
     * 下达随访任务
     * @param type
     * @author h
     * @return
     * 2015.8.31
     */
    public void addVisit()
    {
        JsonObject paramJson = super.getParams();
        try
        {
            JsonArray retJson = new JsonArray();
            if (paramJson == null)
            {
                super.write(ResultTran.STATE_ERROR, "客户端参数为空", retJson);
                return;
            }
            String userId = (String) GsonUtil.toString(paramJson.get("userId"));
            String custId = (String) GsonUtil.toString(paramJson.get("custId"));
            Integer type = (Integer) GsonUtil.toInteger(paramJson.get("type"));
            String planFlupTimeStr = (String) GsonUtil.toString(paramJson.get("planFlupTime"));
            Date planFlupTime = DateUtil.getDateFromHtml(planFlupTimeStr);
            Visit chrVisit = new Visit();
            chrVisit.setCustId(custId);
            chrVisit.setType(type);
            chrVisit.setCreateBy(userId);
            chrVisit.setUpdateBy(userId);
            chrVisit.setPlanFlupTime(planFlupTime);
            Result r = chrVisitService.addVisit(chrVisit);
            if (r.getState() == 1)
            {
                super.write(ResultTran.STATE_OK, "下达随访任务成功", r.getData());
            }
            else
            {
                super.write(ResultTran.STATE_ERROR, "下达随访任务失败", r.getData());
            }
        }
        catch (Exception e)
        {
            logger.error("下达随访任务失败。", e);
            JsonArray errJson = new JsonArray();
            super.write(ResultTran.STATE_ERROR, "服务器端出现异常", errJson);
        }
    }

    /**
     * 查询随访记录人员列表
     * 随访记录：已随访的记录人员列表（对应接口随访记录）
     * @return
     */
    public void visitList()
    {
        try
        {
            JsonObject obj = super.getParams();
            initQuery(obj);
            Map<String, Object> filter = new HashMap<String, Object>();
            JsonElement uObj = obj.get("userId");
            String userId = uObj.getAsString();
            filter.put("userId", userId);
            PageTran<Visit> pageTran = chrVisitService.queryChrVisitTran(filter, synTime, synType, pageSize);
            List<Visit> list = pageTran.getData();
            JsonArray ja = new JsonArray();
            for (Visit v : list)
            {
                JsonObject jObj = new JsonObject();
                jObj.addProperty("custId", v.getCustId());
                jObj.addProperty("userName", v.getMemberName());
                jObj.addProperty("cardNo", v.getIdCard());
                jObj.addProperty("flupId", v.getId());
                jObj.addProperty("type", v.getType());
                jObj.addProperty("danagerGrade", v.getGrade());
                jObj.addProperty("preFlupTime", DateUtil.getTranTime(v.getLastFlupTime()));
                jObj.addProperty("preFlupGrade", v.getPreFlupGrade());
                jObj.addProperty("uploadTime", DateUtil.getTranTime(v.getCreateTime()));
                ja.add(jObj);
            }
            super.write(ResultTran.STATE_OK, null, ja);
        }
        catch (Exception e)
        {
            logger.error("查询慢病人员错误。", e);
            super.write(ResultTran.STATE_ERROR, "查询慢病人员错误。");
        }
    }

    /**
     * 查询随访记录个人具体项目（血压，血糖）列表
     * 查询已随访的个人具体项目列表（对应接口随访记录明细）
     * @return
     */
    public void visitPerList()
    {
        try
        {
            JsonObject obj = super.getParams();
            initQuery(obj);
            Map<String, Object> filter = new HashMap<String, Object>();
            //用户id
            JsonElement uObj = obj.get("custId");
            String userId = uObj.getAsString();
            filter.put("userId", userId);
            //用户管理项目
            JsonElement typeObj = obj.get("type");
            String visitType = typeObj.getAsString();
            filter.put("visitType", visitType);
            PageTran<Visit> pageTran = chrVisitService.queryVisitPerTran(filter, synTime, synType, pageSize);
            List<Visit> list = pageTran.getData();
            JsonArray ja = new JsonArray();
            for (Visit v : list)
            {
                JsonObject jObj = new JsonObject();
                jObj.addProperty("userName", v.getMemberName());
                jObj.addProperty("flupId", v.getId());
                jObj.addProperty("custId", v.getCustId());
                jObj.addProperty("flupCode", v.getVisitNo());
                jObj.addProperty("flupTime", DateUtil.getTranTime(v.getActualFlupTime()));
                jObj.addProperty("flupType", v.getType());
                jObj.addProperty("flupResult", v.getIshandled());
                jObj.addProperty("fluper", v.getDoctorName());
                jObj.addProperty("uploadTime", DateUtil.getTranTime(v.getCreateTime()));
                ja.add(jObj);
            }
            super.write(ResultTran.STATE_OK, null, ja);
        }
        catch (Exception e)
        {
            logger.error("查询慢病人员错误。", e);
            super.write(ResultTran.STATE_ERROR, "查询慢病人员错误。");
        }
    }

    /**
     * 查询随访记录人员列表
     * 随访记录：未随访的记录人员列表（对应接口随访任务）
     * 
     * @return
     */
    public void visitPerNoList()
    {
        try
        {
            JsonObject obj = super.getParams();
            Map<String, Object> filter = new HashMap<String, Object>();
            String userId = GsonUtil.toString(obj.get("userId"));
            filter.put("userId", userId);
            String keyword = GsonUtil.toString(obj.get("keyword"));
            filter.put("keyword", keyword);
            Integer type = GsonUtil.toInteger(obj.get("type"));
            filter.put("type", type);
            PageTran<Visit> pageTran = chrVisitService.queryChrNOVisitTran(filter);
            List<Visit> list = pageTran.getData();
            JsonArray ja = new JsonArray();
            for (Visit v : list)
            {
                JsonObject jObj = new JsonObject();
                jObj.addProperty("flupId", v.getId());
                jObj.addProperty("custId", v.getCustId());
                jObj.addProperty("userName", v.getMemberName());
                jObj.addProperty("cardNo", v.getIdCard());
                jObj.addProperty("visitNo", v.getVisitNo());
                /*jObj.addProperty("flupTaskId", v.getIdCard());*/
                jObj.addProperty("status", v.getType());
                jObj.addProperty("warningType", v.getType());
                jObj.addProperty("warningGrade", v.getGrade());
                jObj.addProperty("preFlupTime", DateUtil.getTranTime(v.getLastFlupTime()));
                jObj.addProperty("nextFlupTime", DateUtil.getTranTime(v.getNextFlupTime()));
                jObj.addProperty("preFlupGrade", v.getPreFlupGrade());
                jObj.addProperty("uploadTime", DateUtil.getTranTime(v.getCreateTime()));
                ja.add(jObj);
            }
            super.write(ResultTran.STATE_OK, null, ja);
        }
        catch (Exception e)
        {
            logger.error("查询慢病人员错误。", e);
            super.write(ResultTran.STATE_ERROR, "查询慢病人员错误。");
        }
    }

    /**
     * 处理血糖随访
    */
    public void saveXtVisit()
    {
        Result r = null;
        try
        {
            //获取客户端参数包
            JsonObject obj = super.getParams();
            //取糖尿病随访数据表封装的数据
            JsonObject obja = obj.getAsJsonObject("glyFlupParams");
            //取要随访用户的随访id号
            String visitId = GsonUtil.toString(obj.get("flupTaskId"));
            //取登录医生用户的userId号
            String userId = GsonUtil.toString(obj.get("userId"));
            //取要随访用户的custId号
            String custId = GsonUtil.toString(obj.get("custId"));
            Glycuresis glyInfo = chrGlyInfoService.getGlyInfoByVisitId(visitId);
            if (glyInfo != null)
            {
                super.write(ResultTran.STATE_ERROR, "已处理", glyInfo.getVisitId());
            }
            Glycuresis info = new Glycuresis();
            //封装血糖数据，借用service中的添加方法。
            info.setCustId(custId);
            info.setVisitId(visitId);
            info.setFlupDateStr(obja.get("flupDate").getAsString());
            info.setFlupType(GsonUtil.toInteger(obja.get("flupType")));
            info.setFlupRsult(GsonUtil.toInteger(obja.get("flupRsult")));
            info.setDoctorId(userId);
            info.setNextFlupTimeStr(GsonUtil.toString(obja.get("nextFlupDate")));
            info.setGlySymptomOther(GsonUtil.toString(obja.get("glySymptomOther")));
            String glySymptomS = "";
            JsonArray hbpSymptoms = obja.getAsJsonArray("glySymptom");//症状
            if (hbpSymptoms != null)
            {
                for (JsonElement glyObj : hbpSymptoms)
                {
                    glySymptomS += glyObj.getAsInt() + ";";
                }
                info.setGlySymptom(glySymptomS);
            }
            info.setHbpBps(GsonUtil.toInteger(obja.get("glySysBP")));
            info.setHbpBpd(GsonUtil.toInteger(obja.get("glyDiaBP")));
            info.setHbpWeight(GsonUtil.toDouble(obja.get("glyWeight")));
            info.setHbpPhysique(GsonUtil.toDouble(obja.get("glyPhysique")));
            info.setHbpFootBack(GsonUtil.toInteger(obja.get("glyFootback")));
            info.setHbpOther(GsonUtil.toString(obja.get("glyFootback")));
            info.setHbpDrinking(GsonUtil.toInteger(obja.get("glyDrinking")));
            info.setHbpSmoking(GsonUtil.toInteger(obja.get("glySmoking")));

            JsonArray sports = obja.getAsJsonArray("glySport");//运动表
            if (sports != null)
            {
                List<GlySport> chrSports = gson.fromJson(sports, new TypeToken<List<GlySport>>()
                {
                }.getType());
                info.setGlySport(chrSports);
            }

            info.setHbpFood(GsonUtil.toInteger(obja.get("glyMainFood")));
            info.setHbpPsycrecovery(GsonUtil.toInteger(obja.get("glyPsycrecovery")));
            info.setHbpCompliance(GsonUtil.toInteger(obja.get("glyCompliance")));
            info.setHbpFastGlu(GsonUtil.toDouble(obja.get("glyFastGlu")));
            info.setHbpDurgsObey(GsonUtil.toInteger(obja.get("glyDurgsObey")));
            info.setHbpDrugsUntoward(GsonUtil.toInteger(obja.get("glyDrugsUntoward")));
            info.setHbpLowSuger(GsonUtil.toInteger(obja.get("glyLowGlu")));
            info.setHbpReferWhy(GsonUtil.toString(obja.get("glyReferWhy")));
            info.setHbpReferOrg(GsonUtil.toString(obja.get("glyReferOrgan")));
            info.setHbpReferObj(GsonUtil.toString(obja.get("glyReferGrade")));

            JsonArray drugs = obja.getAsJsonArray("drugInfo");
            List<GlyDrug> druglist = new ArrayList<GlyDrug>();
            if (drugs != null)
            {
                druglist = gson.fromJson(drugs, new TypeToken<List<GlyDrug>>()
                {
                }.getType());
                if (druglist != null && druglist.size() > 0)
                {
                    for (GlyDrug drug : druglist)
                    {
                        drug.setType(1);
                    }
                }
            }
            JsonArray ydsinfo = obja.getAsJsonArray("ydsInfo");//胰岛素用药
            if (!(ydsinfo == null || ydsinfo.size() < 1))
            {

                for (JsonElement elobj : ydsinfo)
                {

                    JsonObject dobj = elobj.getAsJsonObject();
                    GlyDrug drugSug = new GlyDrug();
                    drugSug.setType(2);
                    drugSug.setHbpDrugsName(GsonUtil.toString(dobj.get("glyInsulinName")));
                    drugSug.setHbpDrugsName(GsonUtil.toString(dobj.get("glyInsulinType")));
                    drugSug.setHbpDrugsFy(GsonUtil.toInteger(dobj.get("glyInsulinFrency")));
                    drugSug.setHbpDrugsCount(GsonUtil.toInteger(dobj.get("glyInsulinCount")));
                    drugSug.setGlyInsulinType(GsonUtil.toString(dobj.get("glyInsulinType")));
                    drugSug.setGlyInsulinUseMethod(GsonUtil.toString(dobj.get("glyInsulinUseMethod")));
                    druglist.add(drugSug);
                }
            }
            info.setGlyDrug(druglist);

            JsonArray ckArr = obja.getAsJsonArray("checkOther");//检查
            List<GlyCheck> cklist = new ArrayList<GlyCheck>();
            if (ckArr != null)
            {
                cklist = gson.fromJson(ckArr, new TypeToken<List<GlyCheck>>()
                {
                }.getType());
            }

            JsonElement bloodSug = obja.get("glyHbA1c");
            JsonElement glyHbA1cTime = obja.get("glyHbA1cTime");

            if (bloodSug != null)
            {

                String bldSug = bloodSug.getAsString();
                GlyCheck ckSug = new GlyCheck();
                ckSug.setHbpCheckType(2);
                ckSug.setHbpCheckName("");
                ckSug.setHbpCheckRemark(bldSug);
                ckSug.setHbpSugerBlood(bldSug.trim().length() > 1 ? Double.valueOf(bldSug) : 0);
                String glyHbA1cTimeStr = null;
                if (glyHbA1cTime != null)
                {
                    glyHbA1cTimeStr = glyHbA1cTime.getAsString() + " 000";
                }
                ckSug.setHbpCheckTime(glyHbA1cTime == null ? null : DateUtil.getSynTimeByStr(glyHbA1cTimeStr));
                cklist.add(ckSug);
            }

            info.setGlyCheck(cklist);
            //当前操作用户
            User curOper = super.getOperUser();
            info.setDoctorId(curOper.getId());
            r = chrGlyInfoService.saveGlyInfoTran(info);
            int state = r.getState();
            //调用血糖分析。判断是否糖尿病，
            if (Result.STATE_SUCESS == state)
            {
                JsonObject rObj = new JsonObject();
                rObj.addProperty("dataCode", "1");
                rObj.addProperty("dataMsg", "保存血糖随访成功");
                rObj.addProperty("userFlupCode", r.getData().toString());
                super.write(ResultTran.STATE_OK, "保存成功", rObj);
            }
            else
            {
                super.write(String.valueOf(r.getState()), r.getMsg(), r.getData());
            }
        }
        catch (Exception e)
        {
            logger.error("保存血糖出现异常错误。", e);
            r = new Result(Result.STATE_FAIL, "保存随访记录表失败", r);
        }
    }

    /**
     * 处理血压随访
    */
    public void saveXyVisit()
    {
        Result r = null;
        try
        {
            //获取客户端参数包
            JsonObject obj = super.getParams();
            //取糖尿病随访数据表封装的数据
            JsonObject obja = obj.getAsJsonObject("hbpFlupParams");
            //取要随访用户的随访id号
            String visitId = GsonUtil.toString(obj.get("flupTaskId"));
            //取登录医生用户的userId号
            String userId = GsonUtil.toString(obj.get("userId"));
            //取要随访用户的custId号
            String custId = GsonUtil.toString(obj.get("custId"));
            Pressure preInfo = chrPreService.getPreInfoByVisitId(visitId);
            if (preInfo != null)
            {
                super.write(ResultTran.STATE_ERROR, "已随访", preInfo.getVisitId());
            }
            Pressure info = new Pressure();
            //封装血糖数据，借用service中的添加方法。
            info.setCustId(custId);
            info.setVisitId(visitId);
            info.setFlupDateStr(obja.get("flupDate").getAsString());
            info.setFlupType(GsonUtil.toInteger(obja.get("flupType")));
            info.setFlupRsult(GsonUtil.toInteger(obja.get("flupRsult")));
            info.setDoctorId(userId);
            info.setNextFlupTimeStr(GsonUtil.toString(obja.get("nextFlupDate")));
            info.setHbpSymptomOther(GsonUtil.toString(obja.get("hbpSymptomOther")));
            String hbpSymptomS = "";
            JsonArray hbpSymptoms = obj.getAsJsonArray("hbpSymptom");//症状
            if (hbpSymptoms != null)
            {
                for (JsonElement hbpobj : hbpSymptoms)
                {
                    hbpSymptomS += hbpobj.getAsInt() + ";";
                }
                info.setHbpSymptom(hbpSymptomS);
            }
            info.setHbpBps(GsonUtil.toInteger(obja.get("hbpSysBP")));
            info.setHbpBpd(GsonUtil.toInteger(obja.get("hbpDiaBP")));
            info.setHbpWeight(GsonUtil.toDouble(obja.get("hbpWeight")));
            info.setHbpPhysique(GsonUtil.toDouble(obja.get("hbpPhysique")));
            info.setHbpPulse(GsonUtil.toInteger(obja.get("hbpPulse")));
            info.setHbpOther(GsonUtil.toString(obja.get("hbpOther")));
            info.setHbpDrinking(GsonUtil.toDouble(obja.get("hbpDrinking")));
            info.setHbpSmoking(GsonUtil.toInteger(obja.get("hbpSmoking")));

            JsonArray sports = obja.getAsJsonArray("hbpSport");//运动表
            if (sports != null)
            {
                List<PreSport> preSports = gson.fromJson(sports, new TypeToken<List<PreSport>>()
                {
                }.getType());
                info.setPreSport(preSports);
            }

            info.setHbpSalarium(GsonUtil.toInteger(obja.get("hbpSalarium")));
            info.setHbpPsycrecovery(GsonUtil.toInteger(obja.get("hbpPsycrecovery")));
            info.setHbpCompliance(GsonUtil.toInteger(obja.get("hbpCompliance")));
            info.setHbpHelpCheck(GsonUtil.toString(obja.get("hbpHelpCheck")));
            info.setHbpDurgsObey(GsonUtil.toInteger(obja.get("hbpDurgsObey")));
            info.setHbpDrugsUntoward(GsonUtil.toString(obja.get("hbpDrugsUntoward")));
            info.setHbpReferWhy(GsonUtil.toString(obja.get("hbpReferWhy")));
            info.setHbpReferOrg(GsonUtil.toString(obja.get("hbpReferOrganization")));
            info.setHbpReferObj(GsonUtil.toString(obja.get("hbpReferGrade")));

            JsonArray drugs = obja.getAsJsonArray("hbpDrugs");
            if (drugs != null)
            {
                List<PreDrug> druglist = gson.fromJson(drugs, new TypeToken<List<PreDrug>>()
                {
                }.getType());
                info.setPreDrug(druglist);
            }

            //当前操作用户
            User curOper = super.getOperUser();
            info.setDoctorId(curOper.getId());
            r = chrPreService.savePreInfoTran(info);
            int state = r.getState();
            //完善血压 调用血压分析，看是否产生预警。
            if (Result.STATE_SUCESS == state)
            {
                JsonObject rObj = new JsonObject();
                rObj.addProperty("dataCode", "1");
                rObj.addProperty("dataMsg", "保存血压随访成功");
                rObj.addProperty("userFlupCode", r.getData().toString());
                super.write(ResultTran.STATE_OK, "保存成功", rObj);
            }
            else
            {
                super.write(String.valueOf(r.getState()), r.getMsg(), r.getData());
            }
        }
        catch (Exception e)
        {
            logger.error("保存血压出现异常错误。", e);
            r = new Result(Result.STATE_FAIL, "保存随访记录表失败", r);
        }
    }

    public void viewXyOrXt()
    {
        String visitId = null;
        try
        {
            JsonObject obj = super.getParams();
            visitId = GsonUtil.toString(obj.get("visitId"));
            Integer type = GsonUtil.toInteger(obj.get("type"));
            JsonObject ret = new JsonObject();
            if (type == 1)
            {
                Pressure preInfo = chrPreService.getPreInfoByVisitId(visitId);
                List<PreDrug> drugs = chrPreService.getDrugsByPreId(preInfo.getId());
                if (drugs != null)
                {
                    preInfo.setPreDrug(drugs);
                }
                List<PreSport> sports = chrPreService.getSportsByPreId(preInfo.getId());
                if (sports != null)
                {
                    preInfo.setPreSport(sports);
                }
                ret = getPreJSONStr(preInfo);

            }
            else if (type == 2)
            {
                Glycuresis glyInfo = chrGlyInfoService.getGlyInfoByVisitId(visitId);
                List<GlyDrug> drugs = chrGlyInfoService.getDrugsByGlyId(glyInfo.getId());
                if (drugs != null)
                {
                    glyInfo.setGlyDrug(drugs);
                }
                List<GlySport> sports = chrGlyInfoService.getSportsByGlyId(glyInfo.getId());
                if (sports != null)
                {
                    glyInfo.setGlySport(sports);
                }
                List<GlyCheck> checks = chrGlyInfoService.getChecksByGlyId(glyInfo.getId());
                if (checks != null)
                {
                    glyInfo.setGlyCheck(checks);
                }
                ret = getGlyJSONStr(glyInfo);
            }
            JsonObject json = new JsonObject();
            json.add("flupData", ret);
            super.write(ResultTran.STATE_OK, "查看随访详细成功", json);
        }
        catch (Exception e)
        {
            logger.error("客户端查看随访详细错误。客户ID：" + visitId, e);
            super.write(ResultTran.STATE_ERROR, "查看随访详细错误。");
        }
    }

    private JsonObject getPreJSONStr(Pressure bean)
    {

        JsonObject json = (JsonObject) gson.toJsonTree(bean);
        json.addProperty("cardNo", bean.getIdcard());
        json.addProperty("custId", bean.getCustId());
        json.addProperty("userName", bean.getMemberName());
        json.addProperty("flupDoctor", bean.getDoctorName());

        List<?> list = bean.getPreSport();
        json.add("hbpSport", (list == null || list.isEmpty()) ? new JsonArray() : gson.toJsonTree(list));

        list = bean.getPreDrug();
        json.add("hbpDrugs", (list == null || list.isEmpty()) ? new JsonArray() : gson.toJsonTree(list));
        //症状
        String HbpSymptomStr = bean.getHbpSymptom();
        JsonParser jsonParser = new JsonParser();
        if (HbpSymptomStr != null && HbpSymptomStr.length() > 0)
        {
            JsonArray ja = new JsonArray();
            String[] hbpSymptomStrs = HbpSymptomStr.split(";");
            for (String hbpSymptomStr : hbpSymptomStrs)
            {
                if (hbpSymptomStr != null && hbpSymptomStr.length() > 0)
                {
                    ja.add(jsonParser.parse(hbpSymptomStr));
                }
            }
            json.add("hbpSymptom", ja);
        }
        else
        {
            JsonArray ja = new JsonArray();
            json.add("hbpSymptom", ja);
        }
        return json;
    }

    private JsonObject getGlyJSONStr(Glycuresis bean)
    {

        JsonObject json = (JsonObject) gson.toJsonTree(bean);
        json.addProperty("cardNo", bean.getIdCard());
        json.addProperty("custId", bean.getCustId());
        json.addProperty("userName", bean.getMemberName());
        json.addProperty("flupDoctor", bean.getDoctorName());

        List<GlySport> list = bean.getGlySport();
        json.add("glySport", (list == null || list.isEmpty()) ? new JsonArray() : gson.toJsonTree(list));

        List<?> cklist = bean.getGlyCheck();
        List<GlyCheck> cklist2 = new ArrayList<GlyCheck>();
        if (!(cklist == null || cklist.isEmpty()))
        {
            for (Object obj : cklist)
            {
                GlyCheck ck = (GlyCheck) obj;
                if (ck.getHbpCheckType() == 2)
                {
                    json.addProperty("glyHbA1c", ck.getHbpSugerBlood());
                    json.addProperty("glyHbA1cTime", DateUtil.getTranDate(ck.getHbpCheckTime()));
                }
                else
                {
                    cklist2.add(ck);
                }
            }
        }
        json.add("checkOther", (cklist2 == null || cklist2.isEmpty()) ? new JsonArray() : gson.toJsonTree(cklist2));

        List<?> druglist = bean.getGlyDrug();
        List<GlyDrug> druglist2 = new ArrayList<GlyDrug>();
        if (!(druglist == null || druglist.isEmpty()))
        {
            for (Object obj : druglist)
            {
                GlyDrug dg = (GlyDrug) obj;
                if (dg.getType() == 2)
                {
                    Map<String, Object> spmap = new HashMap<String, Object>();
                    spmap.put("glyInsulinName", dg.getHbpDrugsName());
                    spmap.put("glyInsulinType", dg.getType());
                    spmap.put("glyInsulinUseMethod", dg.getGlyInsulinUseMethod());
                    spmap.put("glyInsulinFrency", dg.getHbpDrugsFy());
                    spmap.put("glyInsulinCount", dg.getHbpDrugsCount());
                    spmap.put("glyInsulinType", dg.getGlyInsulinType());
                    spmap.put("glyInsulinUseMethod", dg.getGlyInsulinUseMethod());
                    json.add("ydsInfo", gson.toJsonTree(spmap));
                }
                else
                {
                    druglist2.add(dg);
                }
            }
        }
        json.add("drugInfo", (druglist2 == null || druglist2.isEmpty()) ? new JsonArray() : gson.toJsonTree(druglist2));
        //症状
        String GlySymptomStr = bean.getGlySymptom();
        JsonParser jsonParser = new JsonParser();
        if (GlySymptomStr != null && GlySymptomStr.length() > 0)
        {
            JsonArray ja = new JsonArray();
            String[] glySymptomStrs = GlySymptomStr.split(";");
            for (String glySymptomStr : glySymptomStrs)
            {
                if (glySymptomStr != null && glySymptomStr.length() > 0)
                {
                    ja.add(jsonParser.parse(glySymptomStr));
                }
            }
            json.add("glySymptom", ja);
        }
        return json;
    }
}
