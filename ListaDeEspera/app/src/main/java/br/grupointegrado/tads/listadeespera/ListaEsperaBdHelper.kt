package br.grupointegrado.tads.listadeespera

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class ListaEsperaBdHelper : SQLiteOpenHelper {

    companion object {
        val BD_NOME = "listaespera.db"
        val BD_VERSAO = 1
    }

    constructor(context: Context) :
            super(context, BD_NOME, null, BD_VERSAO) {
    }

    override fun onCreate(bd: SQLiteDatabase) {
        val CREATE_TABLE_CLIENTES = """
            CREATE TABLE ${ListaEsperaContrato.Clientes.TABELA} (
                ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${ListaEsperaContrato.Clientes.COLUNA_NOME} TEXT NOT NULL,
                ${ListaEsperaContrato.Clientes.COLUNA_TAMANHO_GRUPO} INTEGER NOT NULL,
                ${ListaEsperaContrato.Clientes.COLUNA_DATA_HORA} TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            );
        """
        bd.execSQL(CREATE_TABLE_CLIENTES)
    }

    override fun onUpgrade(bd: SQLiteDatabase,
                           versaoAnterior: Int, novaVersao: Int) {
        bd.execSQL("DROP TABLE IF EXISTS ${ListaEsperaContrato.Clientes.TABELA};")
        onCreate(bd)
    }

}




