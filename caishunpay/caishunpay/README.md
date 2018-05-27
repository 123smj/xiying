
*Attention: 在IDE中配置成 test Maven Profile 进行开发! 严禁使用prod Profile进行开发!*

### 工程结构

源代码 : src/main/java 

配置文件 : src/main/resource

测试环境配置文件 : src/main/resource-test

正式环境配置文件 : src/main/resource-prd

Hibernate映射 : src/main/resources/hbm

Spring上下文 : src/main/resources/spring

JSP : src/main/webapp

Web描述文档 : src/main/webapp/WEB-INF/web.xml

主页 : src/main/webapp/manage/login.jsp



### 数据库表

1. TRADE_FUNC_INF 功能列表

2. TRADE_ROLE_FUNC_MAP 用户功能映射表

3. TRADE_MCHTROLE_FUNC_MAP 商户功能映射表

4. TRADE_QRCODE_MCHT_INFO 商户表

5. TRADE_OPR_INFO 管理员/公司 用户表

6. TRADE_QRCODE_CHANNEL_INF 支付通道表

7. TRADE_QUICKPAY_INF 银行卡支付账目表(目前未使用)

8. TRADE_WXPAY_INF 三方支付账目表(目前主要使用这个，核心表)

9. TRADE_JUMP_LIST 跳码组表

10. CST_SYS_PARAM 系统参数设置表


