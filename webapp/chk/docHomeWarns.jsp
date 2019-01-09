<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.GregorianCalendar"%>
<!DOCTYPE html>
<html>
<head>
	<%
  //设置每秒刷新一次
  response.setIntHeader("Refresh", 20);
  //获取当前时间
   %>
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
			url: "<%=request.getContextPath()%>/chk/checkWarn_queryDocHomeWarn.action?haveNotDealWarn=Y",
			rownumbers: true,
			pagination: false,
			singleSelect: true,
			nowrap: true,
			striped: true,
			fitColumns: true,
			loadMsg: "数据加载中,请稍候...",
			columns: [[{
				field: "name",
				title: "会员名称",
				width: 60,
				align: "center"
			}, {
				field: "warnTime",
				title: "最新预警时间",
				width: 110,
				align: "center"
			}, {
				field: "level",
				title: "最新预警等级",
				width: 80,
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
				field: "descript",
				title: "最新预警内容",
				width: 200,
				align: "center"
			}, {
				field: "oper",
				title: " ",
				width: 70,
				align: "center",
				formatter: function(val, row){
					return '<a href="#" onclick="handle(\'' + row.custId + '\')">处理</a>';
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