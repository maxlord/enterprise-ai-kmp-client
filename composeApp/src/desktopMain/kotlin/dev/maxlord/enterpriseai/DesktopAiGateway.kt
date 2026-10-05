package dev.maxlord.enterpriseai

import dev.maxlord.enterpriseai.data.AiGateway
import dev.maxlord.enterpriseai.model.GroundedAnswer
import dev.maxlord.enterpriseai.model.KnowledgeSource
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class DesktopAiGateway(private val baseUrl: String) : AiGateway {
    private val client = HttpClient.newHttpClient()

    override suspend fun ask(question: String): GroundedAnswer {
        val escaped = question.replace("\\", "\\\\").replace("\"", "\\\"")
        val request = HttpRequest.newBuilder(URI.create("${baseUrl.trimEnd('/')}/v1/ask"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString("{\"question\":\"$escaped\"}"))
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        check(response.statusCode() in 200..299) { "API returned HTTP ${response.statusCode()}" }
        return GroundedAnswer(
            text = response.body().jsonString("answer") ?: response.body(),
            provider = response.body().jsonString("provider") ?: "api",
            sources = listOf(
                KnowledgeSource(response.body().jsonString("title") ?: "Enterprise knowledge", 1.0)
            ),
        )
    }
}

private fun String.jsonString(key: String): String? {
    val marker = "\"$key\":\""
    val start = indexOf(marker)
    if (start < 0) return null
    val valueStart = start + marker.length
    var cursor = valueStart
    while (cursor < length) {
        if (this[cursor] == '"' && this.getOrNull(cursor - 1) != '\\') break
        cursor++
    }
    return substring(valueStart, cursor)
        .replace("\\n", "\n")
        .replace("\\\"", "\"")
        .replace("\\\\", "\\")
}
