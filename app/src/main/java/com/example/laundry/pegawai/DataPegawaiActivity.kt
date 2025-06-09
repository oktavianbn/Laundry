package com.example.laundry.pegawai

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
import com.example.laundry.adapter.AdapterDataPegawai
import com.example.laundry.cabang.DataCabangActivity
import com.example.laundry.layanan.DataLayananActivity
import com.example.laundry.model_data.ModelPegawai
import com.example.laundry.pelanggan.DataPelangganActivity
import com.example.laundry.tambahan.DataTambahanActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DataPegawaiActivity : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("pegawai")
    lateinit var pegawaiList: ArrayList<ModelPegawai>
    lateinit var rvDataPegawai: RecyclerView
    lateinit var cvTambahData: CardView
    lateinit var btnBack: ImageButton
    lateinit var btnClearSearch: ImageButton
    lateinit var etSearch: EditText
    lateinit var llLayanan: LinearLayout
    lateinit var llCabang: LinearLayout
    lateinit var llPelanggan: LinearLayout
    lateinit var llTambahan: LinearLayout
    lateinit var tvJudul: TextView


    //    lateinit var appContext: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_pegawai)
        // Hide default ActionBar
        supportActionBar?.hide()
        init()
        bottomBar()
        toolbar()
        tvJudul.text = getString(R.string.Pegawai)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rvDataPegawai.layoutManager = layoutManager
        rvDataPegawai.setHasFixedSize(true)
        pegawaiList = arrayListOf<ModelPegawai>()
        getDataPegawai()


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun init() {
        rvDataPegawai = findViewById(R.id.rvDataPegawai)
        cvTambahData = findViewById(R.id.cvTambahData)
        btnBack = findViewById(R.id.btnBack)
        btnClearSearch = findViewById(R.id.btnClearSearch)
        etSearch = findViewById(R.id.etSearch)
        llCabang = findViewById(R.id.llCabang)
        llLayanan = findViewById(R.id.llLayanan)
        llPelanggan = findViewById(R.id.llPelanggan)
        llTambahan = findViewById(R.id.llTambahan)
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
        llTambahan.setOnClickListener {
            llTambahan.animate().scaleX(0.8f).scaleY(0.8f).setDuration(100).withEndAction {
                llTambahan.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
            }
            val intent = Intent(this, DataTambahanActivity::class.java)
            startActivity(intent)
        }
        cvTambahData.setOnClickListener {
            cvTambahData.animate().scaleX(0.8f).scaleY(0.8f).setDuration(100).withEndAction {
                cvTambahData.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
            }

            val intent = Intent(this, TambahPegawaiActivity::class.java)
            intent.putExtra("Judul", getString(R.string.TambahPegawai))
            intent.putExtra("idPegawai", "")
            intent.putExtra("namaPegawai", "")
            intent.putExtra("noHpPegawai", "")
            intent.putExtra("alamatPegawai", "")
            intent.putExtra("cabangPegawai", "")
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

    fun getDataPegawai() {
        val query = myRef.orderByChild("pegawai").limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    pegawaiList.clear()
                    for (data in snapshot.children) {
                        val pegawai = data.getValue(ModelPegawai::class.java)
                        pegawaiList.add(pegawai!!)
                    }
                    val adapter = AdapterDataPegawai(pegawaiList)
                    rvDataPegawai.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DataPegawaiActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        }
        )
    }
}