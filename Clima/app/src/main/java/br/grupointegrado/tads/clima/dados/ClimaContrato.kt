package br.grupointegrado.tads.clima.dados

import android.net.Uri
import android.provider.BaseColumns
import br.grupointegrado.tads.clima.util.DataUtils

object ClimaContrato {

    val AUTORIDADE = "br.grupointegrado.tads.clima.dados.ClimaContentProvider"
    val URI_BASE = Uri.parse("content://$AUTORIDADE")
    val URI_CLIMA = "clima"

    internal object Clima : BaseColumns {

        val CONTENT_URI = URI_BASE.buildUpon()
                .appendPath(URI_CLIMA)
                .build()

        val TABELA = "clima"
        val COLUNA_DATA_HORA = "data_hora"
        val COLUNA_CLIMA_ID = "clima_id"
        val COLUNA_MIN_TEMP = "min_temp"
        val COLUNA_MAX_TEMP = "max_temp"
        val COLUNA_UMIDADE = "umidade"
        val COLUNA_PRESSAO = "pressao"
        val COLUNA_VEL_VENTO = "vel_vento"
        val COLUNA_GRAUS = "graus"

        fun getSqlSelectHojeEmDiante(): String {
            val utcNow = System.currentTimeMillis()
            return "${Clima.COLUNA_DATA_HORA} >= $utcNow"
        }

        fun construirPrevisaoUri(dataHora: Long): Uri {
            return CONTENT_URI.buildUpon()
                    .appendPath(dataHora.toString())
                    .build()
        }
    }

}