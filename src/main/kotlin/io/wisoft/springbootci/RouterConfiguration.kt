package io.wisoft.springbootci

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class RouterConfiguration {
    @Bean
    fun greetingRoutes(greetingHandler: GreetingHandler) =
        coRouter {
            "greeting".nest {
                GET("", greetingHandler::greeting)
            }
        }
}
