package uabc.edu.secuencias.logic

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import uabc.edu.secuencias.data.GameState
import uabc.edu.secuencias.data.Intento
import uabc.edu.secuencias.data.Secuencia
import uabc.edu.secuencias.data.SecuenciasDatabase

class MainGame(context: Context) {
    var secuencias = SecuenciasDatabase(context)
    val intentosMaximos: Int = 5
    var intentosRestantes: Int = intentosMaximos
    var dificultad: String = "Alta"
    var timePerDificulty: Long = 60000
    var score: Int = 0
    var secuenciasEnJuego: List<Secuencia>? = null
    var scoreParaAvanzar = 45

    init {
        iniciarTurno()
    }

    fun iniciarTurno(): List<Secuencia>? {

        if (score >= scoreParaAvanzar) {
            when (dificultad) {
                "Baja" -> {
                    dificultad = "Media"
                    timePerDificulty = 180000
                }
                "Media" ->{
                    dificultad = "Alta"
                    timePerDificulty = 240000
                }
                "Alta" -> {
                    dificultad = "Alta"
                    timePerDificulty = 240000
                }
                else ->{
                    dificultad = ""
                    timePerDificulty = 0
                }
            }
        }

        secuenciasEnJuego = secuencias.secuenciasPorDificultad.get(dificultad)?.take(3)

        return secuenciasEnJuego
    }

    fun calificarRespuesta(userResponses: MutableMap<Pair<Int, Int>, String>): MutableMap<Pair<Int, Int>, Boolean> {
        intentosRestantes --
        val resultados = mutableMapOf<Pair<Int, Int>, Boolean>()

        secuenciasEnJuego?.forEachIndexed { index, secuencia ->
            secuencia.respuestas.forEachIndexed { idx, respuesta ->
                val userKey:Pair<Int, Int> = index to secuencia.indexs[idx]
                val userResponse:String? = userResponses[userKey]

                if (respuesta == userResponse) {
                    resultados[userKey] = true
                } else {
                    resultados[userKey] = false
                }
            }
        }

        return resultados
    }

    fun calificar(
        textosIngresados: MutableMap<Pair<Int, Int>, String>,
        calificaciones: MutableMap<Pair<Int, Int>, Boolean?>,
        cantidadActual: MutableState<Int>,
        showWinDialog: MutableState<Boolean>,
        showLoseDialog: MutableState<Boolean>,
        isTimerRunning: MutableState<Boolean>,
        intentosRestantes: MutableState<Int>,
        score: MutableState<Int>,
        time: MutableState<Long>
    ) {
        val cali:MutableMap<Pair<Int, Int>, Boolean> = calificarRespuesta(textosIngresados)

        cali.forEach { (key:Pair<Int, Int>, value:Boolean) ->
            if (key.first == cantidadActual.value - 1) {
                calificaciones[key] = value
            }
        }

        if (calificaciones.values.all { it == true } && cantidadActual.value == secuenciasEnJuego?.size ?: -1) {
            avanzarTurno(showWinDialog, isTimerRunning, time)
        } else {
            terminarTurno(cali, cantidadActual, time)
        }

        intentosRestantes.value = this.intentosRestantes
        score.value = this.score

        if (intentosRestantes.value <= 0) {
            showLoseDialog.value = true
            isTimerRunning.value = false
        }
    }

    fun avanzarTurno(
        showWinDialog: MutableState<Boolean> = mutableStateOf(false),
        isTimerRunning: MutableState<Boolean> = mutableStateOf(true),
        time: MutableState<Long>
    ){
        showWinDialog.value = true
        isTimerRunning.value = false
        this.score += 3

        GameState.tiempoPorSecuencia.add(this.timePerDificulty - time.value)

        for (i in GameState.tiempoPorSecuencia.size - 1 downTo 1) {
            GameState.tiempoPorSecuencia[i] = GameState.tiempoPorSecuencia[i] - GameState.tiempoPorSecuencia[i - 1]
        }

        GameState.intentos.add(
            Intento(
                secuencias = this.secuenciasEnJuego,
                respuestas = this.secuenciasEnJuego?.flatMap { it.respuestas.toList() } ?: emptyList(),
                indexs = this.secuenciasEnJuego?.flatMap { it.indexs.toList() } ?: emptyList(),
                tiempoPorSecuencia = GameState.tiempoPorSecuencia,
                tiempoGeneral = this.timePerDificulty - time.value,
                dificultad = this.dificultad
            )
        )
        GameState.tiempoPorSecuencia = MutableList(0) { 0 }
    }

    fun terminarTurno(
        cali: MutableMap<Pair<Int, Int>, Boolean>,
        cantidadActual: MutableState<Int>,
        time: MutableState<Long>
    ){

        if (cali.filterKeys { it.first == cantidadActual.value - 1 }.values.all { it }) {
            if (cantidadActual.value < secuenciasEnJuego?.size ?: -1) {
                cantidadActual.value++
                this.intentosRestantes = intentosMaximos
                this.score += 3
                GameState.tiempoPorSecuencia.add(this.timePerDificulty- time.value)
            }
        }
    }
}

