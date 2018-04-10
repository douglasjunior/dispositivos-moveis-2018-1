package br.grupointegrado.tads.listaverde

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
        ListaVerdeAdapter.ItemClickListener {

    val numeros = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layoutManager = LinearLayoutManager(this,
                                    LinearLayoutManager.VERTICAL,
                                    false)
        val adapter = ListaVerdeAdapter(this, numeros, this)

        rv_lista_numerica.layoutManager = layoutManager
        rv_lista_numerica.adapter = adapter
    }

    var toast: Toast? = null

    fun exibirMensagem(mensagem: String){
        toast?.cancel()
        toast = Toast.makeText(this, mensagem, Toast.LENGTH_LONG)
        toast?.show()
    }

    override fun onItemClick(position: Int) {
        val numeroClicado = numeros.get(position)
        exibirMensagem("Clique em $numeroClicado")
    }

    override fun onLongItemClick(position: Int) {
        val numeroClicado = numeros.get(position)
        exibirMensagem("Clique longo em $numeroClicado")
    }

}
