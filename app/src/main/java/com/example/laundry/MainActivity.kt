package com.example.laundry

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.laundry.cabang.DataCabangActivity
import com.example.laundry.laporan.DataLaporanActivity
import com.example.laundry.layanan.DataLayananActivity
import com.example.laundry.pegawai.DataPegawaiActivity
import com.example.laundry.pelanggan.DataPelangganActivity
import com.example.laundry.tambahan.DataTambahanActivity
import com.example.laundry.transaksi.TransaksiActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


class MainActivity : AppCompatActivity() {
    lateinit var tvWelcome: TextView
    lateinit var tvWaktu: TextView
    lateinit var llTransaksi: LinearLayout
    lateinit var llPelanggan: LinearLayout
    lateinit var llLaporan: LinearLayout
    lateinit var llAkun: LinearLayout
    lateinit var llLayanan: LinearLayout
    lateinit var llTambahan: LinearLayout
    lateinit var llPegawai: LinearLayout
    lateinit var llCabang: LinearLayout
    lateinit var llPrinter: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        init()
        pewaktu()
        berpindah()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    fun init() {
        tvWelcome = findViewById(R.id.tvWelcome)
        tvWaktu = findViewById(R.id.tvWaktu)
        llTransaksi = findViewById(R.id.llTransaksi)
        llPelanggan = findViewById(R.id.llPelanggan)
        llLaporan = findViewById(R.id.llLaporan)
        llAkun = findViewById(R.id.llAkun)
        llLayanan = findViewById(R.id.llLayanan)
        llTambahan = findViewById(R.id.llTambahan)
        llPegawai = findViewById(R.id.llPegawai)
        llCabang = findViewById(R.id.llCabang)
        llPrinter = findViewById(R.id.llPrinter)
    }

    fun berpindah() {
        llTransaksi.setOnClickListener {
            val intent = Intent(this, TransaksiActivity::class.java)
            startActivity(intent)
        }
        llPegawai.setOnClickListener {
            val intent = Intent(this, DataPegawaiActivity::class.java)
            startActivity(intent)
        }
        llPelanggan.setOnClickListener {
            val intent = Intent(this, DataPelangganActivity::class.java)
            startActivity(intent)
        }
        llLaporan.setOnClickListener {
            val intent = Intent(this, DataLaporanActivity::class.java)
            startActivity(intent)
        }
        llLayanan.setOnClickListener {
            val intent = Intent(this, DataLayananActivity::class.java)
            startActivity(intent)
        }
        llTambahan.setOnClickListener {
            val intent = Intent(this, DataTambahanActivity::class.java)
            startActivity(intent)
        }
        llCabang.setOnClickListener {
            val intent = Intent(this, DataCabangActivity::class.java)
            startActivity(intent)
        }
        llPrinter.setOnClickListener {
//            belum
            val intent = Intent(this, DataLaporanActivity::class.java)
            startActivity(intent)
        }

    }

    @SuppressLint("SimpleDateFormat")
    private fun pewaktu() {
        val jam = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val hth = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
        val result = when (jam) {
            in 0..11 -> getString(R.string.Selamat_Pagi)
            in 12..15 -> getString(R.string.Selamat_Siang)
            in 16..18 -> getString(R.string.Selamat_Sore)
            else ->getString(R.string.Selamat_Malam)
        }


        tvWelcome.text = result
        tvWaktu.text = hth.format(Calendar.getInstance().time)
    }

    fun startClockUpdater() {
        lifecycleScope.launch {
            while (isActive) {
                pewaktu()
                delay(60 * 1000)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        startClockUpdater()
    }
}