package com.yzxt.yh.module.chk.bean;

import com.google.gson.annotations.SerializedName;


public class ExamPosion implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
	private String examId;
	private String posionName;
	private Double workTime;
	@SerializedName("protectiveMeasure")
	private String safeguard;
	private Integer seq;

	// Constructors

	/** default constructor */
	public ExamPosion() {
	}

	/** minimal constructor */
	public ExamPosion(String examId) {
		this.examId = examId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getExamId() {
		return examId;
	}

	public void setExamId(String examId) {
		this.examId = examId;
	}

	public String getPosionName() {
		return posionName;
	}

	public void setPosionName(String posionName) {
		this.posionName = posionName;
	}


	public Double getWorkTime() {
		return workTime;
	}

	public void setWorkTime(Double workTime) {
		this.workTime = workTime;
	}

	public String getSafeguard() {
		return safeguard;
	}

	public void setSafeguard(String safeguard) {
		this.safeguard = safeguard;
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