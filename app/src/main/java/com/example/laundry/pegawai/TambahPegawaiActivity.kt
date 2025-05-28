package com.example.laundry.pegawai

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.laundry.R
import com.example.laundry.model_data.ModelPegawai
import com.google.firebase.database.FirebaseDatabase

class TambahPegawaiActivity : AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("pegawai")
    private lateinit var tvJudul: TextView
    private lateinit var etNamaPegawai: EditText
    private lateinit var etAlamatPegawai: EditText
    private lateinit var etNoHpPegawai: EditText
    private lateinit var etCabangPegawai: EditText
    private lateinit var btSimpan: Button

    private var idPegawai: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_pegawai)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Pegawai"
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
        etNamaPegawai = findViewById(R.id.etNamaPegawai)
        etAlamatPegawai = findViewById(R.id.etAlamatPegawai)
        etNoHpPegawai = findViewById(R.id.etNoHpPegawai)
        etCabangPegawai = findViewById(R.id.etCabangPegawai)
        btSimpan = findViewById(R.id.btSimpan)
    }

    private fun getData() {
        idPegawai = intent.getStringExtra("idPegawai").toString()
        val judul = intent.getStringExtra("Judul")
        val nama = intent.getStringExtra("namaPegawai")
        val alamat = intent.getStringExtra("alamatPegawai")
        val hp = intent.getStringExtra("noHpPegawai")
        val cabang = intent.getStringExtra("cabangPegawai")
        tvJudul.text = judul
        etNamaPegawai.setText(nama)
        etAlamatPegawai.setText(alamat)
        etNoHpPegawai.setText(hp)
        etCabangPegawai.setText(cabang)
        if (!tvJudul.text.equals("Tambah Pegawai")) {
            if (judul.equals("Edit Pegawai")) {
                mati()
                btSimpan.text = "Sunting"
            }
        } else {
            hidup()
            etNamaPegawai.requestFocus()
            btSimpan.text = "Simpan"
        }
    }

    private fun mati() {
        etNamaPegawai.isEnabled = false
        etAlamatPegawai.isEnabled = false
        etNoHpPegawai.isEnabled = false
        etCabangPegawai.isEnabled = false
    }

    private fun hidup() {
        etNamaPegawai.isEnabled = true
        etAlamatPegawai.isEnabled = true
        etNoHpPegawai.isEnabled = true
        etCabangPegawai.isEnabled = true
    }

    private fun update() {
        val pegawaiRef = database.getReference("pegawai").child(idPegawai)
        val data = ModelPegawai(
            idPegawai,
            etNamaPegawai.text.toString(),
            etAlamatPegawai.text.toString(),
            etNoHpPegawai.text.toString(),
            etCabangPegawai.text.toString()
        )
        val updateData = mutableMapOf<String, Any>()
        updateData["namaPegawai"] = data.namaPegawai.toString()
        updateData["alamatPegawai"] = data.alamatPegawai.toString()
        updateData["noHpPegawai"] = data.noHpPegawai.toString()
        updateData["cabangPegawai"] = data.cabangPegawai.toString()
        pegawaiRef.updateChildren(updateData).addOnSuccessListener {
            Toast.makeText(
                this@TambahPegawaiActivity,
                "Data Pegawai Berhasil Diperbarui",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
            .addOnFailureListener {
                Toast.makeText(
                    this@TambahPegawaiActivity,
                    "Data Pegawai Gagal Diperbarui",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun simpan() {
        val pegawaiBaru = myRef.push()
        val pegawaiID = pegawaiBaru.key ?: ""

        val data = ModelPegawai(
            pegawaiID,
            etNamaPegawai.text.toString(),
            etAlamatPegawai.text.toString(),
            etNoHpPegawai.text.toString(),
            etCabangPegawai.text.toString()
        )

        pegawaiBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this, getString(R.string.BerhasilSimpan), Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun cekValidasi() {
        val nama = etNamaPegawai.text.toString().trim()
        val alamat = etAlamatPegawai.text.toString().trim()
        val nohp = etNoHpPegawai.text.toString().trim()
        val cabang = etCabangPegawai.text.toString().trim()

        if (nama.isEmpty()) {
            etNamaPegawai.error = this.getString(R.string.NamaKosong)
            etNamaPegawai.requestFocus()
            Toast.makeText(
                this@TambahPegawaiActivity,
                getString(R.string.NamaKosong),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (alamat.isEmpty()) {
            etAlamatPegawai.error = this.getString(R.string.AlamatKosong)
            etAlamatPegawai.requestFocus()

            Toast.makeText(
                this@TambahPegawaiActivity,
                getString(R.string.AlamatKosong),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (nohp.isEmpty()) {
            etNoHpPegawai.error = this.getString(R.string.NomorTeleponKosong)
            etNoHpPegawai.requestFocus()
            Toast.makeText(
                this@TambahPegawaiActivity,
                getString(R.string.NomorTeleponKosong),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (cabang.isEmpty()) {
            etCabangPegawai.error = this.getString(R.string.CabangKosong)
            etCabangPegawai.requestFocus()
            Toast.makeText(
                this@TambahPegawaiActivity,
                getString(R.string.TambahPegawai),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (btSimpan.text.equals("Simpan")) {
            simpan()
        } else if (btSimpan.text.equals("Sunting")) {
            hidup()
            etNamaPegawai.requestFocus()
            btSimpan.text = "Perbarui"
        } else if (btSimpan.text.equals("Perbarui")) {
            update()
        }
    }
}