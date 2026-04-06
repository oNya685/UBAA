package cn.edu.ubaa

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
  val icon = painterResource("app-icon.png")
  Window(onCloseRequest = ::exitApplication, title = "UBAA", icon = icon) { App() }
}
