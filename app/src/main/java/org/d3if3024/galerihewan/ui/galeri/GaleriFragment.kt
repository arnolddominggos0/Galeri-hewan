package org.d3if3024.galerihewan.ui.galeri

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import org.d3if3024.galerihewan.databinding.FragmentGaleriBinding
import org.d3if3024.galerihewan.ui.HewanAdapter


class GaleriFragment : Fragment() {

    companion object {
        fun newInstance() = GaleriFragment()
    }

    private val viewModel: GaleriViewModel by lazy {
        ViewModelProvider(this).get(GaleriViewModel::class.java)
    }

    private lateinit var binding: FragmentGaleriBinding
    private lateinit var myAdapter: HewanAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGaleriBinding.inflate(layoutInflater, container, false)
        myAdapter = HewanAdapter()
        with(binding.recyclerView) {
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    RecyclerView.VERTICAL
                )
            )
            adapter = myAdapter
            setHasFixedSize(true)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData().observe(viewLifecycleOwner, {
            myAdapter.updateData(it)
        })
    }
}