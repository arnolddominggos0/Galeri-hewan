package org.d3if3024.galerihewan.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.d3if3024.galerihewan.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(layoutInflater, container, false)

        var args = DetailFragmentArgs.fromBundle(requireArguments())
        binding.imageDetail.setImageResource(args.imgDetail)
        binding.textDetail.text = args.hasilDetail
        return binding.root
    }
}
