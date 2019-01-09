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
		var editState;
		var deviceInfoGrid;
		$(function() {
			deviceInfoGrid = $("#userDevices").datagrid({
				title : "设备 “ ${deviceInfo.name}” 的用户配置列表",
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
				url: "<%=request.getContextPath()%>/chk/deviceConfig_query.action?code=${deviceInfo.code}",
				frozenColumns: [[{
					field: "ck",
					checkbox: true
				}]],
				columns: [[
					/* {
						field: "id",
						title: "设备编码",
						width: 120,
						align: "center"
					}, */{
						field: "deviceTypeCode",
						title: "设备编码",
						width: 120,
						align: "center"
					},{
						field: "userName",
						title: "设备键名",
						width: 120,
						align: "center"
					}, {
						field: "val",
						title: "设备键值",
						width: 120,
						align: "center"
					}, {
						field: "seq",
						title: "配置数序号",
						width: 80,
						align: "center"
					}/* , {
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
					} */, {
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
		
	function addDeviceDetail(){
			$('#configWin').window({title:'配置设备用户数',width:'400',height:'250',resizable:false,
				collapsible:false,
				minimizable:false,
				maximizable:false,
				modal:true,
				zIndex:100,
				closable:true});
			$('#configWin').window('center');
			$("#userConfigForm").form("clear");
			$("#userConfigForm").form("reset");
			editState = 2;
			$('#configWin').window('open');
		}
		
	function modifyDeviceDetail(){
		var selected = $('#userDevices').datagrid("getSelected");
		if(selected){
			var selections = $('#userDevices').datagrid("getSelections");
			if(selections.length==1){
				$('#configWin').window({title:'修改配置明细',width:'400',height:'250',resizable:false,
					collapsible:false,
					minimizable:false,
					maximizable:false,
					modal:true,
					zIndex:100,
					closable:true});
				$('#configWin').window('center');
				$("#userConfigForm").form("clear");
				$("#userConfigForm").form("reset");
				$("#id").val(selected.id);
				$("#deviceCode").val(selected.deviceTypeCode);
				$("#deviceVal").val(selected.val);
				$("#userName").val(selected.userName);
				$("#deviceSeq").val(selected.seq);
				editState = 3;
				$('#configWin').window('open');
			}else{
					$.messager.alert("提示信息","一次只能编辑一行数据","info");
					refreshGrid();
					}
				}else{
					$.messager.alert('提示信息','请选择编辑的行','info');
				}
		}
		
	function closeIt(){
		$("#configWin").window("close");
		$("#userConfigForm").form("clear");
		$("#userConfigForm").form("reset");
	}
	
	function save(){
		var userName = document.getElementById("userName");
		var userNameVal = $.trim(userName.value);
		userName.value = userNameVal;
		if(!userNameVal){
			$.messager.alert("提示信息", "请输入设备按键名。", "error", function(){
				userName.focus();
			});
			return;
		}
		// 设备编号
		var deviceValEle = document.getElementById("deviceVal");
		var deviceValVal = $.trim(deviceValEle.value);
		deviceValEle.value = deviceValVal;
		if(deviceValVal == ""){
			$.messager.alert("提示信息", "请输入设备按键名对应的值", "error", function(){
				deviceValEle.focus();
			});
			return;
		}
		// 用户编号
		var deviceSeqEle = document.getElementById("deviceSeq");
		var deviceSeqVal = $.trim(deviceSeqEle.value);
		deviceSeqEle.value = deviceSeqVal;
		if(!deviceSeqVal){
			$.messager.alert("提示信息", "用户编号不能为空.", "error", function(){
				deviceSeqEle.focus();
			});
			return;
		}else if(!/^[1-9]\d*$/.test(deviceSeqVal)){
			$.messager.alert("提示信息", "用户编号为正整数.", "error", function(){
				deviceSeqEle.focus();
			});
			return;
		}
		var saveUrl ="";
		if(editState == 2){
			saveUrl = "<%=request.getContextPath()%>/chk/deviceConfig_addUserNum.action";
		}
		if(editState == 3){
			saveUrl = "<%=request.getContextPath()%>/chk/deviceConfig_updateUserNum.action";
		}
		// 表单提交
		$("#userConfigForm").form("submit", {
			url : saveUrl,
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
	<div style="padding-top: 2px;padding-right:2px;">
		<table id="userDevices" class="easyui-datagrid"></table>
	</div>
	<div id="toolbar" style="padding: 2px 0px;display: none;">
		<table>
			<tr>
				<td style="padding-left: 2px;">
					<a href="javascript:addDeviceDetail()" class="easyui-linkbutton">添加明细</a>
					<a href="javascript:modifyDeviceDetail()" class="easyui-linkbutton">修改明细</a>
				</td>
			</tr>
		</table>
	</div>
	<div style="display: none;">
	<div id="configWin" title="添加设备用户配置" class="easyui-window" style="padding: 12px;"  data-options="modal:true,closed:true">
		<div >
			<form id="userConfigForm" method="post" accept-charset="UTF-8" style="margin: 0px;padding: 0px;">
				<table class="tb" style="width: 300px;height:50px;">
					<tr>
						<td class="tdTitle" style="width: 35%;text-align: right;">设备编码：</td>
						<td class="tdVal" nowrap="nowrap">
							<input type="text" id="deviceCode" name="deviceConfig.deviceTypeCode" class="ipt" maxlength="30"  readOnly="readOnly"  value="${deviceInfo.code}" />
							<span class="must">*</span>
							<input type="hidden" id="id" name="deviceConfig.id"  />
						</td>
					</tr>
					<tr>
						<td class="tdTitle" style="text-align: right;">
							<label>设备按键：</label>
						</td>
						<td class="tdVal">
							<input type="text" maxlength="30" id="userName" name="deviceConfig.userName" value="" />
							<span class="must">*</span>
						</td>
					</tr>
					<tr>
						<td class="tdTitle" style="text-align: right;">设备按键对应值：</td>
						<td class="tdVal">
							<input type="text" id="deviceVal" name="deviceConfig.val" class="ipt" maxlength="10" value="${userDevice.deviceSn}" />
							<span class="must">*</span>
						</td>
					</tr>
					<tr>
						<td class="tdTitle" style="text-align: right;">配置数序号：</td>
						<td class="tdVal">
							<input type="text" id="deviceSeq" name="deviceConfig.seq" class="ipt" maxlength="4" value="${userDevice.deviceSeq}" />
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