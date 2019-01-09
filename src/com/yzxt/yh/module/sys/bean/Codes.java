package com.yzxt.yh.module.sys.bean;

import java.sql.Timestamp;
import java.util.Date;

public class Codes implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String code;
    private String phone;
    private Date expiryTime;
    private Timestamp createTime;
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Date getExpiryTime() {
		return expiryTime;
	}
	public void setExpiryTime(Date expiryTime) {
		this.expiryTime = expiryTime;
	}
}