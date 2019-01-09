<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.module.sys.bean.Org"%>
<%@ page import="com.yzxt.yh.module.sys.vo.RegisterVO"%>
<%
RegisterVO registerVO= (RegisterVO)request.getAttribute("registerVO");
Org org = (Org)request.getAttribute("org");
%>
<!DOCTYPE html>
<html>

<head>
	<%@ include file="../common/inc.jsp"%>
	<title>用户管理</title>
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
	body{
		font-family: "微软雅黑"; FONT-SIZE: 11pt
	}
	.mainContent{
		margin-top: 20px;
		width: 60%;
		min-width: 800px;
		margin-left: auto;
		margin-right: auto;
	}
	.td_title{
		text-align: right;
	}
	td{
		padding: 5px;
	}
	</style>
		<script type="text/javascript">
			
		function update(){
		$("#filterForm").form("submit", {
			url: "<%=request.getContextPath()+"/pwd/register_updatePwdToEmail.action"%>",
			dataType : "json",
			success : function(data) {
				data =  $.parseJSON(data); 				
				if(data.state == 1){
					$.messager.alert("提示信息", data.msg?data.msg:"请登录邮箱进行密码重置。", "info", function(){
						try{
						  window.close();
							
						}catch(e){}
					});
				}else{
					$.messager.alert("提示信息", data.msg?data.msg:"密码重置失败。", "error");
				}
				
			}
		});
	}
			
			
			function close() {
				location.href = "<%=request.getContextPath()%>/sys/wel_login.action<%=org!=null?"?orgId="+org.getShowId():""%>";
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
			<span>找回密码</span>
		</div>
		<div class="top-right">
			<span>已有账号?&nbsp;去<a href="<%=request.getContextPath()%>/sys/wel_login.action<%=org!=null?"?orgId="+org.getShowId():""%>">登录</a></span>
		</div>
	</div>
	<div class="titleSplit"></div>
			<div class="mainContent">
				<form id="filterForm" style="margin: 0px;padding: 0px;" method="post" accept-charset="UTF-8">
					<table style="width: 100%;margin-left: auto;margin-right: auto;">
						<tr>
							<td class="td_title" width="50%">请输入登录帐号：</td>
							<td>
								<input type="text" id="username"  name="registerVO.name" maxlength="20" style="width: 160px;" />
							</td>
						</tr>
						<tr>
							<td class="td_title">请输入注册邮箱：</td>
							<td>
								<input type="text" id="email"  name="registerVO.email" maxlength="30" style="width: 160px;" />
							</td>
						</tr>
						<tr>
							<td colspan="4"  align="center">
								<a class="easyui-linkbutton" href="javascript:update()">重设密码</a> &nbsp;&nbsp; 
								<a class="easyui-linkbutton" href="javascript:close()">返回</a>
							</td>
						</tr>
					</table>
				</form>
			</div>
	</body>
</html>