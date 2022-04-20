package com.sitamadex11.librarymanagementsystem.presentation.adapter.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sitamadex11.librarymanagementsystem.R

class BookDetailRecyclerAdapter:
    RecyclerView.Adapter<BookDetailRecyclerAdapter.BookDetailRecyclerViewHolder>() {

    inner class BookDetailRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookDetailRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_list_item,parent,false)
        return BookDetailRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookDetailRecyclerViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return 10
    }
}