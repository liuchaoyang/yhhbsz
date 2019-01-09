<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.module.sys.bean.Org"%>
<%@ page import="com.yzxt.yh.module.sys.bean.User"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%
User user= (User)request.getAttribute("user");
Org org = (Org)request.getAttribute("org");
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>修改密码</title>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
	<style type="text/css">
		.top{width: 60%;min-width: 800px;margin-left: auto;margin-right: auto;margin-top: 20px;}
		.top-left{float: left;}
		.logoImg{height: 40px;max-width: 500px;overflow: hidden;}
		.logoText{max-width: 500px;height: 40px;line-height: 40px;font-size: 26px;font-weight: normal;font-family: 'microsoft yahei', 'Times New Roman', '宋体', Times, serif;color: #435527;}
		.colline{width: 2px;height: 40px;background: #E7E7E7;float:left;margin-left:5px;}
		.itemName{font-size: 22px;color: #666;font-family: 黑体;line-height: 40px;height: 40px;width: 120px;float:left;margin-left: 5px;}
		.top-right{float: right;height: 40px;}
		.top-right span{line-height: 40px;font-size: 12px;}
		.titleSplit{clear: both;margin-top: 10px;margin-bottom: 10px;margin-left: auto;margin-right: auto;width: 60%;min-width: 800px;height: 1px;background-color: #CCC;}
		.mainContent{
			margin-top: 20px;
			width: 60%;
			min-width: 800px;
			margin-left: auto;
			margin-right: auto;
		}
		body{
			font-family: "微软雅黑"; FONT-SIZE: 11pt
		}
		.td_title{
			text-align: right;
		}
		td{
			padding: 5px;
		}
		</style>
	<script type="text/javascript">
	function closeIt() {
		try{
			location.href = "<%=request.getContextPath()%>/sys/wel_login.action<%=org!=null?"?orgId="+org.getShowId():""%>";
		}catch(e){}
	}
	function save(){
		var newPassword = document.getElementById("newPassword");
		if(newPassword.value.length < 6){
			$.messager.alert("提示信息", "密码长度不能小于6位。", "error", function(){
				newPassword.focus();
			});
			return;
		}
		var newPassword2 = document.getElementById("newPassword2");
		if(newPassword2.value != newPassword.value){
			$.messager.alert("提示信息", "两次输入密码不一致。", "error", function(){
				newPassword2.focus();
			});
			return;
		}
		$("#changePwdForm").form("submit", {
			url: "<%=request.getContextPath()%>/pwd/register_updateUserPwd.action",
			success: function(data) {
				data =  $.parseJSON(data);
				if(data.state > 0){
					$.messager.alert("提示信息", data.msg ? data.msg : "重置密码成功。", "info", function(){
						closeIt();
					});
				}else{
					$.messager.alert("提示信息", data.msg ? data.msg : "重置密码失败。", "error");
				}
			}
		});
	}
	</script>
</head>
<body>
	<div class="top">
		<div class="top-left">
		<%
		if(org!=null)
		{
			if(StringUtil.isNotEmpty(org.getLogoPath()))
			{
		%>
			<img class="logoImg" src="<%=request.getContextPath()%>/pub/cf_img.action?pt=<%=org.getLogoPath()%>" />
		<%
			}else
			{
		%>
			<span class="logoText"><%=org.getShowText()%></span>
		<%
			}
		}else
		{
		%>
			<%-- <img class="logoImg" src="<%=request.getContextPath()%>/resources/img/portal/logo.png" /> --%>
		<%
		}
		%>
		</div>
		<div class="top-left colline"></div>
		<div class="top-left itemName">
			<span>重设密码</span>
		</div>
		<div class="top-right">
			<span>去<a href="<%=request.getContextPath()%>/sys/wel_login.action<%=org!=null?"?orgId="+org.getShowId():""%>">登录</a></span>
		</div>
	</div>
	<div class="titleSplit"></div>
	<div class="mainContent">
	<form id="changePwdForm" method="post" accept-charset="UTF-8" style="margin: 0px;padding: 0px;width: 100%;">
		<table style="width: 100%;border-spacing: 8px;">
			<tr>
				<td nowrap="nowrap" align="right" style="width: 40%;">
					新密码:
				</td>
				<td nowrap="nowrap" align="left">
					<input type="password" name="newPassword" id="newPassword" maxlength="14" style="width: 150px;" />
					<input type="hidden" name="user.id" id="id" value="<%=StringUtil.trim(user.getId())%>" maxlength="14" style="width: 150px;" />
					<span class="must">*</span>
				</td>
			</tr>
			<tr>
				<td nowrap="nowrap" align="right">
					确认密码:
				</td>
				<td nowrap="nowrap" align="left">
					<input type="password" name="newPassword2" id="newPassword2" maxlength="14" style="width: 150px;" />
					<span class="must">*</span>
				</td>
			</tr>
			<tr>
				<td colspan="2" style="text-align: center;padding: 10px;">
					<a class="easyui-linkbutton" href="javascript:save()">确认</a>
					&nbsp;&nbsp;&nbsp;
					<a class="easyui-linkbutton" href="javascript:closeIt()">取消</a>
				</td>
			</tr>
		</table>
	</form>
	</div>
</body>
</html>
