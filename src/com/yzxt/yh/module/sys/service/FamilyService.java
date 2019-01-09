package com.yzxt.yh.module.sys.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.sys.bean.CustFamily;
import com.yzxt.yh.module.sys.bean.CustFamilyAudit;
import com.yzxt.yh.module.sys.dao.CustFamilyAuditDao;
import com.yzxt.yh.module.sys.dao.CustFamilyDao;
import com.yzxt.yh.module.sys.dao.UserDao;
import com.yzxt.yh.util.StringUtil;

@Transactional(ConstTM.DEFAULT)
public class FamilyService
{
    private CustFamilyDao custFamilyDao;
    
    private UserDao userDao;
   
    public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	private CustFamilyAuditDao custFamilyAuditDao;

    public CustFamilyDao getCustFamilyDao()
    {
        return custFamilyDao;
    }

    public void setCustFamilyDao(CustFamilyDao custFamilyDao)
    {
        this.custFamilyDao = custFamilyDao;
    }

    public CustFamilyAuditDao getCustFamilyAuditDao()
    {
        return custFamilyAuditDao;
    }

    public void setCustFamilyAuditDao(CustFamilyAuditDao custFamilyAuditDao)
    {
        this.custFamilyAuditDao = custFamilyAuditDao;
    }

    /**
     * 增加家庭关系
     * @author h
     * 2015.6.18
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result addFamily(CustFamily custFamily) throws Exception
    {
        /*Timestamp now = new Timestamp(System.currentTimeMillis());*/
        String id = custFamilyDao.insert(custFamily);
        if (StringUtil.isNotEmpty(id))
        {
            return new Result(Result.STATE_SUCESS, "添加成功", id);
        }
        else
        {
            return new Result(Result.STATE_FAIL, "添加失败", id);
        }
    }

    /**
     * 增加家庭成员申请
     * @author h
     * 2015.6.18
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result addApply(CustFamilyAudit applyLog) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        applyLog.setApplyTime(now);
        applyLog.setAuditTime(null);
        String applyId = custFamilyAuditDao.insert(applyLog);
        applyLog.setState(Constant.FAMILY_STATE_APPLY);
        if (StringUtil.isNotEmpty(applyId))
        {
            return new Result(Result.STATE_SUCESS, "发送申请成功", applyId);
        }
        else
        {
            return new Result(Result.STATE_FAIL, "发送申请失败", applyId);
        }
    }

    /**
     * 获取家庭成员申请请求数据接口
     * @author h
     * @param filter 
     * @param 申请信息：applyMsg
     * @return 
     * 2015.6.24
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<CustFamilyAudit> getAudit(Map<String, Object> filter)
    {
        return custFamilyAuditDao.getAuditList(filter);
    }

    /**
     * 获取家庭成员数据接口
     * @author h
     * @return 
     * 2015.6.18
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<CustFamily> getFamily(Map<String, Object> filter)
    {
        return custFamilyDao.getFamily(filter);
    }
    /**
     * 获取户主的Id
     * @param filter
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public CustFamily getFamilyUserId(String custId)
    {
        return custFamilyDao.getFamilyUserId(custId);
    }
    
    /**
     * 从家庭申请表中获取户主的Id
     * @param filter
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public CustFamilyAudit getFamilyUser(String custId)
    {
        return custFamilyDao.getFamilyUser(custId);
    }
    /**
     * 修改家庭成员数据
     * @author h
     * @return 
     * 2015.6.18
     * */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result updateMemInfo(CustFamily custf)
    {
        Integer c = custFamilyDao.updateMemInfo(custf);
        if (c > 0)
        {
            return new Result(Result.STATE_SUCESS, "修改成员信息成功", c);
        }
        else
        {
            return new Result(Result.STATE_FAIL, "修改成员信息失败", c);
        }
    }
    /**
     * 修改家庭成员申请数据
     * @author h
     * @return 
     * 2015.6.18
     * */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result updateMemInfo(CustFamilyAudit custf)
    {
        Integer c = custFamilyAuditDao.updateMemInfo(custf);
        if (c > 0)
        {
            return new Result(Result.STATE_SUCESS, "修改成员信息成功", c);
        }
        else
        {
            return new Result(Result.STATE_FAIL, "修改成员信息失败", c);
        }
    }
    /**
     * 删除家庭成员数据
     * @author h
     * @return 
     * 2015.6.18
     * @throws Exception 
     * */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result deleteMem(String id) throws Exception
    {
        /*CustFamily custF = custFamilyDao.getCustFamily(id);*/
        custFamilyDao.deleteMem(id);
        Integer d = 1;
        if (d > 0)
        {
            return new Result(Result.STATE_SUCESS, "删除家庭成员成功", d);
        }
        else
        {
            return new Result(Result.STATE_FAIL, "删除家庭成员失败", d);
        }
    }

    /**
     * 通过custId 和memberId参数获得关系表
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public CustFamily getCustFamily(Map<String, Object> filter)
    {
        return custFamilyDao.getCustFamily(filter);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Result updateStateForAudit(CustFamilyAudit cfa)
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        cfa.setAuditTime(now);
        Integer c = custFamilyAuditDao.updateStateForAudit(cfa);
        if (c > 0)
        {
            return new Result(Result.STATE_SUCESS, "操作成功", c);
        }
        else
        {
            return new Result(Result.STATE_FAIL, "操作失败", c);
        }
    }

    /**
     * 平台获取家庭申请情况
     * @param filter
     * @author h
     * @param j 
     * @param i 
     * @return
     * 2015.7.8
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<CustFamilyAudit> queryApply(Map<String, Object> filter, int page, int pageSize)
    {
        return custFamilyAuditDao.queryApply(filter, page, pageSize);
    }

    /**
     * 平台获取家庭审核情况
     * @param filter
     * @author h
     * @param j 
     * @param i 
     * @return
     * 2015.7.8
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<CustFamilyAudit> queryAudit(Map<String, Object> filter, int page, int pageSize)
    {
        return custFamilyAuditDao.queryAudit(filter, page, pageSize);
    }

    /**
     * 平台获取家庭列表
     * @param filter
     * @author h
     * @param j 
     * @param i 
     * @return
     * 2015.7.8
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<CustFamily> queryFamily(Map<String, Object> filter, int page, int pageSize)
    {
        return custFamilyDao.queryFamily(filter, page, pageSize);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public CustFamily get(String id)
    {
        return custFamilyDao.get(id);
    }

}
