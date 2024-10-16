package uabc.edu.secuencias.data

data class Secuencia(
    val secuencia: MutableList<String>,
    val logica: String,
    val dificultad: String,
    var respuestas: MutableList<String>,
    var indexs: MutableList<Int>,
) {

    fun selectAnsware() {
        respuestas = mutableListOf()
        indexs = mutableListOf()

        val answareSize = when (dificultad) {
            "Baja" -> 1
            "Media" -> 3
            "Alta" -> 4
            else -> 0
        }

        if (secuencia.size >= answareSize) {
            val availableIndices = secuencia.indices.toMutableList()

            for (i in 0 until answareSize) {
                val randomIndex = availableIndices.random()

                respuestas.add(secuencia[randomIndex])
                indexs.add(randomIndex)

                availableIndices.remove(randomIndex)
            }
        }
    }
}
