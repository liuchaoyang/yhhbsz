package com.yzxt.yh.module.msg.bean;

public class Richtext implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String summary;
    private String content;

    public String getId()
    {
        return this.id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getSummary()
    {
        return this.summary;
    }

    public void setSummary(String summary)
    {
        this.summary = summary;
    }

    public String getContent()
    {
        return this.content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

}