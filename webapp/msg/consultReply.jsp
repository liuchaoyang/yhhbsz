<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.module.msg.bean.ConsultGuide"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.util.DateUtil"%>
<% 
	ConsultGuide consultGuide = (ConsultGuide) request.getAttribute("consultGuide");
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>预警消息</title>
	<style type="text/css">
	td{
		word-break: break-all;
	}
	</style>
	<script type="text/javascript">
	function save(){
		var guideContext = document.getElementById("guideContext");
		var guideContextVal = $.trim(guideContext.value);
		guideContext.value = guideContextVal;
		if(!guideContextVal){
			$.messager.alert("提示信息", "请输入指导内容。", "error", function(){
				guideContext.focus();
			});
			return;
		}
		//备注
		var guideContext = document.getElementById("guideContext");
		if(guideContext.value.length > 200){
			$.messager.alert("提示信息", "字数不能超过200个！", "error", function(){
				guideContext.focus();
			});
			return;
		}
		$("#filterForm").form("submit", {
			url: "<%=request.getContextPath()%>/msg/consultGuide_updateReply.action",
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
			parent.subWin.window("close");
		}catch(e){}
	}
</script>
</head>
<body style="margin: 1px;padding: 0px;">
	<div>
		<form id="filterForm" method="post" accept-charset="UTF-8">
	
	<div class="sectionTitle">咨询信息</div>
	<div>
		
			<table class="table">
				<tr>
					<td width="20%" class="td_title">标题:</td>
					<td colspan="3">
						<input type="hidden" id="id" name="consultGuide.id" value="<%=consultGuide.getId()%>" />
						<%=consultGuide.getConsultTitle()%>
					</td>
				</tr>
				<tr>
					<td width="20%" class="td_title">咨询人:</td>
					<td width="30%">
						<%=consultGuide.getMemberName()%>
					</td>
					<td width="20%" class="td_title">咨询时间:</td>
					<td>
						<%=consultGuide.getConsultTime()%>
					</td>
				</tr>
				<tr>
					<td class="td_title">咨询内容:</td>
					<td colspan="3" style="padding: 10px 3px 10px 3px;">
						<%=consultGuide.getConsultContext()%>
					</td>
				</tr>
			</table>
		
	</div>
	<div class="sectionTitle" style="margin-top: 5px;">指导信息</div>
	<div>
		
			<table class="table">
				<tr>
					<td  class="td_title">指导内容:</td>
					<td colspan="3" style="padding: 10px 3px 10px 3px;">
						<textarea maxlength="200" id="guideContext" name="consultGuide.guideContext"  style="padding: 5px;width: 95%;height: 70px;"></textarea>
					</td>
				</tr>
				<tr>
					<td width="20%" class="td_title">指导人:</td>
					<td colspan="3" >
						<%=consultGuide.getDoctorName()%>
					</td>
					<%-- <td width="20%" class="td_title">指导时间:</td>
					<td width="30%">
						<%=consultGuide.getGuideTime()%>
					</td> --%>
				</tr>
			</table>
		
		</div>
	</form>
	</div>
	<div style="text-align: center;padding: 5px;">
		<a class="easyui-linkbutton" href="javascript:save()">保存</a>
		&nbsp;&nbsp;
		<a class="easyui-linkbutton" href="javascript:closeIt()">关闭</a>
	</div>
</body>
</html>