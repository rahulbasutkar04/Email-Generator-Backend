package com.example.projecone.email.controller;

import com.example.projecone.email.domain.EmailRequest;
import com.example.projecone.email.service.EmailGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@CrossOrigin(origins = "*")
public class EmailGeneratorController {

    @Autowired
    private EmailGeneratorService emailGeneratorService;

    @PostMapping("/generate")
    ResponseEntity <String> generateEmail(@RequestBody EmailRequest emailRequest)
    {
        return ResponseEntity.ok(emailGeneratorService.generateMailReply(emailRequest));
    }

}