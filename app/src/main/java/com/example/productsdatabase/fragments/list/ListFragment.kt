package com.example.productsdatabase.fragments.list

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.productsdatabase.view.ProductViewModel
import com.example.productsdatabase.view.ProductViewModelFactory
import com.example.productsdatabase.R
import com.example.productsdatabase.databinding.FragmentListBinding

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private lateinit var productViewModel: ProductViewModel

    private val args by navArgs<ListFragmentArgs>()
    var spinnerPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)

        // RecyclerView
        val adapter = ListAdapter(this)
        val recyclerView = binding.recyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Add spinner
        if (args.spinnerPosition != -1) {
            spinnerPosition = args.spinnerPosition
        }
        setSpinner(binding.spinnerDb)

        val viewModelFactory = ProductViewModelFactory(activity?.application!!, spinnerPosition)
        productViewModel =
            ViewModelProvider(this, viewModelFactory).get(ProductViewModel::class.java)

        // Get all data, with or without sort
        if (args.sortOrder != "") {
            productViewModel.sortProducts(args.sortOrder)
        }
        productViewModel.getAllProducts.observe(viewLifecycleOwner, { product ->
            adapter.setData(product)
        })

        binding.floatingActionButton.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToAddFragment(spinnerPosition)
            findNavController().navigate(action)
        }

        // Add menu
        setHasOptionsMenu(true)

        return binding.root
    }


    private fun setSpinner(spinner: Spinner) {
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.db_array,
            android.R.layout.simple_spinner_item
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter
        spinner.setSelection(spinnerPosition)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                itemSelected: View?, selectedItemPosition: Int, selectedId: Long
            ) {
                spinnerPosition = selectedItemPosition
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sort_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_sort) {
            val action = ListFragmentDirections.actionListFragmentToSortFragment(spinnerPosition)
            findNavController().navigate(action)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}