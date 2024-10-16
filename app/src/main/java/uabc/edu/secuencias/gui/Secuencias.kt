package uabc.edu.secuencias.gui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import uabc.edu.secuencias.data.Secuencia

@Composable
fun SequencesGrid(
    cantidadActual: MutableState<Int>,
    secuenciasEnJuego: List<Secuencia>?,
    textosIngresados: MutableMap<Pair<Int, Int>, String>,
    calificaciones: MutableMap<Pair<Int, Int>, Boolean?>
) {
    var mostradas = 0
    secuenciasEnJuego?.forEachIndexed { idSecuencia, secuencia ->
        val enable = idSecuencia == cantidadActual.value - 1

        if(mostradas < cantidadActual.value) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(6),
                contentPadding = PaddingValues(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 50.dp, max = 200.dp)
                    .padding(top = 10.dp)
            ) {
                itemsIndexed(secuencia.secuencia) { idx, item ->
                    Secuence(
                        textosIngresados,
                        calificaciones,
                        idSecuencia,
                        idx,
                        secuencia,
                        enable,
                        item
                    )
                }
            }
        }
        mostradas ++
    }
}

@Composable
fun Secuence(
    textosIngresados: MutableMap<Pair<Int, Int>, String>,
    calificaciones: MutableMap<Pair<Int, Int>, Boolean?>,
    idSecuencia: Int,
    idx: Int,
    secuencia: Secuencia,
    enable: Boolean,
    item: String
){
    val uniqueKey = idSecuencia to idx
    val borderColor = when (calificaciones[uniqueKey]) {
        true -> Color.Green
        false -> Color.Red
        else -> Color.Black
    }

    if (secuencia.indexs.contains(idx)) {
        val currentText = textosIngresados[uniqueKey] ?: ""
        BasicTextField(
            value = currentText,
            onValueChange = { newText ->
                val filteredText = newText.filter { it.isDigit() }
                textosIngresados[uniqueKey] = filteredText
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .border(1.dp, borderColor),
            textStyle = TextStyle(textAlign = TextAlign.Center),
            enabled = enable,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

    } else {
        Text(
            text = item,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}