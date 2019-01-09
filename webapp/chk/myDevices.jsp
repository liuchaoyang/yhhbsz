<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.constant.ConstDevice"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../common/inc.jsp"%>
<title>我的设备</title>
<meta charset="UTF-8">
<meta name="pragma" content="no-cache" />
<meta name="cache-control" content="no-cache" />
<meta name="expires" content="0" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/css/flow.css" />
<style type="text/css">
</style>
<script type="text/javascript">
var myDevicesGrid;
$(function() {
	myDevicesGrid = $("#myDevices").datagrid({
		title: "我的设备列表",
		loadMsg: "数据加载中,请稍候...",
		width: "auto",
		height: "auto",
		animate: true,
		nowrap: true,
		striped: true,
		pagination: true,
		rownumbers: true,
		fitColumns: true,
		idField: "id",
		singleSelect: false,
		emptyMsg: "没有可显示的记录.",
		url: null,// "<%=request.getContextPath()%>/chk/userDevice_queryUserDevices.action",
		frozenColumns: [ [ {
			field: "ck",
			checkbox: true
		} ] ],
		columns: [ [ {
			field: "deviceName",
			title: "设备名称",
			width: 60,
			align: "center"
		}, {
			field: "deviceSnExt",
			title: "用户编号",
			width: 80,
			align: "center"
		}, {
			field: "deviceSn",
			title: "设备编号",
			width: 100,
			align: "center"
		}, {
			field: "createTime",
			title: "创建时间",
			width: 60,
			align: "center"
		} , {
			field: "updateTime",
			title: "更新时间",
			width: 60,
			align: "center"
		}] ],
		onSortColumn: function(sort, order) {
		},
		toolbar: "#dlg-toolbar"
	});
	var p = myDevicesGrid.datagrid("getPager");
	if(p){
		$(p).pagination({
			pageNumber: 1,
			showPageList: true,
			pageList: [10, 20, 50, 100]
		});
	}
	query();
});

function refreshGrid(){
	myDevicesGrid.datagrid("reload");
	myDevicesGrid.datagrid("unselectAll");
	myDevicesGrid.datagrid("clearSelections");
}

function query(){
	myDevicesGrid.datagrid("getPager").pagination({pageNumber: 1});
	var option = myDevicesGrid.datagrid("options");
	option.pageNumber = 1;
	option.url = "<%=request.getContextPath()%>/chk/userDevice_queryUserDevices.action";
	option.queryParams = $("#filterForm").serializeObject();
	refreshGrid();
}

function clear(){
	document.getElementById("deviceSn").value = "";
}

var subWin;
function add() {
	subWin = $('<div><iframe src="<%=request.getContextPath()%>/chk/userDevice_toEdit.action" style="width: 99%;height:99%;" border="0" frameborder="0" scoll="no"></iframe></div>').window({
		title: "新增用户设备",
		width: 400,
		height: 250,
		resizable: false,
		collapsible: false,
		minimizable: false,
		maximizable: false,
		modal: true,
		zIndex: 100,
		closable: true
	});
};

function modify() {
	var selections = myDevicesGrid.datagrid("getSelections");
	if(selections && selections.length == 1){
		subWin = $('<div><iframe src="<%=request.getContextPath()%>/chk/userDevice_toEdit.action?id=' + selections[0].id + '" style="width: 99%;height:99%;" border="0" frameborder="0" scoll="no"></iframe></div>').window({
			title: "修改用户设备",
			width: 400,
			height: 300,
			resizable: false,
			collapsible: false,
			minimizable: false,
			maximizable: false,
			modal: true,
			zIndex: 100,
			closable: true
		});
	}else if(selections && selections.length > 1){
		$.messager.alert("提示信息", "一次只能修改一条记录.", "error");
	}else{
		$.messager.alert("提示信息", "请选择一条记录.", "error");
	}
};

function del(){
	var selections = myDevicesGrid.datagrid("getSelections");
	var len = selections ? selections.length : 0;
	if(len == 0){
		$.messager.alert("提示信息", "请选择需要删除的记录.", "error");
		return;
	}
	$.messager.confirm("警告信息", "确定删除吗?", function(r){
		if(r){
			var ids = "";
			for(var i=0; i<len; i++){
				if(i > 0){
					ids = ids + ",";
				}
				ids = ids + selections[i].id;
			}
			$.ajax({
				type: "GET",
				dataType: "json",
				url: "<%=request.getContextPath()%>/chk/userDevice_delete.action?ids=" + ids,
				async: false,
				timeout: 30000,
				success: function(data){
					if(data.state > 0){
						$.messager.alert("提示信息", "删除数据成功.", "info");
						refreshGrid();
					}else{
						$.messager.alert("提示信息", data.msg ? data.msg : "删除失败.", "error");
					}
				},
				error: function(){
					$.messager.alert("提示信息", "删除数据失败.", "error");
			 	}
			});
		}
	});
}
</script>
</head>
<body>
	<div class="sectionTitle">查询条件</div>
	<div>
		<form id="filterForm">
			<table class="table">
				<tr>
					<td width="15%" class="td_title">设备编号:</td>
					<td>
						<input id="deviceSn" name="deviceSn" maxlength="100" />
					</td>
				</tr>
				<tr>
					<td colspan="2" class="td_oper">
						<a class="easyui-linkbutton" href="javascript:query()">查询</a>
						&nbsp;&nbsp;
						<a class="easyui-linkbutton" href="javascript:clear()">重置</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div id="dlg-toolbar" style="padding: 2px 0">
		<table cellpadding="0" cellspacing="0">
			<tr>
				<td style="padding-left: 2px">
					<a href="javascript:add();" class="easyui-linkbutton">新增</a>
					<a href="javascript:modify();" class="easyui-linkbutton">修改</a>
					<a href="javascript:del();" class="easyui-linkbutton">删除</a>
				</td>
			</tr>
		</table>
	</div>
	<div style="padding-top: 15px;">
		<table id="myDevices" class="easyui-datagrid"></table>
	</div>
</body>
</html>