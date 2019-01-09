<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>会员列表</title>
	<style type="text/css">
	</style>
	<script type="text/javascript">
	var custGrid;
	$(function(){
		custGrid = $("#custs").datagrid({
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
			url: "<%=request.getContextPath()%>/sys/cust_memberList.action",
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
			}]]
		});
	});
	function refreshGrid(){
		custGrid.datagrid("reload");
		custGrid.datagrid("unselectAll");
		custGrid.datagrid("clearSelections");
	}
	function query(){
		custGrid.datagrid("getPager").pagination({pageNumber: 1});
		$.extend(custGrid.datagrid("options"), {
			pageNumber: 1,
			queryParams: $("#filterForm").serializeObject()
		});
		refreshGrid();
	}
	function clear(){
		document.getElementById("filterForm").reset();
	}
	function viewChkRecords(){
		var selected = custGrid.datagrid("getSelected");
		if(!selected){
			$.messager.alert("提示信息","请选择一条记录。","error");
			return;
		}
		top.addTab("客户体检列表", "<%=request.getContextPath()%>/chk/exam_toCustExams.action?custId=" + selected.userId);
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
					<td class="td_title">会员状态:</td>
					<td>
						<select id="healthyState" name="healthyState">
							<option value="">请选择</option>
							<option value="1">健康</option>
							<option value="2">亚健康</option>
							<!-- <option>慢病</option> -->
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
		<table id="custs" class="easyui-datagrid"></table>
	</div>
	<div id="toolbar" style="padding: 0px 0px;display: none;">
		<table>
			<tr>
				<td style="padding-left: 2px;">
					<a href="javascript:viewChkRecords();" class="easyui-linkbutton">查看体检记录</a>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>