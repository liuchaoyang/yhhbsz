<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
	<title>我的预警消息</title>
	<style type="text/css">
	</style>
	<script type="text/javascript">
	var archiveGrid;
	$(function(){
		archiveGrid = $("#warnings").datagrid({
			title: "预警信息列表",
			width: "auto",
			height: "auto",
			idField: "id",
			url: "<%=request.getContextPath()%>/chk/checkWarn_myWarnsList.action",
			rownumbers: true,
			pagination: true,
			pageList: [10, 20, 50, 100],
			singleSelect: true,
			nowrap: true,
			striped: true,
			fitColumns: true,
			loadMsg: "数据加载中,请稍候...",
			// toolbar: "#toolbar",
			columns: [[{
				field: "type",
				title: "预警项目",
				width: 60,
				align: "center",formatter:function(v){
					var state = "";
					if(v=="xy"){
						state="血压";
					}else if(v=="xt"){
						state="血糖";
					}else if(v=="tz"){
						state="体脂";
					}else{
						state="未知";
					}
					return state;
				}
			}, {
				field: "descript",
				title: "预警内容",
				width: 130,
				align: "center"
			}, {
				field: "level",
				title: "预警等级",
				width: 60,
				align: "center",formatter:function(v){
					var state = "";
					if(v==1){
						state = "I级";
					}else if(v==2){
						state = "II级";
					}else if(v==3){
						state = "III级";
					}
					return state;
				}
			}, {
				field: "warnTime",
				title: "预警时间",
				width: 90,
				align: "center"
			}]]
		});
	});
	function query(){
		/* var checkOneVal = $("#dealState").val(); */
		var checkTwoVal = $("#warnLevel").val();
		$('#warnings').datagrid("getPager").pagination({pageNumber : 1});
		var option = $('#warnings').datagrid("options");
		option.pageNumber = 1;
		option.url = "<%=request.getContextPath()%>/chk/checkWarn_myWarnsList.action";
		var queryParams = option.queryParams;
		/* queryParams.dealState = checkOneVal; */
		queryParams.warnLevel = checkTwoVal;
		queryParams.beginDate = document.getElementById("beginDate").value;
		queryParams.endDate = document.getElementById("endDate").value;
		refreshGrid();
	}
	
	function refreshGrid(){
			$('#warnings').datagrid("reload");
			$('#warnings').datagrid("unselectAll");
			$('#warnings').datagrid("clearSelections");
		}
	function clear(){
		document.getElementById("filterForm").reset();
	}
	var subWin;
	function viewWarnings(){
		subWin = $('<div><iframe src="<%=request.getContextPath()%>/exam/custWarningList.jsp" style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>').window({
			title: "用户预警列表",
			width: 650,
			height: 450,
			resizable: false,
			collapsible: false,
			minimizable: false,
			maximizable: false,
			modal: true,
			zIndex: 100,
			closable: true
		});
	}
</script>
</head>
<body>
	<div class="sectionTitle">查询条件</div>
	<div>
		<form id="filterForm">
			<table class="table">
				<tr>
					<td width="10%" class="td_title">预警等级:</td>
					<td width="25%">
						<select id="warnLevel">
							<option value="">请选择</option>
							<option value="1">I级</option>
							<option value="2">II级</option>
							<option value="3">III级</option>
						</select>
					</td>
					<td width="15%" class="td_title">预警时间:</td>
					<td style="padding:0px;" nowrap="nowrap">
						<input class="Wdate" style="width: 90px;"id="beginDate" name="beginDate" value="" readonly="readonly" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}'})" />
						至
						<input class="Wdate" style="width: 90px;"id="endDate" name="endDate" value="" readonly="readonly" onclick="WdatePicker({minDate:'#F{$dp.$D(\'beginDate\')}'})" />
					</td>
					<td width="140px;" class="td_oper" nowrap="nowrap">
						<a class="easyui-linkbutton" href="javascript:query()">查询</a>
						&nbsp;
						<a class="easyui-linkbutton" href="javascript:clear()">重置</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div style="padding-top: 10px;">
		<table id="warnings" class="easyui-datagrid"></table>
	</div>
</body>
</html>
