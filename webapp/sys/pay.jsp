<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.yzxt.yh.module.sys.bean.Order"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	Order order = (Order)request.getAttribute("order");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%@ include file="../common/inc.jsp"%>
 <script type="text/javascript">
<%--  	$(function(){
 		  window.loaction.href="<%=request.getContextPath()%>/mobile/corder_createQRCode.action?orderId=<%= order.getOid()%>";
 	}); --%>
</script>
</head>
<body>
   
     <div>
     <!-- 访问Controller的方法，将生成的二维码图片显示在这 -->
     	<input type="text" name="dsag"/>
     	
        <img alt="" src="<%=request.getContextPath()%>/mobile/corder_createQRCode.action?orderId=<%= order.getOid()%>"> 
    </div> 
    
    <%-- <script>
    /* ajax轮回，不停的访问Controller，直到wxPayType=1时，付款成功 */
         var num = 0;
        $(function(){
             panduanWXPay();
        }); 
        function panduanWXPay(){
            $.post("<%=basePath%>index/panduanPay",function(data){
                var wxPayType = data.wxPayType;
                if(wxPayType==1){
                /* 成功 */
                    window.location.href='<%=basePath%>index/gouMai';
                }else if(wxPayType==0 && num!=400){
                     num++;
                     panduanWXPay();
                }else{
                    alert("支付超时");
                }
            });
        }  
    </script> --%>
</body>
</html>