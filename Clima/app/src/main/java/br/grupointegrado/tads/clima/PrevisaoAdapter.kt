package br.grupointegrado.tads.clima

import android.database.Cursor
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import br.grupointegrado.tads.clima.dados.ClimaContrato
import br.grupointegrado.tads.clima.util.ClimaUtils
import br.grupointegrado.tads.clima.util.DataUtils

/**
 * Created by dougl on 20/04/2018.
 */
class PrevisaoAdapter : RecyclerView.Adapter<PrevisaoAdapter.PrevisaoViewHolder> {

    private var cursor: Cursor?
    private var itemClickListener: PrevisaoItemClickListener

    constructor(cursor: Cursor?, itemClickListener: PrevisaoItemClickListener) {
        this.cursor = cursor
        this.itemClickListener = itemClickListener;
    }

    override fun getItemCount(): Int {
        val c = cursor
        if (c != null) {
            return c.count
        }
        return 0
    }

    override fun onBindViewHolder(holder: PrevisaoViewHolder, position: Int) {
        val c = cursor
        if (c == null) {
            return
        }

        if (!c.moveToPosition(position)) {
            return
        }

        val context = holder.itemView.context
        val Clima = ClimaContrato.Clima;

        val dateInMillis = c.getLong(c.getColumnIndex(Clima.COLUNA_DATA_HORA))
        val dateString = DataUtils.getDataAmigavelEmString(context, dateInMillis, false)

        val weatherId = c.getInt(c.getColumnIndex(Clima.COLUNA_CLIMA_ID))
        val description = ClimaUtils.getStringCondicoesDoClima(context, weatherId)

        val highInCelsius = c.getDouble(c.getColumnIndex(Clima.COLUNA_MAX_TEMP))
        val lowInCelsius = c.getDouble(c.getColumnIndex(Clima.COLUNA_MIN_TEMP))
        val highAndLowTemperature = ClimaUtils.formataMaxMin(context, highInCelsius, lowInCelsius)

        val weatherSummary = "$dateString - $description - $highAndLowTemperature"

        holder.tvDadosPrevisao.text = weatherSummary
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrevisaoViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.previsao_list_item, parent, false)

        val viewHolder = PrevisaoViewHolder(view)

        return viewHolder
    }

    fun atualizarCursor(cursor: Cursor?) {
        this.cursor?.close()
        this.cursor = cursor
        notifyDataSetChanged()
    }

    interface PrevisaoItemClickListener {
        fun onItemClick(dataHora: Long)
    }

    inner class PrevisaoViewHolder : RecyclerView.ViewHolder {

        val tvDadosPrevisao: TextView

        constructor(itemView: View) : super(itemView) {
            tvDadosPrevisao = itemView.findViewById(R.id.tv_dados_previsao)

            itemView.setOnClickListener({
                cursor!!.moveToPosition(adapterPosition)
                val dateInMillis = cursor!!.getLong(cursor!!.getColumnIndex(ClimaContrato.Clima.COLUNA_DATA_HORA))
                itemClickListener.onItemClick(dateInMillis)
            })
        }
    }
}