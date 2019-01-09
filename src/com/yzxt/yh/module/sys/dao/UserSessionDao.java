package com.yzxt.yh.module.sys.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.bean.UserSession;

public class UserSessionDao extends HibernateSupport<UserSession> implements BaseDao<UserSession>
{
    /**
     * 判断是否是有效的客户端请求验证信息
     * 
     * @param ticket
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    public UserSession getValidSession(String ticket, String userId, Timestamp now)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.ticket, t.memo from sys_user_session t");
        sql.append(" where t.expiry_time >= ? and t.user_id = ? and t.ticket = ?");
        params.add(now, Hibernate.DATE).add(userId, Hibernate.STRING).add(ticket, Hibernate.STRING);
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString())
                .addScalar("ticket", Hibernate.STRING).addScalar("memo", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        UserSession userSession = null;
        if (objsList != null && !objsList.isEmpty())
        {
            Object[] objs = objsList.get(0);
            userSession = new UserSession();
            int i = 0;
            userSession.setTicket((String) objs[i++]);
            userSession.setMemo((String) objs[i++]);
        }
        return userSession;
    }

    /**
     * 保存客户端登录会话信息
     * 
     * @param ticket
     * @param userId
     */
    public void save(UserSession userSession)
    {
        StringBuilder sql = new StringBuilder();
        sql.append("insert into sys_user_session (ticket, user_id, expiry_time, memo, create_time) values (");
        sql.append("?, ?, ?, ?, ?) on duplicate key update user_id = ?, expiry_time = ?, memo= ? , create_time = ?");
        int i = 0;
        super.getSession().createSQLQuery(sql.toString()).setString(i++, userSession.getTicket())
                .setString(i++, userSession.getUserId()).setDate(i++, userSession.getExpiryTime())
                .setString(i++, userSession.getMemo()).setTimestamp(i++, userSession.getCreateTime())
                .setString(i++, userSession.getUserId()).setDate(i++, userSession.getExpiryTime())
                .setString(i++, userSession.getMemo()).setTimestamp(i++, userSession.getCreateTime()).executeUpdate();
    }

    /**
     * 删除过期的会话
     * @param now
     * @return
     */
    public void deleteExpirySession(Date now)
    {
        StringBuilder sql = new StringBuilder();
        sql.append("delete from sys_user_session where expiry_time < ?");
        int i = 0;
        super.getSession().createSQLQuery(sql.toString()).setDate(i++, now).executeUpdate();
    }
    
    
}
