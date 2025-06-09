package com.example.laundry.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.R
import com.example.laundry.cabang.TambahCabangActivity
import com.example.laundry.model_data.ModelCabang
import com.google.firebase.database.FirebaseDatabase

class AdapterDataCabang (
    private val listCabang: ArrayList<ModelCabang>
) :
    RecyclerView.Adapter<AdapterDataCabang.ViewHolder>() {
    private lateinit var appContext: Context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_cabang, parent, false)
        appContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("cabang")
        val item = listCabang[position]
        holder.tvNamaCabang.text = item.namaCabang
        holder.tvAlamatCabang.text = item.alamatCabang
        holder.cDataCabang.setOnClickListener {

        }
        holder.btnLihat.setOnClickListener {
            val dialogView =
                LayoutInflater.from(appContext).inflate(R.layout.dialog_mod_cabang, null)
            val dialog =
                androidx.appcompat.app.AlertDialog.Builder(appContext).setView(dialogView).create()
            val tvIdCabang = dialogView.findViewById<TextView>(R.id.tvIdCabang)
            val tvNamaCabang = dialogView.findViewById<TextView>(R.id.tvNamaCabang)
            val etAlamatCabang = dialogView.findViewById<TextView>(R.id.tvAlamatCabang)
            val btnSunting = dialogView.findViewById<Button>(R.id.btnSunting)
            val btnHapus = dialogView.findViewById<Button>(R.id.btnHapus)
            tvIdCabang.text = item.idCabang
            tvNamaCabang.text = item.namaCabang
            etAlamatCabang.text = item.alamatCabang

            btnSunting.setOnClickListener {
                val intent = Intent(appContext, TambahCabangActivity::class.java)
                intent.putExtra("Judul", "Edit Cabang")
                intent.putExtra("idCabang", item.idCabang)
                intent.putExtra("namaCabang", item.namaCabang)
                intent.putExtra("alamatCabang", item.alamatCabang)
                appContext.startActivity(intent)
                dialog.dismiss()
            }
            btnHapus.setOnClickListener {
                val konfirmasi = androidx.appcompat.app.AlertDialog.Builder(appContext)
                    .setTitle("Konfirmasi Hapus")
                    .setMessage("Yakin ingin menghapus ${item.namaCabang}?")
                    .setPositiveButton("Hapus") { _, _ ->

                        myRef.child(item.idCabang!!).removeValue()
                            .addOnSuccessListener {
                                Log.d("Firebase", "Data berhasil dihapus")
                                Toast.makeText(appContext, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { error ->
                                Log.e("Firebase", "Gagal menghapus data: ${error.message}")
                                Toast.makeText(appContext,"Data GAGAL Dihapus", Toast.LENGTH_SHORT).show()
                            }
                        dialog.dismiss()
                    }
                    .setNegativeButton("Batal", null)
                    .create()
                konfirmasi.show()
            }
            dialog.show()
        }
    }

    override fun getItemCount(): Int {
        return listCabang.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaCabang: TextView = itemView.findViewById(R.id.tvNamaCabang)
        val tvAlamatCabang: TextView = itemView.findViewById(R.id.tvAlamatCabang)
        val cDataCabang: CardView = itemView.findViewById(R.id.cardDataCabang)
        val btnLihat: Button = itemView.findViewById(R.id.btnLihat)
    }
}
