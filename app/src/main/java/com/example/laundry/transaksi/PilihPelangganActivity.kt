package com.example.laundry.transaksi

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.R
import com.example.laundry.adapter.AdapterPilihDataPelanggan
import com.example.laundry.model_data.ModelPelanggan
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PilihPelangganActivity : AppCompatActivity() {
    private val db = FirebaseDatabase.getInstance()
    private val ref = db.getReference("pelanggan")
    lateinit var rvPilihDataPelanggan: RecyclerView
    lateinit var pelangganList: ArrayList<ModelPelanggan>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilih_pelanggan)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Transaksi"
        rvPilihDataPelanggan = findViewById(R.id.rvPilihDataPelanggan)
        rvPilihDataPelanggan.layoutManager = LinearLayoutManager(this)
        getDataPelanggan()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun getDataPelanggan() {
        val query = ref.orderByChild("pelanggan").limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    pelangganList = ArrayList()
                    for (data in snapshot.children) {
                        val pelanggan = data.getValue(ModelPelanggan::class.java)
                        pelangganList.add(pelanggan!!)
                    }

                    val adapter = AdapterPilihDataPelanggan(pelangganList) { selectedPelanggan ->
                        // kirim data kembali ke activity sebelumnya
                        val resultIntent = Intent()
                        resultIntent.putExtra("idPelanggan", selectedPelanggan.idPelanggan)
                        resultIntent.putExtra("namaPelanggan", selectedPelanggan.namaPelanggan)
                        resultIntent.putExtra("noHpPelanggan", selectedPelanggan.noHpPelanggan)
                        setResult(RESULT_OK, resultIntent)
                        finish()
                    }

                    rvPilihDataPelanggan.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@PilihPelangganActivity, error.message, Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}