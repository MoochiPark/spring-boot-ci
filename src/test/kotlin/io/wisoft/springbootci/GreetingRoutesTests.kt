package io.wisoft.springbootci

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBodyList

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GreetingRoutesTests(@Autowired val client: WebTestClient) {
    @Autowired
    val greetingHandler = GreetingHandler()

    @BeforeEach
    fun initialize() =
        greetingHandler.initialize()

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

    @Test
    fun `새로운 인사말 등록`() {
        client
            .post()
            .uri("/greetings")
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .bodyValue(Greeting(4, "Hello, Daewon!"))
            .exchange()
            .expectStatus().isCreated
            .expectHeader()
            .location("/greetings/4")
    }

    @Test
    fun `아이디로 인사말 조회`() {
        client
            .get()
            .uri("/greetings/1")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("content")
            .isEqualTo("Hello, World!")
    }

    @Test
    fun `인사말 전체 조회`() {
        client
            .get()
            .uri("/greetings")
            .exchange()
            .expectStatus().isOk
            .expectBodyList<Greeting>()
            .hasSize(3)
    }

}
