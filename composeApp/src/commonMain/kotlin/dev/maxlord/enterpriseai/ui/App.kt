package dev.maxlord.enterpriseai.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.maxlord.enterpriseai.data.AiGateway
import dev.maxlord.enterpriseai.model.AssistantState
import dev.maxlord.enterpriseai.presentation.AssistantPresenter
import kotlinx.coroutines.launch

private val Ink = Color(0xFF102A2E)
private val Mist = Color(0xFFF2F7F5)
private val Accent = Color(0xFF00A67E)

@Composable
fun EnterpriseAiApp(gateway: AiGateway) {
    MaterialTheme {
        val presenter = remember(gateway) { AssistantPresenter(gateway) }
        var question by remember {
            mutableStateOf("How should we introduce a private AI assistant into invoice approval?")
        }
        var state by remember { mutableStateOf<AssistantState>(AssistantState.Idle) }
        val scope = rememberCoroutineScope()

        Surface(modifier = Modifier.fillMaxSize(), color = Mist) {
            Column(
                modifier = Modifier.fillMaxSize().padding(32.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().widthIn(max = 920.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text("PRIVATE AI WORKSPACE", color = Accent, fontWeight = FontWeight.Bold)
                    Text(
                        "Turn internal knowledge into a safe business decision",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Ink,
                    )
                    Text(
                        "Shared Kotlin UI and business logic for desktop and iOS, connected to " +
                            "the Enterprise AI Platform API.",
                        color = Ink.copy(alpha = 0.7f),
                    )
                }

                Card(
                    modifier = Modifier.fillMaxWidth().widthIn(max = 920.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        OutlinedTextField(
                            value = question,
                            onValueChange = { question = it },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3,
                            label = { Text("Business question") },
                        )
                        Button(
                            onClick = {
                                scope.launch {
                                    presenter.submit(question)
                                    state = presenter.state
                                }
                            },
                            enabled = state !is AssistantState.Loading,
                        ) { Text("Ask private AI") }

                        when (val current = state) {
                            AssistantState.Idle -> Text("Answers include their evidence source.")
                            AssistantState.Loading -> CircularProgressIndicator(color = Accent)
                            is AssistantState.Error -> Text(current.message, color = MaterialTheme.colorScheme.error)
                            is AssistantState.Success -> AnswerCard(current)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AnswerCard(state: AssistantState.Success) {
    Column(
        modifier = Modifier.fillMaxWidth().background(Mist, RoundedCornerShape(14.dp)).padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Grounded recommendation", fontWeight = FontWeight.Bold, color = Ink)
            Text(state.answer.provider, color = Accent)
        }
        Text(state.answer.text, color = Ink)
        state.answer.sources.forEach { source ->
            Text("Source: ${source.title} · ${(source.score * 100).toInt()}%", color = Ink.copy(alpha = 0.65f))
        }
    }
}
