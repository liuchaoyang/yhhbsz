package com.yzxt.yh.module.sys.vo;

import java.util.List;

public class AchiveFamilyVO implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    private List<String> fatHis;
    private List<String> monHis;
    private List<String> broHis;
    private List<String> dauHis;

    public List<String> getFatHis()
    {
        return fatHis;
    }

    public void setFatHis(List<String> fatHis)
    {
        this.fatHis = fatHis;
    }

    public List<String> getMonHis()
    {
        return monHis;
    }

    public void setMonHis(List<String> monHis)
    {
        this.monHis = monHis;
    }

    public List<String> getBroHis()
    {
        return broHis;
    }

    public void setBroHis(List<String> broHis)
    {
        this.broHis = broHis;
    }

    public List<String> getDauHis()
    {
        return dauHis;
    }

    public void setDauHis(List<String> dauHis)
    {
        this.dauHis = dauHis;
    }

}