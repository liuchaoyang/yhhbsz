package com.yzxt.yh.module.chk.bean;

import com.google.gson.annotations.SerializedName;


public class ExamMedic implements java.io.Serializable
{

    private static final long serialVersionUID = 1L;
    private String id;
    private String examId;
    @SerializedName("drugName")
    private String medName;
    @SerializedName("usage")
    private String useType;
    @SerializedName("dosage")
    private String useNum;
    @SerializedName("userTiem")
    private String useTime;
    @SerializedName("compliance")
    private Integer adhes;
    private Integer seq;


    public ExamMedic() {
    }

    public ExamMedic(String examId) {
        this.examId = examId;
    }

    public ExamMedic(String examId, String medName, String useType,
            String useNum, String useTime, Integer adhes) {
        this.examId = examId;
        this.medName = medName;
        this.useType = useType;
        this.useNum = useNum;
        this.useTime = useTime;
        this.adhes = adhes;
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

    public String getMedName() {
        return this.medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public String getUseType() {
        return this.useType;
    }

    public void setUseType(String useType) {
        this.useType = useType;
    }

    public String getUseNum() {
        return this.useNum;
    }

    public void setUseNum(String useNum) {
        this.useNum = useNum;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public Integer getAdhes() {
        return this.adhes;
    }

    public void setAdhes(Integer adhes) {
        this.adhes = adhes;
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