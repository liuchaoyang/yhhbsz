package com.yzxt.yh.module.msg.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.msg.bean.InfoCatalog;
import com.yzxt.yh.module.msg.dao.InfoCatalogDao;

@Transactional(ConstTM.DEFAULT)
public class InfoCatalogService
{

    private InfoCatalogDao infoCatalogDao;

    public InfoCatalogDao getInfoCatalogDao()
    {
        return infoCatalogDao;
    }

    public void setInfoCatalogDao(InfoCatalogDao infoCatalogDao)
    {
        this.infoCatalogDao = infoCatalogDao;
    }

    /**
     * 分页查询专题信息
     * @param filter
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<InfoCatalog> getInfoTopicByPage(Map<String, Object> filter, int page, int pageSize)
            throws Exception
    {
        return infoCatalogDao.findInfoTopicList(filter, page, pageSize);
    }

    /**
     * 根据资讯目录ID，获取资讯目录信息
     * @param catalogId
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public InfoCatalog getInfoCatalog(String catalogId)
    {
        return infoCatalogDao.get(catalogId);
    }

    /**
     * 根据专题ID，获取专题信息
     * @param catalogId
     * @return
     */
    public InfoCatalog getInfoCatalogById(InfoCatalog infoCatalog)
    {
        return infoCatalog;
        /*return infoCatalogDao.getInfoCatalogById(infoCatalog);*/

    }

    /**
     * 查询所有的专题信息
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<InfoCatalog> getInfoColumns()
    {
        return infoCatalogDao.getInfoColumns();
    }

    /**
     * 根据用户类型查询专题信息
     * @param userType
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<InfoCatalog> getInfoColumnsByUserType(Integer userType, boolean withoutPredefined)
    {
        return infoCatalogDao.getInfoColumnsByUserType(userType, withoutPredefined);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int addInfoTopic(InfoCatalog infoCatalog) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        infoCatalog.setCreateTime(now);
        infoCatalog.setUpdateTime(now);
        infoCatalog.setPredefined(2);
        if (infoCatalogDao.isInfoTopicNameExist(infoCatalog.getName()))
        {
            return -12;
        }
        infoCatalogDao.addInfoTopic(infoCatalog);
        return 1;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int updateInfoTopic(InfoCatalog infoCatalog) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        infoCatalog.setUpdateTime(now);
        infoCatalog.setType(2);
        infoCatalog.setPredefined(2);
        infoCatalog.setState(1);
        infoCatalog.setSeq(11);
        infoCatalogDao.updateInfoTopic(infoCatalog);
        return 1;
    }

    /**
     * 作废 专题
     * @param id
     * @author huanggang
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int updateStateInfoTopic(InfoCatalog infoCatalog) throws Exception
    {
        if (infoCatalog.getState().equals(getInfoCatalogById(infoCatalog)))
        {
        }
        Timestamp now = new Timestamp(System.currentTimeMillis());
        infoCatalog.setUpdateTime(now);
        infoCatalog.setState(2);
        infoCatalogDao.updateStateInfoTopic(infoCatalog);
        return 1;
    }

    /**
     * 生效专题
     * @param id
     * @author huanggang
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int updateState1InfoTopic(InfoCatalog infoCatalog) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        infoCatalog.setUpdateTime(now);
        infoCatalog.setState(1);
        infoCatalogDao.updateStateInfoTopic(infoCatalog);
        return 1;
    }

    /**
     * 栏目模块方法
     * 
     * @author huanggang
     *
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<InfoCatalog> getInfoCatalogList()
    {
        return infoCatalogDao.getInfoCatalogList();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int addInfoCatalog(InfoCatalog infoCatalog) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        infoCatalog.setCreateTime(now);
        infoCatalog.setUpdateTime(now);
        infoCatalog.setPredefined(2);
        if (infoCatalogDao.isInfoTopicNameExist(infoCatalog.getName()))
        {
            return -12;
        }
        infoCatalogDao.addInfoCatalog(infoCatalog);
        return 1;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int updateInfoCatalog(InfoCatalog infoCatalog) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        infoCatalog.setUpdateTime(now);
        infoCatalog.setType(1);
        infoCatalog.setPredefined(2);
        infoCatalog.setState(1);
        infoCatalog.setSeq(11);
        infoCatalogDao.updateInfoCatalog(infoCatalog);
        return 1;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int updateInfoCatalogStateNo(InfoCatalog infoCatalog) throws Exception
    {
        if (infoCatalog.getState().equals(getInfoCatalogById(infoCatalog)))
        {
        }
        Timestamp now = new Timestamp(System.currentTimeMillis());
        infoCatalog.setUpdateTime(now);
        infoCatalog.setState(2);
        infoCatalogDao.updateInfoCatalogState(infoCatalog);
        return 1;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int updateInfoCatalogState(InfoCatalog infoCatalog) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        infoCatalog.setUpdateTime(now);
        infoCatalog.setState(1);
        infoCatalogDao.updateInfoCatalogState(infoCatalog);
        return 1;
    }

}
