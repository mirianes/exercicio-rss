package br.ufpe.cin.if710.rss

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.item_rssview.view.*

class ItemRSSAdapter(private val itensRSS: Array<ItemRSS>, private val c: Context): RecyclerView.Adapter<ItemRSSAdapter.ViewHolder>()  {

    override fun getItemCount(): Int = itensRSS.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemRSSAdapter.ViewHolder {
        // Configura o layout do adapter, aqui associei esse adapter ao layout que criei item_rssview
        val view = LayoutInflater.from(c).inflate(R.layout.item_rssview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemRSSAdapter.ViewHolder, position: Int) {
        // Pega o item que deve ser exibido de acordo com a posição do item na tela e no array
        val item = itensRSS[position]
        // Configura os textos a serem exibidos de acordo com as informações do item que deve ser exibido
        holder.title.text = item.title
        holder.link.text = item.link
    }

    class ViewHolder (item: View): RecyclerView.ViewHolder(item), View.OnClickListener {
        // Pega os textView do layout e associa a variaveis
        val title = item.title
        val link = item.link

        init {
            item.setOnClickListener(this)
        }

        // Função a ser executada quando um item do recycler é clicado
        override fun onClick(v: View) {
            // Exibe um toast com o titulo do item
            Toast.makeText(v.context, "${title.text}", Toast.LENGTH_SHORT).show()
        }
    }
}