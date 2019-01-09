package com.yzxt.yh.module.msg.dao;

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
import com.yzxt.yh.module.msg.bean.InfoCatalogAuth;

public class InfoCatalogDao extends HibernateSupport<InfoCatalog> implements BaseDao<InfoCatalog>
{

    @SuppressWarnings("unchecked")
    public PageModel<InfoCatalog> findInfoTopicList(Map<String, Object> filter, int page, int pageSize)
            throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append("from InfoCatalog t where 1=1 and t.type=2");
        String keyword = (String) filter.get("keyword");
        keyword = keyword != null ? keyword.trim() : null;
        if (keyword != null && keyword.length() > 0)
        {
            mSql.append(" and (t.name like ").append(params.addLikePart(keyword)).append(" or t.detail like ")
                    .append(params.addLikePart(keyword)).append(")");
        }
        StringBuilder totalCountsql = new StringBuilder();
        totalCountsql.append("select count(*) as c ").append(mSql);
        Long totalCount = (Long) super.getSession().createQuery(totalCountsql.toString())
                .setParameters(params.getVals(), params.getTypes()).uniqueResult();
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t ").append(mSql).append(" order by t.seq asc");
        List<InfoCatalog> list = super.getSession().createQuery(pSql.toString())
                .setParameters(params.getVals(), params.getTypes()).setFirstResult(pageSize * (page - 1))
                .setMaxResults(pageSize).list();
        PageModel<InfoCatalog> pageBean = new PageModel<InfoCatalog>();
        pageBean.setTotal(totalCount.intValue());
        pageBean.setData(list);
        return pageBean;
    }

    /**
     * 获取栏目个数
     * @return
     */
    private int getInfoCatalogCount(int type)
    {
        String sql = "select count(*) as c from InfoCatalog t where t.type = ?";
        HibernateParams params = new HibernateParams();
        params.add(type, Hibernate.STRING);
        /*    params.add(infoCatalog.getId(), Hibernate.STRING);*/
        Long c = (Long) super.getSession().createQuery(sql).setInteger(0, type).uniqueResult();
        return c.intValue();
    }

    /**
    * 保存专题
    * @param dict
    * @throws Exception
    */
    public void addInfoTopic(InfoCatalog infoCatalog) throws Exception
    {
        int existCount = getInfoCatalogCount(infoCatalog.getType());
        Integer seqNum = infoCatalog.getSeq();
        if (seqNum == null || seqNum < 1 || seqNum > existCount + 1)
        {
            seqNum = existCount + 1;
            infoCatalog.setSeq(seqNum);
        }
        else
        {
            // 将之后的记录编号+1
            String updateSql = "update msg_info_catalog t set t.seqNum = t.seqNum + 1 where t.seq >= ? and t.id = ?";
            super.getSession().createQuery(updateSql).setInteger(0, seqNum).setString(1, infoCatalog.getId())
                    .executeUpdate();
        }
        infoCatalog.setState(infoCatalog != null ? infoCatalog.getState() : 1);
        super.getHibernateTemplate().save(infoCatalog);
    }

    public boolean isInfoTopicNameExist(String name)
    {

        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) as c from InfoCatalog t where 1=1");
        sql.append(" and lower(t.name) = ?");
        params.add(name.toLowerCase(), Hibernate.STRING);
        /* if (exceptInfoTopicCode != null && exceptInfoTopicCode.length() > 0)
         {
             sql.append(" and lower(t.detail) <> ?");
             params.add(exceptInfoTopicCode.toLowerCase(), Hibernate.STRING);
         }*/
        Long c = (Long) super.getSession().createQuery(sql.toString())
                .setParameters(params.getVals(), params.getTypes()).uniqueResult();
        return c.longValue() > 0;
    }

    /**
     * 查询栏目明细类型查询栏目明细
     * @param dictCode
     * @return
     */
    @SuppressWarnings("unchecked")
    public InfoCatalog getInfoCatalogById(String id)
    {
        String hql = "select t from InfoCatalog t where t.id = ? ";
        List<InfoCatalog> infoCatalogs = super.getSession().createQuery(hql).setString(0, id).list();
        return infoCatalogs != null && infoCatalogs.size() > 0 ? infoCatalogs.get(0) : null;
    }

    public void updateInfoTopic(InfoCatalog infoCatalog) throws Exception
    {
        /*String sql = "update msg_info_catalog t set t.name = ?, t.detail = ?, t.state= ?, t.seq = ?, t.update_by = ?, t.update_time = ? where t.id = ?";
        HibernateParams params = new HibernateParams();
        params.add(infoCatalog.getName(), Hibernate.STRING);
        params.add(infoCatalog.getDetail(), Hibernate.STRING);
        params.add(infoCatalog.getState(), Hibernate.INTEGER);
        params.add(infoCatalog.getSeq(), Hibernate.INTEGER);
        params.add(infoCatalog.getUpdateBy(), Hibernate.STRING);
        params.add(infoCatalog.getUpdateTime(), Hibernate.TIMESTAMP);
        params.add(infoCatalog.getId(), Hibernate.STRING);
        return super.getSession().createSQLQuery(sql).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();*/
        InfoCatalog oldInfoCatalog = getInfoCatalogById(infoCatalog.getId());
        if (oldInfoCatalog == null || oldInfoCatalog.getPredefined() == 1)
        {
            return;
        }
        Integer oldSeqNum = oldInfoCatalog.getSeq();
        Integer newSeqNum = infoCatalog.getSeq();
        if (newSeqNum != null && newSeqNum > 0)
        {
            int existCount = getInfoCatalogCount(infoCatalog.getType());
            if (newSeqNum > existCount)
            {
                newSeqNum = existCount;
            }
            if (newSeqNum < oldSeqNum)
            {
                String updateSql = "update InfoCatalog t set t.seq = t.seq + 1 where t.seq >= ? and t.seq < ?";
                super.getSession().createQuery(updateSql).setInteger(0, newSeqNum).setInteger(1, oldSeqNum)
                        .executeUpdate();
            }
            else if (newSeqNum > oldSeqNum)
            {
                String updateSql = "update InfoCatalog t set t.seq = t.seq - 1 where t.seq > ? and t.seq <= ?";
                super.getSession().createQuery(updateSql).setInteger(0, oldSeqNum).setInteger(1, newSeqNum)
                        .executeUpdate();
            }
            oldInfoCatalog.setSeq(newSeqNum);
        }
        oldInfoCatalog.setName(infoCatalog.getName());
        if (infoCatalog.getState() != null)
        {
            oldInfoCatalog.setState(infoCatalog.getState());
        }
        super.getHibernateTemplate().update(oldInfoCatalog);
    }

    //生效 作废
    public int updateStateInfoTopic(InfoCatalog infoCatalog) throws Exception
    {
        String sql = "update msg_info_catalog t set  t.state= ?,  t.update_by = ?, t.update_time = ? where t.id = ?";
        HibernateParams params = new HibernateParams();
        params.add(infoCatalog.getState(), Hibernate.INTEGER);
        params.add(infoCatalog.getUpdateBy(), Hibernate.STRING);
        params.add(infoCatalog.getUpdateTime(), Hibernate.TIMESTAMP);
        params.add(infoCatalog.getId(), Hibernate.STRING);
        return super.getSession().createSQLQuery(sql).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

    /**
     * 通过栏目id获取到栏目全部信息
     * @param dict
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<InfoCatalog> getInfoCatalogById(InfoCatalog infoCatalog)
    {
        HibernateParams params = new HibernateParams();
        String sql = " select * from InfoCatalog t where t.id = ? order by t.seq asc";
        params.add(infoCatalog.getId(), Hibernate.STRING);
        List<InfoCatalog> list = super.getSession().createSQLQuery(sql)
                .setParameters(params.getVals(), params.getTypes()).list();
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<InfoCatalog> getInfoCatalogList()
    {
        String hql = "select t from InfoCatalog t where t.type = 1 order by t.seq asc";
        List<InfoCatalog> InfoCatalogs = super.getSession().createQuery(hql).list();
        return InfoCatalogs;
    }

    /*
     * 增加栏目
     * */
    public void addInfoCatalog(InfoCatalog infoCatalog) throws Exception
    {
        int existCount = getInfoCatalogCount(infoCatalog.getType());
        Integer seqNum = infoCatalog.getSeq();
        if (seqNum == null || seqNum < 1 || seqNum > existCount + 1)
        {
            seqNum = existCount + 1;
            infoCatalog.setSeq(seqNum);
        }
        else
        {
            // 将之后的记录编号+1
            String updateSql = "update msg_info_catalog t set t.seqNum = t.seqNum + 1 where t.seq >= ? and t.id = ?";
            super.getSession().createQuery(updateSql).setInteger(0, seqNum).setString(1, infoCatalog.getId())
                    .executeUpdate();
        }
        infoCatalog.setState(infoCatalog != null ? infoCatalog.getState() : 1);
        super.getHibernateTemplate().save(infoCatalog);
        String[] infoCatalogAuths = infoCatalog.getUserTypes();
        if (infoCatalogAuths == null)
        {
            return;
        }
        else
        {
            int num[] = new int[infoCatalogAuths.length];
            for (int i = 0; i < num.length; i++)
            {
                /*  String sql = "insert into msg_info_catalog_auth values(?,?,?)";
                String sql = "update msg_info_catalog_auth a set  a.id=?, a.auth_user_type= ? where a.column_id=?";
                HibernateParams params = new HibernateParams();
                params.add(infoCatalogAuth.getId(), Hibernate.STRING);
                
                params.add(infoCatalogAuth.getAuthUserType(), Hibernate.INTEGER);
                 params.add(infoCatalogAuth.getColumnId(), Hibernate.STRING);
                 super.getSession().createSQLQuery(sql).setParameters(params.getVals(), params.getTypes())
                        .executeUpdate();*/
                //实例化对象，一个会话一个实例化，插入一条数据到数据库中
                InfoCatalogAuth infoCatalogAuth = new InfoCatalogAuth();
                num[i] = Integer.parseInt(infoCatalogAuths[i]);
                infoCatalogAuth.setAuthUserType(num[i]);
                infoCatalogAuth.setColumnId(infoCatalog.getId());
                super.getHibernateTemplate().save(infoCatalogAuth);
            }
        }
    }

    //启用，停用 栏目
    public int updateInfoCatalogState(InfoCatalog infoCatalog) throws Exception
    {
        String sql = "update msg_info_catalog t set  t.state= ?,  t.update_by = ?, t.update_time = ? where t.id = ?";
        HibernateParams params = new HibernateParams();
        params.add(infoCatalog.getState(), Hibernate.INTEGER);
        params.add(infoCatalog.getUpdateBy(), Hibernate.STRING);
        params.add(infoCatalog.getUpdateTime(), Hibernate.TIMESTAMP);
        params.add(infoCatalog.getId(), Hibernate.STRING);
        return super.getSession().createSQLQuery(sql).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

    /*
     * 通过栏目得到用户权限
     * */
    @SuppressWarnings("unchecked")
    public List<Object[]> getAuthIdByColumnId(InfoCatalog infoCatalog)
    {
        HibernateParams params = new HibernateParams();
        String sql = "select * from msg_info_catalog_auth m where m.column_id=? ";
        params.add(infoCatalog.getId(), Hibernate.STRING);
        List<Object[]> oldInfoCatalogAuthsList = super.getSession().createSQLQuery(sql)
                .setParameters(params.getVals(), params.getTypes()).list();
        /* 
         List<InfoCatalogAuth> list = new ArrayList<InfoCatalogAuth>();
         int idx = 0;
         for (Object[] objs : oldInfoCatalogAuthsList)
         {
             InfoCatalogAuth auth = new InfoCatalogAuth();
             idx = 0;
             auth.setId((String) objs[idx]);
             idx++;
             auth.setColumnId((String) objs[idx]);
             idx++;
             auth.setAuthUserType((Integer) objs[idx]);
             idx++;
             list.add(auth);
         }*/
        return oldInfoCatalogAuthsList;
    }

    /*
     * 修改栏目
     * */
    public void updateInfoCatalog(InfoCatalog infoCatalog) throws Exception
    {
        /*String sql = "update msg_info_catalog t set t.name = ?, t.detail = ?, t.state= ?, t.seq = ?, t.update_by = ?, t.update_time = ? where t.id = ?";
        HibernateParams params = new HibernateParams();
        params.add(infoCatalog.getName(), Hibernate.STRING);
        params.add(infoCatalog.getDetail(), Hibernate.STRING);
        params.add(infoCatalog.getState(), Hibernate.INTEGER);
        params.add(infoCatalog.getSeq(), Hibernate.INTEGER);
        params.add(infoCatalog.getUpdateBy(), Hibernate.STRING);
        params.add(infoCatalog.getUpdateTime(), Hibernate.TIMESTAMP);
        params.add(infoCatalog.getId(), Hibernate.STRING);
        return super.getSession().createSQLQuery(sql).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();*/
        InfoCatalog oldInfoCatalog = getInfoCatalogById(infoCatalog.getId());
        if (oldInfoCatalog == null || oldInfoCatalog.getPredefined() == 1)
        {
            return;
        }
        Integer oldSeqNum = oldInfoCatalog.getSeq();
        Integer newSeqNum = infoCatalog.getSeq();
        if (newSeqNum != null && newSeqNum > 0)
        {
            int existCount = getInfoCatalogCount(infoCatalog.getType());
            if (newSeqNum > existCount)
            {
                newSeqNum = existCount;
            }
            if (newSeqNum < oldSeqNum)
            {
                String updateSql = "update InfoCatalog t set t.seq = t.seq + 1 where t.seq >= ? and t.seq < ?";
                super.getSession().createQuery(updateSql).setInteger(0, newSeqNum).setInteger(1, oldSeqNum)
                        .executeUpdate();
            }
            else if (newSeqNum > oldSeqNum)
            {
                String updateSql = "update InfoCatalog t set t.seq = t.seq - 1 where t.seq > ? and t.seq <= ?";
                super.getSession().createQuery(updateSql).setInteger(0, oldSeqNum).setInteger(1, newSeqNum)
                        .executeUpdate();
            }
            oldInfoCatalog.setSeq(newSeqNum);
        }
        oldInfoCatalog.setName(infoCatalog.getName());
        if (infoCatalog.getState() != null)
        {
            oldInfoCatalog.setState(infoCatalog.getState());
        }

        //栏目授权表 首先判断栏目是否授权
        String[] infoCatalogAuths = infoCatalog.getUserTypes();
        List<Object[]> oldAuth = getAuthIdByColumnId(infoCatalog);
        if (infoCatalogAuths == null)
        {
            List<InfoCatalogAuth> list = new ArrayList<InfoCatalogAuth>();
            int idx = 0;
            for (Object[] objs : oldAuth)
            {
                InfoCatalogAuth auth = new InfoCatalogAuth();
                idx = 0;
                auth.setId((String) objs[idx]);
                idx++;
                auth.setColumnId((String) objs[idx]);
                idx++;
                auth.setAuthUserType((Integer) objs[idx]);
                idx++;
                list.add(auth);
                super.getHibernateTemplate().delete(auth);
            }
        }
        else
        {
            int num[] = new int[infoCatalogAuths.length];

            if (oldAuth.size() == 0 && infoCatalogAuths.length == 0)
            {
                return;
            }
            if (oldAuth.size() == 0 && infoCatalogAuths.length > 0)
            {
                for (int i = 0; i < num.length; i++)
                {
                    //实例化对象，一个会话一个实例化，插入一条数据到数据库中
                    InfoCatalogAuth infoCatalogAuth = new InfoCatalogAuth();
                    num[i] = Integer.parseInt(infoCatalogAuths[i]);
                    infoCatalogAuth.setAuthUserType(num[i]);
                    infoCatalogAuth.setColumnId(infoCatalog.getId());
                    super.getHibernateTemplate().save(infoCatalogAuth);
                }
            }
            if (oldAuth.size() > 0 && infoCatalogAuths.length == 0)
            {
                List<InfoCatalogAuth> list = new ArrayList<InfoCatalogAuth>();
                int idx = 0;
                for (Object[] objs : oldAuth)
                {
                    InfoCatalogAuth auth = new InfoCatalogAuth();
                    idx = 0;
                    auth.setId((String) objs[idx]);
                    idx++;
                    auth.setColumnId((String) objs[idx]);
                    idx++;
                    auth.setAuthUserType((Integer) objs[idx]);
                    idx++;
                    list.add(auth);
                    super.getHibernateTemplate().delete(auth);
                }
            }
            /*if(oldAuth.size()==2&&infoCatalogAuths.length==0){
                List<InfoCatalogAuth> list = new ArrayList<InfoCatalogAuth>();
                int idx = 0;
                for (Object[] objs : oldAuth)
                {
                    InfoCatalogAuth auth = new InfoCatalogAuth();
                    idx = 0;
                    auth.setId((String) objs[idx]);
                    idx++;
                    auth.setColumnId((String) objs[idx]);
                    idx++;
                    auth.setAuthUserType((Integer) objs[idx]);
                    idx++;
                    list.add(auth);
                        super.getHibernateTemplate().delete(auth);
                }
            }*/
            if (oldAuth.size() > 0 && infoCatalogAuths.length > 0)
            {
                List<InfoCatalogAuth> list = new ArrayList<InfoCatalogAuth>();
                int idx = 0;
                for (Object[] objs : oldAuth)
                {
                    InfoCatalogAuth auth = new InfoCatalogAuth();
                    idx = 0;
                    auth.setId((String) objs[idx]);
                    idx++;
                    auth.setColumnId((String) objs[idx]);
                    idx++;
                    auth.setAuthUserType((Integer) objs[idx]);
                    idx++;
                    list.add(auth);
                    super.getHibernateTemplate().delete(auth);
                }
                for (int i = 0; i < num.length; i++)
                {
                    //实例化对象，一个会话一个实例化，插入一条数据到数据库中
                    InfoCatalogAuth infoCatalogAuth = new InfoCatalogAuth();
                    num[i] = Integer.parseInt(infoCatalogAuths[i]);
                    infoCatalogAuth.setAuthUserType(num[i]);
                    infoCatalogAuth.setColumnId(infoCatalog.getId());
                    super.getHibernateTemplate().save(infoCatalogAuth);
                }

            }
        }
        super.getHibernateTemplate().update(oldInfoCatalog);
    }

    /**
     * 查询所有的栏目信息
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<InfoCatalog> getInfoColumns()
    {
        HibernateParams params = new HibernateParams();
        String sql = "from InfoCatalog t where t.state = ? and t.type = ? order by t.seq asc";
        params.add(Constant.INFO_CATALOG_STATE_IN_USING, Hibernate.INTEGER);
        params.add(Constant.INFO_CATALOG_TYPE_COLUMN, Hibernate.INTEGER);
        List<InfoCatalog> list = super.getSession().createQuery(sql).setParameters(params.getVals(), params.getTypes())
                .list();
        return list;
    }

    /**
     * 根据用户类型查询栏目信息
     * @param userType
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<InfoCatalog> getInfoColumnsByUserType(Integer userType, boolean withoutPredefined)
    {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, t.name_, t.detail from msg_info_catalog t");
        sql.append(" where 1=1");
        if (withoutPredefined)
        {
            // 预定义栏目不用返回
            sql.append(" and t.predefined = ").append(Constant.INFO_CATALOG_IS_NOT_PREDEFINED);
        }
        sql.append(" and t.state = ").append(Constant.INFO_CATALOG_STATE_IN_USING);
        sql.append(" and t.type_ = ").append(Constant.INFO_CATALOG_TYPE_COLUMN);
        // 排序
        sql.append(" order by t.seq asc");
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString()).list();
        List<InfoCatalog> list = new ArrayList<InfoCatalog>();
        int idx = 0;
        for (Object[] objs : objsList)
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
        return list;
    }
}
