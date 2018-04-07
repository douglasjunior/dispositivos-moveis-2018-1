package br.grupointegrado.tads.listaverde

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class ListaVerdeAdapter : RecyclerView.Adapter<ListaVerdeAdapter.NumeroViewHolder> {

    val lista: List<Int>
    val context: Context

    constructor(context: Context, lista: List<Int>) {
        this.context = context;
        this.lista = lista
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumeroViewHolder {
        val numeroListItem = LayoutInflater.from(context)
                .inflate(R.layout.numero_list_item, parent, false);
        val numeroViewHolder = NumeroViewHolder(numeroListItem)
        return numeroViewHolder
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: NumeroViewHolder, position: Int) {
        val numero = lista.get(position)
        holder.tvItemNumero?.text = numero.toString()
        val cor = ColorUtils.getViewHolderBackgroundColor(context, position)
        holder.tvItemNumero?.setBackgroundColor(cor)
    }

    inner class NumeroViewHolder : RecyclerView.ViewHolder {

        val tvItemNumero: TextView?

        constructor(itemView: View?) : super(itemView) {
            tvItemNumero =
                    itemView?.findViewById<TextView>(R.id.tv_item_numero);
        }

    }
}