package com.yzxt.yh.module.sys.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.sys.dao.SeqDao;

@Transactional(ConstTM.DEFAULT)
public class SeqService
{
    private SeqDao seqDao;

    public SeqDao getSeqDao()
    {
        return seqDao;
    }

    public void setSeqDao(SeqDao seqDao)
    {
        this.seqDao = seqDao;
    }

    /**
     * 获取序列下一个值
     * @param cust
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public int getNextVal(String seqName) throws Exception
    {
        return seqDao.getNextVal(seqName);
    }

}
