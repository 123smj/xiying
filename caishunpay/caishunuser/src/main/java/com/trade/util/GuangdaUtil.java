/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.RandomStringUtils
 *  org.apache.log4j.Logger
 */
package com.trade.util;

import com.gy.util.MD5Encrypt;
import com.gy.util.MapKeyComparator;
import com.gy.util.MapValueComparator;
import com.gy.util.StringUtil;
import com.gy.util.UUIDGenerator;
import com.trade.bean.response.QuickpayResponse;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;

public class GuangdaUtil {
    private static Logger log = Logger.getLogger(GuangdaUtil.class);

    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        TreeMap<String, String> sortMap = new TreeMap<String, String>(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }

    public static /* varargs */ String md5Sign(Map<String, String> param, String secretKey, String... charset) {
        if (param == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        String value = "";
        for (Map.Entry<String, String> entry : param.entrySet()) {
            value = entry.getValue();
            if (!StringUtil.isNotEmpty(value)) continue;
            sb.append(entry.getKey()).append("=").append(value).append("&");
        }
        sb.append("key=" + secretKey);
        System.out.println("\u52a0\u5bc6\u524d\uff1a" + sb);
        log.info((Object) ("\u52a0\u5bc6\u524d\uff1a" + sb));
        String signValue = null;
        try {
            signValue = MD5Encrypt.getMessageDigest(sb.toString(), charset).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info((Object) ("\u7cfb\u7edf\u6821\u9a8c\u7b7e\u540d\uff1a" + signValue));
        return signValue;
    }

    public static /* varargs */ String md5KeyedSign(Map<String, String> param, String secretKey, String... charset) {
        if (param == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        String value = "";
        for (Map.Entry<String, String> entry : param.entrySet()) {
            value = entry.getValue();
            if (!StringUtil.isNotEmpty(value)) continue;
            sb.append(entry.getKey()).append("=").append(value).append("&");
        }
        String signData = sb.substring(0, sb.length() - 1);
        System.out.println("\u52a0\u5bc6\u524d\uff1a" + signData);
        log.info((Object) ("\u52a0\u5bc6\u524d\uff1a" + signData));
        String signValue = null;
        try {
            signValue = MD5Encrypt.getKeyedDigest(signData, secretKey, charset);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info((Object) ("\u7cfb\u7edf\u6821\u9a8c\u7b7e\u540d\uff1a" + signValue));
        return signValue;
    }

    public static /* varargs */ String md5SignWithValue(Map<String, String> param, String secretKey, String... charset) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : param.entrySet()) {
            sb.append(entry.getValue());
        }
        sb.append(secretKey);
        String signValue = null;
        System.out.println("\u6392\u5e8f\u540e\uff1a" + sb);
        try {
            signValue = MD5Encrypt.getMessageDigest(sb.toString(), charset).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return signValue;
    }

    public static /* varargs */ String md5SignHelibao(Map<String, String> param, String secretKey, String... charset) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : param.entrySet()) {
            sb.append("&").append(entry.getValue());
        }
        sb.append("&" + secretKey);
        String signValue = null;
        System.out.println("\u6392\u5e8f\u540e\uff1a" + sb);
        try {
            signValue = MD5Encrypt.getMessageDigest(sb.toString(), charset).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return signValue;
    }

    public static String buildHelibaoData4Sign(Map<String, String> param) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : param.entrySet()) {
            sb.append("&").append(entry.getValue());
        }
        System.out.println("\u7b7e\u540d\u5b57\u7b26\u4e32" + sb.toString());
        return sb.toString();
    }

    public static String map2HttpParam(Map<String, String> param) {
        StringBuffer sb = new StringBuffer();
        boolean isFirst = true;
        for (Map.Entry<String, String> entry : param.entrySet()) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append("&");
            }
            sb.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return sb.toString();
    }

    public static Map<String, String> httpParam2Map(String keyValueStr) {
        if (keyValueStr == null) {
            return null;
        }
        HashMap<String, String> paramMap = new HashMap<String, String>();
        String[] keyValues = keyValueStr.split("&");
        int i = 0;
        while (i < keyValues.length) {
            String[] values = keyValues[i].split("=", 2);
            if (values != null) {
                if (values.length == 1) {
                    paramMap.put(values[0], null);
                }
                if (values.length >= 2) {
                    paramMap.put(values[0], values[1]);
                }
            }
            ++i;
        }
        return paramMap;
    }

    public static String getRandom(int length) {
        return RandomStringUtils.randomAlphanumeric((int) length);
    }

    public static String fillString(String str, char fill, int len, boolean isfillEnd) {
        if (str == null) {
            str = "";
        }
        int fillLen = len - str.getBytes().length;
        if (len <= 0) {
            return str;
        }
        if (fillLen <= 0) {
            str = str.substring(str.getBytes().length - len);
        } else {
            int i = 0;
            while (i < fillLen) {
                str = isfillEnd ? String.valueOf(str) + fill : String.valueOf(fill) + str;
                ++i;
            }
        }
        return str;
    }

    public static Map<String, String> sortMapByValue(Map<String, String> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        LinkedHashMap<String, String> sortedMap = new LinkedHashMap<String, String>();
        ArrayList<Map.Entry<String, String>> entryList = new ArrayList<Map.Entry<String, String>>(oriMap.entrySet());
        Collections.sort(entryList, new MapValueComparator());
        Iterator<Map.Entry<String, String>> iter = entryList.iterator();
        Map.Entry<String, String> tmpEntry = null;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        }
        return sortedMap;
    }

    public static Map<String, String> sortMapByChinaValue(Map<String, String> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        LinkedHashMap<String, String> sortedMap = new LinkedHashMap<String, String>();
        ArrayList<String> entryList = new ArrayList<String>(oriMap.keySet());
        Collections.sort(entryList, Collator.getInstance(Locale.CHINA));
        Iterator<String> iter = entryList.iterator();
        String tmpEntry = null;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
            sortedMap.put(tmpEntry, oriMap.get(tmpEntry));
        }
        return sortedMap;
    }

    public static String getMd5Sign(Object obj, String secretKey) {
        Map<String, String> param = new LinkedHashMap<>();
        try {
            BeanInfo info = Introspector.getBeanInfo(obj.getClass());
            for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
                Method reader = pd.getReadMethod();
                if (reader != null && !pd.getName().equals("class")) {
                    Object val = reader.invoke(obj);
                    if (val != null)
                        param.put(pd.getName(), val.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return GuangdaUtil.md5Sign(GuangdaUtil.sortMapByKey(param), secretKey, new String[0]);
    }

    public static /* varargs */ String getMd5SignByMap(Map<String, String> map, String secretKey, String... charset) {
        return GuangdaUtil.md5Sign(GuangdaUtil.sortMapByKey(map), secretKey, charset);
    }

    public static /* varargs */ String getMd5KeyedSignByMap(Map<String, String> map, String secretKey, String... charset) {
        return GuangdaUtil.md5KeyedSign(GuangdaUtil.sortMapByKey(map), secretKey, charset);
    }

    public static /* varargs */ String getMd5SignWithValueByMap(Map<String, String> map, String secretKey, String... charset) {
        return GuangdaUtil.md5SignWithValue(GuangdaUtil.sortMapByKey(map), secretKey, charset);
    }

    public static /* varargs */ String getHelibaoMd5Sign(Map<String, String> map, String secretKey, String... charset) {
        return GuangdaUtil.md5SignHelibao(map, secretKey, charset);
    }

    public static /* varargs */ String getMd5SignByMapValueSort(Map<String, String> map, String secretKey, String... charset) {
        return GuangdaUtil.md5SignWithValue(GuangdaUtil.sortMapByValue(map), secretKey, charset);
    }

    public static String wezbankSign(List<String> values, String signTicket) {
        if (values == null) {
            throw new NullPointerException("values is null");
        }
        values.removeAll(Collections.singleton(null));
        values.add(signTicket);
        Collections.sort(values);
        StringBuilder sb = new StringBuilder();
        for (String s : values) {
            sb.append(s);
        }
        log.info((Object) ("wezbankSign---\u6392\u5e8f\u540e: " + values));
        try {
            MessageDigest md = MessageDigest.getInstance("sha1");
            md.update(sb.toString().getBytes("UTF-8"));
            String sign = StringUtil.byte2hex(md.digest());
            return sign;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static /* varargs */ String getMd5SignByMapChinaSort(Map<String, String> map, String secretKey, String... charset) {
        return GuangdaUtil.md5SignWithValue(GuangdaUtil.sortMapByChinaValue(map), secretKey, charset);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        QuickpayResponse backNotify = new QuickpayResponse();
        backNotify.setBankName("\u519c\u4e1a\u94f6\u884c");
        backNotify.setCardType("01");
        backNotify.setChannelType("1");
        backNotify.setGymchtId("fzh001170001");
        backNotify.setNonce("i3iJHt6V3yKq67F5mOPHyKyP96Tz3efc");
        backNotify.setOrderAmount("1000000");
        backNotify.setOut_transaction_id("8021800920573170526085789431");
        backNotify.setTimeEnd("20170526181633");
        backNotify.setTradeSn("20170526000000001345");
        backNotify.setTradeState("NOTPAY");
        backNotify.setTransaction_id("40012017052600000696828199");
        String checksign = GuangdaUtil.getMd5Sign(backNotify, "35e1d5f97e404606a700b7a9c237007a");
        System.out.println("checksign:" + checksign);
        System.out.println(UUIDGenerator.getUUID());
        String check = "amount=1&orderId=D0100112017703415&pay_number=20170525000463001795";
        String sign = MD5Encrypt.getMessageDigest(check, "UTF-8").toUpperCase();
        System.out.println("sign:" + sign);
        String[] zh = new String[]{"CZ2017050100001270778899", "CZ2017050100000535361588", "CZ2017050100000749313655", "CZ2017050100001789931504", "CZ2017050500001702020806", "CZ2017050500000557008752", "CZ2017050500001201091578", "CZ2017050500000582833303", "CZ2017050800000831405965", "CZ2017050800001063677561", "CZ2017050800000488856749", "CZ2017050800000158985074", "CZ2017050800002098293889", "CZ2017050800001176144857", "CZ2017050800002025438374", "CZ2017050800002069010308", "CZ2017050800001784422031", "CZ2017050800000408950804", "CZ2017050800000548808964", "CZ2017050800000682480780", "CZ2017050800000256462565", "CZ2017050800000963317778", "CZ2017050800000428660776", "CZ2017050800000784909688", "CZ2017050800002052953811", "CZ2017050800001473077483", "CZ2017050800001973001379", "CZ2017050800000105086778", "CZ2017050800001228654649", "CZ2017050800000708844406", "CZ2017050800001371452067", "CZ2017050800000581827821", "CZ2017050800001120886110", "CZ2017050800000231158007", "CZ2017050800001438260207", "CZ2017050800001322542991", "CZ2017050800000585355992", "CZ2017050800001232876836", "CZ2017050800002089630367", "CZ2017050800002074605370", "CZ2017050800001880552528", "CZ2017050800000582815376", "CZ2017050800001214891056", "CZ2017050800001112441956", "CZ2017050800000294890425", "CZ2017050800001962981079", "CZ2017050800000788707045", "CZ2017050800000603236323", "CZ2017050800001817554613", "CZ2017050800000312924097", "CZ2017050800000032840305", "CZ2017050800002056709823", "CZ2017050800001525243199", "CZ2017050800000390021079", "CZ2017050800001709343577", "CZ2017050800001536427391", "CZ2017050800000079384433", "CZ2017050800000341942480", "CZ2017050800002034678765", "CZ2017050800001758019801", "CZ2017050800000707664023", "CZ2017050800000448744972", "CZ2017050800000624804388", "CZ2017050800002000782195", "CZ2017050800001151212241", "CZ2017050800000795811953", "CZ2017050800000191301317", "CZ2017050800000239142241", "CZ2017050800001122350158", "CZ2017050900000520219548", "CZ2017050900000317086881", "CZ2017050900000218927778", "CZ2017050900000037769208", "CZ2017050900001111262844", "CZ2017050900000189803901", "CZ2017050900000126451729", "CZ2017050900000553003716", "CZ2017050900000378484297", "CZ2017050900001081141556", "CZ2017050900001228151221", "CZ2017050900001803315693", "CZ2017050900000907014207", "CZ2017050900000125631234", "CZ2017050900002009464934", "CZ2017050900000738039782", "CZ2017050900001463640783", "CZ2017050900001330317578", "CZ2017050900000681566895", "CZ2017050900001383476136", "CZ2017050900000664876967", "CZ2017050900001073909287", "CZ2017050900001126468814", "CZ2017050900001883264463", "CZ2017050900000539952305", "CZ2017050900000176162980", "CZ2017050900000353009915", "CZ2017050900001618444733", "CZ2017050900000110911422", "CZ2017050900001505250243", "CZ2017050900000031787554", "CZ2017050900000230998578", "CZ2017050900000230017838", "CZ2017050900000481969576", "CZ2017050900002119257626", "CZ2017050900001531200380", "CZ2017050900000771513526", "CZ2017050900002024555785", "CZ2017050900000996586770", "CZ2017050900001427300076", "CZ2017050900000213312534", "CZ2017050900001933899828", "CZ2017050900000267352595", "CZ2017050900001366977401", "CZ2017050900000361443618", "CZ2017050900001331992083", "CZ2017050900001626864312", "CZ2017050900002004141812", "CZ2017050900000207604046", "CZ2017050900001419683607", "CZ2017050900001910050527", "CZ2017050900001278957538", "CZ2017050900001677694340", "CZ2017050900000618184532", "CZ2017050900000865153555", "CZ2017050900000445736056", "CZ2017050900002027654146", "CZ2017050900001184782196", "CZ2017050900000111962573", "CZ2017050900001770160819", "CZ2017050900001378555383", "CZ2017050900000973934954", "CZ2017050900001591265472", "CZ2017050900001755440885", "CZ2017050900000380542539", "CZ2017050900001390413349", "CZ2017050900000176644624", "CZ2017050900000570019586", "CZ2017050900000446101260", "CZ2017050900001194766538", "CZ2017050900001812553941", "CZ2017050900000225561038", "CZ2017050900000634309773", "CZ2017050900001346663442", "CZ2017050900000108958201", "CZ2017050900001149964737", "CZ2017050900000748834219", "CZ2017050900002092973697", "CZ2017050900000771567059", "CZ2017050900000530686263", "CZ2017050900001310727377", "CZ2017050900001309947649", "CZ2017050900000926611590", "CZ2017050900001709541926", "CZ2017050900000189038172", "CZ2017050900000570317599", "CZ2017050900002091631192", "CZ2017050900002137398093", "CZ2017050900000047494494", "CZ2017050900001396156876", "CZ2017050900001692262936", "CZ2017050900000817006686", "CZ2017050900000451309142", "CZ2017050900000333676428", "CZ2017050900001360819946", "CZ2017050900000837232125", "CZ2017050900000556023908", "CZ2017050900000188269210", "CZ2017050900000851321177", "CZ2017050900000856341679", "CZ2017051000001987302804", "CZ2017051000000264116811", "CZ2017051000001969798729", "CZ2017051000002002289573", "CZ2017051000001267813988", "CZ2017051000002057224633", "CZ2017051000001726479406", "CZ2017051000001100757755", "CZ2017051000000891340457", "CZ2017051000000625284122", "CZ2017051000000668137928", "CZ2017051000002105108102", "CZ2017051000001960673272", "CZ2017051000001997384022", "CZ2017051000000998964093", "CZ2017051000000311029575", "CZ2017051000000975018363", "CZ2017051000002080090689", "CZ2017051000000442639495", "CZ2017051000001396802961", "CZ2017051000000071623618", "CZ2017051000000682734763", "CZ2017051000001497404394", "CZ2017051000000336829218", "CZ2017051000000292538518", "CZ2017051000000266255145", "CZ2017051000000680110801", "CZ2017051000000981725845", "CZ2017051000001543648750", "CZ2017051000001773438858", "CZ2017051000001255391932", "CZ2017051000001502241385", "CZ2017051000002002757128", "CZ2017051000001742804303", "CZ2017051000001171465909", "CZ2017051000001092774319", "CZ2017051000002116228361", "CZ2017051000000440057397", "CZ2017051000000762387073", "CZ2017051000000852749756", "CZ2017051000001934569038", "CZ2017051000002104788123", "CZ2017051000001048750974", "CZ2017051000001146472006", "CZ2017051000001666425141", "CZ2017051000000277888034", "CZ2017051000000031375814", "CZ2017051000000667932351", "CZ2017051000000457060201", "CZ2017051000000003692857", "CZ2017051000001228269247", "CZ2017051000000312884474", "CZ2017051000001191880772", "CZ2017051000000813998426", "CZ2017051000000258593142", "CZ2017051000000914845729", "CZ2017051000000581744377", "CZ2017051000001325346820", "CZ2017051000001555724573", "CZ2017051000001899359631", "CZ2017051000002068824475", "CZ2017051000001839571357", "CZ2017051000001754507915", "CZ2017051000001044404002", "CZ2017051000001465142799", "CZ2017051000001042721886", "CZ2017051000000669593943", "CZ2017051000001529874701", "CZ2017051000000204879243", "CZ2017051000002080333362", "CZ2017051000000429426920", "CZ2017051000001517155149", "CZ2017051000001815207735", "CZ2017051000001692444364", "CZ2017051000002116541892", "CZ2017051000000344856762", "CZ2017051000001196036095", "CZ2017051000001646363792", "CZ2017051000001811467631", "CZ2017051000001697612112", "CZ2017051000001881645720", "CZ2017051000002040752909", "CZ2017051000001051152652", "CZ2017051000002084899097", "CZ2017051000001455064015", "CZ2017051000002016517879", "CZ2017051000001374895147", "CZ2017051000000337866581", "CZ2017051000000060363305", "CZ2017051000001801115072", "CZ2017051000002096294453", "CZ2017051000001656884051", "CZ2017051000001090879809", "CZ2017051000001080749811", "CZ2017051000000762804456", "CZ2017051000000335701033", "CZ2017051000000257907860", "CZ2017051000001102061450", "CZ2017051000000450271383", "CZ2017051000002061475229", "CZ2017051000000017841013", "CZ2017051000000735149024", "CZ2017051000000747214955", "CZ2017051000000402554556", "CZ2017051000001955235883", "CZ2017051000001994599287", "CZ2017051000002098742509", "CZ2017051000001750400594", "CZ2017051000001340425906", "CZ2017051000001297383137", "CZ2017051000001141545394", "CZ2017051000000625398452"};
        String[] gy = new String[]{"CZ2017050100000749313655", "CZ2017050100000535361588", "CZ2017050100001789931504", "CZ2017050500000557008752", "CZ2017050500001702020806", "CZ2017050500001201091578", "CZ2017050500000582833303", "CZ2017050800000488856749", "CZ2017050800002098293889", "CZ2017050800000831405965", "CZ2017050800001063677561", "CZ2017050800001784422031", "CZ2017050800002069010308", "CZ2017050800002025438374", "CZ2017050800000256462565", "CZ2017050800000682480780", "CZ2017050800000408950804", "CZ2017050800000784909688", "CZ2017050800000428660776", "CZ2017050800000158985074", "CZ2017050800001176144857", "CZ2017050800000548808964", "CZ2017050800000963317778", "CZ2017050800001473077483", "CZ2017050800002052953811", "CZ2017050800000708844406", "CZ2017050800001228654649", "CZ2017050800000105086778", "CZ2017050800001973001379", "CZ2017050800000231158007", "CZ2017050800001120886110", "CZ2017050800000581827821", "CZ2017050800001371452067", "CZ2017050800000585355992", "CZ2017050800001322542991", "CZ2017050800001438260207", "CZ2017050800000294890425", "CZ2017050800001112441956", "CZ2017050800000582815376", "CZ2017050800000191301317", "CZ2017050900000553003716", "CZ2017050900001111262844", "CZ2017050900000907014207", "CZ2017050900001081141556", "CZ2017050900001126468814", "CZ2017050900000230998578", "CZ2017050900000771513526", "CZ2017050900000213312534", "CZ2017050900000361443618", "CZ2017050900001419683607", "CZ2017050900000865153555", "CZ2017050900001184782196", "CZ2017050900001591265472", "CZ2017050900000856341679", "CZ2017050900000926611590", "CZ2017050900001692262936", "CZ2017050800000032840305", "CZ2017050800001536427391", "CZ2017050800000448744972", "CZ2017050900001883264463", "CZ2017050900000664876967", "CZ2017050900000353009915", "CZ2017050900000110911422", "CZ2017050900002119257626", "CZ2017050900000570019586", "CZ2017050900001149964737", "CZ2017050900002137398093", "CZ2017050900002092973697", "CZ2017050900000333676428", "CZ2017051000001987302804", "CZ2017050800001880552528", "CZ2017050800001962981079", "CZ2017050800000312924097", "CZ2017050800000788707045", "CZ2017050800001758019801", "CZ2017050800000341942480", "CZ2017050800002000782195", "CZ2017050800000795811953", "CZ2017050800000239142241", "CZ2017050800002089630367", "CZ2017050800001817554613", "CZ2017050800000603236323", "CZ2017050800000390021079", "CZ2017050800001525243199", "CZ2017050800000707664023", "CZ2017050900000520219548", "CZ2017050900000378484297", "CZ2017050900000189803901", "CZ2017050900000738039782", "CZ2017050900000125631234", "CZ2017050900001330317578", "CZ2017050900001505250243", "CZ2017050900000481969576", "CZ2017050900002024555785", "CZ2017050900000996586770", "CZ2017050900000037769208", "CZ2017050900000126451729", "CZ2017050900002009464934", "CZ2017050900000681566895", "CZ2017050900001073909287", "CZ2017050900000230017838", "CZ2017050900000267352595", "CZ2017050900001933899828", "CZ2017050900001331992083", "CZ2017050900001278957538", "CZ2017050900000445736056", "CZ2017050900001677694340", "CZ2017050900000973934954", "CZ2017050900001390413349", "CZ2017050900000176644624", "CZ2017050900000108958201", "CZ2017050900000771567059", "CZ2017050900000530686263", "CZ2017050900001309947649", "CZ2017050900000189038172", "CZ2017050900000570317599", "CZ2017050900001396156876", "CZ2017050800001761134732", "CZ2017050800000131483234", "CZ2017050800001577987130", "CZ2017051000000264116811", "CZ2017050900001228151221", "CZ2017050900001463640783", "CZ2017050900000539952305", "CZ2017050900002027654146", "CZ2017050900001770160819", "CZ2017050900001755440885", "CZ2017050900000380542539", "CZ2017050900001812553941", "CZ2017050900000446101260", "CZ2017050900001346663442", "CZ2017050900001310727377", "CZ2017050900001709541926", "CZ2017050900000047494494", "CZ2017050900000451309142", "CZ2017050900000837232125", "CZ2017050800002074605370", "CZ2017050800001232876836", "CZ2017050800001214891056", "CZ2017050800002056709823", "CZ2017050800001709343577", "CZ2017050800002034678765", "CZ2017050800000079384433", "CZ2017050800001151212241", "CZ2017050800000624804388", "CZ2017050800001122350158", "CZ2017050900000218927778", "CZ2017050900000317086881", "CZ2017050900001803315693", "CZ2017050900001383476136", "CZ2017050900001618444733", "CZ2017050900000176162980", "CZ2017050900000031787554", "CZ2017050900001531200380", "CZ2017050900001427300076", "CZ2017050900001366977401", "CZ2017050900001626864312", "CZ2017050900000207604046", "CZ2017050900002004141812", "CZ2017050900001910050527", "CZ2017050900000618184532", "CZ2017050900000111962573", "CZ2017050900001378555383", "CZ2017050900001194766538", "CZ2017050900000225561038", "CZ2017050900000634309773", "CZ2017050900000748834219", "CZ2017050900002091631192", "CZ2017050900001360819946", "CZ2017050900000556023908", "CZ2017050900000188269210", "CZ2017050900000851321177", "CZ2017051000001374895147", "CZ2017051000002084899097", "CZ2017051000002096294453", "CZ2017051000000257907860", "CZ2017051000001080749811", "CZ2017051000001090879809", "CZ2017051000001267813988", "CZ2017051000000625398452", "CZ2017051000002105108102", "CZ2017051000001141545394", "CZ2017051000000311029575", "CZ2017051000000998964093", "CZ2017051000001396802961", "CZ2017051000000442639495", "CZ2017051000000292538518", "CZ2017051000000336829218", "CZ2017051000001497404394", "CZ2017051000001502241385", "CZ2017051000001773438858", "CZ2017051000001543648750", "CZ2017051000001092774319", "CZ2017051000001340425906", "CZ2017051000001934569038", "CZ2017051000000762387073", "CZ2017051000002104788123", "CZ2017051000000457060201", "CZ2017051000000277888034", "CZ2017051000000003692857", "CZ2017051000001191880772", "CZ2017051000000813998426", "CZ2017051000000914845729", "CZ2017051000001839571357", "CZ2017051000002068824475", "CZ2017051000001899359631", "CZ2017051000001555724573", "CZ2017051000001042721886", "CZ2017051000001465142799", "CZ2017051000000669593943", "CZ2017051000001692444364", "CZ2017051000000429426920", "CZ2017051000001955235883", "CZ2017051000002040752909", "CZ2017051000002016517879", "CZ2017051000000402554556", "CZ2017051000001656884051", "CZ2017051000001801115072", "CZ2017051000000762804456", "CZ2017051000001102061450", "CZ2017051000000450271383", "CZ2017051000000747214955", "CZ2017051000002057224633", "CZ2017051000000625284122", "CZ2017051000001297383137", "CZ2017051000002080090689", "CZ2017051000000975018363", "CZ2017051000000682734763", "CZ2017051000000071623618", "CZ2017051000000266255145", "CZ2017051000000981725845", "CZ2017051000000680110801", "CZ2017051000001255391932", "CZ2017051000000440057397", "CZ2017051000001666425141", "CZ2017051000001750400594", "CZ2017051000000031375814", "CZ2017051000000312884474", "CZ2017051000001228269247", "CZ2017051000002098742509", "CZ2017051000000581744377", "CZ2017051000001044404002", "CZ2017051000001754507915", "CZ2017051000001994599287", "CZ2017051000000204879243", "CZ2017051000001517155149", "CZ2017051000001811467631", "CZ2017051000001196036095", "CZ2017051000000344856762", "CZ2017051000002116541892", "CZ2017051000001697612112", "CZ2017051000001455064015", "CZ2017051000000335701033", "CZ2017051000002061475229", "CZ2017051000002002289573", "CZ2017051000001969798729", "CZ2017051000001726479406", "CZ2017051000000891340457", "CZ2017051000001100757755", "CZ2017051000000668137928", "CZ2017051000001960673272", "CZ2017051000001997384022", "CZ2017051000002116228361", "CZ2017051000001171465909", "CZ2017051000001742804303", "CZ2017051000002002757128", "CZ2017051000000852749756", "CZ2017051000001146472006", "CZ2017051000001048750974", "CZ2017051000000667932351", "CZ2017051000000258593142", "CZ2017051000001325346820", "CZ2017051000001529874701", "CZ2017051000002080333362", "CZ2017051000001815207735", "CZ2017051000001646363792", "CZ2017051000001881645720", "CZ2017051000000337866581", "CZ2017051000001051152652", "CZ2017051000000060363305", "CZ2017051000000017841013", "CZ2017051000000735149024"};
        List<String> zhList = Arrays.asList(zh);
        List<String> gyList = Arrays.asList(gy);
        for (String dfSn : zhList) {
            if (gyList.contains(dfSn)) continue;
            System.out.println("\u4e0d\u5b58\u5728:" + dfSn);
        }
        System.out.println(81493488);
        String[] list = new String[]{"15217920006"};
        String sql = GuangdaUtil.createJumpSql(list, "gpzhonghe2", "\u4e2d\u524d\u73e0\u5b9d\u8df3\u7801\u7ec4");
    }

    public static String formatData(String dataFormat, long timeStamp) {
        if (timeStamp == 0L) {
            return "";
        }
        String result = "";
        SimpleDateFormat format = new SimpleDateFormat(dataFormat);
        result = format.format(new Date(timeStamp *= 1000L));
        return result;
    }

    public static String createJumpSql(String[] mchtList, String jumpGroup, String GroupName) {
        StringBuffer sb = new StringBuffer();
        String[] arrstring = mchtList;
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String mcht = arrstring[n2];
            sb.append("insert into trade_jump_list (JUMP_GROUP, GROUP_NAME, TRADE_SOURCE, CHANNEL_ID, CHANNEL_MCHT_NO, WEIGHT) ");
            sb.append("values ('" + jumpGroup + "', '" + GroupName + "', '1', 'guangda', '" + mcht + "', 1);\n");
            ++n2;
        }
        return sb.toString();
    }

    public static String createRate(String[] mchtList, String jumpGroup, String GroupName) {
        StringBuffer sb = new StringBuffer();
        String[] arrstring = mchtList;
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String mcht = arrstring[n2];
            sb.append("insert into trade_rate_info (OWNER_NO, RATE_TYPE, DEBIT_CARD_FEE_VALUE, DEBIT_CARD_MAX_FEE, CREDIT_CARD_FEE_VALUE, WECHAT_FEE_VALUE, ALIPAY_FEE_VALUE, QQ_FEE_VALUE, QUICKPAY_FEE_VALUE, NETPAY_FEE_VALUE, SINGLE_EXTRA_FEE, UPDATE_TIME, OPR_ID) ");
            sb.append("values ('" + mcht + "', '1', 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 200, '20170706092849', 'admin');\n");
            ++n2;
        }
        return sb.toString();
    }
}
