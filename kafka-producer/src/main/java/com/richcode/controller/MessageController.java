package com.richcode.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Messages")
@RestController
@RequestMapping("/messages")
class MessageController {

    @Operation(summary = "Hello Word")
    @GetMapping
    public String hello() {
        return "Hello Word";
    }

}
