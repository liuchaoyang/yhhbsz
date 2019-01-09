<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.yzxt.yh.constant.Constant"%>
<%@ page import="com.yzxt.yh.util.DateUtil"%>
<%@ page import="com.yzxt.fw.pager.PageModel"%>
<%@ page import="com.yzxt.yh.module.msg.bean.Knowledge"%>
<%
	@SuppressWarnings("unchecked")
	PageModel<Knowledge> pageModel = (PageModel<Knowledge>)request.getAttribute("pageModel");
	// 每页记录数
	int pageRow = 10;
%>
<!DOCTYPE HTML>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>健康知识列表</title>
<style type="text/css">
.nlgCon {
	padding: 8px 10px 10px 25px;
}
.nlgTitle {
	font-size: 15px;
	font-weight: bold;
	color: #006699;
	text-decoration: none;
}
.nlgTitle:HOVER {
	color: #bc2a4d;
	text-decoration: underline;
	cursor: pointer;
}
.nlgSummary {
	padding-top: 10px;
	padding-left: 15px;
	line-height: 180%;
}
.nlgFoot {
	padding-top: 10px;
	color: gray;
}
.noDataTip {
	padding-left: 25px;
	font-style: italic;
}
#pageDiv {
	padding-left: 28px;
	padding-top: 15px;
	text-align: left;
}
.pageItem {
	border: 1px solid blue;
	padding: 2px 3px;
	border: 1px solid #006699;
	color: #006699;
	cursor: pointer;
}
.curPage {
	color: #ddd;
	background: #006699;
}
</style>
<script type="text/javascript">
	function searchByKey(value) {
		document.getElementById("keyword").value = $.trim(value);
		var formEle = document.getElementById("nlgFrom");
		formEle.action = "<%=request.getContextPath()%>/msg/nlg_list.action?catalogId=${catalogId}&page=1&rows=<%=pageRow%>";
		formEle.submit();
	}
	function searchByCata(value) {
		var formEle = document.getElementById("nlgFrom");
		document.getElementById("keyword").value = $.trim($("#searchKey").searchbox("getValue"));
		formEle.action = "<%=request.getContextPath()%>/msg/nlg_list.action?catalogFullId=" + value + "&page=1&rows=<%=pageRow%>";
		formEle.submit();
	}
	function searchByRefresh() {
		location.reload();
	}
	function showPage(pn) {
		var formEle = document.getElementById("nlgFrom");
		formEle.action = "<%=request.getContextPath()%>/msg/nlg_list.action?catalogId=${catalogId}&page=" + pn + "&rows=<%=pageRow%>";
		formEle.submit();
	}
	// 查看页面
	function showNlgDetail(nlsId){
        var url = "<%=request.getContextPath()%>/msg/nlg_view.action?pPageId=${param.pPageId}&id=" + nlsId;
		var title = "查看健康知识";
		top.addTab(title, url, null);
	}
</script>
</head>
<body style="width: 100%;overflow: auto;margin: 0px;padding: 0px;">
	<div style="overflow: visible; padding: 5px;">
		<form id="nlgFrom" name="nlgFrom" style="margin: 0px;padding: 0px;" method="post">
			<table style="width: 100%;">
				<tr>
					<td>
						&nbsp;
						<input type="hidden" id="keyword" name="keyword" value="${keyword}" />
					</td>
					<td align="right">
						<input id="searchKey" class="easyui-searchbox" style="width: 300px;" value="${keyword}"
							data-options="searcher:searchByKey,prompt:'请输入标题内容.'" maxlength="100" />
					</td>
				</tr>
			</table>
		</form>
		<hr style="border-top: solid 1px green; width: 85%; text-align: left;">
		<div>
		<%
		List<Knowledge> nlgs = (List<Knowledge>)pageModel.getData();
		if(nlgs != null && nlgs.size() > 0)
		{
			for(Knowledge nlg : nlgs)
			{
		%>
			<div class="nlgCon">
				<div>
					<a href="javascript:void(0);" onclick="showNlgDetail('<%=nlg.getId()%>');return false;" class="nlgTitle"><%=nlg.getTitle()%></a>
				</div>
				<div class="nlgSummary">
					<%=nlg.getSummary()%>
				</div>
			</div>
		<%
			}
		}else
		{
		%>
			<div class="noDataTip">没有查询到相关数据.</div>
		<%
		}
		%>
			<div id="pageDiv">
				<%
				int totalRow = pageModel.getTotal();
				int totalPage = (totalRow + pageRow - 1) / pageRow;
				int curPage = pageModel.getPageNo();
				if(curPage > totalPage){
				    curPage = totalPage;
				}
				curPage = curPage > 0 ? curPage : 1;
				%>
				<%-- 分页显示格式，1,2,...,4,[5],6,...,8,9 --%>
				<%-- 总是显示第一页 --%>
				<%
				if(totalPage > 0)
				{
				%>
				<a class="pageItem<%=(curPage == 1 ? " curPage" : "")%>" onclick="showPage(1);">1</a>
				<%
				}
				%>
				<%-- 有2页才显示第二页 --%>
				<%
				if(totalPage >= 2)
				{
				%>
				<a class="pageItem<%=(curPage == 2 ? " curPage" : "")%>" onclick="showPage(2);">2</a>
				<%
				}
				%>
				<%-- 当前页面大于等于5时当前页面前的省略号 --%>
				<%
				if(curPage >= 5)
				{
				%>
				...
				<%
				}
				%>
				<%-- 当前页大于等于4时显示当前页面的前一页（即前一页须大于2） --%>
				<%
				if(curPage >= 4)
				{
				%>
				<a class="pageItem" onclick="showPage(<%=(curPage - 1)%>);"><%=(curPage - 1)%></a>
				<%
				}
				%>
				<%-- 当前面，当前页面须大于2，以防和前面的页面重复 --%>
				<%
				if(curPage >= 3)
				{
				%>
				<a class="pageItem curPage" onclick="showPage(<%=curPage%>);"><%=curPage%></a>
				<%
				}
				%>
				<%-- 当前页面下一页 --%>
				<%
				if(totalPage > curPage && curPage > 1)
				{
				%>
				<a class="pageItem" onclick="showPage(<%=(curPage + 1)%>);"><%=(curPage + 1)%></a>
				<%
				}
				%>
				<%-- 总页面比当前页面大于时，显示当前页后面的省略页 --%>
				<%
				if(totalPage > curPage + 3)
				{
				%>
				...
				<%
				}
				%>
				<%-- 总页数比当前页大于1时显示倒数第二页面，不能和最后一页重复 --%>
				<%
				if(totalPage > curPage + 2)
				{
				%>
				<a class="pageItem" onclick="showPage(<%=(totalPage - 1)%>);"><%=(totalPage - 1)%></a>
				<%
				}
				%>
				<%-- 总页数比当前页大于时显示最后一页 --%>
				<%
				if(totalPage >= curPage + 2)
				{
				%>
				<a class="pageItem" onclick="showPage(<%=totalPage%>);"><%=totalPage%></a>
				<%
				}
				%>
			</div>
		</div>
	</div>
</body>
</html>