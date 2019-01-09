<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.yzxt.yh.module.sys.bean.User"%>
<%@ page import="com.yzxt.yh.module.sys.bean.Customer"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.util.DateUtil"%>
<%@ page import="com.yzxt.yh.module.sys.bean.DictDetail"%>
<%
Customer cust= (Customer)request.getAttribute("cust");
User user = cust.getUser();
String operType = request.getParameter("operType");
@SuppressWarnings("unchecked")
List<DictDetail> nationals = (List<DictDetail>)request.getAttribute("nationals");
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
		<%--身份证--%>
		var idCard = document.getElementById("idCard");
		var idCardVal = $.trim(idCard.value);
		idCard.value = idCardVal;
		if(idCardVal && !is.idCard(idCardVal)){
			$.messager.alert("提示信息", "身份证号码不正确。", "error", function(){
				idCard.focus();
			});
			return false;
		}
		<%
		// 新增需要判断帐号
		if("add".equals(operType))
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
		<%--联系人电话--%>
		var contactPhone = document.getElementById("contactPhone");
		var contactPhoneVal = $.trim(contactPhone.value);
		contactPhone.value = contactPhoneVal;
		if(contactPhoneVal && !/^[0-9]+-?\d*$/.test(contactPhoneVal)){
			$.messager.alert("提示信息", "联系人电话不正确。", "error", function(){
				contactPhone.focus();
			});
			return false;
		}
		<%--QQ号--%>
		var qqNumber = document.getElementById("qqNumber");
		var qqNumberVal = $.trim(qqNumber.value);
		qqNumber.value = qqNumberVal;
		if(qqNumberVal && !/^[1-9]\d{3,10}$/.test(qqNumberVal)){
			$.messager.alert("提示信息", "QQ号不正确。", "error", function(){
				qqNumber.focus();
			});
			return false;
		}
		<%--微信号--%>
		var wechatNumber = document.getElementById("wechatNumber");
		var wechatNumberVal = $.trim(wechatNumber.value);
		wechatNumber.value = wechatNumberVal;
		<%--地址--%>
		var address = document.getElementById("address");
		var addressVal = $.trim(address.value);
		address.value = addressVal;
		if(address.value.length > 100){
			$.messager.alert("提示信息", "字数不能超过100个！", "error", function(){
				address.focus();
			});
			return;
		}
		var memo = document.getElementById("memo");
		//备注
		if(memo.value.length > 200){
			$.messager.alert("提示信息", "字数不能超过200个！", "error", function(){
				memo.focus();
			});
			return;
		}
		// 验证通过
		return true;
	}
	function save(){
		if(!validForm()){
			return;
		}
		$("#mainForm").form("submit", {
			url: "<%=!"update".equals(operType) ? request.getContextPath()+"/sys/cust_add.action" : request.getContextPath()+"/sys/cust_update.action"%>",
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
<body style="margin: 0px;padding: 1px;">
	<div>
		<form id="mainForm" style="margin: 0px;padding: 0px;" method="post" accept-charset="UTF-8">
			<table class="table">
				<tr>
					<td width="18%" class="td_title">姓名:</td>
					<td width="32%">
						<input type="text" id="userName" name="cust.user.name" value="<%=StringUtil.trim(user.getName())%>" maxlength="20" />
						<span class="must">*</span>
						<input type="hidden" id="userId" name="cust.userId" value="<%=StringUtil.trim(cust.getUserId())%>" />
					</td>
					<td width="18%" class="td_title">身份证号码:</td>
					<td>
						<input type="text" id="idCard" name="cust.user.idCard" value="<%=StringUtil.trim(user.getIdCard())%>" maxlength="18" />
					</td>
				</tr>
				<tr>
					<td class="td_title">性别:</td>
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
					<td class="td_title">出生日期:</td>
					<td>
						<input type="text" class="Wdate" style="width: 90px;" id="birthday" name="cust.birthday" readonly="readonly"
							value="<%=DateUtil.toHtmlDate(cust.getBirthday())%>" maxlength="10" onclick="WdatePicker({})" />
					</td>
				</tr>
				<tr>
					<td class="td_title">登录帐号:</td>
					<td>
						<input type="text" id="account" name="cust.user.account" value="<%=StringUtil.trim(user.getAccount())%>" maxlength="20" <%="update".equals(operType)?"disabled=\"disabled\"":""%> />
						<span class="must">*</span>
					</td>
					<td class="td_title">手机号:</td>
					<td>
						<input type="text" id="phone" name="cust.user.phone" value="<%=StringUtil.trim(user.getPhone())%>" maxlength="11" />
						<span class="must">*</span>
					</td>
				</tr>
				<tr>
					<td class="td_title">Email:</td>
					<td>
						<input type="text" id="email" name="cust.user.email" value="<%=StringUtil.trim(user.getEmail())%>" maxlength="30" />
					</td>
					<td class="td_title">联系人电话:</td>
					<td>
						<input type="text" id="contactPhone" name="cust.contactPhone" value="<%=StringUtil.trim(cust.getContactPhone())%>" maxlength="20" />
					</td>
				</tr>
				<tr>
					<td class="td_title">所属组织:</td>
					<td>
						<%
						String orgId = user.getOrgId();
						if(StringUtil.isEmpty(orgId))
						{
						%>
						<select id="orgId" name="cust.user.orgId" class="easyui-combotree" style="width: 156px;"
							data-options="url:'<%=request.getContextPath()%>/sys/org_treeChildren.action',required:false,valueField:'id',textField:'name',panelHeight:200"></select>  
						<%
						}else
						{
						%>
						<%=StringUtil.ensureStringNotNull(cust.getOrgName())%>
						<input type="hidden" id="orgId" name="cust.user.orgId" value="<%=orgId%>" />
						<%
						}
						%>
					</td>
					<td class="td_title">QQ号:</td>
					<td>
						<input type="text" id="qqNumber" name="cust.qqNumber" value="<%=StringUtil.trim(cust.getQqNumber())%>" maxlength="20" />
					</td>
				</tr>
				<tr>
					<td class="td_title">微信号:</td>
					<td>
						<input type="text" id="wechatNumber" name="cust.wechatNumber" value="<%=StringUtil.trim(cust.getWechatNumber())%>" maxlength="30" />
					</td>
					<td class="td_title">民族:</td>
					<td>
						<select id="national" name="cust.national">
							<option value="">请选择</option>
							<%
							String nationalVal = cust.getNational()!=null?cust.getNational():"";
							for(DictDetail dd : nationals)
							{
							%>
							<option value="<%=dd.getDictDetailCode()%>" <%=nationalVal.equals(dd.getDictDetailCode())?"selected=\"selected\"":""%>><%=dd.getDictDetailName()%></option>
							<%
							}
							%>
						</select>
					</td>
				</tr>
				<tr>
					<td class="td_title">文化程度:</td>
					<td>
						<%
						int degree = cust.getDegree() != null ? cust.getDegree().intValue(): 0;
						%>
						<select id="degree" name="cust.degree">
							<option value="" <%=degree<=0?"selected=\"selected\"":""%>>请选择</option>
							<option value="1" <%=degree==1?"selected=\"selected\"":""%>>文盲及半文盲</option>
							<option value="2" <%=degree==2?"selected=\"selected\"":""%>>小学</option>
							<option value="3" <%=degree==3?"selected=\"selected\"":""%>>文初中</option>
							<option value="4" <%=degree==4?"selected=\"selected\"":""%>>高中/技校/中专</option>
							<option value="5" <%=degree==5?"selected=\"selected\"":""%>>大学专科及以上</option>
							<option value="6" <%=degree==6?"selected=\"selected\"":""%>>不详</option>
						</select>
					</td>
					<td class="td_title">职业:</td>
					<td>
						<%
						String profession = cust.getProfession();
						%>
						<select id="profession" name="cust.profession">
							<option value="" <%=StringUtil.isEmpty(profession)?"selected=\"selected\"":""%>>请选择</option>
							<option value="1" <%="1".equals(profession)?"selected=\"selected\"":""%>>国家机关、党群组织、企业、事业单位负责人</option>
							<option value="2" <%="2".equals(profession)?"selected=\"selected\"":""%>>专业技术人员</option>
							<option value="3" <%="3".equals(profession)?"selected=\"selected\"":""%>>办事人员和有关人员</option>
							<option value="4" <%="4".equals(profession)?"selected=\"selected\"":""%>>商业、服务业人员</option>
							<option value="5" <%="5".equals(profession)?"selected=\"selected\"":""%>>农、林、牧、渔、水利业生产人员</option>
							<option value="6" <%="6".equals(profession)?"selected=\"selected\"":""%>>生产、运输设备操作人员及有关人员</option>
							<option value="7" <%="7".equals(profession)?"selected=\"selected\"":""%>>军人</option>
							<option value="8" <%="8".equals(profession)?"selected=\"selected\"":""%>>不便分类的其他从业人员</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="td_title">是否独居:</td>
					<td>
						<%
						int liveAlone = cust.getLiveAlone()!=null ? cust.getLiveAlone().intValue() : 0;
						%>
						<select id="liveAlone" name="cust.liveAlone">
							<option value="" <%=liveAlone<=0?"selected=\"selected\"":""%>>请选择</option>
							<option value="1" <%=liveAlone==1?"selected=\"selected\"":""%>>是</option>
							<option value="2" <%=liveAlone==2?"selected=\"selected\"":""%>>否</option>
						</select>
					</td>
					<td class="td_title">健康状态:</td>
					<td>
						<%
						int healthyStatus = cust.getHealthyStatus()!=null ? cust.getHealthyStatus().intValue() : 0;
						%>
						<select id="healthyStatus" name="cust.healthyStatus">
							<option value="" <%=healthyStatus<=0?"selected=\"selected\"":""%>>请选择</option>
							<option value="1" <%=healthyStatus==1?"selected=\"selected\"":""%>>健康</option>
							<option value="2" <%=healthyStatus==2?"selected=\"selected\"":""%>>亚健康</option>
							<option value="3" <%=healthyStatus==3?"selected=\"selected\"":""%>>高危</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="td_title">婚姻状态:</td>
					<td colspan="3">
						<%
						int maritalStatus = cust.getMaritalStatus()!=null ? cust.getMaritalStatus().intValue() : 0;
						%>
						<select id="maritalStatus" name="cust.maritalStatus">
							<option value="" <%=maritalStatus<=0?"selected=\"selected\"":""%>>请选择</option>
							<option value="1" <%=maritalStatus==1?"selected=\"selected\"":""%>>未婚</option>
							<option value="2" <%=maritalStatus==2?"selected=\"selected\"":""%>>已婚</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="td_title">地址:</td>
					<td colspan="3">
						<input type="text" maxlength="100" style="width: 85%;" id="address" name="cust.address" value="<%=StringUtil.trim(cust.getAddress())%>" maxlength="100" />
					</td>
				</tr>
				<tr>
					<td class="td_title">备注:</td>
					<td colspan="3">
						<input type="text" maxlength="200" style="width: 85%;" id="memo" name="cust.memo" value="<%=StringUtil.trim(cust.getMemo())%>" maxlength="200" />
					</td>
				</tr>
			</table>
		</form>
		<div style="text-align: center;padding-top: 7px;">
			<a class="easyui-linkbutton" href="javascript:save();">保存</a>
			&nbsp;
			<a class="easyui-linkbutton" href="javascript:closeIt();">关闭</a>
		</div>
	</div>
</body>
</html>