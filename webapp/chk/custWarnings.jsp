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
				field: "noDealNum",
				title: "未处理预警数",
				width: 80,
				align: "center"
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
						state = "III级";
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
		archiveGrid.datagrid("getPager").pagination({pageNumber: 1});
		$.extend(archiveGrid.datagrid("options"), {
			pageNumber: 1,
			queryParams: $("#filterForm").serializeObject()
		});
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
		
	
	var subWin1;
	function addDirect(){
		var selected = $('#archives').datagrid("getSelected");
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
			subWin1 = $('<div><iframe src="<%=request.getContextPath()%>/msg/heaGuide_toAdd.action?custId=' + selected.custId + '" style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>').window({
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
	
	function checkPastData(){
		var selected = $('#archives').datagrid("getSelected");
			if(selected){
				var selections = $('#archives').datagrid("getSelections");
				if(selections.length==1){
					var url = "<%=request.getContextPath()%>/chk/checkData_custCheckDetail.action?custId="+ selected.custId;
					top.addTab("检测记录", url, null);
				}else{
						$.messager.alert("提示信息","一次只能编辑一行数据","info");
						refreshGrid();
					}
			}else{
					$.messager.alert("提示信息","请选择编辑的行","info");
				}
		}
	
	function refreshGrid(){
		archiveGrid.datagrid("reload");
		archiveGrid.datagrid("unselectAll");
		archiveGrid.datagrid("clearSelections");
	}
</script>
</head>
<body>
	<div class="sectionTitle">查询条件</div>
	<div>
		<form id="filterForm">
			<table class="table">
				<tr>
					<td width="20%" class="td_title">会员名称:</td>
					<td width="30%">
						<input id="custName" name="custName" maxlength="100" />
						<input type="hidden" name="operType" value="Q" />
					</td>
					<td width="20%" class="td_title">含有未处理预警:</td>
					<td>
						<select id="haveNotDealWarn" name="haveNotDealWarn">
							<option value="">请选择</option>
							<option value="Y" selected="selected">是</option>
							<option value="N">否</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="td_title">最新预警时间:</td>
					<td>
						<input class="Wdate" style="width: 90px;"id="beginDate" name="beginDate"  readonly="readonly" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}'})" />
						至
						<input class="Wdate" style="width: 90px;"id="endDate" name="endDate"  readonly="readonly" onclick="WdatePicker({minDate:'#F{$dp.$D(\'beginDate\')}'})" />
					</td>
					<td  class="td_oper" colspan="2" style="padding-right: 20px;">
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
					<a href="#" onclick="checkPastData();" class="easyui-linkbutton">检测记录</a>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>