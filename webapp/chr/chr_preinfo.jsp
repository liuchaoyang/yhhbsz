<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page language="java" import="com.yzxt.yh.module.chr.bean.Visit" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	Visit chrVisit = (Visit) request.getAttribute("chrVisit");
 %>
<!DOCTYPE html>
<html>
	<head>
		<title>血压随访记录表</title>
		<%@ include file="../common/inc.jsp"%>
		<script type="text/javascript" src="<%=request.getContextPath() %>/chr/chr-common.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script> 
		<% Integer opt =request.getAttribute("opt")==null?0:(Integer)request.getAttribute("opt");%>
	<style type="text/css">
	.table_title {
		padding: 15px 10px;
		background: #D0EEAE;
		font-weight: bold;
		font-size: 16px;
		}
		tr {
		display: table-row;
		vertical-align: inherit;
		border-color: inherit;
		}
		Inherited from div.easyui-panel.panel-body
		.panel-body {
		color: #000000;
		}
		Inherited from div.panel
		.panel {
		font-size: 12px;
		text-align: left;
		}
		form {
		display: block;
		margin-top: 0em;
		}
		
		div {
		display: block;
		}
		tbody {
		display: table-row-group;
		vertical-align: middle;
		border-color: inherit;
		}
		
		.data_table td {
		border-color: #CCCCCC;
		border-width: 0px 1px 1px 0px;
		border-style: solid;
		padding: 5px;
		}
		TD {
		FONT-SIZE: 13px;
		FONT-FAMILY: Arial, Helvetica, sans-serif;
		}
		td[Attributes Style] {
		width: 75px;
		height: 35px;
		}
		user agent stylesheettd, th {
		display: table-cell;
		vertical-align: inherit;
		}
		Inherited from table.data_table
		user agent stylesheettable {
		border-collapse: separate;
		border-spacing: 2px;
		border-color: gray;
		}
			.mainform td{ border:none;}
			.input150{ width:150px;}
			.input100{ width:100px;}
			.input50{ width:50px;}
	</style>
</head>
<body>
<div style="padding:5px;margin-left: 150px;">
	<div class="easyui-panel" title="高血压随访记录表" style="width:850px;">
	    <form id="mform" action="chrpre" method="post">
	    	<input type="hidden" id="custId" name="info.custId" value="<%=chrVisit.getCustId()%>" />
	    	<input type="hidden" id="visitId" name="info.visitId" value="<%=chrVisit.getId()%>" />
	    	<table  width="100%" border="0"  cellpadding="0" cellspacing="0" class="data_table">
				<tr>
					<td colspan="4" align="center" class="table_title">个人信息</td>
				</tr>
				<tr>
				    <td width="15%" height="35">姓名</td>
				    <td width="35%"><%=chrVisit.getMemberName()%></td>
				    <td width="20%">编号</td>
				    <td><%=chrVisit.getVisitNo()%></td>
			    </tr>
				<tr>
				    <td>随访时间</td>
				    <td><input type = "text" name="info.flupDateStr"  value="<%=chrVisit.getFlupDateStr()%>" readonly="readonly" style="height:25px;width:150px"/></td>
				    <td>随访方式</td>
				    <td>
					    <label><input type="radio" name="info.flupType" value="1" />门诊</label>
					    &nbsp;&nbsp;&nbsp;
						<label><input type="radio" name="info.flupType" value="2" />家庭</label>
						&nbsp;&nbsp;&nbsp;
						<label><input type="radio" name="info.flupType" value="3" />电话</label>
					</td>
				</tr>
				<tr>
					<td colspan="4" align="left" class="table_title">症状描述</td>
				</tr>
		        <tr>
		        	<td colspan="4">
		        		<table width="100%" class="mainform">
		        			<tr>
					          <td width="120"><input type="checkbox" name="zhengz" value="1" />无症状</td>
					          <td width="120" nowrap="nowrap"><input type="checkbox" name="zhengz" value="2" />头痛头晕</td>
					          <td width="120"><label><input type="checkbox" name="zhengz" value="3" />恶心呕吐</label></td>
					          <td width="120"><input type="checkbox" name="zhengz" value="4" />眼花耳鸣</td>
					          <td><label><input type="checkbox" name="zhengz" value="5" />呼吸困难</label></td>
		            		</tr>
		            		<tr>
			                    <td><input type="checkbox" name="zhengz" value="6" />心悸胸闷</td>
			                    <td nowrap="nowrap"><input type="checkbox" name="zhengz" value="7" />鼻衄出血不止</td>
			                    <td><input type="checkbox" name="zhengz" value="8" />四肢发麻</td>
			                    <td><label><input type="checkbox" name="zhengz" value="9"/>下肢水肿</label></td>
			                    <td>&nbsp;</td>
			                </tr>
		            	</table>
		            </td>
		        </tr>
		        <tr height="50">
			    	<td>其它</td>
			    	<td colspan="3"><textarea name="info.hbpSymptomOther" class="easyui-validatebox"  onkeyup="LimitTextArea(this,100)" onkeydown="LimitTextArea(this,100)" style="height:50px;width:575px">${info.hbpSymptomOther}</textarea></td>
			    </tr>
				<tr>
				  <td colspan="4" align="left" class="table_title">体征</td>
				</tr>
				  <tr>
				    <td height="30">体重(KG)</td>
				    <td colspan="3"><input class="easyui-numberbox input150" type="text" name="info.hbpWeight" value="${info.hbpWeight}" data-options="min:0,max:900,precision:1"/></td>
				  </tr>
				  <tr>
				    <td height="30">收缩压(mmHg)</td>
				    <td><input class="easyui-numberbox input150" data-options="required:true" type="text" name="info.hbpBps" value="${info.hbpBps}" maxlength="3"/></td>
				    <td >舒张压(mmHg)</td>
				    <td><input class="easyui-numberbox input170" type="text" name="info.hbpBpd" value="${info.hbpBpd}" maxlength="3" data-options="required:true"/></td>
				  </tr>
				  <tr>
				    <td height="30">体质指数</td>
				    <td><input class="easyui-numberbox input150" type="text" name="info.hbpPhysique" value="${info.hbpPhysique}" data-options="min:0,max:900,precision:1"/></td>
				    <td>心率</td>
				    <td><input class="easyui-numberbox input170" type="text" name="info.hbpPulse" value="${info.hbpPulse}" maxlength="3"/></td>
				  </tr>
				  <tr>
				    <td height="30">其它</td>
				    <td colspan="3"><textarea name="info.hbpOther" class="easyui-validatebox" style="height:60px;width:575px" onkeyup="LimitTextArea(this,100)" onkeydown="LimitTextArea(this,100)">${info.hbpOther}</textarea></td>
				  </tr>
				  <tr>
				  	<td colspan="4" align="left" class="table_title">生活方式指导</td>
				  </tr>
				  <tr>
				    <td height="30">日吸烟量(支)</td>
				    <td><input class="easyui-numberbox input170" type="text" name="info.hbpSmoking" value="${info.hbpSmoking}" data-options="min:0,max:900"/></td>
				    <td>日饮酒量(两)</td>
				    <td><input class="easyui-numberbox input170" type="text" name="info.hbpDrinking" value="${info.hbpDrinking}" data-options="min:0,max:900"/></td>
				  </tr>
				  <tr>
				  	<td height="30">运动</td>
				  	<td colspan="2">
				  		<table id="sporthtml" class="mainform">
				  		<%-- <%if(opt==1){%> --%>
				  			<tr>
				  				<td><input type="checkbox" name="ckbSport" /></td>
				  				<td ><label>每次</label><input class="easyui-numberbox input50" type="text" name="sportm" value="" style="width:100px" data-options="min:0,max:500"/>分钟</td>
				  				<td><label>每周</label><input class="easyui-numberbox input50" type="text" name="sportt" value="" style="width:100px" data-options="min:0,max:500"/>次</td>
				  			</tr>
				  		<%-- <%}%> --%>
				  		</table>
				  	</td>
				  	<td>
				  		<table class="mainform">
				  			<tr>
				  				<td><a name="maddbtn" href="javascript:addSportInput()" class="easyui-linkbutton">添加</a></td>
				  				<td><a name="maddbtn1" href="javascript:deleteSportInput()" class="easyui-linkbutton">删除</a></td>
				  			</tr>
				  		</table>
				  	</td>
				  </tr>
				  <tr>
    <td height="30">摄盐情况(咸淡)</td>
    <td >
	<label><input type="radio" name="info.hbpSalarium" value="1" />轻</label> 
	&nbsp;&nbsp;&nbsp;&nbsp;
	<label><input type="radio" name="info.hbpSalarium" value="2" />中</label>
	&nbsp;&nbsp;&nbsp;&nbsp;
      <label><input type="radio" name="info.hbpSalarium" value="3" />重</label></td>
    <td>心理调整</td>
    <td>
	<label><input type="radio" name="info.hbpPsycrecovery" value="1" />良好 </label>
	&nbsp;&nbsp;
      <label><input type="radio" name="info.hbpPsycrecovery" value="2" />一般</label>
	  &nbsp;&nbsp;
      <label><input type="radio" name="info.hbpPsycrecovery" value="3" />差</label></td>
  </tr>
  <tr>
    <td height="30">遵医行为</td>
    <td colspan="3"><label><input type="radio" name="info.hbpCompliance" value="1" />良好 </label>
	&nbsp;
      <label><input type="radio" name="info.hbpCompliance" value="2" />一般</label>
	  &nbsp;
      <label><input type="radio" name="info.hbpCompliance" value="3" />差</label></td>
  </tr>
  
  <tr><td colspan="4" align="left" class="table_title">辅助信息</td></tr>
  <tr>
    <td height="30">辅助检查</td>
    <td colspan="3"><textarea name="info.hbpHelpCheck" class="easyui-validatebox" style="height:60px;width:575px" onkeyup="LimitTextArea(this,100)" onkeydown="LimitTextArea(this,100)">${info.hbpHelpCheck}</textarea></td>
    </tr>
	<tr>
    <td height="30">服药依从性</td>
    <td colspan="3"><label><input type="radio" name="info.hbpDurgsObey" value="1" />规律　</label>
	&nbsp;
      <label><input type="radio" name="info.hbpDurgsObey" value="2" />间断　</label>
	  &nbsp;
      <label><input type="radio" name="info.hbpDurgsObey" value="3" />不服药</label></td>
    </tr>
  <tr>
    <td height="30">药物不良反应</td>
    <td colspan="3">
	<label><input type="radio" name="hbpDrugsUntoward" value="2" />无</label>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <label><input type="radio" name="hbpDrugsUntoward" value="1"/>有</label> &nbsp;<input class="easyui-validatebox input150" type="text" name="info.hbpDrugsUntoward" value="${info.hbpDrugsUntoward}"/>
      </td>
    </tr>
  <tr>
    <td height="30">此次随访分类</td>
    <td colspan="3">
	
	<label><input type="radio" name="info.flupRsult" value="1" />控制满意</label>
	&nbsp;
      <label><input type="radio" name="info.flupRsult" value="2" />控制不满意</label>
	  &nbsp;
      <label><input type="radio" name="info.flupRsult" value="3" />不良反应</label>
	  &nbsp;
      <label><input type="radio" name="info.flupRsult" value="4" />并发症</label></td>
  </tr>
  <tr><td colspan="4" align="left" class="table_title">用药情况</td></tr>
  <tr>
    <td height="30">药物</td>
    <td colspan="2">
      <table id="drugpt" class="mainform">
      	<%-- <%if(opt==1){%> --%>
        <tr class="mainform">
        	<td ><input type="checkbox" name="ckbDrug" /></td>
			<td ><label>名称</label></td>
			<td  nowrap="nowrap"><textarea class="easyui-validatebox" style="width:140px" name="drugName"  onkeyup="LimitTextArea(this,100)" onkeydown="LimitTextArea(this,100)"></textarea></td>
			<td  nowrap="nowrap">每日<input class="easyui-numberbox input50" style="width:40px" type="text" name="drugNamet" value="" data-options="min:0,max:500"/>次</td>
			<td  nowrap="nowrap">每次<input class="easyui-numberbox input50" style="width:40px" type="text" name="drugNameg" value="" data-options="min:0,max:500"/>mg </td>
        </tr>
       <%--  <%}else{%>
        	&nbsp;
        <%}%> --%>
      </table>
     </td>
	 <td nowrap="nowrap">
	 	<a name="maddbtn" href="javascript:addDataInput1()" class="easyui-linkbutton">添加</a>&nbsp;&nbsp;
	 	<a name="maddbtn1" href="javascript:deleteDataInput1()" class="easyui-linkbutton">删除</a>
	 </td>	
  </tr>
  
 <tr><td colspan="4" align="left" class="table_title">转诊情况</td></tr>
  <tr>
    <td height="30">原因</td>
    <td colspan="3"><textarea name="info.hbpReferWhy" class="easyui-validatebox" style="height:60px;width:575px" onkeyup="LimitTextArea(this,100)" onkeydown="LimitTextArea(this,100)">${info.hbpReferWhy}</textarea></td>
    </tr>
  <tr>
    <td height="30">机构</td>
    <td><input class="easyui-validatebox input150" type="text" name="info.hbpReferOrg" value="${info.hbpReferOrg}" maxlength="100"/></td>
    <td>科别</td>
    <td><input class="easyui-validatebox input150" type="text" name="info.hbpReferObj" value="${info.hbpReferObj}" maxlength="50"/></td>
  </tr>
  <tr>
    <td height="30">下次随访时间</td>
    <td colspan="3"><input id ="nextFlupTimeStr" name="info.nextFlupTimeStr" class="Wdate" value="${info.nextFlupTimeStr}" onClick="WdatePicker({minDate:'%y-%M-%d'})" style="height:25px;width:150px"/></td>
  </tr>
  <%-- <c:if test="${fn:contains(manInfo.roles, '21')}"> --%>
  <tr>
  	<td colspan="4" align="center">
  		<a href="javascript:doFormValid();" id="msubmitbtn" class="easyui-linkbutton">提交</a>
		<!-- <a href="#" id="mviewbtn" class="easyui-linkbutton"   onclick="viewOrAdd(true);">编辑</a> -->
	</td>
  </tr>
  <%-- </c:if> --%>
</table>
		<input type="hidden" id="inhbpSymptom" name="info.hbpSymptom" value=""/>
		<input type="hidden" id="params" name="params" value=""/>
		<input type="hidden" name="info.id" value="${info.id}"/>
		<input type="hidden" id="infodrugs" name="info.drugs" value=""/>
		<input type="hidden" id="infosports" name="info.sports" value=""/>
		<input type="hidden" name="lastparams" value="${lastparams}"/>
		
	    </form>
	    </div>
	</div>
	<script> 
	var druglen =0;
	var sportlen =0;
	$(function() {
		var params = '{"idcard":'+'"${person.idCard}",'+'"visitid":"'+'${visitid}",'+'"nowdate":"'+'${info.flupDateStr}"}';
		$("input[name=params]").val(params);

		$("input[name='info.flupType'][value="+'${info.flupType}'+"]").attr("checked",'checked');
		setValRadioOrCbox('info.flupType', '${info.flupType}');
		setValRadioOrCbox('info.hbpSalarium', '${info.hbpSalarium}');
		setValRadioOrCbox('info.flupRsult', '${info.flupRsult}');
		setValRadioOrCbox('info.hbpDurgsObey', '${info.hbpDurgsObey}');
		setValRadioOrCbox('info.hbpCompliance', '${info.hbpCompliance}');
		setValRadioOrCbox('info.hbpPsycrecovery', '${info.hbpPsycrecovery}');
		setValRadioOrCbox('info.hbpSalarium', '${info.hbpSalarium}');
		setValRadioOrCbox('hbpDrugsUntoward', '${info.hbpDrugsUntoward}'.length>0==""?2:1);

		var hbppsy = '${info.hbpSymptom}';
		var psyArr = hbppsy.split(";");
		
		for(var i in psyArr){
			if(psyArr[i])
			setValRadioOrCbox('zhengz', psyArr[i]);
		}
		
		var infodrugs = '${info.drugs}';
		if(infodrugs){
			var drugs = $.parseJSON(infodrugs);
			for(var i in drugs){
				addDataInput1(drugs[i]['hbpDrugsName'],drugs[i]['hbpDrugsFy'],drugs[i]['hbpDrugsCount']);
			}
		}

		var infosports = '${info.sports}';
		if(infosports){
			var sports = $.parseJSON(infosports);
			for(var i in sports){
				addSportInput(sports[i]['hbpSptFy'],sports[i]['hbpSptTime']);
			}
		}
		<%-- viewOrAdd(1==<%=opt%>); --%>
	});

	function addDataInput1(name,fy,count){
		if(!name)name="";
		if(!fy)fy="";
		if(!count)count="";
		var drugpt = $("#drugpt");
		druglen += 1;
		drugpt.append('<tr class="mainform"><td ><input type="checkbox" name="ckbDrug" /></td><td ><label>名称</label></td><td width="140" nowrap="nowrap"><textarea class="easyui-validatebox" style="width:140px" onkeyup="LimitTextArea(this,100)" onkeydown="LimitTextArea(this,100)" name="drugName'+druglen+'">'+name+'</textarea></td><td >每日<input class="easyui-numberbox input50" style="width:40px" type="text" name="drugNamet'+druglen+'" value="'+fy+'" data-options="min:0,max:5000"/>次</td><td >每次<input class="easyui-numberbox input50" style="width:40px" type="text" name="drugNameg'+druglen+'" value="'+count+'" data-options="min:0,max:5000"/>mg</td></tr>');  
	}
	
	function deleteDataInput1(){
		var checkboxArr = document.getElementsByName("ckbDrug");
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
		var drugTb = document.getElementById("drugpt");
		for(var i=checkboxArr.length-1;i>=0;i--){
			if(checkboxArr[i].checked){

					drugTb.deleteRow(i);

			}

		} 
	}

	function addSportInput(fy,time){
		if(!fy)fy="";
		if(!time)time="";
		var tesst = $("#sporthtml");
		sportlen+=1;
		tesst.append('<tr class="mainform"><td><input type="checkbox" name="ckbSport" ></td><td><label>每次</label><input class="easyui-numberbox input50" type="text" style="width:90px" name="sportm'+sportlen+'" value="'+fy+'" data-options="min:0,max:500"/>分钟</td><td><label>每周</label><input class="easyui-numberbox input50" type="text" style="width:90px" name="sportt'+sportlen+'" value="'+time+'" data-options="min:0,max:50"/>次</td><td></td></tr>');  
	}
	
	function deleteSportInput(){
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
		var sportTb = document.getElementById("sporthtml");
		for(var i=checkboxArr.length-1;i>=0;i--){
			if(checkboxArr[i].checked){

					sportTb.deleteRow(i);

			}

		}
                     
	}
	function doFormValid(){
		var isValid = $("#mform").form('validate');
		if (!isValid){
			return;	
		}
		var nextDateStr = $("#nextFlupTimeStr").val();
		if(nextDateStr == null || nextDateStr.length <5){
			var bps = $("input[name='info.hbpBps']").val();
			var bpd = $("input[name='info.hbpBpd']").val();
			if(analysisPre(bps, bpd)){
				$.messager.confirm('提示信息','血压值超出正常范围,是否进行下次随访?',function(r){
					if (r){
						if(nextDateStr.length<10){
								$.messager.alert("提示信息","下次随访时间没有选择","info");
								return;
							}else{
								initFormInfo();
							}
					}else{
						initFormInfo();
					}
				}); 
			}else{
				initFormInfo();
			}
		}else{
				initFormInfo();
			}
	}
	
	function initFormInfo(){
		
		var zhengz = '';
		$("input[name=zhengz]:checkbox").each(function(index) {
			if($(this).attr("checked"))
			zhengz+=$(this).val()+';';
		 });
//待完善输入为空时
		var drugStr=new Array();
		var spotStr=new Array();
		var innum = 0;
		for(var i=0;i<=druglen;i++){
			var num='';
			if(i>0)num=i;
			var drugArr = new Array();
			$("#drugpt").find("textarea[name='drugName"+num+"']").each(function(index) {
				drugArr[0] = $(this).val();
			 });
			$("#drugpt").find("input[name='drugNamet"+num+"']").each(function(index) {
				drugArr[1] = $(this).val();
			 });
			$("#drugpt").find("input[name='drugNameg"+num+"']").each(function(index) {
				drugArr[2] = $(this).val();
			 });

			 if(drugArr.length ==3){drugStr[innum]= '["'+drugArr.join("\",\"")+'"]';innum++;}
		}
		innum = 0;
		for(var i=0;i<=sportlen;i++){
			var num='';
			if(i>0)num=i;
			var sportArr = new Array();
			$("#sporthtml").find("input[name='sportt"+num+"']").each(function(index) {
				sportArr[0]=$(this).val();
			 });
			$("#sporthtml").find("input[name='sportm"+num+"']").each(function(index) {
				sportArr[1]=$(this).val();
			 });
			
			if(sportArr.length ==2){spotStr[innum]= '["'+sportArr.join("\",\"")+'"]';innum++;};
		}
		var dStr ='{"drug":['+drugStr.join(",")+']}';
		var sStr ='{"sport":['+spotStr.join(",")+']}';

		$("#infodrugs").val(dStr);
		$("#infosports").val(sStr);
		$("#inhbpSymptom").val(zhengz);
		majaxSubmit(1==<%=opt%>?'_add':'_update');
	}

	function majaxSubmit(atname){
		$("#mform").form("submit", {
			url : "<%=request.getContextPath()%>/chr/preInfo_doXYRecord.action",
			onSubmit : function() {
				return true;
			},
			success : function(data) {
				data =  $.parseJSON(data);
				if(data.state > 0){
					$.messager.alert("提示信息", "保存成功.", "info", function(){
						closeIt();
					});
					refreshPage("${param.pPageId}");
					refreshPage("home");
				}else{
					$.messager.alert("提示信息", data.msg, "error");
				}
			}
		});
	}

	function viewOrAdd(isadd){
			
		if(isadd){
			$("input").prop("readonly", false);
			$("textarea").prop("readonly", false);
			$("a[name=maddbtn]").each(function(index) {
				$(this).show();
			});
			$("a[name=maddbtn1]").each(function(index) {
				$(this).show();
			});
			$("#msubmitbtn").attr("style","");
			$("#mviewbtn").attr("style","display:none");
		}else{
			$("input").prop("readonly", true);
			$("textarea").prop("readonly", true);
			$("a[name=maddbtn]").each(function(index) {
				$(this).hide();
			});
			$("a[name=maddbtn1]").each(function(index) {
				$(this).hide();
			});
			$("#msubmitbtn").attr("style","display:none");
			$("#mviewbtn").attr("style","");
		}
	}
	
	function closeIt()
		{
			try {
					var tc = top.$("#tb");
					var tab = tc.tabs("getSelected");
					if (tab) {
						tc.tabs("close", tab.panel("options").title);
					}
				}catch (e) {}
		}
</script> 
</body>
</html>