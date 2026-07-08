package com.codeyogico.micronauthttpclient2curl

import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Filter
import io.micronaut.http.filter.HttpServerFilter
import io.micronaut.http.filter.ServerFilterChain
import org.reactivestreams.Publisher

/**
 * Logs inbound requests hitting `@Controller` endpoints as curl commands.
 * Disable with `curl-logger.server.enabled: false`.
 */
@Filter("/api/applications/**")
@Requires(property = "curl-logger.server.enabled", value = "true", defaultValue = "true")
class CurlServerRequestFilter(private val objectMapper: ObjectMapper) : HttpServerFilter {

    override fun doFilter(request: HttpRequest<*>, chain: ServerFilterChain): Publisher<MutableHttpResponse<*>> {
        println(SEPARATOR)
        println(CurlCommandBuilder.build(request, objectMapper))
        println(SEPARATOR)
        return chain.proceed(request)
    }

    private companion object {
        const val SEPARATOR = "=============================================================="
    }
}
