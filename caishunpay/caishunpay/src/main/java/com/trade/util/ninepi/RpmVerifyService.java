/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.trade.util.ninepi;

import com.trade.util.ninepi.MrpException;
import com.trade.util.ninepi.RSASignUtil;
import com.trade.util.ninepi.ServiceNotSpecifiedException;
import com.trade.util.ninepi.SignFieldEnum;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpmVerifyService {
    private static final Logger log = LoggerFactory.getLogger(RSASignUtil.class);

    public void execute(Map<String, String> signMap, String service) throws Exception {
        if (service == null) {
            throw new ServiceNotSpecifiedException("The service is not specified");
        }
        SignFieldEnum signFieldEnum = SignFieldEnum.SignFieldMap.get(service);
        String notEmptyfields = "charset|version|service|signType|merchantId|requestTime|requestId|" + signFieldEnum.getNotEmptyFields();
        List<String> notEmptySignFields = Arrays.asList(notEmptyfields.split("\\|"));
        HashMap<String, String> notEmptySignFieldsMap = new HashMap<String, String>();
        for (String field : notEmptySignFields) {
            notEmptySignFieldsMap.put(field, field);
        }
        String emptyFields = signFieldEnum.getEmptyFields();
        List<String> emptySignFields = Arrays.asList(emptyFields.split("\\|"));
        HashMap<String, String> emptySignFieldsMap = new HashMap<String, String>();
        for (String field : emptySignFields) {
            if ("".equals(field) || field == null) continue;
            emptySignFieldsMap.put(field, field);
        }
        this.executeVerify(signMap, notEmptySignFieldsMap, emptySignFieldsMap);
    }

    public void executeVerify(Map<String, String> signMap, Map<String, String> notEmptyFields, Map<String, String> emptyFields) throws Exception {
        StringBuilder warnMessage = new StringBuilder();
        for (String ef : emptyFields.keySet()) {
            if (!signMap.containsKey(ef)) {
                warnMessage.append(ef).append("=?&");
                continue;
            }
            signMap.remove(ef);
        }
        if (warnMessage.length() > 0) {
            String res = warnMessage.substring(0, warnMessage.length() - 1).toString();
            log.warn("The empty fields are not filled:[{}]", (Object)res);
        }
        ArrayList<String> message = new ArrayList<String>();
        for (String nef : notEmptyFields.keySet()) {
            if (!signMap.containsKey(nef)) {
                message.add(nef);
                continue;
            }
            signMap.remove(nef);
        }
        HashMap<Integer, List<String>> messageMap = new HashMap<Integer, List<String>>();
        if (!message.isEmpty()) {
            messageMap.put(1, message);
        }
        ArrayList<String> ext = new ArrayList<String>();
        if (!signMap.isEmpty()) {
            for (String extFileds : signMap.keySet()) {
                ext.add(extFileds);
            }
            messageMap.put(2, ext);
        }
        if (!messageMap.isEmpty()) {
            throw new MrpException(messageMap);
        }
    }
}
