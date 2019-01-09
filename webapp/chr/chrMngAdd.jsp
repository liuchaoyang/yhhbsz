<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>慢病管理</title>
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
			{field:'type',title:'管理项目',width:60,align:'center',formatter:function(value){
				var sex = '';
				if(value==1){
					sex='血压';
				}else if(value==2){
					sex='血糖';
				}else if(value==3){
					sex='心脑血管疾病';
				}
				return sex;
			}},
			{field:'createTime',title:'纳入管理时间',width:150,align:'center'},
			{field:'preFlupTime',title:'上次随访时间',width:100,align:'center'},
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
			}},
			{field:'nextFlupTime',title:'下次随访时间',width:100,align:'center'}
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
	//增加栏目
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
	//关闭窗口
	function clear(){
			$('#chrWin').window('close');
			$("#chrForm").form("clear");
			$("#chrForm").form("reset");
		} 
		
	var subWin1;
	function addMngWin(){
		subWin1 = $('<div><iframe src="<%=request.getContextPath()%>/msg/consultGuide_toAdd.action" style="width: 99%;height: 99%;" border="0" frameborder="0"></iframe></div>').window({
			title: "纳入慢病",
			width: 500,
			height: 250,
			resizable: false,
			collapsible: false,
			minimizable: false,
			maximizable: false,
			modal: true,
			zIndex: 100,
			closable: true
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
					<td width="15%" class="td_title">管理项目:</td>
					<td>
						<select id="checkTwo" name="manageType">
							<option value="">请选择</option>
							<option value="1">血压</option>
							<option value="2">血糖</option>
							<option value="3">心脑血管疾病</option>
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
				     <td  style="width: 200px;">
				    	<input type="text" maxlength="100" readonly="readonly" id="resiName" value="唐小林" onclick="openSelResi()" />
						<img id="selUserImg" alt="选择居民" src="<%=request.getContextPath()%>/resources/img/search.png" onclick="addMemWin()" style="cursor: pointer;margin-bottom: -5px;" />
					</td>
				  </tr>
				  <tr>
				    <td class="td_title" align="right" nowrap="nowrap">慢病类型：</td>
				    <td  style="width: 200px;">
		    			<input  type="checkbox" id="xueya" name="type"  value="1" checked="checked"/>血压 &nbsp;
				      	<input  type="checkbox" id="xuetang" name="type" value="2"/>血糖
				      	<input  type="checkbox" id="xinnao" name="type" value="3"/>心脑血管疾病
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
</body>
</html>