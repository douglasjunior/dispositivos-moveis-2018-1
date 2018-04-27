package br.grupointegrado.tads.usandointents

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ShareCompat
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.content.ContentResolver



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_enviar.setOnClickListener({
            val mensagem = et_mensagem.text.toString()

            val activityDestino = SegundaActivity::class.java
            val intentSegundaActivity = Intent(this, activityDestino)
            intentSegundaActivity.putExtra(Intent.EXTRA_TEXT, mensagem)

            startActivity(intentSegundaActivity)
            // Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show()
        })
    }

    fun abrirSite(view: View) {
        val acao = Intent.ACTION_VIEW
        val dado = Uri.parse("https://www.grupointegrado.br")
        val intentImplicito = Intent(acao, dado)
        // verifica se a ação pode ser atendida
        if (intentImplicito.resolveActivity(packageManager) != null) {
            startActivity(intentImplicito)
        }
    }

    fun abrirMapa(view: View) {
        val endereco = "Av. Irmãos Pereira, 670 - Centro, Campo Mourão – PR"
        val builder = Uri.Builder()
                .scheme("geo")
                .path("0,0")
                .appendQueryParameter("q", endereco)
        // construindo a URI
        val uriEndereco = builder.build()
        Log.d("abrirMapa", uriEndereco.toString())
        val intent = Intent(Intent.ACTION_VIEW, uriEndereco)
        // verifica se a ação pode ser atendida
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    fun compartilhar(view: View) {
        val tipoMedia = "text/plain"
        val titulo = "Aprendendo a compartilhar"
        val texto = et_mensagem.text.toString()
        // criando o intent de compartilhamento
        val intentCompartilhar = ShareCompat.IntentBuilder
                .from(this)
                .setType(tipoMedia)
                .setChooserTitle(titulo)
                .setText(texto)
                .intent
        // verifica se a ação pode ser atendida
        if (intentCompartilhar.resolveActivity(packageManager) != null) {
            startActivity(intentCompartilhar)
        }
    }
}
