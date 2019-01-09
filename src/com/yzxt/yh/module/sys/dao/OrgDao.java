package com.yzxt.yh.module.sys.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.yh.module.sys.bean.Org;
import com.yzxt.yh.util.StringUtil;

public class OrgDao extends HibernateSupport<Org> implements BaseDao<Org>
{
    /**
     * 获取子组织
     * @param orgId 组织节点ID
     * @param nextLvl true 为只查询指定节点下级的信息，false 为 只查询指定节点的信息，主要用于组织管理员查询根节点
     */
    @SuppressWarnings("unchecked")
    public List<Org> getChildren(String parentId, boolean nextLvl)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, t.parent_id, t.level, t.orgName, t.mnemonic_code, t.phone, t.contact_person, t.address, t.logo_id, t.show_text");
        sql.append(", (select sfd.path from sys_file_desc sfd where sfd.id = t.logo_id) as logo_path");
        sql.append(", (select count(st.id) from sys_org st where st.parent_id = t.id) as children_count");
        sql.append(" from sys_org t");
        sql.append(" where 1=1");
        // 是否只查询指定节点信息
        if (nextLvl)
        {
            if (StringUtil.isNotEmpty(parentId))
            {
                sql.append(" and t.parent_id = ?");
                params.add(parentId, Hibernate.STRING);
            }
            else
            {
                sql.append(" and (t.parent_id = '' or t.parent_id is null)");
            }
        }
        else
        {
            sql.append(" and t.id = ?");
            params.add(parentId, Hibernate.STRING);
        }
        sql.append(" order by t.create_time asc");
        List<Object[]> objss = super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("parent_id", Hibernate.STRING).addScalar("level", Hibernate.INTEGER)
                .addScalar("orgName", Hibernate.STRING).addScalar("mnemonic_code", Hibernate.STRING)
                .addScalar("phone", Hibernate.STRING).addScalar("contact_person", Hibernate.STRING)
                .addScalar("address", Hibernate.STRING).addScalar("logo_id", Hibernate.STRING)
                .addScalar("show_text", Hibernate.STRING).addScalar("logo_path", Hibernate.STRING)
                .addScalar("children_count", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes())
                .list();
        int i = 0;
        List<Org> orgs = new ArrayList<Org>();
        for (Object[] objs : objss)
        {
            Org org = new Org();
            i = 0;
            org.setId((String) objs[i]);
            i++;
            org.setParentId((String) objs[i]);
            i++;
            org.setLevel((Integer) objs[i]);
            i++;
            org.setName((String) objs[i]);
            i++;
            org.setMnemonicCode((String) objs[i]);
            i++;
            org.setPhone((String) objs[i]);
            i++;
            org.setContactPerson((String) objs[i]);
            i++;
            org.setAddress((String) objs[i]);
            i++;
            org.setLogoId((String) objs[i]);
            i++;
            org.setShowText((String) objs[i]);
            i++;
            org.setLogoPath((String) objs[i]);
            i++;
            org.setIsLeaf(((Integer) objs[i]).intValue() == 0 ? 0 : 1);
            i++;
            orgs.add(org);
        }
        return orgs;
    }

    /**
     * 插入组织信息
     * @param role
     * @return
     * @throws Exception
     */
    public String insert(Org org) throws Exception
    {
        super.save(org);
        return org.getId();
    }

    /**
     * 更新组织信息
     */
    public int update(Org org) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update Org set name = ?, mnemonicCode = ?, createBy = ?, phone = ?, address=?, updateBy=?, updateTime=?, contactPerson=?");
        sql.append(", showText = ?");
        params.add(org.getName(), Hibernate.STRING).add(org.getMnemonicCode(), Hibernate.STRING)
                .add(org.getCreateBy(), Hibernate.STRING).add(org.getPhone(), Hibernate.STRING)
                .add(org.getAddress(), Hibernate.STRING).add(org.getUpdateBy(), Hibernate.STRING)
                .add(org.getUpdateTime(), Hibernate.TIMESTAMP).add(org.getContactPerson(), Hibernate.STRING)
                .add(org.getShowText(), Hibernate.STRING);
        if (StringUtil.isNotEmpty(org.getLogoId()))
        {
            sql.append(", logoId = ?");
            params.add(org.getLogoId(), Hibernate.STRING);
        }
        sql.append(" where id = ?");
        params.add(org.getId(), Hibernate.STRING);
        int c = super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
        return c;
    }

    /**
     * 加载角色信息
     */
    @SuppressWarnings("unchecked")
    public Org load(String id) throws Exception
    {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, t.parent_id, t.level, t.orgName, t.mnemonic_code, t.phone, t.contact_person, t.address");
        sql.append(", t.logo_id , t.show_text, t.create_by, t.create_time, t.update_by, t.update_time");
        sql.append(", (select sfd.path from sys_file_desc sfd where sfd.id = logo_id) as logo_path");
        sql.append(" from sys_org t");
        sql.append(" where t.id = ?");
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("parent_id", Hibernate.STRING).addScalar("level", Hibernate.INTEGER)
                .addScalar("orgName", Hibernate.STRING).addScalar("mnemonic_code", Hibernate.STRING)
                .addScalar("phone", Hibernate.STRING).addScalar("contact_person", Hibernate.STRING)
                .addScalar("address", Hibernate.STRING).addScalar("logo_id", Hibernate.STRING)
                .addScalar("show_text", Hibernate.STRING).addScalar("create_by", Hibernate.STRING)
                .addScalar("create_time", Hibernate.TIMESTAMP).addScalar("update_by", Hibernate.STRING)
                .addScalar("update_time", Hibernate.TIMESTAMP).addScalar("logo_path", Hibernate.STRING)
                .setParameter(0, id).list();
        Org org = null;
        if (objsList != null && !objsList.isEmpty())
        {
            Object[] objs = objsList.get(0);
            org = new Org();
            int i = 0;
            org.setId((String) objs[i]);
            i++;
            org.setParentId((String) objs[i]);
            i++;
            org.setLevel((Integer) objs[i]);
            i++;
            org.setName((String) objs[i]);
            i++;
            org.setMnemonicCode((String) objs[i]);
            i++;
            org.setPhone((String) objs[i]);
            i++;
            org.setContactPerson((String) objs[i]);
            i++;
            org.setAddress((String) objs[i]);
            i++;
            org.setLogoId((String) objs[i]);
            i++;
            org.setShowText((String) objs[i]);
            i++;
            org.setCreateBy((String) objs[i]);
            i++;
            org.setCreateTime((Timestamp) objs[i]);
            i++;
            org.setUpdateBy((String) objs[i]);
            i++;
            org.setUpdateTime((Timestamp) objs[i]);
            i++;
            org.setLogoPath((String) objs[i]);
            i++;
            i++;
        }
        return org;
    }

    /**
     * 根据助记码获取组织信息
     * @param mnemonicCode
     * @return
     */
    @SuppressWarnings("unchecked")
    public Org getByMnemonicCode(String mnemonicCode)
    {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, t.parent_id, t.level, t.orgName, t.mnemonic_code, t.phone, t.contact_person, t.address");
        sql.append(", t.logo_id , t.show_text, t.create_by, t.create_time, t.update_by, t.update_time");
        sql.append(", (select sfd.path from sys_file_desc sfd where sfd.id = logo_id) as logo_path");
        sql.append(" from sys_org t");
        sql.append(" where t.mnemonic_code = ?");
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("parent_id", Hibernate.STRING).addScalar("level", Hibernate.INTEGER)
                .addScalar("orgName", Hibernate.STRING).addScalar("mnemonic_code", Hibernate.STRING)
                .addScalar("phone", Hibernate.STRING).addScalar("contact_person", Hibernate.STRING)
                .addScalar("address", Hibernate.STRING).addScalar("logo_id", Hibernate.STRING)
                .addScalar("show_text", Hibernate.STRING).addScalar("create_by", Hibernate.STRING)
                .addScalar("create_time", Hibernate.TIMESTAMP).addScalar("update_by", Hibernate.STRING)
                .addScalar("update_time", Hibernate.TIMESTAMP).addScalar("logo_path", Hibernate.STRING)
                .setParameter(0, mnemonicCode).list();
        Org org = null;
        if (objsList != null && !objsList.isEmpty())
        {
            Object[] objs = objsList.get(0);
            org = new Org();
            int i = 0;
            org.setId((String) objs[i]);
            i++;
            org.setParentId((String) objs[i]);
            i++;
            org.setLevel((Integer) objs[i]);
            i++;
            org.setName((String) objs[i]);
            i++;
            org.setMnemonicCode((String) objs[i]);
            i++;
            org.setPhone((String) objs[i]);
            i++;
            org.setContactPerson((String) objs[i]);
            i++;
            org.setAddress((String) objs[i]);
            i++;
            org.setLogoId((String) objs[i]);
            i++;
            org.setShowText((String) objs[i]);
            i++;
            org.setCreateBy((String) objs[i]);
            i++;
            org.setCreateTime((Timestamp) objs[i]);
            i++;
            org.setUpdateBy((String) objs[i]);
            i++;
            org.setUpdateTime((Timestamp) objs[i]);
            i++;
            org.setLogoPath((String) objs[i]);
            i++;
            i++;
        }
        return org;
    }

    /**
     * 判断组织是否重复
     * @param roleName
     * @param exceptUserId
     * @return
     * @throws Exception
     */
    public boolean getOrgExist(String orgName, String exceptUserId) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select count(t.id) as c from sys_org t where 1=1 ");
        sql.append(" and lower(t.orgName) = ?");
        params.add(orgName.toLowerCase(), Hibernate.STRING);
        if (StringUtil.isNotEmpty(exceptUserId))
        {
            sql.append(" and t.id <> ?");
            params.add(exceptUserId, Hibernate.STRING);
        }
        Long c = (Long) super.getSession().createSQLQuery(sql.toString()).addScalar("c", Hibernate.LONG)
                .setParameters(params.getVals(), params.getTypes()).uniqueResult();
        return c.longValue() > 0;
    }

    /**
     * 判断组织是否重复
     * @param roleName
     * @param exceptUserId
     * @return
     * @throws Exception
     */
    public boolean getOrgMnemonicCodeExist(String orgMnemonicCode, String exceptUserId) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select count(t.id) as c from sys_org t where 1=1 ");
        sql.append(" and lower(t.mnemonic_code) = ?");
        params.add(orgMnemonicCode.toLowerCase(), Hibernate.STRING);
        if (StringUtil.isNotEmpty(exceptUserId))
        {
            sql.append(" and t.id <> ?");
            params.add(exceptUserId, Hibernate.STRING);
        }
        Long c = (Long) super.getSession().createSQLQuery(sql.toString()).addScalar("c", Hibernate.LONG)
                .setParameters(params.getVals(), params.getTypes()).uniqueResult();
        return c.longValue() > 0;
    }

}
