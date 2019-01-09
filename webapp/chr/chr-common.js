function LimitTextArea(field,maxlength){ 
    if (field.value.length > maxlength) 
    	field.value = field.value.substring(0, maxlength); 
}

//获取被选中的同名的checkbox的值用分号分隔
function getCheckBoxInfos(name){
	var vals = '';
	$('input[name='+name+']').each(function(){
		if($(this).attr('checked')){
			ids+=$(this).val()+";";
		}
	});
	return vals;
}
//列表中的选择框是否被选择
function selectedValid(isSingle){

	var selected = $('#gridInfo').datagrid('getSelected');
	var rows = $('#gridInfo').datagrid('getSelections');
	if(selected){
		if(isSingle){
			if(rows.length==1){
				return rows[0].id;
			}else{
				$.messager.alert("提示","只能对一条信息进行查看");
			}
		}else{
			var ids = '';
			for(var i=0;i<rows.length;i++){
				ids+=rows[i].id+";";
			}
			return ids;
		}
	}else{
		$.messager.alert("提示","请选择需要操作的信息");
	};
}

function setValRadioOrCbox(name, value){
	$("input[name='"+name+"'][value="+value+"]").attr("checked",'checked');
}

function showVTInfo(type){

}

function gridAjaxSubmit(params, atname){
	var formObj = $("#mform");  
	var action = formObj.attr('action')+atname+'.do';
	$.ajax({
		type : "POST",
		url : action,
		data : params,
		success:function(msg){ 
			$('#gridInfo').datagrid("reload");   
			$('#gridInfo').datagrid('clearSelections');
		} 
	});
}

function doInfoFormSubmit(action){
	$("#mform").form('submit',{  
		type: 'post',  
		url : action, 
		onSubmit : function() {
			return $('#mform').form('validate');
		},
		success: function(data){  
			var infos = eval('('+data+')');
			if(infos.succ){
				$.messager.alert("操作提示", infos.info, "info", function () {
					var title = top.getSelectedTabTitle();
					top.closeTab(title);
				});
			}else{
				$.messager.alert("操作提示",infos.info);
			}
		} 
	}); 
}

function loadSchGrid(params, nurl){
	if(nurl){
		var formObj = $("#mform"); 
		var url = formObj.attr('action')+nurl+'.do';
		$('#gridInfo').datagrid('options').url=url;
	}
	$('#gridInfo').datagrid('options').queryParams = params;
	$('#gridInfo').datagrid('load');
}

/*function showDetail(params){
	var formObj = $("#mform");  
	formObj.attr('action', formObj.attr('action')+'_view.do');
	var pinpt = $("input[name=params]");
	pinpt.val(params);
	formObj.submit();  
}*/
function doFormSubmit(fname,atname,params){

	var formObj = $("#"+fname+"");  
	formObj.attr('action', atname+'.do');
	var pint = $("input[name=params]");
	pint.val(params);
//	formObj.submit(); 
	var datas = encodeURI(params);
	top.addTab('详细处理',atname+'.do?params='+datas,null);
}

function mformSubmit(params,atname){
	if(!atname)atname = '_view.do';
	var formObj = $("#mform");  
//	formObj.attr('action', formObj.attr('action')+atname+'.do');
	var pint = $("input[name=params]");
	pint.val(params);
//	formObj.submit();  
	var datas = encodeURI(params);
//	alert(formObj.attr('action')+atname+'.do');
	top.addTab('详细处理',formObj.attr('action')+atname+'.do?params='+datas,null);
}

function toDate(obj){

	if(obj){
		var date = new Date();
		date.setTime(obj.time);
		date.setHours(obj.hours);
		date.setMinutes(obj.minutes);
		date.setSeconds(obj.seconds);
		return date.format("yyyy-MM-dd"); 
	}
}

Date.prototype.format = function(format) {
	/*
	 * format="yyyy-MM-dd hh:mm:ss";
	 */
	var o = {
			"M+" : this.getMonth() + 1,
			"d+" : this.getDate(),
			"h+" : this.getHours(),
			"m+" : this.getMinutes(),
			"s+" : this.getSeconds(),
			"q+" : Math.floor((this.getMonth() + 3) / 3),
			"S" : this.getMilliseconds()
	};
	if (/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4
				- RegExp.$1.length));
	}
	for (var k in o) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1
					? o[k]
			: ("00" + o[k]).substr(("" + o[k]).length));
		}
	}
	return format;
}; 


function strToDate(str) {
	if(str)return str.substring(0,10);
}

function jsonToString(obj){  
   var THIS = this;   
	switch(typeof(obj)){  
		case 'string':  
			return '"' + obj.replace(/(["\\])/g, '\\$1')+ '"';  
		case 'array':  
			return '[' + obj.map(THIS.jsonToString).join(',') + ']';  
		case 'object':  
			 if(obj instanceof Array){  
			   var strArr = [];  
			   var len = obj.length;  
			   for(var i=0; i<len; i++){  
				   strArr.push(THIS.jsonToString(obj[i]));  
			   }  
			   return '[' + strArr.join(',') + ']';  
		   }else if(obj==null){  
			   return 'null';  

		   }else{  
			   var string = [];  
			   for (var property in obj) string.push(THIS.jsonToString(property) + ':' + THIS.jsonToString(obj[property])); 
			   return '{' + string.join(',') + '}';  
		   }  
	   case 'number':  
		   return obj;  
	   case false:  
		   return obj;  
   }  
}

function analysisPre(sbp, dbp) {
	if(dbp>=90||sbp>=140){
		return true;
	}else{
		return false;
	}
}

function analysisGly(bloodSugar) {
	if(bloodSugar>7 || bloodSugar<2.8||bloodSugar>'7' || bloodSugar<'2.8'){
		return true;
	}else{
		return false;
	}
}