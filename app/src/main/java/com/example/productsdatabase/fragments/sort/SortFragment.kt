package com.example.productsdatabase.fragments.sort

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.productsdatabase.R
import com.example.productsdatabase.databinding.FragmentSortBinding
import com.example.productsdatabase.fragments.list.ListFragmentDirections

class SortFragment : Fragment() {

    private var _binding: FragmentSortBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSortBinding.inflate(inflater, container, false)

        binding.btnName.setOnClickListener {
            val order = binding.btnName.text.toString().lowercase()
            val action = SortFragmentDirections.actionSortFragmentToListFragment(order)
            findNavController().navigate(action)
        }

        binding.btnPrice.setOnClickListener {
            val order = binding.btnPrice.text.toString().lowercase()
            val action = SortFragmentDirections.actionSortFragmentToListFragment(order)
            findNavController().navigate(action)
        }

         return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}