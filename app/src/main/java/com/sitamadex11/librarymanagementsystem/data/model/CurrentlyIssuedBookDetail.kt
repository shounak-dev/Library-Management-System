package com.sitamadex11.librarymanagementsystem.data.model

data class CurrentlyIssuedBookDetail(
    val bookDetail: BookDetail,
    val issueDate:String,
    val dueDate:String,
    val interval:Long,
    val hasFine:Boolean,
    val isFinePaid:Boolean
)
