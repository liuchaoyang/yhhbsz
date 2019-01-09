<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.util.DateUtil"%>
<%@ page import="com.yzxt.yh.module.ach.bean.Emr"%>
<%
Emr emr = (Emr)request.getAttribute("emr");
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
	<title>电子病历信息</title>
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
		<form id="mainForm" style="margin: 0px;padding: 0px;" method="post" accept-charset="UTF-8">
			<table class="table">
				<tr>
					<td width="18%" class="td_title">客户姓名:</td>
					<td width="32%">
						<%=emr.getCustName()%>
						<input type="hidden" name="emr.id" value="<%=StringUtil.ensureStringNotNull(emr.getId())%>" />
					</td>
					<td width="18%" class="td_title">就诊日期:</td>
					<td>
						<%=DateUtil.toHtmlDate(emr.getTreatDate())%>
					</td>
				</tr>
				<tr>
					<td class="td_title">接诊医生:</td>
					<td colspan="3"><%=StringUtil.ensureStringNotNull(emr.getDoctorName())%></td>
				</tr>
				<tr style="height: 155px;">
					<td class="td_title">诊疗信息:</td>
					<td colspan="3"><%=StringUtil.ensureStringNotNull(emr.getDiagnosis())%></td>
				</tr>
				<tr style="height: 75px;">
					<td class="td_title">检查化验:</td>
					<td colspan="3"><%=StringUtil.ensureStringNotNull(emr.getTest())%></td>
				</tr>
				<tr style="height: 75px;">
					<td class="td_title">其它信息:</td>
					<td colspan="3"><%=StringUtil.ensureStringNotNull(emr.getOther())%></td>
				</tr>
			</table>
		</form>
		<div style="text-align: center;padding-top: 7px;">
			<a class="easyui-linkbutton" href="javascript:closeIt();">关闭</a>
		</div>
	</div>
</body>
</html>