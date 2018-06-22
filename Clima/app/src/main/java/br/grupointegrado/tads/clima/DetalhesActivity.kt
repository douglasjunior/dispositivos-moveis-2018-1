package br.grupointegrado.tads.clima

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.app.ShareCompat
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.view.Menu
import android.view.MenuItem
import br.grupointegrado.tads.clima.dados.ClimaContrato
import br.grupointegrado.tads.clima.util.ClimaUtils
import br.grupointegrado.tads.clima.util.DataUtils
import kotlinx.android.synthetic.main.activity_detalhes.*

class DetalhesActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    companion object {
        val DADOS_PREVISAO = "DADOS_PREVISAO"
        val DETALHES_PREVISAO_LOADER = 1001
    }

    private var uri: Uri? = null
    private var resumoPrevisao = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes)

        uri = intent.data

        supportLoaderManager.initLoader<Cursor>(DETALHES_PREVISAO_LOADER, null, this)
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
        if (item?.itemId == R.id.acao_configuracao) {
            abrirConfiguracao()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun abrirConfiguracao() {
        val intent = Intent(this, ConfiguracaoActivity::class.java)
        startActivity(intent)
    }

    fun compartilhar() {
        val intentCompartilhar = ShareCompat.IntentBuilder
                .from(this)
                .setType("text/plain")
                .setChooserTitle("Compartilhar previsão")
                .setText(resumoPrevisao)
                .intent
        intentCompartilhar.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
        if (intentCompartilhar.resolveActivity(packageManager) != null) {
            startActivity(intentCompartilhar)
        }
    }

    override fun onCreateLoader(id: Int, loaderArgs: Bundle?): Loader<Cursor> {
        when (id) {
            DETALHES_PREVISAO_LOADER ->
                return CursorLoader(this,
                        uri,
                        null,
                        null, null, null)

            else -> throw RuntimeException("Loader não implementado: $id")
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor?) {
        var cursorHasValidData = false
        if (cursor != null && cursor.moveToFirst()) {
            cursorHasValidData = true
        }

        if (!cursorHasValidData) {
            return
        }

        val Clima = ClimaContrato.Clima

        val localDateMidnightGmt = cursor!!.getLong(cursor!!.getColumnIndex(Clima.COLUNA_DATA_HORA))
        val dateText = DataUtils.getDataAmigavelEmString(this, localDateMidnightGmt, true)

        tv_data.setText(dateText)

        val weatherId = cursor.getInt(cursor!!.getColumnIndex(Clima.COLUNA_CLIMA_ID))

        val description = ClimaUtils.getStringCondicoesDoClima(this, weatherId)

        tv_descricao.setText(description)

        val highInCelsius = cursor.getDouble(cursor!!.getColumnIndex(Clima.COLUNA_MAX_TEMP))

        val highString = ClimaUtils.formataTemperatura(this, highInCelsius)

        tv_max_temp.setText(highString)

        val lowInCelsius = cursor.getDouble(cursor!!.getColumnIndex(Clima.COLUNA_MIN_TEMP))

        val lowString = ClimaUtils.formataTemperatura(this, lowInCelsius)

        tv_min_temp.setText(lowString)

        val humidity = cursor.getFloat(cursor!!.getColumnIndex(Clima.COLUNA_UMIDADE))
        val humidityString = getString(R.string.formata_umidade, humidity)

        tv_umidade.setText(humidityString)

        val windSpeed = cursor.getFloat(cursor!!.getColumnIndex(Clima.COLUNA_VEL_VENTO))
        val windDirection = cursor.getFloat(cursor!!.getColumnIndex(Clima.COLUNA_GRAUS))
        val windString = ClimaUtils.getVentoFormatado(this, windSpeed, windDirection)

        tv_vento.setText(windString)

        val pressure = cursor.getFloat(cursor!!.getColumnIndex(Clima.COLUNA_PRESSAO))

        val pressureString = getString(R.string.formata_pressao, pressure)

        tv_pressao.setText(pressureString)

        resumoPrevisao = String.format("%s - %s - %s/%s",
                dateText, description, highString, lowString)
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {
    }
}
