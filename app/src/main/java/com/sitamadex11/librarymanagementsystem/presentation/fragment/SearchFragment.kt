package com.sitamadex11.librarymanagementsystem.presentation.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.sitamadex11.librarymanagementsystem.R
import com.sitamadex11.librarymanagementsystem.data.model.BookDetail
import com.sitamadex11.librarymanagementsystem.databinding.FragmentSearchBinding
import com.sitamadex11.librarymanagementsystem.presentation.adapter.recyclerview.SearchBookRecyclerAdapter
import java.util.ArrayList
import java.util.*

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var db: FirebaseFirestore
    private val allBookList = ArrayList<BookDetail>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = SearchBookRecyclerAdapter(
            requireContext(),
            binding.txtBookNo,
            false,
            findNavController()
        )
        binding.rvSearch.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSearch.adapter = adapter
        db = FirebaseFirestore.getInstance()
        getPrevIssuedBooks(adapter)
    }

    private fun getPrevIssuedBooks(adapter: SearchBookRecyclerAdapter) {
        db.collection("books")
            .get()
            .addOnSuccessListener { documents ->
                allBookList.clear()
                for (doc in documents) {
                    Log.d("chkList", "success")
                    var bookDetail = BookDetail(
                        doc.get("id") as Long,
                        doc.get("author") as List<String>,
                        doc.get("avlQty") as Long,
                        doc.get("date") as String,
                        doc.get("desc") as String,
                        doc.get("imgUrl") as String,
                        doc.get("page") as String,
                        doc.get("title") as String,
                        doc.get("totalQty") as Long,
                        doc.get("category") as String
                    )
                    allBookList.add(
                        bookDetail
                    )
                }
                Log.d("chkList", allBookList.toString())
                adapter.updateList(allBookList)
                binding.etSearch.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        charSequence: CharSequence,
                        i: Int,
                        i1: Int,
                        i2: Int
                    ) {
                    }

                    override fun onTextChanged(
                        charSequence: CharSequence,
                        i: Int,
                        i1: Int,
                        i2: Int
                    ) {
                        filter(charSequence.toString(), allBookList, adapter)
                    }

                    override fun afterTextChanged(editable: Editable) {
                        //after the change calling the method and passing the search input

                    }
                })
            }
    }

    private fun filter(
        text: String,
        bookItems: List<BookDetail>,
        adapter: SearchBookRecyclerAdapter
    ) {
        //new array list that will hold the filtered data
        val filterdBooks: ArrayList<BookDetail> = ArrayList()

        //looping through existing elements
        for (s in bookItems) {
            //if the existing elements contains the search input
            if (s.title.toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdBooks.add(s)
            }
        }

        //calling a method of the adapter class and passing the filtered list
        adapter.updateFilteredList(filterdBooks, text)
    }
}