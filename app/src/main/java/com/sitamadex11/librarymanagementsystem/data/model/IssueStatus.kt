package com.sitamadex11.librarymanagementsystem.data.model

data class IssueStatus(
    val bookId:Int,
    val cardNo:Int,
    val isAccepted:Boolean,
    val isRejected:Boolean,
    val issueDate:String,
    val studentName:String
)