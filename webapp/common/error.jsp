<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
    String msg = (String)request.getAttribute("msg");
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<title></title>
<style type="text/css">
</style>
<script type="text/javascript">
	
</script>
</head>
<body>
	<div style="margin: auto; text-align: center; vertical-align: middle; height: 100px; margin-top: 50px;">
		<%=msg%>
	</div>
</body>
</html>