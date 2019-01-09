<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../common/inc.jsp"%>
<title>医生首页</title>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/easyui/jquery.portal.js"></script>
	<style type="text/css">
	body{
		min-width: 500px;
	}
	.ifr{
		width: 100%;
		height: 100%;
		overflow: hidden;
		margin: 0px;
		padding: 0px;"
	}
	</style>
	<script type="text/javascript">
		$(function() {
			$("#pp").portal({
				width: $(document).width()-25,
				height: "auto",
				border: false
			});
			var p1 = $('<iframe src="<%=request.getContextPath()%>/chk/docHomeWarns.jsp" frameborder="0" scrollbar="no" class="ifr"></iframe>').appendTo('body');
			p1.panel({
				title: "预警会员列表",
				height: 330,
				closable: true,
				collapsible: true
			});
			$("#pp").portal("add", {
				panel: p1,
				columnIndex: 0
			});
			var p2 = $('<iframe src="<%=request.getContextPath()%>/msg/docHomeConsults.jsp" frameborder="0" scrollbar="no" class="ifr"></iframe>').appendTo('body');
			p2.panel({
				title: "会员咨询列表",
				height: 330,
				closable: true,
				collapsible: true
			});
			$("#pp").portal("add", {
				panel: p2,
				columnIndex: 1
			});
			var p3 = $('<iframe src="<%=request.getContextPath()%>/chr/docHomeVisits.jsp" frameborder="0" scrollbar="no" class="ifr"></iframe>').appendTo('body');
			p3.panel({
				title: "随访列表",
				height: 330,
				closable: true,
				collapsible: true
			});
			$("#pp").portal("add", {
				panel: p3,
				columnIndex: 0
			});
			var p4 = $('<iframe src="<%=request.getContextPath()%>/msg/docHomeAsks.jsp" frameborder="0" scrollbar="no" class="ifr"></iframe>').appendTo('body');
			p4.panel({
				title: "健康问答列表",
				height: 330,
				closable: true,
				collapsible: true
			});
			$("#pp").portal("add", {
				panel: p4,
				columnIndex: 1
			});
		});
		var subWin;
		function showWin(title, url, width, height){
			subWin =$('<div><iframe src="' + url + '" style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>').window({
				title: "咨询指导",
				width: width,
				height: height,
				resizable: false,
				collapsible: false,
				minimizable: false,
				maximizable: false,
				modal: true,
				zIndex: 100,
				closable: true
			});
		}
		function refreshGrid(){
			refresh()
		}
		function refresh(){
			location.reload();
		}
	</script>
</head>
<body>
	<div id="pp">
		<div style="width: 45%;"></div>
		<div style="width: 45%;"></div>
	</div>
</body>
</html>