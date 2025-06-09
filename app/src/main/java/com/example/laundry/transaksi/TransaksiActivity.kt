package com.example.laundry.transaksi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.R
import com.example.laundry.adapter.AdapterPilihLayananTambahan
import com.example.laundry.model_data.ModelTambahan

class TransaksiActivity : AppCompatActivity() {
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
    lateinit var adapter: AdapterPilihLayananTambahan

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
            val harga = data?.getIntExtra("hargaLayanan",0)
            tvNamaLayanan.text = nama
            tvHargaLayanan.text = harga.toString()
        }
    }

    private val pilihTambahanLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d("TransaksiActivity", "Result received - resultCode: ${result.resultCode}")
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val id = data?.getStringExtra("idTambahan") ?: ""
            val nama = data?.getStringExtra("namaTambahan") ?: ""
            val harga = data?.getIntExtra("hargaTambahan",0)

            Log.d("TransaksiActivity", "Data diterima: id=$id, nama=$nama, harga=$harga")

            if (nama.isNotEmpty()) {
                Toast.makeText(this, "Diterima: $nama - $harga", Toast.LENGTH_SHORT).show()

                val tambahan = ModelTambahan(id, nama, harga)

                // Cek duplikasi berdasarkan ID
                val existingItem = listTambahan.find { it.idTambahan == id }
                if (existingItem == null) {
                    listTambahan.add(tambahan)
                    adapter.notifyItemInserted(listTambahan.size - 1)
                    Log.d("TransaksiActivity", "Item added to list. Total items: ${listTambahan.size}")
                } else {
                    Toast.makeText(this, "Item sudah ada: $nama", Toast.LENGTH_SHORT).show()
                    Log.d("TransaksiActivity", "Duplicate item ignored: $nama")
                }
            } else {
                Log.e("TransaksiActivity", "Received empty data")
            }
        } else {
            Log.d("TransaksiActivity", "Result not OK")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaksi)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Transaksi"
        init()
        navigasi()
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

        // Inisialisasi adapter dengan parameter yang benar
        adapter = AdapterPilihLayananTambahan(
            items = listTambahan, // MutableList
            isInTransaction = true, // true untuk activity transaksi (tombol hapus visible)
            onDeleteClick = { tambahan, position ->
                Log.d("TransaksiActivity", "Delete requested for: ${tambahan.namaTambahan} at position: $position")

                // Hapus dari list
                if (position >= 0 && position < listTambahan.size) {
                    val removedItem = listTambahan.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    adapter.notifyItemRangeChanged(position, listTambahan.size)

                    Toast.makeText(this, "Item dihapus: ${removedItem.namaTambahan}", Toast.LENGTH_SHORT).show()
                    Log.d("TransaksiActivity", "Item removed: ${removedItem.namaTambahan}, remaining: ${listTambahan.size}")
                } else {
                    Log.e("TransaksiActivity", "Invalid position for deletion: $position")
                }
            }
        )

        rcLayananTambahan.adapter = adapter

        Log.d("TransaksiActivity", "Adapter initialized with isInTransaction: true")
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
        }
        btnProses.setOnClickListener {
            val intent = Intent(this, ProsesPembayaranActivity::class.java)
            intent.putExtra("namaPelanggan", tvNamaPelanggan.text.toString())
            intent.putExtra("noHpPelanggan", tvNoHpPelanggan.text.toString())
            intent.putExtra("namaLayanan", tvNamaLayanan.text.toString())
            intent.putExtra("hargaLayanan", tvHargaLayanan.text.toString())

            intent.putParcelableArrayListExtra("listTambahan", listTambahan)

            startActivity(intent)

        }

    }
}

