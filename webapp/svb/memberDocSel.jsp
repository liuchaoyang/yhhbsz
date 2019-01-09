<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.yzxt.yh.util.StringUtil"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>选择签约健康管理师</title>
	<style type="text/css">
	body{
		padding: 1px;
		margin: 0px;
	}
	</style>
	<script type="text/javascript"> 
	var docGrid;
	$(function(){
		docGrid = $("#docs").datagrid({
			title: "可签约健康管理师列表",
			width: "auto",
			height: 220,
			idField: "id",
			rownumbers: true,
			pagination: true,
			pageList: [10, 20, 50, 100],
			singleSelect: true,
			nowrap: true,
			striped: true,
			fitColumns: true,
			loadMsg: "数据加载中,请稍候...",
			toolbar: "#toolbar",
			url: "<%=request.getContextPath()%>/svb/memberInfo_queryDocSel.action?orgId=<%=StringUtil.trim((String)request.getAttribute("orgId"))%>",
			frozenColumns: [[{
				field: "ck",
				checkbox: true
			}]],
			columns: [[{
				field: "name",
				title: "健康管理师",
				width: 80,
				align: "center"
			}, {
				field: "phone",
				title: "电话号码",
				width: 130,
				align: "center"
			}, {
				field: "orgName",
				title: "机构名称 ",
				width: 180,
				align: "center"
			}]]
		});
	});
	function refreshGrid(){
		docGrid.datagrid("reload");
		docGrid.datagrid("unselectAll");
		docGrid.datagrid("clearSelections");
	}
	function query(){
		docGrid.datagrid("getPager").pagination({pageNumber: 1});
		$.extend(docGrid.datagrid("options"), {
			pageNumber: 1,
			queryParams: $("#filterForm").serializeObject()
		});
		refreshGrid();
	}
	function clear(){
		document.getElementById("filterForm").reset();
	}
	function ok(){
		var selected = docGrid.datagrid("getSelected");
		if(!selected){
			$.messager.alert("提示信息", "请选择一条记录。", "error");
			return;
		}
		try{
			parent.setSelDoc(selected);
		}catch(e){}
		closeIt();
	}
	function closeIt(){
		try{
			parent.subWin2.window("close");
		}catch(e){}
	}
</script>
</head>
<body>
	<div class="sectionTitle">查询条件</div>
	<div>
		<form id="filterForm">
			<table class="table">
				<tr>
					<td width="20%" class="td_title">健康管理师名称:</td>
					<td>
						<input id="name" name="name" maxlength="100" />
					</td>
					<td width="100">
						<a class="easyui-linkbutton" href="javascript:query()">查询</a>
						&nbsp;
						<a class="easyui-linkbutton" href="javascript:clear()">重置</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div style="text-align: center;padding-top: 10px;">
		<table id="docs" class="easyui-datagrid"></table>
	</div>
	<div style="text-align: center;padding-top: 10px;">
		<a href="#" onclick="ok();" class="easyui-linkbutton">确定</a>
		<a href="#" onclick="closeIt();" class="easyui-linkbutton">关闭</a>
	</div>
</body>
</html>