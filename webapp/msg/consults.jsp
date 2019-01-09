<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>会员咨询</title>
	<style type="text/css">
	</style>
	<script type="text/javascript">
	var archiveGrid;
	$(function(){
		archiveGrid = $("#archives").datagrid({
			title: "会员咨询列表",
			width: "auto",
			height: "auto",
			idField: "id",
			url: "<%=request.getContextPath()%>/msg/consultGuide_getconsultList.action",
			rownumbers: true,
			pagination: true,
			pageList: [10, 20, 50, 100],
			singleSelect: true,
			nowrap: true,
			striped: true,
			fitColumns: true,
			loadMsg: "数据加载中,请稍候...",
			toolbar: "#toolbar",
			// url: null,
			frozenColumns: [[{
				field: "ck",
				checkbox: true
			}]],
			columns: [[ {
				field: "memberName",
				title: "会员名称",
				width: 80,
				align: "center"
			}, {
				field: "consultTitle",
				title: "标题",
				width: 120,
				align: "center"
			}, {
				field: "consultContext",
				title: "内容",
				width: 200,
				align: "center"
			}, {
				field: "state",
				title: "状态",
				width: 70,
				align: "center",formatter:function(v){
					var state = "";
					if(v==1){
						state="<font color='red'>未回复</font>";
					}else if(v==2){
						state="已回复";
					}
					return state;
				}
			}, {
				field: "consultTime",
				title: "提问时间",
				width: 120,
				align: "center"
			}, {
				field: "doctorName",
				title: "咨询医生",
				width: 120,
				align: "center"
			}, {
				field: "guideContext",
				title: "医生指导",
				width: 200,
				align: "center"
			}, {
				field: "guideTime",
				title: "指导时间",
				width: 120,
				align: "center"
			}]]
		});
	});
	
	function query(){
		var checkOneVal = $("#consultTitle").val();
		var checkTwoVal = $("#consultContext").val();
		$('#archives').datagrid("getPager").pagination({pageNumber : 1});
		var option = $('#archives').datagrid("options");
		option.pageNumber = 1;
		option.url = "<%=request.getContextPath()%>/msg/consultGuide_getconsultList.action";
		var queryParams = option.queryParams;
		queryParams.consultTitle = checkOneVal;
		queryParams.consultContext = checkTwoVal;
		queryParams.memberName = document.getElementById("memberName").value;
		queryParams.consultState = document.getElementById("consultState").value;
		refreshGrid();
	}
	
	function clear(){
		document.getElementById("filterForm").reset();
	}
	
	function refreshGrid(){
		$('#archives').datagrid("reload");
		$('#archives').datagrid("unselectAll");
		$('#archives').datagrid("clearSelections");
	}
	
	var subWin;
	function viewC(){
		var selected = $('#archives').datagrid("getSelected");
		if(!selected){
			$.messager.alert("提示信息","请选择一条记录。","info");
			return;
		}
		subWin = $('<div><iframe src="<%=request.getContextPath()%>/msg/consultGuide_toCheck.action?operType=detail&id=' + selected.id + '" style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>').window({
			title: "查看咨询",
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
	function replyC(){
		var selected = $('#archives').datagrid("getSelected");
		if(!selected){
			$.messager.alert("提示信息","请选择一条记录。","info");
			return;
		}
		if(selected.state==2){
			$.messager.alert("提示信息","该信息已回复，请选择未回复的。","info");
			$('#archives').datagrid("clearSelections");
			return;
		}
		subWin = $('<div><iframe src="<%=request.getContextPath()%>/msg/consultGuide_toCheck.action?operType=update&id=' + selected.id + '" style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>').window({
			title: "咨询指导",
			width: 650,
			height: 400,
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
					<td width="8%" class="td_title">标题:</td>
					<td width="15%">
						<input id="consultTitle" name="consultTitle" maxlength="100" />
					</td>
					<td width="8%" class="td_title">内容:</td>
					<td>
						<input id="consultContext" name="consultContext" maxlength="100" />
					</td>
					<td class="td_title">会员名称:</td>
					<td>
						<input id="memberName" name="memberName" maxlength="100" />
					</td>
					<td class="td_title">问题状态:</td>
					<td>
						<select id="consultState">
							<option value="">请选择</option>
							<option value="1">未回复</option>
							<option value="2">已回复</option>
						</select>
					</td>
					<td  class="td_oper">
						<a class="easyui-linkbutton" href="javascript:query()">查询</a>
						&nbsp;
						<a class="easyui-linkbutton" href="javascript:clear()">重置</a>
					</td>
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
					<a href="javascript:viewC();" class="easyui-linkbutton">查看</a>
					<a href="javascript:replyC();" class="easyui-linkbutton">回复</a>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>