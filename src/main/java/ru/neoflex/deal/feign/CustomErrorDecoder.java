package ru.neoflex.deal.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import ru.neoflex.deal.exception.IncorrectParameterException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {

        log.error("Feign error occurred while calling method: {}. HTTP status: {}, reason: {}",
                methodKey, response.status(), response.reason());

        String responseBody = "";
        if (response.body() != null) {
            try (Response.Body body = response.body()) {
                byte[] resp = body.asInputStream().readAllBytes();
                responseBody = new String(resp, StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.getStackTrace();
            }
        }

        switch (response.status()) {
            case 404 -> throw new EntityNotFoundException(responseBody .isBlank() ? "Не найдено!" : responseBody );
            case 400 -> throw new IncorrectParameterException(responseBody .isBlank() ? "Некорректные данные" : responseBody );
            default -> throw new RuntimeException(String.format("Unexpected error: HTTP status{}, reason: {}, body : {}",response.status(),response.reason(), responseBody));
        }
    }
}

