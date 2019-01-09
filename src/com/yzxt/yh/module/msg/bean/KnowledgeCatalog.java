package com.yzxt.yh.module.msg.bean;

public class KnowledgeCatalog implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String parentId;
    private String fullId;
    private String name;
    private String detail;
    private Integer level;
    private Integer isLeaf;
    private Integer state;
    private Integer seq;

    public String getId()
    {
        return this.id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getParentId()
    {
        return this.parentId;
    }

    public void setParentId(String parentId)
    {
        this.parentId = parentId;
    }

    public String getFullId()
    {
        return this.fullId;
    }

    public void setFullId(String fullId)
    {
        this.fullId = fullId;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDetail()
    {
        return this.detail;
    }

    public void setDetail(String detail)
    {
        this.detail = detail;
    }

    public Integer getLevel()
    {
        return this.level;
    }

    public void setLevel(Integer level)
    {
        this.level = level;
    }

    public Integer getIsLeaf()
    {
        return this.isLeaf;
    }

    public void setIsLeaf(Integer isLeaf)
    {
        this.isLeaf = isLeaf;
    }

    public Integer getState()
    {
        return this.state;
    }

    public void setState(Integer state)
    {
        this.state = state;
    }

    public Integer getSeq()
    {
        return this.seq;
    }

    public void setSeq(Integer seq)
    {
        this.seq = seq;
    }

}