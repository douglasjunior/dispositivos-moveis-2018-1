package br.grupointegrado.tads.clima.dados

import android.content.Context

class ClimaPreferencias {

    companion object {

        /*
         * String de lovalização legível aos humanos.
         */
        val PREF_NOME_CIDADE = "city_name"

        /*
         * Para poder abrir o intent de mapa, armazenamos a posição geográfica.
         */
        val PREF_COORD_LAT = "coord_lat"
        val PREF_COORD_LONG = "coord_long"

        /*
         * Antes de implementar os métodos para retornar dados reais, vamos apresentar
         * algumas informações padrões.
         */
        private val LOCAL_PADRAO = "Campo Mourão"
        private val COORDENADAS_PADRAO = doubleArrayOf(-24.043663, -52.378009)

        private val LOCAL_MAPA_PADRAO = "Av. Cap. Índio Bandeira, 1224-1316 - Centro, Campo Mourão - PR"

        /**
         * Método para armazenar os dados do localização nas preferências
         *
         */
        fun setDetalhesLocalizacao(c: Context, cidade: String, lat: Double, lon: Double) {
            /** Será implementado no futuro  */
        }

        /**
         * Método para armazenar os dados do localização nas preferências. Quando isso ocorrer
         * a base de dados precisa ser esvaziada.
         *
         */
        fun setLocalizacao(c: Context, localizacao: String, lat: Double, lon: Double) {
            /** Será implementado no futuro  */
        }

        /**
         * Restaura as coordenada da localização.
         *
         */
        fun restauraCoordenadasDaLocalizacao(c: Context) {
            /** Será implementado no futuro  */
        }

        /**
         * Retorna a lovalização atual das preferências.
         */
        fun getLocalizacaoSalva(context: Context): String {
            /** Será implementado no futuro  */
            return getLocalPadrao()
        }

        /**
         * Retorna true se o usuário selecionou o sistema métrico.
         */
        fun isMetrico(context: Context): Boolean {
            /** Será implementado no futuro  */
            return true
        }

        /**
         * Retorna as coordenadas relacionadas a localização.
         * Se ar coordenadas ainda não foram salvas, será retornado (0, 0)
         *
         */
        fun getCoordenadasDaLocalizacao(context: Context): DoubleArray {
            /** Será implementado no futuro  */
            return getCoordenadasPadrao()
        }

        /**
         * Retorna true se a latitude e longitude estão disponíveis.
         *
         */
        fun isCoordenadasDisponiveis(context: Context): Boolean {
            /** Será implementado no futuro  */
            return false
        }

        private fun getLocalPadrao(): String {
            /** Será implementado no futuro  */
            return LOCAL_PADRAO
        }

        fun getCoordenadasPadrao(): DoubleArray {
            /** Será implementado no futuro  */
            return COORDENADAS_PADRAO
        }

    }
}