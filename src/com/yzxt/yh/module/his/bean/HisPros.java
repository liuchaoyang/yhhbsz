package com.yzxt.yh.module.his.bean;

import java.math.BigDecimal;

import com.yzxt.yh.module.sys.bean.Doctor;

/**
 * HisPros entity. @author MyEclipse Persistence Tools
 */

public class HisPros  implements java.io.Serializable {


    // Fields    
	private static final long serialVersionUID = 1L;
     private String prosid;
     private String patientid;
     private String patientname;
     private String doctorid;
     private String ypid;
     private String ypgg;
     private String userage;
     private String uselevel;
     
     public String getUselevel() {
		return uselevel;
	}

	public void setUselevel(String uselevel) {
		this.uselevel = uselevel;
	}

	public String getYpgg() {
		return ypgg;
	}

	public void setYpgg(String ypgg) {
		this.ypgg = ypgg;
	}

	public String getUserage() {
		return userage;
	}

	public void setUserage(String userage) {
		this.userage = userage;
	}

	private String custName;
     public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getYpmc() {
		return ypmc;
	}

	public void setYpmc(String ypmc) {
		this.ypmc = ypmc;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	private String ypmc;
    private BigDecimal price;
    private Integer unit;
    public Integer getUnit() {
		return unit;
	}

	public void setUnit(Integer unit) {
		this.unit = unit;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	private Integer num;
     public String getYpid() {
		return ypid;
	}

	public void setYpid(String ypid) {
		this.ypid = ypid;
	}

	private Integer status;
     
     public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	private Doctor doctor;

    public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public String getProsid() {
        return this.prosid;
    }
    
    public void setProsid(String prosid) {
        this.prosid = prosid;
    }

    public String getPatientid() {
        return this.patientid;
    }
    
    public void setPatientid(String patientid) {
        this.patientid = patientid;
    }

    public String getPatientname() {
        return this.patientname;
    }
    
    public void setPatientname(String patientname) {
        this.patientname = patientname;
    }

    public String getDoctorid() {
        return this.doctorid;
    }
    
    public void setDoctorid(String doctorid) {
        this.doctorid = doctorid;
    }
   








}