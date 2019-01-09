package com.yzxt.yh.module.sys.service;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.common.Result;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.sys.bean.Codes;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.bean.UserPassword;
import com.yzxt.yh.module.sys.bean.UserSession;
import com.yzxt.yh.module.sys.dao.CodeDao;
import com.yzxt.yh.module.sys.dao.UserDao;
import com.yzxt.yh.module.sys.dao.UserSessionDao;
import com.yzxt.yh.util.DateUtil;

@Transactional(ConstTM.DEFAULT)
public class CodeService
{
    private CodeDao codeDao;


    
    public CodeDao getCodeDao() {
		return codeDao;
	}
	public void setCodeDao(CodeDao codeDao) {
		this.codeDao = codeDao;
	}
	@Transactional(propagation = Propagation.REQUIRED)
    public void saveCode(Codes code) throws Exception
    {   
		Timestamp now = new Timestamp(System.currentTimeMillis());
		code.setCreateTime(now);
		codeDao.save(code);
    }

    /**
     * 通过用户手机号码查询验证码
     * @param phone
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Codes getCodeByPhone(String phone)
    {
        return codeDao.getCodeByPhone(phone);
    }
    /**
     * 更新验证码
     * @param userId
     * 
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result updateCode(Codes code) throws Exception
    {    
    	code.setCreateTime(new Timestamp(System.currentTimeMillis()));
    	codeDao.updateCode(code);
    	return new Result(Result.STATE_SUCESS, "保存成功。",null);
    	
    }
}
