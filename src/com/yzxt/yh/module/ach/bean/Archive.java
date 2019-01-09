package com.yzxt.yh.module.ach.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.yzxt.yh.module.sys.bean.Customer;
import com.yzxt.yh.module.sys.bean.FileDesc;

public class Archive implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String custId;
    private String archiveNo;
    private String idCard;
    private String name;
    private String curAddress;
    private String householdAddress;
    private String telphone;
    private String streetName;
    private String villageName;
    private String medicalEstab;
    private String telMedical;
    private Timestamp createTime;
    private String doctorId;
    private Timestamp updateTime;
    private Timestamp synTime;
    private String otherInfo;
    private String workUnit;
    private String contactName;
    private String contactTelphone;
    private Integer householdType;
    private String national;
    private Integer bloodType;
    private Integer rhNagative;
    private Integer degree;
    private Integer profession;
    private Integer maritalStatus;
    private String payType;
    private String otherPayType;
    private String hoda;
    private String otherHoda;
    private String exposureHistory;
    private String geneticHistory;
    private String disabilityStatus;
    private String otherDisName;
    private Double height;
    private Double weight;

    // 非持久化字段
    private transient List<PreviousHistory> previousHistorys;
    private transient List<FamilyHistory> familyHistorys;
    private transient List<LifeEnv> lifeEnvs;
    private transient Customer customer;
    private transient String doctorName;
    private transient String nationalName;
    private FileDesc userImg;

    public String getCustId()
    {
        return custId;
    }

    public void setCustId(String custId)
    {
        this.custId = custId;
    }

    public String getArchiveNo()
    {
        return archiveNo;
    }

    public void setArchiveNo(String archiveNo)
    {
        this.archiveNo = archiveNo;
    }

    public String getIdCard()
    {
        return idCard;
    }

    public void setIdCard(String idCard)
    {
        this.idCard = idCard;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCurAddress()
    {
        return curAddress;
    }

    public void setCurAddress(String curAddress)
    {
        this.curAddress = curAddress;
    }

    public String getHouseholdAddress()
    {
        return householdAddress;
    }

    public void setHouseholdAddress(String householdAddress)
    {
        this.householdAddress = householdAddress;
    }

    public String getTelphone()
    {
        return telphone;
    }

    public void setTelphone(String telphone)
    {
        this.telphone = telphone;
    }

    public String getStreetName()
    {
        return streetName;
    }

    public void setStreetName(String streetName)
    {
        this.streetName = streetName;
    }

    public String getVillageName()
    {
        return villageName;
    }

    public void setVillageName(String villageName)
    {
        this.villageName = villageName;
    }

    public String getMedicalEstab()
    {
        return medicalEstab;
    }

    public void setMedicalEstab(String medicalEstab)
    {
        this.medicalEstab = medicalEstab;
    }

    public String getTelMedical()
    {
        return telMedical;
    }

    public void setTelMedical(String telMedical)
    {
        this.telMedical = telMedical;
    }

    public Timestamp getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime)
    {
        this.createTime = createTime;
    }

    public String getDoctorId()
    {
        return doctorId;
    }

    public void setDoctorId(String doctorId)
    {
        this.doctorId = doctorId;
    }

    public Timestamp getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime)
    {
        this.updateTime = updateTime;
    }

    public Timestamp getSynTime()
    {
        return synTime;
    }

    public void setSynTime(Timestamp synTime)
    {
        this.synTime = synTime;
    }

    public String getOtherInfo()
    {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo)
    {
        this.otherInfo = otherInfo;
    }

    public String getWorkUnit()
    {
        return workUnit;
    }

    public void setWorkUnit(String workUnit)
    {
        this.workUnit = workUnit;
    }

    public String getContactName()
    {
        return contactName;
    }

    public void setContactName(String contactName)
    {
        this.contactName = contactName;
    }

    public String getContactTelphone()
    {
        return contactTelphone;
    }

    public void setContactTelphone(String contactTelphone)
    {
        this.contactTelphone = contactTelphone;
    }

    public Integer getHouseholdType()
    {
        return householdType;
    }

    public void setHouseholdType(Integer householdType)
    {
        this.householdType = householdType;
    }

    public String getNational()
    {
        return national;
    }

    public void setNational(String national)
    {
        this.national = national;
    }

    public Integer getBloodType()
    {
        return bloodType;
    }

    public void setBloodType(Integer bloodType)
    {
        this.bloodType = bloodType;
    }

    public Integer getRhNagative()
    {
        return rhNagative;
    }

    public void setRhNagative(Integer rhNagative)
    {
        this.rhNagative = rhNagative;
    }

    public Integer getDegree()
    {
        return degree;
    }

    public void setDegree(Integer degree)
    {
        this.degree = degree;
    }

    public Integer getProfession()
    {
        return profession;
    }

    public void setProfession(Integer profession)
    {
        this.profession = profession;
    }

    public Integer getMaritalStatus()
    {
        return maritalStatus;
    }

    public void setMaritalStatus(Integer maritalStatus)
    {
        this.maritalStatus = maritalStatus;
    }

    public String getPayType()
    {
        return payType;
    }

    public void setPayType(String payType)
    {
        this.payType = payType;
    }

    public String getOtherPayType()
    {
        return otherPayType;
    }

    public void setOtherPayType(String otherPayType)
    {
        this.otherPayType = otherPayType;
    }

    public String getHoda()
    {
        return hoda;
    }

    public void setHoda(String hoda)
    {
        this.hoda = hoda;
    }

    public String getOtherHoda()
    {
        return otherHoda;
    }

    public void setOtherHoda(String otherHoda)
    {
        this.otherHoda = otherHoda;
    }

    public String getExposureHistory()
    {
        return exposureHistory;
    }

    public void setExposureHistory(String exposureHistory)
    {
        this.exposureHistory = exposureHistory;
    }

    public String getGeneticHistory()
    {
        return geneticHistory;
    }

    public void setGeneticHistory(String geneticHistory)
    {
        this.geneticHistory = geneticHistory;
    }

    public String getDisabilityStatus()
    {
        return disabilityStatus;
    }

    public void setDisabilityStatus(String disabilityStatus)
    {
        this.disabilityStatus = disabilityStatus;
    }

    public String getOtherDisName()
    {
        return otherDisName;
    }

    public void setOtherDisName(String otherDisName)
    {
        this.otherDisName = otherDisName;
    }

    public Double getHeight()
    {
        return height;
    }

    public void setHeight(Double height)
    {
        this.height = height;
    }

    public Double getWeight()
    {
        return weight;
    }

    public void setWeight(Double weight)
    {
        this.weight = weight;
    }

    public List<PreviousHistory> getPreviousHistorys()
    {
        return previousHistorys;
    }

    public void setPreviousHistorys(List<PreviousHistory> previousHistorys)
    {
        this.previousHistorys = previousHistorys;
    }

    public List<FamilyHistory> getFamilyHistorys()
    {
        return familyHistorys;
    }

    public void setFamilyHistorys(List<FamilyHistory> familyHistorys)
    {
        this.familyHistorys = familyHistorys;
    }

    public List<LifeEnv> getLifeEnvs()
    {
        return lifeEnvs;
    }

    public void setLifeEnvs(List<LifeEnv> lifeEnvs)
    {
        this.lifeEnvs = lifeEnvs;
    }

    public Customer getCustomer()
    {
        return customer;
    }

    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

    public String getDoctorName()
    {
        return doctorName;
    }

    public void setDoctorName(String doctorName)
    {
        this.doctorName = doctorName;
    }

    public String getNationalName()
    {
        return nationalName;
    }

    public void setNationalName(String nationalName)
    {
        this.nationalName = nationalName;
    }

    public FileDesc getUserImg()
    {
        return userImg;
    }

    public void setUserImg(FileDesc userImg)
    {
        this.userImg = userImg;
    }

}