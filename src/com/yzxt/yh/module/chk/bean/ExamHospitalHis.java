package com.yzxt.yh.module.chk.bean;

import java.sql.Timestamp;

import com.google.gson.annotations.SerializedName;

public class ExamHospitalHis implements java.io.Serializable
{

    private static final long serialVersionUID = 1L;
    private String id;
    private String examId;
    @SerializedName("ruyuanRiqi")
    private Timestamp inTime;
    @SerializedName("chuyuanRIqi")
    private Timestamp outTime;
    @SerializedName("jigouming")
    private String hosDept;
    @SerializedName("yuanyin")
    private String admissionReason;
    @SerializedName("binganhao")
    private String recordNo;
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

    public Timestamp getInTime()
    {
        return inTime;
    }

    public void setInTime(Timestamp inTime)
    {
        this.inTime = inTime;
    }

    public Timestamp getOutTime()
    {
        return outTime;
    }

    public void setOutTime(Timestamp outTime)
    {
        this.outTime = outTime;
    }

    public String getHosDept()
    {
        return hosDept;
    }

    public void setHosDept(String hosDept)
    {
        this.hosDept = hosDept;
    }

    public String getAdmissionReason()
    {
        return admissionReason;
    }

    public void setAdmissionReason(String admissionReason)
    {
        this.admissionReason = admissionReason;
    }

    public String getRecordNo()
    {
        return recordNo;
    }

    public void setRecordNo(String recordNo)
    {
        this.recordNo = recordNo;
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