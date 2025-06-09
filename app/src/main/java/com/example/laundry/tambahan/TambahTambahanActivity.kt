package com.example.laundry.tambahan

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
import com.example.laundry.model_data.ModelTambahan
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TambahTambahanActivity : AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("tambahan")
    private val cabangRef = database.getReference("cabang")

    private lateinit var tvJudul: TextView
    private lateinit var tvTitle: TextView
    private lateinit var etNamaTambahan: EditText
    private lateinit var etHargaTambahan: EditText
    private lateinit var cardCabang: MaterialCardView
    private lateinit var tvCabangValue: TextView
    private lateinit var btSimpan: Button
    private lateinit var btnBack: ImageButton

    var idTambahan: String = ""
    var selectedCabang: String = ""
    var cabangList = mutableListOf<ModelCabang>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_tambahan)
        supportActionBar?.hide()
        init()
        getData()
        loadCabangData()
        cardCabang.setOnClickListener {
            showCabangBottomSheet()
        }
        btSimpan.setOnClickListener {
            cekValidasi()
        }
        toolbar()
        tvTitle.text = getString(R.string.Tambahan)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun init() {
        tvJudul = findViewById(R.id.tvJudul)
        etNamaTambahan = findViewById(R.id.etNamaTambahan)
        etHargaTambahan = findViewById(R.id.etHargaTambahan)
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

    fun getData() {
        idTambahan = intent.getStringExtra("idTambahan").toString()
        val judul = intent.getStringExtra("Judul")
        val nama = intent.getStringExtra("namaTambahan")
        val harga = intent.getIntExtra("hargaTambahan", 0)
        val cabang = intent.getStringExtra("cabangTambahan")
        tvJudul.text = judul
        etNamaTambahan.setText(nama)
        etHargaTambahan.setText("$harga")
        tvCabangValue.text = cabang ?: "Pilih cabang"
        selectedCabang = cabang ?: "Pilih cabang"
        if (!tvJudul.text.equals("Tambah Layanan Tambahan")) {
            if (judul.equals("Edit Layanan Tambahan")) {
                mati()
                btSimpan.text = "Sunting"
            }
        } else {
            hidup()
            etNamaTambahan.requestFocus()
            btSimpan.text = "Simpan"
        }
    }

    fun mati() {
        etNamaTambahan.isEnabled = false
        etHargaTambahan.isEnabled = false
        cardCabang.isEnabled = false
    }

    fun hidup() {
        etNamaTambahan.isEnabled = true
        etHargaTambahan.isEnabled = true
        cardCabang.isEnabled = true
    }

    fun update() {
        val tambahanRef = database.getReference("tambahan").child(idTambahan)
        val data = ModelTambahan(
            idTambahan,
            etNamaTambahan.text.toString(),
            etHargaTambahan.text.toString().toInt(),
            selectedCabang.ifEmpty { tvCabangValue.text.toString() }
        )
        val updateData = mutableMapOf<String, Any>()
        updateData["namaTambahan"] = data.namaTambahan.toString()
        updateData["hargaTambahan"] = data.hargaTambahan.toString()
        updateData["cabangTambahan"] = data.cabangTambahan.toString()
        tambahanRef.updateChildren(updateData).addOnSuccessListener {
            Toast.makeText(
                this@TambahTambahanActivity,
                "Data Tambahan Berhasil Diperbarui",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
            .addOnFailureListener {
                Toast.makeText(
                    this@TambahTambahanActivity,
                    "Data Tambahan Gagal Diperbarui",
                    Toast.LENGTH_SHORT
                ).show()
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
                    this@TambahTambahanActivity,
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

    private fun simpan() {
        val tambahanBaru = myRef.push()
        val tambahanID = tambahanBaru.key ?: ""

        val data = ModelTambahan(
            tambahanID,
            etNamaTambahan.text.toString(),
            etHargaTambahan.text.toString().toInt(),
            selectedCabang.ifEmpty { tvCabangValue.text.toString() }
        )

        tambahanBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this, getString(R.string.BerhasilSimpan), Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun cekValidasi() {
        val nama = etNamaTambahan.text.toString().trim()
        val harga = etHargaTambahan.text.toString().trim()

        if (nama.isEmpty()) {
            etNamaTambahan.error = this.getString(R.string.NamaKosong)
            etNamaTambahan.requestFocus()
            Toast.makeText(
                this@TambahTambahanActivity,
                getString(R.string.NamaKosong),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (harga.isEmpty()) {
            etHargaTambahan.error = this.getString(R.string.AlamatKosong)
            etHargaTambahan.requestFocus()

            Toast.makeText(
                this@TambahTambahanActivity,
                getString(R.string.AlamatKosong),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (harga <= "0") {
            etHargaTambahan.error = "Harga harus lebih dari 0"
            etHargaTambahan.requestFocus()
            Toast.makeText(
                this@TambahTambahanActivity,
                "Harga harus lebih dari 0",
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
            etNamaTambahan.requestFocus()
            btSimpan.text = "Perbarui"
        } else if (btSimpan.text.equals("Perbarui")) {
            update()
        }
    }

}