package br.grupointegrado.tads.clima

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val dadosClimaFicticios = listOf("Hoje - Céu limpo - 17°C / 15°C",
            "Amanhã - Nublado - 19°C / 15°C",
            "Quinta - Chuvoso - 30°C / 11°C",
            "Sexta - Chuva com raios - 21°C / 9°C",
            "Sábado - Chuva com raios - 16°C / 7°C",
            "Domingo - Chuvoso - 16°C / 8°C",
            "Segunda - Parcialmente nublado - 15°C / 10°C",
            "Ter, Mai 24 - Vai curintia - 16°C / 18°C",
            "Qua, Mai 25 - Nublado - 19°C / 15°C",
            "Qui, Mai 26 - Tempestade - 30°C / 11°C",
            "Sex, Mai 27 - Furacão - 21°C / 9°C",
            "Sáb, Mai 28 - Meteóro - 16°C / 7°C",
            "Dom, Mai 29 - Apocalipse - 16°C / 8°C",
            "Seg, Mai 30 - Pós-apocalipse - 15°C / 10°C")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var conteudo = ""
        for (clima in dadosClimaFicticios) {
            conteudo += "$clima\n\n\n"
        }
        dados_clima.text = conteudo
    }
}
