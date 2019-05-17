package br.ufpe.cin.if710.rss

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.itemlista.view.*

class ItemRSSAdapter(private val itensRSS: Array<ItemRSS>, private val c: Context): BaseAdapter()  {

    // Retorna a quantidade de itens no array
    override fun getCount(): Int {
        return itensRSS.size
    }

    // Retorna um item do array
    override fun getItem(position: Int): Any {
        return itensRSS[position]
    }

    // Converte a posição pra long só pra gerar um ID
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // Configura a exibição de cada item
    // Define o layout como o definido em itemlista
    // Define o texto para cada textview presente no layout
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(c).inflate(R.layout.itemlista, parent, false)
        view.item_titulo.text = itensRSS[position].title
        view.item_data.text = itensRSS[position].pubDate
        return view
    }
}