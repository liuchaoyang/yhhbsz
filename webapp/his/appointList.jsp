<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.constant.ConstDevice"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.module.chk.bean.DeviceInfo"%>
<%@ page import="com.yzxt.yh.module.chk.bean.DeviceConfig"%>
<%@ page import="com.yzxt.yh.module.chk.bean.UserDevice"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<% 
	@SuppressWarnings("unchecked")
	List<DeviceInfo> deviceInfos = (List<DeviceInfo>)request.getAttribute("deviceInfos");
	@SuppressWarnings("unchecked")
	List<DeviceConfig> deviceConfigs = (List<DeviceConfig>)request.getAttribute("deviceConfigs");
	UserDevice userDevice = (UserDevice)request.getAttribute("userDevice");
%>
<!DOCTYPE html>
<html>
	<head>
		<%@ include file="../common/inc.jsp"%>
		<title>用户设备</title>
		<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/css/flow.css" />
		<style type="text/css">
		</style>
		<script type="text/javascript">
			var appointGrid;
			$(function() {
				appointGrid = $("#appoint").datagrid({
					title: "用户预约挂号列表",
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
					url: "<%=request.getContextPath()%>/his/appoint_queryAppoint.action",
					frozenColumns: [[{
						field: "ck",
						checkbox: true
					}]],
					columns: [[
					{
						field: "status",
						title: "预约状态",
						width: 60,
						align: "center",formatter:function(v)
						{
							var str="";
							if(v==0)
							{
								str='<font color="blue">预约中</font>';
							}else if(v==1)
							{
								str='<font color="red">预约成功</font>';
							}else
							{
								str='<font color="green">预约失败</font>';
							}
							return str;
						}
					}, {
						field: "custName",
						title: "用户名称",
						width: 80,
						align: "center"
					}, {
						field: "deptName",
						title: "医院名称",
						width: 120,
						align: "center"
					}, {
						field: "departName",
						title: "科室名称",
						width: 80,
						align: "center"
					}, {
						field: "selfSymptom",
						title: "自述症状",
						width: 140,
						align: "center"
					},{
						field: "firstVisit",
						title: "初诊结果",
						width: 140,
						align: "center"
					},{
						field: "memo",
						title: "备注",
						width: 140,
						align: "center"
					},{
						field: "appointTime",
						title: "想预约日期",
						width: 80,
						align: "center",formatter:function(v)
						{
							if(v){
								return v.substring(0,10);
							}else{
								return "";
							}
						}
					},{
						field: "detailTime",
						title: "想预约详细时间",
						width: 40,
						align: "center",formatter:function(v)
						{
							var str = "";
							if(v==1){
								str = "全天";
							}else if(v==2){
								str = "上午";
							}else{
								str = "下午";
							}
							return str;
						}
					},{
						field: "resultExplain",
						title: "结果说明",
						width: 140,
						align: "center"
					},{
						field: "createTime",
						title: "创建时间",
						width: 120,
						align: "center"
					}]],
					onSortColumn: function(sort, order){
					},
					toolbar: "#toolbar"
				});
				var p = appointGrid.datagrid("getPager");
				$(p).pagination({
					pageNumber: 1,
					showPageList: true,
					pageList: [10, 20, 50, 100]
				});
				query();
			});
		
			//刷新
			function refreshGrid(){
				appointGrid.datagrid("reload");
				appointGrid.datagrid("unselectAll");
				appointGrid.datagrid("clearSelections");
			}
			
			//查询
			function query(){
				var checkOneVal = $("#status").val();
				var checkTwoVal = $("#deptName").val();
				$('#appoint').datagrid("getPager").pagination({pageNumber : 1});
				var option = $('#appoint').datagrid("options");
				option.pageNumber = 1;
				option.url = "<%=request.getContextPath()%>/his/appoint_queryAppoint.action";
				var queryParams = option.queryParams;
				queryParams.status = checkOneVal;
				queryParams.deptName = checkTwoVal;
				queryParams.custName = document.getElementById("custName").value;
				queryParams.beginDate = document.getElementById("beginDate").value;
				queryParams.endDate = document.getElementById("endDate").value;
				refreshGrid();
			}
		
			//重置
			function clear(){
				document.getElementById("filterForm").reset();
				refreshGrid();
			}
	
			function closeIt(){
				$("#deviceWin").window("close");
				$("#userDeviceForm").form("clear");
				$("#userDeviceForm").form("reset");
			}
			
		
		var subWin1;
		function addAppoint(){
			var selected = $('#appoint').datagrid("getSelected");
			if(!selected){
				subWin1 = $('<div><iframe src="<%=request.getContextPath()%>/his/appoint_toAdd.action?custId=${custId}" style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>').window({
					title: "预约挂号",
					width: 700,
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
			else{
				subWin1 = $('<div><iframe src="<%=request.getContextPath()%>/his/appoint_toAdd.action?custId=' + selected.custId + '" style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>').window({
					title: "预约挂号",
					width: 700,
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
			
		}
		
		var subWin2;
		function deal(){
			var selected = $('#appoint').datagrid("getSelected");
			if(!selected){
				$.messager.alert("提示信息","请选择一条记录。","info");
				return;
			}
			if(selected.status==1 && selected.state==-1 ){
				$.messager.alert("提示信息","该信息已处理，请选择预约中的处理的。","info");
				$('#appoint').datagrid("clearSelections");
				return;
			}
			subWin2 = $('<div><iframe src="<%=request.getContextPath()%>/his/appoint_toCheck.action?operType=update&id=' + selected.id + '" style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>').window({
				title: "处理预约",
				width: 650,
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
		</script>
</head>
<body>
	<div class="sectionTitle">查询条件</div>
	<div>
		<form id="filterForm">
			<table class="table">
				<tr>
					<td width="15%" class="td_title">预约状态:</td>
					<td width="35%">
						<select id="status">
							<option value="">请选择</option>
							<option value="1">预约成功</option>
							<option value="0">预约中</option>
							<option value="-1">预约失败</option>
						</select>
					</td>
					<td width="15%" class="td_title">医院名称:</td>
					<td>
						<input id="deptName" name="deptName" maxlength="100" />
					</td>
				</tr>
				<tr>
					<td class="td_title">用户名称:</td>
					<td >
						<input id="custName" name="custName" maxlength="100" />
					</td>
					<td width="15%" class="td_title">想预约日期:</td>
					<td style="padding:0px;" nowrap="nowrap">
						<input class="Wdate" style="width: 90px;"id="beginDate" name="beginDate" value="" readonly="readonly" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}'})" />
						至
						<input class="Wdate" style="width: 90px;"id="endDate" name="endDate" value="" readonly="readonly" onclick="WdatePicker({minDate:'#F{$dp.$D(\'beginDate\')}'})" />
					</td>
				</tr>
				<tr>
					<td colspan="4" class="td_oper">
						<a class="easyui-linkbutton" href="javascript:query()">查询</a>
						<a class="easyui-linkbutton" href="javascript:clear()">重置</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div style="padding-top: 15px;">
		<table id="appoint" class="easyui-datagrid"></table>
	</div>
	<div id="toolbar" style="padding: 2px 0px;display: none;">
		<table>
			<tr>
				<td style="padding-left: 2px;">
					<a href="javascript:addAppoint();" class="easyui-linkbutton">预约挂号</a>
					<a href="javascript:deal();" class="easyui-linkbutton">处理</a>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>