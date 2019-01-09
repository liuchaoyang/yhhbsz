package com.yzxt.yh.module.chk.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.chk.dao.CheckRemindDao;
import com.yzxt.yh.module.msg.bean.CheckRemind;

@Transactional(ConstTM.DEFAULT)
public class CheckRemindService
{
    private CheckRemindDao checkRemindDao;

    public CheckRemindDao getCheckRemindDao()
    {
        return checkRemindDao;
    }

    public void setCheckRemindDao(CheckRemindDao checkRemindDao)
    {
        this.checkRemindDao = checkRemindDao;
    }

    /**
     * 查询提醒会员列
     * @param filter
     * @param page
     * @param pageSize
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<CheckRemind> queryRemindSet(Map<String, Object> filter, int page, int pageSize) throws Exception
    {
        return checkRemindDao.queryRemindSet(filter, page, pageSize);
    }

    /**
     * 保存检测提醒设置
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result saveSet(CheckRemind checkRemind)
    {
        int c = checkRemindDao.saveSet(checkRemind);
        return new Result(Result.STATE_SUCESS, "保存成功。", c);
    }

    /**
     * 查询需呀发送提醒的用户
     * @param filter
     * @param maxSize
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<CheckRemind> getNeedRemindCust(Map<String, Object> filter, int maxSize)
    {
        return checkRemindDao.getNeedRemindCust(filter, maxSize);
    }

    /**
     * 保存提醒结果
     * @param checkRemind
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveRemind(CheckRemind checkRemind)
    {
        checkRemindDao.saveRemind(checkRemind);
    }

}
