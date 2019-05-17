package br.ufpe.cin.if710.rss

import android.app.ListActivity
import android.os.AsyncTask
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : ListActivity() {

    private val RSS_FEED = "http://pox.globo.com/rss/g1/ciencia-e-saude/"

    override fun onStart() {
        super.onStart()
        var load = LoadXML()
        try {
            // Executa o asynctask e pega o resultado dessa execução
            var xmls = load.execute().get()

            // Cria um array com a lista resultante do download
            val itemRSSArray = Array(xmls.size) {
                i -> xmls[i]
            }

            // Configurando o listAdapter da ListActivity
            listAdapter = ItemRSSAdapter(itemRSSArray, applicationContext)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    internal inner class LoadXML: AsyncTask<Int, Int, List<ItemRSS>>() {

        override fun doInBackground(vararg params: Int?): List<ItemRSS>? {
            return getRssFeed(RSS_FEED)
        }

        @Throws(IOException::class)
        fun getRssFeed(feed: String): List<ItemRSS>? {
            var input: InputStream? = null
            var rssFeed: String

            try {
                val url: URL = URL(feed)
                val conn = url.openConnection() as HttpURLConnection
                input = conn.inputStream
                val out = ByteArrayOutputStream()
                val buffer = ByteArray(1024)
                var count = input.read(buffer)
                while (count != -1) {
                    out.write(buffer, 0, count)
                    count = input.read(buffer)
                }
                val response = out.toByteArray()
                rssFeed = String(response, Charsets.UTF_8)
            } finally {
                if (input != null) {
                    input.close()
                }
            }

            // Pega a string resultante do download do arquivo e faz o parse através da função solicitada
            return ParserRSS.parse(rssFeed)
        }
    }
}
