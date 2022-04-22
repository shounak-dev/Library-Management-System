package com.sitamadex11.librarymanagementsystem.presentation.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sitamadex11.librarymanagementsystem.R
import com.sitamadex11.librarymanagementsystem.data.model.BookDetail
import com.sitamadex11.librarymanagementsystem.databinding.FragmentHomeBinding
import com.sitamadex11.librarymanagementsystem.presentation.adapter.recyclerview.BookCategoryRecyclerAdapter
import com.sitamadex11.librarymanagementsystem.presentation.adapter.recyclerview.BookDetailRecyclerAdapter

class UserHomeFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener  {
    val TAG = "chkFirestore"
    lateinit var binding: FragmentHomeBinding
    val bookList = ArrayList<BookDetail>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = Firebase.firestore
        val bookDetailAdapter = BookDetailRecyclerAdapter(requireContext(),findNavController(),false)
        val bookCategoryAdapter = BookCategoryRecyclerAdapter()
        binding.imgHamburger.setOnClickListener {
            binding.drawerlayout.openDrawer(GravityCompat.START)
        }
        binding.navView.setNavigationItemSelectedListener(this)
        binding.rvNewBooks.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        binding.rvNewBooks.adapter = bookDetailAdapter
        binding.rvTopicWiseBooks.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        binding.rvTopicWiseBooks.adapter = bookDetailAdapter
        binding.rvTopics.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        binding.rvTopics.adapter = bookCategoryAdapter

//        val dsa = hashMapOf(
//                "id" to 201,
//                "author" to listOf("Armstrong Subero"),
//                "avlQty" to 10,
//                "date" to "2020",
//                "desc" to "",
//                "imgUrl" to "",
//                "page" to "146",
//                "title" to "Codeless Data Structures and Algorithms",
//                "totalQty" to 12,
//                "category" to "dsa"
//        )
//        val c = hashMapOf(
//            "id" to 201,
//            "author" to listOf("Brian W. Kernighan","Dennis M. Ritchie"),
//            "avlQty" to 9,
//            "date" to "2020",
//            "desc" to "Introduces the features of the C programming language, discusses data types, variables, operators, control flow, functions, pointers, arrays, and structures, and looks at the UNIX system interface",
//            "imgUrl" to "http://books.google.com/books/content?id=OpJ_0zpF7jIC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api",
//            "page" to "146",
//            "title" to "The C Programming Language",
//            "totalQty" to 12,
//            "category" to "dsa"
//        )

//        val arr = arrayOf(dsa,c)
//
//        repeat(2){
//            db.collection("books")
//                .add(arr[it])
//                .addOnSuccessListener { documentReference ->
//                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
//                }
//                .addOnFailureListener { e ->
//                    Log.w(TAG, "Error adding document", e)
//                }
//        }


        db.collection("books")
            .get()
            .addOnSuccessListener { result ->
                bookList.clear()
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    bookList.add(BookDetail(
                        document.get("id") as Long,
                        document.get("author") as List<String>,
                        document.get("avlQty") as Long,
                        document.get("date") as String,
                        document.get("desc") as String,
                        document.get("imgUrl") as String,
                        document.get("page") as String,
                        document.get("title") as String,
                        document.get("totalQty") as Long,
                        document.get("category") as String
                    ))
                }
                bookDetailAdapter.updateList(bookList)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.itemMyBooks -> {
                Toast.makeText(requireContext(),"${item.title} clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.itemFavourites -> {
                Toast.makeText(requireContext(),"${item.title} clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.itemLogout -> {
                Toast.makeText(requireContext(),"${item.title} clicked", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }
}