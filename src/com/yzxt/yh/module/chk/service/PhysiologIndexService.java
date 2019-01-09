package com.yzxt.yh.module.chk.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.chk.bean.PhysiologIndex;
import com.yzxt.yh.module.chk.dao.PhysiologIndexDao;

/**
 * 用户生理指标
 * @author huangGang
 * 2015.4.27
 *
 */
@Transactional(ConstTM.DEFAULT)
public class PhysiologIndexService
{
    private PhysiologIndexDao physiologIndexDao;

    public PhysiologIndexDao getPhysiologIndexDao()
    {
        return physiologIndexDao;
    }

    public void setPhysiologIndexDao(PhysiologIndexDao physiologIndexDao)
    {
        this.physiologIndexDao = physiologIndexDao;
    }

    /**
     * 新增(居民)用户时添加生理指标记录
     * 
     * @param physiologicalIndex
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int add(PhysiologIndex physiologIndex) throws Exception
    {
        return physiologIndexDao.insert(physiologIndex);
    }

    /**
     * 更加主键（用户身份证号）获取生理指标
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PhysiologIndex get(String custId)
    {
        return physiologIndexDao.get(custId);
    }

}
