<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.constant.ConstDevice"%>
<%@ page import="com.yzxt.yh.module.chk.bean.DeviceInfo"%>
<%@ page import="com.yzxt.yh.module.chk.bean.DeviceConfig"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<% 
	@SuppressWarnings("unchecked")
	List<DeviceInfo> deviceInfos = (List<DeviceInfo>)request.getAttribute("deviceInfos");
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
	jQuery(function(){ 
    // 省级 
     $('#province').combobox({
            valueField:'itemvalue', //值字段
            textField:'itemtext', //显示的字段
            url:'/user/sort/province_list',
            panelHeight:'auto',
            required:true,
            editable:false,//不可编辑，只能选择
            value:'${user.province}'
         });
    //县市区 
         $('#city').combobox({
            valueField:'itemvalue', //值字段
            textField:'itemtext', //显示的字段
            url:'/user/sort/city_list?province=${user.province}',
            panelHeight:'auto',
            required:true,
            editable:false,//不可编辑，只能选择
            value:'${user.city}'
         });
    });
	function setCataList(code){
		$.ajax({
			type : "GET",
			dataType : "json",
			url : "<%=request.getContextPath()%>/chk/deviceConfig_listConfigByJson.action?code=" + code,
			async : false,
			timeout : 30000,
			success : function(data){
				var sel = document.getElementById("catalogId");
				sel.options.length = 0;
				sel.add(new Option("请选择", ""));
				for(var i = 0;i < data.length; i++){
					sel.add(new Option(data[i].name, data[i].id));
				}
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
	var subWin;
	function openSelResi(){
		<%--先选择一个居民，然后跳转到新增页面
			parent.
		--%>
		subWin = $('<div><iframe src="<%=request.getContextPath()%>/chk/customersSel.jsp" style="width: 99%;height:99%;" border="0" frameborder="0" scoll="no"></iframe></div>').window({
			title: "选择用户",
			width: 600,
			height: 400,
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
		var deviceSnExtEle = document.getElementById("deviceSnExt");
		var deviceSnExtVal = $.trim(deviceSnExtEle.value);
		deviceSnExtEle.value = deviceSnExtVal;
		if(deviceSnExtVal == ""){
			$.messager.alert("提示信息", "用户编号不能为空.", "error", function(){
				deviceSnExtEle.focus();
			});
			return;
		}
		// 表单提交
		$("#userDeviceForm").form("submit", {
			<%-- <c:if test="${operType=='add'}">
			url : "<%=request.getContextPath()%>/chk/userDevice_add.action",
			</c:if>
			<c:if test="${operType=='update'}">
			url : "<%=request.getContextPath()%>/chk/userDevice_update.action",
			</c:if>
			dataType : "json", --%>
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
	<form id="userDeviceForm" method="post" accept-charset="UTF-8" style="margin: 0px;padding: 0px;">
		<table class="tb">
			<tr>
				<td class="tdTitle" style="width: 35%;text-align: right;">用户名称：</td>
				<td class="tdVal">
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
					<select style="width:110px" name="foodCata" onchange="setCataList(this.value);">
								<option value="">请选择</option>
								<%
								for(DeviceInfo deviceInfo : deviceInfos)
								{
								%>
								<option value="<%=deviceInfo.getCode()%>" <%=deviceInfo.getCode().equals(deviceInfo.getCode())?"selected=\"selected\"":""%>><%=deviceInfo.getName()%></option>
								<%
								}
								%>
					</select>
					<span class="must">*</span>
					<input type="hidden" id="id" name="userDevice.id" value="${userDevice.id}" />
					<input type="hidden" id="idCard" name="userDevice.idCard" value="${userDevice.idCard}" />
				</td>
			</tr>
			<tr>
				<td class="tdTitle">用户编号：</td>
				<td class="tdVal">
					<select style="width:160px" name="foodId">
								<option value="">请选择</option>
								<%
								List<DeviceConfig> configList = deviceInfo.getFoodList();
								for(DeviceConfig deviceConfig : configList)
								{
								
								%>
								<option value="<%=deviceConfig.getId()%>" <%=deviceConfig.getDeviceTypeCode().equals(deviceInfo.getCode())?"selected=\"selected\"":""%>><%=deviceConfig.getUserName()%></option>
								<%
								}
								%>
							</select>
					<input type="text" id="deviceSnExt" name="userDevice.deviceSnExt" class="ipt" maxlength="30" value="${userDevice.deviceSnExt}" />
					<span class="must">*</span>
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
</body>
</html>