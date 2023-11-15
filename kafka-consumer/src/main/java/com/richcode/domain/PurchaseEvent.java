package com.richcode.domain;

import lombok.Builder;

import java.util.UUID;

@Builder
public record PurchaseEvent(UUID uuid, String userId, String productId, String comment) {
}
