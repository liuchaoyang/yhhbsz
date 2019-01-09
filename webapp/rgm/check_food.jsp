<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html >
<html >
	<head>
		<%@ include file="../common/inc.jsp"%>
		<title>查看饮食日志</title>
		<script type="text/javascript" src="<%=request.getContextPath()%>/pgchr/chr-common.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script> 
	<script> 
			var gridDetail;
	// 页面编辑状态，1查看，2新增，3修改
	var editDetailState;
	$(function(){
		gridDetail = $("#dietaryLogDetails").datagrid({
			title : " ${dietaryTime}膳食日志明细列表",
			loadMsg : "数据加载中,请稍候...",
			width : "auto",
			height : "auto",
			animate : true,
			nowrap : true,
			striped : true,
			singleSelect : true,
			idField : "id",
			url : "<%=request.getContextPath()%>/rgm/dietaryLog_getLogDetails.action?dietaryTime=${dietaryTime}",
			pagination : false,
			rownumbers : true,
			fitColumns : true,
			/* frozenColumns : [ [ {
				field : "ck",
				checkbox : true
			} ] ], */
			columns : [ [ 
				{field:'dietaryTime',title:'录入时间',width:100,align:'center',formatter:function(v){
						if(v!=null){
						return v.substring(0,10);
						}
					}},
				{
				field : "dietaryType",
				title : "餐次",
				width : 200,
				align : "center",
				formatter : function(value){
					if(value == 1){
						return "早餐";
					}
					else if(value == 2){
						return "午餐";
					}
					else if(value == 3){
						return "晚餐";
					}
					else if(value == 4){
						return "加餐";
					}
				}
			}, {
				field : "name",
				title : "食物名称",
				width : 200,
				align : "center"
			},{
				field : "intakeEnergy",
				title : "摄入能量(卡路里)",
				width : 80,
				align : "center"
			} ] ],
			onSortColumn : function(sort, order) {
			},
			toolbar : "#dlg-toolbar"
		});
		var p = $('#dietaryLogDetails').datagrid('getPager');
			if (p){
				$(p).pagination({
					pageNumber : 1,
					showPageList:false
				});
			}
	});
	</script>
</head>
<body>
<div style="padding:5px;width:900px;height: 100%; text-align: center;margin-left: auto;margin-right: auto;">
		<table id="mytab" align="center" cellpadding="0" cellspacing="0"
			border="0" width="98%" height="95%">
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td><table id="dietaryLogDetails"></table></td>
			</tr>
		</table>
</div>
</body>
</html>