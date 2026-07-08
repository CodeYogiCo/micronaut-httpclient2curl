package com.codeyogico.micronauthttpclient2curl

import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpMethod
import io.micronaut.http.HttpRequest

internal object CurlCommandBuilder {

    private const val FORMAT_HEADER = "-H \"%1\$s:%2\$s\""
    private const val FORMAT_METHOD = "-X %1\$s"
    private const val FORMAT_BODY = "-d '%1\$s'"
    private const val FORMAT_URL = "\"%1\$s\""

    fun build(request: HttpRequest<*>, objectMapper: ObjectMapper): String =
        listOf(
            listOf("curl"),
            generateMethod(request.method),
            generateHeaders(request.headers),
            getBodyAsString(request, objectMapper),
            generateUrl(request)
        ).flatten().joinToString(" ")

    private fun getBodyAsString(request: HttpRequest<*>, objectMapper: ObjectMapper): List<String> {
        val body = request.body
        return if (body.isEmpty)
            emptyList()
        else
            listOf(FORMAT_BODY.format(objectMapper.writeValueAsString(body.get())))
    }

    private fun generateUrl(request: HttpRequest<*>): List<String> = listOf(FORMAT_URL.format(request.uri.toString()))

    private fun generateHeaders(headers: HttpHeaders): List<String> =
        headers.map { FORMAT_HEADER.format(it.key, it.value.firstOrNull().orEmpty()) }

    private fun generateMethod(method: HttpMethod): List<String> = listOf(FORMAT_METHOD.format(method))
}
