package com.yzxt.yh.module.his.bean;

import java.math.BigDecimal;
import java.util.Date;


/**
 * HisYpkc entity. @author MyEclipse Persistence Tools
 */

public class HisYpkc implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
    // Fields    

     private String sqid;
     private String ypmc;
     private String ypjx;
     private String ypgg;
     private Integer unit;
     public Integer getUnit() {
		return unit;
	}
	public void setUnit(Integer unit) {
		this.unit = unit;
	}
	private BigDecimal ypdj;
     
	public BigDecimal getYpdj() {
		return ypdj;
	}
	public void setYpdj(BigDecimal ypdj) {
		this.ypdj = ypdj;
	}
	private Integer kcnum;
     private Date yxdate;
     private Integer ybflag;
     private Integer ypflag;
	public String getSqid() {
		return sqid;
	}
	public void setSqid(String sqid) {
		this.sqid = sqid;
	}
	public String getYpmc() {
		return ypmc;
	}
	public void setYpmc(String ypmc) {
		this.ypmc = ypmc;
	}
	public String getYpjx() {
		return ypjx;
	}
	public void setYpjx(String ypjx) {
		this.ypjx = ypjx;
	}
	public String getYpgg() {
		return ypgg;
	}
	public void setYpgg(String ypgg) {
		this.ypgg = ypgg;
	}
	public Integer getKcnum() {
		return kcnum;
	}
	public void setKcnum(Integer kcnum) {
		this.kcnum = kcnum;
	}
	public Date getYxdate() {
		return yxdate;
	}
	public void setYxdate(Date yxdate) {
		this.yxdate = yxdate;
	}
	public Integer getYbflag() {
		return ybflag;
	}
	public void setYbflag(Integer ybflag) {
		this.ybflag = ybflag;
	}
	public Integer getYpflag() {
		return ypflag;
	}
	public void setYpflag(Integer ypflag) {
		this.ypflag = ypflag;
	}


   




}