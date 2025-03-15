package com.example.projecone.email.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class EmailRequest {

    private String emailContent;

    private String tone;

}

