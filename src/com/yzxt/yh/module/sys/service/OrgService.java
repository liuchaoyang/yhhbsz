package com.yzxt.yh.module.sys.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.common.Result;
import com.yzxt.yh.constant.ConstOrg;
import com.yzxt.yh.constant.ConstSeq;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.sys.bean.FileDesc;
import com.yzxt.yh.module.sys.bean.Org;
import com.yzxt.yh.module.sys.dao.OrgDao;
import com.yzxt.yh.util.StringUtil;

@Transactional(ConstTM.DEFAULT)
public class OrgService
{
    private OrgDao orgDao;
    private SeqService seqService;
    private FileDescService fileDescService;

    public OrgDao getOrgDao()
    {
        return orgDao;
    }

    public void setOrgDao(OrgDao orgDao)
    {
        this.orgDao = orgDao;
    }

    public SeqService getSeqService()
    {
        return seqService;
    }

    public void setSeqService(SeqService seqService)
    {
        this.seqService = seqService;
    }

    // 保存组织
    @Transactional(propagation = Propagation.REQUIRED)
    public Result saveUser(Org org) throws Exception
    {
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Org getOrg(String id) throws Exception
    {
        return orgDao.get(id);
    }

    /**
     * 获取子组织
     * @param orgId 组织节点ID
     * @param nextLvl true 为只查询指定节点下级的信息，false 为 只查询指定节点的信息，主要用于组织管理员查询根节点
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Org> getChildren(String orgId, boolean nextLvl)
    {
        List<Org> list = orgDao.getChildren(orgId, nextLvl);
        return list;
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
     * 加载组织信息
     * @param roleId
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Org load(String orgId) throws Exception
    {
        Org org = orgDao.load(orgId);
        return org;
    }

    /**
     * 根据组织ID或助记码获取需要显示的组织信息
     * @param roleId
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Org getShowOrg(String orgId) throws Exception
    {
        Org org = null;
        if (StringUtil.isNotEmpty(orgId) && !ConstOrg.DEFAULT_ID.equals(orgId))
        {
            // 第一个字符不是数字和长度不大于15的，都是助记码，其它为组织ID
            if (orgId.length() <= 15 && !Character.isDigit(orgId.charAt(0)))
            {
                org = orgDao.getByMnemonicCode(orgId);
            }
            else
            {
                org = orgDao.load(orgId);
            }
        }
        // 如果参数中的机构无效，取默认机构
        if (org == null)
        {
            org = orgDao.load(ConstOrg.DEFAULT_ID);
        }
        return org;
    }

    /**
     * 新增客户
     * @param cust
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result add(Org org) throws Exception
    {
        if (orgDao.getOrgExist(org.getName(), null))
        {
            return new Result(Result.STATE_FAIL, "组织重复。", null);
        }
        String mnemonicCode = org.getMnemonicCode();
        if (StringUtil.isNotEmpty(mnemonicCode))
        {
            if (orgDao.getOrgMnemonicCodeExist(mnemonicCode, null))
            {
                return new Result(Result.STATE_FAIL, "组织助记码重复。", null);
            }
        }
        // 保存组织图标
        FileDesc logo = org.getLogo();
        if (logo != null)
        {
            String logoId = fileDescService.add(logo);
            org.setLogoId(logoId);
        }
        String id = seqService.getNextVal(ConstSeq.ORG) + "";
        org.setId(id);
        String pFullId = org.getFullId();
        org.setFullId(StringUtil.isNotEmpty(pFullId) ? pFullId + id + "/" : "/" + id + "/");
        Timestamp now = new Timestamp(System.currentTimeMillis());
        org.setCreateTime(now);
        org.setUpdateBy(org.getCreateBy());
        org.setUpdateTime(now);
        orgDao.insert(org);
        return new Result(Result.STATE_SUCESS, "保存成功。", null);
    }

    /**
     * 更新客户信息
     * @param cust
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result update(Org org) throws Exception
    {
        if (orgDao.getOrgExist(org.getName(), org.getId()))
        {
            return new Result(Result.STATE_FAIL, "组织重复。", null);
        }
        String mnemonicCode = org.getMnemonicCode();
        if (StringUtil.isNotEmpty(mnemonicCode))
        {
            if (orgDao.getOrgMnemonicCodeExist(mnemonicCode, org.getId()))
            {
                return new Result(Result.STATE_FAIL, "组织助记码重复。", null);
            }
        }
        // 保存组织图标
        FileDesc logo = org.getLogo();
        if (logo != null)
        {
            String logoId = fileDescService.add(logo);
            org.setLogoId(logoId);
        }
        Timestamp now = new Timestamp(System.currentTimeMillis());
        org.setUpdateTime(now);
        orgDao.update(org);
        return new Result(Result.STATE_SUCESS, "保存成功。", null);
    }

}
