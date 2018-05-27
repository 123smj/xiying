<%@ page isELIgnored="false" %>
<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.manage.bean.OprInfo,com.trade.bean.*,com.manage.bean.*"%>
<%
OprInfo oprInfo = (OprInfo) request.getSession().getAttribute("oprInfo");
%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

	<meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<base href="<%=basePath%>">
	
	<!--<link href='http://fonts.googleapis.com/css?family=Open+Sans:400,700' rel='stylesheet' type='text/css'>-->
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/lib/bootstrap/css/bootstrap.css">
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/lib/bootstrap/css/bootstrap-datetimepicker.min.css">
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/lib/bootstrap/css/bootstrap-select.css">
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/lib/bootstrap/css/ladda-themeless.min.css">
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/lib/bootstrap/css/fileinput.min.css">
	
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/stylesheets/xcConfirm.css?version=0705">
	
    <link rel="stylesheet" href="<%= request.getContextPath()%>/lib/font-awesome/css/font-awesome.css">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/stylesheets/theme.css">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/stylesheets/comon.css?version=10705">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/stylesheets/premium.css">
    <link href="<%= request.getContextPath()%>/build/toastr.css?version=0707" rel="stylesheet" />
    
    <!-- Le fav and touch icons -->
    <link rel="apple-touch-icon-precomposed" sizes="144x144" href="../assets/ico/apple-touch-icon-144-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="../assets/ico/apple-touch-icon-114-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="72x72" href="../assets/ico/apple-touch-icon-72-precomposed.png">
    <link rel="apple-touch-icon-precomposed" href="../assets/ico/apple-touch-icon-57-precomposed.png">
    
    <script src="<%= request.getContextPath()%>/lib/jquery-1.11.1.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="<%= request.getContextPath()%>/common_js/md5.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath()%>/common_js/common.js?version=1010" ></script>
    <script type="text/javascript" src="<%= request.getContextPath()%>/common_js/idCard.js?version=0409"></script>
    
    <script type="text/javascript" src="<%= request.getContextPath()%>/common_js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath()%>/common_js/xcConfirm.js"></script>
    
    <script type="text/javascript" src="<%= request.getContextPath()%>/dwr/engine.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath()%>/dwr/interface/SelectOptionsDWR.js"></script>
    <script src="<%= request.getContextPath()%>/build/toastr.min.js"></script>
    <script src="<%= request.getContextPath()%>/lib/bootstrap/js/bootstrap.js"></script>
    <script src="<%= request.getContextPath()%>/lib/bootstrap/js/bootstrap-datetimepicker.min.js?version=0701"></script>
    <script src="<%= request.getContextPath()%>/lib/bootstrap/js/bootstrap-select.js"></script>
    <script src="<%= request.getContextPath()%>/lib/bootstrap/js/spin.min.js"></script>
    <script src="<%= request.getContextPath()%>/lib/bootstrap/js/ladda.min.js"></script>
    <script src="<%= request.getContextPath()%>/lib/bootstrap/js/bootstrap-treeview.js"></script>
    <script src="<%= request.getContextPath()%>/lib/bootstrap/js/fileinput.min.js"></script>
    <script src="<%= request.getContextPath()%>/lib/bootstrap/js/zh.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath()%>/lib/bootstrap/js/locles/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>


</head>
<body>



<script type="text/javascript">
</script>
<script type="text/javascript">
	
	window["contextPath"] = "${pageContext.request.contextPath}";
	window["sessionId"] = "${pageContext.session.id}";
	window["sessionName"] = "jsessionid";
	//初始化toastr.js
	toastr.options = {positionClass:'toast-center', timeOut: "2000"};
</script>
</body>
</html>