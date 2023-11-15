package com.richcode.domain;

import lombok.Builder;

import java.io.Serializable;
import java.util.UUID;

@Builder
public record PurchaseEvent(UUID uuid, String userId, String productId, String comment) implements Serializable {
}
