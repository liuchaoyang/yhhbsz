package com.yzxt.yh.module.ach.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.ach.bean.Archive;
import com.yzxt.yh.module.ach.bean.FamilyHistory;
import com.yzxt.yh.module.ach.bean.LifeEnv;
import com.yzxt.yh.module.ach.bean.PreviousHistory;
import com.yzxt.yh.module.ach.dao.ArchiveDao;
import com.yzxt.yh.module.ach.dao.FamilyHistoryDao;
import com.yzxt.yh.module.ach.dao.LifeEnvDao;
import com.yzxt.yh.module.ach.dao.PreviousHistoryDao;
import com.yzxt.yh.module.sys.bean.Customer;
import com.yzxt.yh.module.sys.bean.FileDesc;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.service.CustomerService;
import com.yzxt.yh.module.sys.service.FileDescService;

@Transactional(ConstTM.DEFAULT)
public class ArchiveService
{
    private ArchiveDao archiveDao;
    private FamilyHistoryDao familyHistoryDao;
    private LifeEnvDao lifeEnvDao;
    private PreviousHistoryDao previousHistoryDao;
    private CustomerService customerService;
    private FileDescService fileDescService;

    public ArchiveDao getArchiveDao()
    {
        return archiveDao;
    }

    public void setArchiveDao(ArchiveDao archiveDao)
    {
        this.archiveDao = archiveDao;
    }

    public FamilyHistoryDao getFamilyHistoryDao()
    {
        return familyHistoryDao;
    }

    public void setFamilyHistoryDao(FamilyHistoryDao familyHistoryDao)
    {
        this.familyHistoryDao = familyHistoryDao;
    }

    public LifeEnvDao getLifeEnvDao()
    {
        return lifeEnvDao;
    }

    public void setLifeEnvDao(LifeEnvDao lifeEnvDao)
    {
        this.lifeEnvDao = lifeEnvDao;
    }

    public PreviousHistoryDao getPreviousHistoryDao()
    {
        return previousHistoryDao;
    }

    public void setPreviousHistoryDao(PreviousHistoryDao previousHistoryDao)
    {
        this.previousHistoryDao = previousHistoryDao;
    }

    public CustomerService getCustomerService()
    {
        return customerService;
    }

    public void setCustomerService(CustomerService customerService)
    {
        this.customerService = customerService;
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
     * 查询用户档案列表
     * 
     * @param filter
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<Customer> query(Map<String, Object> filter, int page, int pageSize)
    {
        return archiveDao.query(filter, page, pageSize);
    }

    /**
     * 加载档案信息
     * @param custId
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Archive load(String custId) throws Exception
    {
        Archive archive = archiveDao.load(custId);
        if (archive != null)
        {
            archive.setPreviousHistorys(previousHistoryDao.getByCust(custId));
            archive.setFamilyHistorys(familyHistoryDao.getByCust(custId));
            archive.setLifeEnvs(lifeEnvDao.getByCust(custId));
            archive.setCustomer(customerService.load(custId));
        }
        return archive;
    }

    /**
     * 保存档案信息
     * @param archive
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result save(Archive archive) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String custId = archive.getCustId();
        // 保存档案
        if (archiveDao.isExist(custId))
        // 修改档案
        {
            archive.setUpdateTime(now);
            archiveDao.update(archive);
            previousHistoryDao.deleteByCust(custId);
            familyHistoryDao.deleteByCust(custId);
            lifeEnvDao.deleteByCust(custId);
        }
        else
        // 新增档案
        {
            archive.setArchiveNo("-");
            archive.setCreateTime(now);
            archive.setUpdateTime(now);
            archiveDao.insert(archive);
        }
        // 保存既往史
        List<PreviousHistory> phs = archive.getPreviousHistorys();
        if (phs != null && !phs.isEmpty())
        {
            for (PreviousHistory ph : phs)
            {
                ph.setCustId(custId);
                previousHistoryDao.insert(ph);
            }
        }
        // 保存家族史
        List<FamilyHistory> fhs = archive.getFamilyHistorys();
        if (fhs != null && !fhs.isEmpty())
        {
            for (FamilyHistory fh : fhs)
            {
                fh.setCustId(custId);
                familyHistoryDao.insert(fh);
            }
        }
        // 生活环境
        List<LifeEnv> les = archive.getLifeEnvs();
        if (les != null && !les.isEmpty())
        {
            for (LifeEnv le : les)
            {
                le.setCustId(custId);
                lifeEnvDao.insert(le);
            }
        }
        // 保存客户信息
        Customer cust = archive.getCustomer();
        cust.setUserId(archive.getCustId());
        cust.setUpdateTime(now);
        User user = cust.getUser();
        FileDesc imgFile = user.getIconFile();
        if (imgFile != null)
        {
            String imgFileId = fileDescService.add(imgFile);
            user.setImgFileId(imgFileId);
        }
        customerService.updateByAchive(cust);
        return new Result(Result.STATE_SUCESS, "保存成功", null);
    }

}
