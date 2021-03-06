package com.yzxt.fw.pager;

import java.util.List;

public class PageModel<T>
{
    // 总记录数
    private int total;

    // 当前页的记录集
    private List<T> data;

    // 每页多少条数据
    private int pageSize = 10;

    // 第几页
    private int pageNo = 1;

    public PageModel()
    {
    }

    public PageModel(int pageNo, int pageSize)
    {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    // 求总页数
    public int getTotalPages()
    {
        return (this.total + pageSize - 1) / pageSize;
    }

    // 首页
    public int getTopPage()
    {
        return 1;
    }

    // 末页
    public int getBottomPage()
    {
        return this.getTotalPages() == 0 ? 1 : this.getTotalPages();
    }

    // 上一页
    public int getUpPage()
    {
        if (this.pageNo <= 1)
        {
            return 1;
        }
        return this.pageNo - 1;
    }

    // 下一页
    public int getNextPage()
    {
        if (this.pageNo >= this.getTotalPages())
        {
            return (this.getTotalPages() == 0 ? 1 : this.getTotalPages());
        }
        return pageNo + 1;
    }

    public int getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

    public int getPageNo()
    {
        return pageNo;
    }

    public void setPageNo(int pageNo)
    {
        this.pageNo = pageNo;
    }

    public List<T> getData()
    {
        return data;
    }

    public void setData(List<T> data)
    {
        this.data = data;
    }

    public int getTotal()
    {
        return total;
    }

    public void setTotal(int total)
    {
        this.total = total;
    }

    @Override
    public String toString()
    {
        return "[pageNo: " + this.pageNo + "] [pageSize: " + this.pageSize + "] [total：" + this.total + "] ";
    }

}
