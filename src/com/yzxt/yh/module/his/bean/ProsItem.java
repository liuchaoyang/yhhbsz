package com.yzxt.yh.module.his.bean;

import java.math.BigDecimal;


/**
 * ProsItem entity. @author MyEclipse Persistence Tools
 */

public class ProsItem  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
    // Fields    

     private String itemid;
     private String prosid;
     private String userage;
     private String uselevel;
     private Integer unit;

	public Integer getUnit() {
		return unit;
	}

	public void setUnit(Integer unit) {
		this.unit = unit;
	}

	public String getUselevel() {
		return uselevel;
	}

	public void setUselevel(String uselevel) {
		this.uselevel = uselevel;
	}

	public String getUserage() {
		return userage;
	}

	public void setUserage(String userage) {
		this.userage = userage;
	}

	 private Integer num;
     private BigDecimal price;
    

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	private String title;
     private Long totalfee;



   
    // Property accessors

    public String getItemid() {
        return this.itemid;
    }
    
    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getProsid() {
        return this.prosid;
    }
    
    public void setProsid(String prosid) {
        this.prosid = prosid;
    }

    public Integer getNum() {
        return this.num;
    }
    
    public void setNum(Integer num) {
        this.num = num;
    }


    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTotalfee() {
        return this.totalfee;
    }
    
    public void setTotalfee(Long totalfee) {
        this.totalfee = totalfee;
    }
   








}