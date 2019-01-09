package com.yzxt.yh.module.msg.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.msg.bean.Richtext;
import com.yzxt.yh.module.msg.dao.RichtextDao;

/**
 * 富文本服务类
 * @author f
 *
 */
@Transactional(ConstTM.DEFAULT)
public class RichtextService
{
    private RichtextDao richtextDao;

    public RichtextDao getRichtextDao()
    {
        return richtextDao;
    }

    public void setRichtextDao(RichtextDao richtextDao)
    {
        this.richtextDao = richtextDao;
    }

    /**
     * 添加富文本信息
     * @param richtext
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public String add(Richtext richtext) throws Exception
    {
        return richtextDao.insert(richtext);
    }

    /**
     * 更新富文本信息
     * @param richtext
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void update(Richtext richtext) throws Exception
    {
        richtextDao.update(richtext);
    }

    /**
     * 删除富文本信息
     * @param richtext
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) throws Exception
    {
        richtextDao.delete(id);
    }

}
