package com.caishun;

import com.trade.util.MD5Util;

public class testMd5Sign {

    //{bank_order_no=91517295918782279137, signature=44a9df6d736519225ccab7e9a1b861bf, order_amount=0.03, mer_order_id=20180130001500270181, supply_code=, err_code=, err_info=, merch_id=80162901, channel_code=wxH5, order_id=1801301505159780, order_remark=测试, status=Y}
    public static void main(String[] args) {
        String k = "STYpmUALul8xZuQXMFJfVlv0YrkAhHgJcJzaS0eR5K3I061GeYRoY5qGx6z4J8DVeyvp4PfVB1ikrWa1yMvsZ1Ugt2MUAih5giECKMLyT4V6WgoK6GF6Nl4lNo5cctut";
        String merchantid= "80162901";
        String morderid = "20180130001500270181";
        String amount= "0.03";
        String orderid = "1801301505159780";
        String status = "Y";
//        String sign = "44a9df6d736519225ccab7e9a1b861bf";
        String sign = MD5Util.generateMd5("UTF-8",k,merchantid,morderid,amount,orderid,status);
        String checkSign = MD5Util.generateMd5(
                    "UTF-8",
                    k,
                    merchantid,
                    morderid,
                    status
            );


        System.out.println(sign);
        System.out.println(checkSign);






    }


}
