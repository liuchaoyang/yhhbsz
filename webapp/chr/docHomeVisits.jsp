<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>会员随访列表</title>
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
	var visitGrid;
	$(function(){
		visitGrid = $("#visits").datagrid({
			title: "",
			width: "auto",
			height: "auto",
			idField: "id",
			url: "<%=request.getContextPath()%>/chr/chrVisit_getVisitList.action?rows=10&page=1&ishandled=1",
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
				width: 100,
				align: "center"
			}, {
				field: "type",
				title: "随访项目",
				width: 100,
				align: "center",
				formatter: function(val, row){
					if(val==1){
						return "血压";
					}else if(val==2){
						return "血糖";
				    }else if(val==3){
				    	return "心脑血管";
					}
					return "";	
				}
			}, {
				field: "planFlupTime",
				title: "计划随访时间",
				width: 120,
				align: "center",
				formatter: function(val, row){
					if(val){
						return val.substring(0, 10);
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
					return '<a href="#" onclick="handle(\'' + row.id + '\')">随访</a>';
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
		top.addTab("处理随访", "<%=request.getContextPath()%>/chr/chrVisit_doVisit.action?id="+id, null);
	}
	function more(){
		top.addTab("随访管理", "<%=request.getContextPath()%>/chr/chrVisitList.jsp", null);
	}
	</script>
</head>
<body>
	<table id="visits"></table>
	<div id="moreRecords" style="display: none;">
		<a href="#" onclick="more()">更多</a>
	</div>
</body>
</html>