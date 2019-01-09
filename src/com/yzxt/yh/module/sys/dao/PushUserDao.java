package com.yzxt.yh.module.sys.dao;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.yh.module.sys.bean.PushUser;
import com.yzxt.yh.util.StringUtil;

public class PushUserDao extends HibernateSupport<PushUser> implements BaseDao<PushUser>
{
    /**
     * 插入推送用户映射信息
     */
    public String insert(PushUser pushUser) throws Exception
    {
        super.save(pushUser);
        return pushUser.getId();
    }

    /**
     * 修改推送用户映射信息
     * @param user
     * @return
     */
    public int update(PushUser pushUser)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update PushUser t set");
        sql.append(" t.pushChannelId = ?, t.pushUserId = ?");
        sql.append(" where t.deviceType= ? and t.userId = ?");
        params.add(pushUser.getPushChannelId(), Hibernate.STRING);
        params.add(pushUser.getPushUserId(), Hibernate.STRING);
        params.add(pushUser.getDeviceType(), Hibernate.INTEGER);
        params.add(pushUser.getUserId(), Hibernate.STRING);
        int i = super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
        return i;
    }

    /**
     * 修改推送用户映射信息
     * @param user
     * @return
     */
    public int delete(PushUser pushUser)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("delete from PushUser t where t.deviceType = ?");
        params.add(pushUser.getDeviceType(), Hibernate.INTEGER);
        if (StringUtil.isNotEmpty(pushUser.getPushChannelId()))
        {
            sql.append(" and t.pushChannelId = ?");
            params.add(pushUser.getPushChannelId(), Hibernate.STRING);
        }
        sql.append(" and t.userId = ?");
        params.add(pushUser.getUserId(), Hibernate.STRING);
        int i = super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
        return i;
    }

}
