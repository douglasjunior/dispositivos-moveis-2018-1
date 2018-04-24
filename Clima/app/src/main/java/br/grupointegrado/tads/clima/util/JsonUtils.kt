package br.grupointegrado.tads.clima.util

import android.content.Context
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.net.HttpURLConnection

class JsonUtils {

    companion object {

        val OWM_LIST = "list"


        val OWM_WEATHER = "weather"
        val OWM_DESCRIPTION = "description"

        val OWM_MAIN = "main"
        val OWM_TEMPERATURE = "temp"
        val OWM_MAX = "temp_max"
        val OWM_MIN = "temp_min"

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
        fun getSimplesStringsDeClimaDoJson(context: Context, stringJsonPrevisao: String): Array<String?>? {
            Log.v("JsonUtils", stringJsonPrevisao)

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

            val dadosDoClima = arrayOfNulls<String>(arrayClima.length())

            val dataLocal = System.currentTimeMillis()
            val dataUtc = DataUtils.convertDataLocalParaUtc(dataLocal)
            val inicioDoDia = DataUtils.normalizarData(dataUtc)

            for (i in 0 until arrayClima.length()) {
                val data: String
                val maximaEMinima: String

                val diaPrevisao = arrayClima.getJSONObject(i)

                val dataHoraEmMilissegundos = inicioDoDia + DataUtils.DIA_EM_MILISSEGUNDOS * i
                data = DataUtils.getDataAmigavelEmString(context, dataHoraEmMilissegundos, false)

                val objetoClima = diaPrevisao.getJSONArray(OWM_WEATHER).getJSONObject(0)
                val descricao = objetoClima.getString(OWM_DESCRIPTION)

                val objetoPrincipal = diaPrevisao.getJSONObject(OWM_MAIN)

                val max = objetoPrincipal.getDouble(OWM_MAX)
                val min = objetoPrincipal.getDouble(OWM_MIN)
                maximaEMinima = ClimaUtils.formataMaxMin(context, max, min)

                dadosDoClima[i] = "$data - $descricao - $maximaEMinima"
            }

            return dadosDoClima
        }

    }
}