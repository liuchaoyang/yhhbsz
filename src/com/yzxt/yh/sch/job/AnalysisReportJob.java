package com.yzxt.yh.sch.job;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.BeanFactory;

import com.yzxt.fw.server.BeanFactoryHelper;
import com.yzxt.yh.module.rpt.bean.AnalysisReport;
import com.yzxt.yh.module.rpt.service.AnalysisReportService;
import com.yzxt.yh.module.sys.bean.Customer;
import com.yzxt.yh.module.sys.service.CustomerService;
import com.yzxt.yh.util.DateUtil;

public class AnalysisReportJob implements Job
{
    private Logger logger = Logger.getLogger(AnalysisReportJob.class);

    /**
     * 生成血压血糖月度分析报告
     */
    public void execute(JobExecutionContext context) throws JobExecutionException
    {
        logger.debug("分析报告生成开始.");
        try
        {
            genAnalysisReport(new Date());
        }
        catch (Exception e)
        {
            logger.error("分析报告生成出错。", e);
        }
        logger.debug("分析报告生成结束.");
    }

    /**
     * 产生上个月的分析报告
     * @param today
     * @throws Exception
     */
    public void genAnalysisReport(Date now) throws Exception
    {
        BeanFactory beanFactory = BeanFactoryHelper.getBeanfactory();
        if (beanFactory == null)
        {
            logger.error("BeanFactory不存在，忽略本次分析报告调度。");
            return;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        // ---按月生成报告，上个月1号到月底
        // cal.set(Calendar.HOUR_OF_DAY, 0);
        // cal.set(Calendar.MINUTE, 0);
        // cal.set(Calendar.SECOND, 0);
        // cal.set(Calendar.MILLISECOND, 0);
        // cal.set(Calendar.DAY_OF_MONTH, 1);
        // cal.add(Calendar.DAY_OF_MONTH, -1);
        // Date endDate = cal.getTime();
        // cal.set(Calendar.DAY_OF_MONTH, 1);
        // Date startDate = cal.getTime();
        // ---------------
        // ---按周生成报告，上个周一至周日
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        // 周一至周日分别对应值{2, 3, 4, 5, 6, 7, 1}
        int nowDW = cal.get(Calendar.DAY_OF_WEEK);
        if (nowDW != Calendar.SUNDAY)
        {
            cal.add(Calendar.DAY_OF_MONTH, -nowDW - 5);
        }
        else
        {
            cal.add(Calendar.DAY_OF_MONTH, -13);
        }
        Date startDate = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 6);
        Date endDate = cal.getTime();
        // 查询客户
        int maxSize = 500;
        CustomerService customerService = (CustomerService) beanFactory.getBean("customerService");
        AnalysisReportService analysisReportService = (AnalysisReportService) beanFactory
                .getBean("analysisReportService");
        int c = 0;
        Map<String, Object> filter = new HashMap<String, Object>();
        List<Customer> custs = customerService.getCustomers(filter, c, maxSize);
        // 报告参数值
        AnalysisReport report = new AnalysisReport();
        report.setExamBeginTime(startDate);
        report.setExamEndTime(endDate);
        while (custs != null && !custs.isEmpty())
        {
            // 总查询客户数
            c = c + custs.size();
            // 处理每个客户分析报告
            for (Customer cust : custs)
            {
                report.setCustId(cust.getUserId());
                try
                {
                    analysisReportService.doGenReport(report);
                }
                catch (Exception e)
                {
                    logger.error("生成健康报告出错，客户ID：" + cust.getUserId() + "，分析报告时间段：" + DateUtil.toHtmlDate(startDate)
                            + "至" + DateUtil.toHtmlDate(endDate) + "。", e);
                }
            }
            custs = customerService.getCustomers(filter, c, maxSize);
        }
    }

    public void genMuchMonth(Date firDate, Date secDate) throws Exception
    {
        logger.debug("多月份分析报告生成开始。");
        BeanFactory beanFactory = BeanFactoryHelper.getBeanfactory();
        if (beanFactory == null)
        {
            logger.error("BeanFactory不存在，忽略本次分析报告调度。");
            return;
        }
        // 月初
        if (firDate == null)
        {
            logger.error("生成时间段内健康报告失败，请指定开始时间。");
        }
        if (secDate == null)
        {
            logger.debug("未指定结束时间，以当前时间作为结束时间间。");
            secDate = new Date();
        }
        else
        {
            secDate = DateUtil.addDay(DateUtil.truncDay(secDate), 1);
        }
        // 查找第一个月初
        Calendar cal = Calendar.getInstance();
        cal.setTime(firDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        if (cal.getTimeInMillis() < firDate.getTime())
        {
            cal.add(Calendar.MONTH, 1);
        }
        List<Date[]> dateSeq = new ArrayList<Date[]>();
        while (cal.getTimeInMillis() < secDate.getTime())
        {
            Date startDate = cal.getTime();
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date endDate = cal.getTime();
            if (endDate.getTime() > secDate.getTime())
            {
                break;
            }
            dateSeq.add(new Date[]
            {startDate, endDate});
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        if (dateSeq.isEmpty())
        {
            return;
        }
        int maxSize = 500;
        // 生成报告
        CustomerService customerService = (CustomerService) beanFactory.getBean("customerService");
        AnalysisReportService analysisReportService = (AnalysisReportService) beanFactory
                .getBean("analysisReportService");
        int c = 0;
        Map<String, Object> filter = new HashMap<String, Object>();
        List<Customer> custs = customerService.getCustomers(filter, c, maxSize);
        // 报告参数值
        AnalysisReport report = new AnalysisReport();
        while (custs != null && !custs.isEmpty())
        {
            // 总查询居民数
            c = c + custs.size();
            // 处理每个居民分析报告
            for (Customer cust : custs)
            {
                for (Date[] dates : dateSeq)
                {
                    report.setExamBeginTime(dates[0]);
                    report.setExamEndTime(dates[1]);
                    report.setCustId(cust.getUserId());
                    try
                    {
                        analysisReportService.doGenReport(report);
                    }
                    catch (Exception e)
                    {
                        logger.error("生成健康报告出错，客户ID：" + cust.getUserId() + "，分析报告时间段：" + DateUtil.toHtmlDate(dates[0])
                                + "至" + DateUtil.toHtmlDate(dates[1]) + "。", e);
                    }
                }
            }
            custs = customerService.getCustomers(filter, c, maxSize);
        }
        logger.debug("多月份分析报告生成结束.");
    }

    public void genMuchWeek(Date firDate, Date secDate) throws Exception
    {
        logger.debug("多周分析报告生成开始。");
        BeanFactory beanFactory = BeanFactoryHelper.getBeanfactory();
        if (beanFactory == null)
        {
            logger.error("BeanFactory不存在，忽略本次分析报告调度。");
            return;
        }
        // 第一天
        if (firDate == null)
        {
            logger.error("生成时间段内健康报告失败，请指定开始时间。");
        }
        if (secDate == null)
        {
            logger.debug("未指定结束时间，以当前时间作为结束时间间。");
            secDate = new Date();
        }
        else
        {
            secDate = DateUtil.addDay(DateUtil.truncDay(secDate), 1);
        }
        // 查找第一个周一
        Calendar cal = Calendar.getInstance();
        cal.setTime(firDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        // 周一至周日分别对应值{2, 3, 4, 5, 6, 7, 1}
        int nowDW = cal.get(Calendar.DAY_OF_WEEK);
        if (nowDW != Calendar.SUNDAY)
        {
            if (nowDW != Calendar.MONDAY)
            {
                cal.add(Calendar.DAY_OF_MONTH, 9 - nowDW);
            }
            else
            {
                if (cal.getTime().getTime() < firDate.getTime())
                {
                    cal.add(Calendar.DAY_OF_MONTH, 7);
                }
            }
        }
        else
        {
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        List<Date[]> dateSeq = new ArrayList<Date[]>();
        while (cal.getTimeInMillis() < secDate.getTime())
        {
            Date startDate = cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH, 6);
            Date endDate = cal.getTime();
            if (endDate.getTime() >= secDate.getTime())
            {
                break;
            }
            dateSeq.add(new Date[]
            {startDate, endDate});
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        if (dateSeq.isEmpty())
        {
            return;
        }
        int maxSize = 500;
        // 生成报告
        CustomerService customerService = (CustomerService) beanFactory.getBean("customerService");
        AnalysisReportService analysisReportService = (AnalysisReportService) beanFactory
                .getBean("analysisReportService");
        int c = 0;
        Map<String, Object> filter = new HashMap<String, Object>();
        List<Customer> custs = customerService.getCustomers(filter, c, maxSize);
        // 报告参数值
        AnalysisReport report = new AnalysisReport();
        while (custs != null && !custs.isEmpty())
        {
            // 总查询居民数
            c = c + custs.size();
            // 处理每个居民分析报告
            for (Customer cust : custs)
            {
                for (Date[] dates : dateSeq)
                {
                    report.setExamBeginTime(dates[0]);
                    report.setExamEndTime(dates[1]);
                    report.setCustId(cust.getUserId());
                    try
                    {
                        analysisReportService.doGenReport(report);
                    }
                    catch (Exception e)
                    {
                        logger.error("生成健康报告出错，客户ID：" + cust.getUserId() + "，分析报告时间段：" + DateUtil.toHtmlDate(dates[0])
                                + "至" + DateUtil.toHtmlDate(dates[1]) + "。", e);
                    }
                }
            }
            custs = customerService.getCustomers(filter, c, maxSize);
        }
        logger.debug("多周分析报告生成结束.");
    }
    public void genWeek(Date firDate, Date secDate) throws Exception
    {
        logger.debug("多周分析报告生成开始。");
        BeanFactory beanFactory = BeanFactoryHelper.getBeanfactory();
        if (beanFactory == null)
        {
            logger.error("BeanFactory不存在，忽略本次分析报告调度。");
            return;
        }
        // 第一天
        if (firDate == null)
        {
            logger.error("生成时间段内健康报告失败，请指定开始时间。");
        }
        if (secDate == null)
        {
            logger.debug("未指定结束时间，以当前时间作为结束时间间。");
            secDate = new Date();
        }
        else
        {
            secDate = DateUtil.addDay(DateUtil.truncDay(secDate), 1);
        }
        // 查找第一个周一
        Calendar cal = Calendar.getInstance();
        cal.setTime(firDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        // 周一至周日分别对应值{2, 3, 4, 5, 6, 7, 1}
        int nowDW = cal.get(Calendar.DAY_OF_WEEK);
        if (nowDW != Calendar.SUNDAY)
        {
            if (nowDW != Calendar.MONDAY)
            {
                cal.add(Calendar.DAY_OF_MONTH, 9 - nowDW);
            }
            else
            {
                if (cal.getTime().getTime() < firDate.getTime())
                {
                    cal.add(Calendar.DAY_OF_MONTH, 7);
                }
            }
        }
        else
        {
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        List<Date[]> dateSeq = new ArrayList<Date[]>();
        while (cal.getTimeInMillis() < secDate.getTime())
        {
            Date startDate = cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH, 6);
            Date endDate = cal.getTime();
            if (endDate.getTime() >= secDate.getTime())
            {
                break;
            }
            dateSeq.add(new Date[]
            {startDate, endDate});
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        if (dateSeq.isEmpty())
        {
            return;
        }
        int maxSize = 500;
        // 生成报告
        CustomerService customerService = (CustomerService) beanFactory.getBean("customerService");
        AnalysisReportService analysisReportService = (AnalysisReportService) beanFactory
                .getBean("analysisReportService");
        int c = 0;
        Map<String, Object> filter = new HashMap<String, Object>();
        List<Customer> custs = customerService.getCustomers(filter, c, maxSize);
        // 报告参数值
        AnalysisReport report = new AnalysisReport();
        while (custs != null && !custs.isEmpty())
        {
            // 总查询居民数
            c = c + custs.size();
            // 处理每个居民分析报告
            for (Customer cust : custs)
            {
                for (Date[] dates : dateSeq)
                {
                    report.setExamBeginTime(dates[0]);
                    report.setExamEndTime(dates[1]);
                    report.setCustId(cust.getUserId());
                    try
                    {
                        analysisReportService.doGenReport(report);
                    }
                    catch (Exception e)
                    {
                        logger.error("生成健康报告出错，客户ID：" + cust.getUserId() + "，分析报告时间段：" + DateUtil.toHtmlDate(dates[0])
                                + "至" + DateUtil.toHtmlDate(dates[1]) + "。", e);
                    }
                }
            }
            custs = customerService.getCustomers(filter, c, maxSize);
        }
        logger.debug("多周分析报告生成结束.");
    }

    public static void main(String[] args)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_MONTH, -3);
        System.out.println(DateUtil.toHtmlTime(cal.getTime()));
        // 周一至周日分别对应值{2, 3, 4, 5, 6, 7, 1}
        int nowDW = cal.get(Calendar.DAY_OF_WEEK);
        if (nowDW != Calendar.SUNDAY)
        {
            cal.add(Calendar.DAY_OF_MONTH, -nowDW - 5);
        }
        else
        {
            cal.add(Calendar.DAY_OF_MONTH, -13);
        }
        Date startDate = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 6);
        Date endDate = cal.getTime();
        System.out.println(DateUtil.toHtmlTime(startDate));
        System.out.println(DateUtil.toHtmlTime(endDate));
    }

}
