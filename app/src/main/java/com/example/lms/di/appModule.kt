package com.example.lms.di

import com.example.lms.data.Repository
import com.example.lms.screens.SharedViewModel
import com.example.lms.screens.course.CourseViewModel
import com.example.lms.screens.overview.OverviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        Repository()
    }

    viewModel {
        OverviewViewModel(get())
    }
    viewModel {
        parameters ->
        CourseViewModel(courseId = parameters.get(), repository = get())
    }
    single {
        SharedViewModel()
    }
}