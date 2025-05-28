package com.example.laundry.layanan

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.laundry.R
import com.example.laundry.model_data.ModelLayanan
import com.google.firebase.database.FirebaseDatabase

class TambahLayananActivity : AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("layanan")
    private lateinit var tvJudul: TextView
    private lateinit var etNamaLayanan: EditText
    private lateinit var etHargaLayanan: EditText
    private lateinit var etCabangLayanan: EditText
    private lateinit var btSimpan: Button

    var idLayanan: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_layanan)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Layanan"
        init()
        getData()
        btSimpan.setOnClickListener {
            cekValidasi()
        }
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

    private fun init() {
        tvJudul = findViewById(R.id.tvJudul)
        etNamaLayanan = findViewById(R.id.etNamaLayanan)
        etHargaLayanan = findViewById(R.id.etHargaLayanan)
        etCabangLayanan = findViewById(R.id.etCabangLayanan)
        btSimpan = findViewById(R.id.btSimpan)
    }

    fun getData() {
        idLayanan = intent.getStringExtra("idLayanan").toString()
        val judul = intent.getStringExtra("Judul")
        val nama = intent.getStringExtra("namaLayanan")
        val harga = intent.getStringExtra("hargaLayanan")
        val cabang = intent.getStringExtra("cabangLayanan")
        tvJudul.text = judul
        etNamaLayanan.setText(nama)
        etHargaLayanan.setText(harga)
        etCabangLayanan.setText(cabang)
        if (!tvJudul.text.equals("Tambah Layanan")) {
            if (judul.equals("Edit Layanan")) {
                mati()
                btSimpan.text = "Sunting"
            }
        } else {
            hidup()
            etNamaLayanan.requestFocus()
            btSimpan.text = "Simpan"
        }
    }

    fun mati() {
        etNamaLayanan.isEnabled = false
        etHargaLayanan.isEnabled = false
        etCabangLayanan.isEnabled = false
    }

    fun hidup() {
        etNamaLayanan.isEnabled = true
        etHargaLayanan.isEnabled = true
        etCabangLayanan.isEnabled = true
    }

    fun update() {
        val layananRef = database.getReference("layanan").child(idLayanan)
        val data = ModelLayanan(
            idLayanan,
            etNamaLayanan.text.toString(),
            etHargaLayanan.text.toString(),
            etCabangLayanan.text.toString()
        )
        val updateData = mutableMapOf<String, Any>()
        updateData["namaLayanan"] = data.namaLayanan.toString()
        updateData["HargaLayanan"] = data.hargaLayanan.toString()
        updateData["cabangLayanan"] = data.cabangLayanan.toString()
        layananRef.updateChildren(updateData).addOnSuccessListener {
            Toast.makeText(
                this@TambahLayananActivity,
                "Data Layanan Berhasil Diperbarui",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
            .addOnFailureListener {
                Toast.makeText(
                    this@TambahLayananActivity,
                    "Data Layanan Gagal Diperbarui",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun cekValidasi() {
        val nama = etNamaLayanan.text.toString().trim()
        val alamat = etHargaLayanan.text.toString().trim()
        val cabang = etCabangLayanan.text.toString().trim()

        if (nama.isEmpty()) {
            etNamaLayanan.error = getString(R.string.NamaKosong)
            etNamaLayanan.requestFocus()
            return
        }
        if (alamat.isEmpty()) {
            etHargaLayanan.error = getString(R.string.HargaKosong)
            etHargaLayanan.requestFocus()
            return
        }
        if (cabang.isEmpty()) {
            etCabangLayanan.error = getString(R.string.CabangKosong)
            etCabangLayanan.requestFocus()
            return
        }
        if (btSimpan.text.equals("Simpan")) {
            simpan()
        } else if (btSimpan.text.equals("Sunting")) {
            hidup()
            etNamaLayanan.requestFocus()
            btSimpan.text = "Perbarui"
        } else if (btSimpan.text.equals("Perbarui")) {
            update()
        }
    }

    private fun simpan() {
        val layananBaru = myRef.push()
        val layananID = layananBaru.key ?: ""

        val data = ModelLayanan(
            layananID,
            etNamaLayanan.text.toString(),
            etHargaLayanan.text.toString(),
            etCabangLayanan.text.toString()
        )

        layananBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this, getString(R.string.BerhasilSimpan), Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }
}