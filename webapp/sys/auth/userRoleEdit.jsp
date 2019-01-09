<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.module.sys.bean.User"%>
<%
User user = (User)request.getAttribute("user");
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../../common/inc.jsp"%>
	<title>用户角色</title>
	<style type="text/css">
	</style>
	<script type="text/javascript">
	var userRoleGrid;
	$(function(){
		userRoleGrid = $("#userRoles").datagrid({
			title: "角色列表",
			width: "auto",
			height: 230,
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
			url: "<%=request.getContextPath()%>/sys/userrole_queryRole.action",
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
			}]]
		});
	});
	var subWin;
	function closeIt(){
		try{
			parent.subWin.window("close");
		}catch(e){}
	}
	function save(){
		var selected = userRoleGrid.datagrid("getSelected");
		var roleId=selected ? selected.id : "";
		$("#fm").form("submit", {
			url: "<%=request.getContextPath()%>/sys/userrole_addUserRole.action?roleId="+roleId,
			dataType : "json",
			success : function(data) {
				data =  $.parseJSON(data); 
				if(data.state == 1){
					$.messager.alert("提示信息", data.msg?data.msg:"保存成功。", "info", function(){
						try{
							parent.refreshGrid();
						}catch(e){}
						closeIt();
					});
				}else{
					$.messager.alert("提示信息", data.msg?data.msg:"保存失败。", "error");
				}
			}
		});
	}
</script>
</head>
<body style="padding: 2px;margin: 0px;">
	<form  id="fm" style="margin: 0px;padding: 0px;" method="post" accept-charset="UTF-8">
		<input type="hidden" id="userId" name="userId" value="<%=user.getId()%>" />
	</form>	
	<div class="sectionTitle">查询条件</div>
	<div>
		<form id="filterForm" style="margin: 0px;padding: 0px;" method="post" accept-charset="UTF-8">
			<table class="table">
				<tr>
					<td width="40%" class="td_title">用户名称:</td>
					<td>
						<%=user.getName()%>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div style="padding-top: 10px;">
		<table id="userRoles" class="easyui-datagrid"></table>
	</div>
	
	<div style="padding: 10px;text-align: center;">
		<a class="easyui-linkbutton" href="javascript:save();">保存</a>
		&nbsp;&nbsp;
		<a class="easyui-linkbutton" href="javascript:closeIt();">关闭</a>
	</div>
</body>
</html>