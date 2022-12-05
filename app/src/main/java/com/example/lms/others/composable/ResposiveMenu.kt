package com.example.lms.others.composable

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResponsiveMenu(menuItems: List<MenuItem>, menuItemsShort: List<MenuItem>? = null, content: @Composable ()->Unit){

    val configuration = LocalConfiguration.current
    val isKeyboardOpen by keyboardAsState()

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
                if(isKeyboardOpen == Keyboard.Closed || menuItemsShort == null){
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