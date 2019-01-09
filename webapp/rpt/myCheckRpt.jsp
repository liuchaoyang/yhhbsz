<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
	<title>体检报告列表</title>
	<style type="text/css">
	</style>
	<script type="text/javascript">
	var examRptGrid;
	$(function(){
		examRptGrid = $("#examRpt").datagrid({
			title: "我的体检报告列表",
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
			url: "<%=request.getContextPath()%>/rpt/checkReport_query.action",
			frozenColumns: [[{
				field: "ck",
				checkbox: true
			}]],
			columns: [[{
				field: "custName",
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
				field: "createTime",
				title: "报告生成时间",
				width: 100,
				align: "center"
			}]]
		});
	});
	function refreshGrid(){
		examRptGrid.datagrid("reload");
		examRptGrid.datagrid("unselectAll");
		examRptGrid.datagrid("clearSelections");
	}
	function refresh(){
		refreshGrid();
	}
	function query(){
		examRptGrid.datagrid("getPager").pagination({pageNumber: 1});
		$.extend(examRptGrid.datagrid("options"), {
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
			url: "<%=request.getContextPath()%>/rpt/examRpt_gen.action?t="+(new Date().getTime()),
			async: true,
			timeout: 30000
		});
		--%>
	}
	var subWin;
	function view(){
		var selected = examRptGrid.datagrid("getSelected");
		if(!selected){
			$.messager.alert("提示信息","请选择一条记录。","error");
			return;
		}
		top.addTab("体检分析报告", "<%=request.getContextPath()%>/rpt/checkReport_toDetail.action?pPageId=${param.pageId}&operType=view&reportId=" + selected.id);
	}
</script>
</head>
<body>
	<div class="sectionTitle">查询条件</div>
	<div>
		<form id="filterForm">
			<table class="table">
				<tr>
					<td width="15%" class="td_title">报告生成时间:</td>
					<td style="padding:0px;" nowrap="nowrap">
						<input class="Wdate" style="width: 90px;"id="createTime" name="createTime" value="" readonly="readonly" onclick="WdatePicker({maxDate: '%y-%M-%d' })" />
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
		<table id="examRpt" class="easyui-datagrid"></table>
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