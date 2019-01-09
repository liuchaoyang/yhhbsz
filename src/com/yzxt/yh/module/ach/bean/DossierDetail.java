package com.yzxt.yh.module.ach.bean;

import java.io.Serializable;

public class DossierDetail implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String dossierId;
    private String fileId;
    private Integer seq;
    // 非持久化属性
    private String path;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getDossierId()
    {
        return dossierId;
    }

    public void setDossierId(String dossierId)
    {
        this.dossierId = dossierId;
    }

    public String getFileId()
    {
        return fileId;
    }

    public void setFileId(String fileId)
    {
        this.fileId = fileId;
    }

    public Integer getSeq()
    {
        return seq;
    }

    public void setSeq(Integer seq)
    {
        this.seq = seq;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

}
