<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../common/inc.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>健康计划列表</title>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js">
	</script>
	<style type="text/css">
	</style>
<script type="text/javascript">
	var healthyPlanGrid;
	var subWin;
	$(function(){
		healthyPlanGrid = $("#healthyPlanBox").datagrid({
			title: "健康计划信息",
			loadMsg: "数据加载中,请稍候...",
			width: "auto",
			height: "auto",
			animate: false,
			striped: true,
			url: "<%=request.getContextPath()%>/rgm/healthyPlan_list.action",
			pagination : true,
			idField: "id",
			rownumbers: true,
			fitColumns: true,
			frozenColumns: [ [ {
				field: "ck",
				checkbox: true
			} ] ],
			columns: [ [ {
				field: "name",
				title: "计划名称",
				width: 180,
				align: "center"
			}, {
				field: "type",
				title: "计划类型",
				width: 80,
				align: "center",
				formatter: function(val, row, index) {
					if(val == 1){
						return "血压";
					}else if(val == 2){
						return "血糖";
					}else if(val == 3){
						return "体重";
					}
					return "";
				}
			}, {
				field: "statusDesc",
				title: "状态",
				width: 80,
				align: "center"
			}, {
				field: "startDate",
				title: "开始日期",
				width: 100,
				align: "center",
				formatter: function(val, row, index) {
					if(val){
						return val.substring(0,10);
					}else{
						return "";
					}
				}
			}, {
				field: "endDate",
				title: "结束日期",
				width: 100,
				align: "center",
				formatter: function(val, row, index) {
					if(val){
						return val.substring(0,10);
					}else{
						return "";
					}
				}
			}, {
				field: "startValDesc",
				title: "开始时值",
				width: 150,
				align: "left"
			}, {
				field: "endValDesc",
				title: "结束时值",
				width: 150,
				align: "left"
			}, {
				field: "targetValDesc",
				title: "目标值",
				width: 150,
				align: "left"
			}, {
				field: "updateTime",
				title: "创建时间",
				width: 140,
				align: "center"
			} ] ],
			onSortColumn: function(sort, order) {
			},
			toolbar: "#dlg-toolbar"
		});
		var p = healthyPlanGrid.datagrid("getPager");
		if (p){
			$(p).pagination({
				pageNumber: 1,
				showPageList: false
			});
		}
	});
	
	function refreshGrid(){
		healthyPlanGrid.datagrid("reload");
		healthyPlanGrid.datagrid("unselectAll");
		healthyPlanGrid.datagrid("clearSelections");
	}
	
	function search(val){
		healthyPlanGrid.datagrid("getPager").pagination({pageNumber : 1});
		var option = healthyPlanGrid.datagrid("options");
		option.pageNumber = 1;
		option.url = "<%=request.getContextPath()%>/rgm/healthyPlan_list.action";
		var queryParams = option.queryParams;
		queryParams.keyword = trim(val);
		refreshGrid();
	}
	
	function showAddWin(){
		subWin = $('<div><iframe src="<%=request.getContextPath()%>/rgm/healthyPlan_toEdit.action" style="width: 99%;height:99%;border="0" frameborder="0"></iframe></div>').window({
			title: "新增健康计划",
			width: 500,
			height: 335,
			resizable: false,
			collapsible: false,
			minimizable: false,
			maximizable: false,
			modal: true,
			zIndex: 100,
			closable: true
		});
	}
	
	function showEditWin(){
		var selections = healthyPlanGrid.datagrid("getSelections");
		if(selections && selections.length == 1){
			subWin = $('<div><iframe src="<%=request.getContextPath()%>/rgm/healthyPlan_toEdit.action?id=' + selections[0].id + '" style="width: 99%;height:99%;border="0" frameborder="0"></iframe></div>').window({
				title: "修改健康计划",
				width: 500,
				height: 335,
				resizable: false,
				collapsible: false,
				minimizable: false,
				maximizable: false,
				modal: true,
				zIndex: 100,
				closable: true
			});
			subWin.window("center");
			subWin.window("open");
		}else if(selections && selections.length > 1){
			$.messager.alert("提示信息", "一次只能修改一条记录.", "error");
		}else{
			$.messager.alert("提示信息", "请选择一条需要修改的记录.", "error");
		}
	}
	
	function closeSubWin(){
		subWin.window("close");
	}
	
	function deletePlan(){
		var selections = healthyPlanGrid.datagrid("getSelections");
		if(selections && selections.length > 0){
			$.messager.confirm("警告信息", "确定删除数据吗?", function(r){
				if(r){
					var ids = "";
					for(var i=0;i<selections.length;i++){
						if(i > 0){
							ids = ids + "," + selections[i].id;
						}else{
							ids = selections[i].id;
						}
					}
					$.ajax({
						type : "POST",
						dataType : "json",
						data : {ids : ids},
						url : "<%=request.getContextPath()%>/rgm/healthyPlan_delete.action",
						async : false,
						timeout : 300000,
						success : function(data){
							if(data.state == 1){
								$.messager.alert("提示信息", "删除成功.", "info");
								refreshGrid();
							}else if(data.state < 0){
								$.messager.alert("提示信息", data.msg ? data.msg : "删除失败.", "error");
							}
						},
						error : function(){
							$.messager.alert("提示信息", "删除失败！", "error");
					 	}
					});
				}
			});
		}else{
			$.messager.alert("提示信息", "请选择要删除的行.", "error");
		}
	}
	function viewExec(){
		var selections = healthyPlanGrid.datagrid("getSelections");
		if(selections && selections.length == 1){
			if(selections[0].statusDesc == "未开始"){
				$.messager.alert("提示信息", "计划未开始执行，无法查看.", "error");
				return;
			}
			subWin = $('<div><iframe src="<%=request.getContextPath()%>/rgm/healthyPlan_view.action?id=' + selections[0].id + '" style="width: 99%;height:99%;margin: 0px;padding: 0px;" border="0" frameborder="0" scoll="no"></iframe></div>').window({
				title: "查看健康计划执行状况",
				width: 750,
				height: 450,
				resizable: false,
				collapsible: false,
				minimizable: false,
				maximizable: false,
				modal: true,
				zIndex: 100,
				closable: true
			});
			subWin.window("center");
			subWin.window("open");
		}else{
			$.messager.alert("提示信息", "请选择一条记录.", "error");
		}
	}
</script>
</head>
<body>
	<div style="padding-top: 2px;padding-right:2px;">
		<table id="healthyPlanBox" class="easyui-datagrid"></table>
	</div>
	<div id="dlg-toolbar" style="padding: 2px 0;">
		<table cellpadding="0" cellspacing="0" style="width: 100%">
			<tr>
				<td style="padding-left: 2px">
					<a href="javascript: void(0)" onclick="showAddWin();" class="easyui-linkbutton">新增</a>
					<a href="javascript: void(0)" onclick="showEditWin();" class="easyui-linkbutton">修改</a>
					<a href="javascript: void(0)" onclick="deletePlan();" class="easyui-linkbutton">删除</a>
					<a href="javascript: void(0)" onclick="viewExec();" class="easyui-linkbutton">查看执行情况</a>
				</td>
				<!-- <td style="text-align: right; padding-right: 10px">
					<input class="easyui-searchbox" data-options="prompt:'请输入计划名称.',searcher:search" style="width: 300px;" />
				</td> -->
			</tr>
		</table>
	</div>
</body>
</html>