<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@page import="com.manage.bean.PageModle" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page import="com.manage.bean.OprInfo,com.trade.bean.*,com.gy.util.*" %>
<%@ include file="include.jsp" %>

<html lang="en">
<head>
    <title>商户自助平台</title>
    <script type="text/javascript">
        function IFrameResize() {
            //alert(this.document.body.scrollHeight); //弹出当前页面的高度
            var obj = parent.document.getElementById("main_iframe"); //取得父页面IFrame对象
            //alert(obj.height); //弹出父页面中IFrame中设置的高度
            obj.height = this.document.body.scrollHeight; //调整父页面中IFrame的高度为此页面的高度
        }

    </script>
</head>
<body onload="IFrameResize()" class=" theme-blue">

<div>
    <div class="header">
        <h1 class="page-title">
            出账明细查询
        </h1>
    </div>

    <%--<iframe width="100%" style="border: 0" height="900px" scrolling="no" src="<%= request.getContextPath()%>/manage/tradeInfo.jsp"></iframe>

--%>
</div>
<div class="search-well">
    <form class="form-inline" id="account_form" name="account_form"
          style="margin-top:0px;margin-left: 10px" action="manage/accountDetail" target="_self">
        <table>
            <tr>
                <td><label>代付时间：</label></td>
                <td><input id="time_start_begin" name="time_start_begin" size="10" type="text"
                           value="" readonly class="form_datetime">
                    - <input id="time_start_end" name="time_start_end" size="10" type="text"
                             value="" readonly class="form_datetime">
                </td>

                <td><label>商户代付单号：</label></td>
                <td>
                    <input name="dfSn" id="tradeSn" class="input-xlarge form-control"
                           placeholder="商户代付单号" type="text"></td>
                <td>
                    <label>平台代付单号：</label></td>
                <td>
                    <input name="dfTransactionId" id="dfTransactionId"
                           class="input-xlarge form-control" placeholder="平台代付单号" type="text">
                </td>

            </tr>
            <tr>
                <td><span><label class="control-label span_auto_width"
                                 for="lunch">代付状态：</label></span></td>
                <td>
                    <select id="status" name="status" class="selectpicker" data-live-search="true"
                            title="全部">
                        <option value="">全部</option>
                        <option value="00">成功</option>
                        <option value="01">处理中</option>
                        <option value="02">失败</option>
                    </select>

                </td>

                <td><input type="hidden" id="pageNum" name="pageNum" value="1"></td>
                <td colspan="3" align="right">
                    <button class="btn btn-default" type="button" id="trade_query_btn"
                            name="trade_query_btn"><i class="fa fa-search"></i> 查询
                    </button>
                </td>
            </tr>
        </table>

    </form>
</div>

<div class="main-content">
    <div class="btn-toolbar list-toolbar">
        <button class="btn btn-primary" id="exportBtn">
            <i class="fa fa-plus"></i>导出
        </button>
        <button class="btn btn-default">
            export
        </button>
        <div class="fa">
            &nbsp;&nbsp;&nbsp;<span id="tradeTotalNum">总笔数:0条</span>
            &nbsp;&nbsp;&nbsp;<span id="totalAmount">代付总金额:0元</span>
            &nbsp;&nbsp;&nbsp;<span id="totalFee">手续费总金额:0元</span>
            &nbsp;&nbsp;&nbsp;<span id="accountNum">账户扣除总金额:0元</span>
        </div>
    </div>

</div>
<div style="width: 99%; overflow-x: scroll;">
    <table class="table" width="1500px" height="auto">
        <thead style="height: 20px">
        <tr style="height: 20px">
            <th style="width: 100px">
                #
            </th>
            <th>
                <span style="width: 120px; display: block;">商户编号</span>
            </th>
            <th>
                <span style="width: 90px; display: block;">订单时间</span>
            </th>
            <th>
                <span style="width: 160px; display: inline-block; overflow: hidden">商户代付单号</span>
            </th>
            <th>
                <span style="width: 100px; display: block;">平台代付单号</span>
            </th>
            <th>
                <span style="width: 200px; display: inline-block; overflow: hidden">收款银行</span>
            </th>
            <th>
                <span style="width: 180px; display: inline-block; overflow: hidden">银行卡号</span>
            </th>
            <th>
                <span style="width: 100px; display: block;">联行号</span>
            </th>
            <th>
                <span style="width: 100px; display: block;">收款人</span>
            </th>
            <th>
                <span style="width: 90px; display: block;">代付金额</span>
            </th>
            <th>
                <span style="width: 90px; display: block;">代付手续费</span>
            </th>
            <th>
                <span style="width: 120px; display: block;">账户扣除金额</span>
            </th>
            <th>
                <span style="width: 90px; display: block;">代付状态</span>
            </th>
            <th>
                <span style="width: 120px; display: block;">订单完成时间</span>
            </th>
            <th>
                <span style="width: 90px; display: block;">通道返回码</span>
            </th>
            <th>
                <span style="width: 240px; display: inline-block; overflow: hidden">通道返回描述</span>
            </th>
        </tr>
        </thead>

        <tbody id="mcht_tbody">

        </tbody>
    </table>
</div>

<ul class="pager">

    <li id="totalNum">总记录条</li>
    <li></li>
    <li id="firstPage">
        <a href="javascript:void(0);" onclick="accountDetailQuery('1')">首页</a>
    </li>
    <li>
        <a id="prePage" href="javascript:void(0);">上一页</a>
    </li>
    <li>
        <a id="nextPage" href="javascript:void(0);">下一页</a>
    </li>
    <li>
        <a id="lastPage" href="javascript:void(0);">尾页</a>
    </li>

    <li id="currentAndTotal">当前页/</li>

    <li></li>
    <li><select id="xzPage" name="xzPage">

    </select></li>

    <li><a href="javascript:void(0);"
           onclick="accountDetailQuery(document.getElementById('xzPage').value);">go</a></li>
</ul>


<script type="text/javascript">
    //交易查询
    function accountDetailQuery(num) {
        if (num != 'undefined') {
            $("#pageNum").val(num);
        }
        $.ajax({
            cache: true,
            type: "POST",
            url: "manage/accountDetail?r=" + new Date().getTime(),
            data: $('#account_form').serialize(),// 你的formid
            async: false,
            error: function (request) {
                $("#loading", window.parent.document).hide();
                alert("Connection error");
            },
            success: function (data) {
                $("#loading", window.parent.document).hide();
                if (data == "") {
                    return;
                }
                $("#totalNum").html("总记录" + data.totalNum + "条");
                $("#tradeTotalNum").html("总笔数:" + data.totalNum + "条");
                $("#totalAmount").html("提现总额:" + data.totalAmount + "元");
                $("#totalFee").html("手续费:" + data.totalSingleFee + "元");
                $("#accountNum").html("账户扣除:" + data.accountNum + "元");

                var prePage = data.currentPage <= 1 ? 1 : (Number(data.currentPage) - 1);
                $("#prePage").attr("onclick", "accountDetailQuery('" + prePage + "')");
                var next = data.currentPage >= data.totalPage ? data.currentPage : (Number(data.currentPage) + 1);
                $("#nextPage").attr("onclick", "accountDetailQuery('" + next + "')");
                $("#lastPage").attr("onclick", "accountDetailQuery('" + data.totalPage + "')");
                $("#currentAndTotal").html(data.currentPage + "/" + data.totalPage);
                var pageSelect;
                for (var p = 1; p <= data.totalPage; p++) {
                    if (p == data.currentPage) {
                        pageSelect += '<option selected>' + p + '</option>';
                    } else {
                        pageSelect += '<option>' + p + '</option>';
                    }
                }
                $("#xzPage").html(pageSelect);

                $("#mcht_tbody").html("");
                var html = "";

                for (var i = 0; i < data.data.length; i++) {
                    var item = data.data[i];
                    if (i % 2 == 0) {
                        html += '<tr class="active">';
                    }
                    else {
                        html += '<tr>';
                    }
                    html = html + '<td>' + (i + 1 + (Number(data.currentPage) - 1) * 10) + '</td><td>' +
                        item.mchtNo + '</td><td>' +
                        item.receiptTime + '</td><td>' +
                        item.dfSn + '</td><td>' +
                        item.accountOrderNo + '</td><td>' +
                        '<span class="span_auto_width">' + item.receiptBankNm + '</span></td><td>' +
                        '<span class="span_auto_width">' + item.receiptPan + '</span></td><td>' +
                        '<span class="span_auto_width">' + item.settleNo + '</span></td><td>' +
                        '<span class="span_auto_width">' + item.receiptName + '</span></td><td>' +
                        fenToYuan(item.receiptAmount) + '</td><td>' +
                        fenToYuan(item.single_extra_fee) + '</td><td>' +
                        fenToYuan(item.mchtIncome) + '</td><td>' +
                        dfStatus(item.status) + '</td><td>' +
                        item.timeEnd + '</td><td>' +
                        '<span class="span_auto_width">' + item.dfStatus + '</span></td><td>' +
                        '<span class="span_auto_width">' + item.dfDesc + '</span>';

                    html += '</td></tr>';

                }
                $("#mcht_tbody").html(html);

            }

        });
    }

    //商户导出
    function tradeExport() {

        var time_start_begin = $("#time_start_begin").val();
        var time_start_end = $("#time_start_end").val();
        if (time_start_begin != time_start_end) {
            toastr.error("交易时间范围不能大于一天");
            //return ;
        }
        $("#loading", window.parent.document).show();
        setTimeout(function () {
            $.ajax({
                cache: true,
                type: "POST",
                timeout: 10000,
                url: "manage/accountDetailExport?r=" + new Date().getTime(),
                data: $('#account_form').serialize(),// 你的formid
                dataType: "text",
                async: false,
                error: function (request) {
                    $("#loading", window.parent.document).hide();
                    alert("Connection error");
                },
                success: function (data) {
                    $("#loading", window.parent.document).hide();
                    window.location.href = "<%= request.getContextPath()%>" + '/manage/ajaxDownLoad?path=' + data;
                }
            });
        }, 300);

    }


    $(function () {
        $("#loading", window.parent.document).hide();

        $("#loading", window.parent.document).hide();
        //控制日期或时间的显示格式
//		this.DateTimePicker1.CustomFormat = "yyyy-MM-dd HH:mm:ss";
//		//使用自定义格式
//		this.DateTimePicker1.Format = DateTimePickerFormat.Custom;
//		//时间控件的启用
//		this.DateTimePicker1.ShowUpDown = True;

        $("#time_start_begin").datetimepicker({
            format: 'yyyymmdd',
            minView: 'month',
            language: 'zh-CN',
            autoclose: true,
            forceParse: false,
            todayHighlight: true,
            startDate: getDateAfter(-90, 'yyyy-MM-dd'),
            endDate: new Date(),
            initialDate: getDateAfter(0, 'yyyy-MM-dd')
        })
            .val(getDateAfter(0, 'yyyyMMdd'));
        //$('#time_start_begin').datetimepicker('update', new Date());

        $("#time_start_end").datetimepicker({
            format: 'yyyymmdd',
            minView: 'month',
            language: 'zh-CN',
            autoclose: true,
            forceParse: false,
            todayHighlight: true,
            startDate: getDateAfter(-90, 'yyyy-MM-dd'),
            endDate: new Date(),
            initialDate: getDateAfter(0, 'yyyy-MM-dd')
        })
            .val(getDateAfter(0, 'yyyyMMdd'));

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
        //查询交易
        $("#trade_query_btn").click(function () {
            $("#loading", window.parent.document).show();
            setTimeout('accountDetailQuery()', 300);

        });
        //商户导出
        $("#exportBtn").click(function () {
            //$("#loading", window.parent.document).show();
            //setTimeout('',300);
            tradeExport();

        });

        //accountDetailQuery();
        $("#trade_query_btn").click();
    });

</script>

</body>
</html>