package br.grupointegrado.tads.listadeespera

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.cliente_list_item.*

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
        if (ed_nome_cliente.text.isEmpty()) {
            return;
        }

        val nome = ed_nome_cliente.text.toString()
        var tamanhoGrupo = 1

        try {
            tamanhoGrupo = ed_tamanho_grupo.text.toString().toInt()
        } catch(ex: Exception) {
            ex.printStackTrace()
        }

        adicionarNovoCliente(nome, tamanhoGrupo)
        clientesAdapter?.atualizarCursor(getTodosClientes())

        ed_tamanho_grupo.clearFocus()
        ed_nome_cliente.text.clear()
        ed_tamanho_grupo.text.clear()
    }

    private fun adicionarNovoCliente(nome: String, tamanhoGrupo: Int) : Long {
        println(nome + " " + tamanhoGrupo)
        val cliente = ContentValues()
        cliente.put(ListaEsperaContrato.Clientes.COLUNA_NOME, nome)
        cliente.put(ListaEsperaContrato.Clientes.COLUNA_TAMANHO_GRUPO, tamanhoGrupo)
        return database!!.insert(ListaEsperaContrato.Clientes.TABELA, null, cliente)
    }

}
