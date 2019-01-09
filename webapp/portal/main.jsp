<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.yzxt.fw.server.ConstSv"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.module.sys.bean.User"%>
<%@ page import="com.yzxt.yh.module.sys.bean.Resource"%>
<%@ page import="com.yzxt.yh.module.sys.bean.Org"%>
<%
@SuppressWarnings("unchecked")
List<Resource>[] ress = (List<Resource>[])request.getAttribute("resources");
for(Resource res0 : ress[0])
{
	List<Resource> children0 = new ArrayList<Resource>();
	res0.setChildren(children0);
	for(Resource res1 : ress[1])
	{
		// 添加2级菜单
		if(res0.getId().equals(res1.getParentId()))
		{
			children0.add(res1);
		}
		// 添加3级菜单
		List<Resource> children1 = new ArrayList<Resource>();
		res1.setChildren(children1);
		for(Resource res2 : ress[2])
		{
			if(res1.getId().equals(res2.getParentId()))
			{
				children1.add(res2);
			}
		}
	}
}
Org org = (Org)request.getAttribute("org");
User user = (User)request.getSession().getAttribute(ConstSv.SESSION_USER_KEY);
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="pragma" content="no-cache" />
	<meta name="cache-control" content="no-cache" />
	<meta name="expires" content="0" />
	<title>医疗健康平台</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/easyui/themes/default/easyui.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/easyui/themes/icon.css" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery-1.8.0.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/easyui/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/scriptbreaker-multiple-accordion-1.js"></script>
	<style type="text/css">
	#header{
		height: 70px;
		overflow: hidden;
		background:url(<%=request.getContextPath()%>/resources/img/portal/dingbu.png);
		background-size:cover;
	}
	.header1{
		height: 70px;
		overflow: hidden;
		background:url(<%=request.getContextPath()%>/resources/img/portal/dingbu.png);
		background-size:cover;
	}
	#divUser{
		color: #ffffff;
		line-height: 25px;
	}
	.header_link{
		font-family: "宋体", Arial, Helvetica, sans-serif;
		font-size: 13px;
		font-weight: 800;
		text-decoration: none;
		color: red;
		/* #0066CC */
	}
	#logo{
		float: left;
		height: 40px;
	}
	#toppiz{
		float: right;
		text-align: right;
	}
	.ctSplit{
		margin-left: 2px;
		margin-right: 2px;
	}
	.menuPage{
		text-decoration: none;
		color: black;
	}
	<%--菜单--%>
	ul.topnav {
		width: 100%;
		min-width: 100px;
		padding: 0px;
		margin: 0px;
		font-size: 1em;
		line-height: 0.5em;
		list-style: none;
	}
	ul.topnav li {
		border-top: 2px solid #FFF;
	}
	ul.topnav li a {
		line-height: 100%;
		font-size: 13px;
		padding: 10px 5px 10px 22px;
		color: #000;
		display: block;
		text-decoration: none;
		font-weight: bolder;
	}
	ul.topnav li a:hover {
		background-color:#675C7C;
		color: white;
	}
	ul.topnav ul {
		margin: 0;
		padding: 0;
		display: none;
	}
	ul.topnav ul li {
		margin: 0;
		padding: 0;
		clear: both;
	}
	ul.topnav ul li a {
		padding-left: 30px;
		font-size: 13px;
		font-weight: normal;
		outline:0;
		background-color: #E3E3E3;
	}
	ul.topnav ul li a:hover {
		background-color:#D3C99C;
		color:#675C7C;
	}
	ul.topnav ul ul li a {
		color: #000;
		padding-left: 38px;
	}
	ul.topnav ul ul li a:hover {
		background-color:#D3CEB8;
		color:#675C7C;
	}
	ul.topnav span{
		float:right;
	}
	.menuLvl0{
		vertical-align: middle;
		background: url(<%=request.getContextPath()%>/resources/img/menu_lv0.png) #B3DFDA no-repeat 7px 9px;
	}
	.menuLvl1{
		vertical-align: middle;
		background: url(<%=request.getContextPath()%>/resources/img/menu_lv1.png) #E1ECE8 no-repeat 15px 8px;
	}
	.menuLvl2{
		background: url(<%=request.getContextPath()%>/resources/img/menu_lv2.png) #D3CEB8 no-repeat 23px 9px;
	}
	.menuLvl12{
		background: url(<%=request.getContextPath()%>/resources/img/menu_lv2.png) #D3CEB8 no-repeat 19px 9px;
	}
	.mArrow{
		margin-top: auto;
		margin-bottom: 7px;
		border: none;
	}
	</style>
	<script type="text/javascript">
	var dirtyPages = {};
	var tb;
	$(function(){
		var tb = $("#tb").tabs({
			onSelect: function(title, index){
				if(title){
					var tab = tb.tabs("getTab", title)
					if(tab){
						var ifrs = tab.find("iframe");
						if(ifrs && ifrs.length>0){
							if(dirtyPages[ifrs[0].id]){
								var rfs = ifrs[0].contentWindow.refresh;
								if(rfs){
									rfs();
								}
							}
							dirtyPages[ifrs[0].id] = false;
						}
					}
				}
			}
		});
		<%--菜单--%>
		$(".topnav").accordion({
			accordion:false,
			speed: 300,
			closedSign: "",
			openedSign: ""
			<%--
			,
			closedSign: "<img class=\"mArrow\" src=\"<%=request.getContextPath()%>/resources/img/menu_al.gif\" />",
			openedSign: "<img class=\"mArrow\" src=\"<%=request.getContextPath()%>/resources/img/menu_ad.gif\" />"
			--%>
		});
	});
	function togleDir(node)
	{
		var tt = $("#tt");
		var target = node.target;
		if(!tt.tree("isLeaf", target)){
			tt.tree("toggle", target);
		}
	}
	function addTab(resName, resPath, resId){
		if(!resId){
			resId = (new Date()).getTime();
		}else{
			dirtyPages[resId] = false;
		}
		if(resPath.indexOf("?")==-1){
			resPath = resPath + "?pageId=" + resId;
		}else{
			resPath = resPath + "&pageId=" + resId;
		}
		var tb = $("#tb");
		if (tb.tabs("exists", resName)){
			tb.tabs("select", resName);
			var tab = tb.tabs("getTab", resName);
			if (tab){
				var ifrs = tab.find("iframe");
				if(ifrs && ifrs.length>0){
					ifrs[0].setAttribute("id", resId);
					ifrs[0].src = resPath;
				}
			}
		} else {
			tb.tabs("add",{
				title: resName,
				content: '<div style="height: 100%;overflow: hidden;"><iframe id="' + resId + '" scrolling="auto" frameborder="0" src="' + resPath + '" style="width:100%;height:99.5%;padding:1px;"></iframe></div>',
				closable: true
			});
		}
	}
	function removeAll(){
		var tb = $("#tb");
		var tabs = tb.tabs("tabs");
		if(tabs){
			for(var i=tabs.length-1;i>0;i--){
				tb.tabs("close", i);
			}
		}
	}
	var subWin;
	function changePassword(){
		subWin = $('<div><iframe src="<%=request.getContextPath()%>/sys/changePwd.jsp" style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>').window({
			title: "修改密码",
			width: 580,
			height: 290,
			resizable: false,
			collapsible: false,
			minimizable: false,
			maximizable: false,
			modal: true,
			zIndex: 100,
			closable: true
		});
	}
	</script>
</head>
<body class="easyui-layout">
	<div id="header" data-options="region:'north',border:false">
		<%
		if(org!=null)
		{
			if(StringUtil.isNotEmpty(org.getLogoPath()))
			{
		%>
		<!-- <div class="header1"></div> -->
		<%-- <div id="logo"><img src="<%=request.getContextPath()%>/pub/cf_img.action?pt=<%=org.getLogoPath()%>" title="<%=org.getShowText()%>" style="height: 60px;margin-top:10px;"></div> --%>
		<%
			}else
			{
		%>
		<div id="logo" style="height: 40px;line-height: 40px;font-size: 26px;font-weight: normal;font-family: 'microsoft yahei', 'Times New Roman', '宋体', Times, serif;color: #435527;"><%=org.getShowText()%></div>
		<%
			}
		}else
		{
		%>
		<%-- <div id="logo"><img src="<%=request.getContextPath()%>/resources/img/portal/logo.png" style="height: 40px;"></div> --%>
		<%
		}
		%>
		<div id="toppiz">
			<div id="divUser">欢迎你：<span id="lblBra"><%=user!=null?user.getName():""%></span>
				<a href="<%=request.getContextPath()%>/sys/wel_logout.action<%=org!=null?"?orgId="+org.getShowId():""%>" class="header_link">[退出]</a><span class="ctSplit">|</span><a href="#" onclick="changePassword();return false;" class="header_link">[修改密码]</a>
			</div>
		</div>
	</div>
	<div data-options="region:'west',split:true" style="width: 150px; padding: 0px;padding-top: 1px;">
		<ul class="topnav">
		<%
		int i=0;
		for(Resource res0 : ress[0])
		{
		%>
			<li>
				<a class="menuLvl0" href="#"><%=res0.getName()%></a>
				<ul>
		<%
			List<Resource> children0 = res0.getChildren();
			if(children0!=null&&children0.size()==1)
			{
				List<Resource> children1 = children0.get(0).getChildren();
				if(children1!=null)
				{
					for(Resource res2 : children1)
					{
		%>
					<li <%=i++==0?"class=\"active\"":""%>>
						<a class="menuLvl12" href="#" onclick="addTab('<%=res2.getName()%>', '<%=request.getContextPath()+"/"+res2.getPath()%>', '<%=res2.getId()%>')"><%=res2.getName()%></a>
					</li>
		<%
					}
				}
			}else if(children0!=null&&children0.size()>1)
			{
				for(Resource res1 : children0)
				{
					List<Resource> children1 = res1.getChildren();
					if(children1!=null&&children1.size()>1)
					{
		%>
					<li>
						<a class="menuLvl1" href="#"><%=res1.getName()%></a>
						<ul>
		<%
					for(Resource res2 : children1)
					{
		%>
							<li <%=i++==0?"class=\"active\"":""%>>
								<a class="menuLvl2" href="#" onclick="addTab('<%=res2.getName()%>', '<%=request.getContextPath()+"/"+res2.getPath()%>', '<%=res2.getId()%>')"><%=res2.getName()%></a>
							</li>
		<%
					}
		%>
						</ul>
					</li>
		<%
					}else if(children1!=null&&children1.size()==1)
					{
					Resource res2 = children1.get(0);
		%>
					<li <%=i++==0?"class=\"active\"":""%>>
						<a class="menuLvl12" href="#" onclick="addTab('<%=res2.getName()%>', '<%=request.getContextPath()+"/"+res2.getPath()%>', '<%=res2.getId()%>')"><%=res2.getName()%></a>
					</li>
		<%
					}
				}
			}
		%>
				</ul>
			</li>
		<%
		}
		%>
		</ul>
	</div>
	<div data-options="region:'center',title:''" style="border: none;">
		<div id="tb" class="easyui-tabs" style="width:100%;" data-options="fit:true,tools:'#tab-tools'">
			<div title="首页" style="height: 100%;overflow: hidden;">
				<iframe scrolling="auto" frameborder="0" id="home" src="<%=request.getContextPath()%>/sys/wel_home.action" style="width:100%;height:99.5%;"></iframe>
			</div>
		</div>
	</div>
	<div id="tab-tools" style="display: none;">
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'" onclick="removeAll()"></a>
	</div>
</body>
</html>