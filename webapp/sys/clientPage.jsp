<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.yzxt.yh.module.sys.bean.ClientVersion"%>
<%
@SuppressWarnings("unchecked")
List<ClientVersion> clientVersions = (List<ClientVersion>)request.getAttribute("clientVersions");
ClientVersion	clientVersion = clientVersions.get(0);
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>医疗健康平台客户端</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/css/flow.css" />
	<style type="text/css">
	.top{width: 1000px;height: 120px;margin-left: auto;margin-right: auto;border-bottom:#CCC 1px solid;}
	.top-left{float: left;width: 850px;}
	.top-left img{float: left;}
	.colline{width: 2px;margin-top: 40px;height: 50px;background: #E7E7E7;float:left;margin-left:10px;}
	.top-left span{font-size: 24px;color: #666;font-family: 黑体;height: 55px;margin-top: 50px;width: 120px;float:left;margin-left: 10px;}
	.top-right{float: right;height: 80px;margin-top: 30px;}
	.top-right span{line-height: 130px;font-size: 12px;}
	<%--版本样式--%>
	.wrap{
		width: 800px;
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
<body style="width:100%;height:100%;">
	<div>
		<table style="width:100%;height:100%;">
			<tr style="width:100%;height:100%;">
  				<td >
					<p  style="font-size:36px;">
  						<%=clientVersion.getRemark()!=null?clientVersion.getRemark():""%>&nbsp;&nbsp;v<%=clientVersion.getVersion()!=null?clientVersion.getVersion():""%>
  					</p>
  					<p style="font-size:36px;width:100%;height:30px;">
  						&nbsp;&nbsp;<a href="<%=clientVersion.getPath()%>" style="font-size: 36px;width:160px;" target="hdIfr">点击下载</a>
  					</p>
  					<p style="font-size:36px;">
  						如果没有响应，请选择右上角按钮，选择“在浏览器中打开”
  					</p>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>
