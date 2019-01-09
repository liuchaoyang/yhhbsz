<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>修改密码</title>
	<script type="text/javascript">
	function closeIt() {
		try{
			parent.subWin.window("close");
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
			url: "<%=request.getContextPath()%>/sys/wel_changePwd.action",
			success: function(data) {
				data =  $.parseJSON(data);
				if(data.state > 0){
					$.messager.alert("提示信息", data.msg ? data.msg : "密码修改成功。", "info", function(){
						closeIt();
					});
				}else{
					$.messager.alert("提示信息", data.msg ? data.msg : "密码修改失败。", "error");
				}
			}
		});
	}
	</script>
</head>
<body>
	<form id="changePwdForm" method="post" accept-charset="UTF-8">
		<table style="width: 100%;border-spacing: 8px;">
			<tr>
				<td nowrap="nowrap" align="right" style="width: 35%;">
					用户名:
				</td>
				<td nowrap="nowrap" align="left">
					<input type="text" id="userName" name="userName" style="width: 150px;" disabled="disabled" readonly="readonly" value="${userInfo.account}" />
					<input type="hidden" name="userId" value="${userInfo.id}" />
				</td>
			</tr>
			<tr>
				<td nowrap="nowrap" align="right">
					原密码:
				</td>
				<td nowrap="nowrap" align="left">
					<input type="password" name="oldPassword" id="oldPassword" maxlength="14" style="width: 150px;" />
					<span class="must">*</span>
				</td>
			</tr>
			<tr>
				<td nowrap="nowrap" align="right">
					新密码:
				</td>
				<td nowrap="nowrap" align="left">
					<input type="password" name="newPassword" id="newPassword" maxlength="14" style="width: 150px;" />
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
					<a class="easyui-linkbutton" href="javascript:save()">保存</a>
					&nbsp;&nbsp;&nbsp;
					<a class="easyui-linkbutton" href="javascript:closeIt()">取消</a>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
