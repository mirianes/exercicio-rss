package br.ufpe.cin.if710.rss

import android.app.Activity
import android.os.Bundle
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
        try {
            val feedXML = getRssFeed(RSS_FEED)
            val lista = Array(1) {
                i -> feedXML
            }
            conteudoRSS.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista)
        }
        catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    fun getRssFeed(feed: String): String {
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

        return rssFeed
    }
}
