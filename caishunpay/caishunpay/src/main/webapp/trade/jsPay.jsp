<%@ page language="java" import="com.trade.bean.response.ThirdPartyPayResponse,com.gy.util.StringUtil,com.trade.enums.*" pageEncoding="UTF-8"%>
<%@ page import="com.trade.bean.response.ThirdPartyPayResponse" %>
<%
	ThirdPartyPayResponse thirdPartyPayResponse = (ThirdPartyPayResponse)request.getAttribute("thirdPartyPayResponse");
%>
<!DOCTYPE html>
<html>
<head>
<title>微信安全支付</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8">
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no">
<style type="text/css">
body{padding: 0;margin:0;background-color:#eeeeee;font-family: '黑体';}
.pay-main{background-color: #4cb131;padding-top: 20px;padding-left: 20px;padding-bottom: 20px;}
.pay-main img{margin: 0 auto;display: block;}
.pay-main .lines{margin: 0 auto;text-align: center;color:#cae8c2;font-size:12pt;margin-top: 10px;}
.tips .img{margin: 20px;}
.tips .img img{width:20px;}
.tips span{vertical-align: top;color:#ababab;line-height:18px;padding-left: 10px;padding-top:0px;}
.action{background:#4cb131;padding: 10px 0;color:#ffffff;text-align: center;font-size:14pt;border-radius: 10px 10px; margin: 15px;}
.action:focus{background:#4cb131;}
.action.disabled{background-color:#aeaeae;}
.footer{position: absolute;bottom:0;left:0;right:0;text-align: center;padding-bottom: 20px;font-size:10pt;color:#aeaeae;}
.footer .ct-if{margin-top:6px;font-size:8pt;}
</style>
</head>
<body>
<div class="conainer">
<div class="pay-main">
<img src="../img/pay_logo.png"/>
<div class="lines"><span>微信安全支付</span></div>
</div>
<div class="tips">
<div class="img" >
<img src="../img/pay_ok.png"/>
<span>已开启支付安全</span>
</div>
</div>
<!--disabled  -->
<div id="action" class="action" onclick="onBridgeReady();">确认支付</div>
<div class="footer"><div>支付安全由中国人民财产保险股份有限公司承保</div><div class="ct-if"></div></div>
</div>
<script type="text/javascript">
		String.prototype.startWith=function(str){  
	        if(str==null||str==""||this.length==0||str.length>this.length)  
	          return false;  
	        if(this.substr(0,str.length)==str)  
	          return true;  
	        else  
	          return false;  
	        return true;  
	    }
		function onBridgeReady(){
			
			WeixinJSBridge.invoke(
					'getBrandWCPayRequest',<%=request.getAttribute("pay_info")%>, 
					function(res){
						if(res.err_msg == "get_brand_wcpay_request:ok" ) {

							<%
								String callback_url = (String)request.getAttribute("callback_url");
								if(StringUtil.isNotEmpty(callback_url)){
							%>
							var callback_url = "<%=callback_url%>";
							if(callback_url.startWith("http://") || callback_url.startWith("https://")){
								location.replace(callback_url);
							}
							else {
								location.replace("http://" + callback_url);
							}
							
							<%
								}
							%>
							
						}     //  使用以上方式判断前端返回,微信团队郑重提示：res.err_msg 将在用户支付成功后返回                         ok，但并不保证它绝对可靠
						else if(res.err_msg == "get_brand_wcpay_request:cancel" ){
							//window.opener=null;
							//window.open('','_self');
							//window.close();
						}
						else {
							//alert(res.err_msg);
						}
						
					} 
			); 
		} 
	
		if (typeof WeixinJSBridge == "undefined"){ 
			if( document.addEventListener ){
				document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
			}else if (document.attachEvent){ 
				document.attachEvent('WeixinJSBridgeReady', onBridgeReady); 
				document.attachEvent('onWeixinJSBridgeReady', onBridgeReady); 
			} 
		}else{
			onBridgeReady(); 
		} 

	</script>
</body>
</html>