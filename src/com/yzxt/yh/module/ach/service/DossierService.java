package com.yzxt.yh.module.ach.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.ach.bean.Dossier;
import com.yzxt.yh.module.ach.bean.DossierDetail;
import com.yzxt.yh.module.ach.dao.DossierDao;
import com.yzxt.yh.module.ach.dao.DossierDetailDao;
import com.yzxt.yh.module.sys.bean.FileDesc;
import com.yzxt.yh.module.sys.service.FileDescService;

@Transactional(ConstTM.DEFAULT)
public class DossierService
{
    private DossierDao dossierDao;
    private DossierDetailDao dossierDetailDao;
    private FileDescService fileDescService;

    public DossierDao getDossierDao()
    {
        return dossierDao;
    }

    public void setDossierDao(DossierDao dossierDao)
    {
        this.dossierDao = dossierDao;
    }

    public DossierDetailDao getDossierDetailDao()
    {
        return dossierDetailDao;
    }

    public void setDossierDetailDao(DossierDetailDao dossierDetailDao)
    {
        this.dossierDetailDao = dossierDetailDao;
    }

    public FileDescService getFileDescService()
    {
        return fileDescService;
    }

    public void setFileDescService(FileDescService fileDescService)
    {
        this.fileDescService = fileDescService;
    }

    /**
     * 添加病历夹
     * @param dossier
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result add(Dossier dossier) throws Exception
    {
        // 客户端可能包含创建时间
        if (dossier.getCreateTime() == null)
        {
            dossier.setUpdateTime(dossier.getCreateTime());
        }
        else
        {
            Timestamp now = new Timestamp(System.currentTimeMillis());
            dossier.setCreateTime(now);
            dossier.setUpdateTime(now);
        }
        dossier.setUpdateBy(dossier.getCreateBy());
        String dossierId = dossierDao.insert(dossier);
        List<FileDesc> fds = dossier.getFileDescs();
        if (fds != null && !fds.isEmpty())
        {
            for (int i = 0; i < fds.size(); i++)
            {
                FileDesc fd = fds.get(i);
                String fileId = fileDescService.add(fd);
                DossierDetail detail = new DossierDetail();
                detail.setDossierId(dossierId);
                detail.setFileId(fileId);
                // 顺序号，从1开始
                detail.setSeq(i + 1);
                // 保存图片明细
                dossierDetailDao.insert(detail);
            }
        }
        return new Result(Result.STATE_SUCESS, "保存成功", null);
    }

    /**
     * 获取病历夹明细
     * @param id
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Dossier load(String id) throws Exception
    {
        Dossier dossier = dossierDao.load(id);
        if (dossier != null)
        {
            dossier.setDetails(dossierDetailDao.getByDossier(id));
        }
        return dossier;
    }

    /**
     * 查询病历夹
     * @param filter
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<Dossier> query(Map<String, Object> filter, int page, int pageSize)
    {
        return dossierDao.query(filter, page, pageSize);
    }

    /**
     * 客户端查询病历夹
     * @param dossier
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageTran<Dossier> queryTran(Map<String, Object> filter, Timestamp sysTime, int count)
    {
        PageTran<Dossier> pageTran = dossierDao.queryTran(filter, sysTime, count);
        return pageTran;
    }

}
