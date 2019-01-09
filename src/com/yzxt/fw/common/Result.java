package com.yzxt.fw.common;

/**
 * 业务操作结果包装类
 * @author f
 *
 */
public class Result
{
    /**
     * 操作成功状态
     */
    public final static int STATE_SUCESS = 1;

    /**
     * 操作状态未知
     */
    public final static int STATE_UNKNOWN = 0;

    /**
     * 操作失败状态
     */
    public final static int STATE_FAIL = -1;

    // 操作结果状态
    private int state;
    // 操作结果消息
    private String msg = "";
    // 操作返回数据
    private Object data;

    public Result()
    {
    }

    public Result(int state, String msg, Object data)
    {
        this.state = state;
        this.msg = msg;
        this.data = data;
    }

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public Object getData()
    {
        return data;
    }

    public void setData(Object data)
    {
        this.data = data;
    }

}
