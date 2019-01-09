<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import ="java.util.Date"%>
<%@ page import ="com.yzxt.yh.util.DateUtil"%>
<%
Date now = new Date();
String startDateStr = DateUtil.toHtmlDate(DateUtil.addDay(now, -30));
String endDateStr = DateUtil.toHtmlDate(now);
String custId = (String)request.getAttribute("custId");
String id = (String)request.getAttribute("id");
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>检测记录查询</title>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/highcharts.js"></script>
	<style type="text/css">
	form{
		margin: 0px;
		padding: 0px;
	}
	</style>
	<script type="text/javascript">
	<%--TAB的名称，是否初始化--%>
	var tabInitMap = ["initXy", "initXt", "initXl", "initXo", "initTw", "initDgc", "initNs", "initNsfx", "initTz","initXd"];
	var xyGrid, xtGrid, xlGrid, xoGrid, twGrid, dgcGrid, nsGrid, nsfxGrid, tzGrid,xdGrid;
	var xyChart, xtChart;
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
	});
	function initXy(){
   		xyChart = new Highcharts.Chart({
   			chart: {
   				renderTo: "xyContainer", 
   				type: "line"
   			},
   			title: {
				text: "血压走势图"
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
					text: "毫米汞柱(mmHg)",
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
			url: "<%=request.getContextPath()%>/chk/checkData_queryPrepulse.action?custId=${custId}",
			frozenColumns: [[{
				field: "ck",
				checkbox: true
			}]],
			columns: [[{
				field: "checkTime",
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
				field: "d_b_p",
				title: "舒张压（mmHg）",
				width: 110,
				align: "center"
			}, {
				field: "s_b_p",
				title: "收缩压（mmHg）",
				width: 110,
				align: "center"
			}, {
				field: "pulse",
				title: "脉率",
				width: 110,
				align: "center"
			}, {
				field: "descript",
				title: "风险描述",
				width: 250,
				align: "left",
				formatter: function(val, row, idx){
					if(val && row.level>0){
						return "<label title=\"" + val + "\" style=\"color: #FF0000;\">" + val + "</label>";
					}
					return "";
				}
				
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
	
	function initXt(){
		xtChart = new Highcharts.Chart({
   			chart: {
				renderTo: "xtContainer", 
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
				name: "空腹血糖",
				data: []
			}, {
				name: "餐后血糖",
				data: []
			}, {
				name: "服糖2小时血糖",
				data: []
			}]
		});
		xtGrid = $("#xtRecords").datagrid({
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
			url: "<%=request.getContextPath()%>/chk/checkData_queryBloodSugar.action?custId=<%=custId%>",
			columns: [[{
				field: "checkTime",
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
 			}, {
				field: "warningReason",
				title: "风险描述",
				width: 250,
				align: "left",
				formatter: function(val, row, idx){
					if(val && row.level>0){
						return "<label title=\"" + val + "\" style=\"color: #FF0000;\">" + val + "</label>";
					}
					return "";
				}
			}]],
			onLoadSuccess: function(data){
				var bss = [new Array(), new Array(), new Array(), new Array()];
				var len = data && data.rows ? data.rows.length : 0;
				var sbps = new Array();
				var dbps = new Array();
				if(len > 0){
					var rows = data.rows;
					for(var i=len-1; i>-1; i--){
						var bloodSugarType = parseInt(data.rows[i].b_g_type, 10);
						if(bloodSugarType==1 || bloodSugarType ==2){
							bss[0].push(parseFloat(data.rows[i].b_g_type, 10));
							bss[1].push(null);
							bss[2].push(null);
						}else if(bloodSugarType ==3){
							bss[0].push(null);
							bss[1].push(parseFloat(data.rows[i].b_g_type, 10));
							bss[2].push(null);
						}else if(bloodSugarType ==4){
							bss[0].push(null);
							bss[1].push(null);
							bss[2].push(parseFloat(data.rows[i].b_g_type, 10));
						}
					}
				}
				// 刷新图表
				xtChart.series[0].setData(bss[0]);
				xtChart.series[1].setData(bss[1]);
				xtChart.series[2].setData(bss[2]);
			}
		});
	}
	function refreshXtGrid(){
		xtGrid.datagrid("reload");
		xtGrid.datagrid("unselectAll");
		xtGrid.datagrid("clearSelections");
	}
	function queryXt(){
		xtGrid.datagrid("getPager").pagination({pageNumber: 1});
		$.extend(xtGrid.datagrid("options"), {
			pageNumber: 1,
			queryParams: $("#xtForm").serializeObject()
		});
		refreshXtGrid();
	}
	function resetXt(){
		document.getElementById("xtForm").reset();
	}
	
	function initXl(){
   		xlChart = new Highcharts.Chart({
   			chart: {
   				renderTo: "xlContainer", 
   				type: "line"
   			},
   			exporting:{
   				enabled:false
			},
			title: {
				text: "心(脉)率走势图"
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
					text: "次/分钟",
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
				name : "心(脉)率",
				data : []
			}]
		});
		xlGrid = $("#xlRecords").datagrid({
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
			url: "<%=request.getContextPath()%>/chk/checkData_queryPulse.action?custId=<%=custId%>",
			columns: [[{
				field: "checkTime",
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
				field: "pulse",
				title: "心(脉)率",
				width: 140,
				align: "center"
 			}, {
				field: "pulseType",
				title: "类型",
				width: 140,
				align: "center",
				formatter: function(val){
					if(val==1){
						return "心率";
					}else if(val==2){
						return "脉率";
					}
					return "";
				}
 			}, {
				field: "descript",
				title: "风险描述",
				width: 250,
				align: "left",
				formatter: function(val, row, idx){
					if(val && row.level>0){
						return "<label title=\"" + val + "\" style=\"color: #FF0000;\">" + val + "</label>";
					}
					return "";
				}
			}]],
			onLoadSuccess: function(data){
				var len = data && data.rows ? data.rows.length : 0;
				var xls = new Array();
				if(len > 0){
					var rows = data.rows;
					for(var i=len-1; i>-1; i--){
						xls.push(parseInt(rows[i].pulse, 10));
					}
				}
				// 刷新图表
				xlChart.series[0].setData(xls);
			}
		});
	}
	function refreshXlGrid(){
		xlGrid.datagrid("reload");
		xlGrid.datagrid("unselectAll");
		xlGrid.datagrid("clearSelections");
	}
	function queryXl(){
		xlGrid.datagrid("getPager").pagination({pageNumber: 1});
		$.extend(xlGrid.datagrid("options"), {
			pageNumber: 1,
			queryParams: $("#xlForm").serializeObject()
		});
		refreshXlGrid();
	}
	function resetXl(){
		document.getElementById("xlForm").reset();
	}
	
	function initXo(){
   		xoChart = new Highcharts.Chart({
   			chart: {
   				renderTo: "xoContainer", 
   				type: "line"
   			},
   			title: {
   				text: "血氧走势图"
   			},
   			exporting:{
   				enabled:false
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
				tickInterval: 20,
				offset: -3,
				lineWidth: 1,
				lineColor: "#C0D0E0",
				title: {
					text: "血氧（%）",
					style: {
						color: "#6D869F",
						fontSize: "12px",
						fontWeight: "normal"
					}
				}
			},
			tooltip: {
				enabled: true,
				xDateFormat: "%Y-%m-%d %H:%M:%S",
				formatter: function() {
					return "<b>"+this.series.name+"</b><br/>"+
						this.x+": "+this.y+"%";
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
					enableMouseTracking: false
				}
			},
			series: [{
				name : "血氧",
				data : []
			}]
		});
		xoGrid = $("#xoRecords").datagrid({
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
			url: "<%=request.getContextPath()%>/chk/checkData_queryBloodOxygen.action?custId=<%=custId%>",
			columns: [[{
				field: "checkTime",
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
				field: "b_o",
				title: "血氧",
				width: 140,
				align: "center"
 			}, {
				field: "descript",
				title: "风险描述",
				width: 250,
				align: "left",
				formatter: function(val, row, idx){
					if(val && row.level>0){
						return "<label title=\"" + val + "\" style=\"color: #FF0000;\">" + val + "</label>";
					}
					return "";
				}
			}]],
			onLoadSuccess: function(data){
				var len = data && data.rows ? data.rows.length : 0;
				var xos = new Array();
				if(len > 0){
					var rows = data.rows;
					for(var i=len-1; i>-1; i--){
						xos.push(parseInt(rows[i].b_o, 10));
					}
				}
				// 刷新图表
				xoChart.series[0].setData(xos);
			}
		});
	}
	function refreshXoGrid(){
		xoGrid.datagrid("reload");
		xoGrid.datagrid("unselectAll");
		xoGrid.datagrid("clearSelections");
	}
	function queryXo(){
		xoGrid.datagrid("getPager").pagination({pageNumber: 1});
		$.extend(xoGrid.datagrid("options"), {
			pageNumber: 1,
			queryParams: $("#xoForm").serializeObject()
		});
		refreshXoGrid();
	}
	function resetXo(){
		document.getElementById("xoForm").reset();
	}
	
	function initTw(){
   		twChart = new Highcharts.Chart({
   			chart: {
   				renderTo: "twContainer", 
   				type: "line"
   	   		},
   	   		exporting:{
   	   			enabled:false
   	   		},
   	   		title: {
   	   			text: "体温走势图"
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
   	   			allowDecimals: true,
   	   			min: 30.0,
   	   			max: 49.0,
   	   			tickInterval: 2,
   	   			offset: -3,
   	   			lineWidth: 1,
   	   			lineColor: "#C0D0E0",
   	   			title: {
   	   				text: "体温(℃)",
   	   				style: {
   	   					color: "#6D869F",
   	   					fontSize: "12px",
   	   					fontWeight: "normal"
   	   				}
   	   			}
   	   		},
   	   		tooltip: {
   	   			enabled: true,
   	   			xDateFormat: "%Y-%m-%d %H:%M:%S",
   	   			formatter: function() {
   	   				return "<b>"+this.series.name+"</b><br/>"+
   	   					this.x+": "+this.y+"℃";
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
   	   			name : "体温",
   	   			data : []
   	   		}]
   	   	});
		twGrid = $("#twRecords").datagrid({
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
			url: "<%=request.getContextPath()%>/chk/checkData_queryTemperature.action?custId=<%=custId%>",
			columns: [[{
				field: "checkTime",
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
				field: "tempe",
				title: "体温",
				width: 140,
				align: "center"
 			}, {
				field: "descript",
				title: "风险描述",
				width: 250,
				align: "left",
				formatter: function(val, row, idx){
					if(val && row.level>0){
						return "<label title=\"" + val + "\" style=\"color: #FF0000;\">" + val + "</label>";
					}
					return "";
				}
			}]],
			onLoadSuccess: function(data){
				var len = data && data.rows ? data.rows.length : 0;
				var tws = new Array();
				if(len > 0){
					var rows = data.rows;
					for(var i=len-1; i>-1; i--){
						tws.push(parseFloat(rows[i].tempe, 10));
					}
				}
				// 刷新图表
				twChart.series[0].setData(tws);
			}
		});
	}
	function refreshTwGrid(){
		twGrid.datagrid("reload");
		twGrid.datagrid("unselectAll");
		twGrid.datagrid("clearSelections");
	}
	function queryTw(){
		twGrid.datagrid("getPager").pagination({pageNumber: 1});
		$.extend(twGrid.datagrid("options"), {
			pageNumber: 1,
			queryParams: $("#twForm").serializeObject()
		});
		refreshTwGrid();
	}
	function resetTw(){
		document.getElementById("twForm").reset();
	}
	
	function initDgc(){
   		dgcChart = new Highcharts.Chart({
			chart: {
				renderTo: "dgcContainer", 
				type: "line"
			},
			exporting: {
				enabled:false
			},
			title: {
				text: "总胆固醇走势图"
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
				title: {
					text: "mmol/L"
				},
				style: {
					color: "#6D869F",
					fontSize: "12px",
					fontWeight: "normal"
				}
			},
			tooltip: {
				enabled: true,
				formatter: function() {
					return "<b>"+this.series.name+"</b><br/>"+
						this.x+": "+this.y;
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
				name: "总胆固醇",
				data: []
			}]
   		});
		dgcGrid = $("#dgcRecords").datagrid({
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
			url: "<%=request.getContextPath()%>/chk/checkData_queryTotalCholesterol.action?custId=<%=custId%>",
			columns: [[{
				field: "checkTime",
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
				field: "tChol",
				title: "总胆固醇（mmol/L）",
				width: 140,
				align: "center"
 			}, {
				field: "descript",
				title: "风险描述",
				width: 250,
				align: "left",
				formatter: function(val, row, idx){
					if(val && row.level>0){
						return "<label title=\"" + val + "\" style=\"color: #FF0000;\">" + val + "</label>";
					}
					return "";
				}
			}]],
			onLoadSuccess: function(data){
				var len = data && data.rows ? data.rows.length : 0;
				var dgcs = new Array();
				if(len > 0){
					var rows = data.rows;
					for(var i=len-1; i>-1; i--){
						dgcs.push(parseFloat(rows[i].tChol, 10));
					}
				}
				// 刷新图表
				dgcChart.series[0].setData(dgcs);
			}
		});
	}
	function refreshDgcGrid(){
		dgcGrid.datagrid("reload");
		dgcGrid.datagrid("unselectAll");
		dgcGrid.datagrid("clearSelections");
	}
	function queryDgc(){
		dgcGrid.datagrid("getPager").pagination({pageNumber: 1});
		$.extend(dgcGrid.datagrid("options"), {
			pageNumber: 1,
			queryParams: $("#dgcForm").serializeObject()
		});
		refreshDgcGrid();
	}
	function resetDgc(){
		document.getElementById("dgcForm").reset();
	}
	
	function initNs(){
		nsChart = new Highcharts.Chart({
			chart: {
				renderTo: "nsContainer",
				type: "line"
			},
			exporting:{
				enabled: false
			},
			title: {
				text: "尿酸走势图"
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
				title: {
                    text: "umol/L",
                    style: {
                        color: "#6D869F",
                        fontSize: "12px",
                        fontWeight: "normal"

                    }
                }
            },
			tooltip: {
                enabled: true,
                formatter: function() {
                    return "<b>"+this.series.name+"</b><br/>"+
                        this.x+": "+this.y+"umol/L";
                }
			},
            legend: {
                layout: "vertical",
                align: 'right',
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
				name: "尿酸",
				data: []
			}]
		});
		nsGrid = $("#nsRecords").datagrid({
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
			url: "<%=request.getContextPath()%>/chk/checkData_queryUricAcid.action?custId=<%=custId%>",
			columns: [[{
				field: "checkTime",
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
				field: "uricAcid",
				title: "尿酸（umol/L）",
				width: 140,
				align: "center"
 			}, {
				field: "descript",
				title: "风险描述",
				width: 250,
				align: "left",
				formatter: function(val, row, idx){
					if(val && row.level>0){
						return "<label title=\"" + val + "\" style=\"color: #FF0000;\">" + val + "</label>";
					}
					return "";
				}
			}]],
			onLoadSuccess: function(data){
				var len = data && data.rows ? data.rows.length : 0;
				var nss = new Array();
				if(len > 0){
					var rows = data.rows;
					for(var i=len-1; i>-1; i--){
						nss.push(parseFloat(rows[i].uricAcid, 10));
					}
				}
				// 刷新图表
				nsChart.series[0].setData(nss);
			}
		});
	}
	function refreshNsGrid(){
		nsGrid.datagrid("reload");
		nsGrid.datagrid("unselectAll");
		nsGrid.datagrid("clearSelections");
	}
	function queryNs(){
		nsGrid.datagrid("getPager").pagination({pageNumber: 1});
		$.extend(nsGrid.datagrid("options"), {
			pageNumber: 1,
			queryParams: $("#nsForm").serializeObject()
		});
		refreshNsGrid();
	}
	function resetNs(){
		document.getElementById("nsForm").reset();
	}
	
	function initNsfx(){
		nsfxGrid = $("#nsfxRecords").datagrid({
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
			url: "<%=request.getContextPath()%>/chk/checkData_queryAnaUA.action?custId=<%=custId%>",
			columns:[[{
				field: "checkTime",
				title: "检测时间",
				width: 130,
				align: "center",
				formatter: function(val){
					if(val){
						return val.substring(0,19);
					}
					return "";
				}
			}, {
				field: "leu",
				title: "尿白细胞",
				width: 55,
				align: "center"
			}, {
				field: "nit",
				title: "亚硝酸盐",
				width: 55,
				align: "center"
			}, {
				field: "pro",
				title: "尿蛋白",
				width: 45,
				align: "center"
			}, {
				field: "glu",
				title: "葡萄糖",
				width: 45,
				align: "center"
			}, {
				field: "ket",
				title: "酮体",
				width: 35,
				align: "center"
			}, {
				field: "ubg",
				title: "尿胆原",
				width: 45,
				align: "center"
			}, {
				field: "bil",
				title: "胆红素",
				width: 45,
				align: "center"
			}, {
				field: "ph",
				title: "PH值",
				width: 35,
				align: "center"
			}, {
				field: "sg",
				title: "比重",
				width: 35,
				align: "center"
			}, {
				field: "bld",
				title: "隐血",
				width: 35,
				align: "center"
			}, {
				field: "vc",
				title: "维生素C",
				width: 55,
				align: "center"
			}, {
				field: "descript",
				title: "风险描述",
				width: 80,
				align: "left",
				formatter: function(val, row, idx){
					if(val && row.level>0){
						return "<label title=\"" + val + "\" style=\"color: #FF0000;\">" + val + "</label>";
					}
					return "";
				}
			}]]
		});
	}
	function refreshNsfxGrid(){
		nsfxGrid.datagrid("reload");
		nsfxGrid.datagrid("unselectAll");
		nsfxGrid.datagrid("clearSelections");
	}
	function queryNsfx(){
		nsfxGrid.datagrid("getPager").pagination({pageNumber: 1});
		$.extend(nsfxGrid.datagrid("options"), {
			pageNumber: 1,
			queryParams: $("#nsfxForm").serializeObject()
		});
		refreshNsfxGrid();
	}
	function resetNsfx(){
		document.getElementById("nsfxForm").reset();
	}
	function initXd(){
		xdGrid = $("#xdRecords").datagrid({
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
			url: "<%=request.getContextPath()%>/chk/checkData_queryPulse.action?custId=<%=custId%>",
			columns:[[{
				field: "checkTime",
				title: "检测时间",
				width: 130,
				align: "center",
				formatter: function(val){
					if(val){
						return val.substring(0,19);
					}
					return "";
				}
			}, {
				field: "pulse",
				title: "心率",
				width: 55,
				align: "center"
			}, {
				field: "Paxis",
				title: "P轴",
				width: 55,
				align: "center"
			}, {
				field: "Taxis",
				title: "T轴",
				width: 45,
				align: "center"
			}, {
				field: "QRSaxis",
				title: "QRS轴",
				width: 45,
				align: "center"
			}, {
				field: "PR",
				title: "PR间期",
				width: 35,
				align: "center"
			}, {
				field: "QT",
				title: "QT间期",
				width: 45,
				align: "center"
			}, {
				field: "QTc",
				title: "QTc间期",
				width: 45,
				align: "center"
			}, {
				field: "QRS",
				title: "QRS时限",
				width: 35,
				align: "center"
			}, {
				field: "RV5",
				title: "RV5值",
				width: 35,
				align: "center"
			}, {
				field: "SV1",
				title: "SV1值",
				width: 35,
				align: "center"
			}, {
				field: "descript",
				title: "风险描述",
				width: 80,
				align: "left",
				formatter: function(val, row, idx){
					if(val && row.level>0){
						return "<label title=\"" + val + "\" style=\"color: #FF0000;\">" + val + "</label>";
					}
					return "";
				}
			}]]
		});
	}
	function refreshXdGrid(){
		xdGrid.datagrid("reload");
		xdGrid.datagrid("unselectAll");
		xdGrid.datagrid("clearSelections");
	}
	function queryXd(){
		xdGrid.datagrid("getPager").pagination({pageNumber: 1});
		$.extend(xdGrid.datagrid("options"), {
			pageNumber: 1,
			queryParams: $("#xdForm").serializeObject()
		});
		refreshXdGrid();
	}
	function resetXd(){
		document.getElementById("xdForm").reset();
	}
	function initTz(){
   		tzChart = new Highcharts.Chart({
   			chart: {
   				renderTo: "tzContainer", 
   				type: "line"
   			},
   			exporting:{
   				enabled:false
			},
			title: {
				text: "体脂率走势图"
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
				max: 100,
				tickInterval: 20,
				offset: -3,
				lineWidth: 1,
				gridLineWidth: 1,
				lineColor: "#C0D0E0",
				title: {
					text: "次/百分比",
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
					this.x +": "+ this.y+"次/百分比";
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
				name : "体脂率",
				data : []
			}]
		});
		tzGrid = $("#tzRecords").datagrid({
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
			url: "<%=request.getContextPath()%>/chk/checkData_queryBodyFat.action?custId=<%=custId%>",
			columns: [[{
				field: "checkTime",
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
				field: "subFatRate",
				title: "皮下体脂率",
				width: 55,
				align: "center"
 			}, {
				field: "visceralFatRate",
				title: "内脏脂肪率",
				width: 55,
				align: "center"
 			}, {
				field: "muscleRate",
				title: "肌肉率",
				width: 55,
				align: "center"
 			}, {
				field: "BMR",
				title: "基础代谢率",
				width: 55,
				align: "center"
 			}, {
				field: "bf",
				title: "体脂量",
				width: 55,
				align: "center"
 			}, {
				field: "water",
				title: "含水量",
				width: 55,
				align: "center"
 			}, {
				field: "bone",
				title: "骨骼",
				width: 55,
				align: "center"
 			}, {
				field: "bodyage",
				title: "身体年龄",
				width: 55,
				align: "center"
 			}, {
				field: "warningReason",
				title: "风险描述",
				width: 250,
				align: "left",
				formatter: function(val, row, idx){
					if(val && row.level>0){
						return "<label title=\"" + val + "\" style=\"color: #FF0000;\">" + val + "</label>";
					}
					return "";
				}
			}]],
			onLoadSuccess: function(data){
				var len = data && data.rows ? data.rows.length : 0;
				var tzs = new Array();
				if(len > 0){
					var rows = data.rows;
					for(var i=len-1; i>-1; i--){
						tzs.push(parseFloat(rows[i].subFatRate, 10));
					}
				}
				// 刷新图表
				tzChart.series[0].setData(tzs);
			}
		});
	}
	function refreshTzGrid(){
		tzGrid.datagrid("reload");
		tzGrid.datagrid("unselectAll");
		tzGrid.datagrid("clearSelections");
	}
	function queryTz(){
		tzGrid.datagrid("getPager").pagination({pageNumber: 1});
		$.extend(tzGrid.datagrid("options"), {
			pageNumber: 1,
			queryParams: $("#tzForm").serializeObject()
		});
		refreshTzGrid();
	}
	function resetTz(){
		document.getElementById("tzForm").reset();
	}
	function viewChkRecords(){
		var selected = xyGrid.datagrid("getSelected");
		debugger;
		if(!selected){
			$.messager.alert("提示信息","请选择一条记录。","error");
			return;
		}else{
			var subWin = $('<div><iframe src="<%=request.getContextPath()%>/msg/heaGuide_toAdd.action?custId=' + selected.custId + '&id=' + selected.id + '" style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>').window({
				title: "健康指导",
				width: 700,
				height: 450,
				resizable: false,
				collapsible: false,
				minimizable: false,
				maximizable: false,
				modal: true,
				zIndex: 100,
				closable: true
			});
		}
		<%-- top.addTab("健康指导", "<%=request.getContextPath()%>/msg/heaGuide_toAdd.action.action?custId=" + selected.userId); --%>
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
							<a href="javascript:viewChkRecords();" class="easyui-linkbutton" >健康指导</a>
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
						</td>
					</tr>
				</table>
			</form>
			<table id="xyRecords"></table>
			<div id="xyContainer" class="mychart" style="min-width: 310px;height: 250px;margin: 0px auto;"></div>
		</div>
		
		<div title="血糖" style="padding: 2px;">
			<form id="xtForm">
				<table class="table" style="margin-bottom: 2px;">
					<tr>
						<td style="text-align: right;">
							
							检查日期:&nbsp;
							<input class="Wdate" style="width: 100px;" id="xtStartDate" name="xtStartDate" readonly="readonly" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'xtEndDate\')}'})" />
							&nbsp;至&nbsp;
							<input class="Wdate" style="width: 100px;"id="xtEndDate" name="xtEndDate" readonly="readonly" onclick="WdatePicker({minDate:'#F{$dp.$D(\'xtStartDate\')}'})" />
							&nbsp;&nbsp;
							<a href="javascript:queryXt();" class="easyui-linkbutton" >查询</a>
							&nbsp;
							<a href="javascript:resetXt();" class="easyui-linkbutton" >清空</a>
							&nbsp;
						</td>
					</tr>
				</table>
			</form>
			<table id="xtRecords"></table>
			<div id="xtContainer" class="mychart" style="min-width: 310px;height: 250px;margin: 0px auto;"></div>
		</div>
		<div title="心(脉)率" style="padding: 2px;">
			<form id="xlForm">
				<table class="table" style="margin-bottom: 2px;">
					<tr>
						<td style="text-align: right;">
							类型:&nbsp;
							<select name="pulseType" style="width: 80px;">
								<option value="">全部</option>
								<option value="1">心率</option>
								<option value="2">脉率</option>
							</select>
							&nbsp;
							检查日期:&nbsp;
							<input class="Wdate" style="width: 100px;" id="xlStartDate" name="xlStartDate" readonly="readonly" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'xlEndDate\')}'})" />
							&nbsp;至&nbsp;
							<input class="Wdate" style="width: 100px;"id="xlEndDate" name="xlEndDate" readonly="readonly" onclick="WdatePicker({minDate:'#F{$dp.$D(\'xlStartDate\')}'})" />
							&nbsp;&nbsp;
							<a href="javascript:queryXl();" class="easyui-linkbutton" >查询</a>
							&nbsp;
							<a href="javascript:resetXl();" class="easyui-linkbutton" >清空</a>
							&nbsp;
						</td>
					</tr>
				</table>
			</form>
			<table id="xlRecords"></table>
			<div id="xlContainer" class="mychart" style="min-width: 310px;height: 250px;margin: 0px auto;"></div>
		</div>
		<div title="血氧" style="padding: 2px;">
			<form id="xoForm">
				<table class="table" style="margin-bottom: 2px;">
					<tr>
						<td style="text-align: right;">
							检查日期:&nbsp;
							<input class="Wdate" style="width: 100px;" id="xoStartDate" name="xoStartDate" readonly="readonly" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'xoEndDate\')}'})" />
							&nbsp;至&nbsp;
							<input class="Wdate" style="width: 100px;"id="xoEndDate" name="xoEndDate" readonly="readonly" onclick="WdatePicker({minDate:'#F{$dp.$D(\'xoStartDate\')}'})" />
							&nbsp;&nbsp;
							<a href="javascript:queryXo();" class="easyui-linkbutton" >查询</a>
							&nbsp;
							<a href="javascript:resetXo();" class="easyui-linkbutton" >清空</a>
							&nbsp;
						</td>
					</tr>
				</table>
			</form>
			<table id="xoRecords"></table>
			<div id="xoContainer" class="mychart" style="min-width: 310px;height: 250px;margin: 0px auto;"></div>
		</div>
		<div title="体温" style="padding: 2px;">
			<form id="twForm">
				<table class="table" style="margin-bottom: 2px;">
					<tr>
						<td style="text-align: right;">
							检查日期:&nbsp;
							<input class="Wdate" style="width: 100px;" id="twStartDate" name="twStartDate" readonly="readonly" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'twEndDate\')}'})" />
							&nbsp;至&nbsp;
							<input class="Wdate" style="width: 100px;"id="twEndDate" name="twEndDate" readonly="readonly" onclick="WdatePicker({minDate:'#F{$dp.$D(\'twStartDate\')}'})" />
							&nbsp;&nbsp;
							<a href="javascript:queryTw();" class="easyui-linkbutton" >查询</a>
							&nbsp;
							<a href="javascript:resetTw();" class="easyui-linkbutton" >清空</a>
							&nbsp;
						</td>
					</tr>
				</table>
			</form>
			<table id="twRecords"></table>
			<div id="twContainer" class="mychart" style="min-width: 310px;height: 250px;margin: 0px auto;"></div>
		</div>
		<div title="总胆固醇" style="padding: 2px;">
			<form id="dgcForm">
				<table class="table" style="margin-bottom: 2px;">
					<tr>
						<td style="text-align: right;">
							检查日期:&nbsp;
							<input class="Wdate" style="width: 100px;" id="dgcStartDate" name="dgcStartDate" readonly="readonly" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'dgcEndDate\')}'})" />
							&nbsp;至&nbsp;
							<input class="Wdate" style="width: 100px;"id="dgcEndDate" name="dgcEndDate" readonly="readonly" onclick="WdatePicker({minDate:'#F{$dp.$D(\'dgcStartDate\')}'})" />
							&nbsp;&nbsp;
							<a href="javascript:queryDgc();" class="easyui-linkbutton" >查询</a>
							&nbsp;
							<a href="javascript:resetDgc();" class="easyui-linkbutton" >清空</a>
							&nbsp;
						</td>
					</tr>
				</table>
			</form>
			<table id="dgcRecords"></table>
			<div id="dgcContainer" class="mychart" style="min-width: 310px;height: 250px;margin: 0px auto;"></div>
		</div>
		<div title="尿酸" style="padding: 10px;">
			<form id="nsForm">
				<table class="table" style="margin-bottom: 2px;">
					<tr>
						<td style="text-align: right;">
							检查日期:&nbsp;
							<input class="Wdate" style="width: 100px;" id="nsStartDate" name="nsStartDate" readonly="readonly" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'nsEndDate\')}'})" />
							&nbsp;至&nbsp;
							<input class="Wdate" style="width: 100px;"id="nsEndDate" name="nsEndDate" readonly="readonly" onclick="WdatePicker({minDate:'#F{$dp.$D(\'nsStartDate\')}'})" />
							&nbsp;&nbsp;
							<a href="javascript:queryNs();" class="easyui-linkbutton" >查询</a>
							&nbsp;
							<a href="javascript:resetNs();" class="easyui-linkbutton" >清空</a>
							&nbsp;
						</td>
					</tr>
				</table>
			</form>
			<table id="nsRecords"></table>
			<div id="nsContainer" class="mychart" style="min-width: 310px;height: 250px;margin: 0px auto;"></div>
		</div>
		<div title="尿常规" style="padding: 2px;">
			<form id="nsfxForm">
				<table class="table" style="margin-bottom: 2px;">
					<tr>
						<td style="text-align: right;">
							检查日期:&nbsp;
							<input class="Wdate" style="width: 100px;" id="nsfxStartDate" name="nsfxStartDate" readonly="readonly" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'nsfxEndDate\')}'})" />
							&nbsp;至&nbsp;
							<input class="Wdate" style="width: 100px;"id="nsfxEndDate" name="nsfxEndDate" readonly="readonly" onclick="WdatePicker({minDate:'#F{$dp.$D(\'nsfxStartDate\')}'})" />
							&nbsp;&nbsp;
							<a href="javascript:queryNsfx();" class="easyui-linkbutton" >查询</a>
							&nbsp;
							<a href="javascript:resetNsfx();" class="easyui-linkbutton" >清空</a>
							&nbsp;
						</td>
					</tr>
				</table>
			</form>
			<table id="nsfxRecords"></table>
		</div>
		<div title="体脂率" style="padding: 2px;">
			<form id="tzForm">
				<table class="table" style="margin-bottom: 2px;">
					<tr>
						<td style="text-align: right;">
							检查日期:&nbsp;
							<input class="Wdate" style="width: 100px;" id="tzStartDate" name="tzStartDate" readonly="readonly" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'tzEndDate\')}'})" />
							&nbsp;至&nbsp;
							<input class="Wdate" style="width: 100px;"id="tzEndDate" name="tzEndDate" readonly="readonly" onclick="WdatePicker({minDate:'#F{$dp.$D(\'tzStartDate\')}'})" />
							&nbsp;&nbsp;
							<a href="javascript:queryTz();" class="easyui-linkbutton" >查询</a>
							&nbsp;
							<a href="javascript:resetTz();" class="easyui-linkbutton" >清空</a>
							&nbsp;
						</td>
					</tr>
				</table>
			</form>
			<table id="tzRecords"></table>
			<div id="tzContainer" class="mychart" style="min-width: 310px;height: 250px;margin: 0px auto;"></div>
		</div>
		<div title="心电" style="padding: 2px;">
			<form id="xdForm">
				<table class="table" style="margin-bottom: 2px;">
					<tr>
						<td style="text-align: right;">
							检查日期:&nbsp;
							<input class="Wdate" style="width: 100px;" id="xdStartDate" name="xlStartDate" readonly="readonly" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'xlEndDate\')}'})" />
							&nbsp;至&nbsp;
							<input class="Wdate" style="width: 100px;"id="xdEndDate" name="xlEndDate" readonly="readonly" onclick="WdatePicker({minDate:'#F{$dp.$D(\'xlStartDate\')}'})" />
							&nbsp;&nbsp;
							<a href="javascript:queryXd();" class="easyui-linkbutton" >查询</a>
							&nbsp;
							<a href="javascript:resetXd();" class="easyui-linkbutton" >清空</a>
							&nbsp;
						</td>
					</tr>
				</table>
			</form>
			<table id="xdRecords"></table>
			<!-- <div id="tzContainer" class="mychart" style="min-width: 310px;height: 250px;margin: 0px auto;"></div> -->
		</div>
	</div>
</body>
</html>