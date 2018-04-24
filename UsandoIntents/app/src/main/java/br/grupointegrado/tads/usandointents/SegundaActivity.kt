package br.grupointegrado.tads.usandointents

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_segunda.*

class SegundaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_segunda)

        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            val mensagem = intent.getStringExtra(Intent.EXTRA_TEXT)
            tv_exibir_mensagem.text = mensagem
        }
    }
}
