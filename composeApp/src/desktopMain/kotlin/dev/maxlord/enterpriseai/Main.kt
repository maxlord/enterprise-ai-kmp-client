package dev.maxlord.enterpriseai

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.maxlord.enterpriseai.data.DemoAiGateway
import dev.maxlord.enterpriseai.ui.EnterpriseAiApp

fun main() = application {
    val apiUrl = System.getenv("ENTERPRISE_AI_API_URL")
    val gateway = apiUrl?.takeIf(String::isNotBlank)?.let(::DesktopAiGateway) ?: DemoAiGateway()
    Window(onCloseRequest = ::exitApplication, title = "Enterprise AI Client") {
        EnterpriseAiApp(gateway)
    }
}
