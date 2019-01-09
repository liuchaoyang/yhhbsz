package com.yzxt.yh.module.chk.util;

public class AnalysisResult
{

    // 告警等级，小于0表示正常，大于0表示异常，等于0或为空为未分析
    private Integer level;

    // 告警描述
    private String descript;

    public AnalysisResult()
    {
    }

    public AnalysisResult(Integer level, String descript)
    {
        this.level = level;
        this.descript = descript;
    }

    public Integer getLevel()
    {
        return level;
    }

    public void setLevel(Integer level)
    {
        this.level = level;
    }

    public String getDescript()
    {
        return descript;
    }

    public void setDescript(String descript)
    {
        this.descript = descript;
    }

}
