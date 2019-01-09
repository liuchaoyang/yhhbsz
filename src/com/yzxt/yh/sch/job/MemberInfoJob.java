package com.yzxt.yh.sch.job;

import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.springframework.beans.factory.BeanFactory;

import com.yzxt.fw.server.BeanFactoryHelper;
import com.yzxt.yh.module.svb.service.MemberInfoService;

public class MemberInfoJob implements StatefulJob
{
    private Logger logger = Logger.getLogger(MemberInfoJob.class);

    /**
     * 客户会员过期处理
     */
    public void execute(JobExecutionContext context) throws JobExecutionException
    {
        logger.debug("会员过期处理开始。");
        try
        {
            handleOutdate(new Date());
        }
        catch (Exception e)
        {
            logger.error("会员过期处理出错。", e);
        }
        logger.debug("会员过期处理结束。");
    }

    /**
     * 处理会员过期
     * @param today
     * @throws Exception
     */
    public void handleOutdate(Date now) throws Exception
    {
        BeanFactory beanFactory = BeanFactoryHelper.getBeanfactory();
        if (beanFactory == null)
        {
            logger.error("BeanFactory不存在，忽略本次会员过期处理。");
            return;
        }
        MemberInfoService memberInfoService = (MemberInfoService) beanFactory.getBean("memberInfoService");
        memberInfoService.updateStatusByOutDate(now);
    }

}
