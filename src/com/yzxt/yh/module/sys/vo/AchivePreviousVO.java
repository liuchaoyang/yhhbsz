package com.yzxt.yh.module.sys.vo;

import java.sql.Timestamp;

public class AchivePreviousVO implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    private Integer jbname;
    private Timestamp jbtime;

    public Integer getJbname()
    {
        return jbname;
    }

    public void setJbname(Integer jbname)
    {
        this.jbname = jbname;
    }

    public Timestamp getJbtime()
    {
        return jbtime;
    }

    public void setJbtime(Timestamp jbtime)
    {
        this.jbtime = jbtime;
    }

}