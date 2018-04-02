package br.grupointegrado.tads.clima.util

import android.content.Context
import android.util.Log
import br.grupointegrado.tads.clima.R
import br.grupointegrado.tads.clima.dados.ClimaPreferencias

class ClimaUtils {

    companion object {

        private val LOG_TAG = ClimaUtils::class.java.simpleName

        /**
         * Converte a temperatura de Celsius para Fahrenheit.
         *
         */
        private fun celsiusParaFahrenheit(temperaturaEmCelsius: Double): Double {
            return temperaturaEmCelsius * 1.8 + 32
        }

        /**
         * Os dados são armazenados em Celsius pelo nosso app.
         * Dependendo das preferencias do usuário ele precisa ser exibido em Fahrenheit.
         *
         */
        fun formataTemperatura(context: Context, temperatura: Double): String {
            var temperatura = temperatura
            var temperatureFormatResourceId = R.string.formata_temperatura_celsius

            if (!ClimaPreferencias.isMetrico(context)) {
                temperatura = celsiusParaFahrenheit(temperatura)
                temperatureFormatResourceId = R.string.formata_temperatura_fahrenheit
            }

            /* Por enquanto, assume que o usuário não se importa com casas decimais */
            return String.format(context.getString(temperatureFormatResourceId), temperatura)
        }

        /**
         * Formata a temperatura para ser exibida no formato: "MAX °C / MIN °C"
         *
         */
        fun formataMaxMin(context: Context, max: Double, min: Double): String {
            val maxArredondado = Math.round(max)
            val minArredondado = Math.round(min)

            val maxFormatado = formataTemperatura(context, maxArredondado.toDouble())
            val minFormatado = formataTemperatura(context, minArredondado.toDouble())

            return "$maxFormatado / $minFormatado"
        }

        /**
         * Formata a velocidade e direção do vento.
         *
         */
        fun getVentoFormatado(context: Context, velocidade: Float, graus: Float): String {
            var velocidade = velocidade

            var formatoVento = R.string.formata_vento_kmh

            if (!ClimaPreferencias.isMetrico(context)) {
                formatoVento = R.string.formata_vento_mph
                velocidade = 0.621371192237334f * velocidade // converte km/h para milhas/hora
            }

            /*
            North (N): 0° = 360°
            East (E): 90°
            South (S): 180°
            West (W): 270°

            Northeast (NE), 45°
            Southeast (SE), 135°
            Southwest (SW), 225°
            Northwest (NW), 315°
            */
            var direcao = "Desconhecido"
            if (graus >= 337.5 || graus < 22.5) {
                direcao = "N"
            } else if (graus >= 22.5 && graus < 67.5) {
                direcao = "NE"
            } else if (graus >= 67.5 && graus < 112.5) {
                direcao = "E"
            } else if (graus >= 112.5 && graus < 157.5) {
                direcao = "SE"
            } else if (graus >= 157.5 && graus < 202.5) {
                direcao = "S"
            } else if (graus >= 202.5 && graus < 247.5) {
                direcao = "SW"
            } else if (graus >= 247.5 && graus < 292.5) {
                direcao = "W"
            } else if (graus >= 292.5 && graus < 337.5) {
                direcao = "NW"
            }
            return String.format(context.getString(formatoVento), velocidade, direcao)
        }

        /**
         * Retorna uma string de acordo com as condições do clima retornadas pelo OpenWeatherMap.
         *
         */
        fun getStringCondicoesDoClima(context: Context, climaId: Int): String {
            val stringId: Int
            if (climaId >= 200 && climaId <= 232) {
                stringId = R.string.condicao_2xx
            } else if (climaId >= 300 && climaId <= 321) {
                stringId = R.string.condicao_3xx
            } else
                when (climaId) {
                    500 -> stringId = R.string.condicao_500
                    501 -> stringId = R.string.condicao_501
                    502 -> stringId = R.string.condicao_502
                    503 -> stringId = R.string.condicao_503
                    504 -> stringId = R.string.condicao_504
                    511 -> stringId = R.string.condicao_511
                    520 -> stringId = R.string.condicao_520
                    531 -> stringId = R.string.condicao_531
                    600 -> stringId = R.string.condicao_600
                    601 -> stringId = R.string.condicao_601
                    602 -> stringId = R.string.condicao_602
                    611 -> stringId = R.string.condicao_611
                    612 -> stringId = R.string.condicao_612
                    615 -> stringId = R.string.condicao_615
                    616 -> stringId = R.string.condicao_616
                    620 -> stringId = R.string.condicao_620
                    621 -> stringId = R.string.condicao_621
                    622 -> stringId = R.string.condicao_622
                    701 -> stringId = R.string.condicao_701
                    711 -> stringId = R.string.condicao_711
                    721 -> stringId = R.string.condicao_721
                    731 -> stringId = R.string.condicao_731
                    741 -> stringId = R.string.condicao_741
                    751 -> stringId = R.string.condicao_751
                    761 -> stringId = R.string.condicao_761
                    762 -> stringId = R.string.condicao_762
                    771 -> stringId = R.string.condicao_771
                    781 -> stringId = R.string.condicao_781
                    800 -> stringId = R.string.condicao_800
                    801 -> stringId = R.string.condicao_801
                    802 -> stringId = R.string.condicao_802
                    803 -> stringId = R.string.condicao_803
                    804 -> stringId = R.string.condicao_804
                    900 -> stringId = R.string.condicao_900
                    901 -> stringId = R.string.condicao_901
                    902 -> stringId = R.string.condicao_902
                    903 -> stringId = R.string.condicao_903
                    904 -> stringId = R.string.condicao_904
                    905 -> stringId = R.string.condicao_905
                    906 -> stringId = R.string.condicao_906
                    951 -> stringId = R.string.condicao_951
                    952 -> stringId = R.string.condicao_952
                    953 -> stringId = R.string.condicao_953
                    954 -> stringId = R.string.condicao_954
                    955 -> stringId = R.string.condicao_955
                    956 -> stringId = R.string.condicao_956
                    957 -> stringId = R.string.condicao_957
                    958 -> stringId = R.string.condicao_958
                    959 -> stringId = R.string.condicao_959
                    960 -> stringId = R.string.condicao_960
                    961 -> stringId = R.string.condicao_961
                    962 -> stringId = R.string.condicao_962
                    else -> return context.getString(R.string.condicao_desconhecida, climaId)
                }
            return context.getString(stringId)
        }

        /**
         * Retorna um ícone de acordo com as condições do clima retornadas pelo OpenWeatherMap.
         *
         */
        fun getIconeCondicoesDoClima(climaId: Int): Int {
            /*
             * Baseado em
             * http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
             */
            if (climaId >= 200 && climaId <= 232) {
                return R.drawable.ic_storm
            } else if (climaId >= 300 && climaId <= 321) {
                return R.drawable.ic_light_rain
            } else if (climaId >= 500 && climaId <= 504) {
                return R.drawable.ic_rain
            } else if (climaId == 511) {
                return R.drawable.ic_snow
            } else if (climaId >= 520 && climaId <= 531) {
                return R.drawable.ic_rain
            } else if (climaId >= 600 && climaId <= 622) {
                return R.drawable.ic_snow
            } else if (climaId >= 701 && climaId <= 761) {
                return R.drawable.ic_fog
            } else if (climaId == 761 || climaId == 781) {
                return R.drawable.ic_storm
            } else if (climaId == 800) {
                return R.drawable.ic_clear
            } else if (climaId == 801) {
                return R.drawable.ic_light_clouds
            } else if (climaId >= 802 && climaId <= 804) {
                return R.drawable.ic_cloudy
            }
            return -1
        }

        /**
         * Retorna uma imagem de acordo com as condições do clima retornadas pelo OpenWeatherMap.
         *
         */
        fun getImagemCondicoesDoClima(climaId: Int): Int {
            /*
             * Baseado em
             * http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
             */
            if (climaId >= 200 && climaId <= 232) {
                return R.drawable.art_storm
            } else if (climaId >= 300 && climaId <= 321) {
                return R.drawable.art_light_rain
            } else if (climaId >= 500 && climaId <= 504) {
                return R.drawable.art_rain
            } else if (climaId == 511) {
                return R.drawable.art_snow
            } else if (climaId >= 520 && climaId <= 531) {
                return R.drawable.art_rain
            } else if (climaId >= 600 && climaId <= 622) {
                return R.drawable.art_snow
            } else if (climaId >= 701 && climaId <= 761) {
                return R.drawable.art_fog
            } else if (climaId == 761 || climaId == 771 || climaId == 781) {
                return R.drawable.art_storm
            } else if (climaId == 800) {
                return R.drawable.art_clear
            } else if (climaId == 801) {
                return R.drawable.art_light_clouds
            } else if (climaId >= 802 && climaId <= 804) {
                return R.drawable.art_clouds
            } else if (climaId >= 900 && climaId <= 906) {
                return R.drawable.art_storm
            } else if (climaId >= 958 && climaId <= 962) {
                return R.drawable.art_storm
            } else if (climaId >= 951 && climaId <= 957) {
                return R.drawable.art_clear
            }
            Log.e(LOG_TAG, "Clima desconhecido: $climaId")
            return R.drawable.art_storm
        }

    }

}