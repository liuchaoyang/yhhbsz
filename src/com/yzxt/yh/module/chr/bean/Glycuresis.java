package com.yzxt.yh.module.chr.bean;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Glycuresis implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String custId;
    private String visitId;
    private Date flupDate;
    private Integer flupType;
    private Integer flupRsult;
    private String doctorId;
    @SerializedName("nextFlupDate")
    private Timestamp nextFlupTime;
    private transient String glySymptom;
    @SerializedName("glySymptomOther")
    private String glySymptomOther;
    @SerializedName("glySysBP")
    private Integer hbpBps;
    @SerializedName("glyDiaBP")
    private Integer hbpBpd;
    @SerializedName("glyWeight")
    private Double hbpWeight;
    @SerializedName("glyPhysique")
    private Double hbpPhysique;
    @SerializedName("glyFootback")
    private Integer hbpFootBack;
    @SerializedName("glyOther")
    private String hbpOther;
    @SerializedName("glySmoking")
    private Integer hbpSmoking;
    @SerializedName("glyDrinking")
    private Integer hbpDrinking;
    @SerializedName("glyMainFood")
    private Integer hbpFood;
    @SerializedName("glyPsycrecovery")
    private Integer hbpPsycrecovery;
    @SerializedName("glyCompliance")
    private Integer hbpCompliance;
    @SerializedName("glyFastGlu")
    private Double hbpFastGlu;
    @SerializedName("glyDurgsObey")
    private Integer hbpDurgsObey;
    @SerializedName("glyDrugsUntoward")
    private Integer hbpDrugsUntoward;
    @SerializedName("glyLowGlu")
    private Integer hbpLowSuger;
    @SerializedName("glyReferWhy")
    private String hbpReferWhy;
    @SerializedName("glyReferOrgan")
    private String hbpReferOrg;
    @SerializedName("glyReferGrade")
    private String hbpReferObj;
    private Timestamp createTime;
    private Integer status;

    private String doctorName;
    private String memberName;
    private String visitNo;
    private String idCard;

    private transient String checks;
    private transient String drugs;
    private transient String sports;

    private transient String flupDateStr;
    private transient String nextFlupTimeStr;
    private transient List<GlyCheck> glyCheck;
    private transient List<GlySport> glySport;
    private transient List<GlyDrug> glyDrug;

    public String getId()
    {
        return this.id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getCustId()
    {
        return this.custId;
    }

    public void setCustId(String custId)
    {
        this.custId = custId;
    }

    public String getVisitId()
    {
        return this.visitId;
    }

    public void setVisitId(String visitId)
    {
        this.visitId = visitId;
    }

    public Date getFlupDate()
    {
        return this.flupDate;
    }

    public void setFlupDate(Date flupDate)
    {
        this.flupDate = flupDate;
    }

    public Integer getFlupType()
    {
        return this.flupType;
    }

    public void setFlupType(Integer flupType)
    {
        this.flupType = flupType;
    }

    public Integer getFlupRsult()
    {
        return this.flupRsult;
    }

    public void setFlupRsult(Integer flupRsult)
    {
        this.flupRsult = flupRsult;
    }

    public String getDoctorId()
    {
        return this.doctorId;
    }

    public void setDoctorId(String doctorId)
    {
        this.doctorId = doctorId;
    }

    public Timestamp getNextFlupTime()
    {
        return this.nextFlupTime;
    }

    public void setNextFlupTime(Timestamp nextFlupTime)
    {
        this.nextFlupTime = nextFlupTime;
    }

    public String getGlySymptom()
    {
        return glySymptom;
    }

    public void setGlySymptom(String glySymptom)
    {
        this.glySymptom = glySymptom;
    }

    public String getGlySymptomOther()
    {
        return glySymptomOther;
    }

    public void setGlySymptomOther(String glySymptomOther)
    {
        this.glySymptomOther = glySymptomOther;
    }

    public Integer getHbpBps()
    {
        return this.hbpBps;
    }

    public void setHbpBps(Integer hbpBps)
    {
        this.hbpBps = hbpBps;
    }

    public Integer getHbpBpd()
    {
        return this.hbpBpd;
    }

    public void setHbpBpd(Integer hbpBpd)
    {
        this.hbpBpd = hbpBpd;
    }

    public Double getHbpWeight()
    {
        return this.hbpWeight;
    }

    public void setHbpWeight(Double hbpWeight)
    {
        this.hbpWeight = hbpWeight;
    }

    public Double getHbpPhysique()
    {
        return this.hbpPhysique;
    }

    public void setHbpPhysique(Double hbpPhysique)
    {
        this.hbpPhysique = hbpPhysique;
    }

    public Integer getHbpFootBack()
    {
        return this.hbpFootBack;
    }

    public void setHbpFootBack(Integer hbpFootBack)
    {
        this.hbpFootBack = hbpFootBack;
    }

    public String getHbpOther()
    {
        return this.hbpOther;
    }

    public void setHbpOther(String hbpOther)
    {
        this.hbpOther = hbpOther;
    }

    public Integer getHbpSmoking()
    {
        return this.hbpSmoking;
    }

    public void setHbpSmoking(Integer hbpSmoking)
    {
        this.hbpSmoking = hbpSmoking;
    }

    public Integer getHbpDrinking()
    {
        return this.hbpDrinking;
    }

    public void setHbpDrinking(Integer hbpDrinking)
    {
        this.hbpDrinking = hbpDrinking;
    }

    public Integer getHbpFood()
    {
        return this.hbpFood;
    }

    public void setHbpFood(Integer hbpFood)
    {
        this.hbpFood = hbpFood;
    }

    public Integer getHbpPsycrecovery()
    {
        return this.hbpPsycrecovery;
    }

    public void setHbpPsycrecovery(Integer hbpPsycrecovery)
    {
        this.hbpPsycrecovery = hbpPsycrecovery;
    }

    public Integer getHbpCompliance()
    {
        return this.hbpCompliance;
    }

    public void setHbpCompliance(Integer hbpCompliance)
    {
        this.hbpCompliance = hbpCompliance;
    }

    public Double getHbpFastGlu()
    {
        return this.hbpFastGlu;
    }

    public void setHbpFastGlu(Double hbpFastGlu)
    {
        this.hbpFastGlu = hbpFastGlu;
    }

    public Integer getHbpDurgsObey()
    {
        return this.hbpDurgsObey;
    }

    public void setHbpDurgsObey(Integer hbpDurgsObey)
    {
        this.hbpDurgsObey = hbpDurgsObey;
    }

    public Integer getHbpDrugsUntoward()
    {
        return this.hbpDrugsUntoward;
    }

    public void setHbpDrugsUntoward(Integer hbpDrugsUntoward)
    {
        this.hbpDrugsUntoward = hbpDrugsUntoward;
    }

    public Integer getHbpLowSuger()
    {
        return this.hbpLowSuger;
    }

    public void setHbpLowSuger(Integer hbpLowSuger)
    {
        this.hbpLowSuger = hbpLowSuger;
    }

    public String getHbpReferWhy()
    {
        return this.hbpReferWhy;
    }

    public void setHbpReferWhy(String hbpReferWhy)
    {
        this.hbpReferWhy = hbpReferWhy;
    }

    public String getHbpReferOrg()
    {
        return this.hbpReferOrg;
    }

    public void setHbpReferOrg(String hbpReferOrg)
    {
        this.hbpReferOrg = hbpReferOrg;
    }

    public String getHbpReferObj()
    {
        return this.hbpReferObj;
    }

    public void setHbpReferObj(String hbpReferObj)
    {
        this.hbpReferObj = hbpReferObj;
    }

    public Timestamp getCreateTime()
    {
        return this.createTime;
    }

    public void setCreateTime(Timestamp createTime)
    {
        this.createTime = createTime;
    }

    public Integer getStatus()
    {
        return this.status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public String getDoctorName()
    {
        return doctorName;
    }

    public void setDoctorName(String doctorName)
    {
        this.doctorName = doctorName;
    }

    public String getMemberName()
    {
        return memberName;
    }

    public void setMemberName(String memberName)
    {
        this.memberName = memberName;
    }

    public String getVisitNo()
    {
        return visitNo;
    }

    public void setVisitNo(String visitNo)
    {
        this.visitNo = visitNo;
    }

    public String getChecks()
    {
        return checks;
    }

    public void setChecks(String checks)
    {
        this.checks = checks;
    }

    public String getDrugs()
    {
        return drugs;
    }

    public void setDrugs(String drugs)
    {
        this.drugs = drugs;
    }

    public String getSports()
    {
        return sports;
    }

    public void setSports(String sports)
    {
        this.sports = sports;
    }

    public String getFlupDateStr()
    {
        return flupDateStr;
    }

    public void setFlupDateStr(String flupDateStr)
    {
        this.flupDateStr = flupDateStr;
    }

    public String getNextFlupTimeStr()
    {
        return nextFlupTimeStr;
    }

    public void setNextFlupTimeStr(String nextFlupTimeStr)
    {
        this.nextFlupTimeStr = nextFlupTimeStr;
    }

    public List<GlyCheck> getGlyCheck()
    {
        return glyCheck;
    }

    public void setGlyCheck(List<GlyCheck> glyCheck)
    {
        this.glyCheck = glyCheck;
    }

    public List<GlySport> getGlySport()
    {
        return glySport;
    }

    public void setGlySport(List<GlySport> glySport)
    {
        this.glySport = glySport;
    }

    public List<GlyDrug> getGlyDrug()
    {
        return glyDrug;
    }

    public void setGlyDrug(List<GlyDrug> glyDrug)
    {
        this.glyDrug = glyDrug;
    }

    public String getIdCard()
    {
        return idCard;
    }

    public void setIdCard(String idCard)
    {
        this.idCard = idCard;
    }

}