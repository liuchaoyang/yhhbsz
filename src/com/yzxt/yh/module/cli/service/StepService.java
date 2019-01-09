package com.yzxt.yh.module.cli.service;

import java.sql.Timestamp;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.pager.PageModel;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.cli.bean.Step;
import com.yzxt.yh.module.cli.dao.StepDao;

@Transactional(ConstTM.DEFAULT)
public class StepService
{
    public StepDao stepDao;

    public StepDao getStepDao()
    {
        return stepDao;
    }

    public void setStepDao(StepDao stepDao)
    {
        this.stepDao = stepDao;
    }

    /**
     * 保存客户计步信息
     * @param bean
     * @return
     * @throws Exception
     * @author h
     * 2015.11.4
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(Step bean) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        bean.setCreateTime(now);
        stepDao.insert(bean);
    }

    /**
     * 平台查询计步记录
     * @author h
     * 2015.11.6
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<Step> query(Map<String, Object> filter, int page, int pageSize)
    {
        return stepDao.query(filter, page, pageSize);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageTran<Step> queryTran(Map<String, Object> filter, Timestamp sysTime, int dir, int count)
    {
        return stepDao.queryTran(filter, sysTime, dir, count);
    }
}
