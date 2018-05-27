<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ include file="include.jsp" %>

<html lang="en">
<head>
    <title>商户自助平台</title>
</head>
<body class=" theme-blue" id="bodyId">


<script type="text/javascript">

</script>

<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->


<!--[if lt IE 7 ]>
<body class="ie ie6"> <![endif]-->
<!--[if IE 7 ]>
<body class="ie ie7 "> <![endif]-->
<!--[if IE 8 ]>
<body class="ie ie8 "> <![endif]-->
<!--[if IE 9 ]>
<body class="ie ie9 "> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!-->

<!--<![endif]-->

<div class="navbar navbar-default" role="navigation">
    <div class="navbar-header">
        <a class="" href="index.html"><span class="navbar-brand"><span
                class="fa"></span>  商户自助平台</span></a></div>

    <div class="navbar-collapse collapse" style="height: 1px;">

    </div>
</div>


<div id="loading" class="loading" style="">Loading pages...</div>
<div class="dialog">
    <div class="panel panel-default">
        <p class="panel-heading no-collapse">登录</p>
        <div class="panel-body">
            <form action="/tuser/login" method="post" id="mchtLoginForm">
                <div class="form-group">
                    <label>商户号</label>
                    <input type="text" class="form-control span12" name="mchtNo" id="mchtNo">
                </div>
                <div class="form-group">
                    <label>密码</label>
                    <input type="password" class="form-control span12 form-control"
                           name="password_input" id="password_input">
                    <input type="hidden" class="form-control span12 form-control"
                           name="mcht_password" id="mcht_password">
                </div>
                <a href="javascript:void(0);" class="btn btn-primary pull-right"
                   id="login_btn">登录</a>
                <%--<label class="remember-me"><input type="checkbox"> 记住密码</label>
                --%>
                <div class="clearfix"></div>
            </form>
        </div>
    </div>
    <%--<p class="pull-right" style=""><a href="http://www.portnine.com" target="blank" style="font-size: .75em; margin-top: .25em;">Design by Portnine</a></p>
    --%><p><a href="javascript:void(0)" id="forgetPwd">忘记密码?</a></p>
</div>
<script type="text/javascript">
    //初始化toastr.js
    toastr.options = {positionClass: 'toast-top-center', timeOut: "2000"};

    $(function () {

        var match = document.cookie.match(new RegExp('color=([^;]+)'));
        if (match) var color = match[1];
        if (color) {
            $('body').removeClass(function (index, css) {
                return (css.match(/\btheme-\S+/g) || []).join(' ')
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
    $(function () {
        $('#mchtNo').focus();

        $('#login_btn').click(function () {
            doLogin();
            //alert("警告消息框被关闭。");
        });
        $('#forgetPwd').click(function () {
            toastr.error("联系管理员");
            return;
        });

        function doLogin() {
            if ($('#mchtNo').val() == "") {
                toastr.error("商户号不能为空");
                $('#mchtNo').focus();
                return;
            }

            if ($('#password_input').val() == "") {
                toastr.error("密码不能为空");
                $('#password_input').focus();
                return;
            }
            var secretPw = hex_md5($('#password_input').val());
            $('#mcht_password').val(secretPw);
            $("#loading").show();
            $.ajax({
                url: "tuser/login",//提交地址
                data: $("#mchtLoginForm").serialize(),//将表单数据序列化
                type: "POST",
                dataType: "text",
                success: function (returneddata) {
                    $("#loading").hide();
                    var data = $.parseJSON(returneddata);
                    if ('00' == data.code) {
                        toastr.success("登录成功");
                        $("#loading").hide();
                        window.location.href = "<%= request.getContextPath()%>" + '/tuser/loginRedirect';
                    }
                    else {
                        toastr.error(data.message);
                    }
                },
                error: function (returneddata) {
                    $("#loading").hide();
                    alert("error");
                }
            });
        }

        $("#bodyId").keydown(function (e) {
            if (e.keyCode == 13) {
                $('#login_btn').click();
            }
        });
    });

</script>

</body>
</html>
