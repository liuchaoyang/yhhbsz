<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import ="com.yzxt.yh.constant.Constant"%>
<%@ include file="../common/inc.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE HTML>
<html>
<head>
<title></title>
<meta charset="UTF-8">
<meta name="pragma" content="no-cache" />
<meta name="cache-control" content="no-cache" />
<meta name="expires" content="0" />
<meta name="keywords" content="keyword1,keyword2,keyword3" />
<meta name="description" content="" />
<style type="text/css">
</style>
<script type="text/javascript">
	var grid;
	$(function(){
		grid = $("#topicBox").datagrid({
			loadMsg : "数据加载中,请稍候...",
			width : 870,
			height : 460,
			animate : false,
			striped : true,
			singleSelect : true,
			url : "<%=request.getContextPath()%>/msg/info_queryTopicByPage.action",
			pagination : true,
			idField : "id",
			rownumbers : true,
			fitColumns : true,
			columns : [ [ 
			{
				field : "ck",
				checkbox : true
			}, {
				field : "name",
				title : "专题名称",
				width : 50,
				align : "center"
			}, {
				field : "detail",
				title : "简介",
				width : 120,
				align : "center"
			} ] ],
			onSortColumn : function(sort, order) {
			},
			toolbar : "#dlg-toolbar"
		});
		var p = grid.datagrid("getPager");
		if (p) {
			$(p).pagination({
				pageNumber : 1,
				showPageList : false
			});
		}
	});
	
	function search(val){
		grid.datagrid("getPager").pagination({pageNumber : 1});
		var option = grid.datagrid("options");
		option.pageNumber = 1;
		var queryParams = option.queryParams;
		queryParams.keyword = trim(val);
		reloadGrid();
	}
	
	function reloadGrid(){
		grid.datagrid("reload");
		grid.datagrid("unselectAll");
		grid.datagrid("clearSelections");
	}
	
	function closeWin(){
		try{
			parent.closeSelTopicWin();
		}catch(e){}
	}
	
	function selTopic(){
		try{
			var selections = grid.datagrid("getSelections");
			if(!selections || selections.length == 0){
				$.messager.alert("提示信息", "请选择一条的记录", "info");
				return;
			}
			parent.setSelTopic(selections[0]);
			parent.closeSelTopicWin();
		}catch(e){alert(e)}
	}
</script>
</head>
<body style="overflow: hidden;">
	<div id="dlg-toolbar" style="padding: 2px 0;">
		<table cellpadding="0" cellspacing="0" style="width: 100%">
			<tr>
				<td style="text-align: right; padding-right: 10px">
					<input class="easyui-searchbox" data-options="prompt:'请输入专题名称.',searcher:search" style="width: 300px;" />
				</td>
			</tr>
		</table>
	</div>
	<div
		style="width: 100%; height: 100%; text-align: center; position: relative;">
		<table id="mytab" align="center" cellpadding="0" cellspacing="0"
			border="0" width="98%" height="95%">
			<tr>
				<td><table id="topicBox"></table></td>
			</tr>
			<tr>
				<td style="padding-top: 10px;">
					<a href="javascript: void(0)" onclick="selTopic();" class="easyui-linkbutton">确定</a>
					<a href="javascript: void(0)" onclick="closeWin();" class="easyui-linkbutton">关闭</a>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>