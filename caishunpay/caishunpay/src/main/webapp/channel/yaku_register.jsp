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
    </script>
	</head>
	<body onload="IFrameResize()" class="theme-blue">

		<div >
			<div class="header">
				<h1 class="page-title">
					雅酷进件
				</h1>
			</div>

			<div class="row">
				<div class="col-md-8">
					<div id="myTabContent" class="tab-content">
						<div class="tab-pane active in" id="home">

							<form id="channelMchtForm" name="channelMchtForm" class="form-horizontal" action="manage/channelMchtVerify" method="post" >
								
								<div class="form-group">
									<label for="cmer" class="col-sm-2 control-label">
										商户名称
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="cmer" maxlength="20"
											name="cmer" placeholder="请输入商户名称10个汉字以内" value="">
									</div>
								</div>
								<div class="form-group">
									<label for="cmer_sort" class="col-sm-2 control-label">
										商户简称
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="cmer_sort"
											name="cmer_sort" placeholder="请输入商户简称10个汉字以内" value="">
									</div>
								</div>
								
								<div class="form-group">
									<label for="merchant_type" class="col-sm-2 control-label">
										商户类型
									</label>
									<div class="col-sm-6">
										<select id="merchant_type" name="merchant_type"
										class="selectpicker" data-live-search="true" title="请选择商户类型">
											<option value='B'>企业</option>
											<option value='C'>个人</option>
										</select>
										
									</div>
								</div>
								<div class="form-group">
									<label for="org_code" class="col-sm-2 control-label">
										组织机构代码
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="org_code"
											name="org_code" placeholder="请输入组织机构代码" value="">
									</div>
								</div>
								
								<div class="form-group">
									<label for="merchant_license" class="col-sm-2 control-label">
										营业执照
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="merchant_license"
											name="merchant_license" placeholder="请输入营业执照" value="">
									</div>
								</div>
								
								<div class="form-group">
									<label for="categroy" class="col-sm-2 control-label">
										经营类目
									</label>
									<div class="col-sm-6">
										<select id="categroy" name="categroy"
										class="selectpicker" data-live-search="true" title="请选择经营类目">
											<option value='1000010001'>食品</option>
											<option value='1000010002'>餐饮</option>
											<option value='1000020001'>超市</option>
											<option value='1000020002'>便利店</option>
											<option value='1000020003'>自动贩卖机</option>
											<option value='1000030001'>百货</option>
											<option value='1000030002'>其他综合零售</option>
											<option value='1000030003'>户外/运动/健身器材/安防</option>
											<option value='1000030004'>黄金珠宝/钻石/玉石</option>
											<option value='1000030005'>母婴用品/儿童玩具</option>
											<option value='1000030006'>家装建材/家居家纺</option>
											<option value='1000030007'>美妆/护肤</option>
											<option value='1000030008'>鲜花/盆栽/室内装饰品</option>
											<option value='1000030009'>交通工具/配件/改装</option>
											<option value='1000030010'>服饰/箱包/饰品</option>
											<option value='1000030011'>钟表/眼镜</option>
											<option value='1000030012'>宠物/宠物食品/饲料</option>
											<option value='1000030013'>数码家电/办公设备</option>
											<option value='1000030014'>书籍/音像/文具/乐器</option>
											<option value='1000030015'>计生用品</option>
											<option value='1000030016'>线上商超</option>
											<option value='1000030017'>团购</option>
											<option value='1000030018'>海淘</option>
											<option value='1000040001'>俱乐部/休闲会所</option>
											<option value='1000040002'>美容/健身类会所</option>
											<option value='1000040003'>游艺厅/KTV/网吧</option>
											<option value='1000050001'>婚庆/摄影</option>
											<option value='1000050002'>装饰/设计</option>
											<option value='1000050003'>家政/维修服务</option>
											<option value='1000050004'>广告/会展/活动策划</option>
											<option value='1000050005'>咨询/法律咨询/金融咨询等</option>
											<option value='1000050006'>物流/快递公司</option>
											<option value='1000050007'>加油</option>
											<option value='1000050008'>租车</option>
											<option value='1000050009'>水电煤缴费/交通罚款等生活缴费</option>
											<option value='1000050010'>停车缴费</option>
											<option value='1000050011'>城市交通/高速收费</option>
											<option value='1000050012'>有线电视缴费</option>
											<option value='1000050013'>物业管理费</option>
											<option value='1000050014'>其他生活缴费</option>
											<option value='1000060001'>机票/机票代理</option>
											<option value='1000060002'>旅馆/酒店/度假区</option>
											<option value='1000060003'>娱乐票务</option>
											<option value='1000060004'>交通票务</option>
											<option value='1000060005'>旅行社</option>
											<option value='1000060006'>旅游服务平台</option>
											<option value='1000070001'>保健信息咨询平台</option>
											<option value='1000070002'>保健器械/医疗器械/非处方药品</option>
											<option value='1000070003'>私立/民营医院/诊所</option>
											<option value='1000070004'>挂号平台</option>
											<option value='1000080001'>教育/培训/考试缴费/学费</option>
										</select>
										
									</div>
								</div>
								
								<div class="form-group">
									<label for="bankName" class="col-sm-2 control-label">
										开户银行名称
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="bankName"
											name="bankName" placeholder="请输入银行名称" value="">
									</div>
								</div>
								
								<div class="form-group">
									<label for="branchBankName" class="col-sm-2 control-label">
										开户支行名称
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="branchBankName"
											name="branchBankName" placeholder="请输入支行名称" value="">
									</div>
								</div>
								
								<div class="form-group">
									<label for="location" class="col-sm-2 control-label">
										开户城市
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="location"
											name="location" placeholder="请输入开户城市" value="">
									</div>
								</div>
								<div class="form-group">
									<label for="card_no" class="col-sm-2 control-label">
										卡号
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="card_no"
											name="card_no" placeholder="请输入卡号" value="6214856556237018">
									</div>
								</div>
								<div class="form-group">
									<label for="real_name" class="col-sm-2 control-label">
										开户人名
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="real_name"
											name="real_name" placeholder="请输入开户人名" value="左松">
									</div>
								</div>
								<div class="form-group">
									<label for="cert_no" class="col-sm-2 control-label">
										身份证号
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="cert_no"
											name="cert_no" placeholder="请输入身份证号" value="500382198408202155">
									</div>
								</div>
								
								<div class="form-group">
									<label for="mobile" class="col-sm-2 control-label">
										开户时绑定手机号
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="mobile"
											name="mobile" placeholder="请输入手机号(不能重复)" value="">
									</div>
								</div>
								
								<div class="form-group">
									<label for="phone" class="col-sm-2 control-label">
										客服电话
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="service_phone"
											name="service_phone" placeholder="请输入客服电话" value="4007885998">
									</div>
								</div>
								
								
								<div class="form-group">
									<label for="province_code" class="col-sm-2 control-label">
										省份编码
									</label>
									<div class="col-sm-6">
										<select id="province_code" name="province_code" class="selectpicker" data-live-search="true" title="请输入省份编码">

										</select>
									</div>
								</div>
								<div class="form-group">
									<label for="city_code" class="col-sm-2 control-label">
										城市编码
									</label>
									<div class="col-sm-6">
										<select id="city_code" name="city_code" class="selectpicker" data-live-search="true" title="请输入城市编码">
										</select>
									</div>
								</div>
								<div class="form-group">
									<label for="district_code" class="col-sm-2 control-label">
										区编码
									</label>
									<div class="col-sm-6">
										<select id="district_code" name="district_code" class="selectpicker" data-live-search="true" title="请输入区编码">
										</select>
									</div>
								</div>
								
								
								<div class="form-group">
									<label for="address" class="col-sm-2 control-label">
										详细地址
									</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="address"
											name="address" placeholder="请输入详细地址" value="">
									</div>
								</div>
								
							</form>
						</div>

					</div>
				</div>
			</div>

	<div class="btn-toolbar list-toolbar">
      <button class="btn btn-primary" id="verifyBtn"><i class="fa fa-save"></i> 提交资料</button>
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
	
	$(function() {
		$("#loading", window.parent.document).hide();

		$("#province_code").change(function(){
			switchCity();
		});

		$("#city_code").change(function(){
			switchDistrict();
		});
		function switchCity() {
			SelectOptionsDWR.getComboDataWithParameter('YAKU_CITY',$("#province_code").val(), function(ret) {

				$("#city_code").html("");
				var mchtInfos = jQuery.parseJSON(ret).data;
				var mchtHtml = "";
				mchtInfos.forEach(function(item,index){
					mchtHtml += "<option value='" + item.valueField + "'>" + item.displayField + "</option>"
				});
				$("#city_code").html(mchtHtml);
				$("#city_code").selectpicker('refresh');
				$("#district_code").selectpicker('refresh');
	      		
			});
		}
		function switchDistrict() {
			SelectOptionsDWR.getComboDataWithParameter('YAKU_DISTRICT',$("#city_code").val(), function(ret) {
				$("#district_code").html("");
				var mchtInfos = jQuery.parseJSON(ret).data;
				var mchtHtml = "";
				mchtInfos.forEach(function(item,index){
					
					mchtHtml += "<option value='" + item.valueField + "'>" + item.displayField + "</option>"
				});
				$("#district_code").html(mchtHtml);
				$("#district_code").selectpicker('refresh');
			});
		}
		
		SelectOptionsDWR.getComboData('YAKU_PROVINCE',function(ret) {
			$("#province_code").html("");
			var mchtInfos = jQuery.parseJSON(ret).data;
			var mchtHtml = "";
			mchtInfos.forEach(function(item,index){
				
				mchtHtml += "<option value='" + item.valueField + "'>" + item.displayField + "</option>"
			});
			$("#province_code").html(mchtHtml);
			$("#province_code").selectpicker('refresh');
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
		            url:"manage/yakuChannelVerify?r=" + new Date().getTime(),
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
		$("#verifyBtn").click(function(){

			if($("#cmer").val() == ""){
				alert("商户名称不能为空");
				return ;
			}
			if($("#cmer_sort").val() == ""){
				alert("商户简称不能为空");
				return ;
			}
			if($("#merchant_type").val() == ""){
				alert("商户类型不能为空");
				return ;
			}
			if($("#merchant_type").val() == "B" && $("#merchant_license").val() == ""){
				alert("营业执照不能为空");
				return ;
			}
			if($("#categroy").val() == ""){
				alert("经营类目不能为空");
				return ;
			}
			if($("#bankName").val() == ""){
				alert("开户银行名称不能为空");
				return ;
			}
			if($("#branchBankName").val() == ""){
				alert("开户支行名称不能为空");
				return ;
			}

			if($("#location").val() == ""){
				alert("开户城市不能为空");
				return ;
			}

			if($("#card_no").val() == ""){
				alert("卡号不能为空");
				return ;
			}
			if($("#real_name").val() == ""){
				alert("开户人名不能为空");
				return ;
			}
			if($("#cert_no").val() == ""){
				alert("身份证号不能为空");
				return ;
			}
			if($("#mobile").val() == ""){
				alert("手机号不能为空");
				return ;
			}
			if($("#service_phone").val() == ""){
				alert("客服电话不能为空");
				return ;
			}
			if($("#province_code").val() == ""){
				alert("省份编码不能为空");
				return ;
			}
			if($("#city_code").val() == ""){
				alert("城市编码不能为空");
				return ;
			}
			if($("#district_code").val() == ""){
				alert("区编码不能为空");
				return ;
			}
			if($("#address").val() == ""){
				alert("详细地址不能为空");
				return ;
			}
			
			window.parent.wxc.xcConfirm("确认提交?", window.parent.wxc.xcConfirm.typeEnum.confirm, {
				onOk:function(v){
					doChannelMchtAdd();
				}
			});
		});

		
		
	});
	
	</script>

	</body>
</html>