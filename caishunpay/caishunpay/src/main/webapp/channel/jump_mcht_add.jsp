<%@ page language="java" import="java.util.*,com.trade.bean.own.*,com.gy.util.CommonFunction" pageEncoding="UTF-8"%>
<%@page import="com.manage.bean.PageModle"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@page import="com.manage.bean.OprInfo,com.trade.bean.*,com.gy.util.*"%>
<%@ include file="../manage/include.jsp"%>
<%
QrcodeMchtInfoTmp mchtInfo = (QrcodeMchtInfoTmp)request.getAttribute("mchtInfo");
%>
<html lang="en">
	<head>
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
	<body onload="IFrameResize()" class=" theme-blue">

		<div >
			<div class="header">
				<h1 class="page-title">
					跳码商户新增
				</h1>
			</div>

			<div class="row">
				<div class="col-md-8">
					<div id="myTabContent" class="tab-content">
						<div class="tab-pane active in" id="home">

							<form id="mchtForm" name="mchtForm" class="form-horizontal">
							
							<div class="form-group">
									<label class="control-label col-sm-2" for="lunch">
										跳码组
									</label>
									<div class="col-sm-6">
										<select id="jump_group" name="jump_group"
										class="selectpicker" data-live-search="true" title="选择跳码组">
											
										</select>
									</div>
								</div>
								
								<div class="form-group">
									<label class="control-label col-sm-2" for="lunch">
										渠道编号
									</label>
									<div class="col-sm-6">
										<select id="channel_id" name="channel_id"
										class="selectpicker" data-live-search="true" title="请选择渠道编号">

										</select>
									</div>
									
								</div>
								
								<div class="form-group">
									<label class="control-label col-sm-2" for="lunch">
										渠道商户
									</label>
									<div class="col-sm-6">
										<select id="channel_mcht_no" name="channel_mcht_no"
										class="selectpicker" data-live-search="true" title="请选择渠道商户">

										</select>
									</div>
									
								</div>
								
								<div class="form-group">
									<label for="tradeSource1" class="col-sm-2 control-label">
										交易来源
									</label>
									<div class="col-sm-6">
										<label class="checkbox-inline">
											<input type="checkbox" id="tradeSource1" name="trade_source" value="1" > 支付宝
										</label>
										<label class="checkbox-inline">
											<input type="checkbox"  name="trade_source" value="12" > 支付宝H5支付
										</label>
										<label class="checkbox-inline">
											<input type="checkbox" id="tradeSource2" name="trade_source" value="2" > 微信扫码支付
										</label>
										<label class="checkbox-inline">
											<input type="checkbox" id="tradeSource21" name="trade_source" value="21"> 微信公众号支付
										</label>
										<label class="checkbox-inline">
											<input type="checkbox" id="tradeSource22" name="trade_source" value="22"> 微信H5支付
										</label>
										<label class="checkbox-inline">
											<input type="checkbox" id="tradeSource4" name="trade_source" value="4" > 手机QQ支付
										</label>
										<label class="checkbox-inline">
											<input type="checkbox" id="tradeSource6" name="trade_source" value="6"> 网银支付
										</label>
										<label class="checkbox-inline">
											<input type="checkbox" id="tradeSource7" name="trade_source" value="7"> 快捷支付
										</label>
										<label class="checkbox-inline">
											<input type="checkbox" id="tradeSource71"
												   name="trade_source" value="71"> 银联扫码
										</label>
									</div>
								</div>
								<input id="group_name" name="group_name" type="hidden">
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
				var channel_mcht_no = $("#channel_mcht_no").attr("value");
				$("#channel_mcht_no").html("");
				var mchtInfos = jQuery.parseJSON(ret).data;
				var mchtHtml = "";
				mchtInfos.forEach(function(item,index){
					if(channel_mcht_no == item.valueField){
						mchtHtml += "<option value='" + item.valueField + "' selected = 'selected'>" + item.displayField + "</option>"
					}
					else{
						mchtHtml += "<option value='" + item.valueField + "'>" + item.displayField + "</option>"
					}
					
				});
				$("#channel_mcht_no").html(mchtHtml);
				$("#channel_mcht_no").selectpicker('refresh');
	      		
			});
		}

		$("#channel_id").change(function(){
			switchChannelMcht();
		});
		$("#jump_group").change(function(){
			$("#group_name").val($("#jump_group").text());
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
			
			$("#loading", window.parent.document).show();
			setTimeout(function(){
				$.ajax({
		            cache: true,
		            type: "POST",
		            timeout: 30000,
		            url:"manage/jumpMchtAdd?r=" + new Date().getTime(),
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
			            	toastr.success("跳码商户新增成功!");
			            	
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