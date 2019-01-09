package com.yzxt.fw.common;

import java.io.Serializable;

public interface BaseDao<T>
{
    /**
     * 
     * @param id 实体主键
     * @return 实体对象
     */
    public T get(Serializable id);

    /**
     * 
     * @param entity
     * @return 主键
     * @throws Exception
     */
    public Object insert(T entity) throws Exception;

    /**
     * 
     * @param entityA
     * @return 更新记录条数
     * @throws Exception
     */
    public int update(T entity) throws Exception;

    /**
     * 
     * @param entity
     * @return 删除记录条数
     * @throws Exception
     */
    public int delete(T entity) throws Exception;

}
