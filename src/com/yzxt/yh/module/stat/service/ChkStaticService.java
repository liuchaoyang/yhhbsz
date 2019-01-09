package com.yzxt.yh.module.stat.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.stat.dao.ChkStaticDao;
import com.yzxt.yh.module.stat.vo.AreaChk;
import com.yzxt.yh.module.stat.vo.PeopleDis;

@Transactional(ConstTM.DEFAULT)
public class ChkStaticService
{
    private ChkStaticDao chkStaticDao;

    public ChkStaticDao getChkStaticDao()
    {
        return chkStaticDao;
    }

    public void setChkStaticDao(ChkStaticDao chkStaticDao)
    {
        this.chkStaticDao = chkStaticDao;
    }

    /**
     * 查看指定区域（省、市、县、乡镇）一段时间内的检测数量
     * @return
     * @throws ParseException
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<AreaChk> queryAreaChk(Map<String, Object> filter) throws ParseException
    {
        return chkStaticDao.queryAreaChk(filter);
    }

    /**
     * 查询指定区域的检测人群分布
     * @return
     * @throws ParseException
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<PeopleDis>[] queryPeopleDis(Map<String, Object> filter) throws ParseException
    {
        return chkStaticDao.queryPeopleDis(filter);
    }

}
