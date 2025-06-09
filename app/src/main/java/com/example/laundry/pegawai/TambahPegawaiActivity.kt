package com.example.laundry.pegawai

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
import com.example.laundry.model_data.ModelPegawai
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TambahPegawaiActivity : AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("pegawai")
    private val cabangRef = database.getReference("cabang")

    private lateinit var tvJudul: TextView
    private lateinit var tvTitle: TextView
    private lateinit var etNamaPegawai: EditText
    private lateinit var etAlamatPegawai: EditText
    private lateinit var etNoHpPegawai: EditText
    private lateinit var cardCabang: MaterialCardView
    private lateinit var tvCabangValue: TextView
    private lateinit var btSimpan: Button
    private lateinit var btnBack: ImageButton

    private var idPegawai: String = ""
    private var selectedCabang: String = ""
    private var cabangList = mutableListOf<ModelCabang>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_pegawai)
        supportActionBar?.hide()

        init()
        getData()
        loadCabangData()

        // Set click listener untuk CardView cabang
        cardCabang.setOnClickListener {
            showCabangBottomSheet()
        }

        btSimpan.setOnClickListener {
            cekValidasi()
        }
        toolbar()

        tvTitle.text = getString(R.string.Pegawai)
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
        etNamaPegawai = findViewById(R.id.etNamaPegawai)
        etAlamatPegawai = findViewById(R.id.etAlamatPegawai)
        etNoHpPegawai = findViewById(R.id.etNoHpPegawai)
        cardCabang = findViewById(R.id.cardCabang)
        tvCabangValue = findViewById(R.id.tvCabangValue)
        btSimpan = findViewById(R.id.btSimpan)
        btnBack = findViewById(R.id.btnBack)
        tvTitle = findViewById(R.id.tvTitle)
    }

    private fun getData() {
        idPegawai = intent.getStringExtra("idPegawai").toString()
        val judul = intent.getStringExtra("Judul")
        val nama = intent.getStringExtra("namaPegawai")
        val alamat = intent.getStringExtra("alamatPegawai")
        val hp = intent.getStringExtra("noHpPegawai")
        val cabang = intent.getStringExtra("cabangPegawai")

        tvJudul.text = judul
        etNamaPegawai.setText(nama)
        etAlamatPegawai.setText(alamat)
        etNoHpPegawai.setText(hp)
        tvCabangValue.text = cabang ?: "Pilih cabang"
        selectedCabang = cabang ?: "Pilih cabang"

        if (!tvJudul.text.equals("Tambah Pegawai")) {
            if (judul.equals("Edit Pegawai")) {
                mati()
                btSimpan.text = "Sunting"
            }
        } else {
            hidup()
            etNamaPegawai.requestFocus()
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
                    this@TambahPegawaiActivity,
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

    private fun mati() {
        etNamaPegawai.isEnabled = false
        etAlamatPegawai.isEnabled = false
        etNoHpPegawai.isEnabled = false
        cardCabang.isEnabled = false
    }

    private fun hidup() {
        etNamaPegawai.isEnabled = true
        etAlamatPegawai.isEnabled = true
        etNoHpPegawai.isEnabled = true
        cardCabang.isEnabled = true
    }

    private fun update() {
        val pegawaiRef = database.getReference("pegawai").child(idPegawai)
        val data = ModelPegawai(
            idPegawai,
            etNamaPegawai.text.toString(),
            etAlamatPegawai.text.toString(),
            etNoHpPegawai.text.toString(),
            selectedCabang.ifEmpty { tvCabangValue.text.toString() }
        )
        val updateData = mutableMapOf<String, Any>()
        updateData["namaPegawai"] = data.namaPegawai.toString()
        updateData["alamatPegawai"] = data.alamatPegawai.toString()
        updateData["noHpPegawai"] = data.noHpPegawai.toString()
        updateData["cabangPegawai"] = data.cabangPegawai.toString()

        pegawaiRef.updateChildren(updateData).addOnSuccessListener {
            Toast.makeText(
                this@TambahPegawaiActivity,
                "Data Pegawai Berhasil Diperbarui",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(
                this@TambahPegawaiActivity,
                "Data Pegawai Gagal Diperbarui",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun simpan() {
        val pegawaiBaru = myRef.push()
        val pegawaiID = pegawaiBaru.key ?: ""

        val data = ModelPegawai(
            pegawaiID,
            etNamaPegawai.text.toString(),
            etAlamatPegawai.text.toString(),
            etNoHpPegawai.text.toString(),
            selectedCabang.ifEmpty { tvCabangValue.text.toString() }
        )

        pegawaiBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this, getString(R.string.BerhasilSimpan), Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun cekValidasi() {
        val nama = etNamaPegawai.text.toString().trim()
        val alamat = etAlamatPegawai.text.toString().trim()
        val nohp = etNoHpPegawai.text.toString().trim()

        if (nama.isEmpty()) {
            etNamaPegawai.error = this.getString(R.string.NamaKosong)
            etNamaPegawai.requestFocus()
            Toast.makeText(
                this@TambahPegawaiActivity,
                getString(R.string.NamaKosong),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (alamat.isEmpty()) {
            etAlamatPegawai.error = this.getString(R.string.AlamatKosong)
            etAlamatPegawai.requestFocus()
            Toast.makeText(
                this@TambahPegawaiActivity,
                getString(R.string.AlamatKosong),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (nohp.isEmpty()) {
            etNoHpPegawai.error = this.getString(R.string.NomorTeleponKosong)
            etNoHpPegawai.requestFocus()
            Toast.makeText(
                this@TambahPegawaiActivity,
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
            etNamaPegawai.requestFocus()
            btSimpan.text = "Perbarui"
        } else if (btSimpan.text.equals("Perbarui")) {
            update()
        }
    }
}