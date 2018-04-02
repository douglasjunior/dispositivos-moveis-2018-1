package br.grupointegrado.tads.buscadorgithub

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // exercicioJson()
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
            val task = GithubBuscaTask()
            task.execute(urlBusca)
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

    //    fun exercicioJson() {
    //        var dadosJson = """
    //            {
    //                "temperatura": {
    //                    "maxima": 11.34,
    //                    "minima": 19.01
    //                },
    //                "clima": {
    //                    "id": 801,
    //                    "condicao": "Nuvens",
    //                    "descricao": "poucas nuvens"
    //                },
    //                "pressao": 1023.51,
    //                "umidade": 87
    //            }
    //            """
    //        val objetoPrevisao = JSONObject(dadosJson)
    //        val clima = objetoPrevisao.getJSONObject("clima")
    //        val condicao = clima.getString("condicao")
    //        val pressao = objetoPrevisao.getDouble("pressao")
    //
    //        Log.d("exercicioJson", "$condicao -> $pressao")
    //    }

    inner class GithubBuscaTask : AsyncTask<URL, Void, String>() {

        override fun onPreExecute() {
            exibirProgressBar()
        }

        override fun doInBackground(vararg params: URL?): String? {
            try {
                val url = params[0]
                val resultado =
                        NetworkUtils.obterRespostaDaUrlHtpp(url!!)
                return resultado
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(resultado: String?) {
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
    }

}
