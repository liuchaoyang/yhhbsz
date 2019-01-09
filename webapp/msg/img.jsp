<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.module.msg.bean.InfoCatalog"%>
<%@ page import="com.yzxt.yh.module.msg.bean.Information"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getParameter("pt");
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<title></title>
<style type="text/css">
.ct {
	width: 900px;
	margin-right: auto;
	margin-left: auto;
	font-size: 13px;
}
</style>
</head>
<body>
	<img alt="" src="<%=request.getContextPath()%>/sys/fd_img.action?pt=<%=path%>" />
</body>
</html>