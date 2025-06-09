package com.example.laundry.cabang

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.laundry.MainActivity
import com.example.laundry.R
import com.example.laundry.model_data.ModelCabang
import com.google.firebase.database.FirebaseDatabase

class TambahCabangActivity : AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("cabang")
    private lateinit var etNamaCabang: EditText
    private lateinit var etAlamatCabang: EditText
    private lateinit var btSimpan: Button
    private lateinit var btnBack: ImageButton
    private lateinit var tvJudul: TextView
    private lateinit var tvTitle: TextView
    var idCabang: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_cabang)
        supportActionBar?.hide()
        init()
        btSimpan.setOnClickListener {
            cekValidasi()
        }
        getData()
        toolbar()
        tvTitle.text = getString(R.string.Cabang)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun init() {
        etNamaCabang = findViewById(R.id.etNamaCabang)
        etAlamatCabang = findViewById(R.id.etAlamatCabang)
        btSimpan = findViewById(R.id.btSimpan)
        tvJudul = findViewById(R.id.tvJudul)
        btnBack = findViewById(R.id.btnBack)
        tvTitle = findViewById(R.id.tvTitle)
    }

    fun toolbar() {
        btnBack.setOnClickListener {
            finish()
        }
    }

    fun getData() {
        idCabang = intent.getStringExtra("idCabang").toString()
        val judul = intent.getStringExtra("Judul")
        val nama = intent.getStringExtra("namaCabang")
        val alamat = intent.getStringExtra("alamatCabang")
        tvJudul.text = judul
        etNamaCabang.setText(nama)
        etAlamatCabang.setText("$alamat")
        if (!tvJudul.text.equals("Tambah Cabang")) {
            if (judul.equals("Edit Cabang")) {
                mati()
                btSimpan.text = "Sunting"
            }
        } else {
            hidup()
            etNamaCabang.requestFocus()
            btSimpan.text = "Simpan"
        }
    }

    fun mati() {
        etNamaCabang.isEnabled = false
        etAlamatCabang.isEnabled = false
    }

    fun hidup() {
        etNamaCabang.isEnabled = true
        etAlamatCabang.isEnabled = true
    }

    fun update() {
        val cabangRef = database.getReference("cabang").child(idCabang)
        val data = ModelCabang(
            idCabang,
            etNamaCabang.text.toString(),
            etAlamatCabang.text.toString()
        )
        val updateData = mutableMapOf<String, Any>()
        updateData["namaCabang"] = data.namaCabang.toString()
        updateData["alamatCabang"] = data.alamatCabang.toString()
        cabangRef.updateChildren(updateData).addOnSuccessListener {
            Toast.makeText(
                this@TambahCabangActivity,
                "Data Cabang Berhasil Diperbarui",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
            .addOnFailureListener {
                Toast.makeText(
                    this@TambahCabangActivity,
                    "Data Cabang Gagal Diperbarui",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun simpan() {
        val CabangBaru = myRef.push()
        val CabangID = CabangBaru.key ?: ""

        val data = ModelCabang(
            CabangID,
            etNamaCabang.text.toString(),
            etAlamatCabang.text.toString()
        )

        CabangBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this, getString(R.string.BerhasilSimpan), Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun cekValidasi() {
        val nama = etNamaCabang.text.toString().trim()
        val alamat = etAlamatCabang.text.toString().trim()

        if (nama.isEmpty()) {
            etNamaCabang.error = this.getString(R.string.NamaKosong)
            etNamaCabang.requestFocus()
            Toast.makeText(
                this@TambahCabangActivity,
                getString(R.string.NamaKosong),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (alamat.isEmpty()) {
            etAlamatCabang.error = this.getString(R.string.AlamatKosong)
            etAlamatCabang.requestFocus()

            Toast.makeText(
                this@TambahCabangActivity,
                getString(R.string.AlamatKosong),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (btSimpan.text.equals("Simpan")) {
            simpan()
        } else if (btSimpan.text.equals("Sunting")) {
            hidup()
            etNamaCabang.requestFocus()
            btSimpan.text = "Perbarui"
        } else if (btSimpan.text.equals("Perbarui")) {
            update()
        }
    }

}