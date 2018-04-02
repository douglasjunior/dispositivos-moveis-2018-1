package br.grupointegrado.tads.clima.util

import android.content.Context
import org.json.JSONException
import org.json.JSONObject
import java.net.HttpURLConnection

class JsonUtils {

    companion object {

        val OWM_LIST = "list"

        val OWM_TEMPERATURE = "temp"

        val OWM_MAX = "max"
        val OWM_MIN = "min"

        val OWM_WEATHER = "weather"
        val OWM_DESCRIPTION = "main"

        val OWM_MESSAGE_CODE = "cod"

        /**
         * Este método interpreta o JSON recebido do servidor e retorna um array de Strings
         * descrevendo o clima de vários dias de previsão.
         *
         * Posteriormente, analisaremos o JSON em dados estruturados dentro da função
         * getFullWeatherDataFromJson, aproveitando os dados que armazenamos no JSON. Por enquanto,
         * apenas convertemos o JSON em strings legíveis por humanos.
         *
         */
        @Throws(JSONException::class)
        fun getSimplesStringsDeClimaDoJson(context: Context, stringJsonPrevisao: String): Array<String>? {

            /* String array to hold each day's weather String */
            var dadosDoClima: Array<String>? = null

            val jsonPrevisao = JSONObject(stringJsonPrevisao)

            /* Is there an error? */
            if (jsonPrevisao.has(OWM_MESSAGE_CODE)) {
                val codigoStatus = jsonPrevisao.getInt(OWM_MESSAGE_CODE)

                when (codigoStatus) {
                    HttpURLConnection.HTTP_OK -> {
                        /* Ok */
                    }
                    HttpURLConnection.HTTP_NOT_FOUND ->
                        /* Localização inválida */
                        return null
                    else ->
                        /* Conexão perdida */
                        return null
                }
            }

            val arrayClima = jsonPrevisao.getJSONArray(OWM_LIST)

            dadosDoClima = arrayOf(arrayClima.length().toString())

            val dataLocal = System.currentTimeMillis()
            val dataUtc = DataUtils.convertDataLocalParaUtc(dataLocal)
            val inicioDoDia = DataUtils.normalizarData(dataUtc)

            for (i in 0 until arrayClima.length()) {
                val data: String
                val maximaEMinima: String

                val dataHoraEmMilissegundos: Long
                val max: Double
                val min: Double
                val descricao: String

                val diaPrevisao = arrayClima.getJSONObject(i)


                dataHoraEmMilissegundos = inicioDoDia + DataUtils.DIA_EM_MILISSEGUNDOS * i
                data = DataUtils.getDataAmigavelEmString(context, dataHoraEmMilissegundos, false)


                val objetoClima = diaPrevisao.getJSONArray(OWM_WEATHER).getJSONObject(0)
                descricao = objetoClima.getString(OWM_DESCRIPTION)

                val objetoTemperatura = diaPrevisao.getJSONObject(OWM_TEMPERATURE)
                max = objetoTemperatura.getDouble(OWM_MAX)
                min = objetoTemperatura.getDouble(OWM_MIN)
                maximaEMinima = ClimaUtils.formataMaxMin(context, max, min)

                dadosDoClima[i] = "$data - $descricao - $maximaEMinima"
            }

            return dadosDoClima
        }

    }
}