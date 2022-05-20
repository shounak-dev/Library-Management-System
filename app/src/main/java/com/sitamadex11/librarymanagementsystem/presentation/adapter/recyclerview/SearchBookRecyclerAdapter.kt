package com.sitamadex11.librarymanagementsystem.presentation.adapter.recyclerview

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.sitamadex11.librarymanagementsystem.R
import com.sitamadex11.librarymanagementsystem.data.model.BookDetail
import com.sitamadex11.librarymanagementsystem.data.model.CurrentlyIssuedBookDetail

class SearchBookRecyclerAdapter(
    val context: Context,
    private val txtBookNo: TextView,
    private val isAdmin: Boolean,
    private val navController: NavController
) :
    RecyclerView.Adapter<SearchBookRecyclerAdapter.SearchBookRecyclerViewHolder>() {
    private val bookList = ArrayList<BookDetail>()
    private val filteredList = ArrayList<BookDetail>(bookList)

    inner class SearchBookRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgBook: ImageView = itemView.findViewById(R.id.imgBook)
        val txtBookName: TextView = itemView.findViewById(R.id.txtBookName)
        val txtAuthor: TextView = itemView.findViewById(R.id.txtAuthor)
        val txtBookId: TextView = itemView.findViewById(R.id.txtBookId)
        val txtBookAvl: TextView = itemView.findViewById(R.id.txtBookAvl)
        val flBody: FrameLayout = itemView.findViewById(R.id.flBody)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchBookRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_book_recycler_item, parent, false)
        return SearchBookRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchBookRecyclerViewHolder, position: Int) {
        Glide.with(context).load(filteredList[position].imgUrl).into(holder.imgBook)
        holder.txtBookName.text = filteredList[position].title
        holder.txtAuthor.text = filteredList[position].authors.toString()
        holder.txtBookId.text = "Book id: ${filteredList[position].id}"
        holder.txtBookAvl.text =
            "Available: ${filteredList[position].avlQty}/${filteredList[position].totalQty}"

        holder.flBody.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("title", filteredList[position].title)
            bundle.putString("author", filteredList[position].authors[0])
            bundle.putString("page", filteredList[position].page)
            bundle.putString("date", filteredList[position].date)
            bundle.putString("desc", filteredList[position].description)
            bundle.putString("url", filteredList[position].imgUrl)
            bundle.putBoolean("isAdmin", isAdmin)
            if (!isAdmin)
                navController.navigate(R.id.action_searchFragment_to_bookDetailFragment, bundle)
            else
                navController.navigate(R.id.action_adminHomeFragment_to_bookDetailFragment2, bundle)
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    fun updateList(newList: List<BookDetail>) {
        bookList.clear()
        bookList.addAll(newList)
        filteredList.clear()
        filteredList.addAll(bookList)
        if (filteredList.isEmpty()) {
            txtBookNo.text = "No Book Found"
        } else {
            txtBookNo.text = "${filteredList.size} Books"
        }
        notifyDataSetChanged()
    }

    fun addItemTOList(newBookDetail: BookDetail) {
        bookList.add(newBookDetail)
        filteredList.clear()
        filteredList.addAll(bookList)
        txtBookNo.text = "${filteredList.size} Books"
        notifyDataSetChanged()
    }

    fun updateFilteredList(newList: List<BookDetail>, text: String) {
        filteredList.clear()
        filteredList.addAll(newList)
        if (filteredList.isEmpty() && text == "") {
            filteredList.addAll(bookList)
            txtBookNo.text = "${filteredList.size} Books"
        } else if (filteredList.isEmpty() && text != "") txtBookNo.text = "No Book Found"
        else txtBookNo.text = "${filteredList.size} Books"
        notifyDataSetChanged()
    }
}