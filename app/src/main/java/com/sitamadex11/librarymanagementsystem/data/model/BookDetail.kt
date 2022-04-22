package com.sitamadex11.librarymanagementsystem.data.model

data class BookDetail(
    val id:Long,
    val authors: List<String>,
    val avlQty: Int,
    val date: String,
    val description: String,
    val imgUrl: String,
    val page: String,
    val title: String,
    val totalQty: Int,
    val category: String
)
