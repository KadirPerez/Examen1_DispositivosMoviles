package uabc.edu.secuencias

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import uabc.edu.secuencias.data.GameResult
import uabc.edu.secuencias.data.Intento
import uabc.edu.secuencias.gui.GameResultsScreen
import uabc.edu.secuencias.gui.MainMenu
import uabc.edu.secuencias.ui.theme.Secuencias_ExamenTheme

class MenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Secuencias_ExamenTheme {
                MainMenu(context = this)
            }
        }
    }
}
