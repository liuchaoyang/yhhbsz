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
<meta charset="UTF-8">
<meta name="pragma" content="no-cache" />
<meta name="cache-control" content="no-cache" />
<meta name="expires" content="0" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/css/flow.css" />
<style type="text/css">
</style>
<script type="text/javascript">
var userDevicesGrid;
$(function() {
	userDevicesGrid = $("#userDevices").datagrid({
		title: "用户设备列表",
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
		url: "<%=request.getContextPath()%>/chk/userDevice_queryUserDevices.action",
		frozenColumns: [[{
			field: "ck",
			checkbox: true
		}]],
		columns: [[
		{
			field: "deviceName",
			title: "设备名称",
			width: 120,
			align: "center"
		}, {
			field: "deviceSn",
			title: "设备编号",
			width: 120,
			align: "center"
		}, {
			field: "deviceSnExt",
			title: "用户编号",
			width: 80,
			align: "center"
		}, {
			field: "custName",
			title: "用户名称",
			width: 80,
			align: "center"
		}/* , {
			field: "idCard",
			title: "用户身份证号",
			width: 140,
			align: "center"
		} */, {
			field: "updateTime",
			title: "绑定时间",
			width: 120,
			align: "center"
		}]],
		onSortColumn: function(sort, order){
		},
		toolbar: "#toolbar"
	});
	var p = userDevicesGrid.datagrid("getPager");
	$(p).pagination({
		pageNumber: 1,
		showPageList: true,
		pageList: [10, 20, 50, 100]
	});
	query();
});

	function setCataList(code){
		$.ajax({
			type : "GET",
			dataType : "json",
			url : "<%=request.getContextPath()%>/chk/deviceConfig_listConfigByJson.action?code=" + code,
			async : false,
			timeout : 30000,
			success : function(data){
				var sel = document.getElementById("deviceSnExt");
				sel.options.length = 0;
				sel.add(new Option("请选择", ""));
				if(data.length <= 0){
					/* sel.setAttribute('disabled','true'); */
					sel.disabled = true;
				}else{
					for(var i = 0;i < data.length; i++)
					{
						sel.disabled = false;
						/* sel.setAttribute('disabled','false'); */
						sel.add(new Option(data[i].configName, data[i].configVal));
					}
				}
				
			},
			error : function(){
		 	}
		});
	}

function refreshGrid(){
	userDevicesGrid.datagrid("reload");
	userDevicesGrid.datagrid("unselectAll");
	userDevicesGrid.datagrid("clearSelections");
}

	function query(){
		var checkOneVal = $("#deviceType").val();
		var checkTwoVal = $("#deviceSn").val();
		$('#userDevices').datagrid("getPager").pagination({pageNumber : 1});
		var option = $('#userDevices').datagrid("options");
		option.pageNumber = 1;
		option.url = "<%=request.getContextPath()%>/chk/userDevice_queryUserDevices.action";
		var queryParams = option.queryParams;
		queryParams.deviceType = checkOneVal;
		queryParams.deviceSn = checkTwoVal;
		queryParams.userName = document.getElementById("useName").value;
		/* queryParams.endDate = document.getElementById("endDate").value; */
		refreshGrid();
	}

function clear(){
	document.getElementById("filterForm").reset();
}
function unbind(){
	$.messager.alert("提示信息", "设备解除绑定成功.", "error", function(){
	});
}
<%-- var subWin;
function bind(){
	subWin = $('<div><iframe src="<%=request.getContextPath()%>/chk/userDeviceEdit.jsp" style="width: 99%;height:99%;" border="0" frameborder="0" scoll="no"></iframe></div>').window({
		title: "新增设备绑定",
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
 --%>
	function del(){
		var selections = userDevicesGrid.datagrid("getSelections");
		var len = selections ? selections.length : 0;
		if(len == 0){
			$.messager.alert("提示信息", "请选择需要解除绑定的记录.", "error");
			return;
		}
		$.messager.confirm("警告信息", "确定解除绑定吗?", function(r){
			if(r){
				var ids = "";
				for(var i=0; i<len; i++){
					if(i > 0){
						ids = ids + ",";
					}
					ids = ids + selections[i].id;
				}
				$.ajax({
					type: "GET",
					dataType: "json",
					url: "<%=request.getContextPath()%>/chk/userDevice_delete.action?ids=" + ids,
					async: false,
					timeout: 30000,
					success: function(data){
						if(data.state > 0){
							$.messager.alert("提示信息", "解除绑定成功.", "info");
							refreshGrid();
						}else{
							$.messager.alert("提示信息", data.msg ? data.msg : "解除绑定失败.", "error");
						}
					},
					error: function(){
						$.messager.alert("提示信息", "解除绑定失败.", "error");
				 	}
				});
			}
		});
	}
	
	function bind(){
			$('#deviceWin').window({title:'绑定设备',width:'400',height:'250',resizable:false,
				collapsible:false,
				minimizable:false,
				maximizable:false,
				modal:true,
				zIndex:100,
				closable:true});
			$('#deviceWin').window('center');
			$("#userDeviceForm").form("clear");
			$("#userDeviceForm").form("reset");
			$('#deviceWin').window('open');
		}
		
	function closeIt(){
		$("#deviceWin").window("close");
		$("#userDeviceForm").form("clear");
		$("#userDeviceForm").form("reset");
	}
	var subWin;
	function openSelResi(){
		subWin = $('<div><iframe src="<%=request.getContextPath()%>/chk/customersSel.jsp" style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>').window({
			title: "选择用户",
			width: 500,
			height: 470,
			resizable: false,
			collapsible: false,
			minimizable: false,
			maximizable: false,
			modal: true,
			zIndex: 101,
			closable: true
		});
	};
	
	function closeSelUserWin(){
		subWin.window("close");
	}
	
	function setSelUser(cust){
		$("#userId").val(cust.userId);
		$("#custName").val(cust.name);
	}
	
	function save(){
		var custName = document.getElementById("custName");
		var custNameVal = $.trim(custName.value);
		custName.value = custNameVal;
		if(!custNameVal){
			$.messager.alert("提示信息", "请输入姓名。", "error", function(){
				custName.focus();
			});
			return;
		}
		// 设备编号
		var deviceSnEle = document.getElementById("deviceSn1");
		var deviceSnVal = $.trim(deviceSnEle.value);
		deviceSnEle.value = deviceSnVal;
		if(deviceSnVal == ""){
			$.messager.alert("提示信息", "设备编号不能为空.", "error", function(){
				deviceSnEle.focus();
			});
			return;
		}
		// 用户编号
		var deviceSnExtEle = document.getElementById("deviceSnExt");
		var deviceSnExtVal = $.trim(deviceSnExtEle.value);
		deviceSnExtEle.value = deviceSnExtVal;
		if(deviceSnExtEle.options.length  <= 0){
			if(deviceSnExtVal == ""){
				$.messager.alert("提示信息", "用户编号不能为空.", "error", function(){
					deviceSnExtEle.focus();
				});
				return;
			}
		}
		// 表单提交
		$("#userDeviceForm").form("submit", {
			url : "<%=request.getContextPath()%>/chk/userDevice_add.action",
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
					<td width="15%" class="td_title">设备名称:</td>
					<td width="35%">
						<select id="deviceType" name="deviceType" class="sel" >
								<option value="" <%=StringUtil.isEmpty("") ? "selected=\"selected\"" : ""%>>请选择</option>
								<%
								for(DeviceInfo deviceInfo : deviceInfos)
								{
								%>
								<option value="<%=deviceInfo.getCode()%>" <%=deviceInfo.getCode().equals(userDevice.getDeviceType())?"selected=\"selected\"":""%>><%=deviceInfo.getName()%></option>
								<%
								}
							%>
							</select>
					</td>
					<td width="15%" class="td_title">设备编号:</td>
					<td>
						<input id="deviceSn" name="deviceSn" maxlength="100" />
					</td>
				</tr>
				<tr>
					<td class="td_title">用户名称:</td>
					<td colspan="3">
						<input id="useName" name="useName" maxlength="100" />
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
		<table id="userDevices" class="easyui-datagrid"></table>
	</div>
	<div id="toolbar" style="padding: 2px 0px;display: none;">
		<table>
			<tr>
				<td style="padding-left: 2px;">
					<a href="javascript:bind();" class="easyui-linkbutton">设备绑定</a>
					<a href="javascript:del();" class="easyui-linkbutton">解除绑定</a>
				</td>
			</tr>
		</table>
	</div>
	<div style="display: none;">
	<div id="deviceWin" title="绑定设备" class="easyui-window" style="padding: 12px;"  data-options="modal:true,closed:true">
		<div >
			<form id="userDeviceForm" method="post" accept-charset="UTF-8" style="margin: 0px;padding: 0px;">
				<table class="tb" style="width: 300px;height:50px;">
					<tr>
						<td class="tdTitle" style="width: 35%;text-align: right;">用户名称：</td>
						<td class="tdVal" nowrap="nowrap">
							<input type="text" id="custName" name="userDevice.custName" class="ipt" maxlength="30"  readOnly="readOnly"  value="" />
							<span class="must">*</span>
							<img id="selUserImg" alt="选择用户" src="<%=request.getContextPath()%>/resources/img/search.png" onclick="openSelResi()" style="cursor: pointer;margin-bottom: -5px;" />
							<input type="hidden" id="userId" name="userDevice.custId" value="" />
						</td>
					</tr>
					<tr>
						<td class="tdTitle" style="text-align: right;">
							<label>设备名称：</label>
						</td>
						<td class="tdVal">
							<select id="deviceType" name="userDevice.deviceType" class="sel" onchange="setCataList(this.value);">
								<option value="" <%=StringUtil.isEmpty("") ? "selected=\"selected\"" : ""%>>请选择</option>
								<%
								for(DeviceInfo deviceInfo : deviceInfos)
								{
								%>
								<option value="<%=deviceInfo.getCode()%>" <%=deviceInfo.getCode().equals(userDevice.getDeviceType())?"selected=\"selected\"":""%>><%=deviceInfo.getName()%></option>
								<%
								}
							%>
							</select>
							<span class="must">*</span>
							<input type="hidden" id="id" name="userDevice.id" value="${userDevice.id}" />
							<%-- <input type="hidden" id="custId" name="userDevice.custId" value="${userDevice.custId}" /> --%>
						</td>
					</tr>
					<tr>
						<td class="tdTitle" style="text-align: right;">用户编号：</td>
						<td class="tdVal">
							<select id="deviceSnExt" name="userDevice.deviceSnExt" style="height: 24px;width: 150px;">
								<option value="" <%=StringUtil.isEmpty(userDevice.getDeviceSnExt()) ? "selected=\"selected\"" : ""%>>请选择</option> 
								<%
									for(DeviceConfig deviceconfig : deviceConfigs)
									{
								%>
									<%-- <% 
										if(deviceConfigs==null||deviceConfigs.size()==0){
									%> --%>
										<option value="<%=deviceconfig.getVal()%>" <%=deviceconfig.getVal().equals(userDevice.getDeviceSnExt())?"selected=\"selected\"":""%>><%=deviceconfig.getUserName()%></option>
									<%-- <% 
										}else{
									%>
									
									<%
										}
									%> --%>
								<%
								}
								%>
						</select>
							<span class="must">*</span>
							
						</td>
					</tr>
					<tr>
						<td class="tdTitle" style="text-align: right;">设备编号：</td>
						<td class="tdVal">
							<input type="text" id="deviceSn1" name="userDevice.deviceSn" class="ipt" maxlength="30" value="${userDevice.deviceSn}" />
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