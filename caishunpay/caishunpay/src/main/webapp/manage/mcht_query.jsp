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
					商户查询
				</h1>
			</div>
			
			<%--<iframe width="100%" style="border: 0" height="900px" scrolling="no" src="<%= request.getContextPath()%>/manage/tradeInfo.jsp"></iframe>
			
		--%>
		</div>
		<div class="search-well">
                <form class="form-inline" id="mcht_form" name="mcht_form" style="margin-top:0px;margin-left: 10px" action="manage/mchtQuery" target="_self">
                	<table >
                		<tr>
                		<td><span><label class="control-label span_auto_width" for="lunch">公司编号：</label></span></td><td>
					        
						     <select id="company_id" name="company_id"
										class="selectpicker" data-live-search="true" title="请选公司编号">
							</select>
                    	</td>
                		<td><span><label class="control-label span_auto_width" for="lunch">商户号：</label></span></td><td>
					        <select id="mchtNo" name="mchtNo" class="selectpicker" data-live-search="true" title="全部">
						        
						     </select>
                    	</td>
                    	<td>
            				<label>身份证号：</label></td><td>
                    		<input name="identity_no" id="identity_no" class="input-xlarge form-control" placeholder="身份证号"  type="text">	
          				</td>
                		<td><input type="hidden" id="pageNum" name="pageNum" value="1"></td><td align="right">
                    		<button class="btn btn-default" type="button" id="trade_query_btn" name="trade_query_btn"><i class="fa fa-search"></i> 查询</button>
                    	</td>
                    	</tr>
                	</table>
                	
                </form>
            </div>
            
			<div class="main-content">
				<div class="btn-toolbar list-toolbar">
					<button class="btn btn-primary" id="exportBtn">
						<i class="fa fa-plus"></i>商户导出
					</button>
					<button class="btn btn-default">
						export
					</button>
					<div class="btn-group">
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
									<span style="width: 80px; display: block;">商户编号</span>
								</th>
								<th>
									<span style="width: 80px;  display: inline-block; overflow: hidden">商户名称</span>
								</th>
								<th>
									<span style="width: 90px; display: block;">渠道商户号</span>
								</th>
								<th>
									<span style="width: 80px; display: block;">渠道标识</span>
								</th>
								<th>
									<span style="width: 120px; display: block;">公司编号</span>
								</th>
								<th>
									<span style="width: 120px; display: block;">公司名称</span>
								</th>
								<th>
									<span style="width: 90px; display: block;">商户状态</span>
								</th>
								<th>
									<span style="width: 100px; display: block;">手机号</span>
								</th>
								<th>
									<span style="width: 120px; display: block;">身份证号</span>
								</th>
								<th>
									<span style="width: 120px; display: block;">结算卡号</span>
								</th>
								<th>
									<span style="width: 120px; display: block;">结算银行</span>
								</th>
								<th>
									<span style="width: 110px; display: block;">结算法人</span>
								</th>
								<th>
									<span style="width: 130px; display: block;">微信扫码费率</span>
								</th>
								<th>
									<span style="width: 130px; display: block;">微信wap费率</span>
								</th>
								<th>
									<span style="width: 130px; display: block;">支付宝费率</span>
								</th>
								<th>
									<span style="width: 130px; display: block;">支付宝wap费率</span>
								</th>
								<th>
									<span style="width: 130px; display: block;">QQ费率</span>
								</th>
								<th>
									<span style="width: 130px; display: block;">快捷支付费率</span>
								</th>
								<th>
									<span style="width: 130px; display: block;">网银支付费率</span>
								</th>
								<th>
									<span style="width: 130px; display: block;">单笔代付费/分</span>
								</th>
								<th>
									<span style="width: 130px; display: block;">代扣费率(%)</span>
								</th>
								<th>
									<span style="width: 130px; display: block;">代扣保底费用(分)</span>
								</th>
								<th>
									<span style="width: 90px; display: block;">注册时间</span>
								</th>
								<th>
									<span style="width: 120px; display: block;">操作</span>
								</th>
								</tr>
						</thead>

						<tbody id="mcht_tbody">
							
						</tbody>
					</table>
				</div>
				
				<ul class="pager">  
          
        <li id="totalNum">总记录条</li>  
        <li>  </li>  
            <li id="firstPage">  
                <a href="javascript:void(0);" onclick="mchtQuery('1')">首页</a>  
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
              
            <li><a href="javascript:void(0);" onclick="mchtQuery(document.getElementById('xzPage').value);">go</a></li>  
        </ul>

				
				
	<script type="text/javascript">
	//交易查询
	function mchtQuery(num){
		if(num != 'undefined'){
			$("#pageNum").val(num);
		}
		$.ajax({
            cache: true,
            type: "POST",
            url:"manage/mchtQuery?r=" + new Date().getTime(),
            data:$('#mcht_form').serialize(),// 你的formid
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
	            var prePage = data.currentPage <=1 ? 1: (Number(data.currentPage) -1);
	            $("#prePage").attr("onclick","mchtQuery('" + prePage +"')");
	            var next = data.currentPage >=data.totalPage ? data.currentPage: (Number(data.currentPage) + 1);
	            $("#nextPage").attr("onclick","mchtQuery('" + next + "')");
	            $("#lastPage").attr("onclick","mchtQuery('" + data.totalPage + "')");
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
	           
                $("#mcht_tbody").html("");
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
                	item.mchtNo + '</td><td>' +
                	'<span style="display: inline-block; overflow: hidden;white-space:nowrap;" >' + item.mchtName + '</span></td><td>' +
                	item.channelMchtNo + '</td><td>' +
                	'<span class="span_auto_width">' + item.channel_id + '</span></td><td>' +
                	item.company_id + '</td><td>' +
                	'<span class="span_auto_width">' + item.companyName + '</span></td><td>' +
                	mchtStatusTrans(item.status) + '</td><td>' +
                	item.phone + '</td><td>' +
                	'<span class="span_auto_width">' + item.identity_no + '</span></td><td>' +
                	item.bank_card_no + '</td><td>' +
                	'<span class="span_auto_width">' + item.bank_name + '</span></td><td>' +
                	'<span class="span_auto_width">' + item.card_name + '</span></td><td>' +
                	item.wechat_fee_value + '</td><td>' +
                	item.wechatwap_fee_value + '</td><td>' +
                	item.alipay_fee_value + '</td><td>' +
                	item.aliwap_fee_value + '</td><td>' +
                	item.qq_fee_value + '</td><td>' +
                	item.quickpay_fee_value + '</td><td>' +
                	item.netpay_fee_value + '</td><td>' +
                	item.single_extra_fee + '</td><td>' +
                	item.daikou_fee_value + '</td><td>' +
                	item.daikou_fee_limit_value + '</td><td>' +
                	item.crtTime + '</td><td>' +
                	'<a href="manage/mchtQuerySingle?mchtNo=' + item.mchtNo + '"><span class="text_blue span_auto_width">修改</span></a>&nbsp;&nbsp;';
                	if("00"==item.status) {
                		html = html + '<a href="javascript:void(0)" onclick="mchtFreeze(\'' + item.mchtNo + '\')"><span class="text_red span_auto_width">冻结</span></a>';
                    }
                	else if("02"==item.status){
                        html = html + '<a href="javascript:void(0)" onclick="mchtRecover(\'' + item.mchtNo + '\')"><span class="text_green span_auto_width">恢复</span></a>'
                    }
                	html += '</td></tr>';

                }
                $("#mcht_tbody").html(html);
                
                }
				
        });
	}

	//商户导出
	function mchtExport(){
		//return ;
		$("#loading", window.parent.document).show();
		setTimeout(function(){
			$.ajax({
	            cache: true,
	            type: "POST",
	            timeout: 10000,
	            url:"manage/mchtExport?r=" + new Date().getTime(),
	            data:$('#mcht_form').serialize(),// 你的formid
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
	
	function mchtFreeze(mchtNo){

		window.parent.wxc.xcConfirm("确认冻结商户?", window.parent.wxc.xcConfirm.typeEnum.confirm, {
			onOk:function(v){
			$.ajax({
	            cache: true,
	            type: "POST",
	            timeout: 10000,
	            url:"manage/mchtFreeze?r=" + new Date().getTime(),
	            data:{"mchtNo": mchtNo},
	            async: true,
	            error: function(request) {
					//$("#loading", window.parent.document).hide();
	                alert("Connection error");
	            },
	            success: function(data) {
	            	if(data=="00000") {
	            		toastr.success("商户冻结成功!");
	            		mchtQuery();
			        }
		            else {
		            	toastr.error(data);
			        }
	            }
	        });
			}
		});
	}

	function mchtRecover(mchtNo){

		window.parent.wxc.xcConfirm("确认恢复商户?", window.parent.wxc.xcConfirm.typeEnum.confirm, {
			onOk:function(v){
			$.ajax({
	            cache: true,
	            type: "POST",
	            timeout: 10000,
	            url:"manage/mchtRecover?r=" + new Date().getTime(),
	            dataType : "text",
	            data:{"mchtNo": mchtNo},
	            async: true,
	            error: function(request) {
					//$("#loading", window.parent.document).hide();
	                alert("Connection error");
	            },
	            success: function(data) {
	            	if(data=="00000") {
	            		toastr.success("商户恢复成功!");
	            		mchtQuery();
			        }
		            else {
		            	toastr.error(data);
			        }
	            	
	            }
	        });
			}
		});
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
			startDate: getDateAfter(-30,'yyyy-MM-dd'),
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
			startDate: getDateAfter(-30,'yyyy-MM-dd'),
			endDate : new Date(),
			initialDate: getDateAfter(0,'yyyy-MM-dd')
		})
		.val(getDateAfter(0,'yyyyMMdd'));

		function switchMcht() {
			SelectOptionsDWR.getComboDataWithParameter('MCHT_INFO',$("#company_id").val(), function(ret) {

				$("#mchtNo").html("");
				var mchtInfos = jQuery.parseJSON(ret).data;
				var mchtHtml = "<option value=''>全部</option>";
				mchtInfos.forEach(function(item,index){
					mchtHtml += "<option value='" + item.valueField + "'>" + item.displayField + "</option>"
				});
				$("#mchtNo").html(mchtHtml);
				$(".selectpicker").selectpicker('refresh');
	      		
			});
		}

		$("#company_id").change(function(){
			switchMcht();
		});

		switchMcht();

		SelectOptionsDWR.getComboData('COMPANY_ID',function(ret) {
			$("#company_id").html("");
			var mchtInfos = jQuery.parseJSON(ret).data;
			var mchtHtml = "<option value=''>全部</option>";
			mchtInfos.forEach(function(item,index){
				mchtHtml += "<option value='" + item.valueField + "'>" + item.displayField + "</option>"
			});
			$("#company_id").html(mchtHtml);
			$("#company_id").selectpicker('refresh');
			//$('#company_id').selectpicker('render');
		});
		//查询交易
		$("#trade_query_btn").click(function(){
			$("#loading", window.parent.document).show();
			setTimeout('mchtQuery()',300);
			
		});
		//商户导出
		$("#exportBtn").click(function(){
			mchtExport();
			
		});
		
		//mchtQuery();
		$("#trade_query_btn").click();
	});
	
	</script>

</body></html>