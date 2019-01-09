package com.yzxt.yh.module.sys.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.yh.module.sys.bean.ClientVersion;
import com.yzxt.yh.util.StringUtil;

public class ClientVersionDao extends HibernateSupport<ClientVersion> implements BaseDao<ClientVersion>
{
    @SuppressWarnings("unchecked")
    public ClientVersion getLatestVersion(String appType, String deviceType)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("from ClientVersion where appType = ?");
        params.add(appType, Hibernate.STRING);
        if (StringUtil.isNotEmpty(deviceType))
        {
            sql.append(" and deviceType = ?");
            params.add(deviceType, Hibernate.STRING);
        }
        List<ClientVersion> list = (List<ClientVersion>) super.getSession().createQuery(sql.toString())
                .setParameters(params.getVals(), params.getTypes()).list();
        if (list != null && list.size() > 0)
        {
            return list.get(0);
        }
        return null;
    }

    /**
     * 查询最新客户端版本
     * @param filter
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<ClientVersion> queryLatestVersions(Map<String, Object> filter)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("from ClientVersion where 1=1");
        Integer state = (Integer) filter.get("state");
        if (state != null)
        {
            sql.append(" and state = ?");
            params.add(state, Hibernate.INTEGER);
        }
        String appType = (String) filter.get("appType");
        if (StringUtil.isNotEmpty(appType))
        {
            sql.append(" and appType = ?");
            params.add(appType, Hibernate.STRING);
        }
        // 排序
        sql.append(" order by seq asc");
        List<ClientVersion> list = (List<ClientVersion>) super.getSession().createQuery(sql.toString())
                .setParameters(params.getVals(), params.getTypes()).list();
        return list;
    }

    /**
     * 查询最新客户端版本
     * @param filter
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<ClientVersion> queryVersions(Map<String, Object> filter)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("from ClientVersion where 1=1");
        Integer state = (Integer) filter.get("state");
        if (state != null)
        {
            sql.append(" and state = ?");
            params.add(state, Hibernate.INTEGER);
        }
        String appType = (String) filter.get("appType");
        if (StringUtil.isNotEmpty(appType))
        {
            sql.append(" and appType = ?");
            params.add(appType, Hibernate.STRING);
        }
        else
        {
            appType = "HealthExpertPersonal";
            sql.append(" and appType = ?");
            params.add(appType, Hibernate.STRING);
        }
        // 排序
        sql.append(" order by seq asc");
        List<ClientVersion> list = (List<ClientVersion>) super.getSession().createQuery(sql.toString())
                .setParameters(params.getVals(), params.getTypes()).list();
        return list;
    }
}
