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
import com.sitamadex11.librarymanagementsystem.databinding.FragmentAdminHomeBinding
import com.sitamadex11.librarymanagementsystem.databinding.FragmentHomeBinding
import com.sitamadex11.librarymanagementsystem.presentation.adapter.recyclerview.BookCategoryRecyclerAdapter
import com.sitamadex11.librarymanagementsystem.presentation.adapter.recyclerview.BookDetailRecyclerAdapter


class AdminHomeFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {
    val TAG = "chkFirestore"
    lateinit var binding: FragmentAdminHomeBinding
    val bookList = ArrayList<BookDetail>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_admin_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = Firebase.firestore
        val bookDetailAdapter = BookDetailRecyclerAdapter(requireContext(),findNavController(),true)
        val bookCategoryAdapter = BookCategoryRecyclerAdapter()
        binding.imgHamburger.setOnClickListener {
            binding.drawerlayout.openDrawer(GravityCompat.START)
        }
        binding.navView.setNavigationItemSelectedListener(this)
        binding.rvNewBooks.layoutManager = LinearLayoutManager(requireContext(),
            RecyclerView.HORIZONTAL,false)
        binding.rvNewBooks.adapter = bookDetailAdapter
        binding.rvTopicWiseBooks.layoutManager = LinearLayoutManager(requireContext(),
            RecyclerView.HORIZONTAL,false)
        binding.rvTopicWiseBooks.adapter = bookDetailAdapter
        binding.rvTopics.layoutManager = LinearLayoutManager(requireContext(),
            RecyclerView.HORIZONTAL,false)
        binding.rvTopics.adapter = bookCategoryAdapter

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
            R.id.itemSearch -> {
                Toast.makeText(requireContext(),"${item.title} clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.itemAddBook -> {
                Toast.makeText(requireContext(),"${item.title} clicked", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_adminHomeFragment_to_addBookFragment)
                binding.drawerlayout.closeDrawer(GravityCompat.START)
            }
            R.id.itemRemoveBook -> {
                Toast.makeText(requireContext(),"${item.title} clicked", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_adminHomeFragment_to_removeFragment)
                binding.drawerlayout.closeDrawer(GravityCompat.START)
            }
            R.id.itemUpdate -> {
                Toast.makeText(requireContext(),"${item.title} clicked", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_adminHomeFragment_to_updateBookFragment)
                binding.drawerlayout.closeDrawer(GravityCompat.START)
            }
            R.id.itemIssue -> {
                Toast.makeText(requireContext(),"${item.title} clicked", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_adminHomeFragment_to_issueBookFragment)
                binding.drawerlayout.closeDrawer(GravityCompat.START)
            }
            R.id.itemReturnBook -> {
                Toast.makeText(requireContext(),"${item.title} clicked", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_adminHomeFragment_to_returnBookFragment)
                binding.drawerlayout.closeDrawer(GravityCompat.START)
            }
            R.id.itemReissue -> {
                Toast.makeText(requireContext(),"${item.title} clicked", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_adminHomeFragment_to_reIssueBookFragment)
                binding.drawerlayout.closeDrawer(GravityCompat.START)
            }
            R.id.itemCollectFine -> {
                Toast.makeText(requireContext(),"${item.title} clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.itemLogout -> {
                Toast.makeText(requireContext(),"${item.title} clicked", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }
}