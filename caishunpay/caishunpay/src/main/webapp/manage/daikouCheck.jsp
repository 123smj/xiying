<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.manage.bean.PageModle"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page import="com.manage.bean.OprInfo,com.trade.bean.*,com.gy.util.*"%>
<%@ include file="include.jsp" %>

<html lang="en"><head>
    <title>彩顺网络科技平台</title>
    <script type="text/javascript">
    function IFrameResize(){ 
    	//alert(this.document.body.scrollHeight); //弹出当前页面的高度 
    	var obj = parent.document.getElementById("main_iframe"); //取得父页面IFrame对象 
    	//alert(obj.height); //弹出父页面中IFrame中设置的高度 
    	obj.height = this.document.body.scrollHeight; //调整父页面中IFrame的高度为此页面的高度 
    	} 
	
    </script>
</head>
<body  onload="IFrameResize()" class=" theme-blue" >

<div>
			<div class="header">
				<h1 class="page-title">
					代扣审核
				</h1>
			</div>
			
			<%--<iframe width="100%" style="border: 0" height="900px" scrolling="no" src="<%= request.getContextPath()%>/manage/tradeInfo.jsp"></iframe>
			
		--%>
		</div>
		<div class="search-well">
                <form class="form-inline" id="trade_form" name="trade_form" style="margin-top:0px;margin-left: 10px" action="manage/tradeQuery" target="_self">
                	<input type="hidden" id="trade_state" name="trade_state" value="CHECK">
                	<input type="hidden" id="trade_source" name="trade_source" value="K">
                	
                	<table >
                	<tr>
                		<td><label>接入订单号：</label></td><td>
                    		<input name="tradeSn" id="tradeSn" class="input-xlarge form-control" placeholder="接入订单号" type="text" ></td>
                    	<td>
            				<label>平台订单号：</label></td><td>
                    		<input name="out_trade_no" id="out_trade_no" class="input-xlarge form-control" placeholder="平台订单号"  type="text">	
          				</td>
          				<td>
            				<label>上游订单号：</label></td><td>
                    		<input name="transaction_id" id="transaction_id" class="input-xlarge form-control" placeholder="平台订单号"  type="text">
          				</td>
          			</tr>
                		<tr>
                		<td><label>交易时间：</label></td>
                			<td><input id="time_start_begin" name="time_start_begin" size="10" type="text" value="" readonly class="form_datetime">
                			- <input id="time_start_end" name="time_start_end" size="10" type="text" value="" readonly class="form_datetime">
                			</td>
                		<td><span><label class="control-label span_auto_width" for="lunch">商户号：</label></span></td><td>
					        <select id="merchantId" name="merchantId" class="selectpicker" data-live-search="true" title="全部">
						        
						     </select>
                    	</td>
                    	<td><input type="hidden" id="pageNum" name="pageNum" value="1"></td><td colspan="3" align="right">
                    		<button class="btn btn-default" type="button" id="trade_query_btn" name="trade_query_btn"><i class="fa fa-search"></i> 查询</button>
                    	</td>
                    	</tr>
                	</table>
                	
                </form>
            </div>
            
			<div class="main-content">
				<div class="btn-toolbar list-toolbar">
					<div class="fa">
						&nbsp;&nbsp;&nbsp;<span id="tradeTotalNum">总笔数:0条</span>&nbsp;&nbsp;&nbsp;<span id="totalAmount">总金额:0元</span>
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
									<span style="width: 110px; display: block;">操作</span>
								</th>
								<th>
									<span style="width: 110px; display: block;">卡号</span>
								</th>
								<th>
									<span style="width: 110px; display: block;">持卡人</span>
								</th>
								<th>
									<span style="width: 110px; display: block;">身份证</span>
								</th>
								<th>
									<span style="width: 110px; display: block;">手机号</span>
								</th>
								<th>
									<span style="width: 110px; display: block;">代扣金额(元)</span>
								</th>
								<th>
									<span style="width: 80px; display: block;">申请时间</span>
								</th>
								<th>
									<span style="width: 80px; display: block;">商户编号</span>
								</th>
								<th>
									<span style="width: 80px; display: block;">商户名称</span>
								</th>
								<th>
									<span style="width: 100px; display: block;">渠道商户号</span>
								</th>
								<th>
									<span style="width: 120px; display: block;">接入方订单号</span>
								</th>
								<th>
									<span style="width: 120px; display: block;">平台订单号</span>
								</th>
								<th>
									<span style="width: 110px; display: block;">费率(%)</span>
								</th>
								<th>
									<span style="width: 110px; display: block;">手续费(元)</span>
								</th>
								<th>
									<span style="width: 110px; display: block;">清算金额(元)</span>
								</th>
								<th>
									<span style="width: 110px; display: block;">接口类型</span>
								</th>
								<th>
									<span style="width: 90px; display: block;">应答码</span>
								</th>
								<th>
									<span
										style="width: 120px; display: inline-block; overflow: hidden">应答码描述</span>
								</th>
							</tr>
						</thead>

						<tbody id="trade_tbody">
							
						</tbody>
					</table>
				</div>
				
				<ul class="pager">  
          
        <li id="totalNum">总记录条</li>  
        <li>  </li>  
            <li id="firstPage">  
                <a href="javascript:void(0);" onclick="tradeQuery('1')">首页</a>  
            </li>  
            <li >  
                <a id="prePage" href="javascript:void(0);" >上一页</a>  
            </li>  
            <li >  
                <a id="nextPage" href="javascript:void(0);" >下一页</a>  
            </li>  
            <li >  
                <a id="lastPage" href="javascript:void(0);" >尾页</a>  
            </li>  
              
            <li id="currentAndTotal">当前页/</li>  
              
            <li></li>  
            <li><select id="xzPage" name="xzPage" >  
             
            </select></li>  
              
            <li><a href="javascript:void(0);" onclick="tradeQuery(document.getElementById('xzPage').value);">go</a></li>  
        </ul>

				
				
	<script type="text/javascript">
	//交易查询
	function tradeQuery(num){
		if(num != 'undefined'){
			$("#pageNum").val(num);
		}
		$.ajax({
            cache: true,
            type: "POST",
            url:"manage/quickPayQuery?r=" + new Date().getTime(),
            data:$('#trade_form').serialize(),// 你的formid
            async: false,
            error: function(request) {
				$("#loading", window.parent.document).hide();
                alert("Connection error");
            },
            success: function(data) {
            	$("#loading", window.parent.document).hide();
            	if(data==""){
                	return;
                }
	            $("#totalNum").html("总记录" + data.totalNum + "条");
	            $("#tradeTotalNum").html("总笔数:" + data.totalNum + "条");
	            $("#totalAmount").html("总金额:" + data.totalAmount + "元");
	            
	            var prePage = data.currentPage <=1 ? 1: (Number(data.currentPage) -1);
	            $("#prePage").attr("onclick","tradeQuery('" + prePage +"')");
	            var next = data.currentPage >=data.totalPage ? data.currentPage: (Number(data.currentPage) + 1);
	            $("#nextPage").attr("onclick","tradeQuery('" + next + "')");
	            $("#lastPage").attr("onclick","tradeQuery('" + data.totalPage + "')");
	            $("#currentAndTotal").html(data.currentPage + "/" + data.totalPage);
	            var pageSelect;
	            for(var p = 1; p<=data.totalPage; p++){
					if(p == data.currentPage){
						pageSelect += '<option selected>' + p + '</option>';
					}else{
						pageSelect += '<option>' + p + '</option>';
					}
		        }
	            $("#xzPage").html(pageSelect);
	           
                $("#trade_tbody").html("");
                var html = "";

                for(var i=0; i<data.data.length; i++){
                	var item = data.data[i];
                	if(i%2 == 0) {
                		html += '<tr class="active">';
                	}
                	else {
                		html += '<tr>';
                	}
                	html = html +  '<td>' + (i+1+(Number(data.currentPage)-1)*10) + '</td><td>' +
                        "<button type='button' id='checkDaikou' name='checkDaikou' onclick='checkDaikou(\"" + item.tradeSn + "\", \"" + item.merchantId + "\")'>通过</button><button type='button' channelId='refuseDaikou' name='refuseDaikou' onclick='refuseDaikou(\"" + item.tradeSn + "\", \"" + item.merchantId + "\")' >拒绝</button>" + '</td><td>' +
                	item.cardNo + '</td><td>' +
                	item.cardHolderName + '</td><td>' +
                	item.cerNumber + '</td><td>' +
                	item.mobileNum + '</td><td>' +
                	item.total_fee + '</td><td>' +
                	item.time_start.substring(8,14) + '</td><td>' +
                	item.merchantId + '</td><td>' +
                	'<span class="span_auto_width">' + item.mchtName + '</span></td><td>' +
                	item.mcht_id + '</td><td>';
	                html = html + '<span class="span_auto_width">' + item.tradeSn + '</span></td><td>' +
                	item.out_trade_no + '</td><td>' +
                	item.mcht_rate + '</td><td>' +
                	item.rate_fee + '</td><td>' +
                	item.settle_fee + '</td><td>' +
                	
                	//item.channel_id + '</td><td>' +
                	item.channel_id + '</td><td>';

                	//应答码
                	if(item.result_code == '00' || item.result_code == '0' || item.result_code == '000000' || item.result_code == '0000') {
	                	html += '<span class="text_green">' + item.result_code + '</span></td><td>';
	                }
                	else {
                		html += '<span class="text_red">' + item.result_code + '</span></td><td>';
	                }
	                //应答码描述
                	if(item.result_code == '00' || item.result_code == '0' || item.result_code == '000000' || item.result_code == '00000' || item.result_code == '0000') {
	                	html += '<span class="text_green span_auto_width">' + item.message + '</span></td><td>';
	                }
                	else {
                		html += '<span class="text_red span_auto_width">' + item.message + '</span></td><td>';
	                }

                	
                	

                	<%--if(item.t0RespCode == '00') {
	                	html += '<span class="text_green">' + item.t0RespCode + '</span></td><td>';
	                	html += '<span class="text_green">' + item.t0RespDesc + '</span></td><td>';
	                }
                	else{
                		html += '<span class="text_red span_auto_width">' + item.t0RespCode + '</span></td><td>';
                		html += '<span class="text_red span_auto_width">' + item.t0RespDesc + '</span></td><td>';
	                }
                	

                	html += '<span class="span_auto_width">' + item.bankname + '</span></td><td>' + 
                	'<span class="span_auto_width">' + item.bankcardnum + '</span></td><td>' +
                	'<span class="span_auto_width">' + item.realname + '</span></td><td>' +
                	'<span class="span_auto_width">' + item.free + '</span></td>';--%>

                	html += '</tr>';

                }
                $("#trade_tbody").html(html);
                
                }
				
        });
	}

	//交易导出
	function tradeExport(){

		var time_start_begin = $("#time_start_begin").val();
		var time_start_end = $("#time_start_end").val();
		if(daysBetween(dateFormat(time_start_begin), dateFormat(time_start_end)) > 10) {
			toastr.error("交易时间范围不能大于10天");
        	return ;
		}
		$("#loading", window.parent.document).show();
		setTimeout(function(){
			$.ajax({
	            cache: true,
	            type: "POST",
	            timeout: 10000,
	            url:"manage/quickPayExport?r=" + new Date().getTime(),
	            data:$('#trade_form').serialize(),// 你的formid
	            dataType : "text",
	            async: false,
	            error: function(request) {
					$("#loading", window.parent.document).hide();
	                alert("Connection error");
	            },
	            success: function(data) {
	            	$("#loading", window.parent.document).hide();
	            	window.location.href = "<%= request.getContextPath()%>" + '/manage/ajaxDownLoad?path='+ data ;
	            }
	        });
		},300);
		
	}
	
	$(function() {
		$("#loading", window.parent.document).hide();
		//控制日期或时间的显示格式
//		this.DateTimePicker1.CustomFormat = "yyyy-MM-dd HH:mm:ss";
//		//使用自定义格式
//		this.DateTimePicker1.Format = DateTimePickerFormat.Custom;
//		//时间控件的启用
//		this.DateTimePicker1.ShowUpDown = True;
		
		$("#time_start_begin").datetimepicker( {
			format : 'yyyymmdd',
			minView : 'month',
			language : 'zh-CN',
			autoclose : true,
			forceParse: false,
			todayHighlight : true,
			startDate: getDateAfter(-90,'yyyy-MM-dd'),
			endDate : new Date(),
			initialDate: getDateAfter(0,'yyyy-MM-dd')
		})
		.val(getDateAfter(0,'yyyyMMdd'));
		//$('#time_start_begin').datetimepicker('update', new Date());
		 
		$("#time_start_end").datetimepicker( {
			format : 'yyyymmdd',
			minView : 'month',
			language : 'zh-CN',
			autoclose : true,
			forceParse: false,
			todayHighlight : true,
			startDate: getDateAfter(-90,'yyyy-MM-dd'),
			endDate : new Date(),
			initialDate: getDateAfter(0,'yyyy-MM-dd')
		})
		.val(getDateAfter(0,'yyyyMMdd'));

		SelectOptionsDWR.getComboData('MCHT_INFO', function(ret) {

			$("#merchantId").html("");
			var mchtInfos = jQuery.parseJSON(ret).data;
			var mchtHtml = "<option value=''>全部</option>";
			mchtInfos.forEach(function(item,index){
				mchtHtml += "<option value='" + item.valueField + "'>" + item.displayField + "</option>"
			});
			$("#merchantId").html(mchtHtml);
			$(".selectpicker").selectpicker('refresh');
      		
		});
		//查询交易
		$("#trade_query_btn").click(function(){
			$("#loading", window.parent.document).show();
			setTimeout('tradeQuery()',300);
			
		});
		//交易导出
		$("#exportBtn").click(function(){
			//$("#loading", window.parent.document).show();
			//setTimeout('',300);
			tradeExport();
			
		});
		//tradeQuery();
		$("#trade_query_btn").click();
	});
	function checkDaikou(orderNo, mchtNo){
		$("#loading", window.parent.document).show();
		setTimeout(function(){
			$.ajax({
	            cache: true,
	            type: "POST",
	            timeout: 30000,
	            url:"manage/checkDaikou?r=" + new Date().getTime(),
	            dataType : "text",
	            data:'orderNo=' + orderNo + '&mchtNo=' + mchtNo,
	            async: false,
	            error: function(request) {
					$("#loading", window.parent.document).hide();
	                alert("Connection error");
	            },
	            success: function(data) {
	            	$("#loading", window.parent.document).hide();
	            	var code = data.split('--');
		            if(code[0]=="0000") {
		            	toastr.success("审核成功!");
			        }
		            else {
		            	toastr.error(data);
			        }
		            tradeQuery();
	            }
	        });
		},300);
	}
	jPrompt = function(message, value, title, callback) {
		$.alerts.prompt(message, value, title, callback);
		};
	function refuseDaikou(orderNo, mchtNo){
		//原因
		var remark = '';
		remark = prompt('请输入拒绝原因', '');
		//alert(remark);
		if(remark == null){
			return;
		}
		$("#loading", window.parent.document).show();
		setTimeout(function(){
			$.ajax({
	            cache: true,
	            type: "POST",
	            timeout: 30000,
	            url:"manage/refuseDaikou?r=" + new Date().getTime(),
	            dataType : "text",
	            data:'orderNo=' + orderNo + '&mchtNo=' + mchtNo + '&remark=' + remark,
	            async: false,
	            error: function(request) {
					$("#loading", window.parent.document).hide();
	                alert("Connection error");
	            },
	            success: function(data) {
	            	$("#loading", window.parent.document).hide();
	            	var code = data.split('--');
		            if(code[0]=="0000") {
		            	toastr.success("审核成功!");
			        }
		            else {
		            	toastr.error(data);
			        }
		            tradeQuery();
	            }
	        });
		},300);
	}
	</script>

</body></html>