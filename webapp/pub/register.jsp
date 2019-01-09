<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.yzxt.yh.constant.ConstOrg"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.module.sys.bean.Org"%>
<%
Org org = (Org)request.getAttribute("org");
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>用户管理</title>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
	<style type="text/css">
	.top{width: 70%;min-width: 900px;margin-left: auto;margin-right: auto;margin-top: 20px;}
	.top-left{float: left;}
	.logoImg{height: 40px;max-width: 500px;overflow: hidden;}
	.logoText{max-width: 500px;height: 40px;line-height: 40px;font-size: 26px;font-weight: normal;font-family: 'microsoft yahei', 'Times New Roman', '宋体', Times, serif;color: #435527;}
	.colline{width: 2px;height: 40px;background: #E7E7E7;float:left;margin-left:5px;}
	.itemName{font-size: 22px;color: #666;font-family: 黑体;line-height: 40px;height: 40px;width: 120px;float:left;margin-left: 5px;}
	.top-right{float: right;height: 40px;}
	.top-right span{line-height: 40px;font-size: 12px;}
	.titleSplit{clear: both;margin-top: 10px;margin-bottom: 10px;margin-left: auto;margin-right: auto;width: 80%;min-width: 900px;height: 1px;background-color: #CCC;}
	.mainContent{margin-top: 20px;margin-left: auto;margin-right: auto;width: 80%;min-width: 900px;}
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
		function validForm(){
			<%--姓名--%>
			var userName = document.getElementById("name");
			var userNameVal = $.trim(userName.value);
			userName.value = userNameVal;
			if(!userNameVal){
				$.messager.alert("提示信息", "请输入姓名。", "error", function(){
					userName.focus();
				});
				return false;
			}
			<%--身份证--%>
			var idCard = document.getElementById("idCard");
			var idCardVal = $.trim(idCard.value);
			idCard.value = idCardVal;
			if(idCardVal&&!is.idCard(idCardVal)){
				$.messager.alert("提示信息", "身份证号不正确。", "error", function(){
					idCard.focus();
				});
				return false;
			}
			<%--手机号--%>
			var phone = document.getElementById("phone");
			var phoneVal = $.trim(phone.value);
			phone.value = phoneVal;
			if(!phoneVal){
				$.messager.alert("提示信息", "请输入手机号。", "error", function(){
					phone.focus();
				});
				return false;
			}
			if(!is.mPhone(phoneVal)){
				$.messager.alert("提示信息", "手机号不正确。", "error", function(){
					phone.focus();
				});
				return false;
			}
			<%--Email--%>
			var email = document.getElementById("email");
			var emailVal = $.trim(email.value);
			email.value = emailVal;
			if(!emailVal){
				$.messager.alert("提示信息", "请输入电子邮箱。", "error", function(){
					email.focus();
				});
				return false;
			}else if(!is.email(emailVal)){
				$.messager.alert("提示信息", "电子邮箱不正确。", "error", function(){
					email.focus();
				});
				return false;
			}
			<%--帐号--%>
			var account = document.getElementById("account");
			var accountVal = $.trim(account.value);
			account.value = accountVal;
			if(!accountVal){
				$.messager.alert("提示信息", "请输入登录帐号。", "error", function(){
					account.focus();
				});
				return false;
			}
			if(!is.account(accountVal)){
				$.messager.alert("提示信息", "登录帐号不正确，只能以字母开头，由字母、数字和下划线组成，长度为6-20。", "error", function(){
					account.focus();
				});
				return false;
			}
			var newPassword = document.getElementById("passWord");
			if(newPassword.value.length < 6){
				$.messager.alert("提示信息", "密码长度不能小于6位。", "error", function(){
					newPassword.focus();
				});
				return false;
			}
			var newPassword2 = document.getElementById("passWordNew");
			if(newPassword2.value != newPassword.value){
				$.messager.alert("提示信息", "两次输入密码不一致。", "error", function(){
					newPassword2.focus();
				});
				return false;
			}
			return true;
		}
			
		function save(){
			if(!validForm()){
				return;
			}
			$("#filterForm").form("submit", {
				url: "<%=request.getContextPath()+"/pwd/register_addCustomer.action"%>",
				dataType : "json",
				success : function(data) {
					data =  $.parseJSON(data); 				
					if(data.state == 1){
						$.messager.alert("提示信息", data.msg?data.msg:"注册成功。", "info", function(){
							location.href = "<%=request.getContextPath()%>/sys/wel_login.action<%=org!=null?"?orgId="+org.getShowId():""%>";
						});
					}else{
						$.messager.alert("提示信息", data.msg?data.msg:"注册失败。", "error");
					}
				}
			});
		}
		function show(){
			$("#accountShow").show();
		}
		function check(){
			var account = document.getElementById("account");
			var accountVal = $.trim(account.value);
			account.value = accountVal;
			if(!/^[a-zA-Z][a-zA-Z_0-9]{5,19}$/.test(accountVal)){
				return false;
			}else{
				$("#accountShow").hide();
			}
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
				<span>用户注册</span>
			</div>
			<div class="top-right">
				<span>已有账号?&nbsp;去<a href="<%=request.getContextPath()%>/sys/wel_login.action<%=org!=null?"?orgId="+org.getShowId():""%>">登录</a></span>
			</div>
		</div>
		<div class="titleSplit"></div>
		<div class="mainContent">
			<form id="filterForm" style="margin-left:auto;margin-right:auto;padding: 0px;" method="post" accept-charset="UTF-8">
				<table style="margin-left: auto;margin-right: auto;width:100%;">
					
					<tr>
						<td width="45%" class="td_title">姓名：</td>
						<td>
							<input type="text" id="name"  name=registerVO.name maxlength="20" style="width: 160px;" />
							<span class="must">*</span>
							<input type="hidden" name="registerVO.orgId" value="<%=org!=null?org.getId():""%>" />
						</td>
					</tr>
					<tr>
						<td  class="td_title">性别：</td>
						<td>
							<input type="radio" style="width:20px;" id="sex" class="sex" name="registerVO.sex" value="1" checked="checked" />男
							<input type="radio" style="width:20px;"  class="sex" name="registerVO.sex" value="2" />女
						</td>
					</tr>
					<tr>
						<td class="td_title">出生日期：</td>
						<td>
							<input type="text"   class="Wdate" name="registerVO.birthDay" id="birthDay"  onclick="WdatePicker()" style="width: 160px;" />
						</td>
					</tr>
				
					<tr>
						<td class="td_title">身份证号：</td>
						<td>
							<input type="text" id="idCard"  name=registerVO.idCard maxlength="18" style="width: 160px;" />
						</td>
					</tr>
					<tr>
						<td class="td_title">手机号：</td>
						<td>
							<input type="text" id="phone"  name="registerVO.phone" maxlength="11" style="width: 160px;" />
							<span class="must">*</span>
						</td>
					</tr>
					<tr>
						<td class="td_title">家庭地址：</td>
						<td>
							<input type="text" id="address"  name="registerVO.address" maxlength="50" style="width: 160px;" />
							
						</td>
					</tr>
					<tr>
						<td class="td_title">电子邮箱：</td>
						<td>
							<input type="text" id="email"  name="registerVO.email" maxlength="30" style="width: 160px;" />
							<span class="must">*</span>
						   
						</td>
					</tr>
					<tr>
						<td class="td_title" width="40%">登录帐号：</td>
						<td>
							<input type="text" id="account" name="registerVO.account" maxlength="20" onBlur="check()" onclick="show();"  style="width: 160px;" />
							<span class="must">*</span>
							 <span id="accountShow" style="font-size:12px;display: none;">以字母开头，由字母、数字和下划线组成，长度为6-20</span>
						</td>
					</tr>
					<tr>
						<td class="td_title" width="40%">登录密码：</td>
						<td>
							<input type="password" id="passWord" name="registerVO.passWord" maxlength="20" style="width: 160px;" />
							<span class="must">*</span>
						</td>
					</tr>
					<tr>
						<td class="td_title" width="40%">确认密码：</td>
						<td>
							<input type="password" id="passWordNew" name="passWordNew" maxlength="20" style="width: 160px;" />
							<span class="must">*</span>
						</td>
					</tr>
					<tr>
						<td colspan="4" class="tb_oper" align="center" style="padding: 5px;">
							<a class="easyui-linkbutton" href="javascript:save()">注册</a>&nbsp;
							<a class="easyui-linkbutton" href="javascript:closeIt()">返回</a>
						</td>
					</tr>
				</table>
			</form>
		</div>
	</body>
</html>