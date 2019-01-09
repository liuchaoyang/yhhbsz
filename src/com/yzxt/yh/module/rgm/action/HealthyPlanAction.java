package com.yzxt.yh.module.rgm.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.module.rgm.bean.HealthyPlan;
import com.yzxt.yh.module.rgm.service.HealthyPlanService;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;

public class HealthyPlanAction extends BaseAction
{
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(HealthyPlanAction.class);

    private HealthyPlanService healthyPlanService;

    private HealthyPlan healthyPlan;

    public HealthyPlanService getHealthyPlanService()
    {
        return healthyPlanService;
    }

    public void setHealthyPlanService(HealthyPlanService healthyPlanService)
    {
        this.healthyPlanService = healthyPlanService;
    }

    public HealthyPlan getHealthyPlan()
    {
        return healthyPlan;
    }

    public void setHealthyPlan(HealthyPlan healthyPlan)
    {
        this.healthyPlan = healthyPlan;
    }

    /**
     * 健康计划列表页面
     * @return
     */
    public String toList()
    {
        return "list";
    }

    /**
     * 查询健康计划列表
     * 2015.9.18
     * @author h
     * @return
     */
    @SuppressWarnings("unchecked")
    public void list()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            String keyword = request.getParameter("keyword");
            filter.put("keyword", keyword != null ? keyword.trim() : null);
            Date today = DateUtil.truncDay(new Date());
            filter.put("today", today);
            filter.put("user", super.getCurrentUser());
            PageModel<HealthyPlan> pageBean = healthyPlanService.queryHealthyPlanByPage(filter, getPage(), getRows());
            // 数据显示处理
            List<HealthyPlan> list = (List<HealthyPlan>) pageBean.getData();
            if (list != null)
            {
                long todayLong = today.getTime();
                for (HealthyPlan plan : list)
                {
                    // 完成状态
                    String statusDesc = "";
                    if (todayLong < plan.getStartDate().getTime())
                    {
                        statusDesc = "未开始";
                    }
                    else if (todayLong > plan.getEndDate().getTime())
                    {
                        statusDesc = "已完成";
                    }
                    else
                    {
                        statusDesc = "进行中";
                    }
                    plan.setStatusDesc(statusDesc);
                    // 血压
                    if (plan.getType() == 1)
                    {
                        plan.setStartValDesc(getBPDesc(plan.getStartValDesc()));
                        plan.setEndValDesc(getBPDesc(plan.getEndValDesc()));
                        plan.setTargetValDesc(getBPDesc(StringUtil.ensureStringNotNull(plan.getTargetValue()) + "/"
                                + StringUtil.ensureStringNotNull(plan.getTargetValue2())));
                    }
                    else if (plan.getType() == 2)
                    //血糖
                    {
                        plan.setStartValDesc(getBSDesc(plan.getStartValDesc()));
                        plan.setEndValDesc(getBSDesc(plan.getEndValDesc()));
                        plan.setTargetValDesc(getBSDesc(StringUtil.ensureStringNotNull(plan.getTargetValue()) + "/"
                                + StringUtil.ensureStringNotNull(plan.getTargetValue2()) + "/"
                                + StringUtil.ensureStringNotNull(plan.getTargetValue3())));
                    }
                }
            }
            /*String resultStr = getListByJson(pageBean);*/
            super.write(pageBean);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 获取血压描述
     * @param str
     */
    private static String getBPDesc(String str)
    {
        if (StringUtil.isEmpty(str))
        {
            return "";
        }
        int sp = str.indexOf('/');
        if (sp > -1)
        {
            String htmlSplit = ",<br />";
            return "收缩压: " + str.substring(0, sp) + " mmHg" + htmlSplit + "舒张压: " + str.substring(sp + 1) + " mmHg";
        }
        else
        {
            return "";
        }
    }

    /**
     * 获取血糖描述
     * @param str
     */
    private static String getBSDesc(String str)
    {
        if (StringUtil.isEmpty(str))
        {
            return "";
        }
        int sp1 = str.indexOf('/');
        if (sp1 == -1)
        {
            return "";
        }
        int sp2 = str.indexOf('/', sp1 + 1);
        if (sp2 == -1)
        {
            return "";
        }
        String htmlSplit = ",<br />";
        String[] strs = new String[]
        {str.substring(0, sp1), str.substring(sp1 + 1, sp2), str.substring(sp2 + 1)};
        boolean isFirst = true;
        StringBuilder sb = new StringBuilder();
        if (StringUtil.isNotEmpty(strs[0]))
        {
            isFirst = false;
            sb.append("空腹血糖: ").append(strs[0]).append(" mmol/L");
        }
        if (StringUtil.isNotEmpty(strs[1]))
        {
            if (!isFirst)
            {
                sb.append(htmlSplit);
            }
            else
            {
                isFirst = false;
            }
            sb.append("餐后血糖: ").append(strs[1]).append(" mmol/L");
        }
        if (StringUtil.isNotEmpty(strs[2]))
        {
            if (!isFirst)
            {
                sb.append(htmlSplit);
            }
            else
            {
                isFirst = false;
            }
            sb.append("服糖后血糖: ").append(strs[2]).append(" mmol/L");
        }
        return sb.toString();
    }

    /**
     * 查询健康计划列表
     * @return
     */
    public String toEdit()
    {
        User operUser = (User) super.getCurrentUser();
        try
        {
            String id = super.getRequest().getParameter("id");
            if (StringUtil.isEmpty(id))
            {
                healthyPlan = new HealthyPlan();
                healthyPlan.setUserId(operUser.getId());
            }
            else
            {
                healthyPlan = healthyPlanService.load(id);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        request.setAttribute("healthyPlan", healthyPlan);
        return "edit";
    }

    /**
     * 添加健康计划
     * @return
     */
    public void add()
    {
        String msg = "";
        int state = 0;
        try
        {
            User user = (User) super.getCurrentUser();
            healthyPlan.setCreateBy(user.getId());
            healthyPlan.setUpdateBy(user.getId());
            Result r = healthyPlanService.add(healthyPlan);
            msg = r.getMsg();
            state = r.getState();
        }
        catch (Exception e)
        {
            msg = "保存出错.";
            state = Result.STATE_FAIL;
            e.printStackTrace();
        }
        Result result = new Result();
        result.setMsg(msg);
        result.setState(state);
        super.write(result);
    }

    /**
     * 更新健康计划
     * @return
     */
    public void update()
    {
        String msg = "";
        int state = 0;
        try
        {
            User user = (User) super.getCurrentUser();
            healthyPlan.setUpdateBy(user.getId());
            Result r = healthyPlanService.update(healthyPlan);
            msg = r.getMsg();
            state = r.getState();
        }
        catch (Exception e)
        {
            msg = "保存出错.";
            state = Result.STATE_FAIL;
            e.printStackTrace();
        }
        Result result = new Result();
        result.setMsg(msg);
        result.setState(state);
        super.write(result);
    }

    /**
     * 删除健康计划
     * @return
     */
    public void delete()
    {
        String msg = "";
        int state = 0;
        try
        {
            String idsStr = super.getRequest().getParameter("ids");
            String[] ids = StringUtil.splitWithoutEmpty(idsStr, ",");
            healthyPlanService.delete(ids);
            state = Result.STATE_SUCESS;
        }
        catch (Exception e)
        {
            msg = "保存出错.";
            state = Result.STATE_FAIL;
            e.printStackTrace();
        }
        Result result = new Result();
        result.setMsg(msg);
        result.setState(state);
        super.write(result);
    }

    /**
     * 查看健康计划
     * @return
     */
    public String view()
    {
        try
        {
            String id = super.getRequest().getParameter("id");
            healthyPlan = healthyPlanService.load(id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        request.setAttribute("healthyPlan", healthyPlan);
        if (healthyPlan.getType() == 1)
        {
            return "bpView";
        }
        else if (healthyPlan.getType() == 2)
        {
            return "bsView";
        }
        return "view";
    }

}
