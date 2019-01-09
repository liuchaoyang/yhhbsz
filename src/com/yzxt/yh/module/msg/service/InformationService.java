package com.yzxt.yh.module.msg.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.msg.bean.InfoCatalog;
import com.yzxt.yh.module.msg.bean.Information;
import com.yzxt.yh.module.msg.bean.Richtext;
import com.yzxt.yh.module.msg.dao.InfoCatalogRelateDao;
import com.yzxt.yh.module.msg.dao.InformationDao;
import com.yzxt.yh.module.sys.bean.FileDesc;
import com.yzxt.yh.module.sys.service.FileDescService;
import com.yzxt.yh.util.StringUtil;

/**
 * 资讯服务类
 * @author f
 *
 */
@Transactional(ConstTM.DEFAULT)
public class InformationService
{
    private InformationDao informationDao;

    private InfoCatalogRelateDao infoCatalogRelateDao;

    private FileDescService fileDescService;

    private RichtextService richtextService;

    public InformationDao getInformationDao()
    {
        return informationDao;
    }

    public void setInformationDao(InformationDao informationDao)
    {
        this.informationDao = informationDao;
    }

    public InfoCatalogRelateDao getInfoCatalogRelateDao()
    {
        return infoCatalogRelateDao;
    }

    public void setInfoCatalogRelateDao(InfoCatalogRelateDao infoCatalogRelateDao)
    {
        this.infoCatalogRelateDao = infoCatalogRelateDao;
    }

    public FileDescService getFileDescService()
    {
        return fileDescService;
    }

    public void setFileDescService(FileDescService fileDescService)
    {
        this.fileDescService = fileDescService;
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
     * 添加资讯信息
     * @param infomation
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result add(Information information) throws Exception
    {
        Result result = new Result();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        information.setCreateTime(now);
        information.setUpdateTime(now);
        // 保存图标文件
        String iconFileId = null;
        FileDesc iconFile = information.getIconFile();
        if (iconFile != null)
        {
            iconFileId = fileDescService.add(iconFile);
        }
        information.setIconFileId(iconFileId);
        // 保存富文本信息
        String richtextId = null;
        Richtext richtext = new Richtext();
        richtext.setSummary(information.getSummary());
        richtext.setContent(information.getContent());
        richtextId = richtextService.add(richtext);
        information.setRichtextId(richtextId);
        // 保存资讯，获取资讯ID
        String infoId = informationDao.insert(information);
        // 保存栏目信息
        String[] belongColumns = StringUtil.splitWithoutEmpty(information.getBelongColumns(), ",");
        if (belongColumns != null && belongColumns.length > 0)
        {
            infoCatalogRelateDao.addInfoCatalogRelates(infoId, belongColumns);
        }
        // 选择了专题，保存资讯与专题关系
        if (StringUtil.isNotEmpty(information.getBelongTopic()))
        {
            infoCatalogRelateDao.addInfoCatalogRelate(infoId, information.getBelongTopic());
        }
        result.setState(Result.STATE_SUCESS);
        return result;
    }

    /**
     * 更新资讯信息
     * @param infomation
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result update(Information infomation) throws Exception
    {
        Result result = new Result();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String infoId = infomation.getId();
        // 查询未修改的资讯信息
        Information oldInfomation = informationDao.get(infoId);
        if (oldInfomation == null)
        {
            result.setState(-2);
            result.setMsg("发布出错，此资讯已被删除！");
            return result;
        }
        infomation.setUpdateTime(now);
        //更新存图标文件
        FileDesc iconFile = infomation.getIconFile();
        if (iconFile != null)
        {
            String oldIconFileId = oldInfomation.getIconFileId();
            if (StringUtil.isNotEmpty(oldIconFileId))
            {
                fileDescService.delete(oldIconFileId);
            }
            String newIconFileId = fileDescService.add(iconFile);
            infomation.setIconFileId(newIconFileId);
        }
        else
        {
            infomation.setIconFileId(oldInfomation.getIconFileId());
        }
        // 保存富文本信息
        String oldRichtextId = oldInfomation.getRichtextId();
        if (StringUtil.isNotEmpty(oldRichtextId))
        {
            richtextService.delete(oldRichtextId);
        }
        Richtext richtext = new Richtext();
        richtext.setSummary(infomation.getSummary());
        richtext.setContent(infomation.getContent());
        String newRichtextId = richtextService.add(richtext);
        infomation.setRichtextId(newRichtextId);
        // 保存资讯，获取资讯ID
        informationDao.update(infomation);
        // 更新栏目专题信息
        infoCatalogRelateDao.deleteInfoCatalogRelateByInfo(infoId);
        String[] belongColumns = StringUtil.splitWithoutEmpty(infomation.getBelongColumns(), ",");
        if (belongColumns != null && belongColumns.length > 0)
        {
            infoCatalogRelateDao.addInfoCatalogRelates(infoId, belongColumns);
        }
        // 选择了专题，保存资讯与专题关系
        if (StringUtil.isNotEmpty(infomation.getBelongTopic()))
        {
            infoCatalogRelateDao.addInfoCatalogRelate(infoId, infomation.getBelongTopic());
        }
        result.setState(Result.STATE_SUCESS);
        return result;
    }

    /**
     * 加载资讯信息
     * @param filter
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Information load(String id) throws Exception
    {
        Information information = informationDao.load(id);
        if (information != null)
        {
            // 加载栏目和专题信息
            StringBuilder cSb = new StringBuilder();
            String[] columns = infoCatalogRelateDao.getInfoColumns(id);
            if (columns != null && columns.length > 0)
            {
                int count = 0;
                for (String column : columns)
                {
                    if (StringUtil.isNotEmpty(column))
                    {
                        if (count > 0)
                        {
                            cSb.append(",");
                        }
                        cSb.append(column);
                        count++;
                    }
                }
            }
            information.setBelongColumns(cSb.toString());
            InfoCatalog topic = infoCatalogRelateDao.getInfoTopic(id);
            if (topic != null)
            {
                information.setBelongTopic(StringUtil.trim(topic.getId()));
                information.setBelongTopicName(topic.getName());
            }
        }
        return information;
    }

    /**
     * 删除资讯信息
     * @param filter
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result delete(String id) throws Exception
    {
        Result result = new Result();
        Information information = informationDao.get(id);
        if (information == null)
        {
            result.setState(-2);
            result.setMsg("指定的资讯不存在！");
            return result;
        }
        // 删除图标文件
        String iconFileId = information.getIconFileId();
        if (StringUtil.isNotEmpty(iconFileId))
        {
            fileDescService.delete(iconFileId);
        }
        // 删除富文件信息
        richtextService.delete(information.getRichtextId());
        // 删除资讯栏目等关系
        infoCatalogRelateDao.deleteInfoCatalogRelateByInfo(id);
        // 删除资讯信息
        informationDao.delete(id);
        // 保存删除结果
        result.setState(Result.STATE_SUCESS);
        return result;
    }

    /**
     * 分页查询栏目下的资讯信息
     * @param filter
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<Information> queryInfomationByPage(Map<String, Object> filter, int page, int pageSize)
            throws Exception
    {
        return informationDao.queryInfomationByPage(filter, page, pageSize);
    }

    /**
     * 分页查询栏目下的资讯信息
     * @param filter
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<Information> queryCustHomeInfo(Map<String, Object> filter, int page, int pageSize)
            throws Exception
    {
        return informationDao.queryCustHomeInfo(filter, page, pageSize);
    }

    /**
     * 查询栏目下的资讯信息，指定的最大条数，主要用于客户端查询
     * @param filter
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Information> queryInfomationsBySyn(Map<String, Object> filter) throws Exception
    {
        return informationDao.queryInfomationsBySyn(filter);
    }

    /**
     * 查询专题信息，指定的最大条数，主要用于客户端查询
     * @param filter
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<InfoCatalog> queryTopicsBySyn(Map<String, Object> filter) throws Exception
    {
        return informationDao.queryTopicsBySyn(filter);
    }

    /**
     * 分页查询专题信息
     * @param filter
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<InfoCatalog> queryTopicByPage(Map<String, Object> filter, int page, int pageSize) throws Exception
    {
        return informationDao.queryTopicByPage(filter, page, pageSize);
    }
}
