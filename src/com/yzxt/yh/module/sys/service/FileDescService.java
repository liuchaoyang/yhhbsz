package com.yzxt.yh.module.sys.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.sys.bean.FileDesc;
import com.yzxt.yh.module.sys.dao.FileDescDao;

/**
 * 文件上传服务类
 * @author f
 *
 */
@Transactional(ConstTM.DEFAULT)
public class FileDescService
{
    private FileDescDao fileDescDao;

    public FileDescDao getFileDescDao()
    {
        return fileDescDao;
    }

    public void setFileDescDao(FileDescDao fileDescDao)
    {
        this.fileDescDao = fileDescDao;
    }

    /**
     * 添加文件信息
     * @param fileDesc
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public String add(FileDesc fileDesc) throws Exception
    {
        return fileDescDao.insert(fileDesc);
    }

    /**
     * 更新文件信息
     * @param fileDesc
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void update(FileDesc fileDesc) throws Exception
    {
        fileDescDao.update(fileDesc);
    }

    /**
     * 删除文件信息
     * @param fileDesc
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) throws Exception
    {
        fileDescDao.delete(id);
    }

    /**
     * 获取文件信息
     * @param fileDesc
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public FileDesc get(String id) throws Exception
    {
        return fileDescDao.get(id);
    }

}
