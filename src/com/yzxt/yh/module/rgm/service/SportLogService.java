package com.yzxt.yh.module.rgm.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.rgm.bean.SportLog;
import com.yzxt.yh.module.rgm.dao.SportLogDao;
import com.yzxt.yh.util.StringUtil;

@Transactional(ConstTM.DEFAULT)
public class SportLogService
{
    private SportLogDao sportLogDao;

    public SportLogDao getSportLogDao()
    {
        return sportLogDao;
    }

    public void setSportLogDao(SportLogDao sportLogDao)
    {
        this.sportLogDao = sportLogDao;
    }

    //增加日志
    @Transactional(propagation = Propagation.REQUIRED)
    public Result addSportLog(SportLog sportLog) throws Exception
    {
        String id = sportLogDao.addSportLog(sportLog);
        if (StringUtil.isNotEmpty(id))
        {
            return new Result(Result.STATE_SUCESS, "添加运动日志成功", id);
        }
        else
        {
            return new Result(Result.STATE_FAIL, "添加运动日志失败", id);
        }
    }

    /**
     * 运动日志列表
     * @author h
     * 客户端查询
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<SportLog> getListSportLog(Map<String, Object> filter) throws Exception
    {
        return sportLogDao.getList(filter);
    }

    /**
     * 根据时间段来查询运动日志
     * @author h
     * 客户端查询
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<SportLog> querySportLog(Map<String, Object> filter)
    {
        return sportLogDao.querySportLog(filter);
    }

    /**
     * 运动日志详细列表
     * @author h
     * 客户端查询
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<SportLog> getDetailLog(Map<String, Object> filter) throws Exception
    {
        return sportLogDao.getDetailLog(filter);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<SportLog> getSportLogList(Map<String, Object> filter, int page, int pagesize) throws Exception
    {
        return sportLogDao.getSportLogList(filter, page, pagesize);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public SportLog getSportLog(String sportTime)
    {
        return sportLogDao.get(sportTime);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<SportLog> getDetailsLog(SportLog sportLog) throws Exception
    {
        return sportLogDao.getDetailsLog(sportLog);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public SportLog getLogByDay(Map<String, Object> filter)
    {
        return sportLogDao.getLogByDay(filter);
    }

    /**
     * 删除一天运动日志
     * @author h
     * 客户端删除
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteSportLogs(Map<String, Object> filter)
    {
        sportLogDao.deleteSportLogs(filter);
    }

    /**
     * 删除一条运动日志
     * @author huanggang
     * 客户端删除
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteSportLog(Map<String, Object> filter)
    {
        sportLogDao.deleteSportLog(filter);
    }

}
