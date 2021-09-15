package com.example.productsdatabase.fragments.add

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.productsdatabase.Product
import com.example.productsdatabase.ProductViewModel
import com.example.productsdatabase.ProductViewModelFactory
import com.example.productsdatabase.databinding.FragmentAddBinding

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private lateinit var productViewModel: ProductViewModel

    private val args by navArgs<AddFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        val viewModelFactory =
            ProductViewModelFactory(activity?.application!!, args.spinnerPosition)
        productViewModel =
            ViewModelProvider(this, viewModelFactory).get(ProductViewModel::class.java)

        binding.btnAdd.setOnClickListener {
            addToDatabase()
        }

        return binding.root
    }

    private fun addToDatabase() {
        val name = binding.etName.text.toString()
        val price = binding.etPrice.text

        if (inputCheck(name, price)) {
            val product = Product(0, name, price.toString().toDouble())
            productViewModel.addProduct(product)

            Toast.makeText(requireContext(), "Product added", Toast.LENGTH_LONG).show()
            val action =
                AddFragmentDirections.actionAddFragmentToListFragment(spinnerPosition = args.spinnerPosition)
            findNavController().navigate(action)
        } else {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(name: String, price: Editable): Boolean {
        return !(TextUtils.isEmpty(name) && price.isEmpty())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}