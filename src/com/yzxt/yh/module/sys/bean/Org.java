package com.yzxt.yh.module.sys.bean;

import java.io.Serializable;
import java.sql.Timestamp;

import com.yzxt.yh.util.StringUtil;

public class Org implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String parentId;
    private String fullId;
    private Integer level;
    private String name;
    private String mnemonicCode;
    private String address;
    private String createBy;
    private Timestamp createTime;
    private String updateBy;
    private Timestamp updateTime;
    private String phone;
    private String contactPerson;
    private String logoId;
    private String showText;
    // 非持久化数据扩展
    private Integer isLeaf;
    private String state;
    private String parentName;
    private String logoPath;
    private transient FileDesc logo;

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

    public String getFullId()
    {
        return fullId;
    }

    public void setFullId(String fullId)
    {
        this.fullId = fullId;
    }

    public Integer getLevel()
    {
        return level;
    }

    public void setLevel(Integer level)
    {
        this.level = level;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getMnemonicCode()
    {
        return mnemonicCode;
    }

    public void setMnemonicCode(String mnemonicCode)
    {
        this.mnemonicCode = mnemonicCode;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
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

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getContactPerson()
    {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson)
    {
        this.contactPerson = contactPerson;
    }

    public String getLogoId()
    {
        return logoId;
    }

    public void setLogoId(String logoId)
    {
        this.logoId = logoId;
    }

    public String getShowText()
    {
        return showText;
    }

    public void setShowText(String showText)
    {
        this.showText = showText;
    }

    public Integer getIsLeaf()
    {
        return isLeaf;
    }

    public void setIsLeaf(Integer isLeaf)
    {
        this.isLeaf = isLeaf;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getParentName()
    {
        return parentName;
    }

    public void setParentName(String parentName)
    {
        this.parentName = parentName;
    }

    public String getLogoPath()
    {
        return logoPath;
    }

    public void setLogoPath(String logoPath)
    {
        this.logoPath = logoPath;
    }

    public FileDesc getLogo()
    {
        return logo;
    }

    public void setLogo(FileDesc logo)
    {
        this.logo = logo;
    }

    /**
     * 获取对外显示的ID
     * @return
     */
    public String getShowId()
    {
        return StringUtil.isNotEmpty(mnemonicCode) ? mnemonicCode : id;
    }
}