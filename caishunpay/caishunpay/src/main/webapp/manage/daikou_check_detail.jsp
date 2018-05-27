<%@ page language="java" import="java.util.*,com.trade.bean.own.*,com.gy.util.CommonFunction" pageEncoding="GB18030"%>
<%@page import="com.manage.bean.PageModle"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@page import="com.manage.bean.OprInfo,com.trade.bean.*,com.gy.util.*"%>
<%@ include file="include.jsp"%>
<%
BankCardPay BankCardPay = (BankCardPay)request.getAttribute("quickpaybean");
String tmp = "������������";
if(null != BankCardPay){
	String card = BankCardPay.getCardNo();
	String gatewayUrl = "http://posp.cn/cardbin?cardNo=" + card;
	//Map<String, String> map = new HashMap<String, String>();
	//map.put("zkzh", "2000104050");
	//map.put("sfzh", "445122199908084349");
	String res = AppHttp.appadd(gatewayUrl, "");
	//Common.log("����: " + res);
	int start = res.indexOf("result_item") + "result_item".length() + 2;
	tmp = res.substring(start);
	int end = tmp.indexOf("</div>");
	tmp = tmp.substring(0, end);
	tmp = tmp.replaceAll("<.*?>|\t", "");
	tmp = tmp.replaceAll(" - ", "-");
	
	/* String[] result = tmp.split("\n");
	String tmp1 = "";
	for(String t : result){
		if(t != null && !t.isEmpty()){
			String[] resu = t.split(" ");
			if(resu.length == 2){
				tmp1 += resu[1] + "--";
			}else{
				tmp1 += resu[0] + "--";
			}
		}
	}
	tmp = tmp1;  */
}

%>
<html lang="en">
	<head>
		<title>�������</title>
		<script type="text/javascript">
		//��ʼ��toastr.js
		toastr.options = {positionClass:'toast-bottom-center', timeOut: "2000"};
		
    function IFrameResize(){ 
    	//alert(this.document.body.scrollHeight); //������ǰҳ��ĸ߶� 
    	var obj = parent.document.getElementById("main_iframe"); //ȡ�ø�ҳ��IFrame���� 
    	//alert(obj.height); //������ҳ����IFrame�����õĸ߶� 
    	obj.height = this.document.body.scrollHeight; //������ҳ����IFrame�ĸ߶�Ϊ��ҳ��ĸ߶� 
    	} 

	<%
	if(BankCardPay == null) {
		BankCardPay = new BankCardPay();
	%>
		alert("���ض�����Ϣ�쳣");
	<%
	}
	%>
    </script>
	</head>
	<body onload="IFrameResize()" class=" theme-blue">

		<div >
			<div class="header">
				<h1 class="page-title">
					�����������
				</h1>
			</div>

			<div class="row">
				<div class="col-md-8">
					<div id="myTabContent" class="tab-content">
						<div class="tab-pane active in" id="home">
							<form id="mchtForm" name="mchtForm" class="form-horizontal">
							
							<div class="form-group">
									<label for="card_name" class="col-sm-2 control-label">
										����
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="card_name"
											name="card_name" readonly  value="<%=StringUtil.trans2Str(BankCardPay.getCardHolderName()) %>" >
									</div>
								</div>
								<div class="form-group">
									<label for="cer_number" class="col-sm-2 control-label">
										���֤��
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="cer_number"
											name="cer_number" readonly value="<%=StringUtil.trans2Str(BankCardPay.getCerNumber()) %>" >
									</div>
								</div>
								<div class="form-group">
									<label for="bank_card_no" class="col-sm-2 control-label">
										����
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="bank_card_no"
											name="bank_card_no" readonly value="<%=StringUtil.trans2Str(BankCardPay.getCardNo())%>" >
									</div>
								</div>
								<div class="form-group">
									<label for="bank_card_no_desc" class="col-sm-2 control-label">
										���Ŵ����ϸ
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="bank_card_no_desc"
											name="bank_card_no_desc" readonly value="<%=tmp%>" >
									</div>
								</div>
								<div class="form-group">
									<label for="mobile_number" class="col-sm-2 control-label">
										�ֻ���
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="mobile_number"
											name="mobile_number" readonly value="<%=StringUtil.trans2Str(BankCardPay.getMobileNum())%>" >
									</div>
								</div>
								<div class="form-group">
									<label for="dfAmount" class="col-sm-2 control-label">
										���۽��/��
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="dfAmount" onkeyup="value=value.replace(/[^\d.]/g,'')"
											name="dfAmount" readonly value="<%=StringUtil.trans2Str(BankCardPay.getTotal_fee()) %>">
									</div>
								</div>
								
								<div class="form-group">
									<label for="yzm" class="col-sm-2 control-label">
										��Э��
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="yzm"
											name="yzm" readonly value="<%=StringUtil.trans2Str(BankCardPay.getYzm()) %>">
									</div>
								</div>
								<div class="form-group">
									<label for="orderNo" class="col-sm-2 control-label">
										�̻�������
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="orderNo"
											name="orderNo"  readonly value="<%=StringUtil.trans2Str(BankCardPay.getTradeSn()) %>" >
									</div>
								</div>
							<div class="form-group">
									<label for="orderNo" class="col-sm-2 control-label">
										ƽ̨������
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="orderNo"
											name="orderNo"  readonly value="<%=StringUtil.trans2Str(BankCardPay.getOut_trade_no()) %>" >
									</div>
								</div>
								<div class="form-group" >
									<label class="col-sm-2 control-label">ͼƬ</label>
									
									<div class="col-sm-10" id="mchtFileDiv" >
										
										<div id="myCarousel" class="carousel slide">
											
										</div> 
									</div>
									<c:forEach items="${requestScope.orderFiles}" var="item">
											<input name="mchtFilePath" type="hidden" value="${item.mchtFilePath}">
										</c:forEach>
								</div>
								
								<input type="hidden" value="" id="refuse_reason" name="refuse_reason">
							</form>
						</div>

					</div>
				</div>
			</div>

	<div class="btn-toolbar list-toolbar">
      <!-- href="#myModal" -->
    </div>
		</div>
		
			<footer>
                <hr>
                <!-- Purchase a site license to remove this link from the footer: http://www.portnine.com/bootstrap-themes -->
                <p>�0�8 2017 ykbpay</p>
                <br>
            </footer>
            
<div class="modal small fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">��</button>
        <h3 id="myModalLabel">Delete Confirmation</h3>
      </div>
      <div class="modal-body">
        
        <p class="error-text"><i class="fa fa-warning modal-icon"></i>ȷ�ϱ���?</p>
      </div>
      <div class="modal-footer">
        <button class="btn btn-default" data-dismiss="modal" aria-hidden="true">ȡ��</button>
        <button class="btn btn-danger" data-dismiss="modal">ȷ��</button>
      </div>
    </div>
  </div>
</div>
		<script type="text/javascript">
	//�̻�����
	$(function() {
		$("#loading", window.parent.document).hide();
		//�������ڻ�ʱ�����ʾ��ʽ
//		this.DateTimePicker1.CustomFormat = "yyyy-MM-dd HH:mm:ss";
//		//ʹ���Զ����ʽ
//		this.DateTimePicker1.Format = DateTimePickerFormat.Custom;
//		//ʱ��ؼ�������
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

		initPic();
		function initPic() {

			if($("input[name='mchtFilePath']").size()==0){
				return ;
			}
			var images = "";
			var ol = '<ol class="carousel-indicators">';
			var inner = '<div class="carousel-inner">';
			
			$("input[name='mchtFilePath']").each(function(index,element){
				if(index == 0){
					ol += '<li data-target="#myCarousel" data-slide-to="' + index + '" class="active"></li>';
					inner += '<div class="item active" align="center">';
				}
				else {
					ol += '<li data-target="#myCarousel" data-slide-to="' + index + '"></li>';
					inner += '<div class="item" align="center">'
				}
				inner += '<img src="<%=basePath%>/manage/printImage?fileName=' + encodeURIComponent($(this).val()) + '&height=380" alt="�̻�ͼƬ">';
				inner += '</div>';
				//encodeURIComponent(
				//images += '<image src="http://localhost:8088/gyprovider/manage/printImage?fileName=' + encodeURIComponent($(this).val()) + 
				//'" alt="�̻�ͼƬ" class="img-rounded" width="400">';
			});
			ol += '</ol>';
			inner += '</div>';

			//<!-- �ֲ���Carousel������ -->
			var daohang = '<a class="carousel-control left" href="#myCarousel" data-slide="prev">&lsaquo;</a><a class="carousel-control right" href="#myCarousel" data-slide="next">&rsaquo;</a>';
			   
			 
			document.getElementById('myCarousel').innerHTML = ol + inner + daohang;
			
		}

		SelectOptionsDWR.getComboData('COMPANY_ID',function(ret) {
			var company_id = $("#company_id").attr("value");
			$("#company_id").html("");
			var mchtInfos = jQuery.parseJSON(ret).data;
			var mchtHtml = "";
			mchtInfos.forEach(function(item,index){
				if(company_id == item.valueField) {
					mchtHtml += "<option value='" + item.valueField + "' selected = 'selected'>" + item.displayField + "</option>"
				}
				else {
					mchtHtml += "<option value='" + item.valueField + "'>" + item.displayField + "</option>"
				}
				
			});
			$("#company_id").html(mchtHtml);
			$("#company_id").selectpicker('refresh');
		});
		
		
	
	});
	
	</script>

	</body>
</html>