<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.util.DateUtil"%>
<%@ page import="com.yzxt.yh.module.svb.bean.MemberInfo"%>
<%
MemberInfo memberInfo = (MemberInfo)request.getAttribute("memberInfo");
String nowStr = DateUtil.toHtmlDate(new Date());
String operType = (String)request.getAttribute("operType");
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>签约管理</title>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
	<style type="text/css">
	</style>
	<script type="text/javascript"> 
	function closeIt(){
		try{
			parent.subWin.window("close");
		}catch(e){}
	}
	function selDoc(){
		try{
			parent.selDocWin("<%=request.getContextPath()%>/svb/memberInfo_toDocSel.action?custId=<%=memberInfo.getCustId()%>", window);
		}catch(e){}
	}
	function save(){
		var endDay = document.getElementById("endDay");
		var endDayVal = endDay.value;
		if(!endDayVal){
			$.messager.alert("错误", "请选择签约截止日期", "error");
			return;
		}
		var doctorId = document.getElementById("doctorId");
		var doctorIdVal = doctorId.value;
		if(!doctorIdVal){
			$.messager.alert("错误", "请选择健康管理师", "error");
			return;
		}
		<%
		if(!"update".equals(operType))
		{
		%>
		$("#theForm").form("submit", {
			url: "<%=request.getContextPath()+"/svb/memberInfo_add.action"%>",
			dataType : "json",
			success : function(data) {
				data =  $.parseJSON(data); 
				if(data.state == 1){
					$.messager.alert("提示信息", data.msg?data.msg:"签约成功。", "info", function(){
						try{
							parent.refreshGrid();
						}catch(e){}
						closeIt();
					});
				}else{
					$.messager.alert("提示信息", data.msg?data.msg:"签约失败。", "error");
				}
			}
		});
		<%
		}else
		{
		%>
		$("#theForm").form("submit", {
			url: "<%=request.getContextPath()+"/svb/memberInfo_update.action"%>",
			dataType : "json",
			success : function(data) {
				data =  $.parseJSON(data); 
				if(data.state == 1){
					$.messager.alert("提示信息", data.msg?data.msg:"修改签约成功。", "info", function(){
						try{
							parent.refreshGrid();
						}catch(e){}
						closeIt();
					});
				}else{
					$.messager.alert("提示信息", data.msg?data.msg:"修改签约失败。", "error");
				}
			}
		});
		<%
		}
		%>
	}
</script>
</head>
<body style="margin: 1px;padding: 0px;">
	<div>
		<form id="theForm" style="margin: 0px;padding: 0px;" method="post" accept-charset="UTF-8">
			<table class="table">
				<tr>
					<td width="35%" class="td_title">用户名称:</td>
					<td>
						<%=memberInfo.getMemberName()%>
						<input type="hidden" id="memberId" name="memberInfo.id" value="<%=StringUtil.ensureStringNotNull(memberInfo.getId())%>" />
						<input type="hidden" id="custId" name="memberInfo.custId" value="<%=memberInfo.getCustId()%>" />
					</td>
				</tr>
				<tr>
					<td class="td_title">身份证号码:</td>
					<td>
						<%=StringUtil.ensureStringNotNull(memberInfo.getIdCard())%>
					</td>
				</tr>
				<tr>
					<td class="td_title">签约开始日期:</td>
					<td>
						<%=DateUtil.toHtmlDate(memberInfo.getStartDay())%>
						<input type="hidden" id="startDay" name="memberInfo.startDay" value="<%=DateUtil.toHtmlDate(memberInfo.getStartDay())%>" />
					</td>
				</tr>
				<tr>
					<td class="td_title">签约截止日期:</td>
					<td>
						<input type="text" readonly="readonly" class="Wdate" id="endDay" name="memberInfo.endDay" value="<%=DateUtil.toHtmlDate(memberInfo.getEndDay())%>" onclick="WdatePicker({minDate:'<%=nowStr%>'})" style="width: 100px;" />
					</td>
				</tr>
				<tr>
					<td class="td_title">健康管理师:</td>
					<td>
						<input type="text" readonly="readonly" id="docotrName" style="width: 100px;" value="<%=StringUtil.ensureStringNotNull(memberInfo.getDoctorName())%>" onclick="selDoc()" />
						<input type="hidden" id="doctorId" name="memberInfo.doctorId" value="<%=StringUtil.ensureStringNotNull(memberInfo.getDoctorId())%>" />
						<img alt="选择健康管理师" src="<%=request.getContextPath()%>/resources/img/search.png"  onclick="selDoc()" style="cursor: pointer;margin-bottom: -5px;" />
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div style="text-align: center;padding-top: 15px;">
		<a class="easyui-linkbutton" href="javascript:save();">保存</a>
		&nbsp;
		<a class="easyui-linkbutton" href="javascript:closeIt();">关闭</a>
	</div>
</body>
</html>