package com.yzxt.yh.module.chr.service;

import java.sql.Timestamp;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.yh.constant.ConstChr;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.chr.bean.Visit;
import com.yzxt.yh.module.chr.dao.ChrVisitDao;
import com.yzxt.yh.util.StringUtil;

@Transactional(ConstTM.DEFAULT)
public class ChrVisitService
{
    private ChrVisitDao chrVisitDao;

    public ChrVisitDao getChrVisitDao()
    {
        return chrVisitDao;
    }

    public void setChrVisitDao(ChrVisitDao chrVisitDao)
    {
        this.chrVisitDao = chrVisitDao;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Result addVisit(Visit chrVisit) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        chrVisit.setCreateTime(now);
        chrVisit.setUpdateTime(now);
        String visitNo = (now.toString()).replace("-", "");
        String visitNo2 = visitNo.replace(":", "");
        String visitNo3 = visitNo2.replace(" ", "");
        String visitNo4 = visitNo3.replace(".", "");
        chrVisit.setVisitNo(visitNo4);
        chrVisit.setIshandled(ConstChr.CHR_ISHANDLED_NOT);
        chrVisit.setStatus(ConstChr.CHR_STATUS_YES);
        String id = chrVisitDao.insert(chrVisit);
        if (StringUtil.isNotEmpty(id))
        {
            return new Result(Result.STATE_SUCESS, "下达随访计划成功", id);
        }
        else
        {
            return new Result(Result.STATE_FAIL, "下达随访计划失败", id);
        }

    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<Visit> getVisitList(Map<String, Object> filter, int page, int pageSize)
    {
        return chrVisitDao.getList(filter, page, pageSize);
    }

    //得到随访信息
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Visit getVisitById(String id) throws Exception
    {
        return chrVisitDao.getVisitById(id);
    }

    /*
     * 医生得到个人管理项目的随访列表
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageTran<Visit> queryChrVisitTran(Map<String, Object> filter, Timestamp synTime, int dir, int count)
    {
        return chrVisitDao.queryChrVisitTran(filter, synTime, dir, count);
    }

    /*
     * 个人具体管理项目的随访列表
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageTran<Visit> queryVisitPerTran(Map<String, Object> filter, Timestamp synTime, int dir, int count)
    {
        return chrVisitDao.queryVisitPerTran(filter, synTime, dir, count);
    }

    /*
     * 医生得到个人管理项目的未随访列表
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageTran<Visit> queryChrNOVisitTran(Map<String, Object> filter)
    {
        return chrVisitDao.queryChrNOVisitTran(filter);
    }

    /**
     * 调整随访
     * @author h
     * 2015.7.17
     * @throws Exception 
     * */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result updateVisit(Visit chrVisit) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        chrVisit.setUpdateTime(now);
        int c = chrVisitDao.updatePlan(chrVisit);
        if (c > 0)
        {
            return new Result(Result.STATE_SUCESS, "调整随访计划成功", c);
        }
        else
        {
            return new Result(Result.STATE_FAIL, "调整随访计划失败", c);
        }
    }

}
