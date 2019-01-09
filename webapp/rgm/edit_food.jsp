<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="java.sql.Timestamp"%>
<%@ page import="java.util.List"%>
<%@ page import="com.yzxt.yh.util.DateUtil"%>
<%@ page import="com.yzxt.yh.util.DecimalUtil"%>
<%@ page import="com.yzxt.yh.module.rgm.bean.FoodCatalog"%>
<%@ page import="com.yzxt.yh.module.rgm.bean.Food"%>
<%@ page import="com.yzxt.yh.module.rgm.bean.DietaryLog"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	List<DietaryLog> detailLogs =(List<DietaryLog>)request.getAttribute("detailLogs");
	List<FoodCatalog> foodCatalogs =(List<FoodCatalog>)request.getAttribute("foodCatalogs");
	Timestamp dietaryTime = (Timestamp)request.getAttribute("dietaryTime");
	Timestamp currentTime = (Timestamp)request.getAttribute("now");
 %>
<!DOCTYPE html >
<html >
<head>
<%@ include file="../common/inc.jsp"%>
<script type="text/css" src="<%=request.getContextPath()%>/resources/css/flow.css"></script>
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
			var checkboxArr = document.getElementsByName("ckbFood");
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
			var foodhtmlTb = document.getElementById("foodhtml");
			for(var i=checkboxArr.length-1;i>=0;i--){
				if(checkboxArr[i].checked){
						foodhtmlTb.deleteRow(i);
				}	
			}                       
		}
		
	function addOneRow(){
			var foodpt = $("#foodhtml");
			foodpt.append('<tr class="td"><td style="width:2%"><input type="checkbox" name="ckbFood" ></td>'
				+ '<td style="width:10%" ><select name ="foodType" ><option value="1">早餐</option><option value="2">午餐</option><option value="3">晚餐</option><option value="4">加餐</option></select></td>'
				+ '<td style="width:25%"><select style="width:110px" name="foodCata" onchange= "changeCata(this.value, this.parentNode.parentNode)" ><option value="">请选择</option><option value="_dadou">大豆类</option><option value="_gushu">谷薯类</option><option value="_naizhi">奶制类</option><option value="_roudan">肉蛋类</option><option value="_shucai">蔬菜</option><option value="_shuiguo">水果类</option><option value="_yingguo">硬果类</option><option value="_youzhi">油脂类</option></select></td>'
				+ '<td style="width:25%"> <select style="width:160px" name="foodId"><option value="">请选择</option></select></td>'
				+ '<td><input  maxlength="4" name="foodweight" type="text" style="width:80px;"/>&nbsp;克</td></tr>');  
		}
		
	function changeCata(cata, row){
		var rowIdx = row.rowIndex;
		var foodSel = document.getElementsByName("foodId")[rowIdx-1];
		clearSel(foodSel);
		if(cata){
			$.ajax({
				type: "POST",
				dataType: "json",
				url: "<%=request.getContextPath()%>/rgm/food_getfood.action?catalogId=" + cata,
				async : true,
				timeout : 300000,
				success : function(data){
					if(data){
						for(var i=0; i<data.length; i++){
							foodSel.options.add(new Option(data[i].name, data[i].id));
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
	
	function saveDietaryLogs(){
		var re = /^[1-9]\d*$/;
		var dietaryTime = document.getElementById("dietaryTime").value;
		var foodTypeArr = document.getElementsByName("foodType");
		var foodCataArr = document.getElementsByName("foodCata");		
		var foodIdArr = document.getElementsByName("foodId");
		var foodweightArr = document.getElementsByName("foodweight");
		for(var i=0;i<foodTypeArr.length;i++){
			if((foodCataArr[i].value)==""){
				$.messager.alert("提示信息","请选择食物分类", "error", function(){
					foodCataArr[i].focus();
				});
				return;
			}
			
			if((foodCataArr[i].value)!==""&&(foodIdArr[i].value)==""){
				$.messager.alert("提示信息","请选择食物名称", "error", function(){
					foodIdArr[i].focus();
				});
				return;
			}
			for(var j=1;j<foodTypeArr.length;j++){
				if((foodIdArr[j].value==foodIdArr[i].value)&&(j!=i)){
					if(foodTypeArr[i].value==foodTypeArr[j].value){
						$.messager.alert("提示信息","食物名称相同，请重新选择", "error", function(){
							foodIdArr[j].focus();
						});
						return;
					}
				}
			}
			if((foodweightArr[i].value)==""){
				$.messager.alert("提示信息","请填写食物重量", "error", function(){
					foodweightArr[i].focus();
				});
				return;
			}
			if(!re.test(foodweightArr[i].value)){
				$.messager.alert("提示信息","必须为正整数!", "error", function(){
					foodweightArr[i].focus();
				});
				foodweightArr[i].value="";
				return;
			}			
		}
		var url = "<%=request.getContextPath()%>/rgm/dietaryLog_addDietaryLogs.action?dietaryTime=" + dietaryTime ;
		$("#dietaryForm").form("submit", {
				url:url,
			onSubmit : function() {
				return true;
			},
			success : function(data){
				data =  $.parseJSON(data);
				if (data.msg >0) {
					$.messager.alert("提示信息", "添加成功.", "info");
				} else {
					$.messager.alert("提示信息", data.msg ? data.msg : "添加失败.", "error");
				}
			}
		});
	}
	
	function changeTime(){
		var dietaryTimeVal = document.getElementById("dietaryTime").value;
		if(dietaryTimeVal != document.getElementById("dietaryTime2").value){
			location.href = "<%=request.getContextPath()%>/rgm/dietaryLog_toEdit.action?dietaryTime=" + document.getElementById("dietaryTime").value;
		}
	}
	//校验，当输入完之后就校验onkeyup="checkNum(this)
	/* function checkNum(obj)
	{
		var re = /^[1-9]\d*$/;
		var foodweightArr = document.getElementsByName("foodweight");
		for(var i=0;i<foodweightArr.length;i++){
			if(!re.test(foodweightArr[i].value)){
				$.messager.alert("提示信息","必须为正整数!");
				foodweightArr[i].value="";
				foodweightArr[i].focus();
				return;
			}			
		}
	}  */
</script>
</head>
<body>
<div style="padding:5px;width:900px;margin-left: auto;margin-right: auto;">
	<div class="easyui-panel" title="添加膳食">
		<table  width="100%" border="0"  cellpadding="0" cellspacing="0" class="data_table">
  			<tr>
  				<td class="td">填写人：</td>
  				<td style="text-align:left;width:34%;">${userInfo.name}</td>
  				<td class="td">摄入时间：</td>
  				<td style="text-align:left;">
  					<input type="text" class="Wdate" onclick="WdatePicker({isShowClear:false,onpicked:changeTime,maxDate:'<%=DateUtil.toHtmlDate(currentTime)%>'})" id = "dietaryTime" name="dietaryTime"  readonly ="readonly" value='<%=DateUtil.toHtmlDate(dietaryTime)%>' onclick="WdatePicker()" style="width: 100px;" onchange="changeTime(this.value)" />
  					<input type="hidden" id="dietaryTime2" value="<%=DateUtil.toHtmlDate(dietaryTime)%>" />
  				</td>
  			</tr>
		</table>
	</div>
	<div style="margin-bottom:10px;">
	</div>
	<div class="easyui-panel" title="膳食详情">
			<form id="dietaryForm" action="" method="post"  style="margin: 0px;padding: 0px;">
				<table id="theme" width="100%" border="0"  cellpadding="0" cellspacing="0" class="data_table">
					<tr >
						<td>
							<a name="maddbtn" href="javascript:addOneRow()" class="easyui-linkbutton">添加</a>
							<a name="maddbtn1" href="javascript:deleteOneRow()" class="easyui-linkbutton" >删除</a>
						</td>
					</tr>
  				</table>
				<table id="foodhtml" width="100%" border="0" cellpadding="0" cellspacing="0" class="data_table">
					<tr class="table_tr_td">
						<td style="width:2%;"><input type="checkbox" name="ckbFood" />
						</td>
						<td style="width:12%;">餐次</td>
						<td style="width:27%;">食物分类</td>
						<td style="width:35%;">食物名称</td>
						<td>摄入量</td>
					</tr>
					<% 
						if(detailLogs.size()==0){
					%>
					<tr class="td"><td style="width:2%"><input type="checkbox" name="ckbFood" /></td>
				<td style="width:10%" ><select name ="foodType" ><option value="1">早餐</option><option value="2">午餐</option><option value="3">晚餐</option><option value="4">加餐</option></select></td>
				<td style="width:25%"><select style="width:110px" name="foodCata" onchange= "changeCata(this.value, this.parentNode.parentNode)" ><option value="">请选择</option><option value="_dadou">大豆类</option><option value="_gushu">谷薯类</option><option value="_naizhi">奶制类</option><option value="_roudan">肉蛋类</option><option value="_shucai">蔬菜</option><option value="_shuiguo">水果类</option><option value="_yingguo">硬果类</option><option value="_youzhi">油脂类</option></select></td>
				<td style="width:25%"> <select style="width:160px" name="foodId"><option value="">请选择</option></select></td>
				<td><input  maxlength="4" name="foodweight" type="text" style="width:80px;"/>&nbsp;克</td></tr>
				<%}else %>
				<%
				 {
				%>
					<%
					for(DietaryLog dietaryLog : detailLogs)
					{
					%>
					<tr class="td">
						<td>
							<input type="checkbox" name="ckbFood" />
						</td>
						<td>
							<select name ="foodType">
								<option value="1" <%=dietaryLog.getDietaryType().intValue()==1?"selected=\"selected\"":""%>>早餐</option>
								<option value="2" <%=dietaryLog.getDietaryType().intValue()==2?"selected=\"selected\"":""%>>午餐</option>
								<option value="3" <%=dietaryLog.getDietaryType().intValue()==3?"selected=\"selected\"":""%>>晚餐</option>
								<option value="4" <%=dietaryLog.getDietaryType().intValue()==4?"selected=\"selected\"":""%>>加餐</option>
							</select>
						</td>
						<td>
							
							<select style="width:110px" name="foodCata" onchange="changeCata(this.value, this.parentNode.parentNode)">
								<option value="">请选择</option>
								<%
								for(FoodCatalog foodCatalog : foodCatalogs)
								{
								%>
								<option value="<%=foodCatalog.getId()%>" <%=foodCatalog.getId().equals(dietaryLog.getFoodCatalogId())?"selected=\"selected\"":""%>><%=foodCatalog.getName()%></option>
								<%
								}
								%>
							</select>
						</td>
						<td>
							
							<select style="width:160px" name="foodId">
								<option value="">请选择</option>
								<%
								List<Food> foodList = dietaryLog.getFoodList();
								for(Food food : foodList)
								{
								
								%>
								<option value="<%=food.getId()%>" <%=food.getId().equals(dietaryLog.getFoodId())?"selected=\"selected\"":""%>><%=food.getName()%></option>
								<%
								}
								%>
							</select>
						</td>
						<td>
						<input  name="foodweight" type="text" style="width:80px;" value="<%=DecimalUtil.toIntegerString(dietaryLog.getFoodWeight())%>" />&nbsp;克</td>
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
		<a name="maddbtn" href="javascript:saveDietaryLogs()" class="easyui-linkbutton"">保存膳食日志</a>
	</div> 
</div>
</body>
</html>