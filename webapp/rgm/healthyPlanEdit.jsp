<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.util.DateUtil"%>
<%@ page import="com.yzxt.yh.module.rgm.bean.HealthyPlan"%>
<%
	HealthyPlan healthyPlan = (HealthyPlan)request.getAttribute("healthyPlan");
	int planType = healthyPlan.getType() != null ? healthyPlan.getType().intValue() : 0;
	planType = planType > 0 ? planType : 1;
%>
<!DOCTYPE HTML>
<html>
<head>
	<title>健康计划</title>
	<%@ include file="../common/inc.jsp"%>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
	<style type="text/css">
	</style>
<script type="text/javascript">
	function save(){
		var url = "<%=StringUtil.isEmpty(healthyPlan.getId()) ? request.getContextPath() + "/rgm/healthyPlan_add.action" : request.getContextPath() + "/rgm/healthyPlan_update.action"%>";
		$("#healthyPlanForm").form("submit", {
			url : url,
			onSubmit : function() {
				var planNameEle = document.getElementById("planName");
				var planNameVal = $.trim(planNameEle.value);
				if(!planNameVal){
					$.messager.alert("提示信息", "请填写计划名称.", "error");
					return false;
				}
				planNameEle.value = planNameVal;
				var startDateVal = document.getElementById("startDate").value;
				if(!startDateVal){
					$.messager.alert("提示信息", "请填写开始日期.", "error");
					return false;
				}
				var endDateVal = document.getElementById("endDate").value;
				if(!endDateVal){
					$.messager.alert("提示信息", "请填写结束日期.", "error");
					return false;
				}
				if(startDateVal > endDateVal){
					$.messager.alert("提示信息", "开始日期不能大于结束日期.", "error");
					return false;
				}
				var planType = document.getElementById("planType").value;
				if(planType == 1){
					var sbpEle = document.getElementById("sbp");
					var sbpVal = sbpEle.value;
					var sbpVal = $.trim(sbpVal);
					if(!sbpVal){
						$.messager.alert("提示信息", "请填写收缩压.", "error", function(){
							sbpEle.focus();
						});
						return false;
					}else if(!/^[1-9]\d*$/.test(sbpVal)){
						$.messager.alert("提示信息", "收缩压格式不正确，应为正整数.", "error", function(){
							sbpEle.focus();
						});
						return false;
					}
					document.getElementById("targetValue").value = sbpVal;
					var dbpEle = document.getElementById("dbp");
					var dbpVal = dbpEle.value;
					var dbpVal = $.trim(dbpVal);
					if(!dbpVal){
						$.messager.alert("提示信息", "请填写舒张压.", "error", function(){
							dbpEle.focus();
						});
						return false;
					}else if(!/^[1-9]\d*$/.test(dbpVal)){
						$.messager.alert("提示信息", "舒张压格式不正确，应为正整数.", "error", function(){
							dbpEle.focus();
						});
						return false;
					}
					document.getElementById("targetValue2").value = dbpVal;
					document.getElementById("targetValue3").value = "";
				}else if(planType == 2){
					var bloodSugar1Ele = document.getElementById("bloodSugar1");
					var bloodSugar1Val = bloodSugar1Ele.value;
					var bloodSugar1Val = $.trim(bloodSugar1Val);
					var bloodSugar2Ele = document.getElementById("bloodSugar2");
					var bloodSugar2Val = bloodSugar2Ele.value;
					var bloodSugar2Val = $.trim(bloodSugar2Val);
					var bloodSugar3Ele = document.getElementById("bloodSugar3");
					var bloodSugar3Val = bloodSugar3Ele.value;
					var bloodSugar3Val = $.trim(bloodSugar3Val);
					if(!bloodSugar1Val && !bloodSugar2Val && !bloodSugar3Val){
						$.messager.alert("提示信息", "请至少填写一项血糖值.", "error", function(){
							bloodSugar1Ele.focus();
						});
						return false;
					}
					if(bloodSugar1Val && (!/^\d+(\.\d)?$/.test(bloodSugar1Val) || /^0+(\.0)?$/.test(bloodSugar1Val))){
						$.messager.alert("提示信息", "空腹血糖值不正确，应为正数，最多一位小数.", "error", function(){
							bloodSugar1Ele.focus();
						});
						return false;
					}
					
					if(bloodSugar2Val && (!/^\d+(\.\d)?$/.test(bloodSugar2Val) || /^0+(\.0)?$/.test(bloodSugar2Val))){
						$.messager.alert("提示信息", "餐后血糖值不正确，应为正数，最多一位小数.", "error", function(){
							bloodSugar2Ele.focus();
						});
						return false;
					}
					if(bloodSugar3Val && (!/^\d+(\.\d)?$/.test(bloodSugar3Val) || /^0+(\.0)?$/.test(bloodSugar3Val))){
						$.messager.alert("提示信息", "服糖后血糖值不正确，应为正数，最多一位小数.", "error", function(){
							bloodSugar3Ele.focus();
						});
						return false;
					}
					document.getElementById("targetValue").value = bloodSugar1Val;
					document.getElementById("targetValue2").value = bloodSugar2Val;
					document.getElementById("targetValue3").value = bloodSugar3Val;
				}else{
					return false;
				}
				return true;
			},
			success : function(data){
				var data = $.parseJSON(data);
				if (data.state > 0) {
					$.messager.alert("提示信息", "保存成功.", "info", function (){
						try{
							parent.refreshGrid();
						}catch(e){}
						try{
							closeWin();
						}catch(e){}
					});
				} else {
					$.messager.alert("提示信息", data.msg ? data.msg : "保存失败.", "error");
				}
			}
		});
	}
	function closeWin(){
		try{
			parent.closeSubWin();
		}catch(e){}
	}
	function chgType(typeVal){
		if(typeVal == 1){
			document.getElementById("bloodSugar1").value = "";
			document.getElementById("bloodSugar2").value = "";
			document.getElementById("bloodSugar3").value = "";
			document.getElementById("bsArea").style.display = "none";
			document.getElementById("bpArea").style.display = "";
		}else if(typeVal == 2){
			document.getElementById("sbp").value = "";
			document.getElementById("dbp").value = "";
			document.getElementById("bpArea").style.display = "none";
			document.getElementById("bsArea").style.display = "";
		}
	}
</script>
</head>
<body>
	<div style="height: 235px;">
		<form id="healthyPlanForm" action="" method="post" accept-charset="UTF-8">
			<table style="width: 100%;border-spacing: 10px;">
				<tr>
					<td style="width: 25%;" align="right">计划名称：</td>
					<td>
						<input type="text" id="planName" name="healthyPlan.name" style="height: 20px;width: 260px;" maxlength="50" value="<%=StringUtil.ensureStringNotNull(healthyPlan.getName())%>" />
						<input type="hidden" id="planId" name="healthyPlan.id" value="<%=StringUtil.ensureStringNotNull(healthyPlan.getId())%>" />
						<input type="hidden" id="userId" name="healthyPlan.userId" value="<%=healthyPlan.getUserId()%>" />
						<input type="hidden" id="targetValue" name="healthyPlan.targetValue" />
						<input type="hidden" id="targetValue2" name="healthyPlan.targetValue2" />
						<input type="hidden" id="targetValue3" name="healthyPlan.targetValue3" />
					</td>
				</tr>
				<tr>
					<td align="right">计划类型：</td>
					<td>
						<select id="planType" name="healthyPlan.type" style="height: 26px;width: 100px;" onchange="chgType(this.value);">
							<option value="1" <%=planType == 1 ? "selected=\"selected\"" : ""%>>血压</option>
							<option value="2" <%=planType == 2 ? "selected=\"selected\"" : ""%>>血糖</option>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right">开始日期：</td>
					<td>
						<input type="text" id="startDate" name="healthyPlan.startDate" style="height: 20px;width: 96px;" maxlength="10"
							value="<%=DateUtil.toHtmlDate(healthyPlan.getStartDate())%>" class="Wdate" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
					</td>
				<tr>
					<td align="right">结束日期：</td>
					<td>
						<input type="text" id="endDate" name="healthyPlan.endDate" style="height: 20px;width: 96px;" maxlength="10"
							value="<%=DateUtil.toHtmlDate(healthyPlan.getEndDate())%>" class="Wdate" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
					</td>
				</tr>
				<tr id="bpArea" <%=planType != 1 ? "style=\"display: none\"" : ""%>>
					<td align="right">目标值：</td>
					<td>
						<span style="display: inline-block;">收缩压</span>&nbsp;<input type="text" id="sbp" style="height: 20px;width: 40px;" value="<%=planType == 1 ? StringUtil.ensureStringNotNull(healthyPlan.getTargetValue()) : ""%>" maxlength="10" />mmHg
						&nbsp;&nbsp;&nbsp;&nbsp;
						<span style="display: inline-block;">舒张压</span>&nbsp;<input type="text" id="dbp" style="height: 20px;width: 40px;" value="<%=planType == 1 ? StringUtil.ensureStringNotNull(healthyPlan.getTargetValue2()) : ""%>" maxlength="10" />mmHg
					</td>
				</tr>
				<tr id="bsArea" <%=planType != 2 ? "style=\"display: none\"" : ""%>>
					<td align="right">目标值：</td>
					<td>
						<span style="display: inline-block;">空腹血糖</span>&nbsp;<input type="text" id="bloodSugar1" style="height: 20px;width: 40px;" value="<%=planType == 2 ? StringUtil.ensureStringNotNull(healthyPlan.getTargetValue()) : ""%>" maxlength="10" />mmol/L
						&nbsp;&nbsp;&nbsp;&nbsp;
						<span style="display: inline-block;">餐后血糖</span>&nbsp;<input type="text" id="bloodSugar2" style="height: 20px;width: 40px;" value="<%=planType == 2 ? StringUtil.ensureStringNotNull(healthyPlan.getTargetValue2()) : ""%>" maxlength="10" />mmol/L
						<div style="margin-top: 5px;">
						<span style="display: inline-block;">服糖后血糖</span>&nbsp;<input type="text" id="bloodSugar3" style="height: 20px;width: 40px;" value="<%=planType == 2 ? StringUtil.ensureStringNotNull(healthyPlan.getTargetValue3()) : ""%>" maxlength="10" />mmol/L
						</div>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div style="padding: 10px;text-align: center;">
		<a href="javascript: void(0)" onclick="save();" class="easyui-linkbutton">确定</a>
		&nbsp;
		<a href="javascript: void(0)" onclick="closeWin();" class="easyui-linkbutton">关闭</a>
	</div>
</body>
</html>