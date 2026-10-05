package dev.maxlord.enterpriseai.model

data class KnowledgeSource(
    val title: String,
    val score: Double,
)

data class GroundedAnswer(
    val text: String,
    val provider: String,
    val sources: List<KnowledgeSource>,
)

sealed interface AssistantState {
    data object Idle : AssistantState
    data object Loading : AssistantState
    data class Success(val answer: GroundedAnswer) : AssistantState
    data class Error(val message: String) : AssistantState
}
