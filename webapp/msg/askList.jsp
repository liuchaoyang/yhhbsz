<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.yzxt.yh.constant.Constant"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.util.DateUtil"%>
<%@ page import="com.yzxt.fw.pager.PageModel"%>
<%@ page import="com.yzxt.yh.module.msg.bean.AskCatalog"%>
<%@ page import="com.yzxt.yh.module.msg.bean.Ask"%>
<%
	String topLevelCatalogId = (String)request.getAttribute("topLevelCatalogId");
	String secondLevelCatalogId = (String)request.getAttribute("secondLevelCatalogId");
	String catalogId = StringUtil.isNotEmpty(secondLevelCatalogId) ? secondLevelCatalogId : topLevelCatalogId;
	@SuppressWarnings("unchecked")
	List<AskCatalog> topLevelAskCatalogs = (List<AskCatalog>)request.getAttribute("topLevelAskCatalogs");
	@SuppressWarnings("unchecked")
	List<AskCatalog> secondLevelAskCatalogs = (List<AskCatalog>)request.getAttribute("secondLevelAskCatalogs");
	@SuppressWarnings("unchecked")
	PageModel<Ask> pageModel = (PageModel<Ask>)request.getAttribute("pageModel");
	// 每页记录数
	int pageRow = 10;
%>
<!DOCTYPE HTML>
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/resources/css/base.css">
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/jquery-1.8.0.min.js"></script>
<title>问答列表</title>
<meta charset="UTF-8">
<meta name="pragma" content="no-cache" />
<meta name="cache-control" content="no-cache" />
<meta name="expires" content="0" />
<meta name="keywords" content="keyword1,keyword2,keyword3" />
<meta name="description" content="问答列表" />
<style type="text/css">
ul {
	list-style: none;
	display: block;
	margin-left: -15px;
	margin-top: 0px;
	margin-bottom: 0px;
}

ul li {
	font-size: 18px;
	display: block;
	float: left;
	margin: 5px;
	height: 22px;
}

ul li a {
	text-decoration: none;
	cursor: pointer;
}

ul li a:HOVER {
	text-decoration: none;
	color: #ff580a;
}

a.activCata {
	color: #ff580a;
}

ul li.cataLine {
	width: 2px;
	margin-top: 10px;
	height: 15px;
	background: #e7e7e7;
}

#subCataHeadDiv {
	padding-left: 25px;
}

.subCata {
	margin: 10px;
	cursor: pointer;
}

.subActive{
	/*font-weight: bold;*/
	color: #ff580a;
	text-decoration: underline;
}

.askCon {
	padding: 8px 10px 10px 25px;
}

.askSubCataCon {
	padding: 8px 5px 5px 25px;
}

.askTitle {
	font-size: 15px;
	font-weight: bold;
	color: #006699;
}

.askTitle:HOVER {
	color: #bc2a4d;
	text-decoration: underline;
	cursor: pointer;
}

.askSummary {
	padding-top: 10px;
	padding-left: 15px;
	line-height: 180%;
}

.askFoot {
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
	function showPage(askCatalogId, pageNum) {
		var url = "<%=request.getContextPath()%>/msg/ask_listAsks.action?askCatalogId=" + askCatalogId + "&page=" + pageNum + "&rows=<%=pageRow%>&t=" + (new Date()).getTime();
		location.href = url;
	}
	
    function showAskDetail(askId){
        var url = "<%=request.getContextPath()%>/msg/ask_view.action?id=" + askId;
		var title = "健康问答信息";
		top.addTab(title, url, null);
	}
    
    function addAsk(){
        var url = "<%=request.getContextPath()%>/msg/ask_toEdit.action?pPageId=${param.pageId}";
		var title = "健康问答信息";
		top.addTab(title, url, null);
    }
    function refresh(){
    	location.reload();
    }
</script>
</head>
<body style="width: 95%; margin-left: auto;margin-right: auto;">
	<ul>
	<%
	for(AskCatalog cata : topLevelAskCatalogs)
	{
	%>
		<li><a <%=cata.getId().equals(topLevelCatalogId)? "class=\"activCata\"" : ""%> onclick="showPage('<%=cata.getId()%>', 1)"><%=cata.getName()%></a></li>
		<li class="cataLine"></li>
	<%
	}
	%>
		<%-- 我的提问 --%>
		<li><a <%=Constant.ASK_CATALOG_MY_PUBLISH.equals(topLevelCatalogId) ? "class=\"activCata\"" : ""%> onclick="showPage('<%=Constant.ASK_CATALOG_MY_PUBLISH%>', 1)">我的提问</a></li>
	</ul>
	<div style="clear: both;"></div>
	<div class="askCon askSubCataCon">
		<table style="width: 100%;">
			<tr>
				<td>
					<%
					if(secondLevelAskCatalogs!=null)
					{
					for(AskCatalog subCata : secondLevelAskCatalogs)
					{
					%>
					<a class="subCata<%=subCata.getId().equals(secondLevelCatalogId)? " subActive" : ""%>" onclick="showPage('<%=subCata.getId()%>', 1)"><%=subCata.getName()%></a>
					<%
					}
					}
					%>
				</td>
				<td style="width: 50px;">
					<input type="button" value="提问" onclick="addAsk();" />
				</td>
			</tr>
		</table>
	</div>
	<hr style="width: 50%; text-align: left; margin-left: 25px; border: 1px solid #f2f8ef;">
	<div>
	<%
	List<Ask> asks = (List<Ask>)pageModel.getData();
	if(asks != null && asks.size() > 0)
	{
		for(Ask ask : asks)
		{
	%>
		<div class="askCon">
		    <div>
				<a href="javascript:void(0);" onclick="showAskDetail('<%=ask.getId()%>');return false;" class="askTitle"><%=ask.getTitle()%></a>
			</div>
			<div class="askSummary">
				<%=ask.getSummary()%>
			</div>
			<div class="askFoot">
				提问人:<%=StringUtil.ensureStringNotNull(ask.getUpdateByName())%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;提问时间：<%=DateUtil.toHtmlTime(ask.getUpdateTime())%>
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
	</div>
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
		<a class="pageItem<%=(curPage == 1 ? " curPage" : "")%>" onclick="showPage('<%=catalogId%>', 1);">1</a>
		<%
		}
		%>
		<%-- 有2页才显示第二页 --%>
		<%
		if(totalPage >= 2)
		{
		%>
		<a class="pageItem<%=(curPage == 2 ? " curPage" : "")%>" onclick="showPage('<%=catalogId%>', 2);">2</a>
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
		<a class="pageItem" onclick="showPage('<%=catalogId%>', <%=(curPage - 1)%>);"><%=(curPage - 1)%></a>
		<%
		}
		%>
		<%-- 当前面，当前页面须大于2，以防和前面的页面重复 --%>
		<%
		if(curPage >= 3)
		{
		%>
		<a class="pageItem curPage" onclick="showPage('<%=catalogId%>', <%=curPage%>);"><%=curPage%></a>
		<%
		}
		%>
		<%-- 当前页面下一页 --%>
		<%
		if(totalPage > curPage && curPage > 1)
		{
		%>
		<a class="pageItem" onclick="showPage('<%=catalogId%>', <%=(curPage + 1)%>);"><%=(curPage + 1)%></a>
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
		<a class="pageItem" onclick="showPage('<%=catalogId%>', <%=(totalPage - 1)%>);"><%=(totalPage - 1)%></a>
		<%
		}
		%>
		<%-- 总页数比当前页大于时显示最后一页 --%>
		<%
		if(totalPage >= curPage + 2)
		{
		%>
		<a class="pageItem" onclick="showPage('<%=catalogId%>', <%=totalPage%>);"><%=totalPage%></a>
		<%
		}
		%>
	</div>
</body>
</html>