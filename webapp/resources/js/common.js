Date.prototype.toString=function(format){
	var time={};
	time.Year=this.getFullYear();
	time.TYear=(""+time.Year).substr(2);
	time.Month=this.getMonth()+1;
	time.TMonth=time.Month<10?"0"+time.Month:time.Month;
	time.Day=this.getDate();
	time.TDay=time.Day<10?"0"+time.Day:time.Day;
	time.Hour=this.getHours();
	time.THour=time.Hour<10?"0"+time.Hour:time.Hour;
	time.hour=time.Hour<13?time.Hour:time.Hour-12;
	time.Thour=time.hour<10?"0"+time.hour:time.hour;
	time.Minute=this.getMinutes();
	time.TMinute=time.Minute<10?"0"+time.Minute:time.Minute;
	time.Second=this.getSeconds();
	time.TSecond=time.Second<10?"0"+time.Second:time.Second;
	time.Millisecond=this.getMilliseconds();
	var oNumber=time.Millisecond/1000;
	if(format!=undefined && format.replace(/\s/g,"").length>0){
		format=format
		.replace(/yyyy/ig,time.Year)
		.replace(/yyy/ig,time.Year)
		.replace(/yy/ig,time.TYear)
		.replace(/y/ig,time.TYear)
		.replace(/MM/g,time.TMonth)
		.replace(/M/g,time.Month)
		.replace(/dd/ig,time.TDay)
		.replace(/d/ig,time.Day)
		.replace(/HH/g,time.THour)
		.replace(/H/g,time.Hour)
		.replace(/hh/g,time.Thour)
		.replace(/h/g,time.hour)
		.replace(/mm/g,time.TMinute)
		.replace(/m/g,time.Minute)
		.replace(/ss/ig,time.TSecond)
		.replace(/s/ig,time.Second)
		.replace(/fff/ig,time.Millisecond)
		.replace(/ff/ig,oNumber.toFixed(2)*100)
		.replace(/f/ig,oNumber.toFixed(1)*10);
	}else{
		format=time.Year+"-"+time.Month+"-"+time.Day+" "+time.Hour+":"+time.Minute+":"+time.Second;
	}
	return format;
};
$.fn.serializeObject = function()
{
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};
function refreshPage(pageId){
	try{
		if(pageId){
			top.dirtyPages[pageId] = true;
		}
	}catch(e){}
}
var is={
	account: function(str){return str&&/^[a-zA-Z][a-zA-Z_0-9]{5,19}$/.test(str);},
	email: function(str){return str&&/^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))$/i.test(str);},
	idCard: function(str){return str&&/(^\d{15}$)|(^\d{17}[\dXx]$)/.test(str);},
	mPhone: function(str){return str&&/^1[3|4|5|8][0-9]\d{8}$/.test(str);}
}
function requestServer(method, dataname, datapara, callback) {
	var d = {};
	d[dataname] = datapara;
	jQuery.support.cors = true;
	$.ajax({
		url: method,
		type: "post",
		data:datapara,
		dataType: "json",
		success: function (data) {
			//alert(data);
			data=eval(data);
			callback(data);
		}
	});
}
