package com.example.laundry.transaksi

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.R
import com.example.laundry.adapter.AdapterPilihLayananTambahan
import com.example.laundry.model_data.ModelTambahan
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PilihLayananTambahanActivity : AppCompatActivity() {
    private val db = FirebaseDatabase.getInstance()
    private val ref = db.getReference("tambahan")
    private lateinit var rvPilihLayananTambahan: RecyclerView
    private lateinit var tambahanList: ArrayList<ModelTambahan>
    private lateinit var adapter: AdapterPilihLayananTambahan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilih_layanan_tambahan)

        // Setup ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Transaksi"
        supportActionBar?.subtitle = "Pilih Layanan Tambahan"

        // Setup RecyclerView
        rvPilihLayananTambahan = findViewById(R.id.rvPilihLayananTambahan)
        rvPilihLayananTambahan.layoutManager = LinearLayoutManager(this)
        tambahanList = ArrayList()

        getData()

        // Handle window insets
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

    private fun getData() {
        val query = ref.orderByChild("tambahan").limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    tambahanList.clear()
                    for (data in snapshot.children) {
                        val tambahan = data.getValue(ModelTambahan::class.java)
                        if (tambahan != null) {
                            tambahanList.add(tambahan)
                        }
                    }

                    Log.d("PilihTambahan", "Data loaded: ${tambahanList.size} items")

                    // Inisialisasi adapter dengan parameter yang benar
                    adapter = AdapterPilihLayananTambahan(
                        items = tambahanList, // MutableList
                        isInTransaction = false, // false untuk activity pemilihan
                        onItemClick = { selectedTambahan, position ->
                            Log.d("PilihTambahan", "Item selected: ${selectedTambahan.namaTambahan} at position: $position")

                            // Kirim data kembali ke activity sebelumnya
                            val resultIntent = Intent()
                            resultIntent.putExtra("idTambahan", selectedTambahan.idTambahan)
                            resultIntent.putExtra("namaTambahan", selectedTambahan.namaTambahan)
                            resultIntent.putExtra("hargaTambahan", selectedTambahan.hargaTambahan)

                            Log.d("PilihTambahan", "Mengirim data: ${selectedTambahan.namaTambahan}")
                            Toast.makeText(this@PilihLayananTambahanActivity, "Pilih: ${selectedTambahan.namaTambahan}", Toast.LENGTH_SHORT).show()

                            // PENTING: Hapus item dari list agar tidak bisa dipilih lagi
                            adapter.removeItem(position)

                            setResult(RESULT_OK, resultIntent)
                            finish()
                        }
                    )

                    rvPilihLayananTambahan.adapter = adapter
                    adapter.notifyDataSetChanged()
                } else {
                    Log.d("PilihTambahan", "No data found")
                    Toast.makeText(this@PilihLayananTambahanActivity, "Tidak ada data tambahan", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("PilihTambahan", "Database error: ${error.message}")
                Toast.makeText(
                    this@PilihLayananTambahanActivity,
                    "Gagal memuat data: ${error.message}", Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
