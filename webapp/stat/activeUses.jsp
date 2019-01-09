<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import ="java.util.Date"%>
<%@ page import ="com.yzxt.yh.util.StringUtil"%>
<%@ page import ="com.yzxt.yh.util.DateUtil"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>活跃用户数</title>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/highcharts.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/modules/exporting.js"></script>
	<style type="text/css">
	</style>
	<script type="text/javascript">
	var recordGrid;
	$(function(){
		recordGrid = $("#records").datagrid({
			title: "使用记录列表",
			width: "auto",
			height: "auto",
			idField: "userId",
			rownumbers: true,
			pagination: true,
			pageList: [10, 20, 50, 100],
			singleSelect: true,
			nowrap: true,
			striped: true,
			fitColumns: true,
			loadMsg: "数据加载中,请稍候...",
			toolbar: "#toolbar",
			url: "<%=request.getContextPath()%>/stat/stat_queryUser.action",
			frozenColumns: [[{
				field: "ck",
				checkbox: true
			}]],
			columns: [[{
				field: "userName",
				title: "用户名",
				width: 80,
				align: "center"
			},{
				field: "createTime",
				title: "登录时间",
				width: 80,
				align: "center",formatter:function(v){
					var str = "";
					if(v!=null)
					{
						str= v.substring(0,10);
					}
					return str;
				}
			},{
				field: "type",
				title: "登录方式",
				width: 60,
				align: "center",formatter:function(v){
					var str = "";
					if(v==1){
						str="平台登录";
					}else if(v==2){
						str="客户端登录";
					}else if(v==3){
						str="一体机登录";
					}else {
						str="未知登录";
					}
					return str;
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
	function clear(){
		document.getElementById("filterForm").reset();
	}
	
	function checkDetail(){
		var selected = $('#records').datagrid("getSelected");
			if(selected){
				var selections = $('#records').datagrid("getSelections");
				if(selections.length==1){
					var urls = "<%=request.getContextPath()%>/stat/personalLoginRecords.jsp?userId="+ selected.userId;
	       			var newTitle = "个人详细";
	       			top.addTab(newTitle,urls,null);
	        	}else{
					$.messager.alert("提示信息","一次只能查看一行数据","info");
					refreshGrid();
				}
			}else{
				$.messager.alert('提示信息','请选择要查看的行','info');
				refreshGrid();
			}
		}
	
</script>
</head>
<body>
	<div class="sectionTitle">查询条件</div>
	<div>
		<form id="filterForm">
			<table class="table">
				<!-- <tr style="height: 25px;">
					<td width="20%" class="td_title">一体机编号:</td>
					<td colspan="3">
						
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
	<div id="toolbar" style="padding:2px 0;display: none;">
		<table style="width:100%;">
			<tr>
				<td style="padding-left:2px">
					<a href="javascript:checkDetail()" class="easyui-linkbutton">日详情</a>
				</td>
			</tr>
		</table>
	</div>
	<div style="padding-top: 10px;margin-right:20px;">
		<table id="records" class="easyui-datagrid"></table>
	</div>
</body>
</html>