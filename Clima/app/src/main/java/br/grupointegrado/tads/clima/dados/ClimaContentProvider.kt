package br.grupointegrado.tads.clima.dados

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import br.grupointegrado.tads.clima.util.DataUtils

class ClimaContentProvider : ContentProvider() {

    companion object {
        val CODE_CLIMA = 100
        val CODE_CLIMA_POR_DATA = 101

        val uriMatcher: UriMatcher

        init {
            uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
            val autoridade = ClimaContrato.AUTORIDADE
            uriMatcher.addURI(autoridade, ClimaContrato.PATH_CLIMA, CODE_CLIMA)
            uriMatcher.addURI(autoridade, "${ClimaContrato.PATH_CLIMA}/#", CODE_CLIMA_POR_DATA)
        }
    }

    private var bdHelper: ClimaBdHelper? = null

    override fun onCreate(): Boolean {
        bdHelper = ClimaBdHelper(context)
        return true
    }

    override fun shutdown() {
        bdHelper!!.close()
        super.shutdown()
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor {
        val cursor: Cursor
        when (uriMatcher.match(uri)) {
            CODE_CLIMA_POR_DATA -> {
                val normalizedUtcDateString = uri.getLastPathSegment()
                val selectionArguments = arrayOf(normalizedUtcDateString)

                cursor = bdHelper!!.getReadableDatabase().query(ClimaContrato.Clima.TABELA,
                        projection,
                        "${ClimaContrato.Clima.COLUNA_DATA_HORA} = ? ",
                        selectionArguments, null, null, sortOrder)
            }
            CODE_CLIMA -> {
                cursor = bdHelper!!.getReadableDatabase().query(ClimaContrato.Clima.TABELA,
                        projection, selection,
                        selectionArgs, null, null, sortOrder)
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
                            throw IllegalArgumentException("A data precisa estar normalizada para ser inserida.")
                        }
                        val _id = bd.insert(ClimaContrato.Clima.TABELA, null, value)
                        if (!_id.equals(-1)) {
                            registrosInseridos += 1
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
            else -> return super.bulkInsert(uri, values)
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        var registrosDeletados: Int
        val selecao = if (selection != null) selection else ""
        when (uriMatcher.match(uri)) {
            CODE_CLIMA -> {
                registrosDeletados = bdHelper!!.getWritableDatabase().delete(
                        ClimaContrato.Clima.TABELA,
                        selecao, selectionArgs)
            }
            else -> throw UnsupportedOperationException("Unknown uri: $uri")
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