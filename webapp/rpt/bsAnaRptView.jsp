<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.sql.Timestamp"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="com.yzxt.yh.module.rpt.bean.AnalysisReport"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.util.DateUtil"%>
<%@ page import="com.yzxt.yh.constant.Constant"%>
<%
AnalysisReport report= (AnalysisReport)request.getAttribute("report");
Boolean editable = (Boolean)request.getAttribute("editable");
Object[] data = report.getData();
@SuppressWarnings("unchecked")
List<Object[]>[] dataObjs = (List<Object[]>[])data[0];
StringBuilder kfStr= new StringBuilder("[");
List<Object[]> dataKf = dataObjs[0];
for(int i=0;i<dataKf.size();i++){
    Object[] objs= dataKf.get(i);
    kfStr.append(i>0?",":"");
    kfStr.append("[").append(((Date)objs[2]).getTime()).append(",").append(objs[1]).append("]");
}
kfStr.append("]");
StringBuilder chStr= new StringBuilder("[");
List<Object[]> dataCh = dataObjs[1];
for(int i=0;i<dataCh.size();i++){
    Object[] objs= dataCh.get(i);
    chStr.append(i>0?",":"");
    chStr.append("[").append(((Date)objs[2]).getTime()).append(",").append(objs[1]).append("]");
}
chStr.append("]");
StringBuilder ftStr= new StringBuilder("[");
List<Object[]> dataFt = dataObjs[2];
for(int i=0;i<dataFt.size();i++){
    Object[] objs= dataFt.get(i);
    ftStr.append(i>0?",":"");
    ftStr.append("[").append(((Date)objs[2]).getTime()).append(",").append(objs[1]).append("]");
}
ftStr.append("]");
int[] p = (int[])data[1];
int[][] tStatCount = (int[][])data[2];
double[][] tStatVal = (double[][])data[3];
DecimalFormat df = new DecimalFormat("#0.##");
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<script src="<%=request.getContextPath()%>/resources/js/highcharts.js"></script>
	<title>血糖分析报告</title>
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
		var splineChart = new Highcharts.Chart({
			chart: {
				renderTo: "splineContainer",
				type: "line",
				margin: [35, 30, 30, 50],
			},
			title: {
				style: {
					display: "none"
				}
			},
			credits: {
				enabled: false
			},
			exporting: {
				enabled: false
			},
			xAxis: {
				lineWidth: 2,
				lineColor: "#DFE7EF",
				gridLineWidth: 0.25,
				gridLineColor: "#262626",
				tickPixelInterval: 140,
				type: "datetime",
				dateTimeLabelFormats: {
					day: "%Y-%m-%d",
					week: "%Y-%m-%d",
					month: "%Y-%m-%d",
					year: "%Y-%m-%d"
				},
				title: {
					style: {
						display: "none"
					}
				},
				labels: {
					enabled: false
				}
			},
			yAxis: {
				min: 0,
 				max: 15,
 				tickInterval: 1,
 				title: {
 					text: "mmol/L",
 					style: {
 						color: "#6D869F",
 						fontSize: "12px",
 						fontWeight: "normal"
 					}
				},
				plotLines: [{
					value: 11.0,
					color: "#C2B28A",
					dashStyle: "solid",
					width: 2,
					zIndex: 2,
					label: {
						text: "11.0",
						align: "left",
						x: -25,
						y: 5,
						style: {
							color: "#89691E",
							display: "none"
						}
					}
				}, {
					value: 7.0,
					color: "#C2B28A",
					dashStyle: "solid",
					width: 2,
					zIndex: 2,
					label: {
						text: "7.0",
						align: "left",
						x: -25,
						y: 5,
						style: {
							color: "#89691E",
							display: "none"
						}
					}
				}],
				plotBands: [{
					from: 7.0,
					to: 11.0,
					zIndex: 0,
					color: "#FBF5E7",
					style: {
						opacity: 0.3
					},
					label: {
						text: "",
						align: "right",
						rotation: 90,
						y: 20,
						style: {
							color: "#89691E"
						}
					}
				}]
			},
			legend: {
				layout: "vertical",
				backgroundColor: "#FFFFFF",
				align: "right",
				verticalAlign: "top",
				floating: true,
				shadow: true,
				borderWidth: 0,
				itemHoverStyle: {
					"cursor": "default"
				}
			},
			tooltip: {
				enabled: true,
				shared: true,
				crosshairs: true,
				xDateFormat: "%Y-%m-%d %H:%M:%S",
				valueSuffix: "mmol/L"
			},
			plotOptions: {
				line: {
					dataLabels: {
						enabled: true
					},
					enableMouseTracking: false
				}
			},
			series: [{name: "空腹血糖", data: <%=kfStr%>}, {name: "餐后血糖", data: <%=chStr%>}, {name: "服糖后血糖", data: <%=ftStr%>}]
		});
		// 饼图
		var pieChart = new Highcharts.Chart({
			chart: {
				renderTo: "pieContainer",
				plotShadow: false,
			},
			exporting: {
				enabled: false
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
			title: {
				style: {
					display: "none"
				}
			},
			tooltip: {
				pointFormat: "{series.name}: <b>{point.percentage:.1f}%</b>"
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
			series: [{
				type: "pie",
				name: "血糖分布",
				data: [["正常血糖", <%=p[0]%>], ["疑似糖尿病", <%=p[1]%>], ["疑似低血糖", <%=p[2]%>]]
			}]
		});
	});
	function printRpt(){
		window.open("<%=request.getContextPath()%>/rpt/anaRpt_toDetail.action?operType=print&id=<%=report.getId()%>", "anaRpt",
			"fullscreen=yes,scrollbars=yes,resizable=yes");
	}
	function closeIt(){
		try{
			var tc = parent.$("#tb");
			var tab = tc.tabs("getSelected");
			if (tab) {
				tc.tabs("close", tab.panel("options").title);
			}
		}catch(e){}
	}
	function save(){
		// 验证
		var suggest = document.getElementById("suggest");
		if(suggest.value.length > 250){
			$.messager.alert("提示信息", "血糖评估建议字数不能超过250个！", "error", function(){
				suggest.focus();
			});
			return;
		}
		$("#theForm").form("submit", {
			url: "<%=request.getContextPath()%>/rpt/anaRpt_saveSuggest.action",
			dataType : "json",
			success : function(data) {
				data =  $.parseJSON(data);
				if(data.state == 1){
					$.messager.alert("提示信息", data.msg?data.msg:"保存成功。", "info", function(){
					});
					refreshPage("${param.pPageId}");
				}else{
					$.messager.alert("提示信息", data.msg?data.msg:"保存失败。", "error");
				}
			}
		});
	}
</script>
</head>
<body style="width: 760px; margin: 0 auto;">
	<div id="rptTitle">
		血糖分析报告
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
		<span style="color: #006ebd;">血糖走势图：</span>
	</div>
	<div id="splineContainer" style="min-width: 310px;height: 300px;margin: 0 auto;"></div>
	<div style="width:747px;margin: 0 auto;height: 20px;line-height: 20px;font-family: Microsoft YaHei;font-size: 14px;padding-top: 20px;">
		<span style="color: #006ebd;">血糖监测数据分布(饼图):</span>
	</div>
	<div id="pieContainer" style="height: 200px;margin-top: auto;margin-left: auto;"></div>
	<div style="width:747px;margin: 0 auto;height: 20px;line-height: 20px;font-family: Microsoft YaHei;font-size: 14px;padding-top: 20px;">
		<span style="color: #006EBD;">血糖统计表：</span>
	</div>
	<table cellpadding="0" cellspacing="0" class="table">
		<tr>
			<td class="tdTitle">测量方式</td>
			<td class="tdTitle">测量次数</td>
			<td class="tdTitle">最小值</td>
			<td class="tdTitle">最大值</td>
			<td class="tdTitle">平均值</td>
			<td class="tdTitle">高于正常值次数</td>
			<td class="tdTitle">低于正常值次数</td>
		</tr>
		<tr>
			<td>空腹血糖测量</td>
			<td><%=tStatCount[0][0]%></td>
			<td><%=tStatCount[0][0]>0?df.format(tStatVal[0][0]):"-"%></td>
			<td><%=tStatCount[0][0]>0?df.format(tStatVal[0][1]):"-"%></td>
			<td><%=tStatCount[0][0]>0?df.format(tStatVal[0][2]):"-"%></td>
			<td><%=tStatCount[0][0]>0?tStatCount[0][1]+"":"-"%></td>
			<td><%=tStatCount[0][0]>0?tStatCount[0][2]+"":"-"%></td>
		</tr>
		<tr>
			<td>餐后血糖测量</td>
			<td><%=tStatCount[1][0]%></td>
			<td><%=tStatCount[1][0]>0?df.format(tStatVal[1][0]):"-"%></td>
			<td><%=tStatCount[1][0]>0?df.format(tStatVal[1][1]):"-"%></td>
			<td><%=tStatCount[1][0]>0?df.format(tStatVal[1][2]):"-"%></td>
			<td><%=tStatCount[1][0]>0?tStatCount[1][1]+"":"-"%></td>
			<td><%=tStatCount[1][0]>0?tStatCount[1][2]+"":"-"%></td>
		</tr>
		<tr>
			<td>服糖2小时候血糖测量</td>
			<td><%=tStatCount[2][0]%></td>
			<td><%=tStatCount[2][0]>0?df.format(tStatVal[2][0]):"-"%></td>
			<td><%=tStatCount[2][0]>0?df.format(tStatVal[2][1]):"-"%></td>
			<td><%=tStatCount[2][0]>0?df.format(tStatVal[2][2]):"-"%></td>
			<td><%=tStatCount[2][0]>0?tStatCount[2][1]+"":"-"%></td>
			<td><%=tStatCount[2][0]>0?tStatCount[2][2]+"":"-"%></td>
		</tr>
	</table>
	<div style="width:747px;margin: 0 auto;height: 20px;line-height: 20px;font-family: Microsoft YaHei;font-size: 14px;padding-top: 20px;">
		<span style="color: #006ebd;">血糖评估建议：</span>
	</div>
	<form id="theForm" style="margin: 0px;padding: 5px;" accept-charset="UTF-8" method="post">
		<textarea style="width: 95%;height: 60px;" <%=!editable ? "disabled=\"disabled\"" : ""%>
			id="suggest" name="suggest"><%=StringUtil.ensureStringNotNull(report.getSuggest())%></textarea>
		<input type="hidden" name="id" value="<%=StringUtil.ensureStringNotNull(report.getId())%>" />
	</form>
	<div style="padding: 10px;text-align: center;">
		<%
		if(editable)
		{
		%>
		<a href="javascript:save()" class="easyui-linkbutton">保存</a>
		&nbsp;
		<%
		}
		%>
		<a href="javascript:printRpt()" class="easyui-linkbutton">打印</a>
		&nbsp;
		<a href="javascript:closeIt()" class="easyui-linkbutton">关闭</a>
	</div>
</body>
</html>