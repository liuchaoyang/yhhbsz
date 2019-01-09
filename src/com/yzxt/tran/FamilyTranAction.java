package com.yzxt.tran;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.common.ResultTran;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.fw.util.GsonUtil;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.chk.bean.BloodOxygen;
import com.yzxt.yh.module.chk.bean.PhysiologIndex;
import com.yzxt.yh.module.chk.bean.PressurePulse;
import com.yzxt.yh.module.chk.service.PhysiologIndexService;
import com.yzxt.yh.module.svb.bean.MemberInfo;
import com.yzxt.yh.module.svb.service.MemberInfoService;
import com.yzxt.yh.module.sys.bean.CustFamily;
import com.yzxt.yh.module.sys.bean.CustFamilyAudit;
import com.yzxt.yh.module.sys.bean.Customer;
import com.yzxt.yh.module.sys.bean.Doctor;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.service.CustomerService;
import com.yzxt.yh.module.sys.service.FamilyService;
import com.yzxt.yh.module.sys.service.UserService;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.FileUtil;
import com.yzxt.yh.util.StringUtil;

import common.Logger;

/**
 * 家庭圈接口类：包括8个接口方法
 * 
 * @author h 2014.6.18
 */
public class FamilyTranAction extends BaseTranAction {

	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(FamilyTranAction.class);

	private CustomerService customerService;

	private FamilyService familyService;

	private UserService userService;
	
	private MemberInfoService memberInfoService;

	public MemberInfoService getMemberInfoService() {
		return memberInfoService;
	}

	public void setMemberInfoService(MemberInfoService memberInfoService) {
		this.memberInfoService = memberInfoService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	private PhysiologIndexService physiologIndexService;

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public FamilyService getFamilyService() {
		return familyService;
	}

	public void setFamilyService(FamilyService familyService) {
		this.familyService = familyService;
	}

	public PhysiologIndexService getPhysiologIndexService() {
		return physiologIndexService;
	}

	public void setPhysiologIndexService(PhysiologIndexService physiologIndexService) {
		this.physiologIndexService = physiologIndexService;
	}

	/**
	 * 搜索用户接口:可以通过 电话号码，用户名称方式搜索用户；其中电话号码必须全部匹配才能搜索到用户。
	 * 
	 * @author h
	 * @param
	 * @return 2015.6.18
	 */
	public void queryCust() {
		try {
			JsonObject paramsJson = super.getParams();
			JsonArray retJson = new JsonArray();
			if (paramsJson == null) {
				super.write(ResultTran.STATE_ERROR, "参数为空", retJson);
				return;
			}
			String userId = GsonUtil.toString(paramsJson.get("userId"));
			String keyWords = GsonUtil.toString(paramsJson.get("key"));
			// 查询方法
			Map<String, Object> filter = new HashMap<String, Object>();
			filter.put("userId", userId);
			filter.put("keyword", keyWords);
			List<Customer> custs = customerService.queryCustTran(filter);
			if (custs != null) {
				for (Customer cust : custs) {
					JsonObject custOject = new JsonObject();
					custOject.addProperty("custId", cust.getUserId());
					custOject.addProperty("userName", cust.getName());
					// 头像
					String imgPath = "pub/cf_img.do?pt=" + FileUtil.encodePath(cust.getPath());
					custOject.addProperty("headIcon", imgPath);
					retJson.add(custOject);
				}
			}
			super.write(ResultTran.STATE_OK, "查询成功", retJson);
		} catch (Exception e) {
			logger.error("查询用户错误", e);
			JsonArray errJson = new JsonArray();
			super.write(ResultTran.STATE_ERROR, "搜索用户错误", errJson);
		}
	}

	/**
     * 发送申请家庭成员请求接口
     * @author h
     * @param 申请信息：applyMsg 用户id：userId 成员id：custId
     * @return 
     * */
    public void addApply()
    {
    	Result r = null;
        try
        {
            JsonObject paramsJson = super.getParams();
            JsonArray retJson = new JsonArray();
            if (paramsJson == null)
            {
                super.write(ResultTran.STATE_ERROR, "客户端参数为空", retJson);
                return;
            }
            String userId = GsonUtil.toString(paramsJson.get("userId"));
            String applyMsg = GsonUtil.toString(paramsJson.get("applyMsg"));
            //String memberId = GsonUtil.toString(paramsJson.get("custId"));
            String name = GsonUtil.toString(paramsJson.get("name"));
            Integer sex = GsonUtil.toInteger(paramsJson.get("sex"));
            String cardNo = GsonUtil.toString(paramsJson.get("cardNo"));
            String phone = GsonUtil.toString(paramsJson.get("phone"));
            String memo = GsonUtil.toString(paramsJson.get("memo"));
            String address = GsonUtil.toString(paramsJson.get("address"));
            String email = GsonUtil.toString(paramsJson.get("email"));
            Customer bean = gson.fromJson(paramsJson, Customer.class);
            CustFamily custF = null;
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("userId", userId);
           /* //判断是否已经是家庭成员
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("userId", userId);
            if(StringUtil.isNotEmpty(cardNo)){
        	  User u = userService.getUserByIdCard(cardNo);
        	  if(u!=null){
        		  filter.put("memberId", u.getId());
                  custF = familyService.getCustFamily(filter);
                  if (custF != null)
                  {
                      super.write(ResultTran.STATE_ERROR, "该成员已经是你的家庭成员了", retJson);
                  }
        	  }
            }*/
         	//Customer cust = new Customer();
         	User user = new User();
             user.setName(name);
             user.setSex(sex);
             user.setIdCard(cardNo);
             user.setPhone(phone);
             user.setEmail(email);
             user.setAddress(address);
         	 bean.setUser(user);
             r = customerService.addFamliy(bean);
             int state = r.getState();
             if (Result.STATE_FAIL == state)
             {
            	 if ("身份证号码重复。".equals(r.getMsg()))
                 {
                     super.write(ResultTran.STATEIDCARDEXIST, ResultTran.STATEIDCARDEXISTINFO, null);
                 }
             }
             CustFamilyAudit applyLog = new CustFamilyAudit();
             applyLog.setCustId(userId);
             applyLog.setMemberId(bean.getUser().getId());
             applyLog.setApplyContext(applyMsg);
             applyLog.setMemberPhone(phone);
             applyLog.setMemo(memo);
             CustFamily custFamily = new CustFamily();
             custFamily.setCustId(userId);
             custFamily.setMemberId(bean.getUserId());
             custFamily.setMemo(memo);
             familyService.addFamily(custFamily);
             //添加申请信息记录
             r = familyService.addApply(applyLog);
             MemberInfo member = new MemberInfo();
             PageTran<Doctor> pageTran = memberInfoService.queryDocTran(filter, synTime, pageSize);
             List<Doctor> list = pageTran.getData();
             for (Doctor doctor : list) {
            	 member.setDoctorId(doctor.getUserId());
            	 member.setCustId(bean.getUserId());
            	 member.setEndDay(doctor.getEndDay());
            	 member.setStartDay(doctor.getStartDay());
            	 r = memberInfoService.add(member);
			}
             super.write(ResultTran.STATE_OK, "上传成功");
          
        }catch(

	Exception e)

	{
		logger.error("发送申请出现异常", e);
		JsonArray errJson = new JsonArray();
		super.write(ResultTran.STATE_ERROR, "服务器错误", errJson);
	}

	}

	/**
	 * 获取家庭成员申请请求数据接口
	 * 
	 * @author h
	 * @param 申请信息：applyMsg
	 * @return 2015.6.18
	 */
	public void getApplyData() {
		try {
			JsonObject paramsJson = super.getParams();
			JsonArray retJson = new JsonArray();
			if (paramsJson == null) {
				super.write(ResultTran.STATE_ERROR, "客户端参数为空", retJson);
				return;
			}
			String userId = GsonUtil.toString(paramsJson.get("userId"));
			/*
			 * String applyMsg = GsonUtil.toString(paramsJson.get("applyMsg"));
			 * //获取家庭成员申请请求数据
			 */
			Map<String, Object> filter = new HashMap<String, Object>();
			filter.put("userId", userId);

			List<CustFamilyAudit> cfa = familyService.getAudit(filter);
			if (cfa != null) {
				for (CustFamilyAudit cust : cfa) {
					JsonObject custOject = new JsonObject();
					// custOject.addProperty("applyId", cust.getId());
					custOject.addProperty("userId", cust.getCustId());
					custOject.addProperty("custId", cust.getMemberId());
					custOject.addProperty("name", cust.getApplyName());
					custOject.addProperty("type", cust.getType());
					/*
					 * Integer sex = cust.getSex(); if(sex!=null){ if(sex==0){
					 * custOject.addProperty("sex","男"); }else if(sex == 1){
					 * custOject.addProperty("sex","女"); } }
					 */
					custOject.addProperty("sex", cust.getSex());
					String cardNo = cust.getIdCard();
					custOject.addProperty("cardNo", cardNo);
					if (StringUtil.isNotEmpty(cardNo) && cardNo.length() == 18) {
						String str = cardNo.substring(6, 10);
						Calendar date = Calendar.getInstance();
						String year = String.valueOf(date.get(Calendar.YEAR));
						Integer age = Integer.parseInt(year) - Integer.parseInt(str);
						custOject.addProperty("age", age);
					}
					custOject.addProperty("phone", cust.getMemberPhone());
					custOject.addProperty("address", cust.getAddress());
					custOject.addProperty("memo", cust.getMemo());

					// 头像
					String imgPath = "pub/cf_img.do?pt=" + FileUtil.encodePath(cust.getPath());
					custOject.addProperty("headIcon", imgPath);
					custOject.addProperty("applyMsg", cust.getApplyContext());
					retJson.add(custOject);
				}
			}
			super.write(ResultTran.STATE_OK, "获取成功", retJson);
		} catch (Exception e) {
			logger.error("获取出现异常", e);
			JsonArray errJson = new JsonArray();
			super.write(ResultTran.STATE_ERROR, "服务器端出现异常", errJson);
		}
	}

	/**
	 * 处理家庭成员申请请求接口
	 * 
	 * @author h
	 * @param
	 * @return 2015.6.18
	 */
	public void dealApplyData() {
		try {
			JsonObject paramsJson = super.getParams();
			JsonArray retJson = new JsonArray();
			if (paramsJson == null) {
				super.write(ResultTran.STATE_ERROR, "客户端参数为空", retJson);
				return;
			}
			String userId = GsonUtil.toString(paramsJson.get("userId"));
			String applyId = GsonUtil.toString(paramsJson.get("applyId"));
			String dealTypeStr = GsonUtil.toString(paramsJson.get("dealType"));
			String custId = GsonUtil.toString(paramsJson.get("custId"));
			String alias = GsonUtil.toString(paramsJson.get("alias"));
			Integer dealType = Integer.parseInt(dealTypeStr);
			CustFamilyAudit cfa = new CustFamilyAudit();
			cfa.setCustId(userId);
			cfa.setMemberId(custId);
			cfa.setId(applyId);
			cfa.setMemo(alias);
			if (dealType == 1) {
				// 插入二条关系数据，修改申请表的状态
				CustFamily cf1 = new CustFamily();
				cf1.setCustId(userId);
				cf1.setMemberId(custId);
				cf1.setMemo(alias);
				Result r1 = familyService.addFamily(cf1);
				CustFamily cf2 = new CustFamily();
				cf2.setCustId(custId);
				cf2.setMemberId(userId);
				Result r2 = familyService.addFamily(cf2);
				cfa.setState(Constant.FAMILY_STATE_APPLY_SUCCESS);
				Result r3 = familyService.updateStateForAudit(cfa);
				if (r1.getState() == 1 && r2.getState() == 1 && r2.getState() == 1) {
					JsonObject deal = new JsonObject();
					deal.addProperty("id1", r1.getData().toString());
					deal.addProperty("id2", r2.getData().toString());
					deal.addProperty("c", r3.getData().toString());
					retJson.add(deal);
					super.write(ResultTran.STATE_OK, "同意", retJson);
				} else {
					super.write(ResultTran.STATE_ERROR, "服务端出现错误或参数错误", retJson);
				}
			} else if (dealType == 2) {
				// 修改申请表中的状态
				cfa.setState(Constant.FAMILY_STATE_APPLY_FAIL);
				Result r = familyService.updateStateForAudit(cfa);
				super.write(ResultTran.STATE_OK, "发送成功", r);
			} else {
				super.write(ResultTran.STATE_OK, "发送成功", retJson);
			}
		} catch (Exception e) {
			logger.error("发送申请出现异常", e);
			JsonArray errJson = new JsonArray();
			super.write(ResultTran.STATE_ERROR, "服务器端出现异常", errJson);
		}
	}

	/**
	 * 获取家庭成员数据接口
	 * 
	 * @author h
	 * @param 申请信息：applyMsg
	 * @return 2015.6.18
	 */
	public void getMemData() {
		try {
			JsonObject paramsJson = super.getParams();
			JsonArray retJson = new JsonArray();
			if (paramsJson == null) {
				super.write(ResultTran.STATE_ERROR, "客户端参数为空", retJson);
				return;
			}
			String userId = GsonUtil.toString(paramsJson.get("userId"));
			Map<String, Object> filter = new HashMap<String, Object>();
			filter.put("userId", userId);
			List<CustFamily> cf = familyService.getFamily(filter);
			if (cf != null) {
				for (CustFamily custF : cf) {
					JsonObject custOject = new JsonObject();
					custOject.addProperty("custId", custF.getCustId());
					custOject.addProperty("memberId", custF.getMemberId());
					custOject.addProperty("userName", custF.getApplyName());
					custOject.addProperty("alias", custF.getMemo());
					custOject.addProperty("phone", custF.getPhone());
					// 头像
					String imgPath = "pub/cf_img.do?pt=" + FileUtil.encodePath(custF.getPath());
					custOject.addProperty("headIcon", imgPath);
					retJson.add(custOject);
				}
			}
			super.write(ResultTran.STATE_OK, "获取成功", retJson);
		} catch (Exception e) {
			logger.error("发送申请出现异常", e);
			JsonArray errJson = new JsonArray();
			super.write(ResultTran.STATE_ERROR, "服务器端出现异常", errJson);
		}
	}

	/**
	 * 修改/设置-家庭成员备注信息接口
	 * 
	 * @author h
	 * @param 申请信息：applyMsg
	 * @return 2015.6.18
	 */
	public void updateMemInfo() {
		try {
			JsonObject paramsJson = super.getParams();
			JsonArray retJson = new JsonArray();
			if (paramsJson == null) {
				super.write(ResultTran.STATE_ERROR, "客户端参数为空", retJson);
				return;
			}
			String userId = GsonUtil.toString(paramsJson.get("userId"));
			String memberId = GsonUtil.toString(paramsJson.get("custId"));
			String memberAlias = GsonUtil.toString(paramsJson.get("alias"));
			CustFamily custf = new CustFamily();
			custf.setCustId(userId);
			custf.setMemberId(memberId);
			custf.setMemo(memberAlias);
			/*
			 * Map<String,Object> filter = new HashMap<String,Object>();
			 * filter.put("userId",userId); filter.put("memberId",memberId);
			 * filter.put("memberAlias",memberAlias);
			 */
			Result r = familyService.updateMemInfo(custf);
			// 添加申请信息记录
			super.write(String.valueOf(r.getState()), r.getMsg(), r.getData());
		} catch (Exception e) {
			logger.error("发送申请出现异常", e);
			JsonArray errJson = new JsonArray();
			super.write(ResultTran.STATE_ERROR, "服务器端出现异常", errJson);
		}
	}

	/**
	 * 删除-家庭成员接口
	 * 
	 * @author h
	 * @param 申请信息：applyMsg
	 * @return 2015.6.18
	 */
	public void delMem() {
		try {
			JsonObject paramsJson = super.getParams();
			JsonArray retJson = new JsonArray();
			if (paramsJson == null) {
				super.write(ResultTran.STATE_ERROR, "客户端参数为空", retJson);
				return;
			}
			String custId = GsonUtil.toString(paramsJson.get("userId"));
			String memberId = GsonUtil.toString(paramsJson.get("custId"));
			// 删除成员数据
			Map<String, Object> filter = new HashMap<String, Object>();
			filter.put("custId", custId);
			filter.put("memberId", memberId);
			CustFamily custF = familyService.getCustFamily(filter);
			String[] ids = new String[2];
			ids[0] = custF.getId();
			Map<String, Object> filter2 = new HashMap<String, Object>();
			filter2.put("custId", memberId);
			filter2.put("memberId", custId);
			CustFamily custF2 = familyService.getCustFamily(filter2);
			ids[1] = custF2.getId();
			Result r = null;
			for (String id : ids) {
				r = familyService.deleteMem(id);
			}
			if (r.getState() == 1 && r.getState() == 1) {
				super.write(ResultTran.STATE_OK, r.getMsg(), retJson);
			} else {
				super.write(ResultTran.STATE_ERROR, "删除出错", retJson);
			}
		} catch (Exception e) {
			logger.error("删除成员数据出现异常", e);
			JsonArray errJson = new JsonArray();
			super.write(ResultTran.STATE_ERROR, "服务器端出现异常", errJson);
		}
	}

	/**
	 * 查看家庭成员基本信息接口
	 * 
	 * @author h
	 * @param 申请信息：applyMsg
	 * @return 2015.6.18
	 */
	public void checkMemInfo() {
		try {
			JsonObject paramsJson = super.getParams();
			JsonArray retJson = new JsonArray();
			if (paramsJson == null) {
				super.write(ResultTran.STATE_ERROR, "客户端参数为空", retJson);
				return;
			}
			String userId = GsonUtil.toString(paramsJson.get("userId"));
			String custId = GsonUtil.toString(paramsJson.get("custId"));
			// 查看家庭成员基本信息
			Customer cust = customerService.load(custId);
			PhysiologIndex pslIdx = physiologIndexService.get(custId);
			if (cust != null) {
				JsonObject custOject = new JsonObject();
				custOject.addProperty("userName", cust.getUser().getName());
				custOject.addProperty("userAccount", cust.getUser().getAccount());
				custOject.addProperty("userId", cust.getUserId());
				// 头像
				String imgPath = "pub/cf_img.do?pt=" + FileUtil.encodePath(cust.getUser().getImgFilePath());
				custOject.addProperty("headIcon", imgPath);
				custOject.addProperty("cardNo", cust.getUser().getIdCard());
				custOject.addProperty("mobile", cust.getUser().getPhone());
				if (cust.getBirthday() == null) {
					custOject.addProperty("birthday", "");
				} else {
					custOject.addProperty("birthday", cust.getBirthday().toString().substring(0, 10));
				}
				/*
				 * custOject.addProperty("birthday",
				 * cust.getBirthday().toString());
				 */
				custOject.addProperty("email", cust.getUser().getEmail());
				custOject.addProperty("sex", cust.getSex());
				if (pslIdx != null) {
					custOject.addProperty("height", pslIdx.getHeight());
					custOject.addProperty("weight", pslIdx.getWeight());
				} else {
					custOject.addProperty("height", "");
					custOject.addProperty("weight", "");
				}

				retJson.add(custOject);
			}
			super.write(ResultTran.STATE_OK, "查看家庭成员基本信息成功", retJson);
		} catch (Exception e) {
			logger.error("查看家庭成员基本信息出现异常", e);
			JsonArray errJson = new JsonArray();
			super.write(ResultTran.STATE_ERROR, "服务器端出现异常", errJson);
		}
	}

	/**
	 * 上传通讯录成员信息
	 */
	public void userPhone() {
		try {
			JsonObject paramJson = super.getParams();
			/*
			 * //initQuery(paramJson); String userId =
			 * GsonUtil.toString(paramJson.get("userId")); User user =
			 * userService.loadUser(userId);
			 */
			String userId = GsonUtil.toString(paramJson.get("userId"));
			String userAccount = GsonUtil.toString(paramJson.get("userAccount"));
			String userName = GsonUtil.toString(paramJson.get("userName"));
			String idCard = GsonUtil.toString(paramJson.get("cardNo"));
			// String password = GsonUtil.toString(paramJson.get("password"));
			String email = GsonUtil.toString(paramJson.get("email"));
			String phone = GsonUtil.toString(paramJson.get("phone"));
			String memo = GsonUtil.toString(paramJson.get("memo"));
			Integer type = GsonUtil.toInteger(paramJson.get("type"));
			Date birthday = DateUtil.getFromTranDate(GsonUtil.toString(paramJson.get("birthday")));
			Integer sex = GsonUtil.toInteger(paramJson.get("sex"));
			User bean = gson.fromJson(paramJson, User.class);
			CustFamily custf = gson.fromJson(paramJson, CustFamily.class);

			if (StringUtil.isEmpty(bean.getId())) {
				if (StringUtil.isNotEmpty(userAccount)) {
					bean.setAccount(userAccount);
				} else {
					bean.setAccount(idCard);
				}
				bean.setId(userId);
				bean.setIdCard(idCard);
				bean.setName(userName);
				bean.setEmail(email);
				bean.setPhone(phone);
				bean.setType(type);
				bean.setState(1);
			}
			userService.save(bean);
			if (StringUtil.isEmpty(custf.getCustId())) {
				custf.setMemo(memo);
				custf.setCustId(bean.getId());
				custf.setMemberId(bean.getId());
			}
			familyService.addFamily(custf);
			super.write(ResultTran.STATE_OK);
		} catch (Exception e) {
			logger.error("上传通讯录错误。", e);
			super.write(ResultTran.STATE_ERROR, "上传通讯录错误。");
		}
	}
}
