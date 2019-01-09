package com.yzxt.yh.module.sys.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.yh.constant.ConstRole;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.chk.bean.PressurePulse;
import com.yzxt.yh.module.chk.bean.TotalCholesterol;
import com.yzxt.yh.module.sys.bean.Depts;
import com.yzxt.yh.module.sys.bean.Doctor;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.bean.UserRole;
import com.yzxt.yh.module.sys.dao.DoctorDao;
import com.yzxt.yh.module.sys.dao.UserDao;
import com.yzxt.yh.util.StringUtil;

@Transactional(ConstTM.DEFAULT)
public class DoctorService
{
    private DoctorDao doctorDao;
    
    private UserRoleService userRoleService;

    public UserRoleService getUserRoleService() {
		return userRoleService;
	}

	public void setUserRoleService(UserRoleService userRoleService) {
		this.userRoleService = userRoleService;
	}
	private UserDao userDao;

    public DoctorDao getDoctorDao()
    {
        return doctorDao;
    }

    public void setDoctorDao(DoctorDao doctorDao)
    {
        this.doctorDao = doctorDao;
    }

    public UserDao getUserDao()
    {
        return userDao;
    }

    public void setUserDao(UserDao userDao)
    {
        this.userDao = userDao;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public boolean getValidDoctor(Doctor doctor)
    {
        boolean isExist = false;
        if (doctor.getAccount() != null)
        {
            isExist = doctorDao.queryIsExistAccount(doctor.getAccount());
        }
        else
        {
            isExist = false;
        }

        return isExist;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Result addDoctor(Doctor doctor) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        User user = doctor.getUser();
        String account = user.getAccount();
        if (userDao.getAccountExist(account, null))
        {
            return new Result(Result.STATE_FAIL, "客户帐号重复", null);
        }
        String email = user.getEmail();
        if (StringUtil.isNotEmpty(email) && userDao.getEmailExist(email, null))
        {
            return new Result(Result.STATE_FAIL, "Email重复", null);
        }
        String phone = user.getPhone();
        if (StringUtil.isNotEmpty(phone) && userDao.getPhoneExist(phone, null))
        {
            return new Result(Result.STATE_FAIL, "手机号重复", null);
        }
        user.setState(Constant.USER_STATE_EFFECTIVE);
        user.setType(Constant.USER_TYPE_DOCTOR);
        
        user.setCreateBy(doctor.getCreateBy());
        user.setCreateTime(now);
        user.setUpdateBy(doctor.getCreateBy());
        user.setUpdateTime(now);
        String userId = userDao.insert(user);
        // 保存客户密码
        if (StringUtil.isEmpty(user.getPassword()))
        {
            userDao.addPassword(userId, Constant.USER_DEFAULT_PASSWROD);
        }
        else
        {
            userDao.addPassword(userId, user.getPassword());
        }
        // 添加客户角色
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(ConstRole.DOCTOR);
        userRoleService.add(userRole);
        doctor.setUserId(userId);
        doctor.setUpdateBy(doctor.getCreateBy());
        doctor.setState(0);
        doctor.setCreateTime(now);
        doctor.setUpdateTime(now);
        doctorDao.insert(doctor);
        return new Result(Result.STATE_SUCESS, "保存成功", userId);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<Doctor> getList(Map<String, Object> filter, int page, int pageSize)
    {
        return doctorDao.getList(filter, page, pageSize);
    }
    
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Doctor> deptDocList(Map<String, Object> filter)
    {
        return doctorDao.deptDocList(filter);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public Result update(Doctor doctor) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String userId = doctor.getUserId();
        User user = doctor.getUser();
        String email = user.getEmail();
       /* if (StringUtil.isNotEmpty(email) && userDao.getEmailExist(email, userId))
        {
            return new Result(Result.STATE_FAIL, "Email重复。", null);
        }*/
        String phone = user.getPhone();
        if (StringUtil.isNotEmpty(phone) && userDao.getPhoneExist(phone, userId))
        {
            return new Result(Result.STATE_FAIL, "手机号重复。", null);
        }
        String idCard = user.getIdCard();
        if (StringUtil.isNotEmpty(idCard) && userDao.getIdCardExist(idCard, userId))
        {
            return new Result(Result.STATE_FAIL, "身份证号码重复。", null);
        }
        user.setId(userId);
        user.setUpdateTime(now);
        user.setUpdateBy(doctor.getUpdateBy());
        // 修改医生引起的用户表比较特殊，这里直接改实体属性
        User oldUser = userDao.get(userId);
        BeanUtils.copyProperties(user, oldUser, new String[]
        {"id", "state", "type", "account", "orgId", "createBy", "createTime"});
        userDao.update(oldUser);
        doctor.setUpdateTime(now);
        doctor.setUpdateBy(doctor.getUpdateBy());
        doctorDao.update(doctor);
        return new Result(Result.STATE_SUCESS, "保存成功。", userId);
    }
    
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Doctor> queryDoctorTran(Map<String, Object> filter) {
    	
		return doctorDao.queryDoctorTran(filter);
	}
    
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Depts> queryDept(Integer parentId) {
		return doctorDao.querydept(parentId);
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Object[] getDoctorById(String doctorId){
		
		return doctorDao.getDoctorById(doctorId);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Doctor getDoctor(String doctorId){
		
		return doctorDao.getDoctor(doctorId);
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Object[] getOrderByOrderId(String orderId) {
		
		return doctorDao.getOrderByOrderId(orderId);
	}
	@Transactional(propagation = Propagation.REQUIRED)
    public Result updatePre(Doctor doctor)
    {
        Integer c = doctorDao.updatePre(doctor);
        if (c > 0)
        {
            return new Result(Result.STATE_SUCESS, "上传成功", c);
        }
        else
        {
            return new Result(Result.STATE_FAIL, "上传失败", c);
        }
    }
}
