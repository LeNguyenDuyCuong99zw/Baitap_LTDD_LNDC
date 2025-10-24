package com.map.baivenha1.model

data class Book(
    val id: String,
    val title: String,
    var isSelected: Boolean = false
)