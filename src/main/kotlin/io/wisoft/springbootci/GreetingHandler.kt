package io.wisoft.springbootci

import java.util.concurrent.atomic.AtomicLong
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.queryParamOrNull

@Component
class GreetingHandler {

    private val counter: AtomicLong = AtomicLong()

    suspend fun greeting(request: ServerRequest): ServerResponse =
        ok()
            .bodyValueAndAwait(
                Greeting(
                    counter.incrementAndGet(),
                    "Hello, ${request.queryParamOrNull("name") ?: "World!"}"
                )
            )
}
