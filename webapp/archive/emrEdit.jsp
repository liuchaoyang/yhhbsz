<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.util.DateUtil"%>
<%@ page import="com.yzxt.yh.module.ach.bean.Emr"%>
<%
Emr emr = (Emr)request.getAttribute("emr");
String operType = request.getParameter("operType");
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
	<title>电子病历信息</title>
	<style type="text/css">
	</style>
	<script type="text/javascript">
	function closeIt(){
		try{
			parent.subWin.window("close");
		}catch(e){}
	}
	function validForm(){
		<%--就诊日期--%>
		var treatDate = document.getElementById("treatDate");
		var treatDateVal = treatDate.value;
		if(!treatDateVal){
			$.messager.alert("提示信息", "请输入就诊日期。", "error", function(){
				treatDate.focus();
			});
			return false;
		}
		<%--诊疗信息--%>
		var diagnosis = document.getElementById("diagnosis");
		var diagnosisVal = $.trim(diagnosis.value);
		if(!$.trim(diagnosis.value)){
			$.messager.alert("提示信息", "请输入诊疗信息。", "error", function(){
				diagnosis.focus();
			});
			return false;
		}else if(diagnosis.value.length>300){
			$.messager.alert("提示信息", "诊疗信息不能超过300个字符。", "error", function(){
				diagnosis.focus();
			});
			return false;
		}
		<%--检查化验--%>
		var test = document.getElementById("test");
		if(test.value.length>200){
			$.messager.alert("提示信息", "检查化验不能超过200个字符。", "error", function(){
				test.focus();
			});
			return false;
		}
		<%--其它信息--%>
		var other = document.getElementById("other");
		if(other.value.length>200){
			$.messager.alert("提示信息", "其它信息不能超过200个字符。", "error", function(){
				other.focus();
			});
			return false;
		}
		return true;
	}
	function save(){
		if(!validForm()){
			return;
		}
		$("#mainForm").form("submit", {
			url: "<%=request.getContextPath()%>" + "/ach/emr_save.action",
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
<body style="margin: 0px;padding: 1px;">
	<div>
		<form id="mainForm" style="margin: 0px;padding: 0px;" method="post" accept-charset="UTF-8">
			<table class="table">
				<tr>
					<td width="18%" class="td_title">客户姓名:</td>
					<td width="32%">
						<%=emr.getCustName()%>
						<input type="hidden" name="emr.id" value="<%=StringUtil.ensureStringNotNull(emr.getId())%>" />
						<input type="hidden" name="emr.custId" value="<%=emr.getCustId()%>" />
					</td>
					<td width="18%" class="td_title">就诊日期:</td>
					<td>
						<input class="Wdate" style="width: 100px;" id="treatDate" name="emr.treatDate" readonly="readonly" value="<%=DateUtil.toHtmlDate(emr.getTreatDate())%>" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'endTreatDate\')}'})" />
					</td>
				</tr>
				<tr>
					<td class="td_title">诊疗信息:</td>
					<td colspan="3">
						<textarea id="diagnosis" name="emr.diagnosis" maxlength="300" style="width: 90%;height: 120px;"><%=StringUtil.ensureStringNotNull(emr.getDiagnosis())%></textarea>
					</td>
				</tr>
				<tr>
					<td class="td_title">检查化验:</td>
					<td colspan="3">
						<textarea id="test" name="emr.test" maxlength="200" style="width: 90%;height: 80px;"><%=StringUtil.ensureStringNotNull(emr.getTest())%></textarea>
					</td>
				</tr>
				<tr>
					<td class="td_title">其它信息:</td>
					<td colspan="3">
						<textarea id="other" name="emr.other" maxlength="200" style="width: 90%;height: 80px;"><%=StringUtil.ensureStringNotNull(emr.getOther())%></textarea>
					</td>
				</tr>
			</table>
		</form>
		<div style="text-align: center;padding-top: 7px;">
			<a class="easyui-linkbutton" href="javascript:save();">保存</a>
			&nbsp;
			<a class="easyui-linkbutton" href="javascript:closeIt();">关闭</a>
		</div>
	</div>
</body>
</html>