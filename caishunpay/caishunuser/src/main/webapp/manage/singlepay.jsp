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
            单笔代付
        </h1>
    </div>

    <div class="row">
        <div class="col-md-8">
            <div id="myTabContent" class="tab-content">
                <div class="tab-pane active in" id="home">

                    <form id="singlePayForm" name="singlePayForm" class="form-horizontal">
                        <div class="form-group">
                            <label for="bank_name" class="col-sm-2 control-label">
                                收款银行
                            </label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" id="bank_name"
                                       name="bank_name" placeholder="请输入收款银行"
                                       value="<%=StringUtil.trans2Str(mchtInfo !=null?mchtInfo.getBank_name():"") %>">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="bank_card_no" class="col-sm-2 control-label">
                                收款卡号
                            </label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" id="bank_card_no"
                                       name="bank_card_no" placeholder="请输入收款卡号"
                                       value="<%=StringUtil.trans2Str(mchtInfo !=null?mchtInfo.getBank_card_no():"") %>">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="card_name" class="col-sm-2 control-label">
                                收款人
                            </label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" id="card_name"
                                       name="card_name" placeholder="请输入收款人"
                                       value="<%=StringUtil.trans2Str(mchtInfo !=null?mchtInfo.getCard_name():"") %>">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="dfAmount" class="col-sm-2 control-label">
                                代付金额/元
                            </label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" id="dfAmount"
                                       onkeyup="value=value.replace(/[^\d.]/g,'')"
                                       name="dfAmount" placeholder="请输入代付金额">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="yzm" class="col-sm-2 control-label">
                                验证码
                            </label>
                            <div class="col-sm-6">
                                <input type="text" class="form-yzm" id="yzm"
                                       name="yzm" placeholder="请输入验证码">
                                <a class="btn btn-primary" id="yzmButton" name="yzmButton">获取验证码</a>
                            </div>
                        </div>
                        <input type="hidden" id="msgKey" name="msgKey" value="">
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
</div>

<footer>
    <hr>
    <!-- Purchase a site license to remove this link from the footer: http://www.portnine.com/bootstrap-themes -->
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
    //公司新增

    $(function () {
        $("#loading", window.parent.document).hide();

        function doCompanyAdd() {

            $("#loading", window.parent.document).show();

            setTimeout(function () {
                $.ajax({
                    cache: true,
                    type: "POST",
                    timeout: 30000,
                    url: "tuser/mchtSinglePay?r=" + new Date().getTime(),
                    dataType: "text",
                    data: $('#singlePayForm').serialize(),// 你的formid
                    async: false,
                    error: function (request) {
                        //$("#loading", window.parent.document).hide();
                        alert("Connection error");
                    },
                    success: function (data) {
                        $("#loading", window.parent.document).hide();
                        if (data == "00000") {
                            toastr.success("代付成功!");
                            $('#singlePayForm')[0].reset();
                        }
                        else {
                            toastr.error(data);
                        }
                    }
                });
            }, 300);
        }

        var yzmTime = 60;

        function getYzm() {
            $("#loading", window.parent.document).show();
            setTimeout(function () {
                $.ajax({
                    cache: true,
                    type: "POST",
                    timeout: 30000,
                    url: "yzm/sendYzm?r=" + new Date().getTime() + "&bank_card_no=" + $('#bank_card_no').val() + "&dfAmount=" + $('#dfAmount').val(),
                    dataType: "text",
                    async: false,
                    error: function (request) {
                        //$("#loading", window.parent.document).hide();
                        alert("Connection error");
                    },
                    success: function (returneddata) {
                        $("#loading", window.parent.document).hide();

                        var data = $.parseJSON(returneddata);
                        if ('00' == data.code) {
                            $("#msgKey").val(data.data);
                            decreaseTimer();
                            $('body').everyTime('1s', 'sendMsgTimer', function () {
                                if (yzmTime > 0) {
                                    decreaseTimer();
                                } else {
                                    rebackTimer();
                                }
                            });
                            toastr.success("验证码发送成功");
                        }
                        else {
                            toastr.error(data.message);
                        }

                    }
                });
            }, 300);
        }

        function decreaseTimer() {
            $('#yzmButton').text(yzmTime + " 秒");
            $('#yzmButton').attr("disabled", true);
            yzmTime -= 1;
        }

        function rebackTimer() {
            $('#yzmButton').text("获取验证码");
            $('#yzmButton').attr("disabled", false);
            yzmTime = 60;
            $('body').stopTime("sendMsgTimer");
        }

        SelectOptionsDWR.getComboData('MCHT_INFO', function (ret) {

            $("#mchtNo").html("");
            var mchtInfos = jQuery.parseJSON(ret).data;
            var mchtHtml = "<option value=''>全部</option>";
            mchtInfos.forEach(function (item, index) {
                mchtHtml += "<option value='" + item.valueField + "'>" + item.displayField + "</option>"
            });
            $("#mchtNo").html(mchtHtml);
            $(".selectpicker").selectpicker('refresh');

        });

        $("#resetBtn").click(function () {
            $('#singlePayForm')[0].reset();
        });
        //代付
        $("#singlePayBtn").click(function () {

            if (!checkDf()) {
                return;
            }
            window.parent.wxc.xcConfirm("确认代付金额：" + $('#dfAmount').val() + "元?", window.parent.wxc.xcConfirm.typeEnum.confirm, {
                onOk: function (v) {
                    doCompanyAdd();
                }
            });
        });

        //获取验证码
        $("#yzmButton").click(function () {

            if (!checkSendyzm()) {
                return;
            }
            getYzm();
        });

        function checkDf() {
            if ($('#bank_name').val() == "") {
                toastr.error("请输入收款银行");
                $('#bank_name').focus();
                return false;
            }
            if ($('#bank_card_no').val() == "") {
                toastr.error("请输入收款卡号");
                $('#bank_card_no').focus();
                return false;
            }

            if ($('#card_name').val() == "") {
                toastr.error("请输入收款人");
                $('#card_name').focus();
                return false;
            }
            if ($('#dfAmount').val() == "") {
                toastr.error("请输入代付金额");
                $('#dfAmount').focus();
                return false;
            }
            if ($('#yzm').val() == "") {
                toastr.error("请输入验证码");
                $('#yzm').focus();
                return false;
            }
            return true;
        }

        function checkSendyzm() {
            if ($('#bank_name').val() == "") {
                toastr.error("请输入收款银行");
                $('#bank_name').focus();
                return false;
            }
            if ($('#bank_card_no').val() == "") {
                toastr.error("请输入收款卡号");
                $('#bank_card_no').focus();
                return false;
            }

            if ($('#card_name').val() == "") {
                toastr.error("请输入收款人");
                $('#card_name').focus();
                return false;
            }
            if ($('#dfAmount').val() == "") {
                toastr.error("请输入代付金额");
                $('#dfAmount').focus();
                return false;
            }

            return true;
        }

    });

</script>
</body>
</html>