package com.example.laundry.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.R
import com.example.laundry.model_data.ModelTambahan


class AdapterPilihLayananTambahan(
    private val items: MutableList<ModelTambahan>, // UBAH ke MutableList
    private val isInTransaction: Boolean = false,
    private val onItemClick: (ModelTambahan, Int) -> Unit = { _, _ -> }, // Tambahkan position
    private val onDeleteClick: (ModelTambahan, Int) -> Unit = { _, _ -> } // Tambahkan position
) : RecyclerView.Adapter<AdapterPilihLayananTambahan.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaTambahan: TextView = itemView.findViewById(R.id.tvNamaLayananTambahan)
        val tvHargaTambahan: TextView = itemView.findViewById(R.id.tvHargaLayananTambahan)
        val btnHapus: ImageButton = itemView.findViewById(R.id.btnHapus)

        fun bind(item: ModelTambahan) {
            tvNamaTambahan.text = item.namaTambahan
            tvHargaTambahan.text = item.hargaTambahan

            // Atur visibility tombol hapus berdasarkan konteks
            btnHapus.visibility = if (isInTransaction) View.VISIBLE else View.GONE

            Log.d("AdapterBind", "Item: ${item.namaTambahan}, isInTransaction: $isInTransaction, btnHapus visibility: ${if (isInTransaction) "VISIBLE" else "GONE"}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_pilih_layanan_tambahan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)

        // Click listener untuk item
        holder.itemView.setOnClickListener {
            Log.d("AdapterClick", "Item clicked: ${item.namaTambahan}, position: $position, isInTransaction: $isInTransaction")
            if (!isInTransaction) { // Hanya trigger jika di activity pemilihan
                Log.d("AdapterClick", "Triggering onItemClick for: ${item.namaTambahan}")
                onItemClick(item, position)
            } else {
                Log.d("AdapterClick", "Click ignored - in transaction mode")
            }
        }

        // Click listener untuk tombol hapus
        holder.btnHapus.setOnClickListener {
            Log.d("AdapterDelete", "Delete clicked for: ${item.namaTambahan} at position: $position")
            onDeleteClick(item, position)
        }
    }

    override fun getItemCount(): Int = items.size

    // Method untuk remove item dari adapter
    fun removeItem(position: Int) {
        if (position >= 0 && position < items.size) {
            val removedItem = items[position]
            items.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, items.size)
            Log.d("AdapterRemove", "Item removed: ${removedItem.namaTambahan}, remaining: ${items.size}")
        }
    }
}