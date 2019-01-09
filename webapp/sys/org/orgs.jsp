<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.Collection"%>
<%@ page import="com.yzxt.fw.server.ConstSv"%>
<%@ page import="com.yzxt.yh.constant.ConstRole"%>
<%@ page import="com.yzxt.yh.module.sys.bean.User"%>
<%
User user = (User)request.getSession().getAttribute(ConstSv.SESSION_USER_KEY);
Collection<String> roles = user!=null ? user.getRoles() : null;
boolean isSA = roles.contains(ConstRole.ADMIN);
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../../common/inc.jsp"%>
	<title>组织管理</title>
	<script type="text/javascript">
	var treeGrid;
	$(function(){
		treeGrid=$("#orgs").treegrid({
			title: "组织结构",
			url: "<%=request.getContextPath()%>/sys/org_listChildren.action",
			idField: "id",
			treeField: "name",
			fit: true,
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
				field: "mnemonicCode",
				title: "组织助记码",
				width: 80,
				align: "left"
			}, {
				field: "showText",
				title: "系统名称",
				width: 150,
				align: "left"
			}, {
				field: "logoPath",
				title: "组织图标",
				width: 60,
				align: "center",
				formatter: function(val){
					if(val){
						return '<a href="javascript:showLogo(\'' + val + '\');">查看</a>';
					}
					return "";
				}
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
			toolbar: "#toolbar"
		});
	});
	var subWin;
	function addTopOrg(){
		subWin = $('<div><iframe src="<%=request.getContextPath()%>/sys/org_toDetail.action?operType=addTop" style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>').window({
			title: "新增顶级组织",
			width: 500,
			height: 420,
			resizable: false,
			collapsible: false,
			minimizable: false,
			maximizable: false,
			modal: true,
			zIndex: 100,
			closable: true
		});
	}
	function addSubOrg(){
		var selected = treeGrid.treegrid("getSelected");
		if(!selected){
			$.messager.alert("提示信息","请选择一条记录。","error");
			return;
		}
		if(selected.level>=5){
			$.messager.alert("提示信息","最多增加五级组织。","error");
			return;
		}
		subWin = $('<div><iframe src="<%=request.getContextPath()%>/sys/org_toDetail.action?operType=addSub&parentId=' + selected.id + '" style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>').window({
			title: "新增子组织",
			width: 500,
			height: 420,
			resizable: false,
			collapsible: false,
			minimizable: false,
			maximizable: false,
			modal: true,
			zIndex: 100,
			closable: true
		});
	}
	function updateOrg(){
		var selected = treeGrid.treegrid("getSelected");
		if(!selected){
			$.messager.alert("提示信息","请选择一条记录。","error");
			return;
		}
		subWin = $('<div><iframe src="<%=request.getContextPath()%>/sys/org_toDetail.action?operType=update&id=' + selected.id + '" style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>').window({
			title: "编辑组织",
			width: 500,
			height: 350,
			resizable: false,
			collapsible: false,
			minimizable: false,
			maximizable: false,
			modal: true,
			zIndex: 100,
			closable: true
		});
	}
	function openLogin(){
		var selected = treeGrid.treegrid("getSelected");
		if(!selected){
			$.messager.alert("提示信息","请选择一条记录。","error");
			return;
		}
		window.open("<%=request.getContextPath()%>/sys/wel_login.action?orgId="+(selected.mnemonicCode?selected.mnemonicCode:selected.id), "_blank");
	}
	function refreshGrid(){
		treeGrid.treegrid("reload");
		treeGrid.treegrid("unselectAll");
		treeGrid.treegrid("clearSelections");
	}
	function showLogo(val){
		window.open("<%=request.getContextPath()%>/msg/img.jsp?pt=" + val, "orgLogo");
	}
	</script>
</head>
<body>
	<table id="orgs"></table>
	<div id="toolbar" style="padding:2px 0;display: none;">
		<table style="width:100%;">
			<tr>
				<td style="padding-left:2px">
					<%
					if(isSA)
					{
					%>
					<a href="javascript:addTopOrg()" class="easyui-linkbutton">新增顶级组织</a>
					<%
					}
					%>
					<a href="javascript:addSubOrg()" class="easyui-linkbutton">新增子组织</a>
					<a href="javascript:updateOrg()" class="easyui-linkbutton">编辑组织</a>
					<a href="javascript:openLogin()" class="easyui-linkbutton">打开登录地址</a>
				</td>
			</tr>
		</table>
	</div>
	<div style="display: none;">
		<iframe id="ifrHide"></iframe>
	</div>
</body>
</html>
