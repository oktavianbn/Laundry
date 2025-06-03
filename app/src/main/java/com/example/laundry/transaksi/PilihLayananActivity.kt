package com.example.laundry.transaksi

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.R
import com.example.laundry.adapter.AdapterPilihLayanan
import com.example.laundry.model_data.ModelLayanan
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PilihLayananActivity : AppCompatActivity() {
    val db = FirebaseDatabase.getInstance()
    val ref = db.getReference("layanan")
    lateinit var rvPilihLayanan: RecyclerView
    lateinit var layananList: ArrayList<ModelLayanan>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilih_layanan)
        init()
        getData()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun init(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Transaksi"
        rvPilihLayanan = findViewById(R.id.rvPilihLayanan)
        rvPilihLayanan.layoutManager = LinearLayoutManager(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        Log.d("TAG", "onSupportNavigateUp:run")
        finish()
        return true
    }

    private fun getData() {
        val query = ref.orderByChild("layanan").limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    layananList = ArrayList()
                    for (data in snapshot.children) {
                        val layanan = data.getValue(ModelLayanan::class.java)
                        layananList.add(layanan!!)
                    }

                    val adapter = AdapterPilihLayanan(layananList) { selectedLayanan ->
                        // kirim data kembali ke activity sebelumnya
                        val resultIntent = Intent()
                        resultIntent.putExtra("idLayanan", selectedLayanan.idLayanan)
                        resultIntent.putExtra("namaLayanan", selectedLayanan.namaLayanan)
                        resultIntent.putExtra("hargaLayanan", selectedLayanan.hargaLayanan)
                        setResult(RESULT_OK, resultIntent)
                        finish()
                    }

                    rvPilihLayanan.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@PilihLayananActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}