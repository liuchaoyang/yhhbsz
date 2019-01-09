package com.yzxt.yh.module.his.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.module.his.bean.HisPros;
import com.yzxt.yh.module.his.bean.HisYpkc;
import com.yzxt.yh.module.his.dao.DzcfDao;
import com.yzxt.yh.module.sys.bean.Doctor;
import com.yzxt.yh.util.PinYin4jUtils;

public class DzcfService {
	
	private DzcfDao dzcfDao;
	public DzcfDao getDzcfDao() {
		return dzcfDao;
	}
	public void setDzcfDao(DzcfDao dzcfDao) {
		this.dzcfDao = dzcfDao;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<HisYpkc> getList(Map<String, Object> filter, int page, int pageSize)
    {
        return dzcfDao.getList(filter, page, pageSize);
    }
	@Transactional(propagation = Propagation.REQUIRED)
	public Result addChufang(HisPros hisPros) throws Exception {
		 dzcfDao.insert(hisPros);
	     return new Result(Result.STATE_SUCESS, "保存成功", hisPros.getProsid());
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public HisYpkc getById(String id) {
		return this.dzcfDao.getById(id);
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<HisPros> queryChufang(Map<String, Object> filter) {
		
		return this.dzcfDao.queryChufang(filter);
	}
	
	/**
	 * 
	 * 客户端查询处方
	 */
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<HisPros> queryDzcf(Map<String, Object> filter) {
		
		return this.dzcfDao.queryDzcf(filter);
	}

}
