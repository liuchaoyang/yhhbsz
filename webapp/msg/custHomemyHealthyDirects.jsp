<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>健康指导</title>
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
			url: "<%=request.getContextPath()%>/msg/heaGuide_getPersonalList.action?rows=10&page=1",
			rownumbers: true,
			pagination: false,
			singleSelect: true,
			nowrap: true,
			striped: true,
			fitColumns: true,
			loadMsg: "数据加载中,请稍候...",
			columns: [[{
				field: "directReason",
				title: "指导原因",
				width: 150,
				align: "center"
			}, {
				field: "memo",
				title: "备注",
				width: 100,
				align: "left"
			}, {
				field: "doctorName",
				title: "指导医生",
				width: 70,
				align: "center"
			}, {
				field: "createTime",
				title: "指导时间",
				width: 110,
				align: "center",
				formatter: function(val){
					if(val){
						return val.substring(0, 19);
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
		parent.showWin("健康指导", "<%=request.getContextPath()%>/msg/heaGuide_toCheck.action?id="+id, 650, 350);
	}
	function more(){
		top.addTab("健康指导", "<%=request.getContextPath()%>/msg/myHealthyDirects.jsp", null);
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