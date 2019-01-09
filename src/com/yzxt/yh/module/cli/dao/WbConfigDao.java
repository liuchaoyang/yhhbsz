package com.yzxt.yh.module.cli.dao;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.yh.module.chk.bean.DeviceConfig;
import com.yzxt.yh.module.chk.bean.UserDevice;
import com.yzxt.yh.module.cli.bean.WbConfig;
import com.yzxt.yh.util.StringUtil;

public class WbConfigDao extends HibernateSupport<WbConfig> implements BaseDao<WbConfig>
{

    public String insert(WbConfig bean) throws Exception
    {
        super.save(bean);
        return bean.getId();
    }
    
    public int update(WbConfig bean) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update WbConfig set custId = ?,  deviceId = ? ");
        params.add(bean.getCustId(), Hibernate.STRING);
        params.add(bean.getDeviceId(), Hibernate.STRING);
        sql.append(", createBy = ?, createTime = ?, updateBy = ?, updateTime = ?");
        sql.append(" where id = ?");
        params.add(bean.getCreateBy(), Hibernate.STRING);
        params.add(bean.getCreateTime(), Hibernate.TIMESTAMP);
        params.add(bean.getUpdateBy(), Hibernate.STRING);
        params.add(bean.getUpdateTime(), Hibernate.TIMESTAMP);
        params.add(bean.getId(), Hibernate.STRING);
        return super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }
    
    public int updateId(String id, String deviceSn)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update cli_wb_config t set t.id = ? ");
        sql.append(" where t.id = ?");
        params.add(deviceSn, Hibernate.STRING);
        params.add(id, Hibernate.STRING);
        return super.getSession().createSQLQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }
    /**
     * 1.1.1.2.1 获取心率所有设置参数
     * @param filter
     * @param userId
     * @param custId 对应的设备绑定人的ID
     * @param deviceId 设备ID-对应一类设备
     * @param deviceCode 设备code-对应具体的一个设备
     * 一个人可以带多个腕表
     * 2015.11.25
     * @author h
     * */
    @SuppressWarnings("unchecked")
    public WbConfig queryConfig(Map<String, Object> filter)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id, t.cust_id, t.wb_name, t.device_id, t.state, t.img_file_id, t.sex ");
        sql.append(", t.step, t.birthday, t.height, t.weight, t.pluse_max, t.pluse_min");
        sql.append(", t.pluse_start_time, t.pluse_end_time, t.pluse_span, t.pluse_warning");
        sql.append(", t.step_target, t.step_start_time, t.step_end_time, t.step_span");
        sql.append(", t.position_start_time, t.position_end_time, t.position_span");
        sql.append(", t.sleep_start_time, t.sleep_end_time, t.sleep_status");
        sql.append(", t.xt_max_glucose, t.xt_min_glucose, t.xt_warning");
        sql.append(", t.xy_diastolic_max, t.xy_diastolic_min, t.xy_systolic_max, t.xy_systolic_min, t.xy_warning");
        sql.append(", t.tw_start_time, t.tw_end_time, t.tw_span, t.tw_max_temperature, t.tw_min_temperature, t.tw_warning");
        sql.append(", t.remind_list ");
        /*sql.append(", t.remind_content, t.remind_time, t.remind_status");*/
        sql.append(", t.phones, t.sitting_start_time, t.sitting_end_time");
        sql.append(", t.sitting_warning, t.SOS_nums, t.SOS_text");
        sql.append(", (select sfd.path from sys_file_desc sfd where sfd.id = t.img_file_id) as img_file_path");
        sql.append(", t.ele_latitude, t.ele_longitude, t.ele_distance");
        sql.append(", t.ele_address, t.ele_time ");
        sql.append(" from cli_wb_config t ");
        sql.append(" where 1= 1 and t.cust_id = ? and t.device_id = ? and t.id = ?");
        params.add(filter.get("custId"), Hibernate.STRING);
        params.add(filter.get("deviceId"), Hibernate.STRING);
        params.add(filter.get("deviceCode"), Hibernate.STRING);
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString())
                .addScalar("id", Hibernate.STRING).addScalar("cust_id", Hibernate.STRING)
                .addScalar("wb_name", Hibernate.STRING)
                .addScalar("device_id", Hibernate.STRING).addScalar("state", Hibernate.INTEGER)
                .addScalar("img_file_id", Hibernate.STRING).addScalar("sex", Hibernate.INTEGER)
                .addScalar("step", Hibernate.INTEGER).addScalar("birthday", Hibernate.DATE)
                .addScalar("height", Hibernate.DOUBLE).addScalar("weight", Hibernate.DOUBLE)
                .addScalar("pluse_max", Hibernate.INTEGER).addScalar("pluse_min", Hibernate.INTEGER)
                .addScalar("pluse_start_time", Hibernate.STRING).addScalar("pluse_end_time", Hibernate.STRING)
                .addScalar("pluse_span", Hibernate.INTEGER).addScalar("pluse_warning", Hibernate.INTEGER)
                .addScalar("step_target", Hibernate.INTEGER).addScalar("step_start_time", Hibernate.STRING)
                .addScalar("step_end_time", Hibernate.STRING).addScalar("step_span", Hibernate.INTEGER)
                .addScalar("position_start_time", Hibernate.STRING).addScalar("position_end_time", Hibernate.STRING)
                .addScalar("position_span", Hibernate.INTEGER).addScalar("sleep_start_time", Hibernate.STRING)
                .addScalar("sleep_end_time", Hibernate.STRING).addScalar("sleep_status", Hibernate.INTEGER)
                .addScalar("xt_max_glucose", Hibernate.FLOAT).addScalar("xt_min_glucose", Hibernate.FLOAT)
                .addScalar("xt_warning", Hibernate.INTEGER).addScalar("xy_diastolic_max", Hibernate.INTEGER)
                .addScalar("xy_diastolic_min", Hibernate.INTEGER).addScalar("xy_systolic_max", Hibernate.INTEGER)
                .addScalar("xy_systolic_min", Hibernate.INTEGER).addScalar("xy_warning", Hibernate.INTEGER)
                .addScalar("tw_start_time", Hibernate.STRING).addScalar("tw_end_time", Hibernate.STRING)
                .addScalar("tw_span", Hibernate.INTEGER).addScalar("tw_max_temperature", Hibernate.FLOAT)
                .addScalar("tw_min_temperature", Hibernate.FLOAT).addScalar("tw_warning", Hibernate.INTEGER)
                /*.addScalar("remind_content", Hibernate.STRING).addScalar("remind_time", Hibernate.STRING)*/
                .addScalar("remind_list", Hibernate.STRING).addScalar("phones", Hibernate.STRING)
                .addScalar("sitting_start_time", Hibernate.STRING).addScalar("sitting_end_time", Hibernate.STRING)
                .addScalar("sitting_warning", Hibernate.INTEGER).addScalar("SOS_nums", Hibernate.STRING)
                .addScalar("SOS_text", Hibernate.STRING).addScalar("img_file_path", Hibernate.STRING)
                .addScalar("ele_latitude", Hibernate.DOUBLE).addScalar("ele_longitude", Hibernate.DOUBLE)
                .addScalar("ele_distance", Hibernate.INTEGER).addScalar("ele_address", Hibernate.STRING)
                .addScalar("ele_time", Hibernate.DATE)
                .setParameters(params.getVals(), params.getTypes()).list();
        WbConfig wbConfig = null;
        if (objsList != null && objsList.size() > 0)
        {
            Object[] objs = objsList.get(0);
            wbConfig = new WbConfig();
            int i = 0;
            wbConfig.setId((String) objs[i]);
            i++;
            wbConfig.setCustId((String) objs[i]);
            i++;
            wbConfig.setWbName((String) objs[i]);
            i++;
            wbConfig.setDeviceId((String) objs[i]);
            i++;
            wbConfig.setState((Integer) objs[i]);
            i++;
            wbConfig.setImgFileId((String) objs[i]);
            i++;
            wbConfig.setSex((Integer) objs[i]);
            i++;
            wbConfig.setStep((Integer) objs[i]);
            i++;
            wbConfig.setBirthday((Date) objs[i]);
            i++;
            wbConfig.setHeight((Double) objs[i]);
            i++;
            wbConfig.setWeight((Double) objs[i]);
            i++;
            wbConfig.setPluseMax((Integer) objs[i]);
            i++;
            wbConfig.setPluseMin((Integer) objs[i]);
            i++;
            wbConfig.setPluseStartTime((String) objs[i]);
            i++;
            wbConfig.setPluseEndTime((String) objs[i]);
            i++;
            wbConfig.setPluseSpan((Integer) objs[i]);
            i++;
            wbConfig.setPluseWarning((Integer) objs[i]);
            i++;
            wbConfig.setStepTarget((Integer) objs[i]);
            i++;
            wbConfig.setStepStartTime((String) objs[i]);
            i++;
            wbConfig.setStepEndTime((String) objs[i]);
            i++;
            wbConfig.setStepSpan((Integer) objs[i]);
            i++;
            wbConfig.setPositionStartTime((String) objs[i]);
            i++;
            wbConfig.setPositionEndTime((String) objs[i]);
            i++;
            wbConfig.setPositionSpan((Integer) objs[i]);
            i++;
            wbConfig.setSleepStartTime((String) objs[i]);
            i++;
            wbConfig.setSleepEndTime((String) objs[i]);
            i++;
            wbConfig.setSleepStatus((Integer) objs[i]);
            i++;
            wbConfig.setXtMaxGlucose((Float) objs[i]);
            i++;
            wbConfig.setXtMinGlucose((Float) objs[i]);
            i++;
            wbConfig.setXtWarning((Integer) objs[i]);
            i++;
            wbConfig.setXyDiastolicMax((Integer) objs[i]);
            i++;
            wbConfig.setXyDiastolicMin((Integer) objs[i]);
            i++;
            wbConfig.setXySystolicMax((Integer) objs[i]);
            i++;
            wbConfig.setXySystolicMin((Integer) objs[i]);
            i++;
            wbConfig.setXyWarning((Integer) objs[i]);
            i++;
            wbConfig.setTwStartTime((String) objs[i]);
            i++;
            wbConfig.setTwEndTime((String) objs[i]);
            i++;
            wbConfig.setTwSpan((Integer) objs[i]);
            i++;
            wbConfig.setTwMaxTemperature((Float) objs[i]);
            i++;
            wbConfig.setTwMinTemperature((Float) objs[i]);
            i++;
            wbConfig.setTwWarning((Integer) objs[i]);
            i++;
            wbConfig.setRemindList((String) objs[i]);
            i++;
           /* wbConfig.setRemindContent((String) objs[i]);
            i++;
            wbConfig.setRemindTime((String) objs[i]);
            i++;
            wbConfig.setRemindStatus((Integer) objs[i]);
            i++;*/
            wbConfig.setPhones((String) objs[i]);
            i++;
            wbConfig.setSittingStartTime((String) objs[i]);
            i++;
            wbConfig.setSittingEndTime((String) objs[i]);
            i++;
            wbConfig.setSittingWarning((Integer) objs[i]);
            i++;
            wbConfig.setSosNums((String) objs[i]);
            i++;
            wbConfig.setSosText((String) objs[i]);
            i++;
            wbConfig.setImgFilePath((String) objs[i]);
            i++;
            wbConfig.setEleLatitude((Double) objs[i]);
            i++;
            wbConfig.setEleLongitude((Double) objs[i]);
            i++;
            wbConfig.setEleDistance((Integer) objs[i]);
            i++;
            wbConfig.setEleAddress((String) objs[i]);
            i++;
            wbConfig.setEleTime((Date) objs[i]);
            i++;
        }
        return wbConfig;
    }

    /**
     * 根据设备类型，设备标志 ,判断指定设备是否已经被人配置过信息
     * @param userDevice
     * @return
     * @throws Exception
     */
    public boolean isConfig(String wcId) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(t.id) as c from WbConfig t");
        sql.append(" where 1=1");
        sql.append(" and t.id = ? ");
        params.add(wcId, Hibernate.STRING);
        Object c = super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .uniqueResult();
        return Long.valueOf(c.toString()).longValue() > 0;
    }

    /**
     * 1.1.1.1.2    设置腕表身份信息
     * @param bean
     * 一个人可以带多个腕表
     * 2015.11.25
     * @author h
     * @throws Exception 
     * */
    public int updateWbCard(WbConfig bean)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update cli_wb_config t set t.sex = ?, t.height = ?, t.wb_name = ? ");
        sql.append(", t.weight = ?, t.step = ?, t.birthday = ?, t.img_file_id = ?");
        sql.append(" where t.id = ?");
        params.add(bean.getSex(), Hibernate.INTEGER);
        params.add(bean.getHeight(), Hibernate.DOUBLE);
        params.add(bean.getWbName(), Hibernate.STRING);
        params.add(bean.getWeight(), Hibernate.DOUBLE);
        params.add(bean.getStep(), Hibernate.INTEGER);
        params.add(bean.getBirthday(), Hibernate.DATE);
        params.add(bean.getImgFileId(), Hibernate.STRING);
        params.add(bean.getId(), Hibernate.STRING);
        return super.getSession().createSQLQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

    public int updateWbPluse(WbConfig bean)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update cli_wb_config t set t.pluse_start_time = ?, t.pluse_end_time = ? ");
        sql.append(", t.pluse_span = ? ");
        sql.append(" where t.id = ?");
        params.add(bean.getPluseStartTime(), Hibernate.STRING);
        params.add(bean.getPluseEndTime(), Hibernate.STRING);
        params.add(bean.getPluseSpan(), Hibernate.INTEGER);
        params.add(bean.getId(), Hibernate.STRING);
        return super.getSession().createSQLQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

    public int updateStepTarget(WbConfig bean)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update cli_wb_config t set t.step_target = ? ");
        sql.append(" where t.id = ?");
        params.add(bean.getStepTarget(), Hibernate.INTEGER);
        params.add(bean.getId(), Hibernate.STRING);
        return super.getSession().createSQLQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

    public int updateWbStep(WbConfig bean)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update cli_wb_config t set t.step_start_time = ?, t.step_end_time = ? ");
        sql.append(", t.step_span = ? ");
        sql.append(" where t.id = ?");
        params.add(bean.getStepStartTime(), Hibernate.STRING);
        params.add(bean.getStepEndTime(), Hibernate.STRING);
        params.add(bean.getStepSpan(), Hibernate.INTEGER);
        params.add(bean.getId(), Hibernate.STRING);
        return super.getSession().createSQLQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

    public int updatePositConfig(WbConfig bean)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update cli_wb_config t set t.position_start_time = ?, t.position_end_time = ? ");
        sql.append(", t.position_span = ? ");
        sql.append(" where t.id = ?");
        params.add(bean.getPositionStartTime(), Hibernate.STRING);
        params.add(bean.getPositionEndTime(), Hibernate.STRING);
        params.add(bean.getPositionSpan(), Hibernate.INTEGER);
        params.add(bean.getId(), Hibernate.STRING);
        return super.getSession().createSQLQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

    public int updateSleepConfig(WbConfig bean)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update cli_wb_config t set t.sleep_start_time = ?, t.sleep_end_time = ? ");
        sql.append(", t.sleep_status = ? ");
        sql.append(" where t.id = ?");
        params.add(bean.getSleepStartTime(), Hibernate.STRING);
        params.add(bean.getSleepEndTime(), Hibernate.STRING);
        params.add(bean.getSleepStatus(), Hibernate.INTEGER);
        params.add(bean.getId(), Hibernate.STRING);
        return super.getSession().createSQLQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

    public int updateRemindConfig(WbConfig bean)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update cli_wb_config t set t.remind_list = ? ");
        sql.append(" where t.id = ?");
        params.add(bean.getRemindList(), Hibernate.STRING);
        /*params.add(bean.getRemindContent(), Hibernate.STRING);
        params.add(bean.getRemindTime(), Hibernate.STRING);
        params.add(bean.getRemindStatus(), Hibernate.INTEGER);*/
        params.add(bean.getId(), Hibernate.STRING);
        return super.getSession().createSQLQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

    public int updateSOSInfo(WbConfig bean)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update cli_wb_config t set t.SOS_text = ? ");
        sql.append(" where t.id = ?");
        params.add(bean.getSosText(), Hibernate.STRING);
        params.add(bean.getId(), Hibernate.STRING);
        return super.getSession().createSQLQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

    public int updateSOSConfig(WbConfig bean)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update cli_wb_config t set t.SOS_nums = ? ");
        sql.append(" where t.id = ?");
        params.add(bean.getSosNums(), Hibernate.STRING);
        params.add(bean.getId(), Hibernate.STRING);
        return super.getSession().createSQLQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

    public int updatePhonesConfig(WbConfig bean)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update cli_wb_config t set t.phones = ? ");
        sql.append(" where t.id = ?");
        params.add(bean.getPhones(), Hibernate.STRING);
        params.add(bean.getId(), Hibernate.STRING);
        return super.getSession().createSQLQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

    public int updateEleFence(WbConfig bean)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update cli_wb_config t set t.ele_latitude = ?, t.ele_longitude = ?, t.ele_distance = ? ");
        sql.append(", t.ele_time = ?, t.ele_address = ? ");
        sql.append(" where t.id = ?");
        params.add(bean.getEleLatitude(), Hibernate.DOUBLE);
        params.add(bean.getEleLongitude(), Hibernate.DOUBLE);
        params.add(bean.getEleDistance(), Hibernate.INTEGER);
        params.add(bean.getEleTime(), Hibernate.DATE);
        params.add(bean.getEleAddress(), Hibernate.STRING);
        params.add(bean.getId(), Hibernate.STRING);
        return super.getSession().createSQLQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

}
