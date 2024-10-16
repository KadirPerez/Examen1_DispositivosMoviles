package uabc.edu.secuencias.logic

import android.app.Activity
import android.content.Context
import uabc.edu.secuencias.MainActivity

fun restartMainActivity(context: Context, game: MainGame) {
    val mainActivityClass: Class<*> = MainActivity::class.java
    val intent = android.content.Intent(context, mainActivityClass)

    intent.putExtra("SCORE", game.score)
    intent.putExtra("DIFICULTY", game.dificultad)

    context.startActivity(intent)

    if (context is Activity) {
        context.finish()
        context.overridePendingTransition(0, 0)
    }
}

fun navigateToActivity(context: Context, targetActivity: Class<*>) {

    val intent = android.content.Intent(context, targetActivity)

    context.startActivity(intent)

    if (context is Activity) {
        context.finish()
        context.overridePendingTransition(0, 0)
    }
}