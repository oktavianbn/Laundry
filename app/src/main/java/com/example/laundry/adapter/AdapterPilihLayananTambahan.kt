package com.example.laundry.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.R
import com.example.laundry.adapter.AdapterDataPelanggan.ViewHolder
import com.example.laundry.model_data.ModelTambahan


class AdapterPilihLayananTambahan(
    private val listTambahan: ArrayList<ModelTambahan>,
    private val onItemClick: (ModelTambahan) -> Unit // Tambahan callback lambda
) : RecyclerView.Adapter<AdapterPilihLayananTambahan.ViewHolder>() {

    private lateinit var appContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_pilih_layanan_tambahan, parent, false)
        appContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listTambahan[position]
        holder.tvNamaTambahan.text = item.namaTambahan
        holder.tvHargaTambahan.text = item.hargaTambahan

        holder.cPilihLayananTambahan.setOnClickListener {
            onItemClick(item) // Panggil callback
        }
    }

    override fun getItemCount(): Int = listTambahan.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaTambahan: TextView = itemView.findViewById(R.id.tvNamaLayananTambahan)
        val tvHargaTambahan: TextView = itemView.findViewById(R.id.tvHargaLayananTambahan)
        val cPilihLayananTambahan: CardView = itemView.findViewById(R.id.cardPilihLayananTambahan)
    }
}
