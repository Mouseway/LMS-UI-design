package com.example.lms.screens.courseDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lms.navigation.NavigationScreens
import com.example.lms.others.composable.NavigationItem
import com.example.lms.screens.course.ContentRow
import com.example.lms.screens.course.CourseViewModel
import org.koin.androidx.compose.viewModel
import org.koin.core.parameter.parametersOf

@Composable
fun CourseDetailScreen(courseId: Int?, navController: NavController){

    if(courseId == null)
        throw IllegalArgumentException()

    val viewModel by viewModel<CourseViewModel>(){ parametersOf(courseId) }
    val course = viewModel.course.observeAsState()


    Scaffold(
        floatingActionButton = {
            androidx.compose.material3.FloatingActionButton(
                shape = CircleShape,
                onClick = {
                  navController.navigate(NavigationScreens.CourseScreen.route + "/${courseId}/${course.value?.lastVisitedPage}")
                },
                contentColor = MaterialTheme.colorScheme.onSecondary,
                containerColor = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(60.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.School,
                    contentDescription = "Add icon",
                    Modifier.size(40.dp)
                )
            }
        },
        isFloatingActionButtonDocked = true,
        bottomBar = {
            BottomAppBar(
                // Defaults to null, that is, No cutout
                cutoutShape = MaterialTheme.shapes.small.copy(
                    CornerSize(percent = 50)
                ),
                backgroundColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .height(65.dp)
                    .clip(RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp))
            ) {
                Row(
//                    Modifier.clip(RoundedCornerShape(topEnd = 8.dp, topStart = 8.dp, bottomStart = 0.dp, bottomEnd = 0.dp))
                ){
                    Box(Modifier.weight(1F)){
                        NavigationItem(
                            selected = false,
                            onClick = { navController.popBackStack() },
                            icon = Icons.Filled.ArrowBack,
                            label = "Back"
                        )
                    }
                    Spacer(modifier = Modifier.weight(3F))
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ){
            Column(
                modifier = Modifier
                    .padding(top = 30.dp, start = 30.dp, end = 30.dp, bottom = 30.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                course.value?.let {
                    Text(it.name, fontSize = 25.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Spacer(Modifier.height(10.dp))
                    CourseItem(label = "Status:", text = if(it.completed) "Completed" else "Incomplete")
                    CourseItem(label = "Qualifying quiz:", text = getStrFromBool(it.qualifyingQuiz))
                    CourseItem(label = "Certificate:", text = getStrFromBool(it.certificate))
                    CourseItem(label = "No. of lessons:", text = it.lessons.toString())
                    CourseItem(label = "Activation:", text = it.activation.toString())
                    CourseItem(label = "Deadline", text = it.validityDate.toString())
                    Divider(thickness = 2.dp)
                    Text("Description:")
                    Text(text = it.description)
                    Divider(thickness = 2.dp)
                    Spacer(Modifier.height(20.dp))
                    Text("Content", fontSize = 25.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Spacer(Modifier.height(10.dp))
                    it.content.forEach {
                        ContentRow(
                            text = it.first,
                            page = it.second + 1,
                            onClick = {navController.navigate(getRoute(courseId, it.second)) },
                            divider = true)
                    }
                    ContentRow(
                        text = "Quiz",
                        page = null,
                        onClick = {
                            navController.navigate(NavigationScreens.CourseQuizScreen.route + "/$courseId")
                        },
                        divider = false
                    )
                }
            }
        }
    }
}

fun getRoute(id:Int, page: Int): String {
    return NavigationScreens.CourseScreen.route + "/$id/$page"
}

fun getStrFromBool(bool: Boolean): String = if(bool) "Yes" else "No"

@Composable
fun CourseItem(label: String, text: String){
    Row(){
        Text(label)
        Spacer(Modifier.width(10.dp))
        Text(text)
    }
}