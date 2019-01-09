package com.yzxt.yh.sch.job;

import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.BeanFactory;

import com.yzxt.fw.server.BeanFactoryHelper;
import com.yzxt.yh.module.sys.service.UserService;

public class MonthJob implements Job
{
    private static Logger logger = Logger.getLogger(MonthJob.class);

    /**
     * 定期处理数据
     */
    public void execute(JobExecutionContext context) throws JobExecutionException
    {
        try
        {
            BeanFactory beanFactory = BeanFactoryHelper.getBeanfactory();
            if (beanFactory == null)
            {
                logger.error("BeanFactory不存在，忽略本次处理月度数据调度。");
                return;
            }
            UserService userService = (UserService) beanFactory.getBean("userService");
            userService.deleteExpirySession(new Date());
        }
        catch (Exception e)
        {
            logger.error("处理月度数据出错。", e);
        }
    }

}
