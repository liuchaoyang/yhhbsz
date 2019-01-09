<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript">
function sectTog(imgEle, conId){
	var imgSrc = imgEle.src;
	if(!imgSrc||imgSrc.indexOf("exam-collapse.png")>-1){
		imgEle.src = "<%=request.getContextPath()%>/resources/img/exam-expand.png";
		document.getElementById(conId).style.display = "block";
	}else{
		imgEle.src = "<%=request.getContextPath()%>/resources/img/exam-collapse.png";
		document.getElementById(conId).style.display = "none";
	}
}
function getRadioVal(eles){
	var len = eles?eles.length:0;
	for(var i=0;i<len;i++){
		if(eles[i].checked){
			return eles[i].value;
		}
	}
	return null;
}
function closeIt(){
	try{
		var tc = top.$("#tb");
		var tab = tc.tabs("getSelected");
		if (tab) {
			tc.tabs("close", tab.panel("options").title);
		}
	}catch(e){}
}
function exerciseRateChange(val){
	if(val!="4"){
		document.getElementsByName("exam.everyExerTime")[0].disabled = false;
		document.getElementsByName("exam.insistExerYear")[0].disabled = false;
		document.getElementsByName("exam.exerType")[0].disabled = false;
	}else{
		document.getElementsByName("exam.everyExerTime")[0].disabled = true;
		document.getElementsByName("exam.insistExerYear")[0].disabled = true;
		document.getElementsByName("exam.exerType")[0].disabled = true;
		document.getElementsByName("exam.everyExerTime")[0].value = "";
		document.getElementsByName("exam.insistExerYear")[0].value = "";
		document.getElementsByName("exam.exerType")[0].value = "";
	}
}
function smokeStatusChange(val){
	if(val!="1"){
		document.getElementsByName("exam.dailySmoke")[0].disabled = false;
		document.getElementsByName("exam.startSmokeAge")[0].disabled = false;
		document.getElementsByName("exam.notSmokeAge")[0].disabled = false;
	}else{
		document.getElementsByName("exam.dailySmoke")[0].disabled = true;
		document.getElementsByName("exam.startSmokeAge")[0].disabled = true;
		document.getElementsByName("exam.notSmokeAge")[0].disabled = true;
		document.getElementsByName("exam.dailySmoke")[0].value = "";
		document.getElementsByName("exam.startSmokeAge")[0].value = "";
		document.getElementsByName("exam.notSmokeAge")[0].value = "";
	}
}
function isNotDrinkChange(val){
	if(val!="1"){
		document.getElementsByName("exam.notDrinkAge")[0].disabled = false;
	}else{
		document.getElementsByName("exam.notDrinkAge")[0].disabled = true;
		document.getElementsByName("exam.notDrinkAge")[0].value = "";
	}
}
function drinkWineChange(val){
	if(val!="1"){
		document.getElementsByName("exam.dailyDrink")[0].disabled = false;
		document.getElementById("isNotDrink1").disabled = false;
		document.getElementById("isNotDrink2").disabled = false;
		document.getElementsByName("exam.notDrinkAge")[0].disabled = false;
		document.getElementsByName("exam.startDrinkAge")[0].disabled = false;
		document.getElementById("recentYearDrink1").disabled = false;
		document.getElementById("recentYearDrink2").disabled = false;
		document.getElementById("drinkType1").disabled = false;
		document.getElementById("drinkType2").disabled = false;
		document.getElementById("drinkType3").disabled = false;
		document.getElementById("drinkType4").disabled = false;
		document.getElementById("otherDrinkType").disabled = false;
	}else{
		document.getElementsByName("exam.dailyDrink")[0].disabled = true;
		document.getElementById("isNotDrink1").disabled = true;
		document.getElementById("isNotDrink2").disabled = true;
		document.getElementsByName("exam.notDrinkAge")[0].disabled = true;
		document.getElementsByName("exam.startDrinkAge")[0].disabled = true;
		document.getElementById("recentYearDrink1").disabled = true;
		document.getElementById("recentYearDrink2").disabled = true;
		document.getElementById("drinkType1").disabled = true;
		document.getElementById("drinkType2").disabled = true;
		document.getElementById("drinkType3").disabled = true;
		document.getElementById("drinkType4").disabled = true;
		document.getElementById("otherDrinkType").disabled = true;
		document.getElementsByName("exam.dailyDrink")[0].value = "";
		document.getElementById("isNotDrink1").checked = false;
		document.getElementById("isNotDrink2").checked = false;
		document.getElementsByName("exam.notDrinkAge")[0].value = "";
		document.getElementsByName("exam.startDrinkAge")[0].value = "";
		document.getElementById("recentYearDrink1").checked = false;
		document.getElementById("recentYearDrink2").checked = false;
		document.getElementById("drinkType1").checked = false;
		document.getElementById("drinkType2").checked = false;
		document.getElementById("drinkType3").checked = false;
		document.getElementById("drinkType4").checked = false;
		document.getElementById("otherDrinkType").value = "";
	}
}
function skinChange(val){
	if(val!="7"){
		document.getElementsByName("exam.otherSkin")[0].disabled = true;
		document.getElementsByName("exam.otherSkin")[0].value = "";
	}else{
		document.getElementsByName("exam.otherSkin")[0].disabled = false;
	}
}
function scleraChange(val){
	if(val!="4"){
		document.getElementsByName("exam.otherSclera")[0].disabled = true;
		document.getElementsByName("exam.otherSclera")[0].value = "";
	}else{
		document.getElementsByName("exam.otherSclera")[0].disabled = false;
	}
}
function lymphadenChange(val){
	if(val!="4"){
		document.getElementsByName("exam.otherLymphaden")[0].disabled = true;
		document.getElementsByName("exam.otherLymphaden")[0].value = "";
	}else{
		document.getElementsByName("exam.otherLymphaden")[0].disabled = false;
	}
}
function lungsRaleChange(val){
	if(val!="4"){
		document.getElementsByName("exam.otherLungsRale")[0].disabled = true;
		document.getElementsByName("exam.otherLungsRale")[0].value = "";
	}else{
		document.getElementsByName("exam.otherLungsRale")[0].disabled = false;
	}
}
function anusDreChange(val){
	if(val!="5"){
		document.getElementsByName("exam.otherAnusDre")[0].disabled = true;
		document.getElementsByName("exam.otherAnusDre")[0].value = "";
	}else{
		document.getElementsByName("exam.otherAnusDre")[0].disabled = false;
	}
}
function mammaryGlandChange(val, ckd){
	if(val=="1"||val=="2"){
		document.getElementById("mammaryGland3").checked = false;
		document.getElementById("mammaryGland4").checked = false;
		document.getElementById("mammaryGland5").checked = false;
		document.getElementById("otherMammaryGland").disabled = true;
		document.getElementById("otherMammaryGland").value = "";
	}else if(ckd){
		document.getElementById("mammaryGland1").checked = false;
		document.getElementById("mammaryGland2").checked = false;
		if(val=="5"){
			document.getElementById("otherMammaryGland").disabled = false;
		}
	}else{
		if(val=="5"){
			document.getElementById("otherMammaryGland").disabled = true;
			document.getElementById("otherMammaryGland").value = "";
		}
	}
}
function controlChange(val, ckd){
	if(val=="5"){
		if(ckd){
			document.getElementsByName("exam.dangerControlWeight")[0].disabled = false;
		}else{
			document.getElementsByName("exam.dangerControlWeight")[0].disabled = true;
			document.getElementsByName("exam.dangerControlWeight")[0].value = "";
		}
	}else if(val=="6"){
		if(ckd){
			document.getElementsByName("exam.dangerControlVaccin")[0].disabled = false;
		}else{
			document.getElementsByName("exam.dangerControlVaccin")[0].disabled = true;
			document.getElementsByName("exam.dangerControlVaccin")[0].value = "";
		}
	}
}
function selAllPos(ckd){
	var sels = document.getElementsByName("posSel");
	var len = sels?sels.length:0;
	for(var i=0; i<len; i++){
		sels[i].checked = ckd;
	}
}
function addPos(){
	var tb = document.getElementById("tbPos");
	var tr = tb.insertRow(tb.rows.length);
	var td0 = tr.insertCell(0);
	td0.innerHTML = "<input type=\"checkbox\" name=\"posSel\" />";
	var td1 = tr.insertCell(1);
	td1.innerHTML = "<input type=\"text\" maxlength=\"50\" class=\"posPosionName\" name=\"posPosionName\" />";
	var td2 = tr.insertCell(2);
	td2.innerHTML = "<input type=\"text\" maxlength=\"4\" class=\"iptDate\" name=\"posWorkTime\" />";
	var td3 = tr.insertCell(3);
	td3.innerHTML = "<input type=\"text\" maxlength=\"100\" class=\"posSafeguard\" name=\"posSafeguard\" />";
}
function delPos(){
	var sels = document.getElementsByName("posSel");
	var len = sels?sels.length:0;
	var tb = document.getElementById("tbPos");
	for(var i=len-1; i>-1; i--){
		if(sels[i].checked){
			tb.deleteRow(i+1);
		}
	}
}
function selAllIP(ckd){
	var sels = document.getElementsByName("ipSel");
	var len = sels?sels.length:0;
	for(var i=0; i<len; i++){
		sels[i].checked = ckd;
	}
}
function addIp(){
	var tb = document.getElementById("tbIp");
	var tr = tb.insertRow(tb.rows.length);
	var td0 = tr.insertCell(0);
	td0.innerHTML = "<input type=\"checkbox\" name=\"ipSel\" />";
	var td1 = tr.insertCell(1);
	td1.innerHTML = "<input type=\"text\" maxlength=\"10\" readonly=\"readonly\" class=\"Wdate iptDate\" style=\"width: 85px;\" name=\"ipInTime\" onclick=\"WdatePicker({})\" />"
		+"/<input type=\"text\" maxlength=\"10\" readonly=\"readonly\" class=\"Wdate iptDate\" style=\"width: 85px;\" name=\"ipOutTime\" onclick=\"WdatePicker({})\" />";
	var td2 = tr.insertCell(2);
	td2.innerHTML = "<input type=\"text\" maxlength=\"100\" class=\"hpReason\" name=\"ipAdmissionReason\" />";
	var td3 = tr.insertCell(3);
	td3.innerHTML = "<input type=\"text\" maxlength=\"100\" class=\"hpName\" name=\"ipHosDept\" />";
	var td4 = tr.insertCell(4);
	td4.innerHTML = "<input type=\"text\" maxlength=\"20\" class=\"hpRecNum\" name=\"ipRecordNo\" />";
}
function delIp(){
	var sels = document.getElementsByName("ipSel");
	var len = sels?sels.length:0;
	var tb = document.getElementById("tbIp");
	for(var i=len-1; i>-1; i--){
		if(sels[i].checked){
			tb.deleteRow(i+1);
		}
	}
}
function selAllFh(ckd){
	var sels = document.getElementsByName("fhSel");
	var len = sels?sels.length:0;
	for(var i=0; i<len; i++){
		sels[i].checked = ckd;
	}
}
function addFh(){
	var tb = document.getElementById("tbFh");
	var tr = tb.insertRow(tb.rows.length);
	var td0 = tr.insertCell(0);
	td0.innerHTML = "<input type=\"checkbox\" name=\"fhSel\" />";
	var td1 = tr.insertCell(1);
	td1.innerHTML = "<input type=\"text\" maxlength=\"50\" class=\"fhUserName\" name=\"fhUserName\" />";
	var td2 = tr.insertCell(2);
	td2.innerHTML = "<input type=\"text\" maxlength=\"10\" readonly=\"readonly\" class=\"Wdate iptDate\" style=\"width: 85px;\" name=\"fhCreateBedTime\" onclick=\"WdatePicker({})\" />"
		+"/<input type=\"text\" maxlength=\"10\" readonly=\"readonly\" class=\"Wdate iptDate\" style=\"width: 85px;\" name=\"fhPutBedTime\" onclick=\"WdatePicker({})\" />";
	var td3 = tr.insertCell(3);
	td3.innerHTML = "<input type=\"text\" maxlength=\"100\" class=\"fhReason\" name=\"fhReason\" />";
	var td4 = tr.insertCell(4);
	td4.innerHTML = "<input type=\"text\" maxlength=\"50\" class=\"fhUnit\" name=\"fhHosUnit\" />";
	var td5 = tr.insertCell(5);
	td5.innerHTML = "<input type=\"text\" maxlength=\"20\" class=\"hpRecNum\" name=\"fhMedRecordNo\" />";
}
function delFh(){
	var sels = document.getElementsByName("fhSel");
	var len = sels?sels.length:0;
	var tb = document.getElementById("tbFh");
	for(var i=len-1; i>-1; i--){
		if(sels[i].checked){
			tb.deleteRow(i+1);
		}
	}
}
function selAllMed(ckd){
	var sels = document.getElementsByName("medSel");
	var len = sels?sels.length:0;
	for(var i=0; i<len; i++){
		sels[i].checked = ckd;
	}
}
function addMed(){
	var tb = document.getElementById("tbMed");
	var tr = tb.insertRow(tb.rows.length);
	var td0 = tr.insertCell(0);
	td0.innerHTML = "<input type=\"checkbox\" name=\"medSel\" />";
	var td1 = tr.insertCell(1);
	td1.innerHTML = "<input type=\"text\" maxlength=\"50\" class=\"medMedName\" name=\"medMedName\" />";
	var td2 = tr.insertCell(2);
	td2.innerHTML = "<input type=\"text\" maxlength=\"20\" class=\"medUseType\" name=\"medUseType\" />";
	var td3 = tr.insertCell(3);
	td3.innerHTML = "<input type=\"text\" maxlength=\"50\" class=\"medUseNum\" name=\"medUseNum\" />";
	var td4 = tr.insertCell(4);
	td4.innerHTML = "<input type=\"text\" maxlength=\"50\" class=\"medUseTime\" name=\"medUseTime\" />";
	var td5 = tr.insertCell(5);
	td5.innerHTML = "<select class=\"medAdhes\" name=\"medAdhes\"><option value=\"\" selected=\"selected\">请选择</option>"
		+"<option value=\"1\">规律</option><option value=\"2\"\>间断</option><option value=\"3\"\>不服药</option></select>";
}
function delMed(){
	var sels = document.getElementsByName("medSel");
	var len = sels?sels.length:0;
	var tb = document.getElementById("tbMed");
	for(var i=len-1; i>-1; i--){
		if(sels[i].checked){
			tb.deleteRow(i+1);
		}
	}
}
function selAllIno(ckd){
	var sels = document.getElementsByName("inoSel");
	var len = sels?sels.length:0;
	for(var i=0; i<len; i++){
		sels[i].checked = ckd;
	}
}
function addIno(){
	var tb = document.getElementById("tbIno");
	var tr = tb.insertRow(tb.rows.length);
	var td0 = tr.insertCell(0);
	td0.innerHTML = "<input type=\"checkbox\" name=\"inoSel\" />";
	var td1 = tr.insertCell(1);
	td1.innerHTML = "<input type=\"text\" maxlength=\"20\" class=\"inoInoculateName\" name=\"inoInoculateName\" />";
	var td2 = tr.insertCell(2);
	td2.innerHTML = "<input type=\"text\" maxlength=\"10\" readonly=\"readonly\" class=\"Wdate iptDate\" name=\"inoInoculateTime\" onclick=\"WdatePicker({})\" />";
	var td3 = tr.insertCell(3);
	td3.innerHTML = "<input type=\"text\" maxlength=\"50\" class=\"inoInoculateDept\" name=\"inoInoculateDept\" />";
}
function delIno(){
	var sels = document.getElementsByName("inoSel");
	var len = sels?sels.length:0;
	var tb = document.getElementById("tbIno");
	for(var i=len-1; i>-1; i--){
		if(sels[i].checked){
			tb.deleteRow(i+1);
		}
	}
}
function validForm(){
	<%--基本信息--%>
	var examDate = document.getElementById("examDate");
	if(!examDate.value){
		$.messager.alert("提示信息", "请选择体检日期.", "error", function(){
			examDate.focus();
		});
		return false;
	}
	var industryName = document.getElementById("industryName");
	industryName.value = $.trim(industryName.value);
	<%--症状--%>
	var otherSymptom = document.getElementById("otherSymptom");
	otherSymptom.value = $.trim(otherSymptom.value);
	<%--一般状况--%>
	var temperature = document.getElementsByName("exam.temperature")[0];
	var temperatureVal = $.trim(temperature.value);
	temperature.value = temperatureVal;
	if(temperatureVal && !/^\d{1,2}(\.\d)?$/.test(temperatureVal)){
		$.messager.alert("提示信息", "体温值不对，应为正数，最多2位整数位和1位小数.", "error", function(){
			temperature.focus();
		});
		return false;
	}
	var pulseRate = document.getElementsByName("exam.pulseRate")[0];
	var pulseRateVal = $.trim(pulseRate.value);
	pulseRate.value = pulseRateVal;
	if(pulseRateVal && !/^[1-9]\d{1,2}$/.test(pulseRateVal)){
		$.messager.alert("提示信息", "心率值不对，应为正整数，最多3位整数位.", "error", function(){
			pulseRate.focus();
		});
		return false;
	}
	var breatheRate = document.getElementsByName("exam.breatheRate")[0];
	var breatheRateVal = $.trim(breatheRate.value);
	breatheRate.value = breatheRateVal;
	if(breatheRateVal && !/^[1-9]\d{1,2}$/.test(breatheRateVal)){
		$.messager.alert("提示信息", "呼吸频率值不对，应为正整数，最多3位整数位.", "error", function(){
			breatheRate.focus();
		});
		return false;
	}
	var height = document.getElementsByName("exam.height")[0];
	var heightVal = $.trim(height.value);
	height.value = heightVal;
	if(heightVal && !/^[1-9]\d{1,2}$/.test(heightVal)){
		$.messager.alert("提示信息", "身高值不对，应为正整数，最多3位整数位.", "error", function(){
			height.focus();
		});
		return false;
	}
	var weight = document.getElementsByName("exam.weight")[0];
	var weightVal = $.trim(weight.value);
	weight.value = weightVal;
	if(weightVal && !/^\d{1,3}(\.\d)?$/.test(weightVal)){
		$.messager.alert("提示信息", "体重值不对，应为正数，最多3位整数位和1位小数.", "error", function(){
			weight.focus();
		});
		return false;
	}
	var waistline = document.getElementsByName("exam.waistline")[0];
	var waistlineVal = $.trim(waistline.value);
	waistline.value = waistlineVal;
	if(waistlineVal && !/^[1-9]\d{1,2}$/.test(waistlineVal)){
		$.messager.alert("提示信息", "腰围值不对，应为正整数，最多3位整数位.", "error", function(){
			waistline.focus();
		});
		return false;
	}
	<%--
	var lBloodPressure = document.getElementsByName("exam.lBloodPressure")[0];
	var lBloodPressureVal = $.trim(lBloodPressure.value);
	lBloodPressure.value = lBloodPressureVal;
	if(lBloodPressureVal && !/^[1-9]\d{0,2}$/.test(lBloodPressureVal)){
		$.messager.alert("提示信息", "左侧收缩血压值不对，应为正整数，最多3位整数位.", "error", function(){
			lBloodPressure.focus();
		});
		return false;
	}
	var lBloodPressureBpd = document.getElementsByName("exam.lBloodPressureBpd")[0];
	var lBloodPressureBpdVal = $.trim(lBloodPressureBpd.value);
	lBloodPressureBpd.value = lBloodPressureBpdVal;
	if(lBloodPressureBpdVal && !/^[1-9]\d{0,2}$/.test(lBloodPressureBpdVal)){
		$.messager.alert("提示信息", "左侧舒张血压值不对，应为正整数，最多3位整数位.", "error", function(){
			lBloodPressureBpd.focus();
		});
		return false;
	}
	--%>
	var rBloodPressure = document.getElementsByName("exam.rBloodPressure")[0];
	var rBloodPressureVal = $.trim(rBloodPressure.value);
	rBloodPressure.value = rBloodPressureVal;
	if(rBloodPressureVal && !/^[1-9]\d{0,2}$/.test(rBloodPressureVal)){
		$.messager.alert("提示信息", "收缩血压值不对，应为正整数，最多3位整数位.", "error", function(){
			rBloodPressure.focus();
		});
		return false;
	}
	var rBloodPressureBpd = document.getElementsByName("exam.rBloodPressureBpd")[0];
	var rBloodPressureBpdVal = $.trim(rBloodPressureBpd.value);
	rBloodPressureBpd.value = rBloodPressureBpdVal;
	if(rBloodPressureBpdVal && !/^[1-9]\d{0,2}$/.test(rBloodPressureBpdVal)){
		$.messager.alert("提示信息", "舒张血压值不对，应为正整数，最多3位整数位.", "error", function(){
			rBloodPressureBpd.focus();
		});
		return false;
	}
	var agedKfScore = document.getElementsByName("exam.agedKfScore")[0];
	var agedKfScoreVal = $.trim(agedKfScore.value);
	agedKfScore.value = agedKfScoreVal;
	if(agedKfScoreVal && !/^\d*$/.test(agedKfScoreVal)){
		$.messager.alert("提示信息", "简易智力状态检查总分分值不对，应为整数.", "error", function(){
			agedKfScore.focus();
		});
		return false;
	}
	var agedFsScore = document.getElementsByName("exam.agedFsScore")[0];
	var agedFsScoreVal = $.trim(agedFsScore.value);
	agedFsScore.value = agedFsScoreVal;
	if(agedFsScoreVal && !/^\d*$/.test(agedFsScoreVal)){
		$.messager.alert("提示信息", "老年人抑郁检查评分值不对，应为整数.", "error", function(){
			agedFsScore.focus();
		});
		return false;
	}
	<%--生活方式--%>
	var everyExerTime = document.getElementsByName("exam.everyExerTime")[0];
	var everyExerTimeVal = $.trim(everyExerTime.value);
	everyExerTime.value = everyExerTimeVal;
	if(everyExerTimeVal && !/^[1-9]\d*$/.test(everyExerTimeVal)){
		$.messager.alert("提示信息", "每次锻炼时间值不对，应为正整数.", "error", function(){
			everyExerTime.focus();
		});
		return false;
	}
	var insistExerYear = document.getElementsByName("exam.insistExerYear")[0];
	var insistExerYearVal = $.trim(insistExerYear.value);
	insistExerYear.value = insistExerYearVal;
	if(insistExerYearVal && !/^\d*$/.test(insistExerYearVal)){
		$.messager.alert("提示信息", "坚持锻炼时间值不对，应为整数.", "error", function(){
			insistExerYear.focus();
		});
		return false;
	}
	var exerType = document.getElementsByName("exam.exerType")[0];
	exerType.value = $.trim(exerType.value);
	var dailySmoke = document.getElementsByName("exam.dailySmoke")[0];
	var dailySmokeVal = $.trim(dailySmoke.value);
	dailySmoke.value = dailySmokeVal;
	if(dailySmokeVal && !/^[1-9]\d*$/.test(dailySmokeVal)){
		$.messager.alert("提示信息", "日平均吸烟量值不对，应为正整数.", "error", function(){
			dailySmoke.focus();
		});
		return false;
	}
	var startSmokeAge = document.getElementsByName("exam.startSmokeAge")[0];
	var startSmokeAgeVal = $.trim(startSmokeAge.value);
	startSmokeAge.value = startSmokeAgeVal;
	if(startSmokeAgeVal && !/^[1-9]\d*$/.test(startSmokeAgeVal)){
		$.messager.alert("提示信息", "开始吸烟年龄值不对，应为正整数.", "error", function(){
			startSmokeAge.focus();
		});
		return false;
	}
	var notSmokeAge = document.getElementsByName("exam.notSmokeAge")[0];
	var notSmokeAgeVal = $.trim(notSmokeAge.value);
	notSmokeAge.value = notSmokeAgeVal;
	if(notSmokeAgeVal && !/^[1-9]\d*$/.test(notSmokeAgeVal)){
		$.messager.alert("提示信息", "戒烟年龄值不对，应为正整数.", "error", function(){
			notSmokeAge.focus();
		});
		return false;
	}
	var dailyDrink = document.getElementsByName("exam.dailyDrink")[0];
	var dailyDrinkVal = $.trim(dailyDrink.value);
	dailyDrink.value = dailyDrinkVal;
	if(dailyDrinkVal && !/^[1-9]\d*$/.test(dailyDrinkVal)){
		$.messager.alert("提示信息", "日平均饮酒量不对，应为正整数.", "error", function(){
			dailyDrink.focus();
		});
		return false;
	}
	var notDrinkAge = document.getElementsByName("exam.notDrinkAge")[0];
	var notDrinkAgeVal = $.trim(notDrinkAge.value);
	notDrinkAge.value = notDrinkAgeVal;
	if(notDrinkAgeVal && !/^[1-9]\d*$/.test(notDrinkAgeVal)){
		$.messager.alert("提示信息", "戒酒年龄值不对，应为正整数.", "error", function(){
			notDrinkAge.focus();
		});
		return false;
	}
	var startDrinkAge = document.getElementsByName("exam.startDrinkAge")[0];
	var startDrinkAgeVal = $.trim(startDrinkAge.value);
	startDrinkAge.value = startDrinkAgeVal;
	if(startDrinkAgeVal && !/^[1-9]\d*$/.test(startDrinkAgeVal)){
		$.messager.alert("提示信息", "开始饮酒年龄值不对，应为正整数.", "error", function(){
			startDrinkAge.focus();
		});
		return false;
	}
	var otherDrinkType = document.getElementById("otherDrinkType");
	otherDrinkType.value = $.trim(otherDrinkType.value);
	var workType = document.getElementsByName("exam.workType")[0];
	workType.value = $.trim(workType.value);
	var workTime = document.getElementsByName("exam.workTime")[0];
	var workTimeVal = $.trim(workTime.value);
	workTime.value = workTimeVal;
	if(workTimeVal && !/^\d{1,2}(\.\d)?$/.test(workTimeVal)){
		$.messager.alert("提示信息", "从业时间值不对，应为正数，最多2位整数位和1位小数位.", "error", function(){
			workTime.focus();
		});
		return false;
	}
	<%--脏器功能--%>
	var lEyesight = document.getElementsByName("exam.lEyesight")[0];
	var lEyesightVal = $.trim(lEyesight.value);
	lEyesight.value = lEyesightVal;
	if(lEyesightVal && !/^\d(\.\d)?$/.test(lEyesightVal)){
		$.messager.alert("提示信息", "左眼视力值不对，应为正数，最多1位整数位和1位小数.", "error", function(){
			lEyesight.focus();
		});
		return false;
	}
	var rEyesight = document.getElementsByName("exam.rEyesight")[0];
	var rEyesightVal = $.trim(rEyesight.value);
	rEyesight.value = rEyesightVal;
	if(rEyesightVal && !/^\d(\.\d)?$/.test(rEyesightVal)){
		$.messager.alert("提示信息", "右眼视力值不对，应为正数，最多1位整数位和1位小数.", "error", function(){
			rEyesight.focus();
		});
		return false;
	}
	var reLEyesight = document.getElementsByName("exam.reLEyesight")[0];
	var reLEyesightVal = $.trim(reLEyesight.value);
	reLEyesight.value = reLEyesightVal;
	if(reLEyesightVal && !/^\d(\.\d)?$/.test(reLEyesightVal)){
		$.messager.alert("提示信息", "矫正左眼视力值不对，应为正数，最多1位整数位和1位小数.", "error", function(){
			reLEyesight.focus();
		});
		return false;
	}
	var reREyesight = document.getElementsByName("exam.reREyesight")[0];
	var reREyesightVal = $.trim(reREyesight.value);
	reREyesight.value = reREyesightVal;
	if(reREyesightVal && !/^\d(\.\d)?$/.test(reREyesightVal)){
		$.messager.alert("提示信息", "矫正右眼视力值不对，应为正数，最多1位整数位和1位小数.", "error", function(){
			reREyesight.focus();
		});
		return false;
	}
	<%--查体--%>
	var eyeGround = document.getElementsByName("exam.eyeGround")[0];
	eyeGround.value = $.trim(eyeGround.value);
	var otherSkin = document.getElementsByName("exam.otherSkin")[0];
	otherSkin.value = $.trim(otherSkin.value);
	var otherSclera = document.getElementsByName("exam.otherSclera")[0];
	otherSclera.value = $.trim(otherSclera.value);
	var otherLymphaden = document.getElementsByName("exam.otherLymphaden")[0];
	otherLymphaden.value = $.trim(otherLymphaden.value);
	var lungsBreVoice = document.getElementsByName("exam.lungsBreVoice")[0];
	lungsBreVoice.value = $.trim(lungsBreVoice.value);
	var otherLungsRale = document.getElementsByName("exam.otherLungsRale")[0];
	otherLungsRale.value = $.trim(otherLungsRale.value);
	var heartRate = document.getElementsByName("exam.heartRate")[0];
	var heartRateVal = $.trim(heartRate.value);
	heartRate.value = heartRateVal;
	if(heartRateVal && !/^[1-9]\d*$/.test(heartRateVal)){
		$.messager.alert("提示信息", "心率值不对，应为正整数.", "error", function(){
			heartRate.focus();
		});
		return false;
	}
	var heartNoise = document.getElementsByName("exam.heartNoise")[0];
	heartNoise.value = $.trim(heartNoise.value);
	var abdPrePain = document.getElementsByName("exam.abdPrePain")[0];
	abdPrePain.value = $.trim(abdPrePain.value);
	var abdBagPiece = document.getElementsByName("exam.abdBagPiece")[0];
	abdBagPiece.value = $.trim(abdBagPiece.value);
	var abdHepatomegaly = document.getElementsByName("exam.abdHepatomegaly")[0];
	abdHepatomegaly.value = $.trim(abdHepatomegaly.value);
	var abdSplenomegaly = document.getElementsByName("exam.abdSplenomegaly")[0];
	abdSplenomegaly.value = $.trim(abdSplenomegaly.value);
	var abdMovNoise = document.getElementsByName("exam.abdMovNoise")[0];
	abdMovNoise.value = $.trim(abdMovNoise.value);
	var otherAnusDre = document.getElementsByName("exam.otherAnusDre")[0];
	otherAnusDre.value = $.trim(otherAnusDre.value);
	var otherMammaryGland = document.getElementById("otherMammaryGland");
	otherMammaryGland.value = $.trim(otherMammaryGland.value);
	var vulva = document.getElementsByName("exam.vulva")[0];
	vulva.value = $.trim(vulva.value);
	var vagina = document.getElementsByName("exam.vagina")[0];
	vagina.value = $.trim(vagina.value);
	var cervical = document.getElementsByName("exam.cervical")[0];
	cervical.value = $.trim(cervical.value);
	var corpus = document.getElementsByName("exam.corpus")[0];
	corpus.value = $.trim(corpus.value);
	var attachment = document.getElementsByName("exam.attachment")[0];
	attachment.value = $.trim(attachment.value);
	var otherBodyExam = document.getElementsByName("exam.otherBodyExam")[0];
	otherBodyExam.value = $.trim(otherBodyExam.value);
	<%--辅助检查--%>
	var hemoglobin = document.getElementsByName("exam.hemoglobin")[0];
	var hemoglobinVal = $.trim(hemoglobin.value);
	hemoglobin.value = hemoglobinVal;
	if(hemoglobinVal && !/^\d{1,6}(\.\d{1,2})?$/.test(hemoglobinVal)){
		$.messager.alert("提示信息", "血红蛋白值不对，应为数值，最多6位整数位和2位小数.", "error", function(){
			hemoglobin.focus();
		});
		return false;
	}
	var whiteBloodCells = document.getElementsByName("exam.whiteBloodCells")[0];
	var whiteBloodCellsVal = $.trim(whiteBloodCells.value);
	whiteBloodCells.value = whiteBloodCellsVal;
	if(whiteBloodCellsVal && !/^\d{1,6}(\.\d{1,2})?$/.test(whiteBloodCellsVal)){
		$.messager.alert("提示信息", "白细胞值不对，应为数值，最多6位整数位和2位小数.", "error", function(){
			whiteBloodCells.focus();
		});
		return false;
	}
	var platelet = document.getElementsByName("exam.platelet")[0];
	var plateletVal = $.trim(platelet.value);
	platelet.value = plateletVal;
	if(plateletVal && !/^\d{1,6}(\.\d{1,2})?$/.test(plateletVal)){
		$.messager.alert("提示信息", "血小板值不对，应为数值，最多6位整数位和2位小数.", "error", function(){
			platelet.focus();
		});
		return false;
	}
	var otherBlood = document.getElementsByName("exam.otherBlood")[0];
	otherBlood.value = $.trim(otherBlood.value);
	var urineProteim = document.getElementsByName("exam.urineProteim")[0];
	urineProteim.value = $.trim(urineProteim.value);
	var urineSuger = document.getElementsByName("exam.urineSuger")[0];
	urineSuger.value = $.trim(urineSuger.value);
	var urineKetone = document.getElementsByName("exam.urineKetone")[0];
	urineKetone.value = $.trim(urineKetone.value);
	var urineEry = document.getElementsByName("exam.urineEry")[0];
	urineEry.value = $.trim(urineEry.value);
	var otherUrine = document.getElementsByName("exam.otherUrine")[0];
	otherUrine.value = $.trim(otherUrine.value);
	var fastingGlucose = document.getElementsByName("exam.fastingGlucose")[0];
	var fastingGlucoseVal = $.trim(fastingGlucose.value);
	fastingGlucose.value = fastingGlucoseVal;
	if(fastingGlucoseVal && (!/^\d{1,2}(\.\d)?$/.test(fastingGlucoseVal) || parseFloat(fastingGlucoseVal, 10)<=0)){
		$.messager.alert("提示信息", "空腹血糖值不对，应为正数，最多2位整数位和1位小数.", "error", function(){
			fastingGlucose.focus();
		});
		return false;
	}
	var electrocardiogram = document.getElementsByName("exam.electrocardiogram")[0];
	electrocardiogram.value = $.trim(electrocardiogram.value);
	var urineTraceAlbumin = document.getElementsByName("exam.urineTraceAlbumin")[0];
	var urineTraceAlbuminVal = $.trim(urineTraceAlbumin.value);
	urineTraceAlbumin.value = urineTraceAlbuminVal;
	if(urineTraceAlbuminVal && !/^[1-9]\d*$/.test(urineTraceAlbuminVal)){
		$.messager.alert("提示信息", "尿微量白蛋白值不对，应为正整数.", "error", function(){
			urineTraceAlbumin.focus();
		});
		return false;
	}
	var glycatedHemoglobin = document.getElementsByName("exam.glycatedHemoglobin")[0];
	var glycatedHemoglobinVal = $.trim(glycatedHemoglobin.value);
	glycatedHemoglobin.value = glycatedHemoglobinVal;
	if(glycatedHemoglobinVal && !/^\d{1,3}(\.\d{1,2})?$/.test(glycatedHemoglobinVal)){
		$.messager.alert("提示信息", "糖化血糖蛋白值不对，应为数值，最多3位整数位和2位小数.", "error", function(){
			glycatedHemoglobin.focus();
		});
		return false;
	}
	var serumAlt = document.getElementsByName("exam.serumAlt")[0];
	var serumAltVal = $.trim(serumAlt.value);
	serumAlt.value = serumAltVal;
	if(serumAltVal && !/^\d{1,6}(\.\d{1,2})?$/.test(serumAltVal)){
		$.messager.alert("提示信息", "血清谷丙转氨酶值不对，应为数值，最多6位整数位和2位小数.", "error", function(){
			serumAlt.focus();
		});
		return false;
	}
	var serumAspertateAtf = document.getElementsByName("exam.serumAspertateAtf")[0];
	var serumAspertateAtfVal = $.trim(serumAspertateAtf.value);
	serumAspertateAtf.value = serumAspertateAtfVal;
	if(serumAspertateAtfVal && !/^\d{1,6}(\.\d{1,2})?$/.test(serumAspertateAtfVal)){
		$.messager.alert("提示信息", "血清谷草转氨酶值不对，应为数值，最多6位整数位和2位小数.", "error", function(){
			serumAspertateAtf.focus();
		});
		return false;
	}
	var albumin = document.getElementsByName("exam.albumin")[0];
	var albuminVal = $.trim(albumin.value);
	albumin.value = albuminVal;
	if(albuminVal && !/^\d{1,6}(\.\d{1,2})?$/.test(albuminVal)){
		$.messager.alert("提示信息", "白蛋白值不对，应为数值，最多6位整数位和2位小数.", "error", function(){
			albumin.focus();
		});
		return false;
	}
	var totalBilirubin = document.getElementsByName("exam.totalBilirubin")[0];
	var totalBilirubinVal = $.trim(totalBilirubin.value);
	totalBilirubin.value = totalBilirubinVal;
	if(totalBilirubinVal && !/^\d{1,6}(\.\d{1,2})?$/.test(totalBilirubinVal)){
		$.messager.alert("提示信息", "总胆红素值不对，应为数值，最多6位整数位和2位小数.", "error", function(){
			totalBilirubin.focus();
		});
		return false;
	}
	var combiningBilirubin = document.getElementsByName("exam.combiningBilirubin")[0];
	var combiningBilirubinVal = $.trim(combiningBilirubin.value);
	combiningBilirubin.value = combiningBilirubinVal;
	if(combiningBilirubinVal && !/^\d{1,6}(\.\d{1,2})?$/.test(combiningBilirubinVal)){
		$.messager.alert("提示信息", "结合胆红素值不对，应为数值，最多6位整数位和2位小数.", "error", function(){
			combiningBilirubin.focus();
		});
		return false;
	}
	var serumCreatinine = document.getElementsByName("exam.serumCreatinine")[0];
	var serumCreatinineVal = $.trim(serumCreatinine.value);
	serumCreatinine.value = serumCreatinineVal;
	if(serumCreatinineVal && !/^\d{1,6}(\.\d{1,2})?$/.test(serumCreatinineVal)){
		$.messager.alert("提示信息", "血清肌酐值不对，应为数值，最多6位整数位和2位小数.", "error", function(){
			serumCreatinine.focus();
		});
		return false;
	}
	var bloodUreaNitrogen = document.getElementsByName("exam.bloodUreaNitrogen")[0];
	var bloodUreaNitrogenVal = $.trim(bloodUreaNitrogen.value);
	bloodUreaNitrogen.value = bloodUreaNitrogenVal;
	if(bloodUreaNitrogenVal && !/^\d{1,6}(\.\d{1,2})?$/.test(bloodUreaNitrogenVal)){
		$.messager.alert("提示信息", "血尿素氮值不对，应为数值，最多6位整数位和2位小数.", "error", function(){
			bloodUreaNitrogen.focus();
		});
		return false;
	}
	var potassiumConcentration = document.getElementsByName("exam.potassiumConcentration")[0];
	var potassiumConcentrationVal = $.trim(potassiumConcentration.value);
	potassiumConcentration.value = potassiumConcentrationVal;
	if(potassiumConcentrationVal && !/^\d{1,6}(\.\d{1,2})?$/.test(potassiumConcentrationVal)){
		$.messager.alert("提示信息", "血钾浓度值不对，应为数值，最多6位整数位和2位小数.", "error", function(){
			potassiumConcentration.focus();
		});
		return false;
	}
	var sodiumConcentration = document.getElementsByName("exam.sodiumConcentration")[0];
	var sodiumConcentrationVal = $.trim(sodiumConcentration.value);
	sodiumConcentration.value = sodiumConcentrationVal;
	if(sodiumConcentrationVal && !/^\d{1,6}(\.\d{1,2})?$/.test(sodiumConcentrationVal)){
		$.messager.alert("提示信息", "血钠浓度值不对，应为数值，最多6位整数位和2位小数.", "error", function(){
			sodiumConcentration.focus();
		});
		return false;
	}
	var totalCholesterol = document.getElementsByName("exam.totalCholesterol")[0];
	var totalCholesterolVal = $.trim(totalCholesterol.value);
	totalCholesterol.value = totalCholesterolVal;
	if(totalCholesterolVal && !/^\d{1,6}(\.\d{1,2})?$/.test(totalCholesterolVal)){
		$.messager.alert("提示信息", "总胆固醇值不对，应为数值，最多6位整数位和2位小数.", "error", function(){
			totalCholesterol.focus();
		});
		return false;
	}
	var triglycerides = document.getElementsByName("exam.triglycerides")[0];
	var triglyceridesVal = $.trim(triglycerides.value);
	triglycerides.value = triglyceridesVal;
	if(triglyceridesVal && !/^\d{1,6}(\.\d{1,2})?$/.test(triglyceridesVal)){
		$.messager.alert("提示信息", "甘油三酯值不对，应为数值，最多6位整数位和2位小数.", "error", function(){
			triglycerides.focus();
		});
		return false;
	}
	var sldlc = document.getElementsByName("exam.sldlc")[0];
	var sldlcVal = $.trim(sldlc.value);
	sldlc.value = sldlcVal;
	if(sldlcVal && !/^\d{1,6}(\.\d{1,2})?$/.test(sldlcVal)){
		$.messager.alert("提示信息", "血清低密度脂蛋白胆固醇值不对，应为数值，最多6位整数位和2位小数.", "error", function(){
			sldlc.focus();
		});
		return false;
	}
	var shdlc = document.getElementsByName("exam.shdlc")[0];
	var shdlcVal = $.trim(shdlc.value);
	shdlc.value = shdlcVal;
	if(shdlcVal && !/^\d{1,6}(\.\d{1,2})?$/.test(shdlcVal)){
		$.messager.alert("提示信息", "血清高密度脂蛋白胆固醇值不对，应为数值，最多6位整数位和2位小数.", "error", function(){
			shdlc.focus();
		});
		return false;
	}
	var chestXRay = document.getElementsByName("exam.chestXRay")[0];
	chestXRay.value = $.trim(chestXRay.value);
	var bUltrasound = document.getElementsByName("exam.bUltrasound")[0];
	bUltrasound.value = $.trim(bUltrasound.value);
	var cervicalSmears = document.getElementsByName("exam.cervicalSmears")[0];
	cervicalSmears.value = $.trim(cervicalSmears.value);
	var otherAssistExam = document.getElementsByName("exam.otherAssistExam")[0];
	otherAssistExam.value = $.trim(otherAssistExam.value);
	<%--现存主要健康问题--%>
	var otherCereDisease = document.getElementById("otherCereDisease");
	otherCereDisease.value = $.trim(otherCereDisease.value);
	var otherRenalDisease = document.getElementById("otherRenalDisease");
	otherRenalDisease.value = $.trim(otherRenalDisease.value);
	var otherHeartDisease = document.getElementById("otherHeartDisease");
	otherHeartDisease.value = $.trim(otherHeartDisease.value);
	var otherVascularDisease = document.getElementById("otherVascularDisease");
	otherVascularDisease.value = $.trim(otherVascularDisease.value);
	var otherEyeDisease = document.getElementById("otherEyeDisease");
	otherEyeDisease.value = $.trim(otherEyeDisease.value);
	var ond = document.getElementsByName("exam.ond")[0];
	ond.value = $.trim(ond.value);
	var otherSysDisease = document.getElementsByName("exam.otherSysDisease")[0];
	otherSysDisease.value = $.trim(otherSysDisease.value);
	var healthyAssess = document.getElementById("healthyAssess");
	healthyAssessVal = healthyAssess.value;
	if(healthyAssessVal&&healthyAssessVal.length>255){
		$.messager.alert("提示信息", "体检评价不能超过255个字符.", "error", function(){
			healthyAssess.focus();
		});
		return false;
	}
	<%--健康指导--%>
	var dangerControlWeight = document.getElementsByName("exam.dangerControlWeight")[0];
	var dangerControlWeightVal = $.trim(dangerControlWeight.value);
	dangerControlWeight.value = dangerControlWeightVal;
	if(dangerControlWeightVal && !/^\d{1,3}(\.\d)?$/.test(dangerControlWeightVal)){
		$.messager.alert("提示信息", "减体重目标值不对，应为数值，最多3位整数位和1位小数.", "error", function(){
			dangerControlWeight.focus();
		});
		return false;
	}
	var dangerControlVaccin = document.getElementsByName("exam.dangerControlVaccin")[0];
	dangerControlVaccin.value = $.trim(dangerControlVaccin.value);
	var dangerControlOther = document.getElementById("dangerControlOther");
	dangerControlOther.value = $.trim(dangerControlOther.value);
	<%--危害因素接触史--%>
	var posWorkTimes = document.getElementsByName("posWorkTime");
	if(posWorkTimes){
		for(var i=0;i<posWorkTimes.length;i++){
			var wtVal = $.trim(posWorkTimes[i].value);
			posWorkTimes[i].value = wtVal;
			if(wtVal&&!/^\d+(\.\d)?$/.test(wtVal)){
				$.messager.alert("提示信息", "危害因素接触时长值不对，应为数值，最多2位整数位和1位小数.", "error", function(){
					posWorkTimes[i].focus();
				});
				return false;
			}
		}
	}
	return true;
}
function saveExam(){
	if(!validForm()){
		return;
	}
	// 表单提交
	$("#fm").form("submit", {
		url : "<%=request.getContextPath()%>/chk/exam_save.action",
		dataType : "json",
		success : function(data) {
			data =  $.parseJSON(data);
			if(data.state > 0){
				$.messager.alert("提示信息", "保存成功.", "info", function(){
					var examId = document.getElementById("examId");
					if(!examId.value && data.data){
						examId.value = data.data;
					}
					refreshPage("${param.pPageId}");
					closeIt();
				});
			}else{
				$.messager.alert("提示信息", data.msg?data.msg:"保存出错.", "error");
			}
		}
	});
}
</script>
