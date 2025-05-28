package com.example.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.R
import com.example.laundry.model_data.ModelPelanggan

class AdapterPilihDataPelanggan(
    private val listPelanggan: ArrayList<ModelPelanggan>,
    private val onItemClick: (ModelPelanggan) -> Unit
) : RecyclerView.Adapter<AdapterPilihDataPelanggan.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaPelanggan: TextView = itemView.findViewById(R.id.tvNamaPelanggan)
        val tvAlamatPelanggan: TextView = itemView.findViewById(R.id.tvAlamatPelanggan)
        val tvNoHpPelanggan: TextView = itemView.findViewById(R.id.tvNoHpPelanggan)
        val cPilihPelanggan: CardView = itemView.findViewById(R.id.cardPilihPelanggan)

        init {
            cPilihPelanggan.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(listPelanggan[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_pilih_pelanggan, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listPelanggan.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pelanggan = listPelanggan[position]
        holder.tvNamaPelanggan.text = pelanggan.namaPelanggan
        holder.tvAlamatPelanggan.text = pelanggan.alamatPelanggan
        holder.tvNoHpPelanggan.text = pelanggan.noHpPelanggan
        // Listener sudah ditangani di init block
    }
}
