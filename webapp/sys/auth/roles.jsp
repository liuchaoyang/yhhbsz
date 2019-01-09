<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../../common/inc.jsp"%>
	<title>角色列表</title>
	<style type="text/css">
	</style>
	<script type="text/javascript"> 
	var roleGrid;
	$(function(){
		roleGrid = $("#roles").datagrid({
			title: "角色列表",
			width: "auto",
			height: "auto",
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
			url: "<%=request.getContextPath()%>/sys/role_query.action",
			frozenColumns: [[{
				field: "ck",
				checkbox: true
			}]],
			columns: [[{
				field: "roleName",
				title: "角色名称",
				width: 80,
				align: "center"
			}, {
				field: "memo",
				title: "角色说明",
				width: 200,
				align: "center"
			}, {
				field: "createTime",
				title: "创建时间",
				width: 140,
				align: "center"
			}, {
				field: "updateTime",
				title: "修改时间",
				width: 140,
				align: "center"
			}]]
		});
	});
	function refreshGrid(){
		roleGrid.datagrid("reload");
		roleGrid.datagrid("unselectAll");
		roleGrid.datagrid("clearSelections");
	}
	function query(){
		roleGrid.datagrid("getPager").pagination({pageNumber: 1});
		$.extend(roleGrid.datagrid("options"), {
			pageNumber: 1,
			queryParams: $("#filterForm").serializeObject()
		});
		refreshGrid();
	}
	function clear(){
		document.getElementById("filterForm").reset();
	}
	var subWin;
	function add(){
		subWin = $('<div><iframe src="<%=request.getContextPath()%>/sys/role_toDetail.action?operType=add" style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>').window({
			title: "新增角色",
			width: 400,
			height: 260,
			resizable: false,
			collapsible: false,
			minimizable: false,
			maximizable: false,
			modal: true,
			zIndex: 100,
			closable: true
		});
	}
	function update(){
		var selected = roleGrid.datagrid("getSelected");
		if(!selected){
			$.messager.alert("提示信息","请选择一条记录。","error");
			return;
		}
		
		subWin = $('<div><iframe src="<%=request.getContextPath()%>/sys/role_toDetail.action?operType=update&id=' + selected.id + '" style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>').window({
			title: "修改角色",
			width: 400,
			height: 260,
			resizable: false,
			collapsible: false,
			minimizable: false,
			maximizable: false,
			modal: true,
			zIndex: 100,
			closable: true
		});
	}
	function configRoleRess() {
		var selected = roleGrid.datagrid("getSelected");
		if(!selected){
			$.messager.alert("提示信息","请选择一条记录。","error");
		}else{
			subWin = $('<div><iframe src="<%=request.getContextPath()%>/sys/role_toRoleRess.action?roleId=' + selected.id + '" style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>').window({
				title: "角色菜单配置",
				width: 400,
				height: 480,
				resizable: false,
				collapsible: false,
				minimizable: false,
				maximizable: false,
				modal: true,
				zIndex: 100,
				closable: true
			});
		}
	}
	
	
		function deleteRole(){
			var selected = roleGrid.datagrid("getSelected");
			if(!selected){
				$.messager.alert("提示信息","请选择一条记录。","error");
				
			}else{
		var id=selected.id;
		$("#id").val(id);
		$("#filterForm123").form("submit", {
			url: "<%=request.getContextPath()%>/sys/role_delete.action?id="+id,
			dataType : "json",
			success : function(data) {
				data =  $.parseJSON(data); 
				if(data.state == 1){
					$.messager.alert("提示信息", data.msg?data.msg:"删除成功。", "info", function(){
						try{
							refreshGrid();
						}catch(e){}
						closeIt();
					});
				}else{
					$.messager.alert("提示信息", data.msg?data.msg:"删除失败。", "error");
				}
			}
		});
		  
		 
	
		
		}
		
		
		
	}
		
		
 </script>
</head>
<body>
	<div class="sectionTitle">查询条件</div>
	<div>
		<form id="filterForm">
			<table class="table">
				<tr>
					<td width="10%" class="td_title">角色名称:</td>
					<td>
						<input id="roleName" name="roleName" maxlength="100" />
						
					</td>
					<td width="140px;" class="td_oper" nowrap="nowrap">
						<a class="easyui-linkbutton" href="javascript:query()">查询</a>
						&nbsp;
						<a class="easyui-linkbutton" href="javascript:clear()">重置</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<form id="filterForm123">
	<input type="hidden" id="id" name="id" maxlength="100" />
	</form>
	<div style="padding-top: 10px;">
		<table id="roles" class="easyui-datagrid"></table>
	</div>
	<div id="toolbar" style="padding: 2px 0px;display: none;">
		<table>
			<tr>
				<td style="padding-left: 2px;">
					<a href="javascript:add();" class="easyui-linkbutton">新增</a>
					<a href="javascript:update();" class="easyui-linkbutton">修改</a>
					<a href="javascript:deleteRole();" class="easyui-linkbutton">删除</a>
					<a href="javascript:configRoleRess();" class="easyui-linkbutton">配置菜单</a>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>