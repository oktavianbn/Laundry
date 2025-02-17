package com.example.laundry.pegawai

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.R
import com.example.laundry.adapter.AdapterDataPegawai
import com.example.laundry.model_data.ModelPegawai
import com.example.laundry.model_data.ModelPelanggan
import com.example.laundry.pegawai.TambahPegawaiActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DataPegawaiActivity : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("pegawai")
    lateinit var  fabDataPegawaiTambah: FloatingActionButton
    lateinit var rvDataPegawai : RecyclerView
    lateinit var pegawaiList: ArrayList<ModelPegawai>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        init()
        fabTambah()
        getDatapegawai()
        setContentView(R.layout.activity_data_pegawai)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun init(){
        rvDataPegawai = findViewById(R.id.rvDataPegawai)
        fabDataPegawaiTambah = findViewById(R.id.fabDataPegawaiTambah)
    }
    fun fabTambah(){
        fabDataPegawaiTambah.setOnClickListener {
            val intent = Intent(this, TambahPegawaiActivity::class.java)
            startActivity(intent)
        }

    }
    fun getDatapegawai() {
        val query = myRef.orderByChild("pegawai").limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    pegawaiList.clear()
                    for (data in snapshot.children) {
                        val pegawai = data.getValue(ModelPegawai::class.java)
                        pegawaiList.add(pegawai!!)
                    }
                    val adapter = AdapterDataPegawai(pegawaiList)
                    rvDataPegawai.adapter = adapter
                    adapter.notifyDataSetChanged()

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DataPegawaiActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        }
        )
    }
}