/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.dom4j.Attribute
 *  org.dom4j.Document
 *  org.dom4j.DocumentException
 *  org.dom4j.DocumentHelper
 *  org.dom4j.Element
 *  org.dom4j.io.SAXReader
 *  org.dom4j.io.XMLWriter
 */
package com.gy.util;

import com.gy.util.StringUtil;
import com.trade.bean.NativeNotifyResultBean;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class Dom4jUtil {

    public static /* varargs */ String createXmlStr(Object obj, String... assignRoot) {
        String xml = "";
        try {
            Document document = DocumentHelper.createDocument();
            String rootname = obj.getClass().getSimpleName();
            Element root = document.addElement(assignRoot.length == 0 ? rootname : assignRoot[0]);
            Field[] fields = obj.getClass().getDeclaredFields();
            Method method = null;
            Field[] arrfield = fields;
            int n = arrfield.length;
            int n2 = 0;
            while (n2 < n) {
                Object value;
                Field field = arrfield[n2];
                root.addElement(field.getName()).addCDATA((value = (method = obj.getClass().getMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1), new Class[0])).invoke(obj, new Object[0])) == null ? "" : value.toString());
                ++n2;
            }
            xml = document.getRootElement().asXML();
            System.out.println(xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xml;
    }

    public static /* varargs */ String createXml4Map(Map<String, String> map, String... assignRoot) {
        String xml = "";
        try {
            Document document = DocumentHelper.createDocument();
            Element root = document.addElement(assignRoot.length == 0 ? "xml" : assignRoot[0]);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                root.addElement(entry.getKey()).addText(entry.getValue() == null ? "" : entry.getValue().toString());
            }
            xml = document.getRootElement().asXML();
            System.out.println(xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xml;
    }

    public static Map<String, String> parseXml2Map(String xml) {
        HashMap<String, String> retMap = new HashMap<String, String>();
        try {
            Document document = DocumentHelper.parseText((String) xml);
            Element root = document.getRootElement();
            List<Element> list = root.elements();
            for (Element element : list) {
                if (element.isTextOnly()) {
                    retMap.put(element.getName(), element.getText());
                    continue;
                }
                retMap.put(element.getName(), element.asXML());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retMap;
    }

    public static <T> T parseXml2Object(String xml, T t) {
        Element element = null;
        try {
            if (xml != null) {
                Document document = DocumentHelper.parseText((String) xml);
                Element root = document.getRootElement();
                Field[] fields = t.getClass().getDeclaredFields();
                Method method = null;
                Field[] arrfield = fields;
                int n = arrfield.length;
                int n2 = 0;
                while (n2 < n) {
                    Field field = arrfield[n2];
                    element = root.element(field.getName());
                    if (element != null && StringUtil.isNotEmpty(element.getText())) {
                        method = t.getClass().getMethod("set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1), field.getType());
                        method.invoke(t, element.getText());
                    }
                    ++n2;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(String.valueOf(element.getName()) + element.getText());
        }
        return t;
    }

    public static void createXml(File file) {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("employees");
        root.addComment("An XMLNote");
        root.addProcessingInstruction("target", "text");
        Element empElem = root.addElement("employee");
        empElem.addAttribute("id", "0001");
        empElem.addAttribute("name", "wanglp");
        Element sexElem = empElem.addElement("sex");
        sexElem.setText("m");
        Element ageElem = empElem.addElement("age");
        ageElem.setText("25");
        Element emp2Elem = root.addElement("employee");
        emp2Elem.addAttribute("id", "0002");
        emp2Elem.addAttribute("name", "fox");
        Element sex2Elem = emp2Elem.addElement("sex");
        sex2Elem.setText("f");
        Element age2Elem = emp2Elem.addElement("age");
        age2Elem.setText("24");
        try {
            XMLWriter output = new XMLWriter((Writer) new FileWriter(file));
            output.write(document);
            output.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void parserXml(File file) {
        Document document = null;
        SAXReader saxReader = new SAXReader();
        try {
            document = saxReader.read(file);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element root = document.getRootElement();
        System.out.println("<" + root.getName() + ">");
        Iterator iter = root.elementIterator("employee");
        while (iter.hasNext()) {
            Element empEle = (Element) iter.next();
            System.out.println("<" + empEle.getName() + ">");
            Iterator attrList = empEle.attributeIterator();
            while (attrList.hasNext()) {
                Attribute attr = (Attribute) attrList.next();
                System.out.println(String.valueOf(attr.getName()) + "=" + attr.getValue());
            }
            Iterator eleIte = empEle.elementIterator();
            while (eleIte.hasNext()) {
                Element ele = (Element) eleIte.next();
                System.out.println("<" + ele.getName() + ">" + ele.getTextTrim());
            }
        }
        System.out.println("</" + root.getName() + ">");
    }
}
