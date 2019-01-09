package com.yzxt.tran;

import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.yzxt.fw.common.ResultTran;
import com.yzxt.fw.util.GsonUtil;
import com.yzxt.yh.module.ach.bean.Archive;
import com.yzxt.yh.module.ach.bean.FamilyHistory;
import com.yzxt.yh.module.ach.bean.LifeEnv;
import com.yzxt.yh.module.ach.bean.PreviousHistory;
import com.yzxt.yh.module.ach.service.ArchiveService;
import com.yzxt.yh.module.sys.bean.Customer;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.service.CustomerService;
import com.yzxt.yh.module.sys.service.UserService;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;

public class ArchiveTranAction extends BaseTranAction
{
    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(ArchiveTranAction.class);

    private ArchiveService archiveService;

    private CustomerService customerService;

    private UserService userService;

    public ArchiveService getArchiveService()
    {
        return archiveService;
    }

    public void setArchiveService(ArchiveService archiveService)
    {
        this.archiveService = archiveService;
    }

    public CustomerService getCustomerService()
    {
        return customerService;
    }

    public void setCustomerService(CustomerService customerService)
    {
        this.customerService = customerService;
    }

    public UserService getUserService()
    {
        return userService;
    }

    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }

    /**
     * 上传档案
     */
    public void add()
    {
        update();
    }

    /**
     * 更新档案
     */
    public void update()
    {
        String custId = null;
        try
        {
            JsonObject obj = super.getParams();
            custId = GsonUtil.toString(obj.get("custId"));
            Archive archive = new Archive();
            // 档案封面
            JsonObject recordCover = obj.get("recordCover").getAsJsonObject();
            // 健康档案个人基本信息
            JsonObject recordBaseInfo = obj.get("recordBaseInfo").getAsJsonObject();
            // 修改人
            String updateBy = GsonUtil.toString(recordCover.get("createrId"));
            if (StringUtil.isEmpty(updateBy))
            {
                updateBy = super.getOperUser().getId();
            }
            // 获取档案属性
            archive.setCustId(custId);
            archive.setHouseholdAddress(GsonUtil.toString(recordCover.get("permanentAddr")));
            archive.setStreetName(GsonUtil.toString(recordCover.get("townAddr")));
            archive.setVillageName(GsonUtil.toString(recordCover.get("streetAddr")));
            archive.setMedicalEstab(GsonUtil.toString(recordCover.get("medicalEstab")));
            archive.setTelMedical(GsonUtil.toString(recordCover.get("telMedical")));
            archive.setDoctorId(updateBy);
            archive.setWorkUnit(GsonUtil.toString(recordBaseInfo.get("company")));
            archive.setContactName(GsonUtil.toString(recordBaseInfo.get("emergencyContact")));
            archive.setContactTelphone(GsonUtil.toString(recordBaseInfo.get("telEmergency")));
            archive.setHouseholdType(GsonUtil.toInteger(recordBaseInfo.get("residentType")));
            archive.setNational(GsonUtil.toString(recordBaseInfo.get("national")));
            archive.setBloodType(GsonUtil.toInteger(recordBaseInfo.get("bloodType")));
            archive.setRhNagative(GsonUtil.toInteger(recordBaseInfo.get("RHType")));
            archive.setDegree(GsonUtil.toInteger(recordBaseInfo.get("culturalDegree")));
            archive.setMaritalStatus(GsonUtil.toInteger(recordBaseInfo.get("maritalStatus")));
            JsonElement payTypeEle = recordBaseInfo.get("payment");
            if (payTypeEle != null)
            {
                JsonArray payment = payTypeEle.getAsJsonArray();
                archive.setPayType(GsonUtil.jsonArrToString(payment.getAsJsonArray(), ";"));
            }
            archive.setOtherPayType(GsonUtil.toString(recordBaseInfo.get("paymentOther")));
            JsonElement hodaEle = recordBaseInfo.get("drugAllergy");
            if (hodaEle != null)
            {
                JsonArray hoda = hodaEle.getAsJsonArray();
                archive.setHoda(GsonUtil.jsonArrToString(hoda.getAsJsonArray(), ";"));
            }
            archive.setOtherHoda(GsonUtil.toString(recordBaseInfo.get("drugAllergyOther")));
            JsonElement exposureHistoryEle = recordBaseInfo.get("exposureHistory");
            if (exposureHistoryEle != null)
            {
                JsonArray exposureHistory = exposureHistoryEle.getAsJsonArray();
                archive.setExposureHistory(GsonUtil.jsonArrToString(exposureHistory.getAsJsonArray(), ";"));
            }
            archive.setGeneticHistory(GsonUtil.toString(recordBaseInfo.get("geneticHistory")));
            JsonElement disabilityStatusEle = recordBaseInfo.get("disabilityStatus");
            if (disabilityStatusEle != null)
            {
                JsonArray disabilityStatus = disabilityStatusEle.getAsJsonArray();
                archive.setDisabilityStatus(GsonUtil.jsonArrToString(disabilityStatus.getAsJsonArray(), ";"));
            }
            archive.setOtherDisName(GsonUtil.toString(recordBaseInfo.get("disabilityStatusOther")));
            // 用户信息
            User user = new User();
            user.setId(custId);
            user.setName(GsonUtil.toString(recordBaseInfo.get("name")));
            user.setUpdateBy(updateBy);
            // 客户信息
            Customer cust = new Customer();
            cust.setUser(user);
            cust.setSex(GsonUtil.toInteger(recordBaseInfo.get("sex")));
            String birthdayStr = GsonUtil.toString(recordBaseInfo.get("birthday"));
            cust.setBirthday(DateUtil.getFromTranDate(birthdayStr));
            cust.setNational(GsonUtil.toString(recordBaseInfo.get("national")));
            cust.setProfession(GsonUtil.toString(recordBaseInfo.get("occupation")));
            cust.setAddress(GsonUtil.toString(recordCover.get("presentAddr")));
            cust.setMemo(GsonUtil.toString(recordCover.get("otherInfo")));
            cust.setUpdateBy(updateBy);
            // 既往史
            List<PreviousHistory> phs = null;
            JsonElement recordPastHistoryObj = obj.get("recordPastHistory");
            if (recordPastHistoryObj != null)
            {
                phs = gson.fromJson(recordPastHistoryObj, new TypeToken<List<PreviousHistory>>()
                {
                }.getType());
            }
            // 家族史
            List<FamilyHistory> fhs = null;
            JsonElement recordFamilyHistoryObj = obj.get("recordFamilyHistory");
            if (recordFamilyHistoryObj != null)
            {
                fhs = gson.fromJson(recordFamilyHistoryObj, new TypeToken<List<FamilyHistory>>()
                {
                }.getType());
            }
            // 生活环境
            List<LifeEnv> les = null;
            JsonElement recordEnvironmentObj = obj.get("recordEnvironment");
            if (recordEnvironmentObj != null)
            {
                les = gson.fromJson(recordEnvironmentObj, new TypeToken<List<LifeEnv>>()
                {
                }.getType());
            }
            // 保存信息
            archive.setCustomer(cust);
            archive.setPreviousHistorys(phs);
            archive.setFamilyHistorys(fhs);
            archive.setLifeEnvs(les);
            archiveService.save(archive);
            JsonObject rObj = new JsonObject();
            rObj.addProperty("dataCode", "1");
            rObj.addProperty("dataMsg", "保存档案成功");
            super.write(ResultTran.STATE_OK, "保存档案成功", rObj);
        }
        catch (Exception e)
        {
            logger.error("保存档案错误。" + custId, e);
            super.write(ResultTran.STATE_ERROR, "保存档案详细错误。客户ID：" + custId);
        }
    }

    /**
     * 查看档案
     */
    public void view()
    {
        String custId = null;
        try
        {
            JsonObject obj = super.getParams();
            custId = GsonUtil.toString(obj.get("custId"));
            Customer customer = customerService.load(custId);
            Archive archive = archiveService.load(custId);
            JsonObject rObj = new JsonObject();
            if (archive != null)
            {
                User createUser = userService.getUser(archive.getDoctorId());
                // 档案封面
                JsonObject recordCoverObj = new JsonObject();
                recordCoverObj.addProperty("name", customer.getUser().getName());
                recordCoverObj.addProperty("cardNo", customer.getUser().getIdCard());
                recordCoverObj.addProperty("presentAddr", customer.getAddress());
                recordCoverObj.addProperty("permanentAddr", archive.getHouseholdAddress());
                recordCoverObj.addProperty("tel", customer.getUser().getPhone());
                recordCoverObj.addProperty("townAddr", archive.getStreetName());
                recordCoverObj.addProperty("streetAddr", archive.getVillageName());
                recordCoverObj.addProperty("medicalEstab", archive.getMedicalEstab());
                recordCoverObj.addProperty("telMedical", archive.getTelMedical());
                recordCoverObj.addProperty("createrId", archive.getDoctorId());
                recordCoverObj.addProperty("createrName", createUser != null ? createUser.getName() : "");
                recordCoverObj.addProperty("createTime", DateUtil.getTranDate(archive.getCreateTime()));
                recordCoverObj.addProperty("updateTime", DateUtil.getTranDate(archive.getUpdateTime()));
                recordCoverObj.addProperty("emergencyContact", archive.getContactName());
                recordCoverObj.addProperty("telEmergency", archive.getContactTelphone());
                recordCoverObj.addProperty("otherInfo", customer.getMemo());
                rObj.add("recordCover", recordCoverObj);
                // 个人基本信息
                JsonObject recordBaseInfo = new JsonObject();
                recordBaseInfo.addProperty("custId", custId);
                recordBaseInfo.addProperty("name", customer.getUser().getName());
                recordBaseInfo.addProperty("sex", customer.getSex());
                recordBaseInfo.addProperty("birthday", DateUtil.getTranDate(customer.getBirthday()));
                recordBaseInfo.addProperty("cardNo", customer.getUser().getIdCard());
                recordBaseInfo.addProperty("company", archive.getWorkUnit());
                recordBaseInfo.addProperty("tel", customer.getUser().getPhone());
                recordBaseInfo.addProperty("emergencyContact", archive.getContactName());
                recordBaseInfo.addProperty("telEmergency", archive.getContactTelphone());
                recordBaseInfo.addProperty("residentType", archive.getHouseholdType());
                recordBaseInfo.addProperty("national", archive.getNational());
                recordBaseInfo.addProperty("bloodType", archive.getBloodType());
                recordBaseInfo.addProperty("RHType", archive.getRhNagative());
                recordBaseInfo.addProperty("culturalDegree", archive.getDegree());
                recordBaseInfo.addProperty("occupation", customer.getProfession());
                recordBaseInfo.addProperty("maritalStatus", archive.getMaritalStatus());
                recordBaseInfo.add("payment", GsonUtil.strToJsonArr(archive.getPayType(), ";"));
                recordBaseInfo.addProperty("paymentOther", archive.getOtherPayType());
                recordBaseInfo.add("drugAllergy", GsonUtil.strToJsonArr(archive.getHoda(), ";"));
                recordBaseInfo.addProperty("drugAllergyOther", archive.getOtherHoda());
                recordBaseInfo.add("exposureHistory", GsonUtil.strToJsonArr(archive.getExposureHistory(), ";"));
                // recordBaseInfo.addProperty("geneticHistory", null);
                recordBaseInfo.add("disabilityStatus", GsonUtil.strToJsonArr(archive.getDisabilityStatus(), ";"));
                recordBaseInfo.addProperty("disabilityStatusOther", archive.getOtherDisName());
                // recordBaseInfo.addProperty("chronicDiseas", null);
                // recordBaseInfo.addProperty("chronicDiseaOther", null);
                rObj.add("recordBaseInfo", recordBaseInfo);
                // 既往史
                List<PreviousHistory> previousHistorys = archive.getPreviousHistorys();
                rObj.add("recordPastHistory",
                        (previousHistorys != null && !previousHistorys.isEmpty()) ? gson.toJsonTree(previousHistorys)
                                : new JsonArray());
                // 家族史
                List<FamilyHistory> familyHistorys = archive.getFamilyHistorys();
                rObj.add("recordFamilyHistory",
                        (familyHistorys != null && !familyHistorys.isEmpty()) ? gson.toJsonTree(familyHistorys)
                                : new JsonArray());
                // 生活环境
                List<LifeEnv> lifeEnvs = archive.getLifeEnvs();
                rObj.add("recordEnvironment", (lifeEnvs != null && !lifeEnvs.isEmpty()) ? gson.toJsonTree(lifeEnvs)
                        : new JsonArray());
            }
            else
            {
                // 档案封面
                JsonObject recordCoverObj = new JsonObject();
                recordCoverObj.addProperty("name", customer.getUser().getName());
                recordCoverObj.addProperty("cardNo", customer.getUser().getIdCard());
                recordCoverObj.addProperty("presentAddr", customer.getAddress());
                recordCoverObj.addProperty("tel", customer.getUser().getPhone());
                recordCoverObj.addProperty("otherInfo", customer.getMemo());
                rObj.add("recordCover", recordCoverObj);
                // 个人基本信息
                JsonObject recordBaseInfo = new JsonObject();
                recordBaseInfo.addProperty("custId", custId);
                recordBaseInfo.addProperty("name", customer.getUser().getName());
                recordBaseInfo.addProperty("sex", customer.getSex());
                recordBaseInfo.addProperty("birthday", DateUtil.getTranDate(customer.getBirthday()));
                recordBaseInfo.addProperty("cardNo", customer.getUser().getIdCard());
                recordBaseInfo.addProperty("tel", customer.getUser().getPhone());
                recordBaseInfo.addProperty("occupation", customer.getProfession());
                recordBaseInfo.add("payment", new JsonArray());
                recordBaseInfo.add("drugAllergy", new JsonArray());
                recordBaseInfo.add("exposureHistory", new JsonArray());
                // recordBaseInfo.addProperty("geneticHistory", null);
                recordBaseInfo.add("disabilityStatus", new JsonArray());
                // recordBaseInfo.addProperty("chronicDiseas", null);
                // recordBaseInfo.addProperty("chronicDiseaOther", null);
                rObj.add("recordBaseInfo", recordBaseInfo);
                // 既往史
                rObj.add("recordPastHistory", new JsonArray());
                // 家族史
                rObj.add("recordFamilyHistory", new JsonArray());
                // 生活环境
                rObj.add("recordEnvironment", new JsonArray());
            }
            super.write(ResultTran.STATE_OK, null, rObj);
        }
        catch (Exception e)
        {
            logger.error("客户端查看档案详细错误。客户ID：" + custId, e);
            super.write(ResultTran.STATE_ERROR, "查看档案详细错误。");
        }
    }

}
