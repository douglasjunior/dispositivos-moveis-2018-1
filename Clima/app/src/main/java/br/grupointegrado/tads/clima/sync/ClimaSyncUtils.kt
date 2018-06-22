package br.grupointegrado.tads.clima.sync

import android.content.Context
import android.content.Intent

object ClimaSyncUtils {

    private var sincronizado: Boolean = false

    @Synchronized
    fun inicializar(context: Context) {
        if (sincronizado) return

        sincronizado = true

        sincronizarImediatamente(context)
    }

    fun sincronizarImediatamente(context: Context) {
        val intentToSyncImmediately = Intent(context, ClimaIntentService::class.java)
        context.startService(intentToSyncImmediately)
    }

}