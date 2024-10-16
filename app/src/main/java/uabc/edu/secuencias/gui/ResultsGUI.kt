package uabc.edu.secuencias.gui

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uabc.edu.secuencias.MenuActivity
import uabc.edu.secuencias.data.GameResult
import uabc.edu.secuencias.data.Intento
import uabc.edu.secuencias.logic.navigateToActivity



@Composable
fun GameResultCard(gameResult: GameResult) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Puntaje: ${gameResult.score}",
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyRow(modifier = Modifier.height(300.dp)) {
                items(gameResult.intentos) { intento ->
                    IntentoCard(intento)
                }
            }
        }
    }
}

@Composable
fun IntentoCard(intento: Intento) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Dificultad: ${intento.dificultad}")

            intento.secuencias?.forEachIndexed { secuenciaIdx, secuencia ->
                Text("Secuencia ${secuenciaIdx + 1} - Tiempo: ${formatTime(intento.tiempoPorSecuencia[secuenciaIdx])}")

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    secuencia.secuencia.forEachIndexed { idx, value ->
                        if (secuencia.indexs.contains(idx)) {
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .border(2.dp, Color.Blue)
                                    .padding(4.dp)
                            ) {
                                Text(text = value)
                            }
                        } else {
                            Text(
                                text = value,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }
            }

            Text("Tiempo general: ${formatTime(intento.tiempoGeneral)}")
        }
    }
}

@Composable
fun GameResultsScreen(contex: Context, games: List<GameResult>?) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            modifier = Modifier.padding(top = 50.dp, bottom = 10.dp),
            text = "Resultados",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Blue
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(580.dp),
            contentPadding = PaddingValues(10.dp)
        ) {
            items(games ?: emptyList()) { gameResult ->
                GameResultCard(gameResult)
            }
        }

        Button(
            modifier = Modifier.padding(top = 10.dp),
            onClick = { navigateToActivity(context = contex, MenuActivity::class.java) }) {
            Text(text = "Menu Principal")
        }
    }
}