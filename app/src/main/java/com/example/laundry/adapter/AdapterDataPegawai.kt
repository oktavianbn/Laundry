package com.example.laundry.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.R
import com.example.laundry.model_data.ModelPegawai
import com.example.laundry.pegawai.TambahPegawaiActivity
import com.example.laundry.pelanggan.TambahPelangganActivity
import com.google.firebase.database.DatabaseReference
import java.lang.ref.Reference

class AdapterDataPegawai(
    private val listPegawai: ArrayList<ModelPegawai>) :

    RecyclerView.Adapter<AdapterDataPegawai.ViewHolder>() {
    lateinit var appContext:Context
    lateinit var databaseReference: DatabaseReference
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_pegawai, parent, false)
        appContext=parent.context
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
        holder.cDataPegawai.setOnClickListener {
            val intent=Intent(appContext, TambahPegawaiActivity::class.java)
            intent.putExtra("Judul","Edit Pegawai")
            intent.putExtra("idPegawai",item.idPegawai)
            intent.putExtra("namaPegawai",item.namaPegawai)
            intent.putExtra("noHpPegawai",item.noHpPegawai)
            intent.putExtra("alamatPegawai",item.alamatPegawai)
//            intent.putExtra("idCabang",item.idCabang)
            appContext.startActivity(intent)
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
        val cDataPegawai: CardView = itemView.findViewById(R.id.card_data_pegawai)
        val tvIdPegawai: TextView = itemView.findViewById(R.id.tvIdPegawai)
        val tvNamaPegawai: TextView = itemView.findViewById(R.id.tvNamaPegawai)
        val tvAlamatPegawai: TextView = itemView.findViewById(R.id.tvAlamatPegawai)
        val tvNoHpPegawai: TextView = itemView.findViewById(R.id.tvNoHpPegawai)
        val cabangPegawai: TextView = itemView.findViewById(R.id.tvCabangPegawai)
        val btnHubungi: Button = itemView.findViewById(R.id.btnHubungi)
        val btnLihat: Button = itemView.findViewById(R.id.btnLihat)
    }
}