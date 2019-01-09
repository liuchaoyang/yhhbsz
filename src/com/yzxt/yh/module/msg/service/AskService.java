package com.yzxt.yh.module.msg.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.msg.bean.Ask;
import com.yzxt.yh.module.msg.bean.AskCatalog;
import com.yzxt.yh.module.msg.bean.AskReply;
import com.yzxt.yh.module.msg.bean.Richtext;
import com.yzxt.yh.module.msg.dao.AskCatalogDao;
import com.yzxt.yh.module.msg.dao.AskDao;
import com.yzxt.yh.module.msg.dao.AskReplyDao;
import com.yzxt.yh.util.StringUtil;

/**
 * 健康问答Service类
 * @author f
 *
 */
@Transactional(ConstTM.DEFAULT)
public class AskService
{
    private AskDao askDao;

    private AskReplyDao askReplyDao;

    private AskCatalogDao askCatalogDao;

    private RichtextService richtextService;

    public AskDao getAskDao()
    {
        return askDao;
    }

    public void setAskDao(AskDao askDao)
    {
        this.askDao = askDao;
    }

    public AskReplyDao getAskReplyDao()
    {
        return askReplyDao;
    }

    public void setAskReplyDao(AskReplyDao askReplyDao)
    {
        this.askReplyDao = askReplyDao;
    }

    public AskCatalogDao getAskCatalogDao()
    {
        return askCatalogDao;
    }

    public void setAskCatalogDao(AskCatalogDao askCatalogDao)
    {
        this.askCatalogDao = askCatalogDao;
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
     * 获取一级目录
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<AskCatalog> getTopLevelAskCatalogs()
    {
        return askCatalogDao.getTopLevelAskCatalogs();
    }

    /**
     * 获取子目录
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<AskCatalog> getChildrenAskCatalogs(String cataId)
    {
        return askCatalogDao.getChildrenAskCatalogs(cataId);
    }

    /**
     * 添加健康问答
     * @param ask
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result add(Ask ask) throws Exception
    {
        Result result = new Result();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        ask.setCreateTime(now);
        ask.setUpdateTime(now);
        // 保存富文本信息
        String richtextId = null;
        Richtext richtext = new Richtext();
        richtext.setSummary(ask.getSummary());
        richtext.setContent(ask.getContent());
        richtextId = richtextService.add(richtext);
        ask.setRichtextId(richtextId);
        ask.setReplyCount(0);
        ask.setViewCount(0);
        String id = askDao.insert(ask);
        result.setData(id);
        result.setState(Result.STATE_SUCESS);
        return result;
    }

    /**
     * 更新健康问答
     * @param ask
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result update(Ask ask) throws Exception
    {
        Result result = new Result();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String askId = ask.getId();
        // 查询未修改的资讯信息
        Ask oldAsk = askDao.get(askId);
        if (oldAsk == null)
        {
            result.setState(-2);
            result.setMsg("修改出错，此问答已被删除！");
            return result;
        }
        ask.setUpdateTime(now);
        // 保存富文本信息
        String oldRichtextId = oldAsk.getRichtextId();
        if (StringUtil.isNotEmpty(oldRichtextId))
        {
            richtextService.delete(oldRichtextId);
        }
        Richtext richtext = new Richtext();
        richtext.setSummary(ask.getSummary());
        richtext.setContent(ask.getContent());
        String newRichtextId = richtextService.add(richtext);
        ask.setRichtextId(newRichtextId);
        // 保存资讯，获取资讯ID
        ask.setType(1);
        askDao.update(ask);
        result.setState(Result.STATE_SUCESS);
        return result;
    }

    /**
     * 加载提问信息
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Ask get(String id)
    {
        return askDao.get(id);
    }

    /**
     * 加载提问信息
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Ask load(String id)
    {
        return askDao.load(id);
    }

    /**
     * 加载提问信息
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<AskReply> getReplysByAsk(String id)
    {
        return askReplyDao.getReplysByAsk(id);
    }

    /**
     * 删除问答信息
     * @param id
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result delete(String id) throws Exception
    {
        Result result = new Result();
        Ask ask = askDao.get(id);
        if (ask == null)
        {
            result.setState(-2);
            result.setMsg("指定的提问不存在！");
            return result;
        }
        // 删除富文件信息
        richtextService.delete(ask.getRichtextId());
        // 删除回复信息
        askReplyDao.deleteReplysByAsk(id);
        // 删除问答信息
        askDao.delete(id);
        // 保存删除结果
        result.setState(Result.STATE_SUCESS);
        return result;
    }

    /**
     * 获取问答目录
     * @param askCatalogId
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public AskCatalog getAskCatalog(String askCatalogId)
    {
        return askCatalogDao.get(askCatalogId);
    }

    /**
     * 分页查询问答信息
     * @param filter
     * @param page
     * @param pageSize
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<Ask> queryAskByPage(Map<String, Object> filter, int page, int pageSize) throws Exception
    {
        return askDao.queryAskByPage(filter, page, pageSize);
    }

    /**
     * 分页查询问答信息，主要用户首页查询
     * @param filter
     * @param page
     * @param pageSize
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<Ask> queryDocHomeAsk(Map<String, Object> filter, int page, int pageSize) throws Exception
    {
        return askDao.queryDocHomeAsk(filter, page, pageSize);
    }

    /**
     * 添加健康问答回复
     * @param ask
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result addAskReply(AskReply askReply) throws Exception
    {
        Result result = new Result();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        askReply.setCreateTime(now);
        askReply.setUpdateTime(now);
        // 保存富文本信息
        String richtextId = null;
        Richtext richtext = new Richtext();
        richtext.setSummary(askReply.getSummary());
        richtext.setContent(askReply.getContent());
        richtextId = richtextService.add(richtext);
        askReply.setRichtextId(richtextId);
        // 保存资讯，获取资讯ID
        String askReplyId = askReplyDao.insert(askReply);
        // 增加问题回复次数
        askDao.updateReplyCountByChange(askReply.getAskId(), 1);
        result.setData(askReplyId);
        result.setState(Result.STATE_SUCESS);
        return result;
    }

    /**
     * 更新健康问答回复
     * @param ask
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result updateAskReply(AskReply askReply) throws Exception
    {
        Result result = new Result();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String askReplyId = askReply.getId();
        // 查询未修改的资讯信息
        AskReply oldAskReply = askReplyDao.get(askReplyId);
        if (oldAskReply == null)
        {
            result.setState(-2);
            result.setMsg("删除出错，此回复已不存在！");
            return result;
        }
        askReply.setUpdateTime(now);
        // 保存富文本信息
        String oldRichtextId = oldAskReply.getRichtextId();
        if (StringUtil.isNotEmpty(oldRichtextId))
        {
            richtextService.delete(oldRichtextId);
        }
        Richtext richtext = new Richtext();
        richtext.setSummary(askReply.getSummary());
        richtext.setContent(askReply.getContent());
        String newRichtextId = richtextService.add(richtext);
        askReply.setRichtextId(newRichtextId);
        // 保存回复
        askReplyDao.update(askReply);
        result.setState(Result.STATE_SUCESS);
        result.setState(Result.STATE_SUCESS);
        return result;
    }

    /**
     * 删除问答回复信息
     * @param id
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result deleteAskReply(String askReplyId) throws Exception
    {
        Result result = new Result();
        AskReply askReply = askReplyDao.get(askReplyId);
        if (askReply == null)
        {
            result.setState(-2);
            result.setMsg("指定的回复不存在！");
            return result;
        }
        // 删除富文件信息
        richtextService.delete(askReply.getRichtextId());
        // 删除回复信息
        askReplyDao.delete(askReplyId);
        // 减少问题回复次数
        askDao.updateReplyCountByChange(askReply.getAskId(), -1);
        // 保存删除结果
        result.setState(Result.STATE_SUCESS);
        return result;
    }

    //---------------------------客户端同步使用的方法---------------------------//
    /**
     * 获取最新需要同步的时间，用户客户端查询
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Timestamp getLatestCatalogSynTime()
    {
        return askCatalogDao.getLatestCatalogSynTime();
    }

    /**
     * 获取所有二级目录，用户客户端查询
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<AskCatalog> getCatalogsBySyn()
    {
        return askCatalogDao.getCatalogsBySyn();
    }

    /**
     * 查询问题回复列表，用户客户端查询
     * @param filter
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<AskReply> queryReplysBySyn(Map<String, Object> filter)
    {
        return askReplyDao.queryReplysBySyn(filter);
    }

    /**
     * 查询问题列表，用户客户端查询
     * @param filter
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Ask> queryAsksBySyn(Map<String, Object> filter)
    {
        List<Ask> asks = askDao.queryAsksBySyn(filter);
        if (asks != null && asks.size() > 0)
        {
            for (Ask ask : asks)
            {
                AskReply askReply = askReplyDao.getAskReplyBySyn(ask.getId());
                ask.setAskReply(askReply);
            }
        }
        return asks;
    }
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Ask> getCustId(String custId,String doctorId)
    {
        return askDao.getCustId(custId,doctorId);
    }

}
