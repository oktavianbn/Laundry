package com.example.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.R
import com.example.laundry.model_data.ModelTambahan

class AdapterPembayaran (
    private val list: List<ModelTambahan>
) : RecyclerView.Adapter<AdapterPembayaran.TambahanViewHolder>() {

    inner class TambahanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaTambahan: TextView = itemView.findViewById(R.id.tvNamaTambahan)
        val tvHargaTambahan: TextView = itemView.findViewById(R.id.tvHargaTambahan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TambahanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_pilih_layanan_tambahan, parent, false)
        return TambahanViewHolder(view)
    }

    override fun onBindViewHolder(holder: TambahanViewHolder, position: Int) {
        val item = list[position]
        holder.tvNamaTambahan.text = item.namaTambahan
        holder.tvHargaTambahan.text = "Rp ${item.hargaTambahan}"
    }

    override fun getItemCount(): Int = list.size
}