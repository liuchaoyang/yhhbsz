<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>我的咨询列表</title>
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
			url: "<%=request.getContextPath()%>/msg/consultGuide_getconsultList.action?rows=10&page=1",
			rownumbers: true,
			pagination: false,
			singleSelect: true,
			nowrap: true,
			striped: true,
			fitColumns: true,
			loadMsg: "数据加载中,请稍候...",
			columns: [[{
				field: "consultTitle",
				title: "标题",
				width: 130,
				align: "center"
			}, {
				field: "consultTime",
				title: "提问时间",
				width: 70,
				align: "left",
				formatter: function(val){
					if(val){
						return val.substring(0, 10);
					}
					return "";
				}
			}, {
				field: "doctorName",
				title: "咨询医生",
				width: 70,
				align: "center"
			}, {
				field: "guideContext",
				title: "医生指导",
				width: 140,
				align: "center"
			}, {
				field: "guideTime",
				title: "指导时间",
				width: 70,
				align: "center",
				formatter: function(val){
					if(val){
						return val.substring(0, 10);
					}
					return "";
				}
			}, {
				field: "oper",
				title: " ",
				width: 70,
				align: "center",
				formatter: function(val, row){
					return '<a href="#" onclick="handle(\'' + row.id + '\')">查看</a>';
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
		parent.showWin("查看咨询详情", "<%=request.getContextPath()%>/msg/consultGuide_toCheck.action?operType=detail&id="+id, 650, 450);
	}
	function more(){
		top.addTab("我的咨询", "<%=request.getContextPath()%>/msg/consultGuide_toMyConsult.action", null);
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