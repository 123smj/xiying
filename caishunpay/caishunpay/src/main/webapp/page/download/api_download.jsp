<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.manage.bean.PageModle"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@page import="com.manage.bean.OprInfo,com.trade.bean.*,com.gy.util.*"%>
<%@ include file="../../manage/include.jsp"%>

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

    toastr.options = {positionClass:'toast-top-center', timeOut: "2000"};
    </script>
	</head>
	<body onload="IFrameResize()" class=" theme-blue">

		<div >
			<div class="header">
				<h1 class="page-title">
					文档下载
				</h1>
			</div>

			<div class="row">
				<div class="col-md-8">
					<div id="myTabContent" class="tab-content">
						<div class="tab-pane active in" id="home">

							<form id="channelMchtForm" name="channelMchtForm" class="form-horizontal">
								
								<div class="form-group">
									<label class="control-label col-sm-2" for="lunch">
										文档列表
									</label>
									<div class="col-sm-8">
										<select id="api_list" name="api_list"
										class="selectpicker" data-live-search="true" title="请选择要下载的文档">

										</select>
									</div>
									
								</div>
								
							</form>
						</div>

					</div>
				</div>
			</div>

	<div class="btn-toolbar list-toolbar" style="margin-left: 30px">
      <button class="btn btn-primary" id="downloadBtn"><i class="fa fa-save">下载</i></button>
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

		SelectOptionsDWR.getComboData('API_LIST',function(ret) {
			$("#api_list").html("");
			var mchtInfos = jQuery.parseJSON(ret).data;
			var mchtHtml = "";
			mchtInfos.forEach(function(item,index){
				mchtHtml += "<option value='" + item.displayField + "'>" + item.valueField + "</option>"
			});
			$("#api_list").html(mchtHtml);
			$("#api_list").selectpicker('refresh');
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
		//下载
		$("#downloadBtn").click(function(){
			var data = $("#api_list").val();
			if(data==""){
				toastr.error("请选择要下载的文档");
				return ;
			}
			window.location.href = "<%= request.getContextPath()%>" + "/manage/ajaxDownLoad?path=" + data ;
		});

		
		
	});
	
	</script>

	</body>
</html>