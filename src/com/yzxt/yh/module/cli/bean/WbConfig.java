package com.yzxt.yh.module.cli.bean;

import java.sql.Timestamp;
import java.util.Date;

/**
* 腕表配置类
* 2015.11.24
* @author h
*/
public class WbConfig implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    private String id;
    private String custId;
    private String wbName;
    private String deviceId;
    private Integer state;
    private String imgFileId;
    private Integer sex;
    private Integer step;
    private Date birthday;
    private Double height;
    private Double weight;
    private Integer pluseMin;
    private Integer pluseMax;
    private String pluseStartTime;
    private String pluseEndTime;
    private Integer pluseSpan;
    private Integer pluseWarning;
    private Integer stepTarget;
    private String stepStartTime;
    private String stepEndTime;
    private Integer stepSpan;
    private String positionStartTime;
    private String positionEndTime;
    private Integer positionSpan;
    private String sleepStartTime;
    private String sleepEndTime;
    private Integer sleepStatus;
    private float xtMaxGlucose;
    private float xtMinGlucose;
    private Integer xtWarning;
    private Integer xyDiastolicMax;
    private Integer xyDiastolicMin;
    private Integer xySystolicMax;
    private Integer xySystolicMin;
    private Integer xyWarning;
    private String twStartTime;
    private String twEndTime;
    private Integer twSpan;
    private float twMaxTemperature;
    private float twMinTemperature;
    private Integer twWarning;
    //private String remindContent;
    //private String remindTime;
    //private Integer remindStatus;
    private String remindList;
    private String phones;
    private String sittingStartTime;
    private String sittingEndTime;
    private Integer sittingWarning;
    private String sosNums;
    private String sosText;
    private Double eleLatitude;
    private Double eleLongitude;
    private Integer eleDistance;
    private String eleAddress;
    private Date eleTime;
    private String createBy;
    private Timestamp createTime;
    private String updateBy;
    private Timestamp updateTime;

    // 非持久化数据扩展
    private String imgFilePath;

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

    public String getWbName()
    {
        return wbName;
    }

    public void setWbName(String wbName)
    {
        this.wbName = wbName;
    }

    public String getDeviceId()
    {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId)
    {
        this.deviceId = deviceId;
    }

    public Integer getState()
    {
        return this.state;
    }

    public String getImgFileId()
    {
        return imgFileId;
    }

    public void setImgFileId(String imgFileId)
    {
        this.imgFileId = imgFileId;
    }

    public Integer getSex()
    {
        return sex;
    }

    public void setSex(Integer sex)
    {
        this.sex = sex;
    }

    public Date getBirthday()
    {
        return birthday;
    }

    public void setBirthday(Date birthday)
    {
        this.birthday = birthday;
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

    public Integer getStep()
    {
        return step;
    }

    public void setStep(Integer step)
    {
        this.step = step;
    }

    public Integer getPluseMin()
    {
        return pluseMin;
    }

    public void setPluseMin(Integer pluseMin)
    {
        this.pluseMin = pluseMin;
    }

    public Integer getPluseMax()
    {
        return pluseMax;
    }

    public void setPluseMax(Integer pluseMax)
    {
        this.pluseMax = pluseMax;
    }

    public void setState(Integer state)
    {
        this.state = state;
    }

    public String getPluseStartTime()
    {
        return this.pluseStartTime;
    }

    public void setPluseStartTime(String pluseStartTime)
    {
        this.pluseStartTime = pluseStartTime;
    }

    public String getPluseEndTime()
    {
        return this.pluseEndTime;
    }

    public void setPluseEndTime(String pluseEndTime)
    {
        this.pluseEndTime = pluseEndTime;
    }

    public Integer getPluseSpan()
    {
        return this.pluseSpan;
    }

    public void setPluseSpan(Integer pluseSpan)
    {
        this.pluseSpan = pluseSpan;
    }

    public Integer getPluseWarning()
    {
        return this.pluseWarning;
    }

    public void setPluseWarning(Integer pluseWarning)
    {
        this.pluseWarning = pluseWarning;
    }

    public Integer getStepTarget()
    {
        return this.stepTarget;
    }

    public void setStepTarget(Integer stepTarget)
    {
        this.stepTarget = stepTarget;
    }

    public String getStepStartTime()
    {
        return this.stepStartTime;
    }

    public void setStepStartTime(String stepStartTime)
    {
        this.stepStartTime = stepStartTime;
    }

    public String getStepEndTime()
    {
        return this.stepEndTime;
    }

    public void setStepEndTime(String stepEndTime)
    {
        this.stepEndTime = stepEndTime;
    }

    public Integer getStepSpan()
    {
        return this.stepSpan;
    }

    public void setStepSpan(Integer stepSpan)
    {
        this.stepSpan = stepSpan;
    }

    public String getPositionStartTime()
    {
        return this.positionStartTime;
    }

    public void setPositionStartTime(String positionStartTime)
    {
        this.positionStartTime = positionStartTime;
    }

    public String getPositionEndTime()
    {
        return this.positionEndTime;
    }

    public void setPositionEndTime(String positionEndTime)
    {
        this.positionEndTime = positionEndTime;
    }

    public Integer getPositionSpan()
    {
        return this.positionSpan;
    }

    public void setPositionSpan(Integer positionSpan)
    {
        this.positionSpan = positionSpan;
    }

    public String getSleepStartTime()
    {
        return this.sleepStartTime;
    }

    public void setSleepStartTime(String sleepStartTime)
    {
        this.sleepStartTime = sleepStartTime;
    }

    public String getSleepEndTime()
    {
        return this.sleepEndTime;
    }

    public void setSleepEndTime(String sleepEndTime)
    {
        this.sleepEndTime = sleepEndTime;
    }

    public Integer getSleepStatus()
    {
        return this.sleepStatus;
    }

    public void setSleepStatus(Integer sleepStatus)
    {
        this.sleepStatus = sleepStatus;
    }

    public float getXtMaxGlucose()
    {
        return this.xtMaxGlucose;
    }

    public void setXtMaxGlucose(float xtMaxGlucose)
    {
        this.xtMaxGlucose = xtMaxGlucose;
    }

    public float getXtMinGlucose()
    {
        return this.xtMinGlucose;
    }

    public void setXtMinGlucose(float xtMinGlucose)
    {
        this.xtMinGlucose = xtMinGlucose;
    }

    public Integer getXtWarning()
    {
        return this.xtWarning;
    }

    public void setXtWarning(Integer xtWarning)
    {
        this.xtWarning = xtWarning;
    }

    public Integer getXyDiastolicMax()
    {
        return this.xyDiastolicMax;
    }

    public void setXyDiastolicMax(Integer xyDiastolicMax)
    {
        this.xyDiastolicMax = xyDiastolicMax;
    }

    public Integer getXyDiastolicMin()
    {
        return this.xyDiastolicMin;
    }

    public void setXyDiastolicMin(Integer xyDiastolicMin)
    {
        this.xyDiastolicMin = xyDiastolicMin;
    }

    public Integer getXySystolicMax()
    {
        return this.xySystolicMax;
    }

    public void setXySystolicMax(Integer xySystolicMax)
    {
        this.xySystolicMax = xySystolicMax;
    }

    public Integer getXySystolicMin()
    {
        return this.xySystolicMin;
    }

    public void setXySystolicMin(Integer xySystolicMin)
    {
        this.xySystolicMin = xySystolicMin;
    }

    public Integer getXyWarning()
    {
        return this.xyWarning;
    }

    public void setXyWarning(Integer xyWarning)
    {
        this.xyWarning = xyWarning;
    }

    public String getTwStartTime()
    {
        return this.twStartTime;
    }

    public void setTwStartTime(String twStartTime)
    {
        this.twStartTime = twStartTime;
    }

    public String getTwEndTime()
    {
        return this.twEndTime;
    }

    public void setTwEndTime(String twEndTime)
    {
        this.twEndTime = twEndTime;
    }

    public Integer getTwSpan()
    {
        return this.twSpan;
    }

    public void setTwSpan(Integer twSpan)
    {
        this.twSpan = twSpan;
    }

    public float getTwMaxTemperature()
    {
        return this.twMaxTemperature;
    }

    public void setTwMaxTemperature(float twMaxTemperature)
    {
        this.twMaxTemperature = twMaxTemperature;
    }

    public float getTwMinTemperature()
    {
        return this.twMinTemperature;
    }

    public void setTwMinTemperature(float twMinTemperature)
    {
        this.twMinTemperature = twMinTemperature;
    }

    public Integer getTwWarning()
    {
        return this.twWarning;
    }

    public void setTwWarning(Integer twWarning)
    {
        this.twWarning = twWarning;
    }

    public String getRemindList()
    {
        return remindList;
    }

    public void setRemindList(String remindList)
    {
        this.remindList = remindList;
    }

    public String getPhones()
    {
        return this.phones;
    }

    public void setPhones(String phones)
    {
        this.phones = phones;
    }

    public String getSittingStartTime()
    {
        return this.sittingStartTime;
    }

    public void setSittingStartTime(String sittingStartTime)
    {
        this.sittingStartTime = sittingStartTime;
    }

    public String getSittingEndTime()
    {
        return this.sittingEndTime;
    }

    public void setSittingEndTime(String sittingEndTime)
    {
        this.sittingEndTime = sittingEndTime;
    }

    public Integer getSittingWarning()
    {
        return this.sittingWarning;
    }

    public void setSittingWarning(Integer sittingWarning)
    {
        this.sittingWarning = sittingWarning;
    }

    public String getSosNums()
    {
        return this.sosNums;
    }

    public void setSosNums(String sosNums)
    {
        this.sosNums = sosNums;
    }

    public String getSosText()
    {
        return this.sosText;
    }

    public void setSosText(String sosText)
    {
        this.sosText = sosText;
    }

    public Double getEleLatitude()
    {
        return eleLatitude;
    }

    public void setEleLatitude(Double eleLatitude)
    {
        this.eleLatitude = eleLatitude;
    }

    public Double getEleLongitude()
    {
        return eleLongitude;
    }

    public void setEleLongitude(Double eleLongitude)
    {
        this.eleLongitude = eleLongitude;
    }

    public Integer getEleDistance()
    {
        return eleDistance;
    }

    public void setEleDistance(Integer eleDistance)
    {
        this.eleDistance = eleDistance;
    }

    public String getEleAddress()
    {
        return eleAddress;
    }

    public void setEleAddress(String eleAddress)
    {
        this.eleAddress = eleAddress;
    }

    public Date getEleTime()
    {
        return eleTime;
    }

    public void setEleTime(Date eleTime)
    {
        this.eleTime = eleTime;
    }

    public String getCreateBy()
    {
        return createBy;
    }

    public void setCreateBy(String createBy)
    {
        this.createBy = createBy;
    }

    public Timestamp getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime)
    {
        this.createTime = createTime;
    }

    public String getUpdateBy()
    {
        return updateBy;
    }

    public void setUpdateBy(String updateBy)
    {
        this.updateBy = updateBy;
    }

    public Timestamp getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getImgFilePath()
    {
        return imgFilePath;
    }

    public void setImgFilePath(String imgFilePath)
    {
        this.imgFilePath = imgFilePath;
    }

}