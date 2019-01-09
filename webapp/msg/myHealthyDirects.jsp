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
			title: "健康指导列表",
			width: "auto",
			height: "auto",
			idField: "id",
			rownumbers: true,
			pagination: true,
			url: "<%=request.getContextPath()%>/msg/heaGuide_getPersonalList.action",
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
			columns: [[/* {
				field: "memberName",
				title: "会员名称",
				width: 80,
				align: "center"
			},  */{
				field: "directReason",
				title: "指导原因",
				width: 120,
				align: "center"
			}, {
				field: "sportDirect",
				title: "运动指导",
				width: 120,
				align: "center"
			}, {
				field: "foodDirect",
				title: "食物指导",
				width: 120,
				align: "center"
			}, {
				field: "drugSuggest",
				title: "药物及就医建议",
				width: 120,
				align: "center"
			}, {
				field: "memo",
				title: "备注",
				width: 120,
				align: "center"
			} , {
				field: "doctorName",
				title: "指导医生",
				width: 120,
				align: "center"
			} , {
				field: "createTime",
				title: "指导时间",
				width: 120,
				align: "center",formatter:function(v){
					if(v!=null){
						return v.substring(0,10);
					}
				}
			}]]
		});
	});
	function query(){
		/* var checkOneVal = $("#memberName").val(); */
		var checkTwoVal = $("#directReason").val();
		$('#archives').datagrid("getPager").pagination({pageNumber : 1});
		var option = $('#archives').datagrid("options");
		option.pageNumber = 1;
		option.url = "<%=request.getContextPath()%>/msg/heaGuide_guideList.action";
		var queryParams = option.queryParams;
		/* queryParams.memberName = checkOneVal; */
		queryParams.directReason = checkTwoVal;
		queryParams.beginDate = document.getElementById("beginDate").value;
		queryParams.endDate = document.getElementById("endDate").value;
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
</script>
</head>
<body>
	<div class="sectionTitle">查询条件</div>
	<div>
		<form id="filterForm">
			<table class="table">
				<tr>
					<td width="10%" class="td_title">指导原因:</td>
					<td width="25%">
						<input id="directReason" name="directReason" maxlength="100" />
					</td>
					<td class="td_title" width="20%">指导时间:</td>
					<td>
						<input class="Wdate" style="width: 90px;"id="beginDate" name="beginDate" value="" readonly="readonly" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}'})" />
						至
						<input class="Wdate" style="width: 90px;"id="endDate" name="endDate" value="" readonly="readonly" onclick="WdatePicker({minDate:'#F{$dp.$D(\'beginDate\')}'})" />
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
					<a href="javascript:viewC();" class="easyui-linkbutton">查看</a>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>