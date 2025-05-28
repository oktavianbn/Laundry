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
import com.example.laundry.layanan.TambahLayananActivity
import com.example.laundry.model_data.ModelLayanan
import com.google.firebase.database.FirebaseDatabase

class AdapterDataLayanan(
    private val listLayanan: ArrayList<ModelLayanan>
) :
    RecyclerView.Adapter<AdapterDataLayanan.ViewHolder>() {
    private lateinit var appContext: Context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_layanan, parent, false)
        appContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("layanan")
        val item = listLayanan[position]
        holder.tvNamaLayanan.text = item.namaLayanan
        holder.tvHargaLayanan.text = item.hargaLayanan
        holder.tvCabangLayanan.text = item.cabangLayanan
        holder.cDataLayanan.setOnClickListener {

        }
        holder.btnLihat.setOnClickListener {
            val dialogView =
                LayoutInflater.from(appContext).inflate(R.layout.dialog_mod_layanan, null)
            val dialog =
                androidx.appcompat.app.AlertDialog.Builder(appContext).setView(dialogView).create()
            val tvIdLayanan = dialogView.findViewById<TextView>(R.id.tvIdLayanan)
            val tvNamaLayanan = dialogView.findViewById<TextView>(R.id.tvNamaLayanan)
            val etNoHpLayanan = dialogView.findViewById<TextView>(R.id.tvHargaLayanan)
            val tvCabangLayanan = dialogView.findViewById<TextView>(R.id.tvCabangLayanan)
            val btnSunting = dialogView.findViewById<Button>(R.id.btnSunting)
            val btnHapus = dialogView.findViewById<Button>(R.id.btnHapus)
            tvIdLayanan.text = item.idLayanan
            tvNamaLayanan.text = item.namaLayanan
            etNoHpLayanan.text = item.hargaLayanan
            tvCabangLayanan.text = item.cabangLayanan


            btnSunting.setOnClickListener {
                val intent = Intent(appContext, TambahLayananActivity::class.java)
                intent.putExtra("Judul", "Edit Layanan")
                intent.putExtra("idLayanan", item.idLayanan)
                intent.putExtra("namaLayanan", item.namaLayanan)
                intent.putExtra("hargaLayanan", item.hargaLayanan)
                intent.putExtra("cabangLayanan", item.cabangLayanan)
                appContext.startActivity(intent)
                dialog.dismiss()
            }
            btnHapus.setOnClickListener {
                val konfirmasi = androidx.appcompat.app.AlertDialog.Builder(appContext)
                    .setTitle("Konfirmasi Hapus")
                    .setMessage("Yakin ingin menghapus ${item.namaLayanan}?")
                    .setPositiveButton("Hapus") { _, _ ->

                        myRef.child(item.idLayanan!!).removeValue()
                            .addOnSuccessListener {
                                Log.d("Firebase", "Data berhasil dihapus")
                                Toast.makeText(appContext, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { error ->
                                Log.e("Firebase", "Gagal menghapus data: ${error.message}")
                                Toast.makeText(appContext,"Data GAGAL Dihapus",Toast.LENGTH_SHORT).show()
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
        return listLayanan.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaLayanan: TextView = itemView.findViewById(R.id.tvNamaLayanan)
        val tvHargaLayanan: TextView = itemView.findViewById(R.id.tvHargaLayanan)
        val tvCabangLayanan: TextView = itemView.findViewById(R.id.tvCabangLayanan)
        val cDataLayanan: CardView = itemView.findViewById(R.id.cardDataLayanan)
        val btnLihat: Button = itemView.findViewById(R.id.btnLihat)
    }
}
