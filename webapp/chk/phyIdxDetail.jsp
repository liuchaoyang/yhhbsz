<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import = "com.yzxt.yh.module.sys.bean.Customer" %>
<%@ page import = "com.yzxt.yh.module.chk.bean.PhysiologIndex" %>
<%@ page import = "com.yzxt.yh.module.chk.bean.PressurePulse" %>
<%@ page import = "com.yzxt.yh.module.chk.bean.Pulse" %>
<%@ page import = "com.yzxt.yh.module.chk.bean.BloodSugar" %>
<%@ page import = "com.yzxt.yh.module.chk.bean.BloodOxygen" %>
<%@ page import = "com.yzxt.yh.module.chk.bean.Temperature" %>
<%@ page import = "com.yzxt.yh.module.chk.bean.AnalysisUricAcid" %>
<%@ page import = "com.yzxt.yh.module.chk.bean.BodyFat" %>
<!DOCTYPE html>
<html lang="en">
<head>
<%@ include file="../common/inc.jsp"%>
<% 
	AnalysisUricAcid dataAcid = (AnalysisUricAcid) request.getAttribute("dataAcid");
	
	Customer customer = (Customer)request.getAttribute("customer"); 
	PhysiologIndex phyIdx = (PhysiologIndex)request.getAttribute("phyIdx");
	if(phyIdx==null){
            phyIdx = new PhysiologIndex();
        }
    if(dataAcid==null){
            dataAcid = new AnalysisUricAcid();
     }
     
     PressurePulse xyBean = (PressurePulse)request.getAttribute("xyBean");
     BloodSugar khBean = (BloodSugar)request.getAttribute("khBean");
     BloodSugar chBean = (BloodSugar)request.getAttribute("chBean");
     BloodSugar ftBean = (BloodSugar)request.getAttribute("ftBean");
     Pulse pulse = (Pulse) request.getAttribute("pulse"); 
     BloodOxygen bloodOxygen = (BloodOxygen)request.getAttribute("bloodOxygen");
     BodyFat bodyFat = (BodyFat)request.getAttribute("bodyFat");
     Temperature temperature = (Temperature) request.getAttribute("temperature"); 
     if(xyBean==null){
            xyBean = new PressurePulse();
        }
    if(khBean==null){
            khBean = new BloodSugar();
     }
     if(chBean==null){
            chBean = new BloodSugar();
     }
     if(ftBean==null){
            ftBean = new BloodSugar();
     }
     if(pulse==null){
            pulse = new Pulse();
     }
     if(bloodOxygen==null){
            bloodOxygen = new BloodOxygen();
     }
     if(bodyFat==null){
            bodyFat = new BodyFat();
     }
     if(temperature==null){
            temperature = new Temperature();
     }
	java.text.DecimalFormat   df=new   java.text.DecimalFormat("0.00"); 
 %>
	<title>用户生理指数</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/css/flow.css" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script> 
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery-1.8.0.min.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/highcharts.js"></script>
	<%-- <script src="<%=request.getContextPath()%>/resources/ueditor/third-party/highcharts/highcharts.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/modules/highcharts-3d.js"></script> --%>
	<script src="<%=request.getContextPath()%>/resources/ueditor/third-party/highcharts/highcharts-more.js"></script>
	<style type="text/css">
		.titleReport tr td{
			font-family: 微软雅黑;
			font-size: 16px;
			color: #919191;
		}
		
		.report_title{
		
			font-family: 微软雅黑;
			font-size: 20px;
		
		}
		
		.detial_table4 tr th {
			background-color: #b7dff9;
		}
		.detial_table4 tr td, .detial_table4 tr th {
			border: 1px solid #b6b6b6;
		}
		.detial_table4 tr td{
			border-collapse: collapse;
			font-size: 12px;
			height: 18px;
			line-height: 18px;
			text-align: center;
		}
		
		.detial_table4 tr th {
			font-size: 12px;
			height: 18px;
			line-height: 18px;
			text-align: center;
		}
		
		.detial_table4 tbody{
			display: table-row-group;
			vertical-align: middle;
			border-color: inherit;
		}
		
		
		.title_tr td{
			border-top: 2px solid #919191;
			border-bottom: 2px solid #919191;
		}
	</style>
	<script type="text/javascript">
	$(function(){
	    $("#xueya").highcharts({
	        chart: {
	            type: 'column'
	        },
	        title: {
	            text: '血压'
	        },
	        exporting:{
					enabled:false
				},
	        xAxis: {
	            categories: ['舒张压', '收缩压']
	        },
	        yAxis: {
	            min: 0,
	            title: {
	                text: '血压值范围'
	            }
	        },
	       tooltip: {
	        	
	            pointFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b><br/>',
	            style:{
						display:'none' //通过样式表控制不显示tooltip数据提示框
				},
	            shared: true
	        },
	        plotOptions: {
	            column: {
	                stacking: 'column'
	            }
	        },
	        series: [{
	            name: '高血压',
	            data: [90, 90],
	            color: 'red',
	            stack:'标准'
	        }, {
	            name: '正常',
	            data: [40, 50],
	            color: 'green',
	            display:'none',
	            stack:'标准'
	        }, {
	            name: '低血压',
	            data: [50, 90],
	            color: 'blue',
	            stack:'标准'
	        }, {
	            name: '实际',
	            data: [<%if(phyIdx.getDbp()!=null){%><%=phyIdx.getDbp()%><%}%><%else {%>0<%}%>, <%if(phyIdx.getSbp()!=null){%><%=phyIdx.getSbp()%><%}%><%else {%>0<%}%>],
	            color: '<%if(phyIdx.getDbp()!=null||phyIdx.getSbp()!=null){%><%if(phyIdx.getDbp()<50&&phyIdx.getSbp()<90){%>blue<%}%><%else if((phyIdx.getDbp()>50&&phyIdx.getDbp()<90)&&(phyIdx.getSbp()>90&&phyIdx.getSbp()<140)){%>green<%}%><%else {%>red<%}%><%}%><%else {%>white<%}%>',
	            stack:'实际'
	        }]
	    });
	});
	$(function () {
	    $("#xuetang").highcharts({
	        chart: {
	            type: 'column'
	        },
	        title: {
	            text: '血糖'
	        },
	       exporting:{
					enabled:false
				},
	        xAxis: {
	            categories: ['空腹血糖','餐后血糖', '服糖2小时血糖']
	        },
	        yAxis: {
	            min: 0,
	            title: {
	                text: '血糖范围值'
	            },
	            stackLabels: {
	                enabled: true,
	                style: {
	                    fontWeight: 'bold',
	                    color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
	                }
	            }
	        },
	        legend: {
	            align: 'right',
	            x: -70,
	            verticalAlign: 'top',
	            y: 30,
	            floating: true,
	            backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColorSolid) || 'white',
	            borderColor: '#CCC',
	            borderWidth: 1,
	            shadow: false
	        },
	        tooltip: {
	            formatter: function() {
	                return '<b>'+ this.x +'</b><br>'+
	                    this.series.name +': '+ this.y +'<br>'+
	                    'Total: '+ this.point.stackTotal;
	            },
	            style:{
						display:'none' //通过样式表控制不显示tooltip数据提示框
				}
	        }, 
	        plotOptions: {
	            column: {
	                stacking: 'column',
	                dataLabels: {
	                    enabled: true,
	                    color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white'
	                }
	            }
	        },
	        series: [{
	            name:  '高血糖',
	            data: [13.0,8.9,8.9],
	            color: 'red',
	            stack:'标准'
	        }, {
	            name: '正常',
	            data: [7.0,11.1,11.1],
	            color: 'green',
	            stack:'标准'
	        }, {
	            name: '实际',
	            data: [<%if(phyIdx.getFpg()!=null){%><%=phyIdx.getFpg()%><%}%><%else {%><%}%>, <%if(phyIdx.getH2pbg()!=null){%><%=phyIdx.getH2pbg()%><%}%><%else {%><%}%>, <%if(phyIdx.getL2sugar()!=null){%><%=phyIdx.getL2sugar()%><%}%><%else {%><%}%>],
	            color: ['<%if(phyIdx.getFpg()!=null&&phyIdx.getFpg()>7){%>red<%}%><%else {%>green<%}%>','red','green'],
	            stack:'实际'
	        }]
	    });
	});
	
	$(function () {
	    $("#BMI").highcharts({
	        chart: {
	            type: 'bar'
	        },
	        title: {
	            text: 'BMI(体质指数)'
	        },
	        exporting:{
					enabled:false
				},
	        xAxis: {
	            categories: ['体质指数标准', '实际值']
	        },
	        yAxis: {
	            min: 0,
	            title: {
	                text: '体质指数比较'
	            }
	        },
	       
	        legend: {
	            backgroundColor: '#FFFFFF',
	            reversed: true
	        },
	         tooltip: {
	            style:{
						display:'none' //通过样式表控制不显示tooltip数据提示框
				}
	        }, 
	        plotOptions: {
	            series: {
	                stacking: 'normal'
	            }
	        },
	        series: [ {
	            name: '肥胖',
	            data: [15,<%if((phyIdx.getWeight()!=null&&phyIdx.getHeight()!=null)&&phyIdx.getHeight()!=0){%><%if(phyIdx.getWeight()/(phyIdx.getHeight()*phyIdx.getHeight()/100/100)>28){%><%=df.format(phyIdx.getWeight()/(phyIdx.getHeight()*phyIdx.getHeight()/100/100))%><%}%><%}%><%else {%><%}%>],
	            color:'red'
	        }, {
	            name: '超重',
	            data: [4,<%if((phyIdx.getWeight()!=null&&phyIdx.getHeight()!=null)&&phyIdx.getHeight()!=0){%><%if(phyIdx.getWeight()/(phyIdx.getHeight()*phyIdx.getHeight()/100/100)>24&&phyIdx.getWeight()/(phyIdx.getHeight()*phyIdx.getHeight()/100/100)<28){%><%=df.format(phyIdx.getWeight()/(phyIdx.getHeight()*phyIdx.getHeight()/100/100))%><%}%><%}%><%else {%><%}%>],
	            color:'orange'
	        }, {
	            name: '正常',
	            data: [5.5,<%if((phyIdx.getWeight()!=null&&phyIdx.getHeight()!=null)&&phyIdx.getHeight()!=0){%><%if(phyIdx.getWeight()/(phyIdx.getHeight()*phyIdx.getHeight()/100/100)>18.5&&phyIdx.getWeight()/(phyIdx.getHeight()*phyIdx.getHeight()/100/100)<24){%><%=df.format(phyIdx.getWeight()/(phyIdx.getHeight()*phyIdx.getHeight()/100/100))%><%}%><%}%><%else {%><%}%>],
	            color:'green'
	        },{
	            name: '偏瘦',
	            data: [18.5,<%if((phyIdx.getWeight()!=null&&phyIdx.getHeight()!=null)&&phyIdx.getHeight()!=0){%><%if(phyIdx.getWeight()/(phyIdx.getHeight()*phyIdx.getHeight()/100/100)<18.5){%><%=df.format(phyIdx.getWeight()/(phyIdx.getHeight()*phyIdx.getHeight()/100/100))%><%}%><%}%><%else {%><%}%>],
	            color:'blue'
	        } ]
	    });
	});
	$(function () {
	    $("#BO").highcharts({
	        chart: {
	            plotBackgroundColor: null,
	            plotBorderWidth: null,
	            plotShadow: false
	        },
	        title: {
	            text: '血氧'
	        },
	        subtitle:{
	        	text:'正常的血氧百分比是94%~99%'
	        },
	        exporting:{
					enabled:false
				},
	        tooltip: {
	    	    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>',
	            style:{
						display:'none' //通过样式表控制不显示tooltip数据提示框
				}
	        },
	        plotOptions: {
	            pie: {
	                allowPointSelect: true,
	                cursor: 'pointer',
	                dataLabels: {
	                    enabled: true,
	                    color: '#000000',
	                    connectorColor: '#000000',
	                    format: '<b>{point.name}</b>: {point.percentage:.1f} %'
	                }
	            }
	        },
	        series: [{
	            type: 'pie',
	            name: '血氧百分比',
	            data: [
	                {
	                    name: '血氧',
	                    y: <%if(phyIdx.getBo()!=null){%><%=phyIdx.getBo()%><%}%><%else {%>0<%}%>,
	                    sliced: true,
	                    selected: true
	                },
	                ['其他',<%if(phyIdx.getBo()!=null){%><%=100-phyIdx.getBo()%><%}%><%else {%>100<%}%>]
	            ]
	        }]
	    });
	});
	$(function () {
	    $("#TW").highcharts({
	        chart: {
	            type: 'line'
	        },
	        title: {
	            text: '体温'
	        },
	        exporting:{
					enabled:false
				},
	        xAxis: {
	            categories: ['实际值']
	        },
	        yAxis: {
	            title: {
	                text: 'Temperature (°C)'
	            }
	        },
	        tooltip: {
	            enabled: false,
	            formatter: function() {
	                return '<b>'+ this.series.name +'</b><br/>'+this.x +': '+ this.y +'°C';
	            },
	            style:{
	            	display:'none'
	            }
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
	            name: '实际值',
	            data: [<%if(phyIdx.getTemperature()!=null){%><%=phyIdx.getTemperature()%><%}%><%else {%><%}%>]
	        }]
	    });
	}); 
	$(function () {
		
	    $("#pluse").highcharts({
		
		    chart: {
		        type: 'gauge',
		        plotBackgroundColor: null,
		        plotBackgroundImage: null,
		        plotBorderWidth: 0,
		        plotShadow: false
		    },
		    
		    title: {
		        text: '心率'
		    },
		    
		    pane: {
		        startAngle: -90,
		        endAngle: 90,
		        background: [{
		            backgroundColor: {
		                linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
		                stops: [
		                    [0, '#FFF'],
		                    [1, '#333']
		                ]
		            },
		            borderWidth: 0,
		            outerRadius: '109%'
		        }, {
		            backgroundColor: {
		                linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
		                stops: [
		                    [0, '#333'],
		                    [1, '#FFF']
		                ]
		            },
		            borderWidth: 1,
		            outerRadius: '107%'
		        }, {
		            backgroundColor: '#DDD',
		            borderWidth: 0,
		            outerRadius: '105%',
		            innerRadius: '103%'
		        }]
		    },
		       
		    // the value axis
		    yAxis: {
		        min: 0,
		        max: 160,
		        
		        minorTickInterval: 'auto',
		        minorTickWidth: 1,
		        minorTickLength: 10,
		        minorTickPosition: 'inside',
		        minorTickColor: '#666',
		
		        tickPixelInterval: 30,
		        tickWidth: 2,
		        tickPosition: 'inside',
		        tickLength: 10,
		        tickColor: '#666',
		        labels: {
		            step: 2,
		            rotation: 'auto'
		        },
		        title: {
		            text: '分钟/次'
		        },
		        plotBands: [{
		            from: 0,
		            to: 60,
		            color: 'blue' // green
		        }, {
		            from: 60,
		            to: 100,
		            color: '#55BF3B' // yellow
		        }, {
		            from: 100,
		            to: 160,
		            color: '#DF5353' // red
		        }]        
		    },
			exporting:{
					enabled:false
				},
		    series: [{
		        name: '脉搏',
		        data: [<%if(phyIdx.getPulse()!=null){%><%=phyIdx.getPulse()%><%}%><%else {%><%}%>],
		        tooltip: {
		            valueSuffix: ' 分钟/次'
		        }
		    }]
		
		});
	}); 
	
	$(function () {
	    $("#BFR").highcharts({
	        chart: {
	            plotBackgroundColor: null,
	            plotBorderWidth: null,
	            plotShadow: false
	        },
	        title: {
	            text: '体脂率'
	        },
	        subtitle:{
	        	text:'17%~25%为女子的理想型体脂率，10%~18%为男子的理想型体脂率。'
	        },
	        exporting:{
					enabled:false
				},
	        tooltip: {
	    	    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>',
	            style:{
						display:'none' //通过样式表控制不显示tooltip数据提示框
				}
	        },
	        plotOptions: {
	            pie: {
	                allowPointSelect: true,
	                cursor: 'pointer',
	                dataLabels: {
	                    enabled: true,
	                    color: '#000000',
	                    connectorColor: '#000000',
	                    format: '<b>{point.name}</b>: {point.percentage:.1f} %'
	                }
	            }
	        },
	        series: [{
	            type: 'pie',
	            name: '体脂百分比',
	            data: [
	                {
	                    name: '体脂',
	                    y: <%if(phyIdx.getBfr()!=null){%><%=phyIdx.getBfr()%><%}%><%else {%>0<%}%>,
	                    sliced: true,
	                    selected: true
	                },
	                ['其他',<%if(phyIdx.getBfr()!=null){%><%=100-phyIdx.getBfr()%><%}%><%else {%>100<%}%>]
	            ]
	        }]
	    });
	});  
	</script>
</head>
<body>
	<div style="width:90%;height:90%;">
		<div style="margin-left:100px;text-align: center;font-size: 14px;margin: 10px;font-weight: bold;padding:12px;width:95%">
			<table border="0" class= "titleReport" width = "100%" style="text-align:center;margin-left:60px;">
				<tr>
           			<!-- <td  colspan="6" align="left" class="table_title">个人生理指数</td> -->
           			<td colspan="6" style="text-align: center; height: 35px;">
                        <span class="report_title">个人生理指数报告</span>
                    </td>
           		</tr>
				<tr class="title_tr">
					<td width="30%">姓名:<span>${resident.name}</span></td>
					<%-- <td width="10%">
						<span>${resident.name}</span>
					</td> --%>
					<td width="30%">性别:
						${resident.sex==2 ? '未知性别' : ''}
						${resident.sex==0 ? '男 ' : ''}
						${resident.sex==1 ? '女' : ''}
						${resident.sex==9 ? '未说明' : ''}
					</td>
					<%-- <td width="10%">
						${resident.sex==0 ? '未知性别' : ''}
						${resident.sex==1 ? '男 ' : ''}
						${resident.sex==2 ? '女' : ''}
						${resident.sex==9 ? '未说明' : ''}</td> --%>
					<td width="30%">出生日期:<fmt:formatDate value="${resident.birthday }" pattern="yyyy-MM-dd"/></td>
					<%-- <td><fmt:formatDate value="${resident.birthday }" pattern="yyyy-MM-dd"/></td> --%>
				</tr>
				<tr style="height:100px;">
					<td width="15%">
						整体评估结论：
					</td>
					<td colspan="5" ><%if(xyBean.getDescript()!=null&&xyBean.getLevel()>0) {%><%=xyBean.getDescript()%>,<%}%><%if(khBean.getDescript()!=null&&khBean.getLevel()>0) {%><%=khBean.getDescript()%><%}%><%if(chBean.getDescript()!=null&&chBean.getLevel()>0) {%><%=chBean.getDescript()%><%}%><%if(ftBean.getDescript()!=null&&ftBean.getLevel()>0) {%><%=ftBean.getDescript()%><%}%>
<%if(dataAcid.getDescript()!=null&&dataAcid.getLevel()>0) {%><%=dataAcid.getDescript()%>,<%}%><%if(pulse.getDescript()!=null&&pulse.getLevel()>0) {%><%=pulse.getDescript()%>,<%}%>
<%if(bloodOxygen.getDescript()!=null&&bloodOxygen.getLevel()>0) {%><%=bloodOxygen.getDescript()%>,<%}%><%if(temperature.getDescript()!=null&&temperature.getLevel()>0) {%><%=temperature.getDescript()%>,<%}%>
<%if((phyIdx.getWeight()!=null&&phyIdx.getHeight()!=null)&&phyIdx.getHeight()!=0){%><%if(phyIdx.getWeight()/(phyIdx.getHeight()*phyIdx.getHeight()/100/100)>28){%>你的BMI指数为：<%=df.format(phyIdx.getWeight()/(phyIdx.getHeight()*phyIdx.getHeight()/100/100))%>，属于肥胖<%}%><%}%>
<%if((phyIdx.getWeight()!=null&&phyIdx.getHeight()!=null)&&phyIdx.getHeight()!=0){%><%if(phyIdx.getWeight()/(phyIdx.getHeight()*phyIdx.getHeight()/100/100)>24&&phyIdx.getWeight()/(phyIdx.getHeight()*phyIdx.getHeight()/100/100)<28){%>你的BMI指数为：<%=df.format(phyIdx.getWeight()/(phyIdx.getHeight()*phyIdx.getHeight()/100/100))%>，属于超重<%}%><%}%>
<%-- <%if((phyIdx.getWeight()!=null&&phyIdx.getHeight()!=null)&&phyIdx.getHeight()!=0){%><%if(phyIdx.getWeight()/(phyIdx.getHeight()*phyIdx.getHeight()/100/100)>18.5&&phyIdx.getWeight()/(phyIdx.getHeight()*phyIdx.getHeight()/100/100)<24){%>你的BMI指数为：<%=df.format(phyIdx.getWeight()/(phyIdx.getHeight()*phyIdx.getHeight()/100/100))%>，属于正常<%}%><%}%> --%>
<%if((phyIdx.getWeight()!=null&&phyIdx.getHeight()!=null)&&phyIdx.getHeight()!=0){%><%if(phyIdx.getWeight()/(phyIdx.getHeight()*phyIdx.getHeight()/100/100)<18.5){%>你的BMI指数为：<%=df.format(phyIdx.getWeight()/(phyIdx.getHeight()*phyIdx.getHeight()/100/100))%>属于偏瘦<%}%><%}%>
					</td>
				</tr>
			</table>
		</div>
		<div>
			<div>
				<table border="0"  width = "100%" style="margin-left:60px;">
					<tr>
						<td style="width:50%;">
							<div id="xueya" style="width:500px;height:300px"></div>
							<div style="width:500px;height:auto">
								<table >
									<tr>&nbsp;</tr>
									<tr style="font-size: 14px;margin: 10px;font-weight: bold;padding:12px;">
										<td>结论：</td>
										<td><%if(xyBean.getDescript()!=null) {%><%=xyBean.getDescript()%><%}%><%else {%><%}%></td>
									</tr>
								</table>
							</div>
						</td>
						<td>
							<div id="xuetang" style="width:500px;height:300px;align:right;"></div>
							<div style="width:500px;height:auto">
								<table>
									<tr>&nbsp;</tr>
									<tr style="font-size: 14px;margin: 10px;font-weight: bold;padding:12px;">
										<td>结论：</td>
										<td><%if(khBean.getDescript()!=null) {%><%=khBean.getDescript()%><%}%><%else {%><%}%>
											<%if(chBean.getDescript()!=null) {%><%=chBean.getDescript()%><%}%><%else {%><%}%>
											<%if(ftBean.getDescript()!=null) {%><%=ftBean.getDescript()%><%}%><%else {%><%}%>
										</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
					<tr>
						<td style="width:50%;">
							<div id="pluse" style="width:500px;height:300px"></div>
							<div>
								<table>
									<tr>&nbsp;</tr>
									<tr style="font-size: 14px;margin: 10px;font-weight: bold;padding:12px;">
										<td>结论：</td>
										<td><%if(pulse.getDescript()!=null) {%><%=pulse.getDescript()%>。<%}%><%else {%><%}%></td>
									</tr>
								</table>
							</div>
						</td>
						<td>
							<div id="BMI" style="width:500px;height:300px"></div>
							<div>
								<table>
									<tr>&nbsp;</tr>
									<tr style="font-size: 14px;margin: 10px;font-weight: bold;padding:12px;">
										<td>结论：</td>
										<td>
											<%
												if((phyIdx.getWeight()!=null&&phyIdx.getHeight()!=null)&&phyIdx.getHeight()!=0){
											%>
											<%
											 	if(phyIdx.getWeight()/(phyIdx.getHeight()*phyIdx.getHeight()/100/100)>28){
											%>
											你的体质指数为肥胖<%}%>
											<%		
												if(phyIdx.getWeight()/(phyIdx.getHeight()*phyIdx.getHeight()/100/100)>24&&phyIdx.getWeight()/(phyIdx.getHeight()*phyIdx.getHeight()/100/100)<28){
											%>
											你的体质指数为超重<%}%>
											<%
												if(phyIdx.getWeight()/(phyIdx.getHeight()*phyIdx.getHeight()/100/100)>18.5&&phyIdx.getWeight()/(phyIdx.getHeight()*phyIdx.getHeight()/100/100)<24){
											%>
											你的体质指数为正常<%}%>
											<%
												if(phyIdx.getWeight()/(phyIdx.getHeight()*phyIdx.getHeight()/100/100)<18.5){
											%>
											你的体质指数为偏瘦<%}%>
											<%}%>
										</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
					<tr>
						<td colspan="2" style="width:100%;">
							<table id="NCG" style="width:100%;border:1px;margin-left:100px;">
								<tr>
           							<td  colspan="3" align="left" class="table_title">尿常规</td>
           						</tr>
								<tr>
									<td width="35%">名称</td>
									<td width="15%">测量值</td>
									<td>标准值</td>
								</tr>
								<tr>
									<td style="text-align:left;">尿白细胞（LEU）</td>
									<td><%if(dataAcid.getLeu()!=null) {%><%=dataAcid.getLeu()%><%}%><%else {%><%}%></td>
									<td style="text-align:left;">-（此项目无病变的情况下一般不予筛查）</td>
								</tr>
								<tr>
									<td style="text-align:left;">亚硝酸盐（NIT）</td>
									
									<td><%if(dataAcid.getNit()!=null) {%><%=dataAcid.getNit()%><%}%><%else {%><%}%></td>
									<td style="text-align:left;">-（此项目普通情况下一般不予筛查）</td>
								</tr>
								<tr>
									<td style="text-align:left;">尿蛋白（PRO）</td>
									
									<td><%if(dataAcid.getPro()!=null) {%><%=dataAcid.getPro()%><%}%><%else {%><%}%></td>
									<td style="text-align:left;">+-（+表示轻度白色混浊，-表示清淡无混浊，两者或介于两者之间为正常）</td>
								</tr>
								<tr>
									<td style="text-align:left;">葡萄糖（GLU-U）</td>
									
									<td><%if(dataAcid.getGlu()!=null) {%><%=dataAcid.getGlu()%><%}%><%else {%><%}%></td>
									<td style="text-align:left;">-（蓝色溶液）</td>
								</tr>
								<tr>
									<td style="text-align:left;">酮体（KET）</td>
									
									<td><%if(dataAcid.getKet()!=null) {%><%=dataAcid.getKet()%><%}%><%else {%><%}%></td>
									<td style="text-align:left;">-（此项目一般不予筛查或者筛查时呈紫红色为正常）</td>
								</tr>
								<tr>
									<td style="text-align:left;">尿胆原（URO）</td>
									
									<td><%if(dataAcid.getUbg()!=null) {%><%=dataAcid.getUbg()%><%}%><%else {%><%}%></td>
									<td style="text-align:left;">-（此项目普通情况下一般不予筛查）</td>
								</tr>
								<tr>
									<td style="text-align:left;">胆红素（BIL）</td>
									
									<td><%if(dataAcid.getBil()!=null) {%><%=dataAcid.getBil()%><%}%><%else {%><%}%></td>
									<td style="text-align:left;">-（此项目无病变的情况下一般不予筛查）</td>
								</tr>
								<tr>
									<td style="text-align:left;">PH值（PH-U）[尿液酸碱度]</td>
									
									<td><%if(dataAcid.getPh()!=null) {%><%=dataAcid.getPh()%><%}%><%else {%><%}%></td>
									<td style="text-align:left;">5．5～7．5</td>
								</tr>
								<tr>
									<td style="text-align:left;">比重（SG）</td>
									
									<td><%if(dataAcid.getSg()!=null) {%><%=dataAcid.getSg()%><%}%><%else {%><%}%></td>
									<td style="text-align:left;">1．010～1．030</td>
								</tr>
								<tr>
									<td style="text-align:left;">隐血（BLU）</td>
									
									<td><%if(dataAcid.getBld()!=null) {%><%=dataAcid.getBld()%><%}%><%else {%><%}%></td>
									<td style="text-align:left;">-（无隐血）</td>
								</tr>
								<tr>
									<td style="text-align:left;">维生素C（VC）</td>
									<td><%if(dataAcid.getVc()!=null) {%><%=dataAcid.getVc()%><%}%><%else {%><%}%></td>
									<td style="text-align:left;">-（此项目普通情况下一般不予筛查）</td>
								</tr>
							</table>
							<table>
								<tr>&nbsp;</tr>
								<tr style="font-size: 14px;margin: 10px;font-weight: bold;padding:12px;">
									<td>结论：</td>
									<td><%if(dataAcid.getDescript()!=null) {%><%=dataAcid.getDescript()%><%}%><%else {%>请上传测试的尿酸数据<%}%></td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td style="width:50%;">
							<div id="BO" style="width:500px;height:300px"></div>
							<div>
								<table>
									<tr>&nbsp;</tr>
									<tr style="font-size: 14px;margin: 10px;font-weight: bold;padding:12px;">
										<td>结论：</td>
										<td><%if(bloodOxygen.getDescript()!=null) {%><%=bloodOxygen.getDescript()%>,<%}%><%else {%><%}%></td>
									</tr>
								</table>
							</div>
						</td>
						<td>
							<div id="TW" style="width:500px;height:300px"></div>
							<div>
								<table>
									<tr>&nbsp;</tr>
									<tr style="font-size: 14px;margin: 10px;font-weight: bold;padding:12px;">
										<td>结论：</td>
										<td><%if(temperature.getDescript()!=null) {%><%=temperature.getDescript()%>,<%}%><%else {%><%}%></td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
					<tr>
						<td style="width:50%;">
							<div id="BFR" style="width:500px;height:300px"></div>
							<div>
								<table>
									<tr>&nbsp;</tr>
									<tr style="font-size: 14px;margin: 10px;font-weight: bold;padding:12px;">
										<td>结论：</td>
										<td><%if(bodyFat.getDescript()!=null) {%><%=bodyFat.getDescript()%>,<%}%><%else {%><%}%></td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</body>
</html>