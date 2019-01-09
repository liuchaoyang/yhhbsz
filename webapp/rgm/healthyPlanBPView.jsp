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
	<title>查看血压计划</title>
	<%@ include file="../common/inc.jsp"%>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/css/flow.css" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/highcharts.js"></script>
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
	// 血压
   	var xyGrid;
	var xyChart;
	$(function(){
		xyChart = new Highcharts.Chart({
   			chart: {
   				renderTo: "xyLink", 
   				type: "line"
   			},
   			title: {
				text: "血压走势图"
			},
			xAxis: {
				lineWidth: 1,
				tickPixelInterval: 140,
				type: 'linear',
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
				min: 20,
				tickInterval: 30,
				offset: -3,
				lineWidth: 1,
				gridLineWidth: 1,
				lineColor: "#c0d0e0",
				title: {
					text: "毫米汞柱(mmHg)",
					style: {
						color: "#6d869f",
						fontSize: "12px",
						fontWeight: "normal"
					}
				},
				plotLines: [{
	                value: <%=healthyPlan.getTargetValue()%>,
	                color: "#2F7ED8",
	                dashStyle: "solid",
	                width: 2,
	                zIndex: 2,
	                label: {
	                    text: "140",
	                    align: "left",
	                    x: -25,
	                    y: 5,
	                    style:{
	                        color: "#2F7ED8",
	                        display: "none"
						}
	                }
				}, {
	                value: <%=healthyPlan.getTargetValue2()%>,
	                color: "#D8B378",
	                dashStyle: "solid",
	                width: 2,
	                zIndex: 2,
	                label: {
	                    text: "60",
	                    align: "left",
	                    x: -25,
	                    y: 5,
	                    style:{
	                        color: "#0000FF",
	                        display: "none"
						}
	                }
				}],
				plotBands: [{
	                from: 90,
	                to: 140,
	                zIndex: 0,
	                color: "#ECFFA8",
	                style: {
	                opacity: 0.5
	                },
	                label: {
	                    y: 20
	                }
				},{
	                from: 60,
	                to: 90,
	                zIndex: 0,
	                color: "#C0FFC0",
	                style: {
	                	opacity:0.5
	                },
	                label: {
	                    text: "",
	                    // align: 'center',
	                    // rotation: 90,
	                    y: 20
	                }
				}
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
					point: {
						events: {
							click: function() {
							}
						}
					},
					enableMouseTracking: false
				}
			},
			tooltip :{
				formatter : function(){
					return this.series.name + ": " +this.y +" mmHg";
				}
			},
			series: [{
				name : "收缩压",
				data : [],
				color: "#2F7ED8"
			}, {
				name : "舒张压",
				data : [],
				color: "#D8B378"
			}]
		});
		// 表格
		xyGrid = $("#xyRecord").datagrid({
			loadMsg : "数据加载中,请稍候...",
			iconCls : "icon-redo",
			width : "auto",
			height : 240,
			url : "<%=request.getContextPath()%>/chk/checkData_queryPrepulse.action?custId=<%=healthyPlan.getUserId()%>"
					+ "&examType=1&xyStartDate=<fmt:formatDate value="<%=healthyPlan.getStartDate()%>" />&xyEndDate=<fmt:formatDate value="<%=healthyPlan.getEndDate()%>"  />",
			animate : true,
			nowrap : true,
			striped : true,
			fitColumns:true,
			singleSelect : true,
			pagination : true,
			idField : "id",
			// rownumbers : true,
			columns:[[
			{
				field : "checkTime",
				title : "检测时间",
				width : 150,
				align : "center",
				formatter:function(value){
					if(value != null && value != ""){
						return value.substring(0, 19);
					}
					return value;
				}
			}, {
					field: "d_b_p",
					title: "舒张压（mmHg）",
					width: 110,
					align: "center"
			}, {
					field: "s_b_p",
					title: "收缩压（mmHg）",
					width: 110,
					align: "center"
				}]],
			onLoadSuccess: function(data){
				var len = data && data.rows ? data.rows.length : 0;
				var sbps = new Array();
				var dbps = new Array();
				if(len > 0){
					var rows = data.rows;
					for(var i=len-1; i>-1; i--){
						sbps.push(parseInt(rows[i].s_b_p, 10));
						dbps.push(parseInt(rows[i].d_b_p, 10));
					}
				}
				// 刷新图表
				xyChart.series[0].setData(sbps);
				xyChart.series[1].setData(dbps);
			}
		});
		var p = xyGrid.datagrid("getPager");
		if (p){
			$(p).pagination({
				pageNumber : 1,
				pageSize : 10,
				showPageList : false
			});
		}
	});
</script>
</head>
<body>
	<div>
		<table id="xyRecord"></table>
		<div id="xyLink" class="mychart" style="height: 250px;margin: 0 auto;"></div>
	</div>
</body>
</html>
