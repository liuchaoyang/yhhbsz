package com.yzxt.yh.module.sys.vo;

import java.util.List;

public class PayTypesVO implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    private List<String> payTypes;

    public List<String> getPayTypes()
    {
        return payTypes;
    }

    public void setPayTypes(List<String> payTypes)
    {
        this.payTypes = payTypes;
    }

}