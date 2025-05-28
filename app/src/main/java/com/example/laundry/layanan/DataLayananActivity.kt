package com.example.laundry.layanan

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
import com.example.laundry.adapter.AdapterDataLayanan
import com.example.laundry.model_data.ModelLayanan
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DataLayananActivity : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("layanan")
    lateinit var fabDataLayananTambah: FloatingActionButton
    lateinit var rvDataLayanan: RecyclerView
    lateinit var layananList: ArrayList<ModelLayanan>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_layanan)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Layanan"
        init()
        fabTambah()
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rvDataLayanan.layoutManager = layoutManager
        layananList = arrayListOf<ModelLayanan>()
        getDataLayanan()
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

    fun init() {
        rvDataLayanan = findViewById(R.id.rvDataLayanan)
        fabDataLayananTambah = findViewById(R.id.fabDataLayananTambah)
    }

    fun fabTambah() {
        fabDataLayananTambah.setOnClickListener {
            val intent = Intent(this, TambahLayananActivity::class.java)
            intent.putExtra("Judul", getString(R.string.TambahLayanan))
            intent.putExtra("idLayanan", "")
            intent.putExtra("namaLayanan", "")
            intent.putExtra("hargaLayanan", "")
            intent.putExtra("cabangLayanan", "")
            startActivity(intent)
        }
    }

    fun getDataLayanan() {
        val query = myRef.orderByChild("layanan").limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    layananList.clear()
                    for (data in snapshot.children) {
                        val layanan = data.getValue(ModelLayanan::class.java)
                        layananList.add(layanan!!)
                    }
                    val adapter = AdapterDataLayanan(layananList)
                    rvDataLayanan.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DataLayananActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        }
        )
    }
}