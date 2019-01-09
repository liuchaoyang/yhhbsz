<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>最新资讯列表</title>
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
			url: "<%=request.getContextPath()%>/msg/info_queryCustHomeInfo.action?rows=10&page=1",
			rownumbers: true,
			pagination: false,
			singleSelect: true,
			nowrap: true,
			striped: true,
			fitColumns: true,
			loadMsg: "数据加载中,请稍候...",
			columns: [[{
				field: "title",
				title: "标题",
				width: 160,
				align: "center"
			}, {
				field: "summary",
				title: "主要内容",
				width: 200,
				align: "center"
			}, {
				field: "createTime",
				title: "发布时间",
				width: 110,
				align: "center",
				formatter: function(val, row){
					if(val){
						return val.substring(0, 19);
					}else{
						return "";
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
		top.addTab("查看资讯", "<%=request.getContextPath()%>/msg/info_view.action?id="+id, null);
	}
	function more(){
		top.addTab("健康资讯", "<%=request.getContextPath()%>/msg/info_list.action", null);
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