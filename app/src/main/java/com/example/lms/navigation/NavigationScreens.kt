package com.example.lms.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationScreens(val route: String, val title: String?, val icon: ImageVector?) {
    object LoginScreen : NavigationScreens("loginScreen", null, null)
    object OverviewScreen : NavigationScreens("overviewScreen", "Overview", Icons.Filled.Home)
    object CertificatesScreen : NavigationScreens("certificatesScreen", "Certificates", Icons.Filled.Description)
    object CalendarScreen : NavigationScreens("calendarScreen", "Calendar", Icons.Filled.CalendarMonth)
    object CourseScreen : NavigationScreens("courseScreen", "Course", null)
    object CourseDetailScreen : NavigationScreens("courseDetailScreen", null, null)
    object CourseNotesScreen : NavigationScreens("courseNotesScreen", "All notes", Icons.Filled.Assignment)
    object CourseContentScreen : NavigationScreens("courseContentScreen", "Content", Icons.Filled.ImportContacts)
    object CourseQuizScreen : NavigationScreens("courseQuizScreen", null, null)
}