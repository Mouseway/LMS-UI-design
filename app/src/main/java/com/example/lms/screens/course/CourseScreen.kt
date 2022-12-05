package com.example.lms.screens.course

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lms.R
import com.example.lms.domain.Course
import com.example.lms.navigation.NavigationScreens
import com.example.lms.others.composable.*
import com.example.lms.screens.SharedViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.viewModel
import org.koin.core.parameter.parametersOf


@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class
)
@Composable
fun CourseScreen(courseId: Int?, startPage: Int?, navController: NavController) {

    if(courseId == null || startPage == null)
        throw IllegalArgumentException()

    val viewModel by viewModel<CourseViewModel>(){ parametersOf(courseId)}
    val sharedViewModel by viewModel<SharedViewModel>()

    val course = viewModel.course.observeAsState()
    val screenState = viewModel.screenState.observeAsState()
    val noteState = viewModel.noteState.observeAsState()

    val isKeyboardOpen by keyboardAsState()

    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    val menuButton = MenuItem("Menu", Icons.Filled.Menu){
        scope.launch {
            sharedViewModel.drawerState.open()
        }
    }

    val saveNoteButton = MenuItem("Save", Icons.Filled.Save){
        viewModel.addNewNote(noteState.value)
        viewModel.setScreenState(CourseScreenState.PRESENTATION)
        viewModel.setNoteState("")
    }

    val menuItemsShort = listOf(
        MenuItem("Cancel", Icons.Filled.Close){
            viewModel.setScreenState(CourseScreenState.PRESENTATION)
        },
        saveNoteButton
    )

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
                navController.navigate(NavigationScreens.CourseNotesScreen.route + "/$courseId")
                viewModel.setScreenState(CourseScreenState.PRESENTATION)
            },
            menuButton
        )
//        CourseScreenState.ADDING_NOTE ->  listOf(
//            MenuItem("Quit", Icons.Filled.Close){
//                navController.popBackStack()
//            },
//            MenuItem("Content", Icons.Filled.ImportContacts){
//                navController.navigate(NavigationScreens.CourseContentScreen.route + "/$courseId")
//            },
//            MenuItem("Add note", Icons.Filled.NoteAdd){
//                viewModel.openNewNote()
//            },
//            MenuItem("All notes", Icons.Filled.Assignment){
//                navController.navigate(NavigationScreens.CourseNotesScreen.route + "/$courseId")
//            },
//            menuButton
//            MenuItem("Close", Icons.Filled.Close){
//                viewModel.setScreenState(CourseScreenState.PRESENTATION)
//            },
//            saveNoteButton,
//            MenuItem("All notes", Icons.Filled.Assignment){
//                navController.navigate(NavigationScreens.CourseNotesScreen.route + "/$courseId")
//            },
//            menuButton
//        )


    LaunchedEffect(pagerState) {
        pagerState.scrollToPage(page = startPage)
        viewModel.setLastPage(startPage)

        snapshotFlow { pagerState.currentPage }.collect { page ->
            viewModel.setLastPage(page)
        }
    }


    val configuration = LocalConfiguration.current

    val content = @Composable{
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            course.value?.let { CoursePresentation(course = it, pagerState) {
                navController.navigate(NavigationScreens.CourseQuizScreen.route + "/$courseId")
            }}

            if(screenState.value == CourseScreenState.ADDING_NOTE){
                Box(Modifier.align(Alignment.BottomCenter)) {
                    NoteScreen(noteState.value ?: "",
                        onChange = {viewModel.setNoteState(it)},
                        onClose = {viewModel.setScreenState(CourseScreenState.PRESENTATION)}
                    )
                }
            }
        }
    }

    Drawer {
        when (configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                Row {
                    Box(
                        Modifier
                            .weight(1F)
                            .fillMaxHeight()
                    ){
                        content()
                    }

                    if(isKeyboardOpen == Keyboard.Closed){
                        VerticalMenu(menuItems)
                    }else{
                        ShortVerticalMenu(items = menuItemsShort)
                    }
                }
            }
            else -> {
                Box{
                    Scaffold(
                        bottomBar = {
                            HorizontalMenu(menuItems)}
                    ) { padding ->
                        Box(
                            Modifier
                                .padding(padding)
                                .fillMaxWidth()){
                            content()
                        }
                    }
                }
            }
        }
    }
}



@OptIn(ExperimentalPagerApi::class)
@Composable
fun CoursePresentation(course: Course, pagerState: PagerState, onQuizClick: ()->Unit){
    HorizontalPager(
        count = course.pages.size + 1,
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
//            .verticalScroll(rememberScrollState())
    ) { pageIndex ->
        if(pageIndex < course.pages.size){
            Box(Modifier.fillMaxSize()) {
                Image(painterResource(id = course.pages[pageIndex]), contentDescription = "", modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center))
            }
        }else{
            Box(){
                Button(onClick = {
                    onQuizClick()
                }, modifier = Modifier.align(Alignment.Center)) {
                    Text(
                        text = "Go to quiz"
                    )
                }

            }
//            Image(painter = painterResource(id = R.drawable.miniature), contentDescription = "", modifier = Modifier.fillMaxSize())
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(note: String, onChange: (String)->Unit, onClose: ()->Unit, containerColor: Color = MaterialTheme.colorScheme.primaryContainer, textColor: Color = MaterialTheme.colorScheme.onPrimaryContainer){

    val focusRequester = remember { FocusRequester() }
    Row() {
        TextField(
            value = note,
            onValueChange = {onChange(it)},
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                textColor = textColor,
                containerColor = containerColor,
                focusedTrailingIconColor = textColor,
                unfocusedTrailingIconColor = textColor
            ),
            trailingIcon = {
                IconButton(onClick = {onClose()}) {
                    Icon(imageVector = Icons.Filled.Done, contentDescription = "", tint = MaterialTheme.colorScheme.tertiary)
                }
            },
            placeholder = {Text("Your note...", color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5F))},
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomEnd = 8.dp,
                        bottomStart = 8.dp
                    )
                )
                .focusRequester(focusRequester)
        )

    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

enum class CourseScreenState{
    PRESENTATION, ADDING_NOTE
}

