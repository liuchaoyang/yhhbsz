package com.yzxt.yh.module.chk.dao;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.yh.constant.ConstCheckData;
import com.yzxt.yh.module.chk.bean.AnalysisUricAcid;
import com.yzxt.yh.module.chk.bean.BloodOxygen;
import com.yzxt.yh.module.chk.bean.BloodSugar;
import com.yzxt.yh.module.chk.bean.BodyFat;
import com.yzxt.yh.module.chk.bean.PhysiologIndex;
import com.yzxt.yh.module.chk.bean.PressurePulse;
import com.yzxt.yh.module.chk.bean.Pulse;
import com.yzxt.yh.module.chk.bean.Temperature;
import com.yzxt.yh.module.chk.bean.TotalCholesterol;
import com.yzxt.yh.module.chk.bean.UricAcid;
import com.yzxt.yh.module.sys.bean.Customer;

/**
 * 用户生理指标
 */
public class PhysiologIndexDao extends HibernateSupport<PhysiologIndex> implements BaseDao<PhysiologIndex>
{
    /**
     * 添加用户指标
     * @param physiologicalIndex
     * @return
     * @throws Exception
     */
    public Integer insert(PhysiologIndex physiologIndex) throws Exception
    {
        super.save(physiologIndex);
        return 1;
    }

    /**
     * 根据血压检测数据更新
     */
    public int update(PressurePulse pressurePulse)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update PhysiologIndex set sbp = ?, dbp = ?");
        params.add(pressurePulse.getSBP(), Hibernate.INTEGER);
        params.add(pressurePulse.getDBP(), Hibernate.INTEGER);
        if (pressurePulse.getPulse() != null && pressurePulse.getPulse().intValue() > 0)
        {
            sql.append(", pulse = ?");
            params.add(pressurePulse.getPulse(), Hibernate.INTEGER);
        }
        sql.append(", lastCheckItem = ?, lastCheckTime = ?");
        params.add("xy", Hibernate.STRING);
        params.add(pressurePulse.getCheckTime(), Hibernate.TIMESTAMP);
        sql.append(" where custId = ?");
        params.add(pressurePulse.getCustId(), Hibernate.STRING);
        return super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

    /**
     * 根据血糖检测数据更新
     */
    public int update(BloodSugar bloodSugar)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        int itemCount = 0;
        sql.append("update PhysiologIndex set");
        // 血糖类型,1：空腹血糖,2：餐前血糖,3：餐后血糖,4：服糖2小时血糖。
        int type = bloodSugar.getBloodSugarType() != null ? bloodSugar.getBloodSugarType().intValue() : 0;
        if (type == 1 || type == 2)
        {
            sql.append(" fpg = ?");
            params.add(bloodSugar.getBloodSugar(), Hibernate.DOUBLE);
            itemCount++;
        }
        else if (type == 3)
        {
            sql.append(" h2pbg = ?");
            params.add(bloodSugar.getBloodSugar(), Hibernate.DOUBLE);
            itemCount++;
        }
        else if (type == 4)
        {
            sql.append(" l2sugar = ?");
            params.add(bloodSugar.getBloodSugar(), Hibernate.DOUBLE);
            itemCount++;
        }
        sql.append(", lastCheckItem = ?, lastCheckTime = ?");
        params.add("xt", Hibernate.STRING);
        params.add(bloodSugar.getCheckTime(), Hibernate.TIMESTAMP);
        sql.append(" where custId = ?");
        params.add(bloodSugar.getCustId(), Hibernate.STRING);
        // 如果类型无效
        if (itemCount == 0)
        {
            return 0;
        }
        return super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

    /**
     * 根据脉搏检测数据更新
     */
    public int update(Pulse pulse)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update PhysiologIndex set pulse = ?, lastCheckItem = ?, lastCheckTime = ?");
        params.add(pulse.getPulse(), Hibernate.INTEGER);
        params.add("xl", Hibernate.STRING);
        params.add(pulse.getCheckTime(), Hibernate.TIMESTAMP);
        sql.append(" where custId = ?");
        params.add(pulse.getCustId(), Hibernate.STRING);
        return super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

    /**
     * 根据血氧检测数据更新
     */
    public int update(BloodOxygen bloodOxygen)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update PhysiologIndex set bo = ?");
        params.add(bloodOxygen.getBO(), Hibernate.INTEGER);
        if (bloodOxygen.getPulse() != null && bloodOxygen.getPulse().intValue() > 0)
        {
            sql.append(", pulse = ?");
            params.add(bloodOxygen.getPulse(), Hibernate.INTEGER);
        }
        sql.append(", lastCheckItem = ?, lastCheckTime = ?");
        params.add("xo", Hibernate.STRING);
        params.add(bloodOxygen.getCheckTime(), Hibernate.TIMESTAMP);
        sql.append(" where custId = ?");
        params.add(bloodOxygen.getCustId(), Hibernate.STRING);
        return super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

    /**
     * 根据体温检测数据更新
     */
    public int update(Temperature temperature)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update PhysiologIndex set temperature = ?, lastCheckItem = ?, lastCheckTime = ?");
        params.add(temperature.getTemperature(), Hibernate.DOUBLE);
        params.add("tw", Hibernate.STRING);
        params.add(temperature.getCheckTime(), Hibernate.TIMESTAMP);
        sql.append(" where custId = ?");
        params.add(temperature.getCustId(), Hibernate.STRING);
        return super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

    /**
     * 根据总胆固醇检测数据更新
     */
    public int update(TotalCholesterol totalCholesterol)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update PhysiologIndex set totalCholesterol = ?, lastCheckItem = ?, lastCheckTime = ?");
        params.add(totalCholesterol.getTotalCholesterol(), Hibernate.DOUBLE);
        params.add("dgc", Hibernate.STRING);
        params.add(totalCholesterol.getCheckTime(), Hibernate.TIMESTAMP);
        sql.append(" where custId = ?");
        params.add(totalCholesterol.getCustId(), Hibernate.STRING);
        return super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

    /**
     * 根据尿酸检测数据更新
     */
    public int update(UricAcid uricAcid)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update PhysiologIndex set uricAcid = ?, lastCheckItem = ?, lastCheckTime = ?");
        params.add(uricAcid.getUricAcid(), Hibernate.DOUBLE);
        params.add("ns", Hibernate.STRING);
        params.add(uricAcid.getCheckTime(), Hibernate.TIMESTAMP);
        sql.append(" where custId = ?");
        params.add(uricAcid.getCustId(), Hibernate.STRING);
        return super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

    /**
     * 根据尿常规检测数据更新
     */
    public int update(AnalysisUricAcid analysisUricAcid)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update PhysiologIndex set lastCheckItem = ?, lastCheckTime = ?");
        params.add("nsfx", Hibernate.STRING);
        params.add(analysisUricAcid.getCheckTime(), Hibernate.TIMESTAMP);
        sql.append(" where custId = ?");
        params.add(analysisUricAcid.getCustId(), Hibernate.STRING);
        return super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

    /**
     * 根据用户表更新生理指标表
     */
    public int update(Customer cust)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update PhysiologIndex set ");
        int count = 0;
        if (cust.getHeight() != null && cust.getHeight().doubleValue() > 0)
        {
            sql.append(" height = ?");
            params.add(cust.getHeight(), Hibernate.DOUBLE);
            count++;
        }
        if (cust.getWeight() != null && cust.getWeight().doubleValue() > 0)
        {
            if (count > 0)
            {
                sql.append(", ");
            }
            sql.append(" weight = ?");
            params.add(cust.getWeight(), Hibernate.DOUBLE);
            count++;
        }
        if (count == 0)
        {
            return 0;
        }
        sql.append(" where custId = ?");
        params.add(cust.getUserId(), Hibernate.STRING);
        return super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

    /**
     * 根据体脂检测数据更新
     */
    public int update(BodyFat bodyFat)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update PhysiologIndex set weight = ?, lastCheckItem = ?, lastCheckTime = ?");
        /*params.add(bodyFat.getHeight(), Hibernate.DOUBLE);*/
        params.add(bodyFat.getWeight(), Hibernate.DOUBLE);
        params.add(ConstCheckData.BFR, Hibernate.STRING);
        params.add(bodyFat.getCheckTime(), Hibernate.TIMESTAMP);
        sql.append(" where custId = ?");
        params.add(bodyFat.getCustId(), Hibernate.STRING);
        return super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }
}
