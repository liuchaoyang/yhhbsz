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
import com.yzxt.yh.module.chr.bean.PreDrug;
import com.yzxt.yh.module.chr.bean.PreSport;
import com.yzxt.yh.module.chr.bean.Pressure;
import com.yzxt.yh.module.chr.bean.Visit;
import com.yzxt.yh.module.chr.dao.ChrPreDao;
import com.yzxt.yh.module.chr.dao.ChrVisitDao;
import com.yzxt.yh.module.chr.dao.PreDrugDao;
import com.yzxt.yh.module.chr.dao.PreSportDao;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;

@Transactional(ConstTM.DEFAULT)
public class ChrPreService
{
    private ChrPreDao chrPreDao;

    private PreDrugDao preDrugDao;

    private PreSportDao preSportDao;

    private ChrVisitDao chrVisitDao;

    public ChrPreDao getChrPreDao()
    {
        return chrPreDao;
    }

    public void setChrPreDao(ChrPreDao chrPreDao)
    {
        this.chrPreDao = chrPreDao;
    }

    public PreDrugDao getPreDrugDao()
    {
        return preDrugDao;
    }

    public void setPreDrugDao(PreDrugDao preDrugDao)
    {
        this.preDrugDao = preDrugDao;
    }

    public PreSportDao getPreSportDao()
    {
        return preSportDao;
    }

    public void setPreSportDao(PreSportDao preSportDao)
    {
        this.preSportDao = preSportDao;
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
     * 通过随访id得到血压信息
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Pressure getPreInfoByVisitId(String id)
    {
        return chrPreDao.getPreInfoByVisitId(id);
    }

    /**
     * 通过血压记录表id得到与血压记录表相关的药物信息
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<PreDrug> getDrugsByPreId(String id)
    {
        return preDrugDao.getDrugsByPreId(id);
    }

    /**
     * 通过血压记录表id得到与血压记录表相关的运动信息
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<PreSport> getSportsByPreId(String id)
    {
        return preSportDao.getSportsByPreId(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Result savePreInfo(Pressure info) throws Exception
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
        String id = chrPreDao.insert(info);
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
        int num = chrVisitDao.update(chrVisit);
        //是否有下次随访，如果有，插入新的随访表信息。没有不插入
        String visitId = null;
        if (StringUtil.isNotEmpty(info.getNextFlupTimeStr()) && info.getNextFlupTimeStr() != null)
        {
            Visit addVisit = new Visit();
            addVisit.setCustId(info.getCustId());
            addVisit.setDoctorId(info.getDoctorId());
            addVisit.setType(ConstChr.CHR_TYPE_XY);

            String nextFlupTimeStr = info.getNextFlupTimeStr();
            if (nextFlupTimeStr != null && nextFlupTimeStr.length() == 10)
            {
                nextFlupTimeStr += " 00:00:00";
            }
            addVisit.setPlanFlupTime(nextFlupTimeStr != null && nextFlupTimeStr.length() > 0 ? Timestamp
                    .valueOf(nextFlupTimeStr) : null);
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
        String preDrugId = null;
        if (!(info.getDrugs() == null || info.getDrugs().equals("")))
        {
            JSONObject drugs = JSONObject.fromObject(info.getDrugs());
            JSONArray alArr = drugs.getJSONArray("drug");
            List<PreDrug> drugList = new ArrayList<PreDrug>();

            int size = alArr.size();
            for (int i = 0; i < size; i++)
            {
                JSONArray drugArr = alArr.getJSONArray(i);
                PreDrug drug = new PreDrug();
                int drgSize = drugArr.size();
                drug.setHId(info.getId());
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
                preDrugId = preDrugDao.insert(drug);
            }
        }
        /* if(info.getDrugs()!=null){
             
             PreDrug preDrug = new PreDrug();
             String preDrugId = preDrugDao.insert(preDrug);
         }*/
        //保存与血压记录表相关的运动信息
        String preSportId = null;
        if (!(info.getSports() == null || info.getSports().equals("")))
        {
            JSONObject sports = JSONObject.fromObject(info.getSports());
            JSONArray alArr = sports.getJSONArray("sport");
            List<PreSport> sportList = new ArrayList<PreSport>();
            int size = alArr.size();
            for (int i = 0; i < size; i++)
            {
                JSONArray sportArr = alArr.getJSONArray(i);
                PreSport sport = new PreSport();
                int drgSize = sportArr.size();
                sport.sethId(info.getId());
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
                preSportId = preSportDao.insert(sport);
            }
        }
        /*if(info.getPreSport()!=null){
            
            PreSport preSport = new PreSport();
            String preSportId = preSportDao.insert(preSport);
        }*/
        if (StringUtil.isNotEmpty(id) && num > 0)
        {
            return new Result(Result.STATE_SUCESS, "保存成功", id);
        }
        else
        {
            return new Result(Result.STATE_FAIL, "保存失败", id);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Result savePreInfoTran(Pressure info) throws Exception
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
        String id = chrPreDao.insert(info);
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
        int num = chrVisitDao.update(chrVisit);
        //是否有下次随访，如果有，插入新的随访表信息。没有不插入
        String visitId = null;
        if (StringUtil.isNotEmpty(info.getNextFlupTimeStr()) && info.getNextFlupTimeStr() != null)
        {
            Visit addVisit = new Visit();
            addVisit.setCustId(info.getCustId());
            addVisit.setDoctorId(info.getDoctorId());
            addVisit.setType(ConstChr.CHR_TYPE_XY);
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
        List<?> sports = info.getPreSport();
        if (!(sports == null || sports.size() < 1))
        {
            for (Object spobj : sports)
            {

                PreSport sp = (PreSport) spobj;
                sp.sethId(info.getId());
                preSportDao.insert(sp);
            }
        }
        List<?> drugs = info.getPreDrug();
        if (!(drugs == null || drugs.size() < 1))
        {
            for (Object dgobj : drugs)
            {
                PreDrug dg = (PreDrug) dgobj;
                dg.setHId(info.getId());
                preDrugDao.insert(dg);
            }
        }
        if (StringUtil.isNotEmpty(id) && num > 0)
        {
            return new Result(Result.STATE_SUCESS, "保存成功", id);
        }
        else
        {
            return new Result(Result.STATE_FAIL, "随访更新出错或是血压保存出错", id);
        }
    }

}
