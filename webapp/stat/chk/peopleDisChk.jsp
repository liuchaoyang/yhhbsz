<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../../common/inc.jsp"%>
	<title>检测人群分布</title>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/highcharts.js"></script>
	<style type="text/css">
	.areaType{
		width: 120px;
	}
	</style>
	<script type="text/javascript">
	function query(){
		$.ajax({
			url: "<%=request.getContextPath()%>/stat/chk_queryPeoleDis.action",
			data: $("#filterForm").serializeObject(),
			type: "POST",
			dataType: "json",
			async: true,
			timeout: 30000,
			success: function(data){
				var sexDiss = data.sexDiss;
				var sexDObjs = new Array();
				for(var i=0;i<sexDiss.length;i++){
					sexDObjs.push({"name": sexDiss[i].name, "y": sexDiss[i].num});
				}
				var ageDiss = data.ageDiss;
				var ageDObjs = new Array();
				for(var i=0;i<ageDiss.length;i++){
					ageDObjs.push({"name": ageDiss[i].name, "y": ageDiss[i].num});
				}
				document.getElementById("rstContainer").style.display = "block";
				$("#sexChart").highcharts({
					chart: {
						plotBackgroundColor: null,
						plotBorderWidth: null,
						plotShadow: false,
						type: "pie"
					},
					title: {
						text: "检测人群性别分布"
					},
					tooltip: {
						pointFormat: '{series.name}: <b>{point.y}</b>'
					},
					plotOptions: {
						pie: {
							allowPointSelect: true,
							cursor: "pointer",
							dataLabels: {
								enabled: true,
								format: '<b>{point.name}</b>: {point.percentage:.1f} %',
								style: {
									color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
								}
							}
						}
					},
					series: [{
						name: "人数",
						colorByPoint: true,
						data: sexDObjs
					}]
				});
				$("#ageChart").highcharts({
					chart: {
						plotBackgroundColor: null,
						plotBorderWidth: null,
						plotShadow: false,
						type: "pie"
					},
					title: {
						text: "检测人群年龄分布"
					},
					tooltip: {
						pointFormat: '{series.name}: <b>{point.y}</b>'
					},
					plotOptions: {
						pie: {
							allowPointSelect: true,
							cursor: "pointer",
							dataLabels: {
								enabled: true,
								format: '<b>{point.name}</b>: {point.percentage:.1f} %',
								style: {
									color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
								}
							}
						}
					},
					series: [{
						name: "人数",
						colorByPoint: true,
						data: ageDObjs
					}]
				});
			},
			error: function(){
				$.messager.alert("提示信息", "统计失败.", "error");
			}
		});
	}
	function clear(){
		document.getElementById("filterForm").reset();
	}
</script>
</head>
<body>
	<div class="sectionTitle">统计条件</div>
	<div>
		<form id="filterForm">
			<table class="table">
				<tr>
					<td class="td_title" nowrap="nowrap" width="20%">检测日期:</td>
					<td nowrap="nowrap">
						<input class="Wdate" style="width: 100px;" id="startChkDate" name="startChkDate" readonly="readonly" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'endChkDate\')}'})" />
						&nbsp;至&nbsp;
						<input class="Wdate" style="width: 100px;"id="endChkDate" name="endChkDate" readonly="readonly" onclick="WdatePicker({minDate:'#F{$dp.$D(\'startChkDate\')}'})" />
					</td>
					<td width="120">
						<a class="easyui-linkbutton" href="javascript:query()">统计</a>
						&nbsp;
						<a class="easyui-linkbutton" href="javascript:clear()">重置</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div id="rstContainer" style="margin-top: 20px;">
		<table style="width: 100%">
			<tr>
				<td style="width: 50%;">
					<div id="sexChart" style="min-width: 300px;height: 400px;"></div>
				</td>
				<td>
					<div id="ageChart" style="min-width: 300px;height: 400px;"></div>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>