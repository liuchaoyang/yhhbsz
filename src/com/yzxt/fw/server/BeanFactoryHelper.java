package com.yzxt.fw.server;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

public class BeanFactoryHelper implements BeanFactoryAware
{
    private static BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException
    {
        BeanFactoryHelper.beanFactory = beanFactory;
    }

    public static BeanFactory getBeanfactory()
    {
        return beanFactory;
    }

}