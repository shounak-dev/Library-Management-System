package com.sitamadex11.librarymanagementsystem.presentation.adapter.recyclerview

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sitamadex11.librarymanagementsystem.R
import com.sitamadex11.librarymanagementsystem.data.model.BookDetail

class BookDetailRecyclerAdapter(val context:Context, val navController: NavController,val isAdmin:Boolean):
    RecyclerView.Adapter<BookDetailRecyclerAdapter.BookDetailRecyclerViewHolder>() {
    private val list = ArrayList<BookDetail>()
    private var id: Long?= null
    inner class BookDetailRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
           val imgBook: ImageView = itemView.findViewById(R.id.imgBook)
           val txtBookName: TextView = itemView.findViewById(R.id.txtBookName)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookDetailRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_list_item,parent,false)
        return BookDetailRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookDetailRecyclerViewHolder, pos: Int) {
        Glide.with(context).load(list[pos].imgUrl).into(holder.imgBook)
        holder.txtBookName.text = list[pos].title
        holder.imgBook.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("title", list[pos].title)
            bundle.putString("author", list[pos].authors[0])
            bundle.putString("page", list[pos].page)
            bundle.putString("date", list[pos].date)
            bundle.putString("desc", list[pos].description)
            bundle.putString("url", list[pos].imgUrl)
            bundle.putBoolean("isAdmin", isAdmin)
            if(!isAdmin)
            navController.navigate(R.id.action_homeFragment_to_bookDetailFragment,bundle)
            else
                navController.navigate(R.id.action_adminHomeFragment_to_bookDetailFragment2,bundle)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(newList: List<BookDetail>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    fun getId(id:Long){
        this.id = id
    }
}