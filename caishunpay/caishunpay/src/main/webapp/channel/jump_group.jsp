<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.manage.bean.PageModle"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page import="com.manage.bean.OprInfo,com.trade.bean.*,com.gy.util.*"%>
<%@ include file="../manage/include.jsp" %>

<html lang="en"><head>
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
<body  onload="IFrameResize()" class=" theme-blue" >

<div>
			<div class="header">
				<h1 class="page-title">
					跳码组维护
				</h1>
			</div>
			
			<%--<iframe width="100%" style="border: 0" height="900px" scrolling="no" src="<%= request.getContextPath()%>/manage/tradeInfo.jsp"></iframe>
			
		--%>
		</div>
		<div class="search-well">
                <form class="form-inline" id="mcht_form" name="mcht_form" style="margin-top:0px;margin-left: 10px" action="" target="_self">
                	<table >
                		<tr>
                			<td>
            				<label>跳码组：</label></td><td>
					            <select id="jump_group" name="jump_group"
										class="selectpicker" data-live-search="true" title="请选择跳码组">

										</select>
          					</td>
          					<td><label>交易类型：</label></td><td>
					            <select name="trade_source" id="trade_source" class="form-control">
					              <option value="">全部</option>
					              <option value="2">微信扫码</option>
					              <option value="21">微信公众号</option>
					              <option value="1">支付宝</option>
					              <option value="4">手机QQ支付</option>
					              <option value="6">网银支付</option>
					              <option value="7">快捷支付</option>
					            </select></td>
                			<td>
                			<td>
            				<label>渠道编号：</label></td><td>
					            <select id="channel_id" name="channel_id"
										class="selectpicker" data-live-search="true" title="请选择渠道编号">

										</select>
          				</td>
                		</tr>
                		<tr>
                		
          				<td>
            				<label>渠道商户号：</label></td><td>
                    		<input name="channel_mcht_no" id="channel_mcht_no" class="input-xlarge form-control" placeholder="渠道商户号"  type="text">
          				</td>
          			    
                		<td><input type="hidden" id="pageNum" name="pageNum" value="1"></td><td colspan="5" align="right">
                    		<button class="btn btn-default" type="button" id="trade_query_btn" name="trade_query_btn"><i class="fa fa-search"></i> 查询</button>
                    	</td>
                    	</tr>
                	</table>
                	
                </form>
            </div>
            
			<div class="main-content">
				<div class="btn-toolbar list-toolbar">
					<button class="btn btn-primary" id="exportBtn">
						<i class="fa fa-plus"></i>跳码组导出
					</button>
					<a class="btn btn-default" href="<%=path %>/channel/jump_group_add.jsp">
						跳码组新增
					</a>
					<a class="btn btn-default" href="<%=path %>/channel/jump_mcht_add.jsp">
						跳码商户新增
					</a>
					<div class="btn-group">
					</div>
				</div>
				
			</div>
				<div style="width: 99%; overflow-x: scroll;">
					<table class="table" width="1500px" height="auto">
						<thead style="height: 20px">
							<tr style="height: 20px">
								<th style="width: 100px">
									#
								</th>
								
								<th>
									<span style="width: 90px; display: block;">跳码组编号</span>
								</th>
								<th>
									<span style="width: 90px;  display: inline-block; overflow: hidden">跳码组名称</span>
								</th>
								<th>
									<span style="width: 90px; display: block;">交易类型</span>
								</th>
								<th>
									<span style="width: 90px; display: block;">渠道编号</span>
								</th>
								<th>
									<span style="width: 120px; display: block;">渠道名称</span>
								</th>
								<th>
									<span style="width: 120px; display: block;">通道商户编号</span>
								</th>
								<th>
									<span style="width: 120px; display: block;">通道商户名称</span>
								</th>
								<th>
									<span style="width: 100px; display: block;">跳码权重</span>
								</th>
								<th>
									<span style="width: 120px; display: block;">操作</span>
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
                <a href="javascript:void(0);" onclick="jumpGroupQuery('1')">首页</a>  
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
              
            <li><a href="javascript:void(0);" onclick="jumpGroupQuery(document.getElementById('xzPage').value);">go</a></li>  
        </ul>

				
				
	<script type="text/javascript">
	//交易查询
	function jumpGroupQuery(num){
		if(num != 'undefined'){
			$("#pageNum").val(num);
		}
		$.ajax({
            cache: true,
            type: "POST",
            url:"manage/jumpGroupQuery?r=" + new Date().getTime(),
            data:$('#mcht_form').serialize(),// 你的formid
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
	            $("#prePage").attr("onclick","jumpGroupQuery('" + prePage +"')");
	            var next = data.currentPage >=data.totalPage ? data.currentPage: (Number(data.currentPage) + 1);
	            $("#nextPage").attr("onclick","jumpGroupQuery('" + next + "')");
	            $("#lastPage").attr("onclick","jumpGroupQuery('" + data.totalPage + "')");
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
                	'<span class="span_auto_width" >' + item.jump_group + '</span></td><td>' +
                	'<span class="span_auto_width" >' + item.group_name + '</span></td><td>' +
                	'<span class="span_auto_width">' + tradeResourceTrans(item.trade_source) + '</span></td><td>' +
                	'<span class="span_auto_width">' + item.channel_id + '</span></td><td>' +
                	'<span class="span_auto_width">' + item.channel_name + '</span></td><td>' +
                	'<span class="span_auto_width">' + item.channel_mcht_no + '</span></td><td>' +
                	'<span class="span_auto_width">' + item.channel_name + '</span></td><td>' +
                	item.weight + '</td><td>' +
                	'<a href="javascript:void(0);" onclick="deleteJumpMcht(\'' + item.jump_group + '\',\'' + item.channel_id + '\',\'' + item.channel_mcht_no + '\',\'' + item.trade_source + '\')"><span class="text_red span_auto_width">删除</span></a>';
                	
                	html += '</td></tr>';

                }
                $("#mcht_tbody").html(html);
                
                }
				
        });
	}

	//商户导出
	function jumpGroupExport(){
		//return ;
		$("#loading", window.parent.document).show();
		setTimeout(function(){
			$.ajax({
	            cache: true,
	            type: "POST",
	            timeout: 10000,
	            url:"manage/jumpGroupExport?r=" + new Date().getTime(),
	            data:$('#mcht_form').serialize(),// 你的formid
	            dataType : "text",
	            async: false,
	            error: function(request) {
					$("#loading", window.parent.document).hide();
	                alert("Connection error");
	            },
	            success: function(data) {
	            	$("#loading", window.parent.document).hide();
	            	window.location.href = "<%= request.getContextPath()%>" + '/manage/ajaxDownLoad?path='+ data ;
	            }
	        });
		},300);
		
	}
	
	function deleteJumpMcht(jump_group, channel_id, channel_mcht_no, trade_source){

		window.parent.wxc.xcConfirm("确认删除?", window.parent.wxc.xcConfirm.typeEnum.confirm, {
			onOk:function(v){
			$.ajax({
	            cache: true,
	            type: "POST",
	            timeout: 10000,
	            url:"manage/jumpMchtDelete?r=" + new Date().getTime(),
	            dataType : "text",
	            data:{"jump_group": jump_group, "channel_id": channel_id, "channel_mcht_no": channel_mcht_no, "trade_source": trade_source},
	            async: true,
	            error: function(request) {
	                alert("Connection error");
	            },
	            success: function(data) {
	            	if(data=="00000") {
	            		toastr.success("删除成功!");
	            		jumpGroupQuery();
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

		SelectOptionsDWR.getComboData('JUMP_GROUP',function(ret) {
			$("#jump_group").html("");
			var mchtInfos = jQuery.parseJSON(ret).data;
			var mchtHtml = "<option value=''>全部</option>";
			mchtInfos.forEach(function(item,index){
				mchtHtml += "<option value='" + item.valueField + "'>" + item.displayField + "</option>"
			});
			$("#jump_group").html(mchtHtml);
			$("#jump_group").selectpicker('refresh');
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
		

		//查询交易
		$("#trade_query_btn").click(function(){
			$("#loading", window.parent.document).show();
			setTimeout('jumpGroupQuery()',300);
			
		});
		//商户导出
		$("#exportBtn").click(function(){
			jumpGroupExport();
			
		});
		
		//jumpGroupQuery();
		$("#trade_query_btn").click();
	});
	
	</script>

</body></html>