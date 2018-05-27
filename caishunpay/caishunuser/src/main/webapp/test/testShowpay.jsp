<%@ page language="java"
         import="com.trade.bean.response.ResponseCode,com.gy.util.StringUtil,com.trade.enums.*"
         pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    ResponseCode responseCode = (ResponseCode) request.getAttribute("responseCode");
    String tradeSource = (String) request.getAttribute("tradeSource");
    String orderAmount = String.valueOf(request.getAttribute("orderAmount"));
    String outTradeNo = (String) request.getAttribute("outTradeNo");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title><%=TradeSourceEnum.get(tradeSource) == null ? "" : TradeSourceEnum.get(tradeSource).getMemo()%>
    </title>
    <meta http-equiv="content-type" content="text/html;charset=utf-8">
    <!--<meta name="viewport" content="width=device-width, minimum-scale=1, maximum-scale=1">-->
    <meta name="viewport" content="width=320,maximum-scale=1.3,user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black">

    <!--for original iPhone-->
    <%--		<link rel='stylesheet' href="<%= request.getContextPath()%>/stylesheets/iPhone4.css" media='only screen and (-webkit-min-device-pixel-ratio: 1.5)' >--%>
    <!--for iPhone four-->
    <style type="text/css">
        * {
            margin: 0;
            padding: 0;
        }

        input {
            -webkit-tap-highlight-color: transparent;
        }

        body {
            background-color: #f9f9f9;
            font-family: Arial, STHeiti, Helvetica, sans-serif;
            font-size: 12px;
            color: #666;
            padding-top: 4px;
        }

        h2 {
            color: #000;
            font-weight: normal;
            font-size: 15px;
            line-height: 2.6em;
            padding: 0 4.6%;
        }

        div.qrcode {
            width: 100%;
            margin: 1.5em auto;
        }

        div.icon {
            width: 31.25%;
            margin: 1.5em auto;
        }

        div.icon img {
            width: 100%;
        }

        .message {
            text-align: center;
            font-weight: bold;
        }

        .blue {
            background-color: #39c;
            color: #fff;
        }

        .close {
            border: 1px solid #ccc;
            background: linear-gradient(#fff, #ddd);
            color: #666;
        }

        a.btn {
            display: inline-block;
            width: 93.6%;
            margin: 8px 3.2%;
            text-align: center;
            font-size: 18px;
            line-height: 2.6em;
            border-radius: 0.3em;
            text-decoration: none;
        }

        .copyright {
            width: 90%;
            margin: 5px auto;
            font-size: 10px;
            text-align: center;
            line-height: 2em;
            color: #ccc;
        }
    </style>
    <script src="<%= request.getContextPath()%>/lib/jquery-1.11.1.min.js"
            type="text/javascript"></script><!--
		<script src="js/i18n/jquery.i18n.properties-1.0.9.js"></script>
		-->
    <script src="<%= request.getContextPath()%>/common_js/jquery.qrcode.js"
            type="text/javascript"></script>
    <script src="<%= request.getContextPath()%>/common_js/qrcode.js"
            type="text/javascript"></script>
    <script src="<%= request.getContextPath()%>/common_js/utf.js" type="text/javascript"></script>
    <script type="text/javascript">
        function wxpay() {
            WeixinJSBridge.call('closeWindow');
        }
    </script>
</head>

<body>
<div align="center" class="qrcode" id="qrcode"></div>
<div align="center">交易金额：<strong><%= StringUtil.changeF2Y(Integer.valueOf(orderAmount))%>
</strong></div>
<h2 class="message"><font
        color="red">打开<%=TradeSourceEnum.get(tradeSource) == null ? "" : TradeSourceEnum.get(tradeSource).getMemo()%>
    ，扫一扫支付</font></h2>
</body>
<script type="text/javascript">
    var url = "<%= responseCode.getCode_url()%>";
    //参数1表示图像大小，取值范围1-10；参数2表示质量，取值范围'L','M','Q','H'
    var qr = qrcode(10, 'M');
    qr.addData(url);
    qr.make();
    var dom = document.createElement('DIV');
    dom.innerHTML = qr.createImgTag();
    var element = document.getElementById("qrcode");
    element.appendChild(dom);


</script>
</html>