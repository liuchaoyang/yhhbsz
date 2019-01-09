<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.constant.Constant"%>
<%@ page import="com.yzxt.yh.module.sys.bean.User"%>
<%
	String custId = (String)request.getAttribute("custId");
	String custName = (String)request.getAttribute("custName");
	Boolean editable = (Boolean)request.getAttribute("editable");
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>客户电子病历列表</title>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
	<style type="text/css">
	</style>
	<script type="text/javascript">
	var emrGrid;
	$(function(){
		emrGrid = $("#emrs").datagrid({
			title: "电子病历列表",
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
			url: "<%=request.getContextPath()%>/ach/emr_queryCustEmr.action?custId=<%=custId%>",
			frozenColumns: [[{
				field: "ck",
				checkbox: true
			}]],
			columns: [[{
				field: "treatDate",
				title: "就诊日期",
				width: 80,
				align: "center",
				formatter: function(val){
					if(val!=null){
						return val.substring(0,10);
					}
					return "";
				}
			}, {
				field: "doctorName",
				title: "接诊医生",
				width: 65,
				align: "center"
			}, {
				field: "diagnosis",
				title: "诊疗信息",
				width: 220,
				align: "left"
			}, {
				field: "test",
				title: "检查化验",
				width: 100,
				align: "left"
			}, {
				field: "other",
				title: "其它信息",
				width: 100,
				align: "left"
			}, {
				field: "createTime",
				title: "创建时间",
				width: 120,
				align: "center"
			}]]
		});
	});
	function refreshGrid(){
		emrGrid.datagrid("reload");
		emrGrid.datagrid("unselectAll");
		emrGrid.datagrid("clearSelections");
	}
	function query(){
		emrGrid.datagrid("getPager").pagination({pageNumber: 1});
		$.extend(emrGrid.datagrid("options"), {
			pageNumber: 1,
			queryParams: $("#filterForm").serializeObject()
		});
		refreshGrid();
	}
	function clear(){
		document.getElementById("filterForm").reset();
	}
	var subWin;
	function add(){
		subWin = $('<div><iframe src="<%=request.getContextPath()%>/ach/emr_toDetail.action?operType=add&custId=<%=custId%>" style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>').window({
			title: "新增电子病历",
			width: 600,
			height: 470,
			resizable: false,
			collapsible: false,
			minimizable: false,
			maximizable: false,
			modal: true,
			zIndex: 100,
			closable: true
		});
	}
	function update(){
		var selected = emrGrid.datagrid("getSelected");
		if(!selected){
			$.messager.alert("提示信息","请选择一条记录。","error");
			return;
		}
		subWin = $('<div><iframe src="<%=request.getContextPath()%>/ach/emr_toDetail.action?operType=update&id=' + selected.id + '" style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>').window({
			title: "修改电子病历",
			width: 600,
			height: 470,
			resizable: false,
			collapsible: false,
			minimizable: false,
			maximizable: false,
			modal: true,
			zIndex: 100,
			closable: true
		});
	}
	function view(){
		var selected = emrGrid.datagrid("getSelected");
		if(!selected){
			$.messager.alert("提示信息","请选择一条记录。","error");
			return;
		}
		subWin = $('<div><iframe src="<%=request.getContextPath()%>/ach/emr_toDetail.action?operType=view&id=' + selected.id + '" style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>').window({
			title: "查看电子病历",
			width: 600,
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
	function viewCust(){
		var selected = emrGrid.datagrid("getSelected");
		if(!selected){
			$.messager.alert("提示信息","请选择一条记录。","error");
			return;
		}
		window.open("<%=request.getContextPath()%>/sys/cust_toPersonalDetail.action?operType=pd&id=" + selected.userId, "custInfo",
			"fullscreen=yes,scrollbars=yes,resizable=yes");
	}
	function goCustMedRecords(){
		var selected = emrGrid.datagrid("getSelected");
		if(!selected){
			$.messager.alert("提示信息","请选择一条记录。","error");
			return;
		}
		top.addTab("电子病历管理", "<%=request.getContextPath()%>/ach/emr_queryCustEmr.action?custId=" + selected.userId);
	}
	function goCustDossiers(){
		var selected = emrGrid.datagrid("getSelected");
		if(!selected){
			$.messager.alert("提示信息","请选择一条记录。","error");
			return;
		}
		top.addTab("图片病历管理", "<%=request.getContextPath()%>/ach/dossier_toMyDossier.action?custId=" + selected.userId);
	}
</script>
</head>
<body>
	<div class="sectionTitle">查询条件</div>
	<div>
		<form id="filterForm">
			<table class="table">
				<tr>
					<td width="10%" class="td_title">姓名:</td>
					<td width="25%">
						<%=custName%>
					</td>
					<td width="10%" class="td_title">就诊日期:</td>
					<td>
						<input class="Wdate" style="width: 100px;" id="startTreatDate" name="startTreatDate" readonly="readonly" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'endTreatDate\')}'})" />
						&nbsp;至&nbsp;
						<input class="Wdate" style="width: 100px;"id="endTreatDate" name="endTreatDate" readonly="readonly" onclick="WdatePicker({minDate:'#F{$dp.$D(\'startTreatDate\')}'})" />
					</td>
					<td width="140" class="td_oper">
						<a class="easyui-linkbutton" href="javascript:query()">查询</a>
						&nbsp;
						<a class="easyui-linkbutton" href="javascript:clear()">重置</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div style="padding-top: 10px;">
		<table id="emrs" class="easyui-datagrid"></table>
	</div>
	<div id="toolbar" style="padding: 2px 0px;display: none;">
		<table>
			<tr>
				<td style="padding-left: 2px;">
					<%
					if(editable.booleanValue())
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