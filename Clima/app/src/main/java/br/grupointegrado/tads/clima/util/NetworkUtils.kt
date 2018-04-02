package br.grupointegrado.tads.buscadorgithub

import android.net.Uri
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class NetworkUtils {

    companion object {

        private val TAG = NetworkUtils::class.java.simpleName

        private val OFFICIAL_WEATHER_URL = "https://api.openweathermap.org/data/2.5/forecast"

        private val FAKE_WEATHER_URL = "https://gist.githubusercontent.com/douglasjunior/03febb9bbcffbd02027b5a86d9c1060a/raw"

        private val FORECAST_BASE_URL = FAKE_WEATHER_URL

        private val APPID_PARAM = "appid"
        private val QUERY_PARAM = "q"
        private val LAT_PARAM = "lat"
        private val LON_PARAM = "lon"
        private val FORMAT_PARAM = "mode"
        private val UNITS_PARAM = "units"
        private val DAYS_PARAM = "cnt"

        /* Formato que desejamos receber a resposta da requisição */
        private val format = "json"
        /* Unidade de medida usada na temperatura, pressão, velocidade do vento, etc */
        private val units = "metric"
        /* Número de dias */
        private val numDays = 14
        /* Chave de autenticação obtida no https://home.openweathermap.org/api_keys */
        private  val appKey = "my-key" // substitua essa String pela sua chave de autenticação do Open Weather Map

        fun construirUrl(local: String): URL? {
            val builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, local)
                    .appendQueryParameter(FORMAT_PARAM, format)
                    .appendQueryParameter(UNITS_PARAM, units)
                    .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                    .appendQueryParameter(APPID_PARAM, appKey)
                    .build()

            var url: URL? = null
            try {
                url = URL(builtUri.toString())
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }

            Log.d(TAG, "URI gerada $url")

            return url
        }

        fun construirUrl(lat: Double?, lon: Double?): URL? {
            /** Este será implementado no futuro  */
            return null
        }

        fun obterRespostaDaUrlHttp(url: URL): String? {
            val urlConnection = url.openConnection() as HttpURLConnection
            var inputStream : InputStream? = null
            try {
                inputStream = urlConnection.inputStream

                val scanner = Scanner(inputStream)
                scanner.useDelimiter("\\A")

                if (scanner.hasNext()) {
                    return scanner.next()
                }
            } finally {
                inputStream?.close()
                urlConnection.disconnect()
            }
            return null
        }

    }

}