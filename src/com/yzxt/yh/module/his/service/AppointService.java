package com.yzxt.yh.module.his.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.yh.constant.ConstHis;
import com.yzxt.yh.module.his.bean.Appoint;
import com.yzxt.yh.module.his.bean.BaseArea;
import com.yzxt.yh.module.his.bean.Dept;
import com.yzxt.yh.module.his.dao.AppointDao;
import com.yzxt.yh.module.his.dao.DeptDao;
import com.yzxt.yh.util.StringUtil;

public class AppointService
{
    private AppointDao appointDao;
    private DeptDao deptDao;

    public AppointDao getAppointDao()
    {
        return appointDao;
    }

    public void setAppointDao(AppointDao appointDao)
    {
        this.appointDao = appointDao;
    }
    
    public DeptDao getDeptDao()
    {
        return deptDao;
    }

    public void setDeptDao(DeptDao deptDao)
    {
        this.deptDao = deptDao;
    }

    /**
     * 申请预约挂号
     * @author h
     * 2016.1.22
     * */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result addAppoint(Appoint appoint) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        appoint.setCreateTime(now);
        appoint.setUpdateTime(now);
        appoint.setStatus(ConstHis.HIS_STATUS_YYZ);
        String id = appointDao.insert(appoint);
        if(StringUtil.isNotEmpty(id))
        {
            return new Result(Result.STATE_SUCESS,"添加预约成功",id);
        }
        else
        {
            return new Result(Result.STATE_FAIL,"添加预约失败",id);
        }
    }

    /**
     * 平台查询预约挂号列表
     * @author h
     * 2016.1.22
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<Appoint> query(Map<String, Object> filter, int page, int pageSize)
    {
        return appointDao.query(filter, page, pageSize);
    }

    /**
     * 客户端查询预约挂号列表
     * @author h
     * 2016.1.22
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageTran<Appoint> queryTran(Map<String, Object> filter, Timestamp sysTime, int dir, int count)
    {
        return appointDao.queryTran(filter, sysTime, dir, count);
    }
    
    /**
     * 处理预约挂号申请
     * @author h
     * 2016.1.22
     * */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result updateAppoint(Appoint bean)
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        bean.setUpdateTime(now);
        int c = appointDao.updateAppoint(bean);
        if (c > 0)
        {
            return new Result(Result.STATE_SUCESS,"处理成功",c);
        }
        else
        {
            return new Result(Result.STATE_FAIL,"处理预约时失败",c);
        }
    }

    /**
     * 客户端查询医院列表
     * @author h
     * 2016.1.22
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageTran<Dept> queryDeptTran(Map<String, Object> filter, Timestamp sysTime, int dir, int count)
    {
        return deptDao.queryDeptTran(filter, sysTime, dir, count);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageTran<Dept> queryDepartTran(Map<String, Object> filter, Timestamp sysTime, int dir, int count)
    {
        return deptDao.queryDepartTran(filter, sysTime, dir, count);
    }

    /**
     * 平台查询医院列表
     * @author h
     * 2016.1.22
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Dept> queryDept(Map<String, Object> filter)
    {
        return deptDao.queryDept(filter);
    }
    
    /**
     * 获取地区列表
     * @author h
     * 2016.1.22
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<BaseArea> queryArea(Map<String, Object> filter)
    {
        return deptDao.queryArea(filter);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public Appoint getAppointById(String id)
    {
        return appointDao.getAppointById(id);
    }

	public List<Appoint> queryCustmerName(Map<String, Object> filter) {
		return appointDao.queryCustmerName(filter);
	}

}
