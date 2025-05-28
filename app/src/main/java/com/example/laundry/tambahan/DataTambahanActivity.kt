package com.example.laundry.tambahan

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
import com.example.laundry.adapter.AdapterDataTambahan
import com.example.laundry.model_data.ModelTambahan
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DataTambahanActivity : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("tambahan")
    lateinit var fabDataTambahanTambah: FloatingActionButton
    lateinit var rvDataTambahan: RecyclerView
    lateinit var tambahanList: ArrayList<ModelTambahan>
//    lateinit var appContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_tambahan)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Tambahan"
        init()
        fabTambah()
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rvDataTambahan.layoutManager = layoutManager
        rvDataTambahan.setHasFixedSize(true)
        tambahanList = arrayListOf<ModelTambahan>()
        getData()
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
        rvDataTambahan = findViewById(R.id.rvDataTambahan)
        fabDataTambahanTambah = findViewById(R.id.fabDataTambahanTambah)
    }

    fun fabTambah() {
        fabDataTambahanTambah.setOnClickListener {
            val intent = Intent(this, TambahTambahanActivity::class.java)
            intent.putExtra("Judul", "Tambah Layanan Tambahan")
            intent.putExtra("idTambahan", "")
            intent.putExtra("namaTambahan", "")
            intent.putExtra("noHpTambahan", "")
            intent.putExtra("alamatTambahan", "")
            intent.putExtra("cabangTambahan", "")
//            appContext.startActivity(intent)
            startActivity(intent)
        }
    }

    fun getData() {
        val query = myRef.orderByChild("Tambahan").limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    tambahanList.clear()
                    for (data in snapshot.children) {
                        val tambahan = data.getValue(ModelTambahan::class.java)
                        tambahanList.add(tambahan!!)
                    }
                    val adapter = AdapterDataTambahan(tambahanList)
                    rvDataTambahan.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DataTambahanActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        }
        )
    }

}