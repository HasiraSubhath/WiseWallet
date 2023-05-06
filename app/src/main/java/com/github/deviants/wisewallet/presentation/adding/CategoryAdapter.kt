package com.github.deviants.wisewallet.presentation.adding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.deviants.wisewallet.databinding.CategoryItemBinding


interface CategoryActionListener {
    fun onClick(category: String, v:View?)
}

class CategoryAdapter(private val actionListener: CategoryActionListener)
    : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(), View.OnClickListener {

    var selectedItemPos = -1
    var lastItemSelectedPos = -1

    override fun onClick(v: View?) {
        val category = v?.tag as String
        actionListener.onClick(category, v)
    }

    var categories: List<String> = emptyList()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CategoryItemBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val current = categories[position]


        //TODO(FIX selection)
        holder.binding.root.elevation = 4F
        holder.binding.root.alpha = 1F

        holder.itemView.tag = current
        holder.binding.categoryName.text = current
    }

    override fun getItemCount(): Int = categories.size

    class CategoryViewHolder(val binding: CategoryItemBinding)
        : RecyclerView.ViewHolder(binding.root)


}