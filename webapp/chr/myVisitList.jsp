<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import ="java.util.Date"%>
<%@ page import ="com.yzxt.yh.util.DateUtil"%>
<%
Date now = new Date();
String startDateStr = DateUtil.toHtmlDate(DateUtil.addDay(now, -30));
String endDateStr = DateUtil.toHtmlDate(now);
String custId = (String)request.getAttribute("custId");
%>
<!DOCTYPE html >
<html>
	<head>
		<title>随访计划表</title>
		<%@ include file="../common/inc.jsp"%>
		<script type="text/javascript" src="<%=request.getContextPath() %>/chr/chr-common.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script>
		<script type="text/javascript" >
		$(function(){
			$('#gridInfo').datagrid({
				loadMsg : "数据加载中，请稍候……",
				idField:'id',
				width:'auto',
				fitColumns:true,
				rownumbers: true,
				height:'auto',
				pagination:true,
				frozenColumns:[[
	              {field:'ck',checkbox:true}
				]],
				url : "<%=request.getContextPath()%>/chr/chrVisit_getVisitList.action?custId=<%=custId%>",
				columns:[[
					/* {field:'ishandled',title:'状态',width:60,align:'center',formatter:function(value){
						var sex = '';
						if(value==1){
					        sex='<font color="red">未随访</font>';
					    }else if(value==2){
					        sex='已随访';
					    }
					    return sex;	
					}}, */
					/* {field:'memberName',title:'用户名称',width:100,align:'center'}, */
					/* {field:'achIdCard',title:'身份证号',width:100,align:'center'}, */
					{field:'type',title:'管理项目',width:60,align:'center',formatter:function(value){
						var sex = '';
						if(value==1){
					        sex='血压';
					    }else if(value==2){
					        sex='血糖';
					    }else if(value==3){
					    	sex='心脑血管';
					    }
					    return sex;	
					}},
					{field:'planFlupTime',title:'计划随访时间',width:150,align:'center',formatter:function(value){
						if(value){
							return value.substring(0,10);
						}else{
							return "";
						}
						
					}},
					{field:'actualFlupTime',title:'实际随访时间',width:120,align:'center'},
					{field:'grade',title:'危险分级',width:60,align:'center',formatter:function(value){
						var sex = '';
						if(value==-1){
							sex='正常';
						}else if(value==1){
					        sex='低';
					    }else if(value==2){
					        sex='中';
					    }else if(value==3){
					        sex='高';
					    }else if(value==4){
					        sex='极高';
					    }
					    return sex;	
					}}
					
				]],
				toolbar:'#grid-toolbar',
            	onDblClickRow:function(index,row){
					viewDetail(row);
				}
			});
			$('#gridInfo').datagrid('getPager').pagination({
					displayMsg:'当前显示第 {from}至{to}条, 共{total}条',
					showPageList:false,
					 pageSize:10,
					pageNumber:1,
					// pageList : [10, 40, 60, 80, 100],
					showRefresh:true
			});
	});

	function hdclick(){
		var selected = $('#gridInfo').datagrid("getSelected");
		var rows = $('#gridInfo').datagrid("getSelections");
		if(selected && rows.length==1){
			if(selected.ishandled == 1){
				var urls = "<%=request.getContextPath()%>/chr/chrVisit_doVisit.action?id="+ selected.id;
				var newTitle = "处理随访";
		    	top.addTab(newTitle,urls,null);
			}else{
				$.messager.alert("提示","当前信息已经被处理！","info"); 
			}
		}else{
			$.messager.alert("提示","请选择一行进行处理！","info"); 
		}	
	}

	function schview(){
		var selected = $('#gridInfo').datagrid("getSelected");
		/* var rid = selectedValid(true); */
		if(selected){
			var rows = $('#gridInfo').datagrid('getSelections');
			viewDetail(rows[0]);
		}else{
			$.messager.alert("提示","请选择一行进行查看！","info"); 
		}
	}
	function viewDetail(row){
		if(row.ishandled == 1){
			 $.messager.alert('提示','当前信息还未被处理！'); 
		}else{
			if(row.type ==1||row.type=='1'){
				var urls = "<%=request.getContextPath()%>/chr/preInfo_view.action?id="+row.id;
			}else if(row.type ==2||row.type=='2'){
				var urls = "<%=request.getContextPath()%>/chr/glyInfo_view.action?id="+row.id;
			}
			var newTitle = "查看随访";
		    top.addTab(newTitle,urls,null);
		}
	}

	function handleFlupInfo(atname,row){
		if(!atname)atname= '_fowardPage';
		var datas = '{"type":"'+row.type+'","custId":"'+row.achIdCard+'","visitid":"'+row.id+'"}'; 
		if(row.type ==1||row.type=='1'){
			atname = 'glyInfo'+atname;
		}else if(row.type ==2||row.type=='2'){
			atname = 'glyInfo'+atname;
		}
		var urls = "<%=request.getContextPath()%>/chr/glyInfo_view.action?id="+row.id;
	    var newTitle = "查看详情";
	    top.addTab(newTitle,urls,null);
	}
	
	function refreshGrid(){
			$('#gridInfo').datagrid("reload");
			$('#gridInfo').datagrid("unselectAll");
			$('#gridInfo').datagrid("clearSelections");
		}
		
	function searchData(){
			var checkOneVal = $("#checkOne").val();
			var checkTwoVal = $("#checkTwo").val();
			$('#gridInfo').datagrid("getPager").pagination({pageNumber : 1});
			var option = $('#gridInfo').datagrid("options");
			option.pageNumber = 1;
			option.url = "<%=request.getContextPath()%>/chr/chrVisit_getVisitList.action?custId=<%=custId%>";
			var queryParams = option.queryParams;
			queryParams.memberName = checkOneVal;
			queryParams.ishandled = checkTwoVal;
			refreshGrid();
		}
	
	function resetFilter(){
			$("#checkOne").val("");
			$("#checkTwo").val("");
		}

	//下达随访
	function showVisit(){
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
				/* $("#custId").val(${custId});
				$("#grade").val(selected.grade);
				$("#visitType").val(selected.type); */
				$('#visitWindow').window('open');
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
						refreshGrid();
					});
				}else{
					$.messager.alert("提示信息", data.msg, "error");
				}
			}
		});
	}
</script>
</head>
	<body>
		<div class="sectionTitle">查询条件</div>
	<div style="overflow:hidden;">
		<form id="filterForm">
			<table class="table">
				<tr>
					<!-- <td width="15%" class="td_title">用户名称:</td>
					<td>
						<input type="text" id="checkOne" name="name" maxlength="100" style="width: 200px;" />
					</td> -->
					<td width="15%" class="td_title">处理状态:</td>
					<td>
						<select id="checkTwo" name="ishandled">
								<option value="" selected="selected">全部</option>
								<option value="1">未随访</option>
								<option value="2">已随访</option>
						</select>
					</td>
					<td width="140px;" class="td_oper" nowrap="nowrap">
						<a href="javascript:void(0)" onclick="searchData()" class="easyui-linkbutton">查询</a> &nbsp;
						<a href="javascript:void(0)" onclick="resetFilter()" class="easyui-linkbutton">重置</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div style="padding-top: 10px;">
		<table id="gridInfo" class="easyui-datagrid"></table>
		<form id="mform" action="glyInfo" method="post" name="mform" style='display:none'>
			<input type="hidden" name="params" value=""/>
		</form>
	</div>
			<div id="grid-toolbar" style="padding:2px 0">
				<table cellpadding="0" cellspacing="0" >
					<tr>
						<td width="40%" style="padding-left:2px">
							<a href="javascript:schview()" class="easyui-linkbutton">查看</a>
							<c:if test="${fn:contains(userInfo.type, '2')}">
							<a href="javascript:showVisit()" class="easyui-linkbutton" >下达随访任务</a>
							</c:if>
						</td>
					</tr>
				</table>
			</div>
		<div style="display:none">
		<div id="visitWindow" class="easyui-window" title="随访时间设置" data-options="modal:true,minimizable:false,maximizable:false,collapsible:false,closed:true,iconCls:'icon-save'" style="width:300px;height:200px;padding:10px;">
			<form id="visitForm" action="" method="post">
				<input type="hidden" id="custId" name="chrVisit.custId" value = "${custId}"/>
				<input type="hidden" id="grade" name="chrVisit.grade" />
				<!-- <input type="hidden" id="visitType" name="chrVisit.type" /> -->
				<table width="100%">
					<tr><td>随访时间:</td>
					<td><input id="planTime" name ="chrVisit.planFlupTime" class="Wdate" onClick="WdatePicker({minDate:'%y-%M-%d'})" style="height:25px;width:150px"/></input></td></tr>
					<tr>
						<!-- <td colspan="2">&nbsp;</td> -->
						<td>随访类型:</td>
						<td >
							<select id="visitType" name="chrVisit.type">
								<option value="1">血压</option>
								<option value="2">血糖</option>
							</select>
						</td>
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
