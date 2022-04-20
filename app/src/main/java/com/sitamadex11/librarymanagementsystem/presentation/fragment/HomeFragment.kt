package com.sitamadex11.librarymanagementsystem.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sitamadex11.librarymanagementsystem.R
import com.sitamadex11.librarymanagementsystem.databinding.FragmentHomeBinding
import com.sitamadex11.librarymanagementsystem.presentation.adapter.recyclerview.BookCategoryRecyclerAdapter
import com.sitamadex11.librarymanagementsystem.presentation.adapter.recyclerview.BookDetailRecyclerAdapter

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
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
        val bookDetailAdapter = BookDetailRecyclerAdapter()
        val bookCategoryAdapter = BookCategoryRecyclerAdapter()
        binding.rvNewBooks.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        binding.rvNewBooks.adapter = bookDetailAdapter
        binding.rvTopicWiseBooks.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        binding.rvTopicWiseBooks.adapter = bookDetailAdapter
        binding.rvTopics.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        binding.rvTopics.adapter = bookCategoryAdapter
    }
}