<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.module.sys.bean.Role"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.util.DateUtil"%>
<!DOCTYPE html>
<% 
Role role= (Role)request.getAttribute("role");
String operType = request.getParameter("operType");
%>
<html>
<head>
	<%@ include file="../../common/inc.jsp"%>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
	<title>角色编辑</title>
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
		<%--角色名称--%>
		var roleName = document.getElementById("roleName");
		var roleNameVal = $.trim(roleName.value);
		roleName.value = roleNameVal;
		if(!roleNameVal){
			$.messager.alert("提示信息", "请输入角色名称。", "error", function(){
				roleName.focus();
			});
			return false;
		}
		var memo = document.getElementById("memo");
		var memoVal = $.trim(memo.value);
		memo.value = memoVal;
		if(memoVal && memoVal.length > 100){
			$.messager.alert("提示信息", "角色说明不能超过100个字符。", "error", function(){
				memo.focus();
			});
			return false;
		}
		return true;
	}
	function save(){
		if(!validForm()){
			return;
		}
		$("#roleForm").form("submit", {
			url: "<%=!"update".equals(operType) ? request.getContextPath()+"/sys/role_add.action" : request.getContextPath()+"/sys/role_update.action"%>",
			dataType : "json",
			success : function(data) {
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
	</script>
</head>
<body>
	<div>
		<form id="roleForm" style="margin: 0px;padding: 0px;" method="post" accept-charset="UTF-8">
			<table class="table">
				<tr>
					<td width="20%" class="td_title">角色名称:</td>
					<td>
						<input id="roleName" name="role.roleName" maxlength="50" value="<%=StringUtil.trim(role.getRoleName())%>" />
						<input type="hidden" id="id" name="role.id" value="<%=StringUtil.trim(role.getId())%>" />
					</td>
				</tr>
				<tr>
					<td class="td_title">角色说明:</td>
					<td>
						<textarea maxlength="100" id="memo" name="role.memo" style="width: 95%;height: 85px;"><%=StringUtil.trim(role.getMemo())%></textarea>
					</td>
				</tr>
				<tr>
					<td colspan="2" class="tb_oper" align="center" style="padding: 5px;">
						<a class="easyui-linkbutton" href="javascript:save();">保存</a>
						&nbsp;&nbsp;
						<a class="easyui-linkbutton" href="javascript:closeIt();">关闭</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>