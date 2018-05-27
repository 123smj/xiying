<%@ page language="java" import="java.util.*,com.trade.bean.own.MerchantInf,com.gy.util.CommonFunction" pageEncoding="UTF-8"%>
<%@page import="com.manage.bean.PageModle"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@page import="com.manage.bean.OprInfo,com.trade.bean.*,com.gy.util.*"%>
<%@ include file="include.jsp"%>
<%
OprInfo companyInfo = (OprInfo)request.getAttribute("companyInfo");
RateInfo companyRate = (RateInfo)request.getAttribute("companyRate");
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

	<%
	if(companyInfo == null ) {
	    companyInfo = new OprInfo();
	%>
		alert("加载公司信息异常");
	<%
	}
	if(companyRate == null ) {
	    companyRate = new RateInfo();
	%>
	alert("加载费率信息异常");
	<%
	}
	%>
    </script>
	</head>

	<body onload="IFrameResize()" class=" theme-blue">
	
		<div >
			<div class="header">
				<h1 class="page-title">
					公司修改
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
										<input type="text" class="form-control" id="company_id" maxlength="10"
											name="company_id" placeholder="请输入6-10位英文字母或数字" readonly value="<%=StringUtil.trans2Str(companyInfo.getCompany_id()) %>">
									</div>
								</div>
								
								<div class="form-group">
									<label for="phone" class="col-sm-2 control-label">
										公司名称
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="company_name" maxlength="32"
											name="company_name" placeholder="公司名称" value="<%=StringUtil.trans2Str(companyInfo.getCompany_name()) %>">
									</div>
								</div>
								
								<div class="form-group">
									<label for="connact_name" class="col-sm-2 control-label">
										联系人姓名
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="connact_name" autocomplete="off" maxlength="32"
											name="connact_name" placeholder="请输入联系人姓名" value="<%=StringUtil.trans2Str(companyInfo.getConnact_name()) %>">
									</div>
								</div>
								<div class="form-group">
									<label for="identify_no" class="col-sm-2 control-label">
										联系人身份证
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="identify_no" autocomplete="off" 
											name="identify_no" placeholder="请输入联系人身份证" value="<%=StringUtil.trans2Str(companyInfo.getIdentify_no()) %>">
									</div>
								</div>
								
								<div class="form-group">
									<label for="opr_mobile" class="col-sm-2 control-label">
										联系人手机号
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="opr_mobile" autocomplete="off" maxlength="15"
											name="opr_mobile" placeholder="请输入联系人手机号" value="<%=StringUtil.trans2Str(companyInfo.getOpr_mobile()) %>">
									</div>
								</div>
								
								<div class="form-group">
									<label for="address" class="col-sm-2 control-label">
										地址
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="address" autocomplete="off" maxlength="128"
											name="address" placeholder="请输入地址" value="<%=StringUtil.trans2Str(companyInfo.getAddress()) %>">
									</div>
								</div>
								
								<div class="form-group">
									<label for="wechat_fee_value" class="col-sm-2 control-label">
										微信费率
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="wechat_fee_value" onkeyup="value=value.replace(/[^\d.]/g,'')"
											name="wechat_fee_value" placeholder="请输入微信费率(%)" value="<%=StringUtil.trans2Str(companyRate.getWechat_fee_value()) %>">
									</div>
								</div>
								<div class="form-group">
									<label for="alipay_fee_value" class="col-sm-2 control-label">
										支付宝费率
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="alipay_fee_value" onkeyup="value=value.replace(/[^\d.]/g,'')"
											name="alipay_fee_value" placeholder="请输入支付宝费率(%)" value="<%=StringUtil.trans2Str(companyRate.getAlipay_fee_value()) %>">
									</div>
								</div>
								<div class="form-group">
									<label for="alipay_fee_value" class="col-sm-2 control-label">
										QQ钱包费率
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="qq_fee_value" onkeyup="value=value.replace(/[^\d.]/g,'')"
											name="qq_fee_value" placeholder="请输入QQ钱包费率(%)" value="<%=StringUtil.trans2Str(companyRate.getQq_fee_value()) %>">
									</div>
								</div>
								<div class="form-group">
									<label for="quickpay_fee_value" class="col-sm-2 control-label">
										快捷支付费率
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="quickpay_fee_value" onkeyup="value=value.replace(/[^\d.]/g,'')"
											name="quickpay_fee_value" placeholder="请输入快捷支付费率(%)" value="<%=StringUtil.trans2Str(companyRate.getQuickpay_fee_value()) %>">
									</div>
								</div>
								<div class="form-group">
									<label for="netpay_fee_value" class="col-sm-2 control-label">
										网银支付费率
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="netpay_fee_value" onkeyup="value=value.replace(/[^\d.]/g,'')"
											name="netpay_fee_value" placeholder="请输入网银支付费率(%)" value="<%=StringUtil.trans2Str(companyRate.getNetpay_fee_value()) %>">
									</div>
								</div>
								<div class="form-group">
									<label for="single_extra_fee" class="col-sm-2 control-label">
										单笔代付费/分
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="single_extra_fee" onkeyup="value=value.replace(/[^\d.]/g,'')"
											name="single_extra_fee" placeholder="请输入单笔代付手续费(分)" value="<%=StringUtil.trans2Str(companyRate.getSingle_extra_fee()) %>">
									</div>
								</div>
							</form>
						</div>

					</div>
				</div>
			</div>

	<div class="btn-toolbar list-toolbar">
      <button class="btn btn-primary" id="saveMchtBtn"><i class="fa fa-save"></i> 保存</button>
      <a  data-toggle="modal" class="btn btn-danger" id="backBtn">返回</a>
      
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


		function doCompanyUpdate(){
			var uid = $("#identify_no").val();
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
		            url:"manage/companyUpdate?r=" + new Date().getTime(),
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
			            	toastr.success("公司修改成功!");
				        }
			            else {
			            	toastr.error(data);
				        }
		            }
		        });
			},300);
		}
		//SelectOptionsDWR.getComboData('CHANNEL_ID', function(ret) {});
		
		$("#backBtn").click(function(){
			history.back();
		});
		//保存商户
		$("#saveMchtBtn").click(function(){
			window.parent.wxc.xcConfirm("确认保存公司信息?", window.parent.wxc.xcConfirm.typeEnum.confirm, {
				onOk:function(v){
				doCompanyUpdate();
			}
		});
		});

		
	});
	
	</script>

	</body>
	
</html>