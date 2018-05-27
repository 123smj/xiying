<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@page import="com.manage.bean.PageModle" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page import="com.manage.bean.OprInfo,com.trade.bean.*,com.gy.util.*" %>
<%@ include file="include.jsp" %>

<html lang="en">
<head>
    <title>线上交易管理平台</title>
    <style type="text/css">
        .form-yzm {
            width: auto;
            height: 34px;
            padding: 6px 12px;
            font-size: 14px;
            line-height: 1.42857143;
            color: #555;
            background-color: #fff;
            background-image: none;
            border: 1px solid #ccc;
            border-radius: 4px;
            -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075);
            box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075);
            -webkit-transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
            transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
        }
    </style>
    <script type="text/javascript">
        function IFrameResize() {
            //alert(this.document.body.scrollHeight); //弹出当前页面的高度
            var obj = parent.document.getElementById("main_iframe"); //取得父页面IFrame对象
            //alert(obj.height); //弹出父页面中IFrame中设置的高度
            //obj.height = this.document.body.scrollHeight; //调整父页面中IFrame的高度为此页面的高度
        }

        //初始化toastr.js
        toastr.options = {positionClass: 'toast-top-center', timeOut: "2000"};
    </script>
</head>
<body onload="IFrameResize()" class=" theme-blue">
<div>
    <div class="header">
        <h1 class="page-title">
            代扣
        </h1>
    </div>

    <div class="row">
        <div class="col-md-8">
            <div id="myTabContent" class="tab-content">
                <div class="tab-pane active in" id="home">

                    <form id="singlePayForm" name="singlePayForm" class="form-horizontal">
                        <div class="form-group">
                            <label for="card_name" class="col-sm-2 control-label">
                                姓名
                            </label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" id="card_name"
                                       name="card_name" placeholder="请输入姓名" value="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="cer_number" class="col-sm-2 control-label">
                                身份证号
                            </label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" id="cer_number"
                                       name="cer_number" placeholder="请输入身份证号" value="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="bank_card_no" class="col-sm-2 control-label">
                                卡号
                            </label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" id="bank_card_no"
                                       name="bank_card_no" placeholder="请输入卡号" value="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="mobile_number" class="col-sm-2 control-label">
                                手机号
                            </label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" id="mobile_number"
                                       name="mobile_number" placeholder="请输入手机号" value="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="dfAmount" class="col-sm-2 control-label">
                                代扣金额/元
                            </label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" id="dfAmount"
                                       onkeyup="value=value.replace(/[^\d.]/g,'')"
                                       name="dfAmount" placeholder="请输入代扣金额" value="">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="yzm" class="col-sm-2 control-label">
                                子协议
                            </label>
                            <div class="col-sm-6">
                                <input type="text" class="form-yzm" id="yzm"
                                       name="yzm" readonly value="">
                                <a class="btn btn-primary" id="yzmButton" name="yzmButton">获取子协议</a>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="orderNo" class="col-sm-2 control-label">
                                订单号
                            </label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" id="orderNo"
                                       name="orderNo" readonly value="">
                            </div>
                        </div>
                        <input type="hidden" id="transaction_id" name="transaction_id" value="">
                    </form>
                </div>
            </div>

        </div>
    </div>
</div>

<div class="btn-toolbar list-toolbar">
    <button class="btn btn-primary" id="singlePayBtn"><i class="fa fa-save"></i> 提交</button>
    <a data-toggle="modal" class="btn btn-danger" id="resetBtn">重置</a>

    <!-- href="#myModal" -->
</div>

<footer>
    <hr>
    <!-- Purchase a site license to remove this link from the footer: http://www.portnine.com/bootstrap-themes -->
    <p>温馨提示：</p>
    <p>1.请勿在不确定代扣手机号的前提下尝试多次获取子协议，超过两次获取失败该卡当天不能做代扣</p>
    <p>2.每做一笔代扣，都需要重新点击获取子协议按钮，生成不同都订单号及向后端登记交易，订单号出来后再点击提交</p>
    <p>© 2017 guoyinpay</p>
    <br>
</footer>

<div class="modal small fade" id="myModal" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×
                </button>
                <h3 id="myModalLabel">Delete Confirmation</h3>
            </div>
            <div class="modal-body">

                <p class="error-text"><i class="fa fa-warning modal-icon"></i>确认保存?</p>
            </div>
            <div class="modal-footer">
                <button class="btn btn-default" data-dismiss="modal" aria-hidden="true">取消</button>
                <button class="btn btn-danger" data-dismiss="modal">确定</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">

    function getYzm() {
        if (!checkDf('0')) {
            return;
        }
        $("#loading", window.parent.document).show();
        setTimeout(function () {
            $.ajax({
                cache: true,
                type: "POST",
                timeout: 30000,
                url: "tuser/mchtXieyi?r=" + new Date().getTime(),
                dataType: "text",
                data: $('#singlePayForm').serialize(),
                async: false,
                error: function (request) {
                    //$("#loading", window.parent.document).hide();
                    alert("Connection error");
                },
                success: function (returneddata) {
                    $("#loading", window.parent.document).hide();
                    var data = returneddata.split('--');
                    if ('00' == data[0]) {
                        //子协议和平台订单号
                        $("#orderNo").val(data[4]);
                        $("#transaction_id").val(data[3]);
                        $("#yzm").val(data[2]);
                        toastr.success("代扣协议签订成功");
                    }
                    else {
                        toastr.error(data[1]);
                    }
                }
            });
        }, 300);
    }

    function daiKou() {
        $("#loading", window.parent.document).show();
        setTimeout(function () {
            $.ajax({
                cache: true,
                type: "POST",
                timeout: 30000,
                url: "tuser/mchtDaikou?r=" + new Date().getTime(),
                dataType: "text",
                data: $('#singlePayForm').serialize(),
                async: false,
                error: function (request) {
                    //$("#loading", window.parent.document).hide();
                    alert("Connection error");
                },
                success: function (returneddata) {
                    $("#loading", window.parent.document).hide();
                    var data = returneddata.split('--');
                    if ('00' == data[0]) {
                        toastr.success(data[1]);
                        //$('#singlePayForm')[0].reset();
                    }
                    else {
                        toastr.error(data[1]);
                    }
                    $("#orderNo").val("");
                    $("#yzm").val("");
                }
            });
        }, 300);
    }


    $("#resetBtn").click(function () {
        $('#singlePayForm')[0].reset();
    });
    //代付
    $("#singlePayBtn").click(function () {

        if (!checkDf('1')) {
            return;
        }
        window.parent.wxc.xcConfirm("确认代扣金额：" + $('#dfAmount').val() + "元?", window.parent.wxc.xcConfirm.typeEnum.confirm, {
            onOk: function (v) {
                daiKou();
            }
        });
    });

    //获取验证码
    $("#yzmButton").click(function () {
        getYzm();
    });

    function checkDf(checkOrderNo) {
        if ($('#cer_number').val() == "") {
            toastr.error("请输入身份证号");
            $('#cer_number').focus();
            return false;
        }
        if ($('#mobile_number').val() == "") {
            toastr.error("请输入手机号");
            $('#mobile_number').focus();
            return false;
        }
        if ($('#bank_card_no').val() == "") {
            toastr.error("请输入卡号");
            $('#bank_card_no').focus();
            return false;
        }
        if ($('#card_name').val() == "") {
            toastr.error("请输入姓名");
            $('#card_name').focus();
            return false;
        }
        if (checkOrderNo == '1' && $('#orderNo').val() == "") {
            toastr.error("请先获取子协议");
            $('#orderNo').focus();
            return false;
        }
        if ($('#dfAmount').val() == "") {
            toastr.error("请输入代扣金额");
            $('#dfAmount').focus();
            return false;
        }
        var limit = 5;
        if ($('#dfAmount').val() * 1 < limit) {
            toastr.error("请至少代扣" + limit + "元");
            $('#dfAmount').focus();
            return false;
        }
        return true;
    }


</script>
</body>
</html>