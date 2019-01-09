package com.yzxt.yh.module.rgm.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.rgm.bean.HealthyPlan;
import com.yzxt.yh.module.rgm.dao.HealthyPlanDao;

@Transactional(ConstTM.DEFAULT)
public class HealthyPlanService
{
    private HealthyPlanDao healthyPlanDao;

    public HealthyPlanDao getHealthyPlanDao()
    {
        return healthyPlanDao;
    }

    public void setHealthyPlanDao(HealthyPlanDao healthyPlanDao)
    {
        this.healthyPlanDao = healthyPlanDao;
    }

    /**
     * 添加自主健康计划
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result add(HealthyPlan healthyPlan) throws Exception
    {
        Result result = new Result();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        healthyPlan.setCreateTime(now);
        healthyPlan.setUpdateTime(now);
        healthyPlan.setState(1);
        healthyPlanDao.insert(healthyPlan);
        result.setState(Result.STATE_SUCESS);
        result.setData(healthyPlan.getId());
        return result;
    }

    /**
     * 更新自主健康计划
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result update(HealthyPlan healthyPlan) throws Exception
    {
        Result result = new Result();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        healthyPlan.setUpdateTime(now);
        healthyPlanDao.update(healthyPlan);
        result.setState(Result.STATE_SUCESS);
        result.setData(healthyPlan.getId());
        return result;
    }

    /**
     * 删除自主健康计划
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result delete(String[] ids) throws Exception
    {
        Result result = new Result();
        int c = 0;
        for (String id : ids)
        {
            c = c + healthyPlanDao.delete(id);
        }
        result.setState(Result.STATE_SUCESS);
        result.setData(c);
        return result;
    }

    /**
     * 加载自主健康计划
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public HealthyPlan load(String id) throws Exception
    {
        HealthyPlan healthyPlan = healthyPlanDao.load(id);
        return healthyPlan;
    }

    /**
     * 查询健康计划
     * @param filter
     * @param page
     * @param pageSize
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<HealthyPlan> queryHealthyPlanByPage(Map<String, Object> filter, int page, int pageSize)
            throws Exception
    {
        return healthyPlanDao.queryHealthyPlanByPage(filter, page, pageSize);
    }

    /**
     * 查询健康计划，主要用于客户端查询
     * @param filter
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<HealthyPlan> queryHealthyPlansBySyn(Map<String, Object> filter) throws Exception
    {
        return healthyPlanDao.queryHealthyPlansBySyn(filter);
    }

}
