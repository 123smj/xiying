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
            <form action="quickpay/checkPay.do" method="post" id="loginForm">
                <div class="form-group">
                    <label>商户号</label>
                    
                    <input type="text" class="form-control span12" name="merchantId" id="merchantId" value="6330001011">
                </div>
                <div class="form-group">
                    <label>订单号</label>
                    <input type="text" class="form-control span12" name="tradeSn" id="tradeSn" value="201704091648161">
                </div>
                <div class="form-group">
                    <label>交易金额</label>
                    <input type="text" class="form-control span12" name="orderAmount" id="orderAmount" value="1">
                </div>
                <div class="form-group">
                    <label>持卡人姓名</label>
                    <input type="text" class="form-control span12" name="cardHolderName" id="cardHolderName" value="叶建文">
                </div>
                <div class="form-group">
                    <label>持卡人卡号</label>
                    <input type="text" class="form-control span12" name="cardNo" id="cardNo" value="6225768722687439">
                </div>
                <div class="form-group">
                    <label>卡类型</label>
                    <select name="cardType" id="cardType" class="form-control">
					              <option value="00">贷记卡</option>
					              <option value="01">借记卡</option>
					</select>
                </div>
                <div class="form-group">
                    <label>卡有效期</label>
                    <input type="text" class="form-control span12" name="expireDate" id="expireDate" value="0821">
                </div>
                <div class="form-group">
                    <label>cvv</label>
                    <input type="text" class="form-control span12" name="cvv" id="cvv" value="026">
                </div>
                <div class="form-group">
                    <label>银行名称</label>
                    <input type="text" class="form-control span12" name="bankName" id="bankName" value="招商银行">
                </div>
                <div class="form-group">
                    <label>证件类型</label>
                    <select name="cerType" id="cerType" class="form-control">
					              <option value="01">身份证</option>
					            </select>
                </div>
                <div class="form-group">
                    <label>证件号</label>
                    <input type="text" class="form-control span12" name="cerNumber" id="cerNumber" value="362324199107253051">
                </div>
                <div class="form-group">
                    <label>随机数</label>
                    <input type="text" class="form-control span12" name="nonce" id="nonce" value="5uuLgQTBOpeIOqxvjWFLSMabKKoqP8iE">
                </div>
                <div class="form-group">
                    <label>手机号</label>
                    <input type="text" class="form-control span12" name="mobileNum" id="mobileNum" value="15217928112">
                </div>
                <div class="form-group">
                    <label>验证码</label>
                    <input type="text" class="form-control span12" name="yzm" id="yzm" value="">
                </div>
                <div class="form-group">
                    <label>平台订单号</label>
                    <input type="text" class="form-control span12" name="transaction_id" id="transaction_id" value="">
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
                <input type="button" value="获取验证码" id="getYzmBtn" >
                <input type="button" value="查询交易" id="queryBtn" >
                <input type="submit" value="确认支付" class="btn btn-primary pull-right" >
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

    function getYzmFunc(){

		setTimeout(function(){
			$.ajax({
	            cache: true,
	            type: "POST",
	            timeout: 10000,
	            url:"quickpay/prePay.do?r=" + new Date().getTime(),
	            data:$('#loginForm').serialize(),// 你的formid
	            async: false,
	            error: function(request) {
					
	            },
	            success: function(data) {
	            	alert(JSON.stringify(data));
	            }
	        });
		},300);
		
	}

    function queryTrade(){

		setTimeout(function(){
			$.ajax({
	            cache: true,
	            type: "POST",
	            timeout: 10000,
	            url:"quickpay/queryPay.do?r=" + new Date().getTime(),
	            data:$('#loginForm').serialize(),// 你的formid
	            async: false,
	            error: function(request) {
					
	            },
	            success: function(data) {
	            	alert(JSON.stringify(data));
	            }
	        });
		},300);
		
	}

    $("#getYzmBtn").click(function(){
		//$("#loading", window.parent.document).show();
		//setTimeout('',300);
		getYzmFunc();
		
	});
    $("#queryBtn").click(function(){
		//$("#loading", window.parent.document).show();
		//setTimeout('',300);
		queryTrade();
		
	});
    
    
});

//$("[rel=tooltip]").tooltip();

</script>

</body></html>
