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
<div align="center" id="qrcode">
		<p >
		扫我，扫我
		<br><br>
		QQ:553018567
		</p>
	</div>
 </body>

 <script>
 	//这个地址是Demo.java生成的code_url,这个很关键
	var url = "weixin://wxpay/bizpayurl?pr=IrbYjQm";
	
	//参数1表示图像大小，取值范围1-10；参数2表示质量，取值范围'L','M','Q','H'
	var qr = qrcode(10, 'M');
	qr.addData(url);
	qr.make();
	var dom=document.createElement('DIV');
	dom.innerHTML = qr.createImgTag();
	var element=document.getElementById("qrcode");
	element.appendChild(dom);
 </script>
</html>
