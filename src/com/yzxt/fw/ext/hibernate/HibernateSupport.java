package com.yzxt.fw.ext.hibernate;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public abstract class HibernateSupport<T> extends HibernateDaoSupport
{

    private Class<T> persistentClass;

    public Class<T> getPersistentClass()
    {
        return persistentClass;
    }

    public void setPersistentClass(Class<T> persistentClass)
    {
        this.persistentClass = persistentClass;
    }

    @SuppressWarnings("unchecked")
    public HibernateSupport()
    {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    public int delete(T entity) throws Exception
    {
        this.getHibernateTemplate().delete(entity);
        return 1;
    }

    public int deleteAll(List<T> entities) throws Exception
    {
        this.getHibernateTemplate().deleteAll(entities);
        return entities.size();
    }

    public T get(Long id)
    {
        return (T) this.getHibernateTemplate().get(getPersistentClass(), id);
    }

    public T get(String id)
    {
        return (T) this.getHibernateTemplate().get(getPersistentClass(), id);
    }

    public T get(Serializable id)
    {
        return (T) this.getHibernateTemplate().get(getPersistentClass(), id);
    }

    public void save(T entity) throws Exception
    {
        this.getHibernateTemplate().save(entity);
    }

    public Object insert(T entity) throws Exception
    {
        this.save(entity);
        return null;
    }

    public int update(T entity) throws Exception
    {
        this.getHibernateTemplate().update(entity);
        return 1;
    }

}
