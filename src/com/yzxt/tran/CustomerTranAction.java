package com.yzxt.tran;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.yzxt.fw.common.ResultTran;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.fw.util.GsonUtil;
import com.yzxt.yh.module.sys.bean.Customer;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.service.CustomerService;
import com.yzxt.yh.util.DateUtil;

public class CustomerTranAction extends BaseTranAction
{
    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(CustomerTranAction.class);
    private CustomerService customerService;

    public CustomerService getCustomerService()
    {
        return customerService;
    }

    public void setCustomerService(CustomerService customerService)
    {
        this.customerService = customerService;
    }

    /**
     * 查询会员
     */
    public void query()
    {
        try
        {
            JsonObject obj = super.getParams();
            initQuery(obj);
            Map<String, Object> filter = new HashMap<String, Object>();
            JsonElement uObj = obj.get("userId");
            String userId = uObj.getAsString();
            User operUser = super.getOperUser();
            filter.put("operUser", operUser);
            filter.put("userId", userId);
            //查询条件增加患者类型参数：patientType 1：全部，2：会员，3：非会员
            String patientType = GsonUtil.toString(obj.get("patientType"));
            filter.put("patientType", patientType);
            PageTran<Customer> pageTran = customerService.queryTran(filter, synTime, synType, pageSize);
            List<Customer> list = pageTran.getData();
            JsonArray ja = new JsonArray();
            for (Customer cust : list)
            {
                JsonObject jObj = new JsonObject();
                jObj.addProperty("custId", cust.getUserId());
                jObj.addProperty("name", cust.getName());
                jObj.addProperty("sex", cust.getSex());
                jObj.addProperty("birthday", DateUtil.getTranDate(cust.getBirthday()));
                jObj.addProperty("cardNo", cust.getIdCard());
                jObj.addProperty("tel", cust.getPhone());
                jObj.addProperty("uploadTime", DateUtil.getTranTime(cust.getCreateTime()));
                ja.add(jObj);
            }
            super.write(ResultTran.STATE_OK, null, ja);
        }
        catch (Exception e)
        {
            logger.error("查询会员错误。", e);
            super.write(ResultTran.STATE_ERROR, "查询会员错误。");
        }
    }
}
