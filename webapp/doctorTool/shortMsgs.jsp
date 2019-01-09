<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>短信互动</title>
	<script type="text/javascript">
	var chrMngGrid;
	var subWin;
	$(function() {
		chrMngGrid = $("#chrMng").datagrid({
		title: "短信互动",
		width: "auto",
		height: "auto",
		loadMsg: "数据加载中,请稍候...",
		url: '<%=request.getContextPath()%>/msg/shotmsg_query.action',
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
			{field:'name',title:'会员名称',width:80,align:'center'},
			{field:'phone',title:'手机号',width:100,align:'center'},
			{field:'msgContext',title:'短信内容',width:200,align:'center'},
			{field:'createByName',title:'发送人',width:80,align:'center'},
			{field:'state',title:'发送状态',width:90,align:'center',formatter:function(value){
				var sex="";
				if(value==2){
					sex="发送成功";
				}else if(value==3){
					sex="发送失败";
				}else if(value==1){
					sex="待发送";
				}
				return sex;
			}},
			{field:'createTime',title:'发送时间',width:110,align:'center'}
		]],
		toolbar: "#toolbar"
	});
	var p = $('#chrMng').datagrid('getPager');
		if (p){
			$(p).pagination({
				pageNumber : 1,
				showPageList:false
			});
		}
	});
	
	function refreshGrid(){
			$('#chrMng').datagrid("reload");
			$('#chrMng').datagrid("unselectAll");
			$('#chrMng').datagrid("clearSelections");
		}
		
	function searchData(){
			var checkOneVal = $("#checkOne").val();
			var checkTwoVal = $("#checkTwo").val();
			$('#chrMng').datagrid("getPager").pagination({pageNumber : 1});
			var option = $('#chrMng').datagrid("options");
			option.pageNumber = 1;
			
			var queryParams = option.queryParams;
			queryParams.name = checkOneVal;
			queryParams.belongSubInst = checkTwoVal;
			refreshGrid();
		}
	
	function resetFilter(){
			$("#checkOne").val("");
			$("#checkTwo").val("");
		}
	
	function validForm(){
		var custId = document.getElementById("custId");
		if(!custId.value){
			$.messager.alert("提示信息", "请选择接收短信会员。", "error");
			return;
		}
		var msgContext = document.getElementById("msgContext");
		var msgContextVal = msgContext.value;
		if(!msgContextVal){
			$.messager.alert("提示信息", "请输入短信内容。", "error", function(){
				msgContext.focus();
			});
			return false;
		}
		if(msgContextVal.length>126){
			$.messager.alert("提示信息", "短信内容不能超过126个字符。", "error", function(){
				msgContext.focus();
			});
			return false;
		}
		return true;
	}
	
	function save(){
		if(!validForm()){
			return;
		}
		$("#filterFormMsg").form("submit", {
			url: "<%=request.getContextPath()%>/msg/shotmsg_add.action",
			dataType : "json",
			success : function(data) {
				data =  $.parseJSON(data);
				if(data.state == 1){
					$.messager.alert("提示信息", data.msg?data.msg:"发送成功。", "info", function(){
						try{
							closeIt();
						}catch(e){}
						closeIt();
					});
				}else{
					$.messager.alert("提示信息", data.msg?data.msg:"发送失败。", "error");
				}
				
			}
		});
	}
	//增加栏目
	function addMngWin(){
			$('#chrWin').window({title:'发送短信',width:'540',height:'240',resizable:false,
				collapsible:false,
				minimizable:false,
				maximizable:false,
				modal:true,
				zIndex:100,
				closable:true});
			$('#chrWin').window('center');
			$("#filterFormMsg").form("clear");
			$("#filterFormMsg").form("reset");
			$('#chrWin').window('open');
		}
	
	function addMemWin(){
		subWin = $('<div><iframe src="<%=request.getContextPath()%>/doctorTool/addUser.jsp?winName=subWin" style="width: 99%;height:99%;border="0" frameborder="0"></iframe></div>').window({
			title : "选择会员",
			width : 600,
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
	//关闭窗口
	function clear(){
			$('#chrWin').window('close');
			$("#chrForm").form("clear");
			$("#chrForm").form("reset");
		} 

//赋值
	function assignment(name,phone,userId){
			$("#name").val(name);
			$("#custId").val(userId);
			$("#phone").val(phone);
		} 
		
		function closeIt(){
		$('#chrWin').window('close');
		    refreshGrid();

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
						<input type="text" id="checkOne" name="name" maxlength="100" style="width: 200px;" />
					</td>
					<td  width="140px;" class="td_oper" nowrap="nowrap">
						<a href="javascript:void(0)" onclick="searchData()" class="easyui-linkbutton">查询</a> &nbsp;
						<a href="javascript:void(0)" onclick="resetFilter()" class="easyui-linkbutton">重置</a>
					</td>
				</tr>
				
			</table>
		</form>
	</div>
	<div style="padding-top: 10px;">
		<table id="chrMng" class="easyui-datagrid"></table>
	</div>
	<div id="toolbar" style="padding:2px 0;display: none;">
		<table style="width:100%;">
			<tr>
				<td style="padding-left:2px">
					<a href="javascript:addMngWin()" class="easyui-linkbutton">发送短信</a>
				</td>
			</tr>
		</table>
	</div>
	<div style="display: none;">
		<div id="chrWin" title="发送短信" class="easyui-window" style="padding: 1px;"  data-options="modal:true,closed:true">
			<form id="filterFormMsg" style="margin: 0px;padding: 0px;" method="post" accept-charset="UTF-8">
				<table border="0" style="padding: 0px;margin: 0px;" class="table">
					<tr>
						<td class="td_title" align="right" nowrap="nowrap">会员名称：</td>
						<td  style="width: 205px;">
							<input type="text" maxlength="100" readonly="readonly" id="name" name="shortMsg.name" />
							<input type="hidden" id="custId" name="shortMsg.custId">
							<img id="selUserImg" alt="选择用户" src="<%=request.getContextPath()%>/resources/img/search.png" onclick="addMemWin()" style="cursor: pointer;margin-bottom: -5px;" />
						</td>
						<td class="td_title" align="right" nowrap="nowrap">手机号：</td>
						<td  style="width: 205px;">
							<input type="text" id="phone"  readonly="readonly" name="shortMsg.phone" maxlength="100"   />
						</td>
					</tr>
					<tr>
						<td  class="td_title" align="right" nowrap="nowrap">编辑内容：</td>
						<td colspan="3">
							<textarea maxlength="126" style="width: 400px;height: 85px;" id="msgContext" name="shortMsg.msgContext"></textarea>
						</td>
					</tr>
				</table>
			</form>
			<div style="text-align: center;padding-top: 10px;">
				<a class="easyui-linkbutton" href="javascript:save()">发送</a>
				&nbsp;
				<a class="easyui-linkbutton" href="javascript:clear()">返回</a>
			</div>
		</div>
	</div>
</body>
</html>