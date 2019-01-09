package com.yzxt.yh.eif.mp;

public class LacConvertResult
{
    // LBS转GPS成功
    public static final int STATUS_SUCCESS = 1;

    // LBS转GPS错误
    public static final int STATUS_ERROR = 2;

    // 转换状态
    private int status = 0;

    // 经度
    private double longitude;

    // 纬度
    private double latitude;

    // 地址
    private String address;

    // 错误信息
    private Object errorMsg;

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public Object getErrorMsg()
    {
        return errorMsg;
    }

    public void setErrorMsg(Object errorMsg)
    {
        this.errorMsg = errorMsg;
    }

    public int getSTATUS_SUCCESS()
    {
        return STATUS_SUCCESS;
    }

    public int getSTATUS_ERROR()
    {
        return STATUS_ERROR;
    }

}
