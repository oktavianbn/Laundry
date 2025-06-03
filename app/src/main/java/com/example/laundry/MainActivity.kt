package com.example.laundry

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.laundry.laporan.DataLaporanActivity
import com.example.laundry.layanan.DataLayananActivity
import com.example.laundry.pegawai.DataPegawaiActivity
import com.example.laundry.pelanggan.DataPelangganActivity
import com.example.laundry.tambahan.DataTambahanActivity
import com.example.laundry.transaksi.TransakasiActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


class MainActivity : AppCompatActivity() {
    lateinit var tvWelcome: TextView
    lateinit var tvWaktu: TextView
    lateinit var cvTransaksi: CardView
    lateinit var cvPelanggan: CardView
    lateinit var cvLaporan: CardView
    lateinit var cvAkun: CardView
    lateinit var cvLayanan: CardView
    lateinit var cvTambahan: CardView
    lateinit var cvPegawai: CardView
    lateinit var cvCabang: CardView
    lateinit var cvPrinter: CardView

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
        cvTransaksi = findViewById(R.id.cvTransaksi)
        cvPelanggan = findViewById(R.id.cvPelanggan)
        cvLaporan = findViewById(R.id.cvLaporan)
        cvAkun = findViewById(R.id.cvAkun)
        cvLayanan = findViewById(R.id.cvLayanan)
        cvTambahan = findViewById(R.id.cvTambahan)
        cvPegawai = findViewById(R.id.cvPegawai)
        cvCabang = findViewById(R.id.cvCabang)
        cvPrinter = findViewById(R.id.cvPrinter)
    }

    fun berpindah() {
        cvTransaksi.setOnClickListener {
            val intent = Intent(this, TransakasiActivity::class.java)
            startActivity(intent)
        }
        cvPegawai.setOnClickListener {
            val intent = Intent(this, DataPegawaiActivity::class.java)
            startActivity(intent)
        }
        cvPelanggan.setOnClickListener {
            val intent = Intent(this, DataPelangganActivity::class.java)
            startActivity(intent)
        }
        cvLaporan.setOnClickListener {
            val intent = Intent(this, DataLaporanActivity::class.java)
            startActivity(intent)
        }
        cvLayanan.setOnClickListener {
            val intent = Intent(this, DataLayananActivity::class.java)
            startActivity(intent)
        }
        cvTambahan.setOnClickListener {
            val intent = Intent(this, DataTambahanActivity::class.java)
            startActivity(intent)
        }
        cvCabang.setOnClickListener {
//            belum
            val intent = Intent(this, DataLaporanActivity::class.java)
            startActivity(intent)
        }
        cvPrinter.setOnClickListener {
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
            in 0..11 -> "Selamat Pagi"
            in 12..15 -> "Selamat Siang"
            in 16..18 -> "Selamat Sore"
            else -> "Selamat Malam"
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