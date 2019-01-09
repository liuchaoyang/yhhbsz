<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.util.HashSet"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
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
// 是否是从查询页面链接进来的
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
		padding: 5px;
	}
	input[type="text"].dateInput{
		 width: 100px;
	}
	</style>
	<script type="text/javascript">
	function closeIt(){
		try{
			var tc = top.$("#tb");
			var tab = tc.tabs("getSelected");
			if (tab) {
				tc.tabs("close", tab.panel("options").title);
			}
		}catch(e){}
	}
	function edit(){
		location.href = "<%=request.getContextPath()%>/ach/ach_toDetail.action?operType=edit&custId=<%=archive.getCustId()%><%="Y".equals(fq)?"&fq=Y":""%>";
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
						<%=StringUtil.trim(user.getName())%>
						<input type="hidden" name="archive.custId" value="<%=archive.getCustId()%>" />
					</td>
					<td class="td_title" width="90">性别:</td>
					<td width="120">
						<%
						int sex = cust.getSex()!=null ? cust.getSex().intValue() : 0;
						String sexStr = "";
						if(sex==1){
							sexStr = "男";
						}else if(sex==2){
							sexStr = "女";
						}
						%>
						<%=sexStr%>
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
						<%=StringUtil.ensureStringNotNull(cust.getAddress())%>
					</td>
				</tr>
				<tr style="height: 20px;">
					<td class="td_title">备注:</td>
					<td colspan="5" style="word-break: break-all;">
						<%=StringUtil.ensureStringNotNull(cust.getMemo())%>
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
						<%=StringUtil.trim(archive.getHouseholdAddress())%>
					</td>
				</tr>
				<tr style="height: 30px;">
					<td class="td_title">乡镇(街道)名称:</td>
					<td colspan="3">
						<%=StringUtil.trim(archive.getStreetName())%>
					</td>
				</tr>
				<tr style="height: 30px;">
					<td class="td_title">村(居)委会名称:</td>
					<td colspan="3">
						<%=StringUtil.trim(archive.getVillageName())%>
					</td>
				</tr>
				<tr style="height: 30px;">
					<td class="td_title" width="20%">建档单位:</td>
					<td width="30%" style="word-break: break-all;">
						<%=StringUtil.trim(archive.getMedicalEstab())%>
					</td>
					<td class="td_title" width="20%">建档单位电话:</td>
					<td>
						<%=StringUtil.trim(archive.getTelMedical())%>
					</td>
				</tr>
				<tr style="height: 30px;">
					<td class="td_title" width="20%">建  档  人:</td>
					<td width="30%">
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
						<%=DateUtil.toHtmlDate(cust.getBirthday())%>
					</td>
					<td class="td_title" width="20%">工作单位:</td>
					<td colspan="2" style="word-break: break-all;">
						<%=StringUtil.trim(archive.getWorkUnit())%>
					</td>
				</tr>
				<tr>
					<td class="td_title" colspan="2">联系人姓名:</td>
					<td>
						<%=StringUtil.trim(archive.getContactName())%>
					</td>
					<td class="td_title">联系人电话:</td>
					<td colspan="2">
						<%=StringUtil.trim(archive.getContactTelphone())%>
					</td>
				</tr>
				<tr>
					<td class="td_title" colspan="2">常住类型:</td>
					<td>
						<%
						int householdType = archive.getHouseholdType()!=null?archive.getHouseholdType().intValue():0;
						String householdTypeStr = "";
						if(householdType==1){
							householdTypeStr = "户籍";
						}else if(householdType==2){
							householdTypeStr = "非户籍";
						}
						%>
						<%=householdTypeStr%>
					</td>
					<td class="td_title">民  族:</td>
					<td colspan="2">
						<%=StringUtil.ensureStringNotNull(cust.getNationalName())%>
					</td>
				</tr>
				<tr>
					<td class="td_title" colspan="2">血    型:</td>
					<td>
						<%
						int bloodType = archive.getBloodType()!=null ? archive.getBloodType().intValue() : 0;
						String bloodTypeStr = "";
						if(bloodType==1){
							bloodTypeStr = "A";
						}else if(bloodType==2){
							bloodTypeStr = "B";
						}else if(bloodType==3){
							bloodTypeStr = "O";
						}else if(bloodType==4){
							bloodTypeStr = "AB";
						}else if(bloodType==5){
							bloodTypeStr = "不详";
						}
						%>
						<%=bloodTypeStr%>
					</td>
					<td class="td_title">RH阴性:</td>
					<td colspan="2">
						<%
						int rhNagative = archive.getRhNagative()!=null ? archive.getRhNagative().intValue() : 0;
						String rhNagativeStr = "";
						if(rhNagative==1){
							rhNagativeStr = "是";
						}else if(rhNagative==2){
							rhNagativeStr = "否";
						}else if(rhNagative==3){
							rhNagativeStr = "不详";
						}
						%>
						<%=rhNagativeStr%>
					</td>
				</tr>
				<tr>
					<td class="td_title" colspan="2">文化程度:</td>
					<td colspan="4">
						<%
						int degree = archive.getDegree()!=null ? archive.getDegree().intValue() : 0;
						String degreeStr = "";
						if(degree==1){
							degreeStr = "文盲及半文盲";
						}else if(degree==2){
							degreeStr = "小学";
						}else if(degree==3){
							degreeStr = "初中";
						}else if(degree==4){
							degreeStr = "高中/技校/中专";
						}else if(degree==5){
							degreeStr = "大学专科及以上";
						}else if(degree==6){
							degreeStr = "不详";
						}
						%>
						<%=degreeStr%>
					</td>
				</tr>
				 <tr>
					<td align="left" class="td_title" colspan="2">职    业:</td>
					<td colspan="4">
						<%
						String profession = cust.getProfession();
						String professionStr = "";
						if("1".equals(profession)){
							professionStr = "国家机关、党群组织、企业、事业单位负责人";
						}else if("2".equals(profession)){
							professionStr = "专业技术人员";
						}else if("3".equals(profession)){
							professionStr = "办事人员和有关人员";
						}else if("4".equals(profession)){
							professionStr = "商业、服务业人员";
						}else if("5".equals(profession)){
							professionStr = "农、林、牧、渔、水利业生产人员";
						}else if("6".equals(profession)){
							professionStr = "生产、运输设备操作人员及有关人员";
						}else if("7".equals(profession)){
							professionStr = "军人";
						}else if("8".equals(profession)){
							professionStr = "不便分类的其他从业人员";
						}
						%>
						<%=professionStr%>
					</td>
				</tr>
				<tr>
					<td class="td_title" colspan="2">婚姻状况:</td>
					<td colspan="4">
						<%
							int maritalStatus = archive.getMaritalStatus()!=null?archive.getMaritalStatus().intValue():0;
							String maritalStatusStr = "";
							if(maritalStatus==1){
								maritalStatusStr = "未婚";
							}else if(maritalStatus==2){
								maritalStatusStr = "已婚";
							}else if(maritalStatus==3){
								maritalStatusStr = "丧偶";
							}else if(maritalStatus==4){
								maritalStatusStr = "离婚";
							}else if(maritalStatus==5){
								maritalStatusStr = "未说明婚姻状况";
							}
							%>
							<%=maritalStatusStr%>
					</td>
				</tr>
				<tr>
					<td class="td_title" colspan="2">医疗费用支付方式:</td>
					<td colspan="4" style="line-height: 25px;">
						<%
							String payType = archive.getPayType();
							List<String> payTypes = StringUtil.splitIgnoreEmpty(payType, ";");
							StringBuilder ptStr = new StringBuilder();
							int ptC = 0;
							if(payTypes.contains("1")){
								ptStr.append(ptC++>0?"；":"").append("城镇职工基本医疗保险");
							}
							if(payTypes.contains("2")){
								ptStr.append(ptC++>0?"；":"").append("城镇居民基本医疗保险");
							}
							if(payTypes.contains("3")){
								ptStr.append(ptC++>0?"；":"").append("新型农村合作医疗");
							}
							if(payTypes.contains("4")){
								ptStr.append(ptC++>0?"；":"").append("贫困救助");
							}
							if(payTypes.contains("5")){
								ptStr.append(ptC++>0?"；":"").append("商业医疗保险");
							}
							if(payTypes.contains("6")){
								ptStr.append(ptC++>0?"；":"").append("全公费");
							}
							if(payTypes.contains("7")){
								ptStr.append(ptC++>0?"；":"").append("全自费");
							}
							if(payTypes.contains("8")){
								ptStr.append(ptC++>0?"；":"").append("其他");
							}
						%>
						<%=ptStr%>
					</td>
				</tr>
				<tr>
					<td class="td_title" colspan="2">药物过敏史:</td>
					  <td colspan="4">
						<%
							String hoda = archive.getHoda();
							List<String> hodas = StringUtil.splitIgnoreEmpty(hoda, ";");
							StringBuilder hodaStr = new StringBuilder();
							int hodaC = 0;
							if(hodas.contains("2")){
								hodaStr.append(hodaC++>0?"；":"").append("青霉素");
							}
							if(hodas.contains("3")){
								hodaStr.append(hodaC++>0?"；":"").append("磺胺");
							}
							if(hodas.contains("4")){
								hodaStr.append(hodaC++>0?"；":"").append("链霉素");
							}
							if(hodas.contains("5")){
								hodaStr.append(hodaC++>0?"；":"").append("其他");
							}
						%>
						<%=hodaStr%>
					</td>
				</tr>
				<tr>
					<td class="td_title" colspan="2">暴 露 史:</td>
					<td colspan="4">
						<%
							String exposureHistory = archive.getExposureHistory();
							List<String> exposureHistorys = StringUtil.splitIgnoreEmpty(exposureHistory, ";");
							StringBuilder ehStr = new StringBuilder();
							int ehC = 0;
							if(exposureHistorys.contains("2")){
								ehStr.append(ehC++>0?"；":"").append("化学品");
							}
							if(exposureHistorys.contains("3")){
								ehStr.append(ehC++>0?"；":"").append("毒物");
							}
							if(exposureHistorys.contains("4")){
								ehStr.append(ehC++>0?"；":"").append("射线");
							}
						%>
						<%=ehStr%>
					</td>
				</tr>
				<tr>
					<td rowspan="4" align="left" class="td_title" width="10%">既往史:</td>
					<td class="td_title">疾病</td>
					<td colspan="4">
						<div id="jbDiv">
						<%
						if(previousHistorys!=null)
						{
							Map<String, String> disMap = new HashMap<String, String>();
							disMap.put("1", "无");
							disMap.put("2", "高血压");
							disMap.put("3", "糖尿病");
							disMap.put("4", "冠心病");
							disMap.put("5", "慢性阻塞性肺疾病");
							disMap.put("6", "恶性肿瘤");
							disMap.put("7", "脑卒中");
							disMap.put("8", "重性精神疾病");
							disMap.put("9", "结核病");
							disMap.put("10", "肝炎");
							disMap.put("11", "其他法定传染病");
							disMap.put("12", "职业病");
							for(PreviousHistory previousHistory : previousHistorys)
						 	{
								if(previousHistory.getType()==1)
								{
						%>
							<div class='phItem'>
								疾病名称:&nbsp;<%=StringUtil.ensureStringNotNull(disMap.get(previousHistory.getName()))%>
								&nbsp;&nbsp;&nbsp;
								确诊时间:&nbsp;<%=DateUtil.toHtmlDate(previousHistory.getPastTime())%>
							</div>
						<%
								}
							}
						}
						%>
						</div>
					</td>
				</tr>
				<tr>
					<td class="td_title">手术</td>
					<td colspan="4">
						<div id="ssDiv">
						<%
						if(previousHistorys!=null)
						{
							for(PreviousHistory previousHistory : previousHistorys)
						 	{
								if(previousHistory.getType()==2)
								{
						%>
							<div class='phItem'>
								手术名称:&nbsp;<%=StringUtil.ensureStringNotNull(previousHistory.getName())%>
								&nbsp;&nbsp;&nbsp;
								手术时间:&nbsp;<%=DateUtil.toHtmlDate(previousHistory.getPastTime())%>
							</div>
						<%
								}
							}
						}
						%>
						</div>
					</td>
				</tr>
				<tr>
					<td class="td_title">外伤</td>
					<td colspan="4">
						<div id="wsDiv">
						<%
						if(previousHistorys!=null)
						{
							for(PreviousHistory previousHistory : previousHistorys)
						 	{
								if(previousHistory.getType()==3)
								{
						%>
							<div class='phItem'>
								外伤名称:&nbsp;<%=StringUtil.ensureStringNotNull(previousHistory.getName())%>
								&nbsp;&nbsp;&nbsp;
								外伤时间:&nbsp;<%=DateUtil.toHtmlDate(previousHistory.getPastTime())%>
							</div>
						<%
								}
							}
						}
						%>
						</div>
					</td>
				</tr>
				<tr>
					<td class="td_title">输血</td>
					<td colspan="4">
						<div id="sxDiv">
						<%
						if(previousHistorys!=null)
						{
							for(PreviousHistory previousHistory : previousHistorys)
						 	{
								if(previousHistory.getType()==4)
								{
						%>
							<div class='phItem'>
								输血原因:&nbsp;<%=StringUtil.ensureStringNotNull(previousHistory.getName())%>
								&nbsp;&nbsp;&nbsp;
								输血时间:&nbsp;<%=DateUtil.toHtmlDate(previousHistory.getPastTime())%>
							</div>
						<%
								}
							}
						}
						%>
						</div>
					</td>
				</tr>
				<tr>
					<%
					Map<String, String> fpMap = new HashMap<String, String>();
					fpMap.put("2", "高血压");
					fpMap.put("3", "糖尿病");
					fpMap.put("4", "冠心病");
					fpMap.put("5", "慢性阻塞性肺疾病");
					fpMap.put("6", "恶性肿瘤");
					fpMap.put("7", "脑卒中");
					fpMap.put("8", "重性精神疾病");
					fpMap.put("9", "结核病");
					fpMap.put("10", "肝炎");
					fpMap.put("11", "先天畸形");
					fpMap.put("12", "其他");
					StringBuilder fhFathers = new StringBuilder();
					StringBuilder fhMotners = new StringBuilder();
					StringBuilder fhBrothers = new StringBuilder();
					StringBuilder fhChildrens = new StringBuilder();
					int fhFatherC = 0;
					int fhMotnerC = 0;
					int fhBrotherC = 0;
					int fhChildrenC = 0;
					if(familyHistorys!=null){
					    for(FamilyHistory fh : familyHistorys)
					    {
							if(fh.getRelaType()==1)
							{
								fhFathers.append(fhFatherC++>0?"；":"").append(fpMap.get(fh.getDisease()));
							}else if(fh.getRelaType()==2)
							{
								fhMotners.append(fhMotnerC++>0?"；":"").append(fpMap.get(fh.getDisease()));
							}else if(fh.getRelaType()==3)
							{
								fhBrothers.append(fhBrotherC++>0?"；":"").append(fpMap.get(fh.getDisease()));
							}else if(fh.getRelaType()==4)
							{
								fhChildrens.append(fhChildrenC++>0?"；":"").append(fpMap.get(fh.getDisease()));
							}
						}
					}
					%>
					<td rowspan="4" class="td_title">家族史</td>
					<td class="td_title">父亲</td>
					<td colspan="4" style="line-height: 25px;">
						<%=fhFathers%>
					</td>
				</tr>
				<tr>
					<td class="td_title">母亲</td>
					<td colspan="4" style="line-height: 25px;">
						<%=fhMotners%>
					</td>
				</tr>
				<tr>
					<td class="td_title">兄弟姐妹</td>
					<td colspan="4" style="line-height: 25px;">
						<%=fhBrothers%>
					</td>
				</tr>
				<tr>
					<td class="td_title">子女</td>
					<td colspan="4" style="line-height: 25px;">
						<%=fhChildrens%>
					</td>
				</tr>
				<tr>
					<td class="td_title" colspan="2">遗传病史:</td>
					<td colspan="4" >
						<%=StringUtil.trim(archive.getGeneticHistory())%>
					</td>
				</tr>
				<tr>
					<td class="td_title" colspan="2">残疾情况:</td>
					<td colspan="4">
						<%
							String disabilityStatus = archive.getDisabilityStatus();
							List<String> disabilityStatuss = StringUtil.splitIgnoreEmpty(disabilityStatus, ";");
							StringBuilder disStr = new StringBuilder();
							int disC = 0;
							if(disabilityStatuss.contains("2")){
								disStr.append(disC++>0?"；":"").append("视力残疾");
							}
							if(disabilityStatuss.contains("3")){
								disStr.append(disC++>0?"；":"").append("听力残疾");
							}
							if(disabilityStatuss.contains("4")){
								disStr.append(disC++>0?"；":"").append("言语残疾");
							}
							if(disabilityStatuss.contains("5")){
								disStr.append(disC++>0?"；":"").append("肢体残疾");
							}
							if(disabilityStatuss.contains("6")){
								disStr.append(disC++>0?"；":"").append("智力残疾");
							}
							if(disabilityStatuss.contains("7")){
								disStr.append(disC++>0?"；":"").append("精神残疾");
							}
							if(disabilityStatuss.contains("8")){
								disStr.append(disC++>0?"；":"").append("其他残疾");
							}
						%>
						<%=disStr.toString()%>
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
						<%
						String le0Str = "";
						if("1".equals(les[0]))
						{
						    le0Str = "无";
						}else if("2".equals(les[0]))
						{
						    le0Str = "油烟机";
						}else if("3".equals(les[0]))
						{
						    le0Str = "换风扇";
						}else if("4".equals(les[0]))
						{
						    le0Str = "烟囱";
						}
						%>
						<%=le0Str%>
					</td>
				</tr>
				<tr>
					<td class="td_title">燃料类型</td>
					<td colspan="4">
						<%
						String le1Str = "";
						if("1".equals(les[1]))
						{
						    le1Str = "液化气";
						}else if("2".equals(les[1]))
						{
						    le1Str = "煤";
						}else if("3".equals(les[1]))
						{
						    le1Str = "天然气";
						}else if("4".equals(les[1]))
						{
						    le1Str = "沼气";
						}else if("5".equals(les[1]))
						{
						    le1Str = "柴火";
						}else if("6".equals(les[1]))
						{
						    le1Str = "其他";
						}
						%>
						<%=le1Str%>
					</td>
				</tr>
				<tr>
					<td class="td_title">饮水</td>
					<td colspan="4">
						<%
						String le2Str = "";
						if("1".equals(les[2]))
						{
						    le2Str = "自来水";
						}else if("2".equals(les[2]))
						{
						    le2Str = "经净化过滤的水";
						}else if("3".equals(les[2]))
						{
						    le2Str = "井水";
						}else if("4".equals(les[2]))
						{
						    le2Str = "河湖水";
						}else if("5".equals(les[2]))
						{
						    le2Str = "塘水";
						}else if("6".equals(les[2]))
						{
						    le2Str = "其他";
						}
						%>
						<%=le2Str%>
					</td>
				</tr>
				<tr>
					<td class="td_title">厕所</td>
					<td colspan="4">
						<%
						String le3Str = "";
						if("1".equals(les[3]))
						{
						    le3Str = "卫生厕所";
						}else if("2".equals(les[3]))
						{
						    le3Str = "一格或二格粪池式";
						}else if("3".equals(les[3]))
						{
						    le3Str = "马桶";
						}else if("4".equals(les[3]))
						{
						    le3Str = "露天粪坑";
						}else if("5".equals(les[3]))
						{
						    le3Str = "简易棚厕";
						}
						%>
						<%=le3Str%>
					</td>
				</tr>
				<tr>
					<td class="td_title">禽畜栏</td>
					<td colspan="4">
						<%
						String le4Str = "";
						if("1".equals(les[4]))
						{
							le4Str = "单设";
						}else if("2".equals(les[4]))
						{
							le4Str = "室内";
						}else if("3".equals(les[4]))
						{
							le4Str = "室外";
						}
						%>
						<%=le4Str%>
					</td>
				</tr>
			</table>
		</div>
	</form>
	</div>
	<div style="text-align: center;padding: 10px;">
		<%
		if("Y".equals(request.getAttribute("editable")))
		{
		%>
		<a class="easyui-linkbutton" href="javascript:edit()">编辑</a>
		<%
		}
		if("Y".equals(fq))
		{
		%>
		<a class="easyui-linkbutton" href="javascript:closeIt()">关闭</a>
		<%
		}
		%>
	</div>
</body>
</html>