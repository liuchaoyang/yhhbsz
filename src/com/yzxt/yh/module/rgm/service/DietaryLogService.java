package com.yzxt.yh.module.rgm.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.rgm.bean.DietaryLog;
import com.yzxt.yh.module.rgm.dao.DietaryLogDao;
import com.yzxt.yh.util.StringUtil;

@Transactional(ConstTM.DEFAULT)
public class DietaryLogService
{
    private DietaryLogDao dietaryLogDao;

    public DietaryLogDao getDietaryLogDao()
    {
        return dietaryLogDao;
    }

    public void setDietaryLogDao(DietaryLogDao dietaryLogDao)
    {
        this.dietaryLogDao = dietaryLogDao;
    }

    //增加膳食日志
    @Transactional(propagation = Propagation.REQUIRED)
    public Result addDietaryLog(DietaryLog dietaryLog) throws Exception
    {
        String id = dietaryLogDao.addDietaryLog(dietaryLog);
        if (StringUtil.isNotEmpty(id))
        {
            return new Result(Result.STATE_SUCESS, "增加膳食日志成功", id);
        }
        else
        {
            return new Result(Result.STATE_FAIL, "增加膳食日志失败", id);
        }
    }

    /**
     * 膳食日志列表
     * @author huangGang
     * update 2014.11.17
     * 客户端查询膳食日志总列表
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<DietaryLog> getListDietaryLog(Map<String, Object> filter) throws Exception
    {
        return dietaryLogDao.getList(filter);
    }

    /**
     * 膳食日志详细列表
     * @author huangGang
     * update time 2014.11.17
     * 客户端查询
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<DietaryLog> getDetailLog(Map<String, Object> filter) throws Exception
    {
        return dietaryLogDao.getDetailLog(filter);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<DietaryLog> getDietaryLogList(Map<String, Object> filter, int page, int pagesize) throws Exception
    {
        return dietaryLogDao.getDietaryLogList(filter, page, pagesize);
    }

    /*    public DietaryLog getDietaryLog(String sportTime)
        {
            return dietaryLogDao.get(sportTime);
        }*/

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<DietaryLog> getDetailsLog(DietaryLog dietaryLog) throws Exception
    {
        return dietaryLogDao.getDetailsLog(dietaryLog);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public DietaryLog getLogByDay(Map<String, Object> filter)
    {
        return dietaryLogDao.getLogByDay(filter);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteDietaryLogs(Map<String, Object> filter)
    {
        dietaryLogDao.deleteDietaryLogs(filter);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteDietaryLog(Map<String, Object> filter)
    {
        dietaryLogDao.deleteDietaryLog(filter);
    }

}
