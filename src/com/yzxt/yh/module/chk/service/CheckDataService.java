package com.yzxt.yh.module.chk.service;

import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.chk.bean.CheckData;
import com.yzxt.yh.module.chk.dao.CheckDataDao;

@Transactional(ConstTM.DEFAULT)
public class CheckDataService
{
    private CheckDataDao checkDataDao;

    public CheckDataDao getCheckDataDao()
    {
        return checkDataDao;
    }

    public void setCheckDataDao(CheckDataDao checkDataDao)
    {
        this.checkDataDao = checkDataDao;
    }

    /**
     * 查询所有检测项目
     * @param filter
     * @param page
     * @param pageSize
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<CheckData> query(Map<String, Object> filter, int page, int pageSize) throws Exception
    {
        return checkDataDao.query(filter, page, pageSize);
    }

}
