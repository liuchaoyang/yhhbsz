<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.module.sys.bean.ClientVersion"%>
<%@ page import="com.yzxt.yh.module.sys.bean.Org"%>
<%
@SuppressWarnings("unchecked")
List<ClientVersion> clientVersions = (List<ClientVersion>)request.getAttribute("clientVersions");
Org org = (Org)request.getAttribute("org");
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>医疗健康平台客户端</title>
	<style type="text/css">
	.top{width: 60%;min-width: 800px;margin-left: auto;margin-right: auto;margin-top: 20px;}
	.top-left{float: left;}
	.logoImg{height: 40px;max-width: 500px;overflow: hidden;}
	.logoText{max-width: 500px;height: 40px;line-height: 40px;font-size: 26px;font-weight: normal;font-family: 'microsoft yahei', 'Times New Roman', '宋体', Times, serif;color: #435527;}
	.colline{width: 2px;height: 40px;background: #E7E7E7;float:left;margin-left:5px;}
	.itemName{font-size: 22px;color: #666;font-family: 黑体;line-height: 40px;height: 40px;width: 120px;float:left;margin-left: 5px;}
	.top-right{float: right;height: 40px;}
	.top-right span{line-height: 40px;font-size: 12px;}
	.titleSplit{clear: both;margin-top: 10px;margin-bottom: 10px;margin-left: auto;margin-right: auto;width: 60%;min-width: 800px;height: 1px;background-color: #CCC;}
	<%--版本样式--%>
	.wrap{
		width: 60%;
		min-width: 800px;
		margin-top: 10px;
		margin-right: auto;
		margin-left: auto;
	}
	.ac{
		margin-top: 30px;
		margin-bottom: 30px;
		width: 100%;
	}
	.clientDesc{
		font-size: 16px;
		text-indent: 2pc;
		letter-spacing: 2px;
		line-height: 150%;
	}
	.linkCls{
		text-decoration: underline;
	}
	</style>
	<script type="text/javascript">
	</script>
</head>
<body>
	<div class="top">
		<div class="top-left">
		<%
		if(org!=null)
		{
			if(StringUtil.isNotEmpty(org.getLogoPath()))
			{
		%>
			<img class="logoImg" src="<%=request.getContextPath()%>/pub/cf_img.action?pt=<%=org.getLogoPath()%>" />
		<%
			}else
			{
		%>
			<span class="logoText"><%=org.getShowText()%></span>
		<%
			}
		}else
		{
		%>
			<%-- <img class="logoImg" src="<%=request.getContextPath()%>/resources/img/portal/logo.png" /> --%>
		<%
		}
		%>
		</div>
		<div class="top-left colline"></div>
		<div class="top-left itemName">
			<span>客户端下载</span>
		</div>
		<div class="top-right">
			<span>已有账号?&nbsp;去<a href="<%=request.getContextPath()%>/sys/wel_login.action<%=org!=null?"?orgId="+org.getShowId():""%>">登录</a></span>
		</div>
	</div>
	<div class="titleSplit"></div>
  	<div class="wrap">
  		<%
  		for(ClientVersion clientVersion : clientVersions)
  		{
  		%>
  		<table class="ac">
  			<tr>
  				<td style="font-size: 28px;padding: 10px;" colspan="2"><%=clientVersion.getName()%>&nbsp;&nbsp;v<%=clientVersion.getVersion()!=null?clientVersion.getVersion():""%></td>
  			</tr>
  			<tr>
  				<td style="width: 300px;">
  					<img alt="" src="<%=request.getContextPath()%>/common/clientImg/<%=clientVersion.getShowImg()%>" style="width: 296px;height: 400px;" />
  				</td>
  				<td style="vertical-align: top;">
  					<p class="clientDesc">
  						<%=clientVersion.getRemark()!=null?clientVersion.getRemark():""%>
  					</p>
  					<p style="margin-top: 20px;">
  						电脑下载：
  						&nbsp;&nbsp;
  						<a href="<%=clientVersion.getPath()%>" class="easyui-linkbutton" style="font-size: 14px;" target="hdIfr">立即下载</a>
  					</p>
  					<p style="margin-top: 20px;">
  						手机下载：
  					</p>
  					<div style="padding-top: 0px;text-align: center;">
  						<img alt="" src="<%=request.getContextPath()%>/common/clientImg/<%=clientVersion.getQrCodeName()%>">
  					</div>
  				</td>
  			</tr>
  		</table>
  		<%
  		}
  		%>
  	</div>
  	<iframe name="hdIfr" style="display: none;"></iframe>
</body>
</html>
