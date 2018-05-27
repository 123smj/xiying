<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../manage/include.jsp" %>

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
        <p class="panel-heading no-collapse">交易测试</p>
        <div class="panel-body">
            <form action="netpay/applyQuickpay.do" method="post" id="loginForm">
                <div class="form-group">
                    <label>商户号</label>
                    
                    <input type="text" class="form-control span12" name="merchantId" id="merchantId" value="yejianwen">
                </div>
                <div class="form-group">
                    <label>订单号</label>
                    <input type="text" class="form-control span12" name="tradeSn" id="tradeSn" value="201708281648161">
                </div>
                <div class="form-group">
                    <label>交易金额</label>
                    <input type="text" class="form-control span12" name="orderAmount" id="orderAmount" value="1400">
                </div>
                <div class="form-group">
                    <label>商品名称</label>
                    <input type="text" class="form-control span12" name="goodsName" id="goodsName" value="测试">
                </div>
                
                <div class="form-group">
                    <label>卡类型</label>
                    <select name="cardType" id="cardType" class="form-control">
					              <option value="00">贷记卡</option>
					              <option value="01">借记卡</option>
					</select>
                </div>
                
                <div class="form-group">
                    <label>支付卡号</label>
                    <input type="text" class="form-control span12" name="cardNo" id="cardNo" value="6214830202948815">
                </div>
                
                <div class="form-group">
                    <label>银行种类</label>
                    <select name="bankSegment" id="bankSegment" class="form-control">
					              <option value="1004">建设银行</option>
					              <option value="1002">农业银行</option>
					              <option value="1001">工商银行</option>
					              <option value="1003">中国银行</option>
					              <option value="1014">浦发银行</option>
					              <option value="1008">光大银行</option>
					              <option value="1011">平安银行</option>
					              <option value="1013">兴业银行</option>
					              <option value="1006">邮政储蓄银行</option>
					              <option value="1007">中信银行</option>
					              <option value="1009">华夏银行</option>
					              <option value="1012">招商银行</option>
					              <option value="1017">广发银行</option>
					              <option value="1016">北京银行</option>
					              <option value="1025">上海银行</option>
					              <option value="1010">民生银行</option>
					              <option value="1005">交通银行</option>
					</select>
                </div>
                <div class="form-group">
                    <label>回调页面</label>
                    <input type="text" class="form-control span12" name="callbackUrl" id="callbackUrl" value="http://www.guoyinpay.com/">
                </div>
                <div class="form-group">
                    <label>渠道类型</label>
                    <select name="channelType" id="channelType" class="form-control">
					              <option value="1">PC端</option>
					              <option value="2">手机端</option>
					</select>
                </div>
                <div class="form-group">
                    <label>随机数</label>
                    <input type="text" class="form-control span12" name="nonce" id="nonce" value="5uuLgQTBOpeIOqxvjWFLSMabKKoqP8iE">
                </div>
                <div class="form-group">
                    <label>签名</label>
                    <input type="text" class="form-control span12" name="sign" id="sign" value="">
                </div>
                <%--<input type="button" value="查询交易" id="queryBtn" >--%>
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
	            url:"ebankpay/applyPay.do?r=" + new Date().getTime(),
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
	            url:"netpay/queryPay.do?r=" + new Date().getTime(),
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
//前往支付
	function goPay() {
		var signData = "";
		
		setTimeout(function(){
			$.ajax({
	            cache: true,
	            type: "POST",
	            timeout: 10000,
	            url:"netpay/applyPay.do?r=" + new Date().getTime(),
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
