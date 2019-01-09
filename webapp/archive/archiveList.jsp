<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>档案列表</title>
	<style type="text/css">
	</style>
	<script type="text/javascript">
	var archiveGrid;
	$(function(){
		archiveGrid = $("#archives").datagrid({
			title: "会员列表",
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
			url: "<%=request.getContextPath()%>/ach/ach_query.action",
			frozenColumns: [[{
				field: "ck",
				checkbox: true
			}]],
			columns: [[{
				field: "name",
				title: "会员名称",
				width: 80,
				align: "center"
			}, {
				field: "idCard",
				title: "身份证号码",
				width: 130,
				align: "center"
			}, {
				field: "sex",
				title: "性别",
				width: 70,
				align: "center",
				formatter: function(val){
					var sex = "";
					if(val==1){
						sex = "男";
					}else if(val==2){
						sex = "女";
					}else {
						sex = "";
					}
					return sex;
				}
			}, {
				field: "birthday",
				title: "出生日期",
				width: 100,
				align: "center",
				formatter: function(val){
					if(val){
						return val.substring(0,10);
					}
					return "";
				}
			}, {
				field: "endDay",
				title: "会员有效期",
				width: 100,
				align: "center",
				formatter: function(val){
					if(val){
						return val.substring(0,10);
					}
					return "";
				}
			}, {
				field: "healthyStatus",
				title: "健康状态",
				width: 100,
				align: "center",
				formatter:function(val){
					var state = "";
					if(val==1){
						state = "健康";
					}else if(val==2){
						state = "亚健康";
					}else if(val==3){
						state = "高危";
					}else {
						state = "";
					}
					return state;
				}
			}]]
		});
	});
	function refreshGrid(){
		archiveGrid.datagrid("reload");
		archiveGrid.datagrid("unselectAll");
		archiveGrid.datagrid("clearSelections");
	}
	function refresh(){
		refreshGrid();
	}
	function query(){
		archiveGrid.datagrid("getPager").pagination({pageNumber: 1});
		$.extend(archiveGrid.datagrid("options"), {
			pageNumber: 1,
			queryParams: $("#filterForm").serializeObject()
		});
		refreshGrid();
	}
	function clear(){
		document.getElementById("filterForm").reset();
	}
	function view(){
		var selections = archiveGrid.datagrid("getSelections");
		if(selections && selections.length==1){
			var url = "<%=request.getContextPath()%>/ach/ach_toDetail.action?pPageId=${param.pageId}&fq=Y&operType=view&custId=" + selections[0].userId;
			top.addTab("档案信息", url, null);
		}else if(selections && selections.length>1){
			$.messager.alert("提示信息", "一次只能查看一行数据", "error");
		}else{
			$.messager.alert("提示信息", "请选择行", "error");
		}
	}
	function edit(){
		var selections = archiveGrid.datagrid("getSelections");
		if(selections && selections.length==1){
			var url = "<%=request.getContextPath()%>/ach/ach_toDetail.action?pPageId=${param.pageId}&fq=Y&operType=edit&custId=" + selections[0].userId;
			top.addTab("档案信息", url, null);
		}else if(selections && selections.length>1){
			$.messager.alert("提示信息", "一次只能编辑一行数据", "error");
		}else{
			$.messager.alert("提示信息", "请选择行", "error");
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
					<td width="10%" class="td_title">会员名称:</td>
					<td width="25%">
						<input id="userName" name="userName" maxlength="100" />
					</td>
					<td width="10%" class="td_title">身份证号码:</td>
					<td>
						<input id="idCard" name="idCard" maxlength="100" />
					</td>
					<td class="td_title">健康状态:</td>
					<td>
						<select id="healthyState" name="healthyState">
							<option value="">请选择</option>
							<option value="1">健康</option>
							<option value="2">亚健康</option>
							<option value="3">高危</option>
						</select>
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
				<td style="padding-left: 2px;">
					<a href="javascript:view();" class="easyui-linkbutton">查看档案</a>
					<a href="javascript:edit();" class="easyui-linkbutton">修改档案</a>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>