<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.GregorianCalendar"%>
<%
String id = (String)request.getAttribute("id");
%>
<!DOCTYPE html>
<html>
<head>
	<%
  //设置每秒刷新一次
  response.setIntHeader("Refresh", 20);
  //获取当前时间
  Calendar calendar = new GregorianCalendar();
  String am_pm;
  int hour = calendar.get(Calendar.HOUR);
  int minute = calendar.get(Calendar.MINUTE);
  int second = calendar.get(Calendar.SECOND);
  
  if(calendar.get(Calendar.AM_PM) == 0)
  	am_pm = "上午";
  	else
  	am_pm = "下午";
  	String time = am_pm+"," +hour+":"+minute+":"+second;
  	out.print("当前时间："+time);
   %>
	<%@ include file="../common/inc.jsp"%>
	<title>会员咨询列表</title>
	<style type="text/css">
	body{
		padding: 0px;
		margin: 0px;
	}
	.panel-body{
		border: none;
		border-bottom: 1px solid #95B8E7;
	}
	#moreRecords{
		padding: 5px;
		text-align: right;
	}
	</style>
	<script type="text/javascript">
	var consultGrid;
	$(function(){
		consultGrid = $("#consults").datagrid({
			title: "",
			width: "auto",
			height: "auto",
			idField: "id",
			url: "<%=request.getContextPath()%>/msg/ask_queryDocHomeAsk.action?rows=10&page=1",
			rownumbers: true,
			pagination: false,
			singleSelect: true,
			nowrap: true,
			striped: true,
			fitColumns: true,
			loadMsg: "数据加载中,请稍候...",
			columns: [[{
				field: "createByName",
				title: "会员名称",
				width: 60,
				align: "center"
			}, {
				field: "title",
				title: "内容",
				width: 200,
				align: "center"
			}, /* {
				field: "summary",
				title: "内容",
				width: 200,
				align: "left",
			},  */{
				field: "createTime",
				title: "提问时间",
				width: 80,
				align: "center",
				formatter: function(val, row){
					if(val){
						return val.substring(0, 10);
					}else{
						return "";
					}
					return "";
				}
			}, {
				field: "oper",
				title: " ",
				width: 70,
				align: "center",
				formatter: function(val, row){
					return '<a href="#" onclick="handle(\'' + row.id + '\')">回复</a>';
					debugger;
				}
			}]],
			onLoadSuccess: function(data){
				if(data && data.total > 10){
					document.getElementById("moreRecords").style.display = "block";
				}else{
					document.getElementById("moreRecords").style.display = "none";
				}
			}
		});
	});
	function handle(id){
		top.addTab("健康问答信息", "<%=request.getContextPath()%>/msg/ask_view.action?needReply=Y&id="+id, null);
	}
	function more(){
		top.addTab("健康问答", "<%=request.getContextPath()%>/msg/ask_listAsks.action?nr=y", null);
	}
	</script>
</head>
<body>
	<table id="consults"></table>
	<div id="moreRecords" style="display: none;">
		<a href="#" onclick="more()">更多</a>
	</div>
</body>
</html>