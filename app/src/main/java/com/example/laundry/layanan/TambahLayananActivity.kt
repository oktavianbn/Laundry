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
import com.example.laundry.model_data.ModelLayanan
import com.google.firebase.database.FirebaseDatabase

class TambahLayananActivity : AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("layanan")
    private lateinit var tvJudul: TextView
    private lateinit var etNamaLayanan: EditText
    private lateinit var etAlamatLayanan: EditText
    private lateinit var etNoHpLayanan: EditText
    private lateinit var etCabangLayanan: EditText
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
        etNamaLayanan = findViewById(R.id.etNamaLayanan)
        etAlamatLayanan = findViewById(R.id.etHargaLayanan)
        etCabangLayanan = findViewById(R.id.etCabangLayanan)
        btSimpan = findViewById(R.id.btSimpan)
    }

    private fun cekValidasi() {
        val nama = etNamaLayanan.text.toString().trim()
        val alamat = etAlamatLayanan.text.toString().trim()
        val noHp = etNoHpLayanan.text.toString().trim()
        val cabang = etCabangLayanan.text.toString().trim()

        if (nama.isEmpty()) {
            etNamaLayanan.error = getString(R.string.NamaKosong)
            etNamaLayanan.requestFocus()
            return
        }
        if (alamat.isEmpty()) {
            etAlamatLayanan.error = getString(R.string.AlamatKosong)
            etAlamatLayanan.requestFocus()
            return
        }
        if (noHp.isEmpty()) {
            etNoHpLayanan.error = getString(R.string.NomorTeleponKosong)
            etNoHpLayanan.requestFocus()
            return
        }
        if (cabang.isEmpty()) {
            etCabangLayanan.error = getString(R.string.CabangKosong)
            etCabangLayanan.requestFocus()
            return
        }
        simpan()
    }

    private fun simpan() {
        val LayananBaru = myRef.push()
        val LayananID = LayananBaru.key ?: ""

        Log.d("FirebaseDebug", "Mempersiapkan data Layanan ID: $LayananID")

        val data = ModelLayanan(
            LayananID,
            etNamaLayanan.text.toString(),
            etAlamatLayanan.text.toString(),
            etNoHpLayanan.text.toString(),
            etCabangLayanan.text.toString()
        )

        Log.d("FirebaseDebug", "Menyimpan data ke Firebase...")

        LayananBaru.setValue(data)
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