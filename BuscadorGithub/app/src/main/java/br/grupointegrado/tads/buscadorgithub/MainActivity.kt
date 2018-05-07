package br.grupointegrado.tads.buscadorgithub

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.AsyncTaskLoader
import android.support.v4.content.Loader
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<String> {

    companion object {
        val GITHUB_BUSCA_LOADER = 1000
        val URL_BUSCA_EXTRA = "URL_BUSCA_EXTRA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportLoaderManager.initLoader(GITHUB_BUSCA_LOADER, null, this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_buscar) {
            buscarNoGithub()
        }
        return super.onOptionsItemSelected(item)
    }

    fun buscarNoGithub() {
        val busca = et_busca.text.toString()
        val urlBusca = NetworkUtils.construirUrl(busca)
        tv_url.text = urlBusca.toString()

        if (urlBusca != null) {
            val parametros = Bundle()
            parametros.putString(URL_BUSCA_EXTRA, urlBusca.toString())
            supportLoaderManager.restartLoader(GITHUB_BUSCA_LOADER, parametros, this)
        }
    }

    fun exibirResultado() {
        tv_github_resultado.visibility = View.VISIBLE
        tv_mensagem_erro.visibility = View.INVISIBLE
        pb_aguarde.visibility = View.INVISIBLE
    }

    fun exibirMensagemErro() {
        tv_github_resultado.visibility = View.INVISIBLE
        tv_mensagem_erro.visibility = View.VISIBLE
        pb_aguarde.visibility = View.INVISIBLE
    }

    fun exibirProgressBar() {
        tv_github_resultado.visibility = View.INVISIBLE
        tv_mensagem_erro.visibility = View.INVISIBLE
        pb_aguarde.visibility = View.VISIBLE
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<String> {
        val loader = object : AsyncTaskLoader<String>(this) {
            override fun onStartLoading() {
                super.onStartLoading()
                if (args == null) {
                    return
                }
                exibirProgressBar()
                forceLoad()
            }

            override fun loadInBackground(): String? {
                try {
                    val urlExtra = args!!.getString(URL_BUSCA_EXTRA)
                    val url = URL(urlExtra)
                    val resultado = NetworkUtils.obterRespostaDaUrlHtpp(url)
                    return resultado
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
                return null
            }
        }
        return loader
    }

    override fun onLoadFinished(loader: Loader<String>?, resultado: String?) {
        if (resultado != null) {
            /**
             * Lendo o JSON para exibir apenas os nomes dos repositórios
             */

            tv_github_resultado.text = ""

            val json = JSONObject(resultado)
            val items = json.getJSONArray("items")

            // percorra de Zero até o tamanho do array
            for (i in 0 until items.length()) {
                val repository = items.getJSONObject(i)
                val name = repository.getString("name")

                tv_github_resultado.append("$name \n\n\n")
            }

            exibirResultado()
        } else {
            exibirMensagemErro()
        }
    }

    override fun onLoaderReset(loader: Loader<String>?) {
        // nada a implementar
    }
}
