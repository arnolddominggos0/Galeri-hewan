package org.d3if3024.galerihewan.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import org.d3if3024.galerihewan.R
import org.d3if3024.galerihewan.databinding.ListHewanBinding
import org.d3if3024.galerihewan.model.Hewan


class HewanAdapter :
    RecyclerView.Adapter<HewanAdapter.GaleriViewHolder>() {

    private val data = mutableListOf<Hewan>()

    class GaleriViewHolder(
        private val binding: ListHewanBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(hewan: Hewan) = with(binding) {
            tvNamaHewan.text = hewan.nama
            latinTextView.text = hewan.namaLatin
            imageView.setImageResource(hewan.imageId)

            root.setOnClickListener {
                val message = root.context.getString(R.string.message, hewan.nama)
                Toast.makeText(root.context, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GaleriViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListHewanBinding.inflate(inflater, parent, false)
        return GaleriViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GaleriViewHolder, position: Int) {
        val hewan = data[position]
        holder.bind(hewan)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateData(newData: List<Hewan>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }
}

