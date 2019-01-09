package com.yzxt.yh.module.chk.bean;

import java.sql.Timestamp;

import com.google.gson.annotations.SerializedName;

/**
 * ExamInoculate entity. @author MyEclipse Persistence Tools
 */

public class ExamInoculate implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 3258598003964994411L;
	private String id;
	private String examId;
	@SerializedName("jiezhongMingcheng")
	private String inoculateName;
	@SerializedName("jiezhongRiqi")
	private Timestamp inoculateTime;
	@SerializedName("jiezhongJigou")
	private String inoculateDept;
	private Integer seq;

	// Constructors

	/** default constructor */
	public ExamInoculate() {
	}

	/** minimal constructor */
	public ExamInoculate(String examId) {
		this.examId = examId;
	}

	/** full constructor */
	public ExamInoculate(String examId, String inoculateName,
			Timestamp inoculateTime, String inoculateDept) {
		this.examId = examId;
		this.inoculateName = inoculateName;
		this.inoculateTime = inoculateTime;
		this.inoculateDept = inoculateDept;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getExamId() {
		return this.examId;
	}

	public void setExamId(String examId) {
		this.examId = examId;
	}

	public String getInoculateName() {
		return this.inoculateName;
	}

	public void setInoculateName(String inoculateName) {
		this.inoculateName = inoculateName;
	}

	public Timestamp getInoculateTime() {
		return this.inoculateTime;
	}

	public void setInoculateTime(Timestamp inoculateTime) {
		this.inoculateTime = inoculateTime;
	}

	public String getInoculateDept() {
		return this.inoculateDept;
	}

	public void setInoculateDept(String inoculateDept) {
		this.inoculateDept = inoculateDept;
	}

    public Integer getSeq()
    {
        return seq;
    }

    public void setSeq(Integer seq)
    {
        this.seq = seq;
    }

}