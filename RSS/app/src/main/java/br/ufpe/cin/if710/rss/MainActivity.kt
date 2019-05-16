package br.ufpe.cin.if710.rss

import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.util.Xml
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.textView
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : Activity() {

    private val RSS_FEED = "http://leopoldomt.com/if1001/g1brasil.xml"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        var load = LoadXML()
        try {
            // Executa o asynctask e pega o resultado dessa execução
            var xmls = load.execute().get()

            // Cria um arrayAdapter com a lista resultante do download e popula o list view da main activity
            conteudoRSS.adapter = ArrayAdapter<ItemRSS>(this, android.R.layout.simple_list_item_1, xmls)
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
