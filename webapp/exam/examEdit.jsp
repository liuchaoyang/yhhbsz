<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.util.DecimalUtil"%>
<%@ page import="com.yzxt.yh.util.DateUtil"%>
<%@ page import="com.yzxt.yh.module.sys.bean.Customer"%>
<%@ page import="com.yzxt.yh.module.chk.bean.Exam"%>
<%@ page import="com.yzxt.yh.module.chk.bean.ExamHospitalHis"%>
<%@ page import="com.yzxt.yh.module.chk.bean.ExamFamilyHosHis"%>
<%@ page import="com.yzxt.yh.module.chk.bean.ExamMedic"%>
<%@ page import="com.yzxt.yh.module.chk.bean.ExamInoculate"%>
<%@ page import="com.yzxt.yh.module.chk.bean.ExamPosion"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
	Exam exam = (Exam)request.getAttribute("exam");
	Customer customer = (Customer)request.getAttribute("cust");
	String operType = (String)request.getAttribute("operType");
	boolean womanEnable = customer.getSex()==null||customer.getSex().intValue()==2;
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>体检表</title>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
	<%@ include file="./examCss.jsp"%>
	<%@ include file="./examJs.jsp"%>
</head>
<body>
	<form id="fm" style="width: 700px;margin: 2px auto;padding: 0px;" method="post" accept-charset="UTF-8">
		<div class="pageTitleWrap">
			<strong>健康体检表</strong>
		</div>
		<div class="sectTitleWrap">
			<div class="sectTitle">基本信息</div>
		</div>
		<div class="contentPanel">
			<table class="table">
				<tr>
					<td class="td_title" width="20%">姓名:</td>
					<td width="30%">
						<%=exam.getName()%>
						<input type="hidden" id="examId" name="exam.id" value="<%=StringUtil.ensureStringNotNull(exam.getId())%>" />
						<input type="hidden" id="custId" name="exam.custId" value="<%=StringUtil.ensureStringNotNull(exam.getCustId())%>" />
						<input type="hidden" id="residentName" name="exam.name" value="<%=exam.getName()%>" />
						<input type="hidden" id="examNo" name="exam.examNo" value="<%=StringUtil.ensureStringNotNull(exam.getExamNo())%>" />
					</td>
					<td class="td_title" width="20%">编号:</td>
					<td><%=!"add".equals(operType)? exam.getExamNo():"保存后自动生成"%></td>
				</tr>
				<tr>
					<td class="td_title">体检日期:</td>
					<td>
						<input class="Wdate" maxlength="10" style="width: 90px;" id="examDate" name="exam.examDate" value="<%=DateUtil.toHtmlDate(exam.getExamDate())%>" readonly="readonly" onclick="WdatePicker({})" />
						<span class="must">*</span>
					</td>
					<td class="td_title">体检机构:</td>
					<td>
						<input type="text" style="width: 180px;" maxlength="50" id="industryName" name="exam.industryName" value="<%=StringUtil.ensureStringNotNull(exam.getIndustryName())%>" />
					</td>
				</tr>
				<tr>
			</table>
		</div>
		<div class="sectSplit"></div>
		<div class="sectTitleWrap">
			<div class="sectTitle">症状</div>
		</div>
		<%
		List<String> symptoms = StringUtil.splitIgnoreEmpty(exam.getSymptom(), ";");
		%>
		<div class="contentPanel">
			<table class="table" id="symptomTable">
				<tr>
					<td width="16.6%">
						<input type="checkbox" id="symptomItem2" name="symptomItem" value="2" <%=symptoms.contains("2")?"checked=\"checked\"":""%> />
						<label for="symptomItem2">头痛</label>
					</td>
					<td width="16.6%">
						<input type="checkbox" id="symptomItem3" name="symptomItem" value="3" <%=symptoms.contains("3")?"checked=\"checked\"":""%> />
						<label for="symptomItem3">头晕</label>
					</td>
					<td width="16.6%">
						<input type="checkbox" id="symptomItem4" name="symptomItem" value="4" <%=symptoms.contains("4")?"checked=\"checked\"":""%> />
						<label for="symptomItem4">心悸</label>
					</td>
					<td width="16.6%">
						<input type="checkbox" id="symptomItem5" name="symptomItem" value="5" <%=symptoms.contains("5")?"checked=\"checked\"":""%> />
						<label for="symptomItem5">胸闷</label>
					</td>
					<td width="16.6%">
						<input type="checkbox" id="symptomItem6" name="symptomItem" value="6" <%=symptoms.contains("6")?"checked=\"checked\"":""%> />
						<label for="symptomItem6">胸痛</label>
					</td>
					<td>
						<input type="checkbox" id="symptomItem7" name="symptomItem" value="7" <%=symptoms.contains("7")?"checked=\"checked\"":""%> />
						<label for="symptomItem7">慢性咳嗽</label>
					</td>
				</tr>
				<tr>
					<td>
						<input type="checkbox" id="symptomItem8" name="symptomItem" value="8" <%=symptoms.contains("8")?"checked=\"checked\"":""%> />
						<label for="symptomItem8">咳痰</label>
					</td>
					<td>
						<input type="checkbox" id="symptomItem9" name="symptomItem" value="9" <%=symptoms.contains("9")?"checked=\"checked\"":""%> />
						<label for="symptomItem9">呼吸困难</label>
					</td>
					<td>
						<input type="checkbox" id="symptomItem10" name="symptomItem" value="10" <%=symptoms.contains("10")?"checked=\"checked\"":""%> />
						<label for="symptomItem10">多饮</label>
					</td>
					<td>
						<input type="checkbox" id="symptomItem11" name="symptomItem" value="11" <%=symptoms.contains("11")?"checked=\"checked\"":""%> />
						<label for="symptomItem11">多尿</label>
					</td>
					<td>
						<input type="checkbox" id="symptomItem12" name="symptomItem" value="12" <%=symptoms.contains("12")?"checked=\"checked\"":""%> />
						<label for="symptomItem12">体重下降</label>
					</td>
					<td>
						<input type="checkbox" id="symptomItem13" name="symptomItem" value="13" <%=symptoms.contains("13")?"checked=\"checked\"":""%> />
						<label for="symptomItem13">乏力</label>
					</td>
				</tr>
				<tr>
					<td>
						<input type="checkbox" id="symptomItem14" name="symptomItem" value="14" <%=symptoms.contains("14")?"checked=\"checked\"":""%> />
						<label for="symptomItem14">关节肿痛</label>
					</td>
					<td>
						<input type="checkbox" id="symptomItem15" name="symptomItem" value="15" <%=symptoms.contains("15")?"checked=\"checked\"":""%> />
						<label for="symptomItem15">视力模糊</label>
					</td>
					<td>
						<input type="checkbox" id="symptomItem16" name="symptomItem" value="16" <%=symptoms.contains("16")?"checked=\"checked\"":""%> />
						<label for="symptomItem16">手脚麻木</label>
					</td>
					<td>
						<input type="checkbox" id="symptomItem17" name="symptomItem" value="17" <%=symptoms.contains("17")?"checked=\"checked\"":""%> />
						<label for="symptomItem17">尿急</label>
					</td>
					<td>
						<input type="checkbox" id="symptomItem18" name="symptomItem" value="18" <%=symptoms.contains("18")?"checked=\"checked\"":""%> />
						<label for="symptomItem18">尿痛</label>
					</td>
					<td>
						<input type="checkbox" id="symptomItem19" name="symptomItem" value="19" <%=symptoms.contains("19")?"checked=\"checked\"":""%> />
						<label for="symptomItem19">便秘</label>
					</td>
				</tr>
				<tr>
					<td>
						<input type="checkbox" id="symptomItem20" name="symptomItem" value="20" <%=symptoms.contains("20")?"checked=\"checked\"":""%> />
						<label for="symptomItem20">腹泻</label>
					</td>
					<td>
						<input type="checkbox" id="symptomItem21" name="symptomItem" value="21" <%=symptoms.contains("21")?"checked=\"checked\"":""%> />
						<label for="symptomItem21">恶心呕吐</label>
					</td>
					<td>
						<input type="checkbox" id="symptomItem22" name="symptomItem" value="22" <%=symptoms.contains("22")?"checked=\"checked\"":""%> />
						<label for="symptomItem22">眼花</label>
					</td>
					<td>
						<input type="checkbox" id="symptomItem23" name="symptomItem" value="23" <%=symptoms.contains("23")?"checked=\"checked\"":""%> />
						<label for="symptomItem23">耳鸣</label>
					</td>
					<td>
						<input type="checkbox" id="symptomItem24" name="symptomItem" value="24" <%=symptoms.contains("24")?"checked=\"checked\"":""%> />
						<label for="symptomItem24">乳房胀痛</label>
					</td>
					<td>
						其它:<input type="text" maxlength="50" style="width: 80px;" id="otherSymptom" name="exam.otherSymptom" value="<%=StringUtil.ensureStringNotNull(exam.getOtherSymptom())%>" />
						<%-- 其它ID为25 --%>
					</td>
				</tr>
			</table>
		</div>
		<div class="sectSplit"></div>
		<div class="sectTitleWrap">
			<div class="sectTitle">一般状况</div>
		</div>
		<div class="contentPanel">
			<table class="table">
				<tr>
					<td class="td_title" width="20%">体温:</td>
					<td width="30%">
						<input type="text" maxlength="4" style="width: 100px;" name="exam.temperature" value="<%=exam.getTemperature()!=null?exam.getTemperature():""%>" />℃
					</td>
					<td class="td_title" width="20%">心率:</td>
					<td>
						<input type="text" maxlength="3" style="width: 100px;" name="exam.pulseRate" value="<%=DecimalUtil.toIntegerString(exam.getPulseRate())%>" />次/分钟
					</td>
				</tr>
				<tr>
					<td class="td_title">呼吸频率:</td>
					<td>
						<input type="text" maxlength="3" style="width: 100px;" name="exam.breatheRate" value="<%=DecimalUtil.toIntegerString(exam.getBreatheRate())%>" />次/分钟
					</td>
					<td class="td_title">身高:</td>
					<td>
						<input type="text" maxlength="3" style="width: 100px;" name="exam.height" value="<%=DecimalUtil.toIntegerString(exam.getHeight())%>" />cm
					</td>
				</tr>
				<tr>
					<td class="td_title">体重:</td>
					<td>
						<input type="text" maxlength="5" style="width: 100px;" name="exam.weight" value="<%=exam.getWeight()!=null?exam.getWeight():""%>" />kg
					</td>
					<td class="td_title">腰围:</td>
					<td>
						<input type="text" maxlength="3" style="width: 100px;" name="exam.waistline" value="<%=DecimalUtil.toIntegerString(exam.getWaistline())%>" />cm
					</td>
				</tr>
				<%--
				<tr style="display: none;">
					<td class="td_title">左侧收缩血压:</td>
					<td>
						<input type="text" maxlength="3" style="width: 100px;" name="exam.lBloodPressure" value="<%=DecimalUtil.toIntegerString(exam.getLBloodPressure())%>" />mmHg
					</td>
					<td class="td_title">左侧舒张血压:</td>
					<td>
						<input type="text" maxlength="3" style="width: 100px;" name="exam.lBloodPressureBpd" value="<%=DecimalUtil.toIntegerString(exam.getLBloodPressureBpd())%>" />mmHg
					</td>
				</tr>
				--%>
				<tr>
					<td class="td_title">收缩血压:</td>
					<td>
						<input type="text" maxlength="3" style="width: 100px;" name="exam.rBloodPressure" value="<%=DecimalUtil.toIntegerString(exam.getRBloodPressure())%>" />mmHg
					</td>
					<td class="td_title">舒张血压:</td>
					<td>
						<input type="text" maxlength="3" style="width: 100px;" name="exam.rBloodPressureBpd" value="<%=DecimalUtil.toIntegerString(exam.getRBloodPressureBpd())%>" />mmHg
					</td>
				</tr>
				<tr>
					<td class="td_title">老年人健康状态<br />自我评估:</td>
					<td colspan="3">
						<%
						int agedHealthAssess = exam.getAgedHealthAssess()!=null?exam.getAgedHealthAssess().intValue():0;
						%>
						<input type="radio" id="agedHealthAssess1" name="exam.agedHealthAssess" value="1" <%=(agedHealthAssess==1)?"checked=\"checked\"":""%> /><label for="agedHealthAssess1">满意</label>
						&nbsp;
						<input type="radio" id="agedHealthAssess2" name="exam.agedHealthAssess" value="2" <%=(agedHealthAssess==2)?"checked=\"checked\"":""%> /><label for="agedHealthAssess2">基本满意</label>
						&nbsp;
						<input type="radio" id="agedHealthAssess3" name="exam.agedHealthAssess" value="3" <%=(agedHealthAssess==3)?"checked=\"checked\"":""%> /><label for="agedHealthAssess3">说不清楚</label>
						&nbsp;
						<input type="radio" id="agedHealthAssess4" name="exam.agedHealthAssess" value="4" <%=(agedHealthAssess==4)?"checked=\"checked\"":""%> /><label for="agedHealthAssess4">不太满意</label>
						&nbsp;
						<input type="radio" id="agedHealthAssess5" name="exam.agedHealthAssess" value="5" <%=(agedHealthAssess==5)?"checked=\"checked\"":""%> /><label for="agedHealthAssess5">不满意</label>
					</td>
				</tr>
				<tr>
					<td class="td_title">老年人生活自理<br />能力自我评估:</td>
					<td colspan="3">
						<%
						int agedLifeAssess = exam.getAgedLifeAssess()!=null?exam.getAgedLifeAssess().intValue():0;
						%>
						<input type="radio" id="agedLifeAssess1" name="exam.agedLifeAssess" value="1" <%=(agedLifeAssess==1)?"checked=\"checked\"":""%> /><label for="agedLifeAssess1">可自理（0～3分）</label>
						&nbsp;
						<input type="radio" id="agedLifeAssess2" name="exam.agedLifeAssess" value="2" <%=(agedLifeAssess==2)?"checked=\"checked\"":""%> /><label for="agedLifeAssess2">轻度依赖（4～8分）</label>
						&nbsp;
						<input type="radio" id="agedLifeAssess3" name="exam.agedLifeAssess" value="3" <%=(agedLifeAssess==3)?"checked=\"checked\"":""%> /><label for="agedLifeAssess3">中度依赖（9～18分)</label>
						<br />
						<input type="radio" id="agedLifeAssess4" name="exam.agedLifeAssess" value="4" <%=(agedLifeAssess==4)?"checked=\"checked\"":""%> /><label for="agedLifeAssess4">能自理（≥19分）</label>
					</td>
				</tr>
				<tr>
					<td class="td_title">老年人认知功能:</td>
					<td colspan="3">
						<%
						int agedKonwledgeFunc = exam.getAgedKonwledgeFunc()!=null?exam.getAgedKonwledgeFunc().intValue():0;
						%>
						<input type="radio" id="agedKonwledgeFunc1" name="exam.agedKonwledgeFunc" value="1" <%=(agedKonwledgeFunc==1)?"checked=\"checked\"":""%> /><label for="agedKonwledgeFunc1">初筛阴性</label>
						&nbsp;
						<input type="radio" id="agedKonwledgeFunc2" name="exam.agedKonwledgeFunc" value="2" <%=(agedKonwledgeFunc==2)?"checked=\"checked\"":""%> /><label for="agedKonwledgeFunc2">初筛阳性</label>
						&nbsp;&nbsp;&nbsp;
						<label>简易智力状态检查总分分值:</label><input type="text" style="width: 80px;" maxlength="4" name="exam.agedKfScore" value="<%=DecimalUtil.toIntegerString(exam.getAgedKfScore())%>" />
					</td>
				</tr>
				<tr>
					<td class="td_title">老年人情感状态:</td>
					<td colspan="3">
						<%
						int agedFeelingStatus = exam.getAgedFeelingStatus()!=null?exam.getAgedFeelingStatus().intValue():0;
						%>
						<input type="radio" id="agedFeelingStatus1" name="exam.agedFeelingStatus" value="1" <%=(agedFeelingStatus==1)?"checked=\"checked\"":""%> /><label for="agedFeelingStatus1">初筛阴性</label>
						&nbsp;
						<input type="radio" id="agedFeelingStatus2" name="exam.agedFeelingStatus" value="2" <%=(agedFeelingStatus==2)?"checked=\"checked\"":""%> /><label for="agedFeelingStatus2">初筛阳性</label>
						&nbsp;&nbsp;&nbsp;
						<label>老年人抑郁评分检查:</label><input type="text" style="width: 80px;" maxlength="4" name="exam.agedFsScore" value="<%=DecimalUtil.toIntegerString(exam.getAgedFsScore())%>" />
					</td>
				</tr>
			</table>
		</div>
		<div class="sectSplit"></div>
		<div class="sectTitleWrap">
			<div class="sectTitle">生活方式</div>
		</div>
		<div class="contentPanel">
			<table class="table">
				<tr>
					<%
					int exerciseRate = exam.getExerciseRate()!=null?exam.getExerciseRate().intValue():0;
					boolean exerciseDisable = exerciseRate==4;
					%>
					<td width="15%" rowspan="3" valign="middle" align="center">体育锻炼</td>
					<td class="td_title" width="20%">锻炼频率:</td>
					<td colspan="3">
						<input type="radio" id="exerciseRate1" name="exam.exerciseRate" value="1" <%=(exerciseRate==1)?"checked=\"checked\"":""%> onclick="exerciseRateChange(this.value)" /><label for="exerciseRate1">每天</label>
						&nbsp;
						<input type="radio" id="exerciseRate2" name="exam.exerciseRate" value="2" <%=(exerciseRate==2)?"checked=\"checked\"":""%> onclick="exerciseRateChange(this.value)" /><label for="exerciseRate2">每周一次以上</label>
						&nbsp;
						<input type="radio" id="exerciseRate3" name="exam.exerciseRate" value="3" <%=(exerciseRate==3)?"checked=\"checked\"":""%> onclick="exerciseRateChange(this.value)" /><label for="exerciseRate3">偶尔</label>
						&nbsp;
						<input type="radio" id="exerciseRate4" name="exam.exerciseRate" value="4" <%=(exerciseRate==4)?"checked=\"checked\"":""%> onclick="exerciseRateChange(this.value)" /><label for="exerciseRate4">不锻炼</label>
					</td>
				</tr>
				<tr>
					<td class="td_title" width="20%">每次锻炼时间:</td>
					<td width="21%">
						<input type="text" maxlength="4" style="width: 60px;" name="exam.everyExerTime" <%=exerciseDisable?"disabled=\"disabled\"":""%> value="<%=!exerciseDisable?DecimalUtil.toIntegerString(exam.getEveryExerTime()):""%>" />分钟
					</td>
					<td class="td_title" width="20%">坚持锻炼时间:</td>
					<td>
						<input type="text" maxlength="3" style="width: 60px;" name="exam.insistExerYear" <%=exerciseDisable?"disabled=\"disabled\"":""%> value="<%=!exerciseDisable?DecimalUtil.toIntegerString(exam.getInsistExerYear()):""%>" />年
					</td>
				</tr>
				<tr>
					<td class="td_title">锻炼方式:</td>
					<td colspan="3">
						<input type="text" maxlength="50" style="width: 100px;" name="exam.exerType" <%=exerciseDisable?"disabled=\"disabled\"":""%> value="<%=!exerciseDisable?StringUtil.ensureStringNotNull(exam.getExerType()):""%>" />
					</td>
				</tr>
				<tr>
					<%
					List<String> dietStatuss = StringUtil.splitIgnoreEmpty(exam.getDietStatus(), ";");
					%>
					<td valign="middle" align="center">饮食方式</td>
					<td colspan="4">
						<input type="radio" id="dietStatus1" name="exam.dietStatus" value="1" <%=dietStatuss.contains("1")?"checked=\"checked\"":""%> /><label for="dietStatus1">荤素均衡</label>
						&nbsp;
						<input type="radio" id="dietStatus2" name="exam.dietStatus" value="2" <%=dietStatuss.contains("2")?"checked=\"checked\"":""%> /><label for="dietStatus2">荤食为主</label>
						&nbsp;
						<input type="radio" id="dietStatus3" name="exam.dietStatus" value="3" <%=dietStatuss.contains("3")?"checked=\"checked\"":""%> /><label for="dietStatus3">素食为主</label>
						&nbsp;
						<input type="checkbox" id="dietStatus4" name="dietStatusOther" value="4" <%=dietStatuss.contains("4")?"checked=\"checked\"":""%> /><label for="dietStatus4">嗜盐</label>
						&nbsp;
						<input type="checkbox" id="dietStatus5" name="dietStatusOther" value="5" <%=dietStatuss.contains("5")?"checked=\"checked\"":""%> /><label for="dietStatus5">嗜油</label>
						&nbsp;
						<input type="checkbox" id="dietStatus6" name="dietStatusOther" value="6" <%=dietStatuss.contains("6")?"checked=\"checked\"":""%> /><label for="dietStatus6">嗜糖</label>
					</td>
				</tr>
				<tr>
					<%
					int smokeStatus = exam.getSmokeStatus()!=null?exam.getSmokeStatus().intValue():0;
					boolean smokeDisable = smokeStatus==1;
					%>
					<td rowspan="3" valign="middle" align="center">吸烟情况</td>
					<td class="td_title">吸烟状况:</td>
					<td colspan="3">
						<input type="radio" id="smokeStatus1" name="exam.smokeStatus" value="1" <%=smokeStatus==1?"checked=\"checked\"":""%> onclick="smokeStatusChange(this.value)" /><label for="smokeStatus1">从不吸烟</label>
						&nbsp;
						<input type="radio" id="smokeStatus2" name="exam.smokeStatus" value="2" <%=smokeStatus==2?"checked=\"checked\"":""%> onclick="smokeStatusChange(this.value)" /><label for="smokeStatus2">已戒烟</label>
						&nbsp;
						<input type="radio" id="smokeStatus3" name="exam.smokeStatus" value="3" <%=smokeStatus==3?"checked=\"checked\"":""%> onclick="smokeStatusChange(this.value)" /><label for="smokeStatus3">吸烟</label>
					</td>
				</tr>
				<tr>
					<td class="td_title">日平均吸烟量:</td>
					<td colspan="3">
						<input type="text" maxlength="4" style="width: 60px;" name="exam.dailySmoke" <%=smokeDisable?"disabled=\"disabled\"":""%> value="<%=!smokeDisable?DecimalUtil.toIntegerString(exam.getDailySmoke()):""%>" />支
					</td>
				</tr>
				<tr>
					<td class="td_title">开始吸烟年龄:</td>
					<td>
						<input type="text" maxlength="3" style="width: 60px;" name="exam.startSmokeAge" <%=smokeDisable?"disabled=\"disabled\"":""%> value="<%=!smokeDisable?DecimalUtil.toIntegerString(exam.getStartSmokeAge()):""%>" />岁
					</td>
					<td class="td_title">戒烟年龄:</td>
					<td>
						<input type="text" maxlength="3" style="width: 60px;" name="exam.notSmokeAge" <%=smokeDisable?"disabled=\"disabled\"":""%> value="<%=!smokeDisable?DecimalUtil.toIntegerString(exam.getNotSmokeAge()):""%>" />岁
					</td>
				</tr>
				<tr>
					<%
					int drinkWineRate = exam.getDrinkWineRate()!=null?exam.getDrinkWineRate().intValue():0;
					boolean drinkDisable = drinkWineRate==1;
					%>
					<td rowspan="5" valign="middle" align="center">饮酒情况</td>
					<td class="td_title">饮酒频率:</td>
					<td colspan="3">
						<input type="radio" id="drinkWineRate1" name="exam.drinkWineRate" value="1" <%=drinkWineRate==1?"checked=\"checked\"":""%> onclick="drinkWineChange(this.value)" /><label for="drinkWineRate1">从不</label>
						&nbsp;
						<input type="radio" id="drinkWineRate2" name="exam.drinkWineRate" value="2" <%=drinkWineRate==2?"checked=\"checked\"":""%> onclick="drinkWineChange(this.value)" /><label for="drinkWineRate2">偶尔</label>
						&nbsp;
						<input type="radio" id="drinkWineRate3" name="exam.drinkWineRate" value="3" <%=drinkWineRate==3?"checked=\"checked\"":""%> onclick="drinkWineChange(this.value)" /><label for="drinkWineRate3">经常</label>
						&nbsp;
						<input type="radio" id="drinkWineRate4" name="exam.drinkWineRate" value="4" <%=drinkWineRate==4?"checked=\"checked\"":""%> onclick="drinkWineChange(this.value)" /><label for="drinkWineRate4">每天</label>
					</td>
				</tr>
				<tr>
					<td class="td_title">日平均饮酒量:</td>
					<td colspan="3">
						<input type="text" maxlength="4" style="width: 60px;" name="exam.dailyDrink" <%=drinkDisable?"disabled=\"disabled\"":""%> value="<%=!drinkDisable?DecimalUtil.toIntegerString(exam.getDailyDrink()):""%>" />两
					</td>
				</tr>
				<tr>
					<td class="td_title">是否戒酒:</td>
					<td colspan="3">
						<%
						int isNotDrink = exam.getIsNotDrink()!=null?exam.getIsNotDrink().intValue():0;
						%>
						<input type="radio" id="isNotDrink1" name="exam.isNotDrink" value="1" <%=drinkDisable?"disabled=\"disabled\"":""%> <%=(!drinkDisable)&&isNotDrink==1?"checked=\"checked\"":""%> onclick="isNotDrinkChange(this.value)" /><label for="isNotDrink1">未戒酒</label>
						&nbsp;
						<input type="radio" id="isNotDrink2" name="exam.isNotDrink" value="2" <%=drinkDisable?"disabled=\"disabled\"":""%> <%=(!drinkDisable)&&isNotDrink==2?"checked=\"checked\"":""%> onclick="isNotDrinkChange(this.value)" /><label for="isNotDrink2">已戒酒</label>，戒酒年龄:<input type="text" maxlength="3" style="width: 60px;" name="exam.notDrinkAge" <%=drinkDisable||isNotDrink==1?"disabled=\"disabled\"":""%> value="<%=(!drinkDisable)&&isNotDrink!=1?DecimalUtil.toIntegerString(exam.getNotDrinkAge()):""%>" />岁
					</td>
				</tr>
				<tr>
					<td class="td_title">开始饮酒年龄:</td>
					<td>
						<input type="text" maxlength="3" style="width: 60px;" name="exam.startDrinkAge" <%=drinkDisable?"disabled=\"disabled\"":""%> value="<%=!drinkDisable?DecimalUtil.toIntegerString(exam.getStartDrinkAge()):""%>" />岁
					</td>
					<td class="td_title">近一年内是否曾醉酒:</td>
					<td>
						<%
						int recentYearDrink = exam.getRecentYearDrink()!=null?exam.getRecentYearDrink().intValue():0;
						%>
						<input type="radio" id="recentYearDrink1" name="exam.recentYearDrink" value="1" <%=drinkDisable?"disabled=\"disabled\"":""%> <%=(!drinkDisable)&&recentYearDrink==1?"checked=\"checked\"":""%> /><label for="recentYearDrink1">是</label>
						&nbsp;
						<input type="radio" id="recentYearDrink2" name="exam.recentYearDrink" value="2" <%=drinkDisable?"disabled=\"disabled\"":""%> <%=(!drinkDisable)&&recentYearDrink==2?"checked=\"checked\"":""%> /><label for="recentYearDrink2">否</label>
					</td>
				</tr>
				<tr>
					<td class="td_title">饮酒种类:</td>
					<td colspan="3">
						<%
						List<String> drinkTypes = StringUtil.splitIgnoreEmpty(exam.getDrinkType(), ";");
						%>
						<input type="checkbox" id="drinkType1" name="drinkType" value="1" <%=drinkDisable?"disabled=\"disabled\"":""%> <%=(!drinkDisable)&&drinkTypes.contains("1")?"checked=\"checked\"":""%> /><label for="drinkType1">白酒</label>
						&nbsp;
						<input type="checkbox" id="drinkType2" name="drinkType" value="2" <%=drinkDisable?"disabled=\"disabled\"":""%> <%=(!drinkDisable)&&drinkTypes.contains("2")?"checked=\"checked\"":""%> /><label for="drinkType2">啤酒</label>
						&nbsp;
						<input type="checkbox" id="drinkType3" name="drinkType" value="3" <%=drinkDisable?"disabled=\"disabled\"":""%> <%=(!drinkDisable)&&drinkTypes.contains("3")?"checked=\"checked\"":""%> /><label for="drinkType3">红酒</label>
						&nbsp;
						<input type="checkbox" id="drinkType4" name="drinkType" value="4" <%=drinkDisable?"disabled=\"disabled\"":""%> <%=(!drinkDisable)&&drinkTypes.contains("4")?"checked=\"checked\"":""%> /><label for="drinkType4">黄酒</label>
						&nbsp;
						<label>其它</label>
						<input type="text" maxlength="20" style="width: 60px;" id="otherDrinkType" name="exam.otherDrinkType" <%=drinkDisable?"disabled=\"disabled\"":""%> value="<%=!drinkDisable?StringUtil.ensureStringNotNull(exam.getOtherDrinkType()):""%>" />
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">职业病危害因素接触史</td>
					<td class="td_title">工种:</td>
					<td>
						<input type="text" maxlength="20" style="width: 100px;" name="exam.workType" value="<%=StringUtil.ensureStringNotNull(exam.getWorkType())%>" />
					</td>
					<td class="td_title">从业时间:</td>
					<td>
						<input type="text" maxlength="5" style="width: 100px;" name="exam.workTime" value="<%=exam.getWorkTime()!=null?exam.getWorkTime().toString():""%>" />年
					</td>
				</tr>
			</table>
		</div>
		<div class="sectSplit"></div>
		<div class="sectTitleWrap">
			<div class="sectTitle">脏器功能</div>
		</div>
		<div class="contentPanel">
			<table class="table">
				<tr>
					<td width="15%" rowspan="3" valign="middle" align="center">口腔</td>
					<td class="td_title" width="20%">口唇:</td>
					<td>
						<%
						int lips = exam.getLips()!=null?exam.getLips().intValue():0;
						%>
						<input type="radio" id="lips1" name="exam.lips" value="1" <%=lips==1?"checked=\"checked\"":""%> /><label for="lips1">红润</label>
						&nbsp;
						<input type="radio" id="lips2" name="exam.lips" value="2" <%=lips==2?"checked=\"checked\"":""%> /><label for="lips2">苍白</label>
						&nbsp;
						<input type="radio" id="lips3" name="exam.lips" value="3" <%=lips==3?"checked=\"checked\"":""%> /><label for="lips3">发绀</label>
						&nbsp;
						<input type="radio" id="lips4" name="exam.lips" value="4" <%=lips==4?"checked=\"checked\"":""%> /><label for="lips4">皲裂</label>
						&nbsp;
						<input type="radio" id="lips5" name="exam.lips" value="5" <%=lips==5?"checked=\"checked\"":""%> /><label for="lips5">疱疹</label>
					</td>
				</tr>
				<tr>
					<td class="td_title">齿列:</td>
					<td>
						<%
						int tooth = exam.getTooth()!=null?exam.getTooth().intValue():0;
						%>
						<input type="radio" id="tooth1" name="exam.tooth" value="1" <%=tooth==1?"checked=\"checked\"":""%> /><label for="tooth1">正常</label>
						&nbsp;
						<input type="radio" id="tooth2" name="exam.tooth" value="2" <%=tooth==2?"checked=\"checked\"":""%> /><label for="tooth2">缺齿</label>
						&nbsp;
						<input type="radio" id="tooth3" name="exam.tooth" value="3" <%=tooth==3?"checked=\"checked\"":""%> /><label for="tooth3">龋齿</label>
						&nbsp;
						<input type="radio" id="tooth4" name="exam.tooth" value="4" <%=tooth==4?"checked=\"checked\"":""%> /><label for="tooth4">义齿(假牙)</label>
					</td>
				</tr>
				<tr>
					<td class="td_title">咽部:</td>
					<td>
						<%
						int throad = exam.getThroad()!=null?exam.getThroad().intValue():0;
						%>
						<input type="radio" id="throad1" name="exam.throad" value="1" <%=throad==1?"checked=\"checked\"":""%> /><label for="throad1">无充血</label>
						&nbsp;
						<input type="radio" id="throad2" name="exam.throad" value="2" <%=throad==2?"checked=\"checked\"":""%> /><label for="throad2">充血</label>
						&nbsp;
						<input type="radio" id="throad3" name="exam.throad" value="3" <%=throad==3?"checked=\"checked\"":""%> /><label for="throad3">淋巴滤泡增生</label>
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">视力</td>
					<td colspan="2">
						左眼:<input type="text" maxlength="3" style="width: 60px;" name="exam.lEyesight" value="<%=exam.getLEyesight()!=null?exam.getLEyesight().toString():""%>" />
						&nbsp;&nbsp;
						右眼:<input type="text" maxlength="3" style="width: 60px;" name="exam.rEyesight" value="<%=exam.getREyesight()!=null?exam.getREyesight().toString():""%>" />
						&nbsp;&nbsp;
						矫正视力左眼:<input type="text" maxlength="3" style="width: 60px;" name="exam.reLEyesight" value="<%=exam.getReLEyesight()!=null?exam.getReLEyesight().toString():""%>" />
						&nbsp;&nbsp;
						 矫正视力右眼:<input type="text" maxlength="3" style="width: 60px;" name="exam.reREyesight" value="<%=exam.getReREyesight()!=null?exam.getReREyesight().toString():""%>" />
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">听力</td>
					<td colspan="2">
						<%
						int hearing = exam.getHearing()!=null?exam.getHearing().intValue():0;
						%>
						<input type="radio" id="hearing1" name="exam.hearing" value="1" <%=hearing==1?"checked=\"checked\"":""%> /><label for="hearing1">听见</label>
						&nbsp;
						<input type="radio" id="hearing2" name="exam.hearing" value="2" <%=hearing==2?"checked=\"checked\"":""%> /><label for="hearing2">听不清或无法听见</label>
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">运动功能</td>
					<td colspan="2">
						<%
						int sportsFunc = exam.getSportsFunc()!=null?exam.getSportsFunc().intValue():0;
						%>
						<input type="radio" id="sportsFunc1" name="exam.sportsFunc" value="1" <%=sportsFunc==1?"checked=\"checked\"":""%> /><label for="sportsFunc1">可顺利完成</label>
						&nbsp;
						<input type="radio" id="sportsFunc2" name="exam.sportsFunc" value="2" <%=sportsFunc==2?"checked=\"checked\"":""%> /><label for="sportsFunc2">无法独立完成其中任何一个动作</label>
					</td>
				</tr>
			</table>
		</div>
		<div class="sectSplit"></div>
		<div class="sectTitleWrap">
			<div class="sectTitle">查体</div>
		</div>
		<div class="contentPanel">
			<table class="table">
				<tr>
					<td valign="middle" align="center" width="15%">眼底</td>
					<td colspan="2">
						<input type="text" maxlength="50" style="width: 400px;" name="exam.eyeGround" value="<%=StringUtil.ensureStringNotNull(exam.getEyeGround())%>" />
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">皮肤</td>
					<td colspan="2">
						<%
						int skin = exam.getSkin()!=null?exam.getSkin().intValue():0;
						%>
						<input type="radio" id="skin1" name="exam.skin" value="1" <%=skin==1?"checked=\"checked\"":""%> onclick="skinChange(this.value)" /><label for="skin1">正常</label>
						&nbsp;
						<input type="radio" id="skin2" name="exam.skin" value="2" <%=skin==2?"checked=\"checked\"":""%> onclick="skinChange(this.value)" /><label for="skin2">潮红</label>
						&nbsp;
						<input type="radio" id="skin3" name="exam.skin" value="3" <%=skin==3?"checked=\"checked\"":""%> onclick="skinChange(this.value)" /><label for="skin3">苍白</label>
						&nbsp;
						<input type="radio" id="skin4" name="exam.skin" value="4" <%=skin==4?"checked=\"checked\"":""%> onclick="skinChange(this.value)" /><label for="skin4">发绀</label>
						&nbsp;
						<input type="radio" id="skin5" name="exam.skin" value="5" <%=skin==5?"checked=\"checked\"":""%> onclick="skinChange(this.value)" /><label for="skin5">黄染</label>
						&nbsp;
						<input type="radio" id="skin6" name="exam.skin" value="6" <%=skin==6?"checked=\"checked\"":""%> onclick="skinChange(this.value)" /><label for="skin6">色素沉着</label>
						&nbsp;
						<input type="radio" id="skin7" name="exam.skin" value="7" <%=skin==7?"checked=\"checked\"":""%> onclick="skinChange(this.value)" /><label for="skin7">其它</label>
						<input type="text" maxlength="50" style="width: 60px;" name="exam.otherSkin" <%=skin!=7?"disabled=\"disabled\"":""%> value="<%=skin==7?StringUtil.ensureStringNotNull(exam.getOtherSkin()):""%>" />
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">巩膜</td>
					<td colspan="2">
						<%
						int sclera = exam.getSclera()!=null?exam.getSclera().intValue():0;
						%>
						<input type="radio" id="sclera1" name="exam.sclera" value="1" <%=sclera==1?"checked=\"checked\"":""%> onclick="scleraChange(this.value)" /><label for="sclera1">正常</label>
						&nbsp;
						<input type="radio" id="sclera2" name="exam.sclera" value="2" <%=sclera==2?"checked=\"checked\"":""%> onclick="scleraChange(this.value)" /><label for="sclera2">黄染</label>
						&nbsp;
						<input type="radio" id="sclera3" name="exam.sclera" value="3" <%=sclera==3?"checked=\"checked\"":""%> onclick="scleraChange(this.value)" /><label for="sclera3">充血</label>
						&nbsp;
						<input type="radio" id="sclera4" name="exam.sclera" value="4" <%=sclera==4?"checked=\"checked\"":""%> onclick="scleraChange(this.value)" /><label for="sclera4">其它</label>
						<input type="text" maxlength="50" style="width: 60px;" name="exam.otherSclera" <%=sclera!=4?"disabled=\"disabled\"":""%> value="<%=sclera==4?StringUtil.ensureStringNotNull(exam.getOtherSclera()):""%>" />
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">淋巴结</td>
					<td colspan="2">
						<%
						int lymphaden = exam.getLymphaden()!=null?exam.getLymphaden().intValue():0;
						%>
						<input type="radio" id="lymphaden1" name="exam.lymphaden" value="1" <%=lymphaden==1?"checked=\"checked\"":""%> onclick="lymphadenChange(this.value)" /><label for="lymphaden1">未触及</label>
						&nbsp;
						<input type="radio" id="lymphaden2" name="exam.lymphaden" value="2" <%=lymphaden==2?"checked=\"checked\"":""%> onclick="lymphadenChange(this.value)" /><label for="lymphaden2">锁骨上</label>
						&nbsp;
						<input type="radio" id="lymphaden3" name="exam.lymphaden" value="3" <%=lymphaden==3?"checked=\"checked\"":""%> onclick="lymphadenChange(this.value)" /><label for="lymphaden3">腋窝</label>
						&nbsp;
						<input type="radio" id="lymphaden4" name="exam.lymphaden" value="4" <%=lymphaden==4?"checked=\"checked\"":""%> onclick="lymphadenChange(this.value)" /><label for="lymphaden4">其它</label>
						<input type="text" maxlength="50" style="width: 60px;" name="exam.otherLymphaden" <%=lymphaden!=4?"disabled=\"disabled\"":""%> value="<%=lymphaden==4?StringUtil.ensureStringNotNull(exam.getOtherLymphaden()):""%>" />
					</td>
				</tr>
				<tr>
					<td rowspan="3" valign="middle" align="center" width="15%">肺</td>
					<td class="td_title" width="20%">桶状胸:</td>
					<td>
						<input type="radio" id="lungsChest1" name="exam.lungsChest" value="1" <%=exam.getLungsChest()!=null&&exam.getLungsChest().intValue()==1?"checked=\"checked\"":""%> /><label for="lungsChest1">是</label>
						&nbsp;
						<input type="radio" id="lungsChest2" name="exam.lungsChest" value="2" <%=exam.getLungsChest()!=null&&exam.getLungsChest().intValue()==2?"checked=\"checked\"":""%> /><label for="lungsChest2">否</label>
					</td>
				</tr>
				<tr>
					<td class="td_title">呼吸音:</td>
					<td>
						<input type="text" maxlength="50" style="width: 400px;" name="exam.lungsBreVoice" value="<%=StringUtil.ensureStringNotNull(exam.getLungsBreVoice())%>" />
					</td>
				</tr>
				<tr>
					<td class="td_title">罗音:</td>
					<td>
						<%
						int lungsRale = exam.getLungsRale()!=null?exam.getLungsRale().intValue():0;
						%>
						<input type="radio" id="lungsRale1" name="exam.lungsRale" value="1" <%=lungsRale==1?"checked=\"checked\"":""%> onclick="lungsRaleChange(this.value)" /><label for="lungsRale1">无</label>
						&nbsp;
						<input type="radio" id="lungsRale2" name="exam.lungsRale" value="2" <%=lungsRale==2?"checked=\"checked\"":""%> onclick="lungsRaleChange(this.value)" /><label for="lungsRale2">干罗音</label>
						&nbsp;
						<input type="radio" id="lungsRale3" name="exam.lungsRale" value="3" <%=lungsRale==3?"checked=\"checked\"":""%> onclick="lungsRaleChange(this.value)" /><label for="lungsRale3">湿罗音</label>
						&nbsp;
						<input type="radio" id="lungsRale4" name="exam.lungsRale" value="4" <%=lungsRale==4?"checked=\"checked\"":""%> onclick="lungsRaleChange(this.value)" /><label for="lungsRale4">其它</label>
						<input type="text" maxlength="50" style="width: 60px;" name="exam.otherLungsRale" <%=lungsRale!=4?"disabled=\"disabled\"":""%> value="<%=lungsRale==4?StringUtil.ensureStringNotNull(exam.getOtherLungsRale()):""%>" />
					</td>
				</tr>
				<tr>
					<td rowspan="3" valign="middle" align="center">心脏</td>
					<td class="td_title">心率:</td>
					<td>
						<input type="text" maxlength="3" style="width: 60px;" name="exam.heartRate" value="<%=DecimalUtil.toIntegerString(exam.getHeartRate())%>" />次/分钟
					</td>
				</tr>
				<tr>
					<td class="td_title">心律:</td>
					<td>
						<%
						int heartRhythm = exam.getHeartRhythm()!=null?exam.getHeartRhythm().intValue():0;
						%>
						<input type="radio" id="heartRhythm1" name="exam.heartRhythm" value="1" <%=heartRhythm==1?"checked=\"checked\"":""%> /><label for="heartRhythm1">齐</label>
						&nbsp;
						<input type="radio" id="heartRhythm2" name="exam.heartRhythm" value="2" <%=heartRhythm==2?"checked=\"checked\"":""%> /><label for="heartRhythm2">不齐</label>
						&nbsp;
						<input type="radio" id="heartRhythm3" name="exam.heartRhythm" value="3" <%=heartRhythm==3?"checked=\"checked\"":""%> /><label for="heartRhythm3">绝对不齐</label>
					</td>
				</tr>
				<tr>
					<td class="td_title">杂音:</td>
					<td>
						<input type="text" maxlength="50" style="width: 400px;" name="exam.heartNoise" value="<%=StringUtil.ensureStringNotNull(exam.getHeartNoise())%>" />
					</td>
				</tr>
				<tr>
					<td rowspan="5" valign="middle" align="center">腹部</td>
					<td class="td_title">压痛:</td>
					<td>
						<input type="text" maxlength="50" style="width: 400px;" name="exam.abdPrePain" value="<%=StringUtil.ensureStringNotNull(exam.getAbdPrePain())%>" />
					</td>
				</tr>
				<tr>
					<td class="td_title">包块:</td>
					<td>
						<input type="text" maxlength="50" style="width: 400px;" name="exam.abdBagPiece" value="<%=StringUtil.ensureStringNotNull(exam.getAbdBagPiece())%>" />
					</td>
				</tr>
				<tr>
					<td class="td_title">肝大:</td>
					<td>
						<input type="text" maxlength="50" style="width: 400px;" name="exam.abdHepatomegaly" value="<%=StringUtil.ensureStringNotNull(exam.getAbdHepatomegaly())%>" />
					</td>
				</tr>
				<tr>
					<td class="td_title">脾大:</td>
					<td>
						<input type="text" maxlength="50" style="width: 400px;" name="exam.abdSplenomegaly" value="<%=StringUtil.ensureStringNotNull(exam.getAbdSplenomegaly())%>" />
					</td>
				</tr>
				<tr>
					<td class="td_title">移动性浊音:</td>
					<td>
						<input type="text" maxlength="50" style="width: 400px;" name="exam.abdMovNoise" value="<%=StringUtil.ensureStringNotNull(exam.getAbdMovNoise())%>" />
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">下肢水肿</td>
					<td colspan="2">
						<%
						int limbsEdema = exam.getLimbsEdema()!=null?exam.getLimbsEdema().intValue():0;
						%>
						<input type="radio" id="limbsEdema1" name="exam.limbsEdema" value="1" <%=limbsEdema==1?"checked=\"checked\"":""%> /><label for="limbsEdema1">无</label>
						&nbsp;
						<input type="radio" id="limbsEdema2" name="exam.limbsEdema" value="2" <%=limbsEdema==2?"checked=\"checked\"":""%> /><label for="limbsEdema2">单侧</label>
						&nbsp;
						<input type="radio" id="limbsEdema3" name="exam.limbsEdema" value="3" <%=limbsEdema==3?"checked=\"checked\"":""%> /><label for="limbsEdema3">双侧不对称</label>
						&nbsp;
						<input type="radio" id="limbsEdema4" name="exam.limbsEdema" value="4" <%=limbsEdema==4?"checked=\"checked\"":""%> /><label for="limbsEdema4">双侧对称</label>
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">足背动脉搏动</td>
					<td colspan="2">
						<%
						int footBackArtery = exam.getFootBackArtery()!=null?exam.getFootBackArtery().intValue():0;
						%>
						<input type="radio" id="footBackArtery1" name="exam.footBackArtery" value="1" <%=footBackArtery==1?"checked=\"checked\"":""%> /><label for="footBackArtery1">未触及</label>
						&nbsp;
						<input type="radio" id="footBackArtery2" name="exam.footBackArtery" value="2" <%=footBackArtery==2?"checked=\"checked\"":""%> /><label for="footBackArtery2">触及双侧对称</label>
						&nbsp;
						<input type="radio" id="footBackArtery3" name="exam.footBackArtery" value="3" <%=footBackArtery==3?"checked=\"checked\"":""%> /><label for="footBackArtery3">触及左侧弱或消失</label>
						&nbsp;
						<input type="radio" id="footBackArtery4" name="exam.footBackArtery" value="4" <%=footBackArtery==4?"checked=\"checked\"":""%> /><label for="footBackArtery4">触及右侧弱或消失</label>
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">肛门指诊</td>
					<td colspan="2">
						<%
						int anusDre = exam.getAnusDre()!=null?exam.getAnusDre().intValue():0;
						%>
						<input type="radio" id="anusDre1" name="exam.anusDre" value="1" <%=anusDre==1?"checked=\"checked\"":""%> onclick="anusDreChange(this.value)" /><label for="anusDre1">未及异常</label>
						&nbsp;
						<input type="radio" id="anusDre2" name="exam.anusDre" value="2" <%=anusDre==2?"checked=\"checked\"":""%> onclick="anusDreChange(this.value)" /><label for="anusDre2">触痛</label>
						&nbsp;
						<input type="radio" id="anusDre3" name="exam.anusDre" value="3" <%=anusDre==3?"checked=\"checked\"":""%> onclick="anusDreChange(this.value)" /><label for="anusDre3">包块</label>
						&nbsp;
						<input type="radio" id="anusDre4" name="exam.anusDre" value="4" <%=anusDre==4?"checked=\"checked\"":""%> onclick="anusDreChange(this.value)" /><label for="anusDre4">前列腺异常</label>
						&nbsp;
						<input type="radio" id="anusDre5" name="exam.anusDre" value="5" <%=anusDre==5?"checked=\"checked\"":""%> onclick="anusDreChange(this.value)" /><label for="anusDre5">其它</label>
						<input type="text" maxlength="50" style="width: 60px;" name="exam.otherAnusDre" <%=anusDre!=5?"disabled=\"disabled\"":""%> value="<%=anusDre==5?StringUtil.ensureStringNotNull(exam.getOtherAnusDre()):""%>" />
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">乳腺</td>
					<td colspan="2">
						<%
						List<String> mammaryGlands = StringUtil.splitIgnoreEmpty(exam.getMammaryGland(), ";");
						boolean mSel = !mammaryGlands.contains("1") && !mammaryGlands.contains("2");
						%>
						<input type="radio" id="mammaryGland1" name="exam.mammaryGland" <%=!womanEnable?"disabled=\"disabled\"":""%> value="1" <%=womanEnable&&mammaryGlands.contains("1")?"checked=\"checked\"":""%> onclick="mammaryGlandChange(this.value, this.checked)" /><label for="mammaryGland1">未及异常</label>
						&nbsp;
						<input type="radio" id="mammaryGland2" name="exam.mammaryGland" <%=!womanEnable?"disabled=\"disabled\"":""%> value="2" <%=womanEnable&&mammaryGlands.contains("2")?"checked=\"checked\"":""%> onclick="mammaryGlandChange(this.value, this.checked)" /><label for="mammaryGland2">乳房切除</label>
						&nbsp;
						<input type="checkbox" id="mammaryGland3" name="mammaryGlandM" <%=!womanEnable?"disabled=\"disabled\"":""%> value="3" <%=womanEnable&&mSel&&mammaryGlands.contains("3")?"checked=\"checked\"":""%> onclick="mammaryGlandChange(this.value, this.checked)" /><label for="mammaryGland3">异常泌乳</label>
						&nbsp;
						<input type="checkbox" id="mammaryGland4" name="mammaryGlandM" <%=!womanEnable?"disabled=\"disabled\"":""%> value="4" <%=womanEnable&&mSel&&mammaryGlands.contains("4")?"checked=\"checked\"":""%> onclick="mammaryGlandChange(this.value, this.checked)" /><label for="mammaryGland4">乳腺包块</label>
						&nbsp;
						<input type="checkbox" id="mammaryGland5" name="mammaryGlandM" <%=!womanEnable?"disabled=\"disabled\"":""%> value="5" <%=womanEnable&&mSel&&mammaryGlands.contains("5")?"checked=\"checked\"":""%> onclick="mammaryGlandChange(this.value, this.checked)" /><label for="mammaryGland5">其它</label>
						<input type="text" maxlength="50" style="width: 60px;" id="otherMammaryGland" name="exam.otherMammaryGland" <%=!womanEnable||!mSel||!mammaryGlands.contains("5")?"disabled=\"disabled\"":""%> value="<%=womanEnable&&mSel&&mammaryGlands.contains("5")?StringUtil.ensureStringNotNull(exam.getOtherMammaryGland()):""%>" />
					</td>
				</tr>
				<tr>
					<td rowspan="5" valign="middle" align="center">妇科</td>
					<td class="td_title">外阴:</td>
					<td>
						<input type="text" maxlength="50" style="width: 400px;" name="exam.vulva" <%=!womanEnable?"disabled=\"disabled\"":""%> value="<%=womanEnable?StringUtil.ensureStringNotNull(exam.getVulva()):""%>" />
					</td>
				</tr>
				<tr>
					<td class="td_title">阴道:</td>
					<td>
						<input type="text" maxlength="50" style="width: 400px;" name="exam.vagina" <%=!womanEnable?"disabled=\"disabled\"":""%> value="<%=womanEnable?StringUtil.ensureStringNotNull(exam.getVagina()):""%>" />
					</td>
				</tr>
				<tr>
					<td class="td_title">宫颈:</td>
					<td>
						<input type="text" maxlength="50" style="width: 400px;" name="exam.cervical" <%=!womanEnable?"disabled=\"disabled\"":""%> value="<%=womanEnable?StringUtil.ensureStringNotNull(exam.getCervical()):""%>" />
					</td>
				</tr>
				<tr>
					<td class="td_title">宫体:</td>
					<td>
						<input type="text" maxlength="50" style="width: 400px;" name="exam.corpus" <%=!womanEnable?"disabled=\"disabled\"":""%> value="<%=womanEnable?StringUtil.ensureStringNotNull(exam.getCorpus()):""%>" />
					</td>
				</tr>
				<tr>
					<td class="td_title">附件:</td>
					<td>
						<input type="text" maxlength="50" style="width: 400px;" name="exam.attachment" <%=!womanEnable?"disabled=\"disabled\"":""%> value="<%=womanEnable?StringUtil.ensureStringNotNull(exam.getAttachment()):""%>" />
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">其它</td>
					<td colspan="2">
						<input type="text" maxlength="100" style="width: 400px;" name="exam.otherBodyExam" value="<%=StringUtil.ensureStringNotNull(exam.getOtherBodyExam())%>" />
					</td>
				</tr>
			</table>
		</div>
		<div class="sectSplit"></div>
		<div class="sectTitleWrap">
			<div class="sectTitle">辅助检查</div>
		</div>
		<div class="contentPanel">
			<table class="table">
				<tr>
					<td valign="middle" align="center" width="15%" rowspan="2">血常规</td>
					<td class="td_title" width="20%">血红蛋白:</td>
					<td width="22%">
						<input type="text" maxlength="9" style="width: 60px;" name="exam.hemoglobin" value="<%=exam.getHemoglobin()!=null?exam.getHemoglobin().toString():""%>" />g/L
					</td>
					<td class="td_title" width="20%">白细胞:</td>
					<td>
						<input type="text" maxlength="9" style="width: 60px;" name="exam.whiteBloodCells" value="<%=exam.getWhiteBloodCells()!=null?exam.getWhiteBloodCells().toString():""%>" />×10<sup>9</sup>/L
					</td>
				</tr>
				<tr>
					<td class="td_title">血小板:</td>
					<td>
						<input type="text" maxlength="9" style="width: 60px;" name="exam.platelet" value="<%=exam.getPlatelet()!=null?exam.getPlatelet().toString():""%>" />×10<sup>9</sup>/L
					</td>
					<td class="td_title">其它:</td>
					<td>
						<input type="text" maxlength="100" style="width: 120px;" name="exam.otherBlood" value="<%=StringUtil.ensureStringNotNull(exam.getOtherBlood())%>" />
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center" rowspan="3">尿常规</td>
					<td class="td_title">尿蛋白:</td>
					<td>
						<input type="text" maxlength="15" style="width: 60px;" name="exam.urineProteim" value="<%=StringUtil.ensureStringNotNull(exam.getUrineProteim())%>" />
					</td>
					<td class="td_title">尿糖:</td>
					<td>
						<input type="text" maxlength="15" style="width: 60px;" name="exam.urineSuger" value="<%=StringUtil.ensureStringNotNull(exam.getUrineSuger())%>" />
					</td>
				</tr>
				<tr>
					<td class="td_title">尿酮体:</td>
					<td>
						<input type="text" maxlength="15" style="width: 60px;" name="exam.urineKetone" value="<%=StringUtil.ensureStringNotNull(exam.getUrineKetone())%>" />
					</td>
					<td class="td_title">尿潜血:</td>
					<td>
						<input type="text" maxlength="15" style="width: 60px;" name="exam.urineEry" value="<%=StringUtil.ensureStringNotNull(exam.getUrineEry())%>" />
					</td>
				</tr>
				<tr>
					<td class="td_title">其它:</td>
					<td colspan="3">
						<input type="text" maxlength="50" style="width: 120px;" name="exam.otherUrine" value="<%=StringUtil.ensureStringNotNull(exam.getOtherUrine())%>" />
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">空腹血糖</td>
					<td colspan="4">
						<input type="text" maxlength="4" style="width: 60px;" name="exam.fastingGlucose" value="<%=exam.getFastingGlucose()!=null?exam.getFastingGlucose().toString():""%>" />mmol/L
						<input type="hidden" name="exam.fgUnits" value="mmol/L" />
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">心电图</td>
					<td colspan="4">
						<input type="text" maxlength="100" style="width: 400px;" name="exam.electrocardiogram" value="<%=StringUtil.ensureStringNotNull(exam.getElectrocardiogram())%>" />
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">尿微量白蛋白</td>
					<td colspan="4">
						<input type="text" maxlength="4" style="width: 60px;" name="exam.urineTraceAlbumin" value="<%=DecimalUtil.toIntegerString(exam.getUrineTraceAlbumin())%>" />mg/dL 
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">大便潜血</td>
					<td colspan="4">
						<%
						int defecateOccultBlood = exam.getDefecateOccultBlood()!=null?exam.getDefecateOccultBlood().intValue():0;
						%>
						<input type="radio" id="defecateOccultBlood1" name="exam.defecateOccultBlood" value="1" <%=defecateOccultBlood==1?"checked=\"checked\"":""%> /><label for="defecateOccultBlood1">阴性</label>
						&nbsp;
						<input type="radio" id="defecateOccultBlood2" name="exam.defecateOccultBlood" value="2" <%=defecateOccultBlood==2?"checked=\"checked\"":""%> /><label for="defecateOccultBlood2">阳性</label>
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">糖化血糖蛋白</td>
					<td colspan="4">
						<input type="text" maxlength="6" style="width: 60px;" name="exam.glycatedHemoglobin" value="<%=exam.getGlycatedHemoglobin()!=null?exam.getGlycatedHemoglobin().toString():""%>" />%
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">乙型肝炎<br />表面抗原</td>
					<td colspan="4">
						<%
						int heapSurAntigen = exam.getHeapSurAntigen()!=null?exam.getHeapSurAntigen().intValue():0;
						%>
						<input type="radio" id="heapSurAntigen1" name="exam.heapSurAntigen" value="1" <%=heapSurAntigen==1?"checked=\"checked\"":""%> /><label for="heapSurAntigen1">阴性</label>
						&nbsp;
						<input type="radio" id="heapSurAntigen2" name="exam.heapSurAntigen" value="2" <%=heapSurAntigen==2?"checked=\"checked\"":""%> /><label for="heapSurAntigen2">阳性</label>
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center" rowspan="3">肝功能</td>
					<td class="td_title">血清谷丙转氨酶:</td>
					<td>
						<input type="text" maxlength="9" style="width: 60px;" name="exam.serumAlt" value="<%=exam.getSerumAlt()!=null?exam.getSerumAlt().toString():""%>" />U/L
					</td>
					<td class="td_title">血清谷草转氨酶:</td>
					<td>
						<input type="text" maxlength="9" style="width: 60px;" name="exam.serumAspertateAtf" value="<%=exam.getSerumAspertateAtf()!=null?exam.getSerumAspertateAtf().toString():""%>" />U/L
					</td>
				</tr>
				<tr>
					<td class="td_title">白蛋白:</td>
					<td>
						<input type="text" maxlength="9" style="width: 60px;" name="exam.albumin" value="<%=exam.getAlbumin()!=null?exam.getAlbumin().toString():""%>" />g/L
					</td>
					<td class="td_title">总胆红素:</td>
					<td>
						<input type="text" maxlength="9" style="width: 60px;" name="exam.totalBilirubin" value="<%=exam.getTotalBilirubin()!=null?exam.getTotalBilirubin().toString():""%>" />μmol/L
					</td>
				</tr>
				<tr>
					<td class="td_title">结合胆红素:</td>
					<td colspan="3">
						<input type="text" maxlength="9" style="width: 60px;" name="exam.combiningBilirubin" value="<%=exam.getCombiningBilirubin()!=null?exam.getCombiningBilirubin().toString():""%>" />μmol/L
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center" rowspan="2">肾功能</td>
					<td class="td_title">血清肌酐:</td>
					<td>
						<input type="text" maxlength="9" style="width: 60px;" name="exam.serumCreatinine" value="<%=exam.getSerumCreatinine()!=null?exam.getSerumCreatinine().toString():""%>" />μmol/L
					</td>
					<td class="td_title">血尿素氮:</td>
					<td>
						<input type="text" maxlength="9" style="width: 60px;" name="exam.bloodUreaNitrogen" value="<%=exam.getBloodUreaNitrogen()!=null?exam.getBloodUreaNitrogen().toString():""%>" />mmol/L
					</td>
				</tr>
				<tr>
					<td class="td_title">血钾浓度:</td>
					<td>
						<input type="text" maxlength="9" style="width: 60px;" name="exam.potassiumConcentration" value="<%=exam.getPotassiumConcentration()!=null?exam.getPotassiumConcentration().toString():""%>" />mmol/L
					</td>
					<td class="td_title">血钠浓度:</td>
					<td>
						<input type="text" maxlength="9" style="width: 60px;" name="exam.sodiumConcentration" value="<%=exam.getSodiumConcentration()!=null?exam.getSodiumConcentration().toString():""%>" />mmol/L
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center" rowspan="2">血脂</td>
					<td class="td_title">总胆固醇:</td>
					<td>
						<input type="text" maxlength="9" style="width: 60px;" name="exam.totalCholesterol" value="<%=exam.getTotalCholesterol()!=null?exam.getTotalCholesterol().toString():""%>" />mmol/L
					</td>
					<td class="td_title">甘油三酯:</td>
					<td>
						<input type="text" maxlength="9" style="width: 60px;" name="exam.triglycerides" value="<%=exam.getTriglycerides()!=null?exam.getTriglycerides().toString():""%>" />mmol/L
					</td>
				</tr>
				<tr>
					<td class="td_title">血清低密度<br />脂蛋白胆固醇:</td>
					<td>
						<input type="text" maxlength="9" style="width: 60px;" name="exam.sldlc" value="<%=exam.getSldlc()!=null?exam.getSldlc().toString():""%>" />mmol/L
					</td>
					<td class="td_title">血清高密度<br />脂蛋白胆固醇:</td>
					<td>
						<input type="text" maxlength="9" style="width: 60px;" name="exam.shdlc" value="<%=exam.getShdlc()!=null?exam.getShdlc().toString():""%>" />mmol/L
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">胸部X线片</td>
					<td colspan="4">
						<input type="text" maxlength="100" style="width: 400px;" name="exam.chestXRay" value="<%=StringUtil.ensureStringNotNull(exam.getChestXRay())%>" />
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">B超</td>
					<td colspan="4">
						<input type="text" maxlength="100" style="width: 400px;" name="exam.bUltrasound" value="<%=StringUtil.ensureStringNotNull(exam.getBUltrasound())%>" />
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">宫颈涂片</td>
					<td colspan="4">
						<input type="text" maxlength="100" style="width: 400px;" name="exam.cervicalSmears" value="<%=StringUtil.ensureStringNotNull(exam.getCervicalSmears())%>" />
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">其它</td>
					<td colspan="4">
						<input type="text" maxlength="100" style="width: 400px;" name="exam.otherAssistExam" value="<%=StringUtil.ensureStringNotNull(exam.getOtherAssistExam())%>" />
					</td>
				</tr>
			</table>
		</div>
		<div class="sectSplit"></div>
		<div class="sectTitleWrap">
			<div class="sectTitle">中医体质辨识</div>
		</div>
		<div class="contentPanel">
			<table class="table">
				<tr>
					<td class="td_title" width="20%">平和质:</td>
					<td width="30%">
						<%
						int mildPhysique = exam.getMildPhysique()!=null?exam.getMildPhysique().intValue():0;
						%>
						<input type="radio" id="mildPhysique1" name="exam.mildPhysique" value="1" <%=mildPhysique==1?"checked=\"checked\"":""%> /><label for="mildPhysique1">是</label>
						&nbsp;
						<input type="radio" id="mildPhysique2" name="exam.mildPhysique" value="2" <%=mildPhysique==2?"checked=\"checked\"":""%> /><label for="mildPhysique2">基本是</label>
					</td>
					<td class="td_title" width="20%">气虚质:</td>
					<td>
						<%
						int faintPhysical = exam.getFaintPhysical()!=null?exam.getFaintPhysical().intValue():0;
						%>
						<input type="radio" id="faintPhysical1" name="exam.faintPhysical" value="1" <%=faintPhysical==1?"checked=\"checked\"":""%> /><label for="faintPhysical1">是</label>
						&nbsp;
						<input type="radio" id="faintPhysical2" name="exam.faintPhysical" value="2" <%=faintPhysical==2?"checked=\"checked\"":""%> /><label for="faintPhysical2">倾向是</label>
					</td>
				</tr>
				<tr>
					<td class="td_title">阳虚质:</td>
					<td>
						<%
						int maleQuality = exam.getMaleQuality()!=null?exam.getMaleQuality().intValue():0;
						%>
						<input type="radio" id="maleQuality1" name="exam.maleQuality" value="1" <%=maleQuality==1?"checked=\"checked\"":""%> /><label for="maleQuality1">是</label>
						&nbsp;
						<input type="radio" id="maleQuality2" name="exam.maleQuality" value="2" <%=maleQuality==2?"checked=\"checked\"":""%> /><label for="maleQuality2">倾向是</label>
					</td>
					<td class="td_title">阴虚质:</td>
					<td>
						<%
						int lunarQuality = exam.getLunarQuality()!=null?exam.getLunarQuality().intValue():0;
						%>
						<input type="radio" id="lunarQuality1" name="exam.lunarQuality" value="1" <%=lunarQuality==1?"checked=\"checked\"":""%> /><label for="lunarQuality1">是</label>
						&nbsp;
						<input type="radio" id="lunarQuality2" name="exam.lunarQuality" value="2" <%=lunarQuality==2?"checked=\"checked\"":""%> /><label for="lunarQuality2">倾向是</label>
					</td>
				</tr>
				<tr>
					<td class="td_title">痰湿质:</td>
					<td>
						<%
						int phlegmDamp = exam.getPhlegmDamp()!=null?exam.getPhlegmDamp().intValue():0;
						%>
						<input type="radio" id="phlegmDamp1" name="exam.phlegmDamp" value="1" <%=phlegmDamp==1?"checked=\"checked\"":""%> /><label for="phlegmDamp1">是</label>
						&nbsp;
						<input type="radio" id="phlegmDamp2" name="exam.phlegmDamp" value="2" <%=phlegmDamp==2?"checked=\"checked\"":""%> /><label for="phlegmDamp2">倾向是</label>
					</td>
					<td class="td_title">湿热质:</td>
					<td>
						<%
						int dampnessHeat = exam.getDampnessHeat()!=null?exam.getDampnessHeat().intValue():0;
						%>
						<input type="radio" id="dampnessHeat1" name="exam.dampnessHeat" value="1" <%=dampnessHeat==1?"checked=\"checked\"":""%> /><label for="dampnessHeat1">是</label>
						&nbsp;
						<input type="radio" id="dampnessHeat2" name="exam.dampnessHeat" value="2" <%=dampnessHeat==2?"checked=\"checked\"":""%> /><label for="dampnessHeat2">倾向是</label>
					</td>
				</tr>
				<tr>
					<td class="td_title">血瘀质:</td>
					<td>
						<%
						int bloodQuality = exam.getBloodQuality()!=null?exam.getBloodQuality().intValue():0;
						%>
						<input type="radio" id="bloodQuality1" name="exam.bloodQuality" value="1" <%=bloodQuality==1?"checked=\"checked\"":""%> /><label for="bloodQuality1">是</label>
						&nbsp;
						<input type="radio" id="bloodQuality2" name="exam.bloodQuality" value="2" <%=bloodQuality==2?"checked=\"checked\"":""%> /><label for="bloodQuality2">倾向是</label>
					</td>
					<td class="td_title">气郁质:</td>
					<td>
						<%
						int logisticQuality = exam.getLogisticQuality()!=null?exam.getLogisticQuality().intValue():0;
						%>
						<input type="radio" id="logisticQuality1" name="exam.logisticQuality" value="1" <%=logisticQuality==1?"checked=\"checked\"":""%> /><label for="logisticQuality1">是</label>
						&nbsp;
						<input type="radio" id="logisticQuality2" name="exam.logisticQuality" value="2" <%=logisticQuality==2?"checked=\"checked\"":""%> /><label for="logisticQuality2">倾向是</label>
					</td>
				</tr>
				<tr>
					<td class="td_title">特秉质:</td>
					<td colspan="3">
						<%
						int graspQuality = exam.getGraspQuality()!=null?exam.getGraspQuality().intValue():0;
						%>
						<input type="radio" id="graspQuality1" name="exam.graspQuality" value="1" <%=graspQuality==1?"checked=\"checked\"":""%> /><label for="graspQuality1">是</label>
						&nbsp;
						<input type="radio" id="graspQuality2" name="exam.graspQuality" value="2" <%=graspQuality==2?"checked=\"checked\"":""%> /><label for="graspQuality2">倾向是</label>
					</td>
				</tr>
			</table>
		</div>
		<div class="sectSplit"></div>
		<div class="sectTitleWrap">
			<div class="sectTitle">现存主要健康问题</div>
		</div>
		<div class="contentPanel">
			<table class="table">
				<tr>
					<td align="center" valign="middle" width="15%">脑血管疾病</td>
					<td class="td_mul_line">
						<%
						List<String> cerebraveDiseases = StringUtil.splitIgnoreEmpty(exam.getCerebraveDisease(), ";");
						%>
						<input type="checkbox" id="cerebraveDisease2" name="cerebraveDiseaseItem" value="2" <%=cerebraveDiseases.contains("2")?"checked=\"checked\"":""%> /><label for="cerebraveDisease2">缺血性卒中</label>
						&nbsp;
						<input type="checkbox" id="cerebraveDisease3" name="cerebraveDiseaseItem" value="3" <%=cerebraveDiseases.contains("3")?"checked=\"checked\"":""%> /><label for="cerebraveDisease3">脑出血</label>
						&nbsp;
						<input type="checkbox" id="cerebraveDisease4" name="cerebraveDiseaseItem" value="4" <%=cerebraveDiseases.contains("4")?"checked=\"checked\"":""%> /><label for="cerebraveDisease4">蛛网膜下腔出血</label>
						&nbsp;
						<input type="checkbox" id="cerebraveDisease5" name="cerebraveDiseaseItem" value="5" <%=cerebraveDiseases.contains("5")?"checked=\"checked\"":""%> /><label for="cerebraveDisease5">短暂性脑缺血发作</label>
						<br />
						<label style="margin-left: 3px;">其它</label><input type="text" maxlength="50" style="width: 120px;" id="otherCereDisease" name="exam.otherCereDisease" value="<%=StringUtil.ensureStringNotNull(exam.getOtherCereDisease())%>" />
						<%--其它值为6--%>
					</td>
				</tr>
				<tr>
					<td align="center" valign="middle">肾脏疾病</td>
					<td class="td_mul_line">
						<%
						List<String> renalDiseases = StringUtil.splitIgnoreEmpty(exam.getRenalDisease(), ";");
						%>
						<input type="checkbox" id="renalDisease2" name="renalDiseaseItem" value="2" <%=renalDiseases.contains("2")?"checked=\"checked\"":""%> /><label for="renalDisease2">糖尿病肾病</label>
						&nbsp;
						<input type="checkbox" id="renalDisease3" name="renalDiseaseItem" value="3" <%=renalDiseases.contains("3")?"checked=\"checked\"":""%> /><label for="renalDisease3">肾功能衰竭</label>
						&nbsp;
						<input type="checkbox" id="renalDisease4" name="renalDiseaseItem" value="4" <%=renalDiseases.contains("4")?"checked=\"checked\"":""%> /><label for="renalDisease4">急性肾炎</label>
						&nbsp;
						<input type="checkbox" id="renalDisease5" name="renalDiseaseItem" value="5" <%=renalDiseases.contains("5")?"checked=\"checked\"":""%> /><label for="renalDisease5">慢性肾炎</label>
						<br />
						<label style="margin-left: 3px;">其它</label><input type="text" maxlength="50" style="width: 120px;" id="otherRenalDisease" name="exam.otherRenalDisease" value="<%=StringUtil.ensureStringNotNull(exam.getOtherRenalDisease())%>" />
						<%--其它值为6--%>
					</td>
				</tr>
				<tr>
					<td align="center" valign="middle">心脏疾病</td>
					<td class="td_mul_line">
						<%
						List<String> heartDiseases = StringUtil.splitIgnoreEmpty(exam.getHeartDisease(), ";");
						%>
						<input type="checkbox" id="heartDisease2" name="heartDiseaseItem" value="2" <%=heartDiseases.contains("2")?"checked=\"checked\"":""%> /><label for="heartDisease2">心肌梗死</label>
						&nbsp;
						<input type="checkbox" id="heartDisease3" name="heartDiseaseItem" value="3" <%=heartDiseases.contains("3")?"checked=\"checked\"":""%> /><label for="heartDisease3">心绞痛</label>
						&nbsp;
						<input type="checkbox" id="heartDisease4" name="heartDiseaseItem" value="4" <%=heartDiseases.contains("4")?"checked=\"checked\"":""%> /><label for="heartDisease4">冠状动脉血运重建</label>
						&nbsp;
						<input type="checkbox" id="heartDisease5" name="heartDiseaseItem" value="5" <%=heartDiseases.contains("5")?"checked=\"checked\"":""%> /><label for="heartDisease5">充血性心力衰竭</label>
						&nbsp;
						<input type="checkbox" id="heartDisease6" name="heartDiseaseItem" value="6" <%=heartDiseases.contains("6")?"checked=\"checked\"":""%> /><label for="heartDisease6">心前区疼痛</label>
						<br />
						<label style="margin-left: 3px;">其它</label><input type="text" maxlength="50" style="width: 120px;" id="otherHeartDisease" name="exam.otherHeartDisease" value="<%=StringUtil.ensureStringNotNull(exam.getOtherHeartDisease())%>" />
						<%--其它值为7--%>
					</td>
				</tr>
				<tr>
					<td align="center" valign="middle">血管疾病</td>
					<td class="td_mul_line">
						<%
						List<String> vascularDiseases = StringUtil.splitIgnoreEmpty(exam.getVascularDisease(), ";");
						%>
						<input type="checkbox" id="vascularDisease2" name="vascularDiseaseItem" value="2" <%=vascularDiseases.contains("2")?"checked=\"checked\"":""%> /><label for="vascularDisease2">夹层动脉瘤</label>
						&nbsp;
						<input type="checkbox" id="vascularDisease3" name="vascularDiseaseItem" value="3" <%=vascularDiseases.contains("3")?"checked=\"checked\"":""%> /><label for="vascularDisease3">动脉闭塞性疾病</label>
						<br />
						<label style="margin-left: 3px;">其它</label><input type="text" maxlength="50" style="width: 120px;" id="otherVascularDisease" name="exam.otherVascularDisease" value="<%=StringUtil.ensureStringNotNull(exam.getOtherVascularDisease())%>" />
						<%--其它值为4--%>
					</td>
				</tr>
				<tr>
					<td align="center" valign="middle">眼部疾病</td>
					<td class="td_mul_line">
						<%
						List<String> eyeDiseases = StringUtil.splitIgnoreEmpty(exam.getEyeDisease(), ";");
						%>
						<input type="checkbox" id="eyeDisease2" name="eyeDiseaseItem" value="2" <%=eyeDiseases.contains("2")?"checked=\"checked\"":""%> /><label for="eyeDisease2">视网膜出血或渗出</label>
						&nbsp;
						<input type="checkbox" id="eyeDisease3" name="eyeDiseaseItem" value="3" <%=eyeDiseases.contains("3")?"checked=\"checked\"":""%> /><label for="eyeDisease3">视乳头水肿</label>
						&nbsp;
						<input type="checkbox" id="eyeDisease4" name="eyeDiseaseItem" value="4" <%=eyeDiseases.contains("4")?"checked=\"checked\"":""%> /><label for="eyeDisease4">白内障</label>
						<br />
						<label style="margin-left: 3px;">其它</label><input type="text" maxlength="50" style="width: 120px;" id="otherEyeDisease" name="exam.otherEyeDisease" value="<%=StringUtil.ensureStringNotNull(exam.getOtherEyeDisease())%>" />
						<%--其它值为5--%>
					</td>
				</tr>
				<tr>
					<td align="center" valign="middle">神经系统疾病</td>
					<td>
						<input type="text" maxlength="50" style="width: 400px;" name="exam.ond" value="<%=StringUtil.ensureStringNotNull(exam.getOnd())%>" />
					</td>
				</tr>
				<tr>
					<td align="center" valign="middle">其它系统疾病</td>
					<td>
						<input type="text" maxlength="50" style="width: 400px;" name="exam.otherSysDisease" value="<%=StringUtil.ensureStringNotNull(exam.getOtherSysDisease())%>" />
					</td>
				</tr>
			</table>
		</div>
		<div class="sectSplit"></div>
		<div class="sectTitleWrap">
			<div class="sectTitle">健康评价</div>
		</div>
		<div class="contentPanel">
			<table class="table">
				<tr>
					<td align="center" valign="middle" width="15%">体检评价</td>
					<td>
						<textarea maxlength="255" style="width: 550px;height: 40px;" id="healthyAssess" name="exam.healthyAssess"><%=StringUtil.ensureStringNotNull(exam.getHealthyAssess())%></textarea>
					</td>
				</tr>
				<tr>
					<td align="center" valign="middle">健康等级</td>
					<td>
						<%
						int healthyLevel = exam.getHealthyLevel()!=null?exam.getHealthyLevel().intValue():0;
						%>
						<input type="radio" id="healthyLevel1" name="exam.healthyLevel" value="1" <%=healthyLevel==1?"checked=\"checked\"":""%> /><label for="healthyLevel1">不健康</label>
						&nbsp;
						<input type="radio" id="healthyLevel2" name="exam.healthyLevel" value="2" <%=healthyLevel==2?"checked=\"checked\"":""%> /><label for="healthyLevel2">亚健康</label>
						&nbsp;
						<input type="radio" id="healthyLevel3" name="exam.healthyLevel" value="3" <%=healthyLevel==3?"checked=\"checked\"":""%> /><label for="healthyLevel3">正常</label>
						&nbsp;
						<input type="radio" id="healthyLevel4" name="exam.healthyLevel" value="4" <%=healthyLevel==4?"checked=\"checked\"":""%> /><label for="healthyLevel4">良好</label>
					</td>
				</tr>
			</table>
		</div>
		<div class="sectSplit"></div>
		<div class="sectTitleWrap">
			<div class="sectTitle">健康指导</div>
		</div>
		<div class="contentPanel">
			<table class="table">
				<tr>
					<td align="center" valign="middle" width="15%">建议措施</td>
					<td>
						<%
						List<String> healthyDirects = StringUtil.splitIgnoreEmpty(exam.getHealthyDirect(), ";");
						%>
						<input type="checkbox" id="healthyDirect1" name="healthyDirectItem" value="1" <%=healthyDirects.contains("1")?"checked=\"checked\"":""%> /><label for="healthyDirect1">纳入慢性病患者健康管理</label>
						&nbsp;
						<input type="checkbox" id="healthyDirect2" name="healthyDirectItem" value="2" <%=healthyDirects.contains("2")?"checked=\"checked\"":""%> /><label for="healthyDirect2">建议复查</label>
						&nbsp;
						<input type="checkbox" id="healthyDirect3" name="healthyDirectItem" value="3" <%=healthyDirects.contains("3")?"checked=\"checked\"":""%> /><label for="healthyDirect3">建议转诊</label>
					</td>
				</tr>
				<tr>
					<td align="center" valign="middle">危险因素控制</td>
					<td class="td_mul_line">
						<%
						List<String> dangerControls = StringUtil.splitIgnoreEmpty(exam.getDangerControl(), ";");
						%>
						<input type="checkbox" id="dangerControl1" name="dangerControlItem" value="1" <%=dangerControls.contains("1")?"checked=\"checked\"":""%> /><label for="dangerControl1">戒烟</label>
						&nbsp;
						<input type="checkbox" id="dangerControl2" name="dangerControlItem" value="2" <%=dangerControls.contains("2")?"checked=\"checked\"":""%> /><label for="dangerControl2">健康饮酒</label>
						&nbsp;
						<input type="checkbox" id="dangerControl3" name="dangerControlItem" value="3" <%=dangerControls.contains("3")?"checked=\"checked\"":""%> /><label for="dangerControl3">饮食</label>
						&nbsp;
						<input type="checkbox" id="dangerControl4" name="dangerControlItem" value="4" <%=dangerControls.contains("4")?"checked=\"checked\"":""%> /><label for="dangerControl4">锻炼</label>
						<br />
						<input type="checkbox" id="dangerControl5" name="dangerControlItem" value="5" <%=dangerControls.contains("5")?"checked=\"checked\"":""%> onclick="controlChange(this.value, this.checked)" /><label for="dangerControl5">减体重</label>
						<label>（目标<input type="text" maxlength="5" style="width: 60px;" <%=!dangerControls.contains("5")?"disabled=\"disabled\"":""%> name="exam.dangerControlWeight" value="<%=dangerControls.contains("5")&&exam.getDangerControlWeight()!=null?exam.getDangerControlWeight().toString():""%>" />）</label>
						&nbsp;
						<input type="checkbox" id="dangerControl6" name="dangerControlItem" value="6" <%=dangerControls.contains("6")?"checked=\"checked\"":""%> onclick="controlChange(this.value, this.checked)" /><label for="dangerControl6">建议接种疫苗</label>
						<label>（<input type="text" maxlength="50" style="width: 120px;" <%=!dangerControls.contains("6")?"disabled=\"disabled\"":""%> name="exam.dangerControlVaccin" value="<%=dangerControls.contains("6")?StringUtil.ensureStringNotNull(exam.getDangerControlVaccin()):""%>" />）</label>
						<br />
						<label style="margin-left: 3px;">其它</label><input type="text" maxlength="100" style="width: 180px;" id="dangerControlOther" name="exam.dangerControlOther" value="<%=StringUtil.ensureStringNotNull(exam.getDangerControlOther())%>" />
						<%--其它值为7--%>
					</td>
				</tr>
			</table>
		</div>
		<div class="sectSplit"></div>
		<div class="sectTitleWrap">
			<div class="sectTitle">危害因素接触史</div>
		</div>
		<div class="contentPanel">
			<div class="tbOper">
				<a href="javascript:addPos()" class="easyui-linkbutton">添加</a>
				<a href="javascript:delPos()" class="easyui-linkbutton">删除</a>
			</div>
			<table class="table tb_edit" id="tbPos">
				<tr>
					<td style="width: 25px;" class="td_title">
						<input type="checkbox" onclick="selAllPos(this.checked)" />
					</td>
					<td style="width: 110px;" class="td_title">毒物名称</td>
					<td style="width: 140px;" class="td_title">接触时长（年）</td>
					<td style="width: 140px;" class="td_title">防护措施</td>
				</tr>
				<%
				List<ExamPosion> poss = exam.getExamPosions();
				if(poss!=null)
				{
				    for(ExamPosion pos : poss)
				    {
				%>
				<tr>
					<td>
						<input type="checkbox" name="posSel" />
					</td>
					<td>
						<input type="text" maxlength="50" class="posPosionName" name="posPosionName" value="<%=StringUtil.ensureStringNotNull(pos.getPosionName())%>" />
					</td>
					<td>
						<input type="text" maxlength="4" class="iptDate" name="posWorkTime" value="<%=pos.getWorkTime()!=null?pos.getWorkTime().toString():""%>" />
					</td>
					<td>
						<input type="text" maxlength="100" class="posSafeguard" name="posSafeguard" value="<%=StringUtil.ensureStringNotNull(pos.getSafeguard())%>" />
					</td>
				</tr>
				<%
				    }
				}
				%>
			</table>
		</div>
		<div class="sectSplit"></div>
		<div class="sectTitleWrap">
			<div class="sectTitle">住院史</div>
		</div>
		<div class="contentPanel">
			<div class="tbOper">
				<a href="javascript:addIp()" class="easyui-linkbutton">添加</a>
				<a href="javascript:delIp()" class="easyui-linkbutton">删除</a>
			</div>
			<table class="table tb_edit" id="tbIp">
				<tr>
					<td style="width: 25px;" class="td_title">
						<input type="checkbox" onclick="selAllIP(this.checked)" />
					</td>
					<td style="width: 200px;" class="td_title">入/出院日期</td>
					<td class="td_title">原因</td>
					<td style="width: 140px;" class="td_title">医疗机构名称</td>
					<td style="width: 90px;" class="td_title">病案号</td>
				</tr>
				<%
				List<ExamHospitalHis> ips = exam.getExamHospitalHiss();
				if(ips!=null)
				{
				    for(ExamHospitalHis ip : ips)
				    {
				%>
				<tr>
					<td>
						<input type="checkbox" name="ipSel" />
					</td>
					<td>
						<input type="text" maxlength="10" readonly="readonly" class="Wdate iptDate" name="ipInTime" onclick="WdatePicker({})" value="<%=DateUtil.toHtmlDate(ip.getInTime())%>" />/<input type="text" maxlength="10" readonly="readonly" class="Wdate iptDate" name="ipOutTime" onclick="WdatePicker({})" value="<%=DateUtil.toHtmlDate(ip.getOutTime())%>" />
					</td>
					<td>
						<input type="text" maxlength="100" class="hpReason" name="ipAdmissionReason" value="<%=StringUtil.ensureStringNotNull(ip.getAdmissionReason())%>" />
					</td>
					<td>
						<input type="text" maxlength="100" class="hpName" name="ipHosDept" value="<%=StringUtil.ensureStringNotNull(ip.getHosDept())%>" />
					</td>
					<td>
						<input type="text" maxlength="20" class="hpRecNum" name="ipRecordNo" value="<%=StringUtil.ensureStringNotNull(ip.getRecordNo())%>" />
					</td>
				</tr>
				<%
				    }
				}
				%>
			</table>
		</div>
		<div class="sectSplit"></div>
		<div class="sectTitleWrap">
			<div class="sectTitle">家庭病床史</div>
		</div>
		<div class="contentPanel">
			<div class="tbOper">
				<a href="javascript:addFh()" class="easyui-linkbutton">添加</a>
				<a href="javascript:delFh()" class="easyui-linkbutton">删除</a>
			</div>
			<table class="table tb_edit" id="tbFh">
				<tr>
					<td style="width: 25px;" class="td_title">
						<input type="checkbox" onclick="selAllFh(this.checked)" />
					</td>
					<td style="width: 80px;" class="td_title">成员名称</td>
					<td style="width: 200px;" class="td_title">建/撤床日期</td>
					<td class="td_title">原因</td>
					<td style="width: 110px;" class="td_title">医疗机构名称</td>
					<td style="width: 90px;" class="td_title">病案号</td>
				</tr>
				<%
				List<ExamFamilyHosHis> fhs = exam.getExamFamilyHosHiss();
				if(fhs!=null)
				{
				    for(ExamFamilyHosHis fh : fhs)
				    {
				%>
				<tr>
					<td>
						<input type="checkbox" name="fhSel" />
					</td>
					<td>
						<input type="text" maxlength="50" class="fhUserName" name="fhUserName" value="<%=StringUtil.ensureStringNotNull(fh.getUserName())%>" />
					</td>
					<td>
						<input type="text" maxlength="10" readonly="readonly" class="Wdate iptDate" name="fhCreateBedTime" onclick="WdatePicker({})" value="<%=DateUtil.toHtmlDate(fh.getCreateBedTime())%>" />/<input type="text" maxlength="10" readonly="readonly" class="Wdate iptDate" name="fhPutBedTime" onclick="WdatePicker({})" value="<%=DateUtil.toHtmlDate(fh.getPutBedTime())%>" />
					</td>
					<td>
						<input type="text" maxlength="100" class="fhReason" name="fhReason" value="<%=StringUtil.ensureStringNotNull(fh.getReason())%>" />
					</td>
					<td>
						<input type="text" maxlength="50" class="fhUnit" name="fhHosUnit" value="<%=StringUtil.ensureStringNotNull(fh.getHosUnit())%>" />
					</td>
					<td>
						<input type="text" maxlength="20" class="hpRecNum" name="fhMedRecordNo" value="<%=StringUtil.ensureStringNotNull(fh.getMedRecordNo())%>" />
					</td>
				</tr>
				<%
				    }
				}
				%>
			</table>
		</div>
		<div class="sectSplit"></div>
		<div class="sectTitleWrap">
			<div class="sectTitle">主要用药情况</div>
		</div>
		<div class="contentPanel">
			<div class="tbOper">
				<a href="javascript:addMed()" class="easyui-linkbutton">添加</a>
				<a href="javascript:delMed()" class="easyui-linkbutton">删除</a>
			</div>
			<table class="table tb_edit" id="tbMed">
				<tr>
					<td style="width: 25px;" class="td_title">
						<input type="checkbox" onclick="selAllMed(this.checked)" />
					</td>
					<td style="width: 110px;" class="td_title">药物名称</td>
					<td style="width: 140px;" class="td_title">用法</td>
					<td style="width: 140px;" class="td_title">用量</td>
					<td class="td_title">用药时间</td>
					<td style="width: 80px;" class="td_title">服药依从性</td>
				</tr>
				<%
				List<ExamMedic> meds = exam.getExamMedics();
				if(meds!=null)
				{
				    for(ExamMedic med : meds)
				    {
				%>
				<tr>
					<td>
						<input type="checkbox" name="medSel" />
					</td>
					<td>
						<input type="text" maxlength="50" class="medMedName" name="medMedName" value="<%=StringUtil.ensureStringNotNull(med.getMedName())%>" />
					</td>
					<td>
						<input type="text" maxlength="20" class="medUseType" name="medUseType" value="<%=StringUtil.ensureStringNotNull(med.getUseType())%>" />
					</td>
					<td>
						<input type="text" maxlength="50" class="medUseNum" name="medUseNum" value="<%=StringUtil.ensureStringNotNull(med.getUseNum())%>" />
					</td>
					<td>
						<input type="text" maxlength="50" class="medUseTime" name="medUseTime" value="<%=StringUtil.ensureStringNotNull(med.getUseTime())%>" />
					</td>
					<td>
						<select class="medAdhes" name="medAdhes">
							<option value="" <%=med.getAdhes()==null||med.getAdhes().intValue()==0?"selected=\"selected\"":""%>>请选择</option>
							<option value="1" <%=med.getAdhes()!=null&&med.getAdhes().intValue()==1?"selected=\"selected\"":""%>>规律</option>
							<option value="2" <%=med.getAdhes()!=null&&med.getAdhes().intValue()==2?"selected=\"selected\"":""%>>间断</option>
							<option value="3" <%=med.getAdhes()!=null&&med.getAdhes().intValue()==3?"selected=\"selected\"":""%>>不服药</option>
						</select>
					</td>
				</tr>
				<%
				    }
				}
				%>
			</table>
		</div>
		<div class="sectSplit"></div>
		<div class="sectTitleWrap">
			<div class="sectTitle">非免疫规划预防接种史</div>
		</div>
		<div class="contentPanel">
			<div class="tbOper">
				<a href="javascript:addIno()" class="easyui-linkbutton">添加</a>
				<a href="javascript:delIno()" class="easyui-linkbutton">删除</a>
			</div>
			<table class="table tb_edit" id="tbIno">
				<tr>
					<td style="width: 25px;" class="td_title">
						<input type="checkbox" onclick="selAllIno(this.checked)" />
					</td>
					<td style="width: 210px;" class="td_title">名称</td>
					<td style="width: 120px;" class="td_title">接种日期</td>
					<td class="td_title">接种机构</td>
				</tr>
				<%
				List<ExamInoculate> inos = exam.getExamInoculates();
				if(inos!=null)
				{
				    for(ExamInoculate ino : inos)
				    {
				%>
				<tr>
					<td>
						<input type="checkbox" name="inoSel" />
					</td>
					<td>
						<input type="text" maxlength="20" class="inoInoculateName" name="inoInoculateName" value="<%=StringUtil.ensureStringNotNull(ino.getInoculateName())%>" />
					</td>
					<td>
						<input type="text" maxlength="10" readonly="readonly" class="Wdate iptDate" name="inoInoculateTime" onclick="WdatePicker({})" value="<%=DateUtil.toHtmlDate(ino.getInoculateTime())%>" />
					</td>
					<td>
						<input type="text" maxlength="50" class="inoInoculateDept" name="inoInoculateDept" value="<%=StringUtil.ensureStringNotNull(ino.getInoculateDept())%>" />
					</td>
				</tr>
				<%
				    }
				}
				%>
			</table>
		</div>
		<div style="text-align: center;padding: 20px;">
			<a href="javascript:saveExam()" class="easyui-linkbutton">保存</a>
			<a href="javascript:closeIt()" class="easyui-linkbutton">关闭</a>
		</div>
	</form>
</body>
</html>