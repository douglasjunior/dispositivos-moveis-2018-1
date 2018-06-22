package br.grupointegrado.tads.clima.util

import android.content.ContentValues
import android.content.Context
import br.grupointegrado.tads.clima.dados.ClimaContrato
import java.util.ArrayList
import java.util.concurrent.TimeUnit

object DadosFakeUtils {

    private val weatherIDs = intArrayOf(200, 300, 500, 711, 900, 962)

    private fun createTestWeatherContentValues(date: Long): ContentValues {
        val Clima = ClimaContrato.Clima
        val testWeatherValues = ContentValues()
        testWeatherValues.put(Clima.COLUNA_DATA_HORA, date)
        testWeatherValues.put(Clima.COLUNA_GRAUS, Math.random() * 2)
        testWeatherValues.put(Clima.COLUNA_UMIDADE, Math.random() * 100)
        testWeatherValues.put(Clima.COLUNA_PRESSAO, 870 + Math.random() * 100)
        val maxTemp = (Math.random() * 100).toInt()
        testWeatherValues.put(Clima.COLUNA_MAX_TEMP, maxTemp)
        testWeatherValues.put(Clima.COLUNA_MIN_TEMP, maxTemp - (Math.random() * 10).toInt())
        testWeatherValues.put(Clima.COLUNA_VEL_VENTO, Math.random() * 10)
        testWeatherValues.put(Clima.COLUNA_CLIMA_ID, weatherIDs[(Math.random() * 10).toInt() % 5])
        return testWeatherValues
    }

    fun inserirDadosFake(context: Context) {
        val today = System.currentTimeMillis()
        val fakeValues = ArrayList<ContentValues>()

        for (i in 0..6) {
            fakeValues.add(createTestWeatherContentValues(today + TimeUnit.DAYS.toMillis(i.toLong())))
        }

        context.contentResolver.bulkInsert(
                ClimaContrato.Clima.CONTENT_URI,
                fakeValues.toTypedArray())
    }
}