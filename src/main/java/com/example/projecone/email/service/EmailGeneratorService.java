package com.example.projecone.email.service;

import com.example.projecone.email.domain.EmailRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class EmailGeneratorService {

    private final WebClient webClient;
    private final String geminiApiKey;
    private final String getGeminiApiUrl;

    public EmailGeneratorService(WebClient webClient,
                                 @Value("${gemini.api.key}") String geminiApiKey,
                                 @Value("${gemini.api.url}") String getGeminiApiUrl) {
        this.webClient = webClient;
        this.geminiApiKey = geminiApiKey;
        this.getGeminiApiUrl = getGeminiApiUrl;
    }


    public String generateMailReply(EmailRequest emailRequest)
    {
        // build prompt

        String promt=buildPrompt(emailRequest);

        // craft a request

        Map <String,Object> requestBody=Map.of(
                "contents",new Object[]{
                        Map.of("parts",new Object[]{
                            Map.of("text",promt)
                        })
                }
        );

        // do request and get response
        String response=webClient.post()
                .uri(getGeminiApiUrl + geminiApiKey)
                .header("Content-Type","application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // return response
        return extractContentResponse(response);
    }

    private String extractContentResponse(String response) {

        try{

            ObjectMapper mapper=new ObjectMapper();
            JsonNode rootNode=mapper.readTree(response);
            return rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

        }catch (Exception e)
        {
            return "error processing request:"+e.getMessage();
        }
    }

    private String buildPrompt(EmailRequest request)
    {
        StringBuilder prompt=new StringBuilder();
        prompt.append("Generate a professional email reply for the following email content. Please dont generate a subject line ");
        if(request.getTone()!=null && request.getTone().isEmpty())
        {
            prompt.append("use a").append(request.getTone()).append(" tone.");
        }

        prompt.append("\nOriginal email:\n").append(request.getEmailContent());

        return prompt.toString();
    }

}
