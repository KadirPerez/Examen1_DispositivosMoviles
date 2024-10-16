package uabc.edu.secuencias.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import java.io.File

data class GameResult(
    var intentos: MutableList<Intento>,
    var score: Int
)

data class Intento (
    var secuencias: List<Secuencia>?,
    var respuestas: List<String>,
    var indexs: List<Int>,
    var tiempoPorSecuencia: List<Long>,
    var tiempoGeneral: Long,
    var dificultad: String
)

object GameState {
    var intentos: MutableList<Intento> = mutableListOf()
    var tiempoPorSecuencia: MutableList<Long> = mutableListOf()

}

fun saveGameResult(context: Context, newGameResult: GameResult, fileName: String) {
    val gson = Gson()
    val existingGameResults = loadAllGameResults(context, fileName)?.toMutableList() ?: mutableListOf()
    existingGameResults.add(newGameResult)
    val jsonString = gson.toJson(existingGameResults)
    val file = File(context.filesDir, fileName)
    file.writeText(jsonString)
}

fun loadAllGameResults(context: Context, fileName: String): List<GameResult>? {
    val file = File(context.filesDir, fileName)
    if (!file.exists()) {
        return null
    }
    val jsonString = file.readText()
    val gson = Gson()

    return try {
        // Intentar cargar como lista
        val gameResultType = object : TypeToken<List<GameResult>>() {}.type
        gson.fromJson<List<GameResult>>(jsonString, gameResultType)
    } catch (e: JsonSyntaxException) {
        // Si no es una lista, intentar cargar como un solo objeto
        try {
            val singleGameResult = gson.fromJson(jsonString, GameResult::class.java)
            listOf(singleGameResult) // Convertir el objeto a lista
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
