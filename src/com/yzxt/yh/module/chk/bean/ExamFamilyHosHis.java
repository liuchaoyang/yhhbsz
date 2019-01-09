package com.yzxt.yh.module.chk.bean;

import java.sql.Timestamp;

import com.google.gson.annotations.SerializedName;

public class ExamFamilyHosHis implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String examId;
    private String userName;
    private String recordNo;
    @SerializedName("jianchuangRiqi")
    private Timestamp createBedTime;
    @SerializedName("chechuangRIqi")
    private Timestamp putBedTime;
    @SerializedName("yuanyin")
    private String reason;
    @SerializedName("jigouming")
    private String hosUnit;
    @SerializedName("binganhao")
    private String medRecordNo;
    private Integer seq;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getExamId()
    {
        return examId;
    }

    public void setExamId(String examId)
    {
        this.examId = examId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getRecordNo()
    {
        return recordNo;
    }

    public void setRecordNo(String recordNo)
    {
        this.recordNo = recordNo;
    }

    public Timestamp getCreateBedTime()
    {
        return createBedTime;
    }

    public void setCreateBedTime(Timestamp createBedTime)
    {
        this.createBedTime = createBedTime;
    }

    public Timestamp getPutBedTime()
    {
        return putBedTime;
    }

    public void setPutBedTime(Timestamp putBedTime)
    {
        this.putBedTime = putBedTime;
    }

    public String getReason()
    {
        return reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public String getHosUnit()
    {
        return hosUnit;
    }

    public void setHosUnit(String hosUnit)
    {
        this.hosUnit = hosUnit;
    }

    public String getMedRecordNo()
    {
        return medRecordNo;
    }

    public void setMedRecordNo(String medRecordNo)
    {
        this.medRecordNo = medRecordNo;
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