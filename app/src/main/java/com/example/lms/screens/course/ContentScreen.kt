package com.example.lms.screens.course

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lms.domain.Course
import com.example.lms.navigation.NavigationScreens
import com.example.lms.others.composable.Drawer
import com.example.lms.others.composable.MenuItem
import com.example.lms.others.composable.ResponsiveMenu
import com.example.lms.screens.SharedViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.viewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentScreen(courseId: Int?, navController: NavController){

    if(courseId == null)
        throw NullPointerException("Course id is null")

    val viewModel by viewModel<CourseViewModel>(){ parametersOf(courseId) }
    val sharedViewModel by viewModel<SharedViewModel>()

    val course = viewModel.course.observeAsState()

    val scope = rememberCoroutineScope()

    val menuItems = listOf(
        MenuItem("Close", Icons.Filled.Close){
           navController.popBackStack()
        },
        MenuItem("Menu", Icons.Filled.Menu){
            scope.launch {
                sharedViewModel.drawerState.open()
            }
        }
    )

    Drawer {
        ResponsiveMenu(menuItems = menuItems) {
            course.value?.let {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .verticalScroll(rememberScrollState())
                ){
                    Column(
                        modifier = Modifier
                            .padding(30.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("Course content", fontSize = 25.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        it.content.forEach {
                            ContentRow(text = it.first, page = it.second + 1, onClick = {
                                navController.popBackStack()
                                navController.popBackStack()
                                navController.navigate(NavigationScreens.CourseScreen.route + "/$courseId/${it.second}")
                            }, divider = true)
                        }
                        ContentRow(text = "Quiz", page = null, onClick = { navController.navigate(NavigationScreens.CourseQuizScreen.route + "/$courseId")}, divider = false)
                    }
                }
            }
        }
    }
}

@Composable
fun ContentRow(text: String, page: Int?, onClick: ()->Unit, divider: Boolean){
    Row(
        Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                onClick()
            }
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,

        ) {
        Text(text = text, fontSize = 18.sp, color = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.padding(8.dp))
        Spacer(modifier = Modifier.weight(1F))
        page?.let {
            Text(text = it.toString(), fontSize = 18.sp, color = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.padding(8.dp))
        }
    }
    if(divider){
        Divider(thickness = 2.dp)
    }
}