package com.example.laundry.pelanggan

import android.os.Bundle
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

    private var idPelanggan: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_pelanggan)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Pelanggan"
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
        etNamaPelanggan = findViewById(R.id.etNamaPelanggan)
        etAlamatPelanggan = findViewById(R.id.etAlamatPelanggan)
        etNoHpPelanggan = findViewById(R.id.etNoHpPelanggan)
        btSimpan = findViewById(R.id.btSimpan)
    }

    private fun simpan() {
        val pelangganBaru = myRef.push()
        val pelangganID = pelangganBaru.key ?: ""

        val data = ModelPelanggan(
            pelangganID,
            etNamaPelanggan.text.toString(),
            etAlamatPelanggan.text.toString(),
            etNoHpPelanggan.text.toString(),
        )

        pelangganBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this, getString(R.string.BerhasilSimpan), Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getData() {
        idPelanggan = intent.getStringExtra("idPelanggan").toString()
        val judul = intent.getStringExtra("Judul")
        val nama = intent.getStringExtra("namaPelanggan")
        val alamat = intent.getStringExtra("alamatPelanggan")
        val hp = intent.getStringExtra("noHpPelanggan")
//        val  cabang=intent.getStringExtra("cabangPelanggan")
        tvJudul.text = judul
        etNamaPelanggan.setText(nama)
        etAlamatPelanggan.setText(alamat)
        etNoHpPelanggan.setText(hp)
//        etCabangPelanggan.setText(cabang)
        if (!tvJudul.text.equals("Tambah Pelanggan")) {
            if (judul.equals("Edit Pelanggan")) {
                mati()
                btSimpan.text = "Sunting"
            }
        } else {
            hidup()
            etNamaPelanggan.requestFocus()
            btSimpan.text = "Simpan"
        }
    }

    private fun mati() {
        etNamaPelanggan.isEnabled = false
        etAlamatPelanggan.isEnabled = false
        etNoHpPelanggan.isEnabled = false
//        etCabangPelanggan.isEnabled=false
    }

    private fun hidup() {
        etNamaPelanggan.isEnabled = true
        etAlamatPelanggan.isEnabled = true
        etNoHpPelanggan.isEnabled = true
//        etCabangPelanggan.isEnabled=true
    }

    private fun update() {
        val pelangganRef = database.getReference("pelanggan").child(idPelanggan)
        val data = ModelPelanggan(
            idPelanggan,
            etNamaPelanggan.text.toString().trim(),
            etAlamatPelanggan.text.toString().trim(),
            etNoHpPelanggan.text.toString().trim(),
//            etCabangPelanggan.text.toString()
        )
        val updateData = mutableMapOf<String, Any>()
        updateData["namaPelanggan"] = data.namaPelanggan.toString()
        updateData["alamatPelanggan"] = data.alamatPelanggan.toString()
        updateData["noHpPelanggan"] = data.noHpPelanggan.toString()
//        updateData["cabangPelanggan"]=data.cabangPelanggan.toString()
        pelangganRef.updateChildren(updateData).addOnSuccessListener {
            Toast.makeText(
                this@TambahPelangganActivity,
                "Data Pelanggan Berhasil Diperbarui",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
            .addOnFailureListener {
                Toast.makeText(
                    this@TambahPelangganActivity,
                    "Data Pelanggan Gagal Diperbarui",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun cekValidasi() {
        val nama = etNamaPelanggan.text.toString()
        val alamat = etAlamatPelanggan.text.toString()
        val nohp = etNoHpPelanggan.text.toString()
//        val cabang = etCabangPelanggan.text.toString().trim()

        if (nama.isEmpty()) {
            etNamaPelanggan.error = this.getString(R.string.NamaKosong)
            etNamaPelanggan.requestFocus()
            Toast.makeText(
                this@TambahPelangganActivity,
                getString(R.string.NamaKosong),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (alamat.isEmpty()) {
            etAlamatPelanggan.error = this.getString(R.string.AlamatKosong)
            etAlamatPelanggan.requestFocus()
            Toast.makeText(
                this@TambahPelangganActivity,
                getString(R.string.AlamatKosong),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (nohp.isEmpty()) {
            etNoHpPelanggan.error = this.getString(R.string.NomorTeleponKosong)
            etNoHpPelanggan.requestFocus()
            Toast.makeText(
                this@TambahPelangganActivity,
                getString(R.string.NomorTeleponKosong),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
//        if (cabang.isEmpty()) {
//            etCabangPelanggan.error = this.getString(R.string.CabangKosong)
//            etCabangPelanggan.requestFocus()
//            Toast.makeText(
//                this@TambahPelangganActivity,
//                getString(R.string.TambahPelanggan),
//                Toast.LENGTH_SHORT
//            ).show()
//            return
//        }
        if (btSimpan.text.equals("Simpan")) {
            simpan()
        } else if (btSimpan.text.equals("Sunting")) {
            hidup()
            etNamaPelanggan.requestFocus()
            btSimpan.text = "Perbarui"
        } else if (btSimpan.text.equals("Perbarui")) {
            update()
        }
    }
}
