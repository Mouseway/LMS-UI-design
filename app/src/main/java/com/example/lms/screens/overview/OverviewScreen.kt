package com.example.lms.screens.overview

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomAppBar
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.*
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
import com.example.lms.domain.Survey
import com.example.lms.navigation.NavigationScreens
import com.example.lms.others.composable.BottomNav
import com.example.lms.others.composable.Drawer
import com.example.lms.screens.SharedViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.viewModel
import kotlin.math.floor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverviewScreen(navController: NavController) {

    val viewModel by viewModel<OverviewViewModel>()
    val sharedViewModel by viewModel<SharedViewModel>()

    val lastVisitedCourse = viewModel.lastVisitedCourse.observeAsState()
    val coursesInProgress = viewModel.coursesInProgress.observeAsState()
    val coursesToStudy = viewModel.coursesToStudy.observeAsState()


    val navigateToCourse = { courseId: Int, page: Int ->
        navController.navigate(NavigationScreens.CourseScreen.route + "/${courseId}/$page")
    }

    Drawer {
        Scaffold(
            bottomBar = {
                BottomMenu(navController, sharedViewModel.drawerState)
            },
            floatingActionButtonPosition = FabPosition.Center,
            isFloatingActionButtonDocked = true,
            floatingActionButton = {
                FloatingActionButton(
                    shape = CircleShape,
                    onClick = {
                        if (lastVisitedCourse.value != null){
                            navigateToCourse(lastVisitedCourse.value!!.id, lastVisitedCourse.value!!.lastVisitedPage)
                            Log.i("LMS Course A", lastVisitedCourse.value!!.lastVisitedPage.toString())
                        }

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
            }
        ) { padding ->
            Box(Modifier.padding(padding)) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(top = 20.dp, bottom = 20.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OverviewTitle()
                    CoursesInProgressList(
                        courses = coursesInProgress.value ?: emptyList()
                    ) {
                        navigateToCourse(it.id, it.lastVisitedPage)
                    }
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = 10.dp),
                    ) {
                        CoursesToStudy(courses = coursesToStudy.value ?: emptyList(),
                            onCourseClick = {
                                navigateToCourse(it.id, it.lastVisitedPage)
                            },
                            onInfoClick = {
                                navController.navigate(NavigationScreens.CourseDetailScreen.route + "/${it.id}")
                            }
                        )
                        SurveysList(surveys = viewModel.getSurveysToFill())
                    }
                }
            }
        }
    }
}



@Composable
fun OverviewTitle(){
    Text(
        text = "Overview",
        fontSize = 25.sp,
        modifier = Modifier.padding(horizontal = 10.dp)
    )
}

@Composable
fun BigLabel(text: String){
    Text(
        text = text,
        fontSize = 25.sp,
        modifier = Modifier.padding(horizontal = 10.dp)
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CoursesInProgressList(courses: List<Course>, onCourseClick: (Course) -> Unit){

    val pagerState = rememberPagerState()

    val paddingStart = if(pagerState.currentPage == 0) 10.dp else 20.dp

    HorizontalPager(
        count = courses.size,
        contentPadding = PaddingValues(end = 20.dp, start = paddingStart),
        state = pagerState,
        itemSpacing = 10.dp,
        modifier = Modifier.wrapContentHeight()
    ) { page ->
        CourseInProgress(courses[page]){
            onCourseClick(courses[page])
        }
    }
}

@Composable
fun CourseInProgress(course: Course, onClick: () -> Unit){
    Box(){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .clickable {
                    onClick()
                },
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            )
        ) {
            Column(
                Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    SmallLabel(text = "Course in progress")
                    Spacer(Modifier.weight(1F))
                    Icon(Icons.Filled.CalendarMonth, "date",
                        Modifier
                            .width(20.dp)
                            .padding(end = 5.dp))
                    SmallLabel(course.validityDate.toString())
                }
                Text(text = course.name, fontSize = 18.sp)

                ProcessIndicator(process = course.getProgress())
            }
        }
    }
}

@Composable
fun SmallLabel(text: String){
    Text(text = text, fontSize = 12.sp)
}

@Composable
fun ProcessIndicator(process: Float){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ){
        LinearProgressIndicator(
            progress = process,
            trackColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1F)
        )
        SmallLabel(text = "${floor(process*100)}%")
    }
}

@Composable
fun CoursesToStudy(courses: List<Course>, onCourseClick: (Course)->Unit, onInfoClick: (Course) -> Unit){
    MiddleLabel("Courses to study")
    Column {
        courses.forEach{
            SimpleContainer(it.name, it.getProgress(),
                onContainerClick = {
                    onCourseClick(it)
                },
                onInfoClick = {
                    onInfoClick(it)
                }
            )
        }
    }
}

@Composable
fun SimpleContainer(title: String, progress: Float?, onContainerClick: ()->Unit, onInfoClick: ()->Unit){
    Row(
        Modifier
            .padding(vertical = 5.dp)
//            .height(50.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Card(modifier = Modifier
            .weight(1F)
            .fillMaxHeight()
            .clickable {
                onContainerClick()
            }
        ) {
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceEvenly,
            ){
                Column(
                    Modifier
                        .wrapContentHeight()
                        .weight(1F)
                        .padding(start = 10.dp)) {
                    Text(
                        text = title,
                        fontSize = 15.sp,
                        modifier = Modifier
//                            .padding(horizontal = 10.dp)
                    )
                    progress?.let {
                        ProcessIndicator(process = progress)
                    }
                }
                Box(){
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 10.dp)
                            .size(35.dp)
                    )
                }
            }
        }
       InfoButton(){
            onInfoClick()
       }
    }
}

@Composable
fun InfoButton(onClick: ()->Unit){
    IconButton(onClick = {onClick()}) {
        Icon(
            imageVector = Icons.Filled.Info,
            contentDescription = "",
            Modifier.size(50.dp),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun MiddleLabel(text: String){
    Text(text, fontSize = 20.sp, modifier = Modifier.padding(top = 10.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomMenu(navController: NavController, drawerState: DrawerState){
    val coroutineScope = rememberCoroutineScope()
    BottomAppBar(
        modifier = Modifier
            .height(65.dp)
            .clip(RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp)),
        cutoutShape = CircleShape,
        //backgroundColor = Color.White,
        elevation = 22.dp,
        backgroundColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        BottomNav(navController = navController, NavigationScreens.OverviewScreen,
        onMenuClick = {
            coroutineScope.launch {
                drawerState.open()
            }
        }
        )
    }
}

@Composable
fun SurveysList(surveys: List<Survey>){
    MiddleLabel(text = "Surveys to fill")
    surveys.forEach {
        SimpleContainer(it.title, null,
            onContainerClick = {},
            onInfoClick = {}
        )
    }
}
