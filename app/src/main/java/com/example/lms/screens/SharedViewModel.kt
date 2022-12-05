package com.example.lms.screens

import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.lms.navigation.NavigationScreens

class SharedViewModel : ViewModel() {

    @OptIn(ExperimentalMaterial3Api::class)
    lateinit var drawerState: DrawerState

    lateinit var navController: NavController

    fun drawerItems(): List<DrawerItem>{
        return listOf(
            DrawerItem("Overview", NavigationScreens.OverviewScreen),
            DrawerItem("Courses", null),
            DrawerItem("Surveys", null)
        )
    }
}

data class DrawerItem(
    val title: String,
    val screen: NavigationScreens?
){

}