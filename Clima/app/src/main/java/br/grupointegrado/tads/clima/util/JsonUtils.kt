package br.grupointegrado.tads.clima.util

import android.content.ContentValues
import android.content.Context
import br.grupointegrado.tads.clima.dados.ClimaContrato
import br.grupointegrado.tads.clima.dados.ClimaPreferencias


import org.json.JSONException
import org.json.JSONObject

import java.net.HttpURLConnection

/**
 * Utility functions to handle OpenWeatherMap JSON data.
 */
object JsonUtils {

    /* Location information */
    private val OWM_CITY = "city"
    private val OWM_COORD = "coord"

    /* Location coordinate */
    private val OWM_LATITUDE = "lat"
    private val OWM_LONGITUDE = "lon"

    /* Weather information. Each day's forecast info is an element of the "list" array */
    private val OWM_LIST = "list"

    private val OWM_PRESSURE = "pressure"
    private val OWM_HUMIDITY = "humidity"


    private val OWM_WINDSPEED = "speed"
    private val OWM_WIND_DIRECTION = "deg"

    /* All temperatures are children of the "temp" object */
    private val OWM_TEMPERATURE = "temp"

    /* Max temperature for the day */
    private val OWM_MAX = "temp_max"
    private val OWM_MIN = "temp_min"

    private val OWM_WEATHER = "weather"
    private val OWM_WEATHER_ID = "id"

    private val OWM_MESSAGE_CODE = "cod"

    @Throws(JSONException::class)
    fun getWeatherContentValuesFromJson(context: Context, forecastJsonStr: String?): Array<ContentValues?>? {

        val forecastJson = JSONObject(forecastJsonStr)

        if (forecastJson.has(OWM_MESSAGE_CODE)) {
            val errorCode = forecastJson.getInt(OWM_MESSAGE_CODE)

            when (errorCode) {
                HttpURLConnection.HTTP_OK -> {
                }
                HttpURLConnection.HTTP_NOT_FOUND ->
                    return null
                else ->
                    return null
            }
        }

        val jsonWeatherArray = forecastJson.getJSONArray(OWM_LIST)

        val cityJson = forecastJson.getJSONObject(OWM_CITY)

        val cityCoord = cityJson.getJSONObject(OWM_COORD)
        val cityLatitude = cityCoord.getDouble(OWM_LATITUDE)
        val cityLongitude = cityCoord.getDouble(OWM_LONGITUDE)

        ClimaPreferencias.setDetalhesLocalizacao(context, cityLatitude, cityLongitude)

        val weatherContentValues = arrayOfNulls<ContentValues>(jsonWeatherArray.length())

        for (i in 0 until jsonWeatherArray.length()) {

            val pressure: Double
            val humidity: Int
            val windSpeed: Double
            val windDirection: Double

            val high: Double
            val low: Double

            val weatherId: Int


            val dayForecast = jsonWeatherArray.getJSONObject(i)
            val main = dayForecast.getJSONObject("main")
            val wind = dayForecast.getJSONObject("wind")

            val dt = dayForecast.getLong("dt") * 1000

            pressure = main.getDouble(OWM_PRESSURE)
            humidity = main.getInt(OWM_HUMIDITY)

            windSpeed = wind.getDouble(OWM_WINDSPEED)
            windDirection = wind.getDouble(OWM_WIND_DIRECTION)

            val weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0)

            weatherId = weatherObject.getInt(OWM_WEATHER_ID)

            high = main.getDouble(OWM_MAX)
            low = main.getDouble(OWM_MIN)

            val Clima = ClimaContrato.Clima

            val weatherValues = ContentValues()
            weatherValues.put(Clima.COLUNA_DATA_HORA, dt)
            weatherValues.put(Clima.COLUNA_UMIDADE, humidity)
            weatherValues.put(Clima.COLUNA_PRESSAO, pressure)
            weatherValues.put(Clima.COLUNA_VEL_VENTO, windSpeed)
            weatherValues.put(Clima.COLUNA_GRAUS, windDirection)
            weatherValues.put(Clima.COLUNA_MAX_TEMP, high)
            weatherValues.put(Clima.COLUNA_MIN_TEMP, low)
            weatherValues.put(Clima.COLUNA_CLIMA_ID, weatherId)

            weatherContentValues[i] = weatherValues
        }

        return weatherContentValues
    }
}