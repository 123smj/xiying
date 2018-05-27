## 1 规范说明

### 1.1 通信协议

HTTP协议

### 1.2 请求方法
所有接口只支持POST方法发起请求。

### 1.3 字符编码
HTTP通讯及报文均采用UTF-8字符集编码格式。

### 1.4 报文规范说明

1. 报文规范仅针对交易请求数据进行描述；  

2. 报文规范分为请求报文和响应报文。请求报文描述由发起方，响应报文由报文接收方响应。

## 2 接口定义

### 2.1 发起支付

使用此统一支付接口来发起支付。

- **接口说明：** 发起支付
- **请求方式：**POST 
- **接口地址：** http://gateway.ykbpay.com:8080/caishunpay-web/thirdParty/createOrder

#### 2.1.1 请求参数

| 参数名称        | 类型     | 描述                                       |
| ----------- | :----- | ---------------------------------------- |
| merchantId  | string | 商户号                                      |
| tradeSn     | string | 商户系统订单号                                  |
| orderAmount | int    | 金额（单位分）                                  |
| goodsName   | string | 商品名称                                     |
| notifyUrl   | string | 后端回调URL                                  |
| sign        | string |  签名字符串 |
| tradeSource | int    | 交易类型, 1:支付宝 12:支付宝H5 2: 微信扫码 22:微信H5  4:QQ钱包  22 : 微信H5  7:快捷支付 71:银联扫码 |
| nonce       | String | 随机字符串                                      |

请求示例：
```
   "merchantid":"shunca0002",
  "tradeSn":"test14",
  "orderAmount":2,
  "goodsName":"测试",
  "notifyUrl":"http://www.baidu.com",
  "sign":"CF1D05DA8CA4095...",
  "tradeSource":22,
  "nonce":1234,
```

#### 2.1.2 响应参数

| 参数名称       | 类型     | 描述   |
| ---------- | :----- | ---- |
| merchantId | string | 商户号  |
| resultCode | string |  响应码，如果是00000则为成功    |
| message    | string |  响应码描述   |
| nonce      | string |  随机字符串   |
| pay_url    | string |  引导用户跳转URL    |
| img_url    | string |  引导用户扫码图片URL    |
| sign       | string |   签名   |

响应示例：
```
{
  "resultCode":"000000",
  "message":"请求成功",
  "sign":"29DAQWEFSA...",
  "nonce":"QWEWQEDFSAVCBCOI...",
  "pay_url":"http://gateway.ykbpay.com/pay/",
  "img_url":"http://gateway.ykbpay.com/img/",
  "merchantId":"caishun0002"
}
```

### 2.2 第三方查询订单支付

- **接口说明：** 第三方查询订单支付
- **请求方式：**POST 
- **接口地址：** http://gateway.ykbpay.com:8080/caishunpay-web/thirdParty/queryOrder

#### 2.2.1 请求参数

| 参数名称       | 类型     | 描述                                       |
| ---------- | :----- | ---------------------------------------- |
| merchantId | string | 商户号                                      |
| tradeSn    | string | 商户系统订单号                                  |
| sign       | string | 签名字符串 |
| nonce      | int    | 随机数                                      |

请求示例：
```
  "merchantid":"shunca0002",
  "tradeSn":"test14",
  "sign":"CF1D05DA8CA4095...",
  "nonce":1234,
```

#### 2.2.2 响应参数

| 参数名称        | 类型     | 描述      |
| ----------- | :----- | ------- |
| sign        | string |    签名字符串     |
| nonce       | string |    随机字符串     |
| merchantId  | string | 商户号     |
| tradeSn     | string | 商户系统订单号 |
| orderAmount | int    | 金额（单位分） |
| tradeState  | string |    交易状态  NOTPAY 未支付 SUCCESS 支付成功    |

响应示例：
```
{
  "sign":"29DAQWEFSA...",
  "nonce":"QWEWQEDFSAVCBCOI...",
  "merchantId":"caishun0002",
  "tradeSn":"test12",
  "orderAmount":22,
  "tradeState":"NOTPAY"
}
```

### 2.3 网银发起订单支付

- **接口说明：** 网银发起订单支付
- **请求方式：**POST 
- **接口地址：** http://gateway.ykbpay.com:8080/caishunpay-web/bankCardPay/queryOrder

#### 2.3.1 请求参数

| 参数名称        | 类型     | 描述                                       |
| ----------- | :----- | ---------------------------------------- |
| merchantId  | string | 商户号                                      |
| tradeSn     | string | 商户系统订单号                                  |
| orderAmount | int    | 金额（单位分）                                  |
| goodsName   | string | 商品名称                                     |
| bankSegment | int    |                                          |
| notifyUrl   | string | 后端回调URL                                  |
| tradSource  | int    | 交易类型                                     |
| cardType    | int    |   00借记卡 01贷记卡                                       |
| channelType | int    |      1 PC端 2移动端                                    |
| callbackUrl | string |       交易成功后前端跳转地址     |
| sign        | string | 加密前：goodsName=测试&merchantid=shancai00... |
| nonce       | string | 随机数        |

请求示例：
```
  "merchantid":"shunca0002",
  "tradeSn":"test14",
  "orderAmount":2,
  "goodsName":"测试",
  "bankSegment":1002,
  "notifyUrl":"http://www.baidu.com",
  "cardType":01,
  "channelType":1,
  "callbackUrl":"http://www.baidu.com",
  "sign":"CF1D05DA8CA4095...",
  "tradeSource":22,
  "nonce":1234
```

#### 2.3.2 响应参数

| 参数名称           | 类型     | 描述   |
| -------------- | :----- | ---- |
| resultCode     | string |    响应码  |
| message        | string | 返回消息 |
| sign           | string |    签名  |
| nonce          | string |   随机字符串   |
| merchantId     | string |   商户号   |
| payUrl         | string |   引导用户支付URL   |

响应示例：
```
{
  "resultCode":"000000",
  "message":"请求成功",
  "sign":"29DAQWEFSA...",
  "nonce":"QWEWQEDFSAVCBCOI...",
  "merchantId":"shunca0002",
  "payUrl":"http://gateway.ykbpay.com/pay/",
  "transaction_id":"qwe084524312"
}
```

### 2.4 网银查询订单支付

- **接口说明：** 银行查询订单支付
- **请求方式：**POST 
- **接口地址：** http://gateway.ykbpay.com:8080/caishunpay-web/bankCardPay/queryOrder

#### 2.4.1 请求参数

| 参数名称       | 类型     | 描述                                       |
| ---------- | :----- | ---------------------------------------- |
| merchantId | string | 商户号                                      |
| tradeSn    | string | 商户系统订单号                                  |
| sign       | string | 签名串 |
| nonce      | int    | 随机数                                      |

请求示例：
```
{
  "merchantid":"shunca0002",
  "tradeSn":"test14",
  "sign":"CF1D05DA8CA4095...",
  "nonce":1234,
}
```

#### 2.4.2 响应参数

| 参数名称        | 类型     | 描述      |
| ----------- | :----- | ------- |
| sign        | string |     签名串    |
| nonce       | string |    随机字符串     |
| merchantId  | string | 商户号     |
| tradeSn     | string | 商户系统订单号 |
| orderAmount | int    | 金额（单位分） |
| tradeState  | string |   交易状态      |
| carType     | string | 卡类型     |
| bankName    | string | 银行名称    |
| channelType | string |   1 PC端  2 移动端      |

响应示例：
```
{
  "sign":"29DAQWEFSA...",
  "nonce":"QWEWQEDFSAVCBCOI...",
  "merchantId":"caishun0002",
  "tradeSn":"test12",
  "orderAmount":22,
  "tradeState":"NOTPAY",
  "carType":"01",
  "bankName":"招商银行",
  "channelType":"1"
}
```

### 2.5 支付结果异步回调

方式： HTTP POST请求
Encoding : application/json

示例:
```
{
  "sign":"29DAQWEFSA...",
  "nonce":"QWEWQEDFSAVCBCOI...",
  "merchantId":"caishun0002",
  "tradeSn":"test12",
  "orderAmount":22,
  "tradeState":"NOTPAY"
}
```

| 参数名称        | 类型     | 描述      |
| ----------- | :----- | ------- |
| sign        | string |    签名字符串     |
| nonce       | string |    随机字符串     |
| merchantId  | string | 商户号     |
| tradeSn     | string | 商户系统订单号 |
| orderAmount | int    | 金额（单位分） |
| tradeState  | string |    交易状态  NOTPAY 未支付 SUCCESS 支付成功    |

应返回 *success* 字符串来响应回调



## 3 加密方式

为了保证数据传输过程中的数据真实性和完整性，我们需要对数据进行数字签名，在接收签名数据之后必须进行签名校验。
数字签名有两个步骤，先按一定规则拼接要签名的原始串，再选择具体的算法和密钥计算出签名结果。
一般失败的结果不签名。

### 3.1 签名原始串

无论是请求还是应答，签名原始串按以下方式组装成字符串:

1、除sign 字段外，所有参数按照字段名的ascii码从小到大排序后使用QueryString 的格式(即 key1=value1&key2=value2...)拼接而成，空值不传递，不参与签名组串。
2、 签名原始串中，字段名和字段值都采用原始值，不进行 URLEncode。
3、 平台返回的应答或通知消息可能会由于升级增加参数，请验证应答签名时注意允许这种情况。

### 3.2 签名算法
MD5 签名
MD5 是一种摘要生成算法，通过在签名原始串后加上商户通信密钥的内容，进行 MD5 运算， 形成的摘要字符串即为签名结果。为了方便比较，签名结果统一转换为大写字符。
注意:签名时将字符串转化成字节流时指定的编码字符集应与参数 charset 一致。
MD5 签名计算公式:

sign Md5(原字符串&key=商户密钥).toUpperCase 

假设商户密钥为:sLbAsG00RWs1eF13juevu5WfEFLDSe0c，商户ID为1000001221


i:经过 a 过程 URL 键值对字典序排序后的字符串 string1 为: 
```
merchantId=1000001221&message=请求成功&orderAmount=2&resultCode=00000&tradeSn=222227&tradeState=SUCCESS&transaction_id=5GQjTH1y4xNaii64PMkApg
```

ii:经过 b 过程后得到 sign 为: 
sign=md5(string1&key=sLbAsG00RWs1eF13juevu5WfEFLDSe0c).toUpperCase
= 2274DE08CAB2FB0ABB44A28D8DA45561
