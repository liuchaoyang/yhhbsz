package com.yzxt.yh.module.sys.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.common.Result;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.sys.bean.Dict;
import com.yzxt.yh.module.sys.bean.DictDetail;
import com.yzxt.yh.module.sys.dao.DictDao;

@Transactional(ConstTM.DEFAULT)
public class DictService
{

    private DictDao dictDao;

    public DictDao getDictDao()
    {
        return dictDao;
    }

    public void setDictDao(DictDao dictDao)
    {
        this.dictDao = dictDao;
    }

    /**
     * 查询字典
     * @param dictCode
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Dict getDict(String dictCode)
    {
        return dictDao.get(dictCode);
    }

    /**
     * 查询字典明细
     * @param dictCode
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<DictDetail> getDictDetails(String dictCode)
    {
        return dictDao.getDictDetails(dictCode);
    }

    /**
     * 查询字典明细编码查询字典明细
     * @param dictCode
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public DictDetail getDictDetailByCode(String dictCode, String dictDetailCode)
    {
        return dictDao.getDictDetailByCode(dictCode, dictDetailCode);
    }

    /**
     * 添加数据字典
     * @param dict
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result addDict(Dict dict) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        dict.setCreateTime(now);
        if (dictDao.isDictExist(dict.getCode()))
        {
            return new Result(Result.STATE_FAIL, "字典编码重复。", null);
        }
        else if (dictDao.isDictNameExist(dict.getName(), null))
        {
            return new Result(Result.STATE_FAIL, "字典名称重复。", null);
        }
        dictDao.addDict(dict);
        return new Result(Result.STATE_SUCESS, "保存成功。", null);
    }

    /**
     * 修改数据字典
     * @param dict
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result updateDict(Dict dict) throws Exception
    {
        if (!dictDao.isDictExist(dict.getCode()))
        {
            return new Result(Result.STATE_FAIL, "字典编码重复。", null);
        }
        else if (dictDao.isDictNameExist(dict.getName(), dict.getCode()))
        {
            return new Result(Result.STATE_FAIL, "字典名称重复。", null);
        }
        dictDao.updateDict(dict);
        return new Result(Result.STATE_SUCESS, "保存成功。", null);
    }

    /**
     * 添加数据字典明细
     * @param dict
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result addDictDetail(DictDetail dictDetail) throws Exception
    {
        if (dictDao.isDictDetailExist(dictDetail.getDictCode(), dictDetail.getDictDetailCode()))
        {
            return new Result(Result.STATE_FAIL, "字典明细编码重复。", null);
        }
        else if (dictDao.isDictDetailNameExist(dictDetail.getDictCode(), dictDetail.getDictDetailName(), null))
        {
            return new Result(Result.STATE_FAIL, "字典明细名称重复。", null);
        }
        dictDao.addDictDetail(dictDetail);
        return new Result(Result.STATE_SUCESS, "保存成功。", null);
    }

    /**
     * 修改数据字典明细
     * @param dict
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result updateDictDetail(DictDetail dictDetail) throws Exception
    {
        if (!dictDao.isDictDetailExist(dictDetail.getDictCode(), dictDetail.getDictDetailCode()))
        {
            return new Result(Result.STATE_FAIL, "字典明细编码重复。", null);
        }
        else if (dictDao.isDictDetailNameExist(dictDetail.getDictCode(), dictDetail.getDictDetailName(),
                dictDetail.getDictDetailCode()))
        {
            return new Result(Result.STATE_FAIL, "字典明细名称重复。", null);
        }
        dictDao.updateDictDetail(dictDetail);
        return new Result(Result.STATE_SUCESS, "保存成功。", null);
    }

}
