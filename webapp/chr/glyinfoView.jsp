<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
	<head>
		<title>血糖随访记录表</title>
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
	.input50{ width:50px;}
	</style>
</head>
<body>
<div style="padding:5px;margin-left: 150px;">
	<div class="easyui-panel" title="2型糖尿病随访记录表" style="width:900px;">
	    <form id="mform" action="chrgly" method="post">
	    	<table width="100%" border="0"  cellpadding="0" cellspacing="0" class="data_table">
           		<tr>
           			<td  colspan="4" align="left" class="table_title">个人信息</td>
           		</tr>
  				<tr>
				    <td width="15%" height="35">姓名</td>
				    <td width="35%">${info.memberName}</td>
				    <td width="20%">编号</td>
				    <td>${info.visitNo}</td>
   				 </tr>
  				<tr>
				    <td>随访时间</td>
				    <td>${info.flupDate}</td>
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
				    <td  colspan="4">
						<table width="100%" class="mainform">
				       		<tr>
								<td width="120"><input type="checkbox"  name="zhengz" value="1" />无症状</td>
								<td width="120"><input type="checkbox"  name="zhengz" value="2" />多饮</td>
								<td width="120"><label><input type="checkbox"  name="zhengz" value="3" />多食</label></td>
								<td width="120"><input type="checkbox"  name="zhengz" value="4" />多尿</td>		
								<td><label><input type="checkbox"  name="zhengz" value="5" />视力模糊</label></td>
							</tr>
							<tr>
								<td><input type="checkbox"  name="zhengz" value="6" />感染</td>
								<td><input type="checkbox"  name="zhengz" value="7" />手脚麻木</td>
								<td><input type="checkbox"  name="zhengz" value="8" />下肢浮肿</td>
								<td nowrap="nowrap"><label><input type="checkbox"  name="zhengz" value="9"/>体重明显下降</label></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
    				<td height="61">其他</td>
					<td colspan="3"><textarea name="info.glySymptomOther" class="easyui-validatebox" style="height:60px;width:560px" onkeyup="LimitTextArea(this,100)" onkeydown="LimitTextArea(this,100)">${info.glySymptomOther}</textarea></td>
    			</tr>
				<tr>
					<td colspan="4" align="left" class="table_title">体征</td>
				</tr>
				<tr>
				    <td height="30">体重(KG)</td>
				    <td><input class="easyui-numberbox input150" type="text" name="info.hbpWeight" value="${info.hbpWeight}" data-options="min:0,max:5000,precision:1"/></td>
				    <td>足背动脉搏动</td>
				    <td colspan="1">
				    	<label>
				      		<input type="radio" style="margin-bottom:6px" name="info.hbpFootBack" value="1" />未触及
				    	</label>
				     	 <label>
				      		<input type="radio" style="margin-bottom:6px" name="info.hbpFootBack" value="2" /> 触及
				      	</label>
				     </td>
    			</tr>
  				<tr>
    				<td height="30">收缩压(mmHg)</td>
    				<td><input class="easyui-numberbox input150" type="text" name="info.hbpBps" value="${info.hbpBps}" maxlength="3"/></td>
    				<td>舒张压(mmHg)</td>
    				<td colspan="1"><input class="easyui-numberbox input150" type="text" name="info.hbpBpd" value="${info.hbpBpd}" maxlength="3"/></td>
  				</tr>
  				<tr>
    				<td height="30" >体质指数</td>
   					<td colspan="3"><input class="easyui-numberbox input150" type="text" name="info.hbpPhysique" value="${info.hbpPhysique}" data-options="min:0,max:100,precision:1"/></td>
   
  				</tr>
 				 <tr>
				    <td height="30">其它</td>
				    <td colspan="3"><textarea name="info.hbpOther" class="easyui-validatebox" style="height:60px;width:560px" onkeyup="LimitTextArea(this,100)" onkeydown="LimitTextArea(this,100)">${info.hbpOther}</textarea></td>
  				</tr>
				<tr>
					<td colspan="4" align="left" class="table_title">生活方式指导</td>
				</tr>
				<tr>
				    <td height="30">日吸烟量(支)</td>
				    <td><input class="easyui-numberbox input150" type="text" name="info.hbpSmoking" value="${info.hbpSmoking}" data-options="min:0,max:5000"/></td>
				    <td>日饮酒量(两)</td>
				    <td colspan="1"><input class="easyui-numberbox input150" type="text" name="info.hbpDrinking" value="${info.hbpDrinking}" data-options="min:0,max:5000"/></td>
  				</tr>
 				<tr>
				    <td height="30">主食(克/天)</td>
				    <td><input class="easyui-numberbox input150" type="text" name="info.hbpFood" value="${info.hbpFood}"/></td>
				    <td>心理调整</td>
				    <td colspan="1">
					<label><input type="radio" style="margin-bottom:6px" name="info.hbpPsycrecovery" value="1" />良好 </label>
					&nbsp;
				      <label><input type="radio" style="margin-bottom:6px" name="info.hbpPsycrecovery" value="2" />一般</label>
					  &nbsp;
				      <label><input type="radio" style="margin-bottom:6px" name="info.hbpPsycrecovery" value="3" />差</label></td>
    			</tr>
				<tr>
				    <td height="30">运动</td>
				    <td colspan="2" width="400px"><table id="sporthtml" class="mainform">
					<%if(opt==1){%>
				      <tr>
				      	<td><input type="checkbox" name="ckbSport" /></td>
				        <td><label>每次</label><input class="easyui-numberbox input30" type="text" name="sportm" value="" style="width:100px" data-options="min:0,max:500"/>分钟</td>
				        <td><label>每周</label><input class="easyui-numberbox input30" type="text" name="sportt" value="" style="width:100px" data-options="min:0,max:50"/>次</td>
				      </tr>
				      <%}%>
				    </table></td>
					 <td colspan="1" >
					  <table class="mainform">
					  	<tr>
							<td ><a name="maddbtn" href="javascript:addSportInput()" class="easyui-linkbutton">添加</a></td>
							<td ><a name="maddbtn1" href="javascript:deleteSportInput()" class="easyui-linkbutton">删除</a></td>
						</tr>
						</table>
					  </td>
				</tr>
 				 <tr>
				    <td height="30">遵医行为</td>
				    <td colspan="3"><label><input type="radio" style="margin-bottom:6px" name="info.hbpCompliance" value="1" />良好 </label>
					&nbsp;
				      <label><input type="radio" style="margin-bottom:6px" name="info.hbpCompliance" value="2" />一般</label>
					  &nbsp;
				      <label><input type="radio" style="margin-bottom:6px" name="info.hbpCompliance" value="3" />差</label></td>
  				</tr>
  				<tr>
  					<td colspan="4" align="left" class="table_title">辅助信息</td>
  				</tr>
  				<tr>
				    <td height="30">空腹血糖值<br/>(mmol/L)</td>
				    <td colspan="3"><input class="easyui-numberbox input150" type="text" data-options="required:true,min:0,max:50,precision:2" name="info.hbpFastGlu" value="${info.hbpFastGlu}"/></td>
    			</tr>
				<tr><td>糖化血红蛋白(%)</td>
			        <td><input class="easyui-numberbox input150" id="zcxnamenum" type="text" name="zcxname" data-options="min:0,max:1000,precision:1"/></td>
			        <td>检查日期 </td>
			        <td width="100" colspan="1"><input name="zcxdate"  value="${info.flupDateStr}" readonly="readonly" style="height:25px;width:150px"/></td>
				</tr>
				<tr>
					<td colspan="3">
						<table id="tijianbg" class="mainform">
						<%if(opt == 1){%>	
	  						<tr>
	  							<td><input type="checkbox" name="ckbTiJianBg" /></td>
	  							<td height="30" >检查项</td>
								<td><input class="easyui-validatebox input150" style="width:80px" type="text" name="zcxname0" value=""/></td>
								<td>检查日期 </td>
								<td><input name="zcxdate0" value="" class="Wdate" value="" onClick="WdatePicker({maxDate:'${info.flupDateStr}'})" style="height:25px;width:100px"/></td>
								<td height="30">检查结果</td>
								<td colspan="3"><textarea name="zcxresult0" value="" class="easyui-validatebox" style="height:30px;width:200px;" onkeyup="LimitTextArea(this,100)" onkeydown="LimitTextArea(this,100)"></textarea></td>
	 						</tr>
						<%}%>
  						</table>
					</td>
					<td colspan="1" >
	  					<table class="mainform">
	  						<tr>
								<td><a name="maddbtn" href="javascript:addDataInput2()" class="easyui-linkbutton">添加</a></td>
								<td><a name="maddbtn1" href="javascript:deleteDataInput2()" class="easyui-linkbutton">删除</a></td>
							</tr>
						</table>
	  				</td>
				</tr>
  				<tr>
				    <td height="30">服药依从性</td>
				    <td colspan="3"><label><input type="radio" style="margin-bottom:6px" name="info.hbpDurgsObey" value="1" />规律　</label>
					&nbsp;
				      <label><input type="radio" style="margin-bottom:6px" name="info.hbpDurgsObey" value="2" />间断　</label>
					  &nbsp;
				      <label><input type="radio" style="margin-bottom:6px" name="info.hbpDurgsObey" value="3" />不服药</label></td>
				</tr>
				<tr>
					<td height="30">药物不良反应</td>
				    <td colspan="3">
					<label><input type="radio" style="margin-bottom:6px" name="hbpDrugsUntoward" value="2" />无</label>
					&nbsp;
				      <label><input type="radio" style="margin-bottom:6px" name="hbpDrugsUntoward" value="1"/>有</label> &nbsp;<input class="easyui-validatebox input150" type="text" name="info.hbpDrugsUntoward" value="${info.hbpDrugsUntoward}"/></td>
				</tr>
				<tr>
				    <td height="30">此次随访分类</td>
				    <td colspan="3">
					<label><input type="radio" style="margin-bottom:6px" name="info.flupRsult" value="1" />控制满意</label>
					&nbsp;
				      <label><input type="radio" style="margin-bottom:6px" name="info.flupRsult" value="2" />控制不满意</label>
					  &nbsp;
				      <label><input type="radio" style="margin-bottom:6px" name="info.flupRsult" value="3" />不良反应</label>
					  &nbsp;
				      <label><input type="radio" style="margin-bottom:6px" name="info.flupRsult" value="4" />并发症</label></td>
				</tr>
  				<tr><td colspan="4" align="left" class="table_title">用药情况</td></tr>
				  <tr>
				    <td height="30">胰岛素</td>
				    <td colspan="2">
					<table class="mainform">
				        <tr  class="mainform">
				        	<td >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
							<td ><label>种类</label>
								<input class="easyui-validatebox input150" style="width:140px" type="text" name="drugName" value=""/></td>
							<td nowrap="nowrap">每日<input class="easyui-numberbox input50" style="width:40px" type="text" id="drugNametnum" name="drugNamet" value="" data-options="min:0,max:5000"/>次</td>
							<td nowrap="nowrap">每次<input class="easyui-numberbox input50" style="width:40px" type="text" id="drugNamegnum" name="drugNameg" value="" data-options="min:0,max:5000"/>mg</td>
				        </tr>
				      </table>	</td>
				  </tr>
				  <tr>
				    <td height="30">药物</td>
				    <td colspan="2">
				      <table id="drugpt" class="mainform">
					  <%if(opt == 1){%>
				        <tr class="mainform">
				        	<td ><input type="checkbox" name="ckbDrug" /></td>
							<td ><label>名称</label></td>
							<td  nowrap="nowrap"><textarea class="easyui-validatebox" style="width:140px" name="drugName"  onkeyup="LimitTextArea(this,100)" onkeydown="LimitTextArea(this,100)"></textarea></td>
							<td  nowrap="nowrap">每日<input class="easyui-numberbox input50"  style="width:40px" type="text" name="drugNamet" value="" data-options="min:0,max:500"/>次</td>
							<td  nowrap="nowrap">每次<input class="easyui-numberbox input50" style="width:40px" type="text" name="drugNameg" value="" data-options="min:0,max:500"/>mg </td>
				        </tr>
						<%}%>
				      </table>	  
				     </td>
					 <td colspan="1" >
					 	<table class="mainform">
					  		<tr class="mainform">
								<td><a name="maddbtn" href="javascript:addDataInput1()" class="easyui-linkbutton">添加</a></td>
								<td><a name="maddbtn1" href="javascript:deleteDataInput1()" class="easyui-linkbutton">删除</a></td>
							</tr>
						</table>
					  </td>
				  </tr>
				  
				 <tr><td colspan="4" align="left" class="table_title">转诊情况</td></tr>
				  <tr>
				    <td height="30">原因</td>
				    <td colspan="3"><textarea name="info.hbpReferWhy" class="easyui-validatebox" style="height:60px;width:560px" onkeyup="LimitTextArea(this,100)" onkeydown="LimitTextArea(this,100)">${info.hbpReferWhy}</textarea></td>
				    </tr>
				  <tr>
				    <td height="30">机构</td>
				    <td><input class="easyui-validatebox input150" type="text" name="info.hbpReferOrg" value="${info.hbpReferOrg}" maxlength="100"/></td>
				    <td>科别</td>
				    <td colspan="1"><input class="easyui-validatebox input150" type="text" name="info.hbpReferObj" value="${info.hbpReferObj}" maxlength=50"/></td>
				    </tr>
				  <tr>
				    <td height="30">下次随访时间</td>
				    <td colspan="3"><fmt:formatDate value="${info.nextFlupTime}" pattern="yyyy-MM-dd"/>
				    	<%-- <input name="nextFlupTimeStr"   value="${info.nextFlupTime}"  readonly="readonly" style="height:25px;width:150px"/> --%>
				    </td>
				    </tr>
				    	<c:if test="${fn:contains(manInfo.roles, '21')}">
				    <tr>
				  	<td colspan="10" align="center"><a href="javascript:doFormValid();" id="msubmitbtn" class="easyui-linkbutton">提交</a>
						<a href="#" id="mviewbtn" class="easyui-linkbutton" onclick="viewOrAdd(true);">编辑</a>
					</td>
				  </tr>
  						</c:if>
		</table>
		<input type="hidden" name="params" value="'${person}'"/>
		<input type="hidden" name="info.id" value="${info.id}"/>
		<input type="hidden" id="infodrugs" name="info.drugs" value=""/>
		<input type="hidden" id="infochecks" name="info.checks" value=""/>
		<input type="hidden" id="infosports" name="info.sports" value=""/>
		<input type="hidden" id="inglySymptom" name="info.glySymptom" value=""/>
		
	    </form>
	    </div>
	</div>
	<script> 

		var druglen =0;
		var zcxlen =0;
		var sportlen = 0;
		$(function() {
			var params = '{"idcard":'+'"${person.idCard}",'+'"visitid":"'+'${visitid}",'+'"nowdate":"'+'${info.flupDateStr}"}';
			$("input[name=params]").val(params);
			setValRadioOrCbox('info.flupType', '${info.flupType}');
			setValRadioOrCbox('info.hbpPsycrecovery', '${info.hbpPsycrecovery}');
			setValRadioOrCbox('info.flupRsult', '${info.flupRsult}');
			setValRadioOrCbox('info.hbpDurgsObey', '${info.hbpDurgsObey}');
			setValRadioOrCbox('info.hbpCompliance', '${info.hbpCompliance}');
			setValRadioOrCbox('info.hbpFootBack', '${info.hbpFootBack}');
			setValRadioOrCbox('hbpDrugsUntoward', '${info.hbpDrugsUntoward}'.length>0?'1':'2');
			
			var hbppsy = '${info.glySymptom}';
			var psyArr = hbppsy.split(";");
			for(var i in psyArr){
				if(psyArr[i])
				setValRadioOrCbox('zhengz', psyArr[i]);
			}

			var infodrugs = '${info.drugs}';
			if(infodrugs){
				var drugs = $.parseJSON(infodrugs);
				for(var i in drugs){
					if(drugs[i]['type'] == 2){
						$("input[name=drugName]").val(drugs[i]['hbpDrugsName']);
						$("#drugNametnum").numberbox('setValue', drugs[i]['hbpDrugsFy']);
						$("#drugNamegnum").numberbox('setValue', drugs[i]['hbpDrugsCount']);
					}else{
						addDataInput1(drugs[i]['hbpDrugsName'],drugs[i]['hbpDrugsFy'],drugs[i]['hbpDrugsCount']);
					}
				}
			}

			var infochecks = '${info.checks}';
			if(infochecks){
				var checks = $.parseJSON(infochecks);
				for(var i in checks){
					if(checks[i]['hbpCheckType'] == 2){
						$('#zcxnamenum').numberbox('setValue', checks[i]['hbpSugerBlood']);
						$("input[name=zcxdate]").val(toDate(checks[i]['hbpCheckTime']));
						$("input[name=zcxresult]").val(checks[i]['hbpCheckRemark']);
					}else{
						addDataInput2(checks[i]['hbpCheckName'],toDate(checks[i]['hbpCheckTime']),checks[i]['hbpCheckRemark']);
					}
				}
			}
			var infosports = '${info.sports}';
			if(infosports){
				var sports = $.parseJSON(infosports);
				for(var i in sports){
						addSportInput(sports[i]['hbpSptFy'],sports[i]['hbpSptTime']);
					}
				}
			var a = document.getElementsByTagName("input");
		   for (var i=0; i<a.length; i++)
		   {
		      if (a[i].type=="checkbox" || a[i].type=="radio") {
		      	/* if(a[i].attr("checked",'checked')){ a[i].disabled=true; } */
		      	a[i].disabled=true;
		      }
		      
		      
		   }
		   var b = document.getElementsByTagName("select");
		   for (var i=0; i<b.length; i++)
		   {
		      b[i].disabled=true;
		   }
			viewOrAdd(1==<%=opt%>);
		});

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

		function addDataInput1(name,fy,count){
			if(!name)name="";
			if(!fy)fy="";
			if(!count)count="";

			var drugpt = $("#drugpt");

			druglen += 1;
			drugpt.append('<tr class="mainform"><td ><input type="checkbox" name="ckbDrug" /></td><td><label>名称</label></td><td  nowrap="nowrap"><textarea class="easyui-validatebox" style="width:140px" onkeyup="LimitTextArea(this,100)" onkeydown="LimitTextArea(this,100)" name="drugName'+druglen+'">'+name+'</textarea></td><td >每日<input class="easyui-numberbox input50" style="width:40px" type="text" name="drugNamet'+druglen+'" value="'+fy+'" data-options="min:0,max:5000"/>次</td><td >每次<input class="easyui-numberbox input50" style="width:40px" type="text" name="drugNameg'+druglen+'" value="'+count+'" data-options="min:0,max:5000"/>mg</td></tr>');  
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
		function addDataInput2(name,date,remark){
			if(!name)name="";
			if(!date)date="";
			if(!remark)remark="";
			var tesst = $("#tijianbg");
			zcxlen+=1;
			tesst.append('<tr><td><input type="checkbox" name="ckbTiJianBg" ></td><td height="30">检查项</td><td><input class="easyui-validatebox input150" style="width:80px;" type="text" name="zcxname'+zcxlen+'" value="'+name+'"/></td><td>检查日期 </td><td><input name="zcxdate'+zcxlen+'" class="Wdate" value="'+date+'" onClick="WdatePicker()" style="height:25px;width:100px"/></td><td height="30">检查结果</td><td colspan="3"><textarea name="zcxresult'+zcxlen+'" class="easyui-validatebox" style="height:30px;width:200px" onkeyup="LimitTextArea(this,100)" onkeydown="LimitTextArea(this,100)">'+remark+'</textarea></td></tr>');  
		}
		
		function deleteDataInput2(){
			var checkboxArr = document.getElementsByName("ckbTiJianBg");
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
			var tiJianTb = document.getElementById("tijianbg");
			for(var i=checkboxArr.length-1;i>=0;i--){
				if(checkboxArr[i].checked){
					
						tiJianTb.deleteRow(i);
						
					
				}
			}        
		}
		function doFormValid(){
			
			var isValid = $("#mform").form('validate');
			if (!isValid){
				return;
			}
			var nextDateStr = $("input[name=nextFlupTimeStr]").val();
			if(nextDateStr == null || nextDateStr.length <5){
				var hbpFastGlu = $("input[name='info.hbpFastGlu']").val();
				if(analysisGly(hbpFastGlu)){
					$.messager.confirm('提示信息','血糖值超出正常范围,是否进行下次随访?',function(r){
						if (r){
							initFormInfo();
						}else{
							return;
						}
					}); 
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
			var drugStr=new Array();
			var innum = 0;
			for(var i=-1;i<=druglen;i++){
				var num='';
				if(i>-1)num=i;
				var drugArr = ["","",""];
				$("#mform").find("textarea[name='drugName"+num+"']").each(function(index) {
					drugArr[0] = $(this).val();
				 });
				$("#mform").find("input[name='drugNamet"+num+"']").each(function(index) {
					drugArr[1] = $(this).val();
				 });
				$("#mform").find("input[name='drugNameg"+num+"']").each(function(index) {
					drugArr[2] = $(this).val();
				 });

				 if(drugArr.length ==3){drugStr[innum]= '["'+drugArr.join("\",\"")+'"]'; innum++;};
			}

			var zcxStr=new Array();
			innum = 0;
			for(var i=-1;i<=zcxlen;i++){
				var num='';
				if(i>-1)num=i;
				var zcxArr = ["","",""];
				$("#mform").find("input[name='zcxname"+num+"']").each(function(index) {
					zcxArr[0] = $(this).val();
				 });
				$("#mform").find("input[name='zcxdate"+num+"']").each(function(index) {
					zcxArr[1] = $(this).val();
				 });
				$("#mform").find("input[name='zcxresult"+num+"']").each(function(index) {
					zcxArr[2] = $(this).val();
				 });
				 if(zcxArr.length ==3){zcxStr[innum]= '["'+zcxArr.join("\",\"")+'"]';innum++;};
			}

			var spotStr=new Array();
			innum = 0;
			for(var i=0;i<=sportlen;i++){
				var num='';
				if(i>0)num=i;
				var sportArr = new Array();;
				$("#sporthtml").find("input[name='sportm"+num+"']").each(function(index) {
					sportArr[0]=$(this).val();
				 });
				$("#sporthtml").find("input[name='sportt"+num+"']").each(function(index) {
					sportArr[1]=$(this).val();
				 });

				if(sportArr.length ==2){spotStr[innum]= '["'+sportArr.join("\",\"")+'"]'; innum++;};
			}

			var sStr ='{"sport":['+spotStr.join(",")+']}';
			var dStr ='{"drug":['+drugStr.join(",")+']}';
			var checkStr ='{"check":['+zcxStr.join(",")+']}';

			$("#infochecks").val(checkStr);
			$("#infodrugs").val(dStr);
			$("#infosports").val(sStr);
			$("#inglySymptom").val(zhengz);
			majaxSubmit(1==<%=opt%>?'_add':'_update');
		}

		function majaxSubmit(atname){
			var formObj = $("#mform");  

			var action = formObj.attr('action')+atname+'.do';
			
			doInfoFormSubmit(action);
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
		
	</script>
</body>
</html>