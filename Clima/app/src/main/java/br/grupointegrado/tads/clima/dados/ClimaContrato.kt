package br.grupointegrado.tads.clima.dados

import android.net.Uri
import android.provider.BaseColumns

object ClimaContrato {

    internal object Clima : BaseColumns {
        val TABELA = "clima"
        val COLUNA_DATA_HORA = "data_hora"
        val COLUNA_CLIMA_ID = "clima_id"
        val COLUNA_MIN_TEMP = "min_temp"
        val COLUNA_MAX_TEMP = "max_temp"
        val COLUNA_UMIDADE = "umidade"
        val COLUNA_PRESSAO = "pressao"
        val COLUNA_VEL_VENTO = "vel_vento"
        val COLUNA_GRAUS = "graus"
    }

}