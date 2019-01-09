<%@ page language="java" pageEncoding="UTF-8"%>


<!DOCTYPE html>
<html>
<head>
	<%@ include file="../../common/inc.jsp"%>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/is/jquery-1.8.0.min.js"></script>
	<title>申请审核管理</title>
	<script type="text/javascript">
	var applyPhone;
 	var applyContent;
 	var auditId;
 	var relation;
	var VerifyGrid;
	$(function() {
		VerifyGrid = $("#verify").datagrid({
		title: "审核列表",
		width: "auto",
		height: "auto",
		loadMsg: "数据加载中,请稍候...",
		url: '<%=request.getContextPath()%>/sys/family_queryAudit.action',
		idField: "id",
		rownumbers: true,
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
			{field:'applyName',title:'申请人(户主)',width:80,align:'center'},
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
			{field:'state',title:'审核状态',width:100,align:'center',formatter:function(value){
				if(value==1){
					return '待审核';
				}else if(value==2){
					return '审核通过';
				}else if(value==3){
					return '拒绝';
				}
			}},
			/* {field:'relation',title:'成员与户主关系',width:85,align:'center'}, */
			{field:'applyTime',title:'申请时间',width:100,align:'center'},
			{field:'auditTime',title:'审核时间',width:100,align:'center'}
		]],
		toolbar: "#v-toolbar"
	});
	var p = $('#verify').datagrid('getPager');
		if (p){
			$(p).pagination({
				pageNumber : 1,
				showPageList:false
			});
		}
	});
	
	function addVerifyWin(){
		var selected = $('#verify').datagrid("getSelected");
			if(selected){
			applyPhone=selected.phone;
			applyContent=selected.applyContext;
			auditId=selected.id;
			relation=selected.relation;
			var state=selected.state;
			if(state==2){
				$.messager.alert("提示信息","数据已经审核","info");
				return ;
			}else{
			
			$("#resiName").val(applyPhone);
			$("#applyContent").val(applyContent);
			$("#auditId").val(auditId);
			$("#relation123").val(relation);
				var selections = $('#verify').datagrid("getSelections");
				if(selections.length==1){
		 			$('#verifyWin').window({title:'审核成员',width:'350',height:'260',resizable:false,
				collapsible:false,
				minimizable:false,
				maximizable:false,
				modal:true,
				zIndex:100,
				closable:true});
			$('#verifyWin').window('center');
			//$("#verifyForm").form("clear");
			//$("#verifyForm").form("reset");
			$('#verifyWin').window('open');
		 		}else{
					$.messager.alert("提示信息","一次只能编辑一行数据","info");
				}
			}
			}
			else{
				$.messager.alert('提示信息','请选择编辑的行','info');
			}
		}
		
	function dealVerify(){
		var selected = $('#verify').datagrid("getSelected");
		if(selected){
			var selections = $('#verify').datagrid("getSelections");
			if(selections.length==1){
				$('#verifyWin').window({title:'处理申请',width:'400',height:'300',resizable:false,
					collapsible:false,
					minimizable:false,
					maximizable:false,
					modal:true,
					zIndex:100,
					closable:true});
				$('#verifyWin').window('center');
				$("#verifyForm").form("clear");
				$("#verifyForm").form("reset");
				$("#auditId").val(selected.id);
				$("#custId").val(selected.custId);
				$("#memberId").val(selected.memberId);
				$("#memberPhone").val(selected.memberPhone);
				$("#memo").val(selected.memo);
				$("#applyContext").val(selected.applyContext);
				$('#verifyWin').window('open');
			}else{
					$.messager.alert("提示信息","一次只能编辑一行数据","info");
					refreshGrid();
					}
				}else{
					$.messager.alert('提示信息','请选择编辑的行','info');
				}
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
			var selected = $('#applicationVerify').datagrid("getSelected");
			if(selected){
				var selections = $('#applicationVerify').datagrid("getSelections");
				if(selections.length==1){
					$('#applicationVerifyWin').window({title:'修改医生信息',width:'500',height:'430',resizable:false,
						collapsible:false,
						minimizable:false,
						maximizable:false,
						modal:true,
						closable:true});
					$('#applicationVerifyWin').window('center');
					$("#applicationVerifyForm").form("clear");
					$("#applicationVerifyForm").form("reset");
					$("#handtype").val("1");
					$("#account").attr("readonly",true);
					$("#applicationVerifyId").val(selected.Id);
					$("#account").val(selected.account);
					$("#password").val(selected.password);
					$("#applicationVerifyName").val(selected.name);
					$("input[name='applicationVerify.sex'][value="+selected.sex+"]").attr("checked",true);
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
					$('#applicationVerifyWin').window('open');
					$('#applicationVerifyForm').form('validate');
				}else{
					$.messager.alert("提示信息","一次只能编辑一行数据","info");
					$('#applicationVerify').datagrid("uncheckAll");
					$('#applicationVerify').datagrid("unselectAll");
					$('#applicationVerify').datagrid("clearSelections");
				}
			}else{
				$.messager.alert('提示信息','请选择编辑的行','info');
			}
		}
		
	function refreshGrid(){
			$('#applicationVerify').datagrid("reload");
			$('#applicationVerify').datagrid("unselectAll");
			$('#applicationVerify').datagrid("clearSelections");
			
		}
		
	//关闭窗口
	function clear(){
			$('#applicationVerifyWin').window('close');
			$("#applicationVerifyForm").form("clear");
			$("#applicationVerifyForm").form("reset");
		}
	
	function saveVerify(){
		var applyName=document.getElementById("applyName");
		if(applyName.value.length > 36){
			$.messager.alert("提示信息", "字数不能超过36个！", "error", function(){
				applyName.focus();
			});
			return;
		}
		//保存审核通过，将
			$("#verifyForm").form("submit", {
			url: "<%=request.getContextPath()%>/sys/family_addAudit.action",
			dataType : "json",
			success : function(data) {
				data =  $.parseJSON(data); 			
				if(data.state == 1){
					$.messager.alert("提示信息", data.msg?data.msg:"审核通过。", "info", function(){
							 $('#verifyWin').window('close');
								VerifyGrid.datagrid("reload");
								VerifyGrid.datagrid("unselectAll");
								VerifyGrid.datagrid("clearSelections");
		   					 	refreshGrid();
					});
				}else{
					$.messager.alert("提示信息", data.msg?data.msg:"审核失败。", "error");
				}
				
			}
		});
	
	
		  
		}
		
	//关闭窗口
	function clearVerify(){
		$("#verifyForm").form("submit", {
			url: "<%=request.getContextPath()%>/sys/family_updateAudit.action",
			dataType : "json",
			success : function(data) {
				data =  $.parseJSON(data); 				
				if(data.state == 1){
					$.messager.alert("提示信息", data.msg?data.msg:"审核拒绝。", "info", function(){
							 $('#verifyWin').window('close');
								VerifyGrid.datagrid("reload");
								VerifyGrid.datagrid("unselectAll");
								VerifyGrid.datagrid("clearSelections");
		   					 	refreshGrid();
					});
				}else{
					$.messager.alert("提示信息", data.msg?data.msg:"审核失败。", "error");
				}
				
			}
		});
		}
	
	function verify(){
		var selected = $('#verify').datagrid("getSelected");
			if(selected){
				var selections = $('#verify').datagrid("getSelections");
				if(selections.length==1){
		 			location.href = "<%=request.getContextPath()%>/sys/custom/applicationVerify.jsp";
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
		<div style="padding-top: 10px;">
			<table id="verify" class="easyui-datagrid"></table>
		</div>
	</div>
	<div id="v-toolbar" style="padding:2px 0;display: none;">
		<table style="width:100%;">
			<tr>
				<td style="padding-left:2px">
					<a href="javascript:dealVerify()" class="easyui-linkbutton">审核</a>
				</td>
			</tr>
		</table>
	</div>
	<div style="display: none;">
	<div id="verifyWin" title="申请添加成员" class="easyui-window" style="padding: 12px;"  data-options="modal:true,closed:true">
		<div>
			<form id="verifyForm" action="" method="post" accept-charset="UTF-8">
				<table  border="0">
				  <tr>
				    <td colspan ="2"  align="center" nowrap="nowrap">
				    	<label>对方添加你为家庭成员</label>
				    </td>
				  </tr>
				  <tr>
				    <td width="35%" class="td_title" align="right" nowrap="nowrap">电话号码：</td>
				     <td>
				    	<input type="text" maxlength="100" readonly="readonly"  id="memberPhone" name = "custFamilyAudit.memberPhone"  />
						<input type="hidden" maxlength="100"   id="auditId" name = "custFamilyAudit.id"  />
						<input type="hidden" maxlength="100"   id="custId" name = "custFamilyAudit.custId"  />
						<input type="hidden" maxlength="100"   id="memberId" name = "custFamilyAudit.memberId"  />
					</td>
				  </tr>
				  <tr>
				  <tr>
				    <td width="35%" class="td_title"  align="right" nowrap="nowrap">备注：</td>
				     <td>
				    	<input type="text" maxlength="36"   id="applyName" name = "custFamilyAudit.applyName"  />
				    	<input type="hidden" maxlength="36"   id="memo" name = "custFamilyAudit.memo"  />
				    	<!-- <select id="relation123"  name="relation123" style="margin:2px;">
				    				<option value="0" >请选择</option>
		                  			<option value="1" ${relation123=='1' ? 'checked' : ''}>父子</option>
		                  			<option value="2" ${relation123=='2' ? 'checked' : ''}>母子</option>
		                  			<option value="3" ${relation123=='3' ? 'checked' : ''}>兄弟</option>
		                  			<option value="4" ${relation123=='4' ? 'checked' : ''}>姐妹</option>
						</select> -->
					</td>
				  </tr>
				  <tr>
				    <td class="td_title" align="right"  nowrap="nowrap">验证信息：</td>
				    <td >
		    			<textarea style="height:80px;" readonly="readonly" id="applyContext" name="custFamilyAudit.applyContext" rows="" cols=""></textarea>
		    		</td>
				   </tr>
					<tr>
		    			<td colspan="4" align="center">
		    				<a class="easyui-linkbutton" href="javascript:saveVerify()">通过</a> &nbsp;&nbsp; 
		    				<a class="easyui-linkbutton" href="javascript:clearVerify()">拒绝</a> &nbsp;&nbsp; 
		    			</td>
		    	  </tr>
				</table>
			</form>
		</div>
	</div>
	</div>
</body>
</html>
