package com.yzxt.yh.module.sys.dao;

import java.util.List;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.yh.module.sys.bean.Dict;
import com.yzxt.yh.module.sys.bean.DictDetail;

/**
 * 数据字典
 */
public class DictDao extends HibernateSupport<Dict> implements BaseDao<Dict>
{
    /**
     * 查询字典明细
     * @param dictCode
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<DictDetail> getDictDetails(String dictCode)
    {
        String hql = "select t from DictDetail t where t.dictCode = ? order by t.seqNum asc";
        List<DictDetail> details = super.getSession().createQuery(hql).setString(0, dictCode).list();
        return details;
    }

    /**
     * 查询字典明细编码查询字典明细
     * @param dictCode
     * @return
     */
    @SuppressWarnings("unchecked")
    public DictDetail getDictDetailByCode(String dictCode, String dictDetailCode)
    {
        String hql = "select t from DictDetail t where t.dictDetailCode = ? and t.dictCode = ?";
        List<DictDetail> details = super.getSession().createQuery(hql).setString(0, dictDetailCode)
                .setString(1, dictCode).list();
        return details != null && details.size() > 0 ? details.get(0) : null;
    }

    /**
     * 判断指定编码字典是否存在
     * 
     * @param filter
     * @param page
     * @param pageSize
     * @return
     * @throws Exception
     */
    public boolean isDictExist(String dictCode) throws Exception
    {
        String sql = "select count(*) as c from Dict t where lower(t.code) = ?";
        Long c = (Long) super.getSession().createQuery(sql).setString(0, dictCode.toLowerCase()).uniqueResult();
        return c.longValue() > 0;
    }

    /**
     * 判断指定名称字典是否存在
     * @param dictCode
     * @return
     * @throws Exception
     */
    public boolean isDictNameExist(String dictName, String exceptDictCode) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) as c from Dict t where 1=1");
        sql.append(" and lower(t.name) = ?");
        params.add(dictName.toLowerCase(), Hibernate.STRING);
        if (exceptDictCode != null && exceptDictCode.length() > 0)
        {
            sql.append(" and lower(t.code) <> ?");
            params.add(exceptDictCode.toLowerCase(), Hibernate.STRING);
        }
        Long c = (Long) super.getSession().createQuery(sql.toString())
                .setParameters(params.getVals(), params.getTypes()).uniqueResult();
        return c.longValue() > 0;
    }

    /**
     * 保存字典
     * @param dict
     * @throws Exception
     */
    public void addDict(Dict dict) throws Exception
    {
        super.save(dict);
    }

    /**
     * 修改字典
     * @param dict
     * @throws Exception
     */
    public void updateDict(Dict dict) throws Exception
    {
        String sql = "update Dict t set t.name = ? where t.code = ?";
        int i = 0;
        super.getSession().createQuery(sql).setString(i++, dict.getName()).setString(i++, dict.getCode())
                .executeUpdate();
    }

    /**
     * 判断指定编码字典明细是否存在
     * 
     * @param filter
     * @param page
     * @param pageSize
     * @return
     * @throws Exception
     */
    public boolean isDictDetailExist(String dictCode, String dictDetailCode) throws Exception
    {
        String sql = "select count(*) as c from DictDetail t where lower(t.dictDetailCode) = ? and t.dictCode = ?";
        Long c = (Long) super.getSession().createQuery(sql).setString(0, dictDetailCode.toLowerCase())
                .setString(1, dictCode).uniqueResult();
        return c.longValue() > 0;
    }

    /**
     * 判断指定名称字典明细是否存在
     * @param dictCode
     * @return
     * @throws Exception
     */
    public boolean isDictDetailNameExist(String dictCode, String dictDetailName, String exceptDictDetailCode)
            throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) as c from DictDetail t where 1=1");
        sql.append(" and lower(t.dictDetailName) = ?");
        params.add(dictDetailName.toLowerCase(), Hibernate.STRING);
        if (exceptDictDetailCode != null && exceptDictDetailCode.length() > 0)
        {
            sql.append(" and lower(t.dictDetailCode) <> ?");
            params.add(exceptDictDetailCode.toLowerCase(), Hibernate.STRING);
        }
        sql.append(" and t.dictCode = ?");
        params.add(dictCode, Hibernate.STRING);
        Long c = (Long) super.getSession().createQuery(sql.toString())
                .setParameters(params.getVals(), params.getTypes()).uniqueResult();
        return c.longValue() > 0;
    }

    /**
     * 获取字典明细个数
     * @return
     */
    private int getDictDetailCount(String dictCode)
    {
        String sql = "select count(*) as c from DictDetail t where t.dictCode = ?";
        Long c = (Long) super.getSession().createQuery(sql).setString(0, dictCode).uniqueResult();
        return c.intValue();
    }

    /**
     * 保存字典
     * @param dict
     * @throws Exception
     */
    public void addDictDetail(DictDetail dictDetail) throws Exception
    {
        int existCount = getDictDetailCount(dictDetail.getDictCode());
        Integer seqNum = dictDetail.getSeqNum();
        if (seqNum == null || seqNum < 1 || seqNum > existCount + 1)
        {
            seqNum = existCount + 1;
            dictDetail.setSeqNum(seqNum);
        }
        else
        {
            // 将之后的记录编号+1
            String updateSql = "update DictDetail t set t.seqNum = t.seqNum + 1 where t.seqNum >= ? and t.dictCode = ?";
            super.getSession().createQuery(updateSql).setInteger(0, seqNum).setString(1, dictDetail.getDictCode())
                    .executeUpdate();
        }
        dictDetail.setState(dictDetail != null ? dictDetail.getState() : 1);
        super.getHibernateTemplate().save(dictDetail);
    }

    /**
     * 修改字典
     * @param dict
     * @throws Exception
     */
    public void updateDictDetail(DictDetail dictDetail) throws Exception
    {
        DictDetail oldDictDetail = getDictDetailByCode(dictDetail.getDictCode(), dictDetail.getDictDetailCode());
        if (oldDictDetail == null)
        {
            return;
        }
        Integer oldSeqNum = oldDictDetail.getSeqNum();
        Integer newSeqNum = dictDetail.getSeqNum();
        if (newSeqNum != null && newSeqNum > 0)
        {
            int existCount = getDictDetailCount(dictDetail.getDictCode());
            if (newSeqNum > existCount)
            {
                newSeqNum = existCount;
            }
            if (newSeqNum < oldSeqNum)
            {
                String updateSql = "update DictDetail t set t.seqNum = t.seqNum + 1 where t.seqNum >= ? and t.seqNum < ?";
                super.getSession().createQuery(updateSql).setInteger(0, newSeqNum).setInteger(1, oldSeqNum)
                        .executeUpdate();
            }
            else if (newSeqNum > oldSeqNum)
            {
                String updateSql = "update DictDetail t set t.seqNum = t.seqNum - 1 where t.seqNum > ? and t.seqNum <= ?";
                super.getSession().createQuery(updateSql).setInteger(0, oldSeqNum).setInteger(1, newSeqNum)
                        .executeUpdate();
            }
            oldDictDetail.setSeqNum(newSeqNum);
        }
        oldDictDetail.setDictDetailName(dictDetail.getDictDetailName());
        if (dictDetail.getState() != null)
        {
            oldDictDetail.setState(dictDetail.getState());
        }
        super.getHibernateTemplate().update(oldDictDetail);
    }

}
