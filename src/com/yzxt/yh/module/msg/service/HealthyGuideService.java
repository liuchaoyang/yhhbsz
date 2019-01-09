package com.yzxt.yh.module.msg.service;

import java.sql.Timestamp;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.msg.bean.HealthyGuide;
import com.yzxt.yh.module.msg.dao.HealthyGuideDao;
import com.yzxt.yh.util.StringUtil;

@Transactional(ConstTM.DEFAULT)
public class HealthyGuideService
{
    private HealthyGuideDao healthyGuideDao;

    public HealthyGuideDao getHealthyGuideDao()
    {
        return healthyGuideDao;
    }

    public void setHealthyGuideDao(HealthyGuideDao healthyGuideDao)
    {
        this.healthyGuideDao = healthyGuideDao;
    }

    public PageModel<HealthyGuide> getList(Map<String, Object> filter, int page, int pageSize)
    {
        return healthyGuideDao.getList(filter, page, pageSize);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Result add(HealthyGuide healthyGuide) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        healthyGuide.setCreateTime(now);
        String id = healthyGuideDao.insert(healthyGuide);
        if (StringUtil.isNotEmpty(id))
        {
            return new Result(Result.STATE_SUCESS, "保存成功。", id);
        }
        else
        {
            return new Result(Result.STATE_FAIL, "保存失败。", id);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public HealthyGuide getDetail(String id) throws Exception
    {
        return healthyGuideDao.getDetail(id);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<HealthyGuide> getPersonalList(Map<String, Object> filter, int page, int pageSize)
    {
        return healthyGuideDao.getPersonalList(filter, page, pageSize);
    }

    /**
     * 医生客户端查询自己的健康指导情况
     * @author h
     * 2015.9.23
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageTran<HealthyGuide> queryGuideTran(Map<String, Object> filter, Timestamp sysTime, int dir, int count)
    {
        return healthyGuideDao.queryGuideTran(filter, sysTime, dir, count);
    }
    
    /**
     * 查看医生建议
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public HealthyGuide lookSuggest(String custId,String id) throws Exception {
    	return healthyGuideDao.lookSuggest(custId,id);
	}

}
