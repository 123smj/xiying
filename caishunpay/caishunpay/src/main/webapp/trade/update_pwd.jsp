<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../manage/include.jsp" %>

<html lang="en"><head>
    <title>彩顺网络科技平台</title>
</head>
<body class=" theme-blue" id="bodyId">


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

    <div class="navbar navbar-default" role="navigation">
        <div class="navbar-header">
          <a class="" href="index.html"><span class="navbar-brand"><span class="fa"></span>  彩顺网络科技平台</span></a></div>

        <div class="navbar-collapse collapse" style="height: 1px;">

        </div>
      </div>
    

<div id="loading" class="loading" style="">Loading pages...</div>  
        <div class="dialog">
    <div class="panel panel-default">
        <p class="panel-heading no-collapse">密码修改</p>
        <div class="panel-body">
            <form  action="/manage/login" method="post" id="loginForm">
                <div class="form-group">
                    <label>用户名</label>
                    <input type="text" class="form-control span12" name="opr_id" id="opr_id" value="<%=oprInfo.getOpr_id() %>">
                </div>
                <div class="form-group">
                <label>原密码</label>
                    <input type="password" class="form-control span12 form-control" name="opr_pwd_input" id="opr_pwd_input">
                    <input type="hidden" class="form-control span12 form-control" name="opr_pwd" id="opr_pwd">
                </div>
                <div class="form-group">
                <label>新密码</label>
                    <input type="password" class="form-control span12 form-control" name="new_opr_pwd_input" id="new_opr_pwd_input">
                    <input type="hidden" class="form-control span12 form-control" name="new_opr_pwd" id="new_opr_pwd">
                </div>
                <div class="form-group">
                <label>重复新密码</label>
                    <input type="password" class="form-controlspan12 form-control" name="repeat_new_opr_pwd_input" id="repeat_new_opr_pwd_input">
                    <input type="hidden" class="form-controlspan12 form-control" name="repeat_new_opr_pwd" id="repeat_new_opr_pwd">
                </div>
				<a href="javascript:void(0);" class="btn btn-primary pull-right" id="login_btn" >提交</a>
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
    
});

//$("[rel=tooltip]").tooltip();
$(function() {
	$('#opr_id').focus();

    $('#login_btn').click(function(){
    	doLogin();
    	//alert("警告消息框被关闭。");
    });

    function doLogin() {
        if($('#opr_id').val() == "") {
        	toastr.error("用户名不能为空");
        	$('#opr_id').focus();
        	return ;
        }

        if($('#opr_pwd_input').val() == "") {
        	toastr.error("原密码不能为空");
        	$('#opr_pwd_input').focus();
        	return ;
        }
        if($('#new_opr_pwd_input').val() == "") {
        	toastr.error("新密码不能为空");
        	$('#new_opr_pwd_input').focus();
        	return ;
        }
        if($('#repeat_new_opr_pwd_input').val() == "") {
        	toastr.error("重复新密码不能为空");
        	$('#repeat_new_opr_pwd_input').focus();
        	return ;
        }

        if($('#new_opr_pwd_input').val() !=$('#repeat_new_opr_pwd_input').val()) {
        	toastr.error("两次密码不一致");
        	$('#repeat_new_opr_pwd_input').focus();
        	return ;
        }
        var secretPw = hex_md5($('#opr_pwd_input').val());
        var newSecretPw = hex_md5($('#new_opr_pwd_input').val());
        $('#opr_pwd').val(secretPw);
        $('#new_opr_pwd').val(newSecretPw);
        $("#loading").show();
        $.ajax({
            url:"manage/updatePwd",//提交地址
            data:$("#loginForm").serialize(),//将表单数据序列化
            type:"POST",
            dataType:"text",
            success: function(returneddata){
        		$("#loading").hide();
            	var data = $.parseJSON(returneddata);
                if('00' == data.code) {
                	toastr.success("修改成功");
                	$('#opr_pwd_input').val("");
                	$('#new_opr_pwd_input').val("");
                	$('#repeat_new_opr_pwd_input').val("");
                 }
                else {
                	toastr.error(data.message);
                }
            },
        	error: function(returneddata){
            	$("#loading").hide();
				alert("error");
            }
        });
    }

});

</script>

</body></html>
