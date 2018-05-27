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

  //初始化toastr.js
    toastr.options = {positionClass:'toast-top-center', timeOut: "2000"};
    </script>
	</head>
	<body onload="IFrameResize()" class=" theme-blue">

		<div >
			<div class="header">
				<h1 class="page-title">
					通道商户注册
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
										<select id="agtId" name="agtId"
										class="selectpicker" data-live-search="true" title="请选择通道机构号">
											<option value='714974' selected="selected">恒丰银行机构号-714974</option>
										</select>
										
									</div>
								</div>
								<div class="form-group">
									<label for="channel_mcht_no" class="col-sm-2 control-label">
										通道商户编号
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="channel_mcht_no" maxlength="11" onkeyup="value=value.replace(/[^\d]/g,'')"
											name="channel_mcht_no" placeholder="请输入通道商户编号(手机号)">
									</div>
								</div>
								
								<div class="form-group">
									<label for="secret_key" class="col-sm-2 control-label">
										密码
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="pass_word" maxlength="10" onkeyup="value=value.replace(/[^\da-zA-Z]/g,'')"
											name="pass_word" placeholder="请输入密码(6-10位英文或数字)">
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


		function doChannelMchtAdd(){
			
			$("#loading", window.parent.document).show();
			//toastr.success("商户新增成功!");
			//return ;
			setTimeout(function(){
				$.ajax({
		            cache: true,
		            type: "POST",
		            timeout: 30000,
		            url:"manage/channelMchtRegister?r=" + new Date().getTime(),
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
			            	toastr.success("通道商户注册成功!");
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