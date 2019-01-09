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
				appointGrid = $("#hisPros").datagrid({
					title: "处方列表",
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
					url: "<%=request.getContextPath()%>/his/dzcf_queryChufang.action",
					frozenColumns: [[{
						field: "ck",
						checkbox: true
					}]],
					columns: [[
					{
						field: "custName",
						title: "客户名称",
						width: 80,
						align: "center"
					}, {
						field: "ypmc",
						title: "药品名称",
						width: 120,
						align: "center"
					},
					{field:'unit',title:'单位',width:100,align:'center',formatter:function(value){
						if(value==0){
							return '小盒';
						}else if(value==1){
							return '瓶';
						}else if(value==2){
							return '支';
						}else if(value==3){
							return '粒';
						}else if(value==4){
							return '片';
						}else if(value==5){
							return '克';
						}else{
							return value;
						}
					}}
					,{
						field: "num",
						title: "药品数量",
						width: 80,
						align: "center"
					},{
						field: "userage",
						title: "药品用法",
						width: 140,
						align: "center"
					},{
						field: "uselevel",
						title: "药品用量",
						width: 140,
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
				var checkTwoVal = $("#doctorId").val();
				/* $('#appoint').datagrid("getPager").pagination({pageNumber : 1});
				var option = $('#appoint').datagrid("options");
				option.pageNumber = 1; */
				var option = $('#hisPros').datagrid("options");
				option.url = "<%=request.getContextPath()%>/his/dzcf_queryChufang.action";
				var queryParams = option.queryParams;
				queryParams.status = checkOneVal;
				queryParams.doctorId = checkTwoVal;
				//queryParams.custName = document.getElementById("custName").value;
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
			
		</script>
</head>
<body>
	<div style="padding-top: 15px;">
		<table id="hisPros" class="easyui-datagrid"></table>
	</div>
	<div id="toolbar" style="padding: 2px 0px;display: none;">
		<table>
			<tr>
				<td style="padding-left: 2px;">
					<a href="javascript:addAppoint();" class="easyui-linkbutton">打印</a>
					<a href="javascript:deal();" class="easyui-linkbutton">付款</a>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>