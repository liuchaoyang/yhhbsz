<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ page import="com.yzxt.yh.util.DateUtil"%>
<%
String nowStr = DateUtil.toHtmlDate(new Date());
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>用户签约列表</title>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
	<style type="text/css">
	</style>
	<script type="text/javascript">
	var custGrid;
	$(function(){
		custGrid = $("#custs").datagrid({
			title: "签约列表",
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
			url: "<%=request.getContextPath()%>/svb/memberInfo_queryCust.action",
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
				formatter: function(val){
					if(val == 1){
						return "男";
					}else if(val == 2){
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
				title: "是否签约",
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
				field: "startDay",
				title: "签约开始日期",
				width: 100,
				align: "center",
				formatter: function(v){
					if(v!=null){
						return v.substring(0,10);
					}
				}
			}, {
				field: "endDay",
				title: "签约结束日期",
				width: 100,
				align: "center",
				formatter: function(v){
					if(v!=null){
						return v.substring(0,10);
					}
				}
			}/* , {
				field: "orgName",
				title: "组织名称",
				width: 120,
				align: "center"
			} */]]
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
	var subWin;
	function addMember(){
		var selections = custGrid.datagrid("getSelections");
		if(selections && selections.length==1){
			var selected = selections[0];
			if(selected.memberId){
				$.messager.alert("提示信息", "请选择一个还未签约的用户", "error");
				return;
			}
			subWin = $('<div><iframe src="<%=request.getContextPath()%>/svb/memberInfo_toDetail.action?operType=add&custId='+selected.userId+'" style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>').window({
				title: "新增签约",
				width: 500,
				height: 260,
				resizable: false,
				collapsible: false,
				minimizable: false,
				maximizable: false,
				modal: true,
				zIndex: 100,
				closable: true
			});
		}else{
			$.messager.alert("提示信息", "请选择一条记录行", "error");
		}
	}
	function updateMember(){
		var selections = custGrid.datagrid("getSelections");
		if(selections && selections.length==1){
			var selected = selections[0];
			if(!selected.memberId){
				$.messager.alert("提示信息", "请选择一个已签约的用户", "error");
				return;
			}
			subWin = $('<div><iframe src="<%=request.getContextPath()%>/svb/memberInfo_toDetail.action?operType=update&id='+selected.memberId+'" style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>').window({
				title: "修改加入",
				width: 500,
				height: 260,
				resizable: false,
				collapsible: false,
				minimizable: false,
				maximizable: false,
				modal: true,
				zIndex: 100,
				closable: true
			});
		}else{
			$.messager.alert("提示信息", "请选择一条记录行", "error");
		}
	}
	var subWin2;
	var callWin;
	function selDocWin(url, innerWin){
		callWin = innerWin;
		subWin2 = $('<div><iframe src="' + url + '" style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>').window({
			title: "选择健康管理师",
			width: 600,
			height: 400,
			resizable: false,
			collapsible: false,
			minimizable: false,
			maximizable: false,
			modal: true,
			zIndex: 101,
			closable: true
		});
	}
	function setSelDoc(obj){
		try{
			callWin.document.getElementById("doctorId").value = obj.id;
			callWin.document.getElementById("docotrName").value = obj.name;
		}catch(e){}
	}
	function closeIt(){
		try{
			subWin.window("close")
		}catch(e){}
	}
</script>
</head>
<body>
	<div class="sectionTitle">查询条件</div>
	<div>
		<form id="filterForm">
			<table class="table">
				<tr>
					<td width="20%" class="td_title">用户名称:</td>
					<td width="30%">
						<input id="name" name="name" maxlength="100" />
					</td>
					<td width="20%" class="td_title">身份证号码:</td>
					<td>
						<input id="idCard" name="idCard" maxlength="100" />
					</td>
				</tr>
				<tr>
					<td class="td_title">是否签约:</td>
					<td >
						<select id="memberStatus" name="memberStatus">
							<option value="">请选择</option>
							<option value="Y">是</option>
							<option value="N">否</option>
						</select>
					</td>
					<td colspan="2" class="td_oper" nowrap="nowrap" style="padding-right: 20px;">
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
	<div id="toolbar" style="padding: 2px 0px;display: none;">
		<table>
			<tr>
				<td style="padding-left: 2px;">
					<a href="javascript:addMember();" class="easyui-linkbutton">用户签约</a>
					<a href="javascript:updateMember();" class="easyui-linkbutton">修改签约</a>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>