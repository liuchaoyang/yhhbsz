package com.yzxt.yh.module.sys.dao;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.yh.module.sys.bean.RoleResource;

public class RoleResourceDao extends HibernateSupport<RoleResource> implements BaseDao<RoleResource>
{
    /**
     * 插入角色信息
     */
    public String insert(RoleResource roleResource) throws Exception
    {
        super.save(roleResource);
        return roleResource.getId();
    }

}
