package com.sitamadex11.librarymanagementsystem.presentation.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.sitamadex11.librarymanagementsystem.R
import com.sitamadex11.librarymanagementsystem.databinding.FragmentIssueBookBinding
import java.text.SimpleDateFormat
import java.util.*


class IssueBookFragment : Fragment() {
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
        db = FirebaseFirestore.getInstance()
        binding.pageTitle = "ISSUE BOOK"
        binding.buttonTitle = "ISSUE"

        binding.issueBtn.setOnClickListener {
            val enteredBookId = binding.bookId.text.toString()
            val enteredCardNum = binding.cardNum.text.toString()
            db.collection("issues_log")
                .whereEqualTo("bookId", enteredBookId.toInt())
                .whereEqualTo("cardNo", enteredCardNum.toInt())
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        if(!documents.isEmpty){
                            AlertDialog.Builder(requireContext())
                                .setTitle("Request Found")
                                .setCancelable(false)
                                .setMessage("Are you sure you want to issue this book?")
                                .setPositiveButton("Issue", DialogInterface.OnClickListener { dialogInterface, i ->
                                    Log.d("chkStatus","Possitive Button Clicked")
                                    db.collection("issues_log").document(document.id)
                                        .update(mapOf(
                                            "isAccepted" to true,
                                            "issueDate" to getTodayDate()
                                        )).addOnSuccessListener {
                                            Toast.makeText(
                                                requireContext(),
                                                "Book Issued Successfully",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            dialogInterface.cancel()
                                        }.addOnFailureListener {
                                            Toast.makeText(
                                                requireContext(),
                                                "Something Went Wrong!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                })

                                // A null listener allows the button to dismiss the dialog and take no further action.
                                .setNegativeButton("Reject", DialogInterface.OnClickListener { dialogInterface, i ->
                                    db.collection("issues_log").document(document.id)
                                        .update(mapOf(
                                            "isRejected" to true
                                        )).addOnSuccessListener {
                                            Toast.makeText(
                                                requireContext(),
                                                "Issue Request Rejected Successfully",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            dialogInterface.cancel()
                                        }.addOnFailureListener {
                                            Toast.makeText(
                                                requireContext(),
                                                "Something Went Wrong!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Issue Request Not Found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), "Failed to get the data.", Toast.LENGTH_SHORT).show()
                }
        }

    }

    private fun getTodayDate(): String {
        val c: Date = Calendar.getInstance().time
        val df = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        return df.format(c)
    }
}