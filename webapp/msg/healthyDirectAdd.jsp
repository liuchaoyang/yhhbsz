<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.module.sys.bean.User"%>
<%
	User user = (User)request.getAttribute("user");
	if(user==null){
		user = new User();
	}
	String id = (String)request.getAttribute("id");
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>预警消息</title>
	<style type="text/css">
	</style>
	<script type="text/javascript">
	var archiveGrid;
	$(function(){
		archiveGrid = $("#archives").datagrid({
			title: "预警信息列表",
			width: "auto",
			height: "auto",
			idField: "id",
			rownumbers: true,
			pagination: true,
			pageList: [10, 20, 50, 100],
			singleSelect: true,
			nowrap: true,
			striped: true,
			fitColumns: true,
			loadMsg: "数据加载中,请稍候...",
			// toolbar: "#toolbar",
			// url: null,
			columns: [[{
				field: "itemName",
				title: "预警项目",
				width: 60,
				align: "center"
			}, {
				field: "content",
				title: "预警内容",
				width: 130,
				align: "center"
			}, {
				field: "warningLevel",
				title: "预警等级",
				width: 60,
				align: "center"
			}, {
				field: "warningTime",
				title: "预警时间",
				width: 90,
				align: "center"
			}]],
			data: [{
				id: "1",
				itemName: "血压",
				content: "收缩压值为156mmHg，已属于高血压1级的范围。",
				warningLevel: "一级",
				warningTime: "2015-03-22 11:23:41"
			}, {
				id: "2",
				itemName: "血糖",
				content: "空腹血糖值为11.0 mmol/L，疑似糖尿病。",
				warningLevel: "二级",
				warningTime: "2015-03-20 07:09:35"
			}, {
				id: "3",
				itemName: "血压",
				content: "收缩压值为156mmHg，已属于高血压1级的范围。",
				warningLevel: "一级",
				warningTime: "2015-03-18 110:33:15"
			}]
		});
	});
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
		<%--先选择一个居民，然后跳转到新增页面
			parent.
		--%>
		subWin = $('<div><iframe src="<%=request.getContextPath()%>/msg/menbersSel.jsp" style="width: 98%;height:99%;" border="0" frameborder="0" scoll="no"></iframe></div>').window({
			title: "选择会员",
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
		$("#userId").val(cust.userId);
		$("#memberName").val(cust.name);
	}
	
	function validForm(){
		<%--姓名--%>
		var userName = document.getElementById("memberName");
		var userNameVal = $.trim(userName.value);
		userName.value = userNameVal;
		if(!userNameVal){
			$.messager.alert("提示信息", "请输入姓名。", "error", function(){
				userName.focus();
			});
			return false;
		}
		<%--指导原因--%>
		var directReason = document.getElementById("directReason");
		var directReasonVal = $.trim(directReason.value);
		directReason.value = directReasonVal;
		if(!directReasonVal){
			$.messager.alert("提示信息", "请输入被指导原因。", "error", function(){
				directReason.focus();
			});
			return false;
		}
		if(directReason.value.length > 100){
			$.messager.alert("提示信息", "字数不能超过100个！", "error", function(){
				directReason.focus();
			});
			return;
		}
		// 运动指导
		var sportDirect = document.getElementById("sportDirect");
		if(sportDirect.value.length > 100){
			$.messager.alert("提示信息", "字数不能超过100个！", "error", function(){
				sportDirect.focus();
			});
			return;
		}
		//饮食指导
		var foodDirect = document.getElementById("foodDirect");
		if(foodDirect.value.length > 100){
			$.messager.alert("提示信息", "字数不能超过100个！", "error", function(){
				foodDirect.focus();
			});
			return;
		}
		//药物建议指导
		var drugSuggest = document.getElementById("drugSuggest");
		if(drugSuggest.value.length > 100){
			$.messager.alert("提示信息", "字数不能超过100个！", "error", function(){
				drugSuggest.focus();
			});
			return;
		}
		//备注
		var memo = document.getElementById("memo");
		if(memo.value.length > 200){
			$.messager.alert("提示信息", "字数不能超过200个！", "error", function(){
				drugSuggest.focus();
			});
			return;
		}
		// 验证通过
		return true;
	}
	function save(){
		if(!validForm()){
			return;
		}
		$("#filterForm").form("submit", {
			url: "<%=request.getContextPath()%>/msg/heaGuide_add.action?id=<%=id%>",
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
		<form id="filterForm" method="post" accept-charset="UTF-8">
			<table class="table">
				<tr>
					<td width="20%" class="td_title">会员名称:</td>
					<td width="70%">
						<input type="text" id="memberName" name="healthyGuide.memberName" maxlength="20" value="<%if(user.getName()!=null){ %><%=user.getName()%><%} %>" disabled="disabled" data-options="" style="width: 235px;" />
						<%if(user.getName()==null){ %>
						<img id="selUserImg" alt="选择会员" src="<%=request.getContextPath()%>/resources/img/search.png" onclick="openSelResi()" style="cursor: pointer;margin-bottom: -5px;" />
						<%} %>
						<input type="hidden" id="userId" name="healthyGuide.custId" value="<%if(user.getId()!=null){ %><%=user.getId()%><%} %>" />
					</td>
				</tr>
				<tr>
					<td width="20%" class="td_title">指导原因:</td>
					<td>
						<textarea id="directReason" maxlength="100" name="healthyGuide.directReason" style="padding: 5px;width: 95%;height: 40px;"></textarea>
					</td>
				</tr>
				<!-- <tr>
					<td width="20%" class="td_title">指导内容:</td>
					<td style="padding: 10px 3px 10px 3px;">
						<textarea style="padding: 5px;width: 95%;height: 80px;"></textarea>
					</td>
				</tr> -->
				<tr>
					<td width="20%" class="td_title">运动指导:</td>
					<td>
						<textarea id="sportDirect" maxlength="100" name="healthyGuide.sportDirect" style="padding: 5px;width: 95%;height: 40px;"></textarea>
					</td>
				</tr>
				<tr>
					<td width="20%" class="td_title">饮食指导:</td>
					<td style="padding: 10px 3px 10px 3px;">
						<textarea id="foodDirect" maxlength="100" name="healthyGuide.foodDirect" style="padding: 5px;width: 95%;height: 40px;"></textarea>
					</td>
				</tr>
				<tr>
					<td width="20%" class="td_title">药物建议:</td>
					<td>
						<textarea id="drugSuggest" maxlength="100" name="healthyGuide.drugSuggest" style="padding: 5px;width: 95%;height: 40px;"></textarea>
					</td>
				</tr>
				<tr>
					<td width="20%" class="td_title">备注:</td>
					<td>
						<textarea id="memo" maxlength="200" name="healthyGuide.memo" style="padding: 5px;width: 95%;height: 40px;"></textarea>
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