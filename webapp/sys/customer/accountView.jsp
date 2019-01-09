<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.module.sys.bean.User"%>
<%@ page import="com.yzxt.yh.module.sys.bean.Customer"%>
<%@ page import="com.yzxt.yh.util.DateUtil"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%
Customer cust= (Customer)request.getAttribute("cust");
User user = cust.getUser();
User operUser = (User)request.getAttribute("operUser");
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../../common/inc.jsp"%>
	<title>客户账户信息</title>
	<style type="text/css">
	.table tr{
		height: 35px;
	}
	</style>
	<script type="text/javascript">
	function edit(){
		location.href = "<%=request.getContextPath()%>/sys/cust_toAccount.action?operType=edit";
	}
	</script>
</head>
<body>
	<div style="margin-left: auto;margin-right: auto;width: 750px;">
			<div class="sectionTitle">个人信息</div>
			<div>
				<form style="margin: 0px;padding: 0px;" method="post" accept-charset="UTF-8">
					<table class="table">
						<tr>
							<td class="td_title">用户帐号：</td>
							<td>
								<%=StringUtil.trim(user.getAccount())%>
							</td>
						</tr>
						<tr>
							<td class="td_title">身份证号码：</td>
							<td>
								<%=StringUtil.trim(user.getIdCard())%>
							</td>
						</tr>
						<tr>
							<td class="td_title">用户姓名：</td>
							<td>
								<%=StringUtil.trim(user.getName())%>
							</td>
						</tr>
						<tr>
							<td width="35%" class="td_title">性别：</td>
							<td>
							<%
							int sex = cust.getSex() != null ? cust.getSex().intValue(): 0;
							if(sex==1)
							{
							%>
							男
							<%
							}else if(sex==1)
							{
							%>
							女
							<%
							}
							%>
							</td>
						</tr>
						<tr>
							<td class="td_title">出生日期：</td>
							<td>
								<%=DateUtil.toHtmlDate(cust.getBirthday())%>
							</td>
						</tr>
						<tr>
							<td class="td_title">手机号：</td>
							<td>
								<%=StringUtil.trim(user.getPhone())%>
							</td>
						</tr>
						<tr>
							<td class="td_title">Email：</td>
							<td>
								<%=StringUtil.trim(user.getEmail())%>
							</td>
						</tr>
						<tr>
							<td class="td_title">通讯地址：</td>
							<td>
								<%=StringUtil.trim(cust.getAddress())%>
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
				<a class="easyui-linkbutton" href="javascript:edit();">修改</a>
			</div>
			<%
			}
			%>
		</div>
	</body>
</html>