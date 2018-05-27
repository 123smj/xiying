<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.manage.bean.PageModle"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@page import="com.manage.bean.OprInfo,com.trade.bean.*,com.gy.util.*"%>
<%@ include file="../manage/include.jsp"%>

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
	<body onload="IFrameResize()" class="theme-blue">

		<div >
			<div class="header">
				<h1 class="page-title">
					通道进件审核
				</h1>
			</div>

			<div class="row">
				<div class="col-md-8">
					<div id="myTabContent" class="tab-content">
						<div class="tab-pane active in" id="home">

							<form id="channelMchtForm" name="channelMchtForm" class="form-horizontal" action="manage/channelMchtVerify" method="post" enctype ="multipart/form-data">
								<div class="form-group">
									<label for="userid" class="col-sm-2 control-label">
										通道机构号
									</label>
									<div class="col-sm-6">
										<select id="userid" name="userid"
										class="selectpicker" data-live-search="true" title="请选择通道机构号">
											<option value='714974' selected="selected">恒丰银行机构号-714974</option>
										</select>
										
									</div>
								</div>
								<div class="form-group">
									<label for="account" class="col-sm-2 control-label">
										通道商户编号
									</label>
									<div class="col-sm-6">
										<select id="account" name="account"
										class="selectpicker" data-live-search="true" title="请选择通道商户编号">
											
										</select>
										
									</div>
								</div>
								
								<div class="form-group">
									<label for="real_name" class="col-sm-2 control-label">
										开户人名
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="real_name"
											name="real_name" placeholder="请输入开户人名" value="左松">
									</div>
								</div>
								
								<div class="form-group">
									<label for="cmer" class="col-sm-2 control-label">
										商户名称
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="cmer" maxlength="20"
											name="cmer" placeholder="请输入商户名称10个汉字以内" value="">
									</div>
								</div>
								<div class="form-group">
									<label for="cmer_sort" class="col-sm-2 control-label">
										商户简称
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="cmer_sort"
											name="cmer_sort" placeholder="请输入商户简称10个汉字以内" value="">
									</div>
								</div>
								
								<div class="form-group">
									<label for="channel_code" class="col-sm-2 control-label">
										支付通道
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="channel_code"
											name="channel_code" placeholder="请输入支付通道" readonly="readonly" value="WXPAY">
									</div>
								</div>
								
								<div class="form-group">
									<label for="location" class="col-sm-2 control-label">
										开户城市
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="location"
											name="location" placeholder="请输入开户城市" value="">
									</div>
								</div>
								
								<div class="form-group">
									<label for="card_no" class="col-sm-2 control-label">
										卡号
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="card_no"
											name="card_no" placeholder="请输入卡号" value="6226220618766389">
									</div>
								</div>
								
								<div class="form-group">
									<label for="cert_no" class="col-sm-2 control-label">
										身份证号
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="cert_no"
											name="cert_no" placeholder="请输入身份证号" value="500382198408202155">
									</div>
								</div>
								
								<div class="form-group">
									<label for="mobile" class="col-sm-2 control-label">
										开户时绑定手机号
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="mobile"
											name="mobile" placeholder="请输入手机号" value="15907690877">
									</div>
								</div>
								
								
								<div class="form-group">
									<label for="phone" class="col-sm-2 control-label">
										联系电话
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="phone"
											name="phone" placeholder="请输入联系电话" value="15907690877">
									</div>
								</div>
								
								
								<div class="form-group">
									<label for="regionCode" class="col-sm-2 control-label">
										区编码
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="regionCode"
											name="regionCode" placeholder="请输入区编码" value="310113">
									</div>
								</div>
								
								
								<div class="form-group">
									<label for="address" class="col-sm-2 control-label">
										详细地址
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="address"
											name="address" placeholder="请输入详细地址" value="">
									</div>
								</div>
								
								
								<div class="form-group">
									<label for="cert_correct" class="col-sm-2 control-label">
										身份证正面
									</label>
									<div class="col-sm-6">
										<input id="cert_correct" name="cert_correct" type="file">
									</div>
								</div>
								
								<div class="form-group">
									<label for="cert_opposite" class="col-sm-2 control-label">
										身份证背面
									</label>
									<div class="col-sm-6">
										<input id="cert_opposite" name="cert_opposite" type="file">
									</div>
								</div>
								
								<div class="form-group">
									<label for="cert_meet" class="col-sm-2 control-label">
										手持身份证
									</label>
									<div class="col-sm-6">
										<input id="cert_meet" name="cert_meet" type="file">
									</div>
								</div>
								
								<div class="form-group">
									<label for="card_correct" class="col-sm-2 control-label">
										银行卡正面
									</label>
									<div class="col-sm-6">
										<input id="card_correct" name="card_correct" type="file">
									</div>
								</div>
								
								
								<div class="form-group">
									<label for="card_opposite" class="col-sm-2 control-label">
										银行卡背面
									</label>
									<div class="col-sm-6">
										<input id="card_opposite" name="card_opposite" type="file" >
									</div>
								</div>
								
								
								<div class="form-group">
									<label for="bl_img" class="col-sm-2 control-label">
										营业执照
									</label>
									<div class="col-sm-6">
										<input id="bl_img" name="bl_img" type="file">
									</div>
								</div>
								
								
								<div class="form-group">
									<label for="door_img" class="col-sm-2 control-label">
										门头照
									</label>
									<div class="col-sm-6">
										<input id="door_img" name="door_img" type="file">
									</div>
								</div>
								
								<div class="form-group">
									<label for="cashier_img" class="col-sm-2 control-label">
										收银台照
									</label>
									<div class="col-sm-6">
										<input id="cashier_img" name="cashier_img" type="file">
									</div>
								</div>
								
							</form>
						</div>

					</div>
				</div>
			</div>

	<div class="btn-toolbar list-toolbar">
      <button class="btn btn-primary" id="verifyBtn"><i class="fa fa-save"></i> 验卡</button>
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
	
	$(function() {
		$("#loading", window.parent.document).hide();


		SelectOptionsDWR.getComboData('CHANNEL_MCHT_NO_TEMP', function(ret) {

			$("#account").html("");
			
			var mchtInfos = jQuery.parseJSON(ret).data;
			var mchtHtml = "";
			mchtInfos.forEach(function(item,index){
				mchtHtml += "<option value='" + item.valueField + "'>" + item.displayField + "</option>"
			});
			$("#account").html(mchtHtml);
			$("#account").selectpicker('refresh');
      		
		});

		$("#account").change(function(){

			setTimeout(function(){
				$.ajax({
		            cache: true,
		            type: "POST",
		            timeout: 10000,
		            url:"manage/channelQuerySingle?r=" + new Date().getTime() + "&channelId=hfbank&channelMchtNo=" + $("#account").val(),
		            dataType : "text",
		            async: false,
		            error: function(request) {
		            },
		            success: function(returneddata) {
		            	var data = $.parseJSON(returneddata);
		                if('00' == data.code) {
		                	if(data.data != null && data.data.card_name!=undefined)
			                {
		                		$("#real_name").val(data.data.card_name);
			                }
		                	if(data.data != null && data.data.channel_name!=undefined)
			                {
		                		$("#cmer").val(data.data.channel_name);
			                }
		                	if(data.data != null && data.data.bank_card_no!=undefined)
			                {
		                		$("#card_no").val(data.data.bank_card_no);
			                }
		                	if(data.data != null && data.data.identity_no!=undefined)
			                {
		                		$("#cert_no").val(data.data.identity_no);
			                }
		                	if(data.data != null && data.data.phone!=undefined)
			                {
		                		$("#mobile").val(data.data.phone);
			                }
		                	if(data.data != null && data.data.phone!=undefined)
			                {
		                		$("#phone").val(data.data.phone);
			                }
		                	if(data.data != null && data.data.address!=undefined)
			                {
		                		$("#address").val(data.data.address);
			                }
		                 }
		                else {
		                	toastr.error(data.message);
		                }
		            }
		        });
			},300);
		});
		
		function doChannelMchtAdd(){
			
			//$("#loading", window.parent.document).show();
			//toastr.success("商户新增成功!");
			//return ;
			
			$('#channelMchtForm').submit();
			
		}

		$(":file").fileinput({
            language: 'zh', //设置语言
            uploadUrl: "manage/channelFileUpload", //上传的地址
            allowedFileExtensions : ['jpg', 'png','gif'],//接收的文件后缀,
            maxFileSize: 200,  //允许上传最大规格
            uploadAsync: false,//同步上传
            showUpload: false, //是否显示上传按钮
            showCaption: true,//是否显示标题
            showPreview :false,
            browseClass: "btn btn-primary", //按钮样式             
            previewFileIcon: "<i class='glyphicon glyphicon-king'></i>",
            layoutTemplates:{
                //actionDelete:'',去除缩图删除
				//actionUpload: ''去除缩图上传
            },
            uploadExtraData: function(previewId, index) {   //额外参数的关键点
                var obj = {};
                obj.tempMchtNo = $('#channel_mcht_no').val();
                return obj;
            }
        });
		//SelectOptionsDWR.getComboData('CHANNEL_ID', function(ret) {});
		
		$("#resetBtn").click(function(){
			$('#channelMchtForm')[0].reset();
		});
		//保存商户
		$("#verifyBtn").click(function(){

			window.parent.wxc.xcConfirm("确认提交?", window.parent.wxc.xcConfirm.typeEnum.confirm, {
				onOk:function(v){
					doChannelMchtAdd();
				}
			});
		});

		
		
	});
	
	</script>

	</body>
</html>