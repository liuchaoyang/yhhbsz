<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.constant.Constant"%>
<%@ include file="../common/inc.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE HTML>
<html>
<head>
<title></title>
<meta charset="UTF-8">
<meta name="pragma" content="no-cache" />
<meta name="cache-control" content="no-cache" />
<meta name="expires" content="0" />
<meta name="keywords" content="keyword1,keyword2,keyword3" />
<meta name="description" content="" />
<style type="text/css">
</style>
<script type="text/javascript">
	$(function() {
		$("#tt").tree({
			url : "${pageContext.request.contextPath}/msg/nlgCata_getTreeNodeChildren.action",
			onLoadSuccess : function(node, data){
				$("#tt").tree("expandAll");
				// 如果展开的是根节点，展开二级节点
				if(!node){
				}
			}
		});
	});

	function closeWin() {
		try {
			parent.closeSelCataWin();
		} catch (e) {
		}
	}

	function selCata() {
		try {
			var node = $("#tt").tree("getSelected");
			if(!node || !node.attributes.isLeaf){
				$.messager.alert("提示信息", "请选择一个叶子节点目录.", "error");
				return;
			}
			parent.setSelCata({name : node.text, id : node.id});
			closeWin();
		} catch (e) {
			$.messager.alert("提示信息", e, "error");
		}
	}
</script>
</head>
<body style="overflow: hidden;">
	<div style="height: 400px; padding: 5px; overflow: auto;">
		<ul id="tt"></ul>
	</div>
	<div style="padding-top: 10px; text-align: center;">
		<a href="javascript: void(0)" onclick="selCata();"
			class="easyui-linkbutton">确定</a> <a href="javascript: void(0)"
			onclick="closeWin();" class="easyui-linkbutton">关闭</a>
	</div>
</body>
</html>