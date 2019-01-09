<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/inc.jsp"%>
	<title>管理员首页</title>
	<style type="text/css">
	body{
		padding: 25px;
		width: 90%;
		margin-left: auto;
		margin-right: auto;
		min-width: 400px;
	}
	.pageTitle{
		text-align: center;
		font-size: 16px;
		font-weight: bold;
	}
	.sec{
		margin: 15px;
	}
	.funTitle{
		font-size: 15px;
		padding: 5px;
	}
	.funContent{
		font-size: 15px;
		padding: 5px;
	}
	.titleLink{
		text-decoration: none;
		font-weight: bold;
		color: #0065CC;
	}
	.titleLink:VISITED{
		color: #0065CC;
	}
	.titleLink:HOVER{
		text-decoration: underline;
	}
	</style>
	<script type="text/javascript">
		function openTab(title, url){
			top.addTab(title, url);
		}
	</script>
</head>
<body>
	<div class="pageTitle">管理员常用功能介绍</div>
	<div class="sec">
		<div class="funTitle">
			<a class="titleLink" href="#" onclick="openTab('医生管理', '<%=request.getContextPath()%>/sys/doctor/doctor.jsp')">医生管理</a>
		</div>
		<div class="funContent">负责系统医生信息的维护，医生必须属于一个组织，医生可以查询该组织下的所有普通客户，并可以对管理的会员提供服务。</div>
	</div>
	<div class="sec">
		<div class="funTitle">
			<a class="titleLink" href="#" onclick="openTab('用户管理', '<%=request.getContextPath()%>/sys/customer/customers.jsp')">用户管理</a>
		</div>
		<div class="funContent">普通用户指的是系统客户，管理员、医生都可以添加普通用户。医生添加普通用户与医生属于同一个组织。</div>
	</div>
	<div class="sec">
		<div class="funTitle">
			<a class="titleLink" href="#" onclick="openTab('会员管理', '<%=request.getContextPath()%>/archive/memberList.jsp')">会员管理</a>
		</div>
		<div class="funContent">普通用户交费后可成为系统会员，系统管理员必须为会员分配一个健康管理师为会员提供服务。</div>
	</div>
</body>
</html>