package com.yzxt.yh.module.cli.service;

import java.sql.Timestamp;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.pager.PageModel;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.cli.bean.SleepQuality;
import com.yzxt.yh.module.cli.dao.SleepQualityDao;

@Transactional(ConstTM.DEFAULT)
public class SleepQualityService
{
    public SleepQualityDao sleepQualityDao;

    public SleepQualityDao getSleepQualityDao()
    {
        return sleepQualityDao;
    }

    public void setSleepQualityDao(SleepQualityDao sleepQualityDao)
    {
        this.sleepQualityDao = sleepQualityDao;
    }

    /**
     * 保存客户睡眠质量信息
     * @param bean
     * @return
     * @throws Exception
     * @author h
     * 2015.11.4
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(SleepQuality bean) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        bean.setCreateTime(now);
        sleepQualityDao.insert(bean);
    }

    /**
     * 查询睡眠质量数据记录列表
     * @return
     * @author h
     * 2015.11.6
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<SleepQuality> query(Map<String, Object> filter, int page, int pageSize)
    {
        return sleepQualityDao.query(filter, page, pageSize);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageTran<SleepQuality> queryTran(Map<String, Object> filter, Timestamp sysTime, int dir, int count)
    {
        return sleepQualityDao.queryTran(filter, sysTime, dir, count);
    }

}
