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
import com.example.laundry.adapter.AdapterPilihLayananTambahan
import com.example.laundry.model_data.ModelLayanan
import com.example.laundry.model_data.ModelTambahan
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PilihLayananTambahanActivity : AppCompatActivity() {
    val db = FirebaseDatabase.getInstance()
    val ref = db.getReference("layanan")
    lateinit var rvPilihLayananTambahan: RecyclerView
    lateinit var tambahanList: ArrayList<ModelTambahan>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilih_layanan_tambahan)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Transaksi"
        getData()
        rvPilihLayananTambahan = findViewById(R.id.rvPilihLayananTambahan)
        rvPilihLayananTambahan.layoutManager = LinearLayoutManager(this)
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

    private fun getData() {
        val query = ref.orderByChild("tambahan").limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    tambahanList = ArrayList()
                    for (data in snapshot.children) {
                        val tambahan = data.getValue(ModelTambahan::class.java)
                        tambahanList.add(tambahan!!)
                    }

                    val adapter = AdapterPilihLayananTambahan(tambahanList) { selectedTambahan ->
                        // kirim data kembali ke activity sebelumnya
                        val resultIntent = Intent()
                        resultIntent.putExtra("idLayanan", selectedTambahan.idTambahan)
                        resultIntent.putExtra("namaLayanan", selectedTambahan.namaTambahan)
                        resultIntent.putExtra("hargaLayanan", selectedTambahan.hargaTambahan)
                        setResult(RESULT_OK, resultIntent)
                        finish()
                    }

                    rvPilihLayananTambahan.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@PilihLayananTambahanActivity, error.message, Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

}