package com.yzxt.yh.module.chk.bean;

import java.sql.Timestamp;

import com.google.gson.annotations.SerializedName;

public class PressurePulse implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String custId;
    private String deviceName;
    private String deviceMac;
    private Double latitude;
    private Double longitude;
    private String unit;
    @SerializedName("d_b_p")
    private Integer DBP;
    @SerializedName("s_b_p")
    private Integer SBP;
    private Integer level;
    private String descript;
    private Timestamp checkTime;
    @SerializedName("uploadTime")
    private Timestamp createTime;
    private Integer checkType;
    private Integer pulse;
    private String userName;
    private Integer type;
    private String memo;
    private String reportId;
    private Integer state;
    
    public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getId()
    {
        return this.id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getCustId()
    {
        return this.custId;
    }

    public void setCustId(String custId)
    {
        this.custId = custId;
    }

    public String getDeviceName()
    {
        return this.deviceName;
    }

    public void setDeviceName(String deviceName)
    {
        this.deviceName = deviceName;
    }

    public String getDeviceMac()
    {
        return this.deviceMac;
    }

    public void setDeviceMac(String deviceMac)
    {
        this.deviceMac = deviceMac;
    }

    public Double getLatitude()
    {
        return this.latitude;
    }

    public void setLatitude(Double latitude)
    {
        this.latitude = latitude;
    }

    public Double getLongitude()
    {
        return this.longitude;
    }

    public void setLongitude(Double longitude)
    {
        this.longitude = longitude;
    }

    public String getUnit()
    {
        return this.unit;
    }

    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public Integer getDBP()
    {
        return this.DBP;
    }

    public void setDBP(Integer DBP)
    {
        this.DBP = DBP;
    }

    public Integer getSBP()
    {
        return this.SBP;
    }

    public void setSBP(Integer SBP)
    {
        this.SBP = SBP;
    }

    public Integer getLevel()
    {
        return this.level;
    }

    public void setLevel(Integer level)
    {
        this.level = level;
    }

    public String getDescript()
    {
        return this.descript;
    }

    public void setDescript(String descript)
    {
        this.descript = descript;
    }

    public Timestamp getCheckTime()
    {
        return this.checkTime;
    }

    public void setCheckTime(Timestamp checkTime)
    {
        this.checkTime = checkTime;
    }

    public Timestamp getCreateTime()
    {
        return this.createTime;
    }

    public void setCreateTime(Timestamp createTime)
    {
        this.createTime = createTime;
    }

    public Integer getCheckType()
    {
        return this.checkType;
    }

    public void setCheckType(Integer checkType)
    {
        this.checkType = checkType;
    }

    public Integer getPulse()
    {
        return pulse;
    }

    public void setPulse(Integer pulse)
    {
        this.pulse = pulse;
    }

}