<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import = "com.yzxt.yh.module.chk.bean.CheckWarn" %>
<% 
	CheckWarn checkWarn = (CheckWarn)request.getAttribute("checkWarn");
%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>预警消息</title>
	<style type="text/css">
	</style>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script> 
	<script type="text/javascript">
	var archiveGrid;
	function initial(){
	$(function(){
		archiveGrid = $("#archives").datagrid({
			title: "预警信息列表",
			width: "auto",
			height: "auto",
			idField: "id",
			<%--默认查询未处理告警--%>
			url: "<%=request.getContextPath()%>/chk/checkWarn_queryList.action?id=<%=checkWarn.getCustId()%>&dealState=1",
			rownumbers: true,
			pagination: true,
			pageList: [10, 20, 50, 100],
			singleSelect: true,
			nowrap: true,
			striped: true,
			fitColumns: true,
			loadMsg: "数据加载中,请稍候...",
			toolbar: "#toolbar",
			frozenColumns: [[{
				field: "ck",
				checkbox: true
			}]],
			columns: [[{
				field: "state",
				title: "处理状态",
				width: 60,
				align: "center",formatter:function(v){
					var state = "";
					if(v==1){
						state="<font color='red'>未处理</font>";
					}else if(v==2){
						state="已处理";
					}else{
						state="未知";
					}
					return state;
				}
			},{
				field: "type",
				title: "预警项目",
				width: 60,
				align: "center",formatter:function(v){
					var state = "";
					if(v=="xy"){
						state="血压";
					}else if(v=="xt"){
						state="血糖";
					}else if(v=="tz"){
						state="体脂";
					}else{
						state="未知";
					}
					return state;
				}
			}, {
				field: "descript",
				title: "预警内容",
				width: 130,
				align: "center"
			}, {
				field: "level",
				title: "预警等级",
				width: 60,
				align: "center",formatter:function(v){
					var state = "";
					if(v==1){
						state = "I级";
					}else if(v==2){
						state = "II级";
					}else if(v==3){
						state = "III级";
					}
					return state;
				}
			}, {
				field: "warnTime",
				title: "预警时间",
				width: 90,
				align: "center"
			}]]
		});
	});
	}
	
	function query(){
		var checkOneVal = $("#dealState").val();
		var checkTwoVal = $("#warnLevel").val();
		$('#archives').datagrid("getPager").pagination({pageNumber : 1});
		var option = $('#archives').datagrid("options");
		option.pageNumber = 1;
		option.url = "<%=request.getContextPath()%>/chk/checkWarn_queryList.action?id=<%=checkWarn.getCustId()%>";
		var queryParams = option.queryParams;
		queryParams.dealState = checkOneVal;
		queryParams.warnLevel = checkTwoVal;
		queryParams.beginDate = document.getElementById("beginDate").value;
		queryParams.endDate = document.getElementById("endDate").value;
		refreshGrid();
	}
	
	function refreshGrid(){
			$('#archives').datagrid("reload");
			$('#archives').datagrid("unselectAll");
			$('#archives').datagrid("clearSelections");
		}
		
	function clear(){
		document.getElementById("filterForm").reset();
	}
	function closeIt() {
		try{
			parent.subWin.window("close");
		}catch(e){}
	}
	
	function dealWarning(){
		var selected = $("#archives").datagrid("getSelected");
		if(selected){
			if(selected.state==1){
				var selections = $('#archives').datagrid("getSelections");
				/* alert(selected.id);
				alert(selected.custId); */
				$("#warnId").val(selected.id);
				$("#custId").val(selected.custId);
				$('#dealWay').val("");
				$('#mwindow').window("open");
			}else{
				 $.messager.alert('提示','当前预警已经被处理！'); 
			}
		}else{
			$.messager.alert('提示信息','请选择编辑的行','info');
		}
	}
	
	//处理
	function doAddTask(){
		var warnId=document.getElementById("warnId").value;
		var custId=document.getElementById("custId").value;
		var dealWay= $('#dealWay').val();
		if(!dealWay){
			$.messager.alert("提示","请选择处理方式");
			return;
		}
		$.ajax({
				type: "GET",
				dataType: "json",
				url: "<%=request.getContextPath()%>/chk/checkWarn_doTask.action?warnId=" + warnId+"&custId="+custId+"&dealWay="+dealWay,
				async: false,
				timeout: 30000,
				success: function(data){
					/* res =  $.parseJSON(data);
					alert(data); */ 
					if (data.state == 1) {
						refreshPage("home");
						refreshPage("${param.pPageId}");
						$.messager.alert("提示信息",  data.msg?data.msg:"处理成功.", "info");
						closeWin();
						refreshGrid();
						/* var x=document.getElementById("phoneNum").innerHTML;
						x=parseInt(x)+1;
						document.getElementById("phoneNum").innerHTML=x; */
					} else {
						$.messager.alert('提示信息','处理失败.','info');
					}
				},
				error: function(){
					$.messager.alert("提示信息", "增加失败.", "error");
			 	}
			});
	}
	
	//退出
	function closeWin(){
		$('#mwindow').window("close");
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
<body onload="initial()" style="margin: 1px;padding: 0px;">
	<div class="sectionTitle">查询条件</div>
	<div>
		<form id="filterForm">
			<table class="table">
				<tr>
					<td width="20%" class="td_title">&nbsp;会员名称:</td>
					<td width="30%">
						<%=checkWarn.getName()%>
					</td>
					<td width="20%" class="td_title">身份证号码:</td>
					<td>
						<%=checkWarn.getIdCard()%>
					</td>
				</tr>
				<tr>
					<td class="td_title">处理状态:</td>
					<td>
						<select id="dealState">
							<option value="">请选择</option>
							<option value="1" selected="selected">未处理</option>
							<option value="2">已处理</option>
						</select>
					</td>
					<td class="td_title">预警等级:</td>
					<td>
						<select id="warnLevel">
							<option value="">请选择</option>
							<option value="1">I级</option>
							<option value="2">II级</option>
							<option value="3">III级</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="td_title">告警时间:</td>
					<td>
						<input class="Wdate" style="width: 90px;"id="beginDate" name="beginDate"  readonly="readonly" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}'})" />
						至
						<input class="Wdate" style="width: 90px;"id="endDate" name="endDate"  readonly="readonly" onclick="WdatePicker({minDate:'#F{$dp.$D(\'beginDate\')}'})" />
					</td>
					<td class="td_oper" colspan="2" style="padding-right: 20px;">
						<a class="easyui-linkbutton" href="javascript:query()">查询</a>
						&nbsp;
						<a class="easyui-linkbutton" href="javascript:clear()">重置</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div style="padding-top: 10px;">
		<table id="archives" class="easyui-datagrid"></table>
	</div>
	<div id="toolbar" style="padding: 2px 0px;display: none;">
		<table>
			<tr>
				<td style="padding-left: 2px;">
					<a href="javascript:addMngWin()" class="easyui-linkbutton">纳入慢病</a>
					<a href="#" onclick="dealWarning();" class="easyui-linkbutton">处理</a>
				</td>
			</tr>
		</table>
	</div>
	<div style="display:none">
		<div id="mwindow" class="easyui-window" title="处理预警" data-options="modal:true,minimizable:false,maximizable:false,collapsible:false,closed:true,iconCls:'icon-save'"
			style="width:300px;height:200px;padding:10px;">
			<input type="hidden" id = "warnId" name="warnId" value="">
			<input type="hidden" id = "custId" name="custId" value="">
			<table width="100%">
				<!-- <tr>
					<td>随访时间:</td>
					<td><input id="plantime" class="Wdate" onClick="WdatePicker({minDate:'%y-%M-%d'})" style="height:25px;width:150px" /></input>
					</td>
				</tr> -->
				<tr>
					<td>处理方式:</td>
					<td style="padding: 15px;">
						<select id="dealWay">
							<option value="">请选择</option>
							<option value="1">电话</option>
							<option value="2">推送短信</option>
							<!-- <option value="3">III级</option> -->
						</select>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="doAddTask();">确认</a>
						&nbsp;
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="closeWin()">退出</a>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<div style="display: none;">
		<div id="chrWin" title="添加慢病人员" class="easyui-window" style="padding: 12px;"  data-options="modal:true,closed:true">
			<form id="chrForm" action="" method="post">
			<input type="hidden" id="id" name="id" />
				<table border="0">
				  <tr>
				    <td class="td_title" align="right" nowrap="nowrap">会员名称：</td>
				     <td  nowrap="nowrap" >
				    	<input type="text" id="name" name="manage.memberName" maxlength="20" value="<%if(checkWarn.getName()!=null){ %><%=checkWarn.getName()%><%} %>" disabled="disabled" data-options="" style="width: 135px;" />
				    	<%if(checkWarn.getName()==null){ %>
				    	<img id="selUserImg" alt="选择会员" src="<%=request.getContextPath()%>/resources/img/search.png" onclick="openSelResi()" style="cursor: pointer;margin-bottom: -5px;" />
						<%} %>
						<input type="hidden" id="userId" name="chrManage.custId" value="<%if(checkWarn.getCustId()!=null){ %><%=checkWarn.getCustId()%><%} %>" />
					</td>
				  </tr>
				  <tr>
				    <td class="td_title" align="right" nowrap="nowrap">慢病类型：</td>
				    <td  style="width: 200px;">
		    			<input  type="checkbox" id="xueya" name="mngType"  value="1" />血压 &nbsp;
				      	<input  type="checkbox" id="xuetang" name="mngType" value="2"/>血糖
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
</body>
</html>