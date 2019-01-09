<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
	<title>我的指导</title>
	<style type="text/css">
	</style>
	<script type="text/javascript">
	var archiveGrid;
	$(function(){
		archiveGrid = $("#archives").datagrid({
			title: "膳食日志列表",
			width: "auto",
			height: "auto",
			idField: "id",
			rownumbers: true,
			pagination: true,
			url:'<%=request.getContextPath()%>/rgm/dietaryLog_listDietaryLog.action',
			pageList: [10, 20, 50, 100],
			singleSelect: true,
			nowrap: true,
			striped: true,
			fitColumns: true,
			loadMsg: "数据加载中,请稍候...",
			toolbar: "#toolbar",
			frozenColumns: [[{
				field: "ck",
				checkbox: true
			}]],
			columns:[[{
				field:'dietaryTime',
				title:'录入时间',
				width:100,
				align:'center',formatter:function(v){
					if(v!=null)
					{
						return v.substring(0,10);
					}else
					{
						return v;
					}
					}
				},
			{
				field:'intakeEnergy',
				title:'日摄入能量(卡路里)',
				width:100,align:'center'
			}]]
		});
	});
	function query(){
		$('#archives').datagrid("getPager").pagination({pageNumber : 1});
		var option = $('#archives').datagrid("options");
		option.pageNumber = 1;
		option.url = "<%=request.getContextPath()%>/rgm/dietaryLog_listDietaryLog.action";
		var queryParams = option.queryParams;
		queryParams.beginTime = document.getElementById("beginTime").value;
		queryParams.endTime = document.getElementById("endTime").value;
		refreshGrid();
	}
	function clear(){
		document.getElementById("filterForm").reset();
	}
	var subWin;
	function viewC(){
		var selected = $('#archives').datagrid("getSelected");
		if(!selected){
			$.messager.alert("提示信息","请选择一条记录。","info");
			return;
		}
		subWin = $('<div><iframe src="<%=request.getContextPath()%>/msg/heaGuide_toCheck.action?id=' + selected.id + '" style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>').window({
			title: "健康指导",
			width: 650,
			height: 350,
			resizable: false,
			collapsible: false,
			minimizable: false,
			maximizable: false,
			modal: true,
			zIndex: 100,
			closable: true
		});
	}
	<%-- var subWin1;
	function replyC(){
		subWin1 = $('<div><iframe src="<%=request.getContextPath()%>/msg/healthyDirectAdd.jsp" style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>').window({
			title: "健康指导",
			width: 650,
			height: 350,
			resizable: false,
			collapsible: false,
			minimizable: false,
			maximizable: false,
			modal: true,
			zIndex: 100,
			closable: true
		});
	} --%>
	
	function refreshGrid(){
		$('#archives').datagrid("reload");
		$('#archives').datagrid("unselectAll");
		$('#archives').datagrid("clearSelections");
	}
	
	function editSportDetail(){
		var urls='<%=request.getContextPath()%>/rgm/dietaryLog_toEdit.action';
		top.addTab("膳食详情",urls,null);
		}
	
	function showLogDetail(){
		var selections = $('#archives').datagrid("getSelections");
		if(!selections || selections.length != 1){
			$.messager.alert("提示信息", "请选择一条记录.", "info");
			return;
		}else{
			var url = "<%=request.getContextPath()%>/rgm/dietaryLog_showDetails.action?dietaryTime=" + selections[0].dietaryTime;
	       	var tabTitle = "日志详情";
	        top.addTab(tabTitle, url, null);
		}
	}
</script>
</head>
<body>
	<div class="sectionTitle">查询条件</div>
	<div>
		<form id="filterForm">
			<table class="table">
				<tr>
					<!-- <td width="10%" class="td_title">指导原因:</td>
					<td width="25%">
						<input id="directReason" name="directReason" maxlength="100" />
					</td> -->
					<td class="td_title" width="20%">录入时间:</td>
					<td>
						<input type="text" class="Wdate" name="beginTime" id="beginTime" onclick="WdatePicker()" style="width: 100px;" /> 
						至 
						<input type="text" class="Wdate" name="endTime" id="endTime" onclick="WdatePicker()" style="width: 100px;" /> 
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
		<table id="archives" class="easyui-datagrid"></table>
	</div>
	<div id="toolbar" style="padding: 2px 0px;display: none;">
		<table>
			<tr>
				<td style="padding-left:2px">
					<a href="javascript:void(0)" onclick="showLogDetail()" class="easyui-linkbutton">查看</a> 
					<a href="javascript:void(0)" onclick="editSportDetail()" class="easyui-linkbutton">填写日志</a>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>