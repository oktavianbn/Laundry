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
import com.example.laundry.model_data.ModelPegawai
import com.example.laundry.pegawai.TambahPegawaiActivity
import com.google.firebase.database.FirebaseDatabase

class AdapterDataPegawai(
    private val listPegawai: ArrayList<ModelPegawai>
) :
    RecyclerView.Adapter<AdapterDataPegawai.ViewHolder>() {
    private lateinit var appContext: Context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_pegawai, parent, false)
        appContext = parent.context
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("pegawai")
        val item = listPegawai[position]
        holder.tvIdPegawai.text = item.idPegawai
        holder.tvNamaPegawai.text = item.namaPegawai
        holder.tvAlamatPegawai.text = item.alamatPegawai
        holder.tvNoHpPegawai.text = item.noHpPegawai
        holder.cabangPegawai.text = item.cabangPegawai
        val nomor = item.noHpPegawai
        fun convertToInternationalFormat(number: String): String {
            return if (number.startsWith("0")) {
                "+62" + number.drop(1)
            } else {
                number
            }
        }

        val nomorInter = convertToInternationalFormat(nomor!!)
        holder.cDataPegawai.setOnClickListener {

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
                LayoutInflater.from(appContext).inflate(R.layout.dialog_mod_pegawai, null)
            val dialog =
                androidx.appcompat.app.AlertDialog.Builder(appContext).setView(dialogView).create()
            val tvIdPegawai = dialogView.findViewById<TextView>(R.id.tvIdPegawai)
            val tvNamaPegawai = dialogView.findViewById<TextView>(R.id.tvNamaPegawai)
            val tvAlamatPegawai = dialogView.findViewById<TextView>(R.id.tvAlamatPegawai)
            val etNoHpPegawai = dialogView.findViewById<TextView>(R.id.etNoHpPegawai)
            val tvCabangPegawai = dialogView.findViewById<TextView>(R.id.tvCabangPegawai)
            val btnSunting = dialogView.findViewById<Button>(R.id.btnSunting)
            val btnHapus = dialogView.findViewById<Button>(R.id.btnHapus)
            tvIdPegawai.text = item.idPegawai
            tvNamaPegawai.text = item.namaPegawai
            tvAlamatPegawai.text = item.alamatPegawai
            etNoHpPegawai.text = item.noHpPegawai
            tvCabangPegawai.text = item.cabangPegawai


            btnSunting.setOnClickListener {
                val intent = Intent(appContext, TambahPegawaiActivity::class.java)
                intent.putExtra("Judul", "Edit Pegawai")
                intent.putExtra("idPegawai", item.idPegawai)
                intent.putExtra("namaPegawai", item.namaPegawai)
                intent.putExtra("noHpPegawai", item.noHpPegawai)
                intent.putExtra("alamatPegawai", item.alamatPegawai)
                intent.putExtra("cabangPegawai", item.cabangPegawai)
                appContext.startActivity(intent)
                dialog.dismiss()
            }
            btnHapus.setOnClickListener {
                val konfirmasi = androidx.appcompat.app.AlertDialog.Builder(appContext)
                    .setTitle("Konfirmasi Hapus")
                    .setMessage("Yakin ingin menghapus ${item.namaPegawai}?")
                    .setPositiveButton("Hapus") { _, _ ->

                        myRef.child(item.idPegawai!!).removeValue()
                            .addOnSuccessListener {
                                Log.d("Firebase", "Data berhasil dihapus")
                                Toast.makeText(appContext, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()

                            }
                            .addOnFailureListener { error ->
                                Log.e("Firebase", "Gagal menghapus data: ${error.message}")
                                Toast.makeText(appContext, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()

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
        return listPegawai.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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