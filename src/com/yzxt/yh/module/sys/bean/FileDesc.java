package com.yzxt.yh.module.sys.bean;

public class FileDesc implements java.io.Serializable
{

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String path;
    private String extName;
    private Long fileSize;

    public String getId()
    {
        return this.id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPath()
    {
        return this.path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public String getExtName()
    {
        return this.extName;
    }

    public void setExtName(String extName)
    {
        this.extName = extName;
    }

    public Long getFileSize()
    {
        return this.fileSize;
    }

    public void setFileSize(Long fileSize)
    {
        this.fileSize = fileSize;
    }

}