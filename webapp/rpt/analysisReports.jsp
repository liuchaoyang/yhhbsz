<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>分析报告列表</title>
	<style type="text/css">
	</style>
	<script type="text/javascript">
	var anaRptGrid;
	$(function(){
		anaRptGrid = $("#anaRpt").datagrid({
			title: "分析报告列表",
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
			url: "<%=request.getContextPath()%>/rpt/anaRpt_query.action",
			frozenColumns: [[{
				field: "ck",
				checkbox: true
			}]],
			columns: [[{
				field: "customerName",
				title: "姓名",
				width: 80,
				align: "center"
			}, {
				field: "idCard",
				title: "身份证号码",
				width: 140,
				align: "center"
			}, {
				field: "birthday",
				title: "出生日期",
				width: 70,
				align: "center",
				formatter:function(value){
					if(value){
						return value.substring(0, 10);
					}else{
						return "";
					}
				}
			}, {
				field: "phone",
				title: "手机号",
				width: 100,
				align: "center"
			}, {
				field: "reportType",
				title: "报告类型",
				width: 80,
				align: "center",
				formatter:function(value){
					if(value == 1){
						return "血压";
					}else if(value == 2){
						return "血糖";
					}else{
						return "";
					}
				}
			},{
				field: "dataInterval",
				title: "报告测量时段",
				width: 150,
				align: "center",
				formatter:function(value, row){
					if(row.examBeginTime && row.examEndTime){
						return row.examBeginTime.substring(0, 10) + " 至 " + row.examEndTime.substring(0, 10);
					}
					return "";
				}
			}, {
				field: "suggest",
				title: "评估建议",
				width: 200,
				align: "center"
			}]]
		});
	});
	function refreshGrid(){
		anaRptGrid.datagrid("reload");
		anaRptGrid.datagrid("unselectAll");
		anaRptGrid.datagrid("clearSelections");
	}
	function refresh(){
		refreshGrid();
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
		<%--生成分析报告--%>
		<%--
		$.ajax({
			type: "GET",
			dataType: "json",
			url: "<%=request.getContextPath()%>/rpt/anaRpt_gen.action?t="+(new Date().getTime()),
			async: true,
			timeout: 30000
		});
		--%>
	}
	var subWin;
	function view(){
		var selected = anaRptGrid.datagrid("getSelected");
		if(!selected){
			$.messager.alert("提示信息","请选择一条记录。","error");
			return;
		}
		top.addTab("健康分析报告", "<%=request.getContextPath()%>/rpt/anaRpt_toDetail.action?pPageId=${param.pageId}&operType=view&id=" + selected.id);
	}
</script>
</head>
<body>
	<div class="sectionTitle">查询条件</div>
	<div>
		<form id="filterForm">
			<table class="table">
				<tr>
					<td width="20%" class="td_title">姓名:</td>
					<td width="30%">
						<input type="text" id="custName" name="custName" maxlength="100" />
					</td>
					<td width="20%" class="td_title">身份证号码:</td>
					<td>
						<input type="text" id="idCard" name="idCard" maxlength="100" />
					</td>
				</tr>
				<tr>
					<td width="20%" class="td_title">报告类型:</td>
					<td>
						<select name="reportType">
							<option value="">请选择</option>
							<option value="1">血压</option>
							<option value="2">血糖</option>
						</select>
					</td>
					<td colspan="2" class="td_oper">
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