package com.yzxt.yh.module.sys.vo;

import java.util.List;

public class AchiveVO implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    private Integer kitCondition;
    private Integer gasCondition;
    private Integer watCondition;
    private Integer wahCondition;
    private Integer aclCondition;
    private List<String> exposureHistorys;
    private List<String> hodas;
    private List<String> disabilityStatus;///残疾情况

    public List<String> getDisabilityStatus()
    {
        return disabilityStatus;
    }

    public void setDisabilityStatus(List<String> disabilityStatus)
    {
        this.disabilityStatus = disabilityStatus;
    }

    public List<String> getHodas()
    {
        return hodas;
    }

    public void setHodas(List<String> hodas)
    {
        this.hodas = hodas;
    }

    public List<String> getExposureHistorys()
    {
        return exposureHistorys;
    }

    public void setExposureHistorys(List<String> exposureHistorys)
    {
        this.exposureHistorys = exposureHistorys;
    }

    public Integer getKitCondition()
    {
        return kitCondition;
    }

    public void setKitCondition(Integer kitCondition)
    {
        this.kitCondition = kitCondition;
    }

    public Integer getGasCondition()
    {
        return gasCondition;
    }

    public void setGasCondition(Integer gasCondition)
    {
        this.gasCondition = gasCondition;
    }

    public Integer getWatCondition()
    {
        return watCondition;
    }

    public void setWatCondition(Integer watCondition)
    {
        this.watCondition = watCondition;
    }

    public Integer getWahCondition()
    {
        return wahCondition;
    }

    public void setWahCondition(Integer wahCondition)
    {
        this.wahCondition = wahCondition;
    }

    public Integer getAclCondition()
    {
        return aclCondition;
    }

    public void setAclCondition(Integer aclCondition)
    {
        this.aclCondition = aclCondition;
    }

}