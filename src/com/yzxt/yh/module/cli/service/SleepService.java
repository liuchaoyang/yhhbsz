package com.yzxt.yh.module.cli.service;

import java.sql.Timestamp;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.pager.PageModel;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.cli.bean.Sleep;
import com.yzxt.yh.module.cli.bean.SleepQuality;
import com.yzxt.yh.module.cli.dao.SleepDao;
import com.yzxt.yh.module.cli.dao.SleepQualityDao;

@Transactional(ConstTM.DEFAULT)
public class SleepService
{
    private SleepDao sleepDao;
    private SleepQualityDao sleepQualityDao;

    public SleepDao getSleepDao()
    {
        return sleepDao;
    }

    public void setSleepDao(SleepDao sleepDao)
    {
        this.sleepDao = sleepDao;
    }

    public SleepQualityDao getSleepQualityDao()
    {
        return sleepQualityDao;
    }

    public void setSleepQualityDao(SleepQualityDao sleepQualityDao)
    {
        this.sleepQualityDao = sleepQualityDao;
    }

    /**
     * 上传睡眠数据
     * @param bean
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(Sleep bean) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        // 保存数据
        bean.setCreateTime(now);
        sleepDao.insert(bean);
    }

    /**
     * 上传睡眠质量数据
     * @param bean
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveSleepQuality(SleepQuality bean) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        // 保存数据
        bean.setCreateTime(now);
        sleepQualityDao.insert(bean);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<Sleep> query(Map<String, Object> filter, int page, int pageSize)
    {
        return sleepDao.query(filter, page, pageSize);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageTran<Sleep> queryTran(Map<String, Object> filter, Timestamp sysTime, int dir, int count)
    {
        return sleepDao.queryTran(filter, sysTime, dir, count);
    }

}
