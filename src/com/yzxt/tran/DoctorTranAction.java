package com.yzxt.tran;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.common.ResultTran;
import com.yzxt.fw.util.GsonUtil;
import com.yzxt.yh.module.ach.bean.Archive;
import com.yzxt.yh.module.ach.bean.FamilyHistory;
import com.yzxt.yh.module.ach.bean.LifeEnv;
import com.yzxt.yh.module.ach.bean.PreviousHistory;
import com.yzxt.yh.module.ach.service.ArchiveService;
import com.yzxt.yh.module.svb.bean.MemberInfo;
import com.yzxt.yh.module.svb.service.MemberInfoService;
import com.yzxt.yh.module.sys.bean.Customer;
import com.yzxt.yh.module.sys.bean.Depts;
import com.yzxt.yh.module.sys.bean.Doctor;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.service.CustomerService;
import com.yzxt.yh.module.sys.service.DoctorService;
import com.yzxt.yh.module.sys.service.UserService;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;


public class DoctorTranAction extends BaseTranAction
{

    private static final long serialVersionUID = 1L;

    private static Logger logger = Logger.getLogger(DoctorTranAction.class);

    private DoctorService doctorService;

    private ArchiveService archiveService;

    private CustomerService customerService;

    private UserService userService;

    private MemberInfoService memberInfoService;
    
    public DoctorService getDoctorService()
    {
        return doctorService;
    }

    public void setDoctorService(DoctorService doctorService)
    {
        this.doctorService = doctorService;
    }

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

    public MemberInfoService getMemberInfoService()
    {
        return memberInfoService;
    }

    public void setMemberInfoService(MemberInfoService memberInfoService)
    {
        this.memberInfoService = memberInfoService;
    }

    /**
     * 同步医生信息：
     * 包括：登录账号，密码，姓名，电话号码，邮箱，组织id
     * 
     * */
    public void addDoctor()
    {
        JsonObject jObj = new JsonObject();
        Result r = null;
        try
        {
            JsonObject paramJson = null;
            request.setCharacterEncoding("UTF-8");
            String params = request.getParameter("clientparams");
            if(params!=null&&params.length()>0){
                params = new String(params.getBytes("ISO-8859-1"),"UTF-8");
                paramJson = (JsonObject) GsonUtil.parse(params);
            }
            if (paramJson == null)
            {
                super.write(ResultTran.STATE_ERROR, "客户端参数异常", jObj);
            }
            /*byte[] accountNameObj =  paramJson.get("accountName").toString().getBytes("UTF8");
            String accountName = new String(accountNameObj,"utf-8");
            String accountName = GsonUtil.toString(paramJson.get("accountName")("UTF-8"),"UTF-8");
            byte[] accountNameObj =  paramJson.get("accountName").toString().getBytes("utf-8");
            String accountName = new String(accountNameObj,"utf-8");*/
            String accountName = GsonUtil.toString(paramJson.get("accountName"));
            String password = GsonUtil.toString(paramJson.get("password"));
            String account = GsonUtil.toString(paramJson.get("account"));
            String phone = GsonUtil.toString(paramJson.get("phoneNumber"));
            String email = GsonUtil.toString(paramJson.get("email"));
            String orgIdStr = GsonUtil.toString(paramJson.get("groupId"));
            String orgId = orgIdStr+"z";
            Doctor bean = new Doctor();
            User doctUser = new User();
            doctUser.setAccount(account);
            doctUser.setName(accountName);
            doctUser.setPassword(password);
            doctUser.setPhone(phone);
            doctUser.setEmail(email);
            doctUser.setOrgId(orgId);
            bean.setUser(doctUser);
            bean.setCreateBy("1");
            bean.setUpdateBy("1");
            r = doctorService.addDoctor(bean);
        }
        catch (Exception e)
        {
            logger.error("摩云医疗服务器出现异常", e);
            super.write(ResultTran.STATE_ERROR, "摩云医疗服务端异常", jObj);
        }
        if (r.getState() == 1)
        {
            super.write(ResultTran.STATE_OK, r.getMsg(), r.getData());
        }
        else
        {
            super.write(ResultTran.STATE_ERROR, r.getMsg(), r.getData());
        }
    }

    @SuppressWarnings("unused")
    public void acquireCustArchive()
    {
        JsonObject jObj = new JsonObject();
        Result r = null;
        try
        {
            JsonObject paramJson = super.getParams();
            if (paramJson == null)
            {
                super.write(ResultTran.STATE_ERROR, "客户端参数异常", jObj);
            }
            String idCard = GsonUtil.toString(paramJson.get("idNumber"));
            String phone = GsonUtil.toString(paramJson.get("phone"));
            //判断是否存在此用户 如果存在，给中兴健康档案，如果没有。则返回失败
            if (StringUtil.isNotEmpty(idCard) || StringUtil.isNotEmpty(phone))
            {
                User custUser = null;
                if (StringUtil.isNotEmpty(idCard))
                {
                    custUser = userService.getUserByIdCard(idCard);
                }
                else
                {
                    custUser = userService.getUserByIdCard(idCard);
                }
                Customer customer = customerService.load(custUser.getId());
                Archive archive = archiveService.load(custUser.getId());
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
                    
                    //医生信息
                    //医生信息
                    User docUser = null;
                    MemberInfo memberInfo = memberInfoService.getMemberInfoByCustId(custUser.getId());
                    if(memberInfo.getDoctorId() !=null)
                    {
                        docUser = userService.getUser(memberInfo.getDoctorId());
                    }
                    else
                    {
                        docUser = userService.getUser(custUser.getCreateBy());
                    }
                    if(docUser!=null)
                    {
                        recordCoverObj.addProperty("doctor", docUser.getAccount());
                        recordCoverObj.addProperty("doctorName", docUser.getName());
                        recordCoverObj.addProperty("telDoctor", docUser.getPhone());
                    }
                    else
                    {
                        recordCoverObj.addProperty("doctor", "");
                        recordCoverObj.addProperty("doctorName", "");
                        recordCoverObj.addProperty("telDoctor", "");
                    }
                    rObj.add("recordCover", recordCoverObj);
                    // 个人基本信息
                    JsonObject recordBaseInfo = new JsonObject();
                    recordBaseInfo.addProperty("custId", custUser.getId());
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
                    recordBaseInfo.addProperty("geneticHistory", archive.getGeneticHistory());
                    recordBaseInfo.add("disabilityStatus", GsonUtil.strToJsonArr(archive.getDisabilityStatus(), ";"));
                    recordBaseInfo.addProperty("disabilityStatusOther", archive.getOtherDisName());
                    recordBaseInfo.addProperty("chronicDiseas", "");
                    recordBaseInfo.addProperty("chronicDiseaOther", "");
                    rObj.add("recordBaseInfo", recordBaseInfo);
                    // 既往史
                    List<PreviousHistory> previousHistorys = archive.getPreviousHistorys();
                    rObj.add(
                            "recordPastHistory",
                            (previousHistorys != null && !previousHistorys.isEmpty()) ? gson
                                    .toJsonTree(previousHistorys) : new JsonArray());
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
                    //医生信息
                    User docUser = null;
                    MemberInfo memberInfo = memberInfoService.getMemberInfoByCustId(custUser.getId());
                    if (memberInfo.getDoctorId() != null)
                    {
                        docUser = userService.getUser(memberInfo.getDoctorId());
                    }
                    else
                    {
                        docUser = userService.getUser(custUser.getCreateBy());
                    }
                    recordCoverObj.addProperty("doctor", docUser.getAccount());
                    recordCoverObj.addProperty("doctorName", docUser.getName());
                    recordCoverObj.addProperty("telDoctor", docUser.getPhone());
                    rObj.add("recordCover", recordCoverObj);
                    // 个人基本信息
                    JsonObject recordBaseInfo = new JsonObject();
                    recordBaseInfo.addProperty("custId", custUser.getId());
                    recordBaseInfo.addProperty("name", customer.getUser().getName());
                    recordBaseInfo.addProperty("sex", customer.getSex());
                    recordBaseInfo.addProperty("birthday", DateUtil.getTranDate(customer.getBirthday()));
                    recordBaseInfo.addProperty("cardNo", customer.getUser().getIdCard());
                    recordBaseInfo.addProperty("tel", customer.getUser().getPhone());
                    recordBaseInfo.addProperty("occupation", customer.getProfession());
                    recordBaseInfo.add("payment", new JsonArray());
                    recordBaseInfo.add("drugAllergy", new JsonArray());
                    recordBaseInfo.add("exposureHistory", new JsonArray());
                    recordBaseInfo.addProperty("geneticHistory", "");
                    recordBaseInfo.add("disabilityStatus", new JsonArray());
                    recordBaseInfo.addProperty("chronicDiseas", "");
                    recordBaseInfo.addProperty("chronicDiseaOther", "");
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
            else
            {
                super.write(ResultTran.STATE_ERROR, "客户端参数异常全部为空", jObj);
            }
        }
        catch (Exception e)
        {
            logger.error("摩云医疗腾服务器出现异常。", e);
            super.write(ResultTran.STATE_ERROR, "摩云医疗服务端异常", jObj);
        }
    }
   /**
    * 
    * 获取医生列表
    */
    public void doctorList()
    {
        try
        {   
        	 
            JsonObject obj = super.getParams();
            initQuery(obj);
            String doctorName = GsonUtil.toString(obj.get("doctorName"));
            String address = GsonUtil.toString(obj.get("address"));
            String deptName = GsonUtil.toString(obj.get("deptName"));
            User operUser = (User)super.getOperUser();
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("doctorName", doctorName != null ? doctorName.trim() : null);
            filter.put("address", address != null ? address.trim() : null);
            filter.put("deptName", deptName != null ? deptName.trim() : null);
            // 查询人
            filter.put("operUser", operUser);
            List<Doctor> list = doctorService.queryDoctorTran(filter
                   );
            super.write(ResultTran.STATE_OK, null, list);
        }
        catch (Exception e)
        {
            logger.error("查询医生列表错误。", e);
            super.write(ResultTran.STATE_ERROR, "查询医生错误。");
        }
    }
    /**
     * 
     * 获取科室列表
     * 
     */

    public void deptList(){
    	JsonObject obj = super.getParams();
        initQuery(obj);
        /*String parentId = GsonUtil.toString(obj.get("parentId"));
        Map<String,Object> filter = new HashMap<>();
        filter.put("parentId", 0);*/
        JsonArray retJson = new JsonArray();
        List<Depts> depts = doctorService.queryDept(0);
        List<Depts> list =new ArrayList<>();
        if (depts != null && depts.size() > 0)
        {
            for (Depts dept : depts)
            {
         	   List<Depts> listDepts = doctorService.queryDept(dept.getCodeId());
         	    Depts dept1 =new Depts();
         	    dept1.setCodeId(dept.getCodeId());
         	    dept1.setDeptName(dept.getDeptName());
         	    dept1.setParentId(dept.getParentId());
         	    if(listDepts !=null && listDepts.size()>0){
         	    	dept1.setChildData(listDepts);
         	    }
         	    list.add(dept1);
            }
        }
      
        super.write(ResultTran.STATE_OK, "获取科室列表成功",list);
    }
}
