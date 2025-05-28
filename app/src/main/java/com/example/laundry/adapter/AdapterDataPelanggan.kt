package com.example.laundry.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import com.example.laundry.model_data.ModelPelanggan
import com.example.laundry.pelanggan.TambahPelangganActivity
import com.google.firebase.database.FirebaseDatabase

class AdapterDataPelanggan(
    private val listPelanggan: ArrayList<ModelPelanggan>
) :
    RecyclerView.Adapter<AdapterDataPelanggan.ViewHolder>() {
    private lateinit var appContext: Context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.card_pelanggan, parent, false)
        appContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("pelanggan")
        val item = listPelanggan[position]
        holder.tvIdPelanggan.text = item.idPelanggan
        holder.tvNamaPelanggan.text = item.namaPelanggan
        holder.tvAlamatPelanggan.text = item.alamatPelanggan
        holder.tvNoHpPelanggan.text = item.noHpPelanggan
        val nomor = item.noHpPelanggan
        fun convertToInternationalFormat(number: String): String {
            return if (number.startsWith("0")) {
                "+62" + number.drop(1)
            } else {
                number
            }
        }

        val nomorInter = convertToInternationalFormat(nomor!!)
        holder.cDataPelanggan.setOnClickListener {

        }
        holder.btnHubungi.setOnClickListener {
            val dialogView =
                LayoutInflater.from(appContext).inflate(R.layout.dialog_mod_hubungi, null)
            val dialog =
                androidx.appcompat.app.AlertDialog.Builder(appContext).setView(dialogView).create()
            val btnTelephone = dialogView.findViewById<Button>(R.id.btnTelephone)
            val btnSms = dialogView.findViewById<Button>(R.id.btnSms)
            val btnWhatsapp = dialogView.findViewById<Button>(R.id.btnWhatsapp)
            btnTelephone.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${nomorInter}")
                appContext.startActivity(intent)
            }
            btnSms.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("sms:${nomorInter}")
                intent.putExtra("sms_body", "Halo, saya ingin menghubungi Anda.")
                appContext.startActivity(intent)
            }
            btnWhatsapp.setOnClickListener {
                try {
                    val url = "https://wa.me/${nomorInter}"

                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    intent.setPackage("com.whatsapp")
                    appContext.startActivity(intent)


//                    if (intent.resolveActivity(packageManager) != null) {
//                    } else {
//                        Toast.makeText(this, "WhatsApp tidak terinstal.", Toast.LENGTH_SHORT).show()
//                    }

                } catch (e: Exception) {
                    e.printStackTrace()

                }

            }
            dialog.show()
        }
        holder.btnLihat.setOnClickListener {
            val dialogView =
                LayoutInflater.from(appContext).inflate(R.layout.dialog_mod_pelanggan, null)
            val dialog =
                androidx.appcompat.app.AlertDialog.Builder(appContext).setView(dialogView).create()
            val tvIdPelanggan = dialogView.findViewById<TextView>(R.id.tvIdPelanggan)
            val tvNamaPelanggan = dialogView.findViewById<TextView>(R.id.tvNamaPelanggan)
            val tvAlamatPelanggan = dialogView.findViewById<TextView>(R.id.tvAlamatPelanggan)
            val etNoHpPelanggan = dialogView.findViewById<TextView>(R.id.etNoHpPelanggan)
//            val tvAlamat = dialogView.findViewById<TextView>(R.id.tvNamaPelanggan)
            val btnSunting = dialogView.findViewById<Button>(R.id.btnSunting)
            val btnHapus = dialogView.findViewById<Button>(R.id.btnHapus)
            tvIdPelanggan.text = item.idPelanggan
            tvNamaPelanggan.text = item.namaPelanggan
            tvAlamatPelanggan.text = item.alamatPelanggan
            etNoHpPelanggan.text = item.noHpPelanggan

            btnSunting.setOnClickListener {
                val intent = Intent(appContext, TambahPelangganActivity::class.java)
                intent.putExtra("Judul", "Edit Pelanggan")
                intent.putExtra("idPelanggan", item.idPelanggan)
                intent.putExtra("namaPelanggan", item.namaPelanggan)
                intent.putExtra("noHpPelanggan", item.noHpPelanggan)
                intent.putExtra("alamatPelanggan", item.alamatPelanggan)
                appContext.startActivity(intent)
                dialog.dismiss()
            }
            btnHapus.setOnClickListener {
                val konfirmasi = androidx.appcompat.app.AlertDialog.Builder(appContext)
                    .setTitle("Konfirmasi Hapus")
                    .setMessage("Yakin ingin menghapus ${item.namaPelanggan}?")
                    .setPositiveButton("Hapus") { _, _ ->

                        myRef.child(item.idPelanggan!!).removeValue()
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

    suspend fun hapusData(){
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("pelanggan")

    }

    override fun getItemCount(): Int {
        return listPelanggan.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvIdPelanggan: TextView = itemView.findViewById(R.id.tvIdPelanggan)
        val tvNamaPelanggan: TextView = itemView.findViewById(R.id.tvNamaPelanggan)
        val tvAlamatPelanggan: TextView = itemView.findViewById(R.id.tvAlamatPelanggan)
        val tvNoHpPelanggan: TextView = itemView.findViewById(R.id.tvNoHpPelanggan)
        val cDataPelanggan: CardView = itemView.findViewById(R.id.cardDataPelanggan)
        val btnHubungi: Button = itemView.findViewById(R.id.btnHubungi)
        val btnLihat: Button = itemView.findViewById(R.id.btnLihat)
    }
}