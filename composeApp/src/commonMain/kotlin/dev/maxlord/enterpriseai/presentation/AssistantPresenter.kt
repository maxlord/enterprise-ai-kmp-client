package dev.maxlord.enterpriseai.presentation

import dev.maxlord.enterpriseai.data.AiGateway
import dev.maxlord.enterpriseai.model.AssistantState

class AssistantPresenter(private val gateway: AiGateway) {
    var state: AssistantState = AssistantState.Idle
        private set

    suspend fun submit(question: String) {
        if (question.isBlank()) {
            state = AssistantState.Error("Describe the business question first.")
            return
        }
        state = AssistantState.Loading
        state = try {
            AssistantState.Success(gateway.ask(question.trim()))
        } catch (error: Exception) {
            AssistantState.Error(error.message ?: "The AI service is unavailable.")
        }
    }
}
