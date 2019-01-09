/**
 * 
 */
package com.yzxt.tran;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.common.ResultTran;
import com.yzxt.fw.util.GsonUtil;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.chk.bean.PressurePulse;
import com.yzxt.yh.module.svb.bean.MemberInfo;
import com.yzxt.yh.module.svb.dao.MemberInfoDao;
import com.yzxt.yh.module.svb.service.MemberInfoService;
import com.yzxt.yh.module.sys.bean.CustFamily;
import com.yzxt.yh.module.sys.bean.Order;
import com.yzxt.yh.module.sys.bean.UnifiedOrderRequest;
import com.yzxt.yh.module.sys.bean.UnifiedOrderRespose;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.service.DoctorService;
import com.yzxt.yh.module.sys.service.OrderService;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.MD5Util;
import com.yzxt.yh.util.PayCommonUtil;
import com.yzxt.yh.util.StringUtil;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.ClientConfig;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import common.Logger;

/**
 * <p>
 * Title: OrderAction
 * </p>
 * <p>
 * 
 * @function:
 * 			</p>
 * 
 * @author Codeagles ,
 * @date 下午7:41:41
 */
public class OrderTranAction extends BaseTranAction {
	private static final long serialVersionUID = 1L;
	//private static final Logger log = (Logger) LoggerFactory.getLogger(OrderTranAction.class);
	private static String masterSecret = "******";
    private static String appKey = "*******";
    private static final String ALERT = "支付成功";    
	
	private MemberInfoService memberInfoService;
	private MemberInfoDao memberInfoDao;
	public MemberInfoDao getMemberInfoDao() {
		return memberInfoDao;
	}

	public void setMemberInfoDao(MemberInfoDao memberInfoDao) {
		this.memberInfoDao = memberInfoDao;
	}

	public MemberInfoService getMemberInfoService() {
		return memberInfoService;
	}

	public void setMemberInfoService(MemberInfoService memberInfoService) {
		this.memberInfoService = memberInfoService;
	}
	private Logger logger = Logger.getLogger(OrderTranAction.class);
	private Order order = new Order();
	private OrderService orderService;
	public static final String API_KEY = "********"; 
	public static final String NOTIFY_URL ="*******";
	public OrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}
	private DoctorService doctorService;
	public DoctorService getDoctorService() {
		return doctorService;
	}

	public void setDoctorService(DoctorService doctorService) {
		this.doctorService = doctorService;
	}
	private Integer page;

	// 接收支付通道
	private String pd_FrpId;

	public String getPd_FrpId() {
		return pd_FrpId;
	}

	public void setPd_FrpId(String pd_FrpId) {
		this.pd_FrpId = pd_FrpId;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	/**
	 * 提交订单
	 */
	public void save() {

		try {
			JsonObject paramJson = super.getParams();
			JsonArray retJson = new JsonArray();
			User operUser = super.getOperUser();
			Result r = null;
			String doctorId = GsonUtil.toString(paramJson.get("doctorId"));
			Integer count = GsonUtil.toInteger(paramJson.get("count"));
			//Integer price = GsonUtil.toInteger(paramJson.get("price"));
			Integer total = GsonUtil.toInteger(paramJson.get("total"));
			Integer type = GsonUtil.toInteger(paramJson.get("type"));
			Object[] doctor = doctorService.getDoctorById(doctorId);
			if(doctor==null){
				 super.write(ResultTran.STATE_ERROR, "该医生不存在", null);
				 return;
			}
			//绑定医生
			if (memberInfoDao.getIsMember(operUser.getId(),doctorId))
		        {
				 super.write(ResultTran.STATE_ERROR, "绑定错误，该客户已经绑定该医生。", null);
				 return;
		        }
			Timestamp now = new Timestamp(System.currentTimeMillis());
			order.setOrdertime(now);
			order.setTotal(total*100);
			order.setCount(count);
			order.setUid(operUser.getId());
			order.setState(Constant.ORDER_TYPE_NO);
			order.setDoctorId(doctorId);
			order.setType(type);
			orderService.save(order);
			
			this.createQRCode(order.getOid());
			JsonObject object = new JsonObject();
			object.addProperty("orderId", order.getOid());
			retJson.add(object);
			super.write(ResultTran.STATE_OK, "提交订单成功", retJson);
		} catch (Exception e) {
			logger.error("提交订单错误。", e);
			super.write(ResultTran.STATE_ERROR, "提交订单错误。");
		}

	}

	/**
	 * 计算医生总服务费
	 */

	public void total() {
		try {
			JsonObject paramJson = super.getParams();
			JsonArray retJson = new JsonArray();
			//String doctorId = GsonUtil.toString(paramJson.get("doctorId"));
			Integer count = GsonUtil.toInteger(paramJson.get("count"));
			Integer price = GsonUtil.toInteger(paramJson.get("price"));
			Integer total = count * price;
			/*
			 * Map<String, Object> data = new HashMap<String, Object>();
			 * data.put("total", total);
			 */
			JsonObject object = new JsonObject();
			object.addProperty("total", total);
			//object.addProperty("doctorId", doctorId);
			retJson.add(object);
			super.write(ResultTran.STATE_OK,"计算成功",retJson);
		} catch (Exception e) {
			logger.error("计算错误。", e);
			super.write(ResultTran.STATE_ERROR, "计算错误。");
		}
	}

	// 我的订单的查询
	public void findByUid() {
		try {

			JsonObject paramsJson = super.getParams();
			initQuery(paramsJson);
			JsonArray json = new JsonArray();
			String userId = GsonUtil.toString(paramsJson.get("userId"));
			String state = GsonUtil.toString(paramsJson.get("state"));
			 Map<String, Object> filter = new HashMap<String, Object>();
			 filter.put("userId", userId);
			 filter.put("state", state);
			List<Order> list = orderService.queryOrderTran(filter);
			if(list!=null&&list.size()>0){
				for (Order order : list) {
					JsonObject obj = new JsonObject();
					obj.addProperty("orderId", order.getOid());
					obj.addProperty("total", order.getTotal()/100);
					obj.addProperty("count", order.getCount());
					obj.addProperty("ordertime", order.getOrdertime().toString());
					obj.addProperty("orderId", order.getOid());
					obj.addProperty("state", order.getState());
					Object[] doctor = doctorService.getDoctorById(order.getDoctorId());
					if(doctor!=null){
						obj.addProperty("doctorName",doctor[9].toString());
						obj.addProperty("orgName", doctor[11].toString());
						obj.addProperty("deptName", doctor[4].toString());
						obj.addProperty("professionTitle", doctor[3].toString());
						if(doctor[7]!=null){
							obj.addProperty("price", doctor[7].toString());
						}
					}
					json.add(obj);
				}
				
			}
			super.write(ResultTran.STATE_OK, "查询订单列表成功", json);
		} catch (Exception e) {
			logger.error("查询订单列表错误。", e);
			super.write(ResultTran.STATE_ERROR, "查询订单列表错误。");
		}
	}

	/**
	 * 创建二维码
	 * @throws IOException 
	 */
	public void createQRCode(String orderId) throws IOException {
		JsonObject paramsJson = super.getParams();
		initQuery(paramsJson);
		JsonArray json = new JsonArray();
		//String orderId = GsonUtil.toString(paramsJson.get("orderId"));
        Order order = orderService.findByOid(orderId);
        request.setAttribute("order", order);
		// 生成订单
		String orderInfo = createOrderInfo(orderId);
		// 调统一下单API
		String code_url = httpOrder(orderInfo);
		
		// 将返回预支付交易链接（code_url）生成二维码图片
		// 这里使用的是zxing <span
		// style="color:#ff0000;"><strong>说明1(见文末)</strong></span>
		try {
			int width = 200;
			int height = 200;
			String format = "png";
			Hashtable hints = new Hashtable();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			BitMatrix bitMatrix = new MultiFormatWriter().encode(code_url, BarcodeFormat.QR_CODE, width, height, hints);
			/*OutputStream out = null;
			out = response.getOutputStream();*/
			String contextPath = request.getSession().getServletContext().getRealPath("/file");
			File file = new File(contextPath+File.separator+orderId+".png");
			System.out.println(file);
			String str = file.toString();
			//G:\tomcat\webapps\yhhbsz\
			System.out.println(str);
			//String st = str.replace("D:\\tool\\workspace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\yhhbsz\\", "http://www.mysbdoctor.com/");
			String st = str.replace("G:\\tomcat\\webapps\\yhhbsz\\file\\", "https://www.mysbdoctor.com/file/");
			System.out.println(st);
			order.setFile(st);
			orderService.update(order);
			//File file = new File("d:/file"+File.separator+"new.png");
			MatrixToImageWriter.writeToFile(bitMatrix, format, file);
			JsonObject obj = new JsonObject();
			obj.addProperty("orderId", orderId);
			obj.addProperty("file", st);
			json.add(obj);
			super.write(ResultTran.STATE_OK,"二维码生成成功",json);
			/*out.flush();
			out.close();*/
		} catch (Exception e) {
			super.write(ResultTran.STATE_ERROR,"二维码生错误");
		}
		
		
	}

	/**
	 * 生成订单
	 * 
	 * @param orderId
	 * @return
	 */
	private String createOrderInfo(String orderId) {
		// 生成订单对象
		UnifiedOrderRequest  unifiedOrderRequest = new UnifiedOrderRequest ();
		unifiedOrderRequest.setAppid("wxc1a87fedf2d716ff");// 公众账号ID
		unifiedOrderRequest.setMch_id("1513088091");// 商户号
		unifiedOrderRequest.setNonce_str(StringUtil.makeUUID());
		unifiedOrderRequest.setTime_stamp(System.currentTimeMillis()/1000);
		//unifiedOrderRequest.setNonce_str(StringUtil.makeUUID());// 随机字符串 <span
															// style="color:#ff0000;"><strong>说明2(见文末)</strong></span>
		unifiedOrderRequest.setBody("摩云-收款");//商品描述
		//unifiedOrderRequest.setDevice_info("013467007045764");//商品描述
		unifiedOrderRequest.setOut_trade_no(orderId);// 商户订单号
		order = orderService.findByOid(orderId);
		unifiedOrderRequest.setTotal_fee(order.getTotal());
		//unifiedOrderRequest.setSpbill_create_ip("192.168.1.126");// 终端IP
		unifiedOrderRequest.setSpbill_create_ip("211.157.159.145");
		//unifiedOrderRequest.setNotify_url("http://www.weixin.qq.com/wxpay/pay.php");//通知地址
		unifiedOrderRequest.setNotify_url("https://www.mysbdoctor.com/mobile/corder_notifyWeiXinPay.action");//通知地址
		unifiedOrderRequest.setTrade_type("NATIVE");// JSAPI--公众号支付、NATIVE--原生扫码支付、APP--ape支付
		unifiedOrderRequest.setProduct_id(order.getDoctorId());
		unifiedOrderRequest.setSign(createSign(unifiedOrderRequest));//签名<span
		// style="color:#ff0000;"><strong>说明5(见文末，签名方法一并给出)</strong></span>
		// 将订单对象转为xml格式
		//XStream xStream = new XStream(new XppDriver(new XmlFriendlyNameCoder("_-", "_"))); // <span
		XStream xStream = new XStream(new DomDriver(null, new XmlFriendlyNameCoder("_-", "_")));																			// style="color:#ff0000;"><strong>说明3(见文末)</strong></span>
		xStream.alias("xml", UnifiedOrderRequest.class);// 根元素名需要是xml
		return xStream.toXML(unifiedOrderRequest);
	}

	/**
	 * 调统一下单API
	 * 
	 * @param orderInfo
	 * @return
	 */
	private String httpOrder(String orderInfo) {
		String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		try {
			//HttpURLConnection conn = (HttpURLConnection) new URL(null, url, new sun.net.www.protocol.https.Handler()).openConnection();
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			// 加入数据
			conn.setRequestMethod("POST");
			/*conn.setDoInput(true);
			conn.setUseCaches(false);*/
			conn.setDoOutput(true);
			BufferedOutputStream buffOutStr = new BufferedOutputStream(conn.getOutputStream());
			buffOutStr.write(orderInfo.getBytes("UTF-8"));
			buffOutStr.flush();
			buffOutStr.close();
			//获取输入流    
	           BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));    
	           String line = null;    
	           StringBuffer sb = new StringBuffer();    
	           while((line = reader.readLine())!= null){    
	               sb.append(line);    
	           } 
	           System.out.println("***"+sb);
			//XStream xStream = new XStream(new XppDriver(new XmlFriendlyNameCoder("_-", "_")));// 说明3(见文末)
			// 将请求返回的内容通过xStream转换为UnifiedOrderRespose对象
			XStream xStream = new XStream(new DomDriver(null, new XmlFriendlyNameCoder("_-", "_")));
			xStream.alias("xml", UnifiedOrderRespose.class);
			UnifiedOrderRespose unifiedOrderRespose = (UnifiedOrderRespose) xStream.fromXML(sb.toString());

			// 根据微信文档return_code 和result_code都为SUCCESS的时候才会返回code_url
			// <span style="color:#ff0000;"><strong>说明4(见文末)</strong></span>
			if (null != unifiedOrderRespose && "SUCCESS".equals(unifiedOrderRespose.getReturn_code())
					&& "SUCCESS".equals(unifiedOrderRespose.getResult_code())) {
				return unifiedOrderRespose.getCode_url();
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/** 
	 * 生成签名 
	 *  
	 * @param appid_value 
	 * @param mch_id_value 
	 * @param productId 
	 * @param nonce_str_value 
	 * @param trade_type  
	 * @param notify_url  
	 * @param spbill_create_ip  
	 * @param total_fee  
	 * @param out_trade_no  
	 * @return 
	 */  
	private String createSign(UnifiedOrderRequest unifiedOrderRequest) {  
	    //根据规则创建可排序的map集合  
	    SortedMap<String, String> packageParams = new TreeMap<String, String>();  
	    packageParams.put("appid", unifiedOrderRequest.getAppid());  
	    packageParams.put("body", unifiedOrderRequest.getBody());  
	    packageParams.put("mch_id", unifiedOrderRequest.getMch_id());
	    packageParams.put("nonce_str", unifiedOrderRequest.getNonce_str());  
	    packageParams.put("notify_url", unifiedOrderRequest.getNotify_url());  
	    packageParams.put("out_trade_no", unifiedOrderRequest.getOut_trade_no());  
	    packageParams.put("spbill_create_ip", unifiedOrderRequest.getSpbill_create_ip());  
	    packageParams.put("trade_type", unifiedOrderRequest.getTrade_type());  
	    packageParams.put("product_id", unifiedOrderRequest.getProduct_id());  
	    packageParams.put("total_fee", unifiedOrderRequest.getTotal_fee().toString());
	    packageParams.put("time_stamp", unifiedOrderRequest.getTime_stamp().toString()); 
	    //packageParams.put("trade_type", unifiedOrderRequest.getTrade_type()); 
	  
	    StringBuffer sb = new StringBuffer();  
	    Set es = packageParams.entrySet();//字典序  
	    Iterator it = es.iterator();  
	    while (it.hasNext()) {  
	        Map.Entry entry = (Map.Entry) it.next();  
	        String k = (String) entry.getKey();  
	        String v = (String) entry.getValue();  
	        //为空不参与签名、参数名区分大小写  
	        if (null != v && !"".equals(v) && !"sign".equals(k)  
	                && !"key".equals(k)) {  
	            sb.append(k + "=" + v + "&");  
	        }  
	    }  
	    //第二步拼接key，key设置路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置  
	    sb.append("key=" +API_KEY);  
	    String sign = MD5Util.MD5Encode(sb.toString(), "utf-8").toUpperCase();//MD5加密  
	    return sign;  
	}  
	
	/**
	 * 删除订单
	 * @throws Exception 
	 */
	public void deleteOrder() throws Exception{
		JsonObject paramsJson = super.getParams();
		String orderId = GsonUtil.toString(paramsJson.get("orderId"));
		Order order =null;
		if(StringUtil.isNotEmpty(orderId)){
			order = orderService.findByOid(orderId);
			//MemberInfo info = new MemberInfo();
			//删除客户与医生的绑定关系
			MemberInfo member = memberInfoService.getMember(order.getUid(),order.getDoctorId());
			memberInfoService.delete(member);
			orderService.deleteOrder(order);
		}
		
		super.write(ResultTran.STATE_OK, "删除订单成功", null);
	}
	/**
	 * 
	 * 查看订单详情
	 */
	public void getOrderByOrderId(){
		JsonObject paramsJson = super.getParams();
		JsonArray json = new JsonArray();
		String orderId = GsonUtil.toString(paramsJson.get("orderId"));
		Object[] order = orderService.getOrderByOrderId(orderId);
		Object[] doctor = orderService.getDoctorNameByOrderId(orderId);
		JsonObject obj = new JsonObject();
		if(order!=null){
			if(order[1]!=null){
				obj.addProperty("sex", order[1].toString());
			}
			if(order[2]!=null){
				obj.addProperty("userName", order[2].toString());
			}
			if(order[3]!=null){
				obj.addProperty("startDay", order[3].toString());
			}
			if(order[4]!=null){
				obj.addProperty("endDay", order[4].toString());
			}
			if(order[5]!=null){
				obj.addProperty("orderTime", order[5].toString());
			}
			if(order[6]!=null){
				obj.addProperty("total", Integer.parseInt(order[6].toString())/100);
			}
			if(order[7]!=null){
				obj.addProperty("type", order[7].toString());
			}
			if(order[8]!=null){
				obj.addProperty("deptName", order[8].toString());
			}
			if(order[9]!=null){
				obj.addProperty("memo", order[9].toString());
			}
			if(order[10]!=null){
				obj.addProperty("orgName", order[10].toString());
			}
			if(order[11]!=null){
				obj.addProperty("count", order[11].toString());
			}
			if(order[12]!=null){
				obj.addProperty("professionTitle", order[12].toString());
			}
			if(doctor[0]!=null){
				obj.addProperty("doctorName", doctor[0].toString());
			}
			
			//obj.addProperty("doctorName", order[11].toString());
		}
	  json.add(obj);
	  super.write(ResultTran.STATE_OK, "查询订单成功", json);
	}
	
	   //@ResponseBody
	    public void notifyWeiXinPay() throws Exception{
	        System.out.println("微信支付回调");
	       HttpServletRequest request = ServletActionContext.getRequest();
	       HttpServletResponse response = ServletActionContext.getResponse();
	        InputStream inStream = request.getInputStream();
	        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
	        byte[] buffer = new byte[1024];
	        int len = 0;
	        while ((len = inStream.read(buffer)) != -1) {
	            outSteam.write(buffer, 0, len);
	        }
	        String resultxml = new String(outSteam.toByteArray(), "utf-8");
	        Map<String, String> params = PayCommonUtil.doXMLParse(resultxml);
	        outSteam.close();
	        inStream.close();
	        
	        String resXml = "";
	        Map<String,String> return_data = new HashMap<String,String>();  
	        if (!PayCommonUtil.isTenpaySign(params)) {
	        	 resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"  
	                       + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> "; 
	        } else {
		            System.out.println("===============付款成功==============");
		            // 处理业务开始
		            String total_fee = params.get("total_fee");
		            double v = Double.valueOf(total_fee) / 100;
		            String out_trade_no = params.get("out_trade_no");
		            //String out_trade_no = String.valueOf(Long.parseLong(params.get("out_trade_no").split("O")[0]));
					Date accountTime = DateUtil.stringtoDate(params.get("time_end"), "yyyyMMddHHmmss");
					String ordertime = DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
					String totalAmount = String.valueOf(v);
					String appId = params.get("appid");
					String tradeNo = params.get("transaction_id");
		            if(StringUtil.isNotEmpty(out_trade_no)){
		            	 order= orderService.findByOid(out_trade_no);
		            	if(order!=null&&order.getState()==0){
		            		order.setState(Constant.ORDER_TYPE_YES);
		            		orderService.update(order);
		            	}
		            }
		            //request.getSession().setAttribute("_PAY_RESULT", "OK");
		            if(order.getType()==null){
		            	this.push();
		            }
		            System.out.println("===支付成功===");
		            this.add(order.getUid(), order.getDoctorId(), order.getOid());
		            resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"  
		                       + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
		            BufferedOutputStream out = new BufferedOutputStream(  
			                   response.getOutputStream());  
			           out.write(resXml.getBytes());  
			           //out.flush();  
			           //out.close();
			         System.out.println("支付完成");
		           
	        }
	       
	    }
	    /**
	     * 绑定医生
	     * @param custId
	     * @param doctorId
	     * @param orderId
	     */
	    public void add(String custId,String doctorId,String orderId)
	    {
	        try
	        {
	          /*  JsonObject obj = super.getParams();
	            MemberInfo memberInfo = new MemberInfo();
	            String custId = GsonUtil.toString(obj.get("userId"));
	            String doctorId = GsonUtil.toString(obj.get("doctorId"));
	            String orderId = GsonUtil.toString(obj.get("orderId"));*/
	            //Date endDay = DateUtil.getFromTranDate(GsonUtil.toString(obj.get("endDay")));
	        	MemberInfo memberInfo = new MemberInfo();
	        	JsonArray  retJson = new JsonArray();
	            //更改订单状态
	            Order order =null;
	            if(StringUtil.isNotEmpty(orderId)){
	            	 order= orderService.findByOid(orderId);
	            	/*if(order!=null&&order.getState()==0){
	            		order.setState(Constant.ORDER_TYPE_YES);
	            		orderService.update(order);
	            	}*/
	            }
	            //绑定医生
	            memberInfo.setCustId(custId);
	            memberInfo.setDoctorId(doctorId);
	            memberInfo.setStartDay(new Date());
	            memberInfo.setEndDay(DateUtil.addMonth(new Date(), order.getCount()));
	            //memberInfo.setType(2);
	            Result r = memberInfoService.add(memberInfo);
	           
	            if (Result.STATE_SUCESS == r.getState())
	            {   
	            	JsonObject object = new JsonObject();
	    			object.addProperty("orderId", orderId);
	    			retJson.add(object);
	                super.write(ResultTran.STATE_OK, "绑定医生成功。",retJson);
	            }
	            else
	            {
	                super.write(ResultTran.STATE_ERROR, StringUtil.isNotEmpty(r.getMsg()) ? r.getMsg() : "绑定医生错误。");
	            }
	        }
	        catch (Exception e)
	        {
	            logger.error("绑定医生错误。", e);
	            super.write(ResultTran.STATE_ERROR, "绑定医生错误。");
	        }
	    }
	    /**
	     * 
	     * 去支付
	     * @param orderId
	     * @throws IOException
	     */
	    public void createQRCode() throws IOException {
			try {
				JsonObject paramsJson = super.getParams();
				initQuery(paramsJson);
				JsonArray json = new JsonArray();
				String orderId = GsonUtil.toString(paramsJson.get("orderId"));
		        Order order = orderService.findByOid(orderId);
		        long nd = 1000 * 24 * 60 * 60;//86400000,51372263
		    	long nh = 1000 * 60 * 60;//3600000
		    	long nm = 1000 * 60;//60000
		    	// 获得两个时间的毫秒时间差异
		    	Timestamp now = new Timestamp(System.currentTimeMillis());
		    	long diff = now.getTime()-order.getOrdertime().getTime();
		    	long hour = diff /nh;
		    	if(hour>12){
		    		super.write(ResultTran.STATE_ERROR,"二维码过期,请重新生成");
		    		return;
		    	}
				JsonObject obj = new JsonObject();
				obj.addProperty("orderId", orderId);// G:\tomcat\webapps\yhhbsz\new.png
				obj.addProperty("file",order.getFile());
				json.add(obj);
				super.write(ResultTran.STATE_OK,"获取二维码成功",json);
			} catch (Exception e) {
				super.write(ResultTran.STATE_ERROR,"获取二维码错误");
			}
	    }
	    /**
		 * 去支付
		 * @throws IOException 
		 */
		public void createQRCodes() throws IOException {
			JsonObject paramsJson = super.getParams();
			initQuery(paramsJson);
			JsonArray json = new JsonArray();
			String orderId = GsonUtil.toString(paramsJson.get("orderId"));
	        Order order = orderService.findByOid(orderId);
	        request.setAttribute("order", order);
			// 生成订单
			String orderInfo = createOrderInfo(orderId);
			// 调统一下单API
			String code_url = httpOrder(orderInfo);
			
			// 将返回预支付交易链接（code_url）生成二维码图片
			// 这里使用的是zxing <span
			// style="color:#ff0000;"><strong>说明1(见文末)</strong></span>
			try {
				int width = 200;
				int height = 200;
				String format = "png";
				Hashtable hints = new Hashtable();
				hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
				BitMatrix bitMatrix = new MultiFormatWriter().encode(code_url, BarcodeFormat.QR_CODE, width, height, hints);
				/*OutputStream out = null;
				out = response.getOutputStream();*/
				String contextPath = request.getSession().getServletContext().getRealPath("/file");
				File file = new File(contextPath+File.separator+orderId+".png");
				System.out.println(file);
				String str = file.toString();
				//G:\tomcat\webapps\yhhbsz\
				System.out.println(str);
				//String st = str.replace("D:\\tool\\workspace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\yhhbsz\\", "http://www.mysbdoctor.com/");
				String st = str.replace("G:\\tomcat\\webapps\\yhhbsz\\file\\", "https://www.mysbdoctor.com/file/");
				System.out.println(st);
				order.setFile(st);
				orderService.update(order);
				//File file = new File("d:/file"+File.separator+"new.png");
				MatrixToImageWriter.writeToFile(bitMatrix, format, file);
				JsonObject obj = new JsonObject();
				obj.addProperty("orderId", orderId);
				obj.addProperty("file", st);
				json.add(obj);
				super.write(ResultTran.STATE_OK,"二维码生成成功",json);
				/*out.flush();
				out.close();*/
			} catch (Exception e) {
				super.write(ResultTran.STATE_ERROR,"二维码生错误");
			}
			
		}
		 public PushResult push() throws APIConnectionException, APIRequestException{
		        ClientConfig clientConfig = ClientConfig.getInstance();
		        JPushClient jpushClient = new JPushClient(masterSecret, appKey, 0, null,clientConfig);
		        //PushPayload payload = buildPushObject_android_tag_alertWithTitle();
		        PushPayload payload = buildPushObject_all_alias_alert();
		        return jpushClient.sendPush(payload);
		    }
		   /* public static PushPayload buildPushObject_all_all_alert() {moyunshengbang@163.com  ==  ====   Moyunshengbang2018
		        return PushPayload.alertAll(ALERT);402881ec66806e760166806fbb410001
		    }*/
	    public  PushPayload buildPushObject_all_alias_alert() {
	        return PushPayload.newBuilder()
	                .setPlatform(Platform.all())
	                .setAudience(Audience.alias(order.getUid()))
	                .setNotification(Notification.alert(ALERT))
	                .build();
	    }
}

