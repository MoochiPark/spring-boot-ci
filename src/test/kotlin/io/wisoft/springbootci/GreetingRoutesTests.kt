package io.wisoft.springbootci

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GreetingRoutesTests(@Autowired val client: WebTestClient) {
    @Test
    fun `기본 인사말 테스트`() {
        client
            .get()
            .uri("/greeting")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("content")
            .isEqualTo("Hello, World!")
    }

    @Test
    fun `이름을 입력한 인사말 테스트`() {
        client
            .get()
            .uri("/greeting?name={name}", "Daewon")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("content")
            .isEqualTo("Hello, Daewon")
    }
}
