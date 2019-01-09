<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.constant.ConstDevice"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.module.chk.bean.DeviceInfo"%>
<%@ page import="com.yzxt.yh.module.chk.bean.DeviceConfig"%>
<%@ page import="com.yzxt.yh.module.chk.bean.UserDevice"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
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
<title>个人资料</title>
<meta charset="UTF-8">
<meta name="pragma" content="no-cache" />
<meta name="cache-control" content="no-cache" />
<meta name="expires" content="0" />
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/resources/css/flow.css" />
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
<style type="text/css">
body{
	font-size: 14px;
	color: #666;
	font-family: arial,宋体,sans-serif;
	padding: 2px;
}
.tb{
	width: 100%;
}
.tdTitle{
	font-size: 14px;
	color: #666;
	font-family: arial,宋体,sans-serif;
	text-align: right;
	vertical-align: middle;
}
.tdVal{
	font-size: 14px;
	color: #555;
	font-family: arial,宋体,sans-serif;
	vertical-align: middle;
	text-align: left;
}
input[type="text"].ipt{
	border: 1px solid;
	border-color: #D8D8D8 #E5E5E5 #E5E5E5 #D8D8D8;
	font-size: 14px;
	color: #555;
	box-shadow: inset 2px 3px 3px #F6F8F9;
	-moz-box-shadow: 2px 3px 3px #F6F8F9 inset;
	-webkit-box-shadow: 2px 3px 3px #F6F8F9 inset;
	outline: none;
	padding: 1px 4px;
	line-height: 26px;
	*line-height: 26px;
	height: 26px;
	width: 190px;
}
.sel{
	border: 1px solid;
	border-color: #D8D8D8 #E5E5E5 #E5E5E5 #D8D8D8;
	font-size: 14px;
	color: #555;
	box-shadow: inset 2px 3px 3px #F6F8F9;
	-moz-box-shadow: 2px 3px 3px #F6F8F9 inset;
	-webkit-box-shadow: 2px 3px 3px #F6F8F9 inset;
	outline: none;
	padding: 1px 4px;
	line-height: 26px;
	*line-height: 26px;
	height: 30px;
	width: 200px;
}
</style>
<script type="text/javascript">
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
				/* for(var i = 0;i < data.length; i++){
					sel.add(new Option(data[i].configName, data[i].configVal));
				} */
			},
			error : function(){
		 	}
		});
	}
	function closeIt(){
		try{
			parent.subWin.window("close");
		}catch(e){}
	}
	
	function save(){
		// 设备编号
		var deviceSnEle = document.getElementById("deviceSn");
		var deviceSnVal = $.trim(deviceSnEle.value);
		deviceSnEle.value = deviceSnVal;
		if(deviceSnVal == ""){
			$.messager.alert("提示信息", "设备编号不能为空.", "error", function(){
				deviceSnEle.focus();
			});
			return;
		}
		// 用户编号
		<%-- <%
		for(int i= 0 ; i<deviceInfos.size();i++){
			if(deviceInfos.get(i)!=null && deviceInfos.get(i).getCode()!="")
			{
			
				
			}
		}
		%>
		var deviceSnExtEle = document.getElementById("deviceSnExt");
		var deviceSnExtVal = $.trim(deviceSnExtEle.value);
		deviceSnExtEle.value = deviceSnExtVal;
		if(deviceSnExtVal == ""){
			$.messager.alert("提示信息", "用户编号不能为空.", "error", function(){
				deviceSnExtEle.focus();
			});
			return;
		} --%>
		// 表单提交
		$("#userDeviceForm").form("submit", {
			<c:if test="${operType=='add'}">
			url : "<%=request.getContextPath()%>/chk/userDevice_add.action",
			</c:if>
			<c:if test="${operType=='update'}">
			url : "<%=request.getContextPath()%>/chk/userDevice_update.action",
			</c:if>
			dataType : "json",
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
	<div style="height:190px;width:90%;">
	<form id="userDeviceForm" method="post" accept-charset="UTF-8" style="margin: 0px;padding: 0px;">
		<table class="tb">
			<tr>
				<td class="tdTitle" style="width: 35%;text-align: right;">
					<label>设备名称：</label>
				</td>
				<td class="tdVal">
					<select style="width:200px" id="deviceType" name="userDevice.deviceType" onchange="setCataList(this.value);">
								<option value="" <%=StringUtil.isEmpty("") ? "selected=\"selected\"" : ""%>>请选择</option>
								<%
								for(DeviceInfo deviceInfo : deviceInfos)
								{
								%>
								<option value="<%=deviceInfo.getCode()%>" <%=deviceInfo.getCode().equals(userDevice.getDeviceType())?"selected=\"selected\"":""%>><%=deviceInfo.getName()%></option>
								<%
								}
							%>
						<%-- <option value="<%=ConstDevice.DEVICE_TYPE_BP_AY%>">爱云血压计</option> --%>
					</select>
					<span class="must">*</span>
					<input type="hidden" id="id" name="userDevice.id" value="${userDevice.id}" />
					<input type="hidden" id="custId" name="userDevice.custId" value="${userDevice.custId}" />
				</td>
			</tr>
			<tr>
				<td class="tdTitle">用户编号：</td>
				<td class="tdVal">
					<select id="deviceSnExt" name="userDevice.deviceSnExt" style="height: 24px;width: 200px;">
							<option value="" <%=StringUtil.isEmpty(userDevice.getDeviceSnExt()) ? "selected=\"selected\"" : ""%>>请选择</option> 
							<%
								for(DeviceConfig deviceconfig : deviceConfigs)
								{
								%>
								<option value="<%=deviceconfig.getVal()%>" <%=deviceconfig.getVal().equals(userDevice.getDeviceSnExt())?"selected=\"selected\"":""%>><%=deviceconfig.getUserName()%></option>
								<%
								}
							%>
						</select>
					<%-- <input type="text" id="deviceSnExt" name="userDevice.deviceSnExt" class="ipt" maxlength="30" value="${userDevice.deviceSnExt}" />
					<span class="must">*</span> --%>
				</td>
			</tr>
			<tr>
				<td class="tdTitle">设备编号：</td>
				<td class="tdVal">
					<input type="text" id="deviceSn" name="userDevice.deviceSn" class="ipt" maxlength="30" value="${userDevice.deviceSn}" />
					<span class="must">*</span>
				</td>
			</tr>
			<tr>
				<td colspan="2" style="padding-top: 20px;text-align: center;">
					<a href="javascript:save()" class="easyui-linkbutton">保存</a>
					&nbsp;
					<a href="javascript:closeIt()" class="easyui-linkbutton">关闭</a>
				</td>
			</tr>
		</table>
	</form>
	</div>
</body>
</html>