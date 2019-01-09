package com.yzxt.yh.module.stat.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.JsonObject;
import com.yzxt.fw.common.BaseAction;
import com.yzxt.yh.module.stat.service.ChkStaticService;
import com.yzxt.yh.module.stat.vo.AreaChk;
import com.yzxt.yh.module.stat.vo.PeopleDis;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;
import common.Logger;

/**
 * 档案统计封装类
 *
 */
public class ChkStaticAction extends BaseAction
{
    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(ChkStaticAction.class);

    private ChkStaticService chkStaticService;

    public ChkStaticService getChkStaticService()
    {
        return chkStaticService;
    }

    public void setChkStaticService(ChkStaticService chkStaticService)
    {
        this.chkStaticService = chkStaticService;
    }

    /**
     * 跳转到区域检测数统计
     */
    public String toAreaChk()
    {
        // 顶级省份
        return "areaChk";
    }

    /**
     * 查看指定区域（省、市、县、乡镇）检测数量
     */
    public void queryAreaChk()
    {
        try
        {
            //查询条件
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("startChkDate", DateUtil.getDateFromHtml(request.getParameter("startChkDate")));
            filter.put("endChkDate", DateUtil.getDateFromHtml(request.getParameter("endChkDate")));
            List<AreaChk> list = chkStaticService.queryAreaChk(filter);
            super.write(list);
        }
        catch (Exception e)
        {
            logger.error("统计区域检测数错误", e);
        }
    }

    /**
     * 跳转到查询指定区域的检测人群分布
     */
    public String toPeoleDis()
    {
        // 顶级省份
        return "peoleDis";
    }

    /**
     * 查询指定区域的检测人群分布
     */
    public void queryPeoleDis()
    {
        try
        {
            //查询条件
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("startChkDate", DateUtil.getDateFromHtml(request.getParameter("startChkDate")));
            filter.put("endChkDate", DateUtil.getDateFromHtml(request.getParameter("endChkDate")));
            List<PeopleDis>[] peopleDiss = chkStaticService.queryPeopleDis(filter);
            JsonObject jo = new JsonObject();
            jo.add("sexDiss", gson.toJsonTree(peopleDiss[0]));
            jo.add("ageDiss", gson.toJsonTree(peopleDiss[1]));
            super.write(jo);
        }
        catch (Exception e)
        {
            logger.error("统计检测人群分布错误", e);
        }
    }

}
