package br.grupointegrado.tads.listadeespera

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var clientesAdapter: ClientesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_clientes.layoutManager = LinearLayoutManager(this)

        clientesAdapter = ClientesAdapter()

        rv_clientes.adapter = clientesAdapter
    }

    fun adicionar(view: View) {

    }

}
