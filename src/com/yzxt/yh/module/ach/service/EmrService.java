package com.yzxt.yh.module.ach.service;

import java.sql.Timestamp;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.ach.bean.Emr;
import com.yzxt.yh.module.ach.dao.EmrDao;
import com.yzxt.yh.util.StringUtil;

@Transactional(ConstTM.DEFAULT)
public class EmrService
{
    private EmrDao emrDao;

    public EmrDao getEmrDao()
    {
        return emrDao;
    }

    public void setEmrDao(EmrDao emrDao)
    {
        this.emrDao = emrDao;
    }

    /**
     * 添加电子病历
     * @param emr
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result add(Emr emr) throws Exception
    {
        // 客户端可能包含创建时间
        if (emr.getCreateTime() == null)
        {
            emr.setUpdateTime(emr.getCreateTime());
        }
        else
        {
            Timestamp now = new Timestamp(System.currentTimeMillis());
            emr.setCreateTime(now);
            emr.setUpdateTime(now);
        }
        emr.setUpdateBy(emr.getCreateBy());
        emrDao.insert(emr);
        return new Result(Result.STATE_SUCESS, "保存成功", null);
    }

    /**
     * 获取电子病历
     * @param id
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Emr load(String id) throws Exception
    {
        Emr emr = emrDao.load(id);
        return emr;
    }

    /**
     * 保存电子病历
     * @param emr
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result save(Emr emr) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Result r = null;
        // 新增电子病历
        if (StringUtil.isEmpty(emr.getId()))
        {
            emr.setCreateBy(emr.getUpdateBy());
            emr.setCreateTime(now);
            emr.setUpdateTime(now);
            emrDao.insert(emr);
            r = new Result(Result.STATE_SUCESS, "保存成功.", null);
        }
        else
        {
            emr.setUpdateTime(now);
            emrDao.update(emr);
            r = new Result(Result.STATE_SUCESS, "保存成功.", null);
        }
        return r;
    }

    /**
     * 查询电子病历
     * @param filter
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<Emr> queryCustEmr(Map<String, Object> filter, int page, int pageSize)
    {
        return emrDao.queryCustEmr(filter, page, pageSize);
    }

    /**
     * 客户端查询电子病历
     * @param dossier
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageTran<Emr> queryTran(Map<String, Object> filter, Timestamp sysTime, int count)
    {
        PageTran<Emr> pageTran = emrDao.queryTran(filter, sysTime, count);
        return pageTran;
    }

}
