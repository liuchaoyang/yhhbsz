package com.yzxt.yh.module.sys.dao;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.yh.module.sys.bean.FileDesc;

public class FileDescDao extends HibernateSupport<FileDesc> implements BaseDao<FileDesc>
{
    /**
     * 添加文件信息
     * @param fileDesc
     * @return
     * @throws Exception
     */
    public String insert(FileDesc fileDesc) throws Exception
    {
        super.save(fileDesc);
        return fileDesc.getId();
    }

    /**
     * 更新文件信息
     * @param fileDesc
     * @return
     * @throws Exception
     */
    public int update(FileDesc fileDesc) throws Exception
    {
        HibernateParams params = new HibernateParams();
        String sql = "update FileDesc t set t.name = ?, t.path = ?, t.extName = ?, t.fileSize = ? where t.id = ?";
        params.add(fileDesc.getName(), Hibernate.STRING);
        params.add(fileDesc.getPath(), Hibernate.STRING);
        params.add(fileDesc.getExtName(), Hibernate.STRING);
        params.add(fileDesc.getFileSize(), Hibernate.LONG);
        params.add(fileDesc.getId(), Hibernate.STRING);
        return super.getSession().createQuery(sql).setParameters(params.getVals(), params.getTypes()).executeUpdate();
    }

    /**
     * 删除文件信息
     * @param fileDesc
     * @return
     * @throws Exception
     */
    public void delete(String id) throws Exception
    {
        HibernateParams params = new HibernateParams();
        String sql = "delete from FileDesc t where t.id = ?";
        params.add(id, Hibernate.STRING);
        super.getSession().createQuery(sql).setParameters(params.getVals(), params.getTypes()).executeUpdate();
    }

}
