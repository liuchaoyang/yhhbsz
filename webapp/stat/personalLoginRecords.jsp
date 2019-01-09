<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import ="java.util.Date"%>
<%@ page import ="com.yzxt.yh.util.StringUtil"%>
<%@ page import ="com.yzxt.yh.util.DateUtil"%>
<%
	String userId = (String)request.getParameter("userId");
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>一体机使用记录</title>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/highcharts.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/modules/exporting.js"></script>
	<style type="text/css">
	</style>
	<script type="text/javascript">
	<%-- $(function(){
	    var x = [];//X轴
	    var y = [];//Y轴
	    var xtext = [];//X轴TEXT
	    var color = ["gray","pink","red","blue","yellow","green","#fff"];
	    $.ajax({
	        type:'get',
	        url:'<%=request.getContextPath()%>/stat/stat_queryUserNum.action',//请求数据的地址
	        success:function(data){
	            var json = eval("("+data+")");
	            var s = 1;
	            for(var key in json.list){
	                json.list[key].y = json.list[key].age; //给Y轴赋值
	                xtext = json.list[key].name;//给X轴TEXT赋值
	                json.list[key].color= color[key];
	            }
	                chart.series[0].setData(json.list);//数据填充到highcharts上面
	        },
	        error:function(e){
	        } 
	    });
	    var chart = new Highcharts.Chart({
	        chart:{
	            renderTo:'container',
	            type:'column' //显示类型 柱形
	        },
	        title:{
	            text:'年龄分布图' //图表的标题
	        },
	        xAxis:{
	            categories:xtext
	        },
	        yAxis:{
	            title:{
	                text:'年龄' //Y轴的名称
	            },
	        },
	        series:[{
	            name:"姓名"
	        }]
	    });
	}); --%>
	
	var recordGrid;
	$(function(){
		recordGrid = $("#records").datagrid({
			title: "使用记录列表",
			width: "auto",
			height: "auto",
			idField: "id",
			rownumbers: true,
			pagination: true,
			pageList: [10, 20, 50, 100],
			singleSelect: true,
			nowrap: true,
			striped: true,
			fitColumns: true,
			loadMsg: "数据加载中,请稍候...",
			toolbar: "#toolbar",
			url: "<%=request.getContextPath()%>/stat/stat_getDetail.action?userId=<%=userId%>",
			columns: [[{
				field: "createTime",
				title: "活跃时间",
				width: 80,
				align: "center",formatter:function(v){
					var str = "";
					if(v!=null)
					{
						str= v.substring(0,10);
					}
					return str;
				}
			},{
				field: "type",
				title: "登录方式",
				width: 60,
				align: "center",formatter:function(v){
					var str = "";
					if(v==1){
						str="平台登录";
					}else if(v==2){
						str="客户端登录";
					}else if(v==3){
						str="一体机登录";
					}else {
						str="未知登录";
					}
					return str;
				}
			},{
				field: "c",
				title: "活跃次数",
				width: 60,
				align: "center"
			}]]
		});
	});
	
	function gotoHighchart(){
        var url = '<%=request.getContextPath()%>/stat/stat_queryUserNum.action';
        window.location.href=url;
    }
	function refreshGrid(){
		recordGrid.datagrid("reload");
		recordGrid.datagrid("unselectAll");
		recordGrid.datagrid("clearSelections");
	}
	function query(){
		recordGrid.datagrid("getPager").pagination({pageNumber: 1});
		$.extend(recordGrid.datagrid("options"), {
			pageNumber: 1,
			queryParams: $("#filterForm").serializeObject()
		});
		refreshGrid();
	}
	function clear(){
		document.getElementById("filterForm").reset();
		refreshGrid();
	}
	
	
</script>
</head>
<body>
	<div class="sectionTitle">查询条件</div>
	<div>
		<form id="filterForm">
			<table class="table">
				<%-- <tr style="height: 25px;">
					<td width="20%" class="td_title">一体机编号:</td>
					<td colspan="3">
						<%=StringUtil.ensureStringNotNull(sn)%>
					</td>
				</tr> --%>
				<tr>
					<td width="20%" class="td_title">使用日期:</td>
					<td width="30%">
						<input type="text" readonly="readonly" class="Wdate" style="width: 90px;" id="startDate" name="startDate" value="" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}'})" />
						至
						<input type="text" readonly="readonly" class="Wdate" style="width: 90px;"id="endDate" name="endDate" value="" onclick="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}'})" />
					</td>
					<td colspan="2" align="right" style="padding-right: 20px;">
						<a class="easyui-linkbutton" href="javascript:query()">查询</a>
						&nbsp;
						<a class="easyui-linkbutton" href="javascript:clear()">重置</a>
						<!-- <a href="javascript:gotoHighchart()"  class="easyui-linkbutton" >查看图表</a> -->
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div style="padding-top: 10px;">
		<table id="records" class="easyui-datagrid"></table>
	</div>
	<div id="container" style="width: 800px; height: 400px; margin: 0 auto"></div>
</body>
</html>