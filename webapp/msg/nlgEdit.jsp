<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.module.msg.bean.KnowledgeCatalog"%>
<%@ page import="com.yzxt.yh.module.msg.bean.Knowledge"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	boolean update = (Boolean)request.getAttribute("update");
	Knowledge knowledge = (Knowledge)request.getAttribute("knowledge");
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
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
	// 实例化编辑器
	// 建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，
	// 直接调用UE.getEditor('editor')就能拿到相关的实例
	var ue;
	var UEDITOR_HOME_URL = "${pageContext.request.contextPath}/resources/ueditor/"
	function init() {
		ue = UE.getEditor("editor", {
			autoHeight : false,
			toolbars : [ [ "fullscreen", "source", "|", "undo", "redo", "|",
					"bold", "italic", "underline", "strikethrough",
					"superscript", "subscript", "removeformat", "formatmatch",
					"autotypeset", "blockquote", "|", "forecolor", "backcolor",
					"insertorderedlist", "insertunorderedlist", "selectall",
					"cleardoc", "|", "rowspacingtop", "rowspacingbottom",
					"lineheight", "|", "customstyle", "paragraph",
					"fontfamily", "fontsize", "|", "directionalityltr",
					"directionalityrtl", "indent", "|", "justifyleft",
					"justifycenter", "justifyright", "justifyjustify", "|",
					"touppercase", "tolowercase", "|", "link", "unlink",
					"anchor", "|", "imagenone", "imageright", "imageleft",
					"imagecenter", "|", "simpleupload",
					"insertframe", "insertcode", "pagebreak", "template",
					"background", "|", "horizontal", "date", "time",
					"spechars", "|", "inserttable", "deletetable",
					"insertparagraphbeforetable", "insertrow", "deleterow",
					"insertcol", "deletecol", "mergecells", "mergeright",
					"mergedown", "splittocells", "splittorows", "splittocols",
					"|", "print", "preview", "searchreplace" ] ]
		});
	}
	
	function resetInfo(){
		$("#nlgForm").form("reset");
		document.getElementById("catalogId").value = "";
		// document.getElementById("level").value = "2";
		// document.getElementById("leinfoIconvel").value = "";
		ue.setContent("");
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
	
	function submitInfo(){
		// 验证
		var titleEle = document.getElementById("title");
		var titleVal = $.trim(titleEle.value);
		if(!titleVal){
			$.messager.alert("提示信息", "请填写标题.", "error");
			return;
		}
		titleEle.val = titleVal;
		// 目录信息
		var catalogIdValEle =  document.getElementById("catalogId");
		var catalogIdVal = catalogIdValEle.value;
		if(!catalogIdVal){
			$.messager.alert("提示信息", "请选择所属目录.", "error");
			return;
		}
		// 富文本信息判断
		var contentTxt = $.trim(ue.getContentTxt());
		if(!contentTxt){
			$.messager.alert("提示信息", "请填写健康知识内容.", "error");
			return;
		}
		var maxTxtLen = 100;
		var txtLen = contentTxt.length;
		if(txtLen > maxTxtLen){
			contentTxt = contentTxt.substring(0, maxTxtLen - 3) + "...";
		}
		document.getElementById("summary").value = contentTxt;
		document.getElementById("content").value = ue.getContent();
		// 表单提交
		$("#nlgForm").form("submit", {
			<%
			if(!update)
			{
			%>
			url : "<%=request.getContextPath()%>/msg/nlg_add.action",
			<%
			}else
			{
			%>
			url : "<%=request.getContextPath()%>/msg/nlg_update.action",
			<%
			}
			%>
			dataType : "json",
			onSubmit : function() {
				return true;
			},
			success : function(data) {
				data =  $.parseJSON(data); 
				<%
				if(!update)
				{
				%>
				if(data.state == 1){
					refreshPage("350201");
					$.messager.confirm("确认", "发布成功，需要继续发布吗？", function (r) {
						if (r) {
							resetInfo();
						} else {
							closeIt();
						}
					}); 
				}else{
					$.messager.alert("提示信息", data.msg, "error");
				}
				<%
				}else
				{
				%>
				if(data.state == 1){
					refreshPage("${param.pPageId}");
					refreshPage("350201");
					$.messager.alert("提示信息", "发布成功.", "info",
						function(){
							location.href = "<%=request.getContextPath()%>/msg/nlg_view.action?id=<%=knowledge.getId()%>";
						}
					);
				}else if(data.state == -2){
					$.messager.alert("提示信息", "发布出错，此健康知识已被删除！", "error", function(){
						closeIt();
					});
				}else{
					$.messager.alert("提示信息", data.msg, "error");
				}
				<%
				}
				%>
			}
		});
	}
	
	var selCataWin;
	function selCata(){
		selCataWin = $('<div><iframe src="<%=request.getContextPath()%>/msg/nlgCataSel.jsp" style="width: 99%;height:99%;" border="0" frameborder="0"></iframe></div>').window({
			title : "选择所属目录",
			width : 600,
			height : 500,
			resizable : false,
			collapsible : false,
			minimizable : false,
			maximizable : false,
			modal : true,
			zIndex : 100,
			closable : true
		});
	}
	
	function closeSelCataWin(){
		selCataWin.window("close");
	}
	
	function setSelCata(cataObj){
		if(cataObj){
			document.getElementById("catalogName").value = cataObj.name;
			document.getElementById("catalogId").value = cataObj.id;
		}
	}
</script>
</head>
<body onload="init();">
	<div class="ct">
		<h1 align="center">发布健康知识</h1>
	</div>
	<div class="ct">
		<form method="post" style="margin: 0px; padding: 0px;" id="nlgForm"
			name="nlgForm" action="">
			<table width="100%" border="0" cellspacing="0">
				<tr>
					<td width="50px" align="right" nowrap="nowrap"><font>标题：</font></td>
					<td>
						<input type="text" id="title" name="knowledge.title" style="height: 20px; width: 90%;" maxlength="50"
							value="<%=StringUtil.trim(knowledge.getTitle())%>" />
						<%
						if (update)
						{
						%>
						<input type="hidden" name="knowledge.id" value="<%=knowledge.getId()%>" />
						<%
						}
						%>
					</td>
				</tr>
				<tr height="30">
					<td  align="right" nowrap="nowrap">所属目录：</td>
					<td height="30">
						<input type="text" readonly="readonly" id="catalogName" value="<%=StringUtil.trim(knowledge.getCatalogName())%>" onclick="selCata()" />
						&nbsp;
						<img alt="选择目录" src="<%=request.getContextPath()%>/resources/img/search.png" style="cursor: pointer; margin-bottom: -5px;"
							onclick="selCata();" />
						<input type="hidden" id="catalogId" name="knowledge.catalogId" value="<%=StringUtil.trim(knowledge.getCatalogId())%>" />
					</td>
				</tr>
				<tr>
					<td height="30" align="right" nowrap="nowrap">
						<input type="hidden" id="summary" name="knowledge.summary" />
						<input type="hidden" id="content" name="knowledge.content" />
						健康知识内容：
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div class="ct">
		<script id="editor" type="text/plain" style="height: 300px;"><%=StringUtil.trim(knowledge.getContent())%></script>
	</div>
	<div class="ct" style="text-align: center; margin-top: 15px;">
		<input type="button" id="btnSubmit" name="btnSubmit" onclick="submitInfo();" value="发布" />
		&nbsp;&nbsp;
		<input type="button" id="btnReset" name="btnReset" onclick="closeIt();" value="关闭" />
	</div>
</body>
</html>