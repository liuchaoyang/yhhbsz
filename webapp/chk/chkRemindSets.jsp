<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>检测提醒</title>
	<style type="text/css">
	</style>
	<script type="text/javascript">
	var remindCustGrid;
	$(function(){
		remindCustGrid = $("#remindCusts").datagrid({
			title: "检测提醒列表",
			width: "auto",
			height: "auto",
			idField: "custId",
			rownumbers: true,
			pagination: true,
			pageList: [10, 20, 50, 100],
			singleSelect: false,
			nowrap: true,
			striped: true,
			fitColumns: true,
			loadMsg: "数据加载中,请稍候...",
			toolbar: "#toolbar",
			url: "<%=request.getContextPath()%>/chk/checkRemind_queryRemindSet.action",
			frozenColumns: [[{
				field: "ck",
				checkbox: true
			}]],
			columns: [[{
				field: "custName",
				title: "会员名称",
				width: 80,
				align: "center"
			}, {
				field: "idCard",
				title: "身份证号码",
				width: 130,
				align: "center"
			}, {
				field: "createUserTime",
				title: "注册日期",
				width: 100,
				align: "center",
				formatter: function(val){
					if(val){
						return val.substring(0, 10);
					}
				}
			}, {
				field: "lastCheckTime",
				title: "最后检测日期",
				width: 90,
				align: "center",
				formatter: function(val, row){
					if(val && row.lastCheckItem){
						return val.substring(0, 10);
					}
				}
			}, {
				field: "noCheckDays",
				title: "未做检测天数",
				width: 90,
				align: "center"
			}, {
				field: "remindIntervalDay",
				title: "提醒间隔天数",
				width: 90,
				align: "center",
				formatter:function(val){
					if(val > 0){
						return val;
					}
					return "不提醒";
				}
			}]]
		});
	});
	function refreshGrid(){
		remindCustGrid.datagrid("reload");
		remindCustGrid.datagrid("unselectAll");
		remindCustGrid.datagrid("clearSelections");
	}
	function query(){
		remindCustGrid.datagrid("getPager").pagination({pageNumber: 1});
		$.extend(remindCustGrid.datagrid("options"), {
			pageNumber: 1,
			queryParams: $("#filterForm").serializeObject()
		});
		refreshGrid();
	}
	function clear(){
		document.getElementById("filterForm").reset();
	}
	var subWin;
	function setRemind(){
		var selects = remindCustGrid.datagrid("getSelections");
		var len = selects?selects.length:0;
		if(len==0){
			$.messager.alert("提示信息", "请至少选择一条记录。", "error");
			return;
		}
		var custIdVal = "";
		var custNameVal = "";
		for(var i = 0; i<len; i++){
			if(i>0){
				custIdVal += ",";
				custNameVal += "、&nbsp;";
			}
			custIdVal += selects[i].custId;
			custNameVal += selects[i].custName;
		}
		document.getElementById("remindSetForm").reset();
		document.getElementById("custIds").value = custIdVal;
		document.getElementById("remindCustNames").innerHTML = custNameVal;
		subWin = $("#remindSetWin").window({
			width: 400,
			height: 200,
			modal: true
		});
		subWin.window("center");
		subWin.window("open");
	}
	function closeSubWin(){
		$("#remindSetWin").window("close");
	}
	function saveRemind(){
		$("#remindSetForm").form("submit", {
			url: "<%=request.getContextPath()+"/chk/checkRemind_saveSet.action"%>",
			dataType : "json",
			success : function(data) {
				data =  $.parseJSON(data);
				if(data.state == 1){
					$.messager.alert("提示信息", data.msg?data.msg:"保存成功。", "info", function(){
						refreshGrid();
						closeSubWin();
					});
				}else{
					$.messager.alert("提示信息", data.msg?data.msg:"保存失败。", "error");
				}
			}
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
					<td width="10%" class="td_title">会员名称:</td>
					<td width="23%">
						<input id="custName" name="custName" maxlength="100" />
					</td>
					<td width="10%" class="td_title">身份证号码:</td>
					<td width="23%">
						<input id="idCard" name="idCard" maxlength="100" />
					</td>
					<td width="10%" class="td_title">提醒间隔天数:</td>
					<td>
						<select id="remindIntervalDay" name="remindIntervalDay">
							<option value="">全部</option>
							<option value="7">7天未检测</option>
							<option value="15">15天未检测</option>
							<option value="30">30天未检测</option>
							<option value="-1">不提醒</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="td_title">未做检测天数:</td>
					<td>
						<select id="minNoCheckDay" name="minNoCheckDay">
							<option value="0" selected="selected">全部</option>
							<option value="7">大于7天</option>
							<option value="15">大于15天</option>
							<option value="30">大于30天</option>
						</select>
					</td>
					<td class="td_oper" colspan="4" style="padding-right: 20px;">
						<a class="easyui-linkbutton" href="javascript:query()">查询</a>
						&nbsp;
						<a class="easyui-linkbutton" href="javascript:clear()">重置</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div style="padding-top: 10px;">
		<table id="remindCusts" class="easyui-datagrid"></table>
	</div>
	<div id="toolbar" style="padding: 2px 0px;display: none;">
		<table>
			<tr>
				<td style="padding-left: 2px;">
					<a href="javascript:setRemind();" class="easyui-linkbutton">设置提醒间隔天数</a>
				</td>
			</tr>
		</table>
	</div>
	<div style="display: none;">
		<div id="remindSetWin" class="easyui-window" title="设置检测提醒间隔天数" style="padding: 1px;"  data-options="modal:true,closed:true">
			<form id="remindSetForm" style="margin: 0px;padding: 0px;" method="post" accept-charset="UTF-8">
				<table class="table">
					<tr height="50;">
						<td class="td_title" align="right" nowrap="nowrap" width="30%">会员名称：</td>
						<td>
							<span id="remindCustNames"></span>
							<input type="hidden" id="custIds" name="custIds" />
						</td>
					</tr>
					<tr height="30">
						<td class="td_title" align="right" nowrap="nowrap">提醒间隔天数：</td>
					    <td>
							<select id="intervalDay" name="intervalDay">
								<option value="7" selected="selected">7天未检测</option>
								<option value="15">15天未检测</option>
								<option value="30">30天未检测</option>
								<option value="-1">不提醒</option>
							</select>
			    		</td>
					</tr>
				</table>
				<div style="text-align: center;padding-top: 10px;">
					<a class="easyui-linkbutton" href="javascript:saveRemind()">保存</a> &nbsp;&nbsp; 
					<a class="easyui-linkbutton" href="javascript:closeSubWin()">关闭</a> &nbsp;&nbsp; 
				</div>
			</form>
		</div>
	</div>
</body>
</html>