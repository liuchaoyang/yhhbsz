<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="java.util.List"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.module.sys.bean.User"%>
<%@ page import="com.yzxt.yh.module.his.bean.Dept"%>
<%@ page import="com.yzxt.yh.module.his.bean.BaseArea"%>
<% 
	@SuppressWarnings("unchecked")
	List<Dept> depts = (List<Dept>)request.getAttribute("depts");
	List<BaseArea> areas = (List<BaseArea>)request.getAttribute("areas");
%>
<%
	User user = (User)request.getAttribute("user");
	if(user==null){
		user = new User();
	}
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
		subWin = $('<div><iframe src="<%=request.getContextPath()%>/chk/customersSel.jsp" style="width: 98%;height:99%;" border="0" frameborder="0" scoll="no"></iframe></div>').window({
			title: "选择用户",
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
	
	function validForm(){
		<%--姓名--%>
		var userName = document.getElementById("custName");
		var userNameVal = $.trim(userName.value);
		userName.value = userNameVal;
		if(!userNameVal){
			$.messager.alert("提示信息", "请输入姓名。", "error", function(){
				/* userName.focus(); */
			});
			return false;
		}
		<%--地区：省--%>
		var proName = document.getElementById("deptId");
		var proNameVal = $.trim(proName.value);
		proName.value = proNameVal;
		if(!proNameVal){
			$.messager.alert("提示信息", "请选择省。", "error", function(){
				/* userName.focus(); */
			});
			return false;
		}
		<%--地区：市--%>
		var cityName = document.getElementById("deptId");
		var cityNameVal = $.trim(cityName.value);
		cityName.value = cityNameVal;
		if(!cityNameVal){
			$.messager.alert("提示信息", "请选择市。", "error", function(){
				/* userName.focus(); */
			});
			return false;
		}
		<%--医院--%>
		var deptName = document.getElementById("deptId");
		var deptNameVal = $.trim(deptName.value);
		deptName.value = deptNameVal;
		if(!deptNameVal){
			$.messager.alert("提示信息", "请输入姓名。", "error", function(){
				/* userName.focus(); */
			});
			return false;
		}
		<%--医院--%>
		var deptName = document.getElementById("deptId");
		var deptNameVal = $.trim(deptName.value);
		deptName.value = deptNameVal;
		if(!deptNameVal){
			$.messager.alert("提示信息", "请选择医院。", "error", function(){
				/* userName.focus(); */
			});
			return false;
		}
		<%--医院--%>
		/* var departName = document.getElementById("secondDepartId"); */
		//var departNameVal = $.trim(departName.value);
		//departName.value = departNameVal;
		
		// 验证通过
		return true;
	}
	
	 function save(){
		if(!validForm()){
			return;
		}
		$("#filterForm").form("submit", {
			url: "<%=request.getContextPath()%>/his/appoint_addAppoint.action",
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
	
	function setCataList(parentId){
		$.ajax({
			type : "GET",
			dataType : "json",
			url : "<%=request.getContextPath()%>/his/appoint_listConfigByJson.action?parentId=" + parentId,
			async : false,
			timeout : 30000,
			success : function(data){
				var sel = document.getElementById("departId");
				sel.options.length = 0;
				sel.add(new Option("请选择", ""));
				for(var i = 0;i < data.length; i++){
					sel.add(new Option(data[i].configName, data[i].id));
				}
			},
			error : function(){
		 	}
		});
	}
	function setProList(pId){
		$.ajax({
			type : "GET",
			dataType : "json",
			url : "<%=request.getContextPath()%>/his/appoint_listArea.action?pId=" + pId,
			async : false,
			timeout : 30000,
			success : function(data){
				var sel = document.getElementById("cityId");
				sel.options.length = 0;
				sel.add(new Option("请选择", ""));
				for(var i = 0;i < data.length; i++){
					sel.add(new Option(data[i].cityName, data[i].codeId));
				}
			},
			error : function(){
		 	}
		});
	}
	function setCityList(pId){
		$.ajax({
			type : "GET",
			dataType : "json",
			url : "<%=request.getContextPath()%>/his/appoint_listArea.action?pId=" + pId,
			async : false,
			timeout : 30000,
			success : function(data){
				var sel = document.getElementById("dicId");
				sel.options.length = 0;
				sel.add(new Option("请选择", ""));
				for(var i = 0;i < data.length; i++){
					sel.add(new Option(data[i].cityName, data[i].codeId));
				}
			},
			error : function(){
		 	}
		});
	}
	function setDicList(parentId){
		$.ajax({
			type : "GET",
			dataType : "json",
			url : "<%=request.getContextPath()%>/his/appoint_listConfigByJson.action?parentId=" +parentId,
			async : false,
			timeout : 30000,
			success : function(data){
				var sel = document.getElementById("deptId");
				sel.options.length = 0;
				sel.add(new Option("请选择", ""));
				for(var i = 0;i < data.length; i++){
					sel.add(new Option(data[i].configName, data[i].id));
				}
			},
			error : function(){
		 	}
		});
	}
	function setDocList(deptId){
		$.ajax({
			type : "GET",
			dataType : "json",
			url : "<%=request.getContextPath()%>/sys/doctor_deptDocList.action?deptId=" +deptId,
			async : false,
			timeout : 30000,
			success : function(data){
				var sel = document.getElementById("docId");
				sel.options.length = 0;
				sel.add(new Option("请选择", ""));
				for(var i = 0;i < data.length; i++){
					sel.add(new Option(data[i].doctorName, data[i].userId));
				}
			},
			error : function(){
		 	}
		});
	}
</script>
</head>
<body style="margin: 1px;padding: 0px;">
	<div style="width:100%;">
		<form id="filterForm" method="post" accept-charset="UTF-8">
			<table class="table">
				<tr>
					<td width="20%" class="td_title">用户名称:</td>
					<td width="70%">
						<input type="text" id="custName" name="appoint.custName" maxlength="20" value="<%if(user.getName()!=null){ %><%=user.getName()%><%} %>" disabled="disabled" data-options="" style="width: 235px;" />
						<%if(user.getName()==null){ %>
						<img id="selUserImg" alt="选择用户" src="<%=request.getContextPath()%>/resources/img/search.png" onclick="openSelResi()" style="cursor: pointer;margin-bottom: -5px;" />
						<%} %>
						<input type="hidden" id="custId" name="appoint.custId" value="<%if(user.getId()!=null){ %><%=user.getId()%><%} %>" />
					</td>
				</tr>
				<tr>
				<td width="20%" class="td_title">地区:</td>
				<td id = "pro">
					<select id="proId" name="" class="sel" onchange="setProList(this.value);"style="width: 200px;">
								<option value="" <%=StringUtil.isEmpty("") ? "selected=\"selected\"" : ""%>>请选择省、直辖市、自治区</option>
								<%
								for(BaseArea area : areas)
								{
								%>
								<option value="<%=area.getCodeId()%>"><%=area.getCityName()%></option>
								<%
								}
							%> 
						</select>
				</td>
				<td id = "city">
					<select id="cityId" name="" class="sel" onchange="setCityList(this.value);">
						<option value="">请选择市</option>
					</select>
				</td>
				<td id = "city">
					<select id="dicId" name="" class="sel"onchange="setDicList(this.value);">
						<option value="">请选择县</option>
					</select>
				</td>
				</tr>
				<tr>
					<td width="20%" class="td_title">医院名称:</td>
					<td class="tdVal">
							<select id="deptId" name="appoint.deptId" class="sel" onchange="setCataList(this.value);"style="height: 24px;width: 200px;">
								<option value="">请选择医院</option>
								<%-- <option value="" <%=StringUtil.isEmpty("") ? "selected=\"selected\"" : ""%>>请选择</option>
								<%
								for(Dept dept : depts)
								{
								%>
								<option value="<%=dept.getAreaid()%>" <%=deviceInfo.getCode().equals(userDevice.getDeviceType())><%=dept.getName()%></option>
								<%
								}
							%>  --%>
							</select>
						<span class="must">*</span>
					</td>
				</tr>
				<tr>
					<td width="20%" class="td_title">科室名称:</td>
					<td class="tdVal">
							<select id="departId" name="appoint.departId" class="sel" onchange="setDocList(this.value);" style = "height: 24px;width: 150px;">
								<option value="">请选择</option>
						</select>
						<span class="must">*</span>
					</td>
				</tr>
				<tr>
					<td width="20%" class="td_title">医生:</td>
					<td class="tdVal">
							<select id="docId" name="appoint.doctorId" class="sel" style="height: 24px;width: 150px;">
								<option value="">请选择</option>
						</select>
						<span class="must">*</span>
					</td>
				</tr>
				<!-- <tr>
					<td width="20%" class="td_title">二级科室名称:</td>
					<td class="tdVal">
						<select id="secondDepartId" name="appoint.departId" class="sel" style="height: 24px;width: 150px;">
							<option value="">请选择</option>
						</select>
						<span class="must">*</span>
					</td>
				</tr> -->
				<tr>
					<td width="20%" class="td_title">自述:</td>
					<td>
						<textarea id="selfSymptom" name="appoint.selfSymptom" style="padding: 5px;width: 95%;height: 40px;"></textarea>
					</td>
				</tr>
				<tr>
					<td width="20%" class="td_title">初诊结果:</td>
					<td style="padding: 10px 3px 10px 3px;">
						<textarea id="firstVisit" name="appoint.firstVisit" style="padding: 5px;width: 95%;height: 80px;"></textarea>
					</td>
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