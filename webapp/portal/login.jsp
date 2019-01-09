<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.module.sys.bean.Org"%>
<%
	Org org = (Org)request.getAttribute("org");
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>医疗健康平台</title>
    <meta content="致力于为社区养老，家庭护理和心脑血管健康维护提供高效、可靠的基础设施。由自主研发的“个人健康云平台”和基于其上的“健康管理系统”，为个人和专业服务机构提供各种健康维护解决方案。" name="description">
	<meta content="健康小屋,社区养老,健康云,个人健康云平台,健康管理系统" name="keyword">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/css/portal/login.css">
	<style type="text/css">
		.login_main{ clear:both; width:100%; height:520px; }
		.footerLink .link_disabled{color: #AAA;}
	</style>
	<script type="text/javascript">
	function init()
	{
		if (window != top)
		{
			top.location.href = location.href;
			return;
		}
		var h=window.innerHeight;
		var div1=document.getElementById("header");
		var h1=div1.offsetHeight;
		 h=h-document.getElementById("header").offsetHeight-document.getElementById("footer").offsetHeight-10;
		if(h<500){
			h=500;
		}
		document.getElementById("login_main").style.height=h+"px";
		<%--如果不是通过登录页面提交出错后返回到登录页面，才需要处理Cookie--%>
		<%
		if(!"1".equals(request.getAttribute("formSubFlag")))
		{
		%>
		var userCookie = readCookie("healthyUser");
		if(userCookie)
		{
			var infos = userCookie.split(",");
			document.getElementById("account").value = infos[0];
			document.getElementById("password").value = infos[1];
		}
		<%
		}
		%>
	}
	function login(){
		var isCookie = document.getElementById("atulog").checked;
		if(isCookie)
		{
			var accountVal = document.getElementById("account").value;
			var passwordVal = document.getElementById("password").value;
			var val = accountVal + "," + passwordVal;
			createCookie("healthyUser", val, 7);
		}
		document.logfrom.submit();
	}
	
	function createCookie(name, value, days)
	{
		if(days)
		{
			var date = new Date();
			date.setTime(date.getTime() + (days*24*60*60*1000));
			var expires = "; expires=" + date.toGMTString();
		}
		else
		{
			var expires = "";
		}
		document.cookie = name + "=" + value + expires + "; path=/";
	}
		
	function readCookie(name)
	{
		var nameEQ = name + "=";
		var ca = document.cookie.split(';');
		for(var i=0;i < ca.length;i++)
		{
			var c = ca[i];
			while (c.charAt(0)==' ') c = c.substring(1,c.length);
			if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
		}
		return null;
	}
	document.onkeydown = function (e) {
		var theEvent = e || window.event;
		var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
		if (code == 13) {
			login();
		}
	}
	function showHelps(){
		location.href = "<%=request.getContextPath()%>/pub/help_list.action<%=org!=null?"?orgId="+org.getShowId():""%>";
	}
	function showClients(){
		location.href = "<%=request.getContextPath()%>/pub/cv_dlList.action<%=org!=null?"?orgId="+org.getShowId():""%>";
	}
	function register(){
		location.href = "<%=request.getContextPath()%>/pwd/register_to.action<%=org!=null?("?orgId="+(org.getShowId())):""%>";
	}
	function fgtPwd(){
		location.href = "<%=request.getContextPath()%>/pwd/register_toResetPwd.action<%=org!=null?"?orgId="+org.getShowId():""%>";
	}
	</script>
</head>
<body onload="init();" style="background:#E5F5F1;">
	<div class="container">
	<%
	if(org!=null)
	{
		if(StringUtil.isNotEmpty(org.getLogoPath()))
		{
	%>
		<div id="header" class="header">
			<div class="header1"></div>
			<%-- <div class="wrapper" >
				<div class="header_logo fl"><img src="<%=request.getContextPath()%>/pub/cf_img.action?pt=<%=org.getLogoPath()%>" title="<%=org.getShowText()%>" style="height: 40px;"></div>
			</div> --%>
		</div>
	<%
		}else
		{
	%>
		<div id="header" class="header">
			<div class="wrapper" style="width: 900px;height: 40px;line-height: 40px;font-size: 26px;font-weight: normal;font-family: 'microsoft yahei', 'Times New Roman', '宋体', Times, serif;color: #435527;">
				<%=org.getShowText()%>
			</div>
		</div>
	<%
		}
	}else
	{
	%>
		<%-- <div id="header" class="header">
			<div class="wrapper" >
				<div class="header_logo fl"><img src="<%=request.getContextPath()%>/resources/img/portal/logo.png" style="height: 40px;"></div>
			</div>
		</div> --%>
	<%
	}
	%>
        <!--头部结束-->
        <!--中间主要内容-->
        <div id="login_main" class="login_main">
        	<div class="wrapper login_bg" >
            	<div class="login_box">
                	<div class="login_header">
                    	<h2>用户登录</h2>
                    </div>
                    <div class="login_form">
                    	<form id="logfrom" name="logfrom" method="post" action="<%=request.getContextPath()%>/sys/wel_login.action<%=org!=null?"?orgId="+org.getShowId():""%>">
                    		<span style="padding-left: 30px;color: red"><%=StringUtil.ensureStringNotNull((String)request.getAttribute("errorInfo"))%></span>
                    		<input type="hidden" name="formSubFlag" value="1" />
                    		<input type="hidden" name="orgId" value="<%=org!=null?org.getId():""%>" />
                        	<div class="login">
                            	<div class="login_icon fl"><img src="<%=request.getContextPath()%>/resources/img/portal/user.png"></div>
                                <input type="text" maxlength="30" id="account" name="account" value="<%=StringUtil.ensureStringNotNull((String)request.getAttribute("account"))%>" class="loginInp" placeholder="用户名" />
                            </div>
                            
                            <div class="login">
                            	<div class="login_icon fl"><img src="<%=request.getContextPath()%>/resources/img/portal/lock.png"></div>
                                <input type="password" id="password" name="password"  class="loginInp" placeholder="密码">
                            </div>
                                                     
                            <div  class="logincheck">
                            	<div class="loginAuto fl">
                                	<input class="remAutoLogin" id="atulog" type="checkbox" name="atulog" title="下次自动登录">
                                    <span>是否保存密码？7天内有效.</span>
                                </div>
<!--                                 <div class="loginforget fr"><a href="#">忘记密码？</a></div> -->
                            </div>
                            
                            <div class="loginFormBtn" style="text-align: center;">
                            	<a href="javascript:login()" class="loginBtn">登录</a>
                            </div>
                             <div style="text-align: center;">
                            	<a href="javascript:register()" style="color:rgb(39,158,248);" class="">注册</a>
                            	&nbsp;&nbsp;
                            	<a href="javascript:fgtPwd()" style="color:rgb(39,158,248);" class="">忘记密码</a>
                            </div>
                        </form>
                    </div>
                    
                </div>
            </div>
        </div>
        <!--中间主要内容结束-->
        
        <!--底部-->
        <div id="footer" class="footer">
        	<div class="wrapper">
            	<div class="footerLink">
                    <a class="link_disabled" href="http://www.myyy7.com">联系我们</a>
                    <!-- <a class="link_disabled" href="#">合作洽谈</a>
                    <a href="#" onclick="showHelps();">帮助中心</a>
                    <a class="link_disabled" href="#">意见反馈</a>
                    <a href="#" onclick="showClients();">下载客户端</a> -->
                </div>
                <p class="copyright" style="display: none;">北京摩云圣邦科技有限责任公司</p>
            </div>
        </div>
        <!--底部结束-->
        
    </div>	
	
</body>
</html>