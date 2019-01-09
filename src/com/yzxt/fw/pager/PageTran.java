package com.yzxt.fw.pager;

import java.util.List;

public class PageTran<T>
{
    // 当前页的记录集
    private List<T> data;

    public PageTran()
    {
    }

    public PageTran(List<T> data)
    {
        this.data = data;
    }

    public List<T> getData()
    {
        return data;
    }

    public void setData(List<T> data)
    {
        this.data = data;
    }

}
