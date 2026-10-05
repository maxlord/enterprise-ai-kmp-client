package dev.maxlord.enterpriseai

import dev.maxlord.enterpriseai.data.DemoAiGateway
import dev.maxlord.enterpriseai.model.AssistantState
import dev.maxlord.enterpriseai.presentation.AssistantPresenter
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertTrue

class AssistantPresenterTest {
    @Test
    fun rejectsBlankQuestion() = runTest {
        val presenter = AssistantPresenter(DemoAiGateway())
        presenter.submit("  ")
        assertIs<AssistantState.Error>(presenter.state)
    }

    @Test
    fun returnsGroundedDemoAnswer() = runTest {
        val presenter = AssistantPresenter(DemoAiGateway())
        presenter.submit("How can we automate invoice approval?")
        val state = assertIs<AssistantState.Success>(presenter.state)
        assertTrue(state.answer.sources.isNotEmpty())
        assertTrue(state.answer.text.contains("human approval"))
    }
}
