<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.module.his.bean.HisYpkc"%>
<%@ page import="java.util.List"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<% String path = request.getContextPath(); %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>预约消息</title>
	<style type="text/css">
	</style>
	<script type="text/javascript">
	//查询
	function query(){
		var checkOneVal = $("#ypmc").val();
		/* $('#appoint').datagrid("getPager").pagination({pageNumber : 1});
		var option = $('#appoint').datagrid("options"); */
		<%-- option.pageNumber = 1;
		option.url = "<%=request.getContextPath()%>/his/dzcf_addypcf.action";
		var queryParams = option.queryParams;
		queryParams.ypmc = checkTwoVal;
		refreshGrid(); --%>
		$('#filterForm').submit();
	}
	//重置
	function clear(){
		document.getElementById("filterForm").reset();
		refreshGrid();
	}
	</script>
</head>
<body style="margin: 1px;padding: 0px;">
	<div style="padding-top: 15px;">
		<table id="hisPros" class="easyui-datagrid"></table>
	</div>
	<div class="sectionTitle">查询条件</div>
	<div>
		<form id="filterForm" method="post" action="<%=request.getContextPath()%>/his/dzcf_queryp.action">
			<input type="hidden" id="custId" name="custId" value="${custId }">
			<input type="hidden" id="doctorId" name="doctorId" value="${doctorId }">
			<input type="hidden" id="userName" name="userName" value="${userName }">
			<table class="table">
				<tr>
					<td class="td_title">药品英文简称:</td>
					<td >
						<input id="ypjx" name="ypjx" maxlength="100" />
					</td>
				</tr>
				<tr>
					<td colspan="4" class="td_oper">
						<a class="easyui-linkbutton" href="javascript:query()">查询</a>
						<a class="easyui-linkbutton" href="javascript:clear()">重置</a>
					</td>
				</tr>
			</table>
		</form>
	<div>
	<!-- <form id="filterForm" method="post" accept-charset="UTF-8"> -->
	<div class="sectionTitle" style="margin-top: 5px;">药品列表</div>
	<div>
			<table class="table">
				<tr>
					<td width="15%" class="td_title" align="center">药品序号</td>
					<td width="15%" class="td_title">药品名称</td>
					<td width="15%" class="td_title">药品英文简称</td>
					<!-- <td width="15%" class="td_title">药品单价</td> -->
					<!-- <td width="15%" class="td_title">药品规格</td> -->
					<td width="15%" class="td_title">医保类别</td>
					<td width="15%" class="td_title">药品类别</td>
					<!-- <td width="15%" class="td_title">操作:</td> -->
				</tr>
				<c:forEach var="item" items="${pageModel.data }" varStatus="i">
					<tr>
						<td width="15%">${i.index + 1 }</td>
						<td width="15%">
							<a href="javascript:void(0);" onclick="dzcfDetail('${item.sqid }')">${item.ypmc }</a>
						</td>
						<td width="15%">${item.ypjx }</td>
						<td width="15%">
							<c:if test="${item.ybflag ==0}">
								自费
							</c:if>
							<c:if test="${item.ybflag ==1}">
								医保
							</c:if>
						</td>
						<td width="15%">
							<c:if test="${item.ypflag ==0}">
								西药
							</c:if>
							<c:if test="${item.ypflag ==1}">
								中成药
							</c:if>
							<c:if test="${item.ypflag ==2}">
								中草药
							</c:if>
							<c:if test="${item.ypflag ==3}">
								卫生材料
							</c:if>
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>
	<!-- </form> -->
	</div>
	<script type="text/javascript">
	    var subWin2;
		function dzcfDetail(sqid) {
			var custId = $('#custId').val();
			var doctorId = $('#doctorId').val();
			var userName = $('#userName').val();
			var paramStr = 'ypId=' + sqid + '&	custId=' + custId + '&doctorId=' + doctorId + '&userName='+ userName;
			var htmlStr = "<div><iframe id='win' src='<%=request.getContextPath()%>/his/dzcf_dzcfDetail.action?" + paramStr + "'";
			htmlStr += ' style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>';
			 subWin2 = $(htmlStr).window({
				title: "处方详情",
				width: 450,
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
		
		function closeSelUserWin(){
			subWin2.window("close");
		}
	</script>
</body>
</html>