<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.yzxt.yh.util.StringUtil"%>
<%@ page import="com.yzxt.yh.module.sys.bean.Org"%>
<%
Org org = (Org)request.getAttribute("org");
%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../../common/inc.jsp"%>
<title>客户端帮助</title>
<style type="text/css">
	.top{width: 60%;min-width: 800px;margin-left: auto;margin-right: auto;margin-top: 20px;}
	.top-left{float: left;}
	.logoImg{height: 40px;max-width: 500px;overflow: hidden;}
	.logoText{max-width: 500px;height: 40px;line-height: 40px;font-size: 26px;font-weight: normal;font-family: 'microsoft yahei', 'Times New Roman', '宋体', Times, serif;color: #435527;}
	.colline{width: 2px;height: 40px;background: #E7E7E7;float:left;margin-left:5px;}
	.itemName{font-size: 22px;color: #666;font-family: 黑体;line-height: 40px;height: 40px;width: 120px;float:left;margin-left: 5px;}
	.top-right{float: right;height: 40px;}
	.top-right span{line-height: 40px;font-size: 12px;}
	.titleSplit{clear: both;margin-top: 10px;margin-bottom: 10px;margin-left: auto;margin-right: auto;width: 60%;min-width: 800px;height: 1px;background-color: #CCC;}
	<%--帮助样式--%>
	.warp {
		width: 60%;min-width: 800px;
		margin: 20px auto 20px auto;
	}
	.header {
		font-size: 18px;
		font-family: "微软雅黑";
		padding-top: 15px;
		padding-bottom: 5px;
		color: #FF6600;
	}
	.qWarp{
		padding: 5px;
	}
	.qTitle{
		padding: 5px;
	}
	.qAnswer{
		padding: 5px;
		padding-left: 20px;
	}
</style>
<script type="text/javascript">
</script>
</head>
<body>
	<div class="top">
		<div class="top-left">
		<%
		if(org!=null)
		{
			if(StringUtil.isNotEmpty(org.getLogoPath()))
			{
		%>
			<img class="logoImg" src="<%=request.getContextPath()%>/pub/cf_img.action?pt=<%=org.getLogoPath()%>" />
		<%
			}else
			{
		%>
			<span class="logoText"><%=org.getShowText()%></span>
		<%
			}
		}else
		{
		%>
			<%-- <img class="logoImg" src="<%=request.getContextPath()%>/resources/img/portal/logo.png" /> --%>
		<%
		}
		%>
		</div>
		<div class="top-left colline"></div>
		<div class="top-left itemName">
			<span>帮助中心</span>
		</div>
		<div class="top-right">
			<span>已有账号?&nbsp;去<a href="<%=request.getContextPath()%>/sys/wel_login.action<%=org!=null?"?orgId="+org.getShowId():""%>">登录</a></span>
		</div>
	</div>
	<div class="titleSplit"></div>
	<div class="warp">
		<div class="header">检测记录</div>
		<div class="secWarp">
			<div class="qWarp">
				<div class="qTitle">1) 如何同步服务器端的测量数据</div>
				<div class="qAnswer">
					进入我的检测记录界面，选择想要查看的体征项，弹出记录列表栏后对列表栏进行下拉即可同步服务器端测量数据，并显示在检测记录列表中。
				</div>
			</div>
		</div>
		<div class="header">家庭管理</div>
		<div class="secWarp">
			<div class="qWarp">
				<div class="qTitle">1) 如何添加家庭管理成员</div>
				<div class="qAnswer">
					在首页选择更多选项，进入家庭成员项，点击右上角添加，输入家人用户名或手机号即可发送添加申请，等待同意后即添加成功。
				</div>
			</div>
			<div class="qWarp">
				<div class="qTitle">2) 如何查看家庭成员检测记录</div>
				<div class="qAnswer">
					进入家庭成员选项，可以看到家庭成员列表，点击列表中某位家人，即可查看他的检测记录。
				</div>
			</div>
		</div>
		<div class="header">健康报告</div>
		<div class="secWarp">
			<div class="qWarp">
				<div class="qTitle">1) 可查看健康报告的周期疑问</div>
				<div class="qAnswer">
					由于需要数据累计到一定量方便趋势的查看，健康管家设置每周生成一份健康报告，生成时间为每周星期一凌晨生成上一周健康报告。
				</div>
			</div>
		</div>
		<div class="header">健康预警</div>
		<div class="secWarp">
			<div class="qWarp">
				<div class="qTitle">1) 慢病三级预警的分级标准是什么？</div>
				<div class="qAnswer">
				<span style="font-weight: bold;">高血压</span>
				<br />
				正常血压：90mmHg≤收缩压＜120mmHg和60mmHg≤舒张压80mmHg
				<br />
				正常高值血压：120mmHg≤收缩压≤139mmHg和（或）80mmHg≤舒张压≤89mmHg
				<br />
				平台一级预警：1级高血压（轻度）：140mmHg≤收缩压≤159mmHg和（或）90mmHg≤舒张压≤99mmHg
				<br />
				平台二级预警: 2级高血压（中度）：160mmHg≤收缩压≤179mmHg和（或）100mmHg≤舒张压≤109mmHg
				<br />
				平台三级预警: 3级高血压（重度）:收缩压≥180mmHg和（或）舒张压≥110 b) 保存血糖告警信息
				<br /><br />
				<span style="font-weight: bold;">血糖</span>
				<br />
				正常血糖: 空腹血糖＜6.1mmol/L
				<br />
				空腹血糖受损：6.1mmol/L≤空腹血糖＜7.0mmol/L和糖负荷后2h血糖＜7.8mmol/L
				<br />
				糖耐量异常：空腹血糖＜7.0mmol/L和7.8mmol/L≤糖负荷后2h血糖＜11.1mmol/L
				<br />
				糖尿病：空腹血糖≥7.0mmol/L或糖负荷后2h血糖≥11.1mmol/L
				<br />
				平台一级预警：初次诊断为糖尿病、糖尿病前期
				<br />
				平台二级预警：3.9mmol/L≤任意血糖＜4.4mmol/L或13.9mmol/L≤任意血糖＜16.7mmol/L
				<br />
				平台三级预警: 任意血糖＜3.9mmol/L或任意血糖≥16.7mmol/L</div>
			</div>
			<div class="qWarp">
				<div class="qTitle">2) 慢病预警的显示规则?</div>
				<div class="qAnswer">
					以每天为单位，若测量数据的预警级别为初级或中级则显示当天第一次测量得出预警的值，若测量数据的预警级别升级或为高级，则每一次预警级别升级或预警级别为高级的信息都会显示在预警列表中。
				</div>
			</div>
		</div>
	</div>
</body>
</html>