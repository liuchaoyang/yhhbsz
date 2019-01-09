<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.module.sys.bean.User"%>
<%
	User operUser = (User)session.getAttribute("userInfo");
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../../common/inc.jsp"%>
	<title>组织管理员列表</title>
	<style type="text/css">
	</style>
	<script type="text/javascript">
	var treeGrid;
	$(function(){
		treeGrid=$("#orgs").treegrid({
			title: "组织结构",
			url: "<%=request.getContextPath()%>/sys/org_listChildren.action",
			idField: "id",
			treeField: "name",
			width: "auto",
			height: 300,
			fitColumns: true,
			pagination: false,
			frozenColumns: [[{
				field: "ck",
				checkbox: true
			}]],
			columns: [[{
				field: "name",
				title: "组织名称",
				width: 180,
				align: "left"
			}, {
				field: "showText",
				title: "系统名称",
				width: 150,
				align: "left"
			}, {
				field: "phone",
				title: "联系电话",
				width: 90,
				align: "left"
			}, {
				field: "address",
				title: "组织地址",
				width: 250,
				align: "left"
			}]],
			onCheck: function(row){
				query(row.id);
			}
		});
	});
	
	var adminGrid;
	$(function(){
		adminGrid = $("#admins").datagrid({
			title: "组织管理员列表",
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
			frozenColumns: [[{
				field: "ck",
				checkbox: true
			}]],
			columns: [[{
				field: "name",
				title: "管理员姓名",
				width: 80,
				align: "center"
			}, {
				field: "account",
				title: "管理员帐号",
				width: 200,
				align: "center"
			}, {
				field: "orgName",
				title: "所属组织",
				width: 200,
				align: "center"
			}, {
				field: "phone",
				title: "手机号",
				width: 200,
				align: "center"
			}, {
				field: "email",
				title: "电子邮箱",
				width: 200,
				align: "center"
			}, {
				field: "createTime",
				title: "创建时间",
				width: 140,
				align: "center"
			}]]
		});
	});
	function refreshGrid(){
		adminGrid.datagrid("reload");
	}
	function query(orgId){
		var option = adminGrid.datagrid("options");
		option.url = "<%=request.getContextPath()%>/sys/admin_query.action?orgId="+orgId;
		adminGrid.datagrid("reload");
		adminGrid.datagrid("clearSelections");
	}
	
	var subWin;
	function add(){
		var selected = treeGrid.treegrid("getSelected");
		if(!selected){
			$.messager.alert("提示信息","请选择一条记录。","error");
			return;
		}
		subWin = $('<div><iframe src="<%=request.getContextPath()%>/sys/admin_toEdit.action?orgId='+selected.id+'" style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>').window({
			title: "新增机构管理员",
			width: 500,
			height: 300,
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
		var selected = adminGrid.treegrid("getSelected");
		if(!selected){
			$.messager.alert("提示信息","请选择一条记录。","error");
			return;
		}
		subWin = $('<div><iframe src="<%=request.getContextPath()%>/sys/admin_toEdit.action?id='+selected.id+'" style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>').window({
			title: "新增机构管理员",
			width: 500,
			height: 300,
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
	<table id="orgs"></table>
	<div style="padding-top: 10px;">
		<table id="admins" class="easyui-datagrid"></table>
	</div>
	<div id="toolbar" style="padding: 2px 0px;display: none;">
		<table>
			<tr>
				<td style="padding-left: 2px;">
					<a href="javascript:add();" class="easyui-linkbutton">新增</a>
					<a href="javascript:update();" class="easyui-linkbutton">修改</a>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>