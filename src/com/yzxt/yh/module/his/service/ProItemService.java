package com.yzxt.yh.module.his.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.common.Result;
import com.yzxt.yh.module.his.bean.ProsItem;
import com.yzxt.yh.module.his.dao.ProItemDao;

public class ProItemService {
	
	private ProItemDao proItemDao;

	public ProItemDao getProItemDao() {
		return proItemDao;
	}

	public void setProItemDao(ProItemDao proItemDao) {
		this.proItemDao = proItemDao;
	}
	@Transactional(propagation = Propagation.REQUIRED)
	public Result addProItem(ProsItem proItem) throws Exception {
		proItemDao.insert(proItem);
	     return new Result(Result.STATE_SUCESS, "保存成功", proItem.getItemid());
		
	}

	
}
