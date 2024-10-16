package uabc.edu.secuencias.gui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import uabc.edu.secuencias.data.GameState
import java.util.concurrent.TimeUnit

fun formatTime(timeM1:Long): String {
    val min:Long = TimeUnit.MILLISECONDS.toMinutes(timeM1) % 60
    val sec:Long = TimeUnit.MILLISECONDS.toSeconds(timeM1) % 60
    return String.format("%02d:%02d",min,sec)
}

@Composable
fun Timer(time: MutableState<Long>, isRunning: Boolean, onTimerEnd: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp, bottom = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = formatTime(timeM1 = time.value),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Blue
        )
    }

    // Usamos LaunchedEffect para iniciar el temporizador cuando isRunning cambia
    LaunchedEffect(isRunning) {
        if (isRunning) {
            val startTime = System.currentTimeMillis() // Tiempo inicial (milisegundos)
            val initialTime = time.value              // Guardamos el tiempo inicial del temporizador

            while (time.value > 0) {
                // Tiempo actual menos el tiempo inicial
                val elapsedTime = System.currentTimeMillis() - startTime
                val remainingTime = initialTime - elapsedTime

                // Actualizamos el valor del temporizador
                time.value = remainingTime

                // Si el tiempo llega a 0 o menos, llamamos a onTimerEnd y detenemos el bucle
                if (time.value <= 0) {
                    time.value = 0
                    onTimerEnd()
                    break
                }

                delay(100L) // Intervalo de actualización
            }
        }
    }
}
