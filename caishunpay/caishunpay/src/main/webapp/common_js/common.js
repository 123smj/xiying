Date.prototype.format = function(format)
{
 var o = {
 "M+" : this.getMonth()+1, //month
 "d+" : this.getDate(),    //day
 "h+" : this.getHours(),   //hour
 "m+" : this.getMinutes(), //minute
 "s+" : this.getSeconds(), //second
 "q+" : Math.floor((this.getMonth()+3)/3),  //quarter
 "S" : this.getMilliseconds() //millisecond
 }
 if(/(y+)/.test(format)) format=format.replace(RegExp.$1,
 (this.getFullYear()+"").substr(4 - RegExp.$1.length));
 for(var k in o)if(new RegExp("("+ k +")").test(format))
 format = format.replace(RegExp.$1,
 RegExp.$1.length==1 ? o[k] :
 ("00"+ o[k]).substr((""+ o[k]).length));
 return format;
}

/**
 * 获取AddDayCount天后的日期，-n则为n天前
 */
function getDateAfter(AddDayCount, dateFormat) 
{ 
	var dd = new Date(); 
	dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期 
	//var y = dd.getYear(); 
	//var m = (dd.getMonth()+1)<10?"0"+(dd.getMonth()+1):(dd.getMonth()+1);//获取当前月份的日期，不足10补0
	//var d = dd.getDate()<10?"0"+dd.getDate():dd.getDate(); //获取当前几号，不足10补0
	if(dateFormat == undefined) {
		dateFormat = 'yyyyMMdd';
	}
	return dd.format(dateFormat);
}

function tradeResourceTrans(val) {
		if(val == "1"){
			return "支付宝";
		}
        else if (val == "12") {
            return "支付宝H5";
        }
        else if (val == "71") {
            return "银联扫码";
        }
        else if (val == "2") {
            return "微信扫码";
        }
        else if(val == "3"){
            return "百度支付";
        }
        else if(val == "4"){
            return "手机QQ支付";
        }
        else if(val == "5"){
            return "京东";
        }
        else if(val == "6"){
            return "网银支付";
        }
        else if(val == "7"){
            return "快捷支付";
        }
        else if(val == "8"){
            return "余额代付";
        }
        else if(val == "9"){
            return "垫资代付";
        }
        else if(val == "21"){
            return "微信公众号支付";
        }
        else if(val == "22"){
            return "微信H5";
        }
        else if(val == "K"){
            return "代扣";
        }
		return val;
}

function tradeStateTrans(val) {
	if(val == "SUCCESS"){
		return "交易成功";
	}
	else if(val == "REFUND"){
		return "转入退款";
	}
	else if(val == "NOTPAY"){
		return "未支付";
	}
	else if(val == "CLOSED"){
		return "已关闭";
	}
	else if(val == "PAYERROR"){
		return "支付失败";
	}
	else if(val == "PRESUCCESS"){
		return "预交易成功";
	}
	else if(val == "PREERROR"){
		return "预交易失败";
	}else if(val == "CHECK"){
		return "待审核";
	}
	else if(val == "REFUSE"){
		return "交易失败";
	}
	return val;
}

function t0FlagTrans(val) {
	if(val == "1"){
		return "是";
	}
	else if(val == "0"){
		return "否";
	}
	return val;
}
function mchtStatusTrans(val) {
	if(val == "00"){
		
		return '<span class="text_green span_auto_width">正常</span>';
	}
	else if(val == "01"){
		return '<span class="text_red span_auto_width">新增待审核</span>';
	}
	else if(val == "02"){
		return '<span class="text_red span_auto_width">冻结</span>';
	}
	else if(val == "03"){
		return '<span class="text_red span_auto_width">修改待审核</span>';
	}
	else if(val == "04"){
		return '<span class="text_red span_auto_width">冻结待审核</span>';
	}
	else if(val == "08"){
		return '<span class="text_red span_auto_width">修改审核拒绝</span>';
	}
	else if(val == "09"){
		return '<span class="text_red span_auto_width">新增审核拒绝</span>';
	}
	return val;
}
function channelStatusTrans(val) {
	if(val == "00"){
		
		return '<span class="text_green span_auto_width">正常</span>';
	}
	else if(val == "01"){
		return '<span class="text_blue span_auto_width">新增待提交</span>';
	}
	else if(val == "11"){
		return '<span class="text_blue span_auto_width">提交待审核</span>';
	}
	else if(val == "02"){
		return '<span class="text_red span_auto_width">冻结</span>';
	}
	else if(val == "03"){
		return '<span class="text_red span_auto_width">修改待审核</span>';
	}
	else if(val == "04"){
		return '<span class="text_red span_auto_width">冻结待审核</span>';
	}
	else if(val == "08"){
		return '<span class="text_red span_auto_width">修改审核拒绝</span>';
	}
	else if(val == "09"){
		return '<span class="text_red span_auto_width">新增审核拒绝</span>';
	}
	return val;
}
function dfStatus(val) {
	if(val == "00"){
		
		return '<span class="text_green span_auto_width">成功</span>';
	}
	else if(val == "01"){
		return '<span class="text_red span_auto_width">处理中</span>';
	}
	else if(val == "02"){
		return '<span class="text_red span_auto_width">处理失败</span>';
	}
	else if(val == "09"){
		return '<span class="text_red span_auto_width">提交失败</span>';
	}
	return val;
}
function accountStatus(val) {
	if(val == "00"){
		
		return '<span class="text_green span_auto_width">已入账</span>';
	}
	else if(val == "01"){
		return '<span class="text_red span_auto_width">未入账</span>';
	}
	else if(val == "02"){
		return '<span class="text_red span_auto_width">入账失败</span>';
	}
	
	return val;
}
function fenToYuan(fen) {
	if(fen == "") {
		return fen;
	}
	return (fen/100).toFixed(2);
}

function timeFormatStandard(yyyyMMddhhmmss){
	if(yyyyMMddhhmmss.length == 14){
		return yyyyMMddhhmmss.replace(/^(\d{4})(\d{2})(\d{2})(\d{2})(\d{2})(\d{2})$/, "$1-$2-$3 $4:$5:$6");
	}
	return yyyyMMddhhmmss;
}
function dateFormat(yyyymmdd){
	var pattern = /(\d{4})(\d{2})(\d{2})/;
	var formatedDate = yyyymmdd.replace(pattern, '$1-$2-$3');
	
	return formatedDate;
}
/**
* 根据两个日期，判断相差天数
* @param sDate1 开始日期 如：2016-11-01
* @param sDate2 结束日期 如：2016-11-02
* @returns {number} 返回相差天数
*/
function daysBetween(sDate1,sDate2){
    //Date.parse() 解析一个日期时间字符串，并返回1970/1/1 午夜距离该日期时间的毫秒数
    var time1 = Date.parse(new Date(sDate1));
    var time2 = Date.parse(new Date(sDate2));
    var nDays = Math.abs(parseInt((time2 - time1)/1000/3600/24));
    return  nDays;
};