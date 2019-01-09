<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.module.msg.bean.HealthyGuide"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.util.DateUtil"%>
<% 
	HealthyGuide healthyGuide = (HealthyGuide) request.getAttribute("healthyGuide");
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>健康指导</title>
	<style type="text/css">
	td{
		word-break: break-all;
	}
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
	<div>
		<form id="filterForm">
			<table class="table">
				<tr>
					<td width="20%" class="td_title">名称:</td>
					<td colspan="3" >
						<%=healthyGuide.getMemberName()%>
					</td>
				</tr>
				
				<tr>
					<td width="20%" class="td_title">指导人:</td>
					<td width="30%">
						<%=healthyGuide.getDoctorName()%>
					</td>
					<td width="20%" class="td_title">指导时间:</td>
					<td>
						<%=healthyGuide.getCreateTime()%>
					</td>
				</tr>
				<tr>
					<td class="td_title">指导原因:</td>
					<td colspan="3" style="padding: 10px 3px 10px 3px;">
						<%=healthyGuide.getDirectReason()%>
					</td>
				</tr>
				<tr>
					<td class="td_title">运动指导:</td>
					<td colspan="3" style="padding: 10px 3px 10px 3px;">
						<%=healthyGuide.getSportDirect()%>
					</td>
				</tr>
				<tr>
					<td class="td_title">饮食指导:</td>
					<td colspan="3" style="padding: 10px 3px 10px 3px;">
						<%=healthyGuide.getFoodDirect()%>
					</td>
				</tr>
				<tr>
					<td class="td_title">药物建议:</td>
					<td colspan="3" style="padding: 10px 3px 10px 3px;">
						<%=healthyGuide.getDrugSuggest()%>
					</td>
				</tr>
				<tr>
					<td class="td_title">备注:</td>
					<td colspan="3" style="padding: 10px 3px 10px 3px;">
						<%=healthyGuide.getMemo()%>
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