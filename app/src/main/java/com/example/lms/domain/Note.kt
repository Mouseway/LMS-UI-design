package com.example.lms.domain

import java.time.LocalDate

data class Note(
    val id: Int,
    val text: String,
    val date: LocalDate
)