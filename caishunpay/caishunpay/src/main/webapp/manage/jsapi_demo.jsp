<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>My JSP 'hello.jsp' starting page</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script src="<%= request.getContextPath()%>/common_js/jquery.min.js" type="text/javascript"></script>
	<style>
	.choose{width:500px;height:300px;margin:0 auto;background:#FCF;margin-top:150px;text-align:center;padding-top:100px;}
    </style>
		
	</head>

	<body>
	<div align="center">
		<p >请注意，微信公众平台的授权目录一定要配置到这也页面所在的目录哦</p>
	</div>
	
	<div class="choose" >
		Click me to pay!
		<br><br>
	</div>

 <script>
 	$(document).ready( function(){
		//点击测试,注意参数是demo中生成的package
		$(".choose").click(function(){
		    WeixinJSBridge.invoke('getBrandWCPayRequest',{"appId":"wx1f87d44db95cba7a","timeStamp":"1472195652589","nonceStr":"1472195652589","signType":"MD5","package":"prepay_id=wx20160826151412e58bcaec6c0620361661","paySign":"7954F0C0F4A0A6F267186736C9E870F7"},
		    function(res){
			    alert(res.err_code + "----" + res.err_msg);
		       //支付成功或失败前台判断
    	       if(res.err_msg=='get_brand_wcpay_request:ok'){
    	    	   alert('恭喜您，支付成功!');
    	       }else{
    	    	   alert('支付失败');
    	       }
		     });
	    });
  });
 </script>
		
	</body>
</html>
