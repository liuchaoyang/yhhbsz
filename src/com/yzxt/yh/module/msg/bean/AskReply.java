package com.yzxt.yh.module.msg.bean;

import java.sql.Timestamp;

public class AskReply implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String askId;
    private String richtextId;
    private String createBy;
    private Timestamp createTime;
    private String updateBy;
    private Timestamp updateTime;

    // 非持久化字段
    private String summary;
    private String content;
    private String title;
    private String createBys;
    private Timestamp createTimes;
    
    private Integer type;
    public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreateBys() {
		return createBys;
	}

	public void setCreateBys(String createBys) {
		this.createBys = createBys;
	}

	public Timestamp getCreateTimes() {
		return createTimes;
	}

	public void setCreateTimes(Timestamp createTimes) {
		this.createTimes = createTimes;
	}

	private String updateByName;
    private Integer updateByUserType;

    public String getId()
    {
        return this.id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getAskId()
    {
        return this.askId;
    }

    public void setAskId(String askId)
    {
        this.askId = askId;
    }

    public String getRichtextId()
    {
        return this.richtextId;
    }

    public void setRichtextId(String richtextId)
    {
        this.richtextId = richtextId;
    }

    public String getCreateBy()
    {
        return this.createBy;
    }

    public void setCreateBy(String createBy)
    {
        this.createBy = createBy;
    }

    public Timestamp getCreateTime()
    {
        return this.createTime;
    }

    public void setCreateTime(Timestamp createTime)
    {
        this.createTime = createTime;
    }

    public String getUpdateBy()
    {
        return this.updateBy;
    }

    public void setUpdateBy(String updateBy)
    {
        this.updateBy = updateBy;
    }

    public Timestamp getUpdateTime()
    {
        return this.updateTime;
    }

    public void setUpdateTime(Timestamp updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getSummary()
    {
        return summary;
    }

    public void setSummary(String summary)
    {
        this.summary = summary;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getUpdateByName()
    {
        return updateByName;
    }

    public void setUpdateByName(String updateByName)
    {
        this.updateByName = updateByName;
    }

    public Integer getUpdateByUserType()
    {
        return updateByUserType;
    }

    public void setUpdateByUserType(Integer updateByUserType)
    {
        this.updateByUserType = updateByUserType;
    }

}