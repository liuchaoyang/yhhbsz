package com.yzxt.yh.module.stat.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.module.stat.service.StaticService;
import com.yzxt.yh.module.sys.bean.LoginRecord;
import com.yzxt.yh.module.sys.service.CustomerService;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;
import common.Logger;

/**
 * 统计封装类
 * 2015.7.20
 * @author h
 *
 */
public class StaticAction extends BaseAction
{

    private static final long serialVersionUID = 1L;

    private static Logger logger = Logger.getLogger(StaticAction.class);

    private StaticService staticService;

    private CustomerService customerService;

    public StaticService getStaticService()
    {
        return staticService;
    }

    public void setStaticService(StaticService staticService)
    {
        this.staticService = staticService;
    }

    public CustomerService getCustomerService()
    {
        return customerService;
    }

    public void setCustomerService(CustomerService customerService)
    {
        this.customerService = customerService;
    }

    /**
     * 获取建档数统计，可查看指定区域（省、市、县、乡镇）一段时间内的建档数量
     * @author h
     * 2015.7.20
     * */
    public void queryArchData()
    {

        try
        {
            // User operUser = (User) super.getCurrentUser();
            //查询条件
            Map<String, Object> filter = new HashMap<String, Object>();
            String startDateStr = request.getParameter("startDate");
            if (StringUtil.isNotEmpty(startDateStr))
            {
                filter.put("startDate", DateUtil.getDateFromHtml(startDateStr));
            }
            String endDateStr = request.getParameter("endDate");
            if (StringUtil.isNotEmpty(endDateStr))
            {
                filter.put("endDate", DateUtil.getDateFromHtml(endDateStr));
            }
            /*super.write();*/
        }
        catch (Exception e)
        {
            logger.error("查询数据失败", e);
        }
    }

    /**
     * 可查看指定区域（省、市、县、乡镇）一段时间内指定检测项目的检测数量、异常指标数
     * @author h
     * 2015.7.20
     * */
    public void queryChkData()
    {

        try
        {
            // User operUser = (User) super.getCurrentUser();
            //查询条件
            Map<String, Object> filter = new HashMap<String, Object>();
            String startDateStr = request.getParameter("startDate");
            if (StringUtil.isNotEmpty(startDateStr))
            {
                filter.put("startDate", DateUtil.getDateFromHtml(startDateStr));
            }
            String endDateStr = request.getParameter("endDate");
            if (StringUtil.isNotEmpty(endDateStr))
            {
                filter.put("endDate", DateUtil.getDateFromHtml(endDateStr));
            }
            String checkTypeStr = request.getParameter("checkType");
            if (StringUtil.isNotEmpty(checkTypeStr))
            {
                Integer checkType = Integer.parseInt(checkTypeStr);
                filter.put("checkType", checkType);
            }
            /*super.write();*/
        }
        catch (Exception e)
        {
            logger.error("查询数据失败", e);
        }
    }

    /**
     * 可查看指定区域（省、市、县、乡镇）一段时间内检测人群的分布
     * @author h
     * 2015.7.20
     * */
    public void queryChkHerdData()
    {

        try
        {
            // User operUser = (User) super.getCurrentUser();
            //查询条件
            Map<String, Object> filter = new HashMap<String, Object>();
            String startDateStr = request.getParameter("startDate");
            if (StringUtil.isNotEmpty(startDateStr))
            {
                filter.put("startDate", DateUtil.getDateFromHtml(startDateStr));
            }
            String endDateStr = request.getParameter("endDate");
            if (StringUtil.isNotEmpty(endDateStr))
            {
                filter.put("endDate", DateUtil.getDateFromHtml(endDateStr));
            }
            /*super.write();*/
        }
        catch (Exception e)
        {
            logger.error("查询数据失败", e);
        }
    }

    /**
     * 统计合格健康档案的比例
     * @author h
     * 2015.7.20
     * */
    public void queryScaleData()
    {

        try
        {
            // User operUser = (User) super.getCurrentUser();
            //查询条件
            Map<String, Object> filter = new HashMap<String, Object>();
            String startDateStr = request.getParameter("startDate");
            if (StringUtil.isNotEmpty(startDateStr))
            {
                filter.put("startDate", DateUtil.getDateFromHtml(startDateStr));
            }
            String endDateStr = request.getParameter("endDate");
            if (StringUtil.isNotEmpty(endDateStr))
            {
                filter.put("endDate", DateUtil.getDateFromHtml(endDateStr));
            }
            /*super.write();*/
        }
        catch (Exception e)
        {
            logger.error("查询数据失败", e);
        }
    }

    /**
     * 统计合格健康档案的比例
     * @author h
     * 2015.7.20
     * */
    public void queryUpdateScale()
    {

        try
        {
            // User operUser = (User) super.getCurrentUser();
            //查询条件
            Map<String, Object> filter = new HashMap<String, Object>();
            String startDateStr = request.getParameter("startDate");
            if (StringUtil.isNotEmpty(startDateStr))
            {
                filter.put("startDate", DateUtil.getDateFromHtml(startDateStr));
            }
            String endDateStr = request.getParameter("endDate");
            if (StringUtil.isNotEmpty(endDateStr))
            {
                filter.put("endDate", DateUtil.getDateFromHtml(endDateStr));
            }
            /*super.write();*/
        }
        catch (Exception e)
        {
            logger.error("查询数据失败", e);
        }
    }

    /**
     * 统计指定区域（省、市、县、乡镇）一段时间内各类公共卫生服务开展情况
     * @author h
     * 2015.7.20
     * */
    public void queryServiceOperate()
    {

        try
        {
            // User operUser = (User) super.getCurrentUser();
            //查询条件
            Map<String, Object> filter = new HashMap<String, Object>();
            String startDateStr = request.getParameter("startDate");
            if (StringUtil.isNotEmpty(startDateStr))
            {
                filter.put("startDate", DateUtil.getDateFromHtml(startDateStr));
            }
            String endDateStr = request.getParameter("endDate");
            if (StringUtil.isNotEmpty(endDateStr))
            {
                filter.put("endDate", DateUtil.getDateFromHtml(endDateStr));
            }
            /*super.write();*/
        }
        catch (Exception e)
        {
            logger.error("查询数据失败", e);
        }
    }

    /**
     * 查询不同登录方式的登录次数
     * @author h
     * 2015.7.21
     * */
    public void queryUserNum()
    {
        try
        {
            //查询条件
            Map<String, Object> filter = new HashMap<String, Object>();
            String typeStr = (String) request.getParameter("type");
            if (StringUtil.isNotEmpty(typeStr))
            {
                filter.put("typeStr", typeStr);
            }
            String startDateStr = request.getParameter("startDate");
            if (StringUtil.isNotEmpty(startDateStr))
            {
                filter.put("startDate", DateUtil.getDateFromHtml(startDateStr));
            }
            String endDateStr = request.getParameter("endDate");
            if (StringUtil.isNotEmpty(endDateStr))
            {
                filter.put("endDate", DateUtil.getDateFromHtml(endDateStr));
            }
            // 查询人
            filter.put("operUser", super.getCurrentUser());
            List<Map<String, Object>> list = staticService.queryUserNum(filter);
            /*List<Map<String, Object>> list = staticService.queryUserNum(filter,getPage(), getRows());*/
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("list", list);
            super.write(list);
            /*String json = listMapToJSON(list, null);
            super.write(json);*/
            /*super.write();*/
        }
        catch (Exception e)
        {
            logger.error("查询数据失败", e);
        }
    }

    /**
     * 查询不同登录方式的登录次数
     * @author h
     * 2015.7.21
     * */
    public void queryUser()
    {
        try
        {
            //查询条件
            Map<String, Object> filter = new HashMap<String, Object>();
            String startDateStr = request.getParameter("startDate");
            if (StringUtil.isNotEmpty(startDateStr))
            {
                filter.put("startDate", DateUtil.getDateFromHtml(startDateStr));
            }
            String endDateStr = request.getParameter("endDate");
            if (StringUtil.isNotEmpty(endDateStr))
            {
                filter.put("endDate", DateUtil.getDateFromHtml(endDateStr));
            }
            // 查询人
            filter.put("operUser", super.getCurrentUser());
            PageModel<LoginRecord> pageModel = staticService.queryUser(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询数据失败", e);
        }
    }

    /**
     * 平台查询个人详细
     * @author h
     * 2015.7.23
     * */
    public void getDetail()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            String startDateStr = request.getParameter("startDate");
            if (StringUtil.isNotEmpty(startDateStr))
            {
                filter.put("startDate", DateUtil.getDateFromHtml(startDateStr));
            }
            String endDateStr = request.getParameter("endDate");
            if (StringUtil.isNotEmpty(endDateStr))
            {
                filter.put("endDate", DateUtil.getDateFromHtml(endDateStr));
            }
            String userId = (String) request.getParameter("userId");
            filter.put("userId", userId);
            PageModel<LoginRecord> pageModel = staticService.getDetail(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询数据失败", e);
        }
    }

    /**
     * 统计用户每天注册用户数，现在暂时不分平台还是客户端还是一体机
     * @author h
     * 2015.7.24
     * */
    public void queryRegisterNum()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            String startDateStr = request.getParameter("startDate");
            if (StringUtil.isNotEmpty(startDateStr))
            {
                filter.put("startDate", DateUtil.getDateFromHtml(startDateStr));
            }
            String endDateStr = request.getParameter("endDate");
            if (StringUtil.isNotEmpty(endDateStr))
            {
                filter.put("endDate", DateUtil.getDateFromHtml(endDateStr));
            }
            // 查询人
            filter.put("operUser", super.getCurrentUser());
            List<Map<String, Object>> list = customerService.queryRegisterNum(filter);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("list", list);
            super.write(list);
        }
        catch (Exception e)
        {
            logger.error("查询错误", e);
        }
    }
}
