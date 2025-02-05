package com.example.laundry.pegawai

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.laundry.R
import com.example.laundry.pelanggan.TambahPelangganActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DataPegawaiActivity : AppCompatActivity() {
    lateinit var  fabDataPegawaiTambah: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_pegawai)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun init(){
        fabDataPegawaiTambah = findViewById(R.id.fabDataPegawaiTambah)
    }
    fun fabTambah(){
        fabDataPegawaiTambah.setOnClickListener {
            val intent = Intent(this, TambahPegawaiActivity::class.java)
            startActivity(intent)
        }

    }
}