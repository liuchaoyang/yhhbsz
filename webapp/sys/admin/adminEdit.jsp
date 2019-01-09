<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.module.sys.bean.User"%>
<%@ page import="com.yzxt.yh.module.sys.bean.Org"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.util.DateUtil"%>
<!DOCTYPE html>
<% 
User user= (User)request.getAttribute("user");
Org org = (Org)request.getAttribute("org");
boolean isAdd = StringUtil.isEmpty(user.getId());
%>
<html>
<head>
	<%@ include file="../../common/inc.jsp"%>
	<title>组织管理员编辑</title>
	<style type="text/css">
	body{
		padding: 2px;
		margin: 0px;
	}
	</style>
	<script type="text/javascript">
	function closeIt(){
		try{
			parent.subWin.window("close");
		}catch(e){}
	}
	function validForm(){
		<%--姓名--%>
		var userName = document.getElementById("userName");
		var userNameVal = $.trim(userName.value);
		userName.value = userNameVal;
		if(!userNameVal){
			$.messager.alert("提示信息", "请输入姓名。", "error", function(){
				userName.focus();
			});
			return false;
		}
		<%
		if(isAdd)
		{
		%>
		var account = document.getElementById("account");
		var accountVal = $.trim(account.value);
		account.value = accountVal;
		if(!accountVal){
			$.messager.alert("提示信息", "请输入登录帐号。", "error", function(){
				account.focus();
			});
			return false;
		}else if(!is.account(accountVal)){
			$.messager.alert("提示信息", "登录帐号不正确，只能以字母开头，由字母、数字和下划线组成，长度为6-20。", "error", function(){
				account.focus();
			});
			return false;
		}
		<%
		}
		%>
		<%--手机号--%>
		var phone = document.getElementById("phone");
		var phoneVal = $.trim(phone.value);
		phone.value = phoneVal;
		if(phoneVal && !is.mPhone(phoneVal)){
			$.messager.alert("提示信息", "手机号不正确。", "error", function(){
				phone.focus();
			});
			return false;
		}
		<%--Email--%>
		var email = document.getElementById("email");
		var emailVal = $.trim(email.value);
		email.value = emailVal;
		if(emailVal && !is.email(emailVal)){
			$.messager.alert("提示信息", "Email不正确。", "error", function(){
				email.focus();
			});
			return false;
		}
		return true;
	}
	function save(){
		if(!validForm()){
			return;
		}
		$("#userForm").form("submit", {
			url: "<%=request.getContextPath()%>/sys/admin_save.action",
			dataType : "json",
			success : function(data) {
				data =  $.parseJSON(data);
				if(data.state == 1){
					$.messager.alert("提示信息", data.msg?data.msg:"保存成功。", "info", function(){
						try{
							parent.refreshGrid();
						}catch(e){}
						closeIt();
					});
				}else{
					$.messager.alert("提示信息", data.msg?data.msg:"保存失败。", "error");
				}
			}
		});
	}
	</script>
</head>
<body>
	<div>
		<form id="userForm" style="margin: 0px;padding: 0px;" method="post" accept-charset="UTF-8">
			<table class="table">
				<tr>
					<td width="30%" class="td_title">管理员姓名:</td>
					<td>
						<input maxlength="20" id="userName" name="user.name" value="<%=StringUtil.trim(user.getName())%>" /><span class="must">*</span>
						<input type="hidden" id="id" name="user.id" value="<%=StringUtil.trim(user.getId())%>" />
						<input type="hidden" id="orgId" name="user.orgId" value="<%=StringUtil.trim(user.getOrgId())%>" />
					</td>
				</tr>
				<tr>
					<td class="td_title">管理员帐号:</td>
					<td>
						<%
						if(isAdd)
						{
						%>
						<input id="account" name="user.account" maxlength="20" value="<%=StringUtil.trim(user.getAccount())%>" /><span class="must">*</span>
						<%
						}else
						{
						%>
						<%=StringUtil.trim(user.getAccount())%>
						<input type="hidden" id="account" name="user.account" value="<%=StringUtil.trim(user.getAccount())%>" />
						<%
						}
						%>
 					</td>
				</tr>
				<tr>
					<td class="td_title">所属组织:</td>
					<td><%=StringUtil.trim(org.getName())%></td>
				</tr>
				<tr>
					<td class="td_title">手机号:</td>
					<td>
						<input id="phone" name="user.phone" maxlength="11" value="<%=StringUtil.trim(user.getPhone())%>" />
					</td>
				</tr>
				<tr>
					<td class="td_title">电子邮箱:</td>
					<td>
						<input id="email" name="user.email" maxlength="30" value="<%=StringUtil.trim(user.getEmail())%>" />
					</td>
				</tr>
			</table>
			<div style="text-align: center;padding-top: 7px;">
				<a class="easyui-linkbutton" href="javascript:save();">保存</a>
				<a class="easyui-linkbutton" href="javascript:closeIt();">关闭</a>
			</div>
		</form>
	</div>
</body>
</html>