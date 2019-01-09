<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../../common/inc.jsp"%>
	<title>医生管理</title>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script> 
	<script type="text/javascript">
	// 页面编辑状态，1查看，2新增，3修改
	var editState;
	var doctorInfoGrid;
	$(function() {
		doctorInfoGrid = $("#doctorInfo").datagrid({
		title: "医生列表",
		width: "auto",
		height: "auto",
		loadMsg: "数据加载中,请稍候...",
		url: '<%=request.getContextPath()%>/sys/doctor_list.action',
		idField: "userId",
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
			{field:'doctorName',title:'姓名',width:80,align:'center'},
			{field:'account',title:'用户名',width:80,align:'center'},
			{field:'sex',title:'性别',width:40,align:'center',formatter:function(value){
				if(value==0){
					return '男';
				}else if(value==1){
					return '女';
				}else if(value==2){
					return '未知性别';
				}else if(value==9){
					return '未说明性别';
				}
			}},
			{field:'birthday',title:'出生日期',width:80,align:'center',formatter:function(v){
				if(v!=null){
					return v.substring(0,10);
				}
			}},
			{field:'phone',title:'移动电话',width:85,align:'center'},
			{field:'address',title:'地址',width:120,align:'center'},
			{field:'degree',title:'文化程度',width:100,align:'center',formatter:function(value){
				if(value==1){
					return '文盲及半文盲';
				}else if(value==2){
					return '小学';
				}else if(value==3){
					return '初中';
				}else if(value==4){
					return '高中/技校/中专';
				}else if(value==5){
					return '大学专科及以上';
				}else if(value==6){
					return '不详';
				}else{
					return value;
				}
			}},
			{field:'orgName',title:'隶属组织',width:120,align:'center'},
			/* {field:'professionalTitle',title:'职称',width:80,align:'center'},
			{field:'deptName',title:'科室',width:80,align:'center'},
			{field:'skillInfo',title:'擅长',width:200,align:'center'},
			{field:'describe',title:'简介',width:80,align:'center'}, */
			{field:'createTime',title:'创建时间',width:120,align:'center',formatter:function(v){
				if(v!=null){
					return v.substring(0,10);
				}
			}},
			{field:'updateTime',title:'修改时间',width:120,align:'center',formatter:function(v){
				if(v!=null){
					return v.substring(0,10);
				}
			}}
		]],
		toolbar: "#toolbar"
	});
	var p = $('#doctorInfo').datagrid('getPager');
		if (p){
			$(p).pagination({
				pageNumber : 1,
				showPageList:false
			});
		}
	});
	
	function addWin(){
			$('#doctorWin').window({title:'新增医生信息',width:'520',height:'410',resizable:false,
				collapsible:false,
				minimizable:false,
				maximizable:false,
				modal:true,
				zIndex:100,
				closable:true});
			$('#doctorWin').window('center');
			$("#doctorForm").form("clear");
			$("#doctorForm").form("reset");
			
			$("#handtype").val("0");
			$("#account").removeAttr("readonly");
			$('#deptId').combotree({
				url:'',
				onBeforeExpand : function(row, param) {
					$('#deptId').combotree('tree').tree('options').url='';
				},
				onLoadSuccess:function(){
					$('#deptId').combotree("clear");
				}
			});
			editState = 2;
			$('#doctorWin').window('open');
		}
	function disable(){
		$('#orgId').combotree('disable');
	}
	function updateWin(){
		debugger
		var selections = $('#doctorInfo').datagrid("getSelections");
		if(selections && selections.length==1){
			var selected = selections[0];
			$('#doctorWin').window({title:'修改医生信息',width:'520',height:'410',resizable:false,
				collapsible:false,minimizable:false,maximizable:false,modal:true,closable:true});
			$('#doctorWin').window('center');
			$("#doctorForm").form("clear");
			$("#doctorForm").form("reset");
			$("#handtype").val("1");
			$("#account").attr("readonly",true);
			$("#doctorId").val(selected.userId);
			$("#account").val(selected.account);
			$("#doctorName").val(selected.doctorName);
			$("input[name='doctor.sex'][value="+selected.sex+"]").attr("checked",true);
			$("input[name='doctor.type'][value="+selected.type+"]").attr("checked",true);
			$("#birthday").val(selected.birthday ? selected.birthday.substring(0,10) : "");
			$("#phone").val(selected.phone);
			$("#email").val(selected.email);
			$("#address").val(selected.address);
			$("#degree").combobox('select',selected.degree);
			$("#professionId").combobox({
				editor:false,
				onLoadSuccess:function(){
					$(this).combobox('select',selected.professionId_id)
				}
			});
			$("#professionTitle").val(selected.professionTitle);
			$("#deptName").val(selected.deptName);
			$("#skillInfo").val(selected.skillInfo);
			$("#describe").val(selected.describe);
			$("#price").val(selected.price);
			//console.log($("#price").val(selected.price));
			console.log(selected.price);
			$("#yprice").val(selected.yprice);
			$('#orgId').combotree({
				editor:false,
				onLoadSuccess:function(){
					$('#orgId').combotree('setValue', selected.orgId);
					$('#orgId').combotree('setText', selected.orgName);
				}
			});
			$('#orgId').combotree('disable');
			editState = 3;
			$('#doctorWin').window('open');
			$('#doctorForm').form('validate');
		}else{
			$.messager.alert('提示信息','请选择编辑的一行','info');
		}
	}
		
	function refreshGrid(){
			$('#doctorInfo').datagrid("reload");
			$('#doctorInfo').datagrid("unselectAll");
			$('#doctorInfo').datagrid("clearSelections");
		}
		
	function searchData(){
			var checkOneVal = $("#checkOne").val();
			/* var checkTwoVal = $("#checkTwo").val(); */
			$('#doctorInfo').datagrid("getPager").pagination({pageNumber : 1});
			var option = $('#doctorInfo').datagrid("options");
			option.pageNumber = 1;
			option.url = "<%=request.getContextPath()%>/sys/doctor_list.action";
			<%-- option.url = "<%=basePath%>sys/doctor_getHMList.action"; --%>
			var queryParams = option.queryParams;
			queryParams.name = checkOneVal;
			/* queryParams.belongSubInst = checkTwoVal; */
			refreshGrid();
		}
	
	function resetFilter(){
			$("#checkOne").val("");
			$("#checkTwo").val("");
		}
	
	//保存医生信息
	function save(){
		if(editState == 2){
			var account = document.getElementById("account");
			if(account.value.length > 20){
				$.messager.alert("提示信息", "登录账号长度不能超过20个！", "error", function(){
					account.focus();
				});
				return;
			}
			var accountVal = $.trim(account.value);
			if(!accountVal){
				$.messager.alert("提示信息", "请输入登录账号.", "error", function(){
					account.focus();
				});
				return;
			}else if(!is.account(accountVal)){
				$.messager.alert("提示信息", "登录帐号不正确，只能以字母开头，由字母、数字和下划线组成，长度为6-20.", "error", function(){
					account.focus();
				});
				return;
			}
		}
	 	var doctorName = $("#doctorName");
		var doctorNameVal = $.trim(doctorName.val());
		if(!doctorNameVal){
			$.messager.alert("提示信息", "请输入姓名.", "info");
			return;
		} 
		var doctorName = document.getElementById("doctorName");
		if(doctorName.value.length > 20){
				$.messager.alert("提示信息", "医生名字字数不能超过20个！", "error", function(){
					doctorName.focus();
				});
				return;
			}
		// 移动电话
		var phone = document.getElementById("phone");
		var phoneVal = $.trim(phone.value);
		if(!phoneVal){
			$.messager.alert("提示信息", "请输入移动电话.", "error", function(){
				phone.focus();
			});
			return;
		}else if(!is.mPhone(phoneVal)){
			$.messager.alert("提示信息", "移动电话不正确.", "error", function(){
				phone.focus();
			});
			return;
		}
		if(phone.value.length > 15){
			$.messager.alert("提示信息", "电话号码字数不能超过15个！", "error", function(){
				phone.focus();
			});
			return;
		}
		//邮箱必填
		var email = document.getElementById("email");
		var emailVal = $.trim(email.value);
		if(!emailVal){
			$.messager.alert("提示信息", "请输人邮箱.", "error", function(){
				email.focus();
			});
			return;
		}else if(!is.email(emailVal)){
			$.messager.alert("提示信息", "邮箱不正确.", "error", function(){
				email.focus();
			});
			return;
		}
		if(email.value.length > 30){
			$.messager.alert("提示信息", "邮箱字数不能超过30个！", "error", function(){
				email.focus();
			});
			return;
		}
		// 所属机构
		var orgVal = $("#orgId").combotree("getValue");
		if(!orgVal){
			$.messager.alert("提示信息", "请选择隶属机构.", "error");
			return;
		}
		var address=document.getElementById("address");
		if(address.value.length > 100){
			$.messager.alert("提示信息", "地址字数不能超过100个！", "error", function(){
				address.focus();
			});
			return;
		}
		var skillInfo=document.getElementById("skillInfo");
		if(skillInfo.value.length > 100){
			$.messager.alert("提示信息", "擅长字数不能超过100个！", "error", function(){
				skillInfo.focus();
			});
			return;
		}
		
		var describe=document.getElementById("describe");
		if(describe.value.length > 200){
			$.messager.alert("提示信息", "简介字数不能超过200个！", "error", function(){
				describe.focus();
			});
			return;
		}
		var yprice = document.getElementById("yprice");
		var ypriceVal = $.trim(yprice.value);
		if(!ypriceVal){
			$.messager.alert("提示信息", "请输入服务费原价.", "error", function(){
				yprice.focus();
			});
			return;
		}
		var price = document.getElementById("price");
		var priceVal = $.trim(price.value);
		if(!priceVal){
			$.messager.alert("提示信息", "请输入促销价.", "error", function(){
				price.focus();
			});
			return;
		}
		var saveUrl ="";
		if(editState == 2){
			saveUrl = "<%=request.getContextPath()%>/sys/doctor_addDoctor.action";
		}
		if(editState == 3){
			saveUrl = "<%=request.getContextPath()%>/sys/doctor_updateDoctor.action";
		}
		$("#doctorForm").form("submit", {
			url : saveUrl,
			success : function(r) {
				res =  $.parseJSON(r); 
				if(res.state == "1") {
					$.messager.alert("提示信息",  res.msg?res.msg:"保存成功.", "info");
					clear();
					refreshGrid();
				}else if(res.state == "-1"){
					$.messager.alert("提示信息",  res.msg?res.msg:"保存失败.", "info");
				}
			},
			error : function(msg){
				$.messager.alert("提示信息", msg, "info");
			}
		});
		}
		
	//关闭窗口
	function clear(){
			$("#doctorWin").window("close");
			$("#doctorForm").form("clear");
			$("#doctorForm").form("reset");
		}
	</script>
</head>
<body>
	<div class="sectionTitle">查询条件</div>
	<div>
		<form id="filterForm">
			<table class="table">
				<tr>
					<td width="15%" class="td_title">用户名称:</td>
					<td>
						<input type="text" id="checkOne" name="doctor.name" maxlength="100" style="width: 200px;" />
					</td>
				</tr>
				<tr>
					<td colspan="2" class="td_oper">
						<a href="javascript:void(0)" onclick="searchData()" class="easyui-linkbutton">查询</a> &nbsp;
						<a href="javascript:void(0)" onclick="resetFilter()" class="easyui-linkbutton">重置</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div style="padding-top: 10px;">
		<table id="doctorInfo" class="easyui-datagrid"></table>
	</div>
	<div id="toolbar" style="padding:2px 0;display: none;">
		<table style="width:100%;">
			<tr>
				<td style="padding-left:2px">
					<a href="javascript:addWin()" class="easyui-linkbutton">增加</a>
					<a href="javascript:updateWin()" class="easyui-linkbutton">修改</a>
				</td>
			</tr>
		</table>
	</div>
	<div style="display: none;">
	<div id="doctorWin" title="新增用户信息" class="easyui-window" style="padding: 12px;"  data-options="modal:true,closed:true">
		<div>
			<form id="doctorForm" action="" method="post">
				<table border="0">
					<tr>
		    			<td nowrap="nowrap" align="right">登录账号:</td>
		    			<td>
			    			<input type="text" id="account" name="doctor.user.account" maxlength="20" style="width: 155px;" ><font color="red">*</font>
			    			<input type="hidden" name="doctor.userId" value="" id="doctorId">
		    			</td>
		    			<td nowrap="nowrap" align="right">姓名:</td>
		    			<td>
		    				<input type="text"  id="doctorName" name="doctor.user.name" maxlength="20" style="width: 155px;" ><font color="red">*</font>
		    			</td>
		    		</tr>
		    		<tr>
		    			<td nowrap="nowrap" align="right">性别:</td>
		    			<td>
		    				<input type="radio" class="sex" name="doctor.sex" value="0" checked="checked" />男
		    				&nbsp;
		    				<input type="radio" class="sex" name="doctor.sex" value="1" />女
		    			</td>
		    			<td nowrap="nowrap" align="right">出生日期:</td>
		    			<td>
		    				<input type="text" class="Wdate" style="width: 155px;" id="birthday" name="doctor.birthday" readonly="readonly"
							 maxlength="10" onclick="WdatePicker({dateFmt:'yyyy-MM-dd' })" />
		    			</td>
		    		</tr>
		    		<tr>
		    			<td nowrap="nowrap" align="right">移动电话:</td>
		    			<td>
		    				<input type="text"  maxlength="11"  id="phone" name="doctor.user.phone"  style="width: 155px;" /><font color="red">*</font>
		    			</td>
		    			<td nowrap="nowrap" align="right">邮箱:</td>
		    			<td>
		    				<input type="text"  maxlength="30"  id="email" name="doctor.user.email"  style="width: 155px;" /><font color="red">*</font>
		    			</td>
		    		</tr>
		    		<tr>
		    			<td nowrap="nowrap" align="right">地址:</td>
		    			<td>
		    				<input type="text" id="address" maxlength="100" name="doctor.address" style="width: 155px;" />
		    			</td>
		    			<td nowrap="nowrap" align="right">文化程度:</td>
		    			<td>
		    				<select class="easyui-combobox" name="doctor.degree" id="degree" data-options="width:155,editable:false">
			    				<option value="">请选择</option>
			    				<option value="1">文盲及半文盲</option>
			    				<option value="2">小学</option>
			    				<option value="3">文初中</option>
			    				<option value="4">高中/技校/中专</option>
			    				<option value="5">大学专科及以上</option>
			    				<option value="6">不详</option>
			    			</select>
		    			</td>
		    		</tr>
		    		<tr>
		    			<td nowrap="nowrap" align="right">隶属机构:</td>
		    			<td>
		    				<select id="orgId" name="doctor.user.orgId" class="easyui-combotree" style="width: 156px;"
							data-options="url:'<%=request.getContextPath()%>/sys/org_treeChildren.action',valueField:'id',textField:'name',panelHeight:200,missingMessage:'请选择机构.'"></select>
							<!-- <input type="hidden" id="orgId" name="doctor.user.orgId"  /> --> <font color="red">*</font> 
		    				<!-- <input class="easyui-validatebox" id="orgId" maxlength="100" name="doctor.user.orgId" style="width: 155px;" /><font color="red">*</font> -->
		    			</td>
		    			<td nowrap="nowrap" align="right">职称:</td>
		    			<td>
		    				<input type="text"  id="professionTitle" maxlength="20" name="doctor.professionTitle" style="width: 155px;" />
		    			</td>
		    		</tr>
		    		<tr>
		    			<td nowrap="nowrap" align="right">科室:</td>
		    			<td>
		    				<input type="text"   id="deptName" maxlength="20" name="doctor.deptName" style="width: 155px;" />
		    			</td>
		    			<td nowrap="nowrap" align="right">医生类别:</td>
		    			<td>
		    				<input type="radio" class="type" name="doctor.type" value="1" checked="checked" />全科客服
		    				&nbsp;
		    				<input type="radio" class="type" name="doctor.type" value="2" />家庭医生
		    			</td>
		    		</tr>
		    		<tr>
		    			<td nowrap="nowrap" align="right">服务费原价:</td>
		    			<td>
		    				<input type="text"  maxlength="11"  id="yprice" name="doctor.yprice"  style="width: 155px;" /><font color="red">*</font>
		    			</td>
		    			<td nowrap="nowrap" align="right">促销价:</td>
		    			<td>
		    				<input type="text"  maxlength="11"  id="price" name="doctor.price"  style="width: 155px;" /><font color="red">*</font>
		    			</td>
		    		</tr>
		    		<tr>
		    			<td nowrap="nowrap" align="right">擅长:</td>
		    			<td colspan="3">
		    				<textarea  id="skillInfo" maxlength="100" name="doctor.skillInfo" style="width: 370px;height: 40px;" ></textarea>
		    			</td>
		    		</tr>
		    		<tr>
		    			<td nowrap="nowrap" align="right">简介:</td>
		    			<td colspan="3">
		    				<textarea id="describe"  maxlength="200" name="doctor.describe" style="width: 370px;height: 80px;"></textarea>
		    			</td>
		    		</tr>
		    		<tr>
		    			<td colspan="4" align="center" style="padding-top: 5px;">
		    				<a class="easyui-linkbutton" href="javascript:save()">保存</a>
		    				<a class="easyui-linkbutton" href="javascript:clear()">返回</a>
		    			</td>
		    		</tr>
				</table>
			</form>
		</div>
	</div>
	</div>
</body>
</html>
