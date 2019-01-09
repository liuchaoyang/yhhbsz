package com.yzxt.yh.module.sys.bean;

public class DictDetail implements java.io.Serializable
{

    private static final long serialVersionUID = 1L;

    private String id;
    private String dictCode;
    private String dictDetailCode;
    private String dictDetailName;
    private Integer seqNum;
    private Integer state;

    public String getId()
    {
        return this.id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getDictCode()
    {
        return this.dictCode;
    }

    public void setDictCode(String dictCode)
    {
        this.dictCode = dictCode;
    }

    public String getDictDetailCode()
    {
        return this.dictDetailCode;
    }

    public void setDictDetailCode(String dictDetailCode)
    {
        this.dictDetailCode = dictDetailCode;
    }

    public String getDictDetailName()
    {
        return this.dictDetailName;
    }

    public void setDictDetailName(String dictDetailName)
    {
        this.dictDetailName = dictDetailName;
    }

    public Integer getSeqNum()
    {
        return this.seqNum;
    }

    public void setSeqNum(Integer seqNum)
    {
        this.seqNum = seqNum;
    }

    public Integer getState()
    {
        return this.state;
    }

    public void setState(Integer state)
    {
        this.state = state;
    }

}