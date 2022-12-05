package com.example.lms.domain

import java.time.LocalDate

data class Survey(
    val date: LocalDate,
    val completed: Boolean,
     val title: String
)