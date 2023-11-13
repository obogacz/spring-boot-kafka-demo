package com.richcode.controller;

public record PurchaseEventRequest(String userId, String productId, String comment) {
}
