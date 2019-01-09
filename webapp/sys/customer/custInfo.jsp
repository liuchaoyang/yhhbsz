<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.module.sys.bean.User"%>
<%@ page import="com.yzxt.yh.module.sys.bean.Customer"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.util.DateUtil"%>
<%
Customer cust= (Customer)request.getAttribute("cust");
User user = cust.getUser();
String operType = request.getParameter("operType");
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="pragma" content="no-cache" />
	<meta name="cache-control" content="no-cache" />
	<meta name="expires" content="0" />
	<title><%=StringUtil.ensureStringNotNull(user.getName())%>的用户资料</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/easyui/themes/default/easyui.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/easyui/themes/icon.css" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery-1.8.0.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/easyui/locale/easyui-lang-zh_CN.js"></script>
	<style type="text/css">
	#header{
		height: 34px;
		background: #B3DFDA;
		padding: 5px 10px 5px 10px;
		overflow: hidden;
	}
	#divUser{
		color: #000000;
	}
	.header_link{
		text-decoration: none;
		color: #0066CC;
	}
	#logo{
		float: left;
	}
	#toppiz{
		float: right;
	}
	.menuPage{
		text-decoration: none;
		color: black;
	}
	</style>
	<script type="text/javascript">
	$(function(){
		$("#tb").tabs({
			onSelect:function(title, index){
				if(index == 1)
				{
					document.getElementById("jkda").src = "<%=request.getContextPath()%>/ach/ach_toDetail.action?operType=view&custId=<%=user.getId()%>";
				}else if(index == 2)
				{
					document.getElementById("jcjl").src = "<%=request.getContextPath()%>/chk/checkData_custCheckDetail.action?custId=<%=user.getId()%>";
				}else if(index == 3)
				{
					document.getElementById("jktj").src = "<%=request.getContextPath()%>/chk/exam_toCustExams.action?custId=<%=user.getId()%>";
				}else if(index == 4)
				{
					document.getElementById("blj").src = "<%=request.getContextPath()%>/ach/dossier_toMyDossier.action?custId=<%=user.getId()%>";
				}else if(index == 5)
				{
					document.getElementById("jkbg").src = "<%=request.getContextPath()%>/rpt/anaRpt_toCustRpt.action?custId=<%=user.getId()%>";
				}else if(index == 6)
				{
					document.getElementById("sfjl").src = "<%=request.getContextPath()%>/chr/chrVisit_toMyVisit.action?custId=<%=user.getId()%>";
				}else if(index == 7)
				{
					document.getElementById("jkzd").src = "<%=request.getContextPath()%>/msg/heaGuide_toCustGuide.action?custId=<%=user.getId()%>";
				}else if(index == 8)
				{
					document.getElementById("jkyj").src = "<%=request.getContextPath()%>/chk/checkWarn_toDetailWarn.action?id=<%=user.getId()%>";
				}
			}
		});
	});
	function addTab(resName, resPath, resId){
		var tb = $("#tb");
		if (tb.tabs("exists", resName)){
			tb.tabs("select", resName);
			var tab = tb.tabs("getTab", resName);
			if (tab){
				var ifrs = tab.find("iframe");
				if(ifrs && ifrs.length>0){
					ifrs[0].src = resPath;
				}
			}
		} else {
			tb.tabs("add",{
				title: resName,
				content: '<div style="height: 100%;overflow: hidden;"><iframe scrolling="auto" frameborder="0" src="' + resPath + '" style="width:100%;height:99.5%;padding:1px;"></iframe></div>',
				closable: true
			});
		}
	}
	</script>
</head>
<body>
		<div id="tb" class="easyui-tabs" style="width:810px;" data-options="fit:true,tools:'#tab-tools'">
			<div title="健康状态" style="height: 100%;overflow: hidden;">
				<iframe scrolling="auto" frameborder="0" src="<%=request.getContextPath()%>/chk/phyIdx_showPhyIdx.action?custId=<%=user.getId()%>" style="width:100%;height:99.5%;padding:1px;"></iframe>
			</div>
			<div title="健康档案" style="height: 100%;overflow: hidden;">
				<iframe id="jkda" scrolling="auto" frameborder="0" src="" style="width:100%;height:99.5%;padding:1px;"></iframe>
			</div>
			<div title="检测记录" style="padding:10px"  >
				<iframe id="jcjl"  src="" scrolling="no" frameborder="0" style="border: none;" width="100%" height="780"  class="myife"></iframe>
			</div>
			<div title="健康体检" style="padding:10px">
				<iframe id="jktj" src="" scrolling="no" frameborder="0" style="border: none;" width="100%" height="600" class="myife"></iframe>
			</div>
			<div title="病历夹" style="padding:10px">
				<iframe id="blj" src="" scrolling="no" frameborder="0" style="border: none;" width="100%" height="600" class="myife"></iframe>
			</div>
			<div title="健康报告" style="padding:10px">
				<iframe id="jkbg" src="" scrolling="no" frameborder="0" style="border: none;" width="100%" height="1000" class="myife"></iframe>
			</div>
			<div title="随访记录" style="padding:10px">
				<iframe id="sfjl" src="" scrolling="no" frameborder="0" width="100%" height="500" class="myife"></iframe>
			</div>
			<div title="健康指导" style="padding:10px">
				<iframe id="jkzd" src="" scrolling="no" frameborder="0" width="100%" height="500" class="myife"></iframe>
			</div>
			<div title="健康预警" style="padding:10px">
				<iframe id="jkyj" src="" scrolling="no" frameborder="0" width="100%" height="500" class="myife"></iframe>
			</div>
		</div>
</body>
</html>