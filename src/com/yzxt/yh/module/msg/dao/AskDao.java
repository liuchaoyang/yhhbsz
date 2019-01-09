package com.yzxt.yh.module.msg.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.msg.bean.Ask;
import com.yzxt.yh.module.sys.bean.CustFamily;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.StringUtil;

public class AskDao extends HibernateSupport<Ask> implements BaseDao<Ask>
{
    /**
     * 加载提问信息
     * @throws Exception 
     */
    public String insert(Ask ask) throws Exception
    {
        super.save(ask);
        return ask.getId();
    }

    /**
     * 更新提问信息
     */
    public int update(Ask ask)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update Ask t set t.title = ?, t.richtextId = ?, t.catalogId = ?, t.sex = ?, t.birthday = ?, t.type = ?");
        sql.append(", t.updateBy = ?, t.updateTime = ?");
        sql.append(" where t.id = ?");
        params.add(ask.getTitle(), Hibernate.STRING);
        params.add(ask.getRichtextId(), Hibernate.STRING);
        params.add(ask.getCatalogId(), Hibernate.STRING);
        params.add(ask.getSex(), Hibernate.INTEGER);
        params.add(ask.getBirthday(), Hibernate.DATE);
        params.add(ask.getType(), Hibernate.INTEGER);
        params.add(ask.getUpdateBy(), Hibernate.STRING);
        params.add(ask.getUpdateTime(), Hibernate.TIMESTAMP);
        params.add(ask.getId(), Hibernate.STRING);
        return super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

    /**
     * 加载提问信息
     */
    @SuppressWarnings("unchecked")
    public Ask load(String id)
    {
        HibernateParams params = new HibernateParams();
        // 提问信息
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, t.title, t.richtext_id, t.catalog_id, t.reply_count, t.view_count");
        sql.append(", t.sex, t.birthday, t.create_by, t.update_time,t.update_by");
        sql.append(", trt.summary, trt.content");
        sql.append(", tc.parent_id, tu.name_ as create_by_name");
        sql.append(" from msg_ask t");
        sql.append(" inner join msg_richtext trt on trt.id = t.richtext_id");
        sql.append(" inner join sys_user tu on tu.id = t.update_by");
        sql.append(" inner join msg_ask_catalog tc on tc.id = t.catalog_id");
        sql.append(" where t.id = ?");
        params.add(id, Hibernate.STRING);
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("title", Hibernate.STRING).addScalar("richtext_id", Hibernate.STRING)
                .addScalar("catalog_id", Hibernate.STRING).addScalar("reply_count", Hibernate.INTEGER)
                .addScalar("view_count", Hibernate.INTEGER).addScalar("sex", Hibernate.INTEGER)
                .addScalar("birthday", Hibernate.DATE).addScalar("create_by", Hibernate.STRING)
                .addScalar("update_time", Hibernate.TIMESTAMP).addScalar("update_by", Hibernate.STRING)
                .addScalar("summary", Hibernate.STRING)
                .addScalar("content", Hibernate.STRING).addScalar("parent_id", Hibernate.STRING)
                .addScalar("create_by_name", Hibernate.STRING).setParameters(params.getVals(), params.getTypes())
                .list();
        Ask ask = null;
        if (objsList != null && objsList.size() > 0)
        {
            Object[] objs = objsList.get(0);
            ask = new Ask();
            int idx = 0;
            ask.setId((String) objs[idx]);
            idx++;
            ask.setTitle((String) objs[idx]);
            idx++;
            ask.setRichtextId((String) objs[idx]);
            idx++;
            ask.setCatalogId((String) objs[idx]);
            idx++;
            ask.setReplyCount(objs[idx] != null ? (Integer) objs[idx] : 0);
            idx++;
            ask.setViewCount(objs[idx] != null ? (Integer) objs[idx] : 0);
            idx++;
            ask.setSex(objs[idx] != null ? (Integer) objs[idx] : 0);
            idx++;
            ask.setBirthday((Date) objs[idx]);
            idx++;
            ask.setCreateBy((String) objs[idx]);
            idx++;
            ask.setUpdateTime((Timestamp) objs[idx]);
            idx++;
            ask.setUpdateBy((String) objs[idx]);
            idx++;
            ask.setSummary((String) objs[idx]);
            idx++;
            ask.setContent((String) objs[idx]);
            idx++;
            ask.setParentCatalogId((String) objs[idx]);
            idx++;
            ask.setCreateByName((String) objs[idx]);
            idx++;
        }
        return ask;
    }

    /**
     * 删除提问信息
     */
    public void delete(String id)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("delete from Ask t where t.id = ?");
        params.add(id, Hibernate.STRING);
        super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

    /**
     * 根据问题回复变化次数修改回复数
     * @param id
     * @param change
     */
    public int updateReplyCountByChange(String id, int change)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update msg_ask t");

        if (change >= 0)
        {
            sql.append(" set t.reply_count = t.reply_count + ?");
            params.add(change, Hibernate.INTEGER);
        }
        else
        {
            sql.append(" set t.reply_count = (case when t.reply_count >= ? then t.reply_count + ? else 0 end)");
            params.add(-change, Hibernate.INTEGER);
            params.add(change, Hibernate.INTEGER);
        }
        sql.append(" where t.id = ?");
        params.add(id, Hibernate.STRING);
        int c = super.getSession().createSQLQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
        return c;
    }

    /**
     * 根据问题回复变化次数修改回复数
     * @param id
     * @param change
     */
    public int updateViewCountByChange(String id, int change)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update Ask t");
        sql.append(" set t.viewCount = t.viewCount + ?");
        sql.append(" where t.id = ?");
        params.add(change, Hibernate.INTEGER);
        params.add(id, Hibernate.STRING);
        int c = super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
        return c;
    }

    /**
     * 分页查询问答信息
     * @param filter
     * @param page
     * @param pageSize
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public PageModel<Ask> queryAskByPage(Map<String, Object> filter, int page, int pageSize) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append("from msg_ask t");
        mSql.append(" inner join msg_richtext trt on trt.id = t.richtext_id");
        mSql.append(" left join sys_user tu on tu.id = t.update_by");
        mSql.append(" where 1=1");
        // 所属目录
        String topLevelCatalogId = (String) filter.get("topLevelCatalogId");
        String secondLevelCatalogId = (String) filter.get("secondLevelCatalogId");
        if (StringUtil.isNotEmpty(secondLevelCatalogId))
        {
            mSql.append(" and t.catalog_id = ?");
            params.add(secondLevelCatalogId, Hibernate.STRING);
        }
        else if (StringUtil.isNotEmpty(topLevelCatalogId))
        {
            if (!Constant.ASK_CATALOG_MY_PUBLISH.equals(topLevelCatalogId))
            {
                mSql.append(" and exists(select sc.id from msg_ask_catalog sc where sc.parent_id = ? and sc.id = t.catalog_id)");
                params.add(topLevelCatalogId, Hibernate.STRING);
            }
            else
            {
                mSql.append(" and t.update_by = ?");
                User user = (User) filter.get("user");
                params.add(user.getId(), Hibernate.STRING);
            }
        }
        // 总启启记录数
        StringBuilder totalCountsql = new StringBuilder();
        totalCountsql.append("select count(*) as c ").append(mSql);
        Integer totalCount = (Integer) super.getSession().createSQLQuery(totalCountsql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append(
                "select t.id, t.title, t.reply_count, t.view_count, t.update_by, t.update_time, trt.summary, tu.name_ ")
                .append(mSql).append(" order by t.create_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString())
                .setParameters(params.getVals(), params.getTypes()).list();
        List<Ask> list = new ArrayList<Ask>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Ask ask = new Ask();
            idx = 0;
            ask.setId((String) objs[idx]);
            idx++;
            ask.setTitle((String) objs[idx]);
            idx++;
            ask.setReplyCount(objs[idx] != null ? (Integer) objs[idx] : 0);
            idx++;
            ask.setViewCount(objs[idx] != null ? (Integer) objs[idx] : 0);
            idx++;
            ask.setUpdateBy((String) objs[idx]);
            idx++;
            ask.setUpdateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            ask.setSummary((String) objs[idx]);
            idx++;
            ask.setUpdateByName((String) objs[idx]);
            idx++;
            list.add(ask);
        }
        PageModel<Ask> pageModel = new PageModel<Ask>();
        pageModel.setTotal(totalCount);
        pageModel.setPageNo(page);
        pageModel.setData(list);
        return pageModel;
    }

    /**
     * 分页查询问答信息
     * @param filter
     * @param page
     * @param pageSize
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public PageModel<Ask> queryDocHomeAsk(Map<String, Object> filter, int page, int pageSize) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append("from msg_ask t");
        mSql.append(" inner join msg_richtext trt on trt.id = t.richtext_id");
        mSql.append(" inner join sys_doctor sd on sd.user_id = t.doctorId ");
        mSql.append(" left join sys_user tu on tu.id = t.update_by");
        mSql.append(" where 1=1");
        //mSql.append(" and t.reply_count = 0");
        // 操作人
        User operUser = (User) filter.get("operUser");
        // 医生会员
        mSql.append(" and exists(select smi.id from svb_member_info smi where smi.cust_id = t.update_by and smi.doctor_id = ? and smi.state = 1)");
        mSql.append(" and sd.user_id = ?");
        params.add(operUser.getId(), Hibernate.STRING);
        params.add(operUser.getId(), Hibernate.STRING);
        // 总启启记录数
        StringBuilder totalCountsql = new StringBuilder();
        totalCountsql.append("select count(*) as c ").append(mSql);
        Integer totalCount = (Integer) super.getSession().createSQLQuery(totalCountsql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append(
                "select t.id, t.title, t.reply_count, t.view_count, t.create_by, t.create_time, trt.summary, tu.name_ ")
                .append(mSql).append(" order by t.create_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString())
                .setParameters(params.getVals(), params.getTypes()).list();
        List<Ask> list = new ArrayList<Ask>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Ask ask = new Ask();
            idx = 0;
            ask.setId((String) objs[idx]);
            idx++;
            ask.setTitle((String) objs[idx]);
            idx++;
            ask.setReplyCount(objs[idx] != null ? (Integer) objs[idx] : 0);
            idx++;
            ask.setViewCount(objs[idx] != null ? (Integer) objs[idx] : 0);
            idx++;
            ask.setCreateBy((String) objs[idx]);
            idx++;
            ask.setCreateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            ask.setSummary((String) objs[idx]);
            idx++;
            ask.setCreateByName((String) objs[idx]);
            idx++;
            list.add(ask);
        }
        PageModel<Ask> pageModel = new PageModel<Ask>();
        pageModel.setTotal(totalCount);
        pageModel.setPageNo(page);
        pageModel.setData(list);
        return pageModel;
    }

    /**
     * 查询问题列表，用户客户端查询
     * @param filter
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Ask> queryAsksBySyn(Map<String, Object> filter)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, t.catalog_id, t.title, t.sex, t.birthday");
        sql.append(", trt.content, t.update_by, t.update_time");
        sql.append(", tac.parent_id as parent_catalog_id");
        sql.append(" from msg_ask t");
        sql.append(" inner join msg_richtext trt on trt.id = t.richtext_id");
        sql.append(" inner join msg_ask_catalog tac on tac.id = t.catalog_id");
        sql.append(" left join sys_user tu on tu.id = t.update_by");
        sql.append(" where 1=1");
        String topLevelAskCatalogId = (String) filter.get("topLevelAskCatalogId");
        String secondLevelAskCatalogId = (String) filter.get("secondLevelAskCatalogId");
        // 拉取时间
        Timestamp synTime = (Timestamp) filter.get("sysTime");
        Integer maxSize = (Integer) filter.get("maxSize");
        if (StringUtil.isNotEmpty(secondLevelAskCatalogId))
        {
            sql.append(" and t.catalog_id = ?");
            params.add(secondLevelAskCatalogId, Hibernate.STRING);
        }
        else if (StringUtil.isNotEmpty(topLevelAskCatalogId))
        {
            if (!Constant.ASK_CATALOG_MY_PUBLISH.equals(topLevelAskCatalogId))
            {
                sql.append(" and exists(select sc.id from msg_ask_catalog sc where sc.parent_id = ? and sc.id = t.catalog_id)");
                params.add(topLevelAskCatalogId, Hibernate.STRING);
            }
            else
            {
                sql.append(" and t.update_by = ?");
                User user = (User) filter.get("user");
                params.add(user.getId(), Hibernate.STRING);
            }
        }
        if (synTime != null)
        {
            sql.append(" and t.update_time < ?");
            params.add(synTime, Hibernate.TIMESTAMP);
        }
        // 取最新发布倒序
        sql.append(" order by t.update_time desc");
        // 取数据条数
        sql.append(" limit 0, ?");
        params.add(maxSize, Hibernate.INTEGER);
        // 查询数据
        List<Object[]> listObjs = super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("catalog_id", Hibernate.STRING).addScalar("title", Hibernate.STRING)
                .addScalar("sex", Hibernate.INTEGER).addScalar("birthday", Hibernate.DATE)
                .addScalar("content", Hibernate.STRING).addScalar("update_by", Hibernate.STRING)
                .addScalar("update_time", Hibernate.TIMESTAMP).addScalar("parent_catalog_id", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<Ask> list = new ArrayList<Ask>();
        if (listObjs != null && listObjs.size() > 0)
        {
            int idx = 0;
            for (Object[] objs : listObjs)
            {
                Ask ask = new Ask();
                idx = 0;
                ask.setId((String) objs[idx]);
                idx++;
                ask.setCatalogId((String) objs[idx]);
                idx++;
                ask.setTitle((String) objs[idx]);
                idx++;
                ask.setSex((Integer) objs[idx]);
                idx++;
                ask.setBirthday((Date) objs[idx]);
                idx++;
                ask.setContent((String) objs[idx]);
                idx++;
                ask.setUpdateBy((String) objs[idx]);
                idx++;
                ask.setUpdateTime((Timestamp) objs[idx]);
                idx++;
                ask.setParentCatalogId((String) objs[idx]);
                idx++;
                list.add(ask);
            }
        }
        return list;
    }
    public List<Ask> getCustId(String custId,String doctorId) {

        HibernateParams params = new HibernateParams();
        String hql = "select t from Ask t where t. updateBy= ? and t.doctorId= ? order by t.updateTime asc ";
        params.add(custId, Hibernate.STRING);
        params.add(doctorId, Hibernate.STRING);
        List<Ask> ask = super.getSession().createQuery(hql).setParameters(params.getVals(), params.getTypes())
                .list();
        return ask;

	}
}
