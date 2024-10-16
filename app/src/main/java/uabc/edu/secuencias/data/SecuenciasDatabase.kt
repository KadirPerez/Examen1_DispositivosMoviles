package uabc.edu.secuencias.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SecuenciasDatabase(context: Context) {
    val secuencias:List<Secuencia>
    val secuenciasPorDificultad: Map<String, List<Secuencia>>

    init {
        val inputStream = context.assets.open("secuencias.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }

        val questionListType = object : TypeToken<List<Secuencia>>() {}.type
        secuencias = Gson().fromJson(jsonString, questionListType)
        secuencias.forEach { it.selectAnsware() }

        secuenciasPorDificultad = secuencias
            .groupBy { it.dificultad }
            .mapValues { it.value.toMutableList() }

        secuenciasPorDificultad.forEach { (_, listaSecuencias) ->
            listaSecuencias.shuffle()
        }
    }
}