<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="com.yzxt.yh.module.sys.bean.User"%>
<%@ page import="com.yzxt.yh.module.sys.bean.Customer"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.util.DateUtil"%>
<%
Customer cust= (Customer)request.getAttribute("cust");
User user = cust.getUser();
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>尿常规</title>
	<style type="text/css">
	</style>
	<script type="text/javascript">
	function closeIt(){
		try{
			parent.subWin.window("close");
		}catch(e){}
	}
</script>
</head>
<body style="margin: 0px;padding: 1px;">
	<div>
		<table class="table">
			<tr>
				<td width="20%" class="td_title">检测人:</td>
				<td width="30%">
					<label>${cust.user.name}</label>
				</td>
				<td width="20%" class="td_title">检测时间:</td>
				<td>
					<label><fmt:formatDate value="${analysisUricAcid.checkTime}" pattern="yyyy-MM-dd HH:mm:ss" /></label>
				</td>
			</tr>
			<tr>
				<td class="td_title">尿白细胞:</td>
				<td>
					<label>${analysisUricAcid.leu}</label>
				</td>
				<td class="td_title">亚硝酸盐:</td>
				<td>
					<label>${analysisUricAcid.nit}</label>
				</td>
			</tr>
			<tr>
				<td class="td_title">尿蛋白:</td>
				<td>
					<label>${analysisUricAcid.pro}</label>
				</td>
				<td class="td_title">葡萄糖:</td>
				<td>
					<label>${analysisUricAcid.glu}</label>
				</td>
			</tr>
			<tr>
				<td class="td_title">酮体:</td>
				<td>
					<label>${analysisUricAcid.ket}</label>
				</td>
				<td class="td_title">尿胆原:</td>
				<td>
					<label>${analysisUricAcid.ubg}</label>
				</td>
			</tr>
			<tr>
				<td class="td_title">胆红素:</td>
				<td>
					<label>${analysisUricAcid.bil}</label>
				</td>
				<td class="td_title">PH值:</td>
				<td>
					<label>${analysisUricAcid.ph}</label>
				</td>
			</tr>
			<tr>
				<td class="td_title">比重:</td>
				<td>
					<label>${analysisUricAcid.sg}</label>
				</td>
				<td class="td_title">隐血:</td>
				<td>
					<label>${analysisUricAcid.bld}</label>
				</td>
			</tr>
			<tr>
				<td class="td_title">维生素C:</td>
				<td colspan="3">
					<label>${analysisUricAcid.vc}</label>
				</td>
			</tr>
		</table>
		<div style="padding-top: 10px;text-align: center;">
			<a class="easyui-linkbutton" href="javascript:closeIt();">关闭</a>
		</div>
	</div>
</body>
</html>