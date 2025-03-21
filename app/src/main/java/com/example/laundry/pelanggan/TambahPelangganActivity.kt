package com.example.laundry.pelanggan

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.laundry.R
import com.example.laundry.model_data.ModelPelanggan
import com.google.firebase.database.FirebaseDatabase

class TambahPelangganActivity : AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("pelanggan")
    private lateinit var tvJudul: TextView
    private lateinit var etNamaPelanggan: EditText
    private lateinit var etAlamatPelanggan: EditText
    private lateinit var etNoHpPelanggan: EditText
    private lateinit var btSimpan: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_pelanggan)
        init()
        btSimpan.setOnClickListener {
            cekValidasi()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun init() {
        tvJudul = findViewById(R.id.tvJudul)
        etNamaPelanggan = findViewById(R.id.etNamaPelanggan)
        etAlamatPelanggan = findViewById(R.id.etAlamatPelanggan)
        etNoHpPelanggan = findViewById(R.id.etNoHpPelanggan)
        btSimpan = findViewById(R.id.btSimpan)
    }

    private fun cekValidasi() {
        val nama = etNamaPelanggan.text.toString().trim()
        val alamat = etAlamatPelanggan.text.toString().trim()
        val noHp = etNoHpPelanggan.text.toString().trim()


        if (nama.isEmpty()) {
            etNamaPelanggan.error = getString(R.string.NamaKosong)
            etNamaPelanggan.requestFocus()
            return
        }
        if (alamat.isEmpty()) {
            etAlamatPelanggan.error = getString(R.string.AlamatKosong)
            etAlamatPelanggan.requestFocus()
            return
        }
        if (noHp.isEmpty()) {
            etNoHpPelanggan.error = getString(R.string.NomorTeleponKosong)
            etNoHpPelanggan.requestFocus()
            return
        }
        simpan()
    }

    private fun simpan() {
        val pelangganBaru = myRef.push()
        val pelangganID = pelangganBaru.key ?: ""

        Log.d("FirebaseDebug", "Mempersiapkan data pelanggan ID: $pelangganID")

        val data = ModelPelanggan(
            pelangganID,
            etNamaPelanggan.text.toString(),
            etAlamatPelanggan.text.toString(),
            etNoHpPelanggan.text.toString(),
        )

        pelangganBaru.setValue(data)
            .addOnSuccessListener {
                Log.d("FirebaseDebug", "Data berhasil disimpan!")
                Toast.makeText(this, getString(R.string.BerhasilSimpan), Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { error ->
                Log.e("FirebaseDebug", "Gagal menyimpan data: ${error.message}")
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
