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
    	//obj.height = this.document.body.scrollHeight; //调整父页面中IFrame的高度为此页面的高度 
    	} 
  //初始化toastr.js
    //toastr.options = {positionClass:'toast-top-center', timeOut: "2000"};
    </script>
	</head>
	<body onload="IFrameResize()" class=" theme-blue" >

		<div >
			<div class="header">
				<h1 class="page-title">
					公司新增
				</h1>
			</div>

			<div class="row">
				<div class="col-md-8">
					<div id="myTabContent" class="tab-content">
						<div class="tab-pane active in" id="home">

							<form id="companyForm" name="companyForm" class="form-horizontal">
								<div class="form-group">
									<label for="company_id" class="col-sm-2 control-label">
										公司编号
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="company_id" onkeyup="this.value=this.value.replace(/[^a-z0-9]/g,'');" maxlength="10"
											name="company_id" placeholder="请输入6-10位英文字母或数字">
									</div>
								</div>
								
								<div class="form-group">
									<label for="phone" class="col-sm-2 control-label">
										公司名称
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="company_name" maxlength="32"
											name="company_name" placeholder="公司名称">
									</div>
								</div>
								
								<div class="form-group">
									<label for="opr_pwd_reg" class="col-sm-2 control-label">
										登录密码
									</label>
									<div class="col-sm-6">
										<input type="password" class="form-control" id="opr_pwd_reg" autocomplete="new-password"
											name="opr_pwd_reg" placeholder="请输入登录密码">
											
										<input type="hidden" class="form-control span12 form-control" name="opr_pwd" id="opr_pwd">
									</div>
								</div>
								<div class="form-group">
									<label for="connact_name" class="col-sm-2 control-label">
										联系人姓名
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="connact_name" autocomplete="off" maxlength="32"
											name="connact_name" placeholder="请输入联系人姓名">
									</div>
								</div>
								<div class="form-group">
									<label for="identify_no" class="col-sm-2 control-label">
										联系人身份证
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="identify_no" autocomplete="off"
											name="identify_no" placeholder="请输入联系人身份证">
									</div>
								</div>
								
								<div class="form-group">
									<label for="opr_mobile" class="col-sm-2 control-label">
										联系人手机号
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="opr_mobile" autocomplete="off" maxlength="15"
											name="opr_mobile" placeholder="请输入联系人手机号">
									</div>
								</div>
								
								<div class="form-group">
									<label for="address" class="col-sm-2 control-label">
										地址
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="address" autocomplete="off" maxlength="128"
											name="address" placeholder="请输入地址">
									</div>
								</div>
								
								<div class="form-group">
									<label for="wechat_fee_value" class="col-sm-2 control-label">
										微信费率
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="wechat_fee_value" onkeyup="value=value.replace(/[^\d.]/g,'')"
											name="wechat_fee_value" placeholder="请输入微信费率(%)">
									</div>
								</div>
								<div class="form-group">
									<label for="alipay_fee_value" class="col-sm-2 control-label">
										支付宝费率
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="alipay_fee_value" onkeyup="value=value.replace(/[^\d.]/g,'')"
											name="alipay_fee_value" placeholder="请输入支付宝费率(%)">
									</div>
								</div>
								<div class="form-group">
									<label for="alipay_fee_value" class="col-sm-2 control-label">
										QQ钱包费率
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="qq_fee_value" onkeyup="value=value.replace(/[^\d.]/g,'')"
											name="qq_fee_value" placeholder="请输入QQ钱包费率(%)" >
									</div>
								</div>
								<div class="form-group">
									<label for="quickpay_fee_value" class="col-sm-2 control-label">
										快捷支付费率
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="quickpay_fee_value" onkeyup="value=value.replace(/[^\d.]/g,'')"
											name="quickpay_fee_value" placeholder="请输入快捷支付费率(%)">
									</div>
								</div>
								<div class="form-group">
									<label for="netpay_fee_value" class="col-sm-2 control-label">
										网银支付费率
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="netpay_fee_value" onkeyup="value=value.replace(/[^\d.]/g,'')"
											name="netpay_fee_value" placeholder="请输入网银支付费率(%)">
									</div>
								</div>
								<div class="form-group">
									<label for="single_extra_fee" class="col-sm-2 control-label">
										单笔代付费/分
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="single_extra_fee" onkeyup="value=value.replace(/[^\d.]/g,'')"
											name="single_extra_fee" placeholder="请输入单笔代付手续费(分)">
									</div>
								</div>
								
							</form>
						</div>

					</div>
				</div>
			</div>

	<div class="btn-toolbar list-toolbar">
      <button class="btn btn-primary" id="saveCompanyBtn"><i class="fa fa-save"></i> 保存</button>
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
	//公司新增
	
	$(function() {
		$("#loading", window.parent.document).hide();

		function doCompanyAdd(){
	        
			var secretPw = hex_md5($('#opr_pwd_reg').val());
	        $('#opr_pwd').val(secretPw);
	        
			$("#loading", window.parent.document).show();
			
			setTimeout(function(){
				var uid = $("#identify_no").val();
				if (!IdCardValidate(uid)) {
					toastr.error("身份证号码有误");
					return;
				}
				
				$.ajax({
		            cache: true,
		            type: "POST",
		            timeout: 30000,
		            url:"manage/companyAdd?r=" + new Date().getTime(),
		            dataType : "text",
		            data:$('#companyForm').serialize(),// 你的formid
		            async: false,
		            error: function(request) {
						//$("#loading", window.parent.document).hide();
		                alert("Connection error");
		            },
		            success: function(data) {
		            	$("#loading", window.parent.document).hide();
			            if(data=="00000") {
			            	toastr.success("公司新增成功!");
			            	$('#companyForm')[0].reset();
				        }
			            else {
			            	toastr.error(data);
				        }
		            }
		        });
			},300);
		}
		
		$("#resetBtn").click(function(){
			$('#companyForm')[0].reset();
		});
		//保存商户
		$("#saveCompanyBtn").click(function(){

			if($('#company_id').val() == "") {
	        	toastr.error("用户名不能为空");
	        	$('#company_id').focus();
	        	return ;
	        }
			if($('#company_name').val() == "") {
	        	toastr.error("用户名不能为空");
	        	$('#company_name').focus();
	        	return ;
	        }
	        if($('#opr_pwd_input').val() == "") {
	        	toastr.error("登录密码不能为空");
	        	$('#opr_pwd_input').focus();
	        	return ;
	        }
	        
			window.parent.wxc.xcConfirm("确认保存公司信息?", window.parent.wxc.xcConfirm.typeEnum.confirm, {
				onOk:function(v){
					doCompanyAdd();
				}
			});
		});
		
	});
	
	</script>

	</body>
</html>