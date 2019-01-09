<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.module.his.bean.Appoint"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.util.DateUtil"%>
<% 
	Appoint appoint = (Appoint) request.getAttribute("appoint");
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>预约消息</title>
	<style type="text/css">
	</style>
	<script type="text/javascript">
	function save(){
		var resultExplain = document.getElementById("resultExplain");
		var resultExplainVal = $.trim(resultExplain.value);
		resultExplain.value = resultExplainVal;
		if(!resultExplainVal){
			$.messager.alert("提示信息", "请输入指导内容。", "error", function(){
				resultExplain.focus();
			});
			return;
		}
		var status = document.getElementById("status");
		var statusVal = $.trim(status.value);
		status.value = statusVal;
		if(!statusVal){
			$.messager.alert("提示信息", "请选择结果。", "error", function(){
			});
			return;
		}
		$("#filterForm").form("submit", {
			url: "<%=request.getContextPath()%>/his/appoint_update.action",
			dataType : "json",
			success : function(data) {
				data =  $.parseJSON(data); 
				if(data.state == 1){
					$.messager.alert("提示信息", data.msg?data.msg:"回复成功。", "info", function(){
						try{
							parent.refreshGrid();
						}catch(e){}
						closeIt();
					});
				}else{
					$.messager.alert("提示信息", data.msg?data.msg:"回复失败。", "error");
				}
			}
		});
	}
	
	function closeIt() {
		try{
			parent.subWin2.window("close");
		}catch(e){}
	}
</script>
</head>
<body style="margin: 1px;padding: 0px;">
	<div>
		<form id="filterForm" method="post" accept-charset="UTF-8">
	<div class="sectionTitle">预约信息</div>
	<div>
			<table class="table">
				<tr>
					<td width="20%" class="td_title">医院名称:</td>
					<td>
						<input type="hidden" id="id" name="appoint.id" value="<%=appoint.getId()%>" />
						<%=appoint.getDeptName()%>
					</td>
					<td width="20%" class="td_title">科室名称:</td>
					<td>
						<%=appoint.getDepartName()%>
					</td>
				</tr>
				<tr>
					<td width="20%" class="td_title">用户名称:</td>
					<td width="30%">
						<%=appoint.getCustName()%>
					</td>
					<td width="20%" class="td_title">自述症状:</td>
					<td>
						<%=appoint.getSelfSymptom()%>
					</td>
				</tr>
				<tr>
					<td width="20%" class="td_title">初诊结果:</td>
					<td width="30%">
						<%=appoint.getFirstVisit()%>
					</td>
					<td width="20%" class="td_title">备注:</td>
					<td>
						<%=appoint.getMemo()%>
					</td>
				</tr>
				<tr>
					<td width="20%" class="td_title">预约日期:</td>
					<td width="30%">
						<%=appoint.getAppointTime()%>
					</td>
					<td width="20%" class="td_title">详细时间:</td>
					<td>
						<%if(appoint.getDetailTime()==1){%>
							全天
						<%}%>
						<%if(appoint.getDetailTime()==2){%>
							上午
						<%}%>
						<%if(appoint.getDetailTime()==3){%>
							下午
						<%}%>
					</td>
				</tr>
				<tr>
					<td class="td_title">咨询内容:</td>
					<td colspan="3" style="padding: 10px 3px 10px 3px;">
						<%=appoint.getFirstVisit()%>
					</td>
				</tr>
			</table>
		
	</div>
	<div class="sectionTitle" style="margin-top: 5px;">处理结果</div>
	<div>
			<table class="table">
				<%if(appoint.getStatus()!=0){%>
				<tr>
					<td width="20%" class="td_title">预约结果说明:</td>
					<td colspan="3" style="padding: 10px 3px 10px 3px;">
						<%if(appoint.getResultExplain()!=null){%>
							<%=appoint.getResultExplain()%>
						<%}%>
					</td>
				</tr>
				<%}%>
				<tr>
					<td  class="td_title">处理状态</td>
					<td colspan="3">
						<%if(appoint.getStatus()==1){%>
							预约成功
						<%}%>
						<%if(appoint.getStatus()==0){%>
							预约中
						<%}%>
						<%if(appoint.getStatus()==-1){%>
							预约失败
						<%}%>
					</td>
				</tr>
			</table>
		</div>
	</form>
	</div>
	<div style="text-align: center;padding: 5px;">
		<a class="easyui-linkbutton" href="javascript:closeIt()">关闭</a>
	</div>
</body>
</html>