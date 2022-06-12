package com.fingerchar.core.config.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalTime;


/**
 * @author: Black_Dragon
 * @date: 2021/6/2
 */
public class TimeDeserializer extends JsonDeserializer<LocalTime> {

    @Override
    public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        Long value = p.getLongValue();
        if(null != value && value > 0) {

            return LocalTime.ofSecondOfDay(value);
        }
        return null;
    }
}
