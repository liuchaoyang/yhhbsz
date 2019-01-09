package com.yzxt.yh.module.sys.bean;

import java.sql.Timestamp;
import java.util.Date;

public class Doctor implements java.io.Serializable
{

    private static final long serialVersionUID = 1L;

    private String userId;
    private Integer sex;
    private Date birthday;
    private String address;
    private Integer degree;
    private String professionTitle;
    private String deptName;
    private Integer yprice;
    private Date startDay;
    private Date endDay;
	public Date getStartDay() {
		return startDay;
	}

	public void setStartDay(Date startDay) {
		this.startDay = startDay;
	}

	public Date getEndDay() {
		return endDay;
	}

	public void setEndDay(Date endDay) {
		this.endDay = endDay;
	}

	public Integer getYprice() {
		return yprice;
	}

	public void setYprice(Integer yprice) {
		this.yprice = yprice;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	private Integer price;
    private String speciality;
   

	public String getSpeciality() {
		return speciality;
	}

	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}


	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	private String deptId;
    public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	private String skillInfo;
    private String describe;
    private String createBy;
    private Timestamp createTime;
    private String updateBy;
    private Timestamp updateTime;

    // 扩展属性
    private User user;
    private String doctorName;
    private String account;
    private String phone;
    private String email;
    private String idCard;
    private String orgName;
    private String orgId;
    private Integer type;
    private Integer state;

    public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getUserId()
    {
        return this.userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
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

    public String getAddress()
    {
        return this.address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public Integer getDegree()
    {
        return this.degree;
    }

    public void setDegree(Integer degree)
    {
        this.degree = degree;
    }

    public String getProfessionTitle()
    {
        return this.professionTitle;
    }

    public void setProfessionTitle(String professionTitle)
    {
        this.professionTitle = professionTitle;
    }


    public String getSkillInfo()
    {
        return this.skillInfo;
    }

    public void setSkillInfo(String skillInfo)
    {
        this.skillInfo = skillInfo;
    }

    public String getDescribe()
    {
        return this.describe;
    }

    public void setDescribe(String describe)
    {
        this.describe = describe;
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

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public String getAccount()
    {
        return account;
    }

    public String getDoctorName()
    {
        return doctorName;
    }

    public void setDoctorName(String doctorName)
    {
        this.doctorName = doctorName;
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

    public String getOrgName()
    {
        return orgName;
    }

    public void setOrgName(String orgName)
    {
        this.orgName = orgName;
    }

    public String getOrgId()
    {
        return orgId;
    }

    public void setOrgId(String orgId)
    {
        this.orgId = orgId;
    }

}