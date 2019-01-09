<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.util.HashSet"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Date"%>
<%@ page import="com.yzxt.yh.module.sys.bean.DictDetail"%>
<%@ page import="com.yzxt.yh.util.DateUtil"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.module.sys.bean.User"%>
<%@ page import="com.yzxt.yh.module.sys.bean.Customer"%>
<%@ page import="com.yzxt.yh.module.ach.bean.Archive"%>
<%@ page import="com.yzxt.yh.module.ach.bean.FamilyHistory"%>
<%@ page import="com.yzxt.yh.module.ach.bean.LifeEnv"%>
<%@page import="com.yzxt.yh.module.ach.bean.PreviousHistory"%>
<%
Customer cust = (Customer)request.getAttribute("cust");
User user = cust.getUser();
Archive archive = (Archive)request.getAttribute("archive");
List<FamilyHistory> familyHistorys= archive.getFamilyHistorys();
List<LifeEnv> lifeEnvs= archive.getLifeEnvs();
List<PreviousHistory> previousHistorys= archive.getPreviousHistorys();
@SuppressWarnings("unchecked")
List<DictDetail> nationals = (List<DictDetail>)request.getAttribute("nationals");
String nowStr = DateUtil.toHtmlDate(new Date());
//是否是从查询页面链接进来的
String fq = request.getParameter("fq");
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>档案信息</title>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
	<style type="text/css">
	.sectTitleWrap{
		background: #F7F7F7;
		height: 30px;
		border: 1px solid #AAAAAA;
		border-bottom: 0px solid #F7F7F7;
		clear: both;
	}
	.sectTitle{
		padding-left: 10px;
		font-weight: bold;
		font-size: 12px;
		line-height: 30px;
		float: left;
	}
	.sectSplit{
		height: 5px;
	}
	.phItem{
		padding: 3px;
	}
	.optItem{
		margin-left: 3px;
		margin-right: 3px;
		margin-top: 3px;
		margin-bottom: 3px;
	}
	input[type="text"].dateInput{
		 width: 100px;
	}
	</style>
	<script type="text/javascript">
	function addJB(){
		var r = "<div class='phItem'><input type='checkbox' name='jbchk' />&nbsp;疾病名称:<select name='jbname'>"
			+"<option value='1'>无</option>"
			+"<option value='2'>高血压</option>"
			+"<option value='3'>糖尿病</option>"
			+"<option value='4'>冠心病</option>"
			+"<option value='5'>慢性阻塞性肺疾病</option>"
			+"<option value='6'>恶性肿瘤</option>"
			+"<option value='7'>脑卒中</option>"
			+"<option value='8'>重性精神疾病</option>"
			+"<option value='9'>结核病</option>"
			+"<option value='10'>肝炎</option>"
			+"<option value='11'>其他法定传染病</option>"
			+"<option value='12'>职业病</option>"
			+"</select>&nbsp;&nbsp;&nbsp;确诊时间:<input type='text' readonly='readonly' class='Wdate dateInput' name='jbdate' onclick=\"WdatePicker()\" /></div>";
		$("#jbDiv").append(r);
	}
	function delJB(){
		var chks = document.getElementsByName("jbchk");
		if(chks){
			for(var i=chks.length-1;i>=0;i--){
				if(chks[i].checked){
					var divE = chks[i].parentNode;
					divE.parentNode.removeChild(divE);
				}
			}
		}
	}
	function addSS(){
		var r = "<div class='phItem'><input type='checkbox' name='sschk' />&nbsp;手术名称:<input type='text' maxlength='50' class='ssInput' name='ssname' />"
			+"&nbsp;&nbsp;&nbsp;手术时间:<input type='text' readonly='readonly' class='Wdate dateInput' name='ssdate' onclick=\"WdatePicker()\" /></div>";
		$("#ssDiv").append(r);
	}
	function delSS(){
		var chks = document.getElementsByName("sschk");
		if(chks){
			for(var i=chks.length-1;i>=0;i--){
				if(chks[i].checked){
					var divE = chks[i].parentNode;
					divE.parentNode.removeChild(divE);
				}
			}
		}
	}
	function addWS(){
		var r = "<div class='phItem'><input type='checkbox' name='wschk' />&nbsp;外伤名称:<input type='text' maxlength='50' class='wsInput' name='wsname' />"
			+"&nbsp;&nbsp;&nbsp;外伤时间:<input type='text' readonly='readonly' class='Wdate dateInput' name='wsdate' onclick=\"WdatePicker()\" /></div>";
		$("#wsDiv").append(r);
	}
	function delWS(){
		var chks = document.getElementsByName("wschk");
		if(chks){
			for(var i=chks.length-1;i>=0;i--){
				if(chks[i].checked){
					var divE = chks[i].parentNode;
					divE.parentNode.removeChild(divE);
				}
			}
		}
	}
	function addSX(){
		var r = "<div class='phItem'><input type='checkbox' name='sxchk' />&nbsp;输血原因:<input type='text' maxlength='50' class='sxInput' name='sxname' />"
			+"&nbsp;&nbsp;&nbsp;输血时间:<input type='text' readonly='readonly' class='Wdate dateInput' name='sxdate' onclick=\"WdatePicker()\" /></div>";
		$("#sxDiv").append(r);
	}
	function delSX(){
		var chks = document.getElementsByName("sxchk");
		if(chks){
			for(var i=chks.length-1;i>=0;i--){
				if(chks[i].checked){
					var divE = chks[i].parentNode;
					divE.parentNode.removeChild(divE);
				}
			}
		}
	}
	function validForm(){
		var custName = document.getElementById("custName");
		var custNameVal = $.trim(custName.value);
		custName.value = custNameVal;
		if(!custNameVal){
			$.messager.alert("提示信息", "请输入姓名。", "error", function(){
				custName.focus();
			});
			return false;
		}
		<%--建档单位--%>
		var medicalEstab = document.getElementById("medicalEstab");
		var medicalEstabVal = $.trim(medicalEstab.value);
		medicalEstab.value = medicalEstabVal;
		<%--建档单位电话--%>
		var telMedical = document.getElementById("telMedical");
		var telMedicalVal = $.trim(telMedical.value);
		telMedical.value = telMedicalVal;
		if(telMedicalVal && !/^\d+-?\d*$/.test(telMedicalVal)){
			$.messager.alert("提示信息", "建档单位电话不正确。", "error", function(){
				telMedical.focus();
			});
			return false;
		}
		var contactName = document.getElementById("contactName");
		var contactNameVal = $.trim(contactName.value);
		contactName.value = contactNameVal;
		<%--联系人电话--%>
		var contactTelphone = document.getElementById("contactTelphone");
		var contactTelphoneVal = $.trim(contactTelphone.value);
		contactTelphone.value = contactTelphoneVal;
		if(contactTelphoneVal && !/^\d+-?\d*$/.test(contactTelphoneVal)){
			$.messager.alert("提示信息", "联系人电话不正确。", "error", function(){
				contactTelphone.focus();
			});
			return false;
		}
		// 既往史
		var ssnames = document.getElementsByName("ssname");
		if(ssnames){
			for(var i=0;i<ssnames.length;i++){
				ssnames[i].value = $.trim(ssnames[i].value);
			}
		}
		var wsnames = document.getElementsByName("wsname");
		if(wsnames){
			for(var i=0;i<wsnames.length;i++){
				wsnames[i].value = $.trim(wsnames[i].value);
			}
		}
		var sxnames = document.getElementsByName("sxname");
		if(sxnames){
			for(var i=0;i<sxnames.length;i++){
				sxnames[i].value = $.trim(sxnames[i].value);
			}
		}
		<%--遗传病史--%>
		var geneticHistory = document.getElementById("geneticHistory");
		var geneticHistoryVal = $.trim(geneticHistory.value);
		geneticHistory.value = geneticHistoryVal;
		return true;
	}
	function saveArchive(){
		if(!validForm()){
			return;
		}
		// 表单提交
		$("#fm").form("submit", {
			url : "<%=request.getContextPath()%>/ach/ach_save.action",
			dataType : "json",
			success : function(data) {
				data =  $.parseJSON(data);
				if(data.state > 0){
					$.messager.alert("提示信息", "保存成功.", "info", function(){
						refreshPage("${param.pPageId}");
						<%
						if(!"Y".equals(fq))
						{
						%>
						location.href = "<%=request.getContextPath()%>/ach/ach_toDetail.action?operType=view&custId=<%=archive.getCustId()%>";
						<%
						}else
						{
						%>
						closeIt();
						<%
						}
						%>
					});
				}else{
					$.messager.alert("提示信息", data.msg?data.msg:"保存出错.", "error");
				}
			}
		});
	}
	function closeIt(){
		try{
			var tc = top.$("#tb");
			var tab = tc.tabs("getSelected");
			if (tab) {
				tc.tabs("close", tab.panel("options").title);
			}
		}catch(e){}
	}
	</script>
</head>
<body>
	<div style="width: 800px;margin-left: auto;margin-right: auto;">
	<form id="fm" style="margin: 0px;padding: 0px;" method="post" enctype="multipart/form-data" accept-charset="UTF-8">
		<div class="sectTitleWrap">
			<div class="sectTitle">用户信息</div>
		</div>
		<div class="contentPanel" id="conBase">
			<table class="table">
				<tr style="height: 20px;">
					<td rowspan="5" style="width: 100px;">
						<%
						if(StringUtil.isNotEmpty(user.getImgFilePath()))
						{
						%>
						<img  width="100%" height="120" alt="" src="<%=request.getContextPath()%>/sys/fd_img.action?pt=<%=user.getImgFilePath()%>" />
						<%
						}else
						{
						%>
						<img width="100" height="100" src="<%=request.getContextPath()%>/resources/img/man.png" />
						<%
						}
						%>
					</td>
					<td class="td_title" width="90">姓名:</td>
					<td width="120">
						<input type="text" maxlength="20" style="width: 100px;" id="custName" name="cust.user.name" value="<%=StringUtil.trim(user.getName())%>" />
						<input type="hidden" name="archive.custId" value="<%=archive.getCustId()%>" />
					</td>
					<td class="td_title" width="90">性别:</td>
					<td width="120">
						<%
						int sex = cust.getSex()!=null ? cust.getSex().intValue() : 2;
						%>
						<span class="optItem"><input type="radio" style="width: 20px;" name="cust.sex" value="0" <%=sex==0 ? "checked=\"checked\"" : ""%> />男</span>
						<span class="optItem"><input type="radio" style="width: 20px;" name="cust.sex" value="1" <%=sex==1 ? "checked=\"checked\"" : ""%> />女</span>
					</td>
					<td class="td_title" width="90">年龄:</td>
					<td>
						<%=StringUtil.ensureStringNotNull(cust.getAge())%>
					</td>
				</tr>
				<tr style="height: 20px;">
					<td class="td_title">身份证:</td>
					<td>
						<%=StringUtil.trim(user.getIdCard())%>
					</td>
					<td class="td_title">联系电话:</td>
					<td colspan="3">
						<%=StringUtil.trim(user.getPhone())%>
					</td>
				</tr>
				<tr style="height: 20px;">
					<td class="td_title">联系地址:</td>
					<td colspan="5">
						<input type="text" maxlength="100" style="width: 95%;" id="address" name="cust.address" value="<%=StringUtil.ensureStringNotNull(cust.getAddress())%>" />
					</td>
				</tr>
				<tr style="height: 20px;">
					<td class="td_title">备注:</td>
					<td colspan="5">
						<input type="text" maxlength="200" style="width: 95%;" id="memo" name="cust.memo" value="<%=StringUtil.ensureStringNotNull(cust.getMemo())%>" />
					</td>
				</tr>
				<tr style="height: 20px;">
					<td class="td_title" >用户头像:</td>
					<td colspan="5">
						<input type="file" id="userImg" name="userImg" style="width: 300px;" accept=".gif,.jpg,.jpeg,.png,.JPG" />
					</td>
				</tr>
			</table>
		</div>
		<div class="sectSplit"></div>
		<div class="sectTitleWrap">
			<div class="sectTitle">档案封面</div>
		</div>
		<div class="contentPanel" id="conCover">
			<table class="table">
				<tr style="height: 30px;">
					<td class="td_title" width="20%">户籍地址:</td>
					<td colspan="3">
						<input type="text" maxlength="100" style="width: 95%;" id="achive.householdAddress" name="archive.householdAddress" value="<%=StringUtil.trim(archive.getHouseholdAddress())%>" />
					</td>
				</tr>
				<tr style="height: 30px;">
					<td class="td_title">乡镇(街道)名称:</td>
					<td colspan="3">
						<input type="text" maxlength="100" style="width: 95%;" id="streetName" name="archive.streetName" value="<%=StringUtil.trim(archive.getStreetName())%>" />
					</td>
				</tr>
				<tr style="height: 30px;">
					<td class="td_title">村(居)委会名称:</td>
					<td colspan="3">
						<input type="text" maxlength="100" style="width: 95%;" id="villageName" name="archive.villageName" value="<%=StringUtil.trim(archive.getVillageName())%>" />
					</td>
				</tr>
				<tr style="height: 30px;">
					<td class="td_title" width="20%">建档单位:</td>
					<td>
						<input type="text" maxlength="30" id="medicalEstab" name="archive.medicalEstab" value="<%=StringUtil.trim(archive.getMedicalEstab())%>" />
					</td>
					<td class="td_title" width="20%">建档单位电话:</td>
					<td>
						<input type="text" maxlength="20" id="telMedical" name="archive.telMedical" value="<%=StringUtil.trim(archive.getTelMedical())%>" />
					</td>
				</tr>
				<tr style="height: 30px;">
					<td class="td_title" width="20%">建  档  人:</td>
					<td  width="30%">
						<%=StringUtil.trim(archive.getDoctorName())%>
					</td>
					<td class="td_title" width="20%">建档日期:</td>
					<td>
						<%=DateUtil.toHtmlDate(archive.getCreateTime())%>
					</td>
				</tr>
			</table>
		</div>
		<div class="sectSplit"></div>
		<div class="sectTitleWrap">
			<div class="sectTitle">个人基本信息</div>
		</div>
		<div class="contentPanel" id="conPerson">
			<table class="table">
				<tr style="height: 30px;">
					<td class="td_title" colspan="2" width="20%">出生日期:</td>
					<td width="30%">
						<input type="text" maxlength="10" class="Wdate" style="width: 100px;" readonly="readonly" name="cust.birthday" value="<%=DateUtil.toHtmlDate(cust.getBirthday())%>" onclick="WdatePicker()" />
					</td>
					<td class="td_title" width="20%">工作单位:</td>
					<td colspan="2">
						<input type="text" maxlength="100" id="workUnit" name="archive.workUnit" value="<%=StringUtil.trim(archive.getWorkUnit())%>" />
					</td>
				</tr>
				<tr>
					<td class="td_title" colspan="2">联系人姓名:</td>
					<td>
						<input type="text" maxlength="20" id="contactName" name="archive.contactName" value="<%=StringUtil.trim(archive.getContactName())%>" />
					</td>
					<td class="td_title">联系人电话:</td>
					<td colspan="2">
						<input type="text" maxlength="20" id="contactTelphone" name="archive.contactTelphone" value="<%=StringUtil.trim(archive.getContactTelphone())%>" />
					</td>
				</tr>
				<tr>
					<td class="td_title" colspan="2">常住类型:</td>
					<td>
						<%
						int householdType = archive.getHouseholdType()!=null?archive.getHouseholdType().intValue():0;
						%>
						<span class="optItem"><input type="radio" name="archive.householdType" value="1" <%=householdType==1 ? "checked=\"checked\"" : ""%> />户籍</span>
						<span class="optItem"><input type="radio" name="archive.householdType" value="2" <%=householdType==2 ? "checked=\"checked\"" : ""%> />非户籍</span>
					</td>
					<td class="td_title">民  族:</td>
					<td colspan="2">
						<select id="national" name="cust.national" style="width: 100px;">
							<option value="">请选择</option>
							<%
							String nationalVal = cust.getNational()!=null?cust.getNational():"";
							if(nationals!=null)
							{
								for(DictDetail dd : nationals)
								{
							%>
							<option value="<%=dd.getDictDetailCode()%>" <%=nationalVal.equals(dd.getDictDetailCode())?"selected=\"selected\"":""%>><%=dd.getDictDetailName()%></option>
							<%
								}
							}
							%>
						</select>
					</td>
				</tr>
				<tr>
					<td class="td_title" colspan="2">血    型:</td>
					<td>
						<%
						int bloodType = archive.getBloodType()!=null ? archive.getBloodType().intValue() : 0;
						%>
						<span class="optItem"><input type="radio" name="archive.bloodType" value="1" <%=bloodType==1 ? "checked=\"checked\"" : ""%> />A</span>
						<span class="optItem"><input type="radio" name="archive.bloodType" value="2" <%=bloodType==2 ? "checked=\"checked\"" : ""%> />B</span>
						<span class="optItem"><input type="radio" name="archive.bloodType" value="3" <%=bloodType==3 ? "checked=\"checked\"" : ""%> />O</span>
						<span class="optItem"><input type="radio" name="archive.bloodType" value="4" <%=bloodType==4 ? "checked=\"checked\"" : ""%> />AB</span>
						<span class="optItem"><input type="radio" name="archive.bloodType" value="5" <%=bloodType==5 ? "checked=\"checked\"" : ""%> />不详</span>
					</td>
					<td class="td_title">RH阴性:</td>
					<td colspan="2">
						<%
						int rhNagative = archive.getRhNagative()!=null ? archive.getRhNagative().intValue() : 0;
						%>
						<span class="optItem"><input type="radio" name="archive.rhNagative" value="1" <%=bloodType==1 ? "checked=\"checked\"" : ""%> />是</span>
						<span class="optItem"><input type="radio" name="archive.rhNagative" value="1" <%=bloodType==2 ? "checked=\"checked\"" : ""%> />否</span>
						<span class="optItem"><input type="radio" name="archive.rhNagative" value="1" <%=bloodType==3 ? "checked=\"checked\"" : ""%> />不详</span>
					</td>
				</tr>
				<tr>
					<td class="td_title" colspan="2">文化程度:</td>
					<td colspan="4">
						<%
						int degree = archive.getDegree()!=null ? archive.getDegree().intValue() : 0;
						%>
						<span class="optItem"><input type="radio" name="archive.degree" value="1" <%=degree==1 ? "checked=\"checked\"" : ""%> />文盲及半文盲</span>
						<span class="optItem"><input type="radio" name="archive.degree" value="2" <%=degree==2 ? "checked=\"checked\"" : ""%> />小学</span>
						<span class="optItem"><input type="radio" name="archive.degree" value="3" <%=degree==3 ? "checked=\"checked\"" : ""%> />初中</span>
						<span class="optItem"><input type="radio" name="archive.degree" value="4" <%=degree==4 ? "checked=\"checked\"" : ""%> />高中/技校/中专</span>
						<span class="optItem"><input type="radio" name="archive.degree" value="5" <%=degree==5 ? "checked=\"checked\"" : ""%> />大学专科及以上</span>
						<span class="optItem"><input type="radio" name="archive.degree" value="6" <%=degree==6 ? "checked=\"checked\"" : ""%> />不详</span>
					</td>
				</tr>
				 <tr>
					<td align="left" class="td_title" colspan="2">职    业:</td>
					<td colspan="4">
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
					<td class="td_title" colspan="2">婚姻状况:</td>
					<td colspan="4">
						<%
							int maritalStatus = archive.getMaritalStatus()!=null?archive.getMaritalStatus().intValue():0;
						%>
						<span class="optItem"><input type="radio" name="archive.maritalStatus" value="1" <%=maritalStatus==1 ? "checked=\"checked\"" : ""%> />未婚</span>
						<span class="optItem"><input type="radio" name="archive.maritalStatus" value="2" <%=maritalStatus==2 ? "checked=\"checked\"" : ""%> />已婚</span>
						<span class="optItem"><input type="radio" name="archive.maritalStatus" value="3" <%=maritalStatus==3 ? "checked=\"checked\"" : ""%> />丧偶</span>
						<span class="optItem"><input type="radio" name="archive.maritalStatus" value="4" <%=maritalStatus==4 ? "checked=\"checked\"" : ""%> />离婚</span>
						<span class="optItem"><input type="radio" name="archive.maritalStatus" value="5" <%=maritalStatus==5 ? "checked=\"checked\"" : ""%> />未说明婚姻状况</span>
					</td>
				</tr>
				<tr>
					<td class="td_title" colspan="2">医疗费用支付方式:</td>
					<td colspan="4" style="line-height: 25px;">
						<%
							String payType = archive.getPayType();
							List<String> payTypes = StringUtil.splitIgnoreEmpty(payType, ";");
						%>
						<span class="optItem"><input type="checkbox" name="payType" value="1" <%=payTypes.contains("1") ? "checked=\"checked\"" : ""%> />城镇职工基本医疗保险</span>
						<span class="optItem"><input type="checkbox" name="payType" value="2" <%=payTypes.contains("2") ? "checked=\"checked\"" : ""%> />城镇居民基本医疗保险</span>
						<span class="optItem"><input type="checkbox" name="payType" value="3" <%=payTypes.contains("3") ? "checked=\"checked\"" : ""%> />新型农村合作医疗</span>
						<span class="optItem"><input type="checkbox" name="payType" value="4" <%=payTypes.contains("4") ? "checked=\"checked\"" : ""%> />贫困救助</span>
						<br />
						<span class="optItem"><input type="checkbox" name="payType" value="5" <%=payTypes.contains("5") ? "checked=\"checked\"" : ""%> />商业医疗保险</span>
						<span class="optItem"><input type="checkbox" name="payType" value="6" <%=payTypes.contains("6") ? "checked=\"checked\"" : ""%> />全公费</span>
						<span class="optItem"><input type="checkbox" name="payType" value="7" <%=payTypes.contains("7") ? "checked=\"checked\"" : ""%> />全自费</span>
						<span class="optItem"><input type="checkbox" name="payType" value="8" <%=payTypes.contains("8") ? "checked=\"checked\"" : ""%> />其他</span>
					</td>
				</tr>
				<tr>
					<td class="td_title" colspan="2">药物过敏史:</td>
					  <td colspan="4">
						<%
							String hoda = archive.getHoda();
							List<String> hodas = StringUtil.splitIgnoreEmpty(hoda, ";");
						%>
						<span class="optItem"><input type="checkbox" name="hoda" value="2" <%=hodas.contains("2") ? "checked=\"checked\"" : ""%> />青霉素</span>
						<span class="optItem"><input type="checkbox" name="hoda" value="3" <%=hodas.contains("3") ? "checked=\"checked\"" : ""%> />磺胺</span>
						<span class="optItem"><input type="checkbox" name="hoda" value="4" <%=hodas.contains("4") ? "checked=\"checked\"" : ""%> />链霉素</span>
						<span class="optItem"><input type="checkbox" name="hoda" value="5" <%=hodas.contains("5") ? "checked=\"checked\"" : ""%> />其他</span>
					</td>
				</tr>
				<tr>
					<td class="td_title" colspan="2">暴 露 史:</td>
					<td colspan="4">
						<%
							String exposureHistory = archive.getExposureHistory();
							List<String> exposureHistorys = StringUtil.splitIgnoreEmpty(exposureHistory, ";");
						%>
						<span class="optItem"><input type="checkbox" name="exposureHistory" value="2" <%=exposureHistorys.contains("2") ? "checked=\"checked\"" : ""%> />化学品</span>
						<span class="optItem"><input type="checkbox" name="exposureHistory" value="3" <%=exposureHistorys.contains("3") ? "checked=\"checked\"" : ""%> />毒物</span>
						<span class="optItem"><input type="checkbox" name="exposureHistory" value="4" <%=exposureHistorys.contains("4") ? "checked=\"checked\"" : ""%> />射线</span>
					</td>
				</tr>
				<tr>
					<td rowspan="4" align="left" class="td_title" width="10%">既往史:</td>
					<td class="td_title" width="10%">疾病</td>
					<td colspan="3">
						<div id="jbDiv">
						<%
						if(previousHistorys!=null)
						{
							for(PreviousHistory previousHistory : previousHistorys)
						 	{
								if(previousHistory.getType()==1)
								{
						%>
							<div class='phItem'><input type='checkbox' name='jbchk' />&nbsp;疾病名称:<select name='jbname'>
								<option value='1' <%="1".equals(previousHistory.getName())?"selected=\"selected\"":""%>>无</option>
								<option value='2' <%="2".equals(previousHistory.getName())?"selected=\"selected\"":""%>>高血压</option>
								<option value='3' <%="3".equals(previousHistory.getName())?"selected=\"selected\"":""%>>糖尿病</option>
								<option value='4' <%="4".equals(previousHistory.getName())?"selected=\"selected\"":""%>>冠心病</option>
								<option value='5' <%="5".equals(previousHistory.getName())?"selected=\"selected\"":""%>>慢性阻塞性肺疾病</option>
								<option value='6' <%="6".equals(previousHistory.getName())?"selected=\"selected\"":""%>>恶性肿瘤</option>
								<option value='7' <%="7".equals(previousHistory.getName())?"selected=\"selected\"":""%>>脑卒中</option>
								<option value='8' <%="8".equals(previousHistory.getName())?"selected=\"selected\"":""%>>重性精神疾病</option>
								<option value='9' <%="9".equals(previousHistory.getName())?"selected=\"selected\"":""%>>结核病</option>
								<option value='10' <%="10".equals(previousHistory.getName())?"selected=\"selected\"":""%>>肝炎</option>
								<option value='11' <%="11".equals(previousHistory.getName())?"selected=\"selected\"":""%>>其他法定传染病</option>
								<option value='12' <%="12".equals(previousHistory.getName())?"selected=\"selected\"":""%>>职业病</option>
								</select>&nbsp;&nbsp;&nbsp;确诊时间:<input type='text' readonly='readonly' class='Wdate dateInput' name='jbdate'
									onclick="WdatePicker()" value="<%=DateUtil.toHtmlDate(previousHistory.getPastTime())%>" />
							</div>
						<%
								}
							}
						}
						%>
						</div>
					</td>
					<td width="100">
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="addJB()">添加</a>
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="delJB()">删除</a>
					</td>
				</tr>
				<tr>
					<td class="td_title">手术</td>
					<td colspan="3">
						<div id="ssDiv">
						<%
						if(previousHistorys!=null)
						{
							for(PreviousHistory previousHistory : previousHistorys)
						 	{
								if(previousHistory.getType()==2)
								{
						%>
							<div class='phItem'><input type='checkbox' name='sschk' />&nbsp;手术名称:<input type='text' maxlength='50'
								class='ssInput' name='ssname' value="<%=StringUtil.ensureStringNotNull(previousHistory.getName())%>" />&nbsp;&nbsp;&nbsp;手术时间:<input type='text' readonly='readonly'
								class='Wdate dateInput' name='ssdate' onclick="WdatePicker()" value="<%=DateUtil.toHtmlDate(previousHistory.getPastTime())%>" /></div>
						<%
								}
							}
						}
						%>
						</div>
					</td>
					<td>
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="addSS()">添加</a>
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="delSS()">删除</a>
					</td>
				</tr>
				<tr>
					<td class="td_title">外伤</td>
					<td colspan="3">
						<div id="wsDiv">
						<%
						if(previousHistorys!=null)
						{
							for(PreviousHistory previousHistory : previousHistorys)
						 	{
								if(previousHistory.getType()==3)
								{
						%>
							<div class='phItem'><input type='checkbox' name='wschk' />&nbsp;外伤名称:<input type="text" maxlength="50"
								class='wsInput' name='wsname' value="<%=StringUtil.ensureStringNotNull(previousHistory.getName())%>" />&nbsp;&nbsp;&nbsp;外伤时间:<input type='text' readonly='readonly'
								class='Wdate dateInput' name='wsdate' onclick="WdatePicker()" value="<%=DateUtil.toHtmlDate(previousHistory.getPastTime())%>" /></div>
						<%
								}
							}
						}
						%>
						</div>
					</td>
					<td>
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="addWS()">添加</a>
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="delWS()">删除</a>
					</td>
				</tr>
				<tr>
					<td class="td_title">输血</td>
					<td colspan="3">
						<div id="sxDiv">
						<%
						if(previousHistorys!=null)
						{
							for(PreviousHistory previousHistory : previousHistorys)
						 	{
								if(previousHistory.getType()==4)
								{
						%>
							<div class='phItem'><input type='checkbox' name='sxchk' />&nbsp;输血原因:<input type='text' maxlength='50'
								class='sxInput' name='sxname' value="<%=StringUtil.ensureStringNotNull(previousHistory.getName())%>" />&nbsp;&nbsp;&nbsp;输血时间:<input type='text' readonly='readonly'
								class='Wdate dateInput' name='sxdate' onclick="WdatePicker()" value="<%=DateUtil.toHtmlDate(previousHistory.getPastTime())%>" /></div>
						<%
								}
							}
						}
						%>
						</div>
					</td>
					<td>
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="addSX()">添加</a>
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="delSX()">删除</a>
					</td>
				</tr>
				<tr>
					<%
					Set<String> fhFathers = new HashSet<String>();
					Set<String> fhMotners = new HashSet<String>();
					Set<String> fhBrothers = new HashSet<String>();
					Set<String> fhChildrens = new HashSet<String>();
					if(familyHistorys!=null){
					    for(FamilyHistory fh : familyHistorys)
					    {
					        if(fh.getRelaType()==1)
					        {
					            fhFathers.add(fh.getDisease());
					        }else if(fh.getRelaType()==2)
					        {
					            fhMotners.add(fh.getDisease());
					        }else if(fh.getRelaType()==3)
					        {
					            fhBrothers.add(fh.getDisease());
					        }else if(fh.getRelaType()==4)
					        {
					            fhChildrens.add(fh.getDisease());
					        }
					    }
					}
					%>
					<td rowspan="4" class="td_title">家族史</td>
					<td class="td_title">父亲</td>
					<td colspan="4" style="line-height: 25px;">
						<span class="optItem"><input name="fhFather" type="checkbox" value="2" <%=fhFathers.contains("2") ? "checked=\"checked\"" : ""%> />高血压</span>
						<span class="optItem"><input name="fhFather" type="checkbox" value="3" <%=fhFathers.contains("3") ? "checked=\"checked\"" : ""%> />糖尿病</span>
						<span class="optItem"><input name="fhFather" type="checkbox" value="4" <%=fhFathers.contains("4") ? "checked=\"checked\"" : ""%> />冠心病</span>
						<span class="optItem"><input name="fhFather" type="checkbox" value="5" <%=fhFathers.contains("5") ? "checked=\"checked\"" : ""%> />慢性阻塞性肺疾病</span>
						<span class="optItem"><input name="fhFather" type="checkbox" value="6" <%=fhFathers.contains("6") ? "checked=\"checked\"" : ""%> />恶性肿瘤</span>
						<span class="optItem"><input name="fhFather" type="checkbox" value="7" <%=fhFathers.contains("7") ? "checked=\"checked\"" : ""%> />脑卒中</span>
						<span class="optItem"><input name="fhFather" type="checkbox" value="8" <%=fhFathers.contains("8") ? "checked=\"checked\"" : ""%> />重性精神疾病</span>
						<br />
						<span class="optItem"><input name="fhFather" type="checkbox" value="9" <%=fhFathers.contains("9") ? "checked=\"checked\"" : ""%> />结核病</span>
						<span class="optItem"><input name="fhFather" type="checkbox" value="10" <%=fhFathers.contains("10") ? "checked=\"checked\"" : ""%> />肝炎</span>
						<span class="optItem"><input name="fhFather" type="checkbox" value="11" <%=fhFathers.contains("11") ? "checked=\"checked\"" : ""%> />先天畸形</span>
						<span class="optItem"><input name="fhFather" type="checkbox" value="12" <%=fhFathers.contains("12") ? "checked=\"checked\"" : ""%> />其他</span>
					</td>
				</tr>
				<tr>
					<td class="td_title">母亲</td>
					<td colspan="4" style="line-height: 25px;">
						<span class="optItem"><input name="fhMother" type="checkbox" value="2" <%=fhMotners.contains("2") ? "checked=\"checked\"" : ""%> />高血压</span>
						<span class="optItem"><input name="fhMother" type="checkbox" value="3" <%=fhMotners.contains("3") ? "checked=\"checked\"" : ""%> />糖尿病</span>
						<span class="optItem"><input name="fhMother" type="checkbox" value="4" <%=fhMotners.contains("4") ? "checked=\"checked\"" : ""%> />冠心病</span>
						<span class="optItem"><input name="fhMother" type="checkbox" value="5" <%=fhMotners.contains("5") ? "checked=\"checked\"" : ""%> />慢性阻塞性肺疾病</span>
						<span class="optItem"><input name="fhMother" type="checkbox" value="6" <%=fhMotners.contains("6") ? "checked=\"checked\"" : ""%> />恶性肿瘤</span>
						<span class="optItem"><input name="fhMother" type="checkbox" value="7" <%=fhMotners.contains("7") ? "checked=\"checked\"" : ""%> />脑卒中</span>
						<span class="optItem"><input name="fhMother" type="checkbox" value="8" <%=fhMotners.contains("8") ? "checked=\"checked\"" : ""%> />重性精神疾病</span>
						<br />
						<span class="optItem"><input name="fhMother" type="checkbox" value="9" <%=fhMotners.contains("9") ? "checked=\"checked\"" : ""%> />结核病</span>
						<span class="optItem"><input name="fhMother" type="checkbox" value="10" <%=fhMotners.contains("10") ? "checked=\"checked\"" : ""%> />肝炎</span>
						<span class="optItem"><input name="fhMother" type="checkbox" value="11" <%=fhMotners.contains("11") ? "checked=\"checked\"" : ""%> />先天畸形</span>
						<span class="optItem"><input name="fhMother" type="checkbox" value="12" <%=fhMotners.contains("12") ? "checked=\"checked\"" : ""%> />其他</span>
					</td>
				</tr>
				<tr>
					<td class="td_title">兄弟姐妹</td>
					<td colspan="4" style="line-height: 25px;">
						<span class="optItem"><input name="fhBrother" type="checkbox" value="2" <%=fhBrothers.contains("2") ? "checked=\"checked\"" : ""%> />高血压</span>
						<span class="optItem"><input name="fhBrother" type="checkbox" value="3" <%=fhBrothers.contains("3") ? "checked=\"checked\"" : ""%> />糖尿病</span>
						<span class="optItem"><input name="fhBrother" type="checkbox" value="4" <%=fhBrothers.contains("4") ? "checked=\"checked\"" : ""%> />冠心病</span>
						<span class="optItem"><input name="fhBrother" type="checkbox" value="5" <%=fhBrothers.contains("5") ? "checked=\"checked\"" : ""%> />慢性阻塞性肺疾病</span>
						<span class="optItem"><input name="fhBrother" type="checkbox" value="6" <%=fhBrothers.contains("6") ? "checked=\"checked\"" : ""%> />恶性肿瘤</span>
						<span class="optItem"><input name="fhBrother" type="checkbox" value="7" <%=fhBrothers.contains("7") ? "checked=\"checked\"" : ""%> />脑卒中</span>
						<span class="optItem"><input name="fhBrother" type="checkbox" value="8" <%=fhBrothers.contains("8") ? "checked=\"checked\"" : ""%> />重性精神疾病</span>
						<br />
						<span class="optItem"><input name="fhBrother" type="checkbox" value="9" <%=fhBrothers.contains("9") ? "checked=\"checked\"" : ""%> />结核病</span>
						<span class="optItem"><input name="fhBrother" type="checkbox" value="10" <%=fhBrothers.contains("10") ? "checked=\"checked\"" : ""%> />肝炎</span>
						<span class="optItem"><input name="fhBrother" type="checkbox" value="11" <%=fhBrothers.contains("11") ? "checked=\"checked\"" : ""%> />先天畸形</span>
						<span class="optItem"><input name="fhBrother" type="checkbox" value="12" <%=fhBrothers.contains("12") ? "checked=\"checked\"" : ""%> />其他</span>
					</td>
				</tr>
				<tr>
					<td class="td_title">子女</td>
					<td colspan="4" style="line-height: 25px;">
						<span class="optItem"><input name="fhChildren" type="checkbox" value="2" <%=fhChildrens.contains("2") ? "checked=\"checked\"" : ""%> />高血压</span>
						<span class="optItem"><input name="fhChildren" type="checkbox" value="3" <%=fhChildrens.contains("3") ? "checked=\"checked\"" : ""%> />糖尿病</span>
						<span class="optItem"><input name="fhChildren" type="checkbox" value="4" <%=fhChildrens.contains("4") ? "checked=\"checked\"" : ""%> />冠心病</span>
						<span class="optItem"><input name="fhChildren" type="checkbox" value="5" <%=fhChildrens.contains("5") ? "checked=\"checked\"" : ""%> />慢性阻塞性肺疾病</span>
						<span class="optItem"><input name="fhChildren" type="checkbox" value="6" <%=fhChildrens.contains("6") ? "checked=\"checked\"" : ""%> />恶性肿瘤</span>
						<span class="optItem"><input name="fhChildren" type="checkbox" value="7" <%=fhChildrens.contains("7") ? "checked=\"checked\"" : ""%> />脑卒中</span>
						<span class="optItem"><input name="fhChildren" type="checkbox" value="8" <%=fhChildrens.contains("8") ? "checked=\"checked\"" : ""%> />重性精神疾病</span>
						<br />
						<span class="optItem"><input name="fhChildren" type="checkbox" value="9" <%=fhChildrens.contains("9") ? "checked=\"checked\"" : ""%> />结核病</span>
						<span class="optItem"><input name="fhChildren" type="checkbox" value="10" <%=fhChildrens.contains("10") ? "checked=\"checked\"" : ""%> />肝炎</span>
						<span class="optItem"><input name="fhChildren" type="checkbox" value="11" <%=fhChildrens.contains("11") ? "checked=\"checked\"" : ""%> />先天畸形</span>
						<span class="optItem"><input name="fhChildren" type="checkbox" value="12" <%=fhChildrens.contains("12") ? "checked=\"checked\"" : ""%> />其他</span>
					</td>
				</tr>
				<tr>
					<td class="td_title" colspan="2">遗传病史:</td>
					<td colspan="4" >
						<input type="text" maxlength="50" id="geneticHistory" name="archive.geneticHistory" value="<%=StringUtil.trim(archive.getGeneticHistory())%>" />
					</td>
				</tr>
				<tr>
					<td class="td_title" colspan="2">残疾情况:</td>
					<td colspan="4">
						<%
							String disabilityStatus = archive.getDisabilityStatus();
							List<String> disabilityStatuss = StringUtil.splitIgnoreEmpty(disabilityStatus, ";");
						%>
						<span class="optItem"><input type="checkbox" name="disabilityStatus" value="2" <%=disabilityStatuss.contains("2") ? "checked=\"checked\"" : ""%> />视力残疾</span>
						<span class="optItem"><input type="checkbox" name="disabilityStatus" value="3" <%=disabilityStatuss.contains("3") ? "checked=\"checked\"" : ""%> />听力残疾</span>
						<span class="optItem"><input type="checkbox" name="disabilityStatus" value="4" <%=disabilityStatuss.contains("4") ? "checked=\"checked\"" : ""%> />言语残疾</span>
						<span class="optItem"><input type="checkbox" name="disabilityStatus" value="5" <%=disabilityStatuss.contains("5") ? "checked=\"checked\"" : ""%> />肢体残疾</span>
						<span class="optItem"><input type="checkbox" name="disabilityStatus" value="6" <%=disabilityStatuss.contains("6") ? "checked=\"checked\"" : ""%> />智力残疾</span>
						<span class="optItem"><input type="checkbox" name="disabilityStatus" value="7" <%=disabilityStatuss.contains("7") ? "checked=\"checked\"" : ""%> />精神残疾</span>
						<span class="optItem"><input type="checkbox" name="disabilityStatus" value="8" <%=disabilityStatuss.contains("8") ? "checked=\"checked\"" : ""%> />其他残疾</span>
					</td>
				</tr>
				<tr>
					<%
					// 1：厨房排风设施，2：燃料类型，3：饮水，4：厕所，5：禽畜栏
					String [] les = new String[5];
					if(lifeEnvs!=null)
					{
					    for(LifeEnv le : lifeEnvs)
					    {
					        if(le.getEnvType()==1)
					        {
					            les[0] = le.getDetail();
					        }else if(le.getEnvType()==2)
					        {
					            les[1] = le.getDetail();
					        }else if(le.getEnvType()==3)
					        {
					            les[2] = le.getDetail();
					        }else if(le.getEnvType()==4)
					        {
					            les[3] = le.getDetail();
					        }else if(le.getEnvType()==5)
					        {
					            les[4] = le.getDetail();
					        }
					    }
					}
					%>
					<td class="td_title" rowspan="5">生活环境</td>
					<td class="td_title">厨房排风<br />设施:</td>
					<td colspan="4">
						<span class="optItem"><input type="radio" name="lifeEnv0" value="1" <%="1".equals(les[0]) ? "checked=\"checked\"" : ""%> />无</span>
						<span class="optItem"><input type="radio" name="lifeEnv0" value="2" <%="2".equals(les[0]) ? "checked=\"checked\"" : ""%> />油烟机</span>
						<span class="optItem"><input type="radio" name="lifeEnv0" value="3" <%="3".equals(les[0]) ? "checked=\"checked\"" : ""%> />换风扇</span>
						<span class="optItem"><input type="radio" name="lifeEnv0" value="4" <%="4".equals(les[0]) ? "checked=\"checked\"" : ""%> />烟囱</span>
					</td>
				</tr>
				<tr>
					<td class="td_title">燃料类型</td>
					<td colspan="4">
						<span class="optItem"><input type="radio" name="lifeEnv1" value="1" <%="1".equals(les[1]) ? "checked=\"checked\"" : ""%> />液化气</span>
						<span class="optItem"><input type="radio" name="lifeEnv1" value="2" <%="2".equals(les[1]) ? "checked=\"checked\"" : ""%> />煤</span>
						<span class="optItem"><input type="radio" name="lifeEnv1" value="3" <%="3".equals(les[1]) ? "checked=\"checked\"" : ""%> />天然气</span>
						<span class="optItem"><input type="radio" name="lifeEnv1" value="4" <%="4".equals(les[1]) ? "checked=\"checked\"" : ""%> />沼气</span>
						<span class="optItem"><input type="radio" name="lifeEnv1" value="5" <%="5".equals(les[1]) ? "checked=\"checked\"" : ""%> />柴火</span>
						<span class="optItem"><input type="radio" name="lifeEnv1" value="6" <%="6".equals(les[1]) ? "checked=\"checked\"" : ""%> />其他</span>
					</td>
				</tr>
				<tr>
					<td class="td_title">饮水</td>
					<td colspan="4">
						<span class="optItem"><input type="radio" name="lifeEnv2" value="1" <%="1".equals(les[2]) ? "checked=\"checked\"" : ""%> />自来水</span>
						<span class="optItem"><input type="radio" name="lifeEnv2" value="2" <%="2".equals(les[2]) ? "checked=\"checked\"" : ""%> />经净化过滤的水</span>
						<span class="optItem"><input type="radio" name="lifeEnv2" value="3" <%="3".equals(les[2]) ? "checked=\"checked\"" : ""%> />井水</span>
						<span class="optItem"><input type="radio" name="lifeEnv2" value="4" <%="4".equals(les[2]) ? "checked=\"checked\"" : ""%> />河湖水</span>
						<span class="optItem"><input type="radio" name="lifeEnv2" value="5" <%="5".equals(les[2]) ? "checked=\"checked\"" : ""%> />塘水</span>
						<span class="optItem"><input type="radio" name="lifeEnv2" value="6" <%="6".equals(les[2]) ? "checked=\"checked\"" : ""%> />其他</span>
					</td>
				</tr>
				<tr>
					<td class="td_title">厕所</td>
					<td colspan="4">
						<span class="optItem"><input type="radio" name="lifeEnv3" value="1" <%="1".equals(les[3]) ? "checked=\"checked\"" : ""%> />卫生厕所</span>
						<span class="optItem"><input type="radio" name="lifeEnv3" value="2" <%="2".equals(les[3]) ? "checked=\"checked\"" : ""%> />一格或二格粪池式</span>
						<span class="optItem"><input type="radio" name="lifeEnv3" value="3" <%="3".equals(les[3]) ? "checked=\"checked\"" : ""%> />马桶</span>
						<span class="optItem"><input type="radio" name="lifeEnv3" value="4" <%="4".equals(les[3]) ? "checked=\"checked\"" : ""%> />露天粪坑</span>
						<span class="optItem"><input type="radio" name="lifeEnv3" value="5" <%="5".equals(les[3]) ? "checked=\"checked\"" : ""%> />简易棚厕</span>
					</td>
				</tr>
				<tr>
					<td class="td_title">禽畜栏</td>
					<td colspan="4">
						<span class="optItem"><input type="radio" name="lifeEnv4" value="1" <%="1".equals(les[4]) ? "checked=\"checked\"" : ""%> />单设</span>
						<span class="optItem"><input type="radio" name="lifeEnv4" value="2" <%="2".equals(les[4]) ? "checked=\"checked\"" : ""%> />室内</span>
						<span class="optItem"><input type="radio" name="lifeEnv4" value="3" <%="3".equals(les[4]) ? "checked=\"checked\"" : ""%> />室外</span>
					</td>
				</tr>
			</table>
		</div>
	</form>
	</div>
	<div style="text-align: center;padding: 10px;">
		<a class="easyui-linkbutton" href="javascript:saveArchive()">保存</a>
		<%
		if("Y".equals(fq))
		{
		%>
		&nbsp;
		<a class="easyui-linkbutton" href="javascript:closeIt()">关闭</a>
		<%
		}
		%>
	</div>
</body>
</html>