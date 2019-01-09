package com.yzxt.yh.module.ach.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.yzxt.yh.module.sys.bean.FileDesc;

public class Dossier implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String custId;
    private Integer type;
    private String memo;
    private String createBy;
    private Timestamp createTime;
    private String updateBy;
    private Timestamp updateTime;

    // 非持久化字段
    private String custName;
    private String typeName;
    private List<DossierDetail> details;
    private List<FileDesc> fileDescs;
    private String paths;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getCustId()
    {
        return custId;
    }

    public void setCustId(String custId)
    {
        this.custId = custId;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public String getMemo()
    {
        return memo;
    }

    public void setMemo(String memo)
    {
        this.memo = memo;
    }

    public String getCreateBy()
    {
        return createBy;
    }

    public void setCreateBy(String createBy)
    {
        this.createBy = createBy;
    }

    public Timestamp getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime)
    {
        this.createTime = createTime;
    }

    public String getUpdateBy()
    {
        return updateBy;
    }

    public void setUpdateBy(String updateBy)
    {
        this.updateBy = updateBy;
    }

    public Timestamp getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getCustName()
    {
        return custName;
    }

    public void setCustName(String custName)
    {
        this.custName = custName;
    }

    public String getTypeName()
    {
        return typeName;
    }

    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }

    public List<DossierDetail> getDetails()
    {
        return details;
    }

    public void setDetails(List<DossierDetail> details)
    {
        this.details = details;
    }

    public List<FileDesc> getFileDescs()
    {
        return fileDescs;
    }

    public void setFileDescs(List<FileDesc> fileDescs)
    {
        this.fileDescs = fileDescs;
    }

    public String getPaths()
    {
        return paths;
    }

    public void setPaths(String paths)
    {
        this.paths = paths;
    }

}
