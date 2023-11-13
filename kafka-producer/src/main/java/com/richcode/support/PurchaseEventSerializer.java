package com.richcode.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.richcode.domain.PurchaseEvent;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Objects;

public class PurchaseEventSerializer implements Serializer<PurchaseEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, PurchaseEvent data) {
        Objects.requireNonNull(data);
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Throwable e) {
            throw new SerializationException("Error when serializing MessageDto to byte[]");
        }
    }

}
