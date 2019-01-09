<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.module.sys.bean.User"%>
<%
	User operUser = (User)session.getAttribute("userInfo");
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../../common/inc.jsp"%>
	<title>客户列表</title>
	<style type="text/css">
	</style>
	<script type="text/javascript">
	var custGrid;
	$(function(){
		custGrid = $("#custs").datagrid({
			title: "用户列表",
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
			url: "<%=request.getContextPath()%>/sys/cust_query.action",
			frozenColumns: [[{
				field: "ck",
				checkbox: true
			}]],
			columns: [[{
				field: "name",
				title: "姓名",
				width: 80,
				align: "center"
			}, {
				field: "idCard",
				title: "身份证号码",
				width: 140,
				align: "center"
			}, {
				field: "sex",
				title: "性别",
				width: 60,
				align: "center",
				formatter:function(value){
					if(value == 1){
						return "男";
					}else if(value == 2){
						return "女";
					}
					return "";
				}
			}, {
				field: "phone",
				title: "手机号",
				width: 100,
				align: "center"
			}, {
				field: "memberId",
				title: "是否签约",
				width: 80,
				align: "center",
				formatter: function(val){
					if(val){
						return "是";
					}else{
						return "否";
					}
				}
			}, {
				field: "doctorName",
				title: "签约医生",
				width: 80,
				align: "center"
			}, {
				field: "orgName",
				title: "隶属机构",
				width: 120,
				align: "center"
			}]]
		});
	});
	function refreshGrid(){
		custGrid.datagrid("reload");
		custGrid.datagrid("unselectAll");
		custGrid.datagrid("clearSelections");
	}
	function query(){
		custGrid.datagrid("getPager").pagination({pageNumber: 1});
		$.extend(custGrid.datagrid("options"), {
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
		subWin = $('<div><iframe src="<%=request.getContextPath()%>/sys/cust_toDetail.action?operType=add" style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>').window({
			title: "新增客户",
			width: 600,
			height: 470,
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
		var selected = custGrid.datagrid("getSelected");
		if(!selected){
			$.messager.alert("提示信息","请选择一条记录。","error");
			return;
		}
		subWin = $('<div><iframe src="<%=request.getContextPath()%>/sys/cust_toDetail.action?operType=update&id=' + selected.userId + '" style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>').window({
			title: "修改客户",
			width: 600,
			height: 470,
			resizable: false,
			collapsible: false,
			minimizable: false,
			maximizable: false,
			modal: true,
			zIndex: 100,
			closable: true
		});
	}
	function view(){
		var selected = custGrid.datagrid("getSelected");
		if(!selected){
			$.messager.alert("提示信息","请选择一条记录。","error");
			return;
		}
		subWin = $('<div><iframe src="<%=request.getContextPath()%>/sys/cust_toDetail.action?operType=view&id=' + selected.userId + '" style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>').window({
			title: "修改客户",
			width: 600,
			height: 450,
			resizable: false,
			collapsible: false,
			minimizable: false,
			maximizable: false,
			modal: true,
			zIndex: 100,
			closable: true
		});
	}
	function viewCust(){
		var selected = custGrid.datagrid("getSelected");
		if(!selected){
			$.messager.alert("提示信息","请选择一条记录。","error");
			return;
		}
		window.open("<%=request.getContextPath()%>/sys/cust_toPersonalDetail.action?operType=pd&id=" + selected.userId, "custInfo",
			"fullscreen=yes,scrollbars=yes,resizable=yes");
	}
	function goCustMedRecords(){
		var selected = custGrid.datagrid("getSelected");
		if(!selected){
			$.messager.alert("提示信息","请选择一条记录。","error");
			return;
		}
		top.addTab("电子病历管理", "<%=request.getContextPath()%>/ach/emr_toCustEmrs.action?custId=" + selected.userId);
	}
	function goCustDossiers(){
		var selected = custGrid.datagrid("getSelected");
		if(!selected){
			$.messager.alert("提示信息","请选择一条记录。","error");
			return;
		}
		top.addTab("图片病历管理", "<%=request.getContextPath()%>/ach/dossier_toMyDossier.action?custId=" + selected.userId);
	}
</script>
</head>
<body>
	<div class="sectionTitle">查询条件</div>
	<div>
		<form id="filterForm">
			<table class="table">
				<tr>
					<td width="20%" class="td_title">姓名:</td>
					<td width="30%">
						<input type="text" id="name" name="name" maxlength="100" />
					</td>
					<td width="20%" class="td_title">身份证号码:</td>
					<td>
						<input type="text" id="idCard" name="idCard" maxlength="100" />
					</td>
				</tr>
				<tr>
					<td colspan="4" class="td_oper">
						<a class="easyui-linkbutton" href="javascript:query()">查询</a>
						&nbsp;
						<a class="easyui-linkbutton" href="javascript:clear()">重置</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div style="padding-top: 10px;">
		<table id="custs" class="easyui-datagrid"></table>
	</div>
	<div id="toolbar" style="padding: 2px 0px;display: none;">
		<table>
			<tr>
				<td style="padding-left: 2px;">
					<a href="javascript:add();" class="easyui-linkbutton">新增</a>
					<a href="javascript:update();" class="easyui-linkbutton">修改</a>
					<a href="javascript:view();" class="easyui-linkbutton">查看</a>
					<a href="javascript:viewCust();" class="easyui-linkbutton">健康管理</a>
					<a href="javascript:goCustMedRecords();" class="easyui-linkbutton">电子病历管理</a>
					<a href="javascript:goCustDossiers();" class="easyui-linkbutton">图片病历管理</a>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>