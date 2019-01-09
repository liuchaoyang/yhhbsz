<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.yzxt.yh.module.sys.bean.DictDetail"%>
<%
@SuppressWarnings("unchecked")
List<DictDetail> dossierTypes = (List<DictDetail>)request.getAttribute("dossierTypes");
String custId = (String)request.getAttribute("custId");
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>病历夹列表</title>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
	<style type="text/css">
	</style>
	<script type="text/javascript">
	var dossierGrid;
	$(function(){
		dossierGrid = $("#dossiers").datagrid({
			title: "病历夹列表",
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
			url: "<%=request.getContextPath()%>/ach/dossier_query.action?custId=<%=custId%>",
			frozenColumns: [[{
				field: "ck",
				checkbox: true
			}]],
			columns: [[{
				field: "typeName",
				title: "病历类型",
				width: 90,
				align: "center"
			}, {
				field: "memo",
				title: "病历说明",
				width: 250,
				align: "center"
			}, {
				field: "createTime",
				title: "创建时间",
				width: 115,
				align: "center",
				formatter: function(val){
					return val;
				}
			}]]
		});
	});
	function refreshGrid(){
		dossierGrid.datagrid("reload");
		dossierGrid.datagrid("unselectAll");
		dossierGrid.datagrid("clearSelections");
	}
	function refresh(){
		refreshGrid();
	}
	function query(){
		dossierGrid.datagrid("getPager").pagination({pageNumber: 1});
		$.extend(dossierGrid.datagrid("options"), {
			pageNumber: 1,
			queryParams: $("#filterForm").serializeObject()
		});
		refreshGrid();
	}
	function clear(){
		document.getElementById("filterForm").reset();
	}
	function view(){
		var selections = dossierGrid.datagrid("getSelections");
		if(selections && selections.length==1){
			var url = "<%=request.getContextPath()%>/ach/dossier_toDetail.action?pPageId=${param.pageId}&operType=view&id=" + selections[0].id;
			top.addTab("病历详细", url, null);
		}else if(selections && selections.length>1){
			$.messager.alert("提示信息", "一次只能查看一行数据", "error");
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
					<td width="10%" class="td_title">病历类型:</td>
					<td width="25%">
						<select name="type">
							<option value="" selected="selected">请选择</option>
							<%
							for(DictDetail dd : dossierTypes)
							{
							%>
							<option value="<%=dd.getDictDetailCode()%>"><%=dd.getDictDetailName()%></option>
							<%
							}
							%>
						</select>
					</td>
					<td class="td_title">创建日期:</td>
					<td>
						<input class="Wdate" style="width: 100px;" id="startCreateDate" name="startCreateDate" readonly="readonly" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'endCreateDate\')}'})" />
						&nbsp;至&nbsp;
						<input class="Wdate" style="width: 100px;"id="endCreateDate" name="endCreateDate" readonly="readonly" onclick="WdatePicker({minDate:'#F{$dp.$D(\'startCreateDate\')}'})" />
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
		<table id="dossiers" class="easyui-datagrid"></table>
	</div>
	<div id="toolbar" style="padding: 2px 0px;display: none;">
		<table>
			<tr>
				<td style="padding-left: 2px;">
					<a href="javascript:view();" class="easyui-linkbutton">查看病历夹</a>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>