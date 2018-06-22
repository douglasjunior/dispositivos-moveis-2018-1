package br.grupointegrado.tads.clima.sync

import android.app.IntentService
import android.content.Intent

class ClimaIntentService : IntentService {

    constructor() : super("ClimaIntentService") {
    }

    override fun onHandleIntent(intent: Intent?) {
        ClimaSyncTask.sincronizarClima(this)
    }
}