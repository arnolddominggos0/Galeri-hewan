package org.d3if3024.galerihewan.ui.galeri

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import org.d3if3024.galerihewan.MainActivity
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
    private val _viewModel: GaleriViewModel by lazy {
        ViewModelProvider(this).get(GaleriViewModel::class.java)
    }

    private lateinit var binding: FragmentGaleriBinding
    private lateinit var myAdapter: HewanAdapter
    private var isLinearLayoutManager = true
    private lateinit var layoutDataStore: SettingDataStore

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
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    private fun updateProgress(status: HewanApi.ApiStatus) {
        when (status) {
            HewanApi.ApiStatus.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            HewanApi.ApiStatus.SUCCESS -> {
                binding.progressBar.visibility = View.GONE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestNotificationPermission()
                }
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setIcon(menuItem: MenuItem?) {
        if (menuItem == null) return
        menuItem.iconTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_layout, menu)
        val layoutButton = menu.findItem(R.id.action_switch_layout)
        setIcon(layoutButton)
    }

    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                MainActivity.PERMISSION_REQUEST_CODE
            )
        }
    }
}
