package com.yzxt.yh.module.sys.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.sys.bean.ClientVersion;
import com.yzxt.yh.module.sys.dao.ClientVersionDao;

/**
 * 客户端服务类
 * @author f
 *
 */
@Transactional(ConstTM.DEFAULT)
public class ClientVersionService
{

    private ClientVersionDao clientVersionDao = null;

    public ClientVersionDao getClientVersionDao()
    {
        return clientVersionDao;
    }

    public void setClientVersionDao(ClientVersionDao clientVersionDao)
    {
        this.clientVersionDao = clientVersionDao;
    }

    /**
     * 获取最新客户端版本
     * 
     * @param appType
     * @param deviceType
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ClientVersion getLatestVersion(String appType, String deviceType)
    {
        return clientVersionDao.getLatestVersion(appType, deviceType);
    }

    /**
     * 查询最新客户端版本
     * @param filter
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<ClientVersion> queryLatestVersions(Map<String, Object> filter)
    {
        return clientVersionDao.queryLatestVersions(filter);
    }

    /**
     * 查询客户端版本
     * 2015.8.16 
     * @author h
     * @param filter
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<ClientVersion> queryVersions(Map<String, Object> filter)
    {
        return clientVersionDao.queryVersions(filter);
    }

}
