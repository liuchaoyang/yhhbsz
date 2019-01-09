<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>慢病管理</title>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script> 
	<script type="text/javascript">
	var chrMngGrid;
	var subWin;
	$(function() {
		chrMngGrid = $("#chrMng").datagrid({
		title: "慢病管理",
		width: "auto",
		height: "auto",
		loadMsg: "数据加载中,请稍候...",
		url: "<%=request.getContextPath()%>/chr/chrManage_getChrList.action",
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
			{field:'memberName',title:'用户名称',width:100,align:'center'},
			/* {field:'achIdCard',title:'身份证号',width:100,align:'center'}, */
			{field:'type',title:'慢病类型',width:60,align:'center',formatter:function(value){
				var sex = '';
				if(value==1){
					sex='高血压';
				}else if(value==2){
					sex='糖尿病';
				}/* else if(value==3){
					sex='心脑血管疾病';
				} */
				return sex;
			}},
			{field:'createTime',title:'纳入管理时间',width:150,align:'center'} ,
			{field:'lastFlupTime',title:'上次随访时间',width:100,align:'center',formatter:function(v){
				if(v!=null){
						return v.substring(0,10);
					}
				}
			},/*
			{field:'grade',title:'随访危险分级',width:100,align:'center',formatter:function(value){
				var sex = '';
				if(value==1){
					sex='低';
				}else if(value==2){
					sex='中';
				}else if(value==3){
					sex='高';
				}else if(value==4){
					sex='极高';
				}
				return sex;	
			}},*/
			{field:'nextFlupTime',title:'下次随访时间',width:100,align:'center',formatter:function(v){
				if(v!=null){
						return v.substring(0,10);
					}
				}
			} 
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
			option.url = "<%=request.getContextPath()%>/chr/chrManage_getChrList.action";
			var queryParams = option.queryParams;
			queryParams.memberName = checkOneVal;
			queryParams.manageType = checkTwoVal;
			refreshGrid();
		}
	
	function resetFilter(){
			$("#checkOne").val("");
			$("#checkTwo").val("");
		}
	
	function save(){
		    /* $('#doctorWin').window('close'); */
		    $('#chrWin').window('close');
		    refreshGrid();
		}
		
	//下达随访
	function showVisit(){
		var selected = $('#chrMng').datagrid("getSelected");
		if(selected){
		var selections = $('#chrMng').datagrid("getSelections");
			if(selections.length==1){
				$('#visitWindow').window({title:'下达随访计划',width:'350',height:'200',resizable:false,
					collapsible:false,
					minimizable:false,
					maximizable:false,
					modal:true,
					zIndex:100,
					closable:true});
				$('#visitWindow').window('center');
				$("#visitForm").form("clear");
				$("#visitForm").form("reset");
				$("#custId").val(selected.custId);
				$("#grade").val(selected.grade);
				$("#visitType").val(selected.type);
				$('#visitWindow').window('open');
			}else {
				$.messager.alert("提示信息", "一次只能对一个人下达.", "error");
			}
		}else{
			$.messager.alert("提示信息", "请选择一条记录.", "error");
		}
	}	
	//关闭随访计划
	function closeVisit(){
		$('#visitWindow').window('close');
		$("#visitForm").form("clear");
		$("#visitForm").form("reset");
	}	
	
	function saveVisit(){
		var planTime = document.getElementById("planTime");
		var planTimeVal = $.trim(planTime.value);
		planTime.value = planTimeVal;
		if(!planTimeVal){
			$.messager.alert("提示信息", "请选择随访时间。", "error", function(){
				planTime.focus();
			});
			return;
		}
		$("#visitForm").form("submit", {
			url : "<%=request.getContextPath()%>/chr/chrVisit_addVisitPlan.action",
			onSubmit : function() {
				return true;
			},
			success : function(data) {
				data =  $.parseJSON(data);
				if(data.state > 0){
					$.messager.alert("提示信息", "保存成功.", "info", function(){
						try{
							parent.refreshGrid();
						}catch(e){}
						closeVisit();
					});
				}else{
					$.messager.alert("提示信息", data.msg, "error");
				}
			}
		});
	}
	
	function showDetail(){
		var selected = $('#chrMng').datagrid("getSelected");
		if(selected){
		var selections = $('#chrMng').datagrid("getSelections");
			if(selections.length==1){
				var urls = "<%=request.getContextPath()%>/chr/chrVisit_toMyVisit.action?custId="+ selected.custId;
	       		var newTitle = "随访明细";
	       		top.addTab(newTitle,urls,null);
			}else {
				$.messager.alert("提示信息", "一次只能查看一个人的随访.", "error");
			}
		}else{
			$.messager.alert("提示信息", "请选择一条记录.", "error");
		}
	}	
		
	//慢病人员
	function addMngWin(){
			$('#chrWin').window({title:'添加慢病人员',width:'350',height:'200',resizable:false,
				collapsible:false,
				minimizable:false,
				maximizable:false,
				modal:true,
				zIndex:100,
				closable:true});
			$('#chrWin').window('center');
			$("#chrForm").form("clear");
			$("#chrForm").form("reset");
			$("#handtype").val("0");
			$('#chrWin').window('open');
		}
	
	var subWin;
	function openSelResi(){
		<%--先选择一个居民，然后跳转到新增页面
			parent.
		--%>
		subWin = $('<div><iframe src="<%=request.getContextPath()%>/msg/menbersSel.jsp" style="width: 99%;height:99%;" border="0" frameborder="0" scoll="no"></iframe></div>').window({
			title: "选择居民",
			width: 600,
			height: 400,
			resizable: false,
			collapsible: false,
			minimizable: false,
			maximizable: false,
			modal: true,
			zIndex: 101,
			closable: true
		});
	};
	
	function closeSelUserWin(){
		subWin.window("close");
	}
	
	function setSelUser(cust){
		$("#userId").val(cust.userId);
		$("#name").val(cust.name);
	}
	//关闭窗口
	function clear(){
			$('#chrWin').window('close');
			$("#chrForm").form("clear");
			$("#chrForm").form("reset");
		} 
	
	//纳入慢病	
	function save(){
	 	var name = $("#name");
		var nameVal = $.trim(name.val());
		if(!nameVal){
			$.messager.alert("提示信息", "请选择会员.", "info");
			return;
		}
		//授权 如果二个都没有选择，则给出提示，不能保存
		var checkboxArr = document.getElementsByName("mngType");
		/* var resRole=$("#resid");
		var resMemRole=$("#residMem"); */
		//心脑血管：&&(!checkboxArr[2].checked)
		if((!checkboxArr[0].checked) &&(!checkboxArr[1].checked)){
			$.messager.alert("提示信息", "请选择慢病类型.", "info");
			return;
		} 
		$("#chrForm").form("submit", {
			url: "<%=request.getContextPath()%>/chr/chrManage_addChr.action",
			dataType : "json",
			success : function(data) {
				data =  $.parseJSON(data); 
				if(data.state == 1){
					$.messager.alert("提示信息", data.msg?data.msg:"纳入慢病成功。", "info", function(){
						try{
							parent.refreshGrid();
						}catch(e){}
						clear();
						refreshGrid()
					});
				}else{
					$.messager.alert("提示信息", data.msg?data.msg:"纳入慢病失败。", "error");
				}
			}
		});
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
						<input type="text" id="checkOne" name="memberName" maxlength="100" style="width: 200px;" />
					</td>
					<td width="15%" class="td_title">慢病类型:</td>
					<td>
						<select id="checkTwo" name="manageType">
							<option value="">请选择</option>
							<option value="1">高血压</option>
							<option value="2">糖尿病</option>
							<!-- <option value="3">心脑血管疾病</option> -->
						</select>
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
					<a href="javascript:addMngWin()" class="easyui-linkbutton">纳入慢病</a>
					<a href="javascript:showVisit()" class="easyui-linkbutton" >下达随访任务</a>
					<a href="javascript:showDetail()" class="easyui-linkbutton" >随访详情</a>
				</td>
			</tr>
		</table>
	</div>
	<div style="display: none;">
		<div id="chrWin" title="添加慢病人员" class="easyui-window" style="padding: 12px;"  data-options="modal:true,closed:true">
			<form id="chrForm" action="" method="post">
			<input type="hidden" id="id" name="id" />
				<table border="0">
				  <tr>
				    <td class="td_title" align="right" nowrap="nowrap">会员名称：</td>
				     <td  nowrap="nowrap" >
				    	<input type="text" id="name" name="manage.memberName" maxlength="20" value="" disabled="disabled" data-options="" style="width: 135px;" /><img id="selUserImg" alt="选择会员" src="<%=request.getContextPath()%>/resources/img/search.png" onclick="openSelResi()" style="cursor: pointer;margin-bottom: -5px;" />
						<input type="hidden" id="userId" name="chrManage.custId" value="" />
					</td>
				  </tr>
				  <tr>
				    <td class="td_title" align="right" nowrap="nowrap">慢病类型：</td>
				    <td  style="width: 200px;">
		    			<input  type="checkbox" id="xueya" name="mngType"  value="1" />高血压 &nbsp;
				      	<input  type="checkbox" id="xuetang" name="mngType" value="2"/>糖尿病
				      	<!-- <input  type="checkbox" id="xinnao" name="mngType" value="3"/>心脑血管疾病 -->
		    		</td>
				   </tr>
				   <tr> <td>&nbsp;</td></tr>
					<tr>
		    			<td colspan="4" align="center">
		    				<a class="easyui-linkbutton" href="javascript:save()">保存</a> &nbsp;&nbsp; 
		    				<a class="easyui-linkbutton" href="javascript:clear()">返回</a> &nbsp;&nbsp; 
		    			</td>
		    	  </tr>
				</table>
		   </form>
		</div>
	</div>
	<div style="display:none">
		<div id="visitWindow" class="easyui-window" title="随访时间设置" data-options="modal:true,minimizable:false,maximizable:false,collapsible:false,closed:true,iconCls:'icon-save'" style="width:300px;height:200px;padding:10px;">
			<form id="visitForm" action="" method="post">
				<input type="hidden" id="custId" name="chrVisit.custId" />
				<input type="hidden" id="grade" name="chrVisit.grade" />
				<input type="hidden" id="visitType" name="chrVisit.type" />
				<table width="100%">
					<tr><td>随访时间:</td>
					<td><input id="planTime" name ="chrVisit.planFlupTime" class="Wdate" onClick="WdatePicker({minDate:'%y-%M-%d'})" style="height:25px;width:150px"/></input></td></tr>
					<tr>
						<td colspan="2">&nbsp;</td>
					</tr>
					<tr>
					<td colspan="2" align="center">
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="saveVisit();">确认</a>
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="closeVisit();">退出</a></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
</body>
</html>