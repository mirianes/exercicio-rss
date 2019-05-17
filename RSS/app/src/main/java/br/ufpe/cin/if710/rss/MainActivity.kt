package br.ufpe.cin.if710.rss

import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
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

            // Cria um array com a lista resultante do download
            val itemRSSArray = Array(xmls.size) {
                i -> xmls[i]
            }

            // Configurando o RecyclerView
            conteudoRSS.apply {
                // Utiliza-se o layout manager para dizer ao recycler view como organizar os elementos na tela
                // Aqui foi utilizado o layout linear, para exibir os elementos verticalmente
                layoutManager = LinearLayoutManager(applicationContext)

                // É necessário especificar qual o adapter que será utilizado
                // Aqui foi criado um adapter personalizado que exibe o titulo e o link do itemRSS
                adapter = ItemRSSAdapter(itemRSSArray, applicationContext)

                // Adiciona uma linha dividindo cada célula do recyclerview
                addItemDecoration(DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL))
            }
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
