package com.example.lms.screens.overview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lms.data.Repository
import com.example.lms.domain.Course
import com.example.lms.domain.Survey
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class OverviewViewModel(val repository: Repository) : ViewModel() {

    val coursesInProgress: MutableLiveData<List<Course>> = MutableLiveData(emptyList())
    val coursesToStudy: MutableLiveData<List<Course>> = MutableLiveData(emptyList())
    val lastVisitedCourse: MutableLiveData<Course> = MutableLiveData()

    init {
        viewModelScope.launch {
            repository.getCoursesInProgress().collect{
                coursesInProgress.value = it
            }
        }
        viewModelScope.launch {
            repository.getCoursesToStudy().collect{
                coursesToStudy.value = it
            }
        }
        viewModelScope.launch {
            repository.getLastVisitedCourse().collect{
                lastVisitedCourse.value = it
            }
        }
    }

    fun getSurveysToFill(): List<Survey>{
        return repository.getSurveysToFill()
    }
}