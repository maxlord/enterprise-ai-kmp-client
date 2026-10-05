package dev.maxlord.enterpriseai.data

import dev.maxlord.enterpriseai.model.GroundedAnswer
import dev.maxlord.enterpriseai.model.KnowledgeSource

fun interface AiGateway {
    suspend fun ask(question: String): GroundedAnswer
}

class DemoAiGateway : AiGateway {
    override suspend fun ask(question: String): GroundedAnswer = GroundedAnswer(
        text = "A safe rollout starts with one reversible workflow, explicit human approval, " +
            "quality evaluation, audit logs, monitoring, and a documented rollback path. " +
            "Your question was: $question",
        provider = "offline-demo",
        sources = listOf(KnowledgeSource("Reference architecture", 0.94)),
    )
}
