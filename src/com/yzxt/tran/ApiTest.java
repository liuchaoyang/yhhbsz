package com.yzxt.tran;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

public class ApiTest {
//	private static String code = smsCode();
//    private Logger logger = Logger.getLogger(ApiTest.class);
	private static final String sms_url = "http://localhost:8086/sms/verifyCode";
	// 普通短信
	public String sendsms(String phone) throws Exception {
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(sms_url);
		postMethod.getParams().setContentCharset("UTF-8");
//		postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());

		postMethod.addParameter("mobile", phone);
		String code = smsCode();
		postMethod.addParameter("verify_code", code);

		postMethod.setRequestHeader("Connection", "close");

		int statusCode = httpClient.executeMethod(postMethod);
//		logger.info("ApiTest sendsms response:" + code);
		return code;
	}

	/*
	 * public static void main(String[] args) throws Exception { ApiTest t = new
	 * ApiTest(); //普通短信 t.sendsms(); }
	 */
	// 创建验证码
	private String smsCode(){
        String random=(int)((Math.random()*9+1)*100000)+""; 
        System.out.println("验证码："+random);
        return random;
    	/*String[] beforeShuffle = new String[] { "2", "3", "4", "5", "6", "7","8", "9","1","0"};      
    	List list = Arrays.asList(beforeShuffle);       
    	Collections.shuffle(list);       
    	StringBuilder sb = new StringBuilder();      
    	for (int i = 0; i < list.size(); i++) {           
    		sb.append(list.get(i));      
    		}
	 	String afterShuffle = sb.toString();  
	 	String result = afterShuffle.substring(3, 9);
		return result; */
	}
	/*public static void main(String[] args) {
		for (int i = 0; i < 1; i++) {
			System.out.println("随机验证码：" + smsCode());
		}
	}*/

	public static void main(String[] args) {
		ApiTest t = new ApiTest();
		try {
			t.sendsms("13691156267");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}