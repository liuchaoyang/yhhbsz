<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import ="java.util.Date"%>
<%@ page import ="com.yzxt.yh.util.DateUtil"%>
<%
Date now = new Date();
String startDateStr = DateUtil.toHtmlDate(DateUtil.addDay(now, -30));
String endDateStr = DateUtil.toHtmlDate(now);
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>检测记录查询</title>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
	<style type="text/css">
	</style>
	<script type="text/javascript">
	var recordGrid;
	$(function(){
		recordGrid = $("#records").datagrid({
			title: "检测记录列表",
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
			url: "<%=request.getContextPath()%>/chk/checkData_query.action",
			columns: [[{
				field: "custName",
				title: "检测人",
				width: 60,
				align: "center",
				formatter: function(value, row){
					return '<a href="javascript:showCust(\'' + row.custId + '\');">' + value + '</a>';
				}
			},{
				field: "idCard",
				title: "身份证号码",
				width: 140,
				align: "center"
			},{
				field: "itemName",
				title: "检测项目",
				width: 70,
				align: "center"
			},{
				field: "valDesc",
				title: "检测结果",
				width: 200,
				align: "center",
				formatter: function(value, row){
					if(row.itemType == "nsfx"){
						return '<a href="javascript:showAnaUricDetail(\'' + row.id + '\');" style="text-decoration: underline;">点击查看尿常规数据</a>';	
					}else{
						return value;
					}
				}
			},{
				field: "checkTime",
				title: "检测时间",
				width: 130,
				align: "center"
			},{
				field: "describe",
				title: "风险描述",
				width: 160,
				align: "left",
				formatter: function(val, row, idx){
					if(row.level>0&&val){
						return '<label title="' + val + '" style="color: #FF0000;">' + val + '</label>';
					}
					return "";
				}
			}]]
		});
	});
	function refreshGrid(){
		recordGrid.datagrid("reload");
		recordGrid.datagrid("unselectAll");
		recordGrid.datagrid("clearSelections");
	}
	function query(){
		recordGrid.datagrid("getPager").pagination({pageNumber: 1});
		$.extend(recordGrid.datagrid("options"), {
			pageNumber: 1,
			queryParams: $("#filterForm").serializeObject()
		});
		refreshGrid();
	}
	var subWin;
	function showAnaUricDetail(id){
		subWin = $('<div><iframe src="<%=request.getContextPath()%>/chk/checkData_getAnalysisUricAcid.action?id=' + id + '" style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>').window({
			title: "尿常规明细",
			width: 520,
			height: 300,
			resizable: false,
			collapsible: false,
			minimizable: false,
			maximizable: false,
			modal: true,
			zIndex: 100,
			closable: true
		});
	}
	function clear(){
		document.getElementById("filterForm").reset();
	}
	function showCust(custId){
		window.open("<%=request.getContextPath()%>/sys/cust_toPersonalDetail.action?operType=pd&id=" + custId, "custDetail",
			"fullscreen=yes,scrollbars=yes,resizable=yes");
	}
</script>
</head>
<body>
	<div class="sectionTitle">查询条件</div>
	<div>
		<form id="filterForm">
			<table class="table">
				<tr>
					<td width="20%" class="td_title">检测日期:</td>
					<td width="30%">
						<input type="text" readonly="readonly" class="Wdate" style="width: 90px;" id="startDate" name="startDate" value="<%=startDateStr%>" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}'})" />
						至
						<input type="text" readonly="readonly" class="Wdate" style="width: 90px;"id="endDate" name="endDate" value="<%=endDateStr%>" onclick="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}'})" />
					</td>
					<td width="20%" class="td_title">检测项目:</td>
					<td>
						<select id="itemType" name="itemType" style="width: 90px;border: 1px solid #999999;">
							<option value="" selected="selected">全部</option>
							<option value="xy">血压</option>
							<option value="xt">血糖</option>
							<option value="xl">心率</option>
							<option value="xo">血氧</option>
							<option value="tw">体温</option>
							<option value="dgc">胆固醇</option>
							<option value="ns">尿酸</option>
							<option value="nsfx">尿常规</option>
							<option value="tz">体脂率</option>
						</select>
					</td>
				</tr>
				<tr>
					<td width="20%" class="td_title">检测人:</td>
					<td width="30%">
						<input type="text" id="keyword" name="keyword" maxlength="100" />
					</td>
					<td width="20%" colspan="2" align="right" style="padding-right: 20px;">
						<a class="easyui-linkbutton" href="javascript:query()">查询</a>
						&nbsp;
						<a class="easyui-linkbutton" href="javascript:clear()">重置</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div style="padding-top: 10px;">
		<table id="records" class="easyui-datagrid"></table>
	</div>
</body>
</html>