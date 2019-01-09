package com.yzxt.yh.module.ach.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.ConstDict;
import com.yzxt.yh.constant.ConstFilePath;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.ach.bean.Archive;
import com.yzxt.yh.module.ach.bean.FamilyHistory;
import com.yzxt.yh.module.ach.bean.LifeEnv;
import com.yzxt.yh.module.ach.bean.PreviousHistory;
import com.yzxt.yh.module.ach.service.ArchiveService;
import com.yzxt.yh.module.svb.bean.MemberInfo;
import com.yzxt.yh.module.svb.service.MemberInfoService;
import com.yzxt.yh.module.sys.bean.Customer;
import com.yzxt.yh.module.sys.bean.DictDetail;
import com.yzxt.yh.module.sys.bean.FileDesc;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.service.CustomerService;
import com.yzxt.yh.module.sys.service.DictService;
import com.yzxt.yh.module.sys.service.UserService;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.FileUtil;
import com.yzxt.yh.util.StringUtil;

public class ArchiveAction extends BaseAction
{
    private static final long serialVersionUID = 1L;

    private Logger logger = Logger.getLogger(ArchiveAction.class);

    private ArchiveService archiveService;
    private CustomerService customerService;
    private MemberInfoService memberInfoService;
    private UserService userService;
    private DictService dictService;

    private Archive archive;
    private Customer cust;
    // 图像
    private File userImg;
    private String userImgFileName;
    private String userImgContentType;

    public ArchiveService getArchiveService()
    {
        return archiveService;
    }

    public void setArchiveService(ArchiveService archiveService)
    {
        this.archiveService = archiveService;
    }

    public CustomerService getCustomerService()
    {
        return customerService;
    }

    public void setCustomerService(CustomerService customerService)
    {
        this.customerService = customerService;
    }

    public MemberInfoService getMemberInfoService()
    {
        return memberInfoService;
    }

    public void setMemberInfoService(MemberInfoService memberInfoService)
    {
        this.memberInfoService = memberInfoService;
    }

    public UserService getUserService()
    {
        return userService;
    }

    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }

    public DictService getDictService()
    {
        return dictService;
    }

    public void setDictService(DictService dictService)
    {
        this.dictService = dictService;
    }

    public Archive getArchive()
    {
        return archive;
    }

    public void setArchive(Archive archive)
    {
        this.archive = archive;
    }

    public Customer getCust()
    {
        return cust;
    }

    public void setCust(Customer cust)
    {
        this.cust = cust;
    }

    public File getUserImg()
    {
        return userImg;
    }

    public void setUserImg(File userImg)
    {
        this.userImg = userImg;
    }

    public String getUserImgFileName()
    {
        return userImgFileName;
    }

    public void setUserImgFileName(String userImgFileName)
    {
        this.userImgFileName = userImgFileName;
    }

    public String getUserImgContentType()
    {
        return userImgContentType;
    }

    public void setUserImgContentType(String userImgContentType)
    {
        this.userImgContentType = userImgContentType;
    }

    /**
     * 查询用户档案列表
     */
    public void query()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            String name = (String) request.getParameter("userName");
            String idCard = (String) request.getParameter("idCard");
            String healthyStatus = (String) request.getParameter("healthyState");
            filter.put("name", name != null ? name.trim() : null);
            filter.put("idCard", idCard != null ? idCard.trim() : null);
            filter.put("healthyStatus", StringUtil.isNotEmpty(healthyStatus) ? Integer.valueOf(healthyStatus) : null);
            // 查询人
            filter.put("operUser", super.getCurrentUser());
            PageModel<Customer> pageModel = archiveService.query(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询用户档案列表错误。", e);
        }
    }

    /**
     * 跳转到档案查看页面
     * @return
     */
    public String toDetail()
    {
        try
        {
            User operUser = (User) super.getCurrentUser();
            String operType = request.getParameter("operType");
            String custId = request.getParameter("custId");
            if (StringUtil.isEmpty(custId) && Constant.USER_TYPE_CUSTOMER.equals(operUser.getType()))
            {
                custId = operUser.getId();
            }
            Archive archive = archiveService.load(custId);
            // 还没有新增档案
            if (archive != null)
            {
                String doctorId = archive.getDoctorId();
                if (StringUtil.isNotEmpty(doctorId))
                {
                    User doctor = userService.getUser(doctorId);
                    archive.setDoctorName(doctor != null ? doctor.getName() : "");
                }
            }
            else
            {
                archive = new Archive();
                archive.setCustId(custId);
            }
            // 客户基本信息 
            Customer cust = customerService.load(custId);
            if (cust.getBirthday() != null)
            {
                cust.setAge(DateUtil.getAge(cust.getBirthday(), null) + "");
            }
            else
            {
                cust.setAge("");
            }
            User user = cust.getUser();
            String imgFilePath = user.getImgFilePath();
            if (StringUtil.isNotEmpty(imgFilePath))
            {
                user.setImgFilePath(FileUtil.encodePath(imgFilePath));
            }
            request.setAttribute("operType", operType);
            request.setAttribute("archive", archive);
            request.setAttribute("cust", cust);
            if ("edit".equals(operType))
            {
                List<DictDetail> nationals = dictService.getDictDetails(ConstDict.NATIONAL);
                request.setAttribute("nationals", nationals);
                return "edit";
            }
            else
            {
                String national = cust.getNational();
                if (StringUtil.isNotEmpty(national))
                {
                    DictDetail nationalDictDetail = dictService.getDictDetailByCode(ConstDict.NATIONAL, national);
                    cust.setNationalName(nationalDictDetail != null ? nationalDictDetail.getDictDetailName() : "");
                }
                // 是否拥有修改权限
                String editable = "N";
                if (Constant.USER_TYPE_CUSTOMER.equals(operUser.getType()))
                {
                    if (operUser.getId().equals(custId))
                    {
                        editable = "Y";
                    }
                }
                else
                {
                    MemberInfo mi = memberInfoService.getMemberInfoByCustId(custId);
                    if (mi != null && operUser.getId().equals(mi.getDoctorId()))
                    {
                        editable = "Y";
                    }
                }
                request.setAttribute("editable", editable);
                return "view";
            }
        }
        catch (Exception e)
        {
            logger.error("跳转至档案明细页面出错。", e);
            return "error";
        }
    }

    /**
     * 保存档案
     * @return
     */
    public void save()
    {
        Result r = null;
        try
        {
            // 医疗费用支付方式
            String[] payTypes = request.getParameterValues("payType");
            StringBuilder payType = new StringBuilder();
            if (payTypes != null && payTypes.length > 0)
            {
                for (int i = 0; i < payTypes.length; i++)
                {
                    if (StringUtil.isNotEmpty(payTypes[i]))
                    {
                        payType.append(payTypes[i]).append(";");
                    }
                }
            }
            archive.setPayType(payType.toString());
            // 药物过敏史
            String[] hodas = request.getParameterValues("hoda");
            StringBuilder hoda = new StringBuilder();
            if (hodas != null && hodas.length > 0)
            {
                for (int i = 0; i < hodas.length; i++)
                {
                    if (StringUtil.isNotEmpty(hodas[i]))
                    {
                        hoda.append(hodas[i]).append(";");
                    }
                }
            }
            if (hoda.length() == 0)
            {
                hoda.append("1;");
            }
            archive.setHoda(hoda.toString());
            // 暴 露 史
            String[] exposureHistorys = request.getParameterValues("exposureHistory");
            StringBuilder exposureHistory = new StringBuilder();
            if (exposureHistorys != null && exposureHistorys.length > 0)
            {
                for (int i = 0; i < exposureHistorys.length; i++)
                {
                    if (StringUtil.isNotEmpty(exposureHistorys[i]))
                    {
                        exposureHistory.append(exposureHistorys[i]).append(";");
                    }
                }
            }
            if (exposureHistory.length() == 0)
            {
                exposureHistory.append("1;");
            }
            archive.setExposureHistory(exposureHistory.toString());
            // 既往史
            List<PreviousHistory> phs = new ArrayList<PreviousHistory>();
            // 既往史-疾病
            String[] jbnames = request.getParameterValues("jbname");
            String[] jbdates = request.getParameterValues("jbdate");
            int jbLen = jbnames != null ? jbnames.length : 0;
            for (int i = 0; i < jbLen; i++)
            {
                if (StringUtil.isNotEmpty(jbnames[i]) || StringUtil.isNotEmpty(jbdates[i]))
                {
                    PreviousHistory ph = new PreviousHistory();
                    ph.setType(1);
                    ph.setName(jbnames[i]);
                    ph.setPastTime(DateUtil.getDateFromHtml(jbdates[i]));
                    phs.add(ph);
                }
            }
            // 既往史-手术
            String[] ssnames = request.getParameterValues("ssname");
            String[] ssdates = request.getParameterValues("ssdate");
            int ssLen = ssnames != null ? ssnames.length : 0;
            for (int i = 0; i < ssLen; i++)
            {
                if (StringUtil.isNotEmpty(ssnames[i]) || StringUtil.isNotEmpty(ssdates[i]))
                {
                    PreviousHistory ph = new PreviousHistory();
                    ph.setType(2);
                    ph.setName(ssnames[i]);
                    ph.setPastTime(DateUtil.getDateFromHtml(ssdates[i]));
                    phs.add(ph);
                }
            }
            // 既往史-外伤
            String[] wsnames = request.getParameterValues("wsname");
            String[] wsdates = request.getParameterValues("wsdate");
            int wsLen = wsnames != null ? wsnames.length : 0;
            for (int i = 0; i < wsLen; i++)
            {
                if (StringUtil.isNotEmpty(wsnames[i]) || StringUtil.isNotEmpty(wsdates[i]))
                {
                    PreviousHistory ph = new PreviousHistory();
                    ph.setType(3);
                    ph.setName(wsnames[i]);
                    ph.setPastTime(DateUtil.getDateFromHtml(wsdates[i]));
                    phs.add(ph);
                }
            }
            // 既往史-输血
            String[] sxnames = request.getParameterValues("sxname");
            String[] sxdates = request.getParameterValues("sxdate");
            int sxLen = sxnames != null ? sxnames.length : 0;
            for (int i = 0; i < sxLen; i++)
            {
                if (StringUtil.isNotEmpty(sxnames[i]) || StringUtil.isNotEmpty(sxdates[i]))
                {
                    PreviousHistory ph = new PreviousHistory();
                    ph.setType(4);
                    ph.setName(sxnames[i]);
                    ph.setPastTime(DateUtil.getDateFromHtml(sxdates[i]));
                    phs.add(ph);
                }
            }
            archive.setPreviousHistorys(phs);
            // 家族史
            List<FamilyHistory> fhs = new ArrayList<FamilyHistory>();
            String[] fhFathers = request.getParameterValues("fhFather");
            if (fhFathers != null && fhFathers.length > 0)
            {
                for (int i = 0; i < fhFathers.length; i++)
                {
                    if (StringUtil.isNotEmpty(fhFathers[i]))
                    {
                        FamilyHistory fh = new FamilyHistory();
                        fh.setRelaType(1);
                        fh.setDisease(fhFathers[i]);
                        fhs.add(fh);
                    }
                }
            }
            String[] fhMothers = request.getParameterValues("fhMother");
            if (fhMothers != null && fhMothers.length > 0)
            {
                for (int i = 0; i < fhMothers.length; i++)
                {
                    if (StringUtil.isNotEmpty(fhMothers[i]))
                    {
                        FamilyHistory fh = new FamilyHistory();
                        fh.setRelaType(2);
                        fh.setDisease(fhMothers[i]);
                        fhs.add(fh);
                    }
                }
            }
            String[] fhBrothers = request.getParameterValues("fhBrother");
            if (fhBrothers != null && fhBrothers.length > 0)
            {
                for (int i = 0; i < fhBrothers.length; i++)
                {
                    if (StringUtil.isNotEmpty(fhBrothers[i]))
                    {
                        FamilyHistory fh = new FamilyHistory();
                        fh.setRelaType(3);
                        fh.setDisease(fhBrothers[i]);
                        fhs.add(fh);
                    }
                }
            }
            String[] fhChildrens = request.getParameterValues("fhChildren");
            if (fhChildrens != null && fhChildrens.length > 0)
            {
                for (int i = 0; i < fhChildrens.length; i++)
                {
                    if (StringUtil.isNotEmpty(fhChildrens[i]))
                    {
                        FamilyHistory fh = new FamilyHistory();
                        fh.setRelaType(4);
                        fh.setDisease(fhChildrens[i]);
                        fhs.add(fh);
                    }
                }
            }
            archive.setFamilyHistorys(fhs);
            // 残疾情况
            String[] disabilityStatuss = request.getParameterValues("disabilityStatus");
            StringBuilder disabilityStatus = new StringBuilder();
            if (disabilityStatuss != null && disabilityStatuss.length > 0)
            {
                for (int i = 0; i < disabilityStatuss.length; i++)
                {
                    if (StringUtil.isNotEmpty(disabilityStatuss[i]))
                    {
                        disabilityStatus.append(disabilityStatuss[i]).append(";");
                    }
                }
            }
            if (disabilityStatus.length() == 0)
            {
                disabilityStatus.append("1;");
            }
            archive.setDisabilityStatus(disabilityStatus.toString());
            // 生活环境
            List<LifeEnv> les = new ArrayList<LifeEnv>();
            String lifeEnv0 = request.getParameter("lifeEnv0");
            if (StringUtil.isNotEmpty(lifeEnv0))
            {
                LifeEnv le = new LifeEnv();
                le.setEnvType(1);
                le.setDetail(lifeEnv0);
                les.add(le);
            }
            String lifeEnv1 = request.getParameter("lifeEnv1");
            if (StringUtil.isNotEmpty(lifeEnv1))
            {
                LifeEnv le = new LifeEnv();
                le.setEnvType(2);
                le.setDetail(lifeEnv1);
                les.add(le);
            }
            String lifeEnv2 = request.getParameter("lifeEnv2");
            if (StringUtil.isNotEmpty(lifeEnv2))
            {
                LifeEnv le = new LifeEnv();
                le.setEnvType(3);
                le.setDetail(lifeEnv2);
                les.add(le);
            }
            String lifeEnv3 = request.getParameter("lifeEnv3");
            if (StringUtil.isNotEmpty(lifeEnv3))
            {
                LifeEnv le = new LifeEnv();
                le.setEnvType(4);
                le.setDetail(lifeEnv3);
                les.add(le);
            }
            String lifeEnv4 = request.getParameter("lifeEnv4");
            if (StringUtil.isNotEmpty(lifeEnv4))
            {
                LifeEnv le = new LifeEnv();
                le.setEnvType(5);
                le.setDetail(lifeEnv4);
                les.add(le);
            }
            archive.setLifeEnvs(les);
            // 用户图像
            if (userImg != null)
            {
                FileDesc userIcon = FileUtil.save(userImg, userImgFileName, ConstFilePath.USER_IMG_FOLDER, true);
                cust.getUser().setIconFile(userIcon);
            }
            // 保存数据
            User operUser = (User) super.getCurrentUser();
            archive.setDoctorId(operUser.getId());
            cust.setUpdateBy(operUser.getId());
            archive.setCustomer(cust);
            r = archiveService.save(archive);
        }
        catch (Exception e)
        {
            r = new Result(Result.STATE_FAIL, "保存档案出错。", null);
            logger.error("保存档案明细出错。", e);
        }
        super.write(r);
    }
}
