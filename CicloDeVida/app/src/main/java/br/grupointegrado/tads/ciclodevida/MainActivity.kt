package br.grupointegrado.tads.ciclodevida

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.support.v4.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        val MENSAGENS_LOG = "MENSAGENS_LOG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(MENSAGENS_LOG)){
                val mensagensLog = savedInstanceState.getString(MENSAGENS_LOG)
                tv_mensagem_log.text = mensagensLog
            }
        }

        imprimir("onCreate")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        imprimir("onSaveInstanceState")

        val mensagensLog = tv_mensagem_log.text.toString()
        outState?.putString(MENSAGENS_LOG, mensagensLog)
    }

    override fun onStart() {
        super.onStart()
        imprimir("onStart")
    }

    override fun onResume() {
        super.onResume()
        imprimir("onResume")
    }

    override fun onPause() {
        super.onPause()
        imprimir("onPause")
    }

    override fun onStop() {
        super.onStop()
        imprimir("onStop")
    }

    override fun onRestart() {
        super.onRestart()
        imprimir("onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        imprimir("onDestroy")
    }

    fun imprimir(mensagem: String) {
        Log.d("CicloVida", mensagem)
        tv_mensagem_log.append("$mensagem \n")
    }

    /**
     * Abre uma Dialog de permissão para simular o evento onPause
     */
    fun abrirDialog(view: View) {
        val permissoes = arrayOf(Manifest.permission.WRITE_CALENDAR)
        ActivityCompat.requestPermissions(this, permissoes, 1000)
    }
}
