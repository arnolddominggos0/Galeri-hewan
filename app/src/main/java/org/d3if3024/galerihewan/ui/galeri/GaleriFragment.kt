package org.d3if3024.galerihewan.ui.galeri

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.d3if3024.galerihewan.R
import org.d3if3024.galerihewan.data.SettingDataStore
import org.d3if3024.galerihewan.data.dataStore
import org.d3if3024.galerihewan.databinding.FragmentGaleriBinding
import org.d3if3024.galerihewan.network.HewanApi
import org.d3if3024.galerihewan.ui.HewanAdapter

class GaleriFragment : Fragment() {

    private val viewModel: GaleriViewModel by lazy {
        ViewModelProvider(this).get(GaleriViewModel::class.java)
    }

    private lateinit var binding: FragmentGaleriBinding
    private lateinit var myAdapter: HewanAdapter
    private var isLinearLayoutManager = true
    private lateinit var layoutDataStore: SettingDataStore

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i("GaleriFragment", "onAttach dijalankan")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("GaleriFragment", "onCreate dijalankan")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGaleriBinding.inflate(inflater, container, false)
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
        Log.i("GaleriFragment", "onCreateView dijalankan")
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("GaleriFragment", "onViewCreated dijalankan")

        layoutDataStore = SettingDataStore(requireContext().dataStore)
        layoutDataStore.preferenceFlow.asLiveData()
            .observe(viewLifecycleOwner, { value ->
                isLinearLayoutManager = value
                chooseLayout()
                requireActivity().invalidateOptionsMenu()
            })

        viewModel.getData().observe(viewLifecycleOwner, {
            myAdapter.submitGaleriData(it)
        })
        viewModel.getStatus().observe(viewLifecycleOwner) {
            updateProgress(it)
        }
        viewModel.scheduleUpdater(requireActivity().application)
    }
    override fun onStart() {
        super.onStart()
        Log.i("GaleriFragment", "onStart dijalankan")
    }

    override fun onResume() {
        super.onResume()
        Log.i("GaleriFragment", "onResume dijalankan")
    }

    override fun onPause() {
        super.onPause()
        Log.i("GaleriFragment", "onPause dijalankan")

    }

    override fun onStop() {
        super.onStop()
        Log.i("GaleriFragment", "onStop dijalankan")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("GaleriFragment", "onDestroyView dijalankan")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("GaleriFragment", "onDestroy dijalankan")
    }

    override fun onDetach() {
        super.onDetach()
        Log.i("GaleriFragment", "onDetach dijalankan")
    }

    private fun updateProgress(status: HewanApi.ApiStatus) {
        when (status) {
            HewanApi.ApiStatus.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            HewanApi.ApiStatus.SUCCESS -> {
                binding.progressBar.visibility = View.GONE
            }
            HewanApi.ApiStatus.FAILED -> {
                binding.progressBar.visibility = View.GONE
                binding.networkError.visibility = View.VISIBLE
            }
        }
    }

    private fun chooseLayout() {
        binding.recyclerView.layoutManager = if (isLinearLayoutManager) {
            LinearLayoutManager(requireContext())
        } else {
            GridLayoutManager(requireContext(), 2)
        }
    }

    private fun setIcon(menuItem: MenuItem?) {
        if (menuItem == null) return
        menuItem.icon = if (isLinearLayoutManager) {
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.baseline_grid_view_24
            )
        } else {
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.baseline_view_list_24
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_layout, menu)
        val layoutButton = menu.findItem(R.id.action_switch_layout)
        setIcon(layoutButton)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_switch_layout -> {
                // Sets isLinearLayoutManager to the opposite value
                isLinearLayoutManager = !isLinearLayoutManager

                lifecycleScope.launch {
                    layoutDataStore.saveLayoutToPreferencesStore(
                        isLinearLayoutManager, requireContext()
                    )
                }

                // Sets layout and icon
                chooseLayout()
                setIcon(item)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
