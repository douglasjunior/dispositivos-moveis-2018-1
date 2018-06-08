package br.grupointegrado.tads.clima.dados

import android.content.Context
import android.support.v7.preference.PreferenceManager
import br.grupointegrado.tads.clima.R

class ClimaPreferencias {

    companion object {

        /*
         * Antes de implementar os métodos para retornar dados reais, vamos apresentar
         * algumas informações padrões.
         */
        private val COORDENADAS_PADRAO = doubleArrayOf(-24.043663, -52.378009)

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
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return sharedPreferences.getString(context.getString(R.string.pref_local), getLocalPadrao(context))
        }

        /**
         * Retorna true se o usuário selecionou o sistema métrico.
         */
        fun isMetrico(context: Context): Boolean {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val unidadeSelecioada = sharedPreferences.getString(context.getString(R.string.pref_unidade), context.getString(R.string.pref_unidade_padrao))
            return unidadeSelecioada == context.getString(R.string.pref_unidade_metrico_valor)
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

        private fun getLocalPadrao(context: Context): String {
            return context.getString(R.string.pref_local_padrao)
        }

        fun getCoordenadasPadrao(): DoubleArray {
            /** Será implementado no futuro  */
            return COORDENADAS_PADRAO
        }

    }
}