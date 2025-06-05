package com.example.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.R
import com.example.laundry.model_data.ModelLayanan

class AdapterPilihLayanan(
    private val listLayanan: ArrayList<ModelLayanan>,
    private val onItemClick: (ModelLayanan) -> Unit
) : RecyclerView.Adapter<AdapterPilihLayanan.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaLayanan: TextView = itemView.findViewById(R.id.tvNamaLayanan)
        val tvHargaLayanan: TextView = itemView.findViewById(R.id.tvHargaLayanan)
        val cPilihLayanan: CardView = itemView.findViewById(R.id.cardPilihLayanan)

        init {
            cPilihLayanan.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(listLayanan[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.card_pilih_layanan, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listLayanan.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val layanan = listLayanan[position]
        holder.tvNamaLayanan.text = layanan.namaLayanan
        holder.tvHargaLayanan.text = layanan.hargaLayanan.toString()
        // Listener sudah ditangani di init block
    }
}
