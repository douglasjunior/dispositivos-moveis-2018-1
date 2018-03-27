package br.grupointegrado.tads.buscadorgithub

import android.net.Uri
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*

/**
 * Created by dougl on 16/03/2018.
 */
class NetworkUtils {

    companion object {

        val GITHUB_BASE_URL = "https://api.github.com/search/repositories"

        val PARAM_BUSCA = "q"
        val PARAM_ORDEM = "sort"
        val ordenarPor = "stars"

        fun construirUrl(buscaGithub: String): URL? {
            val builtUri = Uri.parse(GITHUB_BASE_URL).buildUpon()
                    .appendQueryParameter(PARAM_BUSCA, buscaGithub)
                    .appendQueryParameter(PARAM_ORDEM, ordenarPor)
                    .build()
            try {
                return URL(builtUri.toString())
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }
            return null
        }

        fun obterRespostaDaUrlHtpp(url: URL): String? {
            val urlConnection = url.openConnection() as
                    HttpURLConnection
            var inputStream : InputStream? = null
            try {
                inputStream = urlConnection.inputStream

                val scanner = Scanner(inputStream)
                scanner.useDelimiter("\\A")

                if (scanner.hasNext()) {
                    return scanner.next()
                }
            } finally {
                inputStream?.close()
                urlConnection.disconnect()
            }
            return null
        }

    }

}