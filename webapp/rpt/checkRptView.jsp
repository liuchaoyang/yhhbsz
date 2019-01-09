<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.util.DateUtil"%>
<%@ page import="com.yzxt.yh.module.sys.bean.Customer"%>
<%@ page import="com.yzxt.yh.module.rpt.bean.CheckReport"%>
<%
	CheckReport checkRpt = (CheckReport)request.getAttribute("checkReport");
	StringBuilder src = new StringBuilder();
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>体检报告</title>
	<style type="text/css">
	body{
		margin: 10px;
	}
	</style>
	<script type="text/javascript">
	</script>
</head>
<body>
	<%
	if(checkRpt!=null)
	{
	    src.append(request.getContextPath()).append("/sys/fd_img.action");
	    if(StringUtil.isNotEmpty(checkRpt.getReportFilePath()))
	    {
	        src.append("?pt=").append(checkRpt.getReportFilePath());
	%>
	<div style="width: 60%;min-width: 600px;margin-left: auto;margin-right: auto;">
		<img style="width: 100%;" src="<%=src%>" />
	</div>
	<%
	    }
	}
	%>
</body>
</html>