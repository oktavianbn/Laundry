package com.example.laundry.transaksi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.R
import com.example.laundry.adapter.AdapterPilihLayananTambahan
import com.example.laundry.model_data.ModelTambahan

class TransakasiActivity : AppCompatActivity() {
    lateinit var btnPilihPelanggan: Button
    lateinit var btnPilihLayanan: Button
    lateinit var btnTambahan: Button
    lateinit var btnProses: Button
    lateinit var rcLayananTambahan: RecyclerView
    lateinit var tvNamaPelanggan: TextView
    lateinit var tvNoHpPelanggan: TextView
    lateinit var tvNamaLayanan: TextView
    lateinit var tvHargaLayanan: TextView

    val listTambahan = ArrayList<ModelTambahan>()
    val adapter = AdapterPilihLayananTambahan(listTambahan)



    private val pilihPelangganLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val nama = data?.getStringExtra("namaPelanggan")
            val hp = data?.getStringExtra("noHpPelanggan")
            tvNamaPelanggan.text = nama
            tvNoHpPelanggan.text = hp
        }
    }

    private val pilihLayananLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val nama = data?.getStringExtra("namaLayanan")
            val hp = data?.getStringExtra("hargaLayanan")
            tvNamaLayanan.text = nama
            tvHargaLayanan.text = hp
        }
    }
    private val pilihTambahanLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            if (data != null) {
                val id = data.getStringExtra("id") ?: ""
                val nama = data.getStringExtra("nama") ?: ""
                val harga = data.getStringExtra("harga") ?: ""

                val tambahan = ModelTambahan(id, nama, harga)
                listTambahan.add(tambahan)
                adapter.notifyItemInserted(listTambahan.size - 1)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transakasi)

        init()
        navigasi()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Transaksi"
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

    fun init() {
        btnPilihPelanggan = findViewById(R.id.btnPilihPelanggan)
        btnPilihLayanan = findViewById(R.id.btnPilihLayanan)
        btnTambahan = findViewById(R.id.btnTambahan)
        btnProses = findViewById(R.id.btnProses)
        rcLayananTambahan = findViewById(R.id.rcLayananTambahan)
        tvNamaPelanggan = findViewById(R.id.tvNamaPelanggan)
        tvNoHpPelanggan = findViewById(R.id.tvNoHpPelanggan)
        tvNamaLayanan = findViewById(R.id.tvNamaLayanan)
        tvHargaLayanan = findViewById(R.id.tvHargaLayanan)


        rcLayananTambahan.layoutManager = LinearLayoutManager(this)
        rcLayananTambahan.adapter = adapter

    }

    fun navigasi() {
        btnPilihPelanggan.setOnClickListener {
            val intent = Intent(this, PilihPelangganActivity::class.java)
            pilihPelangganLauncher.launch(intent)
        }
        btnPilihLayanan.setOnClickListener {
            val intent = Intent(this, PilihLayananActivity::class.java)
            pilihLayananLauncher.launch(intent)
        }
        btnTambahan.setOnClickListener {
            val intent = Intent(this, PilihLayananTambahanActivity::class.java)
            pilihTambahanLauncher.launch(intent)
            startActivity(intent)
        }

    }
}