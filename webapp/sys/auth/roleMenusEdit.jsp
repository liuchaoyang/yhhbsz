<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.yzxt.yh.module.sys.bean.Resource"%>
<!DOCTYPE html>
<html>
<%
	@SuppressWarnings("unchecked")
	List<Resource>	selRess = (List<Resource>)request.getAttribute("selRess");
	StringBuilder selResIds = new StringBuilder();
	if(selRess!=null){
	    for(Resource res : selRess){
	        selResIds.append(res.getId()).append(",");
	    }
	}
%>
<head>
	<%@ include file="../../common/inc.jsp"%>
	<title>角色资源管理</title>
	<link href="<%=request.getContextPath()%>/common/zTree/css/zTreeStyle/zTreeStyle_urmioo2.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/common/zTree/js/jquery.ztree.core-3.5.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/common/zTree/js/jquery.ztree.excheck-3.5.js"></script>
	<style type="text/css">
	body{
		padding: 2px;
		margin: 0px;
	}
	</style>
	<script type="text/javascript">
	$(document).ready(function() {
		$.ajax({
			url: "<%=request.getContextPath()%>/sys/role_loadRess.action",
			type: "post",
			dataType: "json",
			success: function(rst) {
				initZtree(rst);
			}
		});
	});
	var zTreeObj;
	function initZtree(json) {
		var setting = {
			data: {
				simpleData: {
					enable: true,
					idKey: "id",
					pIdKey: "parentId",
					rootPId: "0"
				},
				key: {
					name: "name"
				}
			},
			view: {
				dblClickExpand: false
			},
			check: {
				enable: true,
				chkStyle: "checkbox",
				chkboxType: { "Y" : "p", "N" : "ps" }
			}
		};
		var selIds = "<%=selResIds.toString()%>";
		if (selIds){
			var arrStr = selIds.split(",");
			$.each(json, function(i, item){
				for (var i=0;i<arrStr.length ;i++)
				{
					if (arrStr[i] == item.id)
					{
						item.checked = true;
					}
				}
			});
		}
		zTreeObj = $.fn.zTree.init($("#ressTree"), setting, json);
		zTreeObj.expandAll(false);
	}
	function closeIt(){
		try{
			parent.subWin.window("close");
		}catch(e){}
	}
	function saveRoleRess(){
		<%--选中的ID--%>
		var nodes = zTreeObj.getCheckedNodes(true);
		var selIds = "";
		if(nodes){
			for(var i=0;i<nodes.length;i++){
				if(nodes[i].id && nodes[i].id.length == 6){
					selIds = selIds + nodes[i].id + ",";
				}
			}
		}
		document.getElementById("resIds").value = selIds;
		$("#roleRessForm").form("submit", {
			url: "<%=request.getContextPath()%>/sys/role_updateRoleRess.action",
			dataType : "json",
			success : function(data) {
				data =  $.parseJSON(data); 
				if(data.state == 1){
					$.messager.alert("提示信息", data.msg?data.msg:"保存成功。", "info", function(){
						try{
							// parent.refreshGrid();
						}catch(e){}
						closeIt();
					});
				}else{
					$.messager.alert("提示信息", data.msg?data.msg:"保存失败。", "error");
				}
			}
		});
	}
	</script>
</head>
<body>
	<form  id="roleRessForm" style="margin: 0px;padding: 0px;" method="post" accept-charset="UTF-8">
		<input type="hidden" id="resIds" name="resIds" value="" />
		<input type="hidden" id="roleId" name="roleId" value="<%=(String)request.getAttribute("roleId")%>" />
	</form>	
	<ul id="ressTree" class="ztree2"></ul>
	<div style="text-align: center;padding-top: 10px;">
		<a class="easyui-linkbutton" href="javascript:saveRoleRess();">保存</a>
		&nbsp;&nbsp;
		<a class="easyui-linkbutton" href="javascript:closeIt();">关闭</a>
	</div>
</body>
</html>
