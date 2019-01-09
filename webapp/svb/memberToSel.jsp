<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<% 
String userId = (String)request.getParameter("id");
%>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
	<title>预警消息</title>
	<style type="text/css">
	body{
		padding: 2px;
	}
	</style>
	<script type="text/javascript"> 
	var archiveGrid;
	$(function(){
		archiveGrid = $("#archives").datagrid({
			/* title: "预警会员列表", */
			width: "auto",
			height: 200,
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
			url: "<%=request.getContextPath()%>/svb/memberInfo_queryDoctor.action?userId=<%=userId%>",
			frozenColumns: [[{
				field: "ck",
				checkbox: true
			}]],
			columns: [[{
				field: "name",
				title: "健康管理师名称",
				width: 80,
				align: "center"
			}, {
				field: "phone",
				title: "电话号码",
				width: 130,
				align: "center"
			}]]
		});
	});
	function query(){
		archiveGrid.datagrid("getPager").pagination({pageNumber: 1});
		$.extend(archiveGrid.datagrid("options"), {
			pageNumber: 1,
			queryParams: $("#filterForm").serializeObject()
		});
		refreshGrid();
	}
	function clear(){
		document.getElementById("filterForm").reset();
	}
	
	function intoMng(closeDlg){
	var selected = archiveGrid.datagrid("getSelected");
		if(!selected){
			$.messager.alert("提示信息","请选择一条记录。","error");
			return;
		}else{
		 var phone=selected.phone;
		 var name=selected.name;
		 var userId=selected.id;
		 var res=0;
		if(res==0){
			$.messager.alert("提示信息", "加入成功.", "info");
			if(!closeDlg){
				needRefreshParent = true;
				parent.assignment(name,phone,userId);
				refreshGrid();
				closeIt();
			}else{
			   alert(99999);
				try{
					parent.refreshGrid();
				}catch(e){}
				closeIt();
			}
		}else{
			$.messager.alert("提示信息", "纳入慢病失败.", "error");
		}
		}
		
		
	}
	
	function closeIt() {
		try{
			parent.subWin.window("close");
		}catch(e){}
	}
	function refreshGrid(){
		archiveGrid.datagrid("reload");
		archiveGrid.datagrid("unselectAll");
		archiveGrid.datagrid("clearSelections");
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
						<input id="userName" name="userName" maxlength="100" />
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
		<table id="archives" class="easyui-datagrid"></table>
		<div style="text-align: center;padding-top: 15px;">
			<a href="#" onclick="intoMng(false);" class="easyui-linkbutton">添加</a>
		</div>
	</div>
</body>
</html>