<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.util.DateUtil"%>
<%@ page import="com.yzxt.yh.module.msg.bean.Knowledge"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	Knowledge knowledge = (Knowledge)request.getAttribute("knowledge");
	Boolean editable = (Boolean)request.getAttribute("editable");

%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<style type="text/css">
	.ct {
		width: 80%;
		margin-right: auto;
		margin-left: auto;
		font-size: 13px;
	}
	.cont{
		font-size: 16px;
	}
	</style>
	<script type="text/javascript">
		function init(){
			
		}
		function editNlg(nlgId){
	        var url = "<%=request.getContextPath()%>/msg/nlg_toEdit.action?pPageId=${param.pageId}&id=<%=knowledge.getId()%>";
			var title = "发布健康知识";
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
		function delNlg(){
			$.ajax({
				type : "POST",
				dataType : "json",
				url : "<%=request.getContextPath()%>/msg/nlg_delete.action?id=<%=knowledge.getId()%>",
				async : false,
				timeout : 300000,
				success : function(data){
					if(data.state == 1){
						refreshPage("350201");
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
<body onload="init();">
	<div class="ct">
		<h1 align="center"><%=knowledge.getTitle()%></h1>
	</div>
	<%if(editable)
	{
	%>
	<div class="ct" style="text-align: right;">
		<input type="button" value="编辑" onclick="editNlg();" />
		<input type="button" value="删除" onclick="delNlg();" />
	</div>
	<%
	}
	%>
	<div class="ct">
		发布人：<%=knowledge.getUpdateByName() != null ? knowledge.getUpdateByName() : ""%>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		发布时间：<%=DateUtil.toHtmlTime(knowledge.getUpdateTime())%>
	</div>
	<br />
	<div class="ct cont">
		<%=knowledge.getContent()%>
	</div>
</body>
</html>