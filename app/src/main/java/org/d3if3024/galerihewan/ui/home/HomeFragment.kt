package org.d3if3024.galerihewan.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import org.d3if3024.galerihewan.R
import org.d3if3024.galerihewan.databinding.FragmentHomeBinding
import org.d3if3024.galerihewan.db.Hewandb
import org.d3if3024.galerihewan.model.Hewan

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var menu: Menu

    private val viewModel: HomeViewModel by lazy {
        val db = Hewandb.getInstance(requireContext())
        val factory = HomeViewModelFactory(db.dao)
        ViewModelProvider(this, factory)[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_opsi, menu)
        this.menu = menu
        updateMenuIconColor()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_histori -> {
                findNavController().navigate(R.id.action_navigation_home_to_historiFragment)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateMenuIconColor() {
        val whiteColor = requireContext().getColorCompat(R.color.white)
        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            val icon = menuItem.icon
            icon?.let {
                val wrappedIcon = DrawableCompat.wrap(it)
                DrawableCompat.setTint(wrappedIcon, whiteColor)
                menuItem.icon = wrappedIcon
            }
        }
    }

    fun Context.getColorCompat(@ColorRes colorResId: Int): Int {
        return ContextCompat.getColor(this, colorResId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.search.setOnClickListener { cari() }
        binding.detailButton.setOnClickListener { navigateToDetail() }
        binding.shareButton.setOnClickListener { shareData() }

        viewModel.getHasilHewan().observe(viewLifecycleOwner, { showResult() })
    }

    private fun cari() {
        val nama = binding.searchInp.text.toString()
        val forImg = binding.searchInp.text.toString().lowercase()
        val imgRes = resources.getIdentifier(forImg, "drawable", "org.d3if3024.galerihewan")

        if (isValidInput(nama)) {
            val hewan = initData().find { it.nama.equals(nama, ignoreCase = true) }
            val latin = hewan?.namaLatin
            if (latin != null) {
                viewModel.hasilInput(nama, latin, imgRes)
            }
        } else {
            Toast.makeText(requireContext(), "Input tidak valid", Toast.LENGTH_SHORT).show()
        }
    }


    private fun isValidInput(input: String): Boolean {
        return input.matches(Regex("[a-zA-Z ]+"))
    }

    private fun navigateToDetail() {
        val hasil = binding.searchInp.text.toString()
        val forImg = binding.searchInp.text.toString().lowercase()
        val imgRes = resources.getIdentifier(forImg, "drawable", "org.d3if3024.galerihewan")
        val hewan = initData().find { it.nama.equals(hasil, ignoreCase = true) }
        val latinDetail = hewan?.namaLatin
        if (latinDetail != null) {
            val action = HomeFragmentDirections.actionNavigationHomeToNavigationDetail(hasil, imgRes, latinDetail)
            findNavController().navigate(action)
        }
    }


    private fun shareData() {
        val message = getString(
            R.string.bagikan_template,
            binding.searchInp.text
        )
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, message)
        if (shareIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(shareIntent)
        }
    }

    private fun showResult() {
        val forImg = binding.searchInp.text.toString().lowercase()
        val imgRes = resources.getIdentifier(forImg, "drawable", "org.d3if3024.galerihewan")

        if (imgRes > 0) {
            binding.imageView.setImageResource(imgRes)
            binding.buttonGroup.visibility = View.VISIBLE
        } else {
            binding.imageView.setImageResource(0)
            binding.buttonGroup.visibility = View.INVISIBLE
            Toast.makeText(requireContext(), "Tidak ada Hewan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initData(): List<Hewan> {
        return listOf(
            Hewan("Angsa", "Cygnus olor", R.drawable.angsa),
            Hewan("Ayam", "Gallus gallus", R.drawable.ayam),
            Hewan("Bebek", "Cairina moschata", R.drawable.bebek),
            Hewan("Domba", "Ovis ammon", R.drawable.domba),
            Hewan("Kalkun", "Meleagris gallopavo", R.drawable.kalkun),
            Hewan("Kambing", "Capricornis sumatrensis", R.drawable.kambing),
            Hewan("Kelinci", "Oryctolagus cuniculus", R.drawable.kelinci),
            Hewan("Kerbau", "Bubalus bubalis", R.drawable.kerbau),
            Hewan("Kuda", "Equus caballus", R.drawable.kuda),
            Hewan("Sapi", "Bos taurus", R.drawable.sapi),
        )
    }
}

