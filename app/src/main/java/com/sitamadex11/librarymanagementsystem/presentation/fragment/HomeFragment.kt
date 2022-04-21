package com.sitamadex11.librarymanagementsystem.presentation.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sitamadex11.librarymanagementsystem.R
import com.sitamadex11.librarymanagementsystem.databinding.FragmentHomeBinding
import com.sitamadex11.librarymanagementsystem.presentation.adapter.recyclerview.BookCategoryRecyclerAdapter
import com.sitamadex11.librarymanagementsystem.presentation.adapter.recyclerview.BookDetailRecyclerAdapter

class HomeFragment : Fragment() {
    val TAG = "chkFirestore"
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
        val db = Firebase.firestore
        val bookDetailAdapter = BookDetailRecyclerAdapter()
        val bookCategoryAdapter = BookCategoryRecyclerAdapter()
        binding.rvNewBooks.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        binding.rvNewBooks.adapter = bookDetailAdapter
        binding.rvTopicWiseBooks.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        binding.rvTopicWiseBooks.adapter = bookDetailAdapter
        binding.rvTopics.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        binding.rvTopics.adapter = bookCategoryAdapter

        val DSA = hashMapOf(
            "201" to hashMapOf(
                "author" to arrayOf("Armstrong Subero"),
                "avlQty" to 10,
                "date" to "2020",
                "desc" to "In the era of self-taught developers and programmers, essential topics in the industry are frequently learned without a formal academic foundation. A solid grasp of data structures and algorithms (DSA) is imperative for anyone looking to do professional software development and engineering, but classes in the subject can be dry or spend too much time on theory and unnecessary readings. Regardless of your programming language background, Codeless Data Structures and Algorithms has you covered. In this book, author Armstrong Subero will help you learn DSAs without writing a single line of code. Straightforward explanations and diagrams give you a confident handle on the topic while ensuring you never have to open your code editor, use a compiler, or look at an integrated development environment. Subero introduces you to linear, tree, and hash data structures and gives you important insights behind the most common algorithms that you can directly apply to your own programs. Codeless Data Structures and Algorithms provides you with the knowledge about DSAs that you will need in the professional programming world, without using any complex mathematics or irrelevant information. Whether you are a new developer seeking a basic understanding of the subject or a decision-maker wanting a grasp of algorithms to apply to your projects, this book belongs on your shelf. Quite often, a new, refreshing, and unpretentious approach to a topic is all you need to get inspired. What You'll Learn Understand tree data structures without delving into unnecessary details or going into too much theory Get started learning linear data structures with a basic discussion on computer memory Study an overview of arrays, linked lists, stacks and queues Who This Book Is ForThis book is for beginners, self-taught developers and programmers, and anyone who wants to understand data structures and algorithms but don’t want to wade through unnecessary details about quirks of a programming language or don’t have time to sit and read a massive book on the subject. This book is also useful for non-technical decision-makers who are curious about how algorithms work.",
                "imgUrl" to "http://books.google.com/books/content?id=GXvQDwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api",
                "page" to "146",
                "title" to "Codeless Data Structures and Algorithms",
                "totalQty" to 12
            )
        )
        db.collection("books")
            .add(DSA)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }
}