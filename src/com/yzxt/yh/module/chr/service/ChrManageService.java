package com.yzxt.yh.module.chr.service;

import java.sql.Timestamp;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.chr.bean.Manage;
import com.yzxt.yh.module.chr.dao.ChrManageDao;
import com.yzxt.yh.util.StringUtil;

@Transactional(ConstTM.DEFAULT)
public class ChrManageService
{
    private ChrManageDao chrManageDao;

    public ChrManageDao getChrManageDao()
    {
        return chrManageDao;
    }

    public void setChrManageDao(ChrManageDao chrManageDao)
    {
        this.chrManageDao = chrManageDao;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Result addChr(Manage chrManage) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        chrManage.setCreateTime(now);
        String id = chrManageDao.insert(chrManage);
        if (id.equals("该客户血压已经纳入慢病."))
        {
            return new Result(Result.STATE_FAIL, id, id);
        }
        else if (id.equals("该客户血糖已经纳入慢病."))
        {
            return new Result(Result.STATE_FAIL, id, id);
        }
        else if (StringUtil.isNotEmpty(id) && !id.equals("该客户血糖已经纳入慢病.") && !id.equals("该客户血压已经纳入慢病."))
        {
            return new Result(Result.STATE_SUCESS, "保存成功。", id);
        }
        else
        {
            return new Result(Result.STATE_FAIL, "保存失败。", id);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<Manage> getChrList(Map<String, Object> filter, int page, int pageSize)
    {
        return chrManageDao.getList(filter, page, pageSize);
    }

    /**
     * 查询慢病人员列表
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageTran<Manage> queryChrTran(Map<String, Object> filter, Timestamp sysTime, int dir, int count)
    {
        return chrManageDao.queryChrTran(filter, sysTime, dir, count);
    }

}
