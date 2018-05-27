<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.manage.bean.PageModle"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@page import="com.manage.bean.OprInfo,com.trade.bean.*,com.gy.util.*"%>
<%@ include file="include.jsp"%>

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
					通道商户新增
				</h1>
			</div>

			<div class="row">
				<div class="col-md-8">
					<div id="myTabContent" class="tab-content">
						<div class="tab-pane active in" id="home">

							<form id="channelMchtForm" name="channelMchtForm" class="form-horizontal">
								<div class="form-group">
									<label for="agtId" class="col-sm-2 control-label">
										通道机构号
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="agtId"
											name="agtId" placeholder="请输入通道机构号">
									</div>
								</div>
								<div class="form-group">
									<label for="channel_mcht_no" class="col-sm-2 control-label">
										通道商户编号
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="channel_mcht_no"
											name="channel_mcht_no" placeholder="请输入通道商户编号">
									</div>
								</div>
								
								<div class="form-group">
									<label for="mchtName" class="col-sm-2 control-label">
										通道商户名称
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="channel_name"
											name="channel_name" placeholder="请输入通道商户名称">
									</div>
								</div>
								
								<div class="form-group">
									<label for="secret_key" class="col-sm-2 control-label">
										交易密钥
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="secret_key"
											name="secret_key" placeholder="请输入交易密钥">
									</div>
								</div>
								
								<div class="form-group">
									<label class="control-label col-sm-2" for="lunch">
										商户所属通道
									</label>
									<div class="col-sm-6">
										<select id="channel_id" name="channel_id"
										class="selectpicker" data-live-search="true" title="请选择所属通道">

										</select>
									</div>
									
								</div>
								
								<div class="form-group">
									<label for="lisence_addr" class="col-sm-2 control-label">
										微信费率
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="wechat_fee_value"
											name="wechat_fee_value" placeholder="请输入微信费率(%)">
									</div>
								</div>
								<div class="form-group">
									<label for="lisence_addr" class="col-sm-2 control-label">
										支付宝费率
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="alipay_fee_value"
											name="alipay_fee_value" placeholder="请输入支付宝费率(%)">
									</div>
								</div>
								<div class="form-group">
									<label for="lisence_addr" class="col-sm-2 control-label">
										快捷支付费率
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="quickpay_fee_value"
											name="quickpay_fee_value" placeholder="请输入快捷支付费率(%)">
									</div>
								</div>
								<div class="form-group">
									<label for="lisence_addr" class="col-sm-2 control-label">
										网银支付费率
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="netpay_fee_value"
											name="netpay_fee_value" placeholder="请输入网银支付费率(%)">
									</div>
								</div>
							</form>
						</div>

					</div>
				</div>
			</div>

	<div class="btn-toolbar list-toolbar">
      <button class="btn btn-primary" id="saveMchtBtn"><i class="fa fa-save"></i> 保存</button>
      <a  data-toggle="modal" class="btn btn-danger" id="resetBtn">重置</a>
      
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

		SelectOptionsDWR.getComboData('CHANNEL_ID',function(ret) {
			$("#channel_id").html("");
			var mchtInfos = jQuery.parseJSON(ret).data;
			var mchtHtml = "";
			mchtInfos.forEach(function(item,index){
				mchtHtml += "<option value='" + item.valueField + "'>" + item.displayField + "</option>"
			});
			$("#channel_id").html(mchtHtml);
			$("#channel_id").selectpicker('refresh');
		});

		function doChannelMchtAdd(){
			
			$("#loading", window.parent.document).show();
			//toastr.success("商户新增成功!");
			//return ;
			setTimeout(function(){
				$.ajax({
		            cache: true,
		            type: "POST",
		            timeout: 30000,
		            url:"manage/channelMchtAdd?r=" + new Date().getTime(),
		            data:$('#channelMchtForm').serialize(),// 你的formid
		            async: false,
		            dataType : "text",
		            error: function(request) {
						//$("#loading", window.parent.document).hide();
		                alert("Connection error");
		            },
		            success: function(data) {
		            	$("#loading", window.parent.document).hide();
			            if(data=="00000") {
			            	toastr.success("通道商户新增成功!");
			            	$('#channelMchtForm')[0].reset();
				        }
			            else {
			            	toastr.error(data);
				        }
		            }
		        });
			},300);
		}
		//SelectOptionsDWR.getComboData('CHANNEL_ID', function(ret) {});
		
		$("#resetBtn").click(function(){
			$('#channelMchtForm')[0].reset();
		});
		//保存商户
		$("#saveMchtBtn").click(function(){

			window.parent.wxc.xcConfirm("确认保存通道商户信息?", window.parent.wxc.xcConfirm.typeEnum.confirm, {
				onOk:function(v){
					doChannelMchtAdd();
				}
			});
		});

		
		
	});
	
	</script>

	</body>
</html>