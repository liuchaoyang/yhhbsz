<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.util.DateUtil"%>
<%@ page import="com.yzxt.yh.module.msg.bean.AskCatalog"%>
<%@ page import="com.yzxt.yh.module.msg.bean.Ask"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	boolean update = (Boolean)request.getAttribute("update");
	Ask ask = (Ask)request.getAttribute("ask");
	@SuppressWarnings("unchecked")
	List<AskCatalog> topLevelAskCatalogs = (List<AskCatalog>)request.getAttribute("topLevelAskCatalogs");
	@SuppressWarnings("unchecked")
	List<AskCatalog> secondLevelAskCatalogs = (List<AskCatalog>)request.getAttribute("secondLevelAskCatalogs");
	String parentCatalogId = ask.getParentCatalogId();
	String catalogId = ask.getCatalogId();
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file=../common/inc.jsp"%>
	<title>我的提问</title>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/ueditor/ueditor.config.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/ueditor/ueditor.all.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="<%=request.getContextPath()%>/resources/ueditor/lang/zh-cn/zh-cn.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script> 
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/common.js"></script>
	<style type="text/css">
	.ct {
		width: 900px;
		margin-right: auto;
		margin-left: auto;
		font-size: 13px;
	}
	</style>
	<script type="text/javascript">	
	function setCataList(parentCataId){
		$.ajax({
			type : "GET",
			dataType : "json",
			url : "<%=request.getContextPath()%>/msg/ask_listChildrenAskCatalogsByJson.action?catalogId=" + parentCataId,
			async : false,
			timeout : 30000,
			success : function(data){
				var sel = document.getElementById("catalogId");
				sel.options.length = 0;
				sel.add(new Option("请选择", ""));
				for(var i = 0;i < data.length; i++){
					sel.add(new Option(data[i].name, data[i].id));
				}
			},
			error : function(){
		 	}
		});
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
	
	function submitAsk(){
		// 验证
		var titleEle = document.getElementById("title");
		var titleVal = $.trim(titleEle.value);
		if(!titleVal){
			$.messager.alert("提示信息", "请填写标题.", "error");
			return;
		}
		titleEle.val = titleVal;
		// 分类信息
		var parentCatalogEle = document.getElementById("parentCatalog");
		if(!parentCatalogEle.value){
			$.messager.alert("提示信息", "请选择问题大类.", "error");
			return;
		}
		var catalogEle = document.getElementById("catalogId");
		if(!catalogEle.value){
			$.messager.alert("提示信息", "请选择问题小类.", "error");
			return;
		}
		// 问题内容
		var askContentEle= document.getElementById("askContent");
		var contentVal = askContentEle.value;
		document.getElementById("content").value = contentVal;
		summaryVal = $.trim(contentVal);
		if(!summaryVal){
			$.messager.alert("提示信息", "请填写问题内容.", "error");
			return;
		}
		var maxTxtLen = 100;
		var txtLen = summaryVal.length;
		if(txtLen > maxTxtLen){
			summaryVal = summaryVal.substring(0, maxTxtLen - 3) + "...";
		}
		document.getElementById("summary").value = summaryVal;
		// 表单提交
		$("#askForm").form("submit", {
			<%
			if(!update)
			{
			%>
			url : "<%=request.getContextPath()%>/msg/ask_add.action",
			<%
			}else
			{
			%>
			url : "<%=request.getContextPath()%>/msg/ask_update.action",
			<%
			}
			%>
			dataType : "json",
			onSubmit : function() {
				return true;
			},
			success : function(data) {
				data =  $.parseJSON(data);
				if(data.state > 0){
					$.messager.alert("提示信息", "提交成功.", "info", function(){
						location.href = "<%=request.getContextPath()%>/msg/ask_view.action?id=" + data.data;
					});
					refreshPage("${param.pPageId}");
				}else{
					$.messager.alert("提示信息", data.msg, "error");
				}
			}
		});
	}
	</script>
</head>
<body>
	<div class="ct">
		<h1 align="center">发布健康问题</h1>
	</div>
	<div class="ct">
		<form method="post" enctype="multipart/form-data" style="margin: 0px; padding: 0px;"
			id="askForm" name="askForm" action="">
			<table width="100%" border="0" cellspacing="0">
				<tr>
					<td width="50px" align="right" nowrap="nowrap"><font>标题：</font></td>
					<td>
						<input type="text" id="title" name="ask.title" style="height: 20px;width: 90%;" maxlength="50" value="<%=StringUtil.trim(ask.getTitle())%>" />
						<%
						if(update)
						{
						%>
						<input type="hidden" name="ask.id" value="<%=ask.getId()%>" />
						<%
						}
						%>
					</td>
				</tr>
				<tr>
					<td height="30" align="right" nowrap="nowrap">问题大类：</td>
					<td height="30">
						<select id="parentCatalog" style="height: 24px;width: 120px;" onchange="setCataList(this.value);">
							<option value="" <%=StringUtil.isEmpty(parentCatalogId) ? "selected=\"selected\"" : ""%>>请选择</option>
							<%
							for(AskCatalog parentCata : topLevelAskCatalogs)
							{
							%>
							<option value="<%=parentCata.getId()%>" <%=parentCata.getId().equals(parentCatalogId) ? "selected=\"selected\"" : ""%>><%=parentCata.getName()%></option>
							<%
							}
							%>
						</select>
						&nbsp;&nbsp;&nbsp;&nbsp;
						问题小类：
						<select id="catalogId" name="ask.catalogId" style="height: 24px;width: 120px;">
							<option value="" <%=StringUtil.isEmpty(catalogId) ? "selected=\"selected\"" : ""%>>请选择</option>
							<%
							for(AskCatalog cata : secondLevelAskCatalogs)
							{
							%>
							<option value="<%=cata.getId()%>" <%=cata.getId().equals(catalogId) ? "selected=\"selected\"" : ""%>><%=cata.getName()%></option>
							<%
							}
							%>
						</select>
					</td>
				</tr>
				<tr>
					<%
					Integer sex = ask.getSex();
					if(sex == null || sex.intValue() == 0)
					{
					    sex = 9;
					}
					%>
					<td height="30" align="right" nowrap="nowrap">性别：</td>
					<td height="30">
						<select id="sex" name="ask.sex" style="height: 24px;width: 120px;">
							<option value="9" <%=(sex == 9 ? "selected=\"selected\"" : "")%>>未知</option>
							<option value="1" <%=(sex == 1 ? "selected=\"selected\"" : "")%>>男</option>
							<option value="2" <%=(sex == 2 ? "selected=\"selected\"" : "")%>>女</option>
						</select>
						&nbsp;&nbsp;&nbsp;&nbsp;
						出生日期：
						<input class="easyui-validatebox Wdate" style="height: 24px;width: 120px;" id="birthday" name="ask.birthday" readonly="readonly"
						value="<%=DateUtil.toHtmlDate(ask.getBirthday())%>" onclick="WdatePicker({})" />
					</td>
				</tr>
				<tr>
					<td height="30" align="left" nowrap="nowrap" valign="top">
						<input type="hidden" id="summary" name="ask.summary" />
						<input type="hidden" id="content" name="ask.content" />
						问题内容：
					</td>
					<td>
						<textarea id="askContent" style="height: 220px;width: 90%;line-height: 22px;" maxlength="1000"><%=StringUtil.ensureStringNotNull(ask.getContent())%></textarea>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div class="ct" style="text-align: center; margin-top: 15px;">
		<input type="button" id="btnSubmit" name="btnSubmit" onclick="submitAsk();" value="提交" />
		&nbsp;&nbsp;
		<input type="button" id="btnReset" name="btnReset" onclick="closeIt();" value="关闭" />
	</div>
</body>
</html>