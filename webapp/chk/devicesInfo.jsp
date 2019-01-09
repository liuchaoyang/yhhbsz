<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.constant.ConstDevice"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
	<title>设备类别</title>
	<%@ include file="../common/inc.jsp"%>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/css/flow.css" />
	<style type="text/css">
	</style>
	<script type="text/javascript">
		var deviceInfoGrid;
		$(function() {
			deviceInfoGrid = $("#userDevices").datagrid({
				title: "设备类别列表",
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
				singleSelect: true,
				emptyMsg: "没有可显示的记录.",
				url: "<%=request.getContextPath()%>/chk/deviceInfo_query.action",
				frozenColumns: [[{
					field: "ck",
					checkbox: true
				}]],
				columns: [[
					{
						field: "code",
						title: "设备编码",
						width: 120,
						align: "center"
					},{
						field: "name",
						title: "设备名称",
						width: 120,
						align: "center"
					}, {
						field: "img",
						title: "图片",
						width: 120,
						align: "center"
					}, {
						field: "checkType",
						title: "设备类型",
						width: 80,
						align: "center",formatter:function(v){
							var text = "";
							if(v==1){
								text = "血压计";
							}else if(v==2)
							{
								text = "血糖仪";
							}
							return text;
						}
					}, {
						field: "state",
						title: "设备状态",
						width: 80,
						align: "center",formatter:function(v){
							var text = "";
							if(v==1){
								text = " 能绑定";
							}else if(v==2)
							{
								text = "未能绑定";
							}
							return text;
						}
					}, {
						field: "createTime",
						title: "创建时间",
						width: 120,
						align: "center"
					}]],
				onSortColumn: function(sort, order){
				},
				toolbar: "#toolbar"
			});
			var p = deviceInfoGrid.datagrid("getPager");
			$(p).pagination({
				pageNumber: 1,
				showPageList: true,
				pageList: [10, 20, 50, 100]
			});
			query();
		});
	
		//重新加载，刷新页面
		function refreshGrid(){
			deviceInfoGrid.datagrid("reload");
			deviceInfoGrid.datagrid("unselectAll");
			deviceInfoGrid.datagrid("clearSelections");
		}
	
		//查询
		function query(){
			deviceInfoGrid.datagrid("getPager").pagination({pageNumber: 1});
			$.extend(deviceInfoGrid.datagrid("options"), {
				pageNumber: 1,
				queryParams: $("#filterForm").serializeObject()
			});
			refreshGrid();
		}
	
		//重置
		function clear(){
			document.getElementById("filterForm").reset();
			refreshGrid();
		}
		
		function showDictDetail(){
			var selections = deviceInfoGrid.datagrid("getSelections");
			if(!selections || selections.length != 1){
				$.messager.alert("提示信息", "请选择一种设备.", "info");
				return;
			}else{
				var url = "<%=request.getContextPath()%>/chk/deviceInfo_showDetails.action?code=" + selections[0].code;
		       	var tabTitle = "配置用户数";
		        top.addTab(tabTitle, url, null);
			}
		}
		
		function updateInfo(){
			var selected = $('#userDevices').datagrid("getSelected");
			if(selected){
				var selections = $('#userDevices').datagrid("getSelections");
				if(selections.length==1){
					$('#deviceWin').window({title:'修改设备信息',width:'400',height:'250',resizable:false,
						collapsible:false,
						minimizable:false,
						maximizable:false,
						modal:true,
						zIndex:100,
						closable:true});
					$('#deviceWin').window('center');
					$("#userDeviceForm").form("clear");
					$("#userDeviceForm").form("reset");
					$("#code").val(selected.code);
					$("#name").val(selected.name);
					$(".checkType").val(selected.checkType);
					$(".state").val(selected.state);
					$('#deviceWin').window('open');
				}else{
					$.messager.alert("提示信息","一次只能编辑一行数据","info");
					refreshGrid();
					}
			}else{
					$.messager.alert('提示信息','请选择编辑的行','info');
			}
		}
		
		function closeIt(){
			$("#deviceWin").window("close");
			$("#userDeviceForm").form("clear");
			$("#userDeviceForm").form("reset");
		}
	
	function save(){
		var deviceName = document.getElementById("name");
		var deviceNameVal = $.trim(deviceName.value);
		deviceName.value = deviceNameVal;
		if(!deviceNameVal){
			$.messager.alert("提示信息", "请输入设备名称，设备名称不许为空", "error", function(){
				deviceName.focus();
			});
			return;
		}
		// 表单提交
		$("#userDeviceForm").form("submit", {
			url : "<%=request.getContextPath()%>/chk/deviceInfo_updateEquipment.action",
			onSubmit : function() {
				return true;
			},
			success : function(data) {
				data =  $.parseJSON(data);
				if(data.state > 0){
					$.messager.alert("提示信息", "保存成功.", "info", function(){
						try{
							parent.refreshGrid();
						}catch(e){}
						closeIt();
						refreshGrid();
					});
				}else{
					$.messager.alert("提示信息", data.msg, "error");
				}
			}
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
					<td width="15%" class="td_title">设备编码:</td>
					<td width="35%">
						<input id="deviceCode" name="deviceCode" maxlength="100" />
					</td>
					<td width="15%" class="td_title">设备名称:</td>
					<td>
						<input id="deviceName" name="deviceName" maxlength="100" />
					</td>
				</tr>
				<tr>
					<td class="td_title">设备类型:</td>
					<td colspan="3">
						<select id = "checkType" name="checkType">
							<option value="">请选择</option>
							<option value="1">血压计</option>
							<option value="2">血糖仪</option>
						</select>
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
	<div style="padding-top: 2px;padding-right:2px;">
		<table id="userDevices" class="easyui-datagrid"></table>
	</div>
	<div id="toolbar" style="padding: 2px 0px;display: none;">
		<table>
			<tr>
				<td style="padding-left: 2px;">
					<a href="javascript:updateInfo();" class="easyui-linkbutton">修改</a>
					<a href="javascript:showDictDetail()" class="easyui-linkbutton">配置用户数</a>
				</td>
			</tr>
		</table>
	</div>
	<div style="display: none;">
	<div id="deviceWin" title="添加绑定设备类别" class="easyui-window" style="padding: 12px;"  data-options="modal:true,closed:true">
		<div >
			<form id="userDeviceForm" method="post" accept-charset="UTF-8" style="margin: 0px;padding: 0px;">
				<table class="tb" style="width: 300px;height:50px;">
					<tr>
						<td class="tdTitle" style="width: 35%;text-align: right;">设备编码：</td>
						<td class="tdVal" nowrap="nowrap">
							<input type="text" id="code" name="deviceInfo.code" class="ipt" maxlength="30"  readOnly="readOnly"  value="" />
							<span class="must">*</span>
						</td>
					</tr>
					<tr>
						<td class="tdTitle" style="text-align: right;">
							<label>设备名称：</label>
						</td>
						<td class="tdVal">
							<input type="text" maxlength="30" id="name" name="deviceInfo.name"  />
							<span class="must">*</span>
						</td>
					</tr>
					<tr>
						<td class="tdTitle" style="text-align: right;">设备类型：</td>
						<td class="tdVal">
							<select class="checkType" id="checkType" name="deviceInfo.checkType">
								<option value="1">血压计</option>
								<option value="2">血糖仪</option>
							</select>
							<span class="must">*</span>
						</td>
					</tr>
					<tr>
						<td class="tdTitle" style="text-align: right;">用户编号：</td>
						<td class="tdVal">
							<select class="state" id="state" name="deviceInfo.state">
								<option value="1">能绑定</option>
								<option value="2">不能绑定</option>
							</select>
							<span class="must">*</span>
						</td>
					</tr>
					<tr>
						<td colspan="2" style="padding-top: 20px;text-align: center;">
							<a href="javascript:save()" class="easyui-linkbutton">保存</a>&nbsp;
							<a href="javascript:closeIt()" class="easyui-linkbutton">关闭</a>
						</td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	</div>
</body>
</html>