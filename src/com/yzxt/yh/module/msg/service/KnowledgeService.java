package com.yzxt.yh.module.msg.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.msg.bean.Knowledge;
import com.yzxt.yh.module.msg.bean.Richtext;
import com.yzxt.yh.module.msg.dao.KnowledgeCatalogDao;
import com.yzxt.yh.module.msg.dao.KnowledgeDao;
import com.yzxt.yh.util.StringUtil;

/**
 * 健康知识服务类
 * @author f
 *
 */
@Transactional(ConstTM.DEFAULT)
public class KnowledgeService
{
    private KnowledgeDao knowledgeDao;

    private KnowledgeCatalogDao knowledgeCatalogDao;

    private RichtextService richtextService;

    public KnowledgeDao getKnowledgeDao()
    {
        return knowledgeDao;
    }

    public void setKnowledgeDao(KnowledgeDao knowledgeDao)
    {
        this.knowledgeDao = knowledgeDao;
    }

    public KnowledgeCatalogDao getKnowledgeCatalogDao()
    {
        return knowledgeCatalogDao;
    }

    public void setKnowledgeCatalogDao(KnowledgeCatalogDao knowledgeCatalogDao)
    {
        this.knowledgeCatalogDao = knowledgeCatalogDao;
    }

    public RichtextService getRichtextService()
    {
        return richtextService;
    }

    public void setRichtextService(RichtextService richtextService)
    {
        this.richtextService = richtextService;
    }

    /**
     * 添加健康知识信息
     * @param knowledge
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result add(Knowledge knowledge) throws Exception
    {
        Result result = new Result();
        Timestamp now = new Timestamp(System.currentTimeMillis() + knowledge.getCount() * 1000);
        knowledge.setCreateTime(now);
        knowledge.setUpdateTime(now);
        // 保存富文本信息
        String richtextId = null;
        Richtext richtext = new Richtext();
        richtext.setSummary(knowledge.getSummary());
        richtext.setContent(knowledge.getContent());
        richtextId = richtextService.add(richtext);
        knowledge.setRichtextId(richtextId);
        // 保存资讯，获取资讯ID
        knowledgeDao.insert(knowledge);
        result.setState(Result.STATE_SUCESS);
        return result;
    }

    /**
     * 更新健康知识信息
     * @param knowledge
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result update(Knowledge knowledge) throws Exception
    {
        Result result = new Result();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String id = knowledge.getId();
        // 查询未修改的资讯信息
        Knowledge oldKnowledge = knowledgeDao.get(id);
        if (oldKnowledge == null)
        {
            result.setState(-2);
            result.setMsg("修改出错，此健康知识已被删除！");
            return result;
        }
        knowledge.setUpdateTime(now);
        // 保存富文本信息
        String oldRichtextId = oldKnowledge.getRichtextId();
        if (StringUtil.isNotEmpty(oldRichtextId))
        {
            richtextService.delete(oldRichtextId);
        }
        Richtext richtext = new Richtext();
        richtext.setSummary(knowledge.getSummary());
        richtext.setContent(knowledge.getContent());
        String newRichtextId = richtextService.add(richtext);
        knowledge.setRichtextId(newRichtextId);
        // 保存
        knowledgeDao.update(knowledge);
        result.setState(Result.STATE_SUCESS);
        return result;
    }

    /**
     * 加载健康知识信息
     * @param filter
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Knowledge load(String id) throws Exception
    {
        Knowledge infomation = knowledgeDao.load(id);
        return infomation;
    }

    /**
     * 删除健康知识信息
     * @param id
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result delete(String id) throws Exception
    {
        Result result = new Result();
        Knowledge knowledge = knowledgeDao.get(id);
        if (knowledge == null)
        {
            result.setState(-2);
            result.setMsg("指定的资讯不存在！");
            return result;
        }
        // 删除富文件信息
        richtextService.delete(knowledge.getRichtextId());
        // 删除主体信息
        knowledgeDao.delete(id);
        // 保存删除结果
        result.setState(Result.STATE_SUCESS);
        return result;
    }

    /**
     * 分页查询健康知识信息
     * @param filter
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<Knowledge> queryKnowledgeByPage(Map<String, Object> filter, int page, int pageSize)
            throws Exception
    {
        return knowledgeDao.queryKnowledgeByPage(filter, page, pageSize);
    }

    /**
     * 查询健康知识信息，指定的最大条数，主要用于客户端查询
     * @param filter
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Knowledge> queryKnowledgesBySyn(Map<String, Object> filter) throws Exception
    {
        return knowledgeDao.queryKnowledgesBySyn(filter);
    }

}
