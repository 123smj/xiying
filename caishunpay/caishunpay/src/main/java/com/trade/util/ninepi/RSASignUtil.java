/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 *  org.bouncycastle.util.encoders.Base64
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.trade.util.ninepi;

import com.trade.util.ninepi.CAP12CertTool;
import com.trade.util.ninepi.HexStringByte;
import com.trade.util.ninepi.MrpException;
import com.trade.util.ninepi.RpmVerifyService;
import com.trade.util.ninepi.SecurityException;
import com.trade.util.ninepi.ServiceNotSpecifiedException;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RSASignUtil {
    private String service = null;
    private String certFilePath = null;
    private String password = null;
    private String hexCert = null;
    private String rootCertPath = null;
    private static final Logger log = LoggerFactory.getLogger(RSASignUtil.class);

    public static void main(String[] arg) throws Exception {
        RSASignUtil util = new RSASignUtil("/data/logs/paygateway/p12/800001305910001.p12", "WSmcha");
        String oriData = "signType=RSA256&mcTransDateTime=20170626113934&orderSts=P&serverSign=79C0FC8AB6F1533F5F16AEFF21DCA625BD4A660B9E7EB772A8D9ECF1AAFE7E2049DCDFA30FC394A971C381880F3F2B039C1AF19B2FF4DC91EC68FF9F5E28EFCF3C7CECE397179674AC84658C7A79ECCAB3C5484D70D4143B28D10E07B0EC04843A12F5D7C54A9EF487FCFCAAB0D9725C21137B7839C3AA0834186783E0651BF1&remark1=null&remark3=null&version=1.0&remark2=null&cardNo=9c731857e615e24f779638b4d1d04197906ad91d4dbb3063&amount=200&transDate=20170626&responseId=20170626113929&transTime=113929&merchantId=800001305910001&responseTime=20170626113929&rspCode=CAP00000&requestId=2017062611393401&charset=00&rspMessage=\u6210\u529f&orderNo=20170626113934&bfbSequenceNo=170623160247715349&serverCert=308203653082024DA00302010202081BF6C2A9D997293C300D06092A864886F70D0101050500305B310B300906035504061302636E310B300906035504080C02626A3110300E06035504070C076265696A696E67310F300D060355040A0C064D75726F6E67310B3009060355040B0C024954310F300D06035504030C06726F6F744341301E170D3135303131393130303530365A170D3435303131393130303530365A305A310B300906035504061302636E310B300906035504080C02626A3110300E06035504070C076265696A696E67310F300D060355040A0C064D75726F6E67310B3009060355040B0C024954310E300C06035504030C05506179434130819F300D06092A864886F70D010101050003818D0030818902818100CEA9A82C9F5E6BF6AE9426EAF879CF0B8614B1C59818B841DBEE3BD44A09F03DEBD59871D975BFE42F19177342D00988A13912B153F0CC72C16EBD23C8596610F45978797414C5D211FB3DE1DA3944A8D75E7A0749EFE2E36DC46FCC42A373FFCD8F4B1C69E6D9789EEB51BF01AABE43E940652DE8F890004A965B07E49A614D0203010001A381B13081AE301D0603551D0E0416041439E6E108F96F61A1FBEA466C406666E545742BD530818C0603551D23048184308181801407C69254961EB26785D105D969445BF72CF3CCA7A15FA45D305B310B300906035504061302636E310B300906035504080C02626A3110300E06035504070C076265696A696E67310F300D060355040A0C064D75726F6E67310B3009060355040B0C024954310F300D06035504030C06726F6F74434182081BF6C2A9D76A7CF2300D06092A864886F70D010105050003820101003498678C1547E2D86CCBEE81971E54221EFBDFFF85F3BAA9BB5C6EC217E02DDD48641B132BB4AAF87BE91461304F7457554465F767D29E61266DC0212F8D1DAE06743B6D62125BE989DF80B889FCA6DC25D67F28581A8C44B400D754F3B0542F56603432131E690F8FC4D632717F914A199E080FABECCD4D931EF4288330B4E9CF684409E3CA2D026EDDD4DFC473C7ADEE759E15CD2937CA95DC2F347FAE4E67257CE9941E361EBAAEC1445CB01DBE74E3BDFF62E4FF6D378C0559E4D75AD42C10B78A1999280EA9A5C3FCE22808A722D3D9C9E8929CF02D0CDB7938E30478FBE21D5F53E7A1646F3510A6A4E062748453B23620273927D0F8A3A8622714DAEB&mcSequenceNo=1498448374019";
        String serverSign = "79C0FC8AB6F1533F5F16AEFF21DCA625BD4A660B9E7EB772A8D9ECF1AAFE7E2049DCDFA30FC394A971C381880F3F2B039C1AF19B2FF4DC91EC68FF9F5E28EFCF3C7CECE397179674AC84658C7A79ECCAB3C5484D70D4143B28D10E07B0EC04843A12F5D7C54A9EF487FCFCAAB0D9725C21137B7839C3AA0834186783E0651BF1";
        String serverCert = "308203653082024DA00302010202081BF6C2A9D997293C300D06092A864886F70D0101050500305B310B300906035504061302636E310B300906035504080C02626A3110300E06035504070C076265696A696E67310F300D060355040A0C064D75726F6E67310B3009060355040B0C024954310F300D06035504030C06726F6F744341301E170D3135303131393130303530365A170D3435303131393130303530365A305A310B300906035504061302636E310B300906035504080C02626A3110300E06035504070C076265696A696E67310F300D060355040A0C064D75726F6E67310B3009060355040B0C024954310E300C06035504030C05506179434130819F300D06092A864886F70D010101050003818D0030818902818100CEA9A82C9F5E6BF6AE9426EAF879CF0B8614B1C59818B841DBEE3BD44A09F03DEBD59871D975BFE42F19177342D00988A13912B153F0CC72C16EBD23C8596610F45978797414C5D211FB3DE1DA3944A8D75E7A0749EFE2E36DC46FCC42A373FFCD8F4B1C69E6D9789EEB51BF01AABE43E940652DE8F890004A965B07E49A614D0203010001A381B13081AE301D0603551D0E0416041439E6E108F96F61A1FBEA466C406666E545742BD530818C0603551D23048184308181801407C69254961EB26785D105D969445BF72CF3CCA7A15FA45D305B310B300906035504061302636E310B300906035504080C02626A3110300E06035504070C076265696A696E67310F300D060355040A0C064D75726F6E67310B3009060355040B0C024954310F300D06035504030C06726F6F74434182081BF6C2A9D76A7CF2300D06092A864886F70D010105050003820101003498678C1547E2D86CCBEE81971E54221EFBDFFF85F3BAA9BB5C6EC217E02DDD48641B132BB4AAF87BE91461304F7457554465F767D29E61266DC0212F8D1DAE06743B6D62125BE989DF80B889FCA6DC25D67F28581A8C44B400D754F3B0542F56603432131E690F8FC4D632717F914A199E080FABECCD4D931EF4288330B4E9CF684409E3CA2D026EDDD4DFC473C7ADEE759E15CD2937CA95DC2F347FAE4E67257CE9941E361EBAAEC1445CB01DBE74E3BDFF62E4FF6D378C0559E4D75AD42C10B78A1999280EA9A5C3FCE22808A722D3D9C9E8929CF02D0CDB7938E30478FBE21D5F53E7A1646F3510A6A4E062748453B23620273927D0F8A3A8622714DAEB";
        Map responseMap = RSASignUtil.coverString2Map(oriData);
        oriData = RSASignUtil.coverMap2String(responseMap);
        System.out.println("oriData is:" + oriData);
        boolean verifyResult = util.verify(oriData, serverSign, serverCert, "GB18030");
        System.out.println("verifyResult is1:" + verifyResult);
        responseMap.remove("charset");
        responseMap.remove("requestId");
        responseMap.remove("responseId");
        responseMap.remove("responseTime");
        oriData = RSASignUtil.coverMap2String(responseMap);
        verifyResult = util.verify(oriData, serverSign, serverCert, "GB18030");
        System.out.println("verifyResult is2:" + verifyResult);
    }

    public RSASignUtil(String certFilePath, String password) {
        this.certFilePath = certFilePath;
        this.password = password;
    }

    public RSASignUtil(String rootCertPath) {
        this.rootCertPath = rootCertPath;
    }

    public RSASignUtil() {
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public String sign(String indata, String encoding) throws Exception {
//        block9 : {
//            serverSign = null;
//            if (StringUtils.isBlank((CharSequence)encoding)) {
//                encoding = "GBK";
//            }
//            RSASignUtil.log.info("[{}]", (Object)indata);
//            signMap = RSASignUtil.coverString2Map(indata);
//            rvs = new RpmVerifyService();
//            try {
//                rvs.execute(signMap, this.service);
//                singData = new CAP12CertTool(this.certFilePath, this.password);
//                cert = singData.getCert();
//                si = singData.getSignData(indata.getBytes(encoding));
//                cr = cert.getEncoded();
//                this.hexCert = HexStringByte.byteToHex(cr);
//                return HexStringByte.byteToHex(si);
//            }
//            catch (CertificateEncodingException var8) {
//                RSASignUtil.log.error("Certificate encoding exception!", (Throwable)var8);
//                return serverSign;
//            }
//            catch (FileNotFoundException var9) {
//                RSASignUtil.log.error("File not found exception", (Throwable)var9);
//                return serverSign;
//            }
//            catch (SecurityException var10) {
//                RSASignUtil.log.error("Security exception", (Throwable)var10);
//                return serverSign;
//            }
//            catch (MrpException mrp) {
//                messageMap = mrp.getMessageMap();
//                if (!messageMap.containsKey(1)) break block9;
//                messageList = messageMap.get(1);
//                messageBuilder = new StringBuilder();
//                i = 0;
//                ** GOTO lbl36
//            }
//            catch (ServiceNotSpecifiedException snse) {
//                RSASignUtil.log.error("Service is null");
//            }
//            return serverSign;
//lbl-1000: // 1 sources:
//            {
//                messageBuilder.append(messageList.get(i)).append("=?&");
//                ++i;
//lbl36: // 2 sources:
//                ** while (i < messageList.size())
//            }
//lbl37: // 1 sources:
//            res = messageBuilder.substring(0, messageBuilder.length() - 1).toString();
//            RSASignUtil.log.error("Non empty fields are not filled:[{}]", (Object)res);
//        }
//        if (messageMap.containsKey(2) == false) return serverSign;
//        messageList = messageMap.get(2);
//        messageBuilder = new StringBuilder();
//        i = 0;
//        while (i < messageList.size()) {
//            messageBuilder.append(messageList.get(i)).append("&");
//            ++i;
//        }
//        res = messageBuilder.substring(0, messageBuilder.length() - 1).toString();
//        RSASignUtil.log.error("Multi pass parameter:[{}]", (Object)res);
//        return serverSign;
        return null;
    }

    public String getCertInfo() {
        return this.hexCert;
    }

    public boolean verify(String oridata, String signData, String svrCert, String encoding) throws UnsupportedEncodingException {
        boolean res = false;
        if (StringUtils.isBlank((CharSequence)encoding)) {
            encoding = "GBK";
        }
        try {
            X509Certificate cert;
            byte[] e = HexStringByte.hexToByte(signData.getBytes());
            byte[] inDataBytes = oridata.getBytes(encoding);
            byte[] signaturepem = RSASignUtil.checkPEM(e);
            if (signaturepem != null) {
                e = Base64.decode((byte[])signaturepem);
            }
            if ((cert = this.getCertFromHexString(svrCert)) != null) {
                PublicKey pubKey = cert.getPublicKey();
                Signature signet = Signature.getInstance("SHA256WITHRSA");
                signet.initVerify(pubKey);
                signet.update(inDataBytes);
                res = signet.verify(e);
            }
        }
        catch (InvalidKeyException var12) {
            log.error("Invalid key exception", (Throwable)var12);
        }
        catch (NoSuchAlgorithmException var13) {
            log.error("No such algorithm exception", (Throwable)var13);
        }
        catch (SignatureException var14) {
            log.error("Signature exception", (Throwable)var14);
        }
        catch (SecurityException var15) {
            log.error("Security exception", (Throwable)var15);
        }
        return res;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private X509Certificate getCertFromHexString(String hexCert) throws SecurityException {
        ByteArrayInputStream bIn = null;
        X509Certificate certobj = null;
        try {
            try {
                byte[] e = HexStringByte.hexToByte(hexCert.getBytes());
                CertificateFactory fact = CertificateFactory.getInstance("X.509");
                bIn = new ByteArrayInputStream(e);
                certobj = (X509Certificate)fact.generateCertificate(bIn);
                bIn.close();
                bIn = null;
                return certobj;
            }
            catch (CertificateException var16) {
                log.error("Certificate exception", (Throwable)var16);
                try {
                    if (bIn == null) return certobj;
                    bIn.close();
                    return certobj;
                }
                catch (IOException iOException) {}
                return certobj;
            }
            catch (IOException var17) {
                try {
                    log.error("IO exception", (Throwable)var17);
                }
                catch (Throwable throwable) {}
                try {
                    if (bIn == null) return certobj;
                    bIn.close();
                    return certobj;
                }
                catch (IOException iOException) {}
                return certobj;
//                throw throwable;
            }
        }
        finally {
            try {
                if (bIn != null) {
                    bIn.close();
                }
            }
            catch (IOException iOException) {}
        }
    }

    public static byte[] checkPEM(byte[] paramArrayOfByte) {
        String str1 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789/+= \r\n-";
        int localStringBuffer = 0;
        while (localStringBuffer < paramArrayOfByte.length) {
            if (str1.indexOf(paramArrayOfByte[localStringBuffer]) == -1) {
                return null;
            }
            ++localStringBuffer;
        }
        StringBuffer var5 = new StringBuffer(paramArrayOfByte.length);
        String str2 = new String(paramArrayOfByte);
        int j = 0;
        while (j < str2.length()) {
            if (str2.charAt(j) != ' ' && str2.charAt(j) != '\r' && str2.charAt(j) != '\n') {
                var5.append(str2.charAt(j));
            }
            ++j;
        }
        return var5.toString().getBytes();
    }

    public String getFormValue(String respMsg, String name) {
        String[] resArr = StringUtils.split((String)respMsg, (String)"&");
        HashMap<String, String> resMap = new HashMap<String, String>();
        int i = 0;
        while (i < resArr.length) {
            String data = resArr[i];
            int index = StringUtils.indexOf((CharSequence)data, (int)61);
            String nm = StringUtils.substring((String)data, (int)0, (int)index);
            String val = StringUtils.substring((String)data, (int)(index + 1));
            resMap.put(nm, val);
            ++i;
        }
        return (String)resMap.get(name) == null ? "" : (String)resMap.get(name);
    }

    public static String getValue(String respMsg, String name) {
        String[] resArr = StringUtils.split((String)respMsg, (String)"&");
        HashMap<String, String> resMap = new HashMap<String, String>();
        int i = 0;
        while (i < resArr.length) {
            String data = resArr[i];
            int index = StringUtils.indexOf((CharSequence)data, (int)61);
            String nm = StringUtils.substring((String)data, (int)0, (int)index);
            String val = StringUtils.substring((String)data, (int)(index + 1));
            resMap.put(nm, val);
            ++i;
        }
        return (String)resMap.get(name) == null ? "" : (String)resMap.get(name);
    }

    public static Map coverString2Map(String respMsg) {
        String[] resArr = StringUtils.split((String)respMsg, (String)"&");
        HashMap<String, String> resMap = new HashMap<String, String>();
        int i = 0;
        while (i < resArr.length) {
            String data = resArr[i];
            int index = StringUtils.indexOf((CharSequence)data, (int)61);
            String nm = StringUtils.substring((String)data, (int)0, (int)index);
            String val = StringUtils.substring((String)data, (int)(index + 1));
            resMap.put(nm, val);
            ++i;
        }
        return resMap;
    }

    public static String coverMap2String(Map<String, String> data) {
        TreeMap<String, String> tree = new TreeMap<String, String>();
        for (Map.Entry<String, String> sf : data.entrySet()) {
            String en = "";
            if ("merchantSign".equals(sf.getKey().trim()) || "serverSign".equals(sf.getKey().trim()) || "serverCert".equals(sf.getKey().trim()) || "null".equals(sf.getValue()) || sf.getValue() == null) continue;
            tree.put(sf.getKey(), sf.getValue());
        }
        Iterator it = tree.entrySet().iterator();
        StringBuffer sf1 = new StringBuffer();
        while (it.hasNext()) {
            Map.Entry en1 = (Map.Entry)it.next();
            sf1.append(String.valueOf((String)en1.getKey()) + "=" + (String)en1.getValue() + "&");
        }
        return sf1.substring(0, sf1.length() - 1);
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getService() {
        return this.service;
    }
}
