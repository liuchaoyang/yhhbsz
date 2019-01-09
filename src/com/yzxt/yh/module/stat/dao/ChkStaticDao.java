package com.yzxt.yh.module.stat.dao;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.yh.module.stat.vo.AreaChk;
import com.yzxt.yh.module.stat.vo.PeopleDis;
import com.yzxt.yh.util.DateUtil;

public class ChkStaticDao extends HibernateSupport<Object> implements BaseDao<Object>
{

    /**
     * 查看指定区域（省、市、县、乡镇）一段时间内的建档数量
     * @return
     * @throws ParseException 
     */
    @SuppressWarnings("unchecked")
    public List<AreaChk> queryAreaChk(Map<String, Object> filter) throws ParseException
    {
        StringBuilder sql = new StringBuilder();
        // 查询条件
        StringBuilder commonCdt = new StringBuilder();
        // 检测时间
        Date startChkDate = (Date) filter.get("startChkDate");
        if (startChkDate != null)
        {
            commonCdt.append(" and tc.check_time >= '").append(DateUtil.toHtmlDate(startChkDate)).append("'");
        }
        Date endChkDate = (Date) filter.get("endChkDate");
        if (endChkDate != null)
        {
            commonCdt.append(" and tc.check_time < '").append(DateUtil.toHtmlDate(DateUtil.addDay(endChkDate, 1)))
                    .append("'");
        }
        // -----------------血压-----------------
        sql.append("select 'xy' as chk_item_id, '血压' as chk_item_name, count(tc.id) as num, sum(case when tc.level >0 then 1 else 0 end) as warn_num");
        sql.append(" from chk_pressure_pulse tc");
        sql.append(" inner join sys_user tu on tu.id = tc.cust_id");
        sql.append(" inner join sys_org tor on tor.id = tu.org_id");
        sql.append(" where 1=1");
        sql.append(commonCdt);
        // -----------------血糖-----------------
        sql.append(" union all ");
        sql.append("select 'xt' as chk_item_id, '血糖' as chk_item_name, count(tc.id) as num, sum(case when tc.level >0 then 1 else 0 end) as warn_num");
        sql.append(" from chk_blood_sugar tc");
        sql.append(" inner join sys_user tu on tu.id = tc.cust_id");
        sql.append(" inner join sys_org tor on tor.id = tu.org_id");
        sql.append(" where 1=1");
        sql.append(commonCdt);
        // -----------------心率-----------------
        sql.append(" union all ");
        sql.append("select 'xl' as chk_item_id, '心率' as chk_item_name, count(tc.id) as num, sum(case when tc.level >0 then 1 else 0 end) as warn_num");
        sql.append(" from chk_pulse tc");
        sql.append(" inner join sys_user tu on tu.id = tc.cust_id");
        sql.append(" inner join sys_org tor on tor.id = tu.org_id");
        sql.append(" where 1=1");
        sql.append(commonCdt);
        // -----------------血氧-----------------
        sql.append(" union all ");
        sql.append("select 'xo' as chk_item_id, '血氧' as chk_item_name, count(tc.id) as num, sum(case when tc.level >0 then 1 else 0 end) as warn_num");
        sql.append(" from chk_blood_oxygen tc");
        sql.append(" inner join sys_user tu on tu.id = tc.cust_id");
        sql.append(" inner join sys_org tor on tor.id = tu.org_id");
        sql.append(" where 1=1");
        sql.append(commonCdt);
        // -----------------体温-----------------
        sql.append(" union all ");
        sql.append("select 'tw' as chk_item_id, '体温' as chk_item_name, count(tc.id) as num, sum(case when tc.level >0 then 1 else 0 end) as warn_num");
        sql.append(" from chk_temperature tc");
        sql.append(" inner join sys_user tu on tu.id = tc.cust_id");
        sql.append(" inner join sys_org tor on tor.id = tu.org_id");
        sql.append(" where 1=1");
        sql.append(commonCdt);
        // -----------------胆固醇-----------------
        sql.append(" union all ");
        sql.append("select 'dgc' as chk_item_id, '胆固醇' as chk_item_name, count(tc.id) as num, sum(case when tc.level >0 then 1 else 0 end) as warn_num");
        sql.append(" from chk_total_cholesterol tc");
        sql.append(" inner join sys_user tu on tu.id = tc.cust_id");
        sql.append(" inner join sys_org tor on tor.id = tu.org_id");
        sql.append(" where 1=1");
        sql.append(commonCdt);
        // -----------------尿酸-----------------
        sql.append(" union all ");
        sql.append("select 'ns' as chk_item_id, '尿酸' as chk_item_name, count(tc.id) as num, sum(case when tc.level >0 then 1 else 0 end) as warn_num");
        sql.append(" from chk_uric_acid tc");
        sql.append(" inner join sys_user tu on tu.id = tc.cust_id");
        sql.append(" inner join sys_org tor on tor.id = tu.org_id");
        sql.append(" where 1=1");
        sql.append(commonCdt);
        // -----------------尿常规-----------------
        sql.append(" union all ");
        sql.append("select 'nsfx' as chk_item_id, '尿常规' as chk_item_name, count(tc.id) as num, sum(case when tc.level >0 then 1 else 0 end) as warn_num");
        sql.append(" from chk_analysis_uric_acid tc");
        sql.append(" inner join sys_user tu on tu.id = tc.cust_id");
        sql.append(" inner join sys_org tor on tor.id = tu.org_id");
        sql.append(" where 1=1");
        sql.append(commonCdt);
        // -----------------体脂-----------------
        sql.append(" union all ");
        sql.append("select 'tz' as chk_item_id, '体脂' as chk_item_name, count(tc.id) as num, sum(case when tc.level >0 then 1 else 0 end) as warn_num");
        sql.append(" from chk_body_fat tc");
        sql.append(" inner join sys_user tu on tu.id = tc.cust_id");
        sql.append(" inner join sys_org tor on tor.id = tu.org_id");
        sql.append(" where 1=1");
        sql.append(commonCdt);
        // 统计
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString())
                .addScalar("chk_item_id", Hibernate.STRING).addScalar("chk_item_name", Hibernate.STRING)
                .addScalar("num", Hibernate.INTEGER).addScalar("warn_num", Hibernate.INTEGER).list();
        List<AreaChk> list = new ArrayList<AreaChk>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            AreaChk ac = new AreaChk();
            idx = 0;
            ac.setChkItemId((String) objs[idx]);
            idx++;
            ac.setChkItemName((String) objs[idx]);
            idx++;
            ac.setNum((Integer) objs[idx]);
            idx++;
            ac.setWarnNum(objs[idx] != null ? (Integer) objs[idx] : 0);
            idx++;
            list.add(ac);
        }
        return list;
    }

    /**
     * 查询指定区域的检测人群分布
     * @return
     * @throws ParseException
     */
    @SuppressWarnings("unchecked")
    public List<PeopleDis>[] queryPeopleDis(Map<String, Object> filter) throws ParseException
    {
        // 年龄区段
        Date today = new Date();
        // 今天刚好等于31岁的最晚出生日期
        String bir1 = DateUtil.toHtmlDate(DateUtil.getLastestBirthday(31, today));
        // 今天刚好等于41岁的最晚出生日期
        String bir2 = DateUtil.toHtmlDate(DateUtil.getLastestBirthday(41, today));
        // 今天刚好等于51岁的最晚出生日期
        String bir3 = DateUtil.toHtmlDate(DateUtil.getLastestBirthday(51, today));
        // 今天刚好等于61岁的最晚出生日期
        String bir4 = DateUtil.toHtmlDate(DateUtil.getLastestBirthday(61, today));
        // 今天刚好等于71岁的最晚出生日期
        String bir5 = DateUtil.toHtmlDate(DateUtil.getLastestBirthday(71, today));
        // 查询条件
        StringBuilder commonCdt = new StringBuilder();
        // 检测时间
        Date startChkDate = (Date) filter.get("startChkDate");
        if (startChkDate != null)
        {
            commonCdt.append(" and tc.check_time >= '").append(DateUtil.toHtmlDate(startChkDate)).append("'");
        }
        Date endChkDate = (Date) filter.get("endChkDate");
        if (endChkDate != null)
        {
            commonCdt.append(" and tc.check_time < '").append(DateUtil.toHtmlDate(DateUtil.addDay(endChkDate, 1)))
                    .append("'");
        }
        StringBuilder sql = new StringBuilder();
        sql.append("select count(t.id) as num, sum(case when tcu.sex = 1 then 1 else 0 end) as num1, sum(case when tcu.sex = 2 then 1 else 0 end) as num2");
        // 30岁及30岁以内的人数
        sql.append(", sum(case when tcu.birthday > str_to_date('").append(bir1)
                .append("', '%Y-%m-%d') then 1 else 0 end) as numa");
        sql.append(", sum(case when tcu.birthday > str_to_date('").append(bir2)
                .append("', '%Y-%m-%d') and tcu.birthday <= str_to_date('").append(bir1)
                .append("', '%Y-%m-%d') then 1 else 0 end) as numb");
        sql.append(", sum(case when tcu.birthday > str_to_date('").append(bir3)
                .append("', '%Y-%m-%d') and tcu.birthday <= str_to_date('").append(bir2)
                .append("', '%Y-%m-%d') then 1 else 0 end) as numc");
        sql.append(", sum(case when tcu.birthday > str_to_date('").append(bir4)
                .append("', '%Y-%m-%d') and tcu.birthday <= str_to_date('").append(bir3)
                .append("', '%Y-%m-%d') then 1 else 0 end) as numd");
        sql.append(", sum(case when tcu.birthday > str_to_date('").append(bir5)
                .append("', '%Y-%m-%d') and tcu.birthday <= str_to_date('").append(bir4)
                .append("', '%Y-%m-%d') then 1 else 0 end) as nume");
        sql.append(", sum(case when tcu.birthday <= str_to_date('").append(bir5)
                .append("', '%Y-%m-%d') then 1 else 0 end) as numf");
        sql.append(" from sys_user t");
        sql.append(" inner join sys_customer tcu on tcu.user_id = t.id");
        sql.append(" inner join sys_org tor on tor.id = t.org_id");
        sql.append(" where 1=1");
        // 有检测记录
        sql.append(" and (");
        // -----------------血压-----------------
        sql.append("exists(select tc.id from chk_pressure_pulse tc where tc.cust_id = t.id").append(commonCdt)
                .append(")");
        // -----------------血糖-----------------
        sql.append(" or exists(select tc.id from chk_blood_sugar tc where tc.cust_id = t.id").append(commonCdt)
                .append(")");
        // -----------------心率-----------------
        sql.append(" or exists(select tc.id from chk_pulse tc where tc.cust_id = t.id").append(commonCdt).append(")");
        // -----------------血氧-----------------
        sql.append(" or exists(select tc.id from chk_blood_oxygen tc where tc.cust_id = t.id").append(commonCdt)
                .append(")");
        // -----------------体温-----------------
        sql.append(" or exists(select tc.id from chk_temperature tc where tc.cust_id = t.id").append(commonCdt)
                .append(")");
        // -----------------胆固醇-----------------
        sql.append(" or exists(select tc.id from chk_total_cholesterol tc where tc.cust_id = t.id").append(commonCdt)
                .append(")");
        // -----------------尿酸-----------------
        sql.append(" or exists(select tc.id from chk_uric_acid tc where tc.cust_id = t.id").append(commonCdt)
                .append(")");
        // -----------------尿常规-----------------
        sql.append(" or exists(select tc.id from chk_analysis_uric_acid tc where tc.cust_id = t.id").append(commonCdt)
                .append(")");
        // -----------------体脂-----------------
        sql.append(" or exists(select tc.id from chk_body_fat tc where tc.cust_id = t.id").append(commonCdt)
                .append(")");
        sql.append(")");
        // 统计性别
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString()).addScalar("num", Hibernate.INTEGER)
                .addScalar("num1", Hibernate.INTEGER).addScalar("num2", Hibernate.INTEGER)
                .addScalar("numa", Hibernate.INTEGER).addScalar("numb", Hibernate.INTEGER)
                .addScalar("numc", Hibernate.INTEGER).addScalar("numd", Hibernate.INTEGER)
                .addScalar("nume", Hibernate.INTEGER).addScalar("numf", Hibernate.INTEGER).list();
        Object[] objs = objsList.get(0);
        List<PeopleDis> sexlist = new ArrayList<PeopleDis>();
        PeopleDis pd0 = new PeopleDis();
        pd0.setName("男");
        pd0.setNum(objs[1] != null ? (Integer) objs[1] : 0);
        PeopleDis pd1 = new PeopleDis();
        pd1.setName("女");
        pd1.setNum(objs[2] != null ? (Integer) objs[2] : 0);
        PeopleDis pd2 = new PeopleDis();
        pd2.setName("未知性别");
        pd2.setNum((Integer) objs[0] - pd0.getNum() - pd1.getNum());
        sexlist.add(pd0);
        sexlist.add(pd1);
        sexlist.add(pd2);
        // 统计年龄
        List<PeopleDis> agelist = new ArrayList<PeopleDis>();
        PeopleDis pd10 = new PeopleDis();
        pd10.setName("30岁及以内");
        pd10.setNum(objs[3] != null ? (Integer) objs[3] : 0);
        agelist.add(pd10);
        PeopleDis pd11 = new PeopleDis();
        pd11.setName("31岁-40岁");
        pd11.setNum(objs[4] != null ? (Integer) objs[4] : 0);
        agelist.add(pd11);
        PeopleDis pd12 = new PeopleDis();
        pd12.setName("41岁-50岁");
        pd12.setNum(objs[5] != null ? (Integer) objs[5] : 0);
        agelist.add(pd12);
        PeopleDis pd13 = new PeopleDis();
        pd13.setName("51岁-60岁");
        pd13.setNum(objs[6] != null ? (Integer) objs[6] : 0);
        agelist.add(pd13);
        PeopleDis pd14 = new PeopleDis();
        pd14.setName("61岁-70岁");
        pd14.setNum(objs[7] != null ? (Integer) objs[7] : 0);
        agelist.add(pd14);
        PeopleDis pd15 = new PeopleDis();
        pd15.setName("71岁及以上");
        pd15.setNum(objs[8] != null ? (Integer) objs[8] : 0);
        agelist.add(pd15);
        PeopleDis pd16 = new PeopleDis();
        pd16.setName("未知年龄");
        pd16.setNum((Integer) objs[0] - pd10.getNum() - pd11.getNum() - pd12.getNum() - pd13.getNum() - pd14.getNum()
                - pd15.getNum());
        agelist.add(pd16);
        return new List[]
        {sexlist, agelist};
    }

}
