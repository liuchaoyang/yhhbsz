<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>首页预警会员列表</title>
	<style type="text/css">
	body{
		padding: 0px;
		margin: 0px;
	}
	.panel-body{
		border: none;
		border-bottom: 1px solid #95B8E7;
	}
	#moreRecords{
		padding: 5px;
		text-align: right;
	}
	</style>
	<script type="text/javascript">
	var custWarnGrid;
	$(function(){
		custWarnGrid = $("#custWarns").datagrid({
			title: "",
			width: "auto",
			height: "auto",
			idField: "id",
			url: "<%=request.getContextPath()%>/chk/checkWarn_myWarnsList.action?rows=10&page=1",
			rownumbers: true,
			pagination: false,
			singleSelect: true,
			nowrap: true,
			striped: true,
			fitColumns: true,
			loadMsg: "数据加载中,请稍候...",
			columns: [[{
				field: "type",
				title: "预警项目",
				width: 60,
				align: "center",
				formatter: function(val){
					if(val=="xy"){
						return "血压";
					}else if(val=="xt"){
						return "血糖";
					}else if(val=="xl"){
						return "心率";
					}else if(val=="xo"){
						return "血氧";
					}else if(val=="tw"){
						return "体温";
					}else if(val=="dgc"){
						return "总胆固醇";
					}else if(val=="ns"){
						return "尿酸";
					}else if(val=="nsfx"){
						return "尿常规";
					}else if(val=="tz"){
						return "体脂率";
					}
					return "";
				}
			}, {
				field: "descript",
				title: "预警内容",
				width: 200,
				align: "center"
			}, {
				field: "level",
				title: "预警等级",
				width: 60,
				align: "center",
				formatter: function(val){
					var state = "";
					if(val==1){
						state = "I级";
					}else if(val==2){
						state = "II级";
					}else if(val==3){
						state = "III级";
					}
					return state;
				}
			}, {
				field: "warnTime",
				title: "预警时间",
				width: 110,
				align: "center",
				formatter: function(val){
					if(val){
						return val.substring(0, 19);
					}
					return "";
				}
			}]],
			onLoadSuccess: function(data){
				if(data && data.total > 10){
					document.getElementById("moreRecords").style.display = "block";
				}else{
					document.getElementById("moreRecords").style.display = "none";
				}
			}
		});
	});
	function handle(custId){
		top.addTab("详细预警", "<%=request.getContextPath()%>/chk/checkWarn_toDetailWarn.action?id=" + custId, null);
	}
	function more(){
		top.addTab("预警消息", "<%=request.getContextPath()%>/chk/custWarnings.jsp", null);
	}
	</script>
</head>
<body>
	<table id="custWarns"></table>
	<div id="moreRecords" style="display: none;">
		<a href="#" onclick="more()">更多</a>
	</div>
</body>
</html>