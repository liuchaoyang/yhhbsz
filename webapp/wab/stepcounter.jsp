<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import ="java.util.Date"%>
<%@ page import ="com.yzxt.yh.util.DateUtil"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>健康数据</title>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/highcharts.js"></script>
	<style type="text/css">
	form{
		margin: 0px;
		padding: 0px;
	}
	</style>
	<script type="text/javascript">
		var tabInitMap=["initJb"];
		var jbGrid;	
		var jbChart;
		$(function(){
			$("#tabs").tabs({
				onSelect: function(title, idx){
					if(!tabInitMap[idx]){
						return;
					}else{
						funcName = tabInitMap[idx];
						tabInitMap[idx] = null;
						eval(funcName+"();");
					}
				}
			});
			if(!tabInitMap[0]){
				return;
			}else{
				funcName = tabInitMap[0];
				tabInitMap[0] = null;
				eval(funcName+"();");
			}
			document.getElementById("seleid").onchange=function(){
				$("#ptype").val("1");
			}
			document.getElementById("jbStartDate").onchange=function(){
				$("#ptype").val("2");
			}
			document.getElementById("jbEndDate").onchange=function(){
				$("#ptype").val("2");
			}
			});
	function initJb(){
   		jbChart = new Highcharts.Chart({
   			chart: {
				renderTo: "jbContainer", 
				type: "line"
   			},
   			title: {
				text: "计步走势图"
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
				tickInterval: 5000,
				offset: -3,
				lineWidth: 1,
				gridLineWidth: 1,
				lineColor: "#C0D0E0",
				title: {
					text: " ",
					style: {
						color: "#6d869f",
						fontSize: "12px",
						fontWeight: "normal"
					}
				}
			},
			legend: {
				layout: "vertical",
				align: "right",
				verticalAlign: "middle",
				borderWidth: 0
			},
			exporting:{
				enabled: false
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
				name: "步数",
				data: []
			}, {
				name: "距离",
				data: []
			}, {
				name: "卡路里",
				data: []
			}]
		});
		jbGrid = $("#jbRecords").datagrid({
			width: "auto",
			height: 310,
			idField: "id",
			rownumbers: true,
			pagination: true,
			pageList: [10, 20, 50, 100],
			singleSelect: true,
			nowrap: true,
			striped: true,
			fitColumns: true,
			loadMsg: "数据加载中,请稍候...",
			url: "<%=request.getContextPath()%>/wab/wabData_querystep.action?custId=${custId}",
			columns: [[{
				field: "MeasureDate",
				title: "检测时间",
				width: 140,
				align: "center",
				formatter: function(val){
					if(val){
 						return val.substring(0,19);
					}
					return val;
 				}
			}, {
				field: "StepCount",
				title: "步数",
				width: 110,
				align: "center"
			}, {
				field: "Distance",
				title: "距离",
				width: 110,
				align: "center"
			}, {
				field: "Calories",
				title: "卡路里",
				width: 110,
				align: "center"
			}]],
			onLoadSuccess: function(data){
				var len = data && data.rows ? data.rows.length : 0;
				var sbps = new Array();
				var dbps = new Array();
				var abps = new Array();
				if(len > 0){
					var rows = data.rows;
					for(var i=len-1; i>-1; i--){
						sbps.push(parseInt(rows[i].StepCount, 10));
						dbps.push(parseInt(rows[i].Distance, 10));
						abps.push(parseInt(rows[i].Calories, 10));
					}
				}
				// 刷新图表
				jbChart.series[0].setData(sbps);
				jbChart.series[1].setData(dbps);
				jbChart.series[2].setData(abps);
			}
		});
	}
	function refreshJbGrid(){
		jbGrid.datagrid("reload");
		jbGrid.datagrid("unselectAll");
		jbGrid.datagrid("clearSelections");
	}
	function queryJb(){
		jbGrid.datagrid("getPager").pagination({pageNumber: 1});
		$.extend(jbGrid.datagrid("options"), {
			pageNumber: 1,
			queryParams: $("#jbForm").serializeObject()
		});
		refreshJbGrid();
	}
	function resetJb(){
		document.getElementById("jbForm").reset();
	}
	
	</script>
</head>
<body>
	<div class="easyui-tabs" id="tabs" style="margin:2px auto 10px auto;" data-options="width:780">
		<div title="计步" style="padding: 2px;">
			<form id="jbForm">
				<table class="table" style="margin-bottom: 2px;">
					<tr>
						<td style="text-align: right;">
							<select id="seleid"  name="seleday" style="width:100px;">
								<option value="0">最近七天</option>
								<option value="1">最近十五天</option>
								<option value="2">最近三十天</option>
							</select>
							&nbsp;
							检查日期:&nbsp;
							<input class="Wdate" style="width: 100px;" id="jbStartDate" name="jbStartDate" readonly="readonly" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'jbEndDate\')}'})" />
							&nbsp;至&nbsp;
							<input class="Wdate" style="width: 100px;"id="jbEndDate" name="jbEndDate" readonly="readonly" onclick="WdatePicker({minDate:'#F{$dp.$D(\'jbStartDate\')}'})" />
							&nbsp;&nbsp;
							<a href="javascript:queryJb();" class="easyui-linkbutton" >查询</a>
							&nbsp;
							<a href="javascript:resetJb();" class="easyui-linkbutton" >清空</a>
							&nbsp;
							<input type="hidden" id="ptype" name="ptype" value="1" />
						</td>
					</tr>
				</table>
			</form>
			<div id="jbContainer" class="mychart" style="min-width: 310px;height: 250px;margin: 0px auto;"></div>
			<table id="jbRecords"></table>
		</div>
	</div>
</body>
</html>