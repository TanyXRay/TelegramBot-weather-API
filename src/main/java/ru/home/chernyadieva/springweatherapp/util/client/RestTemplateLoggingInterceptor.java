package ru.home.chernyadieva.springweatherapp.util.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Обработчик для логирования исходящего трафика по REST.
 */
@Slf4j
@Component
public class RestTemplateLoggingInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution) throws IOException {
        String uuid = UUID.randomUUID().toString();
        logRequest(request, body, uuid);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ClientHttpResponse response = new ClientHttpResponseWrapper(execution.execute(request, body));
        stopWatch.stop();
        logResponse(response, uuid, stopWatch.getTotalTimeMillis());
        return response;
    }

    private void logRequest(HttpRequest request, byte[] body, String uuid) {
        log.info(
                """
                        UUID        : {}
                        URI         : {}
                        Method      : {}
                        Headers     : {}
                        Request body: {}
                        """,
                uuid,
                request.getURI(),
                request.getMethod(),
                request.getHeaders(),
                new String(body, StandardCharsets.UTF_8));
    }


    private void logResponse(
            ClientHttpResponse response,
            String uuid,
            long responseTimeMs) throws IOException {
        log.info(
                """
                        UUID              : {}
                        Status code       : {}
                        Status text       : {}
                        Headers           : {}
                        Response time (ms): {}
                        Response body     : {}
                        """,
                uuid,
                response.getStatusCode(),
                response.getStatusText(),
                response.getHeaders(),
                responseTimeMs,
                StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));
    }
}
