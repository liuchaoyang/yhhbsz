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
			<div class="sectOper"><img class="sectTog" src="<%=request.getContextPath()%>/resources/img/exam-expand.png" onclick="sectTog(this, 'conBase');"></div>
		</div>
		<div class="contentPanel" id="conBase">
			<table class="table">
				<tr>
					<td class="td_title" width="20%">姓名:</td>
					<td width="30%">
						<%=exam.getName()%>
						<input type="hidden" id="physicalTableId" name="achexam.id" value="<%=StringUtil.ensureStringNotNull(exam.getId())%>" />
						<input type="hidden" id="custId" name="achexam.custId" value="<%=StringUtil.ensureStringNotNull(exam.getCustId())%>" />
						<input type="hidden" id="residentName" name="achexam.name" value="<%=exam.getName()%>" />
						<input type="hidden" id="examNo" name="achexam.examNo" value="<%=StringUtil.ensureStringNotNull(exam.getExamNo())%>" />
					</td>
					<td class="td_title" width="20%">编号:</td>
					<td><%=exam.getExamNo()%></td>
				</tr>
				<tr>
					<td class="td_title">体检日期:</td>
					<td>
						<%=DateUtil.toHtmlDate(exam.getExamDate())%>
					</td>
					<td class="td_title">体检机构:</td>
					<td>
						<%=StringUtil.ensureStringNotNull(exam.getIndustryName())%>
					</td>
				</tr>
			</table>
		</div>
		<div class="sectSplit"></div>
		<div class="sectTitleWrap isConExpand">
			<div class="sectTitle">症状</div>
			<div class="sectOper"><img class="sectTog" src="<%=request.getContextPath()%>/resources/img/exam-expand.png" onclick="sectTog(this, 'conSym');"></div>
		</div>
		<%
		List<String> symptoms = StringUtil.splitIgnoreEmpty(exam.getSymptom(), ";");
		StringBuilder symSb = new StringBuilder();
		int sysCount = 0;
		if(symptoms.contains("2"))
		{
		    symSb.append(sysCount++>0?"；":"").append("头痛");
		}
		if(symptoms.contains("3"))
		{
		    symSb.append(sysCount++>0?"；":"").append("头晕");
		}
		if(symptoms.contains("4"))
		{
		    symSb.append(sysCount++>0?"；":"").append("心悸");
		}
		if(symptoms.contains("5"))
		{
		    symSb.append(sysCount++>0?"；":"").append("胸闷");
		}
		if(symptoms.contains("6"))
		{
		    symSb.append(sysCount++>0?"；":"").append("胸痛");
		}
		if(symptoms.contains("7"))
		{
		    symSb.append(sysCount++>0?"；":"").append("慢性咳嗽");
		}
		if(symptoms.contains("8"))
		{
		    symSb.append(sysCount++>0?"；":"").append("咳痰");
		}
		if(symptoms.contains("9"))
		{
		    symSb.append(sysCount++>0?"；":"").append("呼吸困难");
		}
		if(symptoms.contains("10"))
		{
		    symSb.append(sysCount++>0?"；":"").append("多饮");
		}
		if(symptoms.contains("11"))
		{
		    symSb.append(sysCount++>0?"；":"").append("多尿");
		}
		if(symptoms.contains("12"))
		{
		    symSb.append(sysCount++>0?"；":"").append("体重下降");
		}
		if(symptoms.contains("13"))
		{
		    symSb.append(sysCount++>0?"；":"").append("乏力");
		}
		if(symptoms.contains("14"))
		{
		    symSb.append(sysCount++>0?"；":"").append("关节肿痛");
		}
		if(symptoms.contains("15"))
		{
		    symSb.append(sysCount++>0?"；":"").append("视力模糊");
		}
		if(symptoms.contains("16"))
		{
		    symSb.append(sysCount++>0?"；":"").append("手脚麻木");
		}
		if(symptoms.contains("17"))
		{
		    symSb.append(sysCount++>0?"；":"").append("尿急");
		}
		if(symptoms.contains("18"))
		{
		    symSb.append(sysCount++>0?"；":"").append("尿痛");
		}
		if(symptoms.contains("19"))
		{
		    symSb.append(sysCount++>0?"；":"").append("便秘");
		}
		if(symptoms.contains("20"))
		{
		    symSb.append(sysCount++>0?"；":"").append("腹泻");
		}
		if(symptoms.contains("21"))
		{
		    symSb.append(sysCount++>0?"；":"").append("恶心呕吐");
		}
		if(symptoms.contains("22"))
		{
		    symSb.append(sysCount++>0?"；":"").append("眼花");
		}
		if(symptoms.contains("23"))
		{
		    symSb.append(sysCount++>0?"；":"").append("耳鸣");
		}
		if(symptoms.contains("24"))
		{
		    symSb.append(sysCount++>0?"；":"").append("乳房胀痛");
		}
		if(symptoms.contains("25")&&StringUtil.isNotEmpty(exam.getOtherSymptom()))
		{
		    symSb.append(sysCount++>0?"；":"").append(exam.getOtherSymptom());
		}
		%>
		<div class="contentPanel" style="padding: 10px;list-style: 200%;border: 1px solid #AAAAAA;" id="conSym">
			<%=symSb.length()>0?symSb.append("。").toString():"无症状。"%>
		</div>
		<div class="sectSplit"></div>
		<div class="sectTitleWrap">
			<div class="sectTitle">一般状况</div>
			<div class="sectOper"><img class="sectTog" src="<%=request.getContextPath()%>/resources/img/exam-expand.png" onclick="sectTog(this, 'conGener');"></div>
		</div>
		<div class="contentPanel" id="conGener">
			<table class="table">
				<tr>
					<td class="td_title" width="20%">体温:</td>
					<td width="30%">
						<%=exam.getTemperature()!=null?exam.getTemperature():""%>℃
					</td>
					<td class="td_title" width="20%">心率:</td>
					<td>
						<%=DecimalUtil.toIntegerString(exam.getPulseRate())%>次/分钟
					</td>
				</tr>
				<tr>
					<td class="td_title">呼吸频率:</td>
					<td>
						<%=DecimalUtil.toIntegerString(exam.getBreatheRate())%>次/分钟
					</td>
					<td class="td_title">身高:</td>
					<td>
						<%=DecimalUtil.toIntegerString(exam.getHeight())%>cm
					</td>
				</tr>
				<tr>
					<td class="td_title">体重:</td>
					<td>
						<%=exam.getWeight()!=null?exam.getWeight():""%>kg
					</td>
					<td class="td_title">腰围:</td>
					<td>
						<%=DecimalUtil.toIntegerString(exam.getWaistline())%>cm
					</td>
				</tr>
				<%--
				<tr>
					<td class="td_title">左侧收缩血压:</td>
					<td>
						<%=DecimalUtil.toIntegerString(exam.getLBloodPressure())%>mmHg
					</td>
					<td class="td_title">左侧舒张血压:</td>
					<td>
						<%=DecimalUtil.toIntegerString(exam.getLBloodPressureBpd())%>mmHg
					</td>
				</tr>
				--%>
				<tr>
					<td class="td_title">收缩血压:</td>
					<td>
						<%=DecimalUtil.toIntegerString(exam.getRBloodPressure())%>mmHg
					</td>
					<td class="td_title">舒张血压:</td>
					<td>
						<%=DecimalUtil.toIntegerString(exam.getRBloodPressureBpd())%>mmHg
					</td>
				</tr>
				<tr>
					<td class="td_title">老年人健康状态<br />自我评估:</td>
					<td colspan="3">
						<%
						int agedHealthAssess = exam.getAgedHealthAssess()!=null?exam.getAgedHealthAssess().intValue():0;
						if(agedHealthAssess==1)
						{
						%>
						满意
						<%
						}else if(agedHealthAssess==2)
						{
						%>
						基本满意
						<%
						}else if(agedHealthAssess==3)
						{
						%>
						说不清楚
						<%
						}else if(agedHealthAssess==4)
						{
						%>
						不太满意
						<%
						}else if(agedHealthAssess==5)
						{
						%>
						不满意
						<%
						}
						%>
					</td>
				</tr>
				<tr>
					<td class="td_title">老年人生活自理<br />能力自我评估:</td>
					<td colspan="3">
						<%
						int agedLifeAssess = exam.getAgedLifeAssess()!=null?exam.getAgedLifeAssess().intValue():0;
						if(agedLifeAssess==1)
						{
						%>
						可自理（0～3分）
						<%
						}else if(agedLifeAssess==2)
						{
						%>
						轻度依赖（4～8分）
						<%
						}else if(agedLifeAssess==3)
						{
						%>
						中度依赖（9～18分)
						<%
						}else if(agedLifeAssess==4)
						{
						%>
						能自理（≥19分）
						<%
						}
						%>
					</td>
				</tr>
				<tr>
					<td class="td_title">老年人认知功能:</td>
					<td colspan="3">
						<%
						int agedKonwledgeFunc = exam.getAgedKonwledgeFunc()!=null?exam.getAgedKonwledgeFunc().intValue():0;
						if(agedKonwledgeFunc==1)
						{
						%>
						初筛阴性
						<%
						}else if(agedKonwledgeFunc==2)
						{
						%>
						初筛阳性
						<%
						}
						%>
						&nbsp;&nbsp;&nbsp;
						<label>简易智力状态检查总分分值:</label><%=DecimalUtil.toIntegerString(exam.getAgedKfScore())%>
					</td>
				</tr>
				<tr>
					<td class="td_title">老年人情感状态:</td>
					<td colspan="3">
						<%
						int agedFeelingStatus = exam.getAgedFeelingStatus()!=null?exam.getAgedFeelingStatus().intValue():0;
						if(agedFeelingStatus==1)
						{
						%>
						初筛阴性
						<%
						}else if(agedFeelingStatus==2)
						{
						%>
						初筛阳性
						<%
						}
						%>
						&nbsp;&nbsp;&nbsp;
						<label>老年人抑郁评分检查:</label><%=DecimalUtil.toIntegerString(exam.getAgedFsScore())%>
					</td>
				</tr>
			</table>
		</div>
		<div class="sectSplit"></div>
		<div class="sectTitleWrap">
			<div class="sectTitle">生活方式</div>
			<div class="sectOper"><img class="sectTog" src="<%=request.getContextPath()%>/resources/img/exam-expand.png" onclick="sectTog(this, 'conLifeStyle');"></div>
		</div>
		<div class="contentPanel" id="conLifeStyle">
			<table class="table">
				<tr>
					<%
					int exerciseRate = exam.getExerciseRate()!=null?exam.getExerciseRate().intValue():0;
					boolean exerciseDisable = exerciseRate==4;
					%>
					<td width="15%" rowspan="3" valign="middle" align="center">体育锻炼</td>
					<td class="td_title" width="20%">锻炼频率:</td>
					<td colspan="3">
						<%
						if(exerciseRate==1)
						{
						%>
						每天
						<%
						}else if(exerciseRate==2)
						{
						%>
						每周一次以上
						<%
						}else if(exerciseRate==3)
						{
						%>
						偶尔
						<%
						}else if(exerciseRate==4)
						{
						%>
						不锻炼
						<%
						}
						%>
					</td>
				</tr>
				<tr>
					<td class="td_title" width="20%">每次锻炼时间:</td>
					<td width="21%">
						<%=!exerciseDisable?DecimalUtil.toIntegerString(exam.getEveryExerTime()):""%>分钟
					</td>
					<td class="td_title" width="20%">坚持锻炼时间:</td>
					<td>
						<%=!exerciseDisable?DecimalUtil.toIntegerString(exam.getInsistExerYear()):""%>年
					</td>
				</tr>
				<tr>
					<td class="td_title">锻炼方式:</td>
					<td colspan="3">
						<%=!exerciseDisable?StringUtil.ensureStringNotNull(exam.getExerType()):""%>
					</td>
				</tr>
				<tr>
					<%
					List<String> dietStatuss = StringUtil.splitIgnoreEmpty(exam.getDietStatus(), ";");
					StringBuilder dietSb = new StringBuilder();
					int dietCount = 0;
					if(dietStatuss.contains("1"))
					{
					    dietSb.append(dietCount++>0?"；":"").append("荤素均衡");
					}
					if(dietStatuss.contains("2"))
					{
					    dietSb.append(dietCount++>0?"；":"").append("荤食为主");
					}
					if(dietStatuss.contains("3"))
					{
					    dietSb.append(dietCount++>0?"；":"").append("素食为主");
					}
					if(dietStatuss.contains("4"))
					{
					    dietSb.append(dietCount++>0?"；":"").append("嗜盐");
					}
					if(dietStatuss.contains("5"))
					{
					    dietSb.append(dietCount++>0?"；":"").append("嗜油");
					}
					if(dietStatuss.contains("6"))
					{
					    dietSb.append(dietCount++>0?"；":"").append("嗜糖");
					}
					%>
					<td valign="middle" align="center">饮食方式</td>
					<td colspan="4">
						<%=dietCount>0?dietSb.append("。").toString():""%>
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
						<%
						if(smokeStatus==1)
						{
						%>
						从不吸烟
						<%
						}else if(smokeStatus==2)
						{
						%>
						已戒烟
						<%
						}else if(smokeStatus==3)
						{
						%>
						吸烟
						<%
						}
						%>
					</td>
				</tr>
				<tr>
					<td class="td_title">日平均吸烟量:</td>
					<td colspan="3">
						<%=!smokeDisable?DecimalUtil.toIntegerString(exam.getDailySmoke()):""%>支
					</td>
				</tr>
				<tr>
					<td class="td_title">开始吸烟年龄:</td>
					<td>
						<%=!smokeDisable?DecimalUtil.toIntegerString(exam.getStartSmokeAge()):""%>岁
					</td>
					<td class="td_title">戒烟年龄:</td>
					<td>
						<%=!smokeDisable?DecimalUtil.toIntegerString(exam.getNotSmokeAge()):""%>岁
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
						<%
						if(drinkWineRate==1)
						{
						%>
						从不
						<%
						}else if(drinkWineRate==2)
						{
						%>
						偶尔
						<%
						}else if(drinkWineRate==3)
						{
						%>
						经常
						<%
						}else if(drinkWineRate==4)
						{
						%>
						每天
						<%
						}
						%>
					</td>
				</tr>
				<tr>
					<td class="td_title">日平均饮酒量:</td>
					<td colspan="3">
						<%=!drinkDisable?DecimalUtil.toIntegerString(exam.getDailyDrink()):""%>两
					</td>
				</tr>
				<tr>
					<td class="td_title">是否戒酒:</td>
					<td colspan="3">
						<%
						int isNotDrink = exam.getIsNotDrink()!=null?exam.getIsNotDrink().intValue():0;
						if(isNotDrink==1)
						{
						%>
						未戒酒
						<%
						}else if(isNotDrink==2)
						{
						%>
						已戒酒，戒酒年龄:<%=(!drinkDisable)&&isNotDrink!=1?DecimalUtil.toIntegerString(exam.getNotDrinkAge()):""%>岁
						<%
						}
						%>
					</td>
				</tr>
				<tr>
					<td class="td_title">开始饮酒年龄:</td>
					<td>
						<%=!drinkDisable?DecimalUtil.toIntegerString(exam.getStartDrinkAge()):""%>岁
					</td>
					<td class="td_title">近一年内是否曾醉酒:</td>
					<td>
						<%
						int recentYearDrink = exam.getRecentYearDrink()!=null?exam.getRecentYearDrink().intValue():0;
						if(recentYearDrink==1)
						{
						%>
						是
						<%
						}else if(recentYearDrink==2)
						{
						%>
						否
						<%
						}
						%>
					</td>
				</tr>
				<tr>
					<td class="td_title">饮酒种类:</td>
					<td colspan="3">
						<%
						List<String> drinkTypes = StringUtil.splitIgnoreEmpty(exam.getDrinkType(), ";");
						StringBuilder drinkTypeSb = new StringBuilder();
						int drinkTypeCount = 0;
						if(drinkTypes.contains("1"))
						{
							drinkTypeSb.append(drinkTypeCount++>0?"；":"").append("白酒");
						}
						if(drinkTypes.contains("2"))
						{
							drinkTypeSb.append(drinkTypeCount++>0?"；":"").append("啤酒");
						}
						if(drinkTypes.contains("3"))
						{
							drinkTypeSb.append(drinkTypeCount++>0?"；":"").append("红酒");
						}
						if(drinkTypes.contains("4"))
						{
							drinkTypeSb.append(drinkTypeCount++>0?"；":"").append("黄酒");
						}
						if(drinkTypes.contains("5")&&StringUtil.isNotEmpty(exam.getOtherDrinkType()))
						{
							drinkTypeSb.append(drinkTypeCount++>0?"；":"").append(exam.getOtherDrinkType());
						}
						%>
						<%=drinkTypeCount>0?drinkTypeSb.append("。").toString():""%>
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">职业病危害因素接触史</td>
					<td class="td_title">工种:</td>
					<td>
						<%=StringUtil.ensureStringNotNull(exam.getWorkType())%>
					</td>
					<td class="td_title">从业时间:</td>
					<td>
						<%=exam.getWorkTime()!=null?exam.getWorkTime().toString():""%>年
					</td>
				</tr>
			</table>
		</div>
		<div class="sectSplit"></div>
		<div class="sectTitleWrap">
			<div class="sectTitle">脏器功能</div>
			<div class="sectOper"><img class="sectTog" src="<%=request.getContextPath()%>/resources/img/exam-expand.png" onclick="sectTog(this, 'conOrgan');"></div>
		</div>
		<div class="contentPanel" id="conOrgan">
			<table class="table">
				<tr>
					<td width="15%" rowspan="3" valign="middle" align="center">口腔</td>
					<td class="td_title" width="20%">口唇:</td>
					<td>
						<%
						int lips = exam.getLips()!=null?exam.getLips().intValue():0;
						if(lips==1)
						{
						%>
						红润
						<%
						}else if(lips==2)
						{
						%>
						苍白
						<%
						}else if(lips==3)
						{
						%>
						发绀
						<%
						}else if(lips==4)
						{
						%>
						皲裂
						<%
						}else if(lips==5)
						{
						%>
						疱疹
						<%
						}
						%>
					</td>
				</tr>
				<tr>
					<td class="td_title">齿列:</td>
					<td>
						<%
						int tooth = exam.getTooth()!=null?exam.getTooth().intValue():0;
						if(tooth==1)
						{
						%>
						正常
						<%
						}else if(tooth==2)
						{
						%>
						缺齿
						<%
						}else if(tooth==3)
						{
						%>
						龋齿
						<%
						}else if(tooth==4)
						{
						%>
						义齿(假牙)
						<%
						}
						%>
					</td>
				</tr>
				<tr>
					<td class="td_title">咽部:</td>
					<td>
						<%
						int throad = exam.getThroad()!=null?exam.getThroad().intValue():0;
						if(throad==1)
						{
						%>
						无充血
						<%
						}else if(throad==2)
						{
						%>
						充血
						<%
						}else if(throad==3)
						{
						%>
						淋巴滤泡增生
						<%
						}
						%>
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">视力</td>
					<td colspan="2">
						左眼:<%=exam.getLEyesight()!=null?exam.getLEyesight().toString():""%>
						&nbsp;&nbsp;
						右眼:<%=exam.getREyesight()!=null?exam.getREyesight().toString():""%>
						&nbsp;&nbsp;
						矫正视力左眼:<%=exam.getReLEyesight()!=null?exam.getReLEyesight().toString():""%>
						&nbsp;&nbsp;
						 矫正视力右眼:<%=exam.getReREyesight()!=null?exam.getReREyesight().toString():""%>
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">听力</td>
					<td colspan="2">
						<%
						int hearing = exam.getHearing()!=null?exam.getHearing().intValue():0;
						if(hearing==1)
						{
						%>
						听见
						<%
						}else if(hearing==2)
						{
						%>
						听不清或无法听见
						<%
						}
						%>
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">运动功能</td>
					<td colspan="2">
						<%
						int sportsFunc = exam.getSportsFunc()!=null?exam.getSportsFunc().intValue():0;
						if(sportsFunc==1)
						{
						%>
						可顺利完成
						<%
						}else if(sportsFunc==2)
						{
						%>
						无法独立完成其中任何一个动作
						<%
						}
						%>
					</td>
				</tr>
			</table>
		</div>
		<div class="sectSplit"></div>
		<div class="sectTitleWrap">
			<div class="sectTitle">查体</div>
			<div class="sectOper"><img class="sectTog" src="<%=request.getContextPath()%>/resources/img/exam-expand.png" onclick="sectTog(this, 'ckBody');"></div>
		</div>
		<div class="contentPanel" id="ckBody">
			<table class="table">
				<tr>
					<td valign="middle" align="center" width="15%">眼底</td>
					<td colspan="2">
						<%=StringUtil.ensureStringNotNull(exam.getEyeGround())%>
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">皮肤</td>
					<td colspan="2">
						<%
						int skin = exam.getSkin()!=null?exam.getSkin().intValue():0;
						if(skin==1)
						{
						%>
						正常
						<%
						}else if(skin==2)
						{
						%>
						潮红
						<% 
						}else if(skin==3)
						{
						%>
						苍白
						<% 
						}else if(skin==4)
						{
						%>
						发绀
						<% 
						}else if(skin==5)
						{
						%>
						黄染
						<% 
						}else if(skin==6)
						{
						%>
						色素沉着
						<% 
						}else if(skin==7)
						{
						%>
						<%=StringUtil.ensureStringNotNull(exam.getOtherSkin())%>
						<% 
						}
						%>
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">巩膜</td>
					<td colspan="2">
						<%
						int sclera = exam.getSclera()!=null?exam.getSclera().intValue():0;
						if(sclera==1)
						{
						%>
						正常
						<%
						}else if(sclera==2)
						{
						%>
						黄染
						<% 
						}else if(sclera==3)
						{
						%>
						充血
						<% 
						}else if(sclera==4)
						{
						%>
						<%=StringUtil.ensureStringNotNull(exam.getOtherSclera())%>
						<% 
						}
						%>
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">淋巴结</td>
					<td colspan="2">
						<%
						int lymphaden = exam.getLymphaden()!=null?exam.getLymphaden().intValue():0;
						if(lymphaden==1)
						{
						%>
						未触及
						<%
						}else if(lymphaden==2)
						{
						%>
						锁骨上
						<% 
						}else if(lymphaden==3)
						{
						%>
						腋窝
						<% 
						}else if(lymphaden==4)
						{
						%>
						<%=StringUtil.ensureStringNotNull(exam.getOtherLymphaden())%>
						<% 
						}
						%>
					</td>
				</tr>
				<tr>
					<td rowspan="3" valign="middle" align="center" width="15%">肺</td>
					<td class="td_title" width="20%">桶状胸:</td>
					<td>
						<%
						if(exam.getLungsChest()!=null&&exam.getLungsChest().intValue()==1)
						{
						%>
						是
						<%
						}else if(exam.getLungsChest()!=null&&exam.getLungsChest().intValue()==2)
						{
						%>
						否
						<%
						}
						%>
					</td>
				</tr>
				<tr>
					<td class="td_title">呼吸音:</td>
					<td>
						<%=StringUtil.ensureStringNotNull(exam.getLungsBreVoice())%>
					</td>
				</tr>
				<tr>
					<td class="td_title">罗音:</td>
					<td>
						<%
						int lungsRale = exam.getLungsRale()!=null?exam.getLungsRale().intValue():0;
						if(lungsRale==1)
						{
						%>
						无
						<%
						}else if(lungsRale==2)
						{
						%>
						干罗音
						<% 
						}else if(lungsRale==3)
						{
						%>
						湿罗音
						<% 
						}else if(lungsRale==4)
						{
						%>
						<%=StringUtil.ensureStringNotNull(exam.getOtherLungsRale())%>
						<% 
						}
						%>
					</td>
				</tr>
				<tr>
					<td rowspan="3" valign="middle" align="center">心脏</td>
					<td class="td_title">心率:</td>
					<td>
						<%=DecimalUtil.toIntegerString(exam.getHeartRate())%>次/分钟
					</td>
				</tr>
				<tr>
					<td class="td_title">心律:</td>
					<td>
						<%
						int heartRhythm = exam.getHeartRhythm()!=null?exam.getHeartRhythm().intValue():0;
						if(heartRhythm==1)
						{
						%>
						齐
						<%
						}else if(heartRhythm==2)
						{
						%>
						不齐
						<% 
						}else if(heartRhythm==3)
						{
						%>
						绝对不齐
						<% 
						}
						%>
					</td>
				</tr>
				<tr>
					<td class="td_title">杂音:</td>
					<td>
						<%=StringUtil.ensureStringNotNull(exam.getHeartNoise())%>
					</td>
				</tr>
				<tr>
					<td rowspan="5" valign="middle" align="center">腹部</td>
					<td class="td_title">压痛:</td>
					<td>
						<%=StringUtil.ensureStringNotNull(exam.getAbdPrePain())%>
					</td>
				</tr>
				<tr>
					<td class="td_title">包块:</td>
					<td>
						<%=StringUtil.ensureStringNotNull(exam.getAbdBagPiece())%>
					</td>
				</tr>
				<tr>
					<td class="td_title">肝大:</td>
					<td>
						<%=StringUtil.ensureStringNotNull(exam.getAbdHepatomegaly())%>
					</td>
				</tr>
				<tr>
					<td class="td_title">脾大:</td>
					<td>
						<%=StringUtil.ensureStringNotNull(exam.getAbdSplenomegaly())%>
					</td>
				</tr>
				<tr>
					<td class="td_title">移动性浊音:</td>
					<td>
						<%=StringUtil.ensureStringNotNull(exam.getAbdMovNoise())%>
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">下肢水肿</td>
					<td colspan="2">
						<%
						int limbsEdema = exam.getLimbsEdema()!=null?exam.getLimbsEdema().intValue():0;
						if(limbsEdema==1)
						{
						%>
						无
						<%
						}else if(limbsEdema==2)
						{
						%>
						单侧
						<% 
						}else if(limbsEdema==3)
						{
						%>
						双侧不对称
						<% 
						}else if(limbsEdema==4)
						{
						%>
						双侧对称
						<% 
						}
						%>
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">足背动脉搏动</td>
					<td colspan="2">
						<%
						int footBackArtery = exam.getFootBackArtery()!=null?exam.getFootBackArtery().intValue():0;
						if(footBackArtery==1)
						{
						%>
						未触及
						<%
						}else if(footBackArtery==2)
						{
						%>
						触及双侧对称
						<% 
						}else if(footBackArtery==3)
						{
						%>
						触及左侧弱或消失
						<% 
						}else if(footBackArtery==4)
						{
						%>
						触及右侧弱或消失
						<% 
						}
						%>
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">肛门指诊</td>
					<td colspan="2">
						<%
						int anusDre = exam.getAnusDre()!=null?exam.getAnusDre().intValue():0;
						if(anusDre==1)
						{
						%>
						未及异常
						<%
						}else if(anusDre==2)
						{
						%>
						触痛
						<% 
						}else if(anusDre==3)
						{
						%>
						包块
						<% 
						}else if(anusDre==4)
						{
						%>
						前列腺异常
						<% 
						}else if(anusDre==5)
						{
						%>
						<%=StringUtil.ensureStringNotNull(exam.getOtherAnusDre())%>
						<% 
						}
						%>
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">乳腺</td>
					<td colspan="2">
						<%
						List<String> mammaryGlands = StringUtil.splitIgnoreEmpty(exam.getMammaryGland(), ";");
						boolean mSel = !mammaryGlands.contains("1") && !mammaryGlands.contains("2");
						if(womanEnable)
						{
						    StringBuilder mammarySb = new StringBuilder();
						    int mammaryCount = 0;
						    if(mammaryGlands.contains("1"))
						    {
						        mammarySb.append("未及异常");
						        mammaryCount = 1;
						    }else if(mammaryGlands.contains("2"))
						    {
						        mammarySb.append("乳房切除");
						        mammaryCount = 1;
						    }else
						    {
						        if(mammaryGlands.contains("3"))
						        {
						            mammarySb.append(mammaryCount++>0?"；":"").append("异常泌乳");
						        }
						        if(mammaryGlands.contains("4"))
						        {
						            mammarySb.append(mammaryCount++>0?"；":"").append("乳腺包块");
						        }
						        if(mammaryGlands.contains("5")&&StringUtil.isNotEmpty(exam.getOtherMammaryGland()))
						        {
						            mammarySb.append(mammaryCount++>0?"；":"").append(exam.getOtherMammaryGland());
						        }
						    }
						%>
						<%=mammaryCount>0?mammarySb.append("。").toString():""%>
						<%
						}
						%>
					</td>
				</tr>
				<tr>
					<td rowspan="5" valign="middle" align="center">妇科</td>
					<td class="td_title">外阴:</td>
					<td>
						<%=womanEnable?StringUtil.ensureStringNotNull(exam.getVulva()):""%>
					</td>
				</tr>
				<tr>
					<td class="td_title">阴道:</td>
					<td>
						<%=womanEnable?StringUtil.ensureStringNotNull(exam.getVagina()):""%>
					</td>
				</tr>
				<tr>
					<td class="td_title">宫颈:</td>
					<td>
						<%=womanEnable?StringUtil.ensureStringNotNull(exam.getCervical()):""%>
					</td>
				</tr>
				<tr>
					<td class="td_title">宫体:</td>
					<td>
						<%=womanEnable?StringUtil.ensureStringNotNull(exam.getCorpus()):""%>
					</td>
				</tr>
				<tr>
					<td class="td_title">附件:</td>
					<td>
						<%=womanEnable?StringUtil.ensureStringNotNull(exam.getAttachment()):""%>
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">其它</td>
					<td colspan="2">
						<%=StringUtil.ensureStringNotNull(exam.getOtherBodyExam())%>
					</td>
				</tr>
			</table>
		</div>
		<div class="sectSplit"></div>
		<div class="sectTitleWrap">
			<div class="sectTitle">辅助检查</div>
			<div class="sectOper"><img class="sectTog" src="<%=request.getContextPath()%>/resources/img/exam-expand.png" onclick="sectTog(this, 'accExam');"></div>
		</div>
		<div class="contentPanel" id="accExam">
			<table class="table">
				<tr>
					<td valign="middle" align="center" width="15%" rowspan="2">血常规</td>
					<td class="td_title" width="20%">血红蛋白:</td>
					<td width="22%">
						<%=exam.getHemoglobin()!=null?exam.getHemoglobin().toString():""%>g/L
					</td>
					<td class="td_title" width="20%">白细胞:</td>
					<td>
						<%=exam.getWhiteBloodCells()!=null?exam.getWhiteBloodCells().toString():""%>×10<sup>9</sup>/L
					</td>
				</tr>
				<tr>
					<td class="td_title">血小板:</td>
					<td>
						<%=exam.getPlatelet()!=null?exam.getPlatelet().toString():""%>×10<sup>9</sup>/L
					</td>
					<td class="td_title">其它:</td>
					<td>
						<%=StringUtil.ensureStringNotNull(exam.getOtherBlood())%>
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center" rowspan="3">尿常规</td>
					<td class="td_title">尿蛋白:</td>
					<td>
						<%=StringUtil.ensureStringNotNull(exam.getUrineProteim())%>
					</td>
					<td class="td_title">尿糖:</td>
					<td>
						<%=StringUtil.ensureStringNotNull(exam.getUrineSuger())%>
					</td>
				</tr>
				<tr>
					<td class="td_title">尿酮体:</td>
					<td>
						<%=StringUtil.ensureStringNotNull(exam.getUrineKetone())%>
					</td>
					<td class="td_title">尿潜血:</td>
					<td>
						<%=StringUtil.ensureStringNotNull(exam.getUrineEry())%>
					</td>
				</tr>
				<tr>
					<td class="td_title">其它:</td>
					<td colspan="3">
						<%=StringUtil.ensureStringNotNull(exam.getOtherUrine())%>
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">空腹血糖</td>
					<td colspan="4">
						<%=exam.getFastingGlucose()!=null?exam.getFastingGlucose().toString():""%>mmol/L
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">心电图</td>
					<td colspan="4">
						<%=StringUtil.ensureStringNotNull(exam.getElectrocardiogram())%>
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">尿微量白蛋白</td>
					<td colspan="4">
						<%=DecimalUtil.toIntegerString(exam.getUrineTraceAlbumin())%>mg/dL 
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">大便潜血</td>
					<td colspan="4">
						<%
						int defecateOccultBlood = exam.getDefecateOccultBlood()!=null?exam.getDefecateOccultBlood().intValue():0;
						if(defecateOccultBlood==1)
						{
						%>
						阴性
						<%
						}else if(defecateOccultBlood==2)
						{
						%>
						阳性
						<% 
						}
						%>
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">糖化血糖蛋白</td>
					<td colspan="4">
						<%=exam.getGlycatedHemoglobin()!=null?exam.getGlycatedHemoglobin().toString():""%>%
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">乙型肝炎<br />表面抗原</td>
					<td colspan="4">
						<%
						int heapSurAntigen = exam.getHeapSurAntigen()!=null?exam.getHeapSurAntigen().intValue():0;
						if(heapSurAntigen==1)
						{
						%>
						阴性
						<%
						}else if(heapSurAntigen==2)
						{
						%>
						阳性
						<% 
						}
						%>
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center" rowspan="3">肝功能</td>
					<td class="td_title">血清谷丙转氨酶:</td>
					<td>
						<%=exam.getSerumAlt()!=null?exam.getSerumAlt().toString():""%>U/L
					</td>
					<td class="td_title">血清谷草转氨酶:</td>
					<td>
						<%=exam.getSerumAspertateAtf()!=null?exam.getSerumAspertateAtf().toString():""%>U/L
					</td>
				</tr>
				<tr>
					<td class="td_title">白蛋白:</td>
					<td>
						<%=exam.getAlbumin()!=null?exam.getAlbumin().toString():""%>g/L
					</td>
					<td class="td_title">总胆红素:</td>
					<td>
						<%=exam.getTotalBilirubin()!=null?exam.getTotalBilirubin().toString():""%>μmol/L
					</td>
				</tr>
				<tr>
					<td class="td_title">结合胆红素:</td>
					<td colspan="3">
						<%=exam.getCombiningBilirubin()!=null?exam.getCombiningBilirubin().toString():""%>μmol/L
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center" rowspan="2">肾功能</td>
					<td class="td_title">血清肌酐:</td>
					<td>
						<%=exam.getSerumCreatinine()!=null?exam.getSerumCreatinine().toString():""%>μmol/L
					</td>
					<td class="td_title">血尿素氮:</td>
					<td>
						<%=exam.getBloodUreaNitrogen()!=null?exam.getBloodUreaNitrogen().toString():""%>mmol/L
					</td>
				</tr>
				<tr>
					<td class="td_title">血钾浓度:</td>
					<td>
						<%=exam.getPotassiumConcentration()!=null?exam.getPotassiumConcentration().toString():""%>mmol/L
					</td>
					<td class="td_title">血钠浓度:</td>
					<td>
						<%=exam.getSodiumConcentration()!=null?exam.getSodiumConcentration().toString():""%>mmol/L
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center" rowspan="2">血脂</td>
					<td class="td_title">总胆固醇:</td>
					<td>
						<%=exam.getTotalCholesterol()!=null?exam.getTotalCholesterol().toString():""%>mmol/L
					</td>
					<td class="td_title">甘油三酯:</td>
					<td>
						<%=exam.getTriglycerides()!=null?exam.getTriglycerides().toString():""%>mmol/L
					</td>
				</tr>
				<tr>
					<td class="td_title">血清低密度<br />脂蛋白胆固醇:</td>
					<td>
						<%=exam.getSldlc()!=null?exam.getSldlc().toString():""%>mmol/L
					</td>
					<td class="td_title">血清高密度<br />脂蛋白胆固醇:</td>
					<td>
						<%=exam.getShdlc()!=null?exam.getShdlc().toString():""%>mmol/L
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">胸部X线片</td>
					<td colspan="4">
						<%=StringUtil.ensureStringNotNull(exam.getChestXRay())%>
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">B超</td>
					<td colspan="4">
						<%=StringUtil.ensureStringNotNull(exam.getBUltrasound())%>
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">宫颈涂片</td>
					<td colspan="4">
						<%=StringUtil.ensureStringNotNull(exam.getCervicalSmears())%>
					</td>
				</tr>
				<tr>
					<td valign="middle" align="center">其它</td>
					<td colspan="4">
						<%=StringUtil.ensureStringNotNull(exam.getOtherAssistExam())%>
					</td>
				</tr>
			</table>
		</div>
		<div class="sectSplit"></div>
		<div class="sectTitleWrap">
			<div class="sectTitle">中医体质辨识</div>
			<div class="sectOper"><img class="sectTog" src="<%=request.getContextPath()%>/resources/img/exam-expand.png" onclick="sectTog(this, 'conChiCons');"></div>
		</div>
		<div class="contentPanel" id="conChiCons">
			<table class="table">
				<tr>
					<td class="td_title" width="20%">平和质:</td>
					<td width="30%">
						<%
						int mildPhysique = exam.getMildPhysique()!=null?exam.getMildPhysique().intValue():0;
						if(mildPhysique==1)
						{
						%>
						是
						<%
						}else if(mildPhysique==2)
						{
						%>
						基本是
						<%
						}
						%>
					</td>
					<td class="td_title" width="20%">气虚质:</td>
					<td>
						<%
						int faintPhysical = exam.getFaintPhysical()!=null?exam.getFaintPhysical().intValue():0;
						if(faintPhysical==1)
						{
						%>
						是
						<%
						}else if(faintPhysical==2)
						{
						%>
						倾向是
						<%
						}
						%>
					</td>
				</tr>
				<tr>
					<td class="td_title">阳虚质:</td>
					<td>
						<%
						int maleQuality = exam.getMaleQuality()!=null?exam.getMaleQuality().intValue():0;
						if(maleQuality==1)
						{
						%>
						是
						<%
						}else if(maleQuality==2)
						{
						%>
						倾向是
						<%
						}
						%>
					</td>
					<td class="td_title">阴虚质:</td>
					<td>
						<%
						int lunarQuality = exam.getLunarQuality()!=null?exam.getLunarQuality().intValue():0;
						if(lunarQuality==1)
						{
						%>
						是
						<%
						}else if(lunarQuality==2)
						{
						%>
						倾向是
						<%
						}
						%>
					</td>
				</tr>
				<tr>
					<td class="td_title">痰湿质:</td>
					<td>
						<%
						int phlegmDamp = exam.getPhlegmDamp()!=null?exam.getPhlegmDamp().intValue():0;
						if(phlegmDamp==1)
						{
						%>
						是
						<%
						}else if(phlegmDamp==2)
						{
						%>
						倾向是
						<%
						}
						%>
					</td>
					<td class="td_title">湿热质:</td>
					<td>
						<%
						int dampnessHeat = exam.getDampnessHeat()!=null?exam.getDampnessHeat().intValue():0;
						if(dampnessHeat==1)
						{
						%>
						是
						<%
						}else if(dampnessHeat==2)
						{
						%>
						倾向是
						<%
						}
						%>
					</td>
				</tr>
				<tr>
					<td class="td_title">血瘀质:</td>
					<td>
						<%
						int bloodQuality = exam.getBloodQuality()!=null?exam.getBloodQuality().intValue():0;
						if(bloodQuality==1)
						{
						%>
						是
						<%
						}else if(bloodQuality==2)
						{
						%>
						倾向是
						<%
						}
						%>
					</td>
					<td class="td_title">气郁质:</td>
					<td>
						<%
						int logisticQuality = exam.getLogisticQuality()!=null?exam.getLogisticQuality().intValue():0;
						if(logisticQuality==1)
						{
						%>
						是
						<%
						}else if(logisticQuality==2)
						{
						%>
						倾向是
						<%
						}
						%>
					</td>
				</tr>
				<tr>
					<td class="td_title">特秉质:</td>
					<td colspan="3">
						<%
						int graspQuality = exam.getGraspQuality()!=null?exam.getGraspQuality().intValue():0;
						if(graspQuality==1)
						{
						%>
						是
						<%
						}else if(graspQuality==2)
						{
						%>
						倾向是
						<%
						}
						%>
					</td>
				</tr>
			</table>
		</div>
		<div class="sectSplit"></div>
		<div class="sectTitleWrap">
			<div class="sectTitle">现存主要健康问题</div>
			<div class="sectOper"><img class="sectTog" src="<%=request.getContextPath()%>/resources/img/exam-expand.png" onclick="sectTog(this, 'conMainIssure');"></div>
		</div>
		<div class="contentPanel" id="conMainIssure">
			<table class="table">
				<tr>
					<td align="center" valign="middle" width="15%">脑血管疾病</td>
					<td class="td_mul_line">
						<%
						List<String> cerebraveDiseases = StringUtil.splitIgnoreEmpty(exam.getCerebraveDisease(), ";");
						StringBuilder cerebSb = new StringBuilder();
						int cerebCount = 0;
						if(cerebraveDiseases.contains("2"))
						{
						    cerebSb.append(cerebCount++>0?"；":"").append("缺血性卒中");
						}
						if(cerebraveDiseases.contains("3"))
						{
						    cerebSb.append(cerebCount++>0?"；":"").append("脑出血");
						}
						if(cerebraveDiseases.contains("4"))
						{
						    cerebSb.append(cerebCount++>0?"；":"").append("蛛网膜下腔出血");
						}
						if(cerebraveDiseases.contains("5"))
						{
						    cerebSb.append(cerebCount++>0?"；":"").append("短暂性脑缺血发作");
						}
						if(cerebraveDiseases.contains("6")&&StringUtil.isNotEmpty(exam.getOtherCereDisease()))
						{
						    cerebSb.append(cerebCount++>0?"；":"").append(exam.getOtherCereDisease());
						}
						%>
						<%=cerebCount>0?cerebSb.append("。").toString():"未发现"%>
					</td>
				</tr>
				<tr>
					<td align="center" valign="middle">肾脏疾病</td>
					<td class="td_mul_line">
						<%
						List<String> renalDiseases = StringUtil.splitIgnoreEmpty(exam.getRenalDisease(), ";");
						StringBuilder renSb = new StringBuilder();
						int renCount = 0;
						if(renalDiseases.contains("2"))
						{
						    renSb.append(renCount++>0?"；":"").append("糖尿病肾病");
						}
						if(renalDiseases.contains("3"))
						{
						    renSb.append(renCount++>0?"；":"").append("肾功能衰竭");
						}
						if(renalDiseases.contains("4"))
						{
						    renSb.append(renCount++>0?"；":"").append("急性肾炎");
						}
						if(renalDiseases.contains("5"))
						{
						    renSb.append(renCount++>0?"；":"").append("慢性肾炎");
						}
						if(StringUtil.isNotEmpty(exam.getOtherRenalDisease()))
						{
						    renSb.append(renCount++>0?"；":"").append(exam.getOtherRenalDisease());
						}
						%>
						<%=renCount>0?renSb.append("。").toString():"未发现"%>
					</td>
				</tr>
				<tr>
					<td align="center" valign="middle">心脏疾病</td>
					<td class="td_mul_line">
						<%
						List<String> heartDiseases = StringUtil.splitIgnoreEmpty(exam.getHeartDisease(), ";");
						StringBuilder heaSb = new StringBuilder();
						int heaCount = 0;
						if(heartDiseases.contains("2"))
						{
						    heaSb.append(heaCount++>0?"；":"").append("心肌梗死");
						}
						if(heartDiseases.contains("3"))
						{
						    heaSb.append(heaCount++>0?"；":"").append("心绞痛");
						}
						if(heartDiseases.contains("4"))
						{
						    heaSb.append(heaCount++>0?"；":"").append("冠状动脉血运重建");
						}
						if(heartDiseases.contains("5"))
						{
						    heaSb.append(heaCount++>0?"；":"").append("充血性心力衰竭");
						}
						if(heartDiseases.contains("6"))
						{
						    heaSb.append(heaCount++>0?"；":"").append("心前区疼痛");
						}
						if(StringUtil.isNotEmpty(exam.getOtherHeartDisease()))
						{
						    heaSb.append(heaCount++>0?"；":"").append(exam.getOtherHeartDisease());
						}
						%>
						<%=heaCount>0?heaSb.append("。").toString():"未发现"%>
					</td>
				</tr>
				<tr>
					<td align="center" valign="middle">血管疾病</td>
					<td class="td_mul_line">
						<%
						List<String> vascularDiseases = StringUtil.splitIgnoreEmpty(exam.getVascularDisease(), ";");
						StringBuilder vasSb = new StringBuilder();
						int vasCount = 0;
						if(vascularDiseases.contains("2"))
						{
						    vasSb.append(vasCount++>0?"；":"").append("夹层动脉瘤");
						}
						if(vascularDiseases.contains("3"))
						{
						    vasSb.append(vasCount++>0?"；":"").append("动脉闭塞性疾病");
						}
						if(StringUtil.isNotEmpty(exam.getOtherVascularDisease()))
						{
						    vasSb.append(vasCount++>0?"；":"").append(exam.getOtherVascularDisease());
						}
						%>
						<%=vasCount>0?vasSb.append("。").toString():"未发现"%>
					</td>
				</tr>
				<tr>
					<td align="center" valign="middle">眼部疾病</td>
					<td class="td_mul_line">
						<%
						List<String> eyeDiseases = StringUtil.splitIgnoreEmpty(exam.getEyeDisease(), ";");
						StringBuilder eyeSb = new StringBuilder();
						int eyeCount = 0;
						if(eyeDiseases.contains("2"))
						{
						    eyeSb.append(eyeCount++>0?"；":"").append("视网膜出血或渗出");
						}
						if(eyeDiseases.contains("3"))
						{
						    eyeSb.append(eyeCount++>0?"；":"").append("视乳头水肿");
						}
						if(eyeDiseases.contains("4"))
						{
						    eyeSb.append(eyeCount++>0?"；":"").append("白内障");
						}
						if(StringUtil.isNotEmpty(exam.getOtherEyeDisease()))
						{
						    eyeSb.append(eyeCount++>0?"；":"").append(exam.getOtherEyeDisease());
						}
						%>
						<%=eyeCount>0?eyeSb.append("。").toString():"未发现"%>
					</td>
				</tr>
				<tr>
					<td align="center" valign="middle">神经系统疾病</td>
					<td>
						<%=StringUtil.ensureStringNotNull(exam.getOnd())%>
					</td>
				</tr>
				<tr>
					<td align="center" valign="middle">其它系统疾病</td>
					<td>
						<%=StringUtil.ensureStringNotNull(exam.getOtherSysDisease())%>
					</td>
				</tr>
			</table>
		</div>
		<div class="sectSplit"></div>
		<div class="sectTitleWrap">
			<div class="sectTitle">健康评价</div>
			<div class="sectOper"><img class="sectTog" src="<%=request.getContextPath()%>/resources/img/exam-expand.png" onclick="sectTog(this, 'conAssess');"></div>
		</div>
		<div class="contentPanel" id="conAssess">
			<table class="table">
				<tr>
					<td align="center" valign="middle" width="15%">体检评价</td>
					<td style="padding: 5px;line-height: 200%;">
						<%=StringUtil.ensureStringNotNull(exam.getHealthyAssess())%>
					</td>
				</tr>
				<tr>
					<td align="center" valign="middle">健康等级</td>
					<td>
						<%
						int healthyLevel = exam.getHealthyLevel()!=null?exam.getHealthyLevel().intValue():0;
						if(healthyLevel==1)
						{
						%>
						不健康
						<%
						}else if(healthyLevel==2)
						{
						%>
						亚健康
						<%
						}else if(healthyLevel==3)
						{
						%>
						正常
						<%
						}else if(healthyLevel==4)
						{
						%>
						良好
						<%
						}
						%>
					</td>
				</tr>
			</table>
		</div>
		<div class="sectSplit"></div>
		<div class="sectTitleWrap">
			<div class="sectTitle">健康指导</div>
			<div class="sectOper"><img class="sectTog" src="<%=request.getContextPath()%>/resources/img/exam-expand.png" onclick="sectTog(this, 'conDire');"></div>
		</div>
		<div class="contentPanel" id="conDire">
			<table class="table">
				<tr>
					<td align="center" valign="middle" width="15%">建议措施</td>
					<td>
						<%
						List<String> healthyDirects = StringUtil.splitIgnoreEmpty(exam.getHealthyDirect(), ";");
						StringBuilder direSb = new StringBuilder();
						int direCount = 0;
						if(healthyDirects.contains("1"))
						{
						    direSb.append(direCount++>0?"；":"").append("纳入慢性病患者健康管理");
						}
						if(healthyDirects.contains("2"))
						{
						    direSb.append(direCount++>0?"；":"").append("建议复查");
						}
						if(healthyDirects.contains("3"))
						{
						    direSb.append(direCount++>0?"；":"").append("建议转诊");
						}
						%>
						<%=direCount>0?direSb.append("。").toString():""%>
					</td>
				</tr>
				<tr>
					<td align="center" valign="middle">危险因素控制</td>
					<td class="td_mul_line">
						<%
						List<String> dangerControls = StringUtil.splitIgnoreEmpty(exam.getDangerControl(), ";");
						StringBuilder danConSb = new StringBuilder();
						int danConCount = 0;
						if(dangerControls.contains("1"))
						{
						    danConSb.append(danConCount++>0?"；":"").append("戒烟");
						}
						if(dangerControls.contains("2"))
						{
						    danConSb.append(danConCount++>0?"；":"").append("健康饮酒");
						}
						if(dangerControls.contains("3"))
						{
						    danConSb.append(danConCount++>0?"；":"").append("饮食");
						}
						if(dangerControls.contains("4"))
						{
						    danConSb.append(danConCount++>0?"；":"").append("锻炼");
						}
						if(dangerControls.contains("5"))
						{
						    danConSb.append(danConCount++>0?"；":"").append("减体重");
						    if(exam.getDangerControlWeight()!=null)
						    {
						        danConSb.append("<label>（目标").append(exam.getDangerControlWeight().toString()).append("）");
						    }
						}
						if(dangerControls.contains("6"))
						{
						    danConSb.append(danConCount++>0?"；":"").append("建议接种疫苗");
						    if(StringUtil.isNotEmpty(exam.getDangerControlVaccin()))
						    {
						        danConSb.append("（").append(exam.getDangerControlVaccin()).append("）");
						    }
						}
					    if(StringUtil.isNotEmpty(exam.getDangerControlOther()))
					    {
					        danConSb.append(danConCount++>0?"；":"").append(exam.getDangerControlOther());
					    }
						%>
						<%=danConCount>0?danConSb.append("。").toString():""%>
					</td>
				</tr>
			</table>
		</div>
		<div class="sectSplit"></div>
		<div class="sectTitleWrap">
			<div class="sectTitle">危害因素接触史</div>
			<div class="sectOper"><img class="sectTog" src="<%=request.getContextPath()%>/resources/img/exam-collapse.png" onclick="sectTog(this, 'conDanPos');"></div>
		</div>
		<div class="contentPanel" id="conDanPos" style="display: none;">
			<table class="table tb_view" id="tbPos">
				<tr>
					<td style="width: 110px;" class="td_title">毒物名称</td>
					<td style="width: 140px;" class="td_title">接触时长（年）</td>
					<td class="td_title">防护措施</td>
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
						<%=StringUtil.ensureStringNotNull(pos.getPosionName())%>
					</td>
					<td>
						<%=pos.getWorkTime()!=null?pos.getWorkTime().toString():""%>
					</td>
					<td>
						<%=StringUtil.ensureStringNotNull(pos.getSafeguard())%>
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
			<div class="sectOper"><img class="sectTog" src="<%=request.getContextPath()%>/resources/img/exam-collapse.png" onclick="sectTog(this, 'conIp');"></div>
		</div>
		<div class="contentPanel" id="conIp" style="display: none;">
			<table class="table tb_view" id="tbIp">
				<tr>
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
					<td style="text-align: center;">
						<%=DateUtil.toHtmlDate(ip.getInTime())%>/<%=DateUtil.toHtmlDate(ip.getOutTime())%>
					</td>
					<td>
						<%=StringUtil.ensureStringNotNull(ip.getAdmissionReason())%>
					</td>
					<td>
						<%=StringUtil.ensureStringNotNull(ip.getHosDept())%>
					</td>
					<td>
						<%=StringUtil.ensureStringNotNull(ip.getRecordNo())%>
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
			<div class="sectOper"><img class="sectTog" src="<%=request.getContextPath()%>/resources/img/exam-collapse.png" onclick="sectTog(this, 'conFh');"></div>
		</div>
		<div class="contentPanel" id="conFh" style="display: none;">
			<table class="table tb_view" id="tbFh">
				<tr>
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
						<%=StringUtil.ensureStringNotNull(fh.getUserName())%>
					</td>
					<td>
						<%=DateUtil.toHtmlDate(fh.getCreateBedTime())%>/<%=DateUtil.toHtmlDate(fh.getPutBedTime())%>
					</td>
					<td>
						<%=StringUtil.ensureStringNotNull(fh.getReason())%>
					</td>
					<td>
						<%=StringUtil.ensureStringNotNull(fh.getHosUnit())%>
					</td>
					<td>
						<%=StringUtil.ensureStringNotNull(fh.getMedRecordNo())%>
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
			<div class="sectOper"><img class="sectTog" src="<%=request.getContextPath()%>/resources/img/exam-collapse.png" onclick="sectTog(this, 'conMed');"></div>
		</div>
		<div class="contentPanel" id="conMed" style="display: none;">
			<table class="table tb_view" id="tbMed">
				<tr>
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
						<%=StringUtil.ensureStringNotNull(med.getMedName())%>
					</td>
					<td>
						<%=StringUtil.ensureStringNotNull(med.getUseType())%>
					</td>
					<td>
						<%=StringUtil.ensureStringNotNull(med.getUseNum())%>
					</td>
					<td>
						<%=StringUtil.ensureStringNotNull(med.getUseTime())%>
					</td>
					<td>
						<%
						if(med.getAdhes()!=null)
						{
						    if(med.getAdhes().intValue()==1)
						    {
						%>
						规律
						<%
						    }else if(med.getAdhes().intValue()==2)
						    {
						%>
						间断
						<%
						    }else if(med.getAdhes().intValue()==3)
						    {
						%>
						不服药
						<%
						    }
						}
						%>
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
			<div class="sectOper"><img class="sectTog" src="<%=request.getContextPath()%>/resources/img/exam-collapse.png" onclick="sectTog(this, 'conIno');"></div>
		</div>
		<div class="contentPanel" id="conIno" style="display: none;">
			<table class="table tb_view" id="tbIno">
				<tr>
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
						<%=StringUtil.ensureStringNotNull(ino.getInoculateName())%>
					</td>
					<td>
						<%=DateUtil.toHtmlDate(ino.getInoculateTime())%>
					</td>
					<td>
						<%=StringUtil.ensureStringNotNull(ino.getInoculateDept())%>
					</td>
				</tr>
				<%
				    }
				}
				%>
			</table>
		</div>
		<div style="text-align: center;padding: 20px;">
			<a href="javascript:closeIt()" class="easyui-linkbutton">关闭</a>
		</div>
	</form>
</body>
</html>