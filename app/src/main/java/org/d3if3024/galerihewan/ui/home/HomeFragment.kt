package org.d3if3024.galerihewan.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import org.d3if3024.galerihewan.R
import org.d3if3024.galerihewan.databinding.FragmentHomeBinding
import org.d3if3024.galerihewan.db.Hewandb
import org.d3if3024.galerihewan.model.Hewan

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.search.setOnClickListener { cari() }
        binding.detailButton.setOnClickListener { navigateToDetail() }
        binding.shareButton.setOnClickListener { shareData() }

        viewModel.getHasilHewan().observe(viewLifecycleOwner, { showResult(it) })
    }

    private fun cari() {
        val hasil = binding.searchInp.text.toString()

        viewModel.hasilInput(hasil, "", 0)
    }

    private fun navigateToDetail() {
        val hasil = binding.searchInp.text.toString()
        val forImg = binding.searchInp.text.toString().lowercase()
        val imgRes = resources.getIdentifier(forImg, "drawable", "org.d3if3024.galerihewan")

        val action = HomeFragmentDirections.actionNavigationHomeToNavigationDetail(hasil, imgRes)
        findNavController().navigate(action)
    }

    private fun shareData() {
        val message = getString(
            R.string.bagikan_template,
            binding.searchInp.text
        )
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain").putExtra(Intent.EXTRA_TEXT, message)
        if (shareIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(shareIntent)
        }
    }

    private fun showResult(result: Hewan?) {
        val forImg = binding.searchInp.text.toString().lowercase()
        val imgRes = resources.getIdentifier(forImg, "drawable", "org.d3if3024.galerihewan")

        if (imgRes > 0) {
            binding.imageView.setImageResource(imgRes)
            binding.buttonGroup.visibility = View.VISIBLE
        } else {
            binding.imageView.setImageResource(imgRes)
            binding.buttonGroup.visibility = View.INVISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_opsi, menu)
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
}
