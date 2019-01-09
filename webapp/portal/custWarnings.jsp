<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
	<title>预警消息</title>
	<style type="text/css">
	</style>
	<script type="text/javascript">
	var archiveGrid;
	$(function(){
		archiveGrid = $("#archives").datagrid({
			title: "预警会员列表",
			width: "auto",
			height: "auto",
			idField: "id",
			url: "<%=request.getContextPath()%>/chk/checkWarn_warningList.action",
			rownumbers: true,
			pagination: true,
			pageList: [10, 20, 50, 100],
			singleSelect: true,
			nowrap: true,
			striped: true,
			fitColumns: true,
			loadMsg: "数据加载中,请稍候...",
			toolbar: "#toolbar",
			// 
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
						state = "未知";
					}
					return state;
				}
			}, {
				field: "descript",
				title: "最新预警内容",
				width: 200,
				align: "center"
			}, {
				field: "level",
				title: "最新预警等级",
				width: 100,
				align: "center",formatter:function(v){
					var state = "";
					if(v==1){
						state = "I级";
					}else if(v==2){
						state = "II级";
					}else if(v==3){
						state = "II级";
					}
					return state;
				}
			}, {
				field: "warnTime",
				title: "最新预警时间",
				width: 110,
				align: "center"
			}]]
		});
	});
	function query(){
		var checkOneVal = $("#userName").val();
		var checkTwoVal = $("#healthyStatus").val();
		$('#archives').datagrid("getPager").pagination({pageNumber : 1});
		var option = $('#archives').datagrid("options");
		option.pageNumber = 1;
		option.url = "<%=request.getContextPath()%>/chk/checkWarn_warningList.action";
		var queryParams = option.queryParams;
		queryParams.userName = checkOneVal;
		queryParams.healthyStatus = checkTwoVal;
		queryParams.beginDate = document.getElementById("beginDate").value;
		queryParams.endDate = document.getElementById("endDate").value;
		refreshGrid();
	}
	function clear(){
		document.getElementById("filterForm").reset();
	}
	
	function showDetail(){
		var selected = $('#archives').datagrid("getSelected");
			if(selected){
				var selections = $('#archives').datagrid("getSelections");
				if(selections.length==1){
					alert(selected.custId);
					<%-- var urls = "<%=request.getContextPath()%>/chk/custWarningList.action?type=edit&idCard="+data; --%>
					var urls = "<%=request.getContextPath()%>/chk/checkWarn_toDetailWarn.action?id="+ selected.custId;
	       			var newTitle = "详细预警";
	       			top.addTab(newTitle,urls,null);
	        	}else{
					$.messager.alert("提示信息","一次只能编辑一行数据","info");
					refreshGrid();
				}
			}else{
				$.messager.alert('提示信息','请选择编辑的行','info');
				refreshGrid();
			}
		};
		
	<%-- var subWin;
	function viewWarnings(){
		subWin = $('<div><iframe src="<%=request.getContextPath()%>/chk/custWarningList.jsp" style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>').window({
			title: "用户预警列表",
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
	} --%>
	var subWin1;
	function addDirect(){
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
	}
	
	function checkPastData(){
		var selections = $('#archives').datagrid("getSelections");
		if(selections && selections.length == 1){
			var url = "<%=request.getContextPath()%>/ach/achRes_toPage.action?type=jc&idCard=360420198802031278&showResident=Y&nr=y";
			top.addTab("检测记录", url, null);
		}else{
			$.messager.alert("提示信息", "请选择一条记录.", "info");
		}
	}
	
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
					<td width="10%" class="td_title">会员名称:</td>
					<td width="20%">
						<input id="userName" name="userName" maxlength="100" />
					</td>
					<td width="10%" class="td_title">健康状态:</td>
					<td>
						<select id="healthyStatus">
							<option value="">请选择</option>
							<option value="1">健康</option>
							<option value="2">亚健康</option>
							<option value="3">慢病</option>
							<option value="4">高危</option>
						</select>
					</td>
					<td class="td_title">告警时间:</td>
					<td>
						<input class="Wdate" style="width: 90px;"id="beginDate" name="beginDate"  readonly="readonly" onclick="WdatePicker({})" />
						至
						<input class="Wdate" style="width: 90px;"id="endDate" name="endDate"  readonly="readonly" onclick="WdatePicker({})" />
						
					</td>
					<td  class="td_oper">
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
					<a href="#" onclick="showDetail();" class="easyui-linkbutton">查看预警</a>
					<a href="#" onclick="addDirect();" class="easyui-linkbutton">健康指导</a>
					<a href="#" onclick="checkPastData();" class="easyui-linkbutton">健康指导</a>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>