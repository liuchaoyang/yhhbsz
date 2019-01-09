package com.yzxt.tran;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.alibaba.fastjson.JSONObject;

public class ApiTest {
	private static String code = smsCode();

	// 普通短信
	public String sendsms(String phone) throws Exception {
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod("*******");
		postMethod.getParams().setContentCharset("UTF-8");
		postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());

		String accesskey = "**********"; // 用户开发key
		String accessSecret = "********"; // 用户开发秘钥

		NameValuePair[] data = { new NameValuePair("accesskey", accesskey), new NameValuePair("secret", accessSecret),
				new NameValuePair("sign", "8188"), new NameValuePair("templateId", "10359"),
				new NameValuePair("mobile", phone),
				new NameValuePair("content", URLEncoder.encode(ApiTest.code, "utf-8"))// （示例模板：{1}您好，您的订单于{2}已通过{3}发货，运单号{4}）
		};
		postMethod.setRequestBody(data);
		postMethod.setRequestHeader("Connection", "close");

		int statusCode = httpClient.executeMethod(postMethod);
		System.out.println("statusCode: " + statusCode + ", body: " + postMethod.getResponseBodyAsString());
		System.out.println(ApiTest.code+"=============");
		return ApiTest.code;
	}

	/*
	 * public static void main(String[] args) throws Exception { ApiTest t = new
	 * ApiTest(); //普通短信 t.sendsms(); }
	 */
	// 创建验证码
	public static String smsCode(){
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

}