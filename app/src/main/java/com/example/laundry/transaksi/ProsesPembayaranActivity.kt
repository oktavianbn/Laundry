package com.example.laundry.transaksi

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.R
import com.example.laundry.adapter.AdapterPembayaran
import com.example.laundry.model_data.ModelTambahan

@Suppress("DEPRECATION")
class ProsesPembayaranActivity : AppCompatActivity() {
    lateinit var tvNamaPelanggan: TextView
    lateinit var tvNoHpPelanggan: TextView
    lateinit var tvNamaLayanan: TextView
    lateinit var tvHargaLayanan: TextView
    lateinit var tvTotalHarga: TextView
    lateinit var rcLayananTambahan: RecyclerView
    lateinit var btnBatal: Button
    lateinit var btnProses: Button

    var hargaTambahan: Int? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proses_pembayaran)
        init()
        set()
        naviagasi()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun init() {
        tvNamaPelanggan = findViewById(R.id.tvNamaPelanggan)
        tvNoHpPelanggan = findViewById(R.id.tvNoHpPelanggan)
        tvNamaLayanan = findViewById(R.id.tvNamaLayanan)
        tvHargaLayanan = findViewById(R.id.tvHargaLayanan)
        tvTotalHarga = findViewById(R.id.tvTotalHarga)
        rcLayananTambahan = findViewById(R.id.rcLayananTambahan)
        btnBatal = findViewById(R.id.btnBatal)
        btnProses = findViewById(R.id.btnProses)
    }

    @SuppressLint("SetTextI18n")
    fun set() {
        val nama = intent.getStringExtra("namaPelanggan")
        val noHp = intent.getStringExtra("noHpPelanggan")
        val namaLayanan = intent.getStringExtra("namaLayanan")
        val hargaLayanan = intent.getStringExtra("hargaLayanan")
        val list: ArrayList<ModelTambahan>? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableArrayListExtra("dataTambahan", ModelTambahan::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableArrayListExtra("dataTambahan")
            }



        tvNamaPelanggan.text = nama
        tvNoHpPelanggan.text = noHp
        tvNamaLayanan.text = namaLayanan
        tvHargaLayanan.text = hargaLayanan
        if (!list.isNullOrEmpty()) {
            val adapter = AdapterPembayaran(list)
            rcLayananTambahan.layoutManager = LinearLayoutManager(this)
            rcLayananTambahan.adapter = adapter
            hargaTambahan = list.sumOf { tambahan ->
                tambahan.hargaTambahan ?: 0
            }
        }
        val hargaLayananInt = hargaLayanan?.toIntOrNull() ?: 0
        val tambahan = hargaTambahan ?: 0
        val totalHarga = hargaLayananInt + tambahan
        tvTotalHarga.text = totalHarga.toString()


    }

    fun naviagasi() {
        btnBatal.setOnClickListener {
            val intent = Intent(this, TransaksiActivity::class.java)
            startActivity(intent)
        }
        btnProses.setOnClickListener {
            val dialogView=LayoutInflater.from(this).inflate(R.layout.dialog_mod_hubungi,null)
            val dialog= androidx.appcompat.app.AlertDialog.Builder(this).setView(dialogView).create()
        }
    }
}