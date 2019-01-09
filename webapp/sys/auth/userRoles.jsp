<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../../common/inc.jsp"%>
	<title>用户角色列表</title>
	<style type="text/css">
	</style>
	<script type="text/javascript">
	var userRoleGrid;
	$(function(){
		userRoleGrid = $("#userRoles").datagrid({
			title: "用户角色列表",
			width: "auto",
			height: "auto",
			idField: "userId",
			rownumbers: true,
			pagination: true,
			pageList: [10, 20, 50, 100],
			singleSelect: true,
			nowrap: true,
			striped: true,
			fitColumns: true,
			loadMsg: "数据加载中,请稍候...",
			toolbar: "#toolbar",
			url: "<%=request.getContextPath()%>/sys/userrole_query.action",
			frozenColumns: [[{
				field: "ck",
				checkbox: true
			}]],
			columns: [[{
				field: "userName",
				title: "医生名称",
				width: 80,
				align: "center"
			}, {
				field: "orgName",
				title: "所属组织",
				width: 200,
				align: "center"
			}, {
				field: "roleName",
				title: "角色名称",
				width: 200,
				align: "center"
			}]]
		});
	});
	function refreshGrid(){
		userRoleGrid.datagrid("reload");
		userRoleGrid.datagrid("unselectAll");
		userRoleGrid.datagrid("clearSelections");
	}
	function query(){
		userRoleGrid.datagrid("getPager").pagination({pageNumber: 1});
		$.extend(userRoleGrid.datagrid("options"), {
			pageNumber: 1,
			queryParams: $("#filterForm").serializeObject()
		});
		refreshGrid();
	}
	function clear(){
		document.getElementById("filterForm").reset();
	}
	var subWin;
	function edit(){
		var selected = userRoleGrid.datagrid("getSelected");
		if(!selected){
			$.messager.alert("提示信息","请选择一条记录。","error");
			return;
		}
		var userId=selected.userId;
		subWin = $('<div><iframe src="<%=request.getContextPath()%>/sys/userrole_toUserRoleEdit.action?userId='+userId+'"  style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>').window({
			title: "用户角色配置",
			width: 500,
			height: 410,
			resizable: false,
			collapsible: false,
			minimizable: false,
			maximizable: false,
			modal: true,
			zIndex: 100,
			closable: true
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
					<td width="10%" class="td_title">医生名称:</td>
					<td>
						<input id="userName" name="userName" maxlength="100" />
					</td>
					<td width="140px;" class="td_oper" nowrap="nowrap">
						<a href="#" onclick="query()" class="easyui-linkbutton">查询</a> &nbsp;
						<a href="#" onclick="clear()" class="easyui-linkbutton">重置</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div style="padding-top: 10px;">
		<table id="userRoles" class="easyui-datagrid"></table>
	</div>
	<div id="toolbar" style="padding: 2px 0px;display: none;">
		<table>
			<tr>
				<td style="padding-left: 2px;">
					<a href="javascript:edit();" class="easyui-linkbutton">配置角色</a>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>