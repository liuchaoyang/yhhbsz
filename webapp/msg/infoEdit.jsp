<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.module.msg.bean.Information"%>
<%@ page import="com.yzxt.yh.module.msg.bean.InfoCatalog"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	boolean update = (Boolean)request.getAttribute("update");
	Information information = (Information)request.getAttribute("information");
	List<InfoCatalog> infoColumns = (List<InfoCatalog>)request.getAttribute("infoColumns");
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title></title>
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
					"imagecenter", "|", "simpleupload", "attachment",
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
		$("#infoForm").form("reset");
		// document.getElementById("title").value = "";
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
		var infoIconEle = document.getElementById("infoIcon");
		var infoIconVal = infoIconEle.value;
		<%-- 如果是新建资讯需要判断附件是否存在 --%>
		<%
		if(!update)
		{
		%>
		var infoIconEle = document.getElementById("infoIcon");
		var infoIconVal = infoIconEle.value;
		if(!infoIconVal){
			$.messager.alert("提示信息", "请选择图标文件.", "error");
			return;
		}
		var iconExt = infoIconVal.lastIndexOf('.') > -1 ? infoIconVal.substring(infoIconVal.lastIndexOf('.')) : "";
		iconExt = iconExt.toLowerCase();
		if(iconExt != ".gif" && iconExt != ".jpg" && iconExt != ".jpeg" && iconExt != ".png"){
			$.messager.alert("提示信息", "请选择图标文件.", "error");
			return;
		}
		// 图标文件大小暂时无法判断
		<%
		}else
		{
		%>
		if(infoIconVal){
			var iconExt = infoIconVal.lastIndexOf('.') > -1 ? infoIconVal.substring(infoIconVal.lastIndexOf('.')) : "";
			iconExt = iconExt.toLowerCase();
			if(iconExt != ".gif" && iconExt != ".jpg" && iconExt != ".jpeg" && iconExt != ".png"){
				$.messager.alert("提示信息", "请选择图标文件.", "error");
				return;
			}
		}
		<%
		}
		%>
		// 栏目信息
		var belongColumnsVal = $("#belongColumns").combobox("getValue");
		if(!belongColumnsVal || belongColumnsVal == ","){
			$.messager.alert("提示信息", "请选择所属栏目.", "error");
			return;
		}
		// 资讯来源
		var infoSrcEle = document.getElementById("infoSrc");
		infoSrcEle.value = $.trim(infoSrcEle.value);
		// 富文本信息判断
		var contentTxt = $.trim(ue.getContentTxt());
		if(!contentTxt){
			$.messager.alert("提示信息", "请填写资讯内容.", "error");
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
		$("#infoForm").form("submit", {
			<%
			if(!update)
			{
			%>
			url : "<%=request.getContextPath()%>/msg/info_add.action",
			<%
			}else
			{
			%>
			url : "<%=request.getContextPath()%>/msg/info_update.action",
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
					refreshPage("350101");
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
					refreshPage("350101");
					$.messager.alert("提示信息", "发布成功.", "info",
						function(){
							location.href = "<%=request.getContextPath()%>/msg/info_view.action?id=<%=information.getId()%>";
						}
					);
				}else if(data.state == -2){
					$.messager.alert("提示信息", "发布出错，此资讯已被删除！", "error", function(){
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
	
	var selTopicWin;
	function selTopic(){
		selTopicWin = $('<div><iframe src="<%=request.getContextPath()%>/msg/topicSel.jsp" style="width: 99%;height:99%;" border="0" frameborder="0"></iframe></div>').window({
			title : "选择所属专题",
			width : 900,
			height : 560,
			resizable : false,
			collapsible : false,
			minimizable : false,
			maximizable : false,
			modal : true,
			zIndex : 100,
			closable : true
		});
	}
	
	function closeSelTopicWin(){
		selTopicWin.window("close");
	}
	
	function setSelTopic(topicObj){
		if(topicObj){
			document.getElementById("belongTopicName").value = topicObj.name;
			document.getElementById("belongTopic").value = topicObj.id;
		}
	}
	
	function clearTopic(){
		document.getElementById("belongTopicName").value = "";
		document.getElementById("belongTopic").value = "";
	}
	</script>
</head>
<body onload="init();">
	<div class="ct">
		<h1 align="center">发布资讯</h1>
	</div>
	<div class="ct">
		<form method="post" enctype="multipart/form-data" accept-charset="UTF-8" style="margin: 0px; padding: 0px;"
			id="infoForm" name="infoForm" action="">
			<table width="100%" border="0" cellspacing="0">
				<tr>
					<td width="50px" align="right" nowrap="nowrap"><font>标题：</font></td>
					<td>
						<input type="text" id="title" name="information.title" style="height: 20px;width: 90%;" maxlength="50" value="<%=StringUtil.trim(information.getTitle())%>" />
						<%
						if(update)
						{
						%>
						<input type="hidden" name="information.id" value="<%=information.getId()%>" />
						<%
						}
						%>
					</td>
				</tr>
				<tr height="30">
					<td align="right" nowrap="nowrap"><font>是否置顶：</font></td>
					<td>
						<select id="level" name="information.level" style="height: 24px;width: 120px;">
							<option value="1" <%=(information.getLevel() == 1 ? "selected=\"selected\"" : "")%>>是</option>
							<option value="2" <%=(information.getLevel() != 1 ? "selected=\"selected\"" : "")%>>否</option>
						</select>
						&nbsp;&nbsp;&nbsp;&nbsp;
						图标文件：
						<input type="file" id="infoIcon" name="infoIcon" style="height: 24px;width: 300px;" accept=".gif,.jpg,.jpeg,.png,.JPG" value="" />&nbsp;
						<span style="font-size: 12px;">(最大2M,格式为.gif,.jpg,.jpeg,.png,.JPG)</span>
					</td>
				</tr>
				<%
				if(update)
				{
				%>
				<tr>
					<td align="right" nowrap="nowrap"><font>已传图标：</font></td>
					<td>
						<a href="<%=request.getContextPath()%>/msg/img.jsp?pt=<%=information.getIconFilePath()%>" target="_blank"><%=information.getIconFileName()%></a>
					</td>
				</tr>
				<%
				}
				%>
				<tr>
					<td height="30" align="right" nowrap="nowrap">所属栏目：</td>
					<td height="30">
						<select class="easyui-combobox" id="belongColumns" name="information.belongColumns" data-options="multiple:true" style="height: 24px;width: 120px;" multiple="multiple">
							<%
							String belongColumns= information.getBelongColumns();
							belongColumns = "," + belongColumns + ",";
							for(InfoCatalog infoColumn : infoColumns)
							{
							%>
							<option value="<%=infoColumn.getId()%>" <%=(belongColumns.indexOf("," + infoColumn.getId() +",") > -1 ? "selected=\"selected\"" : "") %>><%=infoColumn.getName()%></option>
							<%
							}
							%>
						</select>
						<!--
						&nbsp;&nbsp;&nbsp;&nbsp;
						所属专题：
						<input type="text" readonly="readonly" id="belongTopicName" value="<%=StringUtil.trim(information.getBelongTopicName())%>" onclick="selTopic();" />
						&nbsp;
						<img id="selUserImg" alt="选择专题" src="<%=request.getContextPath()%>/resources/img/search.png" style="cursor: pointer;margin-bottom: -5px;" onclick="selTopic();" />
						&nbsp;&nbsp;
						<img id="selUserImg" alt="取消选择" src="<%=request.getContextPath()%>/resources/img/del2.png" style="cursor: pointer;margin-bottom: -5px;" onclick="clearTopic();" />
						<input type="hidden" id="belongTopic" name="information.belongTopic" value="<%=StringUtil.trim(information.getBelongTopic())%>" />
						-->
					</td>
				</tr>
				<tr>
					<td align="right" nowrap="nowrap"><font>资讯来源：</font></td>
					<td>
						<input type="text" maxlength="50" style="height: 20px;width: 117px;" id="infoSrc" name="information.src" value="<%=StringUtil.trim(information.getSrc())%>" />
					</td>
				</tr>
				<tr>
					<td height="30" align="right" nowrap="nowrap">
						<input type="hidden" id="summary" name="information.summary" />
						<input type="hidden" id="content" name="information.content" />
						资讯内容：
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div class="ct">
		<script id="editor" type="text/plain" style="height: 300px;"><%=StringUtil.trim(information.getContent())%></script>
	</div>
	<div class="ct" style="text-align: center; margin-top: 15px;">
		<input type="button" id="btnSubmit" name="btnSubmit" onclick="submitInfo();" value="发布" />
		&nbsp;&nbsp;
		<input type="button" id="btnReset" name="btnReset" onclick="closeIt();" value="关闭" />
	</div>
</body>
</html>