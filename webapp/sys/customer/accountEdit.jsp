<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ page import="com.yzxt.yh.module.sys.bean.User"%>
<%@ page import="com.yzxt.yh.module.sys.bean.Customer"%>
<%@ page import="com.yzxt.yh.util.DateUtil"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%
Customer cust= (Customer)request.getAttribute("cust");
User user = cust.getUser();
User operUser = (User)request.getAttribute("operUser");
String nowDateStr = DateUtil.toHtmlDate(new Date());
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../../common/inc.jsp"%>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
	<title>客户账户信息</title>
	<style type="text/css">
	.table tr{
		height: 35px;
	}
	</style>
	<script type="text/javascript">
	function goView(){
		location.href = "<%=request.getContextPath()%>/sys/cust_toAccount.action?operType=view";
	}
	function validForm(){
		<%--用户帐号--%>
		<%
		// 如果账号不是身份证号码不允许修改
		if(user.getAccount().equals(user.getIdCard()))
		{
		%>
		var account = document.getElementById("account");
		var accountVal = $.trim(account.value);
		// 如果填写了账号
		if(accountVal){
			if(!is.account(accountVal)){
				$.messager.alert("提示信息", "用户帐号不正确，只能以字母开头，由字母、数字和下划线组成，长度为6-20。", "error", function(){
					account.focus();
				});
				return false;
			}
		}
		<%
		}
		%>
		<%--身份证号码--%>
		<%
		if(StringUtil.isEmpty(user.getIdCard()))
		{
		%>
		var idCard = document.getElementById("idCard");
		var idCardVal = $.trim(idCard.value);
		idCard.value = idCardVal;
		if(idCardVal){
			if(!is.idCard(idCardVal)){
				$.messager.alert("提示信息", "身份证号码不正确。", "error", function(){
					idCard.focus();
				});
				return false;
			}
		}
		<%
		}
		%>
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
		if(emailVal && !is.email(emailVal)){
			$.messager.alert("提示信息", "Email不正确。", "error", function(){
				email.focus();
			});
			return false;
		}
		<%--地址--%>
		var address = document.getElementById("address");
		var addressVal = $.trim(address.value);
		address.value = addressVal;
		<%--验证成功--%>
		return true;
	}
	function save(){
		if(!validForm()){
			return;
		}
		$("#mainForm").form("submit", {
			url: "<%=request.getContextPath()%>/sys/cust_saveAccount.action",
			dataType : "json",
			success : function(data) {
				data =  $.parseJSON(data); 
				if(data.state == 1){
					$.messager.alert("提示信息", data.msg?data.msg:"保存成功。", "info", function(){
						goView();
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
	<div style="margin-left: auto;margin-right: auto;width: 750px;">
			<div class="sectionTitle">个人信息</div>
			<div>
				<form id="mainForm" style="margin: 0px;padding: 0px;" method="post" accept-charset="UTF-8">
					<table class="table">
						<tr>
							<td class="td_title">用户帐号：</td>
							<td>
								<%
								// 如果账号不是身份证号码不允许修改
								if(!user.getAccount().equals(user.getIdCard()))
								{
								%>
								<%=user.getAccount()%>
								<%
								}else
								{
								%>
								<input type="text" id="account" name="cust.user.account" value="<%=user.getAccount()%>" maxlength="20" style="width: 160px;" />
								<%
								}
								%>
								<input type="hidden" name="cust.userId" value="<%=user.getId()%>" />
							</td>
						</tr>
						<tr>
							<td class="td_title">身份证号码：</td>
							<td>
								<%
								if(StringUtil.isNotEmpty(user.getIdCard()))
								{
								%>
								<%=StringUtil.trim(user.getIdCard())%>
								<%
								}else
								{
								%>
								<input type="text" id="idCard" name="cust.user.idCard" value="<%=user.getIdCard()%>" maxlength="18" style="width: 160px;" />
								<%
								}
								%>
							</td>
						</tr>
						<tr>
							<td class="td_title">用户姓名：</td>
							<td>
								<input type="text" id="userName" name="cust.user.name" value="<%=StringUtil.trim(user.getName())%>" maxlength="20" style="width: 160px;" />
								<font class="must" >*</font>
							</td>
						</tr>
						<tr>
							<td width="35%" class="td_title">性别：</td>
							<td>
								<%
								int sex = cust.getSex() != null ? cust.getSex().intValue(): 0;
								%>
								<select id="sex" name="cust.sex">
									<option value="" <%=sex<=0?"selected=\"selected\"":""%>>请选择</option>
									<option value="1" <%=sex==1?"selected=\"selected\"":""%>>男</option>
									<option value="2" <%=sex==2?"selected=\"selected\"":""%>>女</option>
								</select>
							</td>
						</tr>
						<tr>
							<td class="td_title">出生日期：</td>
							<td>
								<input type="text" readonly="readonly" class="Wdate" name="cust.birthday" value="<%=DateUtil.toHtmlDate(cust.getBirthday())%>" onclick="WdatePicker({maxDate:'<%=nowDateStr%>'})" style="width: 100px;" />
							
							</td>
						</tr>
						<tr>
							<td class="td_title">手机号：</td>
							<td>
								<input type="text" id="phone" name="cust.user.phone" value="<%=StringUtil.trim(user.getPhone())%>" maxlength="20" style="width: 160px;" />
								<font class="must">*</font>
							</td>
						</tr>
						<tr>
							<td class="td_title">Email：</td>
							<td>
								<input type="text" id="email" name="cust.user.email" value="<%=StringUtil.trim(user.getEmail())%>" maxlength="30" style="width: 160px;" />
							</td>
						</tr>
						<tr>
							<td class="td_title">通讯地址：</td>
							<td>
								<input type="text" id="address" value="<%=StringUtil.trim(cust.getAddress())%>" name="cust.address" maxlength="100" style="width: 160px;" />
							</td>
						</tr>
					</table>
				</form>
			</div>
			<%
			if(operUser!=null&&user.getId().equals(operUser.getId()))
			{
			%>
			<div style="text-align: center;padding-top: 15px;">
				<a class="easyui-linkbutton" href="javascript:save();">保存</a>
				&nbsp;
				<a class="easyui-linkbutton" href="javascript:goView();">返回</a>
			</div>
			<%
			}
			%>
		</div>
	</body>
</html>