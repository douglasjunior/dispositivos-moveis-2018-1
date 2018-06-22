package br.grupointegrado.tads.buscadorgithub

import android.content.Context
import android.net.Uri
import android.util.Log
import br.grupointegrado.tads.clima.dados.ClimaPreferencias
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*

object NetworkUtils {


    private val TAG = NetworkUtils::class.java.simpleName

    private val OFFICIAL_WEATHER_URL = "https://api.openweathermap.org/data/2.5/forecast"

    private val FAKE_WEATHER_URL = "https://gist.githubusercontent.com/douglasjunior/03febb9bbcffbd02027b5a86d9c1060a/raw"

    private val FORECAST_BASE_URL = OFFICIAL_WEATHER_URL

    private val APPID_PARAM = "appid"
    private val QUERY_PARAM = "q"
    private val LAT_PARAM = "lat"
    private val LON_PARAM = "lon"
    private val FORMAT_PARAM = "mode"
    private val UNITS_PARAM = "units"
    private val DAYS_PARAM = "cnt"
    private val LANG_PARAM = "lang"

    /* Formato que desejamos receber a resposta da requisição */
    private val format = "json"
    /* Unidade de medida usada na temperatura, pressão, velocidade do vento, etc */
    private val units = "metric"
    /* Número de previsões */
    private val num = 40
    /* Idioma do retorno */
    private val lang = "pt"

    /* Faça o cadastro em https://home.openweathermap.org/users/sign_up */
    /* Chave de autenticação obtida no https://home.openweathermap.org/api_keys */
    private val appKey = "sua-key-aqui" // substitua essa String pela sua chave de autenticação do Open Weather Map

    fun getUrl(context: Context): URL? {
        if (ClimaPreferencias.isCoordenadasDisponiveis(context)) {
            val preferredCoordinates = ClimaPreferencias.getCoordenadasDaLocalizacao(context)
            val latitude = preferredCoordinates[0]
            val longitude = preferredCoordinates[1]
            return buildUrlWithLatitudeLongitude(latitude, longitude)
        } else {
            val locationQuery = ClimaPreferencias.getLocalizacaoSalva(context)
            return buildUrlWithLocationQuery(locationQuery)
        }
    }

    private fun buildUrlWithLatitudeLongitude(latitude: Double?, longitude: Double?): URL? {
        val weatherQueryUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                .appendQueryParameter(LAT_PARAM, latitude.toString())
                .appendQueryParameter(LON_PARAM, longitude.toString())
                .appendQueryParameter(FORMAT_PARAM, format)
                .appendQueryParameter(UNITS_PARAM, units)
                .appendQueryParameter(DAYS_PARAM, num.toString())
                .appendQueryParameter(APPID_PARAM, appKey)
                .appendQueryParameter(LANG_PARAM, lang)
                .build()

        try {
            val weatherQueryUrl = URL(weatherQueryUri.toString())
            Log.v(TAG, "URL: $weatherQueryUrl")
            return weatherQueryUrl
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            return null
        }

    }

    private fun buildUrlWithLocationQuery(locationQuery: String): URL? {
        val weatherQueryUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, locationQuery)
                .appendQueryParameter(FORMAT_PARAM, format)
                .appendQueryParameter(UNITS_PARAM, units)
                .appendQueryParameter(DAYS_PARAM, num.toString())
                .appendQueryParameter(APPID_PARAM, appKey)
                .appendQueryParameter(LANG_PARAM, lang)
                .build()

        try {
            val weatherQueryUrl = URL(weatherQueryUri.toString())
            Log.v(TAG, "URL: $weatherQueryUrl")
            return weatherQueryUrl
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            return null
        }
    }

    fun getResponseFromHttpUrl(url: URL?): String? {
        val urlConnection = url?.openConnection() as HttpURLConnection
        try {
            val input = urlConnection.inputStream

            val scanner = Scanner(input)
            scanner.useDelimiter("\\A")

            val hasInput = scanner.hasNext()
            var response: String? = null
            if (hasInput) {
                response = scanner.next()
            }
            scanner.close()
            return response
        } finally {
            urlConnection.disconnect()
        }
    }
}