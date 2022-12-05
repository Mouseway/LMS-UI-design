package com.example.lms.screens.course

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lms.domain.Note
import com.example.lms.navigation.NavigationScreens
import com.example.lms.others.composable.Drawer
import com.example.lms.others.composable.MenuItem
import com.example.lms.others.composable.ResponsiveMenu
import com.example.lms.others.composable.VerticalMenu
import com.example.lms.screens.SharedViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.viewModel
import org.koin.core.parameter.parametersOf


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun NotesScreen(
    courseId: Int?,
    navController: NavController
){
    if(courseId == null)
        throw NullPointerException("Course id is null")

    val viewModel by viewModel<CourseViewModel>(){ parametersOf(courseId) }
    val sharedViewModel by viewModel<SharedViewModel>()

    val course = viewModel.course.observeAsState()

    val showShareDialog = remember { mutableStateOf(false)}
    var focusRequesters = course.value?.notes?.map { FocusRequester() } ?: emptyList()

//        remember {
//        mutableStateOf(course.value?.notes?.map { FocusRequester() }?.toMutableList() ?: emptyList() )
//    }

    val focusRequest = remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()

    val menuItems = listOf(
        MenuItem("Back", Icons.Filled.ArrowBack){
            navController.popBackStack()
        },
        MenuItem("Add note", Icons.Filled.NoteAdd){
            viewModel.addNewNote()
            focusRequesters =  focusRequesters.toMutableList().also { it.add(FocusRequester()) }
//            Log.i("LMS", focusRequesters.value.size.toString())
//            focusManager.moveFocus(FocusDirection.Exit)
            focusRequest.value = true
        },
        MenuItem("Share", Icons.Filled.Share){
             showShareDialog.value = true
        },
        MenuItem("Menu", Icons.Filled.Menu){
            scope.launch {
                sharedViewModel.drawerState.open()
            }
        }
    )

    course.value?.let {
        Drawer {
            ResponsiveMenu(menuItems = menuItems) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
                        .verticalScroll(rememberScrollState())
                ){
                    Column(
                        content = {
                            Text(
                                "Notes",
                                fontSize = 25.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            it.notes.forEachIndexed { index, note ->
                                NoteCard(
                                    note = note,
                                    onDelete = {
                                        val index2 = course.value?.notes?.indexOf(note)
                                        focusRequesters = focusRequesters.toMutableList().also{
                                            it.removeAt(index2!!)
                                        }
                                        viewModel.deleteNote(note)
                                    },
                                    onEdit = { newText ->
                                        viewModel.updateNote(note, newText)
                                    },
                                    focusRequester = focusRequesters[index]
//                                    requestFocus = index == it.notes.size - 1 && requestFocus.value
                                )
                            }
                        },
                        modifier = Modifier
                            .padding(30.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    )
                }
            }
        }
    }

    ShareAllDialog(showDialog = showShareDialog)

    LaunchedEffect(focusRequest.value){
        if(focusRequest.value){
            delay(300)
            focusRequesters.last().requestFocus()
            focusRequest.value = false
            Log.i("LMS", "LAUNCHED")
        }
    }
//    if(requestFocus.value){
//        LaunchedEffect(Unit) {
////            focusRequesters.value.last().requestFocus()
//            focusRequesters.value.requestFocus()
////            focusRequesters.value.takeLast(2)[0].requestFocus()
//        }
//        requestFocus.value = false
//    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteCard(note: Note, onDelete: ()->Unit, onEdit: (String) -> Unit, focusRequester: FocusRequester){

    val showMenu = remember{ mutableStateOf(false)}

    Row(
        Modifier
            .padding(vertical = 5.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Card(modifier = Modifier
            .weight(1F)
            .wrapContentHeight()
            .fillMaxWidth()
        ) {
            Column(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()) {

                Box(
                    Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                ) {
                    val modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .focusTarget()
                        .focusRequester(focusRequester)

                    TextField(
                        colors = TextFieldDefaults.textFieldColors(
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            textColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        value = note.text,
                        onValueChange = {
                            onEdit(it)
                        },
                        modifier = modifier,
                        singleLine = false,
                        placeholder = {Text("Your note...", color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5F))}
                    )
                    Box(Modifier.align(Alignment.TopEnd)) {
                        IconButton(onClick = { showMenu.value = !showMenu.value }, ) {
                            Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "More")
                        }
                        DropdownMenu(
                            expanded = showMenu.value,
                            onDismissRequest = { showMenu.value = false },
                            offset = DpOffset((-66).dp, (-10).dp)
                        ) {
                            DropdownMenuItem(text = { Text("delete") }, onClick = {
                                showMenu.value = false
                                onDelete()
                            })
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ShareAllDialog(showDialog: MutableState<Boolean>){
    if(showDialog.value){
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            confirmButton = {
                Button(onClick = { showDialog.value = false }, colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.primary
                )) {
                    Text("Close")
                }
            },
            title = {Text("You have successfully shared your notes.")},
        )
    }
}