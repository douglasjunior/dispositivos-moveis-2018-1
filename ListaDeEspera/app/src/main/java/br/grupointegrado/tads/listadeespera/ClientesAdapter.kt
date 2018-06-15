package br.grupointegrado.tads.listadeespera

import android.database.Cursor
import android.provider.BaseColumns
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class ClientesAdapter : RecyclerView.Adapter<ClientesAdapter.ClienteViewHolder> {

    private var cursor: Cursor

    constructor(cursor: Cursor) {
        this.cursor = cursor
    }

    fun atualizarCursor(cursor: Cursor) {
        this.cursor.close()
        this.cursor = cursor
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.cliente_list_item, parent, false)
        return ClienteViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        if (!cursor.moveToPosition(position)) {
            return;
        }

        val id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID))
        val nome = cursor.getString(cursor.getColumnIndex(ListaEsperaContrato.Clientes.COLUNA_NOME))
        val tamanhoGrupo = cursor.getInt(cursor.getColumnIndex(ListaEsperaContrato.Clientes.COLUNA_TAMANHO_GRUPO))

        holder.itemView.tag = id
        holder.tvNome.text = nome
        holder.tvTamanhoGrupo.text = tamanhoGrupo.toString()
    }

    override fun getItemCount(): Int {
        return cursor.count
    }

    inner class ClienteViewHolder : RecyclerView.ViewHolder {

        var tvNome: TextView
        var tvTamanhoGrupo: TextView

        constructor(itemView: View) : super(itemView) {
            tvNome = itemView.findViewById(R.id.tv_nome)
            tvTamanhoGrupo = itemView.findViewById(R.id.tv_tamanho_grupo)
        }
    }
}
