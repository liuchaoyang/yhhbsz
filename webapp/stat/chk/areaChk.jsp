<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../../common/inc.jsp"%>
	<title>检测数据分析</title>
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
			url: "<%=request.getContextPath()%>/stat/chk_queryAreaChk.action",
			data: $("#filterForm").serializeObject(),
			type: "POST",
			dataType: "json",
			async: true,
			timeout: 30000,
			success: function(data){
				var cates = new Array();
				var normalNums = new Array();
				var warnNums = new Array();
				for(var i=0;i<data.length;i++){
					cates.push(data[i].chkItemName);
					normalNums.push(data[i].num-data[i].warnNum);
					warnNums.push(data[i].warnNum);
				}
				document.getElementById("rstContainer").style.display = "block";
				$("#chart").highcharts({
					chart: {
						type: "column"
					},
					title: {
						text: "检测数据分析"
					},
					xAxis: {
						categories: cates
					},
					yAxis: {
						min: 0,
						title: {
							text: "检测次数"
						},
						stackLabels: {
							enabled: true,
							style: {
								fontWeight: "bold",
								color: (Highcharts.theme && Highcharts.theme.textColor) || "gray"
							}
						}
					},
					legend: {
						align: "right",
						x: -30,
						verticalAlign: "top",
						y: 25,
						floating: true,
						backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || "white",
						borderColor: "#CCC",
						borderWidth: 1,
						shadow: false
					},
					tooltip: {
						formatter: function () {
							return "<b>" + this.x + "</b><br/>" +
								this.series.name + ": " + this.y + "<br/>" + "总检测数: " + this.point.stackTotal;
						}
					},
					plotOptions: {
						column: {
							stacking: "normal",
							dataLabels: {
								enabled: true,
								color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || "white",
								style: {
									textShadow: "0 0 3px black"
								}
							}
						}
					},
					series: [{
						name: "异常检测数",
						data: warnNums,
						color: "red"
					}, {
						name: "正常检测数",
						data: normalNums,
						color: "green"
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
		<div id="chart" style="min-width: 400px;height: 400px;"></div>
	</div>
</body>
</html>