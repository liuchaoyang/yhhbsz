package com.yzxt.tran;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.yzxt.fw.common.ResultTran;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.fw.util.GsonUtil;
import com.yzxt.yh.module.chk.bean.Exam;
import com.yzxt.yh.module.chk.bean.ExamFamilyHosHis;
import com.yzxt.yh.module.chk.bean.ExamHospitalHis;
import com.yzxt.yh.module.chk.bean.ExamInoculate;
import com.yzxt.yh.module.chk.bean.ExamMedic;
import com.yzxt.yh.module.chk.bean.ExamPosion;
import com.yzxt.yh.module.chk.service.ExamService;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.DateUtil;

public class ExamTranAction extends BaseTranAction
{
    private Logger logger = Logger.getLogger(ExamTranAction.class);

    private static final long serialVersionUID = 1L;

    private ExamService examService;

    public ExamService getExamService()
    {
        return examService;
    }

    public void setExamService(ExamService examService)
    {
        this.examService = examService;
    }

    /**
     * 用户体检列表
     * @return
     */
    public void list()
    {
        try
        {
            JsonObject obj = super.getParams();
            initQuery(obj);
            Map<String, Object> filter = new HashMap<String, Object>();
            // 客户ID
            JsonElement cObj = obj.get("custId");
            filter.put("custId", GsonUtil.toString(cObj));
            PageTran<Exam> pageTran = examService.queryCustExamTran(filter, synTime, synType, pageSize);
            List<Exam> list = pageTran.getData();
            JsonArray ja = new JsonArray();
            for (Exam exam : list)
            {
                JsonObject jObj = new JsonObject();
                jObj.addProperty("id", exam.getId());
                jObj.addProperty("examNo", exam.getExamNo());
                jObj.addProperty("custId", exam.getCustId());
                jObj.addProperty("name", exam.getName());
                jObj.addProperty("examWhere", exam.getIndustryName());
                jObj.addProperty("examTime", DateUtil.getTranDate(exam.getExamDate()));
                jObj.addProperty("uploadTime", DateUtil.getTranTime(exam.getCreateTime()));
                ja.add(jObj);
            }
            super.write(ResultTran.STATE_OK, null, ja);
        }
        catch (Exception e)
        {
            logger.error("查询客户体检列表错误。", e);
            super.write(ResultTran.STATE_ERROR, "查询客户体检列表错误。");
        }
    }

    /**
     * 新增体检表
     */
    public void add()
    {
        try
        {
            JsonObject obj = super.getParams();
            Exam exam = gson.fromJson(obj, Exam.class);
            User operUser = super.getOperUser();
            exam.setCreateBy(operUser.getId());
            JsonArray posionArr = obj.getAsJsonArray("posion");
            JsonArray hhArr = obj.getAsJsonArray("zhuyuanQingkuang");
            JsonArray bcArr = obj.getAsJsonArray("binghcuangshi");
            JsonArray mArr = obj.getAsJsonArray("drugUseInfo");
            JsonArray iArr = obj.getAsJsonArray("Jiezhongshi");

            if (posionArr != null && posionArr.size() > 0)
            {
                List<ExamPosion> posions = gson.fromJson(posionArr, new TypeToken<List<ExamPosion>>()
                {
                }.getType());
                exam.setExamPosions(posions);
            }

            if (hhArr != null && hhArr.size() > 0)
            {
                List<ExamHospitalHis> hhs = gson.fromJson(hhArr, new TypeToken<List<ExamHospitalHis>>()
                {
                }.getType());
                exam.setExamHospitalHiss(hhs);
            }

            if (bcArr.size() > 0)
            {
                List<ExamFamilyHosHis> bcs = gson.fromJson(bcArr, new TypeToken<List<ExamFamilyHosHis>>()
                {
                }.getType());
                exam.setExamFamilyHosHiss(bcs);
            }

            if (mArr.size() > 0)
            {
                List<ExamMedic> ms = gson.fromJson(mArr, new TypeToken<List<ExamMedic>>()
                {
                }.getType());
                exam.setExamMedics(ms);
            }

            if (iArr.size() > 0)
            {
                List<ExamInoculate> is = gson.fromJson(iArr, new TypeToken<List<ExamInoculate>>()
                {
                }.getType());
                exam.setExamInoculates(is);
            }

            JsonArray symptomArr = obj.getAsJsonArray("symptom");
            exam.setSymptom(getFenHaoStr(symptomArr));

            JsonArray foodArr = obj.getAsJsonArray("foodHabit");
            exam.setDietStatus(getFenHaoStr(foodArr));

            JsonArray drinkArr = obj.getAsJsonArray("drinkingType");
            exam.setDrinkType(getFenHaoStr(drinkArr));

            JsonArray naoxueArr = obj.getAsJsonArray("naoXueguan");
            exam.setCerebraveDisease(getFenHaoStr(naoxueArr));

            JsonArray shenzArr = obj.getAsJsonArray("shenzang");
            exam.setRenalDisease(getFenHaoStr(shenzArr));

            JsonArray xinzangArr = obj.getAsJsonArray("xinzang");
            exam.setHeartDisease(getFenHaoStr(xinzangArr));

            JsonArray xueguanArr = obj.getAsJsonArray("xueguan");
            exam.setVascularDisease(getFenHaoStr(xueguanArr));

            JsonArray ybjbArr = obj.getAsJsonArray("yanbuJibing");
            exam.setEyeDisease(getFenHaoStr(ybjbArr));

            JsonArray jkzdArr = obj.getAsJsonArray("jiankangZhidao");
            exam.setHealthyDirect(getFenHaoStr(jkzdArr));

            JsonArray wxkArr = obj.getAsJsonArray("weixianKongzhi");
            exam.setDangerControl(getFenHaoStr(wxkArr));

            String id = examService.add(exam);
            JsonObject rObj = new JsonObject();

            rObj.addProperty("examNo", id);
            super.write(ResultTran.STATE_OK, "保存体检成功。", rObj);
        }
        catch (Exception e)
        {
            logger.error("客户端保存体检错误。", e);
            super.write(ResultTran.STATE_ERROR, "保存体检错误。");
        }
    }

    /**
     * 体检详细
     * @return
     */
    public void view()
    {
        try
        {
            JsonObject obj = super.getParams();
            // 客户端体检编号为体检表ID
            String id = GsonUtil.toString(obj.get("id"));
            Exam exam = examService.load(id);
            JsonObject rObj = gson.toJsonTree(exam).getAsJsonObject();

            String[] symptiom = exam.getSymptom().split(";");
            rObj.add("symptom", gson.toJsonTree(symptiom));

            String[] foodHabit = exam.getDietStatus().split(";");
            rObj.add("foodHabit", gson.toJsonTree(foodHabit));

            String[] drinkingType = exam.getDrinkType().split(";");
            rObj.add("drinkingType", gson.toJsonTree(drinkingType));

            String[] naoXueguan = exam.getCerebraveDisease().split(";");
            rObj.add("naoXueguan", gson.toJsonTree(naoXueguan));

            String[] shenzang = exam.getRenalDisease().split(";");
            rObj.add("shenzang", gson.toJsonTree(shenzang));

            String[] xinzang = exam.getHeartDisease().split(";");
            rObj.add("xinzang", gson.toJsonTree(xinzang));

            String[] xueguan = exam.getVascularDisease().split(";");
            rObj.add("xueguan", gson.toJsonTree(xueguan));

            String[] yanbuJibing = exam.getEyeDisease().split(";");
            rObj.add("yanbuJibing", gson.toJsonTree(yanbuJibing));

            String[] jiankangZhidao = exam.getHealthyDirect().split(";");
            rObj.add("jiankangZhidao", gson.toJsonTree(jiankangZhidao));

            String[] weixianKongzhi = exam.getDangerControl().split(";");
            rObj.add("weixianKongzhi", gson.toJsonTree(weixianKongzhi));

            JsonElement posions = (exam.getExamPosions() != null && !exam.getExamPosions().isEmpty()) ? gson
                    .toJsonTree(exam.getExamPosions()) : new JsonArray();
            rObj.add("posion", posions);

            JsonElement hhs = (exam.getExamHospitalHiss() != null && !exam.getExamHospitalHiss().isEmpty()) ? gson
                    .toJsonTree(exam.getExamHospitalHiss()) : new JsonArray();
            rObj.add("zhuyuanQingkuang", hhs);

            JsonElement fhhs = (exam.getExamFamilyHosHiss() != null && !exam.getExamFamilyHosHiss().isEmpty()) ? gson
                    .toJsonTree(exam.getExamFamilyHosHiss()) : new JsonArray();
            rObj.add("binghcuangshi", fhhs);

            JsonElement ms = (exam.getExamMedics() != null && !exam.getExamMedics().isEmpty()) ? gson.toJsonTree(exam
                    .getExamMedics()) : new JsonArray();
            rObj.add("drugUseInfo", ms);

            JsonElement is = (exam.getExamInoculates() != null && !exam.getExamInoculates().isEmpty()) ? gson
                    .toJsonTree(exam.getExamInoculates()) : new JsonArray();
            rObj.add("Jiezhongshi", is);

            super.write(ResultTran.STATE_OK, null, rObj);
        }
        catch (Exception e)
        {
            logger.error("查看体检错误。", e);
            super.write(ResultTran.STATE_ERROR, "查看体检错误。");
        }
    }

    /**
     * 用分号分隔
     * @param arr
     * @return
     */
    private String getFenHaoStr(JsonArray arr)
    {
        String str = "";
        if (arr != null)
        {
            for (JsonElement el : arr)
            {
                str += el.getAsString() + ";";
            }
        }
        return str;
    }

}
