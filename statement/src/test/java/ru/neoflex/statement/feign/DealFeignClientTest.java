package ru.neoflex.statement.feign;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import ru.neoflex.statement.dto.LoanOfferDto;
import ru.neoflex.statement.dto.LoanStatementRequestDto;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.http.RequestEntity.post;

@SpringBootTest
@AutoConfigureWireMock(port = 8082)
class DealFeignClientTest {

    @Autowired
    private DealFeignClient dealFeignClient;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void calculateLoanOffers() throws JsonProcessingException {
        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/deal/statement"))
                .withHeader(HttpHeaders.CONTENT_TYPE, equalTo(MediaType.APPLICATION_JSON_VALUE))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        assertDoesNotThrow(() -> dealFeignClient.calculateLoanOffers(new LoanStatementRequestDto()));

        verify(1, postRequestedFor(urlEqualTo("/deal/statement")));
        verify(postRequestedFor(urlEqualTo("/deal/statement"))
                .withHeader(HttpHeaders.CONTENT_TYPE, equalTo(MediaType.APPLICATION_JSON_VALUE))
                .withRequestBody(equalToJson(mapper.writeValueAsString(new LoanStatementRequestDto()))));

    }

    @Test
    void selectLoanOffers() throws JsonProcessingException {
        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/deal/offer/select"))
                .withHeader(HttpHeaders.CONTENT_TYPE, equalTo(MediaType.APPLICATION_JSON_VALUE))
                .willReturn(aResponse()
                        .withStatus(200)));

        assertDoesNotThrow(() -> dealFeignClient.selectLoanOffers(new LoanOfferDto()));

        verify(1, postRequestedFor(urlEqualTo("/deal/offer/select")));
        verify(postRequestedFor(urlEqualTo("/deal/offer/select"))
                .withHeader(HttpHeaders.CONTENT_TYPE, equalTo(MediaType.APPLICATION_JSON_VALUE))
                .withRequestBody(equalToJson(mapper.writeValueAsString(new LoanOfferDto()))));
    }
}
