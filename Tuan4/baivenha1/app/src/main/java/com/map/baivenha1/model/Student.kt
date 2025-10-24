package com.map.baivenha1.model

data class Student(
    val name: String,
    val borrowedBooks: MutableList<Book> = mutableListOf()
)