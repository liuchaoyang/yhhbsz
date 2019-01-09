package com.yzxt.yh.module.msg.service;

import java.sql.Timestamp;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.msg.bean.ConsultGuide;
import com.yzxt.yh.module.msg.dao.ConsultGuideDao;
import com.yzxt.yh.util.StringUtil;

@Transactional(ConstTM.DEFAULT)
public class ConsultGuideService
{
    private ConsultGuideDao consultGuideDao;

    public ConsultGuideDao getConsultGuideDao()
    {
        return consultGuideDao;
    }

    public void setConsultGuideDao(ConsultGuideDao consultGuideDao)
    {
        this.consultGuideDao = consultGuideDao;
    }

    /**
     * 增加咨询
     * @author huangGang
     * 2015.4.21
     * @return 
     * */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result add(ConsultGuide consultGuide) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String doctorId = consultGuide.getDoctorId();
        if (doctorId.equals("null") && StringUtil.isNotEmpty(doctorId))
        {
            return new Result(Result.STATE_FAIL, "保存失败。", doctorId);
        }
        consultGuide.setConsultTime(now);
        consultGuide.setState(Constant.CONSULT_STATE_NOT_REPLY);
        String id = consultGuideDao.insert(consultGuide);
        if (StringUtil.isNotEmpty(id))
        {
            return new Result(Result.STATE_SUCESS, "保存成功。", id);
        }
        else
        {
            return new Result(Result.STATE_FAIL, "保存失败。", id);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<ConsultGuide> getConsultList(Map<String, Object> filter, int page, int pageSize)
    {
        return consultGuideDao.getList(filter, page, pageSize);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ConsultGuide getDetail(String id) throws Exception
    {
        return consultGuideDao.getDetail(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Result update(ConsultGuide consultGuide) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        consultGuide.setGuideTime(now);
        consultGuide.setState(Constant.CONSULT_STATE_REPLY);
        int r = consultGuideDao.update(consultGuide);
        if (r > 0)
        {
            return new Result(Result.STATE_SUCESS, "回复成功。", r);
        }
        else
        {
            return new Result(Result.STATE_FAIL, "回复失败。", r);
        }
    }

}
