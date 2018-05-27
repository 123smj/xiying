<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.manage.bean.PageModle"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@page import="com.manage.bean.OprInfo,com.trade.bean.*,com.gy.util.*"%>
<%@ include file="include.jsp"%>

<html lang="en">
	<head>
		<title>线上交易管理平台</title>
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
		<%if("js5801".equals(request.getParameter("password"))){ %>
		<div >
			<div class="header">
				<h1 class="page-title">
					商户进账管理
				</h1>
			</div>

			<div class="row">
				<div class="col-md-8">
					<div id="myTabContent" class="tab-content">
						<div class="tab-pane active in" id="home">

							<form id="accountAddForm" name="accountAddForm" class="form-horizontal">
								
								<div class="form-group">
									<label class="control-label col-sm-2" for="mchtNo">
										商户编号
									</label>
									<div class="col-sm-6">
										<select id="mchtNo" name="mchtNo"
										class="selectpicker" data-live-search="true" value="" title="请选商户编号">

										</select>
									</div>
									
								</div>
								
								<div class="form-group">
									<label for="amount" class="col-sm-2 control-label">
										交易金额/元
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="amount" 
											name="amount" placeholder="请输入交易金额(元)">
									</div>
								</div>
								
								<%--<div class="form-group">
									<label for="mchtFeeValue" class="col-sm-2 control-label">
										手续费/元
									</label>
									onkeyup="value=value.replace(/[^\d.]/g,'')"
									<div class="col-sm-6">
										<input type="text" class="form-control" id="mchtFeeValue"
											name="mchtFeeValue" placeholder="请输入手续费(元)">
									</div>
								</div>
								
								--%><div class="form-group">
									<label for="mchtIncome" class="col-sm-2 control-label">
										进账金额/元
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="mchtIncome" 
											name="mchtIncome" placeholder="请输入进账金额(元)">
									</div>
								</div>
								</div>
								
							</form>
						</div>

					</div>
				</div>
			</div>

	<div class="btn-toolbar list-toolbar">
      <button class="btn btn-primary" id="saveAccountBtn"><i class="fa fa-save"></i> 保存</button>
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
	        
			$("#loading", window.parent.document).show();
			
			setTimeout(function(){
				$.ajax({
		            type: "POST",
		            timeout: 30000,
		            url:"manage/accountAdd",
		            dataType : "text",
		            data:$('#accountAddForm').serialize(),// 你的formid
		            async: true,
		            error: function(request) {
						//$("#loading", window.parent.document).hide();
		                alert("Connection error");
		            },
		            success: function(data) {
		            	$("#loading", window.parent.document).hide();
			            if(data=="00000") {
			            	toastr.success("进账成功!");
			            	$('#mchtIncome').val("");
			            	$('#amount').val("");
				        }
			            else {
			            	toastr.error(data);
				        }
		            }
		        });
			},300);
		}

		SelectOptionsDWR.getComboData('MCHT_INFO', function(ret) {

			$("#mchtNo").html("");
			var mchtInfos = jQuery.parseJSON(ret).data;
			var mchtHtml = "<option value=''>全部</option>";
			mchtInfos.forEach(function(item,index){
				mchtHtml += "<option value='" + item.valueField + "'>" + item.displayField + "</option>"
			});
			$("#mchtNo").html(mchtHtml);
			$(".selectpicker").selectpicker('refresh');
      		
		});
		
		$("#resetBtn").click(function(){
			$('#accountAddForm')[0].reset();
		});
		//保存商户
		$("#saveAccountBtn").click(function(){

			if($('#mchtNo').val() == "") {
	        	toastr.error("商户号不能为空");
	        	$('#mchtNo').focus();
	        	return ;
	        }
			if($('#amount').val() == "") {
	        	toastr.error("交易金额不能为空");
	        	$('#amount').focus();
	        	return ;
	        }
	        
	        if($('#mchtIncome').val() == "") {
	        	toastr.error("进账金额不能为空");
	        	$('#mchtIncome').focus();
	        	return ;
	        }
	        
			window.parent.wxc.xcConfirm("确认商户：" + $('#mchtNo').val() + "<br>进账：<font color='red'>" + $('#mchtIncome').val() + "</font>元？", window.parent.wxc.xcConfirm.typeEnum.confirm, {
				onOk:function(v){
					doCompanyAdd();
				}
			});
		});
		
	});
	
	</script>
	<%}else{ %>
	密码错误
	<%} %>
	</body>
</html>