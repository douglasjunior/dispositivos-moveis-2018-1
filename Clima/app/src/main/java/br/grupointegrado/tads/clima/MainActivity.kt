package br.grupointegrado.tads.clima

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import br.grupointegrado.tads.buscadorgithub.NetworkUtils
import br.grupointegrado.tads.clima.dados.ClimaPreferencias
import br.grupointegrado.tads.clima.util.JsonUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), PrevisaoAdapter.PrevisaoItemClickListener {

    var previsaoAdapter: PrevisaoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        previsaoAdapter = PrevisaoAdapter(null, this)
        val layoutManager = LinearLayoutManager(this)

        rv_clima.layoutManager = layoutManager
        rv_clima.adapter = previsaoAdapter

        carregarDadosClima()
    }

    override fun onItemClick(index: Int) {
        val previsao = previsaoAdapter?.getDadosClima()?.get(index)
        Toast.makeText(this, "Previsão: $previsao", Toast.LENGTH_SHORT).show()
    }

    fun carregarDadosClima() {
        val localizacao = ClimaPreferencias.getLocalizacaoSalva(this)
        BuscarClimaTask().execute(localizacao)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.clima, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId === R.id.acao_atualizar) {
            carregarDadosClima()
        }
        return super.onOptionsItemSelected(item)
    }

    fun exibirResultado() {
        rv_clima.visibility = View.VISIBLE
        tv_mensagem_erro.visibility = View.INVISIBLE
        pb_aguarde.visibility = View.INVISIBLE
    }

    fun exibirMensagemErro() {
        rv_clima.visibility = View.INVISIBLE
        tv_mensagem_erro.visibility = View.VISIBLE
        pb_aguarde.visibility = View.INVISIBLE
    }

    fun exibirProgressBar() {
        rv_clima.visibility = View.INVISIBLE
        tv_mensagem_erro.visibility = View.INVISIBLE
        pb_aguarde.visibility = View.VISIBLE
    }

    inner class BuscarClimaTask : AsyncTask<String, Void, String>() {

        override fun onPreExecute() {
            exibirProgressBar()
        }

        override fun doInBackground(vararg params: String): String? {
            try {
                val localizacao = params[0]
                val url = NetworkUtils.construirUrl(localizacao)

                if (url != null) {
                    val resultado = NetworkUtils.obterRespostaDaUrlHttp(url)
                    return resultado
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(resultado: String?) {
            if (resultado != null) {

                val dadosClima = JsonUtils
                        .getSimplesStringsDeClimaDoJson(this@MainActivity,
                                resultado)
                previsaoAdapter?.setDadosClima(dadosClima)

                exibirResultado()
            } else {
                exibirMensagemErro()
            }
        }

    }
}
