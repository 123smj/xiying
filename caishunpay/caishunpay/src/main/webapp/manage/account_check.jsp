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
					账户额度审核
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
									<span style="width: 90px; display: block;">批次号</span>
								</th>
								<th>
									<span style="width: 160px; display: inline-block; overflow: hidden">账目文件名</span>
								</th>
								<th>
									<span style="width: 100px; display: block;">操作时间</span>
								</th>
								<th>
									<span style="width: 120px; display: block;">操作员</span>
								</th>
								<th>
									<span style="width: 90px; display: block;">状态</span>
								</th>
								<th>
									<span style="width: 90px; display: block;">操作</span>
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

	<script type="text/javascript">
	//交易查询
	function accountFileQuery(num){
		if(num != 'undefined'){
			$("#pageNum").val(num);
		}
		$.ajax({
            cache: true,
            type: "POST",
            url:"manage/accountFile4CheckQuery?r=" + new Date().getTime(),
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
                	mchtStatusTrans(item.status) + '</td><td>';

                	html += '<a href="javascript:void(0);" onclick="acceptCheck(\'' + item.offerSeq + '\')"><span class="text_green span_auto_width">通过</span></a>&nbsp;&nbsp';
                	html += '<a href="javascript:void(0);" onclick="refuseCheck(\'' + item.offerSeq + '\')"><span class="text_red span_auto_width">拒绝</span></a>&nbsp;&nbsp';
	                
                	html += '</td></tr>';
                }
                $("#mcht_tbody").html(html);
                
                }
				
        });
	}

	//商户导出
	function tradeExport(){

		toastr.error("亲，暂不支持导出");
	}

	function acceptCheck(offerSeq){
		window.parent.wxc.xcConfirm("确认通过?", window.parent.wxc.xcConfirm.typeEnum.confirm, {
			onOk:function(v){
				doCheck(offerSeq, '00', '');
			}
		});
	}
	function refuseCheck(offerSeq){

		var txt=  "请输入拒绝原因";
		window.parent.wxc.xcConfirm(txt, window.parent.wxc.xcConfirm.typeEnum.input,{
			onOk:function(v){
				if(v.length <= 0 || v.length > 100) {
					toastr.error("拒绝原因长度有误");
					return ;
				}
				doCheck(offerSeq, '09', v);
			}
		});
	}

	function doCheck(offerSeq, checkReslut, refuseReason) {
		$.ajax({
            type: "POST",
            url:"manage/accountFileCheck?r=" + new Date().getTime(),
            data:{"offerSeq" : offerSeq, "checkReslut": checkReslut, "refuseReason": refuseReason},
            dataType : "text",
            error: function(request) {
				//$("#loading", window.parent.document).hide();
                alert("Connection error");
            },
            success: function(data) {
            	$("#loading", window.parent.document).hide();
	            if(data=="00000") {
	            	toastr.success("审核成功!");
	            	accountFileQuery();
		        }
	            else {
	            	toastr.error(data);
		        }
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

		//accountFileQuery();
		$("#trade_query_btn").click();
	});
	
	</script>

</body></html>