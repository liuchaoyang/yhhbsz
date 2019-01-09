<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head><meta http-equiv="x-ua-compatible" content="ie=8" />
<meta http-equiv="X-UA-Compatible" content="IE=8">

<!-- jquery -->
<link
	href="<%=path%>/common/css/themes/redmond/jquery-ui-1.8.2.custom.css"
	type="text/css" rel="stylesheet" />
<script type="text/javascript"
	src="<%=path%>/common/js/jquery-1.10.2.min.js"></script>
<!-- jqgrid -->
<link rel="stylesheet" type="text/css"
	href="<%=path%>/common/js/jqgrid/css/ui.jqgrid.css">
<script type="text/javascript"
	src="<%=path%>/common/js/jqgrid/js/jquery.jqGrid.min.js"></script>
<script type="text/javascript"
	src="<%=path%>/common/js/jqgrid/js/i18n/grid.locale-cn.js"></script>
<link
	href="<%=path%>/common/js/zTree/css/zTreeStyle/zTreeStyle_urmioo2.css"
	rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="<%=path%>/common/js/zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript"
	src="<%=path%>/common/js/zTree/js/jquery.ztree.excheck-3.5.js"></script>
<!-- artDialog -->
<script type="text/javascript"
	src="<%=path%>/common/js/artDialog/jquery.artDialog.js?skin=default"></script>
<script type="text/javascript"
	src="<%=path%>/common/js/artDialog/plugins/iframeTools.js"></script>

<link href="<%=path%>/common/css/css.css" type="text/css"
	rel="stylesheet" />
<link href="<%=path%>/common/css/public.css" type="text/css"
	rel="stylesheet" />

<title>部门树</title>
</head>
<body>
<input type="hidden" id="gxpdidNew" name="gnId"
				value="${gnId }" />
	<ul id="treeDemo" class="ztree2"></ul>
</body>
<script text="text/javascript">
	$(document).ready(function() {
		$.ajax({
			url : "modgn/gn_hqGnTree",
			type : "post",
			dataType : "json",
			success : function(result) {
				initZtree(result);
			}
		});
	});
	var zTreeObj;
	var setting = {
		data : {
			simpleData : {
				enable : true,
				idKey : "gnid",
				pIdKey : "pid",
				rootPId : "0"
			},
			key : {
				name : "gnname"
			}
		},
		callback : {
			beforeCheck : true,
			onCheck : zTreeOnClick
		},
		view : {
			dblClickExpand : false

		},
		check : {
			enable : true,
			chkStyle : "checkbox",
			chkboxType : { "Y" : "p", "N" : "ps" }
		}
	};
	function initZtree(json) {
	 var gxpdids = $("#gxpdidNew").val();
	 if (gxpdids != "" && gxpdids != null){
		 $.each(json.gnList, function(i, item){
			 var arrStr= new Array(); //定义一数组
			 arrStr = gxpdids.split(","); //字符分割     
			 for (i=0;i<arrStr.length ;i++ )   
			     {   
			        if (arrStr[i] == item.gnid){
			        	item.checked = true;
			        }
			     } 
			 });

	 }
		var data = (json.gnList);
		zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, data);
		zTreeObj.expandAll(false);//整个节点为关闭
	}

	function zTreeOnClick(event, treeId, treeNode) {
		var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
		var nodes = treeObj.getCheckedNodes(true);
		var id = "";
		var name = "";
		for ( var i = 0; i < nodes.length; i++) {
			 
				id = id + nodes[i].gnid + ",";
				name = name + nodes[i].gnname + ",";
			 
		}
		name = name.substring(0, name.length - 1);
		id = id.substring(0, id.length - 1);
		art.dialog.data("gnname", name);//将name的值全部给Province这个变量，在父页面就可以过得province这个变量
		art.dialog.data("gnid", id);
		art.dialog.data("gxpdidNew", id);
		//alert(art.dialog.data("level"));
	}
</script>
</html>
