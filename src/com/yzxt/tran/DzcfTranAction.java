package com.yzxt.tran;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yzxt.fw.common.ResultTran;
import com.yzxt.fw.util.GsonUtil;
import com.yzxt.yh.module.his.bean.HisPros;
import com.yzxt.yh.module.his.service.DzcfService;
import com.yzxt.yh.module.sys.bean.User;

public class DzcfTranAction extends BaseTranAction{
	
	 private static final long serialVersionUID = 1L;

	 private static Logger logger = Logger.getLogger(DzcfTranAction.class);
	
	 private DzcfService dzcfService;
	    public DzcfService getDzcfService() {
			return dzcfService;
		}
		public void setDzcfService(DzcfService dzcfService) {
			this.dzcfService = dzcfService;
		}
	 /**
	 * 
	 * 客户端查看处方
	 */
	
	public void queryDzcf(){
		try {
			JsonObject obj = super.getParams();
			JsonArray array = new JsonArray();
			initQuery(obj);
			Integer status = GsonUtil.toInteger(obj.get("status"));
			String custId = GsonUtil.toString(obj.get("custId"));
			User operUser = (User) super.getOperUser();
			Map<String, Object> filter = new HashMap<String, Object>();
			filter.put("status", status);
			filter.put("custId", custId);
			JsonObject object = new JsonObject();
			List<HisPros> hisPros = dzcfService.queryDzcf(filter);
			if(hisPros!=null){
				for (HisPros pros : hisPros) {
					object.addProperty("custName", pros.getCustName());
					object.addProperty("ypmc", pros.getYpmc());
					object.addProperty("unit", pros.getUnit());
					object.addProperty("num", pros.getNum());
					object.addProperty("userage", pros.getUserage());
					object.addProperty("uselevel", pros.getUselevel());
					array.add(object);
				}
			}
			super.write(ResultTran.STATE_OK, "获取电子处方成功", array);
		} catch (Exception e) {
			 logger.error("查询电子处方列表错误。", e);
	         super.write(ResultTran.STATE_ERROR, "查询电子处方错误。");
		}
	}
	
}
