<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="expires" content="0" />
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3" />
	<meta http-equiv="description" content="随访数据统计" />
	<%@ include file="../common/inc.jsp"%>
	<script src="<%=request.getContextPath()%>/resources/js/highcharts.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/modules/exporting.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery/jquery-1.8.0.min.js"></script>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/css/flow.css" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/highcharts.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/highcharts-add.js"></script>
	<style type="text/css">
	body{
		width: 800px;
		margin-left: auto;
		margin-right: auto;
	}
	.lvlLbl{
		vertical-align: middle;
	}
	.lvlRd{
		vertical-align: middle;
	}
	.areaSel{
		width: 120px;
		margin-right: 8px;
	}
	</style>
	<script type="text/javascript">
    $(function () {
	    $('#container').highcharts({
	        chart: {
	            type: 'column'
	        },
	        title: {
	            text: '公共卫生服务开展情况'
	        },
	        xAxis: {
	            categories: ['桥北社区居委会', '华新社区居委会', '嘉陵社区居委会', '适中社区居委会', '建新社区居委会', '大兴社区居委会']
	        },
	        exporting: {
	        	enabled:false
	        },
	        yAxis: {
	            min: 0,
	            title: {
	                text: '公共卫生服务开展次数'
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
	            y: 20,
	            floating: true,
	            backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColorSolid) || 'white',
	            borderColor: '#CCC',
	            borderWidth: 1,
	            shadow: false
	        },
	        tooltip: {
	            formatter: function() {
	                return '<b>'+ this.x +'</b><br/>'+
	                    this.series.name +': '+ this.y +'<br/>'+
	                    'Total: '+ this.point.stackTotal;
	            }
	        },
	        plotOptions: {
	            column: {
	                stacking: 'normal',
	                dataLabels: {
	                    enabled: true,
	                    color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white'
	                }
	            }
	        },
	        series: [{
	            name: '开展数',
	            data: [124, 121, 90, 67,89,245]
	        }]
	    });
	});				
    </script>
</head>
<body style="width: 760px; margin: 0 auto;padding-top: 5px;">
	<div class="sectionTitle">统计条件</div>
	<div>
		<form id="filterForm">
			<table class="table">
				<tr>
					<td width="10%" class="td_title">开展日期:</td>
					<td width="40%" nowrap="nowrap">
						<input class="Wdate" style="width: 90px;" id="startDate" name="startDate" value="" readonly="readonly" onclick="WdatePicker({})" />
						至
						<input class="Wdate" style="width: 90px;" id="endDate" name="endDate" value="" readonly="readonly" onclick="WdatePicker({})" />
					</td>
					<td width="10%" class="td_title">服务项目:</td>
					<td>
						<select>
							<option value="1">健康教育服务</option>
							<option value="2">预防接种服务</option>
							<option value="3">0～6岁儿童健康管理服务</option>
							<option value="4">孕产妇健康管理服务</option>
							<option value="5">老年人健康管理服务</option>
							<option value="6">重性精神疾病患者管理服务</option>
							<option value="7">传染病及突发公共卫生事件报告和处理服务</option>
							<option value="8">卫生监督协管服务</option>
							<option value="9">65岁以上老年人中医药健康服务</option>
							<option value="10">0－36个月儿童中医药健康服务</option>
							<option value="11">叶酸服务</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="td_title">区域:</td>
					<td nowrap="nowrap" colspan="3">
						<select id="province" name="province" class="areaSel" onchange="changeCity(this.value);">
							<option value="">请选择省份</option>
						</select>
						<select id="city" name="city" class="areaSel" onclick="changeCounty(this.value);">
							<option value="">请选择市</option>
						</select>
						<select id="county" name="county" class="areaSel" onclick="changeTown(this.value);">
							<option value="">请选择区县</option>
						</select>
						<select id="town" name="town" class="areaSel" onclick="changeVillage(this.value);">
							<option value="">请选择乡镇</option>
						</select>
						<select id="village" name="village" class="areaSel">
							<option value="">请选择村</option>
						</select>
					</td>
				</tr>
				<tr>
					<td colspan="4" class="td_oper">
						<a class="easyui-linkbutton" href="javascript:staticExam()">统计</a>
						<a class="easyui-linkbutton" href="javascript:clear()">重置</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div id="container" style="min-width:700px;height:400px"></div>
</body>
</html>