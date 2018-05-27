<%@ page language="java" import="java.util.*,com.trade.bean.own.*,com.gy.util.CommonFunction" pageEncoding="UTF-8"%>
<%@page import="com.manage.bean.PageModle"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@page import="com.manage.bean.OprInfo,com.trade.bean.*,com.gy.util.*"%>
<%@ include file="include.jsp"%>
<%
QrcodeMchtInfoTmp mchtInfo = (QrcodeMchtInfoTmp)request.getAttribute("mchtInfo");
%>
<html lang="en">
	<head>
		<title>彩顺网络科技平台</title>
		<script type="text/javascript">
		//初始化toastr.js
		toastr.options = {positionClass:'toast-bottom-center', timeOut: "2000"};
		
    function IFrameResize(){ 
    	//alert(this.document.body.scrollHeight); //弹出当前页面的高度 
    	var obj = parent.document.getElementById("main_iframe"); //取得父页面IFrame对象 
    	//alert(obj.height); //弹出父页面中IFrame中设置的高度 
    	obj.height = this.document.body.scrollHeight; //调整父页面中IFrame的高度为此页面的高度 
    	} 

	<%
	if(mchtInfo == null) {
	    mchtInfo = new QrcodeMchtInfoTmp();
	%>
		alert("加载商户信息异常");
	<%
	}
	%>
    </script>
	</head>
	<body onload="IFrameResize()" class=" theme-blue">

		<div >
			<div class="header">
				<h1 class="page-title">
					商户修改
				</h1>
			</div>

			<div class="row">
				<div class="col-md-8">
					<div id="myTabContent" class="tab-content">
						<div class="tab-pane active in" id="home">

							<form id="mchtForm" name="mchtForm" class="form-horizontal">
							
								<div class="form-group">
									<label for="mchtName" class="col-sm-2 control-label">
										商户编号
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="mchtNo"
											name="mchtNo" placeholder="请输入商户名称"  readonly value="<%=StringUtil.trans2Str(mchtInfo.getMchtNo()) %>">
									</div>
								</div>
								<div class="form-group">
									<label for="mchtName" class="col-sm-2 control-label">
										商户名称
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="mchtName"
											name="mchtName" placeholder="请输入商户名称" value="<%=StringUtil.trans2Str(mchtInfo.getMchtName()) %>">
									</div>
								</div>
								
								<div class="form-group">
									<label for="phone" class="col-sm-2 control-label">
										邮箱
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="email"
											name="email" placeholder="请输入邮箱" value="<%=StringUtil.trans2Str(mchtInfo.getEmail()) %>">
									</div>
								</div>
								<div class="form-group">
									<label for="phone" class="col-sm-2 control-label">
										手机号码
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="phone"
											name="phone" placeholder="请输入手机号码" readonly value="<%=StringUtil.trans2Str(mchtInfo.getPhone()) %>">
									</div>
								</div>
								
								<div class="form-group">
									<label for="identity_no" class="col-sm-2 control-label">
										身份证号
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="identity_no"
											name="identity_no" placeholder="请输入身份证号"  value="<%=StringUtil.trans2Str(mchtInfo.getIdentity_no())%>">
									</div>
								</div>
								
								<div class="form-group">
									<label for="bank_card_no" class="col-sm-2 control-label">
										结算卡号
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="bank_card_no"
											name="bank_card_no" placeholder="请输入结算卡号" value="<%=StringUtil.trans2Str(mchtInfo.getBank_card_no()) %>">
									</div>
								</div>
								
								<div class="form-group">
									<label for="bank_name" class="col-sm-2 control-label">
										结算银行
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="bank_name"
											name="bank_name" placeholder="请输入结算银行" value="<%=StringUtil.trans2Str(mchtInfo.getBank_name())%>">
									</div>
								</div>
								<div class="form-group">
									<label for="card_name" class="col-sm-2 control-label">
										结算人
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="card_name"
											name="card_name" placeholder="请输入结算人"  value="<%=StringUtil.trans2Str(mchtInfo.getCard_name())%>">
									</div>
								</div>
								<div class="form-group">
									<label for="bank_no" class="col-sm-2 control-label">
										联行号
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="bank_no"
											name="bank_no" placeholder="请输入联行号"  value="<%=StringUtil.trans2Str(mchtInfo.getBank_no()) %>">
									</div>
								</div>
								<div class="form-group">
									<label for="lisence_addr" class="col-sm-2 control-label">
										商户地址
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="lisence_addr"
											name="lisence_addr" placeholder="请输入商户地址"  value="<%=StringUtil.trans2Str(mchtInfo.getLisence_addr()) %>">
									</div>
								</div>
								
								<div class="form-group">
									<label for="lisence_addr" class="col-sm-2 control-label">
										微信费率
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="wechat_fee_value"
											name="wechat_fee_value" placeholder="请输入微信费率(%)" value="<%=StringUtil.trans2Str(mchtInfo.getWechat_fee_value()) %>">
									</div>
								</div>
								<div class="form-group">
									<label for="lisence_addr" class="col-sm-2 control-label">
										微信wap费率
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="wechatwap_fee_value" onkeyup="value=value.replace(/[^\d.]/g,'')"
											name="wechatwap_fee_value" placeholder="请输入微信wap费率(%)" value="<%=StringUtil.trans2Str(mchtInfo.getWechatwap_fee_value()) %>">
									</div>
								</div>
								<div class="form-group">
									<label for="lisence_addr" class="col-sm-2 control-label">
										支付宝费率
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="alipay_fee_value"
											name="alipay_fee_value" placeholder="请输入支付宝费率(%)" value="<%=StringUtil.trans2Str(mchtInfo.getAlipay_fee_value()) %>">
									</div>
								</div>
								<div class="form-group">
									<label for="aliwap_fee_value" class="col-sm-2 control-label">
										支付宝wap费率
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="aliwap_fee_value" onkeyup="value=value.replace(/[^\d.]/g,'')"
											name="aliwap_fee_value" placeholder="请输入支付宝wap费率(%)" value="<%=StringUtil.trans2Str(mchtInfo.getAliwap_fee_value()) %>">
									</div>
								</div>
								<div class="form-group">
									<label for="lisence_addr" class="col-sm-2 control-label">
										QQ钱包费率
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="qq_fee_value"
											name="qq_fee_value" placeholder="请输入QQ钱包费率(%)" value="<%=StringUtil.trans2Str(mchtInfo.getQq_fee_value()) %>">
									</div>
								</div>
								<div class="form-group">
									<label for="lisence_addr" class="col-sm-2 control-label">
										快捷支付费率
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="quickpay_fee_value"
											name="quickpay_fee_value" placeholder="请输入快捷支付费率(%)" value="<%=StringUtil.trans2Str(mchtInfo.getQuickpay_fee_value()) %>">
									</div>
								</div>
								<div class="form-group">
									<label for="lisence_addr" class="col-sm-2 control-label">
										网银支付费率
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="netpay_fee_value"
											name="netpay_fee_value" placeholder="请输入网银支付费率(%)"  value="<%=StringUtil.trans2Str(mchtInfo.getNetpay_fee_value()) %>">
									</div>
								</div>
								
								<div class="form-group">
									<label for="single_extra_fee" class="col-sm-2 control-label">
										单笔代付费/分
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="single_extra_fee" 
											name="single_extra_fee" placeholder="请输入单笔代付手续费(分)" value="<%=StringUtil.trans2Str(mchtInfo.getSingle_extra_fee()) %>">
									</div>
								</div>
								
								<div class="form-group">
									<label class="control-label col-sm-2" for="lunch">
										公司编号
									</label>
									<div class="col-sm-6">
										<select id="company_id" name="company_id"
										class="selectpicker" data-live-search="true" title="请选公司编号" value="<%=StringUtil.trans2Str(mchtInfo.getCompany_id()) %>">

										</select>
									</div>
									
								</div>
								
								<div class="form-group" >
									<label class="col-sm-2 control-label">图片</label>
									
									<div class="col-sm-10" id="mchtFileDiv" >
										
										<div id="myCarousel" class="carousel slide">
											
										</div> 
									</div>
									<c:forEach items="${requestScope.mchtFiles}" var="item">
											<input name="mchtFilePath" type="hidden" value="${item.mchtFilePath}">
										</c:forEach>
								</div>
								
								<input type="hidden" value="" id="refuse_reason" name="refuse_reason">
							</form>
						</div>

					</div>
				</div>
			</div>

	<div class="btn-toolbar list-toolbar">
      <button class="btn btn-primary" id="saveBtn"><i class="fa fa-save"></i> 保存</button>
      <a  data-toggle="modal" class="btn btn-default" id="backBtn">返回</a>
      <!-- href="#myModal" -->
    </div>
		</div>
		
			<footer>
                <hr>
                <!-- Purchase a site license to remove this link from the footer: http://www.portnine.com/bootstrap-themes -->
                <p>© 2017 ykbpay</p>
                <br>
            </footer>
            
<div class="modal small fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
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
	//商户新增
	
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

		initPic();
		
		function initPic() {

			if($("input[name='mchtFilePath']").size()==0){
				return ;
			}
			var images = "";
			var ol = '<ol class="carousel-indicators">';
			var inner = '<div class="carousel-inner">';
			
			$("input[name='mchtFilePath']").each(function(index,element){
				if(index == 0){
					ol += '<li data-target="#myCarousel" data-slide-to="' + index + '" class="active"></li>';
					inner += '<div class="item active" align="center">';
				}
				else {
					ol += '<li data-target="#myCarousel" data-slide-to="' + index + '"></li>';
					inner += '<div class="item" align="center">'
				}
				inner += '<img src="<%=basePath%>/manage/printImage?fileName=' + encodeURIComponent($(this).val()) + '&height=380" alt="商户图片">';
				inner += '</div>';
				//encodeURIComponent(
				//images += '<image src="http://localhost:8088/gyprovider/manage/printImage?fileName=' + encodeURIComponent($(this).val()) + 
				//'" alt="商户图片" class="img-rounded" width="400">';
			});
			ol += '</ol>';
			inner += '</div>';

			//<!-- 轮播（Carousel）导航 -->
			var daohang = '<a class="carousel-control left" href="#myCarousel" data-slide="prev">&lsaquo;</a><a class="carousel-control right" href="#myCarousel" data-slide="next">&rsaquo;</a>';
			   
			 
			document.getElementById('myCarousel').innerHTML = ol + inner + daohang;
			
		}
		
		switchChannelMcht();

		function switchChannelMcht() {
			SelectOptionsDWR.getComboDataWithParameter('CHANNEL_MCHT_NO',$("#channel_id").val(), function(ret) {
				var channelMchtNo = $("#channelMchtNo").attr("value");
				$("#channelMchtNo").html("");
				var mchtInfos = jQuery.parseJSON(ret).data;
				var mchtHtml = "";
				mchtInfos.forEach(function(item,index){
					if(channelMchtNo == item.valueField){
						mchtHtml += "<option value='" + item.valueField + "' selected = 'selected'>" + item.displayField + "</option>"
					}
					else{
						mchtHtml += "<option value='" + item.valueField + "'>" + item.displayField + "</option>"
					}
					
				});
				$("#channelMchtNo").html(mchtHtml);
				$("#channelMchtNo").selectpicker('refresh');
	      		
			});
		}

		$("#channel_id").change(function(){
			switchChannelMcht();
		});

		SelectOptionsDWR.getComboData('CHANNEL_ID',function(ret) {
			var channel_id = $("#channel_id").attr("value");
			$("#channel_id").html("");
			var mchtInfos = jQuery.parseJSON(ret).data;
			var mchtHtml = "";
			//selected = "selected"
			mchtInfos.forEach(function(item,index){
				if(channel_id == item.valueField){
					mchtHtml += "<option value='" + item.valueField + "' selected = 'selected'>" + item.displayField + "</option>"
				}
				else {
					mchtHtml += "<option value='" + item.valueField + "'>" + item.displayField + "</option>"
				}
				
			});
			$("#channel_id").html(mchtHtml);
			$("#channel_id").selectpicker('refresh');
		});

		SelectOptionsDWR.getComboData('COMPANY_ID',function(ret) {
			var company_id = $("#company_id").attr("value");
			$("#company_id").html("");
			var mchtInfos = jQuery.parseJSON(ret).data;
			var mchtHtml = "";
			mchtInfos.forEach(function(item,index){
				if(company_id == item.valueField) {
					mchtHtml += "<option value='" + item.valueField + "' selected = 'selected'>" + item.displayField + "</option>"
				}
				else {
					mchtHtml += "<option value='" + item.valueField + "'>" + item.displayField + "</option>"
				}
				
			});
			$("#company_id").html(mchtHtml);
			$("#company_id").selectpicker('refresh');
		});

		SelectOptionsDWR.getComboData('JUMP_GROUP',function(ret) {
			var jump_group = $("#jump_group").attr("value");
			$("#jump_group").html("");
			var mchtInfos = jQuery.parseJSON(ret).data;
			var mchtHtml = "";
			mchtInfos.forEach(function(item,index){
				if(jump_group == item.valueField) {
					mchtHtml += "<option value='" + item.valueField + "' selected = 'selected'>" + item.displayField + "</option>"
				}
				else {
					mchtHtml += "<option value='" + item.valueField + "'>" + item.displayField + "</option>"
				}
				
			});
			$("#jump_group").html(mchtHtml);
			$("#jump_group").selectpicker('refresh');
		});

		function doAccept(){
			var uid = $("#identity_no").val();
			if (!IdCardValidate(uid)) {
				toastr.error("身份证号码有误");
				return;
			}
			
			$("#loading", window.parent.document).show();
			//toastr.success("商户新增成功!");
			//return ;
			setTimeout(function(){
				$.ajax({
		            cache: true,
		            type: "POST",
		            timeout: 30000,
		            url:"manage/mchtTmpUpdate?r=" + new Date().getTime(),
		            dataType : "text",
		            data:$('#mchtForm').serialize(),// 你的formid
		            async: false,
		            error: function(request) {
						//$("#loading", window.parent.document).hide();
		                alert("Connection error");
		            },
		            success: function(data) {
		            	$("#loading", window.parent.document).hide();
			            if(data=="00000") {
			            	toastr.success("商户修改成功!");
				        }
			            else {
			            	toastr.error(data);
				        }
		            }
		        });
			},300);
		}

		
		$("#backBtn").click(function(){
			history.back();
		});
		//保存
		$("#saveBtn").click(function(){
			window.parent.wxc.xcConfirm("确认保存商户信息?", window.parent.wxc.xcConfirm.typeEnum.confirm, {
				onOk:function(v){
				doAccept();
			}
		});
		});
		
	
	});
	
	</script>

	</body>
</html>