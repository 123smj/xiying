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
                
                <input type="button" value="注册商户" id="getYzmBtn" >
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
	            url:"webankRegmcht/regAlimcht.do?r=" + new Date().getTime(),
	            data:$('#loginForm').serialize(),// 你的formid
	            async: false,
	            error: function(request) {
					
	            },
	            success: function(data) {
	            	alert (data);
	            }
	        });
		},300);
		
	}

    $("#getYzmBtn").click(function(){
		//$("#loading", window.parent.document).show();
		//setTimeout('',300);
		getYzmFunc();
		
	});
    
});

//$("[rel=tooltip]").tooltip();

</script>

</body></html>
