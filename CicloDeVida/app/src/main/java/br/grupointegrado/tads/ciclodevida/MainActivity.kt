package br.grupointegrado.tads.ciclodevida

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.support.v4.app.ActivityCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imprimir("onCreate")
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
    }

    /**
     * Abre uma Dialog de permissão para simular o evento onPause
     */
    fun abrirDialog(view: View) {
        val permissoes = arrayOf(Manifest.permission.WRITE_CALENDAR)

        ActivityCompat.requestPermissions(this,
                permissoes,
                1000)
    }
}
