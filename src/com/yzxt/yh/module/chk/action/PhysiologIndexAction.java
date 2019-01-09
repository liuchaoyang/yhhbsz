package com.yzxt.yh.module.chk.action;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.chk.bean.AnalysisUricAcid;
import com.yzxt.yh.module.chk.bean.BloodOxygen;
import com.yzxt.yh.module.chk.bean.BloodSugar;
import com.yzxt.yh.module.chk.bean.BodyFat;
import com.yzxt.yh.module.chk.bean.PhysiologIndex;
import com.yzxt.yh.module.chk.bean.PressurePulse;
import com.yzxt.yh.module.chk.bean.Pulse;
import com.yzxt.yh.module.chk.bean.Temperature;
import com.yzxt.yh.module.chk.service.AnalysisUricAcidService;
import com.yzxt.yh.module.chk.service.PhysiologIndexService;
import com.yzxt.yh.module.chk.util.AnalysisCheckData;
import com.yzxt.yh.module.chk.util.AnalysisResult;
import com.yzxt.yh.module.sys.bean.Customer;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.service.CustomerService;

/**
 * 用户生理指标
 * @author huangGang
 *
 */
public class PhysiologIndexAction extends BaseAction
{
    private static final long serialVersionUID = 1L;

    // private Logger  logger = Logger.getLogger(PhysiologIndexAction.class);

    private PhysiologIndexService physiologIndexService;

    private CustomerService customerService;

    private AnalysisUricAcidService analysisUricAcidService;

    public PhysiologIndexService getPhysiologIndexService()
    {
        return physiologIndexService;
    }

    public void setPhysiologIndexService(PhysiologIndexService physiologIndexService)
    {
        this.physiologIndexService = physiologIndexService;
    }

    public CustomerService getCustomerService()
    {
        return customerService;
    }

    public void setCustomerService(CustomerService customerService)
    {
        this.customerService = customerService;
    }

    public AnalysisUricAcidService getAnalysisUricAcidService()
    {
        return analysisUricAcidService;
    }

    public void setAnalysisUricAcidService(AnalysisUricAcidService analysisUricAcidService)
    {
        this.analysisUricAcidService = analysisUricAcidService;
    }

    /**
     * 显示(居民)用户生理指标记录
     * 
     * @throws Exception
     */
    public String showPhyIdx() throws Exception
    {
        User user = (User) super.getCurrentUser();
        String custId = request.getParameter("custId");
        // 如果是个人用户，则只能查看自己的指标
        if (Constant.USER_TYPE_CUSTOMER.equals(user.getType()) && custId == null)
        {
            custId = ((User) super.getCurrentUser()).getId();
        }
        Customer cust = customerService.load(custId);
        cust.setName(cust.getUser().getName());
        PhysiologIndex phyIdx = physiologIndexService.get(custId);
        //尿酸的描述和数据
        AnalysisUricAcid dataAcid = analysisUricAcidService.load(custId);
        if (phyIdx == null)
        {
            phyIdx = new PhysiologIndex();
        }
        //血压的描述
        PressurePulse xyBean = null;
        if (phyIdx.getDbp() != null || phyIdx.getSbp() != null)
        {
            AnalysisResult a = AnalysisCheckData.bloodPressure((double) phyIdx.getDbp(), (double) phyIdx.getSbp());
            xyBean = new PressurePulse();
            xyBean.setLevel(a.getLevel());
            xyBean.setDescript(a.getDescript());
        }
        else
        {
            xyBean = new PressurePulse();
        }

        //心率的描述
        Pulse pulse = null;
        if (phyIdx.getPulse() != null)
        {
            pulse = new Pulse();
            pulse.setPulse(phyIdx.getPulse());
            AnalysisResult a = AnalysisCheckData.pulse(phyIdx.getPulse());
            pulse.setLevel(a.getLevel());
            pulse.setDescript(a.getDescript());
        }
        else
        {
            pulse = new Pulse();
        }

        //血氧的描述
        BloodOxygen bloodOxygen = null;
        if (phyIdx.getBo() != null)
        {
            bloodOxygen = new BloodOxygen();
            bloodOxygen.setBO(phyIdx.getBo());
            AnalysisResult a = AnalysisCheckData.bloodOxygen(phyIdx.getBo());
            bloodOxygen.setLevel(a.getLevel());
            bloodOxygen.setDescript(a.getDescript());
        }
        else
        {
            bloodOxygen = new BloodOxygen();
        }
        
        //体脂的描述
        BodyFat bodyFat = null;
        if (phyIdx.getBfr() != null)
        {
            bodyFat = new BodyFat();
            bodyFat.setBfr(phyIdx.getBfr());
            AnalysisResult a = AnalysisCheckData.bodyFat(phyIdx.getBfr(),cust.getSex());
            bodyFat.setLevel(a.getLevel());
            bodyFat.setDescript(a.getDescript());
        }
        else
        {
            bodyFat = new BodyFat();
        }
        //温度的描述
        Temperature temperature = null;
        if (phyIdx.getTemperature() != null)
        {
            temperature = new Temperature();
            temperature.setTemperature(phyIdx.getTemperature());
            AnalysisResult a = AnalysisCheckData.temperature(phyIdx.getTemperature());
            temperature.setLevel(a.getLevel());
            temperature.setDescript(a.getDescript());
        }
        else
        {
            temperature = new Temperature();
        }

        //血糖的描述
        BloodSugar khBean = null;
        if (phyIdx.getFpg() != null)
        {
            AnalysisResult a = AnalysisCheckData.bloodSugar(1, (double) phyIdx.getFpg());
            khBean = new BloodSugar();
            khBean.setLevel(a.getLevel());
            khBean.setDescript(a.getDescript());
        }
        else
        {
            khBean = new BloodSugar();
        }
        BloodSugar chBean = null;
        if (phyIdx.getH2pbg() != null)
        {
            AnalysisResult a = AnalysisCheckData.bloodSugar(3, (double) phyIdx.getH2pbg());
            chBean = new BloodSugar();
            chBean.setLevel(a.getLevel());
            chBean.setDescript(a.getDescript());
        }
        else
        {
            chBean = new BloodSugar();
        }
        BloodSugar ftBean = null;
        if (phyIdx.getL2sugar() != null)
        {
            AnalysisResult a = AnalysisCheckData.bloodSugar(4, (double) phyIdx.getL2sugar());
            ftBean = new BloodSugar();
            ftBean.setLevel(a.getLevel());
            ftBean.setDescript(a.getDescript());
        }
        else
        {
            ftBean = new BloodSugar();
        }
        request.setAttribute("xyBean", xyBean);
        request.setAttribute("khBean", khBean);
        request.setAttribute("chBean", chBean);
        request.setAttribute("ftBean", ftBean);
        request.setAttribute("pulse", pulse);
        request.setAttribute("bloodOxygen", bloodOxygen);
        request.setAttribute("bodyFat", bodyFat);
        request.setAttribute("temperature", temperature);
        request.setAttribute("dataAcid", dataAcid);
        request.setAttribute("resident", cust);
        request.setAttribute("phyIdx", phyIdx);
        return "phyIdxDetail";
    }

}
