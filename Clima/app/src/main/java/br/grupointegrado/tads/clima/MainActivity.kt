package br.grupointegrado.tads.clima

import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v7.preference.PreferenceManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import br.grupointegrado.tads.clima.dados.ClimaContrato
import br.grupointegrado.tads.clima.dados.ClimaPreferencias
import br.grupointegrado.tads.clima.sync.ClimaSyncUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
        PrevisaoAdapter.PrevisaoItemClickListener,
        LoaderManager.LoaderCallbacks<Cursor>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    companion object {
        val DADOS_PREVISAO_LOADER = 1000
        var PREFERENCIAS_FORAM_ALTERADAS = false
    }

    var previsaoAdapter: PrevisaoAdapter? = null
    var posicao = RecyclerView.NO_POSITION

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        DadosFakeUtils.inserirDadosFake(this)

        previsaoAdapter = PrevisaoAdapter(null, this)
        val layoutManager = LinearLayoutManager(this)

        rv_clima.layoutManager = layoutManager
        rv_clima.adapter = previsaoAdapter

        exibirProgressBar()

        supportLoaderManager.initLoader(DADOS_PREVISAO_LOADER, null, this)

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this)

        ClimaSyncUtils.inicializar(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {

        when (id) {
            DADOS_PREVISAO_LOADER -> {
                val forecastQueryUri = ClimaContrato.Clima.CONTENT_URI
                val sortOrder = "${ClimaContrato.Clima.COLUNA_DATA_HORA} ASC"
                val selection = ClimaContrato.Clima.getSqlSelectHojeEmDiante()

                return CursorLoader(this,
                        forecastQueryUri,
                        null,
                        selection,
                        null,
                        sortOrder)
            }
            else -> throw RuntimeException("Loader não implementado: $id")
        }

    }

    override fun onLoadFinished(loader: Loader<Cursor>?,
                                cursor: Cursor) {
        previsaoAdapter?.atualizarCursor(cursor)
        if (this.posicao == RecyclerView.NO_POSITION) {
            this.posicao = 0
        }
        rv_clima.smoothScrollToPosition(this.posicao)
        if (cursor.count > 0) {
            exibirResultado()
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {
        previsaoAdapter?.atualizarCursor(null)
    }

    override fun onItemClick(dataHora: Long) {
        val intentDetalhes = Intent(this, DetalhesActivity::class.java)

        val uri = ClimaContrato.Clima.construirPrevisaoUri(dataHora)
        intentDetalhes.data = uri

        startActivity(intentDetalhes)
    }

    fun abrirMapa() {
        val addressString = ClimaPreferencias.getLocalizacaoSalva(this)
        val uriGeo = Uri.parse("geo:0,0?q=$addressString")

        val intentMapa = Intent(Intent.ACTION_VIEW)
        intentMapa.data = uriGeo

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intentMapa)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.clima, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId === R.id.acao_atualizar) {
            ClimaSyncUtils.sincronizarImediatamente(this)
            return true
        }
        if (item?.itemId === R.id.acao_mapa) {
            abrirMapa()
            return true
        }
        if (item?.itemId === R.id.acao_configuracao) {
            abrirConfiguracao()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun abrirConfiguracao() {
        val intent = Intent(this, ConfiguracaoActivity::class.java)
        startActivity(intent)
    }

    fun exibirResultado() {
        rv_clima.visibility = View.VISIBLE
        tv_mensagem_erro.visibility = View.INVISIBLE
        pb_aguarde.visibility = View.INVISIBLE
    }

    fun exibirProgressBar() {
        rv_clima.visibility = View.INVISIBLE
        tv_mensagem_erro.visibility = View.INVISIBLE
        pb_aguarde.visibility = View.VISIBLE
    }

    override fun onStart() {
        super.onStart()

        if (PREFERENCIAS_FORAM_ALTERADAS) {
            supportLoaderManager.restartLoader(DADOS_PREVISAO_LOADER, null, this)
            PREFERENCIAS_FORAM_ALTERADAS = false
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?,
                                           key: String?) {
        PREFERENCIAS_FORAM_ALTERADAS = true
    }
}
