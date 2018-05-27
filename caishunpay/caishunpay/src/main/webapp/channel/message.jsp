<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.manage.bean.PageModle"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@page import="com.manage.bean.OprInfo,com.trade.bean.*,com.gy.util.*"%>
<%@ include file="../manage/include.jsp"%>

<html lang="en">
	<head>
		<title>消息</title>
		<script type="text/javascript">
	
    </script>
	</head>
	<body class="theme-blue">
			
		<div style="margin-left: 30px; margin-top: 20px"><%="00000".equals(request.getAttribute("result")) ? "成功": request.getAttribute("result")%>
		
		<a  data-toggle="modal" class="btn btn-danger" id="backBtn">返回</a>
		</div>
		<script type="text/javascript">
		$(function() {
			$("#backBtn").click(function(){
				history.back();
			});
		});
		</script>
	</body>
</html>