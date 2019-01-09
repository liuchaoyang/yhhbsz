<%@ page language="java" pageEncoding="UTF-8"%>
<% 
	String custId = (String)request.getAttribute("custId");
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>我的分析报告列表</title>
	<style type="text/css">
	</style>
	<script type="text/javascript">
	var anaRptGrid;
	$(function(){
		anaRptGrid = $("#anaRpt").datagrid({
			title: "我的分析报告列表",
			width: "auto",
			height: "auto",
			idField: "id",
			rownumbers: true,
			pagination: true,
			pageList: [10, 20, 50, 100],
			singleSelect: true,
			nowrap: true,
			striped: true,
			fitColumns: true,
			loadMsg: "数据加载中,请稍候...",
			toolbar: "#toolbar",
			url: "<%=request.getContextPath()%>/rpt/anaRpt_query.action?custId=<%=custId%>",
			frozenColumns: [[{
				field: "ck",
				checkbox: true
			}]],
			columns: [[{
				field: "reportType",
				title: "报告类型",
				width: 80,
				align: "center",
				formatter: function(val){
					if(val == 1){
						return "血压";
					}else if(val == 2){
						return "血糖";
					}else{
						return "";
					}
				}
			},{
				field: "timeInterval",
				title: "报告测量时段",
				width: 150,
				align: "center",
				formatter: function(val, r){
					if(r.examBeginTime && r.examEndTime){
						return r.examBeginTime.substring(0, 10) + " 至 " + r.examEndTime.substring(0, 10);
					}else{
						return "";
					}
				}
			}, {
				field: "suggest",
				title: "评估建议",
				width: 300,
				align: "center"
			}]]
		});
	});
	function refreshGrid(){
		anaRptGrid.datagrid("reload");
		anaRptGrid.datagrid("unselectAll");
		anaRptGrid.datagrid("clearSelections");
	}
	function query(){
		anaRptGrid.datagrid("getPager").pagination({pageNumber: 1});
		$.extend(anaRptGrid.datagrid("options"), {
			pageNumber: 1,
			queryParams: $("#filterForm").serializeObject()
		});
		refreshGrid();
	}
	function clear(){
		document.getElementById("filterForm").reset();
	}
	var subWin;
	function view(){
		var selected = anaRptGrid.datagrid("getSelected");
		if(!selected){
			$.messager.alert("提示信息","请选择一条记录。","error");
			return;
		}
		top.addTab("健康分析报告", "<%=request.getContextPath()%>/rpt/anaRpt_toDetail.action?operType=view&id=" + selected.id);
	}
</script>
</head>
<body>
	<div class="sectionTitle">查询条件</div>
	<div>
		<form id="filterForm">
			<table class="table">
				<tr>
					<td width="20%" class="td_title">报告类型:</td>
					<td>
						<select name="reportType">
							<option value="">请选择</option>
							<option value="1">血压</option>
							<option value="2">血糖</option>
						</select>
					</td>
					<td style="text-align: right;" width="120">
						<a class="easyui-linkbutton" href="javascript:query()">查询</a>
						&nbsp;
						<a class="easyui-linkbutton" href="javascript:clear()">重置</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div style="padding-top: 10px;">
		<table id="anaRpt" class="easyui-datagrid"></table>
	</div>
	<div id="toolbar" style="padding: 2px 0px;display: none;">
		<table>
			<tr>
				<td style="padding-left: 2px;">
					<a href="javascript:view();" class="easyui-linkbutton">查看</a>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>