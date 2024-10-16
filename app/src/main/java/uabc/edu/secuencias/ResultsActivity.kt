package uabc.edu.secuencias

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import uabc.edu.secuencias.data.GameResult
import uabc.edu.secuencias.data.loadAllGameResults
import uabc.edu.secuencias.gui.GameResultsScreen
import uabc.edu.secuencias.gui.MainMenu
import uabc.edu.secuencias.ui.theme.Secuencias_ExamenTheme

class ResultsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Secuencias_ExamenTheme {
                val gameToShow: List<GameResult>? = loadAllGameResults(this, "gameResult.json")
                GameResultsScreen(contex = this, games = gameToShow)
            }
        }
    }
}