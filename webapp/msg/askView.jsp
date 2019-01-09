<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.yzxt.yh.constant.Constant"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.util.DateUtil"%>
<%@ page import="com.yzxt.yh.module.msg.bean.Ask"%>
<%@ page import="com.yzxt.yh.module.msg.bean.AskReply"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	Ask ask = (Ask)request.getAttribute("ask");
	@SuppressWarnings("unchecked")
	List<AskReply> askReplys = (List<AskReply>)request.getAttribute("askReplys");
	String ageStr = (String)request.getAttribute("ageStr");
	Boolean replyAble = (Boolean)request.getAttribute("replyAble");
	Boolean needReply = (Boolean)request.getAttribute("needReply");
	String id  = (String)request.getAttribute("id");
	String sexStr = "未知";
	Integer sex = ask.getSex();
	if(sex != null && sex.intValue() == 0){
	    sexStr = "男";
	}else if(sex != null && sex.intValue() == 1){
	    sexStr = "女";
	}
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>提问回复</title>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/ueditor/ueditor.config.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/ueditor/ueditor.all.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="<%=request.getContextPath()%>/resources/ueditor/lang/zh-cn/zh-cn.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script> 
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/common.js"></script>
	<script type="text/javascript">
	function closeIt(){
		try{
			var tc = parent.$("#ttabs");
			var tab = tc.tabs("getSelected");
			if (tab) {
				tc.tabs("close", tab.panel("options").title);
			}
		}catch(e){}
	}
	
	function showReplyForm(){
		var c = document.getElementById("replyContent");
		c.value = "";
		document.getElementById("replyArea").style.display = "block";
		window.scrollTo(0, 99999);
		c.focus();
	}
	
	function cancelReply(){
		document.getElementById("replyArea").style.display = "none";
	}
	
	function submitAskReply(){
		var replyContentEle= document.getElementById("replyContent");
		var replyContentVal = replyContentEle.value;
		// 富文本信息判断
		if(!$.trim(replyContentVal)){
			$.messager.alert("提示信息", "请填写回复内容.", "error");
			return;
		}
		// 处理数据
		document.getElementById("content").value = replyContentVal;
		var askReplyIdEle = document.getElementById("askReplyId");
		var url = "<%=request.getContextPath()%>/msg/ask_addAskReply.action?id=<%=ask.getId()%>";
		if(askReplyIdEle.value){
			url = "<%=request.getContextPath()%>/msg/ask_updateAskReply.action";
		}
		// 表单提交
		$("#askReplyForm").form("submit", {
			url : url,
			dataType : "json",
			onSubmit : function() {
				return true;
			},
			success : function(data) {
				data =  $.parseJSON(data); 
				if(data.state > 0){
					$.messager.alert("提示信息", "回复成功.", "info", function(){
						location.reload();
					});
				}else{
					$.messager.alert("提示信息", data.msg, "error");
				}
			}
		});
	}
	
	function editAsk(){
        var url = "<%=request.getContextPath()%>/msg/ask_toEdit.action?id=<%=ask.getId()%>";
		var title = "健康问答信息";
		top.addTab(title, url, null);
	}
	function delAsk(){
		$.ajax({
			type : "POST",
			dataType : "json",
			url : "<%=request.getContextPath()%>/msg/ask_delete.action?id=<%=ask.getId()%>",
			async : false,
			timeout : 300000,
			success : function(data){
				if(data.state == 1){
					$.messager.alert("提示信息", "删除成功.", "error", function(){
						closeIt();
					});
				}else if(data.state < 0){
					$.messager.alert("提示信息", data.msg, "error");
				}
			},
			error : function(){
				$.messager.alert("提示信息", "删除失败！", "error");
		 	}
		});
	}
	</script>
	<style type="text/css">
	.askBox{
		margin-right: auto;
		margin-left: auto;
		width: 900px;
		border: 1px;
		border-style: solid;
		border-color: #E8E8E8;
	}
	
	.askTitle{
		padding: 10px;
		padding-left: 20px;
		padding-right: 20px;
	}
	.askTitleH{
		font-size: 16px;
	}
	.askSubH{
		font-size: 12px;
		padding-left: 20px;
		color: #666666;
	}
	
	.askContentSplit{
		text-align :left;
		margin-left: 20px;
		margin-right: 20px;
		width: 80%;
		border-color: #F5F5F5;
		border-style: dashed;
	}
	.askBody{
		color: #333333;
		padding-left: 20px;
		padding-right: 20px;
		font-size: 14px;
		line-height: 26px;
	}
	.askOper{
		padding-left: 20px;
		font-size: 14px;
		margin-bottom: 5px;
	}
	
	.askOperItem{
		text-decoration: underline;
		color: #E10900;
	}
	
	.replySum{
		margin-top: 30px;
		margin-right: auto;
		margin-left: auto;
		width: 900px;
		border: 0px;
		
	}
	.replySumH{
		font-size: 16px;
		color: #333333;
	}
	
	.replyHeaderSplit{
		 width: 100%;
		 border: 0px;
		 border-top: 5px solid #CCE8D3;
	}
	
	.replyBoxs{
		margin-top: 20px;
		margin-right: auto;
		margin-left: auto;
		width: 900px;
	}
	.replyBox{
	}
	.replyHead{
		font-size: 12px;
		color: #666666;
	}
	.replyBody{
		color: #333333;
		padding-right: 20px;
		font-size: 14px;
		line-height: 26px;
	}
	.replySplit{
		text-align :left;
		width: 100%;
		border-color: #F5F5F5;
		border-style: dashed;
	}
	
	.replyAsk{
		margin-top: 30px;
		margin-right: auto;
		margin-left: auto;
		width: 900px;
		border: 0px;
	}
	
	.replyAskH{
		font-size: 16px;
		color: #333333;
	}
	.replyAskSplit{
		 width: 100%;
		 border: 0px;
		 border-top: 5px solid #CCE8D3;
	}
	</style>
</head>
<body style="padding-top: 20px;">
	<div class="askBox">
		<div class="askTitle">
			<h1 class="askTitleH"><%=ask.getTitle()%></h1>
		</div>
		<div class="askSubH">
			<%=ask.getCreateByName() != null ? ask.getCreateByName() : ""%>
			&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;<%=sexStr%>
			<%
			if(StringUtil.isNotEmpty(ageStr))
			{
			%>
			&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;<%=ageStr%>岁
			<%
			}
			%>
			&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;<%=DateUtil.toHtmlTime(ask.getUpdateTime())%>
		</div>
		<hr class="askContentSplit" />
		<div class="askBody">
			<p>
				<%=ask.getContent()%>
			</p>
		</div>
		<%
		if(replyAble)
		{
		%>
		<hr class="askContentSplit" />
		<div class="askOper">
			<a href="javascript: void(0);" onclick="showReplyForm();return false;" class="askOperItem">我要回复</a>
		</div>
		<%
		}
		%>
	</div>
	<div class="replySum">
		<h2 class="replySumH">共&nbsp;<%=askReplys != null ? askReplys.size() : 0%>&nbsp;条回复</h2>
		<hr class="replyHeaderSplit">
	</div>
	<%
	if(askReplys != null && askReplys.size() > 0)
	{
	%>
	<div class="replyBoxs">
	<%
	    for(int i = 0; i < askReplys.size(); i++)
	    {
	        AskReply reply = askReplys.get(i);
	%>
	<%
			if(i > 0)
			{
	%>
		<hr class="replySplit" />
	<%
			}
	%>
		<div class="replyBox">
			<div class="replyHead"><%=reply.getUpdateByName()%><%=Constant.USER_TYPE_DOCTOR.equals(reply.getUpdateByUserType()) ? "（医生）" : ""%>&nbsp;&nbsp;&nbsp;<%=DateUtil.toHtmlTime(reply.getUpdateTime())%></div>
			<div class="replyBody">
				<p>
				<%=reply.getContent()%>
				</p>
			</div>
			<div class="replyOper"></div>
		</div>
	<%
	    }
	%>
	</div>
	<%
	}
	%>
	<div id="replyArea" style="display: <%=needReply?"block":"none"%>;" class="replyAsk">
		<h2 class="replyAskH">回复</h2>
		<hr class="replyAskSplit">
		<div>
			<textarea id="replyContent" rows="" cols="" maxlength="1000"
				style="height: 120px;width: 100%;line-height: 22px;"></textarea>
		</div>
		<div class="ct" style="text-align: center; margin-top: 15px;">
			<input type="button" id="btnSubmit" name="btnSubmit" onclick="submitAskReply();" value="提交" />
			&nbsp;&nbsp;
			<input type="button" id="btnReset" name="btnReset" onclick="cancelReply();" value="取消" />
		</div>
	</div>
	<a id="replyPos"></a>
	<div style="display: none;">
		<form method="post" id="askReplyForm" name="askReplyForm" action="" enctype="multipart/form-data">
			<input type="hidden" name="askReply.askId" value="<%=ask.getId()%>" />
			<input type="hidden" id="askReplyId" name="askReply.id" value="" />
			<input type="hidden" id="content" name="askReply.content" />
		</form>
	</div>
</body>
</html>