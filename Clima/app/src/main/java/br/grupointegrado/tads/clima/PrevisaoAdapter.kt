package br.grupointegrado.tads.clima

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by dougl on 20/04/2018.
 */
class PrevisaoAdapter : RecyclerView.Adapter<PrevisaoAdapter.PrevisaoViewHolder> {

    private var dadosClima: Array<String?>?
    private var itemClickListener: PrevisaoItemClickListener

    constructor(dadosClima: Array<String?>?, itemClickListener: PrevisaoItemClickListener) {
        this.dadosClima = dadosClima
        this.itemClickListener = itemClickListener;
    }

    override fun getItemCount(): Int {
        val dados = dadosClima
        if (dados != null) {
            return dados.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: PrevisaoViewHolder, position: Int) {
        val previsao = dadosClima?.get(position)
        holder.tvDadosPrevisao.text = previsao
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrevisaoViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.previsao_list_item, parent, false)

        val viewHolder = PrevisaoViewHolder(view)

        return viewHolder
    }

    fun setDadosClima(dadosClima: Array<String?>?) {
        this.dadosClima = dadosClima
        notifyDataSetChanged()
    }

    fun getDadosClima() : Array<String?>? {
        return dadosClima
    }

    interface PrevisaoItemClickListener {
        fun onItemClick(index: Int)
    }

    inner class PrevisaoViewHolder : RecyclerView.ViewHolder {

        val tvDadosPrevisao: TextView

        constructor(itemView: View) : super(itemView) {
            tvDadosPrevisao = itemView.findViewById(R.id.tv_dados_previsao)

            itemView.setOnClickListener({
                itemClickListener.onItemClick(adapterPosition)
            })
        }
    }
}