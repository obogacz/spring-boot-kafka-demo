package com.richcode.domain;

import com.richcode.controller.PurchaseEventRequest;
import lombok.Builder;

import java.io.Serializable;
import java.util.UUID;

@Builder
public record PurchaseEvent(UUID uuid, String userId, String productId, String comment) implements Serializable {

    public static PurchaseEvent from(final PurchaseEventRequest request) {
        return PurchaseEvent.builder()
            .uuid(UUID.randomUUID())
            .userId(request.userId())
            .productId(request.productId())
            .comment(request.comment())
            .build();
    }

}
