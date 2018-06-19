package br.grupointegrado.tads.clima.dados

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import br.grupointegrado.tads.clima.util.DataUtils

class ClimaContentProvider : ContentProvider() {


    companion object {
        var CODE_CLIMA = 100
        var CODE_CLIMA_POR_DATA = 101

        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            // content://br.grupointegrado.tads.clima.dados.ClimaContentProvider/clima
            uriMatcher.addURI(ClimaContrato.AUTORIDADE,
                    ClimaContrato.URI_CLIMA,
                    CODE_CLIMA)

            // content://br.grupointegrado.tads.clima.dados.ClimaContentProvider/clima/#
            uriMatcher.addURI(ClimaContrato.AUTORIDADE,
                    "${ClimaContrato.URI_CLIMA}/#",
                    CODE_CLIMA_POR_DATA)
        }
    }

    var bdHelper: ClimaBdHelper? = null

    override fun onCreate(): Boolean {
        bdHelper = ClimaBdHelper(this.context)
        return true
    }

    override fun shutdown() {
        bdHelper?.close()
    }

    override fun query(uri: Uri, projection: Array<out String>?,
                       selection: String?, selectionArgs: Array<out String>?,
                       sortOrder: String?): Cursor {
        val bd = bdHelper!!.readableDatabase
        val cursor: Cursor
        when (uriMatcher.match(uri)) {
            CODE_CLIMA -> {
                cursor = bd.query(ClimaContrato.Clima.TABELA,
                        null, null, null, null, null,
                        sortOrder)
            }
            CODE_CLIMA_POR_DATA -> {
                val dataClima = uri.lastPathSegment
                val where = "${ClimaContrato.Clima.COLUNA_DATA_HORA} = ?"
                var whereArgs = arrayOf(dataClima.toString())
                cursor = bd.query(ClimaContrato.Clima.TABELA,
                        null, where, whereArgs, null, null,
                        sortOrder)
            }
            else -> throw UnsupportedOperationException("Uri desconhecida: $uri")
        }
        cursor.setNotificationUri(context.contentResolver, uri)
        return cursor
    }

    override fun bulkInsert(uri: Uri, values: Array<out ContentValues>): Int {
        val bd = bdHelper!!.writableDatabase
        when (uriMatcher.match(uri)) {
            CODE_CLIMA -> {
                var registrosInseridos = 0
                bd.beginTransaction()
                try {
                    for (value in values) {
                        val dataClima = value.getAsLong(ClimaContrato.Clima.COLUNA_DATA_HORA)
                        if (!DataUtils.dataEstaNormalizada(dataClima)) {
                            throw IllegalArgumentException("A data deve estar normalizada.")
                        }
                        val _id = bd.insert(ClimaContrato.Clima.TABELA, null, value)
                        if (_id != -1L) {
                            registrosInseridos++
                        }
                    }
                    bd.setTransactionSuccessful()
                } finally {
                    bd.endTransaction()
                }
                if (registrosInseridos > 0) {
                    context.contentResolver.notifyChange(uri, null)
                }
                return registrosInseridos
            }
        }
        return super.bulkInsert(uri, values)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        var registrosDeletados: Int
        val selecao = if (selection != null) selection else "1"
        when (uriMatcher.match(uri)) {
            CODE_CLIMA -> {
                registrosDeletados = bdHelper!!.getWritableDatabase().delete(
                        ClimaContrato.Clima.TABELA,
                        selecao, selectionArgs)
            }
            else -> throw UnsupportedOperationException("Uri desconhecida: $uri")
        }
        if (registrosDeletados != 0) {
            context.contentResolver.notifyChange(uri, null)
        }
        return registrosDeletados
    }

    override fun insert(uri: Uri?, values: ContentValues?): Uri {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getType(uri: Uri?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}