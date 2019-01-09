<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.util.DateUtil"%>
<%@ page import="com.yzxt.yh.module.msg.bean.Information"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	Information information = (Information)request.getAttribute("information");
	Boolean editable = (Boolean)request.getAttribute("editable");
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>查看资讯</title>
	<style type="text/css">
	.ct {
		width: 80%;
		margin-right: auto;
		margin-left: auto;
		font-size: 13px;
		margin-bottom: 10px;
	}
	.cont{
		font-size: 16px;
	}
	</style>
	<script type="text/javascript">
	function editInfo(infoId){
        var url = "<%=request.getContextPath()%>/msg/info_toEdit.action?pPageId=${param.pageId}&id=<%=information.getId()%>";
		var title = "发布资讯";
		top.addTab(title, url, null);
	}
	function closeIt(){
		try{
			var tc = parent.$("#tb");
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
			url : "<%=request.getContextPath()%>/msg/info_delete.action?id=<%=information.getId()%>",
			async : false,
			timeout : 300000,
			success : function(data){
				if(data.state == 1){
					refreshPage("350101");
					$.messager.alert("提示信息", "删除成功.", "info", function(){
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
	function refresh(){
		location.reload();
	}
	</script>
</head>
<body>
	<div class="ct">
		<h1 align="center"><%=information.getTitle()%></h1>
	</div>
	<%if(editable)
	{
	%>
	<div class="ct" style="text-align: right;">
		<input type="button" value="编辑" onclick="editInfo();" />
		<input type="button" value="删除" onclick="delInfo();" />
	</div>
	<%
	}
	%>
	<div class="ct">
		发布人：<%=information.getUpdateByName() != null ? information.getUpdateByName() : ""%>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		发布时间：<%=DateUtil.toHtmlTime(information.getUpdateTime())%>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		资讯来源：<%=StringUtil.trim(information.getSrc())%>
	</div>
	<div class="ct cont">
		<%=information.getContent()%>
	</div>
</body>
</html>