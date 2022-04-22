package com.sitamadex11.librarymanagementsystem.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sitamadex11.librarymanagementsystem.R
import com.sitamadex11.librarymanagementsystem.databinding.FragmentAddBookBinding

class AddBookFragment : Fragment() {
    lateinit var binding: FragmentAddBookBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_book, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = Firebase.firestore
        binding.pageTitle = "ADD BOOK"
        binding.buttonTitle = "ADD"
        binding.btn.setOnClickListener {
            val book = hashMapOf(
                "id" to binding.bookId.text.toString().toLong(),
                "author" to listOf("uploading soon"),
                "avlQty" to binding.etQuantity.text.toString().toLong(),
                "date" to "uploading soon",
                "desc" to "some details about this book is uploading soon",
                "imgUrl" to "",
                "page" to "uploading soon",
                "title" to binding.etBookName.text.toString(),
                "totalQty" to binding.etQuantity.text.toString().toLong(),
                "category" to binding.etCategory.text.toString()
            )
            db.collection("books")
                .add(book)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(requireContext(),"Books are uploaded successfully",Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(),"Something went wrong!",Toast.LENGTH_SHORT).show()
                }
        }
    }
}