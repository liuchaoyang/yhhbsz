package com.yzxt.yh.module.his.action;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.module.his.bean.HisPros;
import com.yzxt.yh.module.his.bean.HisYpkc;
import com.yzxt.yh.module.his.bean.ProsItem;
import com.yzxt.yh.module.his.service.DzcfService;
import com.yzxt.yh.module.his.service.ProItemService;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.StringUtil;

import common.Logger;

public class DzcfAction extends BaseAction{
	
	private static final long serialVersionUID = 1L;
    
    private Logger logger  =  Logger.getLogger(DzcfAction.class);
    
    private HisPros hisPros;
    
    private ProItemService proItemService;
    
    public ProItemService getProItemService() {
		return proItemService;
	}
	public void setProItemService(ProItemService proItemService) {
		this.proItemService = proItemService;
	}
	private HisYpkc hisYpkc;
    
	public HisYpkc getHisYpkc() {
		return hisYpkc;
	}
	public void setHisYpkc(HisYpkc hisYpkc) {
		this.hisYpkc = hisYpkc;
	}
	private ProsItem prosItem;
    
    public HisPros getHisPros() {
		return hisPros;
	}
	public void setHisPros(HisPros hisPros) {
		this.hisPros = hisPros;
	}
	public ProsItem getProsItem() {
		return prosItem;
	}
	public void setProsItem(ProsItem prosItem) {
		this.prosItem = prosItem;
	}
	private DzcfService dzcfService;
    public DzcfService getDzcfService() {
		return dzcfService;
	}
	public void setDzcfService(DzcfService dzcfService) {
		this.dzcfService = dzcfService;
	}
	/**
     * 开处方
     * @author h
     * */
    public String queryp(){
	   Map<String,Object> filter = new HashMap<String,Object>();
       String ypjx = request.getParameter("ypjx");
       String custId = request.getParameter("custId");
      // String doctorId = request.getParameter("doctorId");
       String userName = request.getParameter("userName");
       User user = (User) super.getCurrentUser();
       filter.put("ypjx", ypjx);
       PageModel<HisYpkc> pageModel = dzcfService.getList(filter, getPage(), getRows());
       request.setAttribute("pageModel", pageModel);
       request.setAttribute("custId", custId);
       request.setAttribute("doctorId", user.getId());
       request.setAttribute("userName", userName);
       return "add";
        
    }
    
    
    /**
     * 
     * 获取电子处方列表
     * 
     */
    
    public String queryChufang(){
	    {
	        try
	        {
	            Map<String, Object> filter = new HashMap<String, Object>();
	            //查询条件
	            //filter.put("doctorId", request.getParameter("doctorId"));
	            filter.put("status", request.getParameter("status"));
	            // 查询人
	            filter.put("operUser", super.getCurrentUser());
	            List<HisPros> hisPros  = dzcfService.queryChufang(filter);
	            super.write(hisPros);
	        }
	        catch (Exception e)
	        {
	            logger.error("平台查询提醒记录出现异常", e);
	        }
	    }
		return null;
    	
    }
    
    public void addChufang()
    {
        Result r = null;
        try
        {
            // 当前操作人
            User curOper = (User) super.getCurrentUser();
            hisPros.setDoctorid(curOper.getId());
            r = dzcfService.addChufang(hisPros);

        }
        catch (Exception e)
        {
            r = new Result(Result.STATE_FAIL, "新增处方错误。", null);
            logger.error("添加处方错误。", e);
        }
        super.write(r);
    }
    public String toAdd() throws Exception{
    	String custId = request.getParameter("custId");
    	String userName = request.getParameter("userName");
    	User user = (User) super.getCurrentUser();
    	PageModel<HisYpkc> pageModel = dzcfService.getList(null, getPage(), getRows());
    	request.setAttribute("pageModel", pageModel);
    	request.setAttribute("custId", custId);
    	request.setAttribute("doctorId", user.getId());
    	request.setAttribute("userName", userName);
		return "add";
    	
    }
    
    /**
     * 电子处方详情
     * @return
     */
    public String dzcfDetail() {
    	String custId = request.getParameter("custId");
    	User user = (User) super.getCurrentUser();
    	String userName = request.getParameter("userName");
    	String ypId = request.getParameter("ypId");
    	HisYpkc hisYpkc = this.dzcfService.getById(ypId);
    	request.setAttribute("hisYpkc", hisYpkc);
    	request.setAttribute("custId", custId);
    	request.setAttribute("doctorId", user.getId());
    	request.setAttribute("userName", userName);
    	return "detail";
    }
    /**
     * 保存电子处方
     * @return
     * @throws Exception
     */
    public void saveDzcf() {
    	
    	try {
    		String custId = request.getParameter("custId");
    		User user = (User) super.getCurrentUser();
        	String userName = request.getParameter("userName");
        	String userage = request.getParameter("userage");
        	String uselevel = request.getParameter("uselevel");
        	//String ypid = request.getParameter("ypid");
        	String num = request.getParameter("num");
        	String unit = request.getParameter("unit");
        	String ypid = hisYpkc.getSqid();
        	BigDecimal ypdj = hisYpkc.getYpdj();
        	HisPros pros = new HisPros();
        	pros.setYpid(ypid);
        	pros.setPatientid(custId);
        	pros.setDoctorid(user.getId());
        	pros.setPatientname(userName);
        	pros.setStatus(0);
        	dzcfService.addChufang(pros);
        	ProsItem proItem = new ProsItem();
        	proItem.setProsid(pros.getProsid());
        	proItem.setPrice(ypdj);
        	proItem.setUserage(userage);
        	proItem.setUselevel(uselevel);
        	if(StringUtil.isNotEmpty(num))
        		proItem.setNum(Integer.parseInt(num));
        	proItem.setUnit(Integer.parseInt(unit));
        	proItemService.addProItem(proItem);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
 //   	return "list";
    }
    
}
