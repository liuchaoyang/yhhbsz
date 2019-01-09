package com.yzxt.yh.module.rpt.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.chk.dao.BloodSugarDao;
import com.yzxt.yh.module.chk.dao.PressurePulseDao;
import com.yzxt.yh.module.rpt.bean.AnalysisReport;
import com.yzxt.yh.module.rpt.dao.AnalysisReportDao;
import com.yzxt.yh.module.sys.bean.Customer;
import com.yzxt.yh.module.sys.service.CustomerService;

@Transactional(ConstTM.DEFAULT)
public class AnalysisReportService
{
    private AnalysisReportDao analysisReportDao;

    private CustomerService customerService;

    private PressurePulseDao pressurePulseDao;

    private BloodSugarDao bloodSugarDao;

    public AnalysisReportDao getAnalysisReportDao()
    {
        return analysisReportDao;
    }

    public void setAnalysisReportDao(AnalysisReportDao analysisReportDao)
    {
        this.analysisReportDao = analysisReportDao;
    }

    public CustomerService getCustomerService()
    {
        return customerService;
    }

    public void setCustomerService(CustomerService customerService)
    {
        this.customerService = customerService;
    }

    public PressurePulseDao getPressurePulseDao()
    {
        return pressurePulseDao;
    }

    public void setPressurePulseDao(PressurePulseDao pressurePulseDao)
    {
        this.pressurePulseDao = pressurePulseDao;
    }

    public BloodSugarDao getBloodSugarDao()
    {
        return bloodSugarDao;
    }

    public void setBloodSugarDao(BloodSugarDao bloodSugarDao)
    {
        this.bloodSugarDao = bloodSugarDao;
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(AnalysisReport report) throws Exception{
    	analysisReportDao.insert(report);
    }
 
    /**
     * 自动生成健康分析报告
     * @param idCard
     * @param startDate
     * @param endDate
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void doGenReport(AnalysisReport report) throws Exception
    {
        Timestamp nowXy = new Timestamp(System.currentTimeMillis());
        // 过滤条件
        Map<String, Object> filter = new HashMap<String, Object>();
        String custId = report.getCustId();
        Date startDate = report.getExamBeginTime();
        Date endDate = report.getExamEndTime();
        filter.put("custId", custId);
        filter.put("startDate", startDate);
        filter.put("endDate", endDate);
        // -----开始产生血压分析报告-----
        int bloodPressureDataCount = pressurePulseDao.queryDataCount(filter);
        // 查询血压数据是否存在
        // if (bloodPressureDataCount > 0)
        // {
        // 查询次期间血压报告是否存在
        filter.put("reportType", Constant.ANALYSIS_REPORT_TYPE_BLOOD_PRESSURE);
        int existBloodPressureReportCount = analysisReportDao.queryReportCount(filter);
        if (existBloodPressureReportCount <= 0)
        {
            // 保存血压分析报告
            AnalysisReport rpt = new AnalysisReport();
            rpt.setCustId(custId);
            rpt.setExamBeginTime(startDate);
            rpt.setExamEndTime(endDate);
            rpt.setReportType(Constant.ANALYSIS_REPORT_TYPE_BLOOD_PRESSURE);
            rpt.setCreateTime(nowXy);
            rpt.setUpdateTime(nowXy);
            rpt.setSuggest(bloodPressureDataCount > 0 ? null : "请坚持测量，以保证生成的报告的有效性。");
            analysisReportDao.insert(rpt);
            
        }
        // }
        Timestamp nowXt = new Timestamp(System.currentTimeMillis());
        // -----开始产生血糖分析报告-----
        int bloodSugarDataCount = bloodSugarDao.queryDataCount(filter);
        // 查询血糖数据是否存在
        // if (bloodSugarDataCount > 0)
        // {
        // 查询次期间血糖报告是否存在
        filter.put("reportType", Constant.ANALYSIS_REPORT_TYPE_BLOOD_SUGAR);
        int existBloodSugarReportCount = analysisReportDao.queryReportCount(filter);
        if (existBloodSugarReportCount <= 0)
        {
            // 保存血糖分析报告
            AnalysisReport rpt = new AnalysisReport();
            rpt.setCustId(custId);
            rpt.setExamBeginTime(startDate);
            rpt.setExamEndTime(endDate);
            rpt.setReportType(Constant.ANALYSIS_REPORT_TYPE_BLOOD_SUGAR);
            rpt.setCreateTime(nowXt);
            rpt.setUpdateTime(nowXt);
            rpt.setSuggest(bloodSugarDataCount > 0 ? null : "请坚持测量，以保证生成的报告的有效性。");
            analysisReportDao.insert(rpt);
        }
        // }
    }

    /**
     * 加载分析报告信息
     * @param id
     * @return
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public AnalysisReport load(String id) throws Exception
    {
        AnalysisReport report = analysisReportDao.get(id);
        // 客户信息
        if (report != null)
        {
            Customer cust = customerService.load(report.getCustId());
            if (cust != null)
            {
                report.setCustomerName(cust.getUser().getName());
                report.setIdCard(cust.getUser().getIdCard());
                report.setSex(cust.getSex());
                report.setBirthday(cust.getBirthday());
            }
        }
        // 报表数据
        if (Constant.ANALYSIS_REPORT_TYPE_BLOOD_PRESSURE.equals(report.getReportType()))
        {
            Object[] dataObjs = pressurePulseDao.getAnaRptData(report.getCustId(), report.getExamBeginTime(),
                    report.getExamEndTime());
            List<Object[]> list = (List<Object[]>) dataObjs[0];
            int c = list.size();
            int[] dbps = new int[c];
            int[] sbps = new int[c];
            Timestamp[] cts = new Timestamp[c];
            int[] pulse = new int[c];
            String[] preId = new String[c];
            // 饼图值，分别为正常血压、正常高压、一级高血压、二级高血压、三级高血压
            int[] p = new int[5];
            // 血压判断标准
            // 正常血压，收缩压<120，舒张压<80。
            // 正常高值，收缩压120～139，舒张压80～89。
            // 高血压，收缩压≥140，舒张压≥90。
            // 1级高血压（轻度），收缩压140～159 ，舒张压90～99。
            // 2级高血压（中度），收缩压160～179 ，舒张压100～109。
            // 3级高血压（重度），收缩压≥180，舒张压≥110。
            // 单纯收缩期高血压，收缩压≥140，舒张压<90。
            // 如患者的收缩压与舒张压分属不同的级别时，则以较高的分级标准为准。单纯收缩期高血压也可按照收缩压水平分为1、2、3级。
            if (c > 0)
            {
                int spos = 0;
                int dpos = 0;
                for (int i = 0; i < c; i++)
                {
                    Object[] obj = list.get(i);
                    dbps[i] = (Integer) obj[0];
                    sbps[i] = (Integer) obj[1];
                    cts[i] = (Timestamp) obj[2];
                    pulse[i] = (Integer) obj[3];
                    preId[i] = (String) obj[4];
                    if (dbps[i] >= 110)
                    {
                        dpos = 4;
                    }
                    else if (sbps[i] >= 100)
                    {
                        dpos = 3;
                    }
                    else if (dbps[i] >= 90)
                    {
                        dpos = 2;
                    }
                    else if (dbps[i] >= 80)
                    {
                        dpos = 1;
                    }
                    else
                    {
                        dpos = 0;
                    }
                    if (sbps[i] >= 180)
                    {
                        spos = 4;
                    }
                    else if (sbps[i] >= 160)
                    {
                        spos = 3;
                    }
                    else if (sbps[i] >= 140)
                    {
                        spos = 2;
                    }
                    else if (sbps[i] >= 120)
                    {
                        spos = 1;
                    }
                    else
                    {
                        spos = 0;
                    }
                    p[Math.max(dpos, spos)]++;
                }
            }
            report.setData(new Object[]
            {dbps, sbps, cts,pulse,preId, p, dataObjs[1]});
        }
        else if (Constant.ANALYSIS_REPORT_TYPE_BLOOD_SUGAR.equals(report.getReportType()))
        {
            Object[] dataObjs = bloodSugarDao.getAnaRptData(report.getCustId(), report.getExamBeginTime(),
                    report.getExamEndTime());
            List<Object[]> list = (List<Object[]>) dataObjs[0];
            int c = list.size();
            // 血糖判断标准
            // 1.有糖尿病的症状，任何时间的静脉血浆葡萄糖浓度≥ 11.1 mmol/L （ 200mg/dl ）。
            // 2.空腹静脉血浆葡萄糖浓度≥ 7.0 mmol/L （ 126mg/dl ）。
            // 3.糖耐量试验（ OGTT ）口服 75g 葡萄糖后 2 小时静脉血浆葡萄糖浓度≥ 11.1 mmol/L 。
            // 任何时候血糖低于2.8 mmol/L为低血糖。
            // 线图数据，分别为空腹血糖、餐后血糖，服糖后血糖
            List<Object[]>[] linkData = new List[]
            {new ArrayList<Object[]>(), new ArrayList<Object[]>(), new ArrayList<Object[]>(), list};
            // 饼图数据，分别为正常数据，高血糖，低血糖
            int[] p = new int[3];
            // 统计表数据
            // 分别为测量次数、高压次数、低于次数
            int[][] tStatCount = new int[3][3];
            // 分别为最小值，最大值，平均值
            double[][] tStatVal = new double[3][3];
            if (c > 0)
            {
                for (int i = 0; i < c; i++)
                {
                    Object[] objs = list.get(i);
                    Integer type = (Integer) objs[0];
                    Double bloodSugar = (Double) objs[1];
                    // 空腹血糖
                    if (type == 1 || type == 2)
                    {
                        if (tStatVal[0][0] > 0)
                        {
                            tStatVal[0][0] = Math.min(tStatVal[0][0], bloodSugar);
                        }
                        else
                        {
                            tStatVal[0][0] = bloodSugar;
                        }
                        tStatVal[0][1] = Math.max(tStatVal[0][1], bloodSugar);
                        tStatVal[0][2] += bloodSugar;
                        tStatCount[0][0]++;
                        if (bloodSugar >= 7.0)
                        {
                            tStatCount[0][1]++;
                            p[1]++;
                        }
                        else if (bloodSugar < 2.8)
                        {
                            tStatCount[0][2]++;
                            p[2]++;
                        }
                        else
                        {
                            p[0]++;
                        }
                        linkData[0].add(objs);
                    }
                    else if (type == 3)
                    {
                        if (tStatVal[1][0] > 0)
                        {
                            tStatVal[1][0] = Math.min(tStatVal[1][0], bloodSugar);
                        }
                        else
                        {
                            tStatVal[1][0] = bloodSugar;
                        }
                        tStatVal[1][1] = Math.max(tStatVal[1][1], bloodSugar);
                        tStatVal[1][2] += bloodSugar;
                        tStatCount[1][0]++;
                        if (bloodSugar >= 7.0)
                        {
                            tStatCount[1][1]++;
                            p[1]++;
                        }
                        else if (bloodSugar < 2.8)
                        {
                            tStatCount[1][2]++;
                            p[2]++;
                        }
                        else
                        {
                            p[0]++;
                        }
                        linkData[1].add(objs);
                    }
                    else
                    {
                        if (tStatVal[2][0] > 0)
                        {
                            tStatVal[2][0] = Math.min(tStatVal[2][0], bloodSugar);
                        }
                        else
                        {
                            tStatVal[2][0] = bloodSugar;
                        }
                        tStatVal[2][1] = Math.max(tStatVal[2][1], bloodSugar);
                        tStatVal[2][2] += bloodSugar;
                        tStatCount[2][0]++;
                        if (bloodSugar >= 11.1)
                        {
                            tStatCount[2][1]++;
                            p[1]++;
                        }
                        else if (bloodSugar < 2.8)
                        {
                            tStatCount[2][2]++;
                            p[2]++;
                        }
                        else
                        {
                            p[0]++;
                        }
                        linkData[2].add(objs);
                    }
                }
                //处理平均值
                tStatVal[0][2] = tStatCount[0][0] > 0 ? tStatVal[0][2] / tStatCount[0][0] : 0d;
                tStatVal[1][2] = tStatCount[1][0] > 0 ? tStatVal[1][2] / tStatCount[1][0] : 0d;
                tStatVal[2][2] = tStatCount[2][0] > 0 ? tStatVal[2][2] / tStatCount[2][0] : 0d;
            }
            report.setData(new Object[]
            {linkData, p, tStatCount, tStatVal});
        }
        return report;
    }

    /**
     * 保存分析报告评估建议
     * @param report
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result saveSuggest(AnalysisReport report) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        report.setUpdateTime(now);
        analysisReportDao.saveSuggest(report);
        return new Result(Result.STATE_SUCESS, "保存成功。", null);
    }

    /**
     * 分页查询分析报告数据
     * @param filter
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<AnalysisReport> query(Map<String, Object> filter, int page, int pageSize)
    {
        return analysisReportDao.query(filter, page, pageSize);
    }

    /**
     * 客户端分页查询分析报告数据
     * @param filter
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageTran<AnalysisReport> queryMyTran(Map<String, Object> filter, Timestamp sysTime, int dir, int count)
    {
        return analysisReportDao.queryMyTran(filter, sysTime, dir, count);
    }

}
