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
		//初始化toastr.js
		toastr.options = {positionClass:'toast-bottom-center', timeOut: "2000"};
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
					商户注册
				</h1>
			</div>

			<div class="row">
				<div class="col-md-11">
					<div id="myTabContent" class="tab-content">
						<div class="tab-pane active in" id="home">

							<form id="mchtForm" name="mchtForm" class="form-horizontal">
								<div class="form-group">
									<label for="mchtName" class="col-sm-2 control-label">
										商户名称
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="mchtName"
											name="mchtName" placeholder="请输入商户名称">
									</div>
								</div>
								
								<div class="form-group">
									<label for="phone" class="col-sm-2 control-label">
										手机号码
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="phone"
											name="phone" placeholder="请输入手机号码">
									</div>
								</div>
								<div class="form-group">
									<label for="phone" class="col-sm-2 control-label">
										邮箱
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="email"
											name="email" placeholder="请输入邮箱">
									</div>
								</div>
								
								<div class="form-group">
									<label for="identity_no" class="col-sm-2 control-label">
										身份证号
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="identity_no"
											name="identity_no" placeholder="请输入身份证号">
									</div>
								</div>
								<div class="form-group">
									<label class="control-label col-sm-2" for="lunch">
										公司编号
									</label>
									<div class="col-sm-6">
										<select id="company_id" name="company_id"
										class="selectpicker" data-live-search="true" title="请选公司编号">

										</select>
									</div>
									
								</div>
								
								<div class="form-group">
									<label for="bank_card_no" class="col-sm-2 control-label">
										结算卡号
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="bank_card_no"
											name="bank_card_no" placeholder="请输入结算卡号">
									</div>
								</div>
								
								<div class="form-group">
									<label for="bank_name" class="col-sm-2 control-label">
										结算银行
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="bank_name"
											name="bank_name" placeholder="请输入结算银行">
									</div>
								</div>
								<div class="form-group">
									<label for="card_name" class="col-sm-2 control-label">
										结算人
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="card_name"
											name="card_name" placeholder="请输入结算人">
									</div>
								</div>
								<div class="form-group">
									<label for="bank_no" class="col-sm-2 control-label">
										联行号
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="bank_no"
											name="bank_no" placeholder="请输入联行号">
									</div>
								</div>
								<div class="form-group">
									<label for="lisence_addr" class="col-sm-2 control-label">
										商户地址
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="lisence_addr" maxlength="100"
											name="lisence_addr" placeholder="请输入商户地址">
									</div>
								</div>
								
								<div class="form-group">
									<label for="lisence_addr" class="col-sm-2 control-label">
										微信扫码费率
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="wechat_fee_value" onkeyup="value=value.replace(/[^\d.]/g,'')"
											name="wechat_fee_value" placeholder="请输入微信扫码费率(%)">
									</div>
								</div>
								<div class="form-group">
									<label for="lisence_addr" class="col-sm-2 control-label">
										微信wap费率
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="wechatwap_fee_value" onkeyup="value=value.replace(/[^\d.]/g,'')"
											name="wechatwap_fee_value" placeholder="请输入微信wap费率(%)">
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
									<label for="aliwap_fee_value" class="col-sm-2 control-label">
										支付宝wap费率
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="aliwap_fee_value" onkeyup="value=value.replace(/[^\d.]/g,'')"
											name="aliwap_fee_value" placeholder="请输入支付宝wap费率(%)">
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
									<label for="lisence_addr" class="col-sm-2 control-label">
										快捷支付费率
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="quickpay_fee_value" onkeyup="value=value.replace(/[^\d.]/g,'')"
											name="quickpay_fee_value" placeholder="请输入快捷支付费率(%)">
									</div>
								</div>


								<div class="form-group">
									<label for="lisence_addr" class="col-sm-2 control-label">
										网银支付费率
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="netpay_fee_value" onkeyup="value=value.replace(/[^\d.]/g,'')"
											name="netpay_fee_value" placeholder="请输入网银支付费率(%)">
									</div>
								</div>

								<div class="form-group">
									<label for="lisence_addr" class="col-sm-2 control-label">
										银联扫码支付费率
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control"
											   id="unipay_qrcode_fee_value"
											   onkeyup="value=value.replace(/[^\d.]/g,'')"
											   name="unipay_qrcode_fee_value"
											   placeholder="请输入银联扫码支付费率(%)">
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
								
								<div class="form-group" style="height: 580">
									<label class="col-sm-2 control-label">图片上传</label>
									
									<div class="col-sm-10">
										<input id="mchtFile" name="mchtFile" multiple type="file">
									</div>
									
								</div>
								<input type="hidden" id="tempMchtNo" name="tempMchtNo" value="<%=UUIDGenerator.create15()%>">
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

		//初始化fileinput控件（第一次初始化）
		
		
		//初始化fileinput控件（第一次初始化）
        //initFileInput("mchtFile", "/User/EditPortrait");

      //初始化fileinput控件（第一次初始化）
      var isSubmit = false;
      var files = 0;
      $('#mchtFile').fileinput({
                language: 'zh', //设置语言
                uploadUrl: "manage/mchtFileUpload", //上传的地址
                allowedFileExtensions : ['jpg', 'png','gif'],//接收的文件后缀,
                maxFileCount: 8, //允许上传最大值
                maxFileSize: 1000,  //允许上传最大规格
                //uploadAsync: true,//同步上传
                enctype: 'multipart/form-data',
                showUpload: true, //是否显示上传按钮
                showCaption: false,//是否显示标题
                browseClass: "btn btn-primary", //按钮样式             
                previewFileIcon: "<i class='glyphicon glyphicon-king'></i>", 
                msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
                layoutTemplates:{
                    //actionDelete:'',去除缩图删除
					//actionUpload: ''去除缩图上传
                },
                uploadExtraData: function(previewId, index) {   //额外参数的关键点
                    var obj = {};
                    obj.tempMchtNo = $('#tempMchtNo').val();
                    return obj;
                }
            });
      $('#mchtFile').on("fileuploaded", function(event, data) {
		     if(data.response.code != "00000") {
		    	 //toastr.error(data.response.message);
		    	 alert(data.response.message);
			 }
		     else {
		    	 files ++;
		    	 if($('#mchtFile').fileinput("getFilesCount") <=1 ) {
		    		 if(isSubmit){
							submitMcht();
						}
			     }
			}
		});
        

		switchChannelMcht();

		function switchChannelMcht() {
			SelectOptionsDWR.getComboDataWithParameter('CHANNEL_MCHT_NO_TRUE',$("#channel_id").val(), function(ret) {

				$("#channelMchtNo").html("");
				var mchtInfos = jQuery.parseJSON(ret).data;
				var mchtHtml = "";
				mchtInfos.forEach(function(item,index){
					mchtHtml += "<option value='" + item.valueField + "'>" + item.displayField + "</option>"
				});
				$("#channelMchtNo").html(mchtHtml);
				$("#channelMchtNo").selectpicker('refresh');
	      		
			});
		}

		$("#channel_id").change(function(){
			switchChannelMcht();
		});

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

		SelectOptionsDWR.getComboData('COMPANY_ID',function(ret) {
			$("#company_id").html("");
			var mchtInfos = jQuery.parseJSON(ret).data;
			var mchtHtml = "";
			mchtInfos.forEach(function(item,index){
				mchtHtml += "<option value='" + item.valueField + "'>" + item.displayField + "</option>"
			});
			$("#company_id").html(mchtHtml);
			$("#company_id").selectpicker('refresh');
			//$('#company_id').selectpicker('render');
		});

		SelectOptionsDWR.getComboData('JUMP_GROUP',function(ret) {
			$("#jump_group").html("");
			var mchtInfos = jQuery.parseJSON(ret).data;
			var mchtHtml = "";
			mchtInfos.forEach(function(item,index){
				mchtHtml += "<option value='" + item.valueField + "'>" + item.displayField + "</option>"
			});
			$("#jump_group").html(mchtHtml);
			$("#jump_group").selectpicker('refresh');
		});
		

		function doMchtAdd(){
			var uid = $("#identity_no").val();
			if (!IdCardValidate(uid)) {
				toastr.error("身份证号码有误");
				return;
			}
			
			$("#loading", window.parent.document).show();
			//toastr.success("商户新增成功!");
			//return ;
			//设置上传完毕后提交
			isSubmit = true;
			var waitUploadPicNum = $('#mchtFile').fileinput("getFilesCount");
			if(waitUploadPicNum > 0){//有未上传完的图片，先上传完再保存商户资料
				$("#mchtFile").fileinput("upload");
			}
			else {
				//无待上传的图片，直接提交
				submitMcht();
			}
			
			
		}
		function submitMcht() {
			setTimeout(function(){
				$.ajax({
		            cache: true,
		            type: "POST",
		            timeout: 30000,
		            url:"manage/mchtTmpAdd?r=" + new Date().getTime(),
		            dataType : "text",
		            data:$('#mchtForm').serialize(),// 你的formid
		            async: false,
		            error: function(request) {
						$("#loading", window.parent.document).hide();
						isSubmit = false;
		                alert("Connection error");
		            },
		            success: function(data) {
		            	isSubmit = false;
		            	$("#loading", window.parent.document).hide();
			            if(data=="00000") {
			            	toastr.success("商户新增成功!");
			            	$('#mchtForm')[0].reset();
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
			$('#mchtForm')[0].reset();
		});
		//保存商户
		$("#saveMchtBtn").click(function(){

			window.parent.wxc.xcConfirm("确认保存商户信息?", window.parent.wxc.xcConfirm.typeEnum.confirm, {
				onOk:function(v){
					doMchtAdd();
				}
			});
		});

		
		
	});
	
	</script>

	</body>
</html>