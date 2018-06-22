package br.grupointegrado.tads.clima.sync

import android.content.Context
import br.grupointegrado.tads.buscadorgithub.NetworkUtils
import br.grupointegrado.tads.clima.dados.ClimaContrato
import br.grupointegrado.tads.clima.util.JsonUtils

object ClimaSyncTask {

    @Synchronized
    fun sincronizarClima(context: Context) {
        try {
            val weatherRequestUrl = NetworkUtils.getUrl(context)

            println(weatherRequestUrl)

            val jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl)

            val weatherValues = JsonUtils
                    .getWeatherContentValuesFromJson(context, jsonWeatherResponse)

            if (weatherValues != null && weatherValues.size != 0) {
                val sunshineContentResolver = context.contentResolver

                sunshineContentResolver.delete(ClimaContrato.Clima.CONTENT_URI, null, null)


                sunshineContentResolver
                        .bulkInsert(ClimaContrato.Clima.CONTENT_URI, weatherValues)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}