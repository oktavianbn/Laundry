package com.example.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.R
import com.example.laundry.model_data.ModelLayanan

class AdapterDataLayanan (
    private val listLayanan: ArrayList<ModelLayanan>) :
    RecyclerView.Adapter<AdapterDataLayanan.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_layanan, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listLayanan[position]
        holder.tvNamaLayanan.text = item.namaLayanan
        holder.tvHargaLayanan.text = item.hargaLayanan
        holder.tvCabangLayanan.text = item.cabangLayanan
        holder.cDataLayanan.setOnClickListener {
        }
        holder.btnLihat.setOnClickListener {
        }
    }

    override fun getItemCount(): Int {
        return listLayanan.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaLayanan: TextView = itemView.findViewById(R.id.tvNamaLayanan)
        val tvHargaLayanan: TextView = itemView.findViewById(R.id.tvHargaLayanan)
        val tvCabangLayanan: TextView = itemView.findViewById(R.id.tvCabangLayanan)
        val cDataLayanan: CardView = itemView.findViewById(R.id.card_data_Layanan)
        val btnLihat: Button = itemView.findViewById(R.id.btnLihat)
    }
}
