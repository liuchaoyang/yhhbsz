<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="com.yzxt.yh.module.rgm.bean.HealthyPlan"%>
<% 
	HealthyPlan healthyPlan = (HealthyPlan)request.getAttribute("healthyPlan");
%>
<!DOCTYPE html>
<html>
<head>
	<title>查看血糖计划</title>
	<%@ include file="../common/inc.jsp"%>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/css/flow.css" />
	<script src="<%=request.getContextPath()%>/resources/js/highcharts.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/modules/exporting.js"></script>
<style type="text/css">
html{
	padding: 0px;
	margin: 0px;
}
body{
	padding: 0px;
	margin: 0px;
}
</style>
<script type="text/javascript">
	// 血糖
   	var xtGrid;
	var xtChart;
	$(function(){
   		xtChart = new Highcharts.Chart({
   			chart: {
   				renderTo: "xtLink", 
   				type: "line"
   			},
   			title: {
				text: "血糖走势图"
			},
			xAxis: {
				lineWidth: 1,
				tickPixelInterval: 140,
				type: "linear",
				tickLength : 20, //横坐标各个名称间的竖线，例如Jan 和Feb间的竖线
				title: {
					style:{
						display: "none"
					}
				},
				labels: {
					enabled: false
				}
			},
			yAxis: {
				allowDecimals: false,
				min: 0,
				tickInterval: 5,
				offset: -3,
				lineWidth: 1,
				gridLineWidth: 1,
				lineColor: "#C0D0E0",
				title: {
					text: " mmol/L",
					style: {
						color: "#6d869f",
						fontSize: "12px",
						fontWeight: "normal"
					}
				},
				plotLines: [
					/* <c:if test="${not empty healthyPlan.targetValue}"> */
					{
						value: <%=healthyPlan.getTargetValue()%>,
						color: "#2F7ED8",
						dashStyle: "solid",
						width: 2,
						zIndex: 2
					},
					/* </c:if>
					<c:if test="${not empty healthyPlan.targetValue2}"> */
					{
						value: <%=healthyPlan.getTargetValue2()%>,
						color: "#8BBC21",
						dashStyle: "solid",
						width: 2,
						zIndex: 2
					},
					/* </c:if> */
					/* <c:if test="${not empty healthyPlan.targetValue3}"> */
					{
						value: <%=healthyPlan.getTargetValue3()%>,
						color: "#D8B378",
						dashStyle: "solid",
						width: 2,
						zIndex: 2
					}
					/* </c:if> */
				]
			},
			legend: {
				layout: "vertical",
				align: "right",
				verticalAlign: "middle",
				borderWidth: 0
			},
			exporting:{
				// 是否允许导出
				enabled:false
			},
			plotOptions: {
				line: {
					dataLabels: {
						enabled: true
					},
					enableMouseTracking: false
				},
				series: {
					connectNulls: true
				}
			},
			series: [{
				name : "空腹血糖",
				data : [],
				color: "#2F7ED8"
			}, {
				name : "餐后血糖",
				data : [],
				color: "#8BBC21"
			}, {
				name : "服糖2小时血糖",
				data : [],
				color: "#D8B378"
			}]
		});
		// 表格
   		xtGrid = $("#xtRecord").datagrid({
   			loadMsg : '数据加载中,请稍候...',
 			iconCls : 'icon-redo',
 			width : 'auto',
			height : 240,
			url: "<%=request.getContextPath()%>/chk/checkData_queryBloodSugar.action?custId=<%=healthyPlan.getUserId()%>"
					+ "&examType=2&xtStartDate=<fmt:formatDate value="<%=healthyPlan.getStartDate()%>" />&xtEndDate=<fmt:formatDate value="<%=healthyPlan.getEndDate()%>"  />",
			animate : true,
			nowrap : true,
			striped : true,
			fitColumns:true,
			singleSelect : true,
			pagination : true,
 			idField : 'id',
 			columns:[[{
 				field:'checkTime',
 				title:'检测时间',
 				width:150,
 				align:'center',
 				formatter:function(value){
 					if(value!=null && value!=''){
 						return value.substring(0,19);
 					}
 					return value;
 				}
 			}, {
 				field: "b_g_type",
 				title: "血糖类型",
 				width: 150,
 				align: "center",
 				formatter:function(stype, row){
					var mtype = row.b_g_type;
					var t2meal = row.timeToMeal;
					var unit = row.unit;
					var meal;
					if(stype==1 || stype==2){
						return "空腹血糖";
					}else if(stype==3){
						return "餐后" + t2meal + "小时血糖";
					}else if(stype==4){
						return "服糖后2小时血糖";
					}else{
						return "";
					}
 				}
 			},  {
				field: "b_glucose",
				title: "血糖",
				width: 220,
				align: "center",
				formatter: function(val, row){
					var sType = row.b_g_type;
					if(sType==1){
						return "空腹血糖值：" + val + "mmol/L";
					}else if(sType==2){
						return "餐前血糖值：" + val + "mmol/L";
					}else if(sType==3){
						return "餐后血糖值：" + val + "mmol/L";
					}else if(sType==4){
						return "服糖后2小时血糖值：" + val + "mmol/L";
					}else{
						return "";
					}
				}
 			}]],
			toolbar: "#xt-toolbar",
			onLoadSuccess: function(data){
				var bss = [new Array(), new Array(), new Array(), new Array()];
				var len = data && data.rows ? data.rows.length : 0;
				if(len > 0){
					var rows = data.rows;
					for(var i=len-1; i>-1; i--){
						var bloodSugarType = parseInt(data.rows[i].b_g_type, 10);
						if(bloodSugarType==1 || bloodSugarType==2){
							bss[0].push(parseFloat(data.rows[i].b_glucose, 10));
							bss[1].push(null);
							bss[2].push(null);
						}else if(bloodSugarType==3){
							bss[0].push(null);
							bss[1].push(parseFloat(data.rows[i].b_glucose, 10));
							bss[2].push(null);
						}else if(bloodSugarType==4){
							bss[0].push(null);
							bss[1].push(null);
							bss[2].push(parseFloat(data.rows[i].b_glucose, 10));
						}
					}
				}
				// 刷新图表
				xtChart.series[0].setData(bss[0]);
				xtChart.series[1].setData(bss[1]);
				xtChart.series[2].setData(bss[2]);
			}
   		});
		var p = xtGrid.datagrid("getPager");
		if(p){
			$(p).pagination({
				pageNumber : 1,
				pageSize : 10,
				showPageList:false
			});
		}
	});
</script>
</head>
<body>
	<div>
		<table id="xtRecord"></table>
		<div id="xtLink" class="mychart" style="height: 250px;margin: 0 auto;"></div>
	</div>
</body>
</html>
