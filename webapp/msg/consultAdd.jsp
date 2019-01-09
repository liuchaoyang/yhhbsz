<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.module.svb.bean.MemberInfo"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.util.DateUtil"%>
<% 
	MemberInfo memberInfo = (MemberInfo) request.getAttribute("memberInfo");
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>添加咨询</title>
	<style type="text/css">
	</style>
	<script type="text/javascript">
	function query(){
	}
	function clear(){
		document.getElementById("consultForm").reset();
	}
	function closeIt() {
		try{
			parent.subWin1.window("close");
		}catch(e){}
	}
	
	function selDoc(){
		try{
			parent.selDocWin("<%=request.getContextPath()%>/svb/memberInfo_toDocSel.action?custId=<%=memberInfo.getCustId()%>", window);
		}catch(e){}
	}
	
	var subWin;
	function openSelResi(){
		<%--先选择一个居民，然后跳转到新增页面
			parent.
		--%>
		subWin = $('<div><iframe src="<%=request.getContextPath()%>/msg/menbersSel.jsp" style="width: 99%;height:99%;" border="0" frameborder="0" scoll="no"></iframe></div>').window({
			title: "选择居民",
			width: 600,
			height: 400,
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
		$("#userId").val(cust.userId);
		$("#memberName").val(cust.name);
	}
	
	function save(){
		var consultTitle = document.getElementById("consultTitle");
		var consultTitleVal = $.trim(consultTitle.value);
		consultTitle.value = consultTitleVal;
		if(!consultTitleVal){
			$.messager.alert("提示信息", "请输入咨询标题。", "error", function(){
				consultTitle.focus();
			});
			return;
		}
		if(consultTitle.value.length > 100){
			$.messager.alert("提示信息", "字数不能超过100个！", "error", function(){
				consultContext.focus();
			});
			return;
		}
		var consultContext = document.getElementById("consultContext");
		var consultContextVal = $.trim(consultContext.value);
		consultContext.value = consultContextVal;
		if(!consultContextVal){
			$.messager.alert("提示信息", "请输入咨询内容。", "error", function(){
				consultContext.focus();
			});
			return;
		}
		//备注
		if(consultContext.value.length > 200){
			$.messager.alert("提示信息", "字数不能超过200个！", "error", function(){
				consultContext.focus();
			});
			return;
		}
		$("#consultForm").form("submit", {
			url: "<%=request.getContextPath()%>/msg/consultGuide_add.action",
			dataType : "json",
			success : function(data) {
				data =  $.parseJSON(data); 
				if(data.state == 1){
					$.messager.alert("提示信息", data.msg?data.msg:"新增成功。", "info", function(){
						try{
							parent.refreshGrid();
						}catch(e){}
						closeIt();
					});
				}else{
					$.messager.alert("提示信息", data.msg?data.msg:"新增失败。", "error");
				}
			}
		});
	}
</script>
</head>
<body style="margin: 1px;padding: 0px;">
	<div style="width:100%;">
		<form id="consultForm" method="post" accept-charset="UTF-8">
			<table class="table">
				<tr>
					<td width="20%" class="td_title">标题:</td>
					<td colspan="3">
						<textarea  maxlength="100" id ="consultTitle" name="consultGuide.consultTitle" style="width: 95%;height:80px;"></textarea>
					</td>
				</tr>
				<tr>
					<td width="20%" class="td_title">咨询医生:</td>
					<td>
						<input type="text" readonly="readonly" id="docotrName" style="width: 100px;" value="<%=StringUtil.ensureStringNotNull(memberInfo.getDoctorName())%>" onclick="selDoc()" />
						<input type="hidden" id="doctorId" name="consultGuide.doctorId" value="<%=StringUtil.ensureStringNotNull(memberInfo.getDoctorId())%>" />
						<img alt="选择医生" src="<%=request.getContextPath()%>/resources/img/search.png"  onclick="selDoc()" style="cursor: pointer;margin-bottom: -5px;" />
						
						<%-- <label id="doctorName" ><%=memberInfo.getDoctorName() %> </label>
						<input type="hidden" id="doctorId" name= "consultGuide.doctorId" value="<%=memberInfo.getDoctorId() %>" /> --%>
					</td>
				</tr>
				<tr>
					<td class="td_title">咨询内容:</td>
					<td colspan="3" style="padding: 10px 3px 10px 3px;">
						<textarea id="consultContext" maxlength="200" name="consultGuide.consultContext" style="padding: 5px;width: 95%;height: 130px;"></textarea>
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