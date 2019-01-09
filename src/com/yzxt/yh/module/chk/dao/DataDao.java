package com.yzxt.yh.module.chk.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.springframework.dao.DataAccessException;

import com.yzxt.fw.pager.PageModel;

public interface DataDao
{

    public List<?> findAll(String tname, Order order);

    /**
     * 保存一个对象
     */
    public void save(Object obj) throws HibernateException;

    /**
     * 保存或者更新一个对象
     */
    public void saveOrUpdate(Object obj) throws HibernateException;

    /**
     * 更新一个对象
     */
    public void update(Object obj) throws HibernateException;

    /**
     * 删除一个对象
     */
    public void delete(Object obj) throws HibernateException;

    /**
     * 
     * This method is used to delete multiple obj
     * @param list
     * @throws Exception
     */
    public void deleteAll(List<?> list) throws Exception;

    /**
     * 根据ID和类Class获得一个对象
     * @param 要获得的类Class
     *@param id
     */
    public Object get(Class<?> classz, Serializable pk) throws HibernateException;

    /**
     * 根据传入的查询条件，获得一个List结果集
     * @param 传入的查询条件
     */
    public List<?> find(String queryString) throws HibernateException;

    /**
     * 根据传入的查询条件，获得第一条数据结果
     * @param 传入的查询条件
     */
    public Object getFirstRow(String queryString) throws HibernateException;

    /**
     * 根据传入的查询条件，获得一个List非泛型结果集
     * @param 传入的查询条件
     */
    @SuppressWarnings("rawtypes")
    public List findBy(String queryString) throws HibernateException;

    /**
     * 一个查询方法
     * eg:from User where username=?
     * 
     * @param queryString
     * @param value
     * @return
     * @throws DataAccessException
     */
    public List<?> find(String queryString, Object param) throws HibernateException;

    /**
     * 一个查询方法
     * eg:from User where username=? and password=?
     * 
     * @param queryString
     * @param values
     * @return
     * @
     */
    public List<?> find(String queryString, Object[] params) throws HibernateException;

    /**
     * 根据传入的属性名和值，返回一个List
     *@param 表名
     *@param 属性名
     *@param 属性值
     */
    public List<?> findByProperty(String table, String propertyName, Object value) throws HibernateException;

    /**
     * 根据传入的属性名和值，返回一个List
     *@param 表名
     *@param 属性名
     *@param 属性值
     */
    public List<?> findByProperty(Class<?> classz, String propertyName, Object value) throws HibernateException;

    /**
     * 根据传入的HQL语句，得到一个Query对象
     */
    //public Query createQuery(String hql) throws HibernateException;

    /**
     * 根据传入的一个SQL语句，得到一个SQLQuery对象
     */
    //public SQLQuery createSQLQuery(String sql) throws HibernateException;

    /**
     * 一个分页方法
     * @param queryString
     * @param page
     * @param pagesize
     * @return
     * @throws HibernateException
     */
    public PageModel<?> getPageQuery(final String queryString, final int page, final int pageSize)
            throws HibernateException;

    /**
     * 将实体修改为游离态
     * @param entity
     */
    public void evict(Object entity);

}
