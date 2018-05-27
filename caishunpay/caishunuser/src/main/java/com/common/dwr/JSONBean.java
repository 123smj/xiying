/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  net.sf.json.JSONArray
 *  net.sf.json.JSONObject
 */
package com.common.dwr;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JSONBean {
    private Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
    private List<Object> dataList = new LinkedList<Object>();
    private JSONObject object = null;
    private JSONArray array = null;

    public void addJSONElement(String id, Object name) {
        this.dataMap.put(id, name);
    }

    public void addJSONArrayElement(Object obj) {
        this.dataList.add(obj);
    }

    public Map<String, Object> getDataMap() {
        return this.dataMap;
    }

    public List<Object> getDataList() {
        return this.dataList;
    }

    public void setDataMap(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
    }

    public void setDataList(List<Object> dataList) {
        this.dataList = dataList;
    }

    public void addChild(String id, List<Object> list) {
        this.dataMap.put(id, list);
    }

    public static String genListToJSON(List<Object> list) {
        return JSONArray.fromObject(list).toString();
    }

    public static String genMapToJSON(Map<String, Object> map) {
        try {
            return new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            return null;
        }
//        return JSONObject.fromObject(map).toString();
    }

    public JSONArray parseJSONArrayData(String jsonData) {
        this.array = JSONArray.fromObject((Object) jsonData);
        return this.array;
    }

    public JSONObject getJSONDataAt(int index) {
        this.object = (JSONObject) this.array.get(index);
        return this.object;
    }

    public Object getElementByKey(String key) {
        return this.object.get(key);
    }

    public String getStringElementByKey(String key) {
        return this.object.getString(key);
    }

    public String toString() {
        return JSONBean.genMapToJSON(this.getDataMap());
    }

    public JSONObject getObject() {
        return this.object;
    }

    public void setObject(JSONObject object) {
        this.object = object;
    }

    public JSONArray getArray() {
        return this.array;
    }

    public void setArray(JSONArray array) {
        this.array = array;
    }
}
