package br.grupointegrado.tads.listadeespera

import android.content.ContentValues
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import java.util.ArrayList

class BdUtil {

    companion object {
        fun inserirDadosFicticios(db: SQLiteDatabase?) {
            if (db == null) {
                return
            }
            //create a list of fake guests
            val list = ArrayList<ContentValues>()

            var cv = ContentValues()
            cv.put(ListaEsperaContrato.Clientes.COLUNA_NOME, "John")
            cv.put(ListaEsperaContrato.Clientes.COLUNA_TAMANHO_GRUPO, 12)
            list.add(cv)

            cv = ContentValues()
            cv.put(ListaEsperaContrato.Clientes.COLUNA_NOME, "Tim")
            cv.put(ListaEsperaContrato.Clientes.COLUNA_TAMANHO_GRUPO, 2)
            list.add(cv)

            cv = ContentValues()
            cv.put(ListaEsperaContrato.Clientes.COLUNA_NOME, "Jessica")
            cv.put(ListaEsperaContrato.Clientes.COLUNA_TAMANHO_GRUPO, 99)
            list.add(cv)

            cv = ContentValues()
            cv.put(ListaEsperaContrato.Clientes.COLUNA_NOME, "Larry")
            cv.put(ListaEsperaContrato.Clientes.COLUNA_TAMANHO_GRUPO, 1)
            list.add(cv)

            cv = ContentValues()
            cv.put(ListaEsperaContrato.Clientes.COLUNA_NOME, "Kim")
            cv.put(ListaEsperaContrato.Clientes.COLUNA_TAMANHO_GRUPO, 45)
            list.add(cv)

            //insert all guests in one transaction
            try {
                db.beginTransaction()
                //clear the table first
                db.delete(ListaEsperaContrato.Clientes.TABELA, null, null)
                //go through the list and add one by one
                for (c in list) {
                    db.insert(ListaEsperaContrato.Clientes.TABELA, null, c)
                }
                db.setTransactionSuccessful()
            } catch (e: SQLException) {
                //too bad :(
            } finally {
                db.endTransaction()
            }
        }
    }

}