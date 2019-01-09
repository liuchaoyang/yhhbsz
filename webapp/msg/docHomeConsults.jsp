<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>会员咨询列表</title>
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
	var consultGrid;
	$(function(){
		consultGrid = $("#consults").datagrid({
			title: "",
			width: "auto",
			height: "auto",
			idField: "id",
			url: "<%=request.getContextPath()%>/msg/consultGuide_getconsultList.action?rows=10&page=1&consultState=1",
			rownumbers: true,
			pagination: false,
			singleSelect: true,
			nowrap: true,
			striped: true,
			fitColumns: true,
			loadMsg: "数据加载中,请稍候...",
			columns: [[{
				field: "memberName",
				title: "会员名称",
				width: 60,
				align: "center"
			}, {
				field: "consultTitle",
				title: "内容",
				width: 150,
				align: "center"
			},/*  {
				field: "consultContext",
				title: "内容",
				width: 200,
				align: "left",
			}, */ {
				field: "consultTime",
				title: "提问时间",
				width: 120,
				align: "center"
			}, {
				field: "oper",
				title: " ",
				width: 70,
				align: "center",
				formatter: function(val, row){
					return '<a href="#" onclick="handle(\'' + row.id + '\')">回复</a>';
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
	function handle(id){
		parent.showWin("咨询指导", "<%=request.getContextPath()%>/msg/consultGuide_toCheck.action?operType=update&id="+id, 650, 450);
	}
	function more(){
		top.addTab("会员咨询", "<%=request.getContextPath()%>/msg/consults.jsp", null);
	}
	</script>
</head>
<body>
	<table id="consults"></table>
	<div id="moreRecords" style="display: none;">
		<a href="#" onclick="more()">更多</a>
	</div>
</body>
</html>