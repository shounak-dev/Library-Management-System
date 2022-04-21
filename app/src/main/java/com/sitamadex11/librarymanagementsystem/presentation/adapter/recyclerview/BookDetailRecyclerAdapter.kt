package com.sitamadex11.librarymanagementsystem.presentation.adapter.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sitamadex11.librarymanagementsystem.R
import com.sitamadex11.librarymanagementsystem.data.model.BookDetail

class BookDetailRecyclerAdapter():
    RecyclerView.Adapter<BookDetailRecyclerAdapter.BookDetailRecyclerViewHolder>() {
    private val list = ArrayList<BookDetail>()
    lateinit var context:Context
    inner class BookDetailRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
           val imgBook: ImageView = itemView.findViewById(R.id.imgBook)
           val txtBookName: TextView = itemView.findViewById(R.id.txtBookName)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookDetailRecyclerViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_list_item,parent,false)
        return BookDetailRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookDetailRecyclerViewHolder, pos: Int) {
        Glide.with(context).load(list[pos].imgUrl).into(holder.imgBook)
        holder.txtBookName.text = list[pos].title
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(newList: List<BookDetail>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}