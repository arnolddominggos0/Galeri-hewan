package org.d3if3024.galerihewan.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.d3if3024.galerihewan.R
import org.d3if3024.galerihewan.databinding.ListHewanBinding
import org.d3if3024.galerihewan.databinding.ListHistoryBinding
import org.d3if3024.galerihewan.db.HewanEntity
import org.d3if3024.galerihewan.model.Hewan
import java.text.SimpleDateFormat
import java.util.*

class HewanAdapter :
    ListAdapter<HewanAdapter.HewanItem, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HewanItem>() {
            override fun areItemsTheSame(oldItem: HewanItem, newItem: HewanItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: HewanItem, newItem: HewanItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    sealed class HewanItem {
        data class GaleriItem(val hewan: Hewan) : HewanItem()
        data class HistoriItem(val hewanEntity: HewanEntity) : HewanItem()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            R.layout.list_hewan -> {
                val binding = ListHewanBinding.inflate(inflater, parent, false)
                GaleriViewHolder(binding)
            }
            R.layout.list_history -> {
                val binding = ListHistoryBinding.inflate(inflater, parent, false)
                HistoriViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        when (holder) {
            is GaleriViewHolder -> {
                val galeriItem = item as HewanItem.GaleriItem
                holder.bind(galeriItem.hewan)
            }
            is HistoriViewHolder -> {
                val historiItem = item as HewanItem.HistoriItem
                holder.bind(historiItem.hewanEntity)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is HewanItem.GaleriItem -> R.layout.list_hewan
            is HewanItem.HistoriItem -> R.layout.list_history
        }
    }

    fun submitGaleriData(hewanList: List<Hewan>?) {
        val galeriItems = hewanList?.map { HewanItem.GaleriItem(it) } ?: emptyList()
        submitList(galeriItems)
    }


    fun submitHistoriData(data: List<HewanEntity?>) {
        val historiItems = data.mapNotNull { it?.let { HewanItem.HistoriItem(it) } }
        submitList(historiItems)
    }

    inner class GaleriViewHolder(private val binding: ListHewanBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(hewan: Hewan) {
            with(binding) {
                tvNamaHewan.text = hewan.nama
                tvPengertian.text = hewan.pengertian
                tvSumber.text = hewan.sumber

                Glide.with(root.context)
                    .load(hewan.img)
                    .into(imageView)

                root.setOnClickListener {
                    val message = root.context.getString(R.string.message, hewan.nama)
                    Toast.makeText(root.context, message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    inner class HistoriViewHolder(private val binding: ListHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val dateFormatter = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))

        fun bind(hewanEntity: HewanEntity) {
            with(binding) {
                tanggalTextView.text = dateFormatter.format(Date(hewanEntity.tanggal))
                tvHewan.text = root.context.getString(R.string.hasil_x, hewanEntity.nama)
                tvData.text = root.context.getString(R.string.data_x, hewanEntity.pengertian)
            }
        }
    }
}
