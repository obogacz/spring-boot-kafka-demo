package com.richcode.controller;

import com.richcode.domain.PurchaseEvent;
import com.richcode.publisher.EventPublisher;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Events")
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
class PurchaseEventController {

    private final EventPublisher eventPublisher;

    @Operation(summary = "Sends an event of a purchase")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void send(@RequestBody PurchaseEventRequest request) {
        eventPublisher.send(PurchaseEvent.from(request));
    }

}
