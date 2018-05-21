package br.grupointegrado.tads.buscadorgithub

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.AsyncTaskLoader
import android.support.v4.content.ContextCompat
import android.support.v4.content.Loader
import android.support.v7.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<String>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    companion object {
        val GITHUB_BUSCA_LOADER = 1000
        val URL_BUSCA_EXTRA = "URL_BUSCA_EXTRA"
        val RESULTADO_EXTRA = "RESULTADO_EXTRA"
    }

    var cacheResultado: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(URL_BUSCA_EXTRA)) {
                tv_url.text = savedInstanceState.getString(URL_BUSCA_EXTRA)
            }
//            if (savedInstanceState.containsKey(RESULTADO_EXTRA)) {
//                tv_github_resultado.text = savedInstanceState.getString(RESULTADO_EXTRA)
//            }
        }

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        val exibirUrl = sharedPreferences.getBoolean(
                getString(R.string.pref_exibir_url),
                resources.getBoolean(R.bool.pref_exibir_url_padrao)
        )

        tv_url.visibility = if (exibirUrl) View.VISIBLE else View.INVISIBLE

        val corFundo = sharedPreferences.getString(
                getString(R.string.pref_cor_fundo),
                getString(R.string.pref_cor_fundo_padrao)
        )
        window.decorView.setBackgroundColor(selecionaCorDeFundo(corFundo))

        sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        et_busca.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                cacheResultado = null
            }

            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        supportLoaderManager.initLoader(GITHUB_BUSCA_LOADER, null, this)
    }

    fun selecionaCorDeFundo(corFundo: String): Int {
        return when (corFundo) {
            getString(R.string.pref_cor_fundo_branco_valor) -> ContextCompat.getColor(this, R.color.fundoBranco)
            getString(R.string.pref_cor_fundo_verde_valor) -> ContextCompat.getColor(this, R.color.fundoVerde)
            getString(R.string.pref_cor_fundo_azul_valor) -> ContextCompat.getColor(this, R.color.fundoAzul)
            else -> ContextCompat.getColor(this, R.color.fundoBranco)
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == getString(R.string.pref_exibir_url)) {
            val exibirUrl = sharedPreferences.getBoolean(
                    key,
                    resources.getBoolean(R.bool.pref_exibir_url_padrao)
            )
            tv_url.visibility = if (exibirUrl) View.VISIBLE else View.INVISIBLE
        } else if (key == getString(R.string.pref_cor_fundo)) {
            val corFundo = sharedPreferences.getString(
                    key,
                    getString(R.string.pref_cor_fundo_padrao)
            )
            window.decorView.setBackgroundColor(selecionaCorDeFundo(corFundo))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        val url = tv_url.text.toString()
//        val resultado = tv_github_resultado.text.toString()

        outState?.putString(URL_BUSCA_EXTRA, url)
//        outState?.putString(RESULTADO_EXTRA, resultado)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_buscar) {
            buscarNoGithub()
        }
        if (item?.itemId == R.id.action_configuracoes) {
            val intent = Intent(this, ConfiguracaoActivity::class.java)
            startActivity(intent)
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
                if (cacheResultado != null) {
                    deliverResult(cacheResultado)
                } else {
                    forceLoad()
                }
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

            override fun deliverResult(resultado: String?) {
                super.deliverResult(resultado)
                cacheResultado = resultado
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
