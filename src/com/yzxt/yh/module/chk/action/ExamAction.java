package com.yzxt.yh.module.chk.action;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.chk.bean.Exam;
import com.yzxt.yh.module.chk.bean.ExamFamilyHosHis;
import com.yzxt.yh.module.chk.bean.ExamHospitalHis;
import com.yzxt.yh.module.chk.bean.ExamInoculate;
import com.yzxt.yh.module.chk.bean.ExamMedic;
import com.yzxt.yh.module.chk.bean.ExamPosion;
import com.yzxt.yh.module.chk.bean.PhysiologIndex;
import com.yzxt.yh.module.chk.service.ExamService;
import com.yzxt.yh.module.chk.service.PhysiologIndexService;
import com.yzxt.yh.module.sys.bean.Customer;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.service.CustomerService;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;
import common.Logger;

/**
 * 
 * 封装所有健康体检相关方法类
 *
 */
public class ExamAction extends BaseAction
{
    private static final long serialVersionUID = 1L;

    private static Logger logger = Logger.getLogger(ExamAction.class);

    private ExamService examService;

    private PhysiologIndexService physiologIndexService;

    private CustomerService customerService;

    private Exam exam;

    public ExamService getExamService()
    {
        return examService;
    }

    public void setExamService(ExamService examService)
    {
        this.examService = examService;
    }

    public PhysiologIndexService getPhysiologIndexService()
    {
        return physiologIndexService;
    }

    public void setPhysiologIndexService(PhysiologIndexService physiologIndexService)
    {
        this.physiologIndexService = physiologIndexService;
    }

    public CustomerService getCustomerService()
    {
        return customerService;
    }

    public void setCustomerService(CustomerService customerService)
    {
        this.customerService = customerService;
    }

    public Exam getExam()
    {
        return exam;
    }

    public void setExam(Exam exam)
    {
        this.exam = exam;
    }

    /**
      * 获得体检健康表详细
      * @return
      */
    public String toExam()
    {
        String operType = request.getParameter("operType");
        String id = request.getParameter("id");
        try
        {
            Exam exam = null;
            Customer customer = null;
            if ("view".equals(operType) || "update".equals(operType))
            {
                exam = examService.load(id);
                customer = customerService.load(exam.getCustId());
            }
            else
            {
                exam = new Exam();
                // 客户信息
                String custId = request.getParameter("custId");
                exam.setCustId(custId);
                // 补充客户其它信息
                customer = customerService.load(custId);
                exam.setIdCard(customer.getUser().getIdCard());
                exam.setName(customer.getUser().getName());
                exam.setExamDate(new Date());
                // 生理指标
                PhysiologIndex pslIdx = physiologIndexService.get(custId);
                if (pslIdx != null)
                {
                    exam.setTemperature(pslIdx.getTemperature());
                    exam.setPulseRate(pslIdx.getPulse());
                    exam.setHeight(pslIdx.getHeight() != null ? (int) (pslIdx.getHeight().doubleValue() * 100d) : null);
                    exam.setWeight(pslIdx.getWeight());
                    // 默认右侧收缩压
                    exam.setRBloodPressure(pslIdx.getSbp());
                    // 默认右侧舒张压
                    exam.setRBloodPressureBpd(pslIdx.getDbp());
                    exam.setFastingGlucose(pslIdx.getFpg());
                }
            }
            // 设置数据
            request.setAttribute("exam", exam);
            request.setAttribute("cust", customer);
            request.setAttribute("operType", operType);
            if ("add".equals(operType) || "update".equals(operType))
            {
                return "examEdit";
            }
            else
            {
                return "examView";
            }
        }
        catch (Exception e)
        {
            logger.error("跳转到体检表失败，operType=" + operType + ",id=" + id, e);
            return null;
        }
    }

    /**
     * 保存体检表
     */
    public void save()
    {
        String msg = "";
        int state = Result.STATE_UNKNOWN;
        String id = exam.getId();
        User operUser = (User) super.getCurrentUser();
        try
        {
            // 数据处理
            // 基本信息
            exam.setIndustryName(StringUtil.trim(exam.getIndustryName()));
            // 症状
            String[] symptoms = request.getParameterValues("symptomItem");
            StringBuilder symptom = new StringBuilder();
            if (symptoms != null && symptoms.length > 0)
            {
                for (int i = 0; i < symptoms.length; i++)
                {
                    if (StringUtil.isNotEmpty(symptoms[i]))
                    {
                        symptom.append(symptoms[i]).append(";");
                    }
                }
            }
            if (StringUtil.isNotEmpty(exam.getOtherSymptom()))
            {
                symptom.append("25;");
            }
            exam.setSymptom(symptom.toString());
            // 一般状况
            if (exam.getHeight() != null && exam.getHeight().intValue() > 0 && exam.getWeight() != null
                    && exam.getWeight().doubleValue() > 0)
            {
                double bmi = exam.getWeight().doubleValue() * 10000d / exam.getHeight().doubleValue()
                        / exam.getHeight().doubleValue();
                exam.setBmi(bmi <= 999.9 ? bmi : null);
            }
            // 生活方式
            String dietStatus = StringUtil.ensureStringNotNull(exam.getDietStatus());
            String[] dietStatusOthers = request.getParameterValues("dietStatusOther");
            if (dietStatusOthers != null && dietStatusOthers.length > 0)
            {
                for (String dietStatusOther : dietStatusOthers)
                {
                    dietStatus += ";" + dietStatusOther;
                }
            }
            exam.setDietStatus(dietStatus);
            String[] drinkTypes = request.getParameterValues("drinkType");
            StringBuilder drinkType = new StringBuilder();
            if (drinkTypes != null && drinkTypes.length > 0)
            {
                for (int i = 0; i < drinkTypes.length; i++)
                {
                    if (StringUtil.isNotEmpty(drinkTypes[i]))
                    {
                        drinkType.append(drinkTypes[i]).append(";");
                    }
                }
            }
            if (StringUtil.isNotEmpty(exam.getOtherDrinkType()))
            {
                symptom.append("5;");
            }
            exam.setDrinkType(drinkType.toString());
            // 查体
            String mammaryGland = exam.getMammaryGland();
            if (StringUtil.isEmpty(mammaryGland))
            {
                String[] mammaryGlandMs = request.getParameterValues("mammaryGlandM");
                StringBuilder mammaryGlandBd = new StringBuilder();
                if (mammaryGlandMs != null)
                {
                    for (String mammaryGlandM : mammaryGlandMs)
                    {
                        mammaryGlandBd.append(mammaryGlandM).append(";");
                    }
                }
                exam.setMammaryGland(mammaryGlandBd.toString());
            }
            // 现存主要健康问题
            String[] cerebraveDiseases = request.getParameterValues("cerebraveDiseaseItem");
            StringBuilder cerebraveDisease = new StringBuilder();
            if (cerebraveDiseases != null && cerebraveDiseases.length > 0)
            {
                for (int i = 0; i < cerebraveDiseases.length; i++)
                {
                    if (StringUtil.isNotEmpty(cerebraveDiseases[i]))
                    {
                        cerebraveDisease.append(cerebraveDiseases[i]).append(";");
                    }
                }
            }
            if (StringUtil.isNotEmpty(exam.getOtherCereDisease()))
            {
                symptom.append("6;");
            }
            exam.setCerebraveDisease(cerebraveDisease.toString());
            String[] renalDiseases = request.getParameterValues("renalDiseaseItem");
            StringBuilder renalDisease = new StringBuilder();
            if (renalDiseases != null && renalDiseases.length > 0)
            {
                for (int i = 0; i < renalDiseases.length; i++)
                {
                    if (StringUtil.isNotEmpty(renalDiseases[i]))
                    {
                        renalDisease.append(renalDiseases[i]).append(";");
                    }
                }
            }
            if (StringUtil.isNotEmpty(exam.getOtherRenalDisease()))
            {
                renalDisease.append("6;");
            }
            exam.setRenalDisease(renalDisease.toString());
            String[] heartDiseases = request.getParameterValues("heartDiseaseItem");
            StringBuilder heartDisease = new StringBuilder();
            if (heartDiseases != null && heartDiseases.length > 0)
            {
                for (int i = 0; i < heartDiseases.length; i++)
                {
                    if (StringUtil.isNotEmpty(heartDiseases[i]))
                    {
                        heartDisease.append(heartDiseases[i]).append(";");
                    }
                }
            }
            if (StringUtil.isNotEmpty(exam.getOtherHeartDisease()))
            {
                heartDisease.append("7;");
            }
            exam.setHeartDisease(heartDisease.toString());
            String[] vascularDiseases = request.getParameterValues("vascularDiseaseItem");
            StringBuilder vascularDisease = new StringBuilder();
            if (vascularDiseases != null && vascularDiseases.length > 0)
            {
                for (int i = 0; i < vascularDiseases.length; i++)
                {
                    if (StringUtil.isNotEmpty(vascularDiseases[i]))
                    {
                        vascularDisease.append(vascularDiseases[i]).append(";");
                    }
                }
            }
            if (StringUtil.isNotEmpty(exam.getOtherVascularDisease()))
            {
                vascularDisease.append("7;");
            }
            exam.setVascularDisease(vascularDisease.toString());
            String[] eyeDiseases = request.getParameterValues("eyeDiseaseItem");
            StringBuilder eyeDisease = new StringBuilder();
            if (eyeDiseases != null && eyeDiseases.length > 0)
            {
                for (int i = 0; i < eyeDiseases.length; i++)
                {
                    if (StringUtil.isNotEmpty(eyeDiseases[i]))
                    {
                        eyeDisease.append(eyeDiseases[i]).append(";");
                    }
                }
            }
            if (StringUtil.isNotEmpty(exam.getOtherEyeDisease()))
            {
                eyeDisease.append("7;");
            }
            exam.setEyeDisease(eyeDisease.toString());
            // 健康指导
            String[] healthyDirects = request.getParameterValues("healthyDirectItem");
            StringBuilder healthyDirect = new StringBuilder();
            if (healthyDirects != null && healthyDirects.length > 0)
            {
                for (int i = 0; i < healthyDirects.length; i++)
                {
                    if (StringUtil.isNotEmpty(healthyDirects[i]))
                    {
                        healthyDirect.append(healthyDirects[i]).append(";");
                    }
                }
            }
            exam.setHealthyDirect(healthyDirect.toString());
            String[] dangerControls = request.getParameterValues("dangerControlItem");
            StringBuilder dangerControl = new StringBuilder();
            if (dangerControls != null && dangerControls.length > 0)
            {
                for (int i = 0; i < dangerControls.length; i++)
                {
                    if (StringUtil.isNotEmpty(dangerControls[i]))
                    {
                        dangerControl.append(dangerControls[i]).append(";");
                    }
                }
            }
            if (StringUtil.isNotEmpty(exam.getDangerControlOther()))
            {
                dangerControl.append("7;");
            }
            exam.setDangerControl(dangerControl.toString());
            // 住院史
            String[] inTimes = request.getParameterValues("ipInTime");
            String[] outTimes = request.getParameterValues("ipOutTime");
            String[] admissionReasons = request.getParameterValues("ipAdmissionReason");
            String[] hosDepts = request.getParameterValues("ipHosDept");
            String[] recordNos = request.getParameterValues("ipRecordNo");
            int ipLen = inTimes != null ? inTimes.length : 0;
            List<ExamHospitalHis> ips = new ArrayList<ExamHospitalHis>();
            for (int i = 0; i < ipLen; i++)
            {
                if (StringUtil.isEmpty(inTimes[i]) && StringUtil.isEmpty(outTimes[i])
                        && StringUtil.isEmpty(admissionReasons[i]) && StringUtil.isEmpty(hosDepts[i])
                        && StringUtil.isEmpty(recordNos[i]))
                {
                    continue;
                }
                ExamHospitalHis ip = new ExamHospitalHis();
                ip.setInTime(StringUtil.isNotEmpty(inTimes[i]) ? new Timestamp(DateUtil.getDateFromHtml(inTimes[i])
                        .getTime()) : null);
                ip.setOutTime(StringUtil.isNotEmpty(outTimes[i]) ? new Timestamp(DateUtil.getDateFromHtml(outTimes[i])
                        .getTime()) : null);
                ip.setAdmissionReason(admissionReasons[i]);
                ip.setHosDept(hosDepts[i]);
                ip.setRecordNo(recordNos[i]);
                ips.add(ip);
            }
            exam.setExamHospitalHiss(ips);
            // 家庭病床史
            String[] userNames = request.getParameterValues("fhUserName");
            String[] createBedTimes = request.getParameterValues("fhCreateBedTime");
            String[] putBedTimes = request.getParameterValues("fhPutBedTime");
            String[] reasons = request.getParameterValues("fhReason");
            String[] hosUnits = request.getParameterValues("fhHosUnit");
            String[] medRecordNos = request.getParameterValues("fhMedRecordNo");
            int fhLen = userNames != null ? userNames.length : 0;
            List<ExamFamilyHosHis> fhs = new ArrayList<ExamFamilyHosHis>();
            for (int i = 0; i < fhLen; i++)
            {
                if (StringUtil.isEmpty(userNames[i]) && StringUtil.isEmpty(createBedTimes[i])
                        && StringUtil.isEmpty(putBedTimes[i]) && StringUtil.isEmpty(reasons[i])
                        && StringUtil.isEmpty(hosUnits[i]) && StringUtil.isEmpty(medRecordNos[i]))
                {
                    continue;
                }
                ExamFamilyHosHis fh = new ExamFamilyHosHis();
                fh.setUserName(userNames[i]);
                fh.setCreateBedTime(StringUtil.isNotEmpty(createBedTimes[i]) ? new Timestamp(DateUtil.getDateFromHtml(
                        createBedTimes[i]).getTime()) : null);
                fh.setPutBedTime(StringUtil.isNotEmpty(putBedTimes[i]) ? new Timestamp(DateUtil.getDateFromHtml(
                        putBedTimes[i]).getTime()) : null);
                fh.setReason(reasons[i]);
                fh.setHosUnit(hosUnits[i]);
                fh.setMedRecordNo(medRecordNos[i]);
                fhs.add(fh);
            }
            exam.setExamFamilyHosHiss(fhs);
            // 主要用药情况
            String[] medNames = request.getParameterValues("medMedName");
            String[] useTypes = request.getParameterValues("medUseType");
            String[] useNums = request.getParameterValues("medUseNum");
            String[] useTimes = request.getParameterValues("medUseTime");
            String[] adhess = request.getParameterValues("medAdhes");
            int medLen = medNames != null ? medNames.length : 0;
            List<ExamMedic> meds = new ArrayList<ExamMedic>();
            for (int i = 0; i < medLen; i++)
            {
                if (StringUtil.isEmpty(medNames[i]) && StringUtil.isEmpty(useTypes[i])
                        && StringUtil.isEmpty(useNums[i]) && StringUtil.isEmpty(useTimes[i])
                        && StringUtil.isEmpty(adhess[i]))
                {
                    continue;
                }
                ExamMedic med = new ExamMedic();
                med.setMedName(medNames[i]);
                med.setUseType(useTypes[i]);
                med.setUseNum(useNums[i]);
                med.setUseTime(useTimes[i]);
                med.setAdhes(StringUtil.isNotEmpty(adhess[i]) ? Integer.valueOf(adhess[i]) : null);
                meds.add(med);
            }
            exam.setExamMedics(meds);
            // 非免疫规划预防接种史
            String[] inoculateNames = request.getParameterValues("inoInoculateName");
            String[] inoculateTimes = request.getParameterValues("inoInoculateTime");
            String[] inoculateDepts = request.getParameterValues("inoInoculateDept");
            int inoLen = inoculateNames != null ? inoculateNames.length : 0;
            List<ExamInoculate> inos = new ArrayList<ExamInoculate>();
            for (int i = 0; i < inoLen; i++)
            {
                if (StringUtil.isEmpty(inoculateNames[i]) && StringUtil.isEmpty(inoculateTimes[i])
                        && StringUtil.isEmpty(inoculateDepts[i]))
                {
                    continue;
                }
                ExamInoculate ino = new ExamInoculate();
                ino.setInoculateName(inoculateNames[i]);
                ino.setInoculateTime(StringUtil.isNotEmpty(inoculateTimes[i]) ? new Timestamp(DateUtil.getDateFromHtml(
                        inoculateTimes[i]).getTime()) : null);
                ino.setInoculateDept(inoculateDepts[i]);
                inos.add(ino);
            }
            exam.setExamInoculates(inos);
            // 非免疫规划预防接种史
            String[] posionNames = request.getParameterValues("posPosionName");
            String[] workTimes = request.getParameterValues("posWorkTime");
            String[] safeguards = request.getParameterValues("posSafeguard");
            int posLen = posionNames != null ? posionNames.length : 0;
            List<ExamPosion> poss = new ArrayList<ExamPosion>();
            for (int i = 0; i < posLen; i++)
            {
                if (StringUtil.isEmpty(posionNames[i]) && StringUtil.isEmpty(workTimes[i])
                        && StringUtil.isEmpty(safeguards[i]))
                {
                    continue;
                }
                ExamPosion pos = new ExamPosion();
                pos.setPosionName(posionNames[i]);
                pos.setWorkTime(StringUtil.isNotEmpty(workTimes[i]) ? Double.valueOf(workTimes[i]) : null);
                pos.setSafeguard(safeguards[i]);
                poss.add(pos);
            }
            exam.setExamPosions(poss);
            // 保存数据
            if (StringUtil.isEmpty(id))
            {
                exam.setCreateBy(operUser.getId());
                id = examService.add(exam);
                state = Result.STATE_SUCESS;
            }
            else
            {
                exam.setUpdateBy(operUser.getId());
                id = examService.update(exam);
                state = Result.STATE_SUCESS;
            }
        }
        catch (Exception e)
        {
            state = Result.STATE_FAIL;
            msg = "保存体检表失败。";
            logger.error("保存体检表失败。", e);
        }
        super.write(new Result(state, msg, id));
    }

    /**
     * 跳转到客户体检数据列表
     * @return
     */
    public String toCustExams()
    {
        String custId = request.getParameter("custId");
        try
        {
            User operUser = (User) super.getCurrentUser();
            if (StringUtil.isEmpty(custId) && Constant.USER_TYPE_CUSTOMER.equals(operUser.getType()))
            {
                custId = operUser.getId();
            }
            request.setAttribute("cust", customerService.load(custId));
            request.setAttribute("editable", operUser != null && operUser.getId().equals(custId));
            return "custExams";
        }
        catch (Exception e)
        {
            logger.error("跳转到客户体检记录列表败，客户ID：" + custId);
            return null;
        }
    }

    /**
     * 查询客户体检记录
     */
    public void queryCustExam()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("custId", request.getParameter("custId"));
            filter.put("startExamDate", DateUtil.getDateFromHtml(request.getParameter("startExamDate")));
            filter.put("endExamDate", DateUtil.getDateFromHtml(request.getParameter("endExamDate")));
            filter.put("examNo", request.getParameter("examNo"));
            // 查询人
            filter.put("operUser", super.getCurrentUser());
            PageModel<Exam> pageModel = examService.queryCustExam(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询客户检测记录错误。", e);
        }
    }

    /**
      * 保存体检表
      */
    /*
    public void savePhysicalTable()
    {
     String msg = "";
     int state = Result.STATE_UNKNOWN;
     String id = healthyExam.getId();
     try
     {
         // 数据处理
         // 基本信息
         healthyExam.setIndustryName(StringUtil.trim(healthyExam.getIndustryName()));
         // 症状
         String[] symptoms = request.getParameterValues("symptomItem");
         StringBuilder symptom = new StringBuilder();
         if (symptoms != null && symptoms.length > 0)
         {
             for (int i = 0; i < symptoms.length; i++)
             {
                 if (StringUtil.isNotEmpty(symptoms[i]))
                 {
                     symptom.append(symptoms[i]).append(";");
                 }
             }
         }
         if (StringUtil.isNotEmpty(healthyExam.getOtherSymptom()))
         {
             symptom.append("25;");
         }
         healthyExam.setSymptom(symptom.toString());
         // 一般状况
         if (healthyExam.getHeight() != null && healthyExam.getHeight().intValue() > 0
                 && healthyExam.getWeight() != null && healthyExam.getWeight().doubleValue() > 0)
         {
             double bmi = healthyExam.getWeight().doubleValue() * 10000d
                     / healthyExam.getHeight().doubleValue() / healthyExam.getHeight().doubleValue();
             healthyExam.setBmi(bmi <= 999.9 ? bmi : null);
         }
         // 生活方式
         String dietStatus = StringUtil.ensureStringNotNull(healthyExam.getDietStatus());
         String[] dietStatusOthers = request.getParameterValues("dietStatusOther");
         if (dietStatusOthers != null && dietStatusOthers.length > 0)
         {
             for (String dietStatusOther : dietStatusOthers)
             {
                 dietStatus += ";" + dietStatusOther;
             }
         }
         healthyExam.setDietStatus(dietStatus);
         String[] drinkTypes = request.getParameterValues("drinkType");
         StringBuilder drinkType = new StringBuilder();
         if (drinkTypes != null && drinkTypes.length > 0)
         {
             for (int i = 0; i < drinkTypes.length; i++)
             {
                 if (StringUtil.isNotEmpty(drinkTypes[i]))
                 {
                     drinkType.append(drinkTypes[i]).append(";");
                 }
             }
         }
         if (StringUtil.isNotEmpty(healthyExam.getOtherDrinkType()))
         {
             symptom.append("5;");
         }
         healthyExam.setDrinkType(drinkType.toString());
         // 查体
         String mammaryGland = healthyExam.getMammaryGland();
         if (StringUtil.isEmpty(mammaryGland))
         {
             String[] mammaryGlandMs = request.getParameterValues("mammaryGlandM");
             StringBuilder mammaryGlandBd = new StringBuilder();
             if (mammaryGlandMs != null)
             {
                 for (String mammaryGlandM : mammaryGlandMs)
                 {
                     mammaryGlandBd.append(mammaryGlandM).append(";");
                 }
             }
             healthyExam.setMammaryGland(mammaryGlandBd.toString());
         }
         // 现存主要健康问题
         String[] cerebraveDiseases = request.getParameterValues("cerebraveDiseaseItem");
         StringBuilder cerebraveDisease = new StringBuilder();
         if (cerebraveDiseases != null && cerebraveDiseases.length > 0)
         {
             for (int i = 0; i < cerebraveDiseases.length; i++)
             {
                 if (StringUtil.isNotEmpty(cerebraveDiseases[i]))
                 {
                     cerebraveDisease.append(cerebraveDiseases[i]).append(";");
                 }
             }
         }
         if (StringUtil.isNotEmpty(healthyExam.getOtherCereDisease()))
         {
             symptom.append("6;");
         }
         healthyExam.setCerebraveDisease(cerebraveDisease.toString());
         String[] renalDiseases = request.getParameterValues("renalDiseaseItem");
         StringBuilder renalDisease = new StringBuilder();
         if (renalDiseases != null && renalDiseases.length > 0)
         {
             for (int i = 0; i < renalDiseases.length; i++)
             {
                 if (StringUtil.isNotEmpty(renalDiseases[i]))
                 {
                     renalDisease.append(renalDiseases[i]).append(";");
                 }
             }
         }
         if (StringUtil.isNotEmpty(healthyExam.getOtherRenalDisease()))
         {
             renalDisease.append("6;");
         }
         healthyExam.setRenalDisease(renalDisease.toString());
         String[] heartDiseases = request.getParameterValues("heartDiseaseItem");
         StringBuilder heartDisease = new StringBuilder();
         if (heartDiseases != null && heartDiseases.length > 0)
         {
             for (int i = 0; i < heartDiseases.length; i++)
             {
                 if (StringUtil.isNotEmpty(heartDiseases[i]))
                 {
                     heartDisease.append(heartDiseases[i]).append(";");
                 }
             }
         }
         if (StringUtil.isNotEmpty(healthyExam.getOtherHeartDisease()))
         {
             heartDisease.append("7;");
         }
         healthyExam.setHeartDisease(heartDisease.toString());
         String[] vascularDiseases = request.getParameterValues("vascularDiseaseItem");
         StringBuilder vascularDisease = new StringBuilder();
         if (vascularDiseases != null && vascularDiseases.length > 0)
         {
             for (int i = 0; i < vascularDiseases.length; i++)
             {
                 if (StringUtil.isNotEmpty(vascularDiseases[i]))
                 {
                     vascularDisease.append(vascularDiseases[i]).append(";");
                 }
             }
         }
         if (StringUtil.isNotEmpty(healthyExam.getOtherVascularDisease()))
         {
             vascularDisease.append("7;");
         }
         healthyExam.setVascularDisease(vascularDisease.toString());
         String[] eyeDiseases = request.getParameterValues("eyeDiseaseItem");
         StringBuilder eyeDisease = new StringBuilder();
         if (eyeDiseases != null && eyeDiseases.length > 0)
         {
             for (int i = 0; i < eyeDiseases.length; i++)
             {
                 if (StringUtil.isNotEmpty(eyeDiseases[i]))
                 {
                     eyeDisease.append(eyeDiseases[i]).append(";");
                 }
             }
         }
         if (StringUtil.isNotEmpty(healthyExam.getOtherEyeDisease()))
         {
             eyeDisease.append("7;");
         }
         healthyExam.setEyeDisease(eyeDisease.toString());
         // 健康指导
         String[] healthyDirects = request.getParameterValues("healthyDirectItem");
         StringBuilder healthyDirect = new StringBuilder();
         if (healthyDirects != null && healthyDirects.length > 0)
         {
             for (int i = 0; i < healthyDirects.length; i++)
             {
                 if (StringUtil.isNotEmpty(healthyDirects[i]))
                 {
                     healthyDirect.append(healthyDirects[i]).append(";");
                 }
             }
         }
         healthyExam.setHealthyDirect(healthyDirect.toString());
         String[] dangerControls = request.getParameterValues("dangerControlItem");
         StringBuilder dangerControl = new StringBuilder();
         if (dangerControls != null && dangerControls.length > 0)
         {
             for (int i = 0; i < dangerControls.length; i++)
             {
                 if (StringUtil.isNotEmpty(dangerControls[i]))
                 {
                     dangerControl.append(dangerControls[i]).append(";");
                 }
             }
         }
         if (StringUtil.isNotEmpty(healthyExam.getDangerControlOther()))
         {
             dangerControl.append("7;");
         }
         healthyExam.setDangerControl(dangerControl.toString());
         // 住院史
         String[] inTimes = request.getParameterValues("ipInTime");
         String[] outTimes = request.getParameterValues("ipOutTime");
         String[] admissionReasons = request.getParameterValues("ipAdmissionReason");
         String[] hosDepts = request.getParameterValues("ipHosDept");
         String[] recordNos = request.getParameterValues("ipRecordNo");
         int ipLen = inTimes != null ? inTimes.length : 0;
         List<ExamHospitalHis> ips = new ArrayList<ExamHospitalHis>();
         for (int i = 0; i < ipLen; i++)
         {
             if (StringUtil.isEmpty(inTimes[i]) && StringUtil.isEmpty(outTimes[i])
                     && StringUtil.isEmpty(admissionReasons[i]) && StringUtil.isEmpty(hosDepts[i])
                     && StringUtil.isEmpty(recordNos[i]))
             {
                 continue;
             }
             ExamHospitalHis ip = new ExamHospitalHis();
             ip.setInTime(StringUtil.isNotEmpty(inTimes[i]) ? new Timestamp(DateUtil.getDateFromHtml(inTimes[i])
                     .getTime()) : null);
             ip.setOutTime(StringUtil.isNotEmpty(outTimes[i]) ? new Timestamp(DateUtil.getDateFromHtml(outTimes[i])
                     .getTime()) : null);
             ip.setAdmissionReason(admissionReasons[i]);
             ip.setHosDept(hosDepts[i]);
             ip.setRecordNo(recordNos[i]);
             ips.add(ip);
         }
         healthyExam.setExamHospitalHis(ips);
         // 家庭病床史
         String[] userNames = request.getParameterValues("fhUserName");
         String[] createBedTimes = request.getParameterValues("fhCreateBedTime");
         String[] putBedTimes = request.getParameterValues("fhPutBedTime");
         String[] reasons = request.getParameterValues("fhReason");
         String[] hosUnits = request.getParameterValues("fhHosUnit");
         String[] medRecordNos = request.getParameterValues("fhMedRecordNo");
         int fhLen = userNames != null ? userNames.length : 0;
         List<ExamFamilyHosHis> fhs = new ArrayList<ExamFamilyHosHis>();
         for (int i = 0; i < fhLen; i++)
         {
             if (StringUtil.isEmpty(userNames[i]) && StringUtil.isEmpty(createBedTimes[i])
                     && StringUtil.isEmpty(putBedTimes[i]) && StringUtil.isEmpty(reasons[i])
                     && StringUtil.isEmpty(hosUnits[i]) && StringUtil.isEmpty(medRecordNos[i]))
             {
                 continue;
             }
             ExamFamilyHosHis fh = new ExamFamilyHosHis();
             fh.setUserName(userNames[i]);
             fh.setCreateBedTime(StringUtil.isNotEmpty(createBedTimes[i]) ? new Timestamp(DateUtil.getDateFromHtml(
                     createBedTimes[i]).getTime()) : null);
             fh.setPutBedTime(StringUtil.isNotEmpty(putBedTimes[i]) ? new Timestamp(DateUtil.getDateFromHtml(
                     putBedTimes[i]).getTime()) : null);
             fh.setReason(reasons[i]);
             fh.setHosUnit(hosUnits[i]);
             fh.setMedRecordNo(medRecordNos[i]);
             fhs.add(fh);
         }
         healthyExam.setExamFamilyHosHis(fhs);
         // 主要用药情况
         String[] medNames = request.getParameterValues("medMedName");
         String[] useTypes = request.getParameterValues("medUseType");
         String[] useNums = request.getParameterValues("medUseNum");
         String[] useTimes = request.getParameterValues("medUseTime");
         String[] adhess = request.getParameterValues("medAdhes");
         int medLen = medNames != null ? medNames.length : 0;
         List<ExamMedic> meds = new ArrayList<ExamMedic>();
         for (int i = 0; i < medLen; i++)
         {
             if (StringUtil.isEmpty(medNames[i]) && StringUtil.isEmpty(useTypes[i])
                     && StringUtil.isEmpty(useNums[i]) && StringUtil.isEmpty(useTimes[i])
                     && StringUtil.isEmpty(adhess[i]))
             {
                 continue;
             }
             ExamMedic med = new ExamMedic();
             med.setMedName(medNames[i]);
             med.setUseType(useTypes[i]);
             med.setUseNum(useNums[i]);
             med.setUseTime(useTimes[i]);
             med.setAdhes(StringUtil.isNotEmpty(adhess[i]) ? Integer.valueOf(adhess[i]) : null);
             meds.add(med);
         }
         healthyExam.setExamMedic(meds);
         // 非免疫规划预防接种史
         String[] inoculateNames = request.getParameterValues("inoInoculateName");
         String[] inoculateTimes = request.getParameterValues("inoInoculateTime");
         String[] inoculateDepts = request.getParameterValues("inoInoculateDept");
         int inoLen = inoculateNames != null ? inoculateNames.length : 0;
         List<ExamInoculate> inos = new ArrayList<ExamInoculate>();
         for (int i = 0; i < inoLen; i++)
         {
             if (StringUtil.isEmpty(inoculateNames[i]) && StringUtil.isEmpty(inoculateTimes[i])
                     && StringUtil.isEmpty(inoculateDepts[i]))
             {
                 continue;
             }
             ExamInoculate ino = new ExamInoculate();
             ino.setInoculateName(inoculateNames[i]);
             ino.setInoculateTime(StringUtil.isNotEmpty(inoculateTimes[i]) ? new Timestamp(DateUtil.getDateFromHtml(
                     inoculateTimes[i]).getTime()) : null);
             ino.setInoculateDept(inoculateDepts[i]);
             inos.add(ino);
         }
         healthyExam.setExamInoculate(inos);
         // 非免疫规划预防接种史
         String[] posionNames = request.getParameterValues("posPosionName");
         String[] workTimes = request.getParameterValues("posWorkTime");
         String[] safeguards = request.getParameterValues("posSafeguard");
         int posLen = posionNames != null ? posionNames.length : 0;
         List<ExamPosion> poss = new ArrayList<ExamPosion>();
         for (int i = 0; i < posLen; i++)
         {
             if (StringUtil.isEmpty(posionNames[i]) && StringUtil.isEmpty(workTimes[i])
                     && StringUtil.isEmpty(safeguards[i]))
             {
                 continue;
             }
             ExamPosion pos = new ExamPosion();
             pos.setPosionName(posionNames[i]);
             pos.setWorkTime(StringUtil.isNotEmpty(workTimes[i]) ? Double.valueOf(workTimes[i]) : null);
             pos.setSafeguard(safeguards[i]);
             poss.add(pos);
         }
         healthyExam.setExamPosion(poss);
         // 保存数据
         if (StringUtil.isEmpty(id))
         {
             id = healthyService.saveHealthyTable(healthyExam);
             state = Result.STATE_SUCESS;
         }
         else
         {
             msg = healthyService.updateHealthyTable(healthyExam);
             state = StringUtil.isEmpty(msg) ? Result.STATE_SUCESS : Result.STATE_FAIL;
         }
     }
     catch (Exception e)
     {
         state = Result.STATE_FAIL;
         msg = "保存体检表失败.";
         logger.error("保存体检表失败.", e);
     }
     super.outPrintData(new Result(state, msg, id));
    }

    *//**
      * 获得体检表子表住院史列表
      * @return
      */
    /*
    public String getHosHisByExamId(){
    
    String examId = getRequest().getParameter("examId");
    
    String res = "";
    
    try {
        PageModel<ExamHospitalHis> pageBean = healthyService.getHosList(examId, this.getPage(), this.getRows(), null, null);
        res = this.getJSON(pageBean, true);
    } catch (Exception e) {
        res = "{\"total\":0,\"rows\":[]}";
        e.printStackTrace();
    }
    super.write(res);
    return null;
    
    }
    
    *//**
      * 获得家庭住院史列表
      * @return
      */
    /*
    public String getFamilyHisByExamId(){
    
    String examId = getRequest().getParameter("examId");
    
    String res = "";
    
    try {
        PageModel<ExamFamilyHosHis> pageBean = healthyService.getFamilyHosList(examId, this.getPage(), this.getRows(), null, null);
        res = this.getJSON(pageBean, true);
    } catch (Exception e) {
        res = "{\"total\":0,\"rows\":[]}";
        e.printStackTrace();
    }
    super.write(res);
    return null;
    
    }
    
    *//**
      * 获得接种史列表
      * @return
      */
    /*
    public String getInoculateByExamId(){
    
    String examId = getRequest().getParameter("examId");
    
    String res = "";
    
    try {
        PageModel<ExamInoculate> pageBean = healthyService.getInoculateList(examId, this.getPage(), this.getRows(), null, null);
        res = this.getJSON(pageBean, true);
    } catch (Exception e) {
        res = "{\"total\":0,\"rows\":[]}";
        e.printStackTrace();
    }
    super.write(res);
    return null;
    
    }
    
    *//**
      * 获得药物史列表
      * @return
      */
    /*
    public String getMedicByExamId(){
    
    String examId = getRequest().getParameter("examId");
    
    String res = "";
    
    try {
        PageModel<ExamMedic> pageBean = healthyService.getMedicList(examId, this.getPage(), this.getRows(), null, null);
        res = this.getJSON(pageBean, true);
    } catch (Exception e) {
        res = "{\"total\":0,\"rows\":[]}";
        e.printStackTrace();
    }
    super.write(res);
    return null;
    
    }
    
    *//**
      * 毒物接触史列表
      * @return
      */
    /*
    public String getPosionByExamId(){
    
    String examId = getRequest().getParameter("examId");
    
    String res = "";
    
    try {
        PageModel<ExamPosion> pageBean = healthyService.getPosionList(examId, this.getPage(), this.getRows(), null, null);
        res = this.getJSON(pageBean, true);
    } catch (Exception e) {
        res = "{\"total\":0,\"rows\":[]}";
        e.printStackTrace();
    }
    super.write(res);
    return null;
    
    }
    
    *//**
      * 报告跳转
      * @return
      */
    /*
    public String getReport(){
    
            生成分析报告
     *      1.根据页面传回的reportType(1.血压 2.血糖 3.跳转血压打印 4.跳转血糖打印)和时间区间（最近一周1，最近一个月2）、患者idCard
     *      3.传参数，根据type跳转到不同报告页面
     *  
    
    JSONObject jsonObj = JSONObject.fromObject(params);
    
    String reportType = jsonObj.getString("reportType");
    String idCard = jsonObj.getString("idCard");
    String rid = jsonObj.getString("reportId");
    
    String forward = getRequest().getParameter("forward");
    
    getRequest().setAttribute("params", jsonObj.toString());
    try {
        customer = customerService.getResidentDetail(idCard);
         if (customer != null)
         {
             customer.setAge(DateUtil.getAge(customer.getBirthday(), null));
         }
        if(rid!=null &&rid.trim().length()>0){
             healthyReport = reportService.getHealthyReportById(rid);
        }else{
            healthyReport = null;
        }
        
        if(forward==null){
            if(reportType.equals("1")){
                // 血压报告
                return "toXyReport";
            }else if(reportType.equals("2")){
                // 血糖报告
                return "toXtReport";
            }
        }else{
            if(forward.equals("1")){
                return "toXyPrint";
            }else if(forward.equals("2")){
                return "toXtPrint";
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
    }
    
    *//**
      * 保存健康报告
      * @return
      */
    /*
    public String saveReport(){
     User user = this.getCurrentUser();

     String beginTime = getRequest().getParameter("examBeginTime");
     String endTime = getRequest().getParameter("examEndTime");

     String msg = "0";

     Date sdate = CommonUtil.olineStrToDate(beginTime);
     Date edate = CommonUtil.olineStrToDate(endTime);

     healthyReport.setExamBeginTime(edate);
     healthyReport.setExamEndTime(sdate);
     healthyReport.setCreateTime(sdate);
     healthyReport.setCreateId(user.getId());

     try
     {
         reportService.saveReport(healthyReport);

         msg = "1";
     }
     catch (Exception e)
     {
         e.printStackTrace();
     }
     super.write(msg);
     return null;
    }
    
    *//**
      * 健康报告列表
      * @return
      */
    /*
    public String getReportList(){
    
    String idCard = getRequest().getParameter("idCard");
    String type = getRequest().getParameter("reportType");
    
    if(type==null){
        type ="1";
    }
    
    try {
        PageModel<HealthyReport> reports = reportService.getHealthyListByIdCard(idCard,Integer.parseInt(type),this.getPage(), this.getRows(),null,null);
        String json = this.getJSON(reports, true);
        super.write(json);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
    }
    
    */

}
