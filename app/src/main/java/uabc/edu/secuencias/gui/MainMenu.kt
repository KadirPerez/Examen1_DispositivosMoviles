package uabc.edu.secuencias.gui

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uabc.edu.secuencias.MainActivity
import uabc.edu.secuencias.ResultsActivity
import uabc.edu.secuencias.data.GameResult
import uabc.edu.secuencias.data.GameState
import uabc.edu.secuencias.logic.navigateToActivity


@Composable
fun MainMenu(
    modifier: Modifier = Modifier,
    context: Context
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Adivina la secuencia",
            modifier = Modifier.padding(bottom = 16.dp)
        )


        Button(
            onClick = {
                navigateToActivity(context, MainActivity::class.java)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(text = "Iniciar Juego")
        }

        Button(
            onClick = {
                navigateToActivity(context, ResultsActivity::class.java)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(text = "Puntajes")
        }
    }
}
