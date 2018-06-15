package br.grupointegrado.tads.listadeespera

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
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
//        BdUtil.inserirDadosFicticios(database)
        val cursor = getTodosClientes()

        clientesAdapter = ClientesAdapter(cursor)
        rv_clientes.adapter = clientesAdapter

        val itemTouch = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val id = viewHolder.itemView.tag as Long
                removerCliente(id)
                clientesAdapter!!.atualizarCursor(getTodosClientes())
            }

        })

        itemTouch.attachToRecyclerView(rv_clientes)
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
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        adicionarNovoCliente(nome, tamanhoGrupo)
        clientesAdapter?.atualizarCursor(getTodosClientes())

        ed_tamanho_grupo.clearFocus()
        ed_nome_cliente.text.clear()
        ed_tamanho_grupo.text.clear()
    }

    private fun adicionarNovoCliente(nome: String, tamanhoGrupo: Int): Long {
        val cliente = ContentValues()
        cliente.put(ListaEsperaContrato.Clientes.COLUNA_NOME, nome)
        cliente.put(ListaEsperaContrato.Clientes.COLUNA_TAMANHO_GRUPO, tamanhoGrupo)
        return database!!.insert(ListaEsperaContrato.Clientes.TABELA, null, cliente)
    }

    private fun removerCliente(clienteId: Long): Boolean {
        val nomeTabela = ListaEsperaContrato.Clientes.TABELA
        val where = "${BaseColumns._ID} = ?"
        val argumentos = arrayOf(clienteId.toString())

        val removidos = database!!.delete(nomeTabela, where, argumentos)

        return removidos > 0
    }

}
