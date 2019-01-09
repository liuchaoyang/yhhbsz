package com.yzxt.yh.module.chk.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.pager.PageModel;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.yh.constant.ConstCheckData;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.chk.bean.CheckWarn;
import com.yzxt.yh.module.chk.bean.Temperature;
import com.yzxt.yh.module.chk.dao.PhysiologIndexDao;
import com.yzxt.yh.module.chk.dao.TemperatureDao;
import com.yzxt.yh.module.chk.util.AnalysisCheckData;
import com.yzxt.yh.module.chk.util.AnalysisResult;
import com.yzxt.yh.module.svb.service.MemberInfoService;
import com.yzxt.yh.module.sys.service.UserService;
import com.yzxt.yh.util.DateUtil;

@Transactional(ConstTM.DEFAULT)
public class TemperatureService
{
    private TemperatureDao temperatureDao;
    private PhysiologIndexDao physiologIndexDao;
    private CheckWarnService checkWarnService;
    private UserService userService;
    private MemberInfoService memberInfoService;

    public TemperatureDao getTemperatureDao()
    {
        return temperatureDao;
    }

    public void setTemperatureDao(TemperatureDao temperatureDao)
    {
        this.temperatureDao = temperatureDao;
    }

    public PhysiologIndexDao getPhysiologIndexDao()
    {
        return physiologIndexDao;
    }

    public void setPhysiologIndexDao(PhysiologIndexDao physiologIndexDao)
    {
        this.physiologIndexDao = physiologIndexDao;
    }

    public CheckWarnService getCheckWarnService()
    {
        return checkWarnService;
    }

    public void setCheckWarnService(CheckWarnService checkWarnService)
    {
        this.checkWarnService = checkWarnService;
    }

    public UserService getUserService()
    {
        return userService;
    }

    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }

    public MemberInfoService getMemberInfoService()
    {
        return memberInfoService;
    }

    public void setMemberInfoService(MemberInfoService memberInfoService)
    {
        this.memberInfoService = memberInfoService;
    }

    /**
     * 上传体温数据
     * @param bean
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(Temperature bean) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        // 根据检测值的有效性调整检测时间
        if (bean.getCheckTime() == null || bean.getCheckTime().getTime() > now.getTime())
        {
            bean.setCheckTime(now);
        }
        // 分析数据
        AnalysisResult anaRst = AnalysisCheckData.temperature(bean.getTemperature());
        bean.setLevel(anaRst.getLevel());
        bean.setDescript(anaRst.getDescript());
        // 保存数据
        bean.setCreateTime(now);
        temperatureDao.insert(bean);
        // 更新客户身体指标
        physiologIndexDao.update(bean);
        // 保存告警信息
        // saveWarn(bean);
    }

    /**
     * 保存体温告警信息
     * @param bean
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveWarn(Temperature bean) throws Exception
    {
        // 风险评估等级
        int level = bean.getLevel() != null ? bean.getLevel().intValue() : 0;
        if (level > 0)
        {
            // 当天已有告警
            Map<String, Object> warnFilter = new HashMap<String, Object>();
            warnFilter.put("type", ConstCheckData.TEMPERATURE);
            warnFilter.put("custId", bean.getCustId());
            warnFilter.put("startDate", DateUtil.truncDay(bean.getCreateTime()));
            CheckWarn lastWarn = checkWarnService.getLatest(warnFilter);
            // 每天告警级别增加时，会产生新的告警
            if (lastWarn == null || lastWarn.getLevel() < level)
            {
                CheckWarn warn = new CheckWarn();
                warn.setId(bean.getId());
                warn.setCustId(bean.getCustId());
                warn.setType(ConstCheckData.TEMPERATURE);
                warn.setLevel(level);
                warn.setDescript(bean.getDescript());
                warn.setState(Constant.WARNING_STATE_NOT_DEAL);
                warn.setWarnTime(bean.getCreateTime());
                checkWarnService.save(warn);
            }
        }
    }

    /**
     * 查询体温列表
     * @param filter 过滤条件
     * @param sysTime 同步时间点
     * @param dir 同步方式，大于0：取时间点后的数据，小于0取时间点前的数据
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageTran<Temperature> queryTran(Map<String, Object> filter, Timestamp sysTime, int dir, int count)
    {
        return temperatureDao.queryTran(filter, sysTime, dir, count);
    }

    /**
     * 查询体温列表
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<Temperature> query(Map<String, Object> filter, int page, int pageSize)
    {
        return temperatureDao.query(filter, page, pageSize);
    }

}
