package com.example.laundry.tambahan

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.MainActivity
import com.example.laundry.R
import com.example.laundry.adapter.AdapterDataTambahan
import com.example.laundry.cabang.DataCabangActivity
import com.example.laundry.layanan.DataLayananActivity
import com.example.laundry.model_data.ModelTambahan
import com.example.laundry.pelanggan.DataPelangganActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DataTambahanActivity : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("tambahan")
    lateinit var rvDataTambahan: RecyclerView
    lateinit var tambahanList: ArrayList<ModelTambahan>
    lateinit var cvTambahData: CardView
    lateinit var btnBack: ImageButton
    lateinit var btnClearSearch: ImageButton
    lateinit var etSearch: EditText
    lateinit var llLayanan: LinearLayout
    lateinit var llCabang: LinearLayout
    lateinit var llPelanggan: LinearLayout
    lateinit var llPegawai: LinearLayout
    lateinit var tvJudul: TextView
//    lateinit var appContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_tambahan)
        supportActionBar?.hide()
        init()
        bottomBar()
        toolbar()
        tvJudul.text = getString(R.string.Tambahan)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rvDataTambahan.layoutManager = layoutManager
        rvDataTambahan.setHasFixedSize(true)
        tambahanList = arrayListOf<ModelTambahan>()
        getData()
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
        rvDataTambahan = findViewById(R.id.rvDataTambahan)
        cvTambahData = findViewById(R.id.cvTambahData)
        btnBack = findViewById(R.id.btnBack)
        btnClearSearch = findViewById(R.id.btnClearSearch)
        etSearch = findViewById(R.id.etSearch)
        llCabang = findViewById(R.id.llCabang)
        llLayanan = findViewById(R.id.llLayanan)
        llPelanggan = findViewById(R.id.llPelanggan)
        llPegawai = findViewById(R.id.llPegawai)
        tvJudul = findViewById(R.id.tvJudul)
    }

    fun bottomBar() {
        llCabang.setOnClickListener {
            llCabang.animate().scaleX(0.8f).scaleY(0.8f).setDuration(100).withEndAction {
                llCabang.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
            }
            val intent = Intent(this, DataCabangActivity::class.java)
            startActivity(intent)
        }
        llLayanan.setOnClickListener {
            llLayanan.animate()
                .scaleX(0.8f).scaleY(0.8f).setDuration(100).withEndAction {
                    llLayanan.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
                }
            val intent = Intent(this, DataLayananActivity::class.java)
            startActivity(intent)
        }
        llPelanggan.setOnClickListener {
            llPelanggan.animate().scaleX(0.8f).scaleY(0.8f).setDuration(100).withEndAction {
                llPelanggan.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
            }
            val intent = Intent(this, DataPelangganActivity::class.java)
            startActivity(intent)
        }
        llPegawai.setOnClickListener {
            llPegawai.animate().scaleX(0.8f).scaleY(0.8f).setDuration(100).withEndAction {
                llPegawai.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
            }
            val intent = Intent(this, DataTambahanActivity::class.java)
            startActivity(intent)
        }
        cvTambahData.setOnClickListener {
            cvTambahData.animate().scaleX(0.8f).scaleY(0.8f).setDuration(100).withEndAction {
                cvTambahData.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
            }

            val intent = Intent(this, TambahTambahanActivity::class.java)
            intent.putExtra("Judul", "Tambah Layanan Tambahan")
            intent.putExtra("idTambahan", "")
            intent.putExtra("namaTambahan", "")
            intent.putExtra("noHpTambahan", "")
            intent.putExtra("alamatTambahan", "")
            intent.putExtra("cabangTambahan", "")
//            appContext.startActivity(intent)
            startActivity(intent)
        }
    }

    fun toolbar() {
        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        btnClearSearch.setOnClickListener {
            etSearch.setText("")
            etSearch.clearFocus()
        }
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btnClearSearch.visibility = if ((s?.length ?: 0) > 0) View.VISIBLE else View.GONE
            }

            override fun afterTextChanged(s: Editable?) {
//                performSearch(s.toString())
            }
        })

    }

    fun getData() {
        val query = myRef.orderByChild("Tambahan").limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    tambahanList.clear()
                    for (data in snapshot.children) {
                        val tambahan = data.getValue(ModelTambahan::class.java)
                        tambahanList.add(tambahan!!)
                    }
                    val adapter = AdapterDataTambahan(tambahanList)
                    rvDataTambahan.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DataTambahanActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        }
        )
    }

}