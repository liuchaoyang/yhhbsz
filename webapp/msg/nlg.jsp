<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.util.DateUtil"%>
<%@ page import="com.yzxt.yh.module.msg.bean.Information"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<title></title>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/resources/css/easyui.css">
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/jquery.easyui.min.js"></script>
<style type="text/css">
.ct {
	width: 900px;
	margin-right: auto;
	margin-left: auto;
	font-size: 13px;
}
.cont{
	font-size: 16px;
}
</style>
<%-- <script type="text/javascript">
	function init(){
		
	}
	function editInfo(infoId){
        var url = "<%=request.getContextPath()%>/msg/info_toEdit.action?id=<%=infomation.getId()%>";
		var title = "发布资讯";
		top.addTab(title, url, null);
	}
	function closeIt(){
		try{
			var tc = parent.$("#ttabs");
			var tab = tc.tabs("getSelected");
			if (tab) {
				tc.tabs("close", tab.panel("options").title);
			}
		}catch(e){}
	}
	function delInfo(){
		$.ajax({
			type : "POST",
			dataType : "json",
			url : "<%=request.getContextPath()%>/msg/info_delete.action?id=<%=infomation.getId()%>",
			async : false,
			timeout : 300000,
			success : function(data){
				if(data.state == 1){
					$.messager.alert("提示信息", "删除成功.", "error", function(){
						closeIt();
					});
				}else if(data.state < 0){
					$.messager.alert("提示信息", data.msg, "error");
				}
			},
			error : function(){
				$.messager.alert("提示信息", "删除失败！", "error");
		 	}
		});
	}
</script> --%>
</head>
<body >
	<form action="<%=request.getContextPath()%>/msg/nlg_saveData.action" method="post"
		enctype="multipart/form-data">
		<table>
			<tr>
				<td><input type="file" name="attachment"  id="excelFile"></input>选择导入的文件</td>
				<td><input type="submit" value="导入数据" /></td>
			</tr>
		</table>
	</form>
</body>
</html>