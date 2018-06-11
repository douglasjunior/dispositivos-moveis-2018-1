package br.grupointegrado.tads.listadeespera

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var clientesAdapter: ClientesAdapter? = null
    private var database: SQLiteDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_clientes.layoutManager = LinearLayoutManager(this)

        val dbHelper = ListaEsperaBdHelper(this)
        database = dbHelper.writableDatabase
        BdUtil.inserirDadosFicticios(database)
        val cursor = getTodosClientes()

        clientesAdapter = ClientesAdapter(cursor)
        rv_clientes.adapter = clientesAdapter
    }

    fun getTodosClientes(): Cursor {
        return database!!.query(
                ListaEsperaContrato.Clientes.TABELA,
                null, null, null, null, null,
                ListaEsperaContrato.Clientes.COLUNA_DATA_HORA
        )
    }

    fun adicionar(view: View) {

    }

}
