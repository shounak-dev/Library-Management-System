package com.sitamadex11.librarymanagementsystem.presentation.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.sitamadex11.librarymanagementsystem.R
import com.sitamadex11.librarymanagementsystem.data.model.BookDetail
import com.sitamadex11.librarymanagementsystem.data.model.CurrentlyIssuedBookDetail
import com.sitamadex11.librarymanagementsystem.databinding.FragmentSeeMyBookBinding
import com.sitamadex11.librarymanagementsystem.presentation.adapter.recyclerview.CurrentlyIssuedBookRecyclerAdapter
import com.sitamadex11.librarymanagementsystem.presentation.adapter.recyclerview.SearchBookRecyclerAdapter
import java.text.SimpleDateFormat
import java.util.*


const val ONE_DAY_IN_MILLIS = 86400000
const val SEVEN_DAY_IN_MILLIS = 604800000

class SeeMyBookFragment : Fragment() {
    private lateinit var binding: FragmentSeeMyBookBinding
    private lateinit var db: FirebaseFirestore
    private val currentBookList = ArrayList<CurrentlyIssuedBookDetail>()
    private val prevBookList = ArrayList<BookDetail>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_see_my_book, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = FirebaseFirestore.getInstance()
        val previouslyIssuedBookAdapter =
            SearchBookRecyclerAdapter(requireContext(), binding.txtBookNo,false,findNavController())
        val currentlyIssuedBookAdapter = CurrentlyIssuedBookRecyclerAdapter(
            requireContext(),
            db,
            previouslyIssuedBookAdapter,
            binding.etSearch
        )
        binding.rvCurrentIssued.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCurrentIssued.adapter = currentlyIssuedBookAdapter
        binding.rvPrevIssued.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPrevIssued.adapter = previouslyIssuedBookAdapter
        checkFineAndUpdate()
        getCurrentlyIssuedBookDetail(currentlyIssuedBookAdapter)
        getPrevIssuedBooks(previouslyIssuedBookAdapter)
    }

    private fun calculateInterval(dueDate: String): Long {
        val dueTime = SimpleDateFormat("dd-MMM-yyyy").parse(dueDate).time
        val currentTime = System.currentTimeMillis()
        var timeInterval: Long = dueTime - currentTime
        if (timeInterval < 0) {
            timeInterval = (timeInterval * -1) + SEVEN_DAY_IN_MILLIS
        }
        return (timeInterval / ONE_DAY_IN_MILLIS)
    }

    private fun calculateDueDate(issueDate: String): String {
        val issueTime = SimpleDateFormat("dd-MMM-yyyy").parse(issueDate).time
        val dueDateTime = issueTime + SEVEN_DAY_IN_MILLIS
        val dueDate = Date(dueDateTime)
        val timeZoneDate = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        return timeZoneDate.format(dueDate)
    }

    /**
     * To check if there has fine or not and update accordingly
     */
    private fun checkFineAndUpdate() {
        db.collection("issues_log")
            .whereEqualTo("cardNo", 1234)
            .whereEqualTo("isAccepted", true)
            .get()
            .addOnSuccessListener { docs ->
                for (doc in docs) {
                    val issueDate = doc.get("issueDate") as String
                    val interval = calculateInterval(issueDate)
                    val isFinePaid = doc.get("isFinePaid") as Boolean
                    if (interval > 10 && !isFinePaid) {
                        db.collection("issues_log").document(doc.id)
                            .update(
                                mapOf(
                                    "hasFine" to true,
                                    "fineAmount" to calculateFine(interval)
                                )
                            )
                            .addOnSuccessListener {
                                Toast.makeText(
                                    context,
                                    "Issued Books' Status Updated",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }.addOnFailureListener {
                                Toast.makeText(
                                    context,
                                    "Something Went Wrong!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                }
            }
    }

    private fun calculateFine(interval: Long): Long {
        var tempInterval = interval
        if (interval > 10) {
            tempInterval -= 7
        }
        return (tempInterval / 7) * 5
    }

    private fun getCurrentlyIssuedBookDetail(adapter: CurrentlyIssuedBookRecyclerAdapter) {
        db.collection("issues_log")
            .whereEqualTo("cardNo", 1234)
            .whereEqualTo("isAccepted", true)
            .whereEqualTo("isReturned", false)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val bookId = document.get("bookId") as Long
                    val hasFine = document.get("hasFine") as Boolean
                    val isFinePaid = document.get("isFinePaid") as Boolean
                    val issueDate = document.get("issueDate") as String
                    val dueDate = calculateDueDate(issueDate)
                    db.collection("books")
                        .whereEqualTo("id", bookId)
                        .get()
                        .addOnSuccessListener { docs ->
                            currentBookList.clear()
                            for (doc in docs) {
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
                                currentBookList.add(
                                    CurrentlyIssuedBookDetail(
                                        bookDetail,
                                        issueDate,
                                        dueDate,
                                        calculateInterval(dueDate),
                                        hasFine,
                                        isFinePaid
                                    )
                                )
                            }
                            Log.d("chkList", currentBookList.toString())
                            adapter.updateList(currentBookList)
                        }
                }
            }
    }

    private fun getPrevIssuedBooks(adapter: SearchBookRecyclerAdapter) {
        db.collection("issues_log")
            .whereEqualTo("cardNo", 1234)
            .whereEqualTo("isReturned", true)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val bookId = document.get("bookId") as Long
                    db.collection("books")
                        .whereEqualTo("id", bookId)
                        .get()
                        .addOnSuccessListener { docs ->
                            prevBookList.clear()
                            Log.d("chkList", "success")
                            for (doc in docs) {
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
                                prevBookList.add(
                                    bookDetail
                                )
                            }
                            Log.d("chkList", prevBookList.toString())
                            adapter.updateList(prevBookList)
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
                                }

                                override fun afterTextChanged(editable: Editable) {
                                    //after the change calling the method and passing the search input
                                    filter(editable.toString(), prevBookList, adapter)
                                }
                            })
                        }
                }
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