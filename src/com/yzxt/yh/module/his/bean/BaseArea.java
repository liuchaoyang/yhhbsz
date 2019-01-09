package com.yzxt.yh.module.his.bean;

public class BaseArea implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	private Integer codeId;
	private Integer parentId;
	private String 	cityName;
	public Integer getCodeId() {
		return codeId;
	}
	public void setCodeId(Integer codeId) {
		this.codeId = codeId;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
}
