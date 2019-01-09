package com.yzxt.yh.module.cli.service;

import java.sql.Timestamp;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.pager.PageModel;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.cli.bean.Remind;
import com.yzxt.yh.module.cli.dao.RemindDao;

@Transactional(ConstTM.DEFAULT)
public class RemindService
{
    private RemindDao remindDao;

    public RemindDao getRemindDao()
    {
        return remindDao;
    }

    public void setRemindDao(RemindDao remindDao)
    {
        this.remindDao = remindDao;
    }

    /**
     * 上传提醒数据
     * @param bean
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(Remind bean) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        // 保存数据
        bean.setCreateTime(now);
        remindDao.insert(bean);
    }

    /**
     * 平台查询提醒数据记录列表
     * @param filter
     * @param page
     * @param pageSize
     * @return
     * @throws Exception
     * @author h
     * 2015.11.6
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<Remind> query(Map<String, Object> filter, int page, int pageSize)
    {
        return remindDao.queryRemind(filter, page, pageSize);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageTran<Remind> queryTran(Map<String, Object> filter, Timestamp sysTime, int dir, int count)
    {
        return remindDao.queryTran(filter, sysTime, dir, count);
    }

}
