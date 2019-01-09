<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.module.sys.bean.User"%>
<%@ page import="com.yzxt.yh.module.svb.bean.MemberInfo"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.util.DateUtil"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%
User user= (User)request.getAttribute("user");
MemberInfo memberInfo= (MemberInfo)request.getAttribute("memberInfo");
String operType = request.getParameter("operType");
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>会员列表</title>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
	<style type="text/css">
	</style>
	<script type="text/javascript"> 
	
	function closeIt(){
		try{
			parent.subWin.window("close");
		}catch(e){}
	}
		function save(){
		$("#filterForm").form("submit", {
			url: "<%=request.getContextPath()%>/msg/shotmsg_add.action",
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
	function addMemWin(){
		subWin = $('<div><iframe src="<%=request.getContextPath()%>/doctorTool/addUser.jsp" style="width: 99%;height:99%;border="0" frameborder="0"></iframe></div>').window({
			title : "添加会员名称",
			width : 900,
			height : 400,
			resizable : false,
			collapsible : false,
			minimizable : false,
			maximizable : false,
			modal : true,
			zIndex : 100,
			closable : true
		});
	}
	
</script>
</head>
<body style="margin: 1px;padding: 0px;">
		<div>
		<form id="filterForm" style="margin: 0px;padding: 0px;" method="post" accept-charset="UTF-8">
		<table border="0">
				  <tr>
				    <td class="td_title" align="right" nowrap="nowrap">会员名称：</td>
				     <td  style="width: 200px;">
				    	<input type="text" maxlength="100" readonly="readonly" id="name" name="shortMsg.name" value="<%=request.getParameter("name") %>"/>
				    	<input type="hidden" id="custId" name="shortMsg.custId" value="<%=request.getParameter("custId") %> ">
						<img id="selUserImg" alt="选择居民" src="<%=request.getContextPath()%>/resources/img/search.png" onclick="addMemWin()" style="cursor: pointer;margin-bottom: -5px;" />
					</td>
					<td class="td_title" align="right" nowrap="nowrap">手机号：</td>
				    <td  style="width: 200px;">
		    			<input type="text" id="phone"  readonly="readonly" name="shortMsg.phone" maxlength="100" value="<%=request.getParameter("phone") %>"  />
		    		</td>
				  </tr>
				   <tr>
				    <td  class="td_title" align="right" nowrap="nowrap">编辑内容：</td>
				    <td colspan="3">
		    			<textarea  style="width: 400px;height:100px;" id="msgContext" name="shortMsg.msgContext" maxlength="100"   ></textarea>
		    		</td>
				   </tr>
				   <tr> <td>&nbsp;</td></tr>
					<tr>
		    			<td colspan="4" align="center">
		    				<a class="easyui-linkbutton" href="javascript:save()">发送</a> &nbsp;&nbsp; 
		    				<a class="easyui-linkbutton" href="javascript:clear()">返回</a> &nbsp;&nbsp; 
		    			</td>
		    	  </tr>
				</table>

		</form>
	</div>
	
</body>
</html>