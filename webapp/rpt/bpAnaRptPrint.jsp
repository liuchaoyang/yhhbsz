<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.module.rpt.bean.AnalysisReport"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.util.DateUtil"%>
<%@ page import="com.yzxt.yh.constant.Constant"%>
<%
AnalysisReport report= (AnalysisReport)request.getAttribute("report");
Object[] data = report.getData();
int[] dbps = (int[])data[0];
int[] sbps = (int[])data[1];
StringBuilder dbpStr = new StringBuilder("[");
StringBuilder sbpStr = new StringBuilder("[");
StringBuilder cStr = new StringBuilder("[");
StringBuilder sStr = new StringBuilder("[");
int c = dbps.length;
for(int i=0;i<c;i++){
    dbpStr.append(i>0?",":"").append(dbps[i]);
    sbpStr.append(i>0?",":"").append(sbps[i]);
    cStr.append(i>0?",":"").append(i);
    sStr.append(i>0?",":"").append("[").append(sbps[i]).append(",").append(dbps[i]).append("]");
}
dbpStr.append("]");
sbpStr.append("]");
cStr.append("]");
sStr.append("]");
int[] p = (int[])data[3];
Integer[][] tStat = (Integer[][])data[4];
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<script src="<%=request.getContextPath()%>/resources/js/highcharts.js"></script>
	<title>血压分析报告</title>
	<style type="text/css">
	#rptTitle{
		font-family: 微软雅黑;
		color: #919191;
		font-size: 20px;
		text-align: center;
		padding: 10px;
	}
	#summaryTable td{
		font-family: 微软雅黑;
		font-size: 16px;
		color: #919191;
		padding-top: 2px;
		padding-bottom: 2px;
		border-bottom: 2px solid #919191;
	}
	.table td{
		text-align: center;
		border-color: #b6b6b6;
	}
	.tdTitle{
		background-color: #B7DFF9;
	}
	</style>
	<script type="text/javascript">
	$(function(){
		var splinechart = new Highcharts.Chart({
			chart: {
				renderTo: "splineContainer",
				type: "spline",
				margin: [35, 30, 30, 50]
			},
			exporting: {
				enabled: false
			},
			credits: {
				enabled: false
			},
			colors: [
				"#4774a8",
				"#a53b38"
			],
			title: {
				text: "",
				x: 50,
				y: 10,
				style: {
					display: "none"
				}
			},
			xAxis: {
				categories: <%=cStr%>,
				labels: {
					enabled: false
				}
			},
			yAxis: {
				allowDecimals: false,
				min: 20,
				tickInterval: 30,
				offset: -3,
				lineWidth: 2,
				gridLineWidth: 0.25,
				gridLineColor: "#262626",
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
					zIndex: 2,
					label: {
						text: "140",
						align: "left",
						x: -25,
						y: 5,
						style: {
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
						style: {
							color: "#89691e",
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
						style: {
							color: "#89691e",
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
						text: "",
						align: "right",
						rotation: 90,
						y: 20,
						style: {
							color: "#89691e"
						}
					}
				}, {
					from: 60,
					to: 90,
					zIndex: 0,
					color: "#C0FFC0",
					style: {
						opacity: 0.5
					},
					label: {
						text: "",
						align: "right",
						rotation: 90,
						y: 20,
						style: {
							color: "#89691e"
						}
					}
				}]
			},
			legend: {
				layout: "horizontal",
				backgroundColor: "#FFFFFF",
				align: "right",
				verticalAlign: 'top',
				x: 0,
				y: -10,
				floating: true,
				shadow: false,
				borderWidth: 0,
				itemHoverStyle: {
					"cursor": "default"
				}
			},
			tooltip: {
				formatter : function(){
					return this.series.name + ": " +this.y +" mmHg";
				}
			},
			plotOptions: {
				spline: {
					shadow: false,
					animation: false
				}
			},
			series: [{
				name: "舒张压",
				data: <%=dbpStr%>
			},{
				name: "收缩压",
				data: <%=sbpStr%>
			}]
		});
		// 饼图
    	var pieChart = $("#pieContainer").highcharts({
			chart: {
				plotBackgroundColor: null,
				plotBorderWidth: null,
				plotShadow: false
			},
	        tooltip: {
				pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
			},
			plotOptions: {
				pie: {
					allowPointSelect: true,
					cursor: "pointer",
					dataLabels: {
						enabled: true,
						color: "#000000",
						connectorColor: "#000000",
						format: "<b>{point.name}</b>: {point.percentage:.1f} %"
					}
				}
			},
			title: {
				style: {
					display: "none"
				}
			},
			exporting: {
				enabled:false
			},
			credits: {
				enabled: false
			},
			colors: [
				"#73F6E8",
				"#F7F4B2",
				"#F6D075",
				"#EA7D25",
				"#E94A04"
			],
			series: [{
				type: "pie",
	            name: "血压分布",
				data: [["正常血压", <%=p[0]%>], ["正常高值血压", <%=p[1]%>], ["一级高血压", <%=p[2]%>], ["二级高血压", <%=p[3]%>], ["三级高血压", <%=p[4]%>]]
			}]
    	});
		// 散点图
		var scatterChart = new Highcharts.Chart({
			chart: {
				renderTo: "scatterContianer",
				plotShadow: false,
				marginTop: 20,
				marginLeft: 35,
				marginBottom: 20,
				spacingLeft: 0,
				spacingRight: 0,
				type: "scatter",
				plotBackgroundImage: "<%=request.getContextPath()%>/resources/img/bpscatter.png",
			},
			credits: {
				enabled: false
			},
			title: {
				style: {
					display: "none"
				}
			},
			exporting: {
				enabled: false
			},
			xAxis: {
				title: {
					enabled: true,
					text: "收缩压(mmHg)",
					style: {
						display: "none"
					}
				},
				labels: {
					formatter: function () {
						if (this.value == 0 || this.value == 80 || this.value == 120 || this.value == 140 || this.value == 160 || this.value == 180) {
							return this.value;
						}
					},
					style: {
						color: "black",
						fontSize: "12px",
						fontWeight: "normal"
					}
				},
				min: 0,
				max: 200,
				allowDecimals: false,
				tickColor: "#000000",
				tickWidth: 1,
				tickLength: 5,
				tickPixelInterval: 30,
				tickPosition:'on',
				gridLineColor: null,
				gridLineWidth: 0
			},
			yAxis: {
				title: {
					text: "舒张压(mmHg)",
					style: {
						display: "none"
					}
				},
				labels: {
					formatter: function(){
						if(this.value == 0 || this.value == 60 || this.value == 80 || this.value == 90 || this.value == 100 || this.value == 110){
							return this.value;
						}
					},
					style: {
						color: "black",
						fontSize: "12px",
						fontWeight: "normal"
					}
				},
				min: 0,
				max: 120,
				tickWidth: 1,
				tickLength: 5,
				tickColor: "#000000",
				tickPosition: "on",
				tickPixelInterval: 15,
				gridLineWidth: 0,
				endOnTick: false,
				allowDecimals: false
			},
			legend: {
				layout: "horizontal",
				backgroundColor: "#F4F4F4",
				align: "right",
				verticalAlign: "top",
				x: 0,
				y: -15,
				floating: true,
				shadow: false,
				borderWidth: 0
			},
			tooltip: {
				formatter: function(){
					return " 收缩压：" + this.x + " mmHg, 舒张压：" + this.y + " mmHg";
				}
			},
			plotOptions: {
				scatter: {
					allowPointSelect: false,
					animation: false
				}
			},
			series: [{
				name: "血压值",
				data: <%=sStr%>
			}]
		});
	});
	function printHtml(){
		var divOper = document.getElementById("divOper");
		divOper.style.display = "none";
		print();
		divOper.style.display = "block";
	}
</script>
</head>
<body style="width: 760px; margin: 0 auto;">
	<div id="rptTitle">
		血压分析报告
	</div>
	<table id="summaryTable" cellpadding="0" cellspacing="0" border="0" style="width: 760px;">
 		<tr>
			<td width="12%" align="right">
				身份证号：
			</td>
			<td width="25%" nowrap="nowrap">
				<%=StringUtil.ensureStringNotNull(report.getIdCard())%>
			</td>
			<td width="12%" align="right" nowrap="nowrap">
				<span>测量时段：</span>
			</td>
			<td width="27%" nowrap="nowrap">
				<span style="font-size: 13px;"><%=DateUtil.toHtmlDate(report.getExamBeginTime())%></span>~<span style="font-size: 13px;"><%=DateUtil.toHtmlDate(report.getExamEndTime())%></span>
			</td>
			<td width="12%" align="right" nowrap="nowrap">
				<span>报告时间：</span>
			</td>
			<td nowrap="nowrap">
				<span style="font-size: 13px;"><%=DateUtil.toHtmlDate(report.getCreateTime())%></span>
			</td>
		</tr>
		<tr class="title_tr">
			<td align="right" nowrap="nowrap">
				<span>姓名：</span>
 			</td>
			<td nowrap="nowrap">
				<%=report.getCustomerName()%>
			</td>
			<td align="right" nowrap="nowrap">
				<span >性别：</span>
  			</td>
			<td nowrap="nowrap">
				<%
				int sex = report.getSex()!=null?report.getSex().intValue():0;
				%>
				<%=sex==1?"男":""%><%=sex==2?"女":""%>
			</td>
			<td align="right" nowrap="nowrap">
				<span >年龄：</span>
			</td>
			<td nowrap="nowrap">
				<%=report.getBirthday()!=null ? DateUtil.getAge(report.getBirthday(), null) + "" : ""%>
			</td>
		</tr>
	</table>
	<div style="width:747px;margin: 0 auto;height: 20px;line-height: 20px;font-family: Microsoft YaHei;font-size: 14px;padding-top: 30px;">
		<span style="color: #006ebd;">血压曲线图：</span>
	</div>
	<div id="splineContainer" style="width:100%;height: 200px;margin: 0px auto;"></div>
	<div style="font-family: Microsoft YaHei;font-size: 14px;">
		<div style="width: 360px;height: 260px;float: left;border: 1px solid #9D9D9D;margin: 0px;">
			<div style="width: 120px;height: 20px;line-height: 20px;color: #FFFFFF;background-color: #2080C5;margin-left: 0px;padding-left: 10px;">
				血压分布(饼图):
			</div>
			<div id="pieContainer" style="width: 360px; height: 240px; margin-top: 0px; margin-left: 0px;"></div>
		</div>
		<div style="width: 377px; height: 260px;float: right; margin-left: 5px; border: 1px solid #9d9d9d;">
			<div style="width: 140px;height: 20px;line-height: 20px;color: #FFFFFF;background-color: #2080C5;margin-left: 0px;padding-left: 10px;">
				血压分布(散点图):
			</div>
			<div id="scatterContianer" style="width: 370px;height: 240px;margin-top: 0px;margin-left: 5px;">
			</div>
		</div>
		<div style="clear: both;">
		</div>
	</div>
	<div style="width:747px;margin: 0 auto;height: 20px;line-height: 20px;font-family: Microsoft YaHei;font-size: 14px;padding-top: 20px;">
		<span style="color: #006ebd;">血压统计表：</span>
	</div>
	<table cellpadding="0" cellspacing="0" class="table">
		<tr>
			<td rowspan="5" style="width:60px;">
				收&nbsp;缩&nbsp;压
			</td>
			<td class="tdTitle" style="width: 120px;">统计时间</td>
			<td class="tdTitle">测量次数</td>
			<td class="tdTitle">最小值</td>
			<td class="tdTitle">最大值</td>
			<td class="tdTitle">平均值</td>
		</tr>
		<tr>
			<td>早间(06:00-09:59)</td>
			<td><%=tStat[0][0]%></td>
			<td><%=tStat[0][1]!=null?tStat[0][4]:"-"%></td>
			<td><%=tStat[0][2]!=null?tStat[0][5]:"-"%></td>
			<td><%=tStat[0][3]!=null?tStat[0][6]:"-"%></td>
		</tr>
		<tr>
			<td>白天(10:00-18:59)</td>
			<td><%=tStat[1][0]%></td>
			<td><%=tStat[1][1]!=null?tStat[1][4]:"-"%></td>
			<td><%=tStat[1][2]!=null?tStat[1][5]:"-"%></td>
			<td><%=tStat[1][3]!=null?tStat[1][6]:"-"%></td>
		</tr>
		<tr>
			<td> 晚间(19:00-05:59)</td>
			<td><%=tStat[2][0]%></td>
			<td><%=tStat[2][1]!=null?tStat[2][4]:"-"%></td>
			<td><%=tStat[2][2]!=null?tStat[2][5]:"-"%></td>
			<td><%=tStat[2][3]!=null?tStat[2][6]:"-"%></td>
		</tr>
		<tr>
			<td>全天(00:00-23:59)</td>
			<td><%=tStat[3][0]%></td>
			<td><%=tStat[3][1]!=null?tStat[3][4]:"-"%></td>
			<td><%=tStat[3][2]!=null?tStat[3][5]:"-"%></td>
			<td><%=tStat[3][3]!=null?tStat[3][6]:"-"%></td>
		</tr>
	</table>
	<table cellpadding="0" cellspacing="0" class="table" style="margin-top: 10px;">
		<tr>
			<td rowspan="5" style="width:60px;">
				舒&nbsp;张&nbsp;压
			</td>
			<td class="tdTitle" style="width: 120px;">统计时间</td>
			<td class="tdTitle">测量次数</td>
			<td class="tdTitle">最小值</td>
			<td class="tdTitle">最大值</td>
			<td class="tdTitle">平均值</td>
		</tr>
		<tr>
			<td>早间(06:00-09:59)</td>
			<td><%=tStat[0][0]%></td>
			<td><%=tStat[0][1]!=null?tStat[0][1]:"-"%></td>
			<td><%=tStat[0][2]!=null?tStat[0][2]:"-"%></td>
			<td><%=tStat[0][3]!=null?tStat[0][3]:"-"%></td>
		</tr>
		<tr>
			<td>白天(10:00-18:59)</td>
			<td><%=tStat[1][0]%></td>
			<td><%=tStat[1][1]!=null?tStat[1][1]:"-"%></td>
			<td><%=tStat[1][2]!=null?tStat[1][2]:"-"%></td>
			<td><%=tStat[1][3]!=null?tStat[1][3]:"-"%></td>
		</tr>
		<tr>
			<td> 晚间(19:00-05:59)</td>
			<td><%=tStat[2][0]%></td>
			<td><%=tStat[2][1]!=null?tStat[2][1]:"-"%></td>
			<td><%=tStat[2][2]!=null?tStat[2][2]:"-"%></td>
			<td><%=tStat[2][3]!=null?tStat[2][3]:"-"%></td>
		</tr>
		<tr>
			<td>全天(00:00-23:59)</td>
			<td><%=tStat[3][0]%></td>
			<td><%=tStat[3][1]!=null?tStat[3][1]:"-"%></td>
			<td><%=tStat[3][2]!=null?tStat[3][2]:"-"%></td>
			<td><%=tStat[3][3]!=null?tStat[3][3]:"-"%></td>
		</tr>
	</table>
	<div style="width:747px;margin: 0 auto;height: 20px;line-height: 20px;font-family: Microsoft YaHei;font-size: 14px;padding-top: 20px;">
		<span style="color: #006ebd;">血压评估建议：</span>
	</div>
	<div style="padding: 5px;">
		<%=StringUtil.ensureStringNotNull(report.getSuggest())%>
		<input type="hidden" name="id" value="<%=StringUtil.ensureStringNotNull(report.getId())%>" />
	</div>
	<div id="divOper" style="padding: 10px;text-align: center;">
		<a href="javascript:printHtml();" class="easyui-linkbutton">打印</a>
	</div>
</body>
</html>