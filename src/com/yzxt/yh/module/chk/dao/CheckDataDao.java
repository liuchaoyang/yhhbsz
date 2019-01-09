package com.yzxt.yh.module.chk.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.chk.bean.CheckData;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;

public class CheckDataDao extends HibernateSupport<CheckData> implements BaseDao<CheckData>
{
    /**
     * 查询所有检测项目
     * @param filter
     * @param page
     * @param pageSize
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public PageModel<CheckData> query(Map<String, Object> filter, int page, int pageSize) throws Exception
    {
        PageModel<CheckData> pageModel = new PageModel<CheckData>();
        StringBuilder mSql = new StringBuilder();
        StringBuilder commonCdt = new StringBuilder();
        // 操作人
        User user = (User) filter.get("operUser");
        // 权限
        if (Constant.USER_TYPE_DOCTOR.equals(user.getType()))
        // 医生查询自己的会员记录
        {
            commonCdt
                    .append(" and exists(select smi.id from svb_member_info smi where smi.cust_id = tc.cust_id and smi.doctor_id = '")
                    .append(user.getId()).append("' and smi.state = ").append(Constant.CUSTOMER_MEMBER_STATUS_YES)
                    .append(")");
        }
        else if (Constant.USER_TYPE_CUSTOMER.equals(user.getType()))
        // 普通客户查询自己的记录
        {
            commonCdt.append(" and tc.cust_id = '").append(user.getId()).append("'");
        }
        else if (!Constant.USER_TYPE_ADMIN.equals(user.getType()))
        // 其它非管理员用户不能查询到记录
        {
            return pageModel;
        }

        String keyword = (String) filter.get("keyword");
        if (StringUtil.isNotEmpty(keyword))
        {
            commonCdt.append(" and (tu.id_card like ").append(HibernateParams.getLikePart(keyword))
                    .append(" or tu.name_ like ").append(HibernateParams.getLikePart(keyword)).append(")");
        }
        // 只查询有告警信息的记录
        if ("Y".equals(filter.get("warnRec")))
        {
            commonCdt.append(" and tcw.id is not null");
        }
        String startDate = (String) filter.get("startDate");
        if (StringUtil.isNotEmpty(startDate))
        {
            commonCdt.append(" and tc.check_time >= '").append(startDate).append("'");
        }
        String endDate = (String) filter.get("endDate");
        if (StringUtil.isNotEmpty(endDate))
        {
            endDate = DateUtil.toHtmlDate(DateUtil.addDay(DateUtil.getDateFromHtml(endDate), 1));
            commonCdt.append(" and tc.check_time < '").append(endDate).append("'");
        }
        Set<String> itemSet = null;
        String itemType = (String) filter.get("itemType");
        if (StringUtil.isNotEmpty(itemType))
        {
            itemSet = new HashSet<String>();
            itemSet.add(itemType);
        }
        int itemC = 0;
        // -----------------血压-----------------
        if (itemSet == null || itemSet.contains("xy"))
        {
            if (itemC++ > 0)
            {
                mSql.append(" union all ");
            }
            mSql.append("select tc.id, tc.check_time, tu.name_ as cust_name, tc.cust_id, tu.id_card, 'xy' as item_type, '血压' as item_name");
            mSql.append(", concat('收缩压： ',CAST(tc.s_b_p AS CHAR), ' mmHg, 舒张压： ', CAST(tc.d_b_p AS CHAR), ' mmHg') as val_desc, tc.level, tc.descript");
            mSql.append(" from chk_pressure_pulse tc");
            mSql.append(" inner join sys_customer as tcu on tcu.user_id = tc.cust_id");
            mSql.append(" inner join sys_user as tu on tu.id = tc.cust_id");
            // mSql.append(" left join chk_check_warn as tcw on tcw.id = tc.id");
            mSql.append(" where 1=1");
            mSql.append(commonCdt);
        }
        // -----------------血糖-----------------
      /*  if (itemSet == null || itemSet.contains("xt"))
        {
            if (itemC++ > 0)
            {
                mSql.append(" union all ");
            }
            mSql.append("select tc.id, tc.check_time, tu.name_ as cust_name, tc.cust_id, tu.id_card, 'xt' as item_type, '血糖' as item_name");
            // 血糖类型1-空腹血糖2-餐前血糖3-餐后血糖4-服糖2小时后血糖
            mSql.append(", concat((case tc.blood_sugar_type when 1 then '空腹血糖' when 2 then '餐前血糖' when 3 then '餐后血糖' when 4 then '服糖2小时后血糖' else '' end), '值： '");
            mSql.append(", tc.blood_sugar, ' mmol/L') as val_desc, tc.level, tc.descript");
            mSql.append(" from chk_blood_sugar tc");
            mSql.append(" inner join sys_customer as tcu on tcu.user_id = tc.cust_id");
            mSql.append(" inner join sys_user as tu on tu.id = tc.cust_id");
            // mSql.append(" left join chk_check_warn as tcw on tcw.id = tc.id");
            mSql.append(" where tc.blood_sugar is not null");
            mSql.append(commonCdt);
        }*/
        // -----------------心率-----------------
        if (itemSet == null || itemSet.contains("xl"))
        {
            if (itemC++ > 0)
            {
                mSql.append(" union all ");
            }
            mSql.append("select tc.id, tc.check_time, tu.name_ as cust_name, tc.cust_id, tu.id_card, 'xl' as item_type, '心率' as item_name");
            mSql.append(", concat('心率值： '");
            mSql.append(", CAST(tc.pulse AS CHAR), ' 次/分钟') as val_desc, tc.level, tc.descript");
            mSql.append(" from chk_pulse tc");
            mSql.append(" inner join sys_customer as tcu on tcu.user_id = tc.cust_id");
            mSql.append(" inner join sys_user as tu on tu.id = tc.cust_id");
            // mSql.append(" left join chk_check_warn as tcw on tcw.id = tc.id");
            mSql.append(" where tc.pulse is not null");
            mSql.append(commonCdt);
        }
       /* //-----------------血氧----------------
        if (itemSet == null || itemSet.contains("xo"))
        {
            if (itemC++ > 0)
            {
                mSql.append(" union all ");
            }
            mSql.append("select tc.id, tc.check_time, tu.name_ as cust_name, tc.cust_id, tu.id_card, 'xo' as item_type, '血氧' as item_name");
            mSql.append(", concat('血氧值： '");
            mSql.append(", tc.b_o, ' %') as val_desc, tc.level, tc.descript");
            mSql.append(" from chk_blood_oxygen tc");
            mSql.append(" inner join sys_customer as tcu on tcu.user_id = tc.cust_id");
            mSql.append(" inner join sys_user as tu on tu.id = tc.cust_id");
            // mSql.append(" left join chk_check_warn as tcw on tcw.id = tc.id");
            mSql.append(" where tc.b_o is not null");
            mSql.append(commonCdt);
        }
        //-----------------体温----------------
        if (itemSet == null || itemSet.contains("tw"))
        {
            if (itemC++ > 0)
            {
                mSql.append(" union all ");
            }
            mSql.append("select tc.id, tc.check_time, tu.name_ as cust_name, tc.cust_id, tu.id_card, 'tw' as item_type, '体温' as item_name");
            mSql.append(", concat('体温值： '");
            mSql.append(", tc.temperature, ' ℃') as val_desc, tc.level, tc.descript");
            mSql.append(" from chk_temperature tc");
            mSql.append(" inner join sys_customer as tcu on tcu.user_id = tc.cust_id");
            mSql.append(" inner join sys_user as tu on tu.id = tc.cust_id");
            // mSql.append(" left join chk_check_warn as tcw on tcw.id = tc.id");
            mSql.append(" where tc.temperature is not null");
            mSql.append(commonCdt);
        }
        //-----------------胆固醇----------------
        if (itemSet == null || itemSet.contains("dgc"))
        {
            if (itemC++ > 0)
            {
                mSql.append(" union all ");
            }
            mSql.append("select tc.id, tc.check_time, tu.name_ as cust_name, tc.cust_id, tu.id_card, 'dgc' as item_type, '胆固醇' as item_name");
            mSql.append(", concat('胆固醇值： '");
            mSql.append(", tc.total_cholesterol, ' mmol/L') as val_desc, tc.level, tc.descript");
            mSql.append(" from chk_total_cholesterol tc");
            mSql.append(" inner join sys_customer as tcu on tcu.user_id = tc.cust_id");
            mSql.append(" inner join sys_user as tu on tu.id = tc.cust_id");
            // mSql.append(" left join chk_check_warn as tcw on tcw.id = tc.id");
            mSql.append(" where tc.total_cholesterol is not null");
            mSql.append(commonCdt);
        }
        //-----------------尿酸----------------
        if (itemSet == null || itemSet.contains("ns"))
        {
            if (itemC++ > 0)
            {
                mSql.append(" union all ");
            }
            mSql.append("select tc.id, tc.check_time, tu.name_ as cust_name, tc.cust_id, tu.id_card, 'ns' as item_type, '尿酸' as item_name");
            mSql.append(", concat('尿酸值： '");
            mSql.append(", tc.uric_acid, ' umol/L') as val_desc, tc.level, tc.descript");
            mSql.append(" from chk_uric_acid tc");
            mSql.append(" inner join sys_customer as tcu on tcu.user_id = tc.cust_id");
            mSql.append(" inner join sys_user as tu on tu.id = tc.cust_id");
            // mSql.append(" left join chk_check_warn as tcw on tcw.id = tc.id");
            mSql.append(" where tc.uric_acid is not null");
            mSql.append(commonCdt);
        }
        //-----------------尿常规----------------
        if (itemSet == null || itemSet.contains("nsfx"))
        {
            if (itemC++ > 0)
            {
                mSql.append(" union all ");
            }
            mSql.append("select tc.id, tc.check_time, tu.name_ as cust_name, tc.cust_id, tu.id_card, 'nsfx' as item_type, '尿常规' as item_name");
            mSql.append(", '查看尿常规数据' as val_desc, tc.level, tc.descript");
            mSql.append(" from chk_analysis_uric_acid tc");
            mSql.append(" inner join sys_customer as tcu on tcu.user_id = tc.cust_id");
            mSql.append(" inner join sys_user as tu on tu.id = tc.cust_id");
            // mSql.append(" left join chk_check_warn as tcw on tcw.id = tc.id");
            mSql.append(" where 1=1");
            mSql.append(commonCdt);
        }
        //-----------------体脂----------------
        if (itemSet == null || itemSet.contains("tz"))
        {
            if (itemC++ > 0)
            {
                mSql.append(" union all ");
            }
            mSql.append("select tc.id, tc.check_time, tu.name_ as cust_name, tc.cust_id, tu.id_card, 'tz' as item_type, '体脂率' as item_name");
            mSql.append(", concat('体脂率： '");
            mSql.append(", tc.BFR, ' %') as val_desc, tc.level, tc.descript");
            mSql.append(" from chk_body_fat tc");
            mSql.append(" inner join sys_customer as tcu on tcu.user_id = tc.cust_id");
            mSql.append(" inner join sys_user as tu on tu.id = tc.cust_id");
            // mSql.append(" left join chk_check_warn as tcw on tcw.id = tc.id");
            mSql.append(" where tc.BFR is not null");
            mSql.append(commonCdt);
        }*/
        // 总计录数
        StringBuilder totalCountsql = new StringBuilder();
        totalCountsql.append("select count(1) as c from (").append(mSql).append(") as t2");
        Integer totalCount = (Integer) super.getSession().createSQLQuery(totalCountsql.toString())
                .addScalar("c", Hibernate.INTEGER).uniqueResult();
        StringBuilder pSql = new StringBuilder();
        pSql.append(
                "select t2.id, t2.check_time, t2.cust_name, t2.cust_id, t2.id_card, t2.item_type, t2.item_name, t2.val_desc, t2.level, t2.descript from (")
                .append(mSql).append(") as t2 order by t2.check_time desc limit ").append(pageSize * (page - 1))
                .append(", ").append(pageSize);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("check_time", Hibernate.TIMESTAMP).addScalar("cust_name", Hibernate.STRING)
                .addScalar("cust_id", Hibernate.STRING).addScalar("id_card", Hibernate.STRING)
                .addScalar("item_type", Hibernate.STRING).addScalar("item_name", Hibernate.STRING)
                .addScalar("val_desc", Hibernate.STRING).addScalar("level", Hibernate.INTEGER)
                .addScalar("descript", Hibernate.STRING).list();
        List<CheckData> list = new ArrayList<CheckData>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            CheckData checkData = new CheckData();
            idx = 0;
            checkData.setId((String) objs[idx]);
            idx++;
            checkData.setCheckTime((Timestamp) objs[idx]);
            idx++;
            checkData.setCustName((String) objs[idx]);
            idx++;
            checkData.setCustId((String) objs[idx]);
            idx++;
            checkData.setIdCard((String) objs[idx]);
            idx++;
            checkData.setItemType((String) objs[idx]);
            idx++;
            checkData.setItemName((String) objs[idx]);
            idx++;
            checkData.setValDesc((String) objs[idx]);
            idx++;
            checkData.setLevel((Integer) objs[idx]);
            idx++;
            checkData.setDescribe((String) objs[idx]);
            idx++;
            list.add(checkData);
        }
        pageModel.setTotal(totalCount);
        pageModel.setData(list);
        return pageModel;
    }

}
