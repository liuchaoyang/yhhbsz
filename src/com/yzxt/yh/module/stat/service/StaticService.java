package com.yzxt.yh.module.stat.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.stat.dao.StaticDao;
import com.yzxt.yh.module.sys.bean.LoginRecord;

@Transactional(ConstTM.DEFAULT)
public class StaticService
{
    private StaticDao staticDao;

    public StaticDao getStaticDao()
    {
        return staticDao;
    }

    public void setStaticDao(StaticDao staticDao)
    {
        this.staticDao = staticDao;
    }

    /**
     * 查询用户活跃数统计
     * @author h
     * 2015.7.21
     * @param pageSize 
     * @param page 
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Map<String, Object>> queryUserNum(Map<String, Object> filter)
    {
        return staticDao.queryUserNum(filter);
    }

    /**
     * 查询个人用户的登录次数统计
     * @author h
     * 2015.7.21
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<LoginRecord> queryUser(Map<String, Object> filter, int page, int pageSize)
    {
        return staticDao.queryUser(filter, page, pageSize);
    }

    /**
     * 查询个人的登录次数统计
     * @author h
     * 2015.7.23
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<LoginRecord> getDetail(Map<String, Object> filter, int page, int pageSize)
    {
        return staticDao.getDetail(filter, page, pageSize);
    }

}
