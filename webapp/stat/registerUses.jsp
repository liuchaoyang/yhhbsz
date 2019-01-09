<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import ="java.util.Date"%>
<%@ page import ="com.yzxt.yh.util.StringUtil"%>
<%@ page import ="com.yzxt.yh.util.DateUtil"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>注册统计</title>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/highcharts.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/modules/exporting.js"></script>
	<style type="text/css">
	</style>
	<script type="text/javascript">
	var recordGrid;
	$(function(){
		recordGrid = $("#records").datagrid({
			title: "注册统计列表",
			width: "auto",
			height: "auto",
			idField: "id",
			rownumbers: true,
			pagination: false,
			pageList: [10, 20, 50, 100],
			singleSelect: true,
			nowrap: true,
			striped: true,
			fitColumns: true,
			loadMsg: "数据加载中,请稍候...",
			toolbar: "#toolbar",
			url: "<%=request.getContextPath()%>/stat/stat_queryRegisterNum.action",
			columns: [[{
				field: "orgName",
				title: "机构名称",
				width: 80,
				align: "center"
			}/* ,{
				field: "createTime",
				title: "日期",
				width: 80,
				align: "center",formatter:function(v){
					var str = "";
					if(v!=null)
					{
						str= v.substring(0,10);
					}
					return str;
				}
			} */,{
				field: "c",
				title: "注册次数",
				width: 60,
				align: "center"
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
	function clear(){
		document.getElementById("filterForm").reset();
	}
	
	
</script>
</head>
<body>
	<div class="sectionTitle">查询条件</div>
	<div>
		<form id="filterForm">
			<table class="table">
				<!-- <tr style="height: 25px;">
					<td width="20%" class="td_title">登录方式:</td>
					<td colspan="3">
						<select id = "type"  name ="type" >
							<option value="">请选择</option>
							<option value="1">平台登录</option>
							<option value="2">客户端登录</option>
							<option value="3">一体机登录</option>
						</select>
					</td>
				</tr> -->
				<tr>
					<td width="20%" class="td_title">使用日期:</td>
					<td width="30%">
						<input type="text" readonly="readonly" class="Wdate" style="width: 90px;" id="startDate" name="startDate" value="" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}'})" />
						至
						<input type="text" readonly="readonly" class="Wdate" style="width: 90px;"id="endDate" name="endDate" value="" onclick="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}'})" />
					</td>
					<td colspan="2" align="right" style="padding-right: 20px;">
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