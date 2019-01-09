package com.yzxt.yh.module.msg.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.msg.bean.KnowledgeCatalog;
import com.yzxt.yh.module.msg.dao.KnowledgeCatalogDao;

/**
 * 健康知识目录服务类
 * @author f
 *
 */
@Transactional(ConstTM.DEFAULT)
public class KnowledgeCatalogService
{
    private KnowledgeCatalogDao knowledgeCatalogDao;

    public KnowledgeCatalogDao getKnowledgeCatalogDao()
    {
        return knowledgeCatalogDao;
    }

    public void setKnowledgeCatalogDao(KnowledgeCatalogDao knowledgeCatalogDao)
    {
        this.knowledgeCatalogDao = knowledgeCatalogDao;
    }

    /**
     * 查询健康知识目录信息，指定的最大条数，主要用于客户端查询
     * @param filter
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<KnowledgeCatalog> queryKnowledgeCatalogsBySyn(Map<String, Object> filter) throws Exception
    {
        return knowledgeCatalogDao.queryKnowledgeCatalogsBySyn(filter);
    }

    /**
     * 目录树获取子节点
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<KnowledgeCatalog> getTreeNodeChildren(String parentId)
    {
        return knowledgeCatalogDao.getTreeNodeChildren(parentId);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<KnowledgeCatalog> getKnowledgeCatalogByName(String parentname)
    {
        return knowledgeCatalogDao.getKnowledgeCatalogByName(parentname);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<KnowledgeCatalog> getKnowledgeCatalogById(String parentId)
    {
        return knowledgeCatalogDao.getKnowledgeCatalogById(parentId);
    }

}
