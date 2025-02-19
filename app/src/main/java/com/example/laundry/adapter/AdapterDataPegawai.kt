package com.example.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.R
import com.example.laundry.model_data.ModelPegawai

class AdapterDataPegawai(
    private val listPegawai: ArrayList<ModelPegawai>) :
    RecyclerView.Adapter<AdapterDataPegawai.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_pegawai, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listPegawai[position]
        holder.tvIdPegawai.text = item.idPegawai
        holder.tvNamaPegawai.text = item.namaPegawai
        holder.tvAlamatPegawai.text = item.alamatPegawai
        holder.tvNoHpPegawai.text = item.noHpPegawai
        holder.tvNoHpPegawai.text = item.noHpPegawai
        holder.cabangPegawai.text = item.cabangPegawai
//        holder.tvTerdaftar.text = item.terdaftar
        holder.cDataPegawai.setOnClickListener {
        }
        holder.btnHubungi.setOnClickListener{
        }
        holder.btnLihat.setOnClickListener{
        }
    }

    override fun getItemCount(): Int {
        return listPegawai.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvIdPegawai: TextView = itemView.findViewById(R.id.tvIdPegawai)
        val tvNamaPegawai: TextView = itemView.findViewById(R.id.tvNamaPegawai)
        val tvAlamatPegawai: TextView = itemView.findViewById(R.id.tvAlamatPegawai)
        val tvNoHpPegawai: TextView = itemView.findViewById(R.id.tvNoHpPegawai)
        val cabangPegawai: TextView = itemView.findViewById(R.id.tvCabangPegawai)
//        val tvTerdaftar: TextView = itemView.findViewById(R.id.tvTerdaftar)
        val cDataPegawai: CardView = itemView.findViewById(R.id.card_data_pegawai)
        val btnHubungi: Button = itemView.findViewById(R.id.btnHubungi)
        val btnLihat: Button = itemView.findViewById(R.id.btnLihat)
    }
}