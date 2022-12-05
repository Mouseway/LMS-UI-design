package com.example.lms.screens.course

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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
fun QuizScreen(courseId: Int?, navController: NavController) {

    if(courseId == null)
        throw NullPointerException()

    val viewModel by viewModel<CourseViewModel>(){ parametersOf(courseId) }
    val sharedViewModel by viewModel<SharedViewModel>()

    val course = viewModel.course.observeAsState()
    val scope = rememberCoroutineScope()
    val showDialog = remember {
        mutableStateOf(false)
    }
    val screenState = viewModel.screenState.observeAsState()
    val noteState = viewModel.noteState.observeAsState()

    val menuItems = listOf(
        MenuItem("Quit", Icons.Filled.Close){
            navController.popBackStack()
        },
        MenuItem("Content", Icons.Filled.ImportContacts){
            navController.navigate(NavigationScreens.CourseContentScreen.route + "/$courseId")
        },
        MenuItem("Add note", Icons.Filled.NoteAdd){
            viewModel.openNewNote()
            viewModel.setScreenState(CourseScreenState.ADDING_NOTE)
        },
        MenuItem("All notes", Icons.Filled.Assignment){
            viewModel.setScreenState(CourseScreenState.PRESENTATION)
            navController.navigate(NavigationScreens.CourseNotesScreen.route + "/$courseId")
        },
        MenuItem("Menu", Icons.Filled.Menu){
            scope.launch {
                sharedViewModel.drawerState.open()
            }
        }
    )

//        listOf(
//        MenuItem("Quit", Icons.Filled.Close){
//            navController.popBackStack()
//        },
//        MenuItem("Submit", Icons.Filled.Save){
//            showDialog.value = true
//        },
//        MenuItem("All notes", Icons.Filled.Assignment){
//            navController.navigate(NavigationScreens.CourseNotesScreen.route + "/$courseId")
//        },
//        MenuItem("Menu", Icons.Filled.Menu){
//            scope.launch {
//                sharedViewModel.drawerState.open()
//            }
//        }
//    )

    Drawer {
        ResponsiveMenu(menuItems = menuItems) {
            course.value?.let {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ){
                    Column(
                        modifier = Modifier
                            .padding(30.dp)
                        ,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("Quiz", fontSize = 25.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Spacer(modifier = Modifier.height(10.dp))
                        Column() {
                            Question(question = "How do you like this app?", options = listOf(
                                "\uD83E\uDD84\uD83E\uDD84\uD83E\uDD84\uD83E\uDD84\uD83E\uDD84",
                                "It's awesome!",
                                "I like it, but there is room for improvement",
                                "Not at all",
                                "Can I go home, please?"
                            ))
                            Question(question = "What's your favorite movie?", options = listOf(
                                "The Fellowship of the Ring",
                                "The Two Towers",
                                "The Return of the King",
                                "Can't choose only one from these amazing options",
                            ))
                        }
                        Divider()
                        Box(Modifier.fillMaxWidth()){
                            Button(onClick = {showDialog.value = true}, Modifier.align(Alignment.Center)){
                                Text("Submit")
                            }
                        }
                    }
                    if(screenState.value == CourseScreenState.ADDING_NOTE){
                        Box(Modifier.align(Alignment.BottomCenter)) {
                            NoteScreen(noteState.value ?: "",
                                onChange = {viewModel.setNoteState(it)},
                                textColor = MaterialTheme.colorScheme.onSurface,
                                containerColor = MaterialTheme.colorScheme.surface,
                                onClose = { viewModel.setScreenState(CourseScreenState.PRESENTATION) }
                            )
                        }
                    }
                }

            }
        }
    }

    SubmitDialog(showDialog = showDialog) {
        viewModel.completeCourse()
        navController.popBackStack(NavigationScreens.OverviewScreen.route, false, true)
        showDialog.value = false
    }
}


@Composable
fun Question(question: String, options: List<String>){
    val selected = remember { mutableStateOf(-1)}
    Column(
    ) {
        Text(text = question, modifier = Modifier.padding(top = 10.dp, bottom = 5.dp), fontWeight = FontWeight.Bold)
        options.forEachIndexed { index, s ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        selected.value = index
                    }
            ){
                RadioButton(
                    selected = index == selected.value,
                    onClick = {
                        selected.value = index
                    }
                )
                Text(text = s, modifier = Modifier.padding(vertical = 5.dp))
            }
        }
    }
}

@Composable
fun SubmitDialog(showDialog: MutableState<Boolean>, onConfirm: ()->Unit){
    if(showDialog.value){
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            confirmButton = {
                Button(onClick = { onConfirm() }) {
                    Text("Send")
                }
            },
            title = {Text("Do you really want to submit your answers?")},
            dismissButton = {
                Button(
                    onClick = {showDialog.value = false},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ){
                    Text("Cancel")
                } }
        )
    }
}
