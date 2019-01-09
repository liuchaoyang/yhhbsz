package com.yzxt.yh.module.sys.bean;

import java.util.List;

public class Depts implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	private Integer codeId;
	private Integer parentId;
	private String 	deptName;
	
	private List<Depts> childData;
	
	public List<Depts> getChildData() {
		return childData;
	}
	public void setChildData(List<Depts> childData) {
		this.childData = childData;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
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
}
