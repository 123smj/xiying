/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.databind.JsonSerializer
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.fasterxml.jackson.databind.SerializerProvider
 */
package com.common.json;

import com.common.json.NullSerializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CustomObjectMapper
extends ObjectMapper {
    private static final long serialVersionUID = 1L;

    public CustomObjectMapper() {
        this.getSerializerProvider().setNullValueSerializer((JsonSerializer)new NullSerializer());
    }
}
