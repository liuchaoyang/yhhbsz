package com.yzxt.yh.module.rpt.service;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.yh.constant.ConstFilePath;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.chk.bean.AnalysisUricAcid;
import com.yzxt.yh.module.chk.bean.PhysiologIndex;
import com.yzxt.yh.module.chk.service.AnalysisUricAcidService;
import com.yzxt.yh.module.chk.service.PhysiologIndexService;
import com.yzxt.yh.module.rpt.bean.CheckReport;
import com.yzxt.yh.module.rpt.dao.CheckReportDao;
import com.yzxt.yh.module.svb.service.MemberInfoService;
import com.yzxt.yh.module.sys.bean.Customer;
import com.yzxt.yh.module.sys.bean.FileDesc;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.service.CustomerService;
import com.yzxt.yh.module.sys.service.FileDescService;
import com.yzxt.yh.module.sys.service.UserService;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.DecimalUtil;
import com.yzxt.yh.util.FileUtil;
import com.yzxt.yh.util.StringUtil;

@Transactional(ConstTM.DEFAULT)
public class CheckReportService
{
    private static SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");

    private CheckReportDao checkReportDao;
    private PhysiologIndexService physiologIndexService;
    private AnalysisUricAcidService analysisUricAcidService;
    private CustomerService customerService;
    private FileDescService fileDescService;
    private UserService userService;
    private MemberInfoService memberInfoService;

    public CheckReportDao getCheckReportDao()
    {
        return checkReportDao;
    }

    public void setCheckReportDao(CheckReportDao checkReportDao)
    {
        this.checkReportDao = checkReportDao;
    }

    public PhysiologIndexService getPhysiologIndexService()
    {
        return physiologIndexService;
    }

    public void setPhysiologIndexService(PhysiologIndexService physiologIndexService)
    {
        this.physiologIndexService = physiologIndexService;
    }

    public AnalysisUricAcidService getAnalysisUricAcidService()
    {
        return analysisUricAcidService;
    }

    public void setAnalysisUricAcidService(AnalysisUricAcidService analysisUricAcidService)
    {
        this.analysisUricAcidService = analysisUricAcidService;
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

    public UserService getUserService()
    {
        return userService;
    }

    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }

    public MemberInfoService getMemberInfoService()
    {
        return memberInfoService;
    }

    public void setMemberInfoService(MemberInfoService memberInfoService)
    {
        this.memberInfoService = memberInfoService;
    }

    /**
     * 生成体检报告
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result doGen(CheckReport checkReport) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String custId = checkReport.getCustId();
        Customer cust = customerService.load(custId);
        User user = cust.getUser();
        PhysiologIndex physiologIndex = physiologIndexService.get(custId);
        // 尿常规不在身体指标表中需要专门查询生理指标表
        AnalysisUricAcid analysisUricAcid = analysisUricAcidService.getLastestByCust(custId);
        // 生成图片
        BufferedImage bImg = ImageIO.read(CheckReportService.class
                .getResourceAsStream("/com/yzxt/yh/template/checkReport.jpg"));
        Graphics g = bImg.getGraphics();
        g.setColor(Color.black);
        Font font1 = new Font("serif", Font.PLAIN, 14);
        g.setFont(font1);
        int x1 = 105;
        // 姓名
        g.drawString(user.getName(), x1, 54);
        // 性别
        int sex = cust.getSex() != null ? cust.getSex().intValue() : 0;
        g.drawString(sex == 1 ? "男" : (sex == 2 ? "女" : ""), x1, 74);
        // 年龄
        Date birthday = cust.getBirthday();
        g.drawString(birthday != null ? String.valueOf(DateUtil.getAge(birthday, now)) : "", x1, 94);
        // 联系方式
        g.drawString(StringUtil.ensureStringNotNull(user.getPhone()), x1, 114);
        // 生成日期
        g.drawString(df.format(now), x1, 134);
        Font font2 = new Font("serif", Font.PLAIN, 12);
        g.setFont(font2);
        FontMetrics fm = g.getFontMetrics();
        int x2 = 665;
        // 收缩压
        String val = physiologIndex != null && physiologIndex.getSbp() != null ? physiologIndex.getSbp().toString()
                : "";
        g.drawString(val, x2 - fm.stringWidth(val) / 2, 195);
        // 舒张压
        val = physiologIndex != null && physiologIndex.getDbp() != null ? physiologIndex.getDbp().toString() : "";
        g.drawString(val, x2 - fm.stringWidth(val) / 2, 255);
        // 体温
        val = physiologIndex != null && physiologIndex.getTemperature() != null ? DecimalUtil.toString(physiologIndex
                .getTemperature()) : "";
        g.drawString(val, x2 - fm.stringWidth(val) / 2, 357);
        // 血糖
        // 空腹血糖
        val = physiologIndex != null && physiologIndex.getFpg() != null ? DecimalUtil.toString(physiologIndex.getFpg())
                : "";
        g.drawString(val, x2 - fm.stringWidth(val) / 2, 481);
        // 餐后血糖
        val = physiologIndex != null && physiologIndex.getH2pbg() != null ? DecimalUtil.toString(physiologIndex
                .getH2pbg()) : "";
        g.drawString(val, x2 - fm.stringWidth(val) / 2, 513);
        // 血氧
        val = physiologIndex != null && physiologIndex.getBo() != null ? physiologIndex.getBo().toString() : "";
        g.drawString(val, x2 - fm.stringWidth(val) / 2, 606);
        // 尿酸
        val = physiologIndex != null && physiologIndex.getUricAcid() != null ? DecimalUtil.toString(physiologIndex
                .getUricAcid()) : "";
        g.drawString(val, x2 - fm.stringWidth(val) / 2, 721);
        // 总胆固醇
        val = physiologIndex != null && physiologIndex.getTotalCholesterol() != null ? DecimalUtil
                .toString(physiologIndex.getTotalCholesterol()) : "";
        g.drawString(val, x2 - fm.stringWidth(val) / 2, 829);
        // 尿常规酸碱度PH
        val = analysisUricAcid != null && analysisUricAcid.getPh() != null ? DecimalUtil.toString(analysisUricAcid
                .getPh()) : "";
        g.drawString(val, x2 - fm.stringWidth(val) / 2, 895);
        // 尿比重SG
        val = analysisUricAcid != null && analysisUricAcid.getSg() != null ? DecimalUtil.toString(analysisUricAcid
                .getSg()) : "";
        g.drawString(val, x2 - fm.stringWidth(val) / 2, 951);
        // 尿胆原URO
        val = analysisUricAcid != null && analysisUricAcid.getUbg() != null ? StringUtil
                .ensureStringNotNull(analysisUricAcid.getUbg()) : "";
        g.drawString(val, x2 - fm.stringWidth(val) / 2, 1006);
        // 隐血BLO
        val = analysisUricAcid != null && analysisUricAcid.getBld() != null ? StringUtil
                .ensureStringNotNull(analysisUricAcid.getBld()) : "";
        g.drawString(val, x2 - fm.stringWidth(val) / 2, 1062);
        // 白细胞WBC
        val = analysisUricAcid != null && analysisUricAcid.getLeu() != null ? StringUtil
                .ensureStringNotNull(analysisUricAcid.getLeu()) : "";
        g.drawString(val, x2 - fm.stringWidth(val) / 2, 1107);
        // 尿蛋白PRO
        val = analysisUricAcid != null && analysisUricAcid.getPro() != null ? StringUtil
                .ensureStringNotNull(analysisUricAcid.getPro()) : "";
        g.drawString(val, x2 - fm.stringWidth(val) / 2, 1127);
        // 尿糖GLU
        val = analysisUricAcid != null && analysisUricAcid.getGlu() != null ? StringUtil
                .ensureStringNotNull(analysisUricAcid.getGlu()) : "";
        g.drawString(val, x2 - fm.stringWidth(val) / 2, 1155);
        // 胆红素BIL
        val = analysisUricAcid != null && analysisUricAcid.getBil() != null ? StringUtil
                .ensureStringNotNull(analysisUricAcid.getBil()) : "";
        g.drawString(val, x2 - fm.stringWidth(val) / 2, 1199);
        // 酮体KET
        val = analysisUricAcid != null && analysisUricAcid.getKet() != null ? StringUtil
                .ensureStringNotNull(analysisUricAcid.getKet()) : "";
        g.drawString(val, x2 - fm.stringWidth(val) / 2, 1267);
        // 体脂
        val = physiologIndex != null && physiologIndex.getBfr() != null ? DecimalUtil.toString(physiologIndex.getBfr())
                + "%" : "";
        g.drawString(val, x2 - fm.stringWidth(val) / 2, 1345);
        // 保存文件和数据
        String innerPath = FileUtil.genNewPath("jpg", ConstFilePath.RPT_FOLDER);
        File file = new File(FileUtil.getFullPath(innerPath));
        File dir = file.getParentFile();
        if (!dir.exists())
        {
            dir.mkdirs();
        }
        ImageIO.write(bImg, "jpg", file);
        FileDesc fd = new FileDesc();
        fd.setName(file.getName());
        fd.setPath(innerPath);
        fd.setExtName("jpg");
        fd.setFileSize(file.length());
        String fileId = fileDescService.add(fd);
        checkReport.setReportFileId(fileId);
        checkReport.setCreateTime(now);
        checkReport.setUpdateBy(checkReport.getCreateBy());
        checkReport.setUpdateTime(now);
        checkReportDao.insert(checkReport);
        return new Result(Result.STATE_SUCESS, "生成成功", innerPath);
    }

    /**
     * 查询客户体检信息
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageTran<CheckReport> queryTran(Map<String, Object> filter, Timestamp sysTime, int count)
    {
        return checkReportDao.queryTran(filter, sysTime, count);
    }

    /**
     * 平台查询体检报告
     * @author h
     * @param filter
     * 2015.8.19
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<CheckReport> query(Map<String, Object> filter, int page, int pageSize)
    {
        return checkReportDao.query(filter, page, pageSize);
    }

    /**
     * 平台查看某人的体检报告的图片
     * @author h
     * @param filter
     * 2015.8.19
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public CheckReport getReportPic(Map<String, Object> filter)
    {
        return checkReportDao.getReport(filter);
    }

}
