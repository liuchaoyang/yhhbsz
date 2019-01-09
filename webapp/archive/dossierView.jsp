<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.util.DateUtil"%>
<%@ page import="com.yzxt.yh.module.ach.bean.Dossier"%>
<%@ page import="com.yzxt.yh.module.ach.bean.DossierDetail"%>
<%
Dossier dossier= (Dossier)request.getAttribute("dossier");
List<DossierDetail> details = dossier.getDetails();
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>病历夹信息</title>
	<style type="text/css">
	.mrImg{
		margin: 2px;
		float: left;
		border: 1px solid black;
	}
	</style>
	<script type="text/javascript">
	</script>
</head>
<body style="margin: 0px;padding: 20px;min-width: 600px;">
	<div>
		<table style="width: auto;">
			<tr>
				<td width="100" align="right">姓名:</td>
				<td width="100" align="left">
					<%=StringUtil.ensureStringNotNull(dossier.getCustName())%>
				</td>
				<td width="100" align="right" >病历类型:</td>
				<td width="100" align="left">
					<%=StringUtil.ensureStringNotNull(dossier.getTypeName())%>
				</td>
				<td width="100" align="right" >创建时间:</td>
				<td width="150" align="left">
					<%=DateUtil.toHtmlTime(dossier.getCreateTime())%>
				</td>
			</tr>
		</table>
		<hr style="width: 750px;text-align: left;margin-top: 5px;margin-bottom: 5px;margin-left: 2px;" />
		<div>
			<%
			for(DossierDetail dd : details)
			{
			%>
			<img class="mrImg" style="max-width: 100%;" alt="" src="<%=request.getContextPath()%>/sys/fd_img.action?pt=<%=dd.getPath()%>" />
			<%
			}
			%>	
		</div>
		<div style="clear: both;height: 0px;"></div>
	</div>
</body>
</html>