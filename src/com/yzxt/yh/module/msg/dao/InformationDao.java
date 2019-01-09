package com.yzxt.yh.module.msg.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.msg.bean.InfoCatalog;
import com.yzxt.yh.module.msg.bean.Information;
import com.yzxt.yh.util.StringUtil;

public class InformationDao extends HibernateSupport<Information> implements BaseDao<Information>
{

    /**
     * 插入健康资讯
     * @param infomation
     * @return 主键ID
     * @throws Exception 
     */
    public String insert(Information information) throws Exception
    {
        super.save(information);
        return information.getId();
    }

    /**
     * 插入健康资讯
     * @param infomation
     * @return 主键ID
     * @throws Exception 
     */
    public int update(Information information) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update Information t set t.title = ?, t.level = ?");
        sql.append(", t.iconFileId = ?, t.richtextId = ?, t.src = ?");
        sql.append(", t.updateBy = ?, t.updateTime = ?");
        sql.append(" where t.id = ?");
        params.add(information.getTitle(), Hibernate.STRING);
        params.add(information.getLevel(), Hibernate.INTEGER);
        params.add(information.getIconFileId(), Hibernate.STRING);
        params.add(information.getRichtextId(), Hibernate.STRING);
        params.add(information.getSrc(), Hibernate.STRING);
        params.add(information.getUpdateBy(), Hibernate.STRING);
        params.add(information.getUpdateTime(), Hibernate.TIMESTAMP);
        params.add(information.getId(), Hibernate.STRING);
        return super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

    /**
     * 删除健康资讯
     * @param infomation
     * @return 主键ID
     * @throws Exception 
     */
    public int delete(String id) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("delete from Information t where t.id = ?");
        params.add(id, Hibernate.STRING);
        return super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

    /**
     * 插入健康资讯
     * @param infomation
     * @return 主键ID
     * @throws Exception 
     */
    public Information load(String id) throws Exception
    {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, t.title, t.richtext_id, t.level, t.icon_file_id, t.src");
        sql.append(", t.create_by, t.create_time, t.update_by, t.update_time");
        sql.append(", trt.summary, trt.content");
        sql.append(", tfd.name_ as icon_file_name, tfd.path");
        sql.append(", tu.name_ as update_by_name");
        sql.append(" from msg_information t");
        sql.append(" inner join msg_richtext trt on trt.id = t.richtext_id");
        sql.append(" left join sys_file_desc tfd on tfd.id = t.icon_file_id");
        sql.append(" left join sys_user tu on tu.id = t.update_by");
        sql.append(" where t.id = ?");
        Object[] objs = (Object[]) super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("title", Hibernate.STRING).addScalar("richtext_id", Hibernate.STRING)
                .addScalar("level", Hibernate.INTEGER).addScalar("icon_file_id", Hibernate.STRING)
                .addScalar("src", Hibernate.STRING).addScalar("create_by", Hibernate.STRING)
                .addScalar("create_time", Hibernate.TIMESTAMP).addScalar("update_by", Hibernate.STRING)
                .addScalar("update_time", Hibernate.TIMESTAMP).addScalar("summary", Hibernate.STRING)
                .addScalar("content", Hibernate.STRING).addScalar("icon_file_name", Hibernate.STRING)
                .addScalar("path", Hibernate.STRING).addScalar("update_by_name", Hibernate.STRING).setString(0, id)
                .uniqueResult();
        Information information = null;
        if (objs != null)
        {
            information = new Information();
            int i = 0;
            information.setId((String) objs[i]);
            i++;
            information.setTitle((String) objs[i]);
            i++;
            information.setRichtextId((String) objs[i]);
            i++;
            information.setLevel((Integer) objs[i]);
            i++;
            information.setIconFileId((String) objs[i]);
            i++;
            information.setSrc((String) objs[i]);
            i++;
            information.setCreateBy((String) objs[i]);
            i++;
            information.setCreateTime(objs[i] != null ? (Timestamp) objs[i] : null);
            i++;
            information.setUpdateBy((String) objs[i]);
            i++;
            information.setUpdateTime(objs[i] != null ? (Timestamp) objs[i] : null);
            i++;
            information.setSummary((String) objs[i]);
            i++;
            information.setContent((String) objs[i]);
            i++;
            information.setIconFileName((String) objs[i]);
            i++;
            information.setIconFilePath((String) objs[i]);
            i++;
            information.setUpdateByName((String) objs[i]);
            i++;
        }
        return information;
    }

    /**
     * 分页查询资讯信息
     */
    @SuppressWarnings("unchecked")
    public PageModel<Information> queryInfomationByPage(Map<String, Object> filter, int page, int pageSize)
            throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append("from msg_information t");
        mSql.append(" inner join msg_richtext trt on trt.id = t.richtext_id");
        mSql.append(" inner join sys_file_desc tsd on tsd.id = t.icon_file_id");
        mSql.append(" left join sys_user tu on tu.id = t.update_by");
        mSql.append(" where 1=1");
        // 所属栏目或专题
        String infoCatalogId = (String) filter.get("infoCatalogId");
        // 如果是按栏目和专题查询，需要作栏目和权限判断
        if (!Constant.INFO_COLUMN_MY_PUBLISH.equals(infoCatalogId))
        {
            // 当操作用户是居民，需要做权限判断
            // 如果是查询栏目下的资讯列表，如果栏目已经授权，则栏目下的所有资讯可见，不需要判断;
            // 如果是专题下的资讯，则要判断操作人对此资讯所栏目是否有操作权限
            //            Integer infoCatalogType = (Integer) filter.get("infoCatalogType");
            //            if (Constant.INFO_CATALOG_TYPE_TOPIC.equals(infoCatalogType))
            //            {
            //                Integer userType = (Integer) filter.get("userType");
            //                if (Constant.USER_TYPE_CUSTOMER.equals(userType))
            //                {
            //                    mSql.append(" and exists (select sicr.id from msg_info_catalog_relate sicr");
            //                    mSql.append(" inner join msg_info_catalog_auth sica on sica.column_id = sicr.catalog_id");
            //                    mSql.append(" where sicr.info_id = t.id)");
            //                }
            //            }
            // 所属栏目或专题
            mSql.append(" and exists (select sicr.id from msg_info_catalog_relate sicr where sicr.catalog_id = ? and sicr.info_id = t.id)");
            params.add(infoCatalogId, Hibernate.STRING);
        }
        else
        {
            mSql.append(" and t.update_by = ?");
            params.add((String) filter.get("userId"), Hibernate.STRING);
        }
        // 总启启记录数
        StringBuilder totalCountsql = new StringBuilder();
        totalCountsql.append("select count(*) as c ").append(mSql);
        Integer totalCount = (Integer) super.getSession().createSQLQuery(totalCountsql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.id, t.title, trt.summary, tu.name_, t.update_time ").append(mSql)
                .append(" order by t.create_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString())
                .setParameters(params.getVals(), params.getTypes()).list();
        List<Information> list = new ArrayList<Information>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Information info = new Information();
            idx = 0;
            info.setId((String) objs[idx]);
            idx++;
            info.setTitle((String) objs[idx]);
            idx++;
            info.setSummary((String) objs[idx]);
            idx++;
            info.setUpdateByName((String) objs[idx]);
            idx++;
            info.setUpdateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            list.add(info);
        }
        PageModel<Information> pageModel = new PageModel<Information>();
        pageModel.setTotal(totalCount);
        pageModel.setData(list);
        return pageModel;
    }

    /**
     * 分页查询资讯信息
     * @param filter
     * @param page
     * @param pageSize
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public PageModel<Information> queryCustHomeInfo(Map<String, Object> filter, int page, int pageSize)
            throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append("from msg_information t");
        mSql.append(" inner join msg_richtext trt on trt.id = t.richtext_id");
        mSql.append(" inner join sys_file_desc tsd on tsd.id = t.icon_file_id");
        mSql.append(" left join sys_user tu on tu.id = t.create_by");
        mSql.append(" where 1=1");
        // 总启启记录数
        StringBuilder totalCountsql = new StringBuilder();
        totalCountsql.append("select count(*) as c ").append(mSql);
        Integer totalCount = (Integer) super.getSession().createSQLQuery(totalCountsql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.id, t.title, trt.summary, tu.name_, t.create_time ").append(mSql)
                .append(" order by t.create_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString())
                .setParameters(params.getVals(), params.getTypes()).list();
        List<Information> list = new ArrayList<Information>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Information info = new Information();
            idx = 0;
            info.setId((String) objs[idx]);
            idx++;
            info.setTitle((String) objs[idx]);
            idx++;
            info.setSummary((String) objs[idx]);
            idx++;
            info.setCreateByName((String) objs[idx]);
            idx++;
            info.setCreateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            list.add(info);
        }
        PageModel<Information> pageModel = new PageModel<Information>();
        pageModel.setTotal(totalCount);
        pageModel.setData(list);
        return pageModel;
    }

    /**
     * 查询栏目下的资讯信息，指定的最大条数，主要用于客户端查询
     * @param filter
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Information> queryInfomationsBySyn(Map<String, Object> filter) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, t.title, t.src, trt.summary, tsd.path, tu.name_, t.update_time");
        sql.append(" from msg_information t");
        sql.append(" inner join msg_richtext trt on trt.id = t.richtext_id");
        sql.append(" inner join sys_file_desc tsd on tsd.id = t.icon_file_id");
        sql.append(" left join sys_user tu on tu.id = t.update_by");
        sql.append(" where 1=1");
        // 所属栏目或专题
        String infoCatalogId = (String) filter.get("infoCatalogId");
        // 当操作用户是居民，需要做权限判断
        // 如果是查询栏目下的资讯列表，如果栏目已经授权，则栏目下的所有资讯可见，不需要判断;
        // 如果是专题下的资讯，则要判断操作人对此资讯所栏目是否有操作权限
        Integer infoCatalogType = (Integer) filter.get("infoCatalogType");
        if (Constant.INFO_CATALOG_TYPE_TOPIC.equals(infoCatalogType))
        {
            Integer userType = (Integer) filter.get("userType");
            if (Constant.USER_TYPE_CUSTOMER.equals(userType))
            {
                sql.append(" and exists (select sicr.id from msg_info_catalog_relate sicr");
                sql.append(" inner join msg_info_catalog_auth sica on sica.column_id = sicr.catalog_id");
                sql.append(" where sicr.info_id = t.id)");
            }
        }
        // 所属栏目或专题
        sql.append(" and exists (select sicr.id from msg_info_catalog_relate sicr where sicr.catalog_id = ? and sicr.info_id = t.id)");
        params.add(infoCatalogId, Hibernate.STRING);
        // 等级
        Integer level = (Integer) filter.get("infoLevel");
        sql.append(" and t.level = ?");
        params.add(level, Hibernate.INTEGER);
        // 指定拉取时间
        Timestamp sysTime = (Timestamp) filter.get("sysTime");
        if (sysTime != null)
        {
            sql.append(" and t.update_time < ?");
            params.add(sysTime, Hibernate.TIMESTAMP);
        }
        // 取最新发布倒序
        sql.append(" order by t.update_time desc");
        // 取数据条数
        sql.append(" limit 0, ?");
        params.add(filter.get("maxSize"), Hibernate.INTEGER);
        // 查询数据
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString())
                .setParameters(params.getVals(), params.getTypes()).list();
        List<Information> list = new ArrayList<Information>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Information info = new Information();
            idx = 0;
            info.setId((String) objs[idx]);
            idx++;
            info.setTitle((String) objs[idx]);
            idx++;
            info.setSrc((String) objs[idx]);
            idx++;
            info.setSummary((String) objs[idx]);
            idx++;
            info.setIconFilePath((String) objs[idx]);
            idx++;
            info.setUpdateByName((String) objs[idx]);
            idx++;
            info.setUpdateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            list.add(info);
        }
        return list;

    }

    /**
     * 查询专题信息，指定的最大条数，主要用于客户端查询
     * @param filter
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<InfoCatalog> queryTopicsBySyn(Map<String, Object> filter) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, t.name_, t.detail");
        // 取专题下的第一个资讯图标当专题图标
        sql.append(", (select sfd.path");
        sql.append(" from msg_info_catalog_relate sicr");
        sql.append(" inner join msg_information si on si.id = sicr.info_id");
        sql.append(" inner join sys_file_desc sfd on sfd.id = si.icon_file_id");
        sql.append(" where sicr.catalog_id = t.id");
        sql.append(" order by si.update_time desc");
        sql.append(" limit 0, 1) as icon_file_path");
        sql.append(" from msg_info_catalog t");
        // 专题下有资讯信息
        sql.append(" where 1=1");
        sql.append(" and exists (select sicr.id from msg_info_catalog_relate sicr where sicr.catalog_id = t.id)");
        // 专题类型
        sql.append(" and t.state = ? and t.type_ = ?");
        params.add(Constant.INFO_CATALOG_STATE_IN_USING, Hibernate.INTEGER);
        params.add(Constant.INFO_CATALOG_TYPE_TOPIC, Hibernate.INTEGER);
        // 是否指定的拉取时间点
        Timestamp sysTime = (Timestamp) filter.get("sysTime");
        if (sysTime != null)
        {
            sql.append(" and t.update_time < ?");
            params.add(sysTime, Hibernate.TIMESTAMP);
        }
        // 取最新发布倒序
        sql.append(" order by t.update_time desc");
        // 取数据条数
        sql.append(" limit 0, ?");
        params.add(filter.get("maxSize"), Hibernate.INTEGER);
        // 查询数据
        List<Object[]> listObjs = super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("name", Hibernate.STRING).addScalar("detail", Hibernate.STRING)
                .addScalar("icon_file_path", Hibernate.STRING).setParameters(params.getVals(), params.getTypes())
                .list();
        List<InfoCatalog> list = new ArrayList<InfoCatalog>();
        if (listObjs != null && listObjs.size() > 0)
        {
            int idx = 0;
            for (Object[] objs : listObjs)
            {
                InfoCatalog cata = new InfoCatalog();
                idx = 0;
                cata.setId((String) objs[idx]);
                idx++;
                cata.setName((String) objs[idx]);
                idx++;
                cata.setDetail((String) objs[idx]);
                idx++;
                cata.setIconFilePath((String) objs[idx]);
                idx++;
                list.add(cata);
            }
        }
        return list;
    }

    /**
     * 分页查询专题信息
     */
    @SuppressWarnings("unchecked")
    public PageModel<InfoCatalog> queryTopicByPage(Map<String, Object> filter, int page, int pageSize) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append("from msg_info_catalog t");
        // 专题下有资讯信息
        mSql.append(" where 1=1");
        Boolean haveInfoOnly = (Boolean) filter.get("haveInfoOnly");
        if (haveInfoOnly != null && haveInfoOnly.booleanValue())
        {
            mSql.append(" and exists (select sicr.id from msg_info_catalog_relate sicr where sicr.catalog_id = t.id)");
        }
        mSql.append(" and t.state = ? and t.type_ = ?");
        params.add(Constant.INFO_CATALOG_STATE_IN_USING, Hibernate.INTEGER);
        params.add(Constant.INFO_CATALOG_TYPE_TOPIC, Hibernate.INTEGER);
        String keyword = (String) filter.get("keyword");
        if (StringUtil.isNotEmpty(keyword))
        {
            mSql.append(" and lower(t.name_) like ").append(params.addLikePart(keyword.toLowerCase()));
        }
        // 总记录数
        StringBuilder totalCountsql = new StringBuilder();
        totalCountsql.append("select count(*) as c ").append(mSql);
        Integer totalCount = ((Integer) super.getSession().createSQLQuery(totalCountsql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult())
                .intValue();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.id, t.name_, t.detail ").append(mSql).append(" order by t.seq asc");
        List<Object[]> listObjs = super.getSession().createSQLQuery(pSql.toString())
                .setParameters(params.getVals(), params.getTypes()).setFirstResult(pageSize * (page - 1))
                .setMaxResults(pageSize).list();
        List<InfoCatalog> list = new ArrayList<InfoCatalog>();
        if (listObjs != null && listObjs.size() > 0)
        {
            int idx = 0;
            for (Object[] objs : listObjs)
            {
                InfoCatalog cata = new InfoCatalog();
                idx = 0;
                cata.setId((String) objs[idx]);
                idx++;
                cata.setName((String) objs[idx]);
                idx++;
                cata.setDetail((String) objs[idx]);
                idx++;
                list.add(cata);
            }
        }
        PageModel<InfoCatalog> pageModel = new PageModel<InfoCatalog>();
        pageModel.setTotal(totalCount);
        pageModel.setData(list);
        return pageModel;
    }

}
