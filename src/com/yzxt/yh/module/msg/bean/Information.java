package com.yzxt.yh.module.msg.bean;

import java.sql.Timestamp;

import com.yzxt.yh.module.sys.bean.FileDesc;

public class Information implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String title;
    private String src;
    private String richtextId;
    private Integer level;
    private String iconFileId;
    private String createBy;
    private Timestamp createTime;
    private String updateBy;
    private Timestamp updateTime;

    // 非持久化字段
    // 富文本信息
    private String summary;

    private String content;

    private FileDesc iconFile;

    private String iconFileName;

    private String iconFileExtName;

    private String iconFilePath;

    private String belongColumns;

    private String belongTopic;

    private String belongTopicName;

    private String createByName;

    private String updateByName;

    public String getId()
    {
        return this.id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return this.title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getSrc()
    {
        return this.src;
    }

    public void setSrc(String src)
    {
        this.src = src;
    }

    public String getRichtextId()
    {
        return this.richtextId;
    }

    public void setRichtextId(String richtextId)
    {
        this.richtextId = richtextId;
    }

    public Integer getLevel()
    {
        return this.level;
    }

    public void setLevel(Integer level)
    {
        this.level = level;
    }

    public String getIconFileId()
    {
        return this.iconFileId;
    }

    public void setIconFileId(String iconFileId)
    {
        this.iconFileId = iconFileId;
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

    public FileDesc getIconFile()
    {
        return iconFile;
    }

    public void setIconFile(FileDesc iconFile)
    {
        this.iconFile = iconFile;
    }

    public String getIconFileName()
    {
        return iconFileName;
    }

    public void setIconFileName(String iconFileName)
    {
        this.iconFileName = iconFileName;
    }

    public String getIconFileExtName()
    {
        return iconFileExtName;
    }

    public void setIconFileExtName(String iconFileExtName)
    {
        this.iconFileExtName = iconFileExtName;
    }

    public String getIconFilePath()
    {
        return iconFilePath;
    }

    public void setIconFilePath(String iconFilePath)
    {
        this.iconFilePath = iconFilePath;
    }

    public String getBelongColumns()
    {
        return belongColumns;
    }

    public void setBelongColumns(String belongColumns)
    {
        this.belongColumns = belongColumns;
    }

    public String getBelongTopic()
    {
        return belongTopic;
    }

    public void setBelongTopic(String belongTopic)
    {
        this.belongTopic = belongTopic;
    }

    public String getBelongTopicName()
    {
        return belongTopicName;
    }

    public void setBelongTopicName(String belongTopicName)
    {
        this.belongTopicName = belongTopicName;
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

}