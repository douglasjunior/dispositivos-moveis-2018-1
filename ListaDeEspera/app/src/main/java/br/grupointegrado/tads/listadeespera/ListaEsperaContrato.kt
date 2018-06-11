package br.grupointegrado.tads.listadeespera

import android.provider.BaseColumns

object ListaEsperaContrato {

    internal object Clientes : BaseColumns {
        const val TABELA = "clientes"
        const val COLUNA_NOME = "nome"
        const val COLUNA_TAMANHO_GRUPO = "tamanhoGrupo"
        const val COLUNA_DATA_HORA = "dataHora"
    }

}