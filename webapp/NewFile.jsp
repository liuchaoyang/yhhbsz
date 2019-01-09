<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../common/inc.jsp"%>
<title>ffff</title>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/highcharts.js"></script>
<style type="text/css">
</style>
<script type="text/javascript">
	function saveWb(){
				function(data){
				     console.log(data.time); //  2pm
				     
				   }, "json");
	}
</script>
</head>
<body onload="saveWb()">
<!-- <a href="javascript:saveWb();"
					style="margin-left: 10px;" class="easyui-linkbutton">submit</a>  -->
</body>
</html>