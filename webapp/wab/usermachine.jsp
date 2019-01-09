<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import ="java.util.Date"%>
<%@ page import ="com.yzxt.yh.util.DateUtil"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>健康数据</title>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/highcharts.js"></script>
	<style type="text/css">
	form{
		margin: 0px;
		padding: 0px;
	}
	</style>
	<script type="text/javascript">
		var tabInitMap=["initJb"];
		var jbGrid;	
		$(function(){
			$("#tabs").tabs({
				onSelect: function(title, idx){
					if(!tabInitMap[idx]){
						return;
					}else{
						funcName = tabInitMap[idx];
						tabInitMap[idx] = null;
						eval(funcName+"();");
					}
				}
			});
			if(!tabInitMap[0]){
				return;
			}else{
				funcName = tabInitMap[0];
				tabInitMap[0] = null;
				eval(funcName+"();");
			}
			});
	function initJb(){
		jbGrid = $("#jbRecords").datagrid({
			width: "auto",
			height: 310,
			idField: "id",
			rownumbers: true,
			pagination: true,
			pageList: [10, 20, 50, 100],
			singleSelect: true,
			nowrap: true,
			striped: true,
			fitColumns: true,
			loadMsg: "数据加载中,请稍候...",
			url: "<%=request.getContextPath()%>/wab/wabData_queryusm.action?custId=${custId}",
			columns: [[{
				field: "IMEICode",
				title: "IEMI",
				width: 100,
				align: "center"
			}, {
				field: "BindDate",
				title: "绑定时间",
				width: 100,
				align: "center"
			}, {
				field: "Nick",
				title: "设备用户",
				width: 100,
				align: "center"
			}]]
		});
	}
	function refreshJbGrid(){
		jbGrid.datagrid("reload");
		jbGrid.datagrid("unselectAll");
		jbGrid.datagrid("clearSelections");
	}
	function queryJb(){
		jbGrid.datagrid("getPager").pagination({pageNumber: 1});
		$.extend(jbGrid.datagrid("options"), {
			pageNumber: 1,
			queryParams: $("#jbForm").serializeObject()
		});
		refreshJbGrid();
	}
	function bangd(){
		$("#imdiv").show();
	}
	function resetJb(){
		document.getElementById("jbForm").reset();
	}
	function saveWb(){
		var IMEIStr=$("#imeiName").val();
		 $.ajax({
               type: "POST",
               url: "<%=request.getContextPath()%>/wab/wabData_saveWb.action",
               data: "IMEIStr="+IMEIStr,
               success: function(data){
               		if(data==1){
               			alert("绑定成功！");
               		}else{
               			alert("绑定失败！");
               		}
               		$("#imdiv").hide();
               		queryJb();
                  }
            });
	}
	function closeim(){
		$("#imdiv").hide();
	}
	</script>
</head>
<body>
	<div class="easyui-tabs" id="tabs" style="margin:2px auto 10px auto;" data-options="width:780">
		<div title="绑定腕表" style="padding: 2px;">
			<form id="jbForm">
				<table class="table" style="margin-bottom: 2px;">
					<tr>
						<td style="text-align: right;">
							<a href="javascript:bangd();" class="easyui-linkbutton" >绑定新腕表</a>
							&nbsp;
						</td>
					</tr>
				</table>
			</form>
			<table id="jbRecords"></table>
			<div id="imdiv" style="float:left;width:300px;height:150px;border:1px solid rgb(149,184,231);margin-top:-42%;margin-left:30%;position:relative;z-index:999; background-color: rgb(224,236,255);display: none;">
				<div onclick="closeim();" style="cursor:pointer;float:right;width:10px;height:10px;background: url('../resources/easyui/themes/default/images/tabs_icons.png') no-repeat -34px center;"></div>
				<input id="imeiName" style="margin-top:55px;margin-left:40px;" maxlength="100" />
				<a href="javascript:saveWb();" style="margin-left:10px;" class="easyui-linkbutton" >绑定腕表</a>
			</div>
		</div>
	</div>
</body>
</html>