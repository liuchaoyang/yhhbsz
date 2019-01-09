<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.module.sys.bean.Customer"%>
<%
Customer customer = (Customer)request.getAttribute("cust");
Boolean editable = (Boolean)request.getAttribute("editable");
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
	<title>会员列表</title>
	<style type="text/css">
	</style>
	<script type="text/javascript">
	var custGrid;
	$(function(){
		custGrid = $("#custs").datagrid({
			title: "体检列表",
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
			url: "<%=request.getContextPath()%>/chk/exam_queryCustExam.action?custId=<%=customer.getUserId()%>",
			frozenColumns: [[{
				field: "ck",
				checkbox: true
			}]],
			columns: [[{
				field: "examNo",
				title: "体检编号",
				width: 120,
				align: "center"
			}, {
				field: "healthCheckTime",
				title: "体检日期",
				width: 100,
				align: "center",
				formatter: function(val){
					if(val){
						return val.substring(0, 10);
					}
					return "";
				}
			}, {
				field: "examWhere",
				title: "体检机构",
				width: 350,
				align: "center"
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
	function refresh(){
		refreshGrid();
	}
	function clear(){
		document.getElementById("filterForm").reset();
	}
	function add(){
		top.addTab("健康体检表", "<%=request.getContextPath()%>/chk/exam_toExam.action?pPageId=${param.pageId}&operType=add&custId=<%=customer.getUserId()%>");
	}
	function update(){
		var selected = custGrid.datagrid("getSelected");
		if(!selected){
			$.messager.alert("提示信息","请选择一条记录。","error");
			return;
		}
		top.addTab("健康体检表", "<%=request.getContextPath()%>/chk/exam_toExam.action?pPageId=${param.pageId}&operType=update&id=" + selected.id);
	}
	function view(){
		var selected = custGrid.datagrid("getSelected");
		if(!selected){
			$.messager.alert("提示信息","请选择一条记录。","error");
			return;
		}
		top.addTab("健康体检表", "<%=request.getContextPath()%>/chk/exam_toExam.action?operType=view&id=" + selected.id);
	}
</script>
</head>
<body>
	<div class="sectionTitle">查询条件</div>
	<div>
		<form id="filterForm">
			<table class="table">
				<tr>
					<td width="80" class="td_title">客户名称:</td>
					<td width="150">
						<%=StringUtil.ensureStringNotNull(customer.getUser().getName())%>
					</td>
					<td width="80" class="td_title">体检编号:</td>
					<td width="180">
						<input id="examNo" name="examNo" maxlength="100" />
					</td>
					<td width="80" class="td_title">体检日期:</td>
					<td>
						<input class="Wdate" style="width: 100px;" id="startExamDate" name="startExamDate" readonly="readonly" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'endExamDate\')}'})" />
						&nbsp;至&nbsp;
						<input class="Wdate" style="width: 100px;"id="endExamDate" name="endExamDate" readonly="readonly" onclick="WdatePicker({minDate:'#F{$dp.$D(\'startExamDate\')}'})" />
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
					<%
					if(editable)
					{
					%>
					<a href="javascript:add();" class="easyui-linkbutton">新增</a>
					<a href="javascript:update();" class="easyui-linkbutton">修改</a>
					<%
					}
					%>
					<a href="javascript:view();" class="easyui-linkbutton">查看</a>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>