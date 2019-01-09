<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.module.sys.bean.User"%>
<%@ page import="com.yzxt.yh.module.sys.bean.Customer"%>
<%@ page import="com.yzxt.yh.util.DateUtil"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%
User user= (User)request.getAttribute("user");
Customer customer= (Customer)request.getAttribute("cust");
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../../common/inc.jsp"%>
	<title>家庭管理</title>
	<script type="text/javascript">
	var familyMngGrid;
	$(function() {
		familyMngGrid = $("#familyMng").datagrid({
		title: "家庭列表",
		width: "auto",
		height: "auto",
		loadMsg: "数据加载中,请稍候...",
		url: '<%=request.getContextPath()%>/sys/family_queryFamily.action',
		idField: "memberId",
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
			{field:'applyName',title:'姓名',width:80,align:'center'},
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
			{field:'phone',title:'联系电话',width:85,align:'center'},
			/* {field:'age',title:'年龄',width:40,align:'center'}, */
			/* 
			{field:'state',title:'申请状态',width:100,align:'center',formatter:function(value){
				if(value==1){
					return '待审核';
				}else if(value==2){
					return '已通过';
				}
			}}, */
			{field:'memo',title:'备注',width:85,align:'center'}/* ,
			
			{field:'auditTime',title:'申请时间',width:100,align:'center'},
			{field:'applyTime',title:'审核通过时间',width:100,align:'center'} */
		]],
		toolbar: "#toolbar"
	});
	var p = $('#familyMng').datagrid('getPager');
		if (p){
			$(p).pagination({
				pageNumber : 1,
				showPageList:false
			});
		}
		
	});

		
		
	function addWin(){
		var selected = $('#familyMng').datagrid("getSelected");
		if(selected){
			var selections = $('#familyMng').datagrid("getSelections");
			if(selections.length==1){
				window.open("<%=request.getContextPath()%>/sys/cust_toPersonalDetail.action?operType=pd&id=" + selected.memberId, "custDetail");
			}else {
				$.messager.alert("提示信息", "一次只能编辑一行数据", "info");
				refreshGrid();
			}
		}else {
				$.messager.alert('提示信息', '请选择行', 'info');
		}
	}
	
	function deleteFamily(){
		var selected = $('#familyMng').datagrid("getSelected");
		if(selected)
		{
			var selections = $('#familyMng').datagrid("getSelections");
			if(selections.length==1)
			{
				$.messager.confirm("警告信息", "确定解除绑定吗?", function(r){
					if(r)
					{
						$.ajax({
							type: "GET",
							dataType: "json",
							url: "<%=request.getContextPath()%>/sys/family_deleteMem.action?id="+selected.id,
							async: false,
							timeout: 30000,
							success: function(data)
							{
								if(data.state > 0){
									$.messager.alert("提示信息", data.msg?data.msg:"删除成功。", "info", function(){
										try{
												refreshGrid();
										}catch(e){}
											/* closeIt(); */
									});
								}else{
									$.messager.alert("提示信息", data.msg?data.msg:"删除失败。", "error");
								}
							},
							error: function(){
								$.messager.alert("提示信息", "删除失败.", "error");
							}
						});
					}
				});
			}
			else 
			{
				$.messager.alert("提示信息", "一次只能编辑一行数据", "info");
				refreshGrid();
			}
		}
		else
		{
				$.messager.alert('提示信息', '请选择行', 'info');
		}
	}
		
	function addMemWin(){
		subWin = $('<div><iframe src="<%=request.getContextPath()%>/pgchr/custWarnings.jsp" style="width: 99%;height:99%;border="0" frameborder="0"></iframe></div>').window({
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
			var selected = $('#familyMng').datagrid("getSelected");
			if(selected){
				var selections = $('#familyMng').datagrid("getSelections");
				if(selections.length==1){
					$('#familyMngWin').window({title:'修改医生信息',width:'500',height:'430',resizable:false,
						collapsible:false,
						minimizable:false,
						maximizable:false,
						modal:true,
						closable:true});
					$('#familyMngWin').window('center');
					$("#familyMngForm").form("clear");
					$("#familyMngForm").form("reset");
					$("#handtype").val("1");
					$("#account").attr("readonly",true);
					$("#familyMngId").val(selected.Id);
					$("#account").val(selected.account);
					$("#password").val(selected.password);
					$("#familyMngName").val(selected.name);
					$("input[name='familyMng.sex'][value="+selected.sex+"]").attr("checked",true);
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
					$('#familyMngWin').window('open');
					$('#familyMngForm').form('validate');
				}else{
					$.messager.alert("提示信息","一次只能编辑一行数据","info");
					$('#familyMng').datagrid("uncheckAll");
					$('#familyMng').datagrid("unselectAll");
					$('#familyMng').datagrid("clearSelections");
				}
			}else{
				$.messager.alert('提示信息','请选择编辑的行','info');
			}
		}
		
	function refreshGrid(){
			$('#familyMng').datagrid("reload");
			$('#familyMng').datagrid("unselectAll");
			$('#familyMng').datagrid("clearSelections");
		}
	
	function save(){
		    $('#familyMngWin').window('close');
		    refreshGrid();
		}
		
	//关闭窗口
	function clear(){
			$('#familyMngWin').window('close');
			$("#familyMngForm").form("clear");
			$("#familyMngForm").form("reset");
		}
		
	
	</script>
</head>
<body>
	<div style="padding-top: 10px;">
		<div class="sectionTitle">账户信息</div>
			<div>
				<form>
					<table class="table">
						<tr>
							<td class="td_title">用户名：</td>
							<td>
								<%=StringUtil.trim(user.getName())%>
						
							</td>
							<td  class="td_title">性别：</td>
							<td>
							<s:if test="cust.sex == 1 ">
						         	男
							</s:if>
							<s:else>
								女
						    </s:else>
							</td>
						</tr>
						<tr>
							<td class="td_title">出生日期：</td>
							<td>
								<%=DateUtil.toHtmlDate(customer.getBirthday())%>
							</td>
							<td class="td_title">联系电话：</td>
							<td>
								<%=StringUtil.trim(user.getPhone())%>
							</td>
						</tr>
						<tr>
							<td class="td_title">家庭地址：</td>
							<td>
								<%=StringUtil.trim(customer.getAddress())%>
							</td>
							<td class="td_title">邮箱：</td>
							<td>
								<%=StringUtil.trim(user.getEmail())%>
							</td>
						</tr>
					</table>
				</form>
			</div>
	</div>
	<div style="padding-top: 10px;">
		<table id="familyMng" class="easyui-datagrid"></table>
	</div>
	<div id="toolbar" style="padding:2px 0;display: none;">
		<table style="width:100%;">
			<tr>
				<td style="padding-left:2px">
					<a href="javascript:addWin()" class="easyui-linkbutton">查看成员信息</a>&nbsp;&nbsp;
					<a href="javascript:deleteFamily()" class="easyui-linkbutton">删除家庭成员</a>
				</td>
				<td style="padding-left:2px">
					
				</td>
			</tr>
		</table>
	</div>
	<div style="display: none;">
	<div id="familyMngWin" title="申请添加成员" class="easyui-window" style="padding: 12px;"  data-options="modal:true,closed:true">
		<div>
			<form id="familyMngForm" action="" method="post">
				<table class ="table" border="0">
				  <tr>
				    <td width="35%" class="td_title" align="right" nowrap="nowrap">电话号码：</td>
				     <td>
				    	<input type="text" maxlength="100"  id="resiName" value="18179135555" onclick="openSelResi()" />
						
					</td>
				  </tr>
				  <tr>
				    <td class="td_title" align="right" nowrap="nowrap">验证信息：</td>
				    <td >
		    			<textarea style="height:100px;" rows="" cols=""></textarea>
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
