<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.manage.bean.PageModle"%>
<%@page import="com.gy.system.SysParamUtil"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page import="com.manage.bean.OprInfo,com.trade.bean.*,com.gy.util.*"%>
<%@ include file="include.jsp" %>

<html lang="en"><head>
    <title>线上交易管理平台</title>
    <script type="text/javascript">
    toastr.options = {positionClass:'toast-top-center', timeOut: "2000"};
    
    function IFrameResize(){ 
    	//alert(this.document.body.scrollHeight); //弹出当前页面的高度 
    	var obj = parent.document.getElementById("main_iframe"); //取得父页面IFrame对象 
    	//alert(obj.height); //弹出父页面中IFrame中设置的高度 
    	obj.height = this.document.body.scrollHeight; //调整父页面中IFrame的高度为此页面的高度 
    	} 
	
    </script>
</head>
<body  onload="IFrameResize()" class=" theme-blue" >
	<div>
			<div class="header">
				<h1 class="page-title">
					账户额度设置
				</h1>
			</div>
			
			<%--<iframe width="100%" style="border: 0" height="900px" scrolling="no" src="<%= request.getContextPath()%>/manage/tradeInfo.jsp"></iframe>
			
		--%>
		</div>
		<div class="search-well">
                <form class="form-inline" id="account_query_form" name="account_query_form" style="margin-top:0px;margin-left: 10px" action="manage/accountFileQuery" target="_self">
                	<table >
                		<tr>
                		<td><label>上传时间：</label></td>
                			<td><input id="time_start_begin" name="time_start_begin" size="10" type="text" value="" readonly class="form_datetime">
                			- <input id="time_start_end" name="time_start_end" size="10" type="text" value="" readonly class="form_datetime">
                			</td>
                		<td>
            				<label>批次号：</label></td><td>
                    		<input name="offerSeq" id="offerSeq" class="input-xlarge form-control" placeholder="批次号"  type="text">
          				</td>
          				<td><label>状态：</label></td><td>
					            <select name="status" id="status" class="form-control">
					              <option value="">全部</option>
					              <option value="01">新增待审核</option>
					              <option value="00">正常</option>
					              <option value="09">审核拒绝</option>
					            </select></td>
                		<td><input type="hidden" id="pageNum" name="pageNum" value="1"></td><td colspan="3" align="right">
                    		<button class="btn btn-default" type="button" id="trade_query_btn" name="trade_query_btn"><i class="fa fa-search"></i> 查询</button>
                    	</td>
                    	</tr>
                	</table>
                	
                </form>
            </div>
            
			<div class="main-content">
				<div class="btn-toolbar list-toolbar">
					<button class="btn btn-primary" id="exportBtn">
						<i class="fa fa-plus"></i>导出
					</button>
					<button class="btn btn-default" id="fileUploadBtn">
						账目文件上传
					</button>
					<div class="btn-group">
					</div>
				</div>
				
			</div>
			
				<div style="width: 99%; overflow-x: scroll;">
					<table class="table" width="1500px" height="auto">
						<thead style="height: 20px">
							<tr style="height: 20px">
								<th style="width: 40px">
									#
								</th>
								<th>
									<span style="width: 70px; display: block;">批次号</span>
								</th>
								<th>
									<span style="width: 160px; display: inline-block; overflow: hidden">账目文件名</span>
								</th>
								<th>
									<span style="width: 100px; display: block;">操作时间</span>
								</th>
								<th>
									<span style="width: 100px; display: block;">操作员</span>
								</th>
								<th>
									<span style="width: 100px; display: block;">状态</span>
								</th>
								<th>
									<span style="width: 90px; display: block;">审核人</span>
								</th>
								<th>
									<span style="width: 90px; display: block;">审核时间</span>
								</th>
								<th>
									<span style="width: 90px; display: block;">备注</span>
								</th>
								</tr>
						</thead>

						<tbody id="mcht_tbody">
							
						</tbody>
					</table>
				</div>
				
				<ul class="pager">  
          
        <li id="totalNum">总记录条</li>  
        <li>  </li>  
            <li id="firstPage">  
                <a href="javascript:void(0);" onclick="accountFileQuery('1')">首页</a>  
            </li>  
            <li >  
                <a id="prePage" href="javascript:void(0);" >上一页</a>  
            </li>  
            <li >  
                <a id="nextPage" href="javascript:void(0);" >下一页</a>  
            </li>  
            <li >  
                <a id="lastPage" href="javascript:void(0);" >尾页</a>  
            </li>  
              
            <li id="currentAndTotal">当前页/</li>  
              
            <li></li>  
            <li><select id="xzPage" name="xzPage" >  
             
            </select></li>  
              
            <li><a href="javascript:void(0);" onclick="accountFileQuery(document.getElementById('xzPage').value);">go</a></li>  
        </ul>

		<div class="modal fade" id="selectDeliverModel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static">
			<div class="modal-dialog" style="width: 800px;">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
							&times;
						</button>
						<h4 class="modal-title" id="myModalLabel">
							账目文件上传
						</h4>
					</div>
					<div class="modal-body">
						<form id="accountFileForm" name="accountFileForm" class="form-horizontal" method="post" enctype ="multipart/form-data">
						
							<div class="form-group">
									<label class="col-sm-2 control-label">
										账目文件
									</label>
									<div class="col-sm-6">
										<input id="accountFile" name="accountFile" type="file">
									</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">
										
								</label>
								<div class="col-sm-6">
									<button class="btn btn-primary" type="button" id="file_submit_btn" name="file_submit_btn"><i class="fa"></i> 提交</button>
								</div>
							</div>	
						</form>
					</div>
				</div>
			</div>
		</div>		
	<script type="text/javascript">
	//交易查询
	function accountFileQuery(num){
		if(num != 'undefined'){
			$("#pageNum").val(num);
		}
		$.ajax({
            cache: true,
            type: "POST",
            url:"manage/accountFileQuery?r=" + new Date().getTime(),
            data:$('#account_query_form').serialize(),// 你的formid
            async: false,
            error: function(request) {
				$("#loading", window.parent.document).hide();
                alert("Connection error");
            },
            success: function(data) {
            	$("#loading", window.parent.document).hide();
            	if(data==""){
                	return;
                }
	            $("#totalNum").html("总记录" + data.totalNum + "条");
	            var prePage = data.currentPage <=1 ? 1: (Number(data.currentPage) -1);
	            $("#prePage").attr("onclick","accountFileQuery('" + prePage +"')");
	            var next = data.currentPage >=data.totalPage ? data.currentPage: (Number(data.currentPage) + 1);
	            $("#nextPage").attr("onclick","accountFileQuery('" + next + "')");
	            $("#lastPage").attr("onclick","accountFileQuery('" + data.totalPage + "')");
	            $("#currentAndTotal").html(data.currentPage + "/" + data.totalPage);
	            var pageSelect;
	            for(var p = 1; p<=data.totalPage; p++){
					if(p == data.currentPage){
						pageSelect += '<option selected>' + p + '</option>';
					}else{
						pageSelect += '<option>' + p + '</option>';
					}
		        }
	            $("#xzPage").html(pageSelect);
	           
                $("#mcht_tbody").html("");
                var html = "";

                for(var i=0; i<data.data.length; i++){
                	var item = data.data[i];
                	if(i%2 == 0) {
                		html += '<tr class="active">';
                	}
                	else {
                		html += '<tr>';
                	}
                	html = html +  '<td>' + (i+1+(Number(data.currentPage)-1)*10) + '</td><td>' + 
                	item.offerSeq + '</td><td>' +
                	'<span class="span_auto_width"><a class="text_green" href="<%= request.getContextPath()%>/manage/ajaxDownLoad?path=<%= SysParamUtil.getParam("mcht_account_file")%>' + item.offerTime.substring(0,8) + "/" + item.offerDoc + '">' + item.offerDoc + '</a></span></td><td>' +
                	item.oprTime + '</td><td>' +
                	'<span class="span_auto_width">' + item.oprId + '</span></td><td>' +
                	mchtStatusTrans(item.status) + '</td><td>' +
                	'<span class="span_auto_width">' + item.checkOpr + '</span></td><td>' + 
                	'<span class="span_auto_width">' + item.checkTime + '</span></td><td>' + 
                	'<span class="span_auto_width text_red" >' + item.description + '</span></td>';
                	
                	html += '</tr>';
                }
                $("#mcht_tbody").html(html);
                
                }
				
        });
	}

	//商户导出
	function tradeExport(){

		toastr.error("亲，暂不支持导出");
	}

	$(":file").fileinput({
        language: 'zh', //设置语言
        uploadUrl: "manage/accountFileUpload", //上传的地址
        allowedFileExtensions : ['xls','xlsx'],//接收的文件后缀,
        maxFileSize: 200,  //允许上传最大规格
        uploadAsync: true,//同步上传
        showUpload: false, //是否显示上传按钮
        showCaption: true,//是否显示标题
        showPreview :false,
        browseClass: "btn btn-primary", //按钮样式             
        previewFileIcon: "<i class='glyphicon glyphicon-king'></i>",
        layoutTemplates:{
            //actionDelete:'',去除缩图删除
			//actionUpload: ''去除缩图上传
        }
        //uploadExtraData: function(previewId, index) {   //额外参数的关键点
            //var obj = {};
            //obj.tempMchtNo = $('#channel_mcht_no').val();
            //return obj;
        //}
    });


	switchChannelMcht();

	function switchChannelMcht() {
		SelectOptionsDWR.getComboDataWithParameter('CHANNEL_MCHT_NO',$("#channel_id").val(), function(ret) {

			$("#channel_mcht_no").html("");
			var mchtInfos = jQuery.parseJSON(ret).data;
			var mchtHtml = "<option value=''>全部</option>";
			mchtInfos.forEach(function(item,index){
				mchtHtml += "<option value='" + item.valueField + "'>" + item.displayField + "</option>"
			});
			$("#channel_mcht_no").html(mchtHtml);
			$("#channel_mcht_no").selectpicker('refresh');
      		
		});
	}

	$("#channel_id").change(function(){
		switchChannelMcht();
	});
	
	SelectOptionsDWR.getComboData('CHANNEL_ID',function(ret) {
		$("#channel_id").html("");
		var mchtInfos = jQuery.parseJSON(ret).data;
		var mchtHtml = "<option value=''>全部</option>";
		mchtInfos.forEach(function(item,index){
			mchtHtml += "<option value='" + item.valueField + "'>" + item.displayField + "</option>"
		});
		$("#channel_id").html(mchtHtml);
		$("#channel_id").selectpicker('refresh');
	});
	
	function channelMchtFreeze(channel_mcht_no, channel_id){

		window.parent.wxc.xcConfirm("确认冻结通道商户?", window.parent.wxc.xcConfirm.typeEnum.confirm, {
			onOk:function(v){
			$.ajax({
	            cache: true,
	            type: "POST",
	            timeout: 10000,
	            url:"manage/channelMchtFreeze?r=" + new Date().getTime(),
	            data:{"channel_mcht_no": channel_mcht_no, "channel_id": channel_id},
	            async: true,
	            error: function(request) {
					//$("#loading", window.parent.document).hide();
	                alert("Connection error");
	            },
	            success: function(data) {
	            	if(data=="00000") {
	            		toastr.success("通道商户冻结成功!");
	            		accountFileQuery();
			        }
		            else {
		            	toastr.error(data);
			        }
	            }
	        });
			}
		});
	}

	function channelMchtRecover(channel_mcht_no, channel_id){

		window.parent.wxc.xcConfirm("确认恢复通道商户?", window.parent.wxc.xcConfirm.typeEnum.confirm, {
			onOk:function(v){
			$.ajax({
	            cache: true,
	            type: "POST",
	            timeout: 10000,
	            url:"manage/channelMchtRecover?r=" + new Date().getTime(),
	            data:{"channel_mcht_no": channel_mcht_no, "channel_id": channel_id},
	            async: true,
	            error: function(request) {
					//$("#loading", window.parent.document).hide();
	                alert("Connection error");
	            },
	            success: function(data) {
	            	if(data=="00000") {
	            		toastr.success("通道商户恢复成功!");
	            		accountFileQuery();
			        }
		            else {
		            	toastr.error(data);
			        }
	            	
	            }
	        });
			}
		});
	}

	
	$(function() {
		$("#loading", window.parent.document).hide();

		$("#time_start_begin").datetimepicker( {
			format : 'yyyymmdd',
			minView : 'month',
			language : 'zh-CN',
			autoclose : true,
			forceParse: false,
			todayHighlight : true,
			startDate: getDateAfter(-120,'yyyy-MM-dd'),
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
			startDate: getDateAfter(-120,'yyyy-MM-dd'),
			endDate : new Date(),
			initialDate: getDateAfter(0,'yyyy-MM-dd')
		})
		.val(getDateAfter(0,'yyyyMMdd'));
		
		//商户导出
		$("#exportBtn").click(function(){
			//$("#loading", window.parent.document).show();
			//setTimeout('',300);
			tradeExport();
			
		});

		//查询交易
		$("#trade_query_btn").click(function(){
			$("#loading", window.parent.document).show();
			setTimeout('accountFileQuery()',300);
			
		});

		$("#fileUploadBtn").click(function(){
			$('#selectDeliverModel').modal('show');
			
		});

		$("#file_submit_btn").click(function(){
			$("#file_submit_btn").attr("disabled", true); 
			$("#loading", window.parent.document).show();
			$.ajax({
	            type: "POST",
	            url:"manage/accountFileUpload?r=" + new Date().getTime(),
	            processData: false,
        		contentType: false,
        		dataType: "text",
	            data: new FormData($('#accountFileForm')[0]),
	            error: function(request) {
					$("#file_submit_btn").attr("disabled", false); 
					$("#loading", window.parent.document).hide();
	                alert("Connection error");
	            },
	            success: function(data) {
	            	$("#file_submit_btn").attr("disabled", false); 
	            	$("#loading", window.parent.document).hide();
		            if(data=="00000") {
		            	toastr.success("提交成功!");
		            	$('#accountFileForm')[0].reset();
			        }
		            else {
		            	toastr.error(data);
			        }
	            }
	        });
		});
		
		
		//accountFileQuery();
		$("#trade_query_btn").click();
	});
	
	</script>

</body></html>