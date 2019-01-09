<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.yzxt.yh.module.sys.bean.Org"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.util.DateUtil"%>
<!DOCTYPE html>
<% 
Org org = (Org)request.getAttribute("org");
String operType = request.getParameter("operType");
// 村、乡镇、县、市、省份
%>
<html>
<head>
	<%@ include file="../../common/inc.jsp"%>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
	<title>组织管理</title>
	<style type="text/css">
	body{
		padding: 2px;
		margin: 0px;
	}
	.areaType{
		width: 120px;
	}
	</style>
	<script type="text/javascript">
	function validForm(){
		<%--组织名称--%>
		var name = document.getElementById("name");
		var nameVal = $.trim(name.value);
		name.value = nameVal;
		if(!nameVal){
			$.messager.alert("提示信息", "请输入组织名称。", "error", function(){
				name.focus();
			});
			return false;
		}
		
		var mnemonicCode = document.getElementById("mnemonicCode");
		var mnemonicCodeVal = $.trim(mnemonicCode.value);
		mnemonicCode.value = mnemonicCodeVal;
		if(mnemonicCodeVal){
			if(!/^[a-zA-Z]+$/.test(mnemonicCodeVal)){
				$.messager.alert("提示信息", "组织助记码错误，只能小写英文字母组成。", "error", function(){
					mnemonicCode.focus();
				});
				return;
			}
		}
		<%--系统显示名称--%>
		var showText = document.getElementById("showText");
		var showTextVal = $.trim(showText.value);
		showText.value = showTextVal;
		if(!showTextVal){
			$.messager.alert("提示信息", "请输入组织对于系统名称。", "error", function(){
				showText.focus();
			});
			return false;
		}
		<%--组织图标--%>
		var logoIpt = document.getElementById("logoIpt");
		var fileName = logoIpt.value;
		if(fileName){
			var len = fileName.length;
			if(len<4 || ".png"!=fileName.substring(len-4).toLowerCase()){
				$.messager.alert("提示信息", "组织图标须是.png格式的图片。", "error");
				return false;
			}
		}
		return true;
	}
	function closeIt(){
		try{
			parent.subWin.window("close");
		}catch(e){}
	}
	function save(){
		if(!validForm()){
			return;
		}
		$("#orgForm").form("submit", {
			url: "<%=!"update".equals(operType) ? request.getContextPath()+"/sys/org_add.action" : request.getContextPath()+"/sys/org_update.action"%>",
			dataType: "json",
			success: function(data) {
				data =  $.parseJSON(data); 
				<%
				if(!"update".equals(operType))
				{
				%>
				if(data.state == 1){
					$.messager.alert("提示信息", data.msg?data.msg:"新增成功。", "info", function(){
						try{
							parent.refreshGrid();
						}catch(e){}
						closeIt();
					});
				}else{
					$.messager.alert("提示信息", data.msg?data.msg:"新增失败。", "error");
				}
				<%
				}else
				{
				%>
				if(data.state == 1){
					$.messager.alert("提示信息", data.msg?data.msg:"修改成功。", "info", function(){
						try{
							parent.refreshGrid();
						}catch(e){}
						closeIt();
					});
				}else{
					$.messager.alert("提示信息", data.msg?data.msg:"修改失败。", "error");
				}
				<%
				}
				%>
			}
		});
	}
	function showLogo(val){
		window.open("<%=request.getContextPath()%>/msg/img.jsp?pt=" + val, "orgLogo");
	}
	</script>
</head>
<body>
	<div>
		<form id="orgForm" style="margin: 0px;padding: 0px;" method="post" accept-charset="UTF-8" enctype="multipart/form-data">
			<table class="table">
				<tr>
					<td width="20%" class="td_title">组织名称:</td>
					<td colspan="3">
						<input type="text" style="width: 180px;" id="name" name="org.name" maxlength="50" value="<%=StringUtil.trim(org.getName())%>" />
						<input type="hidden" id="id" name="org.id" value="<%=StringUtil.trim(org.getId())%>" />
						<input type="hidden" id="parentId" name="org.parentId" value="<%=StringUtil.trim(org.getParentId())%>" />
						<input type="hidden" id="fullId" name="org.fullId" value="<%=StringUtil.trim(org.getFullId())%>" />
						<input type="hidden" id="level" name="org.level" value="<%=Integer.valueOf(org.getLevel())%>" />
						<input type="hidden" id="logoId" name="org.logoId" value="<%=StringUtil.ensureStringNotNull(org.getLogoId())%>" />
						<span class="must">*</span>
					</td>
				</tr>
				<tr>
					<td class="td_title">组织助记码:</td>
					<td colspan="3">
						<input type="text" style="width: 100px;" id="mnemonicCode" name="org.mnemonicCode" maxlength="15" value="<%=StringUtil.trim(org.getMnemonicCode())%>" />
					</td>
				</tr>
				<tr>
					<td class="td_title">系统名称:</td>
					<td colspan="3">
						<input type="text" style="width: 180px;" id="showText" name="org.showText" maxlength="50" value="<%=StringUtil.trim(org.getShowText())%>" />
						<span class="must">*</span>
					</td>
				</tr>
				<tr>
					<td class="td_title">组织图标:</td>
					<td colspan="3">
						<input type="file" style="width: 210px;" accept="image/x-png" id="logoIpt" name="logo" style="width: 180px;" style="margin-right: 10px;" />
						<img style="cursor: pointer;margin-right: 10px;margin-bottom: -2px;" alt="帮助" src="<%=request.getContextPath()%>/resources/img/help.png" title="组织图标为高度为40宽度不要超过650的.png格式的图片" />
						<%
						if(StringUtil.isNotEmpty(org.getLogoPath()))
						{
						%>
						<a href="javascript: showLogo('<%=org.getLogoPath()%>');">查看图标</a>
						<%
						}
						%>
					</td>
				</tr>
				<tr>
					<td class="td_title">上级组织:</td>
					<td colspan="3">
						<%=StringUtil.trim(org.getParentName())%>
					</td>
				</tr>
				<tr>
					<td class="td_title">组织联系人:</td>
					<td width="30%">
						<input type="text" style="width: 115px;" id="contactPerson" name="org.contactPerson" maxlength="10" value="<%=StringUtil.trim(org.getContactPerson())%>" />
					</td>
					<td class="td_title" width="20%">联系电话:</td>
					<td>
						<input type="text" style="width: 115px;" id="phone" name="org.phone" maxlength="15" value="<%=StringUtil.trim(org.getPhone())%>" />
					</td>
				</tr>
				<tr>
					<td class="td_title">地址其他部分:</td>
					<td colspan="3">
						<input style="width: 250px;" type="text" id="address" name="org.address" maxlength="50" value="<%=StringUtil.trim(org.getAddress())%>" />
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div style="text-align: center;padding-top: 12px;">
		<a class="easyui-linkbutton" href="javascript:save();">保存</a>
		<a class="easyui-linkbutton" href="javascript:closeIt();">关闭</a>
	</div>
</body>
</html>