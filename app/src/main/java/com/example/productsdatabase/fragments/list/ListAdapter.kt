package com.example.productsdatabase.fragments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.productsdatabase.Product
import com.example.productsdatabase.databinding.DataRowBinding

class ListAdapter(private val parentFragment: ListFragment) :
    RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    private var productList = emptyList<Product>()

    class ListViewHolder(val binding: DataRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            DataRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentItem = productList[position]
        holder.binding.tvName.text = currentItem.name
        holder.binding.tvPrice.text = currentItem.price.toString()

        holder.binding.rowLayout.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
            parentFragment.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun setData(products: List<Product>) {
        this.productList = products
        notifyDataSetChanged()
    }
}