<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../header.jsp"%>
<%
	String basePath = request.getContextPath();
%>
<%@ page import="com.yzxt.yh.module.rgm.bean.FoodCatalog"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	@SuppressWarnings("unchecked")
	List<FoodCatalog> topLevelFoodCatalogs = (List<FoodCatalog>)request.getAttribute("topLevelFoodCatalogs");
%>
<!DOCTYPE HTML>
<html>
<head>
<title>食物列表</title>
<meta charset="UTF-8">
<meta name="pragma" content="no-cache" />
<meta name="cache-control" content="no-cache" />
<meta name="expires" content="0" />
<meta name="keywords" content="keyword1,keyword2,keyword3" />
<meta name="description" content="居民会员列表" />
<script type="text/javascript"
	src="<%=basePath%>resources/My97DatePicker/WdatePicker.js"></script>
<style type="text/css">
#foodTb td{
	padding: 8px;
}
</style>
<script type="text/javascript">
	var foodGrid;
	var subWin;
	$(function(){
		var tabHeight = getHeight();
		foodGrid = $("#foodBox").datagrid({
			title : "食物信息列表",
			loadMsg : "数据加载中,请稍候...",
			width : "auto",
			height : tabHeight,
			animate : false,
			striped : true,
			url : "<%=basePath%>rgm/food_list.action",
			pagination : true,
			idField : "id",
			rownumbers : true,
			fitColumns : true,
			frozenColumns : [ [ {
				field : "ck",
				checkbox : true
			} ] ],
			columns : [ [ {
				field : "name",
				title : "食物名称",
				width : 100,
				align : "center"
			}/* , {
				field : "catalogFullName",
				title : "所属分类",
				width : 120,
				align : "center"
			} */, {
				field : "foodHeat",
				title : "热量（卡/千克）",
				width : 80,
				align : "center"
			}, {
				field : "updateTime",
				title : "录入时间",
				width : 80,
				align : "center"
			} ] ],
			onSortColumn : function(sort, order) {
			},
			toolbar : "#dlg-toolbar"
		});
		var p = foodGrid.datagrid("getPager");
		if (p) {
			$(p).pagination({
				pageNumber : 1,
				showPageList : false
			});
		}
	});
	
	function refreshGrid(){
		foodGrid.datagrid("reload");
		foodGrid.datagrid("unselectAll");
		foodGrid.datagrid("clearSelections");
	}
	
	function search(val){
		foodGrid.datagrid("getPager").pagination({pageNumber : 1});
		var option = foodGrid.datagrid("options");
		option.pageNumber = 1;
		option.url = "<%=basePath%>rgm/food_list.action";
		var queryParams = option.queryParams;
		queryParams.keyword = trim(val);
		refreshGrid();
	}
	
	function setCataList(parentCataId, targetSel, selVal){
		if(parentCataId){
			$.ajax({
				type : "GET",
				dataType : "json",
				url : "<%=basePath%>rgm/food_listChildrenFoodCatalogsByJson.action?catalogId=" + parentCataId,
				async : false,
				timeout : 30000,
				success : function(data){
					targetSel.options.length = 0;
					targetSel.add(new Option("请选择", ""));
					for(var i = 0;i < data.length; i++){
						targetSel.add(new Option(data[i].name, data[i].id));
					}
					targetSel.value = selVal ? selVal : "";
				},
				error : function(){
			 	}
			});
		}else{
			targetSel.options.length = 0;
			targetSel.add(new Option("请选择", ""));
			targetSel.value = "";
		}
	}
	
	function showAddWin(){
		$("#parentCatalogId").val("");
		// 清除小类选项
		setCataList("", document.getElementById("catalogId"), "");
		$("#foodName").val("");
		$("#foodHeat").val("");
		$("#foodId").val("");
		subWin = $("#foodWin").window({title: "添加食物信息", width: "auto", height: "auto", resizable: false,
			collapsible: false, minimizable: false, maximizable: false, modal: true, zIndex:100, closable: true});
		subWin.window("center");
		subWin.window("open");
	}
	
	function showUpdateWin(){
		var selections = foodGrid.datagrid("getSelections");
		if(selections && selections.length == 1){
			var selection = selections[0];
			$("#parentCatalogId").val(selection.parentCatalogId);
			// 清除小类选项
			setCataList(selection.parentCatalogId, document.getElementById("catalogId"), selection.catalogId);
			$("#foodName").val(selection.name);
			$("#foodHeat").val(selection.foodHeat);
			$("#foodId").val(selection.id);
			subWin = $("#foodWin").window({title: "修改食物信息", width: "auto", height: "auto", resizable: false,
				collapsible: false, minimizable: false, maximizable: false, modal: true, zIndex:100, closable: true});
			subWin.window("center");
			subWin.window("open");
		}else if(selections && selections.length > 1){
			$.messager.alert("提示信息", "一次只能修改一条记录.", "error");
		}else{
			$.messager.alert("提示信息", "请选择需要修改的记录.", "error");
		}
	}
	
	function saveFood(){
		var url = "<%=basePath%>rgm/food_add.action";
		if(document.getElementById("foodId").value){
			url = "<%=basePath%>rgm/food_update.action";
		}
		$("#foodForm").form("submit", {
			url : url,
			onSubmit : function() {
				var parentCatalogIdVal = $("#parentCatalogId").val();
				if(!parentCatalogIdVal){
					$.messager.alert("提示信息", "请选择食物大类.", "error");
					return false;
				}
				var catalogIdVal = $("#catalogId").val();
				if(!catalogIdVal){
					$.messager.alert("提示信息", "请选择食物小类.", "error");
					return false;
				}
				var foodNameEle = document.getElementById("foodName");
				var foodNameVal = $.trim(foodNameEle.value);
				foodNameEle.value = foodNameVal;
				if(!foodNameVal){
					$.messager.alert("提示信息", "请填写食物名称.", "error");
					return false;
				}
				var foodHeatEle = document.getElementById("foodHeat");
				var foodHeatVal = $.trim(foodHeatEle.value);
				foodHeatEle.value = foodHeatVal;
				if(!foodHeatVal){
					$.messager.alert("提示信息", "请填写食物热量.", "error");
					return false;
				}else if(!/^[1-9]\d{0,3}(\.\d{1,2})?$/.test(foodHeatVal)){
					$.messager.alert("提示信息", "食物热量数据不对,食物热量最多四位整数和两位小数的正数.", "error");
					return false;
				}
				return true;
			},
			success : function(data){
				data =  $.parseJSON(data);
				if (data.state > 0) {
					$.messager.alert("提示信息", "添加成功.", "info");
					refreshGrid();
					closeSubWin();
				} else {
					$.messager.alert("提示信息", data.msg ? data.msg : "添加失败.", "error");
				}
			}
		});
	}
	
	function del(){
		var selections = foodGrid.datagrid("getSelections");
		var len = selections ? selections.length : 0;
		if(len <= 0){
			$.messager.alert("提示信息", "请选择需要删除的行.", "error");
			return;
		}
		var ids = "";
		for(var i = 0; i < len; i++){
			if(i > 0){
				ids = ids + "," + selections[i].id;
			}else{
				ids = selections[i].id;
			}
		}
		$.messager.confirm("警告信息", "确定删除数据吗?", function(r){
			if (r){
				$.ajax({
					type : "POST",
					dataType : "json",
					data : {ids : ids},
					url : "<%=basePath%>rgm/food_delete.action",
					async : false,
					timeout : 300000,
					success : function(data){
						if(data.state == 1){
							$.messager.alert("提示信息", "删除成功.", "error");
							refreshGrid();
						}else if(data.state < 0){
							$.messager.alert("提示信息", data.msg ? data.msg : "删除失败.", "error");
						}
					},
					error : function(){
						$.messager.alert("提示信息", "删除失败！", "error");
				 	}
				});
			}
		});
	}
	
	function closeSubWin(){
		try{
			subWin.window("close");
		}catch(e){}
	}
</script>
</head>
<body>
	<div id="dlg-toolbar" style="padding: 2px 0;">
		<table cellpadding="0" cellspacing="0" style="width: 100%">
			<tr>
				<td style="padding-left: 2px">
					<a href="javascript: void(0)" onclick="showAddWin();" class="easyui-linkbutton">添加</a>
					<a href="javascript: void(0)" onclick="showUpdateWin();" class="easyui-linkbutton">修改</a>
					<a href="javascript: void(0)" onclick="del();" class="easyui-linkbutton">删除</a>
				</td>
				<td style="text-align: right; padding-right: 10px">
					<input class="easyui-searchbox" data-options="prompt:'食物名称.',searcher:search" style="width: 150px;" />
				</td>
			</tr>
		</table>
	</div>
	<div
		style="width: 100%; height: 100%; text-align: center; position: relative;">
		<table id="mytab" align="center" cellpadding="0" cellspacing="0"
			border="0" width="98%" height="95%">
			<tr>
				<td><table id="foodBox"></table></td>
			</tr>
		</table>
	</div>
	<div style="display: none;">
		<div id="foodWin" title="食物信息" class="easyui-window" data-options="modal:true, closed:true" style="text-align: center;overflow: hidden;">
			<form id="foodForm" action="" method="post">
				<table cellspaceing="20" id="foodTb">
					<tr>
						<td nowrap="nowrap" align="right" width="80">食物大类:</td>
		    			<td width="200" align="left">
			    			<select id="parentCatalogId" name="food.parentCatalogId" style="width: 180px;height: 26px;" onchange="setCataList(this.value, document.getElementById('catalogId'), '');">
			    				<option value="" selected="selected">请选择</option>
			    				<%
			    				for(FoodCatalog foodCatalog : topLevelFoodCatalogs)
			    				{
			    				%>
			    				<option value="<%=foodCatalog.getId()%>"><%=foodCatalog.getName()%></option>
			    				<%
			    				}
			    				%>
			    			</select>
			    			<input type="hidden" id="foodId" name="food.id" />
		    			</td>
		    		</tr>
		    		<tr>
		    			<td nowrap="nowrap" align="right">食物小类:</td>
		    			<td align="left">
		    				<select id="catalogId" name="food.catalogId" style="width: 180px;height: 26px;">
		    					<option value="" selected="selected">请选择</option>
		    				</select>
		    			</td>
		    		</tr>
		    		<tr>
		    			<td nowrap="nowrap" align="right">食物名称:</td>
		    			<td align="left">
		    				<input id="foodName" name="food.name" maxlength="30" style="width: 180px;height: 20px;" maxlength="50" />
		    			</td>
		    		</tr>
		    		<tr>
		    			<td nowrap="nowrap" align="right">热量（卡/克）:</td>
		    			<td align="left">
		    				<input id="foodHeat" name="food.foodHeat" maxlength="30" style="width: 180px;height: 20px;" maxlength="50" />
		    			</td>
		    		</tr>
					<tr>
						<td colspan="2" align="center" style="padding: 15px 0px 5px 0px;">
		    				<a class="easyui-linkbutton" href="javascript:saveFood()">保存</a>
		    				&nbsp;&nbsp;
		    				<a class="easyui-linkbutton" href="javascript:closeSubWin()">返回</a>
		    			</td>
		    		</tr>
				</table>
			</form>
		</div>
	</div>
</body>
</html>