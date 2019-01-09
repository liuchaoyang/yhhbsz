<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../common/inc.jsp"%>

<!DOCTYPE HTML>
<html>
<head>
<title>区域建档人数</title>
<meta charset="UTF-8">
<meta name="pragma" content="no-cache" />
<meta name="cache-control" content="no-cache" />
<meta name="expires" content="0" />
<meta name="keywords" content="keyword1,keyword2,keyword3" />
<meta name="description" content="区域建档人数" />
<script type="text/javascript" src="<%=request.getContextPath()%>/stat/area.linkage.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/highcharts.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/modules/exporting.js"></script>

    <script type="text/javascript">
    
    	var barDatas;
    
		$(function(){
			initData();
			 $('#container').highcharts({
		            chart: {
		                type: 'bar'
		            },
		            title: {
		                text: '地区已建档人数统计汇总'
		            },
		            xAxis: {
		                categories: barDatas.area,
		                title: {
		                    text: null
		                }
		            },
		            yAxis: {
		                min: 0,
		                allowDecimals: false,
		                title: {
		                    text: '人数',
		                    align: 'high'
		                },
		                labels: {
		                    overflow: 'justify'
		                }
		            },
		            tooltip: {
		                valueSuffix: ' 人'
		            },
		            plotOptions: {
		                bar: {
		                    dataLabels: {
		                        enabled: true
		                    }
		                },
		                series: {
		                	pointPadding:0.3
		                }
		            },
		            exporting:{
                        // 是否允许导出
                        enabled:false
                    },
                    legend: {
        				enabled: false
                	},
		            credits: {
		                enabled: false
		            },
		            series: [{
		                name: '区域建档人数',
		                data: barDatas.number
		            }]
		        });
			 
			 
				$('#doctor_info').datagrid({
					title : '责任医生建档数列表',
					loadMsg : "数据加载中,请稍候...",
					width : '760',
					height : 460,
					animate:true,
					striped : true,
					url:'<%=request.getContextPath()%>/stat/stat_getDoctorList.action',
					pagination : true,
					idField : 'id',
					rownumbers : true,
					fitColumns : true,
					singleSelect:true,
					columns : [ [ {
								field : 'userName',
								title : '姓名',
								width : 100,
								align : 'center'
							}, {
								field : 'deptName',
								title : '所属部门名称',
								width : 120,
								align : 'center'
							}, {
								field : 'number',
								title : '建档数',
								width : 100,
								align : 'center'
							}] ],
							toolbar : '#dlg-toolbar'
						});
				
				var p = $('#doctor_info').datagrid('getPager');
				if (p) {
					$(p).pagination({
						pageNumber : 1,
						pageSize : 10,
						showPageList:false
					});
				}
				
			});
		
		
		
		
		function initData(){
			
			var urls = '<%=request.getContextPath()%>/stat/stat_getCasecadeJDN.action';
        	var jsonData = ajaxRequest(urls,null);
        	barDatas = $.parseJSON(jsonData);
			
		}
		
		
		function getSubArea(){
			
			var level = "";
			
			var provinceId = $("#provinceId").val();
			var cityId = $("#cityId").val();
			var countyId = $("#countyId").val();
			var townId = $("#townId").val();
			
			var parentId;

			if(townId!=null && townId.length>0){
					parentId = townId;
					level = "4";
			}else if(countyId != null && countyId.length>0){
					parentId = countyId;
					level = "3";
			}else if(cityId != null && cityId.length>0){
					parentId = cityId;
					level = "2";
			}else{
				parentId = provinceId;
				level = "1";
			}
			
// 			alert("parentId:   "+parentId+"level:   "+level);
			
			var urls = '<%=request.getContextPath()%>/stat/stat_getCasecadeJDN.action';
    		var data = {areaId:parentId,level:level};
        	var jsonData = ajaxRequest(urls,data);
        	var barJson = eval("("+jsonData+")");
        	
        	var mychart = $('#container').highcharts();
        	var series=mychart.series;                  
            while(series.length > 0) {  
                series[0].remove(false);  
            }  
            mychart.addSeries({
            			name: '区域建档人数',
               			data: barJson.number
    	            }, false);
            
            mychart.xAxis[0].setCategories(barJson.area);
            mychart.redraw();
			
		}
		
		function doSearch(value){
			$('#doctor_info').datagrid('getPager').pagination({pageNumber:1});
			var urls = '<%=request.getContextPath()%>/stat/stat_getDoctorList.action';
			var option=$('#doctor_info').datagrid('options');
			option.pageNumber=1;
			option.url=urls;
			var queryParams = option.queryParams;
			queryParams.keywords = value;
			$('#doctor_info').datagrid('reload');
			$('#doctor_info').datagrid('unselectAll');
			$('#doctor_info').datagrid('clearSelections');
		}
		
	</script>
</head>
<body>

	
	
	

		<div id="tb" style="min-width: 310px; width:800px; margin: 5px auto;" >
				<input id="provinceId" type="hidden"/>	
				<input id="cityId" type="hidden"/>	
				<input id="countyId" type="hidden"/>	
				<input id="townId" type="hidden"/>
				<span>请选择:</span>省:
				<input id="province" name="province" class="easyui-combobox"
					style="width: 150px;" />
					
				市:
				<input id="city" name="city" class="easyui-combobox"
					style="width: 150px;" />
				区县:
				<input id="county" name="county" class="easyui-combobox"
					style="width: 150px;" />
				乡镇:
				<input id="town" name="town" class="easyui-combobox"
					style="width: 150px;" />
		</div>
	
	<div id="container" style="min-width: 310px; width:850px; height: 600px; margin: 0 auto"></div>
	
	<div id="dlg-toolbar" style="padding:2px 0">
		<table cellpadding="0" cellspacing="0" style="width:100%">
			<tr>
				<td  style="text-align:right;padding-right:2px">
					<input class="easyui-searchbox" data-options="prompt:'请输入医生姓名.',searcher:doSearch" style="width: 300px;"></input>	 
				</td>
			</tr>
		</table>
	</div>
	
	<div style="min-width: 310px; width:800px; margin: 10px auto;">
			<table id="doctor_info"></table>
	</div>
	
	
	
</body>
</html>