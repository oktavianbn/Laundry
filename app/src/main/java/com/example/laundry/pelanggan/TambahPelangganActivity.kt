package com.example.laundry.pelanggan

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.laundry.MainActivity
import com.example.laundry.R
import com.example.laundry.model_data.ModelCabang
import com.example.laundry.model_data.ModelPelanggan
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TambahPelangganActivity : AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("pelanggan")
    private val cabangRef = database.getReference("cabang")

    private lateinit var tvJudul: TextView
    private lateinit var tvTitle: TextView
    private lateinit var etNamaPelanggan: EditText
    private lateinit var etAlamatPelanggan: EditText
    private lateinit var etNoHpPelanggan: EditText
    private lateinit var cardCabang: MaterialCardView
    private lateinit var tvCabangValue: TextView
    private lateinit var btSimpan: Button
    private lateinit var btnBack: ImageButton

    private var idPelanggan: String = ""
    private var selectedCabang: String = ""
    private var cabangList = mutableListOf<ModelCabang>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_pelanggan)
        supportActionBar?.hide()
        init()
        getData()
        loadCabangData()
        btSimpan.setOnClickListener {
            cekValidasi()
        }
        cardCabang.setOnClickListener {
            showCabangBottomSheet()
        }
        toolbar()
        tvTitle.text = getString(R.string.Pelanggan)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun init() {
        tvJudul = findViewById(R.id.tvJudul)
        etNamaPelanggan = findViewById(R.id.etNamaPelanggan)
        etAlamatPelanggan = findViewById(R.id.etAlamatPelanggan)
        etNoHpPelanggan = findViewById(R.id.etNoHpPelanggan)
        cardCabang = findViewById(R.id.cardCabang)
        tvCabangValue = findViewById(R.id.tvCabangValue)
        btSimpan = findViewById(R.id.btSimpan)
        btnBack = findViewById(R.id.btnBack)
        tvTitle = findViewById(R.id.tvTitle)
    }

    fun toolbar() {
        btnBack.setOnClickListener {
            finish()
        }
    }


    fun formatTanggal(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale("id", "ID"))
        val date = Date(timestamp)
        return sdf.format(date)
    }

    private fun simpan() {
        val pelangganBaru = myRef.push()
        val pelangganID = pelangganBaru.key ?: ""
        val timeStamp = System.currentTimeMillis()
        val tanggalTerformat = formatTanggal(timeStamp)
        val data = ModelPelanggan(
            pelangganID,
            etNamaPelanggan.text.toString(),
            etAlamatPelanggan.text.toString(),
            etNoHpPelanggan.text.toString(),
            tvCabangValue.text.toString(),
            tanggalTerformat
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

    private fun loadCabangData() {
        cabangRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                cabangList.clear()
                for (snapshot in dataSnapshot.children) {
                    val cabang = snapshot.getValue(ModelCabang::class.java)
                    cabang?.let {
                        cabangList.add(it)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(
                    this@TambahPelangganActivity,
                    "Error loading cabang data: ${databaseError.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun showCabangBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_cabang, null)

        val listViewCabang = bottomSheetView.findViewById<ListView>(R.id.listViewCabang)
        val tvTitleBottomSheet = bottomSheetView.findViewById<TextView>(R.id.tvTitle)

        tvTitleBottomSheet.text = "Pilih Cabang"

        // Create adapter for ListView
        val cabangNames = cabangList.map { it.namaCabang ?: "Unknown" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, cabangNames)
        listViewCabang.adapter = adapter

        // Set item click listener
        listViewCabang.setOnItemClickListener { _, _, position, _ ->
            val selectedModel = cabangList[position]
            selectedCabang = selectedModel.namaCabang ?: ""
            tvCabangValue.text = selectedCabang
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    private fun getData() {
        idPelanggan = intent.getStringExtra("idPelanggan").toString()
        val judul = intent.getStringExtra("Judul")
        val nama = intent.getStringExtra("namaPelanggan")
        val alamat = intent.getStringExtra("alamatPelanggan")
        val hp = intent.getStringExtra("noHpPelanggan")
        val cabang = intent.getStringExtra("cabangPelanggan")
        tvJudul.text = judul
        etNamaPelanggan.setText(nama)
        etAlamatPelanggan.setText(alamat)
        etNoHpPelanggan.setText(hp)
        tvCabangValue.text = cabang ?: "Pilih cabang"
        selectedCabang = cabang ?: "Pilih cabang"
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
        cardCabang.isEnabled = false
    }

    private fun hidup() {
        etNamaPelanggan.isEnabled = true
        etAlamatPelanggan.isEnabled = true
        etNoHpPelanggan.isEnabled = true
        cardCabang.isEnabled = true
    }

    private fun update() {
        val pelangganRef = database.getReference("pelanggan").child(idPelanggan)
        val data = ModelPelanggan(
            idPelanggan,
            etNamaPelanggan.text.toString().trim(),
            etAlamatPelanggan.text.toString().trim(),
            etNoHpPelanggan.text.toString().trim(),
            tvCabangValue.text.toString().trim()
        )
        val updateData = mutableMapOf<String, Any>()
        updateData["namaPelanggan"] = data.namaPelanggan.toString()
        updateData["alamatPelanggan"] = data.alamatPelanggan.toString()
        updateData["noHpPelanggan"] = data.noHpPelanggan.toString()
        updateData["cabangPelanggan"] = data.cabangPelanggan.toString()
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
        if (selectedCabang.isEmpty() || selectedCabang == "Pilih cabang") {
            Toast.makeText(this, getString(R.string.CabangKosong), Toast.LENGTH_SHORT).show()
            return
        }

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
