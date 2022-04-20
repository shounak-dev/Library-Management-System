package com.sitamadex11.librarymanagementsystem.presentation.adapter.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sitamadex11.librarymanagementsystem.R

class BookCategoryRecyclerAdapter: RecyclerView.Adapter<BookCategoryRecyclerAdapter.BookCategoryRecyclerViewHolder>() {

    inner class BookCategoryRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookCategoryRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_category_list_item,parent,false)
        return BookCategoryRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookCategoryRecyclerViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 10
    }
}