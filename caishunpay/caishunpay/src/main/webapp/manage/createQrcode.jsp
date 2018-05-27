<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="include.jsp" %>

<html lang="en"><head>
    <title>彩顺网络科技平台</title>
</head>
<body class=" theme-blue">


    <script type="text/javascript">
        
    </script>

    <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->


  <!--[if lt IE 7 ]> <body class="ie ie6"> <![endif]-->
  <!--[if IE 7 ]> <body class="ie ie7 "> <![endif]-->
  <!--[if IE 8 ]> <body class="ie ie8 "> <![endif]-->
  <!--[if IE 9 ]> <body class="ie ie9 "> <![endif]-->
  <!--[if (gt IE 9)|!(IE)]><!--> 
   
  <!--<![endif]-->

    

<div id="loading" class="loading" style="">Loading pages...</div>  
        <div class="dialog">
    <div class="panel panel-default">
        <p class="panel-heading no-collapse">登录</p>
        <div class="panel-body">
            <form action="getNativeUrl.do" method="post" id="loginForm">
                <div class="form-group">
                    <label>商户号</label>
                    
                    <input type="text" class="form-control span12" name="merchantId" id="merchantId" value="6220002152">
                </div>
                <div class="form-group">
                    <label>订单号</label>
                    <input type="text" class="form-control span12" name="tradeSn" id="tradeSn" value="20170331100701">
                </div>
                <div class="form-group">
                    <label>交易金额</label>
                    <input type="text" class="form-control span12" name="orderAmount" id="orderAmount" value="2">
                </div>
                <div class="form-group">
                    <label>商户名称</label>
                    <input type="text" class="form-control span12" name="goodsName" id="goodsName" value="测试扫码商品">
                </div>
                <div class="form-group">
                    <label>通知地址</label>
                    <input type="text" class="form-control span12" name="notifyUrl" id="notifyUrl" value="www.baidu.com">
                </div>
                <div class="form-group">
                	<label>支付来源：</label></td><td>
					            <select name="tradeSource" id="tradeSource" class="form-control">
					              <option value="2">微信</option>
					              <option value="1">支付宝</option>
					              <option value="4">QQ钱包</option>
					              <option value="22">微信wap</option>
					              <option value="12">支付宝wap</option>
					            </select>
                </div>
                <%--<div class="form-group">
                    <label>是否T0</label>
                    <input type="text" class="form-control span12" name="t0Flag" id="t0Flag" value="1">
                </div>
                <div class="form-group">
                    <label>T0收款身份证</label>
                    <input type="text" class="form-control span12" name="idcard" id="idcard" value="362324199107253051">
                </div>
                <div class="form-group">
                    <label>T0收款人姓名</label>
                    <input type="text" class="form-control span12" name="realname" id="realname" value="叶建文">
                </div>
                <div class="form-group">
                    <label>收款卡号</label>
                    <input type="text" class="form-control span12" name="bankcardnum" id="bankcardnum" value="6214830202948815">
                </div>
                <div class="form-group">
                    <label>手续费</label>
                    <input type="text" class="form-control span12" name="free" id="free" value="1">
                </div>
                <div class="form-group">
                    <label>开户行名称</label>
                    <input type="text" class="form-control span12" name="bankname" id="bankname" value="招商银行">
                </div>
                <div class="form-group">
                    <label>支行名称</label>
                    <input type="text" class="form-control span12" name="subbranch" id="subbranch" value="招商银行广州天府路支行">
                </div>
                <div class="form-group">
                    <label>联行号</label>
                    <input type="text" class="form-control span12" name="pcnaps" id="pcnaps" value="308581002345">
                </div>
                --%><div class="form-group">
                    <label>签名</label>
                    <input type="text" class="form-control span12" name="sign" id="sign" value="">
                </div>
                <input type="submit" value="生成" class="btn btn-primary pull-right" >
                <%--<label class="remember-me"><input type="checkbox"> 记住密码</label>
                --%><div class="clearfix"></div>
            </form>
        </div>
    </div>
    <%--<p class="pull-right" style=""><a href="http://www.portnine.com" target="blank" style="font-size: .75em; margin-top: .25em;">Design by Portnine</a></p>
    --%>
</div>

<script type="text/javascript">

$(function() {
	
    var match = document.cookie.match(new RegExp('color=([^;]+)'));
    if(match) var color = match[1];
    if(color) {
        $('body').removeClass(function (index, css) {
            return (css.match (/\btheme-\S+/g) || []).join(' ')
        })
        $('body').addClass('theme-' + color);
    }

//    $('[data-popover="true"]').popover({html: true});
    
    var uls = $('.sidebar-nav > ul > *').clone();
    uls.addClass('visible-xs');
    $('#main-menu').append(uls.clone());
    
    $("#loading").hide();

    var timeStamp = (new Date()).valueOf();
    //$("#tradeSn").val(timeStamp);
    
});

//$("[rel=tooltip]").tooltip();

</script>

</body></html>
