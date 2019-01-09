<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.yzxt.yh.constant.Constant"%>
<%@ page import="com.yzxt.yh.util.DateUtil"%>
<%@ page import="com.yzxt.fw.pager.PageModel"%>
<%@ page import="com.yzxt.yh.module.msg.bean.Information"%>
<%@ page import="com.yzxt.yh.module.msg.bean.InfoCatalog"%>
<%
	String infoCatalogId = (String)request.getAttribute("infoCatalogId");
	Integer infoCatalogType = (Integer)request.getAttribute("infoCatalogType");
	InfoCatalog infoCatalog = (InfoCatalog)request.getAttribute("infoCatalog");
	boolean canPublish = (Boolean)request.getAttribute("canPublish");
	@SuppressWarnings("unchecked")
	List<InfoCatalog> columns = (List<InfoCatalog>)request.getAttribute("columns");
	@SuppressWarnings({"unchecked" ,"rawtypes"})
	PageModel pageModel = (PageModel<Information>)request.getAttribute("pageModel");
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
<title>资讯列表</title>
<meta charset="UTF-8">
<meta name="pragma" content="no-cache" />
<meta name="cache-control" content="no-cache" />
<meta name="expires" content="0" />
<meta name="keywords" content="keyword1,keyword2,keyword3" />
<meta name="description" content="资讯列表" />
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

a.activCol {
	font-weight: bold;
	color: #ff580a;
}

ul li.colLine {
	width: 2px;
	margin-top: 10px;
	height: 15px;
	background: #e7e7e7;
}

#topicHeadDiv {
	padding-left: 25px;
}

.topicHead {
	margin: 10px;
}

.infoCon {
	padding: 8px 10px 10px 25px;
}

.infoTitle {
	font-size: 15px;
	font-weight: bold;
	color: #006699;
}

.infoTitle:HOVER {
	color: #bc2a4d;
	text-decoration: underline;
	cursor: pointer;
}

.infoSummary {
	padding-top: 10px;
	padding-left: 15px;
	line-height: 180%;
}

.infoFoot {
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
	function showPage(infoCataId, pageNum) {
		var url = "<%=request.getContextPath()%>/msg/info_list.action?pageId=${param.pageId}&infoCatalogId=" + infoCataId + "&page=" + pageNum + "&rows=<%=pageRow%>&t=" + (new Date()).getTime();
		location.href = url;
	}
	
    function showInfoDetail(infoId){
        var url = "<%=request.getContextPath()%>/msg/info_view.action?id=" + infoId;
		var title = "查看资讯";
		top.addTab(title, url, null);
	}
	function refresh(){
		location.reload();
	}
</script>
</head>
<body style="width: 95%; margin: auto;">
	<ul>
	<%
	for(InfoCatalog cata : columns)
	{
	%>
		<li><a <%=infoCatalogId.equals(cata.getId())? "class=\"activCol\"" : ""%> onclick="showPage('<%=cata.getId()%>', 1)"><%=cata.getName()%></a></li>
		<li class="colLine"></li>
	<%
	}
	%>
		<%-- 专题栏 --%>
		<!--
		<li><a <%=infoCatalogId.equals(Constant.INFO_COLUMN_TOPIC) || Constant.INFO_CATALOG_TYPE_TOPIC.equals(infoCatalogType) ? "class=\"activCol\"" : ""%> onclick="showPage('<%=Constant.INFO_COLUMN_TOPIC%>', 1)">专题</a></li>
		<li class="colLine"></li>
		-->
		<%-- 我的发布 --%>
		<%
		if(canPublish)
		{
		%>
		<li><a <%=infoCatalogId.equals(Constant.INFO_COLUMN_MY_PUBLISH)? "class=\"activCol\"" : ""%> onclick="showPage('<%=Constant.INFO_COLUMN_MY_PUBLISH%>', 1)">我的发布</a></li>
		<%
		}
		%>
	</ul>
	<div style="clear: both;"></div>
	<%-- 如果具体选中了一个专题，显示这个选中的专题 --%>
	<%
	if(Constant.INFO_CATALOG_TYPE_TOPIC.equals(infoCatalogType))
	{
	%>
		<div style="margin: 5px;" id="topicHeadDiv">
			<a class="topicHead"><%=infoCatalog.getName()%></a>
		</div>
	<%
	}
	%>
	<hr style="width: 50%; text-align: left; margin-left: 25px; border: 1px solid #f2f8ef;">
	<%-- 如果具体选中了一个非专题栏目或一个具体的专题，显示资讯列表 --%>
	<%
	if(!Constant.INFO_COLUMN_TOPIC.equals(infoCatalogId))
	{
	%>
	<div>
	<%
	@SuppressWarnings({"unchecked"})
	List<Information> infos = (List<Information>)pageModel.getData();
	if(infos != null && infos.size() > 0)
	{
		for(Information info : infos)
		{
	%>
		<div class="infoCon">
		    <div>
				<a href="javascript:void(0);" onclick="showInfoDetail('<%=info.getId()%>');return false;" class="infoTitle"><%=info.getTitle()%></a>
			</div>
			<div class="infoSummary">
				<%=info.getSummary()%>
			</div>
			<div class="infoFoot">
				发布者:<%=info.getUpdateByName()%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;发布时间：<%=DateUtil.toHtmlTime(info.getUpdateTime())%>
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
	<%
	}else
	// 专题列表
	{
	@SuppressWarnings({"unchecked"})
	List<InfoCatalog> catas = (List<InfoCatalog>)pageModel.getData();
	if(catas != null && catas.size() > 0)
	{
		for(InfoCatalog cata : catas)
		{
	%>
		<div class="infoCon">
		    <div>
				<img src="<%=request.getContextPath()%>/resources/img/cata.png" style="margin-bottom: -2px;" />&nbsp;
				<a href="javascript:void(0);" onclick="showPage('<%=cata.getId()%>', 1);return false;" class="infoTitle"><%=cata.getName()%></a>
			</div>
			<div class="infoSummary">
				<%=cata.getDetail()%>
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
		<a class="pageItem<%=(curPage == 1 ? " curPage" : "")%>" onclick="showPage('<%=infoCatalogId%>', 1);">1</a>
		<%
		}
		%>
		<%-- 有2页才显示第二页 --%>
		<%
		if(totalPage >= 2)
		{
		%>
		<a class="pageItem<%=(curPage == 2 ? " curPage" : "")%>" onclick="showPage('<%=infoCatalogId%>', 2);">2</a>
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
		<a class="pageItem" onclick="showPage('<%=infoCatalogId%>', <%=(curPage - 1)%>);"><%=(curPage - 1)%></a>
		<%
		}
		%>
		<%-- 当前面，当前页面须大于2，以防和前面的页面重复 --%>
		<%
		if(curPage >= 3)
		{
		%>
		<a class="pageItem curPage" onclick="showPage('<%=infoCatalogId%>', <%=curPage%>);"><%=curPage%></a>
		<%
		}
		%>
		<%-- 当前页面下一页 --%>
		<%
		if(totalPage > curPage && curPage > 1)
		{
		%>
		<a class="pageItem" onclick="showPage('<%=infoCatalogId%>', <%=(curPage + 1)%>);"><%=(curPage + 1)%></a>
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
		<a class="pageItem" onclick="showPage('<%=infoCatalogId%>', <%=(totalPage - 1)%>);"><%=(totalPage - 1)%></a>
		<%
		}
		%>
		<%-- 总页数比当前页大于时显示最后一页 --%>
		<%
		if(totalPage >= curPage + 2)
		{
		%>
		<a class="pageItem" onclick="showPage('<%=infoCatalogId%>', <%=totalPage%>);"><%=totalPage%></a>
		<%
		}
		%>
	</div>
</body>
</html>