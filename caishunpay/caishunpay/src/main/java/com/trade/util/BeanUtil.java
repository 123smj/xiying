/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.springframework.beans.BeanUtils
 *  org.springframework.beans.BeansException
 *  org.springframework.beans.FatalBeanException
 *  org.springframework.util.Assert
 *  org.springframework.util.ClassUtils
 */
package com.trade.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

public class BeanUtil
extends BeanUtils {
    public static /* varargs */ void copyPropertiesNotNull(Object source, Object target, Class<?> editable, String ... ignoreProperties) throws BeansException {
        Assert.notNull((Object)source, (String)"Source must not be null");
        Assert.notNull((Object)target, (String)"Target must not be null");
        Class actualEditable = target.getClass();
        if (editable != null) {
            if (!editable.isInstance(target)) {
                throw new IllegalArgumentException("Target class [" + target.getClass().getName() + "] not assignable to Editable class [" + editable.getName() + "]");
            }
            actualEditable = editable;
        }
        PropertyDescriptor[] targetPds = BeanUtil.getPropertyDescriptors(actualEditable);
        List<String> ignoreList = ignoreProperties != null ? Arrays.asList(ignoreProperties) : null;
        PropertyDescriptor[] arrpropertyDescriptor = targetPds;
        int n = arrpropertyDescriptor.length;
        int n2 = 0;
        while (n2 < n) {
            PropertyDescriptor sourcePd;
            Method readMethod;
            PropertyDescriptor targetPd = arrpropertyDescriptor[n2];
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null && (ignoreProperties == null || !ignoreList.contains(targetPd.getName())) && (sourcePd = BeanUtil.getPropertyDescriptor(source.getClass(), (String)targetPd.getName())) != null && (readMethod = sourcePd.getReadMethod()) != null && ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                try {
                    Object value;
                    if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                        readMethod.setAccessible(true);
                    }
                    if ((value = readMethod.invoke(source, new Object[0])) != null) {
                        if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                            writeMethod.setAccessible(true);
                        }
                        writeMethod.invoke(target, value);
                    }
                }
                catch (Throwable ex) {
                    throw new FatalBeanException("Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                }
            }
            ++n2;
        }
    }
}
