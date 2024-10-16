package uabc.edu.secuencias

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import uabc.edu.secuencias.ui.theme.Secuencias_ExamenTheme
import androidx.compose.foundation.layout.*
import uabc.edu.secuencias.data.GameResult
import uabc.edu.secuencias.data.GameState
import uabc.edu.secuencias.data.SecuenciasDatabase
import uabc.edu.secuencias.gui.GameView
import uabc.edu.secuencias.logic.MainGame

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val score: Int = intent.getIntExtra("SCORE", 0)
            val difficulty: String = intent.getStringExtra("DIFICULTY") ?: "Alta"


        val currentGameManager = MainGame(this)
        currentGameManager.score = score
        currentGameManager.dificultad = difficulty

        currentGameManager.iniciarTurno()

        setContent {
            Secuencias_ExamenTheme {
                GameView(currentGameManager, context = this)
            }
        }
    }
}
