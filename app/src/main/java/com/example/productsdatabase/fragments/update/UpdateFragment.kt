package com.example.productsdatabase.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.productsdatabase.Product
import com.example.productsdatabase.view.ProductViewModel
import com.example.productsdatabase.view.ProductViewModelFactory
import com.example.productsdatabase.R
import com.example.productsdatabase.databinding.FragmentUpdateBinding

class UpdateFragment : Fragment() {

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private lateinit var productViewModel: ProductViewModel

    private val args by navArgs<UpdateFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)

        val viewModelFactory =
            ProductViewModelFactory(activity?.application!!, args.spinnerPosition)
        productViewModel =
            ViewModelProvider(this, viewModelFactory).get(ProductViewModel::class.java)

        binding.etNameUpdate.setText(args.currentProduct.name)
        binding.etPriceUpdate.setText(args.currentProduct.price.toString())

        binding.btnUpdate.setOnClickListener {
            updateItem()
        }

        // Add menu
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete) {
            deleteItem()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateItem() {
        val name = binding.etNameUpdate.text.toString()
        val price = binding.etPriceUpdate.text

        if (inputCheck(name, price)) {
            val product = Product(args.currentProduct.id, name, price.toString().toDouble())
            productViewModel.updateProduct(product)

            Toast.makeText(requireContext(), "Product updated", Toast.LENGTH_LONG).show()
            val action = UpdateFragmentDirections
                .actionUpdateFragmentToListFragment(spinnerPosition = args.spinnerPosition)
            findNavController().navigate(action)
        } else {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(name: String, price: Editable): Boolean {
        return !(TextUtils.isEmpty(name) && price.isEmpty())
    }

    private fun deleteItem() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            productViewModel.deleteProduct(args.currentProduct)
            Toast.makeText(
                requireContext(),
                "Product: ${args.currentProduct.name} deleted",
                Toast.LENGTH_SHORT
            ).show()

            val action = UpdateFragmentDirections
                .actionUpdateFragmentToListFragment(spinnerPosition = args.spinnerPosition)
            findNavController().navigate(action)
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete ${args.currentProduct.name}?")
        builder.setMessage("Are you sure?")
        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}