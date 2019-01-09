package com.yzxt.yh.module.sys.bean;

import java.io.Serializable;
import java.util.List;

public class Resource implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String parentId;
    private String name;
    private Integer level;
    private Integer type;
    private String icon;
    private String path;
    private Integer seq;
    // 非持久化字段
    private transient List<Resource> children;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getParentId()
    {
        return parentId;
    }

    public void setParentId(String parentId)
    {
        this.parentId = parentId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getLevel()
    {
        return level;
    }

    public void setLevel(Integer level)
    {
        this.level = level;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public Integer getSeq()
    {
        return seq;
    }

    public void setSeq(Integer seq)
    {
        this.seq = seq;
    }

    public List<Resource> getChildren()
    {
        return children;
    }

    public void setChildren(List<Resource> children)
    {
        this.children = children;
    }

}