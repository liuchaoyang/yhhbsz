<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../common/inc.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<title>客户列表</title>
<style type="text/css">
#filterForm{
	margin: 0px;
	padding: 0px;
}
</style>
<script type="text/javascript">
	var custGrid;
	$(function(){
		custGrid = $("#custs").datagrid({
			title: "客户列表",
			width: "auto",
			height: 300,
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
			url: "<%=request.getContextPath()%>/sys/cust_query.action",
			frozenColumns: [[{
				field: "ck",
				checkbox: true
			}]],
			columns: [[{
				field: "name",
				title: "姓名",
				width: 80,
				align: "center"
			}, {
				field: "idCard",
				title: "身份证号码",
				width: 140,
				align: "center"
			}, {
				field: "sex",
				title: "性别",
				width: 60,
				align: "center",
				formatter:function(value){
					if(value == 1){
						return "男";
					}else if(value == 2){
						return "女";
					}
					return "";
				}
			}, {
				field: "phone",
				title: "手机号",
				width: 100,
				align: "center"
			}, {
				field: "memberId",
				title: "是否会员",
				width: 80,
				align: "center",
				formatter: function(val){
					if(val){
						return "是";
					}else{
						return "否";
					}
				}
			}, {
				field: "orgName",
				title: "隶属机构",
				width: 120,
				align: "center"
			}]]
		});
	});
	
	function query(){
		custGrid.datagrid("getPager").pagination({pageNumber: 1});
		$.extend(custGrid.datagrid("options"), {
			pageNumber: 1,
			queryParams: $("#filterForm").serializeObject()
		});
		reloadGrid();
	}
	function clear(){
		document.getElementById("filterForm").reset();
	}
	
	function reloadGrid(){
		custGrid.datagrid("reload");
		custGrid.datagrid("unselectAll");
		custGrid.datagrid("clearSelections");
	}
	
	function closeIt(){
		try{
			parent.subWin.window("close");
		}catch(e){}
	}
	
	function selDoc(){
		try{
			var selections = custGrid.datagrid("getSelections");
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
<body style="margin: 0px;padding: 1px;">
	<div class="sectionTitle">查询条件</div>
	<div>
		<form id="filterForm">
			<table class="table">
				<tr>
					<td width="20%" class="td_title">客户名称:</td>
					<td>
						<input id="name" name="name" maxlength="100" />
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
	<div style="margin-top: 5px;">
		<table id="custs" class="easyui-datagrid"></table>
	</div>
	<div style="text-align: center;padding-top: 7px;">
			<a href="javascript:selDoc()"  class="easyui-linkbutton">确定</a>
			&nbsp;
			<a href="javascript:closeIt()"  class="easyui-linkbutton">关闭</a>
	</div>
</body>
</html>