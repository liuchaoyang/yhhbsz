package com.yzxt.yh.module.sys.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;

public class User implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private Integer state;
    private Integer type;
    private String account;
    private String code;
    public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	private String phone;
    private String email;
    private String idCard;
    private String name;
    private Integer sex;
    private Integer jjLinkman;
    public Integer getJjLinkman() {
		return jjLinkman;
	}

	public void setJjLinkman(Integer jjLinkman) {
		this.jjLinkman = jjLinkman;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}


	private String address;
    public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}


	private String imgFileId;
    private String orgId;
    private String createBy;
    private Timestamp createTime;
    private String updateBy;
    private Timestamp updateTime;

    // 非持久化数据扩展
    private String password;
    private String funcs;
    private Collection<String> roles;
    private String imgFilePath;
    private Date startDay;
    private Date endDay;
    private String doctorId;
    private FileDesc iconFile;
    private String orgName;

    public FileDesc getIconFile()
    {
        return iconFile;
    }

    public void setIconFile(FileDesc iconFile)
    {
        this.iconFile = iconFile;
    }

    public Date getStartDay()
    {
        return startDay;
    }

    public void setStartDay(Date startDay)
    {
        this.startDay = startDay;
    }

    public Date getEndDay()
    {
        return endDay;
    }

    public void setEndDay(Date endDay)
    {
        this.endDay = endDay;
    }

    public String getDoctorId()
    {
        return doctorId;
    }

    public void setDoctorId(String doctorId)
    {
        this.doctorId = doctorId;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public Integer getState()
    {
        return state;
    }

    public void setState(Integer state)
    {
        this.state = state;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public String getAccount()
    {
        return account;
    }

    public void setAccount(String account)
    {
        this.account = account;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getIdCard()
    {
        return idCard;
    }

    public void setIdCard(String idCard)
    {
        this.idCard = idCard;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getImgFileId()
    {
        return imgFileId;
    }

    public void setImgFileId(String imgFileId)
    {
        this.imgFileId = imgFileId;
    }

    public String getOrgId()
    {
        return orgId;
    }

    public void setOrgId(String orgId)
    {
        this.orgId = orgId;
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

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getFuncs()
    {
        return funcs;
    }

    public void setFuncs(String funcs)
    {
        this.funcs = funcs;
    }

    public Collection<String> getRoles()
    {
        return roles;
    }

    public void setRoles(Collection<String> roles)
    {
        this.roles = roles;
    }

    public String getImgFilePath()
    {
        return imgFilePath;
    }

    public void setImgFilePath(String imgFilePath)
    {
        this.imgFilePath = imgFilePath;
    }

    public String getOrgName()
    {
        return orgName;
    }

    public void setOrgName(String orgName)
    {
        this.orgName = orgName;
    }

}