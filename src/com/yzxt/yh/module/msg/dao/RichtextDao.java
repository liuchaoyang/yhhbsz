package com.yzxt.yh.module.msg.dao;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.yh.module.msg.bean.Richtext;

public class RichtextDao extends HibernateSupport<Richtext> implements BaseDao<Richtext>
{
    /**
     * 添加富文本信息
     * @param richtext
     * @return
     * @throws Exception
     */
    public String insert(Richtext richtext) throws Exception
    {
        super.save(richtext);
        return richtext.getId();
    }

    /**
     * 更新富文本信息
     * @param richtext
     * @return
     * @throws Exception
     */
    public int update(Richtext richtext) throws Exception
    {
        HibernateParams params = new HibernateParams();
        String sql = "update Richtext t set t.summary = ?, t.content = ? where t.id = ?";
        params.add(richtext.getSummary(), Hibernate.STRING);
        params.add(richtext.getContent(), Hibernate.STRING);
        params.add(richtext.getId(), Hibernate.STRING);
        return super.getSession().createQuery(sql).setParameters(params.getVals(), params.getTypes()).executeUpdate();
    }

    /**
     * 删除富文本信息
     * @param richtext
     * @return
     * @throws Exception
     */
    public void delete(String id) throws Exception
    {
        HibernateParams params = new HibernateParams();
        String sql = "delete from Richtext t where t.id = ?";
        params.add(id, Hibernate.STRING);
        super.getSession().createQuery(sql).setParameters(params.getVals(), params.getTypes()).executeUpdate();
    }
}
