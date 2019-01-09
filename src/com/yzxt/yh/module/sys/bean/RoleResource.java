package com.yzxt.yh.module.sys.bean;

public class RoleResource implements java.io.Serializable
{

    private static final long serialVersionUID = 1L;

    private String id;
    private String roleId;
    private String resourceId;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getRoleId()
    {
        return roleId;
    }

    public void setRoleId(String roleId)
    {
        this.roleId = roleId;
    }

    public String getResourceId()
    {
        return resourceId;
    }

    public void setResourceId(String resourceId)
    {
        this.resourceId = resourceId;
    }

}