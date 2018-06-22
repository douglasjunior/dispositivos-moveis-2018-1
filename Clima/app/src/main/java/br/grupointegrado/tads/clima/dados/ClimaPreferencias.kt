package br.grupointegrado.tads.clima.dados

import android.content.Context
import android.preference.PreferenceManager
import br.grupointegrado.tads.clima.R

object ClimaPreferencias {


    val PREF_COORD_LAT = "coord_lat"
    val PREF_COORD_LONG = "coord_long"

    /*
     * Antes de implementar os métodos para retornar dados reais, vamos apresentar
     * algumas informações padrões.
     */
    private val COORDENADAS_PADRAO = doubleArrayOf(-24.043663, -52.378009)

    /**
     * Método para armazenar os dados do localização nas preferências
     *
     */
    fun setDetalhesLocalizacao(context: Context, lat: Double, lon: Double) {
        val sp = android.preference.PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sp.edit()

        editor.putLong(PREF_COORD_LAT, java.lang.Double.doubleToRawLongBits(lat))
        editor.putLong(PREF_COORD_LONG, java.lang.Double.doubleToRawLongBits(lon))
        editor.apply()
    }

    fun restauraCoordenadasDaLocalizacao(context: Context) {
        val sp = android.preference.PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sp.edit()

        editor.remove(PREF_COORD_LAT)
        editor.remove(PREF_COORD_LONG)
        editor.apply()
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
     * Retorna a lovalização atual das preferências.
     */
    fun getLocalizacaoSalva(context: Context): String {
        val sp = android.preference.PreferenceManager.getDefaultSharedPreferences(context)

        val keyForLocation = context.getString(R.string.pref_local)
        val defaultLocation = context.getString(R.string.pref_local_padrao)

        return sp.getString(keyForLocation, defaultLocation)
    }

    /**
     * Retorna true se o usuário selecionou o sistema métrico.
     */
    fun isMetrico(context: Context): Boolean {
        val sp = android.preference.PreferenceManager.getDefaultSharedPreferences(context)

        val keyForUnits = context.getString(R.string.pref_unidade)
        val defaultUnits = context.getString(R.string.pref_unidade_padrao)
        val preferredUnits = sp.getString(keyForUnits, defaultUnits)
        val metric = context.getString(R.string.pref_unidade_metrico_valor)

        var userPrefersMetric = false
        if (metric == preferredUnits) {
            userPrefersMetric = true
        }

        return userPrefersMetric
    }

    /**
     * Retorna as coordenadas relacionadas a localização.
     * Se ar coordenadas ainda não foram salvas, será retornado (0, 0)
     *
     */
    fun getCoordenadasDaLocalizacao(context: Context): DoubleArray {
        val sp = android.preference.PreferenceManager.getDefaultSharedPreferences(context)

        val preferredCoordinates = DoubleArray(2)

        preferredCoordinates[0] = java.lang.Double
                .longBitsToDouble(sp.getLong(PREF_COORD_LAT, java.lang.Double.doubleToRawLongBits(0.0)))
        preferredCoordinates[1] = java.lang.Double
                .longBitsToDouble(sp.getLong(PREF_COORD_LONG, java.lang.Double.doubleToRawLongBits(0.0)))

        return preferredCoordinates
    }

    /**
     * Retorna true se a latitude e longitude estão disponíveis.
     *
     */
    fun isCoordenadasDisponiveis(context: Context): Boolean {
        val sp = android.preference.PreferenceManager.getDefaultSharedPreferences(context)

        val spContainLatitude = sp.contains(PREF_COORD_LAT)
        val spContainLongitude = sp.contains(PREF_COORD_LONG)

        var spContainBothLatitudeAndLongitude = false
        if (spContainLatitude && spContainLongitude) {
            spContainBothLatitudeAndLongitude = true
        }

        return spContainBothLatitudeAndLongitude
    }

    fun getTempoUltimaNotificacao(context: Context): Long {
        /* Key for accessing the time at which Sunshine last displayed a notification */
        val lastNotificationKey = context.getString(R.string.pref_ultima_notificacao)

        /* As usual, we use the default SharedPreferences to access the user's preferences */
        val sp = android.preference.PreferenceManager.getDefaultSharedPreferences(context)

        /*
         * Here, we retrieve the time in milliseconds when the last notification was shown. If
         * SharedPreferences doesn't have a value for lastNotificationKey, we return 0. The reason
         * we return 0 is because we compare the value returned from this method to the current
         * system time. If the difference between the last notification time and the current time
         * is greater than one day, we will show a notification again. When we compare the two
         * values, we subtract the last notification time from the current system time. If the
         * time of the last notification was 0, the difference will always be greater than the
         * number of milliseconds in a day and we will show another notification.
         */

        return sp.getLong(lastNotificationKey, 0)
    }

    fun getTempoPassadoUltimaNotificacao(context: Context): Long {
        val lastNotificationTimeMillis = getTempoUltimaNotificacao(context)
        return System.currentTimeMillis() - lastNotificationTimeMillis
    }

    fun salvaTempoUltimaNotificacao(context: Context, timeOfNotification: Long) {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sp.edit()
        val lastNotificationKey = context.getString(R.string.pref_ultima_notificacao)
        editor.putLong(lastNotificationKey, timeOfNotification)
        editor.apply()
    }

}