package com.example.lms.domain

import java.security.cert.Certificate
import java.time.LocalDate
import java.util.*

data class Course(
    val id: Int,
    val name: String,
    val validityDate: LocalDate,
    val lastVisitedPage: Int,
    val notes: List<Note>,
    val furthestVisitedPage: Int = -1,
    // Title and page number
    val content: List<Pair<String, Int>>,
    val pages: List<Int>,
    val completed: Boolean,
    val qualifyingQuiz: Boolean,
    val certificate: Boolean,
    val description: String,
    val lessons: Int,
    val activation: LocalDate
){
    fun getProgress(): Float {
        return if(furthestVisitedPage < pages.size)
                    (furthestVisitedPage + 1).toFloat() / pages.size
                else 1.0F
    }
}