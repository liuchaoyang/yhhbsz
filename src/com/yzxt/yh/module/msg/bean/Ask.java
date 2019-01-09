package com.yzxt.yh.module.msg.bean;

import java.sql.Timestamp;
import java.util.Date;

public class Ask implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String catalogId;
    private String title;
    private String richtextId;
    private Integer sex;
    private Date birthday;
    private Integer replyCount;
    private Integer viewCount;
    private String createBy;
    private Timestamp createTime;
    private String updateBy;
    private Timestamp updateTime;
    private String doctorId;
    private Integer state;
    
    public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	private Integer type;
    public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	// 非持久化字段
    private String parentCatalogId;
    private String summary;
    private String content;
    private String createByName;
    private String updateByName;
    private AskReply askReply;

    public String getId()
    {
        return this.id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getCatalogId()
    {
        return this.catalogId;
    }

    public void setCatalogId(String catalogId)
    {
        this.catalogId = catalogId;
    }

    public String getTitle()
    {
        return this.title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getRichtextId()
    {
        return this.richtextId;
    }

    public void setRichtextId(String richtextId)
    {
        this.richtextId = richtextId;
    }

    public Integer getSex()
    {
        return this.sex;
    }

    public void setSex(Integer sex)
    {
        this.sex = sex;
    }

    public Date getBirthday()
    {
        return this.birthday;
    }

    public void setBirthday(Date birthday)
    {
        this.birthday = birthday;
    }

    public Integer getReplyCount()
    {
        return this.replyCount;
    }

    public void setReplyCount(Integer replyCount)
    {
        this.replyCount = replyCount;
    }

    public Integer getViewCount()
    {
        return this.viewCount;
    }

    public void setViewCount(Integer viewCount)
    {
        this.viewCount = viewCount;
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

    public String getParentCatalogId()
    {
        return parentCatalogId;
    }

    public void setParentCatalogId(String parentCatalogId)
    {
        this.parentCatalogId = parentCatalogId;
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

    public String getCreateByName()
    {
        return createByName;
    }

    public void setCreateByName(String createByName)
    {
        this.createByName = createByName;
    }

    public String getUpdateByName()
    {
        return updateByName;
    }

    public void setUpdateByName(String updateByName)
    {
        this.updateByName = updateByName;
    }

    public AskReply getAskReply()
    {
        return askReply;
    }

    public void setAskReply(AskReply askReply)
    {
        this.askReply = askReply;
    }

}