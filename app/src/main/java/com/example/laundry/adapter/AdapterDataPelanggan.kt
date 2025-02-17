package com.example.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.R
import com.example.laundry.model_data.ModelPelanggan

class AdapterDataPelanggan(
    private val listPelanggan: ArrayList<ModelPelanggan>) :
    RecyclerView.Adapter<AdapterDataPelanggan.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_pelanggan, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listPelanggan[position]
        holder.tvIdPelanggan.text = item.idPelanggan
        holder.tvNamaPelanggan.text = item.namaPelanggan
        holder.tvAlamatPelanggan.text = item.alamatPelanggan
        holder.tvNoHpPelanggan.text = item.noHpPelanggan
        holder.tvTerdaftar.text = item.terdaftar
        holder.cDataPelanggan.setOnClickListener {
        }
        holder.btnHubungi.setOnClickListener{
        }
        holder.btnLihat.setOnClickListener{
        }
    }

    override fun getItemCount(): Int {
        return listPelanggan.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvIdPelanggan: TextView = itemView.findViewById(R.id.tvIdPelanggan)
        val tvNamaPelanggan: TextView = itemView.findViewById(R.id.tvNamaPelanggan)
        val tvAlamatPelanggan: TextView = itemView.findViewById(R.id.tvAlamatPelanggan)
        val tvNoHpPelanggan: TextView = itemView.findViewById(R.id.tvNoHpPelanggan)
        val tvTerdaftar: TextView = itemView.findViewById(R.id.tvTerdaftar)
        val cDataPelanggan: CardView = itemView.findViewById(R.id.card_data_pelanggan)
        val btnHubungi: Button = itemView.findViewById(R.id.btnHubungi)
        val btnLihat: Button = itemView.findViewById(R.id.btnLihat)
    }
}