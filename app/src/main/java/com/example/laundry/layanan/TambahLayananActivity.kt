package com.example.laundry.layanan

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.laundry.R
import com.example.laundry.model_data.ModelPegawai
import com.google.firebase.database.FirebaseDatabase

class TambahLayananActivity : AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("pegawai")
    private lateinit var tvJudul: TextView
    private lateinit var etNamaPegawai: EditText
    private lateinit var etAlamatPegawai: EditText
    private lateinit var etNoHpPegawai: EditText
    private lateinit var etCabangPegawai: EditText
    private lateinit var btSimpan: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_layanan)

        init()
        btSimpan.setOnClickListener{
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
        etNamaPegawai = findViewById(R.id.etNamaPegawai)
        etAlamatPegawai = findViewById(R.id.etAlamatPegawai)
        etNoHpPegawai = findViewById(R.id.etNoHpPegawai)
        etCabangPegawai = findViewById(R.id.etCabangPegawai)
        btSimpan = findViewById(R.id.btSimpan)
    }

    private fun cekValidasi() {
        val nama = etNamaPegawai.text.toString().trim()
        val alamat = etAlamatPegawai.text.toString().trim()
        val noHp = etNoHpPegawai.text.toString().trim()
        val cabang = etCabangPegawai.text.toString().trim()

        if (nama.isEmpty()) {
            etNamaPegawai.error = getString(R.string.NamaKosong)
            etNamaPegawai.requestFocus()
            return
        }
        if (alamat.isEmpty()) {
            etAlamatPegawai.error = getString(R.string.AlamatKosong)
            etAlamatPegawai.requestFocus()
            return
        }
        if (noHp.isEmpty()) {
            etNoHpPegawai.error = getString(R.string.NomorTeleponKosong)
            etNoHpPegawai.requestFocus()
            return
        }
        if (cabang.isEmpty()) {
            etCabangPegawai.error = getString(R.string.CabangKosong)
            etCabangPegawai.requestFocus()
            return
        }
        simpan()
    }

    private fun simpan() {
        val pegawaiBaru = myRef.push()
        val pegawaiID = pegawaiBaru.key ?: ""

        Log.d("FirebaseDebug", "Mempersiapkan data Pegawai ID: $pegawaiID")

        val data = ModelPegawai(
            pegawaiID,
            etNamaPegawai.text.toString(),
            etAlamatPegawai.text.toString(),
            etNoHpPegawai.text.toString(),
            etCabangPegawai.text.toString()
        )

        Log.d("FirebaseDebug", "Menyimpan data ke Firebase...")

        pegawaiBaru.setValue(data)
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