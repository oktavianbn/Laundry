package com.example.laundry.tambahan

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.laundry.R
import com.example.laundry.model_data.ModelTambahan
import com.google.firebase.database.FirebaseDatabase

class TambahTambahanActivity : AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("tambahan")
    private lateinit var tvJudul: TextView
    private lateinit var etNamaTambahan: EditText
    private lateinit var etHargaTambahan: EditText
    private lateinit var etCabangTambahan: EditText
    private lateinit var btSimpan: Button

    var idTambahan: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_tambahan)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Tambahan"
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
        etNamaTambahan = findViewById(R.id.etNamaTambahan)
        etHargaTambahan = findViewById(R.id.etHargaTambahan)
        etCabangTambahan = findViewById(R.id.etCabangTambahan)
        btSimpan = findViewById(R.id.btSimpan)
    }

    fun getData() {
        idTambahan = intent.getStringExtra("idTambahan").toString()
        val judul = intent.getStringExtra("Judul")
        val nama = intent.getStringExtra("namaTambahan")
        val harga = intent.getStringExtra("hargaTambahan")
        val cabang = intent.getStringExtra("cabangTambahan")
        tvJudul.text = judul
        etNamaTambahan.setText(nama)
        etHargaTambahan.setText(harga)
        etCabangTambahan.setText(cabang)
        if (!tvJudul.text.equals("Tambah Layanan Tambahan")) {
            if (judul.equals("Edit Layanan Tambahan")) {
                mati()
                btSimpan.text = "Sunting"
            }
        } else {
            hidup()
            etNamaTambahan.requestFocus()
        }
        btSimpan.text = "Simpan"
    }

    fun mati() {
        etNamaTambahan.isEnabled = false
        etHargaTambahan.isEnabled = false
        etCabangTambahan.isEnabled = false
    }

    fun hidup() {
        etNamaTambahan.isEnabled = true
        etHargaTambahan.isEnabled = true
        etCabangTambahan.isEnabled = true
    }

    fun update() {
        val tambahanRef = database.getReference("tambahan").child(idTambahan)
        val data = ModelTambahan(
            idTambahan,
            etNamaTambahan.text.toString(),
            etHargaTambahan.text.toString(),
            etCabangTambahan.text.toString(),
        )
        val updateData = mutableMapOf<String, Any>()
        updateData["namaTambahan"] = data.namaTambahan.toString()
        updateData["hargaTambahan"] = data.hargaTambahan.toString()
        updateData["cabangTambahan"] = data.cabangTambahan.toString()
        tambahanRef.updateChildren(updateData).addOnSuccessListener {
            Toast.makeText(
                this@TambahTambahanActivity,
                "Data Tambahan Berhasil Diperbarui",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
            .addOnFailureListener {
                Toast.makeText(
                    this@TambahTambahanActivity,
                    "Data Tambahan Gagal Diperbarui",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun simpan() {
        val tambahanBaru = myRef.push()
        val tambahanID = tambahanBaru.key ?: ""

        val data = ModelTambahan(
            tambahanID,
            etNamaTambahan.text.toString(),
            etHargaTambahan.text.toString(),
            etCabangTambahan.text.toString()
        )

        tambahanBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this, getString(R.string.BerhasilSimpan), Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun cekValidasi() {
        val nama = etNamaTambahan.text.toString().trim()
        val harga = etHargaTambahan.text.toString().trim()
        val cabang = etCabangTambahan.text.toString().trim()

        if (nama.isEmpty()) {
            etNamaTambahan.error = this.getString(R.string.NamaKosong)
            etNamaTambahan.requestFocus()
            Toast.makeText(
                this@TambahTambahanActivity,
                getString(R.string.NamaKosong),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (harga.isEmpty()) {
            etHargaTambahan.error = this.getString(R.string.AlamatKosong)
            etHargaTambahan.requestFocus()

            Toast.makeText(
                this@TambahTambahanActivity,
                getString(R.string.AlamatKosong),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (cabang.isEmpty()) {
            etCabangTambahan.error = this.getString(R.string.CabangKosong)
            etCabangTambahan.requestFocus()
            Toast.makeText(
                this@TambahTambahanActivity,
//                getString(R.string.tambahTambahan),
                "Tambahan Kosong",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (btSimpan.text.equals("Simpan")) {
            simpan()
        } else if (btSimpan.text.equals("Sunting")) {
            hidup()
            etNamaTambahan.requestFocus()
            btSimpan.text = "Perbarui"
        } else if (btSimpan.text.equals("Perbarui")) {
            update()
        }
    }

}