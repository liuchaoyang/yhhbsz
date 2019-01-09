package com.yzxt.yh.module.chr.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.common.Result;
import com.yzxt.yh.constant.ConstChr;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.chr.bean.GlyCheck;
import com.yzxt.yh.module.chr.bean.GlyDrug;
import com.yzxt.yh.module.chr.bean.GlySport;
import com.yzxt.yh.module.chr.bean.Glycuresis;
import com.yzxt.yh.module.chr.bean.Visit;
import com.yzxt.yh.module.chr.dao.ChrGlyInfoDao;
import com.yzxt.yh.module.chr.dao.ChrVisitDao;
import com.yzxt.yh.module.chr.dao.GlyCheckDao;
import com.yzxt.yh.module.chr.dao.GlyDrugDao;
import com.yzxt.yh.module.chr.dao.GlySportDao;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;

@Transactional(ConstTM.DEFAULT)
public class ChrGlyInfoService
{
    private ChrGlyInfoDao chrGlyInfoDao;
    private GlyDrugDao glyDrugDao;
    private GlySportDao glySportDao;
    private GlyCheckDao glyCheckDao;
    private ChrVisitDao chrVisitDao;

    public ChrGlyInfoDao getChrGlyInfoDao()
    {
        return chrGlyInfoDao;
    }

    public void setChrGlyInfoDao(ChrGlyInfoDao chrGlyInfoDao)
    {
        this.chrGlyInfoDao = chrGlyInfoDao;
    }

    public GlyDrugDao getGlyDrugDao()
    {
        return glyDrugDao;
    }

    public void setGlyDrugDao(GlyDrugDao glyDrugDao)
    {
        this.glyDrugDao = glyDrugDao;
    }

    public GlySportDao getGlySportDao()
    {
        return glySportDao;
    }

    public void setGlySportDao(GlySportDao glySportDao)
    {
        this.glySportDao = glySportDao;
    }

    public GlyCheckDao getGlyCheckDao()
    {
        return glyCheckDao;
    }

    public void setGlyCheckDao(GlyCheckDao glyCheckDao)
    {
        this.glyCheckDao = glyCheckDao;
    }

    public ChrVisitDao getChrVisitDao()
    {
        return chrVisitDao;
    }

    public void setChrVisitDao(ChrVisitDao chrVisitDao)
    {
        this.chrVisitDao = chrVisitDao;
    }

    /**
     * 查询糖尿病信息
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Glycuresis getGlyInfoByVisitId(String id) throws Exception
    {
        return chrGlyInfoDao.getGlyInfoByVisitId(id);
    }

    /**
     * 查询与糖尿病相关的药物信息
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<GlyDrug> getDrugsByGlyId(String id)
    {
        return glyDrugDao.getDrugsByGlyId(id);
    }

    /**
     * 查询与糖尿病相关的运动信息
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<GlySport> getSportsByGlyId(String id)
    {
        return glySportDao.getSportsByGlyId(id);
    }

    /**
     * 查询与糖尿病相关的检测信息
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<GlyCheck> getChecksByGlyId(String id)
    {
        return glyCheckDao.getChecksByGlyId(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Result saveGlyInfo(Glycuresis info) throws Exception
    {
        //插入随访记录表
        Timestamp now = new Timestamp(System.currentTimeMillis());
        info.setCreateTime(now);
        //实际随访时间转换
        if (info.getFlupDateStr().length() > 10)
        {
            String flupTimeStr = info.getFlupDateStr();
            flupTimeStr = flupTimeStr + " 000";
            info.setFlupDate(DateUtil.getSynTimeByStr(flupTimeStr));
        }
        else
        {
            String flupTimeStr = info.getFlupDateStr();
            flupTimeStr = flupTimeStr + " 00:00:00";
            info.setFlupDate(DateUtil.getTimeByStr(flupTimeStr));

        }
        /*info.setFlupDate(CommonUtil.stringToShortDate(info.getFlupDateStr()));*/
        //下次随访时间转换
        if (info.getNextFlupTimeStr().length() > 10)
        {
            String nextFlupTimeStr = info.getNextFlupTimeStr();
            nextFlupTimeStr = nextFlupTimeStr + " 000";
            info.setNextFlupTime(DateUtil.getSynTimeByStr(nextFlupTimeStr));
        }
        else
        {
            String nextFlupTimeStr = info.getNextFlupTimeStr();
            nextFlupTimeStr = nextFlupTimeStr + " 00:00:00";
            info.setNextFlupTime(DateUtil.getTimeByStr(nextFlupTimeStr));

        }
        info.setStatus(ConstChr.CHR_STATUS_YES);
        String id = chrGlyInfoDao.insert(info);
        //更新随访表
        Visit chrVisit = new Visit();
        chrVisit.setId(info.getVisitId());
        chrVisit.setUpdateBy(info.getDoctorId());
        chrVisit.setUpdateTime(now);
        chrVisit.setIshandled(ConstChr.CHR_ISHANDLED_YES);
        //实际随访时间转换
        if (info.getFlupDateStr().length() > 10)
        {
            String actualFlupTime = info.getFlupDateStr();
            actualFlupTime = actualFlupTime + " 000";
            chrVisit.setActualFlupTime(DateUtil.getSynTimeByStr(actualFlupTime));
        }
        else
        {
            String actualFlupTime = info.getFlupDateStr();
            actualFlupTime = actualFlupTime + " 00:00:00";
            chrVisit.setActualFlupTime(DateUtil.getTimeByStr(actualFlupTime));

        }
        /*chrVisit.setActualFlupTime(DateUtil.getTimeByStr((info.getFlupDateStr()).toString()));*/
        int num = chrVisitDao.update(chrVisit);
        //是否有下次随访，如果有，插入新的随访表信息。没有不插入
        String visitId = null;
        if (StringUtil.isNotEmpty(info.getNextFlupTimeStr()) && info.getNextFlupTimeStr() != null)
        {
            Visit addVisit = new Visit();
            addVisit.setCustId(info.getCustId());
            addVisit.setDoctorId(info.getDoctorId());
            addVisit.setType(ConstChr.CHR_TYPE_XT);
            //下次随访时间转换
            if (info.getNextFlupTimeStr().length() > 10)
            {
                String nextFlupTimeStr = info.getNextFlupTimeStr();
                nextFlupTimeStr = nextFlupTimeStr + " 000";
                addVisit.setPlanFlupTime(DateUtil.getSynTimeByStr(nextFlupTimeStr));
            }
            else
            {
                String nextFlupTimeStr = info.getNextFlupTimeStr();
                nextFlupTimeStr = nextFlupTimeStr + " 00:00:00";
                addVisit.setPlanFlupTime(DateUtil.getTimeByStr(nextFlupTimeStr));

            }

            addVisit.setCreateBy(info.getDoctorId());
            addVisit.setUpdateBy(info.getDoctorId());
            addVisit.setCreateTime(now);
            addVisit.setUpdateTime(now);
            String visitNo = (now.toString()).replace("-", "");
            String visitNo2 = visitNo.replace(":", "");
            String visitNo3 = visitNo2.replace(" ", "");
            String visitNo4 = visitNo3.replace(".", "");
            addVisit.setVisitNo(visitNo4);
            addVisit.setIshandled(ConstChr.CHR_ISHANDLED_NOT);
            addVisit.setStatus(ConstChr.CHR_STATUS_YES);
            visitId = chrVisitDao.insert(addVisit);
        }
        //保存与血压记录表相关的药物信息
        String glyDrugId = null;
        if (!(info.getDrugs() == null || info.getDrugs().equals("")))
        {
            JSONObject drugs = JSONObject.fromObject(info.getDrugs());
            JSONArray alArr = drugs.getJSONArray("drug");
            List<GlyDrug> drugList = new ArrayList<GlyDrug>();

            int size = alArr.size();
            for (int i = 0; i < size; i++)
            {
                JSONArray drugArr = alArr.getJSONArray(i);
                GlyDrug drug = new GlyDrug();
                drug.setBId(info.getId());
                int drgSize = drugArr.size();
                if (drgSize == 3)
                {

                    String val = drugArr.getString((0));
                    if (val.trim().length() > 0)
                    {
                        drug.setHbpDrugsName(drugArr.getString(0));
                    }
                    else
                    {
                        continue;
                    }
                    val = drugArr.getString((1));
                    if (val.trim().length() > 0)
                        drug.setHbpDrugsFy(Integer.valueOf(val));
                    val = drugArr.getString((2));
                    if (val.trim().length() > 0)
                        drug.setHbpDrugsCount(Integer.valueOf(val));
                }

                if (i == 0)
                {
                    drug.setType(1);
                }
                else
                {
                    drug.setType(2);
                }
                drug.setType(i == 0 ? 2 : 1);
                glyDrugId = glyDrugDao.insert(drug);
            }
        }
        //保存与血压记录表相关的运动信息
        String glySportId = null;
        if (!(info.getSports() == null || info.getSports().equals("")))
        {
            JSONObject sports = JSONObject.fromObject(info.getSports());
            JSONArray alArr = sports.getJSONArray("sport");
            List<GlySport> sportList = new ArrayList<GlySport>();
            int size = alArr.size();
            for (int i = 0; i < size; i++)
            {
                JSONArray sportArr = alArr.getJSONArray(i);
                GlySport sport = new GlySport();
                sport.setBId(info.getId());
                int drgSize = sportArr.size();

                if (drgSize == 2)
                {
                    String val = sportArr.getString((0));
                    if (val.trim().length() > 0)
                    {
                        sport.setHbpSptTime(Integer.valueOf(val));
                    }
                    else
                    {

                        continue;
                    }
                    val = sportArr.getString((1));
                    if (val.trim().length() > 0)
                        sport.setHbpSptFy(Integer.valueOf(val));

                }
                glySportId = glySportDao.insert(sport);
            }
        }
        //保存check的
        String glyCheck = null;
        if (!(info.getChecks() == null || info.getChecks().equals("")))
        {

            JSONObject checks = JSONObject.fromObject(info.getChecks());
            JSONArray alArr = checks.getJSONArray("check");
            List<GlyCheck> checkList = new ArrayList<GlyCheck>();

            int size = alArr.size();

            for (int i = 0; i < size; i++)
            {
                JSONArray checkArr = alArr.getJSONArray(i);
                GlyCheck check = new GlyCheck();
                check.setbId(info.getId());
                int drgSize = checkArr.size();
                if (drgSize == 3)
                {

                    String val = checkArr.getString((0));
                    if (val.trim().length() > 0)
                    {
                        check.setHbpCheckName(val);
                        if (i == 0)
                        {
                            check.setHbpCheckType(2);
                            check.setHbpSugerBlood(Double.valueOf(val));
                        }
                        else
                        {
                            check.setHbpCheckType(1);
                        }
                    }
                    else
                    {
                        continue;
                    }
                    val = checkArr.getString((1));
                    if (val != null && val.length() == 10)
                    {
                        val += " 00:00:00";
                    }
                    if (val != null && val.trim().length() > 1)
                    {
                        check.setHbpCheckTime(Timestamp.valueOf(val));
                    }

                    val = checkArr.getString((2));
                    if (val.trim().length() > 0)
                        check.setHbpCheckRemark(val);
                }
                glyCheck = glyCheckDao.insert(check);
            }
        }
        if (StringUtil.isNotEmpty(id) && num > 0)
        {
            return new Result(Result.STATE_SUCESS, "保存成功", id);
        }
        else
        {
            return new Result(Result.STATE_FAIL, "随访更新出错或是血糖保存失败", id);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Result saveGlyInfoTran(Glycuresis info) throws Exception
    {
        //插入随访记录表
        Timestamp now = new Timestamp(System.currentTimeMillis());
        info.setCreateTime(now);
        //实际随访时间转换
        if (info.getFlupDateStr().length() > 10)
        {
            String flupTimeStr = info.getFlupDateStr();
            flupTimeStr = flupTimeStr + " 000";
            info.setFlupDate(DateUtil.getSynTimeByStr(flupTimeStr));
        }
        else
        {
            String flupTimeStr = info.getFlupDateStr();
            flupTimeStr = flupTimeStr + " 00:00:00";
            info.setFlupDate(DateUtil.getTimeByStr(flupTimeStr));

        }
        /*info.setFlupDate(CommonUtil.stringToShortDate(info.getFlupDateStr()));*/
        //下次随访时间转换
        if (info.getNextFlupTimeStr().length() > 10)
        {
            String nextFlupTimeStr = info.getNextFlupTimeStr();
            nextFlupTimeStr = nextFlupTimeStr + " 000";
            info.setNextFlupTime(DateUtil.getSynTimeByStr(nextFlupTimeStr));
        }
        else
        {
            String nextFlupTimeStr = info.getNextFlupTimeStr();
            nextFlupTimeStr = nextFlupTimeStr + " 00:00:00";
            info.setNextFlupTime(DateUtil.getTimeByStr(nextFlupTimeStr));

        }
        info.setStatus(ConstChr.CHR_STATUS_YES);
        String id = chrGlyInfoDao.insert(info);
        //更新随访表
        Visit chrVisit = new Visit();
        chrVisit.setId(info.getVisitId());
        chrVisit.setUpdateBy(info.getDoctorId());
        chrVisit.setUpdateTime(now);
        chrVisit.setIshandled(ConstChr.CHR_ISHANDLED_YES);
        //实际随访时间转换
        if (info.getFlupDateStr().length() > 10)
        {
            String actualFlupTime = info.getFlupDateStr();
            actualFlupTime = actualFlupTime + " 000";
            chrVisit.setActualFlupTime(DateUtil.getSynTimeByStr(actualFlupTime));
        }
        else
        {
            String actualFlupTime = info.getFlupDateStr();
            actualFlupTime = actualFlupTime + " 00:00:00";
            chrVisit.setActualFlupTime(DateUtil.getTimeByStr(actualFlupTime));

        }
        /*chrVisit.setActualFlupTime(DateUtil.getTimeByStr((info.getFlupDateStr()).toString()));*/
        int num = chrVisitDao.update(chrVisit);
        //是否有下次随访，如果有，插入新的随访表信息。没有不插入
        String visitId = null;
        if (StringUtil.isNotEmpty(info.getNextFlupTimeStr()) && info.getNextFlupTimeStr() != null)
        {
            Visit addVisit = new Visit();
            addVisit.setCustId(info.getCustId());
            addVisit.setDoctorId(info.getDoctorId());
            addVisit.setType(ConstChr.CHR_TYPE_XT);
            //下次随访时间转换
            if (info.getNextFlupTimeStr().length() > 10)
            {
                String nextFlupTimeStr = info.getNextFlupTimeStr();
                nextFlupTimeStr = nextFlupTimeStr + " 000";
                addVisit.setPlanFlupTime(DateUtil.getSynTimeByStr(nextFlupTimeStr));
            }
            else
            {
                String nextFlupTimeStr = info.getNextFlupTimeStr();
                nextFlupTimeStr = nextFlupTimeStr + " 00:00:00";
                addVisit.setPlanFlupTime(DateUtil.getTimeByStr(nextFlupTimeStr));

            }

            addVisit.setCreateBy(info.getDoctorId());
            addVisit.setUpdateBy(info.getDoctorId());
            addVisit.setCreateTime(now);
            addVisit.setUpdateTime(now);
            String visitNo = (now.toString()).replace("-", "");
            String visitNo2 = visitNo.replace(":", "");
            String visitNo3 = visitNo2.replace(" ", "");
            String visitNo4 = visitNo3.replace(".", "");
            addVisit.setVisitNo(visitNo4);
            addVisit.setIshandled(ConstChr.CHR_ISHANDLED_NOT);
            addVisit.setStatus(ConstChr.CHR_STATUS_YES);
            visitId = chrVisitDao.insert(addVisit);
        }
        //保存与血压记录表相关的药物信息
        List<?> checks = info.getGlyCheck();
        if (!(checks == null || checks.size() < 1))
        {
            for (Object spobj : checks)
            {

                GlyCheck sp = (GlyCheck) spobj;
                sp.setbId(info.getId());
                sp.setHbpCheckType(2);
                glyCheckDao.insert(sp);
            }
        }
        List<?> drugs = info.getGlyDrug();
        if (!(drugs == null || drugs.size() < 1))
        {
            for (Object dgobj : drugs)
            {

                GlyDrug dg = (GlyDrug) dgobj;
                dg.setBId(info.getId());
                glyDrugDao.insert(dg);
            }
        }
        List<?> sports = info.getGlySport();
        if (!(sports == null || sports.size() < 1))
        {
            for (Object spobj : sports)
            {

                GlySport sp = (GlySport) spobj;
                sp.setBId(info.getId());
                glySportDao.insert(sp);
            }
        }
        if (StringUtil.isNotEmpty(id) && num > 0)
        {
            return new Result(Result.STATE_SUCESS, "保存成功", id);
        }
        else
        {
            return new Result(Result.STATE_FAIL, "随访更新出错或是血糖保存失败", id);
        }
    }
}
