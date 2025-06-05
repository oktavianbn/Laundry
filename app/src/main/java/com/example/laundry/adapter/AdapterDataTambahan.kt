package com.example.laundry.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.R
import com.example.laundry.model_data.ModelTambahan
import com.example.laundry.tambahan.TambahTambahanActivity
import com.google.firebase.database.FirebaseDatabase

class AdapterDataTambahan(
    private val listTambahan: ArrayList<ModelTambahan>
) :
    RecyclerView.Adapter<AdapterDataTambahan.ViewHolder>() {
    private lateinit var appContext: Context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.card_tambahan, parent, false)
        appContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("tambahan")
        val item = listTambahan[position]
        holder.tvNamaTambahan.text = item.namaTambahan
        holder.tvHargaTambahan.text = item.hargaTambahan.toString()
        holder.tvCabangTambahan.text = item.cabangTambahan
        holder.cDataTambahan.setOnClickListener {

        }
        holder.btnLihat.setOnClickListener {
            val dialogView =
                LayoutInflater.from(appContext).inflate(R.layout.dialog_mod_tambahan, null)
            val dialog =
                androidx.appcompat.app.AlertDialog.Builder(appContext).setView(dialogView).create()
            val tvIdTambahan = dialogView.findViewById<TextView>(R.id.tvIdTambahan)
            val tvNamaTambahan = dialogView.findViewById<TextView>(R.id.tvNamaTambahan)
            val etNoHpTambahan = dialogView.findViewById<TextView>(R.id.tvHargaTambahan)
            val tvCabangTambahan = dialogView.findViewById<TextView>(R.id.tvCabangTambahan)
            val btnSunting = dialogView.findViewById<Button>(R.id.btnSunting)
            val btnHapus = dialogView.findViewById<Button>(R.id.btnHapus)
            tvIdTambahan.text = item.idTambahan
            tvNamaTambahan.text = item.namaTambahan
            etNoHpTambahan.text = item.hargaTambahan.toString()
            tvCabangTambahan.text = item.cabangTambahan


            btnSunting.setOnClickListener {
                val intent = Intent(appContext, TambahTambahanActivity::class.java)
                intent.putExtra("Judul", "Edit Layanan Tambahan")
                intent.putExtra("idTambahan", item.idTambahan)
                intent.putExtra("namaTambahan", item.namaTambahan)
                intent.putExtra("hargaTambahan", item.hargaTambahan)
                intent.putExtra("cabangTambahan", item.cabangTambahan)
                appContext.startActivity(intent)
                dialog.dismiss()
            }
            btnHapus.setOnClickListener {
                val konfirmasi = androidx.appcompat.app.AlertDialog.Builder(appContext)
                    .setTitle("Konfirmasi Hapus")
                    .setMessage("Yakin ingin menghapus ${item.namaTambahan}?")
                    .setPositiveButton("Hapus") { _, _ ->

                        myRef.child(item.idTambahan!!).removeValue()
                            .addOnSuccessListener {
                                Log.d("Firebase", "Data berhasil dihapus")
                            }
                            .addOnFailureListener { error ->
                                Log.e("Firebase", "Gagal menghapus data: ${error.message}")
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
        return listTambahan.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaTambahan: TextView = itemView.findViewById(R.id.tvNamaTambahan)
        val tvHargaTambahan: TextView = itemView.findViewById(R.id.tvHargaTambahan)
        val tvCabangTambahan: TextView = itemView.findViewById(R.id.tvCabangTambahan)
        val cDataTambahan: CardView = itemView.findViewById(R.id.cardDataTambahan)
        val btnLihat: Button = itemView.findViewById(R.id.btnLihat)
    }
}