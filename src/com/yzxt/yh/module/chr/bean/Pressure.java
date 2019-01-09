package com.yzxt.yh.module.chr.bean;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Pressure implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String custId;
    @SerializedName("flupTaskId")
    private String visitId;
    private Date flupDate;
    private Integer flupType;
    private Integer flupRsult;
    private String doctorId;
    @SerializedName("nextFlupDate")
    private Timestamp nextFlupTime;
    private String hbpSymptom;
    private String hbpSymptomOther;
    @SerializedName("hbpSysBP")
    private Integer hbpBps;
    @SerializedName("hbpDiaBP")
    private Integer hbpBpd;
    private Double hbpWeight;
    private Double hbpPhysique;
    private Integer hbpPulse;
    private String hbpOther;
    private Integer hbpSmoking;
    private Double hbpDrinking;
    private Integer hbpSalarium;
    private Integer hbpPsycrecovery;
    private Integer hbpCompliance;
    private String hbpHelpCheck;
    private Integer hbpDurgsObey;
    private String hbpDrugsUntoward;
    private String hbpReferWhy;
    @SerializedName("hbpReferOrganization")
    private String hbpReferOrg;
    @SerializedName("hbpReferGrade")
    private String hbpReferObj;
    private Timestamp createTime;
    private Integer status;

    private String doctorName;
    private String memberName;
    private String visitNo;
    private String idcard;

    private transient String sports;
    private transient String drugs;

    private transient String flupDateStr;
    private transient String nextFlupTimeStr;

    private transient List<PreDrug> preDrug;
    private transient List<PreSport> preSport;

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

    public String getHbpSymptom()
    {
        return this.hbpSymptom;
    }

    public void setHbpSymptom(String hbpSymptom)
    {
        this.hbpSymptom = hbpSymptom;
    }

    public String getHbpSymptomOther()
    {
        return this.hbpSymptomOther;
    }

    public void setHbpSymptomOther(String hbpSymptomOther)
    {
        this.hbpSymptomOther = hbpSymptomOther;
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

    public Integer getHbpPulse()
    {
        return this.hbpPulse;
    }

    public void setHbpPulse(Integer hbpPulse)
    {
        this.hbpPulse = hbpPulse;
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

    public Double getHbpDrinking()
    {
        return this.hbpDrinking;
    }

    public void setHbpDrinking(Double hbpDrinking)
    {
        this.hbpDrinking = hbpDrinking;
    }

    public Integer getHbpSalarium()
    {
        return this.hbpSalarium;
    }

    public void setHbpSalarium(Integer hbpSalarium)
    {
        this.hbpSalarium = hbpSalarium;
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

    public String getHbpHelpCheck()
    {
        return this.hbpHelpCheck;
    }

    public void setHbpHelpCheck(String hbpHelpCheck)
    {
        this.hbpHelpCheck = hbpHelpCheck;
    }

    public Integer getHbpDurgsObey()
    {
        return this.hbpDurgsObey;
    }

    public void setHbpDurgsObey(Integer hbpDurgsObey)
    {
        this.hbpDurgsObey = hbpDurgsObey;
    }

    public String getHbpDrugsUntoward()
    {
        return this.hbpDrugsUntoward;
    }

    public void setHbpDrugsUntoward(String hbpDrugsUntoward)
    {
        this.hbpDrugsUntoward = hbpDrugsUntoward;
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

    public String getSports()
    {
        return sports;
    }

    public void setSports(String sports)
    {
        this.sports = sports;
    }

    public String getDrugs()
    {
        return drugs;
    }

    public void setDrugs(String drugs)
    {
        this.drugs = drugs;
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

    public List<PreDrug> getPreDrug()
    {
        return preDrug;
    }

    public void setPreDrug(List<PreDrug> preDrug)
    {
        this.preDrug = preDrug;
    }

    public List<PreSport> getPreSport()
    {
        return preSport;
    }

    public void setPreSport(List<PreSport> preSport)
    {
        this.preSport = preSport;
    }

    public String getIdcard()
    {
        return idcard;
    }

    public void setIdcard(String idcard)
    {
        this.idcard = idcard;
    }

}