package com.yzxt.tran;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.common.ResultTran;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.fw.util.GsonUtil;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.svb.bean.MemberInfo;
import com.yzxt.yh.module.svb.service.MemberInfoService;
import com.yzxt.yh.module.sys.bean.Customer;
import com.yzxt.yh.module.sys.bean.Doctor;
import com.yzxt.yh.module.sys.bean.Order;
import com.yzxt.yh.module.sys.service.CustomerService;
import com.yzxt.yh.module.sys.service.OrderService;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;
import common.Logger;

/**
 * 用户签约管理
 */
public class MemberInfoTranAction extends BaseTranAction
{
    private static final long serialVersionUID = 1L;
    private Logger logger = Logger.getLogger(MemberInfoTranAction.class);

    private MemberInfoService memberInfoService;
    private CustomerService customerService;
    
    private OrderService orderService;

    public OrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	public MemberInfoService getMemberInfoService()
    {
        return memberInfoService;
    }

    public void setMemberInfoService(MemberInfoService memberInfoService)
    {
        this.memberInfoService = memberInfoService;
    }

    public CustomerService getCustomerService()
    {
        return customerService;
    }

    public void setCustomerService(CustomerService customerService)
    {
        this.customerService = customerService;
    }

    /**
     * 绑定医生
     */
    public void add()
    {
        try
        {
            JsonObject obj = super.getParams();
            MemberInfo memberInfo = new MemberInfo();
            String custId = GsonUtil.toString(obj.get("userId"));
            String doctorId = GsonUtil.toString(obj.get("doctorId"));
            String orderId = GsonUtil.toString(obj.get("orderId"));
            //Date endDay = DateUtil.getFromTranDate(GsonUtil.toString(obj.get("endDay")));
            //更改订单状态
            Order order =null;
            if(StringUtil.isNotEmpty(orderId)){
            	 order= orderService.findByOid(orderId);
            	if(order!=null&&order.getState()==0){
            		order.setState(Constant.ORDER_TYPE_YES);
            		orderService.update(order);
            	}
            }
            //绑定医生
            memberInfo.setCustId(custId);
            memberInfo.setDoctorId(doctorId);
            memberInfo.setStartDay(new Date());
            memberInfo.setEndDay(DateUtil.addMonth(new Date(), order.getCount()));
            //memberInfo.setType(2);
            Result r = memberInfoService.add(memberInfo);
           
            if (Result.STATE_SUCESS == r.getState())
            {
                super.write(ResultTran.STATE_OK, "绑定医生成功。");
            }
            else
            {
                super.write(ResultTran.STATE_ERROR, StringUtil.isNotEmpty(r.getMsg()) ? r.getMsg() : "绑定医生错误。");
            }
        }
        catch (Exception e)
        {
            logger.error("绑定医生错误。", e);
            super.write(ResultTran.STATE_ERROR, "绑定医生错误。");
        }
    }

    /**
     * 客户签约修改
     */
    public void update()
    {
        try
        {
            JsonObject obj = super.getParams();
            String custId = GsonUtil.toString(obj.get("custId"));
            Customer cust = customerService.get(custId);
            String memberId = cust.getMemberId();
            if (StringUtil.isEmpty(memberId))
            {
                super.write(ResultTran.STATE_ERROR, "修改客户签约错误，客户未签约。");
            }
            MemberInfo memberInfo = new MemberInfo();
            memberInfo.setId(memberId);
            String doctorId = GsonUtil.toString(obj.get("doctorId"));
            Date endDay = DateUtil.getFromTranDate(GsonUtil.toString(obj.get("endDay")));
            memberInfo.setDoctorId(doctorId);
            memberInfo.setEndDay(endDay);
            Result r = memberInfoService.update(memberInfo);
            if (Result.STATE_SUCESS == r.getState())
            {
                super.write(ResultTran.STATE_OK, "修改客户签约成功。");
            }
            else
            {
                super.write(ResultTran.STATE_ERROR, StringUtil.isNotEmpty(r.getMsg()) ? r.getMsg() : "修改客户签约错误。");
            }
        }
        catch (Exception e)
        {
            logger.error("修改客户签约错误。", e);
            super.write(ResultTran.STATE_ERROR, "修改客户签约错误。");
        }
    }

    /**
     * 查询客户签约
     */
    public void queryCust()
    {
        try
        {
            JsonObject obj = super.getParams();
            initQuery(obj);
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("operUser", super.getOperUser());
            filter.put("memberStatus", GsonUtil.toString(obj.get("memberStatus")));
            PageTran<Customer> pageTran = memberInfoService.queryCustTran(filter, synTime, pageSize);
            List<Customer> list = pageTran.getData();
            JsonArray ja = new JsonArray();
            for (Customer c : list)
            {
                JsonObject jo = new JsonObject();
                jo.addProperty("custId", c.getUserId());
                jo.addProperty("custName", c.getName());
                jo.addProperty("idCard", c.getIdCard());
                jo.addProperty("sex", c.getSex());
                jo.addProperty("phone", c.getPhone());
                jo.addProperty("memberStatus", StringUtil.isNotEmpty(c.getMemberId()) ? "Y" : "N");
                jo.addProperty("startDay", DateUtil.getTranDate(c.getStartDay()));
                jo.addProperty("endDay", DateUtil.getTranDate(c.getEndDay()));
                jo.addProperty("orgName", c.getOrgName());
                jo.addProperty("synTime", DateUtil.getTranTimestamp(c.getCreateTime()));
                ja.add(jo);
            }
            super.write(ResultTran.STATE_OK, null, ja);
        }
        catch (Exception e)
        {
            logger.error("查询客户签约错误。", e);
            super.write(ResultTran.STATE_ERROR, "查询客户签约错误。");
        }
    }
    /**
     * 查询我的家庭医生
     */
    public void queryDoctor()
    {
        try
        {
            JsonObject obj = super.getParams();
            initQuery(obj);
            Map<String, Object> filter = new HashMap<String, Object>();
            //filter.put("operUser", super.getOperUser());
            filter.put("userId", GsonUtil.toString(obj.get("userId")));
            PageTran<Doctor> pageTran = memberInfoService.queryDocTran(filter, synTime, pageSize);
            List<Doctor> list = pageTran.getData();
            JsonArray ja = new JsonArray();
            for (Doctor d : list)
            {
                JsonObject jo = new JsonObject();
                jo.addProperty("doctorId", d.getUserId());
                jo.addProperty("state", d.getState());
                jo.addProperty("doctorName", d.getDoctorName());
                jo.addProperty("idCard", d.getIdCard());
                jo.addProperty("sex", d.getSex());
                jo.addProperty("phone", d.getPhone());
                jo.addProperty("price", d.getPrice()/100);
                jo.addProperty("yprice", d.getYprice()/100);
                jo.addProperty("professionTitle",d.getProfessionTitle());
                jo.addProperty("deptName", d.getDeptName());
                jo.addProperty("skillInfo", d.getSkillInfo());
                jo.addProperty("describe", d.getDescribe());
                jo.addProperty("type", d.getType());
                //jo.addProperty("memberStatus", StringUtil.isNotEmpty(c.getMemberId()) ? "Y" : "N");
                jo.addProperty("startDay", DateUtil.getTranDate(d.getStartDay()));
                jo.addProperty("endDay", DateUtil.getTranDate(d.getEndDay()));
                jo.addProperty("orgName", d.getOrgName());
                jo.addProperty("synTime", DateUtil.getTranTimestamp(d.getCreateTime()));
                ja.add(jo);
            }
            super.write(ResultTran.STATE_OK, null, ja);
        }
        catch (Exception e)
        {
            logger.error("查询医生错误。", e);
            super.write(ResultTran.STATE_ERROR, "查询医生错误。");
        }
    }
}
