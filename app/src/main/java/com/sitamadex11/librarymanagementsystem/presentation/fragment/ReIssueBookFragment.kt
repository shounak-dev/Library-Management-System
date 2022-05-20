package com.sitamadex11.librarymanagementsystem.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.sitamadex11.librarymanagementsystem.R
import com.sitamadex11.librarymanagementsystem.databinding.FragmentIssueBookBinding
import java.text.SimpleDateFormat
import java.util.*

const val SEVEN_DAY_INTERVAL = 604800000
class ReIssueBookFragment : Fragment() {
    private lateinit var binding: FragmentIssueBookBinding
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_issue_book, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pageTitle = "Re-ISSUE BOOK"
        binding.buttonTitle = "Re-ISSUE"
        db = FirebaseFirestore.getInstance()
        binding.issueBtn.setOnClickListener {
            val enteredBookId = binding.bookId.text.toString()
            val enteredCardNum = binding.cardNum.text.toString()
            db.collection("issues_log")
                .whereEqualTo("bookId", enteredBookId.toInt())
                .whereEqualTo("cardNo", enteredCardNum.toInt())
                .get()
                .addOnSuccessListener {documents ->
                    for (document in documents) {
                        if(!documents.isEmpty){
                            val isIssued = document.get("isAccepted") as Boolean
                            val issueDate = document.get("issueDate") as String
                            if (isIssued && isReissueRequired(issueDate)) {
                                db.collection("issues_log").document(document.id)
                                    .update(mapOf(
                                        "issueDate" to getTodayDate()
                                    )).addOnSuccessListener {
                                        Toast.makeText(
                                            requireContext(),
                                            "Book Re-Issued Successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }.addOnFailureListener {
                                        Toast.makeText(
                                            requireContext(),
                                            "Something Went Wrong!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "No Need to Reissue",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(
                        requireContext(),
                        "Something Went Wrong!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun isReissueRequired(date: String): Boolean{
        val issueTime = SimpleDateFormat("dd-MMM-yyyy").parse(date).time
        val currentTime = System.currentTimeMillis()
        val timeInterval:Long = currentTime - issueTime
        if(timeInterval> SEVEN_DAY_INTERVAL) return true
        return false
    }

    private fun getTodayDate(): String {
        val c: Date = Calendar.getInstance().time
        val df = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        return df.format(c)
    }
}