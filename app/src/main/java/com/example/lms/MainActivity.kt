package com.example.lms

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.lms.navigation.Navigation
import com.example.lms.screens.SharedViewModel
import com.example.lms.ui.theme.LMSTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                LMSTheme {
                    // A surface container using the 'background' color from the theme

                    val drawerState = rememberDrawerState(DrawerValue.Closed)
                    val navController = rememberNavController()

                    val sharedViewModel by viewModel<SharedViewModel>()
                    sharedViewModel.drawerState = drawerState
                    sharedViewModel.navController = navController

                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Navigation(navController)
                    }
                }
        }
//        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}