package com.yzxt.yh.sch.job;

import java.sql.Timestamp;
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
import com.yzxt.yh.module.chk.service.CheckRemindService;
import com.yzxt.yh.module.msg.bean.CheckRemind;
import com.yzxt.yh.util.DateUtil;

public class CheckRemindJob implements Job
{
    private Logger logger = Logger.getLogger(CheckRemindJob.class);

    /**
     * 未检测数据用户提醒
     */
    public void execute(JobExecutionContext context) throws JobExecutionException
    {
        logger.debug("用户未检测数据提醒调度开始.");
        try
        {
            remind();
        }
        catch (Exception e)
        {
            logger.error("用户未检测数据提醒调度出错。", e);
        }
        logger.debug("用户未检测数据提醒调度结束.");
    }

    /**
     * 用户未检测数据提醒
     * @param today
     * @throws Exception
     */
    public void remind() throws Exception
    {
        BeanFactory beanFactory = BeanFactoryHelper.getBeanfactory();
        if (beanFactory == null)
        {
            logger.error("BeanFactory不存在，忽略本次检测提醒调度。");
            return;
        }
        int maxSize = 500;
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put("today", DateUtil.truncDay(new Date()));
        CheckRemindService checkRemindService = (CheckRemindService) beanFactory.getBean("checkRemindService");
        List<CheckRemind> remindCusts = checkRemindService.getNeedRemindCust(filter, maxSize);
        while (remindCusts != null && !remindCusts.isEmpty())
        {
            for (CheckRemind checkRemind : remindCusts)
            {
                checkRemind.setLastRemindTime(new Timestamp(System.currentTimeMillis()));
                checkRemindService.saveRemind(checkRemind);
            }
            // 重新查询未处理数据
            remindCusts = checkRemindService.getNeedRemindCust(filter, maxSize);
        }
    }

}
