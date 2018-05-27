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
					报表下载
				</h1>
			</div>

			<div class="row">
				<div class="col-md-8">
					<div id="myTabContent" class="tab-content">
						<div class="tab-pane active in" id="home">

							<form id="downloadForm" name="downloadForm" class="form-horizontal">
								
								<div class="form-group">
									<label class="control-label col-sm-2" for="lunch">
										报表列表
									</label>
									<div class="col-sm-8">
										<select id="path" name="path"
										class="selectpicker" data-live-search="true" title="请选择要下载的报表">

										</select>
									</div>
								</div>
								<div class="form-group">
									<label class="control-label col-sm-2" for="lunch">
										商户编号
									</label>
									<div class="col-sm-8">
										<select id="mchtNo" name="mchtNo" class="selectpicker" data-live-search="true" title="商户编号">
						        
						     		</select>
									</div>
								</div>
								
								
								<div class="form-group">
									<label class="control-label col-sm-2" for="lunch">
										交易日期
									</label>
									<div class="col-sm-8">
										<input id="date" name="date" size="10" type="text" value="" readonly class="form_datetime">
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

		$("#date").datetimepicker( {
			format : 'yyyymmdd',
			minView : 'month',
			language : 'zh-CN',
			autoclose : true,
			forceParse: false,
			todayHighlight : true,
			startDate: getDateAfter(-90,'yyyy-MM-dd'),
			endDate : new Date(),
			initialDate: getDateAfter(-1,'yyyy-MM-dd')
		})
		.val(getDateAfter(-1,'yyyyMMdd'));

		SelectOptionsDWR.getComboData('REPORT_LIST',function(ret) {
			$("#path").html("");
			var mchtInfos = jQuery.parseJSON(ret).data;
			var mchtHtml = "";
			mchtInfos.forEach(function(item,index){
				mchtHtml += "<option value='" + item.displayField + "'>" + item.valueField + "</option>"
			});
			$("#path").html(mchtHtml);
			$("#path").selectpicker('refresh');
		});

		SelectOptionsDWR.getComboData('MCHT_INFO', function(ret) {

			$("#mchtNo").html("");
			var mchtHtml = "";
			var mchtInfos = jQuery.parseJSON(ret).data;
			//var mchtInfos = ret.data;
			mchtInfos.forEach(function(item,index){
				mchtHtml += "<option value='" + item.valueField + "'>" + item.displayField + "</option>"
			});
			$("#mchtNo").html(mchtHtml);
			$(".selectpicker").selectpicker('refresh');

		});
		//SelectOptionsDWR.getComboData('CHANNEL_ID', function(ret) {});
		
		$("#resetBtn").click(function(){
			$('#channelMchtForm')[0].reset();
		});
		//下载
		$("#downloadBtn").click(function(){
			var path = $("#path").val();
			var date = $("#date").val();
			var mchtNo = $("#mchtNo").val();
			if(path==""){
				toastr.error("请选择要下载的文档");
				return ;
			}
			if(date==""){
				toastr.error("请选择交易日期");
				return ;
			}

			$("#loading", window.parent.document).show();
			setTimeout(function(){
				$.ajax({
		            cache: true,
		            type: "POST",
		            timeout: 10000,
		            url:"manage/getReportPath?r=" + new Date().getTime(),
		            data:$('#downloadForm').serialize(),// 你的formid
		            dataType : "text",
		            async: false,
		            error: function(request) {
						$("#loading", window.parent.document).hide();
		                alert("Connection error");
		            },
		            success: function(returneddata) {
		            	$("#loading", window.parent.document).hide();
		            	var data = $.parseJSON(returneddata);
		                if('00' == data.code) {
		                	window.location.href = "<%= request.getContextPath()%>" + '/manage/ajaxDownLoad?path='+ data.data ;
		                 }
		                else {
		                	toastr.error(data.message);
		                }
		            }
		        });
			},300);
			
			
		});

		
		
	});
	
	</script>

	</body>
</html>