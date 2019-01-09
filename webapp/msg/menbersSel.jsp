<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../common/inc.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<title>选择会员</title>
<style type="text/css">
</style>
<script type="text/javascript">
	var grid;
	$(function(){
		grid = $("#doctorBox").datagrid({
			loadMsg : "数据加载中,请稍候...",
			width : "auto",
			height : 170,
			animate : false,
			striped : true,
			singleSelect : true,
			url : "<%=request.getContextPath()%>/sys/cust_memberList.action",
			pagination : true,
			idField : "userId",
			rownumbers : true,
			fitColumns : true,
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
				align: "center",formatter:function(v){
					var sex = "";
					if(v==1){
						sex = "男";
					}else if(v==2){
						sex = "女";
					}else {
						sex = "未知性别";
					}
					return sex;
				}
			}, {
				field: "birthday",
				title: "出生日期",
				width: 100,
				align: "center",formatter:function(v){
					if(v!=null){
						return v.substring(0,10);
					}
				}
			}, {
				field: "endDay",
				title: "会员有效期",
				width: 100,
				align: "center",formatter:function(v){
					if(v!=null){
						return v.substring(0,10);
					}
				}
			}, {
				field: "healthyStatus",
				title: "健康状态",
				width: 100,
				align: "center",formatter:function(v){
					var state = "";
					if(v==1){
						state = "健康";
					}else if(v==2){
						state = "亚健康";
					}else if(v==3){
						state = "高危";
					}else {
						state = "";
					}
					return state;
				}
			}]],
			onSortColumn : function(sort, order) {
			},
			toolbar : "#toolbar"
		});
		var p = grid.datagrid("getPager");
		if (p) {
			$(p).pagination({
				pageNumber : 1,
				showPageList : false
			});
		}
	});
	
	function query(){
		grid.datagrid("getPager").pagination({pageNumber: 1});
		$.extend(grid.datagrid("options"), {
			pageNumber: 1,
			queryParams: $("#filterForm").serializeObject()
		});
		reloadGrid();
	}
	function clear(){
		document.getElementById("filterForm").reset();
	}
	
	function reloadGrid(){
		grid.datagrid("reload");
		grid.datagrid("unselectAll");
		grid.datagrid("clearSelections");
	}
	
	function closeIt(){
		try{
			parent.subWin.window("close");
		}catch(e){}
	}
	
	function selDoc(){
		try{
			var selections = grid.datagrid("getSelections");
			if(!selections || selections.length == 0){
				$.messager.alert("提示信息", "请选择一条的记录", "info");
				return;
			}
			parent.setSelUser(selections[0]);
			parent.closeSelUserWin();
		}catch(e){}
	}
</script>
</head>
<body style="margin: 0px;padding: 2px;">
	<div>
		<form id="filterForm">
			<table class="table">
				<tr>
					<td width="20%" class="td_title">会员名称:</td>
					<td>
						<input id="userName" name="userName" maxlength="100" />
					</td>
					<td width="100">
						<a class="easyui-linkbutton" href="javascript:query()">查询</a>
						&nbsp;
						<a class="easyui-linkbutton" href="javascript:clear()">重置</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div style="text-align: center;padding-top: 5px;">
		<table id="doctorBox" class="easyui-datagrid"></table>
		<div style="text-align: center;padding-top: 10px;">
			<a href="javascript:selDoc()"  class="easyui-linkbutton">确定</a>
			&nbsp;
			<a href="javascript:closeIt()"  class="easyui-linkbutton">关闭</a>
		</div>
	</div>
</body>
</html>