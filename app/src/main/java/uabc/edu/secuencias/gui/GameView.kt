package uabc.edu.secuencias.gui

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import uabc.edu.secuencias.MenuActivity
import uabc.edu.secuencias.data.GameResult
import uabc.edu.secuencias.data.GameState
import uabc.edu.secuencias.data.saveGameResult
import uabc.edu.secuencias.logic.MainGame
import uabc.edu.secuencias.logic.navigateToActivity
import uabc.edu.secuencias.logic.restartMainActivity


@SuppressLint("RememberReturnType")
@Composable
fun GameView(
    game: MainGame,
    context: Context
) {
    val gameController = remember { game }
    val intentosRestantes = remember { mutableStateOf(gameController.intentosRestantes) }
    val difficulty = remember { mutableStateOf(gameController.dificultad) }
    val score = remember { mutableStateOf(gameController.score) }
    val showWinDialog = remember { mutableStateOf(false) }
    val showLoseDialog = remember { mutableStateOf(false) }
    val isTimerRunning = remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)
    val textosIngresados = remember { mutableStateMapOf<Pair<Int, Int>, String>() }
    val calificaciones = remember { mutableStateMapOf<Pair<Int, Int>, Boolean?>() }
    val cantidadActual = remember { mutableStateOf(1) }
    val time = remember { mutableStateOf(gameController.timePerDificulty) }

    LaunchedEffect(key1 = keyboardHeight) {
        coroutineScope.launch {
            scrollState.scrollBy(keyboardHeight.toFloat())
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(scrollState)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Header(
                difficulty = difficulty.value,
                score = score.value,
                timePerDificulty = time,
                intentosRestantes = intentosRestantes.value,
                isRunning = isTimerRunning.value,
                onTimerEnd = {
                    showLoseDialog.value = true
                    isTimerRunning.value = false
                }
            )

            SequencesGrid(
                cantidadActual = cantidadActual,
                secuenciasEnJuego = gameController.secuenciasEnJuego,
                textosIngresados = textosIngresados,
                calificaciones = calificaciones
            )

            Button(
                onClick = {
                    gameController.calificar(
                        textosIngresados = textosIngresados,
                        calificaciones = calificaciones,
                        cantidadActual = cantidadActual,
                        showWinDialog = showWinDialog,
                        showLoseDialog = showLoseDialog,
                        isTimerRunning = isTimerRunning,
                        intentosRestantes = intentosRestantes,
                        score = score,
                        time = time
                    )
                },
                modifier = Modifier.padding(top = 25.dp)
            ) {
                Text(text = "Revisar")
            }
        }
    }

    if (showWinDialog.value) {
        val title = "¡Ganaste!"
        val message:String = if (gameController.score >= gameController.scoreParaAvanzar) {
            "¡Felicidades!, avanzarás de dificultad"
        } else {
            "¡Felicidades!"
        }
        ResultDialog(
            title,
            message,
            context,
            showWinDialog,
            gameController,
            false
        )
    }
    if (showLoseDialog.value) {
        val title = "Perdiste"
        val message = "Vuelve a intentarlo"
        ResultDialog(
            title,
            message,
            context,
            showLoseDialog,
            gameController,
            true
        )
    }

}

@Composable
fun Header(
    difficulty: String,
    score: Int,
    intentosRestantes: Int,
    isRunning: Boolean,
    onTimerEnd: () -> Unit,
    timePerDificulty: MutableState<Long>
) {
    Text(
        modifier = Modifier.padding(top = 50.dp, bottom = 20.dp),
        text = "Dificultad: $difficulty",
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Blue
    )

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
        Text(text = "Puntaje: $score", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Blue)
        Text(text = "Intentos: $intentosRestantes", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Blue)
    }

    Timer(time = timePerDificulty, isRunning = isRunning, onTimerEnd = onTimerEnd)

    Divider(
        color = Color.Gray,
        thickness = 1.dp,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun ResultDialog(
    title: String,
    message: String,
    context: Context,
    showWinDialog: MutableState<Boolean>,
    gameController: MainGame,
    lost: Boolean
) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            if (!lost) {
                Button(
                    onClick = {
                        showWinDialog.value = false
                        restartMainActivity(context = context, game = gameController)
                    }
                ) {
                    Text("Continuar")
                }
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    showWinDialog.value = false
                    val gameToSave = GameResult(GameState.intentos, gameController.score)

                    saveGameResult(
                        context = context,
                        newGameResult = gameToSave,
                        fileName = "gameResult.json"
                    )

                    GameState.intentos = mutableListOf()
                    GameState.tiempoPorSecuencia = mutableListOf()
                    navigateToActivity(context = context, MenuActivity::class.java)
                }
            ) {
                Text("Salir")
            }
        },
        modifier = Modifier.width(300.dp)
    )
}
