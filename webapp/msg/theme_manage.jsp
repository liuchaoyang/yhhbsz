<%@ page language="java" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
  <script type="text/javascript">
  	// 页面编辑状态，1查看，2新增，3修改
	var editState;
    $(function(){
			$('#column_info').datagrid({
				title : '专题列表',
				loadMsg : "数据加载中,请稍候...",
				width : 'auto',
				height : 'auto',
				animate:true,
				nowrap : true,
				striped : true,
				singleSelect : true,
				url:"<%=request.getContextPath()%>/msg/infoCatalog_list.action",
				pagination:true,
				idField:'id',
				rownumbers:false,
				fitColumns:true,
				frozenColumns:[[
					{field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'name',title:'专题名称 ',width:100,align:'center'},
					{field:'detail',title:'专题简介',width:100,align:'center'},
					{field:'predefined',title:'是否预定义',width:100,align:'center',formatter:function(value){
						var sex="";
						if(value==1){
							sex="是";
						}else if(value==2){
							sex="否";
						}
						return sex;
					}},
					{field:'state',title:'状态',width:100,align:'center',formatter:function(value){
						var sex="";
						if(value==1){
							sex="使用中";
						}else if(value==2){
							sex="已停用";
						}else if(value==0){
							sex="作废";
						}
						
						return sex;
					}},
					/* {field:'seq',title:'顺序号',width:100,align:'center'} */
				]],onSortColumn:function(sort,order){
				},toolbar:'#dlg-toolbar'
			});
			var p = $('#column_info').datagrid("getPager");
			if (p) {
				$(p).pagination({
					pageNumber : 1,
					showPageList : false
				});
			}
		});
		
	   //作废专题 
       function effInfoCatalog(){
			var selected = $('#column_info').datagrid('getSelected');
			var rows = $('#column_info').datagrid('getSelections');
			if (selected) {
				if(rows.length==1){
					if(selected.predefined!=1){
					$("#id").val(selected.id);
					if(selected.state!=2){
					$.messager.confirm('警告信息', '确定停用该专题吗?', function(r){
						if (r){
						$('#colWin').window({title:'编辑专题',width:'auto',height:'auto',resizable:false,
						collapsible:false,
						minimizable:false,
						maximizable:false,
						modal:true,
						zIndex:100,
						closable:true});
					$('#colWin').window('center');
					$("#colForm").form("clear");
					$("#colForm").form("reset");
					$("#handtype").val("1");
					$("#colName").val(selected.name);
					$("#id").val(selected.id);
					$("#colIntro").val(selected.detail);
						var urls = '<%=request.getContextPath()%>/msg/infoCatalog_updateState.action';
						$("#colForm").form("submit", {
							url : urls,
							onSubmit : function() {
								return true;
							},
							success : function(data) {
								if(data == "0") {
									$.messager.alert('提示信息','专题停用成功.','info');
									refreshGrid();
					    }else{
					    	$.messager.alert('提示信息','专题停用失败.错误信息:','error');
					    }
					}
					});
			}
			else{
					$.messager.alert('提示信息','请选择要停用的记录.','info');
				}
			});
			}else{
				$.messager.alert('提示信息','该专题已停用.','info');
			}
			}else{
       		$.messager.alert('提示信息','预定义的专题不可编辑.','info');
       }
       }
       
		}} 
		
	//生效专题
		function effNoInfoCatalog(){
			var selected = $('#column_info').datagrid('getSelected');
			var rows = $('#column_info').datagrid('getSelections');
			if (selected) {
				if(rows.length==1){
					if(selected.predefined!=1){
					if(selected.state!=1){
					$("#id").val(selected.id);
					$.messager.confirm('警告信息', '确定启用该专题吗?', function(r){
						if (r){
						$('#colWin').window({title:'编辑专题',width:'auto',height:'auto',resizable:false,
						collapsible:false,
						minimizable:false,
						maximizable:false,
						modal:true,
						zIndex:100,
						closable:true});
					$('#colWin').window('center');
					$("#colForm").form("clear");
					$("#colForm").form("reset");
					$("#handtype").val("1");
					$("#colName").val(selected.name);
					$("#id").val(selected.id);
					$("#colIntro").val(selected.detail);
						var urls = '<%=request.getContextPath()%>/msg/infoCatalog_updateInfoTopicState.action';
						$("#colForm").form("submit", {
							url : urls,
							onSubmit : function() {
								return true;
							},
							success : function(data) {
								if(data == "0") {
									$.messager.alert('提示信息','专题启用成功.','info');
									refreshGrid();
					    }
					    
					    else{
					    	$.messager.alert('提示信息','专题启用失败.错误信息:','error');
					    }
					}
					});
			}
			else{
					$.messager.alert('提示信息','请选择要停用的记录.','info');
				}
			});
			}else{
       		$.messager.alert('提示信息','该专题已经启用了.','info');
       }
			}else{
       		$.messager.alert('提示信息','预定义的专题不可编辑.','info');
       }
       }
		}
	}
		
		
		function clear(){
			$('#colWin').window('close');
			$("#colForm").form("clear");
			$("#colForm").form("reset");
		}
		
		//关闭窗口
		function clearTheme(){
			$('#themeWin').window('close');
			$("#themeForm").form("clear");
			$("#themeForm").form("reset");
		}
		
		//增加专题
		function addWin(){
			$('#colWin').window({title:'添加专题',width:'auto',height:'auto',resizable:false,
				collapsible:false,
				minimizable:false,
				maximizable:false,
				modal:true,
				zIndex:100,
				closable:true});
			$('#colWin').window('center');
			$("#colForm").form("clear");
			$("#colForm").form("reset");
			$("#handtype").val("0");
			$("#seq").val("");
			$("#state").val("1");
			editState = 2;
			$('#colWin').window('open');
		}
		
		//增加专题
		function addThemeWin(){
			$('#themeWin').window({title:'添加专题',width:'auto',height:'auto',resizable:false,
				collapsible:false,
				minimizable:false,
				maximizable:false,
				modal:true,
				zIndex:100,
				closable:true});
			$('#themeWin').window('center');
			$("#themeForm").form("clear");
			$("#themeForm").form("reset");
			$('#themeWin').window('open');
		}
		
		function editWin(){
			var selected = $('#column_info').datagrid("getSelected");
			if(selected){
				var selections = $('#column_info').datagrid("getSelections");
				if(selections.length==1){
					if(selected.predefined!=1){
					$('#colWin').window({title:'编辑专题',width:'auto',height:'auto',resizable:false,
						collapsible:false,
						minimizable:false,
						maximizable:false,
						modal:true,
						zIndex:100,
						closable:true});
					$('#colWin').window('center');
					$("#colForm").form("clear");
					$("#colForm").form("reset");
					$("#handtype").val("1");
					$("#colName").val(selected.name);
					$("#id").val(selected.id);
					$("#colIntro").val(selected.detail);
					$(".state").val(selected.state);
					editState = 3;
					$('#colWin').window('open');
				}else{
					$.messager.alert("提示信息","预定义专题不可编辑.","info");
				}
				}else{
					$.messager.alert("提示信息","一次只能编辑一条专题.","info");
				}
			}else{
				$.messager.alert("提示信息","请选择要编辑的专题.","info");
			}
		}
	//保存专题
		function saveCol(){
		  var colName = document.getElementById("colName");
		var colNameVal = $.trim(colName.value);
		colName.value = colNameVal;
		if(!colNameVal){
			$.messager.alert("提示信息", "请输入专题名称.", "info");
			return;
		}
		var infoCatalogDetailEle = $("#colIntro");
		if(editState == 2){
			var infoCatalogDetailVal = trim(infoCatalogDetailEle.val());
			if(!infoCatalogDetailVal){
				$.messager.alert("提示信息", "请输入专题简介.", "info");
				return;
			}
			infoCatalogDetailEle.val(infoCatalogDetailVal);
		}
		//判断排序号是否为整数的正则表达式
		var seqNumEle = $("#seqNum");
		var seqNumVal = $.trim(seqNumEle.val());
		/* if(!seqNumVal){
			$.messager.alert("提示信息", "请输入栏目排序号.", "info");
			return;
		} */
		var regSeq=/^[1-9]*$/;
		if(!(regSeq.test(seqNumVal))){
			$.messager.alert("提示信息", "栏目排序号只能是正整数.", "info");
			return;
		}
		var saveUrl = "<%=request.getContextPath()%>/msg/infoCatalog_addInfoCatalog.action";
		if(editState == 3){
			saveUrl = "<%=request.getContextPath()%>/msg/infoCatalog_updateInfoCatalog.action";
		}
		var saveUrl = "<%=request.getContextPath()%>/msg/infoCatalog_addInfoTopic.action";
		if(editState == 3){
			saveUrl = "<%=request.getContextPath()%>/msg/infoCatalog_updateInfoTopic.action";
		}
		$("#colForm").form("submit", {
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
					$.messager.alert("提示信息", "新增失败，此专题详细已经存在.", "info");
				}else if(data == "-11"){
					$.messager.alert("提示信息", "新增失败，此专题名称已经存在.", "info");
				}else if(data == "-21"){
					$.messager.alert("提示信息", "修改失败，此专题记录不存在.", "info");
				}else if(data == "-22"){
					$.messager.alert("提示信息", "修改失败，此专题名称已经存在.", "info");
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
		$('#column_info').datagrid("reload");
		$('#column_info').datagrid("unselectAll");
		$('#column_info').datagrid("clearSelections");
	}
	
	function closeWin(){
		$("#colWin").window("close");
	}
	
  </script>
  </head>
     
  <body>
     <div style="width: 100%;height: 100%;text-align: center;position:relative;">
		<table id="mytab" align="center" cellpadding="0" cellspacing="0" border="0" width="98%" height="95%">
			<tr><td>&nbsp;</td></tr>
			<tr>
				<td><table id="column_info"></table></td>
			</tr>
		</table>
	</div>
	<div id="dlg-toolbar" style="padding:2px 0">
		<table cellpadding="0" cellspacing="0" style="width:100%">
			<tr>
				<td style="padding-left:2px">
				  <a href="javascript:addWin()" class="easyui-linkbutton">增加专题</a>
				  <a href="javascript:editWin()" class="easyui-linkbutton">编辑专题</a>
				  <a href="javascript:effNoInfoCatalog()" class="easyui-linkbutton">启用</a>
				  <a href="javascript:effInfoCatalog()" class="easyui-linkbutton">停用</a>
				</td>
			</tr>
		</table>
	</div>
	
	<div style="display: none;">
		<div id="colWin" title="增加专题" class="easyui-window" style="padding: 12px;"  data-options="modal:true,closed:true">
			<form id="colForm" action="" method="post">
			<input type="hidden" id="id" name="infoCatalog.id" />
				<table border="0">
				  <tr>
				    <td align="right" nowrap="nowrap">专题名称</td>
				    <td>
				      <input type="text" id="colName"  name="infoCatalog.name" maxlength="10" style="width: 200px;"/>
				      <input type="hidden" name="handtype" value="" id="handtype">
		    		</td>
				  </tr>
				  <tr style="display:none">
				    <td align="right" nowrap="nowrap">可见角色</td>
				    <td>
				      <input  type="checkbox" value="" />会员 &nbsp;
				      <input  type="checkbox" value="" />普通居民
				    </td>
				   </tr>
				   <tr >
				    <td nowrap="nowrap" align="right">专题简介</td>
				    <td>
				      <textarea  id="colIntro" name="infoCatalog.detail" class="easyui-validatebox" style="width: 200px;height: 100px;"></textarea>
		    		</td>
				  </tr>
					<tr style="display:none">
						<td nowrap="nowrap" align="right">排序号:</td>
						<td><input id="seqNum" name="infoCatalog.seq"
							maxlength="4" style="width: 200px;" /></td>
					</tr>
					<tr style="display:none">
						<td nowrap="nowrap" align="right">预定义:</td>
						<td><input id="predefined" name="infoCatalog.predefined"
							maxlength="30" style="width: 200px;" /></td>
					</tr>
					<tr>
						<td nowrap="nowrap" align="right">状态:</td>
						<td><select class="state" id="state" name="infoCatalog.state"
							style="width: 205px;">
								<option value="1">使用中</option>
								<option value="2">已停用</option>
								<!-- <option value="0">已作废</option> -->
						</select></td>
					</tr>
					<tr>
		    			<td colspan="4" align="center">
		    				<a class="easyui-linkbutton" href="javascript:saveCol()">保存</a> &nbsp;&nbsp; 
		    				<a class="easyui-linkbutton" href="javascript:clear()">返回</a> &nbsp;&nbsp; 
		    			</td>
		    	  </tr>
				</table>
		   </form>
		</div>
	</div>

  </body>
</html>
