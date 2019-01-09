package com.yzxt.yh.module.chk.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.chk.bean.CheckWarn;
import com.yzxt.yh.module.chk.dao.CheckWarnDao;
import com.yzxt.yh.module.sys.bean.Customer;
import com.yzxt.yh.module.sys.dao.CustomerDao;

@Transactional(ConstTM.DEFAULT)
public class CheckWarnService
{
    private CheckWarnDao checkWarnDao;

    private CustomerDao customerDao;

    private Customer customer;

    public CheckWarnDao getCheckWarnDao()
    {
        return checkWarnDao;
    }

    public void setCheckWarnDao(CheckWarnDao checkWarnDao)
    {
        this.checkWarnDao = checkWarnDao;
    }

    public CustomerDao getCustomerDao()
    {
        return customerDao;
    }

    public void setCustomerDao(CustomerDao customerDao)
    {
        this.customerDao = customerDao;
    }

    public Customer getCustomer()
    {
        return customer;
    }

    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

    /**
     * 保存告警信息
     * @param checkWarn
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(CheckWarn checkWarn) throws Exception
    {
        checkWarnDao.insert(checkWarn);
    }

    /**
     * 获取最新的告警信息
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public CheckWarn getLatest(Map<String, Object> filter) throws Exception
    {
        return checkWarnDao.getLatest(filter);
    }

    /**
     * @author huangGang
     * 2015.4.10
     * 会员预警信息的合并展示
     * @return 
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<CheckWarn> getWarnList(Map<String, Object> filter, int page, int pageSize)
    {
        return checkWarnDao.getWarnList(filter, page, pageSize);
    }

    /**
     * 客户预警明细页面
     * @author huangGang
     * 2015.4.10
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public CheckWarn getPersonal(String id) throws Exception
    {
        return checkWarnDao.getPersonal(id);
    }

    /**
     * @author huangGang
     * 2015.4.10
     * 一个会员的全部预警列表
     * @return 
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<CheckWarn> query(Map<String, Object> filter, int page, int pageSize)
    {
        return checkWarnDao.query(filter, page, pageSize);
    }

    /**
     * @author huangGang
     * 2015.4.11
     * 处理一个会员的预警
     * @return 
     * */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result updateState(Map<String, Object> filter, CheckWarn checkWarn) throws Exception
    {
        int a = checkWarnDao.updateState(checkWarn);
        if (a > 0)
        {
            return new Result(Result.STATE_SUCESS, "处理成功。", a);
        }
        else
        {
            return new Result(Result.STATE_FAIL, "处理失败。", a);
        }

    }

    /**
     * 个人预警
     * @author huangGang
     * 2015.4.14
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<CheckWarn> queryMyWarns(Map<String, Object> filter, int page, int pageSize)
    {
        return checkWarnDao.queryMyWarns(filter, page, pageSize);
    }

    /**
     * 客服端获取个人预警 未处理的。
     * @author huangGang
     * 2015.4.29
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<CheckWarn> getWarnListForClient(Map<String, Object> filter)
    {
        return checkWarnDao.getWarnListForClient(filter);
    }

    /**
     * 客户端查询告警会员列表
     * @param filter
     * @param sysTime
     * @param dir
     * @param count
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageTran<CheckWarn> queryWarnCustTran(Map<String, Object> filter, Timestamp sysTime, int dir, int count)
    {
        return checkWarnDao.queryWarnCustTran(filter, sysTime, dir, count);
    }

    /**
     * 查询有未处理告警的用户。
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<CheckWarn> queryWarnCust(Map<String, Object> filter, int page, int pageSize) throws Exception
    {
        return checkWarnDao.queryWarnCust(filter, page, pageSize);
    }

    /**
     * 获取用户告警相关人信息。
     * 用户id，用户姓名，手机号，是否会员
     * 会员用户健康管理师ID，手机号
     * 自己的推送ID信息List<Object[]>，Object[]设备类型，设备CHANNELID
     * 其他家庭成员推送ID信息List<Object[]>，Object[]设备类型，设备CHANNELID
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Object[] getWarnRelatedPersonnels(String custId)
    {
        return checkWarnDao.getWarnRelatedPersonnels(custId);
    }

}
