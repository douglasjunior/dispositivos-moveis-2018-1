package br.grupointegrado.tads.clima

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ShareCompat
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_detalhes.*

class DetalhesActivity : AppCompatActivity() {

    companion object {
        val DADOS_PREVISAO = "DADOS_PREVISAO"
    }

    var previsao: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes)

        if (intent.hasExtra(DADOS_PREVISAO)) {
            previsao = intent.getStringExtra(DADOS_PREVISAO)
            tv_exibir_previsao.text = previsao
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detalhes, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.acao_compartilhar) {
            compartilhar()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun compartilhar() {
        val intentCompartilhar = ShareCompat.IntentBuilder
                .from(this)
                .setType("text/plain")
                .setChooserTitle("Compartilhar previsão")
                .setText(previsao)
                .intent

        if (intentCompartilhar.resolveActivity(packageManager) != null) {
            startActivity(intentCompartilhar)
        }
    }
}
