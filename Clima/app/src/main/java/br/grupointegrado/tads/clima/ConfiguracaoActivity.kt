package br.grupointegrado.tads.clima

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem

class ConfiguracaoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracao)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
            return true;
        }
        return super.onOptionsItemSelected(item)
    }
}
