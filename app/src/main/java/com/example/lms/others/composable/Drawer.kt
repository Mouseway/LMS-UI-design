package com.example.lms.others.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lms.screens.SharedViewModel
import com.example.lms.screens.overview.*
import kotlinx.coroutines.launch
import org.koin.androidx.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Drawer( content: @Composable ()->Unit){
    val sharedViewModel by viewModel<SharedViewModel>()

    val coroutineScope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = sharedViewModel.drawerState,
        drawerContent = {
//            .fillMaxWidth(0.75F)
            ModalDrawerSheet(modifier = Modifier.padding(end = 100.dp)) {
                Column(Modifier.padding(10.dp)) {
                    val items = sharedViewModel.drawerItems()
                    BigLabel(text = "Menu")
                    Spacer(modifier = Modifier.height(20.dp))
                    items.forEach {
                        val selected = it.screen?.route == sharedViewModel.navController.currentDestination?.route
                        NavigationDrawerItem(
                            label = { Text(text = it.title) },
                            selected = selected,
                            onClick = {
                                if(!selected) {
                                    it.screen?.let { it1 ->
                                        sharedViewModel.navController.navigate(
                                            it1.route
                                        )
                                        coroutineScope.launch {
                                            sharedViewModel.drawerState.close()
                                        }
                                    }
                                }})
                    }
                }
            }
        },
        content = {
           content()
        }
    )
}