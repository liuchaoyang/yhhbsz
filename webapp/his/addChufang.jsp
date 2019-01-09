<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="java.util.List"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.module.his.bean.HisPros"%>
<%@ page import="com.yzxt.yh.module.his.bean.HisYpkc"%>
<% 
	@SuppressWarnings("unchecked")
	List<HisYpkc> ypList = (List<HisYpkc>)request.getAttribute("ypList");
    HisPros hisPros = (HisPros)request.getAttribute("hisPros");
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>预警消息</title>
	<style type="text/css">
	</style>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script> 
	<script type="text/javascript">
	function query(){
	}
	function clear(){
		document.getElementById("filterForm").reset();
	}
	function closeIt() {
		try{
			parent.subWin1.window("close");
		}catch(e){}
	}
	var subWin;
	function openSelResi(){
		subWin = $('<div><iframe src="<%=request.getContextPath()%>/his/ypList.jsp" style="width: 98%;height:99%;" border="0" frameborder="0" scoll="no"></iframe></div>').window({
			title: "选择药品",
			width: 600,
			height: 300,
			resizable: false,
			collapsible: false,
			minimizable: false,
			maximizable: false,
			modal: true,
			zIndex: 101,
			closable: true
		});
	};
	
	function closeSelUserWin(){
		subWin.window("close");
	}
	
	function setSelUser(cust){
		$("#custId").val(cust.userId);
		$("#custName").val(cust.name);
	}
	
	 function save(){
		if(!validForm()){
			return;
		}
		$("#filterForm").form("submit", {
			url: "<%=request.getContextPath()%>/his/dzcf_addChufang.action",
			dataType : "json", 
			success : function(data) {
				data =  $.parseJSON(data); 
				if(data.state == 1){
					$.messager.alert("提示信息", data.msg?data.msg:"保存成功。", "info", function(){
						try{
							parent.refreshGrid();
						}catch(e){}
						closeIt();
					});
				}else{
					$.messager.alert("提示信息", data.msg?data.msg:"保存失败。", "error");
				}
			}
		});
	}
	
</script>
</head>
<body style="margin: 1px;padding: 0px;">
	<div style="width:100%;">
		<form id="filterForm" method="post" accept-charset="UTF-8">
			<table class="table">
				<%-- <%
				for(HisYpkc hisYpkc : ypList)
				{
				%>
				<tr>
					<td width="20%" class="td_title">药品名称:</td>
					<td width="70%">
						<input type="text" id="ypmc" name="hisYpkc.ypmc" maxlength="20" value="<%if(hisYpkc.getYpmc()!=null){ %><%=user.getName()%><%} %>" disabled="disabled" data-options="" style="width: 235px;" />
						<%if(hisYpkc.getYpmc()==null){ %>
						<img id="selUserImg" alt="选择用户" src="<%=request.getContextPath()%>/resources/img/search.png" onclick="openSelResi()" style="cursor: pointer;margin-bottom: -5px;" />
						<%} %>
						<input type="hidden" id="sqid" name="hisYpkc.sqid" value="<%if(hisYpkc.getSqid()!=null){ %><%=hisYpkc.getSqid()%><%} %>" />
					</td>
				</tr>
				<%
					}
				%>  --%>
				<tr>
				<td width="18%" class="td_title">规格</td>
					<input id="ypgg" name ="hisYpkc.ypgg><input/>
				</tr>
				<tr>
					<td width="20%" class="td_title">备注:</td>
					<td>
						<textarea id="memo" name="appoint.memo" style="padding: 5px;width: 95%;height: 40px;"></textarea>
					</td>
				</tr>
				<tr>
					<td width="20%" class="td_title">预约时间:</td>
					<td style="padding: 10px 3px 10px 3px;">
						<input id="appointTime" name ="appoint.appointTime" class="Wdate" onClick="WdatePicker({minDate:'%y-%M-%d'})" style="height:25px;width:150px"/></input>
						<span class="must">*</span>
					</td>
				</tr>
				<tr>
					<td width="20%" class="td_title">详细时间:</td>
					<td>
						<select id="detailTime" name="appoint.detailTime">
							<option value="">请选择</option>
							<option value="1">全天</option>
							<option value="2">上午</option>
							<option value="3">下午</option>
						</select>
						<span class="must">*</span>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div style="text-align: center;padding: 5px;">
		<a class="easyui-linkbutton" href="javascript:save()">保存</a>
		&nbsp;&nbsp;
		<a class="easyui-linkbutton" href="javascript:closeIt()">关闭</a>
	</div>
</body>
</html>