<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../../common/inc.jsp"%>
	<title>会员列表</title>
	<style type="text/css">
	</style>
	<script type="text/javascript">
	var archiveGrid;
	$(function(){
		archiveGrid = $("#members").datagrid({
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
					if(v==0){
						sex = "男";
					}else if(v==1){
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
	function query(){
		var checkOneVal = $("#userName").val();
		var checkTwoVal = $("#idCard").val(); 
		var checkThreeVal = $("#healthyState").val(); 
		$('#members').datagrid("getPager").pagination({pageNumber : 1});
		var option = $('#members').datagrid("options");
		option.pageNumber = 1;
		option.url = "<%=request.getContextPath()%>/sys/cust_memberList.action";
		var queryParams = option.queryParams;
		queryParams.userName = checkOneVal;
		queryParams.idCard = checkTwoVal;
		queryParams.healthyState = checkThreeVal;
		refreshGrid();
	}
	
	function refreshGrid(){
			$('#members').datagrid("reload");
			$('#members').datagrid("unselectAll");
			$('#members').datagrid("clearSelections");
		}
		
	function clear(){
		document.getElementById("filterForm").reset();
	}
	
	function viewAchive(){
		var selected = $('#members').datagrid("getSelected");
		if(selected){
			var selections = $('#members').datagrid("getSelections");
			if(selections.length==1){
				window.open("<%=request.getContextPath()%>/sys/cust_toPersonalDetail.action?operType=pd&id=" + selected.userId, "custInfo",
					"fullscreen=yes,scrollbars=yes,resizable=yes");
				<%-- window.open("<%=request.getContextPath()%>/sys/customer/custInfo.jsp","_blank"); --%>
			}else {
				$.messager.alert("提示信息", "一次只能编辑一行数据", "info");
				refreshGrid();
			}
		}else {
				$.messager.alert('提示信息', '请选择行', 'info');
		}
	}
		
		var subWin1;
		function adddr() {
			var selected = $('#members').datagrid("getSelected");
			if(!selected){
				subWin1 = $('<div><iframe src="<%=request.getContextPath()%>/msg/healthyDirectAdd.jsp" style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>').window({
					title: "健康指导",
					width: 700,
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
			else{
				subWin1 = $('<div><iframe src="<%=request.getContextPath()%>/msg/heaGuide_toAdd.action?custId=' + selected.userId + '" style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>').window({
					title: "健康指导",
					width: 700,
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

		}

		var subWin;
		function viewService() {
			var selected = $('#members').datagrid("getSelected");
			if(selected){
				var selections = $('#members').datagrid("getSelections");
				if(selections.length==1){
					subWin = $('<div><iframe src="<%=request.getContextPath()%>/svb/service_getServiceInfo.action?operType=svin&memberId='+selected.memberId+'" style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>').window({
						title: "会员服务信息",
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
				}else {
					$.messager.alert("提示信息", "一次只能编辑一行数据", "info");
					refreshGrid();
				}
			}else {
					$.messager.alert('提示信息', '请选择行', 'info');
			}
			
	}
	function refresh(){
		refreshGrid();
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
					<td class="td_title">健康状态:</td>
					<td>
						<select id="healthyState">
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
		<table id="members" class="easyui-datagrid"></table>
	</div>
	<div id="toolbar" style="padding: 2px 0px;display: none;">
		<table>
			<tr>
				<td style="padding-left: 2px;">
					<a href="javascript:viewAchive();" class="easyui-linkbutton">健康管理</a>
					<!-- <a href="javascript:adddr();" class="easyui-linkbutton">健康指导</a> -->
					<%--<a href="javascript:viewService();" class="easyui-linkbutton">服务信息</a>--%>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>