package com.example.lms.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lms.screens.course.ContentScreen
import com.example.lms.screens.course.CourseScreen
import com.example.lms.screens.course.NotesScreen
import com.example.lms.screens.course.QuizScreen
import com.example.lms.screens.courseDetail.CourseDetailScreen
import com.example.lms.screens.login.LoginScreen
import com.example.lms.screens.overview.OverviewScreen

@Composable
fun Navigation(navController: NavHostController) {

    NavHost(
    navController = navController,
    startDestination = NavigationScreens.OverviewScreen.route
    ){
        composable(
            route = NavigationScreens.LoginScreen.route,
        ){
            LoginScreen{
                navController.navigate(NavigationScreens.OverviewScreen.route)
            }
        }
        composable(
            route = NavigationScreens.OverviewScreen.route
        ){
            OverviewScreen(navController)
        }
        composable(
            route = NavigationScreens.CourseScreen.route + "/{courseId}/{page}",
            arguments = listOf(
                navArgument("courseId"){
                    type = NavType.IntType
                },
                navArgument("page"){
                    type = NavType.IntType
                }

            )
        ){ entry ->
            val id = entry.arguments?.getInt("courseId")
            val page = entry.arguments?.getInt("page")
            CourseScreen(courseId = id, startPage = page,navController = navController)
        }
        composable(
            route = NavigationScreens.CourseDetailScreen.route + "/{courseId}",
            arguments = listOf(navArgument("courseId"){
                type = NavType.IntType
            })
        ){ entry ->
            val id = entry.arguments?.getInt("courseId")
            CourseDetailScreen(courseId = id, navController)
        }
        composable(
            route = NavigationScreens.CourseNotesScreen.route + "/{courseId}",
            arguments = listOf(navArgument("courseId"){
                type = NavType.IntType
            })
        ){ entry ->
            val id = entry.arguments?.getInt("courseId")
            NotesScreen(courseId = id, navController)
        }
        composable(
            route = NavigationScreens.CourseContentScreen.route + "/{courseId}",
            arguments = listOf(navArgument("courseId"){
                type = NavType.IntType
            })
        ){ entry ->
            val id = entry.arguments?.getInt("courseId")
            ContentScreen(courseId = id, navController)
        }
        composable(
            route = NavigationScreens.CourseQuizScreen.route + "/{courseId}",
            arguments = listOf(navArgument("courseId"){
                type = NavType.IntType
            })
        ){ entry ->
            val id = entry.arguments?.getInt("courseId")
            QuizScreen(courseId = id, navController)
        }
    }
}