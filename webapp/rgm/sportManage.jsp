<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../header.jsp"%>
<%
	String basePath = request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>运动维护</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">

  <script type="text/javascript">
  	// 页面编辑状态，1查看，2新增，3修改
	var editState;
    $(function(){
			var tab_height = getHeight();
			$('#sport_info').datagrid({
				title : '运动列表',
				loadMsg : "数据加载中,请稍候...",
				width : 'auto',
				height : tab_height,
				animate:true,
				nowrap : true,
				striped : true,
				singleSelect : true,
				url:"<%=basePath%>rgm/sport_list.action",
				pagination:true,
				idField:'id',
				rownumbers:false,
				fitColumns:true,
				frozenColumns:[[
								{field:'ck',checkbox:true},
							]],
				columns:[[
					{field:'name',title:'运动项目名称 ',width:100,align:'center'},
					{field:'sportType',title:'运动强度',width:100,align:'center',formatter:function(value){
						var sex="";
						if(value==1){
							sex="轻度";
						}else if(value==2){
							sex="中度";
						}else {
							sex="稍强度";
						}
						return sex;
					}},
					{field:'sportHeat',title:'能量',width:100,align:'center'},
					{field:'state',title:'状态',width:100,align:'center',formatter:function(value){
						var sex="";
						if(value==1){
							sex="使用中";
						}else if(value==2){
							sex="已作废";
						}
						
						return sex;
					}} 
				]],onSortColumn:function(sort,order){
				},toolbar:'#dlg-toolbar'
			});
			var p = $('#sport_info').datagrid("getPager");
			if (p) {
				$(p).pagination({
					pageNumber : 1,
					showPageList : false
				});
			}
		});
		
	   //作废运动 
       function deleteSport(){
			var selected = $('#sport_info').datagrid('getSelected');
			var rows = $('#sport_info').datagrid('getSelections');
			if (selected) {
				if(rows.length==1){
					$("#id").val(selected.id);
					$.messager.confirm('警告信息', '确定停用该运动吗?', function(r){
						if (r){
						$('#sportWin').window({title:'编辑运动',width:'auto',height:'auto',resizable:false,
						collapsible:false,
						minimizable:false,
						maximizable:false,
						modal:true,
						zIndex:100,
						closable:true});
					$('#sportWin').window('center');
					$("#sportForm").form("clear");
					$("#sportForm").form("reset");
					$("#handtype").val("1");
					$("#sportName").val(selected.name);
					$("#id").val(selected.id);
					$("#type").val(selected.type);
						var urls = '<%=basePath%>rgm/sport_deleteSport.action';
						$("#sportForm").form("submit", {
							url : urls,
							onSubmit : function() {
								return true;
							},
							success : function(data) {
								if(data == "0") {
									$.messager.alert('提示信息','运动停用成功.','info');
									refreshGrid();
					    }else{
					    	$.messager.alert('提示信息','运动停用失败.错误信息:','error');
					    }
					}
					});
			}
			else{
					$.messager.alert('提示信息','请选择要停用的记录.','info');
				}
			});
       }
       
		}} 
		
	//生效运动
		function deleteSport1(){
			var selected = $('#sport_info').datagrid('getSelected');
			var rows = $('#sport_info').datagrid('getSelections');
			if (selected) {
				if(rows.length==1){
					$("#id").val(selected.id);
					$.messager.confirm('警告信息', '确定删除该运动吗?', function(r){
						if (r){
						$('#sportWin').window({title:'编辑运动',width:'auto',height:'auto',resizable:false,
						collapsible:false,
						minimizable:false,
						maximizable:false,
						modal:true,
						zIndex:100,
						closable:true});
					$('#sportWin').window('center');
					$("#sportForm").form("clear");
					$("#sportForm").form("reset");
					$("#handtype").val("1");
					$("#sportName").val(selected.name);
					$("#id").val(selected.id);
					$("#type").val(selected.type);
						var urls = '<%=basePath%>rgm/sport_deleteSport.action';
						$("#sportForm").form("submit", {
							url : urls,
							onSubmit : function() {
								return true;
							},
							success : function(data) {
								if(data == "0") {
									$.messager.alert('提示信息','运动删除成功.','info');
									refreshGrid();
					    }
					    else{
					    	$.messager.alert('提示信息','运动删除失败.错误信息:','error');
					    }
					}
					});
			}
			else{
					$.messager.alert('提示信息','请选择要停用的记录.','info');
				}
			});
			
       }
		}
	}
		
		
		function clear(){
			$('#sportWin').window('close');
			$("#sportForm").form("clear");
			$("#sportForm").form("reset");
		}
		
		//关闭窗口
		function clearTheme(){
			$('#themeWin').window('close');
			$("#themeForm").form("clear");
			$("#themeForm").form("reset");
		}
		
		//增加运动
		function addSportWin(){
			$('#sportWin').window({title:'添加运动项目',width:'auto',height:'auto',resizable:false,
				collapsible:false,
				minimizable:false,
				maximizable:false,
				modal:true,
				zIndex:100,
				closable:true});
			$('#sportWin').window('center');
			$("#sportForm").form("clear");
			$("#sportForm").form("reset");
			$("#handtype").val("0");
			$("#type").val("1");
			$("#state").val("1");
			editState = 2;
			$('#sportWin').window('open');
		}

		function editSportWin(){
			var selected = $('#sport_info').datagrid("getSelected");
			if(selected){
				var selections = $('#sport_info').datagrid("getSelections");
				if(selections.length==1){
					if(selected.predefined!=1){
					$('#sportWin').window({title:'编辑运动',width:'auto',height:'auto',resizable:false,
						collapsible:false,
						minimizable:false,
						maximizable:false,
						modal:true,
						zIndex:100,
						closable:true});
					$('#sportWin').window('center');
					$("#sportForm").form("clear");
					$("#sportForm").form("reset");
					$("#handtype").val("1");
					$("#sportName").val(selected.name);
					$("#id").val(selected.id);
					$("#type").val(selected.type);
					$("#sportHeat").val(selected.sportHeat);
					editState = 3;
					$('#sportWin').window('open');
				}else{
					$.messager.alert("提示信息","预定义运动不可编辑.","info");
				}
				}else{
					$.messager.alert("提示信息","一次只能编辑一条运动.","info");
				}
			}else{
				$.messager.alert("提示信息","请选择要编辑的运动.","info");
			}
		}
	//保存运动
		function saveSport(){
		  var sportName = $("#sportName");
		var sportNameVal = trim(sportName.val());
		if(!sportNameVal){
			$.messager.alert("提示信息", "请输入运动名称.", "info");
			return;
		}
		sportName.val(sportNameVal);
		var saveUrl = "<%=basePath%>rgm/sport_addSport.action";
		if(editState == 3){
			saveUrl = "<%=basePath%>rgm/sport_updateSport.action";
		}
		$("#sportForm").form("submit", {
			url : saveUrl,
			onSubmit : function() {
				return true;
			},
			success : function(data) {
				if(data == "0") {
					$.messager.alert("提示信息", "保存成功.", "info");
					closeWin();
					refreshGrid();
					
				}else if(data == "-11"){
					$.messager.alert("提示信息", "新增失败，此运动详细已经存在.", "info");
				}else if(data == "-11"){
					$.messager.alert("提示信息", "新增失败，此运动名称已经存在.", "info");
				}else if(data == "-21"){
					$.messager.alert("提示信息", "修改失败，此运动记录不存在.", "info");
				}else if(data == "-22"){
					$.messager.alert("提示信息", "修改失败，此运动名称已经存在.", "info");
				}else{
					$.messager.alert("提示信息", "保存失败.", "info");
				}
			},
			error : function(a){
				$.messager.alert("提示信息", a, "info");
			}
		});
		}
		
		function saveTheme(){
		    $('#themeWin').window('close');
		    reloadGrid();
		}
		
		function refreshGrid(){
		$('#sport_info').datagrid("reload");
		$('#sport_info').datagrid("unselectAll");
		$('#sport_info').datagrid("clearSelections");
	}
	
	function closeWin(){
		$("#sportWin").window("close");
	}
	
  </script>
  </head>
     
  <body>
     <div style="width: 100%;height: 100%;text-align: center;position:relative;">
		<table id="mytab" align="center" cellpadding="0" cellspacing="0" border="0" width="98%" height="95%">
			<tr><td>&nbsp;</td></tr>
			<tr>
				<td><table id="sport_info"></table></td>
			</tr>
		</table>
	</div>
	<div id="dlg-toolbar" style="padding:2px 0">
		<table cellpadding="0" cellspacing="0" style="width:100%">
			<tr>
				<td style="padding-left:2px">
				  <a href="javascript:addSportWin()" class="easyui-linkbutton">增加运动</a>
				  <a href="javascript:editSportWin()" class="easyui-linkbutton">编辑运动</a>
				  <a href="javascript:deleteSport()" class="easyui-linkbutton">删除运动</a>
				</td>
			</tr>
		</table>
	</div>

	<div style="display: none;">
		<div id="sportWin" title="增加运动项目" class="easyui-window"
			style="padding: 12px;" data-options="modal:true,closed:true">
			<form id="sportForm" action="" method="post">
				<input type="hidden" id="id" name="sport.id" />
				<table border="0">
					<tr>
						<td align="right" nowrap="nowrap">运动项目名称：</td>
						<td>
							<input type="text" id="sportName" name="sport.name" maxlength="10" style="width: 200px;" /> 
							<input type="hidden" name="handtype" value="" id="handtype">
						</td>
					</tr>
					<tr>
						<td align = "right" nowrap="nowrap">运动强度 ：</td>
						<td>
							<select id="type" name="sport.sportType" style="width: 205px;">
								<option value="1">轻度</option>
								<option value="2">中度</option>
								<option value="3">稍强度</option>
							</select>
						</td>
					</tr>
					<tr>
						<td align="right" nowrap="nowrap">运动项目能量：</td>
						<td>
							<input type="text" id="sportHeat" name="sport.sportHeat" maxlength="10" style="width: 200px;" /> 
							<input type="hidden" name="handtype" value="" id="handtype">
						</td>
					</tr>
					 <tr>
						<td nowrap="nowrap" align="right">运动项目状态：</td>
						<td>
							<select id="state" name="sport.state" style="width: 205px;">
								<option value="1">使用中</option>
								<option value="2">已停用</option>
							</select>
						</td>
					</tr>
					<tr>
						<td colspan="4" align="center">
							<a class="easyui-linkbutton" href="javascript:saveSport()">保存</a> &nbsp;&nbsp;
							<a class="easyui-linkbutton" href="javascript:clear()">返回</a>&nbsp;&nbsp;
						</td>
					</tr>
				</table>
			</form>
		</div>
	</div>

</body>
</html>
