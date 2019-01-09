<%@ page language="java" pageEncoding="UTF-8"%>


<!DOCTYPE html>
<html>
<head>
	<%@ include file="../../common/inc.jsp"%>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/is/jquery-1.8.0.min.js"></script>
	<title>成员申请</title>
	<script type="text/javascript">
	var applyDataGrid;
	var applyPhone;
 	var applyContent;
 	var auditId;
 	var relation;
	$(function() {
		applyDataGrid = $("#applyData").datagrid({
		title: "申请列表",
		width: "auto",
		height: "auto",
		loadMsg: "数据加载中,请稍候...",
		url: '<%=request.getContextPath()%>/sys/family_queryApply.action',
		idField: "id",
		rownumbers: true,
		pageList: [10, 20, 50, 100],
		pagination: true,
		fitColumns: true,
		singleSelect: true,
		nowrap: true,
		striped: true,
		
		frozenColumns: [[{
			field: "ck",
			checkbox: true
		}]],
		columns:[[
			{field:'applyName',title:'成员名称',width:80,align:'center'},
			{field:'idCard',title:'身份证号码',width:100,align:'center'},
			{field:'sex',title:'性别',width:40,align:'center',formatter:function(value){
				if(value==1){
					return '男';
				}else if(value==2){
					return '女';
				}else if(value==0){
					return '未知性别';
				}else if(value==9){
					return '未说明性别';
				}
			}},
			{field:'memberPhone',title:'联系电话',width:85,align:'center'},
			{field:'state',title:'申请状态',width:100,align:'center',formatter:function(value){
				if(value==1){
					return '申请中';
				}else if(value==2){
					return '已通过';
				}else if(value==3){
					return '已拒绝';
				}
			}},
			{field:'memo',title:'备注',width:85,align:'center'},
			{field:'applyTime',title:'申请时间',width:100,align:'center'},
			{field:'auditTime',title:'审核时间',width:100,align:'center'}
		]],
		toolbar: "#toolbar"
	});
	var p = $('#applyData').datagrid('getPager');
		if (p){
			$(p).pagination({
				pageNumber : 1,
				showPageList:false
			});
		}
	});
	
	function addWin(){
			$('#applyWin').window({title:'申请添加成员',width:'350',height:'260',resizable:false,
				collapsible:false,
				minimizable:false,
				maximizable:false,
				modal:true,
				zIndex:100,
				closable:true});
			$('#applyWin').window('center');
			$("#applyForm").form("clear");
			$("#applyForm").form("reset");
			$('#applyWin').window('open');
		}
		
	function addMemWin(){
		subWin = $('<div><iframe src="/<%=request.getContextPath()%>/pgchr/custWarnings.jsp" style="width: 99%;height:99%;border="0" frameborder="0"></iframe></div>').window({
			title : "添加会员名称",
			width : 900,
			height : 400,
			resizable : false,
			collapsible : false,
			minimizable : false,
			maximizable : false,
			modal : true,
			zIndex : 100,
			closable : true
		});
	}
		
	function updateWin(){
			var selected = $('#applyData').datagrid("getSelected");
			if(selected){
				var selections = $('#applyData').datagrid("getSelections");
				if(selections.length==1){
					$('#applyWin').window({title:'修改医生信息',width:'500',height:'430',resizable:false,
						collapsible:false,
						minimizable:false,
						maximizable:false,
						modal:true,
						closable:true});
					$('#applyWin').window('center');
					$("#applyForm").form("clear");
					$("#applyForm").form("reset");
					$("#handtype").val("1");
					$("#account").attr("readonly",true);
					$("#applyDataId").val(selected.Id);
					$("#account").val(selected.account);
					$("#password").val(selected.password);
					$("#applyDataName").val(selected.name);
					$("input[name='applyData.sex'][value="+selected.sex+"]").attr("checked",true);
					$("#age").numberbox('setValue',selected.age);
					$("#contactTel").val(selected.contactTel);
					$("#email").val(selected.email);
					$("#address").val(selected.address);
					$("#degree").combobox('select',selected.degree);
					$("#professionId").combobox({
						editor:false,
						onLoadSuccess:function(){
							$(this).combobox('select',selected.professionId_id)
						}
					});
					$("#membershipOrgs").val(selected.membershipOrgs);
					$("#professionalTitle").val(selected.professionalTitle);
					$("#departments").val(selected.departments);
					$("#genius").val(selected.genius);
					$("#profile").val(selected.profile);
					$('#deptId').combotree({
						url:'',
						onBeforeExpand : function(row, param) {
							$('#deptId').combotree('tree').tree('options').url='';
						},
						onLoadSuccess:function(){
							$('#deptId').combotree('setValue', selected.orgId_id);
							$('#deptId').combotree('setText', selected.orgId_deptName);
						}
					});
					$("#areaNames").val(selected.areaNames);
					$("#areaIds").val(selected.areaIds);
					$('#applyWin').window('open');
					$('#applyForm').form('validate');
				}else{
					$.messager.alert("提示信息","一次只能编辑一行数据","info");
					$('#applyData').datagrid("uncheckAll");
					$('#applyData').datagrid("unselectAll");
					$('#applyData').datagrid("clearSelections");
				}
			}else{
				$.messager.alert('提示信息','请选择编辑的行','info');
			}
		}
		
	function refreshGrid(){
			$('#applyData').datagrid("reload");
			$('#applyData').datagrid("unselectAll");
			$('#applyData').datagrid("clearSelections");
			
		}
		
	function save(){
		var phone=document.getElementById("phone");
		var phoneVal = $.trim(phone.value);
			phone.value = phoneVal;
		if(phoneVal && !/^[0-9]+-?\d*$/.test(phoneVal)){
			$.messager.alert("提示信息", "电话号码格式不正确。", "error", function(){
				phone.focus();
			});
			return false;
		}
		var memo=document.getElementById("memo");
		if(memo.value.length > 36){
			$.messager.alert("提示信息", "字数不能超过36个！", "error", function(){
				memo.focus();
			});
			return;
		}
		var content=document.getElementById("content");
		if(content.value.length > 100){
			$.messager.alert("提示信息", "字数不能超过100个！", "error", function(){
				content.focus();
			});
			return;
		}
	  	$("#applyForm").form("submit", {
			url: "<%=request.getContextPath()%>/sys/family_addApply.action",
			dataType : "json",
			success : function(data) {
				data =  $.parseJSON(data); 				
				if(data.state > 0){
					$.messager.alert("提示信息", data.msg?data.msg:"注册成功。", "info", function(){
						  $('#applyWin').window('close');
		  				  refreshGrid();
					});
				}else{
					$.messager.alert("提示信息", data.msg?data.msg:"手机号码不正确。", "error");
				}
				
			}
		});
		 
		}
		
	//关闭窗口
	function clear(){
			$('#applyWin').window('close');
			$("#applyForm").form("clear");
			$("#applyForm").form("reset");
		}
	
	function checkInfo(){
		var selected = $('#applyData').datagrid("getSelected");
			if(selected){
				var selections = $('#applyData').datagrid("getSelections");
				if(selections.length==1){
		 			location.href = "<%=request.getContextPath()%>/exam/phyIdxDetail.jsp";
		 		}else{
					$.messager.alert("提示信息","一次只能编辑一行数据","info");
				}
			}else{
				$.messager.alert('提示信息','请选择编辑的行','info');
			}
		 
	}
	</script>
</head>
<body>
	<div style="padding-top: 10px;">
		<div>
			<table id="applyData" class="easyui-datagrid"></table>
		</div>
	</div>
	<div id="toolbar" style="padding:2px 0;display: none;">
		<table style="width:100%;">
			<tr>
				<td style="padding-left:2px">
					<a href="javascript:addWin()" class="easyui-linkbutton">申请添加成员</a>
					
				</td>
			</tr>
		</table>
	</div>
	<div style="display: none;">
	<div id="applyWin" title="申请添加成员" class="easyui-window" style="padding: 12px;"  data-options="modal:true,closed:true">
		<div>
			<form id="applyForm" action="" method="post" accept-charset="UTF-8">
				<table border="0">
				  <tr>
				    <td width="35%" class="td_title" align="right" nowrap="nowrap">电话号码：</td>
				     <td>
				    	<input type="text" maxlength="15" id ="phone" name="custFamilyAudit.memberPhone"  />
						
					</td>
				  </tr>
				  <tr>
				    <td width="35%" class="td_title" align="right" nowrap="nowrap">备注：</td>
				    <td>
				    	<input type="text" maxlength="36"  id="memo" name = "custFamilyAudit.memo"  />
					</td>
				  </tr>
				  <tr>
				    <td class="td_title" align="right" nowrap="nowrap">验证信息：</td>
				    <td >
		    			<textarea id="content" maxlength="100"  style="height:100px;" name="custFamilyAudit.applyContext" rows="" cols=""></textarea>
		    		</td>
				   </tr>
					<tr>
		    			<td colspan="4" align="center">
		    				<a class="easyui-linkbutton" href="javascript:save()">申请</a> &nbsp;&nbsp; 
		    				<a class="easyui-linkbutton" href="javascript:clear()">返回</a> &nbsp;&nbsp; 
		    			</td>
		    	  </tr>
				</table>
			</form>
		</div>
	</div>
	</div>
</body>
</html>
