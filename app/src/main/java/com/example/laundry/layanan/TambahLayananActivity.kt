package com.example.laundry.layanan

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
import com.example.laundry.model_data.ModelLayanan
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TambahLayananActivity : AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("layanan")
    private val cabangRef = database.getReference("cabang")

    private lateinit var tvJudul: TextView
    private lateinit var tvTitle: TextView
    private lateinit var etNamaLayanan: EditText
    private lateinit var etHargaLayanan: EditText
    private lateinit var tvCabangValue: TextView
    private lateinit var cardCabang: MaterialCardView
    private lateinit var btSimpan: Button
    private lateinit var btnBack: ImageButton

    var idLayanan: String = ""
    private var selectedCabang: String = ""
    private var cabangList = mutableListOf<ModelCabang>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_layanan)
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
        tvTitle.text = getString(R.string.Layanan)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun toolbar() {
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun init() {
        tvJudul = findViewById(R.id.tvJudul)
        etNamaLayanan = findViewById(R.id.etNamaLayanan)
        etHargaLayanan = findViewById(R.id.etHargaLayanan)
        tvCabangValue = findViewById(R.id.tvCabangValue)
        cardCabang = findViewById(R.id.cardCabang)
        btSimpan = findViewById(R.id.btSimpan)
        btnBack = findViewById(R.id.btnBack)
        tvTitle = findViewById(R.id.tvTitle)
    }

    fun getData() {
        idLayanan = intent.getStringExtra("idLayanan").toString()
        val judul = intent.getStringExtra("Judul")
        val nama = intent.getStringExtra("namaLayanan")
        val harga = intent.getIntExtra("hargaLayanan", 0)
        val cabang = intent.getStringExtra("cabangLayanan")
        tvJudul.text = judul
        etNamaLayanan.setText(nama)
        etHargaLayanan.setText("$harga")
        tvCabangValue.text = cabang ?: "Pilih cabang"
        selectedCabang = cabang ?: "Pilih cabang"
        if (!tvJudul.text.equals("Tambah Layanan")) {
            if (judul.equals("Edit Layanan")) {
                mati()
                btSimpan.text = "Sunting"
            }
        } else {
            hidup()
            etNamaLayanan.requestFocus()
            btSimpan.text = "Simpan"
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
                    this@TambahLayananActivity,
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

    fun mati() {
        etNamaLayanan.isEnabled = false
        etHargaLayanan.isEnabled = false
        cardCabang.isEnabled = false
    }

    fun hidup() {
        etNamaLayanan.isEnabled = true
        etHargaLayanan.isEnabled = true
        cardCabang.isEnabled = true
    }

    fun update() {
        val layananRef = database.getReference("layanan").child(idLayanan)
        val data = ModelLayanan(
            idLayanan,
            etNamaLayanan.text.toString(),
            etHargaLayanan.text.toString().toInt(),
            selectedCabang.ifEmpty { tvCabangValue.text.toString() }
        )
        val updateData = mutableMapOf<String, Any>()
        updateData["namaLayanan"] = data.namaLayanan.toString()
        updateData["HargaLayanan"] = data.hargaLayanan.toString()
        updateData["cabangLayanan"] = data.cabangLayanan.toString()
        layananRef.updateChildren(updateData).addOnSuccessListener {
            Toast.makeText(
                this@TambahLayananActivity,
                "Data Layanan Berhasil Diperbarui",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
            .addOnFailureListener {
                Toast.makeText(
                    this@TambahLayananActivity,
                    "Data Layanan Gagal Diperbarui",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun cekValidasi() {
        val nama = etNamaLayanan.text.toString().trim()
        val alamat = etHargaLayanan.text.toString().trim()

        if (nama.isEmpty()) {
            etNamaLayanan.error = getString(R.string.NamaKosong)
            etNamaLayanan.requestFocus()
            return
        }
        if (alamat.isEmpty()) {
            etHargaLayanan.error = getString(R.string.HargaKosong)
            etHargaLayanan.requestFocus()
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
            etNamaLayanan.requestFocus()
            btSimpan.text = "Perbarui"
        } else if (btSimpan.text.equals("Perbarui")) {
            update()
        }
    }

    private fun simpan() {
        val layananBaru = myRef.push()
        val layananID = layananBaru.key ?: ""

        val data = ModelLayanan(
            layananID,
            etNamaLayanan.text.toString(),
            etHargaLayanan.text.toString().toInt(),
            selectedCabang.ifEmpty { tvCabangValue.text.toString() }
        )

        layananBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this, getString(R.string.BerhasilSimpan), Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }
}