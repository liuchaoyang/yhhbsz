<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.module.sys.bean.User"%>
<%@ page import="com.yzxt.yh.module.sys.bean.Customer"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.util.DateUtil"%>
<%
Customer cust= (Customer)request.getAttribute("cust");
User user = cust.getUser();
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../../common/inc.jsp"%>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
	<title>客户信息</title>
	<style type="text/css">
	</style>
	<script type="text/javascript">
	function closeIt(){
		try{
			parent.subWin.window("close");
		}catch(e){}
	}
</script>
</head>
<body style="margin: 0px;padding: 1px;">
	<div>
		<form id="mainForm" style="margin: 0px;padding: 0px;" method="post" accept-charset="UTF-8">
			<table class="table">
				<tr>
					<td width="18%" class="td_title">姓名:</td>
					<td width="32%">
						<%=StringUtil.trim(user.getName())%>
						<input type="hidden" id="userId" name="cust.userId" value="<%=StringUtil.trim(cust.getUserId())%>" />
					</td>
					<td width="18%" class="td_title">身份证号码:</td>
					<td>
						<%=StringUtil.trim(user.getIdCard())%>
					</td>
				</tr>
				<tr>
					<td class="td_title">性别:</td>
					<td>
						<%
						int sex = cust.getSex() != null ? cust.getSex().intValue(): 0;
						%>
						<%=sex==1?"男":""%>
						<%=sex==2?"女":""%>
					</td>
					<td class="td_title">出生日期:</td>
					<td>
						<%=DateUtil.toHtmlDate(cust.getBirthday())%>
					</td>
				</tr>
				<tr>
					<td class="td_title">登录帐号:</td>
					<td>
						<%=StringUtil.trim(user.getAccount())%>
					</td>
					<td class="td_title">手机号:</td>
					<td>
						<%=StringUtil.trim(user.getPhone())%>
					</td>
				</tr>
				<tr>
					<td class="td_title">Email:</td>
					<td>
						<%=StringUtil.trim(user.getEmail())%>
					</td>
					<td class="td_title">联系人电话:</td>
					<td>
						<%=StringUtil.trim(cust.getContactPhone())%>
					</td>
				</tr>
				<tr>
					<td class="td_title">所属组织:</td>
					<td><%=StringUtil.trim(cust.getOrgName())%></td>
					<td class="td_title">QQ号:</td>
					<td>
						<%=StringUtil.trim(cust.getQqNumber())%>
					</td>
				</tr>
				<tr>
					<td class="td_title">微信号:</td>
					<td>
						<%=StringUtil.trim(cust.getWechatNumber())%>
					</td>
					<td class="td_title">民族:</td>
					<td>
						<%=StringUtil.trim(cust.getNationalName())%>
					</td>
				</tr>
				<tr>
					<td class="td_title">文化程度:</td>
					<td>
						<%
						int degree = cust.getDegree() != null ? cust.getDegree().intValue(): 0;
						%>
						<%=degree==1?"文盲及半文盲":""%>
						<%=degree==2?"小学":""%>
						<%=degree==3?"初中":""%>
						<%=degree==4?"高中/技校/中专":""%>
						<%=degree==5?"大学专科及以上":""%>
						<%=degree==6?"不详":""%>
					</td>
					<td class="td_title">职业:</td>
					<td>
						<%
						String profession = cust.getProfession();
						%>
						<%="1".equals(profession)?"国家机关、党群组织、企业、事业单位负责人":""%>
						<%="2".equals(profession)?"专业技术人员":""%>
						<%="3".equals(profession)?"办事人员和有关人员":""%>
						<%="4".equals(profession)?"商业、服务业人员":""%>
						<%="5".equals(profession)?"农、林、牧、渔、水利业生产人员":""%>
						<%="6".equals(profession)?"生产、运输设备操作人员及有关人员":""%>
						<%="7".equals(profession)?"军人":""%>
						<%="8".equals(profession)?"不便分类的其他从业人员":""%>
					</td>
				</tr>
				<tr>
					<td class="td_title">是否独居:</td>
					<td>
						<%
						int liveAlone = cust.getLiveAlone()!=null ? cust.getLiveAlone().intValue() : 0;
						%>
						<%=liveAlone==1?"是":""%>
						<%=liveAlone==2?"否":""%>
					</td>
					<td class="td_title">健康状态:</td>
					<td>
						<%
						int healthyStatus = cust.getHealthyStatus()!=null ? cust.getHealthyStatus().intValue() : 0;
						%>
						<%=healthyStatus==1?"健康":""%>
						<%=healthyStatus==2?"亚健康":""%>
						<%=healthyStatus==3?"高危":""%>
					</td>
				</tr>
				<tr>
					<td class="td_title">婚姻状态:</td>
					<td colspan="3">
						<%
						int maritalStatus = cust.getMaritalStatus()!=null ? cust.getMaritalStatus().intValue() : 0;
						%>
						<%=maritalStatus==1?"未婚":""%>
						<%=maritalStatus==2?"已婚":""%>
					</td>
				</tr>
				<tr>
					<td class="td_title">地址:</td>
					<td colspan="3">
						<%=StringUtil.trim(cust.getAddress())%>
					</td>
				</tr>
				<tr>
					<td class="td_title">备注:</td>
					<td colspan="3">
						<%=StringUtil.trim(cust.getMemo())%>
					</td>
				</tr>
			</table>
		</form>
		<div style="text-align: center;padding: 7px;">
			<a class="easyui-linkbutton" href="javascript:closeIt();">关闭</a>
		</div>
	</div>
</body>
</html>