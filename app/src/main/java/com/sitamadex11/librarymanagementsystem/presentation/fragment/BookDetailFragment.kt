package com.sitamadex11.librarymanagementsystem.presentation.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sitamadex11.librarymanagementsystem.R
import com.sitamadex11.librarymanagementsystem.databinding.FragmentBookDetailBinding

class BookDetailFragment : Fragment() {
    val TAG = "chkSts"
    lateinit var binding: FragmentBookDetailBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_book_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = Firebase.firestore
        Glide.with(requireContext()).load(requireArguments().getString("url")).into(binding.imgBook)
        binding.txtBookName.text = requireArguments().getString("title")
        binding.txtDesc.text = requireArguments().getString("desc")
        binding.txtAuthors.text = requireArguments().getString("author")
        val isAdmin:Boolean = requireArguments().getBoolean("isAdmin")
        if (!isAdmin) {
            binding.btnReqIssue.setOnClickListener {
                binding.btnReqIssue.text = "Request for Issue"
                val newIssueReq = hashMapOf(
                    "cardNo" to 1234,
                    "studentName" to "Sitam Sardar",
                    "bookId" to 101,
                    "isAccepted" to false,
                    "isRejected" to false,
                    "issueDate" to "null"
                )
                db.collection("issues_log")
                    .add(newIssueReq)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        Toast.makeText(
                            requireContext(),
                            "Request Sent Successfully.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                    }
            }
        } else {
            binding.btnReqIssue.text = "Edit Here"
            binding.btnReqIssue.setOnClickListener {
                Toast.makeText(requireContext(),"Edit according to your prefernce",Toast.LENGTH_SHORT).show()
            }
        }
    }
}