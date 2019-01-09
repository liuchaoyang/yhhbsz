<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/My97DatePicker/WdatePicker.js"></script> 
<%@ include file="../common/inc.jsp"%>
<%-- <script type="text/javascript">
	function save(){
				//debugger
				数量
			//var num = document.getElementById("num");
			var num = $("#num");
			var numVal = num.val();
			if(!numVal){
				$.messager.alert("提示信息", "请输入数量。", "error", function(){
					return;
				});
			}
		}
</script> --%>
</head>
<body style="margin: 1px;padding: 0px;">
	<div>
		<form id="filterForm" method="post" action="<%=request.getContextPath()%>/his/dzcf_saveDzcf.action">
			<input type="hidden" name="custId" value="${custId }">
			<input type="hidden" name="doctorId" value="${doctorId }">
			<input type="hidden" name="hisYpkc.sqid" value="${hisYpkc.sqid }">
			<table class="table">
				<tr>
					<td class="td_title">患者名称</td>
					<td colspan="3"><input type="text"  name="userName" value="${userName }"></td>
				</tr>
				<tr>
					<td class="td_title">药品名称</td>
					<td colspan="3"><input type="text"  name="hisYpkc.ypmc" value="${hisYpkc.ypmc }"></td>
				</tr>
				<tr>
					<td class="td_title">药品英文简称</td>
					<td colspan="3"><input type="text" name="hisYpkc.ypjx" value="${hisYpkc.ypjx }"></td>
				</tr>
				<%-- <tr>
					<td class="td_title">药品单价</td>
					<td colspan="3"><input type="text"  name="hisYpkc.ypdj" value="${hisYpkc.ypdj }"></td>
				</tr>
				<tr>
					<td class="td_title">药品规格</td>
					<td colspan="3"><input type="text" name="hisYpkc.ypgg" value="${hisYpkc.ypgg }"></td>
				</tr> --%>
				<tr>
					<td class="td_title">药品单位</td>
					<td colspan="3">
						<select id = "unit" name="unit" value="${unit}">
							<option value=0>小盒</option>
							<option value=1>瓶</option>
							<option value=2>支</option>
							<option value=3>粒</option>
							<option value=4>片</option>
							<option value=5>克</option>
						</select>
						<span class="must">*</span>
					</td>
				</tr>
				<tr>
					<td class="td_title">药品数量</td>
					<td colspan="3"><input type="text" id = "num" name="num" value="1">
					<span class="must">默认为1</span>
					</td>
					
				</tr>
				<tr>
					<td class="td_title">医保类别</td>
					<td colspan="3"><input type="text" name="hisYpkc.ybflag" value="${hisYpkc.ybflag }"></td>
				</tr>
				<tr>
					<td class="td_title">药品用法</td>
					<td colspan="3">
						<select id = "userage" name="userage">
							<option value="请选择">--请选择--</option>
							<option value="一日一次">一日一次</option>
							<option value="一日两次">一日两次</option>
							<option value="一日三次">一日三次</option>
							<option value="每六小时">每六小时</option>
							<option value="每八小时">每八小时</option>
							<option value="每十二小时">每十二小时</option>
							<option value="每连天一次">每两天一次</option>
							<option value="每小时">每小时</option>
							<option value="必要时">必要时</option>
							<option value="立即">立即</option>
							<option value="一周一次">一周一次</option>
							<option value="一月一次">一月一次</option>
							<option value="滴眼">滴眼</option>
							<option value="封闭注射">封闭注射</option>
							<option value="灌肠">灌肠</option>
							<option value="含服">含服</option>
							<option value="局部麻醉">局部麻醉</option>
							<option value="加壶">加壶</option>
							<option value="嚼服">嚼服</option>
							<option value="肌肉注射">肌肉注射</option>
							<option value="静脉滴注">静脉滴注</option>
							<option value="静脉注射">静脉注射</option>
							<option value="静推">静推</option>
							<option value="口服">口服</option>
							<option value="皮下注射">皮下注射</option>
							<option value="皮试">皮试</option>
							<option value="膀胱冲洗">膀胱冲洗</option>
							<option value="神经阻滞">神经阻滞</option>
							<option value="外用">外用</option>
							<option value="吸入">吸入</option>
							<option value="胸腔注射">胸腔注射</option>
						</select>
						<span class="must">*</span>
					</td>
				</tr>
				<tr>
					<td class="td_title">药品用量</td>
					<td colspan="3">
						<select id = "uselevel" name="uselevel">
							<option value="请选择">--请选择--</option>
							<option value="一次一粒">一次一粒</option>
							<option value="一次两粒">一次两粒</option>
							<option value="一次三粒">一次三粒</option>
							<option value="一次四粒">一次四粒</option>
							<option value="一支">一支</option>
							<option value="一毫升">一毫升</option>
							<option value="两毫升">两毫升</option>
							<option value="一瓶">一瓶</option>
						</select>
						<span class="must">*</span>
					</td>
				</tr>
				<tr>
					<td colspan="4" class="td_oper">
						<a class="easyui-linkbutton" href="javascript:submit()">提交</a>
					</td>
				</tr>
			</table>
		</form>
	<div>
	</div>
	<script type="text/javascript">
		function submit() {
			<%-- //debugger
			/* if(!validForm()){
				return false;
			}  */
			$("#filterForm").form("submit", {
				url: "<%=request.getContextPath()%>/his/dzcf_saveDzcf.action",
				dataType : "json", 
				success : function(data) {
					data =  $.parseJSON(data); 
					if(data.state == 1){
						$.messager.alert("提示信息", data.msg?data.msg:"保存成功。", "info", function(){
							try{
								parent.refreshGrid();
							}catch(e){}
							closeIt();
						});
					}else{
						$.messager.alert("提示信息", data.msg?data.msg:"保存失败。", "error");
					}
				}
			}); --%>
			//var num = document.getElementById("num");
			var num = $("#num");
			var numVal = num.val();
			if(!numVal){
				$.messager.alert("提示信息", "请输入数量。", "error", function(){
					return;
				});
			}
			 $('#filterForm').submit();
			parent.closeSelUserWin(); 
			
		}
		
	</script>
</body>
</html