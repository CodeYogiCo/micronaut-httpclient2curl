package com.codeyogico.micronauthttpclient2curl

import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpRequest
import io.micronaut.http.annotation.Filter
import io.micronaut.http.filter.ClientFilterChain
import io.micronaut.http.filter.HttpClientFilter
import org.reactivestreams.Publisher

/**
 * Logs outgoing requests made through declarative `@Client` interfaces as curl commands.
 * Disable with `curl-logger.client.enabled: false`.
 */
@Filter("/api/applications/**")
@Requires(property = "curl-logger.client.enabled", value = "true", defaultValue = "true")
class CurlRequestFilter(private val objectMapper: ObjectMapper) : HttpClientFilter {

    override fun doFilter(request: MutableHttpRequest<*>, chain: ClientFilterChain): Publisher<out HttpResponse<*>> {
        println(SEPARATOR)
        println(CurlCommandBuilder.build(request, objectMapper))
        println(SEPARATOR)
        return chain.proceed(request)
    }

    private companion object {
        const val SEPARATOR = "=============================================================="
    }
}
