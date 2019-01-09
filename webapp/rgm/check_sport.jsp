<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html >
<html >
	<head>
		<%@ include file="../common/inc.jsp"%>
		<title>查看运动日志</title>
		<script type="text/javascript" src="<%=request.getContextPath()%>/pgchr/chr-common.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
		<script> 
		var gridDetail;
		// 页面编辑状态，1查看，2新增，3修改
		var editDetailState;
		$(function(){
			gridDetail = $("#sportLogDetails").datagrid({
				title : "${sportTime}运动日志明细列表",
				loadMsg : "数据加载中,请稍候...",
				width : "auto",
				height : "auto",
				animate : true,
				nowrap : true,
				striped : true,
				singleSelect : true,
				idField : "id",
				url : "<%=request.getContextPath()%>/rgm/sportLog_getLogDetails.action?sportTime=${sportTime}",
				pagination : false,
				rownumbers : true,
				fitColumns : true,
				/* frozenColumns : [ [ {
					field : "ck",
					checkbox : true
				} ] ], */
				columns : [ [ 
					{field:'sportTime',title:'录入时间',width:100,align:'center',formatter:function(v){
							if(v!=null){
							return v.substring(0,10);
							}
						}},
					{
					field : "sportType",
					title : "运动类型",
					width : 200,
					align : "center",
					formatter : function(value){
						if(value == 1){
							return "轻度";
						}else if(value == 2){
							return "中度";
						}else if(value == 3){
							return "稍强度";
						}
					}
				}, {
					field : "sportName",
					title : "运动名称",
					width : 200,
					align : "center"
				},{
					field : "timeSpan",
					title : "运动时长(分钟)",
					width : 200,
					align : "center"
				},{
					field : "dayConsumeEnergy",
					title : "消耗能量(卡路里)",
					width : 80,
					align : "center"
				} ,] ],
				onSortColumn : function(sort, order) {
				},
				toolbar : "#dlg-toolbar"
			});
			var p = $('#sportLogDetails').datagrid('getPager');
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
					<!-- <tr>
						<td>&nbsp;</td>
					</tr> -->
					<tr>
						<td><table id="sportLogDetails"></table></td>
					</tr>
				</table>
		</div>
	</body>
</html>