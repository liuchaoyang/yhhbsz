<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>健康知识</title>
	<style type="text/css">
	.nlgCon {
		padding: 8px 10px 10px 25px;
	}
	.nlgTitle {
		font-size: 15px;
		font-weight: bold;
		color: #006699;
	}
	.nlgTitle:HOVER {
		color: #bc2a4d;
		text-decoration: underline;
		cursor: pointer;
	}
	.nlgSummary {
		padding-top: 10px;
		padding-left: 15px;
	}
	.nlgFoot {
		padding-top: 10px;
		color: gray;
	}
	.noDataTip {
		padding-left: 25px;
		font-style: italic;
	}
	#pageDiv {
		padding-left: 28px;
		padding-top: 15px;
		text-align: left;
	}
	.pageItem {
		border: 1px solid blue;
		padding: 2px 3px;
		border: 1px solid #006699;
		color: #006699;
		cursor: pointer;
	}
	.curPage {
		color: #ddd;
		background: #006699;
	}
	</style>
<script type="text/javascript">
	$(function(){
		$("#tt").tree({
			url : "${pageContext.request.contextPath}/msg/nlgCata_getTreeNodeChildren.action",
			onLoadSuccess : function(node, data) {
				// 如果展开的是根节点，展开二级节点
				$("#tt").tree("expandAll");
				if (!node) {
				}
			},
			onSelect: function(node) {
				if (node) {
					try {
						document.getElementById("nlsList").contentWindow.searchByCata(node.attributes.fullId);
					} catch (e) {
					}
				}
			}
		});
	});
	function refresh(){
		try {
			document.getElementById("nlsList").contentWindow.searchByRefresh();
		} catch (e) {
		}
	}
</script>
</head>
<body class="easyui-layout">
	<div data-options="region:'west',split:true,hide:true" style="width: 200px;padding: 5px;padding-top: 10px;">
		<ul id="tt"></ul>
	</div>
	<div data-options="region:'center',border:false" style="overflow: hidden;padding: 5px;min-width: 300px;">
		<iframe src="<%=request.getContextPath()%>/msg/nlg_list.action" id="nlsList" style="height: 100%;width: 100%;margin: 0px;padding: 0px;" scrolling="auto" frameborder="0"></iframe>
	</div>
</body>
</html>