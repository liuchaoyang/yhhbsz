<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.module.msg.bean.ConsultGuide"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.util.DateUtil"%>
<% 
	ConsultGuide consultGuide = (ConsultGuide) request.getAttribute("consultGuide");
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>咨询查看</title>
	<style type="text/css">
	</style>
	<script type="text/javascript">
	function closeIt() {
		try{
			parent.subWin.window("close");
		}catch(e){}
	}
</script>
</head>
<body style="margin: 1px;padding: 0px;">
	<div class="sectionTitle">咨询信息</div>
	<div>
		<form id="filterForm">
			<table class="table">
				<tr>
					<td width="20%" class="td_title">标题:</td>
					<td colspan="3">
						<%=consultGuide.getConsultTitle()%>
					</td>
				</tr>
				<tr>
					<td width="20%" class="td_title">咨询人:</td>
					<td width="30%">
						<%=consultGuide.getMemberName()%>
					</td>
					<td width="20%" class="td_title">咨询时间:</td>
					<td>
						<%=DateUtil.toHtmlDate(consultGuide.getConsultTime())%>
					</td>
				</tr>
				<tr>
					<td class="td_title">咨询内容:</td>
					<td colspan="3" style="padding: 10px 3px 10px 3px;">
						<%=consultGuide.getConsultContext()%>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div class="sectionTitle" style="margin-top: 5px;">指导信息</div>
	<div>
		<form id="filterForm">
			<table class="table">
				<tr>
					<td width="20%" class="td_title">指导内容:</td>
					<td colspan="3" style="padding: 10px 3px 10px 3px;">
						<% if(consultGuide.getGuideContext()!=null){%><%=consultGuide.getGuideContext()%>
						<%} %>
					</td>
				</tr>
				<tr>
					<td width="20%" class="td_title">指导人:</td>
					<td width="30%">
						<% if(consultGuide.getDoctorName()!=null){%><%=consultGuide.getDoctorName()%>
						<%} %>
					</td>
					<td width="20%" class="td_title">指导时间:</td>
					<td width="30%">
						<%=DateUtil.toHtmlDate(consultGuide.getGuideTime())%>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div style="text-align: center;padding: 5px;">
		<a class="easyui-linkbutton" href="javascript:closeIt()">关闭</a>
	</div>
</body>
</html>