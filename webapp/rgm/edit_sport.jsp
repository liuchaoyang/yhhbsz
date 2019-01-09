<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.sql.Timestamp"%>
<%@ page import="java.util.List"%>
<%@ page import="com.yzxt.yh.util.DateUtil"%>
<%@ page import = "com.yzxt.yh.module.rgm.bean.Sport" %>
<%@ page import = "com.yzxt.yh.module.rgm.bean.SportLog" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% 
	List<SportLog> sportLogs =(List<SportLog>)request.getAttribute("sportLogs");
	Timestamp sportTime = (Timestamp)request.getAttribute("sportTime");
	Timestamp currentTime = (Timestamp)request.getAttribute("now");
	
%>
<!DOCTYPE HTML>
<html>
<head>
<%@ include file="../common/inc.jsp"%>
<script type="text/javascript" src="<%=request.getContextPath()%>/pgchr/chr-common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script> 
<style type="text/css">
.td {
	text-align:center;
	font-size: 13px;
	background:#eeeeee;
	width:16%;
}

.table_tr_td {
	text-align:center;
	background:#cccccc;
	font-weight:bold;
}
</style>
<script> 
	function deleteOneRow(){
			var checkboxArr = document.getElementsByName("ckbSport");
			var c = 0;		
			for(var i=0;i<checkboxArr.length;i++){
				if(checkboxArr[i].checked){
					c++;
				}
			}
			if(c==0){
				$.messager.alert("提示信息","请至少选择一条记录删除");
					return;
			}
			var sportHtmlTb = document.getElementById("sporthtml");
			for(var i=checkboxArr.length-1;i>=0;i--){
				if(checkboxArr[i].checked){
						sportHtmlTb.deleteRow(i);
				}	
			}                       
		}
		
	function addOneRow(){
			var sportpt = $("#sporthtml");
			sportpt.append('<tr class="td"><td style="width:2%"><input type="checkbox" name="ckbSport" ></td>'
				+ '<td style="width:27%"><select  name="sportType" onchange= "changeCata(this.value, this.parentNode.parentNode)" ><option value="">请选择</option><option value="1">轻度</option><option value="2">中度</option><option value="3">稍强度</option></select></td>'
				+ '<td style="width:27%"><select style="width:160px" name="sportId"><option value="">请选择</option></select></td>'
				+ '<td><input maxlength="4" name="timeSpan" type="text" style="width:80px;"/>&nbsp;分钟</td></tr>');  
		}
		
	function changeCata(sportType, row){
		var rowIdx = row.rowIndex;
		var sportSel = document.getElementsByName("sportId")[rowIdx-1];
		clearSel(sportSel);
		if(sportType){
			$.ajax({
				type: "POST",
				dataType: "json",
				url: "<%=request.getContextPath()%>/rgm/sport_getSportInfo.action?sportType=" + sportType,
				async : true,
				timeout : 300000,
				success : function(data){
					if(data){
						for(var i=0; i<data.length; i++){
							sportSel.options.add(new Option(data[i].name, data[i].id));
						}
					}
				},
				error : function(){
					$.messager.alert("提示信息", "取消失败！", "error");
			 	}
			});
		}
	}
	
	function clearSel(selEle){
		selEle.options.length = 0;
        selEle.options.add(new Option("请选择", ""));
	}
	
	function saveSportLogs(){
		var re = /^[1-9]\d*$/;
		var sportTime = document.getElementById("sportTime").value;
		var sportTypeArr = document.getElementsByName("sportType");	
		var sportIdArr = document.getElementsByName("sportId");
		var timeSpanArr = document.getElementsByName("timeSpan");
		for(var i=0;i<sportTypeArr.length;i++){
			if((sportTypeArr[i].value)==""){
				$.messager.alert("提示信息","请选择运动类型");
				return;
			} 
			if((sportTypeArr[i].value)!==""&&(sportIdArr[i].value)==""){
				$.messager.alert("提示信息","请选择运动名称");
				return;
			}
			for(var j=1;j<sportTypeArr.length;j++){
				if((sportIdArr[j].value==sportIdArr[i].value)&&(j!=i)){
					if(sportTypeArr[i].value==sportTypeArr[j].value){
						$.messager.alert("提示信息","运动名称相同，请重新选择", "error", function(){
							sportIdArr[j].focus();
						});
						return;
					}
				}
			}
			if((timeSpanArr[i].value)==""){
				$.messager.alert("提示信息","请填写运动时长");
				return;
			}
			if(!re.test(timeSpanArr[i].value)){
				$.messager.alert("提示信息","必须为正整数!");
				timeSpanArr[i].value="";
				timeSpanArr[i].focus();
				return;
			}			
		}
		var url = "<%=request.getContextPath()%>/rgm/sportLog_addSportLogs.action?sportTime=" + sportTime ;
		$("#sportForm").form("submit", {
				url:url,
			onSubmit : function() {
				return true;
			},
			success : function(data){
				data =  $.parseJSON(data);
				if (data.msg >0) {
					$.messager.alert("提示信息", "添加成功.", "info");
					refreshGrid(); 
					closeSubWin();
				} else {
					$.messager.alert("提示信息", data.msg ? data.msg : "添加失败.", "error");
				}
			}
		});
	}
	
	function changeTime(){
		var sportTimeVal = document.getElementById("sportTime").value;
		if(sportTimeVal != document.getElementById("sportTime2").value){
			location.href = "<%=request.getContextPath()%>/rgm/sportLog_toEdit.action?sportTime=" + document.getElementById("sportTime").value;
		}
	}
</script>
</head>
<body>
<div style="padding:5px;width:900px;margin-left: auto;margin-right: auto;">
	<div class="easyui-panel" title="添加运动">
		<table  width="100%" border="0"  cellpadding="0" cellspacing="0" class="data_table">
  			<tr>
  				<td class="td">填写人：</td>
  				<td style="text-align:left;width:34%;">${userInfo.name}</td>
  				<td class="td">运动时间：</td>
  				<td style="text-align:left;">
  					<input type="text" class="Wdate" onclick="WdatePicker({isShowClear:false,onpicked:changeTime,maxDate:'<%=DateUtil.toHtmlDate(currentTime)%>'})" id = "sportTime" name="sportTime"  readonly ="readonly" value='<%=DateUtil.toHtmlDate(sportTime)%>' onclick="WdatePicker()" style="width: 100px;" onchange="changeTime(this.value)" />
  					<input type="hidden" id="sportTime2" value="<%=DateUtil.toHtmlDate(sportTime)%>" />
  				</td>
  			</tr>
		</table>
	</div>
	<div style="margin-bottom:10px;">
	</div>
	<div class="easyui-panel" title="运动详情">
		<form id="sportForm" action="" method="post"  style="margin: 0px;padding: 0px;">
	    	<table id="theme" width="100%" border="0"  cellpadding="0" cellspacing="0" class="data_table">
					<tr >
						<td>
							<a name="maddbtn" href="javascript:addOneRow()" class="easyui-linkbutton">添加</a>
							<a name="maddbtn1" href="javascript:deleteOneRow()" class="easyui-linkbutton" >删除</a>
						</td>
					</tr>
  			</table>
  			<table id="sporthtml" width="100%"  class="data_table">
					<tr class="table_tr_td">
						<td style="width:2%;"><input type="checkbox" name="ckbSport" />
						</td>
						<td style="width:27%;">运动类型</td>
						<td style="width:27%;">运动项目</td>
						<td style="width:35%;">运动时长</td>
					</tr>
					<% 
						if(sportLogs.size()==0){ 
					%>
					<tr class="td"><td style="width:2%"><input type="checkbox" name="ckbSport" ></td>
				<td style="width:27%"><select  name="sportType" onchange= "changeCata(this.value, this.parentNode.parentNode)" ><option value="">请选择</option><option value="1">轻度</option><option value="2">中度</option><option value="3">稍强度</option></select></td>
				<td style="width:27%"><select style="width:160px" name="sportId"><option value="">请选择</option></select></td>
				<td><input  name="timeSpan" maxlength="4" type="text" style="width:80px;"/>&nbsp;分钟</td></tr>
					<% }else %>
					<% { %>
					<%
					for(SportLog sportLog : sportLogs)
					{
					%>
					<tr class="td">
						<td>
							<input type="checkbox" name="ckbSport" />
						</td>
						<td>
							
							<select name ="sportType">
								<option value="1" <%=sportLog.getSportType().intValue()==1?"selected=\"selected\"":""%>>轻度</option>
								<option value="2" <%=sportLog.getSportType().intValue()==2?"selected=\"selected\"":""%>>中度</option>
								<option value="3" <%=sportLog.getSportType().intValue()==3?"selected=\"selected\"":""%>>稍强度</option>
							</select>
						</td>
						<td>
							
							<select style="width:160px" name="sportId">
								<option value="">请选择</option>
								<%
								List<Sport> sportList = sportLog.getSportList();
								for(Sport sport : sportList)
								{
								
								%>
								<option value="<%=sport.getId()%>" <%=sport.getId().equals(sportLog.getSportId())?"selected=\"selected\"":""%>><%=sport.getName()%></option>
								<%
								}
								%>
							</select>
						</td>
						<td>
						<input  name="timeSpan" maxlength="4" type="text" style="width:80px;" value="<%=sportLog.getTimeSpan()%>" />&nbsp;分钟</td>
					</tr>
					<%
					}
					%>
					<%
					}
					%>
			</table>
	    </form>
	</div>
	<div style="text-align:center;padding:20px;">
		<a name="maddbtn" href="javascript:saveSportLogs()" class="easyui-linkbutton"">保存运动日志</a>
	</div>
</div>
</body>
</html>