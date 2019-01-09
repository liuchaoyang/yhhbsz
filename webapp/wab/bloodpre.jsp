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
		var tabInitMap=["initXy", "initMb"];
		var xyGrid,mbGrid;	
		var xyChart,mbChart;
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
			document.getElementById("xyStartDate").onchange=function(){
				$("#ptype").val("2");
			}
			document.getElementById("xyEndDate").onchange=function(){
				$("#ptype").val("2");
			}
			document.getElementById("seleid2").onchange=function(){
				$("#ptype2").val("1");
			}
			document.getElementById("mbStartDate").onchange=function(){
				$("#ptype2").val("2");
			}
			document.getElementById("mbEndDate").onchange=function(){
				$("#ptype2").val("2");
			}
			});
	function initXy(){
   		xyChart = new Highcharts.Chart({
   			chart: {
   				renderTo: "xyContainer", 
   				type: "line"
   			},
   			title: {
				text: "血压数据统计折线图"
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
				min: 20,
				tickInterval: 30,
				offset: -3,
				lineWidth: 1,
				gridLineWidth: 1,
				lineColor: "#C0D0E0",
				title: {
					text: " ",
					style: {
						color: "#6D869F",
						fontSize: "12px",
						fontWeight: "normal"
					}
				},
				plotLines: [{
					value: 140,
					color: "#C2B28A",
	                dashStyle: "solid",
	                width: 1,
	                zIndex: 1,
	                label: {
	                    text: "140",
	                    align: "left",
	                    x: -25,
	                    y: 5,
	                    style:{
	                        color: "#89691E",
	                        display: "none"
	                    }
	                }
	            }, {
	                value: 90,
	                color: "#C2B28A",
	                dashStyle: "solid",
	                width: 1,
	                zIndex: 2,
	                label: {
	                    text: "90",
	                    align: "left",
	                    x: -25,
	                    y: 5,
	                    style:{
	                        color: "#89691E",
	                        display: "none"
						}
	                }
				}, {
	                value: 60,
	                color: "#C2B28A",
	                dashStyle: "solid",
	                width: 1,
	                zIndex: 2,
	                label: {
	                    text: "60",
	                    align: "left",
	                    x: -25,
	                    y: 5,
	                    style:{
	                        color: "#89691E",
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
	                    // text: '正常收缩压',
	                    // align: 'right',
	                    // rotation: 90,
	                    y: 20,
	                    style:{
	                        color: "#C0FFC0"
	                    }
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
	                    // align: "center",
	                    // rotation: 90,
	                    y: 20,
	                    style:{
	                        color: "#89691E"
	                    }
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
				data : []
			}, {
				name : "舒张压",
				data : []
			}]
		});
		xyGrid = $("#xyRecords").datagrid({
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
			url: "<%=request.getContextPath()%>/wab/wabData_queryPressure.action?custId=${custId}",
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
				field: "Diastolic",
				title: "舒张压（mmHg）",
				width: 110,
				align: "center"
			}, {
				field: "Systolic",
				title: "收缩压（mmHg）",
				width: 110,
				align: "center"
			}, {
				field: "Pulse",
				title: "脉搏",
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
						sbps.push(parseInt(rows[i].Diastolic, 10));
						dbps.push(parseInt(rows[i].Systolic, 10));
					}
				}
				// 刷新图表
				xyChart.series[0].setData(sbps);
				xyChart.series[1].setData(dbps);
			}
		});
	}
	function refreshXyGrid(){
		xyGrid.datagrid("reload");
		xyGrid.datagrid("unselectAll");
		xyGrid.datagrid("clearSelections");
	}
	function queryXy(){
		xyGrid.datagrid("getPager").pagination({pageNumber: 1});
		$.extend(xyGrid.datagrid("options"), {
			pageNumber: 1,
			queryParams: $("#xyForm").serializeObject()
		});
		refreshXyGrid();
	}
	function resetXy(){
		document.getElementById("xyForm").reset();
	}
	
	function initMb(){
   		mbChart = new Highcharts.Chart({
   			chart: {
   				renderTo: "mbContainer", 
   				type: "line"
   			},
   			exporting:{
   				enabled:false
			},
			title: {
				text: "脉率走势图"
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
				min: 40,
				max: 120,
				tickInterval: 30,
				offset: -3,
				lineWidth: 1,
				gridLineWidth: 1,
				lineColor: "#C0D0E0",
				title: {
					text: " ",
					style: {
						color: "#6D869F",
						fontSize: "12px",
						fontWeight: 'normal'
					}
				}
			},
			tooltip: {
				enabled: true,
				formatter: function() {
					return "<b>"+ this.series.name +"</b><br/>"+
					this.x +": "+ this.y+"次/分钟";
				}
			},
			legend: {
				layout: "vertical",
				align: "right",
				verticalAlign: "middle",
				borderWidth: 0
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
			series: [{
				name : "脉率",
				data : []
			}]
		});
		mbGrid = $("#mbRecords").datagrid({
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
			url: "<%=request.getContextPath()%>/wab/wabData_queryMaibo.action?custId=${custId}",
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
				field: "Diastolic",
				title: "舒张压（mmHg）",
				width: 110,
				align: "center"
			}, {
				field: "Systolic",
				title: "收缩压（mmHg）",
				width: 110,
				align: "center"
			}, {
				field: "Pulse",
				title: "脉搏",
				width: 110,
				align: "center"
			}]],
			onLoadSuccess: function(data){
				var len = data && data.rows ? data.rows.length : 0;
				var sbps = new Array();
				if(len > 0){
					var rows = data.rows;
					for(var i=len-1; i>-1; i--){
						sbps.push(parseInt(rows[i].Pulse, 10));
					}
				}
				// 刷新图表
				mbChart.series[0].setData(sbps);
			}
		});
	}
	function refreshMbGrid(){
		mbGrid.datagrid("reload");
		mbGrid.datagrid("unselectAll");
		mbGrid.datagrid("clearSelections");
	}
	function queryMb(){
		mbGrid.datagrid("getPager").pagination({pageNumber: 1});
		$.extend(mbGrid.datagrid("options"), {
			pageNumber: 1,
			queryParams: $("#mbForm").serializeObject()
		});
		refreshMbGrid();
	}
	function resetMb(){
		document.getElementById("mbForm").reset();
	}
	</script>
</head>
<body>
	<div class="easyui-tabs" id="tabs" style="margin:2px auto 10px auto;" data-options="width:780">
		<div title="血压" style="padding: 2px">
			<form id="xyForm">
				<table class="table" style="margin-bottom: 2px;">
					<tr>
						<td style="text-align: right;">
							<select id="seleid"  name="seleday" style="width: 80px;">
								<option value="0">最近七天</option>
								<option value="1">最近十五天</option>
								<option value="2">最近三十天</option>
							</select>
							&nbsp;
							检查日期:&nbsp;
							<input class="Wdate" style="width: 100px;" id="xyStartDate" name="xyStartDate" readonly="readonly" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'xyEndDate\')}'})" />
							&nbsp;至&nbsp;
							<input class="Wdate" style="width: 100px;"id="xyEndDate" name="xyEndDate" readonly="readonly" onclick="WdatePicker({minDate:'#F{$dp.$D(\'xyStartDate\')}'})" />
							&nbsp;&nbsp;
							<a href="javascript:queryXy();" class="easyui-linkbutton" >查询</a>
							&nbsp;
							<a href="javascript:resetXy();" class="easyui-linkbutton" >清空</a>
							&nbsp;
							<input type="hidden" id="ptype" name="ptype" value="1" />
						</td>
					</tr>
				</table>
			</form>
			<div id="xyContainer" class="mychart" style="min-width: 310px;height: 250px;margin: 0px auto;"></div>
			<table id="xyRecords"></table>
		</div>
		<div title="脉搏" style="padding: 2px">
			<form id="mbForm">
				<table class="table" style="margin-bottom: 2px;">
					<tr>
						<td style="text-align: right;">
							<select id="seleid2"  name="seleday2" style="width: 80px;">
								<option value="0">最近七天</option>
								<option value="1">最近十五天</option>
								<option value="2">最近三十天</option>
							</select>
							检查日期:&nbsp;
							<input class="Wdate" style="width: 100px;" id="mbStartDate" name="mbStartDate" readonly="readonly" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'mbEndDate\')}'})" />
							&nbsp;至&nbsp;
							<input class="Wdate" style="width: 100px;"id="mbEndDate" name="mbEndDate" readonly="readonly" onclick="WdatePicker({minDate:'#F{$dp.$D(\'mbStartDate\')}'})" />
							&nbsp;&nbsp;
							<a href="javascript:queryMb();" class="easyui-linkbutton" >查询</a>
							&nbsp;
							<a href="javascript:resetMb();" class="easyui-linkbutton" >清空</a>
							&nbsp;
							<input type="hidden" id="ptype2" name="ptype2" value="1" />
						</td>
					</tr>
				</table>
			</form>
			<div id="mbContainer" class="mychart" style="min-width: 310px;height: 250px;margin: 0px auto;"></div>
			<table id="mbRecords"></table>
		</div>
	</div>
</body>
</html>