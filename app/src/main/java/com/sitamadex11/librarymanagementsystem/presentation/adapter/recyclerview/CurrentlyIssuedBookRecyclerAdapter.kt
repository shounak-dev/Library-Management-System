package com.sitamadex11.librarymanagementsystem.presentation.adapter.recyclerview

import android.content.Context
import android.content.DialogInterface
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.FirebaseFirestore
import com.sitamadex11.librarymanagementsystem.R
import com.sitamadex11.librarymanagementsystem.data.model.BookDetail
import com.sitamadex11.librarymanagementsystem.data.model.CurrentlyIssuedBookDetail

class CurrentlyIssuedBookRecyclerAdapter(
    val context: Context,
    val db: FirebaseFirestore,
    private val prevBookAdapter: SearchBookRecyclerAdapter,
    private val etSearch: EditText
) :
    RecyclerView.Adapter<CurrentlyIssuedBookRecyclerAdapter.CurrentlyIssuedBookRecyclerViewHolder>() {

    private val bookList = ArrayList<CurrentlyIssuedBookDetail>()

    inner class CurrentlyIssuedBookRecyclerViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val imgBook: ImageView = itemView.findViewById(R.id.imgBook)
        val txtBookName: TextView = itemView.findViewById(R.id.txtBookName)
        val txtAuthor: TextView = itemView.findViewById(R.id.txtAuthor)
        val txtBookId: TextView = itemView.findViewById(R.id.txtBookId)
        val txtIssueDate: TextView = itemView.findViewById(R.id.txtIssueDate)
        val txtDueDate: TextView = itemView.findViewById(R.id.txtDueDate)
        val txtTimeInterval: TextView = itemView.findViewById(R.id.txtTimeInterval)
        val llOuter: LinearLayout = itemView.findViewById(R.id.llOuter)
        val llInner: LinearLayout = itemView.findViewById(R.id.llInner)
        val btnReturn: MaterialButton = itemView.findViewById(R.id.btnReturn)
        val btnReIssue: MaterialButton = itemView.findViewById(R.id.btnReIssue)
        val btnPayFine: MaterialButton = itemView.findViewById(R.id.btnPayFine)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CurrentlyIssuedBookRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.currently_issued_book_item, parent, false)
        return CurrentlyIssuedBookRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: CurrentlyIssuedBookRecyclerViewHolder, position: Int) {

        var hasFine = bookList[position].hasFine
        var isFinePaid = bookList[position].isFinePaid
        val interval = bookList[position].interval

        Glide.with(context).load(bookList[position].bookDetail.imgUrl).into(holder.imgBook)
        holder.txtBookName.text = bookList[position].bookDetail.title
        holder.txtAuthor.text = bookList[position].bookDetail.authors.toString()
        holder.txtBookId.text = "Book id: ${bookList[position].bookDetail.id}"
        holder.txtIssueDate.text = bookList[position].issueDate
        holder.txtDueDate.text = bookList[position].dueDate

        if (interval < 7)
            holder.txtTimeInterval.text = "${bookList[position].interval} days left"
        else
            holder.txtTimeInterval.text = "Overdue"

        var isTouched = false
        holder.llOuter.setOnClickListener {
            if (!isTouched) {
                holder.llInner.visibility = View.VISIBLE
                isTouched = true
            } else {
                holder.llInner.visibility = View.GONE
                isTouched = false
            }
        }
        onReturnBtnClick(holder, hasFine, isFinePaid, position)
        onReIssueBtnClick(holder, hasFine, isFinePaid, position)
        onPayFineBtnClick(holder, hasFine, isFinePaid, position)

        etSearch.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    fun updateList(newList: List<CurrentlyIssuedBookDetail>) {
        bookList.clear()
        bookList.addAll(newList)
        notifyDataSetChanged()
    }
    fun removeItemFromList(pos:Int){
        bookList.removeAt(pos)
        notifyDataSetChanged()
    }

    private fun calculateFine(interval: Long): Long {
        var tempInterval = interval
        if (interval > 10) {
            tempInterval -= 7
        }
        return (tempInterval / 7) * 5
    }

    private fun onReturnBtnClick(
        holder: CurrentlyIssuedBookRecyclerViewHolder,
        hasFine: Boolean,
        isFinePaid: Boolean,
        position: Int
    ) {
        holder.btnReturn.setOnClickListener {
            if (!hasFine || isFinePaid) {
                db.collection("issues_log")
                    .whereEqualTo("bookId", bookList[position].bookDetail.id)
                    .get()
                    .addOnSuccessListener { docs ->
                        for (doc in docs) {
                            db.collection("issues_log").document(doc.id)
                                .update(
                                    mapOf(
                                        "isReturned" to true
                                    )
                                )
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        context,
                                        "Book Returned Successfully!!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    prevBookAdapter.addItemTOList(bookList[position].bookDetail)
                                    removeItemFromList(position)
                                }.addOnFailureListener {
                                    Toast.makeText(
                                        context,
                                        "Something Went Wrong!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            context,
                            "Something Went Wrong!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                Toast.makeText(
                    context,
                    "Clear your pending fine first!",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    private fun onReIssueBtnClick(
        holder: CurrentlyIssuedBookRecyclerViewHolder,
        hasFine: Boolean,
        isFinePaid: Boolean,
        position: Int
    ) {
        holder.btnReIssue.setOnClickListener {
            val interval = bookList[position].interval
            if (!hasFine || isFinePaid) {
                if (interval in 8..10) {
                    db.collection("issues_log")
                        .whereEqualTo("bookId", bookList[position].bookDetail.id)
                        .get()
                        .addOnSuccessListener { docs ->
                            for (doc in docs) {
                                db.collection("issues_log").document(doc.id)
                                    .update(
                                        mapOf(
                                            "hasReIssueReq" to true,
                                        )
                                    )
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            context,
                                            "ReIssue Request Sent Successfully!",
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
                } else {
                    Toast.makeText(
                        context,
                        "Due date is not over for this book!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    context,
                    "Clear your pending fine first!",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    private fun onPayFineBtnClick(
        holder: CurrentlyIssuedBookRecyclerViewHolder,
        hasFine: Boolean,
        isFinePaid: Boolean,
        position: Int
    ) {
        holder.btnPayFine.setOnClickListener {
            val interval = bookList[position].interval
            when {
                interval <= 7 -> {
                    Toast.makeText(
                        context,
                        "Due date is not over for this book!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                interval in 8..10 -> {
                    Toast.makeText(
                        context,
                        "No Fine Required!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    // pay the fine
                    AlertDialog.Builder(context)
                        .setTitle("!Fine Alert!")
                        .setCancelable(false)
                        .setMessage("Your total fine is ${calculateFine(interval)}")
                        .setPositiveButton(
                            "PAY",
                            DialogInterface.OnClickListener { dialogInterface, i ->
                                db.collection("issues_log")
                                    .whereEqualTo("bookId", bookList[position].bookDetail.id)
                                    .get()
                                    .addOnSuccessListener { docs ->
                                        for (doc in docs) {
                                            db.collection("issues_log").document(doc.id)
                                                .update(
                                                    mapOf(
                                                        "isFinePaid" to true,
                                                    )
                                                )
                                                .addOnSuccessListener {
                                                    Toast.makeText(
                                                        context,
                                                        "Payment Successful!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    dialogInterface.cancel()
                                                }.addOnFailureListener {
                                                    Toast.makeText(
                                                        context,
                                                        "Something Went Wrong!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }

                                        }
                                    }
                            })
                        .setNegativeButton(
                            "CANCEL",
                            DialogInterface.OnClickListener { dialogInterface, i ->
                                dialogInterface.cancel()
                            }).show()
                }
            }
        }
    }
}