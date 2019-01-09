package com.yzxt.tran;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.common.ResultTran;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.fw.util.GsonUtil;
import com.yzxt.yh.module.rpt.bean.AnalysisReport;
import com.yzxt.yh.module.rpt.bean.CheckReport;
import com.yzxt.yh.module.rpt.service.AnalysisReportService;
import com.yzxt.yh.module.rpt.service.CheckReportService;
import com.yzxt.yh.module.sys.bean.Customer;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.service.CustomerService;
import com.yzxt.yh.module.sys.service.UserService;
import com.yzxt.yh.sch.job.AnalysisReportJob;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.DecimalUtil;
import com.yzxt.yh.util.FileUtil;
import com.yzxt.yh.util.StringUtil;

public class AnalysisReportTranAction extends BaseTranAction
{
    private Logger logger = Logger.getLogger(AnalysisReportTranAction.class);

    private static SimpleDateFormat simpleDf = new SimpleDateFormat("yyyy/MM/dd");

    private static final long serialVersionUID = 1L;

    private AnalysisReportService analysisReportService;

    private CheckReportService checkReportService;

    private CustomerService customerService;
    
    private UserService userService;

    public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public AnalysisReportService getAnalysisReportService()
    {
        return analysisReportService;
    }

    public void setAnalysisReportService(AnalysisReportService analysisReportService)
    {
        this.analysisReportService = analysisReportService;
    }

    public CheckReportService getCheckReportService()
    {
        return checkReportService;
    }

    public void setCheckReportService(CheckReportService checkReportService)
    {
        this.checkReportService = checkReportService;
    }

    public CustomerService getCustomerService()
    {
        return customerService;
    }

    public void setCustomerService(CustomerService customerService)
    {
        this.customerService = customerService;
    }

    /**
     * 报告列表
     * @return
     */
    public void list()
    {
        try
        {
            JsonObject obj = super.getParams();
            initQuery(obj);
            Map<String, Object> filter = new HashMap<String, Object>();
            // 客户ID
            JsonElement cObj = obj.get("custId");
            String custId = obj.get("custId").getAsString();
            if (cObj != null)
            {
                filter.put("custId", custId);
            }
            JsonElement typeObj = obj.get("type");
            if (typeObj != null && StringUtil.isNotEmpty(typeObj.getAsString()))
            {
                filter.put("reportType", typeObj.getAsInt());
            }
            PageTran<AnalysisReport> pageTran = analysisReportService.queryMyTran(filter, synTime, synType, pageSize);
            User user = userService.getUser(custId);
            JsonArray jArray = new JsonArray();
            List<AnalysisReport> list = pageTran.getData();
            JsonObject jObj = null;
            for (AnalysisReport rpt : list)
            {
                jObj = new JsonObject();
                jObj.addProperty("reportId", rpt.getId());
                jObj.addProperty("type", rpt.getReportType());
                jObj.addProperty("stEdTime",
                        simpleDf.format(rpt.getExamBeginTime()) + "-" + simpleDf.format(rpt.getExamEndTime()));
                jObj.addProperty("createTime", DateUtil.toHtmlDate(rpt.getCreateTime()));
                jObj.addProperty("uploadTime", DateUtil.getTranTime(rpt.getCreateTime()));
                jObj.addProperty("name",user.getName());
                jObj.addProperty("cardNo",user.getIdCard());
                jObj.addProperty("sex",user.getSex());
                jObj.addProperty("age", DateUtil.getAgeByIDNumber(user.getIdCard()));
                jArray.add(jObj);
            }
            super.write(ResultTran.STATE_OK, null, jArray);
        }
        catch (Exception e)
        {
            logger.error("查询分析报告列表错误。", e);
            super.write(ResultTran.STATE_ERROR, "查询分析报告列表错误。");
        }
    }

    /**
     * 报告详细
     * @return
     */
    @SuppressWarnings("unchecked")
    public void view()
    {
        try
        {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            JsonObject obj = super.getParams();
            String id = obj.get("reportId").getAsString();
            AnalysisReport report = analysisReportService.load(id);
            if (report == null)
            {
                super.write(ResultTran.STATE_ERROR, "查看的分析报告不存在。");
                logger.error("分析报告不存在，报告ID：" + id);
                return;
            }
            Customer cust = customerService.get(report.getCustId());
            Object[] data = report.getData();
            JsonObject jsonObj = new JsonObject();
            // 血压报告
            if (report.getReportType() == 1)
            {
                // 分时段统计数据
                Integer[][] tStat = (Integer[][]) data[6];
                int[] dbps = (int[]) data[0];
                int[] sbps = (int[]) data[1];
                Timestamp[] cts = (Timestamp[]) data[2];
                int[] pulse = (int[]) data[3];
                String[] preId = (String[]) data[4];
                int total = dbps.length;
                JsonArray linkJsonArray = new JsonArray();
                for (int i = 0; i < total; i++)
                {
                    JsonObject linkJson = new JsonObject();
                    linkJson.addProperty("id", preId[i]);
                    linkJson.addProperty("dbp", dbps[i]);
                    linkJson.addProperty("sbp", sbps[i]);
                    linkJson.addProperty("pulse", pulse[i]);
                    if (cts[i] != null)
                    {
                        linkJson.addProperty("checkTime", df.format(cts[i]));
                    }
                    
                    linkJsonArray.add(linkJson);
                }
                List<String[]> datas = new ArrayList<String[]>();
                datas.add(new String[]
                {"收缩压", tStat[0][0] + "", DecimalUtil.toIntegerString(tStat[0][4]),
                        DecimalUtil.toIntegerString(tStat[0][5]), DecimalUtil.toIntegerString(tStat[0][6])});
                datas.add(new String[]
                {"收缩压", tStat[1][0] + "", DecimalUtil.toIntegerString(tStat[1][4]),
                        DecimalUtil.toIntegerString(tStat[1][5]), DecimalUtil.toIntegerString(tStat[1][6])});
                datas.add(new String[]
                {"收缩压", tStat[2][0] + "", DecimalUtil.toIntegerString(tStat[2][4]),
                        DecimalUtil.toIntegerString(tStat[2][5]), DecimalUtil.toIntegerString(tStat[2][6])});
                datas.add(new String[]
                {"收缩压", tStat[3][0] + "", DecimalUtil.toIntegerString(tStat[3][4]),
                        DecimalUtil.toIntegerString(tStat[3][5]), DecimalUtil.toIntegerString(tStat[3][6])});
                datas.add(new String[]
                {"舒张压", tStat[0][0] + "", DecimalUtil.toIntegerString(tStat[0][1]),
                        DecimalUtil.toIntegerString(tStat[0][2]), DecimalUtil.toIntegerString(tStat[0][3])});
                datas.add(new String[]
                {"舒张压", tStat[1][0] + "", DecimalUtil.toIntegerString(tStat[1][1]),
                        DecimalUtil.toIntegerString(tStat[1][2]), DecimalUtil.toIntegerString(tStat[1][3])});
                datas.add(new String[]
                {"舒张压", tStat[2][0] + "", DecimalUtil.toIntegerString(tStat[2][1]),
                        DecimalUtil.toIntegerString(tStat[2][2]), DecimalUtil.toIntegerString(tStat[2][3])});
                datas.add(new String[]
                {"舒张压", tStat[3][0] + "", DecimalUtil.toIntegerString(tStat[3][1]),
                        DecimalUtil.toIntegerString(tStat[3][2]), DecimalUtil.toIntegerString(tStat[3][3])});
                jsonObj.add("xAxis", gson.toJsonTree(new String[]
                {"统计时间", "类型", "测量次数", "最小值", "最大值", "平均值"}));
                jsonObj.add("yAxis", gson.toJsonTree(new String[]
                {"早间(06:00-09:59)", "白天(10:00-18:59)", "晚间(19:00-05:59)", "全天(00:00-23:59)"}));
                jsonObj.addProperty("suggest", report.getSuggest());
                jsonObj.add("datas", gson.toJsonTree(datas));
                jsonObj.add("bpDatas", linkJsonArray);
              /*  jsonObj.addProperty("sex", cust.getSex());
                jsonObj.addProperty("name", cust.getUser().getName());
                jsonObj.addProperty("cardNo",cust.getUser().getIdCard());
                jsonObj.addProperty("birthday", cust.getBirthday() != null ? df.format(cust.getBirthday()) : "");*/
            }
            else if (report.getReportType() == 2)
            // 血糖报告
            {
                List<Object[]> linkData = ((List<Object[]>[]) data[0])[3];
                int[][] tStatCount = (int[][]) data[2];
                double[][] tStatVal = (double[][]) data[3];
                jsonObj.add("xAxis", gson.toJsonTree(new String[]
                {"测量方式", "测量次数", "最小值", "最大值", "平均值", "高于正常值次数", "低于正常值次数"}));
                jsonObj.add("yAxis", gson.toJsonTree(new String[]
                {"空腹血糖测量", "餐后血糖测量", "服糖2小时后血糖测量"}));
                jsonObj.addProperty("suggest", report.getSuggest());
                // 统计表
                String[] kfDatas = new String[]
                {tStatCount[0][0] + "", DecimalUtil.toString(tStatVal[0][1]), DecimalUtil.toString(tStatVal[0][0]),
                        DecimalUtil.toString(tStatVal[0][2]), tStatCount[0][1] + "", tStatCount[0][2] + ""};
                String[] chDatas = new String[]
                {tStatCount[1][0] + "", DecimalUtil.toString(tStatVal[1][1]), DecimalUtil.toString(tStatVal[1][0]),
                        DecimalUtil.toString(tStatVal[1][2]), tStatCount[1][1] + "", tStatCount[1][2] + ""};
                String[] ftDatas = new String[]
                {tStatCount[2][0] + "", DecimalUtil.toString(tStatVal[2][1]), DecimalUtil.toString(tStatVal[2][0]),
                        DecimalUtil.toString(tStatVal[2][2]), tStatCount[2][1] + "", tStatCount[2][2] + ""};
                JsonArray arr = new JsonArray();
                arr.add(gson.toJsonTree(kfDatas));
                arr.add(gson.toJsonTree(chDatas));
                arr.add(gson.toJsonTree(ftDatas));
                jsonObj.add("datas", arr);
                // 血糖序列值
                JsonArray linkJsonArray = new JsonArray();
                if (linkData != null && linkData.size() > 0)
                {
                    for (Object[] objs : linkData)
                    {
                        JsonObject linkJson = new JsonObject();
                        linkJson.addProperty("bloodSugar", DecimalUtil.toString((Double) objs[1]));
                        linkJson.addProperty("bloodSugarType", (Integer) objs[0]);
                        Timestamp checkTime = (Timestamp) objs[2];
                        if (checkTime != null)
                        {
                            linkJson.addProperty("checkTime", df.format(checkTime));
                        }
                        linkJsonArray.add(linkJson);
                    }
                }
                jsonObj.add("bgDatas", linkJsonArray);
            }
            super.write(ResultTran.STATE_OK, null, jsonObj);
        }
        catch (Exception e)
        {
            logger.error("查看分析报告列表错误。", e);
            super.write(ResultTran.STATE_ERROR, "查看分析报告列表错误。");
        }
    }

    /**
     * 体检报告列表
     */
    public void listChk()
    {
        try
        {
            JsonObject obj = super.getParams();
            initQuery(obj);
            
            User operUser = super.getOperUser();
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("operUser", operUser);
            // 客户ID
            JsonElement cObj = obj.get("custId");
            if (cObj != null)
            {
                filter.put("custId", cObj.getAsString());
            }
            PageTran<CheckReport> pageTran = checkReportService.queryTran(filter, synTime, pageSize);
            List<CheckReport> list = pageTran.getData();
            JsonArray ja = new JsonArray();
            for (CheckReport cr : list)
            {
                JsonObject jo = new JsonObject();
                jo.addProperty("id", cr.getId());
                jo.addProperty("custName", cr.getCustName());
                if (StringUtil.isNotEmpty(cr.getReportFilePath()))
                {
                    jo.addProperty("reportFilePath", "pub/cf_img.do?pt=" + FileUtil.encodePath(cr.getReportFilePath()));
                }
                jo.addProperty("synTime", DateUtil.getTranTimestamp(cr.getCreateTime()));
                ja.add(jo);
            }
            super.write(ResultTran.STATE_OK, null, ja);
        }
        catch (Exception e)
        {
            logger.error("查询体检报告列表错误。", e);
            super.write(ResultTran.STATE_ERROR, "查询体检报告列表错误。");
        }
    }

    /**
     * 生成体检报告
     */
    public void genChk()
    {
        try
        {
            JsonObject obj = super.getParams();
            User user = (User) super.getOperUser();
            String custId = GsonUtil.toString(obj.get("custId"));
            CheckReport cr = new CheckReport();
            cr.setCustId(custId);
            cr.setCreateBy(user.getId());
            Result r = checkReportService.doGen(cr);
            JsonObject rObj = new JsonObject();
            if (r.getState() == Result.STATE_SUCESS)
            {
                rObj.addProperty("reportFilePath", "pub/cf_img.do?pt=" + FileUtil.encodePath((String) r.getData()));
                super.write(ResultTran.STATE_OK, null, rObj);
            }
            else
            {
                super.write(ResultTran.STATE_ERROR, "生成体检报告出错");
            }
        }
        catch (Exception e)
        {
            logger.error("生成体检报告错误。", e);
            super.write(ResultTran.STATE_ERROR, "生成体检报告错误。");
        }
    }
    /**
     * 
     * 生成分析报告
     */
    public void gen()
    {
        AnalysisReportJob job = new AnalysisReportJob();
        Date now = new Date();
        try
        {
            job.genMuchWeek(DateUtil.addDay(now, -7), DateUtil.addDay(now, 7));
        }
        catch (Exception e)
        {
            logger.error("临时生成分析报告出错。", e);
        }
    }
}
