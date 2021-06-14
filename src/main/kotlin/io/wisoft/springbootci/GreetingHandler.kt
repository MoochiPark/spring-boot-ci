package io.wisoft.springbootci

import java.net.URI
import java.util.concurrent.atomic.AtomicLong
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.badRequest
import org.springframework.web.reactive.function.server.ServerResponse.created
import org.springframework.web.reactive.function.server.ServerResponse.notFound
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait
import org.springframework.web.reactive.function.server.queryParamOrNull

@Component
class GreetingHandler {
    private val counter: AtomicLong = AtomicLong()
    private var greetings = HashMap<Long, Greeting>()

    suspend fun greeting(request: ServerRequest): ServerResponse =
        ok()
            .bodyValueAndAwait(
                Greeting(
                    counter.incrementAndGet(),
                    "Hello, ${request.queryParamOrNull("name") ?: "World!"}"
                )
            )

    suspend fun create(request: ServerRequest): ServerResponse =
        request
            .awaitBody<Greeting>()
            .let {
                greetings[it.id] = it
                created(URI.create("/greetings/${it.id}"))
                    .buildAndAwait()
            }

    suspend fun getAll(request: ServerRequest): ServerResponse =
        ok()
            .bodyValueAndAwait(greetings.values)

    suspend fun getById(request: ServerRequest): ServerResponse {
        val id = request
            .pathVariable("id")
            .toLongOrNull()
            ?: return badRequest()
                .buildAndAwait()

        return greetings[id]
            ?.let {
                ok()
                    .bodyValueAndAwait(it)
            } ?: notFound()
            .buildAndAwait()
    }

    fun initialize() {
        this.greetings = hashMapOf(
            1L to Greeting(1, "Hello, World!"),
            2L to Greeting(2, "Hello, Sangmin!"),
            3L to Greeting(3, "Hello, Soonho!"),
        )
    }
}
