/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.dom4j.Document
 *  org.dom4j.DocumentException
 *  org.dom4j.Element
 *  org.dom4j.Node
 *  org.dom4j.io.SAXReader
 */
package com.common.dwr;

import com.common.dwr.SystemDictionaryUnit;
import java.io.InputStream;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class XmlDBParser {
    private static SAXReader saxReader = new SAXReader();

    public static void parseSysDic(InputStream inputStream) throws DocumentException {
        Document document = saxReader.read(inputStream);
        List fieldList = document.selectNodes("//results/row");
        int i = 0;
        while (i < fieldList.size()) {
            Element field = (Element)fieldList.get(i);
            Node tblNmNode = field.selectSingleNode("TBL_NM");
            Node fldNmNode = field.selectSingleNode("FLD_NM");
            Node fldValNode = field.selectSingleNode("FLD_VAL");
            Node fldDescNode = field.selectSingleNode("FLD_DESC");
            SystemDictionaryUnit.addRecord(tblNmNode.getText(), fldNmNode.getText(), fldValNode.getText(), fldDescNode.getText());
            ++i;
        }
    }
}
