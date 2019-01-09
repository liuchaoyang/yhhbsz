<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import ="java.util.Date"%>
<%@ page import ="com.yzxt.yh.util.DateUtil"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>健康数据</title>
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=8mOsVlOxjtyihXjNlFNRxEkdHpFVlUTr"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/common.js"></script>
	<style type="text/css">
	form{
		margin: 0px;
		padding: 0px;
	}
	.table_cell{
		width:50%;
		height:40px;
		font-size: 15px;
	}
	.tr{
		width:50%;
		height:40px;
		font-size: 15px;
		text-align: right;
	}
	</style>
	<script type="text/javascript">
		$(function(){
			map = new BMap.Map("graphic_table");
			map.centerAndZoom(new BMap.Point(116.404, 39.915), 15);  // 初始化地图,设置中心点坐标和地图级别
			map.setCurrentCity("北京");
			map.addControl(new BMap.NavigationControl());   
			var lookBlogjson1="custId="+"${custId}";
			
			requestServer("<%=request.getContextPath()%>/wab/wabData_querytrail.action", "json", lookBlogjson1,function(data){
				if(data.data!=null&&data.data.length>0){
					
					var points=data.data;
					var point1=new Array();
					for(var i=0;i<points.length;i++){
						var poi=new BMap.Point(points[i][0],points[i][1]);
						point1[i]=(poi);
					} 
					map.centerAndZoom(new BMap.Point(points[0][0],points[0][1]), 15);       // 初始化地图，设置中心点坐标和地图级别     

					 addStart(new BMap.Point(points[0][0],points[0][1]));
			        addEnd(new BMap.Point(points[points.length - 1][0],points[points.length - 1][1]));
			        //画线
			        var polyline = new BMap.Polyline(point1, { strokeColor: "blue", strokeWeight: 6, strokeOpacity: 0.5 });
			        map.addOverlay(polyline);
			        
			        var tabstr="";
			        tabstr+="<div class='table_top'><div style='float: left;' class='info_hd'><div style='font-size: 15px;'>最近呼救记录</div></div><div class='right_box'><a target='_blank' href='#'></a></div></div>";
			        tabstr+="<table class='table' cellspacing='0'>";
			        tabstr+="<thead class='thead' style='background: transparent'>";
	                tabstr+="<tr><th class='table_cell rank_area tl wp40'>说明</th>";
	                tabstr+="<th class='table_cell rank_area tr wp60 hcenter'>数据</th></tr></thead>";
			        tabstr+="<tbody class='tbody'>";
			        tabstr+=" <tr><td class='table_cell'>最后地址</td><td class='table_cell tr'>"+points[points.length-1][2]+"</td></tr>";
			        tabstr+=" <tr><td class='table_cell'>最后地址天气</td><td class='table_cell tr'>"+points[points.length-1][3]+"</td></tr>";
			        tabstr+=" <tr><td class='table_cell'>最后坐标点</td><td class='table_cell tr'>经度("+points[points.length-1][0]+"),纬度("+points[points.length-1][1]+")</td></tr>";
			        tabstr+=" <tr><td class='table_cell'>最后地址气温</td><td class='table_cell tr'>"+points[points.length-1][4]+"</td></tr>";
			        tabstr+="</tbody></table>";
			        
			        $("#tabdiv").html(tabstr);
				}else{
					alert("暂无数据");
				}
				
	  	    }); 
		});
		  function addStart(point) {
            // 创建图标对象
            // 创建标注对象并添加到地图
            var marker = new BMap.Marker(point, { icon: new BMap.Icon("<%=request.getContextPath()%>/resources/img/map/start.png", new BMap.Size(35, 59), { anchor: new BMap.Size(18, 59) }) });
            map.addOverlay(marker);
        }

        function addEnd(point) {
            var marker = new BMap.Marker(point, { icon: new BMap.Icon("<%=request.getContextPath()%>/resources/img/map/end.png", new BMap.Size(35, 59), { anchor: new BMap.Size(18, 59) }) });
            map.addOverlay(marker);
        } 
        function requestServer(method, dataname, datapara, callback) {
			var d = {};
			d[dataname] = datapara;
			jQuery.support.cors = true;
			$.ajax({
				url: method,
				type: "post",
				data:datapara,
				dataType: "json",
				success: function (data) {
					//alert(data);
					data=eval(data);
					callback(data);
				}
			});
		}
	</script>
</head>
<body>
	<div class="easyui-tabs" id="tabs" style="margin:2px auto 10px auto;" data-options="width:780">
		<div title="轨迹" style="padding: 2px">
			 <div class="inner" id="js_actions">
                <div class="info_hd append_ask">
                    <h4>位置轨迹</h4>
                </div>
                <div class="info_bd">
                    <div class="sub_content" id="graphic_table" style="height:400px;"></div>
                </div>
            </div>
			 <div class="sub_content table_wrap user_menu_sub" style="margin-top:20px;">
			 	<div id="tabdiv" class="table_wrp">
                 </div>
			 </div>
		</div>
	</div>
</body>
</html>