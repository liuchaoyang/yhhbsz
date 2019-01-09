package com.yzxt.yh.module.sys.bean;

/**
 * 客户端版本类
 * @author fan
 *
 */
public class ClientVersion
{
    private Integer id;
    private String appType;
    private String deviceType;
    private String version;
    private Integer versionNum;
    private String path;
    private String qrCodeName;
    private String showImg;
    private String name;
    private String remark;
    private Integer state;
    private Integer seq;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getAppType()
    {
        return appType;
    }

    public void setAppType(String appType)
    {
        this.appType = appType;
    }

    public String getDeviceType()
    {
        return deviceType;
    }

    public void setDeviceType(String deviceType)
    {
        this.deviceType = deviceType;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public Integer getVersionNum()
    {
        return versionNum;
    }

    public void setVersionNum(Integer versionNum)
    {
        this.versionNum = versionNum;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public String getQrCodeName()
    {
        return qrCodeName;
    }

    public void setQrCodeName(String qrCodeName)
    {
        this.qrCodeName = qrCodeName;
    }

    public String getShowImg()
    {
        return showImg;
    }

    public void setShowImg(String showImg)
    {
        this.showImg = showImg;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public Integer getState()
    {
        return state;
    }

    public void setState(Integer state)
    {
        this.state = state;
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